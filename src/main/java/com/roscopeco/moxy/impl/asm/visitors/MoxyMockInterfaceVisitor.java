package com.roscopeco.moxy.impl.asm.visitors;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.roscopeco.moxy.api.Mock;

/**
 * Creates mocks from interfaces.
 * 
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class MoxyMockInterfaceVisitor extends AbstractMoxyTypeVisitor {  
  private final String interfaceInternalName;

  public MoxyMockInterfaceVisitor(Class<?> iface) {
    super(AbstractMoxyTypeVisitor.makeMockName(iface));
    
    this.interfaceInternalName = Type.getInternalName(iface);
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName,
      String[] originalInterfaces) {
    ArrayList<String> newInterfaces = new ArrayList<>(originalInterfaces.length + 2);
    newInterfaces.add(this.interfaceInternalName);
    newInterfaces.add(MOXY_SUPPORT_INTERFACE_INTERNAL_NAME);
    newInterfaces.addAll(Arrays.asList(originalInterfaces));
    
    // Start the class visit
    super.visit(version, 
                access & ~ACC_ABSTRACT & ~ACC_INTERFACE | ACC_SUPER, 
                getNewClassInternalName(), 
                signature, 
                OBJECT_INTERNAL_NAME, 
                newInterfaces.toArray(new String[newInterfaces.size()]));
    
    // Add the IsMock annotation
    this.visitAnnotation(Type.getDescriptor(Mock.class), true).visitEnd();
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
    // Do the mocking
    return new MoxyMockingMethodVisitor(this.cv.visitMethod(
        access & ~ACC_ABSTRACT | ACC_SYNTHETIC, 
        name, desc, signature, exceptions),
    this.interfaceInternalName,
    name,
    desc,
    Type.getReturnType(desc),
    Type.getArgumentTypes(desc),
    true);
  }
  
  @Override
  public void visitEnd() {
    // Manually generate a constructor in case user wants to manually instantiate,
    // like they do for partial mocks...
    MoxyPassThroughConstructorVisitor mv = 
        new MoxyPassThroughConstructorVisitor(cv.visitMethod(ACC_PUBLIC | ACC_SYNTHETIC,
                            INIT_NAME,
                            MOCK_CONSTRUCTOR_DESCRIPTOR,
                            null,
                            null),
            OBJECT_INTERNAL_NAME,
            this.getNewClassInternalName(),
            VOID_VOID_DESCRIPTOR,
            EMPTY_TYPE_ARRAY);
    
    mv.visitCode();
    mv.visitEnd();
    
    super.visitEnd();
  }
}
