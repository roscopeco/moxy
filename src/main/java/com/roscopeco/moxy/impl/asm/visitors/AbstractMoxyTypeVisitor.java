/*
 * Moxy - Lean-and-mean mocking framework for Java with a fluent API.
 *
 * Copyright 2018 Ross Bamford
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included
 *   in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.roscopeco.moxy.impl.asm.visitors;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

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
  private static final List<Pattern> PROHIBITED_PACKAGES = Arrays.asList(
      Pattern.compile("java\\..*"),
      Pattern.compile("javax\\..*"),
      Pattern.compile("javafx\\..*"),
      Pattern.compile("sun\\..*"),
      Pattern.compile("com\\.sun\\..*"),
      Pattern.compile("oracle\\..*")
    );

  /*
   * Ensures we don't try to use any of the prohibited packages
   * (e.g. java.lang).
   */
  private static String makeMockPackageInternalName(final Package originalPackage) {
    final String originalName = originalPackage.getName();

    if (PROHIBITED_PACKAGES.stream().anyMatch(regex -> regex.matcher(originalName).find())) {
      // default package
      return "";
    } else {
      return originalName.replace('.', '/') + "/";
    }
  }

  protected static String makeMockName(final Class<?> originalClass) {
    return makeMockPackageInternalName(originalClass.getPackage()) + "Mock "
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
    return this.newClassInternalName;
  }

  @Override
  public FieldVisitor visitField(final int access, final String name, final String descriptor, final String signature, final Object value) {
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
    final FieldVisitor fv = this.cv.visitField(
        ACC_PRIVATE | ACC_FINAL | ACC_SYNTHETIC,
        SUPPORT_ivars_FIELD_NAME,
        MOXY_SUPPORT_ivars_DESCRIPTOR,
        null,
        null);

    fv.visitEnd();
  }

  void generateSupportMethods() {
    this.generateSupportGetter(SUPPORT_GET_ivars_METHOD_NAME,
                               SUPPORT_GET_ivars_DESCRIPTOR,
                               SUPPORT_ivars_FIELD_NAME,
                               MOXY_SUPPORT_ivars_DESCRIPTOR);
  }

  private void generateSupportGetter(final String methodName,
                             final String methodDescriptor,
                             final String fieldName,
                             final String fieldDescriptor) {
    final MethodVisitor mv = this.cv.visitMethod(ACC_PUBLIC | ACC_SYNTHETIC,
                                           methodName,
                                           methodDescriptor,
                                           null,
                                           EMPTY_STRING_ARRAY);
    mv.visitCode();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(GETFIELD, this.newClassInternalName, fieldName, fieldDescriptor);
    mv.visitInsn(ARETURN);
    mv.visitEnd();
  }

  public ClassNode getNode() {
    return this.node;
  }
}
