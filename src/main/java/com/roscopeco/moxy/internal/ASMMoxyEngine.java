package com.roscopeco.moxy.internal;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.TraceClassVisitor;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.api.Mock;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyInvocationRecorder;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.internal.visitors.AbstractMoxyTypeVisitor;
import com.roscopeco.moxy.internal.visitors.MoxyMockClassVisitor;
import com.roscopeco.moxy.internal.visitors.MoxyMockInterfaceVisitor;

/**
 * Default MoxyEngine implementation.
 * 
 * Based on bytecode generation with ASM.
 * 
 * @author Ross.Bamford
 *
 */
public class ASMMoxyEngine implements MoxyEngine {
  private static final Set<Method> EMPTY_METHODS = Collections.emptySet();
  
  private final MoxyInvocationRecorder recorder; 
  
  private static final Method defineClass;       
  static {
    try {
      defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
    } catch (NoSuchMethodException e) {
      throw new IllegalStateException("Unrecoverable Error: NoSuchMethodException 'defineClass' on ClassLoader.\n"
          + "This is most likely an environment issue.", e);
    }
    defineClass.setAccessible(true);
  }

  public ASMMoxyEngine() {
    this.recorder = new ThreadLocalInvocationRecorder();
  }
  
  public ASMMoxyEngine(MoxyInvocationRecorder recorder) {
    this.recorder = recorder;    
  }
  
  public MoxyInvocationRecorder getRecorder() {
    return recorder;
  }
  
  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#mock(java.lang.Class)
   */
  @Override
  public <T> T mock(Class<T> clz) {
    return this.mock(clz,  null);
  }
  
  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#mock(java.lang.Class, java.io.PrintStream)
   */
  @Override
  public <T> T mock(Class<T> clz, PrintStream trace) {
    try {
      Class<? extends T> mockClass = getMockClass(clz, trace);
      Constructor<? extends T> ctor = mockClass.getConstructor(ASMMoxyEngine.class);
      return ctor.newInstance(this);
    } catch (Exception e) {
      throw new RuntimeException("Unrecoverable error: exception in mock constructor", e);
      
    }
  }
  
  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.ClassLoader, java.lang.Class, java.util.Set, java.io.PrintStream)
   */
  @Override
  @SuppressWarnings("unchecked")
  public <I> Class<? extends I> getMockClass(ClassLoader loader, 
                                             Class<I> clz, 
                                             Set<Method> methods, 
                                             PrintStream trace) {
    try {
      return (Class<I>)defineClass(loader, createMockClassNode(clz, methods, trace));
    } catch (IOException e) {
      throw new RuntimeException("Unrecoverable Error", e);
    }
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.ClassLoader, java.lang.Class, java.util.Set)
   */
  @Override
  public <I> Class<? extends I> getMockClass(ClassLoader loader, 
                                             Class<I> clz, 
                                             Set<Method> methods) {
    return getMockClass(loader, clz, methods, null);    
  }
  
  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.Class, java.util.Set)
   */
  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz, 
                                             Set<Method> extraMethods) {
    return getMockClass(Moxy.class.getClassLoader(), clz, extraMethods, null);    
  }
  
  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.ClassLoader, java.lang.Class)
   */
  @Override
  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz) {
    return getMockClass(loader, clz, EMPTY_METHODS, null);
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.Class, java.io.PrintStream)
   */
  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz, PrintStream trace) {
    return getMockClass(Moxy.class.getClassLoader(), clz, EMPTY_METHODS, trace);
  }
  
  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.Class)
   */
  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz) {
    return getMockClass(Moxy.class.getClassLoader(), clz);
  }
  
  HashSet<Method> gatherPublicMethods(Class<?> originalClass) {
    HashSet<Method> methods = new HashSet<Method>();

    Class<?> currentClass = originalClass;
    while (originalClass != null && !originalClass.equals(Object.class)) {
      for (Method m : currentClass.getMethods()) {
        methods.add(m);
      }
      originalClass = originalClass.getSuperclass();
    }
    
    return methods;
  }
  
  public boolean isMock(Class<?> clz) {
    return clz.getAnnotation(Mock.class) != null;
  }
  
  public boolean isMock(Object obj) {
    return isMock(obj.getClass());
  }
  
  public <T> MoxyStubber<T> when(T invocation) {
    deleteLatestInvocationFromList();
    return new ASMMoxyStubber<T>(this);
  }
  
  public <T> MoxyStubber<T> when(Supplier<T> invocation) {
    try {
      invocation.get();
    } catch (Exception e) {
      // TODO naively swallow for now, revisit later when we have a base exception for the framework.
    }
    deleteLatestInvocationFromList();
    return new ASMMoxyStubber<T>(this);
    
  }
  
  public MoxyVerifier assertMock(Object invocation) {
    deleteLatestInvocationFromList();
    return new ASMMoxyVerifier(this);
  }
  
  @Override
  public MoxyVerifier assertMock(Runnable invocation) {
    invocation.run();
    deleteLatestInvocationFromList();
    return new ASMMoxyVerifier(this);
  }

  void deleteLatestInvocationFromList() {
    this.getRecorder().unrecordLastInvocation();
  }
  
  ClassNode createMockClassNode(Class<?> clz, Set<Method> methods, PrintStream trace) throws IOException {
    String clzInternalName = Type.getInternalName(clz);
    ClassReader reader = new ClassReader(clzInternalName);
    
    // Find the annotated methods (and their interfaces)
    
    Set<Method> mockableMethods;
    if (methods == null || methods.isEmpty()) {
      mockableMethods = gatherPublicMethods(clz);
    } else {
      mockableMethods = methods;
    }
       
    AbstractMoxyTypeVisitor visitor = 
        clz.isInterface() ? 
            new MoxyMockInterfaceVisitor(clz)               :
            new MoxyMockClassVisitor(clz, mockableMethods)  ;
            
    reader.accept(visitor, ClassReader.SKIP_DEBUG);

    if (trace != null) {
      visitor.getNode().accept(new TraceClassVisitor(new PrintWriter(trace)));
    }
    
    return visitor.getNode();    
  }
  
  byte[] generateBytecode(ClassNode node) {
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    node.accept(writer);
    return writer.toByteArray();    
  }
  
  Class<?> defineClass(ClassLoader loader, ClassNode node) {
    byte[] code = generateBytecode(node);
    try {
      return (Class<?>)defineClass.invoke(loader, node.name.replace('/',  '.'), code, 0, code.length);      
    } catch (InvocationTargetException e) {
      System.err.println("InvocationTargetException: in defineClass: " + e.getMessage());      
      throw new RuntimeException("Unrecoverable Error", e);
    } catch (IllegalAccessException e) {
      System.err.println("IllegalAccessException: in defineClass: " + e.getMessage());      
      throw new RuntimeException("Unrecoverable Error", e);
    }
  }
}
