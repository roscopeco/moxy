package com.roscopeco.moxy.internal.visitors;

import static com.roscopeco.moxy.internal.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

/**
 * Superclass for both class and interface visitors.
 * 
 * Generates a ClassNode.
 */
public abstract class AbstractMoxyTypeVisitor extends ClassVisitor {   
  private final ClassNode node = new ClassNode();
  private final String newClassInternalName;
  
  protected AbstractMoxyTypeVisitor(final String newClassInternalName) {
    super(ASM5);
    this.cv = this.node;
    this.newClassInternalName = newClassInternalName;
  }
  
  // Get return type given method descriptor.
  protected char getReturnType(String desc) {
    return desc.charAt(desc.indexOf(')') + 1);
  }
  
  protected String getNewClassInternalName() {
    return newClassInternalName;
  }
  
  @Override
  public void visitEnd() {
    this.generateSupportFields();
    this.generateSupportMethods();
    super.visitEnd();
  }
  
  /**
   * Called from subclass to generate approriate constructor.
   * 
   * Call during visitEnd, before calling super.
   */
  protected void generateConstructors(String superClassInternalName, String superDescriptor) {
    generateActualConstructor(superClassInternalName, superDescriptor);
    generateNullConstructor(superClassInternalName, superDescriptor);    
  }
  
  void generateActualConstructor(String superClassInternalName, String superDescriptor) {
    MethodVisitor mv = this.cv.visitMethod(ACC_PUBLIC, INIT_NAME, MOCK_CONSTRUCTOR_DESCRIPTOR, null, EMPTY_STRING_ARRAY);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, superClassInternalName, INIT_NAME, superDescriptor, false);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitFieldInsn(PUTFIELD, newClassInternalName, SUPPORT_ENGINE_FIELD_NAME, MOXY_ASM_ENGINE_DESCRIPTOR);
    mv.visitInsn(RETURN);
    mv.visitEnd();
  }
  
  void generateNullConstructor(String superClassInternalName, String superDescriptor) {
    MethodVisitor mv = this.cv.visitMethod(ACC_PUBLIC, INIT_NAME, VOID_VOID_DESCRIPTOR, null, EMPTY_STRING_ARRAY);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, superClassInternalName, INIT_NAME, superDescriptor, false);
    mv.visitMethodInsn(INVOKEVIRTUAL,
                       newClassInternalName, 
                       SUPPORT_NULL_CONSTRUCTOR_THROWER_METHOD_NAME,
                       SUPPORT_NULL_CONSTRUCTOR_THROWER_METHOD_DESCRIPTOR, 
                       false);
    mv.visitInsn(RETURN);
    mv.visitEnd();
  }
  
  void generateSupportFields() {
    FieldVisitor fv = this.cv.visitField(ACC_PRIVATE | ACC_FINAL, SUPPORT_ENGINE_FIELD_NAME, MOXY_ASM_ENGINE_DESCRIPTOR, null, null);
    fv.visitEnd();
  }
  
  void generateSupportMethods() {
    generateMoxyAsmGetEngineMethod();
  }
  
  void generateMoxyAsmGetEngineMethod() {
    MethodVisitor mv = this.cv.visitMethod(ACC_PUBLIC, 
                                           SUPPORT_GETENGINE_METHOD_NAME,
                                           SUPPORT_GETENGINE_DESCRIPTOR,
                                           null, 
                                           EMPTY_STRING_ARRAY);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(GETFIELD, newClassInternalName, SUPPORT_ENGINE_FIELD_NAME, MOXY_ASM_ENGINE_DESCRIPTOR);
    mv.visitInsn(ARETURN);
    mv.visitEnd();    
  }
  
  public ClassNode getNode() {
    return this.node;
  }  
}
