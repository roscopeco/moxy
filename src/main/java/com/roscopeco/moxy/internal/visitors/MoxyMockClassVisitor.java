package com.roscopeco.moxy.internal.visitors;

import static com.roscopeco.moxy.internal.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.roscopeco.moxy.internal.IsMock;

/**
 * Creates mocks from classes.
 * 
 * @author Ross.Bamford
 */
public class MoxyMockClassVisitor extends AbstractMoxyTypeVisitor {  
  private final String originalClassInternalName;
  private final String originalSuperClassInternalName;
  private final Set<Method> mockMethods;

  public MoxyMockClassVisitor(Class<?> originalClass, Set<Method> methods) {
    super(originalClass.getPackage().getName().replace('.', '/') + "/MoxyMock-"
        + UUID.randomUUID());
    this.originalClassInternalName = Type.getInternalName(originalClass);
    this.originalSuperClassInternalName = Type.getInternalName(originalClass.getSuperclass());

    this.mockMethods = methods == null ? Collections.emptySet() : methods;
  }

  @Override
  public void visit(int version, 
                    int access, 
                    String name, 
                    String signature, 
                    String superName,
                    String[] originalInterfaces) {
    ArrayList<String> newInterfaces = new ArrayList<>(originalInterfaces.length + 1);
    newInterfaces.add(MOXY_SUPPORT_INTERFACE_INTERNAL_NAME);
    newInterfaces.addAll(Arrays.asList(originalInterfaces));

    super.visit(version, 
                access & ~ACC_ABSTRACT, 
                super.getNewClassInternalName(),
                signature, 
                originalClassInternalName, 
                newInterfaces.toArray(new String[newInterfaces.size()]));
    
    this.visitAnnotation(Type.getDescriptor(IsMock.class), true).visitEnd();
  }
  
  private boolean isToMock(String name, String desc) {
    return mockMethods.stream()
      .anyMatch((m) -> m.getName().equals(name) && Type.getMethodDescriptor(m).equals(desc));
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
    // Generate field for method return value
    boolean isAbstract = (access & ACC_ABSTRACT) != 0;
    
    // Never generate constructors (do that manually later).
    if (!INIT_NAME.equals(name)) {
      // Always mock abstract methods (or it won't verify), decide for concrete based on mockMethods.
      if (isAbstract || isToMock(name, desc)) {
        // Create a return field for this mocked method
        super.generateMethodReturnField(name, desc);
 
        // Do the mocking
        return new MoxyMockingMethodVisitor(cv.visitMethod(access & ~ACC_ABSTRACT, 
                                                           name, desc, signature, exceptions),
                                                           this.getNewClassInternalName(),
                                                           name + desc,
                                                           getReturnType(desc),
                                                           Type.getArgumentTypes(desc),
                                                           isAbstract);
      } else {
        return new MoxyReferenceFixingMethodVisitor(cv.visitMethod(access, name, desc, signature, exceptions),
                                                    originalClassInternalName, 
                                                    originalSuperClassInternalName,
                                                    super.getNewClassInternalName());
      }
    } else {
      return null;
    }
  }

  @Override
  public void visitEnd() {
    super.generateConstructors(originalClassInternalName, VOID_VOID_DESCRIPTOR);
    super.visitEnd();
  }
  
  
}
