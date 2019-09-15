/*
 * CopyClassVisitor.java -
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

package com.roscopeco.moxy.impl.asm.visitors.classmock;

import com.roscopeco.moxy.api.MoxyMock;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.Remapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

/**
 * Class adapter that copies the original class to a new
 * class mock delegate class.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class MoxyClassMockDelegateAdapter extends ClassVisitor {
    private final class CopyRemapper extends Remapper {
        private final String originalInternalName;
        private final String newInternalName;

        private CopyRemapper(final String originalInternalName, final String newInternalName) {
            this.originalInternalName = originalInternalName;
            this.newInternalName = newInternalName;
        }

        @Override
        public String map(final String internalName) {
            if (this.originalInternalName.equals(internalName)) {
                return this.newInternalName;
            } else {
                return internalName;
            }
        }
    }

    private static final AtomicInteger aint = new AtomicInteger();

    private final String originalInternalName;
    private final String newInternalName;

    /**
     * Create a new CopyClassVisitor that will copy a source class
     * to a class-mock backing class with the given internal name.
     *
     * @param delegate The ClassVisitor to delegate to.
     * @param source   The source class.
     */
    public MoxyClassMockDelegateAdapter(final ClassVisitor delegate, final Class<?> source) {
        super(ASM7);

        this.originalInternalName = Type.getInternalName(source);
        this.newInternalName = this.generateNewInternalName(this.originalInternalName);

        final CopyRemapper copyRemapper = new CopyRemapper(this.originalInternalName,
                this.newInternalName);

        this.cv = new ClassRemapper(delegate, copyRemapper);
    }

    private String generateNewInternalName(final String originalInternalName) {
        final String originalPackage;

        if (originalInternalName.contains("/")) {
            originalPackage = originalInternalName.substring(0, originalInternalName.lastIndexOf('/') + 1);
        } else {
            originalPackage = "";
        }

        return originalPackage + "Moxy ClassMock Delegate {" + aint.getAndIncrement() + "}";
    }

    /**
     * @return The original type's internal name.
     */
    public String getOriginalInternalName() {
        return this.originalInternalName;
    }

    /**
     * @return The new type's internal name.
     */
    public String getNewInternalName() {
        return this.newInternalName;
    }

    /**
     * @return The new type's Java name.
     */
    public String getNewJavaName() {
        return this.newInternalName.replace('/', '.');
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        final ArrayList<String> newInterfaces = new ArrayList<>(interfaces.length + 1);
        newInterfaces.add(MOXY_SUPPORT_INTERFACE_INTERNAL_NAME);
        newInterfaces.addAll(Arrays.asList(interfaces));

        super.visit(version,
                access | ACC_SYNTHETIC,
                this.newInternalName,
                signature,
                superName,
                newInterfaces.toArray(new String[0]));

        this.visitAnnotation(Type.getDescriptor(MoxyMock.class), true).visitEnd();
    }

    private void generateSupportFields() {
        // Generating public at the mo to allow setting from class mock ctor.
        // TODO Make private final and set through delegate ctor.
        final FieldVisitor fv = this.cv.visitField(
                ACC_PUBLIC | ACC_SYNTHETIC,
                SUPPORT_IVARS_FIELD_NAME,
                MOXY_SUPPORT_IVARS_DESCRIPTOR,
                null,
                null);

        fv.visitEnd();
    }

    private void generateSupportMethods() {
        this.generateSupportGetter(SUPPORT_GET_IVARS_METHOD_NAME,
                SUPPORT_GET_IVARS_DESCRIPTOR,
                SUPPORT_IVARS_FIELD_NAME,
                MOXY_SUPPORT_IVARS_DESCRIPTOR);
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
        mv.visitFieldInsn(GETFIELD, this.newInternalName, fieldName, fieldDescriptor);
        mv.visitInsn(ARETURN);
        mv.visitEnd();
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature, final String[] exceptions) {
        return super.visitMethod(access | ACC_SYNTHETIC, name, descriptor, signature, exceptions);
    }

    @Override
    public void visitEnd() {
        this.generateSupportFields();
        this.generateSupportMethods();
    }
}
