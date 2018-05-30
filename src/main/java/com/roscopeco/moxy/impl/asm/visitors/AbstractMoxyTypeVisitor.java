package com.roscopeco.moxy.impl.asm.visitors;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

import java.util.concurrent.atomic.AtomicInteger;

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
  protected static final AtomicInteger mockNumber = new AtomicInteger();
  
  /*
   * Ensures we don't try to use any of the prohibited packages
   * (e.g. java.lang).
   */
  private static String makeMockPackageInternalName(Package originalPackage) {
    final String originalName = originalPackage.getName();
    
    if ("java.lang".equals(originalName)) {
      // default package
      return "";      
    } else {
      return originalName.replace('.', '/');
    }    
  }
  
  protected static String makeMockName(Class<?> originalClass) {
    return makeMockPackageInternalName(originalClass.getPackage()) + "/Mock " 
           + originalClass.getSimpleName() 
           + " {"
           + AbstractMoxyTypeVisitor.mockNumber.getAndIncrement()
           + "}";
  }

  private final ClassNode node = new ClassNode();
  private final String newClassInternalName;
  
  protected AbstractMoxyTypeVisitor(final String newClassInternalName) {
    super(ASM6);
    this.cv = this.node;
    this.newClassInternalName = newClassInternalName;
  }
    
  protected String getNewClassInternalName() {
    return newClassInternalName;
  }
  
  @Override
  public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
    // Don't visit, we don't need to copy fields...
    return null;
  }

  @Override
  public void visitEnd() {
    this.generateSupportFields();
    this.generateSupportMethods();
    super.visitEnd();
  }
  
  void generateSupportFields() {
    FieldVisitor fv = this.cv.visitField(ACC_PRIVATE | ACC_FINAL | ACC_SYNTHETIC, SUPPORT_ENGINE_FIELD_NAME, MOXY_ASM_ENGINE_DESCRIPTOR, null, null);
    fv.visitEnd();

    fv = this.cv.visitField(ACC_PRIVATE | ACC_FINAL | ACC_SYNTHETIC, SUPPORT_RETURNMAP_FIELD_NAME, HASHMAP_DESCRIPTOR, null, null);
    fv.visitEnd();

    fv = this.cv.visitField(ACC_PRIVATE | ACC_FINAL | ACC_SYNTHETIC, SUPPORT_THROWMAP_FIELD_NAME, HASHMAP_DESCRIPTOR, null, null);
    fv.visitEnd();
  }
  
  void generateSupportMethods() {
    this.generateSupportGetter(SUPPORT_GETENGINE_METHOD_NAME,
                               SUPPORT_GETENGINE_DESCRIPTOR,
                               SUPPORT_ENGINE_FIELD_NAME,
                               MOXY_ASM_ENGINE_DESCRIPTOR);
    
    this.generateSupportGetter(SUPPORT_GETRETURNMAP_METHOD_NAME,
                               SUPPORT_GETRETURNMAP_DESCRIPTOR,
                               SUPPORT_RETURNMAP_FIELD_NAME,
                               HASHMAP_DESCRIPTOR);
    
    this.generateSupportGetter(SUPPORT_GETTHROWMAP_METHOD_NAME,
                               SUPPORT_GETTHROWMAP_DESCRIPTOR,
                               SUPPORT_THROWMAP_FIELD_NAME,
                               HASHMAP_DESCRIPTOR);
  }
  
  private void generateSupportGetter(String methodName,
                             String methodDescriptor,
                             String fieldName,
                             String fieldDescriptor) {
    MethodVisitor mv = this.cv.visitMethod(ACC_PUBLIC | ACC_SYNTHETIC, 
                                           methodName,
                                           methodDescriptor,
                                           null, 
                                           EMPTY_STRING_ARRAY);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(GETFIELD, newClassInternalName, fieldName, fieldDescriptor);
    mv.visitInsn(ARETURN);
    mv.visitEnd();    
  }
  
  public ClassNode getNode() {
    return this.node;
  }  
}
