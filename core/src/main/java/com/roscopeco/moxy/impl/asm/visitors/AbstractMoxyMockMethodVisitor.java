/*
 * AbstractMoxyMockMethodVisitor.java -
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

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;
import static org.objectweb.asm.Opcodes.*;

/**
 * Base-class for method visiting adaptors.
 * <p>
 * This class extracts the commonality used across the
 * various types of mocked methods/constructors.
 * <p>
 * Subclasses need only implement the abstract methods,
 * or override the visitXXX methods to further customise
 * (as is done by the class mock constructor visitors).
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public abstract class AbstractMoxyMockMethodVisitor extends MethodVisitor {
    private static final String UNRECOGNISED_PRIMITIVE_TYPE = "Unrecognised JVM primitive type: '";
    private static final String SUPER_NEW_JVM = "Your JVM must be super-new and improved.\n";
    private static final String TO_FIX = "To fix, add mysterious new type to switch in ";

    protected final MethodVisitor delegate;
    private final boolean wasAbstract;
    private final boolean wasNative;
    protected final Class<?> originalClass;
    final String originalClassInternalName;
    private final Type returnType;
    private final Type[] argTypes;
    protected final String methodName;
    protected final String methodDescriptor;

    @SuppressWarnings("squid:S00107" /* This internal class requires these parameters */)
    protected AbstractMoxyMockMethodVisitor(final MethodVisitor delegate,
                                            final Class<?> originalClass,
                                            final String methodName,
                                            final String methodDescriptor,
                                            final Type returnType,
                                            final Type[] argTypes,
                                            final boolean wasAbstract,
                                            final boolean wasNative) {
        // don't pass the delegate to the super constructor, or we'll generate
        // both old and new bytecode. Instead, use our own 'delegate' field.
        super(ASM7, delegate);
        this.delegate = delegate;
        this.originalClass = originalClass;
        this.originalClassInternalName = Type.getInternalName(originalClass);
        this.returnType = returnType;
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
        this.argTypes = argTypes;
        this.wasAbstract = wasAbstract;
        this.wasNative = wasNative;
    }

    @Override
    public void visitEnd() {
        // If the method was abstract or native, we'll need to manually force
        // code generation (since there's no original code attribute
        // to visit).
        if (this.wasAbstract || this.wasNative) {
            this.visitCode();
            super.visitEnd();
        }
    }

    /**
     * Generate appropriate bytecode to load this mock's delegated
     * ASMMockSupport to the top of the stack.
     */
    protected abstract void generateLoadMockSupport();

    /**
     * Generate appropriate bytecoode to call this mock's real
     * method implementation.
     * <p>
     * The generated bytecode is inserted into a try/catch block,
     * to allow exceptions to be recorded. This method is responsible
     * for generating code to load the appropriate receiver and
     * arguments to the stack, and invoking the method.
     * <p>
     * The return value (if any) will be handled automatically.
     */
    protected abstract void generateRealMethodCall();

    /**
     * Returns the local slot number for the first argument. For
     * static methods, this should be zero. For instance methods,
     * it should be 1.
     *
     * @return the local slot number.
     */
    protected abstract int getFirstArgumentLocalSlot();

    /**
     * @return The first available local slot, after (width-adjusted) arguments.
     */
    private int getFirstAvailableLocalSlot() {
        final int argsSize = Type.getArgumentsAndReturnSizes(this.methodDescriptor) >> 2;
        return argsSize + 1;
    }

    /**
     * Generate autoboxing if required (type is not L, V or [).
     * If type is non-prim, generates nothing.
     * <p>
     * Expects the primitive to be boxed at top of the stack.
     *
     * @param primType The type as a <code>char</code>.
     */
    private void generateAutobox(final char primType) {
        switch (primType) {
            case BYTE_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitMethodInsn(INVOKESTATIC,
                        BYTE_CLASS_INTERNAL_NAME,
                        VALUEOF_METHOD_NAME,
                        BYTE_VALUEOF_DESCRIPTOR,
                        false);
                break;
            case CHAR_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitMethodInsn(INVOKESTATIC,
                        CHAR_CLASS_INTERNAL_NAME,
                        VALUEOF_METHOD_NAME,
                        CHAR_VALUEOF_DESCRIPTOR,
                        false);
                break;
            case SHORT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitMethodInsn(INVOKESTATIC,
                        SHORT_CLASS_INTERNAL_NAME,
                        VALUEOF_METHOD_NAME,
                        SHORT_VALUEOF_DESCRIPTOR,
                        false);
                break;
            case INT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitMethodInsn(INVOKESTATIC,
                        INT_CLASS_INTERNAL_NAME,
                        VALUEOF_METHOD_NAME,
                        INT_VALUEOF_DESCRIPTOR,
                        false);
                break;
            case BOOL_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitMethodInsn(INVOKESTATIC,
                        BOOL_CLASS_INTERNAL_NAME,
                        VALUEOF_METHOD_NAME,
                        BOOL_VALUEOF_DESCRIPTOR,
                        false);
                break;
            case LONG_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitMethodInsn(INVOKESTATIC,
                        LONG_CLASS_INTERNAL_NAME,
                        VALUEOF_METHOD_NAME,
                        LONG_VALUEOF_DESCRIPTOR,
                        false);
                break;
            case FLOAT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitMethodInsn(INVOKESTATIC,
                        FLOAT_CLASS_INTERNAL_NAME,
                        VALUEOF_METHOD_NAME,
                        FLOAT_VALUEOF_DESCRIPTOR,
                        false);
                break;
            case DOUBLE_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitMethodInsn(INVOKESTATIC,
                        DOUBLE_CLASS_INTERNAL_NAME,
                        VALUEOF_METHOD_NAME,
                        DOUBLE_VALUEOF_DESCRIPTOR,
                        false);
                break;
            case ARRAY_PRIMITIVE_INTERNAL_NAME:
            case OBJECT_PRIMITIVE_INTERNAL_NAME:
            case VOID_PRIMITIVE_INTERNAL_NAME:
                /* do nothing */
                break;
            default:
                throw new IllegalArgumentException(UNRECOGNISED_PRIMITIVE_TYPE + primType + "'.\n"
                        + SUPER_NEW_JVM
                        + TO_FIX + "MoxyMockingMethodVisitor#generateLoadAndAutoboxing()");
        }
    }

    /**
     * Generate autoboxing if required (type is not L, V or [).
     * If type is non-prim, generates nothing.
     * <p>
     * Expects the primitive to be boxed at top of the stack.
     *
     * @param type The type as an asm <code>Type</code>.
     */
    private void generateAutobox(final Type type) {
        this.generateAutobox(type.getDescriptor().charAt(0));
    }

    /* Little bit of weirdness going on here!
     *
     * Some types (i.e. long and double) take up two local slots. This method
     * takes care of that by returning the number of slots that were taken
     * up by this particular load.
     *
     * The caller should add the result to any iterator variable or whatever
     * they're using to keep track of locals.
     */

    /**
     * Load next local from slot; Always autobox.
     * Returns number of slots taken by the loaded type.
     *
     * @param argNum           The argument number.
     * @param currentLocalSlot the local slot to load from.
     * @return The local slot for the next argument.
     */
    private int generateLoadAndAutoboxing(final int argNum, final int currentLocalSlot) {
        return this.generateLoadOptionalAutoboxing(argNum, currentLocalSlot, true);
    }

    /**
     * Load from slot; Optionally autobox
     * Returns number of slots taken by the loaded type.
     *
     * @param argNum           The argument number.
     * @param currentLocalSlot the local slot to load from.
     * @param autoBoxRequired  <code>true</code> if autoboxing is required.
     * @return The local slot for the next argument.
     */
    private int generateLoadOptionalAutoboxing(final int argNum, final int currentLocalSlot, final boolean autoBoxRequired) {
        final char argType = this.argTypes[argNum].toString().charAt(0);
        int localSlots = 1;
        switch (argType) {
            case BYTE_PRIMITIVE_INTERNAL_NAME:
            case CHAR_PRIMITIVE_INTERNAL_NAME:
            case SHORT_PRIMITIVE_INTERNAL_NAME:
            case INT_PRIMITIVE_INTERNAL_NAME:
            case BOOL_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitVarInsn(ILOAD, currentLocalSlot);
                if (autoBoxRequired) {
                    this.generateAutobox(argType);
                }
                break;
            case LONG_PRIMITIVE_INTERNAL_NAME:
                localSlots = 2;
                this.delegate.visitVarInsn(LLOAD, currentLocalSlot);
                if (autoBoxRequired) {
                    this.generateAutobox(argType);
                }
                break;
            case FLOAT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitVarInsn(FLOAD, currentLocalSlot);
                if (autoBoxRequired) {
                    this.generateAutobox(argType);
                }
                break;
            case DOUBLE_PRIMITIVE_INTERNAL_NAME:
                localSlots = 2;
                this.delegate.visitVarInsn(DLOAD, currentLocalSlot);
                if (autoBoxRequired) {
                    this.generateAutobox(argType);
                }
                break;
            case ARRAY_PRIMITIVE_INTERNAL_NAME:
            case OBJECT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitVarInsn(ALOAD, currentLocalSlot);
                break;
            default:
                throw new IllegalArgumentException(UNRECOGNISED_PRIMITIVE_TYPE + argType + "'.\n"
                        + SUPER_NEW_JVM
                        + TO_FIX + "MoxyMockingMethodVisitor#generateLoadAndAutoboxing()");
        }
        return localSlots;
    }

    /**
     * @return <code>true</code> if this method is void, <code>false</code> otherwise.
     */
    private boolean isNonVoidMethod() {
        return (this.returnType.getDescriptor().charAt(0) != 'V');
    }

    /**
     * Generate an appropriate DUPx instruction for the given type.
     *
     * @param type The type being DUPed
     */
    private void generateTypeAppropriateDup(final Type type) {
        final char primType = type.getDescriptor().charAt(0);
        if (primType == 'J' || primType == 'D') {
            this.delegate.visitInsn(DUP2);
        } else {
            this.delegate.visitInsn(DUP);
        }
    }

    /**
     * JVM has no SWAP2, so this emulates a swap if necessary
     * where the second from top value is a cat2.
     * <p>
     * NOTE the top MUST be a cat1 or this will go awry...
     *
     * @param secondFromTopType The type at second from top type on the stack.
     */
    private void generateTypeAppropriateSwap(final Type secondFromTopType) {
        final char primType = secondFromTopType.getDescriptor().charAt(0);
        if (primType == 'J' || primType == 'D') {
            this.delegate.visitInsn(DUP_X2);
            this.delegate.visitInsn(POP);
        } else {
            this.delegate.visitInsn(SWAP);
        }
    }

    /**
     * Get the local slot that will be used as temporary storage
     * for the return value.
     *
     * @return The local slot with the return value.
     */
    private int getMethodReturnLocalSlot() {
        return this.getFirstAvailableLocalSlot();
    }

    /**
     * Get the local slot that will be used as temporary storage
     * for the thrown exception.
     *
     * @return The local slot with the return value.
     */
    private int getMethodThrowLocalSlot() {
        return this.getFirstAvailableLocalSlot() + 1;
    }

    /**
     * Load the method arguments to the stack.
     * <p>
     * Arguments are loaded verbatim - no boxing/unboxing
     * is performed.
     */
    protected void generateLoadMethodArguments() {
        int currentLocalSlot = this.getFirstArgumentLocalSlot();
        for (int i = 0; i < this.argTypes.length; i++) {
            currentLocalSlot += this.generateLoadOptionalAutoboxing(i, currentLocalSlot, false);
        }
    }

    /**
     * Generate an appropriate default value for the given type.
     * This is used when stubbing is disabled (i.e. in a when or assertMock).
     *
     * @param returnType The return type for this method.
     */
    private void generateDefaultValue(final Type returnType) {
        final char primitiveReturnType = returnType.toString().charAt(0);
        switch (primitiveReturnType) {
            case BYTE_PRIMITIVE_INTERNAL_NAME:
            case CHAR_PRIMITIVE_INTERNAL_NAME:
            case SHORT_PRIMITIVE_INTERNAL_NAME:
            case INT_PRIMITIVE_INTERNAL_NAME:
            case BOOL_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitInsn(ICONST_0);
                break;
            case LONG_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitInsn(LCONST_0);
                break;
            case FLOAT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitInsn(FCONST_0);
                break;
            case DOUBLE_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitInsn(DCONST_0);
                break;
            case ARRAY_PRIMITIVE_INTERNAL_NAME:
            case OBJECT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitInsn(ACONST_NULL);
                break;
            case VOID_PRIMITIVE_INTERNAL_NAME:
                break;
            default:
                throw new IllegalArgumentException(UNRECOGNISED_PRIMITIVE_TYPE + this.returnType + "'.\n"
                        + SUPER_NEW_JVM
                        + TO_FIX + "MoxyMockingMethodVisitor#generateDefaultValue()");
        }
    }

    /**
     * Generate an auto-unboxing return using the appropriate value from
     * the ASMMoxySupport.__moxy_asm_getReturnForCurrentInvocation method.
     * The defaultValue parameter is expected to generate the appropriate
     * default value bytecode for primitive types if there is no value
     * returned from that method (i.e. if this method has not been stubbed).
     *
     * @param boxClass           the Box class (e.g. java/lang/Byte)
     * @param valueOfMethod      method name for the xxxValue method
     * @param valueOfDescriptor  descriptor for the xxxValue method.
     * @param returnLabel        The label that should be visited directly before the return.
     * @param returnOpcode       The opcode that should be used for the return.
     * @param defaultValueOpcode the opcode that will generate a default value.
     */
    private void generateUnboxingReturnFromStubbing(final String boxClass,
                                                    final String valueOfMethod,
                                                    final String valueOfDescriptor,
                                                    final Label returnLabel,
                                                    final int returnOpcode,
                                                    final int defaultValueOpcode) {
        final Label defaultValueLabel = new Label();

        // Do we have a return value?
        this.delegate.visitVarInsn(ALOAD, this.getMethodReturnLocalSlot());
        this.delegate.visitJumpInsn(IFNULL, defaultValueLabel);

        // Yes - load it, unbox it, return it.
        this.delegate.visitVarInsn(ALOAD, this.getMethodReturnLocalSlot());
        this.delegate.visitTypeInsn(CHECKCAST, boxClass);
        this.delegate.visitMethodInsn(INVOKEVIRTUAL, boxClass, valueOfMethod, valueOfDescriptor, false);
        this.delegate.visitLabel(returnLabel);
        this.delegate.visitInsn(returnOpcode);

        this.delegate.visitLabel(defaultValueLabel);

        // No - return default value for type.
        this.delegate.visitInsn(defaultValueOpcode);
        this.delegate.visitInsn(returnOpcode);
    }

    protected void generateThrowInvalidStubbing(final String postfixMessage) {
        // Throw InvalidStubbing (can't call super to abstract)
        this.delegate.visitTypeInsn(NEW, INVALID_STUBBING_INTERNAL_NAME);
        this.delegate.visitInsn(DUP);

        // Load format string
        this.delegate.visitLdcInsn("Cannot call real method '%s' (" + postfixMessage + ")");

        // Make Java method signature
        this.generateLoadMockSupport();
        this.delegate.visitLdcInsn(this.methodName);
        this.delegate.visitLdcInsn(this.methodDescriptor);
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_MAKE_JAVA_SIGNATURE_METHOD_NAME,
                SUPPORT_MAKE_JAVA_SIGNATURE_DESCRIPTOR,
                true);

        this.delegate.visitMethodInsn(INVOKESPECIAL,
                INVALID_STUBBING_INTERNAL_NAME,
                INIT_NAME,
                VOID_STRING_STRING_DESCRIPTOR,
                false);
        this.delegate.visitInsn(ATHROW);
    }

    /**
     * Generate the mock method preamble (invocation recording).
     */
    protected void generatePreamble() {
        final int argc = this.argTypes.length;

        // load support to pass to recordInvocation later
        this.generateLoadMockSupport();

        // Get recorder from Support interface method as receiver
        this.delegate.visitInsn(DUP);
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_GETRECORDER_METHOD_NAME,
                SUPPORT_GETRECORDER_DESCRIPTOR,
                true);

        // Make stack right for recorder being receiver, and `this` being first param in later invoke.
        this.delegate.visitInsn(SWAP);

        // Loadconst method name and sig as second and third param
        this.delegate.visitLdcInsn(this.methodName);
        this.delegate.visitLdcInsn(this.methodDescriptor);

        // Create List as fourth param
        this.delegate.visitTypeInsn(NEW, ARRAYLIST_INTERNAL_NAME);
        this.delegate.visitInsn(DUP);
        this.delegate.visitIntInsn(BIPUSH, argc);
        this.delegate.visitMethodInsn(INVOKESPECIAL, ARRAYLIST_INTERNAL_NAME, INIT_NAME, VOID_INT_DESCRIPTOR, false);

        // Go through arguments, load and autobox (if necessary).
        int currentLocalSlot = this.getFirstArgumentLocalSlot();
        for (int argNum = 0; argNum < argc; argNum++) {

            this.delegate.visitInsn(DUP);

            currentLocalSlot += this.generateLoadAndAutoboxing(argNum, currentLocalSlot);

            this.delegate.visitMethodInsn(INVOKEVIRTUAL, ARRAYLIST_INTERNAL_NAME, ADD_NAME, BOOLEAN_OBJECT_DESCRIPTOR, false);
            this.delegate.visitInsn(POP);
        }

        // Call recorder - Need to record it before we can get the return/throw
        // for it, since the support methods rely on getLastInvocation().
        this.delegate.visitMethodInsn(INVOKEVIRTUAL,
                MOXY_RECORDER_INTERNAL_NAME,
                MOXY_RECORDER_RECORD_METHOD_NAME,
                MOXY_RECORDER_RECORD_DESCRIPTOR,
                false);
    }

    // NOTE this isn't super-efficient, but it's easier to grok this way...
    //
    // NOTES:
    //
    //   This method reserves locals[numargs+1] for the return object.
    //   This method reserves locals[numargs+2] for the throw object.
    //
    // TODO This method is ridiculously long...
    protected void generateReturn() {
        final Label checkCallSuperLabel = new Label();
        final Label checkDelegateLabel = new Label();
        final Label loadStubReturnLabel = new Label();
        final Label returnLabel = new Label();
        final Label noCallSuperOrDelegateLabel = new Label();

        // Firstly, if stubbing is currently disabled, just generate a default
        // value and jump directly to return; Don't call super, don't return or throw,
        // don't collect two-hundred pounds.
        this.generateLoadMockSupport();
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_IS_STUBBING_DISABLED_METHOD_NAME,
                SUPPORT_IS_STUBBING_DISABLED_DESCRIPTOR,
                true);

        this.delegate.visitInsn(ICONST_0);
        this.delegate.visitJumpInsn(IF_ICMPEQ, checkCallSuperLabel);

        /////////////  IS_STUBBING_DISABLED == true
        // All stubbing is disabled, we must be in either when() or assertMock[s]().
        // First up, load default
        this.generateDefaultValue(this.returnType);

        // Goto return (generated _much_ later).
        this.delegate.visitJumpInsn(GOTO, returnLabel);

        ///////////// IS_STUBBING_DISABLED == false
        // All stubbing is enabled, so continue.
        this.delegate.visitLabel(checkCallSuperLabel);

        // Firstly, run any thenDo actions.
        this.generateLoadMockSupport();
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_RUN_DOACTIONS_METHOD_NAME,
                SUPPORT_RUN_DOACTIONS_METHOD_DESCRIPTOR,
                true);

        // Check if we should be calling super. Super call trumps stubbing.
        // Higher-level API ensures we don't mix stubbing and supers.
        this.generateLoadMockSupport();
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_SHOULD_CALL_SUPER_METHOD_NAME,
                SUPPORT_SHOULD_CALL_SUPER_DESCRIPTOR,
                true);
        this.delegate.visitInsn(ICONST_0);
        this.delegate.visitJumpInsn(IF_ICMPEQ, checkDelegateLabel);

        if (!this.wasAbstract) {
            // Handle super call
            // Labels for try/catch
            final Label superTryStart = new Label();
            final Label superTryEnd = new Label();
            final Label superTryHandler = new Label();

            this.delegate.visitTryCatchBlock(superTryStart, superTryEnd, superTryHandler, THROWABLE_INTERNAL_NAME);

            // start of try
            this.delegate.visitLabel(superTryStart);

            // call super
            this.generateRealMethodCall();

            if (this.isNonVoidMethod()) {
                // Update the current invocation's return field to reflect the result
                this.generateTypeAppropriateDup(this.returnType);
                this.generateLoadMockSupport();
                this.generateTypeAppropriateSwap(this.returnType);
                this.generateAutobox(this.returnType);
                this.delegate.visitInsn(ACONST_NULL);
                this.delegate.visitMethodInsn(INVOKEINTERFACE,
                        MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                        SUPPORT_UPDATECURRENTRETURNED_METHOD_NAME,
                        VOID_OBJECT_THROWABLE_DESCRIPTOR,
                        true);
            }

            // end of try
            this.delegate.visitLabel(superTryEnd);
            this.delegate.visitJumpInsn(GOTO, returnLabel);

            // start of catch : exception is now on top of stack.
            this.delegate.visitLabel(superTryHandler);
            this.delegate.visitInsn(DUP);             // dup for later rethrow

            // Update the current invocation's thrown field to reflect the exception
            this.generateLoadMockSupport();
            this.delegate.visitInsn(SWAP);
            this.delegate.visitInsn(ACONST_NULL);
            this.delegate.visitInsn(SWAP);

            this.delegate.visitMethodInsn(INVOKEINTERFACE,
                    MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                    SUPPORT_UPDATECURRENTRETURNED_METHOD_NAME,
                    VOID_OBJECT_THROWABLE_DESCRIPTOR,
                    true);

            this.delegate.visitInsn(ATHROW);

            // end of catch
        } else {
            this.generateThrowInvalidStubbing("it is abstract");
        }

        ////////////////////////////////////////
        // No super call, so see if we should delegate...
        this.delegate.visitLabel(checkDelegateLabel);

        this.generateLoadMockSupport();
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_SHOULD_DELEGATE_METHOD_NAME,
                SUPPORT_SHOULD_DELEGATE_DESCRIPTOR,
                true);

        this.delegate.visitInsn(ICONST_0);
        this.delegate.visitJumpInsn(IF_ICMPEQ, noCallSuperOrDelegateLabel);

        // Delegating, so delegate.
        // TODO Need to be catching and recording exceptions here, like for super, above. See #2
        this.generateLoadMockSupport();
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_RUN_DELEGATE_METHOD_NAME,
                SUPPORT_RUN_DELEGATE_DESCRIPTOR,
                true);

        if (this.isNonVoidMethod()) {
            // Update the current invocation's return field to reflect the result
            this.delegate.visitInsn(DUP);
            this.generateLoadMockSupport();
            this.delegate.visitInsn(SWAP);
            this.delegate.visitInsn(ACONST_NULL);
            this.delegate.visitMethodInsn(INVOKEINTERFACE,
                    MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                    SUPPORT_UPDATECURRENTRETURNED_METHOD_NAME,
                    VOID_OBJECT_THROWABLE_DESCRIPTOR,
                    true);
        }

        // Unbox if necessary
        switch (this.returnType.getDescriptor().charAt(0)) {
            case BYTE_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitTypeInsn(CHECKCAST, BYTE_CLASS_INTERNAL_NAME);
                this.delegate.visitMethodInsn(INVOKEVIRTUAL,
                        BYTE_CLASS_INTERNAL_NAME,
                        BYTEVALUE_METHOD_NAME,
                        BYTEVALUE_DESCRIPTOR,
                        false);
                break;
            case CHAR_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitTypeInsn(CHECKCAST, CHAR_CLASS_INTERNAL_NAME);
                this.delegate.visitMethodInsn(INVOKEVIRTUAL,
                        CHAR_CLASS_INTERNAL_NAME,
                        CHARVALUE_METHOD_NAME,
                        CHARVALUE_DESCRIPTOR,
                        false);
                break;
            case SHORT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitTypeInsn(CHECKCAST, SHORT_CLASS_INTERNAL_NAME);
                this.delegate.visitMethodInsn(INVOKEVIRTUAL,
                        SHORT_CLASS_INTERNAL_NAME,
                        SHORTVALUE_METHOD_NAME,
                        SHORTVALUE_DESCRIPTOR,
                        false);
                break;
            case INT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitTypeInsn(CHECKCAST, INT_CLASS_INTERNAL_NAME);
                this.delegate.visitMethodInsn(INVOKEVIRTUAL,
                        INT_CLASS_INTERNAL_NAME,
                        INTVALUE_METHOD_NAME,
                        INTVALUE_DESCRIPTOR,
                        false);
                break;
            case BOOL_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitTypeInsn(CHECKCAST, BOOL_CLASS_INTERNAL_NAME);
                this.delegate.visitMethodInsn(INVOKEVIRTUAL,
                        BOOL_CLASS_INTERNAL_NAME,
                        BOOLVALUE_METHOD_NAME,
                        BOOLVALUE_DESCRIPTOR,
                        false);
                break;
            case LONG_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitTypeInsn(CHECKCAST, LONG_CLASS_INTERNAL_NAME);
                this.delegate.visitMethodInsn(INVOKEVIRTUAL,
                        LONG_CLASS_INTERNAL_NAME,
                        LONGVALUE_METHOD_NAME,
                        LONGVALUE_DESCRIPTOR,
                        false);
                break;
            case FLOAT_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitTypeInsn(CHECKCAST, FLOAT_CLASS_INTERNAL_NAME);
                this.delegate.visitMethodInsn(INVOKEVIRTUAL,
                        FLOAT_CLASS_INTERNAL_NAME,
                        FLOATVALUE_METHOD_NAME,
                        FLOATVALUE_DESCRIPTOR,
                        false);
                break;
            case DOUBLE_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitTypeInsn(CHECKCAST, DOUBLE_CLASS_INTERNAL_NAME);
                this.delegate.visitMethodInsn(INVOKEVIRTUAL,
                        DOUBLE_CLASS_INTERNAL_NAME,
                        DOUBLEVALUE_METHOD_NAME,
                        DOUBLEVALUE_DESCRIPTOR,
                        false);
                break;
            case OBJECT_PRIMITIVE_INTERNAL_NAME:
            case ARRAY_PRIMITIVE_INTERNAL_NAME:
                // Checkcast to keep verifier happy
                this.delegate.visitTypeInsn(CHECKCAST, this.returnType.getInternalName());
                break;
            case VOID_PRIMITIVE_INTERNAL_NAME:
                // Void, pop null return from invoke.
                this.delegate.visitInsn(POP);
                break;
            default:
                throw new IllegalArgumentException(UNRECOGNISED_PRIMITIVE_TYPE + this.returnType + "'.\n"
                        + SUPER_NEW_JVM
                        + TO_FIX + "MoxyMockingMethodVisitor#generateReturn()");
        }

        this.delegate.visitJumpInsn(GOTO, returnLabel);

        ///////////////////////////////////////////
        // Not calling super or delegating, so go with stubbing.
        //
        // Firstly, we'll record the return and exception we are stubbed with, if any
        this.delegate.visitLabel(noCallSuperOrDelegateLabel);

        // Get the value this method will return or null if none.
        this.generateLoadMockSupport();
        this.delegate.visitInsn(DUP);
        this.delegate.visitInsn(DUP);
        this.delegate.visitInsn(ICONST_1);    // forceRetain = true
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_GETCURRENTRETURN_METHOD_NAME,
                SUPPORT_GETCURRENTRETURN_DESCRIPTOR,
                true);

        this.delegate.visitInsn(DUP);
        this.delegate.visitVarInsn(ASTORE, this.getMethodReturnLocalSlot());

        // Get the exception this method will throw (or null if none)
        this.delegate.visitInsn(SWAP);
        this.delegate.visitInsn(ICONST_1);    // forceRetain = true
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_GETCURRENTTHROW_METHOD_NAME,
                SUPPORT_GETCURRENTTHROW_DESCRIPTOR,
                true);

        this.delegate.visitInsn(DUP);
        this.delegate.visitVarInsn(ASTORE, this.getMethodThrowLocalSlot());

        // Update the current invocation's returned and thrown fields.
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_UPDATECURRENTRETURNED_METHOD_NAME,
                SUPPORT_UPDATECURRENTRETURNED_DESCRIPTOR,
                true);

        // pop return/throw
        this.generateLoadMockSupport();
        this.delegate.visitMethodInsn(INVOKEINTERFACE,
                MOXY_SUPPORT_INTERFACE_INTERNAL_NAME,
                SUPPORT_POPRETURNTHROW_METHOD_NAME,
                SUPPORT_POPRETURNTHROW_DESCRIPTOR,
                true);

        // Always do exception first.
        // Should never have both anyway, this is/ enforced in ASMMoxyMockSupport
        // when the fields are set.
        this.delegate.visitVarInsn(ALOAD, this.getMethodThrowLocalSlot());
        this.delegate.visitJumpInsn(IFNULL, loadStubReturnLabel);

        this.delegate.visitVarInsn(ALOAD, this.getMethodThrowLocalSlot());
        this.delegate.visitInsn(ATHROW);

        this.delegate.visitLabel(loadStubReturnLabel);

        final char primitiveReturnType = this.returnType.toString().charAt(0);
        switch (primitiveReturnType) {
            case BYTE_PRIMITIVE_INTERNAL_NAME:
                this.generateUnboxingReturnFromStubbing(BYTE_CLASS_INTERNAL_NAME,
                        BYTEVALUE_METHOD_NAME,
                        BYTEVALUE_DESCRIPTOR,
                        returnLabel,
                        IRETURN,
                        ICONST_0);
                break;
            case CHAR_PRIMITIVE_INTERNAL_NAME:
                this.generateUnboxingReturnFromStubbing(CHAR_CLASS_INTERNAL_NAME,
                        CHARVALUE_METHOD_NAME,
                        CHARVALUE_DESCRIPTOR,
                        returnLabel,
                        IRETURN,
                        ICONST_0);
                break;
            case SHORT_PRIMITIVE_INTERNAL_NAME:
                this.generateUnboxingReturnFromStubbing(SHORT_CLASS_INTERNAL_NAME,
                        SHORTVALUE_METHOD_NAME,
                        SHORTVALUE_DESCRIPTOR,
                        returnLabel,
                        IRETURN,
                        ICONST_0);
                break;
            case INT_PRIMITIVE_INTERNAL_NAME:
                this.generateUnboxingReturnFromStubbing(INT_CLASS_INTERNAL_NAME,
                        INTVALUE_METHOD_NAME,
                        INTVALUE_DESCRIPTOR,
                        returnLabel,
                        IRETURN,
                        ICONST_0);
                break;
            case BOOL_PRIMITIVE_INTERNAL_NAME:
                this.generateUnboxingReturnFromStubbing(BOOL_CLASS_INTERNAL_NAME,
                        BOOLVALUE_METHOD_NAME,
                        BOOLVALUE_DESCRIPTOR,
                        returnLabel,
                        IRETURN,
                        ICONST_0);
                break;
            case LONG_PRIMITIVE_INTERNAL_NAME:
                this.generateUnboxingReturnFromStubbing(LONG_CLASS_INTERNAL_NAME,
                        LONGVALUE_METHOD_NAME,
                        LONGVALUE_DESCRIPTOR,
                        returnLabel,
                        LRETURN,
                        LCONST_0);
                break;
            case FLOAT_PRIMITIVE_INTERNAL_NAME:
                this.generateUnboxingReturnFromStubbing(FLOAT_CLASS_INTERNAL_NAME,
                        FLOATVALUE_METHOD_NAME,
                        FLOATVALUE_DESCRIPTOR,
                        returnLabel,
                        FRETURN,
                        FCONST_0);
                break;
            case DOUBLE_PRIMITIVE_INTERNAL_NAME:
                this.generateUnboxingReturnFromStubbing(DOUBLE_CLASS_INTERNAL_NAME,
                        DOUBLEVALUE_METHOD_NAME,
                        DOUBLEVALUE_DESCRIPTOR,
                        returnLabel,
                        DRETURN,
                        DCONST_0);
                break;
            case ARRAY_PRIMITIVE_INTERNAL_NAME:
            case OBJECT_PRIMITIVE_INTERNAL_NAME:
                // Grab return value
                this.delegate.visitVarInsn(ALOAD, this.getMethodReturnLocalSlot());

                // return it. CHECKCAST not strictly necessary, but for safety's sake...
                this.delegate.visitTypeInsn(CHECKCAST, this.returnType.getInternalName());
                this.delegate.visitLabel(returnLabel);
                this.delegate.visitInsn(ARETURN);
                break;
            case VOID_PRIMITIVE_INTERNAL_NAME:
                this.delegate.visitLabel(returnLabel);
                this.delegate.visitInsn(RETURN);
                break;
            default:
                throw new IllegalArgumentException(UNRECOGNISED_PRIMITIVE_TYPE + this.returnType + "'.\n"
                        + SUPER_NEW_JVM
                        + TO_FIX + "MoxyMockingMethodVisitor#generateReturn()");
        }
    }
}
