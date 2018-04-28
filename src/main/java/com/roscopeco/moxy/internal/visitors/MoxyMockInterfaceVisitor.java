package com.roscopeco.moxy.internal.visitors;

import static com.roscopeco.moxy.internal.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.roscopeco.moxy.internal.IsMock;

/**
 * Creates mocks from interfaces.
 * 
 * @author Ross.Bamford
 */
public class MoxyMockInterfaceVisitor extends AbstractMoxyTypeVisitor {  
  private final String interfaceInternalName;

  public MoxyMockInterfaceVisitor(Class<?> iface) {
    super(iface.getPackage().getName().replace('.', '/') + "/MoxyMock-"
        + UUID.randomUUID());
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
                access & ~ACC_ABSTRACT & ~ACC_INTERFACE, 
                getNewClassInternalName(), 
                signature, 
                OBJECT_INTERNAL_NAME, 
                newInterfaces.toArray(new String[newInterfaces.size()]));
    
    // Add the IsMock annotation
    this.visitAnnotation(Type.getDescriptor(IsMock.class), true).visitEnd();
  }
  
  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {    
    // Generate field for method return value
    if (!VOID_TYPE.equals(Type.getReturnType(desc).toString())) {
      FieldVisitor fv = this.cv.visitField(ACC_PRIVATE | ACC_SYNTHETIC, 
                                           super.makeMethodReturnFieldName(name, desc),                                          
                                           Type.getReturnType(desc).toString(), 
                                           null, null);
      fv.visitEnd();
    }
    
    // Do the mocking
    return new MoxyMockingMethodVisitor(this.cv.visitMethod(
        access & ~ACC_ABSTRACT, 
        name, desc, signature, exceptions),
    this.getNewClassInternalName(),
    name + desc,
    getReturnType(desc),
    Type.getArgumentTypes(desc),
    true);
  }
  
  @Override
  public void visitEnd() {
    super.generateConstructors(OBJECT_INTERNAL_NAME, VOID_VOID_DESCRIPTOR);
    super.visitEnd();    
  }
}
