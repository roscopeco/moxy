package com.roscopeco.moxy.impl.asm;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.TraceClassVisitor;

import com.roscopeco.moxy.api.Mock;
import com.roscopeco.moxy.api.MockGenerationException;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.api.MoxyMatcherEngine;
import com.roscopeco.moxy.api.MoxyStubber;
import com.roscopeco.moxy.api.MoxyVerifier;
import com.roscopeco.moxy.api.MoxyVoidStubber;
import com.roscopeco.moxy.impl.asm.visitors.AbstractMoxyTypeVisitor;
import com.roscopeco.moxy.impl.asm.visitors.MoxyMockClassVisitor;
import com.roscopeco.moxy.impl.asm.visitors.MoxyMockInterfaceVisitor;
import com.roscopeco.moxy.matchers.PossibleMatcherUsageError;

/**
 * Default MoxyEngine implementation.
 * 
 * Based on bytecode generation with ASM.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 *
 */
public class ASMMoxyEngine implements MoxyEngine {
  private static final String UNRECOVERABLE_ERROR = "Unrecoverable Error";
  private static final String CANNOT_MOCK_NULL_CLASS = "Cannot mock null class";
  
  private final ThreadLocalInvocationRecorder recorder;
  private final ASMMoxyMatcherEngine matcherEngine;
  
  @SuppressWarnings("restriction")
  private static final sun.misc.Unsafe UNSAFE = getUnsafe();
  
  @SuppressWarnings("restriction")
  private static sun.misc.Unsafe getUnsafe() {    
    try {
      Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
      unsafeField.setAccessible(true);
      return (sun.misc.Unsafe)unsafeField.get(null);
    } catch (NoSuchFieldException e) {
      throw new IllegalStateException("Unrecoverable Error: NoSuchFieldException 'theUnsafe' on sun.misc.Unsafe.\n"
          + "This is most likely an environment issue.", e);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("Unrecoverable Error: IllegalAccessException when accessing 'theUnsafe' on sun.misc.Unsafe.\n"
          + "This is most likely an environment issue.", e);
    }
  }

  public ASMMoxyEngine() {
    this.recorder = new ThreadLocalInvocationRecorder(this);
    this.matcherEngine = new ASMMoxyMatcherEngine(this);
  }
  
  ASMMoxyEngine(ThreadLocalInvocationRecorder recorder, ASMMoxyMatcherEngine matcherEngine) {
    this.recorder = recorder;
    this.matcherEngine = matcherEngine;
  }
  
  public ThreadLocalInvocationRecorder getRecorder() {
    return this.recorder;
  }
  
  @Override
  public MoxyMatcherEngine getMatcherEngine() {
    return this.matcherEngine;
  }
  
  ASMMoxyMatcherEngine getASMMatcherEngine() {
    return this.matcherEngine;
  }
  
  @Override
  public void reset() {
    getRecorder().reset();
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#mock(java.lang.Class)
   */
  @Override
  public <T> T mock(Class<T> clz) {
    return this.mock(clz, null);
  }
  
  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#mock(java.lang.Class, java.io.PrintStream)
   */
  @Override
  public <T> T mock(Class<T> clz, PrintStream trace) {
    if (clz == null) {
      throw new IllegalArgumentException(CANNOT_MOCK_NULL_CLASS);
    }

    try {
      Class<? extends T> mockClass = getMockClass(clz, MoxyEngine.ALL_METHODS, trace);
      return instantiateMock(mockClass);
    } catch (MoxyException e) {
      throw e;
    } catch (Exception e) {
      throw new MockGenerationException("Unrecoverable error: exception during mock generation", e);      
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
      throw new MoxyException(UNRECOVERABLE_ERROR, e);
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
                                             Set<Method> methods) {
    return getMockClass(MoxyEngine.class.getClassLoader(), clz, methods, null);    
  }
  
  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.ClassLoader, java.lang.Class)
   */
  @Override
  public <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz) {
    return getMockClass(loader, clz, MoxyEngine.ALL_METHODS, null);
  }

  /*
   * (non-Javadoc)
   * @see com.roscopeco.moxy.api.MoxyEngine#getMockClass(java.lang.Class, java.util.Set, java.io.PrintStream)
   */
  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz, Set<Method> methods, PrintStream trace) {
    return getMockClass(MoxyEngine.class.getClassLoader(), clz, methods, trace);
  }

  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.Class, java.io.PrintStream)
   */
  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz, PrintStream trace) {
    return getMockClass(MoxyEngine.class.getClassLoader(), clz, MoxyEngine.ALL_METHODS, trace);
  }
  
  /* (non-Javadoc)
   * @see com.roscopeco.moxy.internal.MoxyEngine#getMockClass(java.lang.Class)
   */
  @Override
  public <I> Class<? extends I> getMockClass(Class<I> clz) {
    return getMockClass(MoxyEngine.class.getClassLoader(), clz);
  }
  
  @Override
  public boolean isMock(Class<?> clz) {
    return clz.getAnnotation(Mock.class) != null;
  }
  
  @Override
  public boolean isMock(Object obj) {
    return isMock(obj.getClass());
  }
  
  boolean isMockCandidate(final Method m) {
    return ((m.getModifiers() & Opcodes.ACC_FINAL) == 0) 
        && (((m.getModifiers() & Opcodes.ACC_PUBLIC) > 0)
            || ((m.getModifiers() & Opcodes.ACC_PROTECTED) > 0)
            || ((m.getModifiers() & Opcodes.ACC_PRIVATE) == 0));
  }
  
  HashSet<Method> gatherAllMockableMethods(final Class<?> originalClass) {
    HashSet<Method> methods = new HashSet<>();

    for (Method m : originalClass.getDeclaredMethods()) {
      if (isMockCandidate(m)) {
        methods.add(m);
      }
    }
    
    return methods;
  }
  
  void ensureEngineConsistencyBeforeMonitoredInvocation() {
    this.getRecorder().clearLastInvocation();
    this.getASMMatcherEngine().validateStackConsistency();
  }

  /*
   * Validates matcher stack consistency.
   */
  void naivelyInvokeAndSwallowExceptions(Runnable doInvoke) {
    ensureEngineConsistencyBeforeMonitoredInvocation();
    try {
      doInvoke.run();
    } catch (MoxyException e) {
      // Framework error - rethrow this.
      throw e;
    } catch (NullPointerException e) {
      // Often an autoboxing error, give (hopefully) useful error message.
      throw new PossibleMatcherUsageError(
          "NPE in invocation: If you're using primitive matchers, ensure you're using the " 
        + "correct type (e.g. anyInt() rather than any()), especially when nesting.\n"
        + "Otherwise, the causing exception may have more information.", e);
    } catch (Exception e) {      
      // TODO naively swallow everything else.
    }
  }
  
  /*
   * Deletes latest invocation, and validates matcher stack consistency.
   */
  void deleteLatestInvocationFromList() {
    this.getRecorder().unrecordLastInvocation();
    this.getASMMatcherEngine().validateStackConsistency();
  }
  
  @Override
  public <T> MoxyStubber<T> when(Supplier<T> invocation) {
    naivelyInvokeAndSwallowExceptions(invocation::get);
    this.getRecorder().replaceInvocationArgsWithMatchers();
    deleteLatestInvocationFromList();
    return new ASMMoxyStubber<>(this);
  }
  
  @Override
  public MoxyVoidStubber when(Runnable invocation) {
    naivelyInvokeAndSwallowExceptions(invocation::run);
    this.getRecorder().replaceInvocationArgsWithMatchers();
    deleteLatestInvocationFromList();
    return new ASMMoxyVoidStubber(this);
  }

  @Override
  public MoxyVerifier assertMock(Runnable invocation) {
    naivelyInvokeAndSwallowExceptions(invocation::run);
    this.getRecorder().replaceInvocationArgsWithMatchers();
    deleteLatestInvocationFromList();
    return new ASMMoxyVerifier(this);
  }

  /* 
   * Instantiate a mock of the given class 
   */
  @SuppressWarnings({ "unchecked", "restriction", "rawtypes" })
  <T> T instantiateMock(Class<? extends T> mockClass) {
    try {
      Field engineField = mockClass.getDeclaredField(TypesAndDescriptors.SUPPORT_ENGINE_FIELD_NAME);
      Field returnMapField = mockClass.getDeclaredField(TypesAndDescriptors.SUPPORT_RETURNMAP_FIELD_NAME);
      Field throwMapField = mockClass.getDeclaredField(TypesAndDescriptors.SUPPORT_THROWMAP_FIELD_NAME);
      Object mock = UNSAFE.allocateInstance(mockClass);
      UNSAFE.putObject(mock, UNSAFE.objectFieldOffset(engineField), this);
      UNSAFE.putObject(mock, UNSAFE.objectFieldOffset(returnMapField), new HashMap());
      UNSAFE.putObject(mock, UNSAFE.objectFieldOffset(throwMapField), new HashMap());
      return (T)mock;
    } catch (Exception e) {
      throw new MoxyException("Unrecoverable error: Instantiation exception; see cause", e);
    }
  }
  
  /*
   * Actual generator; create the ASM ClassNode for a mock.
   */
  ClassNode createMockClassNode(Class<?> clz, Set<Method> methods, PrintStream trace) throws IOException {
    if (clz == null) {
      throw new IllegalArgumentException(CANNOT_MOCK_NULL_CLASS);
    }
    
    if ((clz.getModifiers() & Opcodes.ACC_FINAL) > 0) {
      throw new MockGenerationException("Mocking of final classes is not yet supported");
    }
    
    String clzInternalName = Type.getInternalName(clz);
    ClassReader reader = new ClassReader(clzInternalName);
    
    // Find the annotated methods (and their interfaces)
    
    Set<Method> mockableMethods;
    if (methods == MoxyEngine.ALL_METHODS) {
      mockableMethods = gatherAllMockableMethods(clz);
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
  
  /*
   * Define a class given a loader and an ASM ClassNode. 
   */
  @SuppressWarnings("restriction")
  Class<?> defineClass(ClassLoader loader, ClassNode node) {
    if (loader == null) {
      throw new IllegalArgumentException("Implicit definition in the system classloader is unsupported.\n"
                                       + "Defining mocks here will almost certainly fail with NoClassDefFoundError for framework classes.\n"
                                       + "If you're sure this is what you want to do, pass system loader explicitly (rather than null)");      
    }
    
    byte[] code = generateBytecode(node);
    return UNSAFE.defineClass(node.name.replace('/',  '.'), code, 0, code.length, loader, null);      
  }
  
  /*
   * Transform a ClassNode into bytecode.
   */
  byte[] generateBytecode(ClassNode node) {
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    node.accept(writer);
    return writer.toByteArray();    
  }  
}
