package com.roscopeco.moxy.internal.visitors;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.MethodVisitor;

public class MoxyReferenceFixingMethodVisitor extends MethodVisitor {
  private final String originalClassInternalName;
  private final String originalSuperClassInternalName;
  private final String newClassInternalName;
  
  public MoxyReferenceFixingMethodVisitor(MethodVisitor delegate, 
                                          String originalClassInternalName,
                                          String originalSuperClassInternalName,
                                          String newClassInternalName) {
    super(ASM5, delegate);
    
    this.originalClassInternalName = originalClassInternalName;
    this.originalSuperClassInternalName = originalSuperClassInternalName;
    this.newClassInternalName = newClassInternalName;
  }
  
  @Override
  public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
    if (originalClassInternalName.equals(owner)) {
      // We need to rewrite references to the original class to our new class.
      // This will probably be mostly INVOKESPECIALS (private), but also INVOKESTATICS as well.
      // In either case, refer to the method in our new class. Without this classes won't
      // verify when they call their own private methods.
      owner = newClassInternalName; 
    } else if (opcode == INVOKESPECIAL && originalSuperClassInternalName.equals(owner)) {
      // This case fixes up INVOKESPECIAL (super) calls.
      owner = originalClassInternalName;
    }
    super.visitMethodInsn(opcode, owner, name, desc, itf);
  }}
