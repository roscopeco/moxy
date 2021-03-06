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
package com.roscopeco.moxy.api;

import com.roscopeco.moxy.matchers.Matchers;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

/**
 * <p>A MoxyEngine is the class that is responsible for generating mocks
 * according to whatever strategy it is designed to use.</p>
 *
 * <p>It usually also coordinates activities of mocks, as well as
 * integrating the matchers.</p>
 *
 * <p>The default implementation uses the ASM library to generate mocks
 * at runtime. If you wish to replace that implementation, see
 * {@link com.roscopeco.moxy.Moxy#setMoxyEngine(MoxyEngine)}.</p>
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 */
@SuppressWarnings("squid:S1214" /* Cleaner than using an enum - allows either one of these consts or an actual set to be passed */)
public interface MoxyEngine {
    /**
     * Constant set to be passed to mock generation methods when all methods are
     * to be mocked.
     *
     * @since 1.0
     */
    @SuppressWarnings({
            "squid:S2386", /* Ignore this in sonar, it's final and null, hence immutable. */
            "squid:S1214"  /* Cleaner than using an enum - allows either one of these consts or an actual set to be passed */
    })
    Set<Method> ALL_METHODS = null;

    /**
     * Constant set to be passed to mock generation methods when no methods are
     * to be mocked.
     *
     * @since 1.0
     */
    @SuppressWarnings("squid:S1214" /* Cleaner than using an enum - allows either one of these consts or an actual set to be passed */)
    Set<Method> NO_METHODS = Collections.emptySet();

    /**
     * <p>Register a default return value generator for the specified type.</p>
     *
     * <p>The return generator will be used to generate default return values for the
     * specified types as required by mocks. In practice, this means whenever a
     * mock hasn't been stubbed with another return type or other behaviour.</p>
     *
     * <p>By default, the framework will provide zero, null or false returns for
     * all types except <code>java.util.Optional</code>. For the latter, the
     * default return is <code>Optional.empty()</code>.</p>
     *
     * <p><strong>Note:</strong> The default value generator will <strong>not</strong>
     * be called during <em>monitored invocations</em>. The framework will always
     * generate the same return values during those invocations - however this
     * is a detail that should concern only those involved in developing or extending
     * the framework itself.</p>
     *
     * @param className The type this generator will create return objects for.
     * @param generator The generator that will generate return values.
     * @since 1.0
     */
    void registerDefaultReturnForType(String className, DefaultReturnGenerator generator);

    /**
     * Remove a configured default return value generator for the specified type.
     *
     * @param className The type of for which the return value generator should be removed.
     * @since 1.0
     */
    void removeDefaultReturnForType(String className);

    /**
     * Reset default return type generators to the defaults.
     */
    void resetDefaultReturnTypes();

    /**
     * <p>Reset this engine.</p>
     *
     * <p>Implementations may choose to globally reset the mocking framework,
     * or to reset only for the current thread. This must be documented
     * with the engine implementation.</p>
     *
     * <p>The default engine <code>reset()</code>s on a global basis.</p>
     *
     * @since 1.0
     */
    void reset();

    /**
     * <p>Create a mock class of the given class and then define it using the supplied
     * {@link ClassDefinitionStrategy}, suggesting that the strategy use the given ClassLoader.</p>
     *
     * <p>Depending on the strategy implementation used, the supplied ClassLoader may not be used - the
     * default definition strategy however will always use it if possible.</p>
     *
     * <p>This method allows only a specific subset of methods to be mocked.
     * Please note, however, that any abstract methods will always be mocked
     * in order to generate loadable classes. This also applies to interfaces,
     * in which all methods (except those with a <code>default</code>
     * implementation) are considered abstract.</p>
     *
     * <p>The <code>MoxyEngine</code> class provides two useful constants you
     * may want to use for the <code>methods</code> parameter:</p>
     *
     * <pre><code>
     *   MoxyEngine.ALL_METHODS       // mock all methods
     *   MoxyEngine.NO_METHODS        // mock no methods
     * </code></pre>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * <p>During creation of the mock, this method will also dump the
     * generated bytecode, in a debugging format, to the supplied
     * <code>PrintStream</code>.</p>
     *
     * @param loader             The <code>ClassLoader</code> to define the new class in.
     * @param clz                The original class.
     * @param definitionStrategy The {@link ClassDefinitionStrategy} to use.
     * @param methods            <code>Set</code> of <code>Method</code>s to mock.
     * @param trace              If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
     * @param <I>                The type being mocked.
     * @return The new <code>Class</code> object.
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(
            ClassLoader loader,
            Class<I> clz,
            ClassDefinitionStrategy definitionStrategy,
            Set<Method> methods,
            PrintStream trace
    );

    /**
     * <p>Create a mock class of the given class and then define it in the
     * given <code>ClassLoader</code>.</p>
     *
     * <p>This method allows only a specific subset of methods to be mocked.
     * Please note, however, that any abstract methods will always be mocked
     * in order to generate loadable classes. This also applies to interfaces,
     * in which all methods (except those with a <code>default</code>
     * implementation) are considered abstract.</p>
     *
     * <p>The <code>MoxyEngine</code> class provides two useful constants you
     * may want to use for the <code>methods</code> parameter:</p>
     *
     * <pre><code>
     *   MoxyEngine.ALL_METHODS       // mock all methods
     *   MoxyEngine.NO_METHODS        // mock no methods
     * </code></pre>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * <p>During creation of the mock, this method will also dump the
     * generated bytecode, in a debugging format, to the supplied
     * <code>PrintStream</code>.</p>
     *
     * @param loader  The <code>ClassLoader</code> to define the new class in.
     * @param clz     The original class.
     * @param methods <code>Set</code> of <code>Method</code>s to mock.
     * @param trace   If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
     * @param <I>     The type being mocked.
     * @return The new <code>Class</code> object.
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, Set<Method> methods, PrintStream trace);

    /**
     * <p>Create a mock class of the given class and then define it using the supplied
     * {@link ClassDefinitionStrategy}, suggesting that the strategy use the given ClassLoader.</p>
     *
     * <p>Depending on the strategy implementation used, the supplied ClassLoader may not be used - the
     * default definition strategy however will always use it if possible.</p>
     *
     * <p>This method allows only a specific subset of methods to be mocked.
     * Please note, however, that any abstract methods will always be mocked
     * in order to generate loadable classes. This also applies to interfaces,
     * in which all methods (except those with a <code>default</code>
     * implementation) are considered abstract.</p>
     *
     * <p>The <code>MoxyEngine</code> class provides two useful constants you
     * may want to use for the <code>methods</code> parameter:</p>
     *
     * <pre><code>
     *   MoxyEngine.ALL_METHODS       // mock all methods
     *   MoxyEngine.NO_METHODS        // mock no methods
     * </code></pre>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * @param loader             The <code>ClassLoader</code> to define the new class in.
     * @param clz                The original class.
     * @param definitionStrategy The {@link ClassDefinitionStrategy} to use.
     * @param methods            <code>Set</code> of <code>Method</code>s to mock.
     * @param <I>                The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(Class, Set)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, ClassDefinitionStrategy definitionStrategy, Set<Method> methods);

    /**
     * <p>Create a mock class of the given class and then define it in the
     * given <code>ClassLoader</code>.</p>
     *
     * <p>This method allows only a specific subset of methods to be mocked.
     * Please note, however, that any abstract methods will always be mocked
     * in order to generate loadable classes. This also applies to interfaces,
     * in which all methods (except those with a <code>default</code>
     * implementation) are considered abstract.</p>
     *
     * <p>The <code>MoxyEngine</code> class provides two useful constants you
     * may want to use for the <code>methods</code> parameter:</p>
     *
     * <pre><code>
     *   MoxyEngine.ALL_METHODS       // mock all methods
     *   MoxyEngine.NO_METHODS        // mock no methods
     * </code></pre>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * @param loader  The <code>ClassLoader</code> to define the new class in.
     * @param clz     The original class.
     * @param methods <code>Set</code> of <code>Method</code>s to mock.
     * @param <I>     The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(Class, Set)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, Set<Method> methods);

    /**
     * <p>Create a mock class of the given class and then then define it using the supplied
     * {@link ClassDefinitionStrategy}.</p>
     *
     * <p>For the default engine, the default <code>ClassLoader</code> is the same
     * <code>ClassLoader</code> that loaded the Moxy framework.</p>
     *
     * <p>This method allows only a specific subset of methods to be mocked.
     * Please note, however, that any abstract methods will always be mocked
     * in order to generate loadable classes. This also applies to interfaces,
     * in which all methods (except those with a <code>default</code>
     * implementation) are considered abstract.</p>
     *
     * <p>The <code>MoxyEngine</code> class provides two useful constants you
     * may want to use for the <code>methods</code> parameter:</p>
     *
     * <pre><code>
     *   MoxyEngine.ALL_METHODS       // mock all methods
     *   MoxyEngine.NO_METHODS        // mock no methods
     * </code></pre>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * @param clz                The original class.
     * @param definitionStrategy The {@link ClassDefinitionStrategy} to use.
     * @param methods            <code>Set</code> of <code>Method</code>s to mock.
     * @param <I>                The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(ClassLoader, Class, Set)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(Class<I> clz, ClassDefinitionStrategy definitionStrategy, Set<Method> methods);

    /**
     * <p>Create a mock class of the given class and then define it in the
     * engine's default <code>ClassLoader</code>.</p>
     *
     * <p>For the default engine, the default <code>ClassLoader</code> is the same
     * <code>ClassLoader</code> that loaded the Moxy framework.</p>
     *
     * <p>This method allows only a specific subset of methods to be mocked.
     * Please note, however, that any abstract methods will always be mocked
     * in order to generate loadable classes. This also applies to interfaces,
     * in which all methods (except those with a <code>default</code>
     * implementation) are considered abstract.</p>
     *
     * <p>The <code>MoxyEngine</code> class provides two useful constants you
     * may want to use for the <code>methods</code> parameter:</p>
     *
     * <pre><code>
     *   MoxyEngine.ALL_METHODS       // mock all methods
     *   MoxyEngine.NO_METHODS        // mock no methods
     * </code></pre>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * @param clz     The original class.
     * @param methods <code>Set</code> of <code>Method</code>s to mock.
     * @param <I>     The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(ClassLoader, Class, Set)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(Class<I> clz, Set<Method> methods);

    /**
     * <p>Create a mock class of the given class and then define it using the supplied
     * {@link ClassDefinitionStrategy}, suggesting that the strategy use the given ClassLoader.</p>
     *
     * <p>Depending on the strategy implementation used, the supplied ClassLoader may not be used - the
     * default definition strategy however will always use it if possible.</p>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * @param loader             The <code>ClassLoader</code> to define the new class in.
     * @param clz                The original class.
     * @param definitionStrategy The {@link ClassDefinitionStrategy} to use.
     * @param <I>                The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(Class, Set)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz, ClassDefinitionStrategy definitionStrategy);

    /**
     * <p>Create a mock class of the given class and then define it in the
     * given <code>ClassLoader</code>.</p>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * @param loader The <code>ClassLoader</code> to define the new class in.
     * @param clz    The original class.
     * @param <I>    The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(Class, Set)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(ClassLoader loader, Class<I> clz);

    /**
     * <p>Create a mock class of the given class and then define it using the supplied
     * {@link ClassDefinitionStrategy}.</p>
     *
     * <p>For the default engine, the default <code>ClassLoader</code> is the same
     * <code>ClassLoader</code> that loaded the Moxy framework.</p>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * <p>During creation of the mock, this method will also dump the
     * generated bytecode, in a debugging format, to the supplied
     * <code>PrintStream</code>.</p>
     *
     * @param clz                The original class.
     * @param definitionStrategy The {@link ClassDefinitionStrategy} to use.
     * @param trace              If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
     * @param <I>                The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(ClassLoader, Class, Set, PrintStream)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(Class<I> clz, ClassDefinitionStrategy definitionStrategy, PrintStream trace);

    /**
     * <p>Create a mock class of the given class and then define it in the
     * engine's default <code>ClassLoader</code>.</p>
     *
     * <p>For the default engine, the default <code>ClassLoader</code> is the same
     * <code>ClassLoader</code> that loaded the Moxy framework.</p>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * <p>During creation of the mock, this method will also dump the
     * generated bytecode, in a debugging format, to the supplied
     * <code>PrintStream</code>.</p>
     *
     * @param clz   The original class.
     * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
     * @param <I>   The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(ClassLoader, Class, Set, PrintStream)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(Class<I> clz, PrintStream trace);

    /**
     * <p>Create a mock class of the given class and then define it using the supplied
     * {@link ClassDefinitionStrategy}.</p>
     *
     * <p>This method allows only a specific subset of methods to be mocked.
     * Please note, however, that any abstract methods will always be mocked
     * in order to generate loadable classes. This also applies to interfaces,
     * in which all methods (except those with a <code>default</code>
     * implementation) are considered abstract.</p>
     *
     * <p>The <code>MoxyEngine</code> class provides two useful constants you
     * may want to use for the <code>methods</code> parameter:</p>
     *
     * <pre><code>
     *   MoxyEngine.ALL_METHODS       // mock all methods
     *   MoxyEngine.NO_METHODS        // mock no methods
     * </code></pre>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * <p>During creation of the mock, this method will also dump the
     * generated bytecode, in a debugging format, to the supplied
     * <code>PrintStream</code>.</p>
     *
     * @param clz                The original class.
     * @param definitionStrategy The {@link ClassDefinitionStrategy} to use.
     * @param methods            <code>Set</code> of <code>Method</code>s to mock.
     * @param trace              If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
     * @param <I>                The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(ClassLoader, Class, Set, PrintStream)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(Class<I> clz, ClassDefinitionStrategy definitionStrategy, Set<Method> methods, PrintStream trace);

    /**
     * <p>Create a mock class of the given class and then define it in the
     * engine's default <code>ClassLoader</code>.</p>
     *
     * <p>For the default engine, the default <code>ClassLoader</code> is the same
     * <code>ClassLoader</code> that loaded the Moxy framework.</p>
     *
     * <p>This method allows only a specific subset of methods to be mocked.
     * Please note, however, that any abstract methods will always be mocked
     * in order to generate loadable classes. This also applies to interfaces,
     * in which all methods (except those with a <code>default</code>
     * implementation) are considered abstract.</p>
     *
     * <p>The <code>MoxyEngine</code> class provides two useful constants you
     * may want to use for the <code>methods</code> parameter:</p>
     *
     * <pre><code>
     *   MoxyEngine.ALL_METHODS       // mock all methods
     *   MoxyEngine.NO_METHODS        // mock no methods
     * </code></pre>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * <p>During creation of the mock, this method will also dump the
     * generated bytecode, in a debugging format, to the supplied
     * <code>PrintStream</code>.</p>
     *
     * @param clz     The original class.
     * @param methods <code>Set</code> of <code>Method</code>s to mock.
     * @param trace   If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
     * @param <I>     The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(ClassLoader, Class, Set, PrintStream)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(Class<I> clz, Set<Method> methods, PrintStream trace);

    /**
     * <p>Create a mock class of the given class and then define it using the supplied
     * {@link ClassDefinitionStrategy}.</p>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * @param clz                The original class.
     * @param definitionStrategy The {@link ClassDefinitionStrategy} to use.
     * @param <I>                The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(ClassLoader, Class)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(Class<I> clz, ClassDefinitionStrategy definitionStrategy);

    /**
     * <p>Create a mock class of the given class and then define it in the
     * engine's default <code>ClassLoader</code>.</p>
     *
     * <p>For the default engine, the default <code>ClassLoader</code> is the same
     * <code>ClassLoader</code> that loaded the Moxy framework.</p>
     *
     * <p>Generated Mock classes will define public pass-through constructors to
     * all public constructors on the superclass (if concrete), or a single
     * public constructor (if mocking an interface). These constructors
     * will have the same arguments as the original constructor, with a single
     * argument of type <code>MoxyEngine</code> prepended.</p>
     *
     * <p>Therefore, to instantiate a mock from a class, you will need to
     * obtain the appropriate constructor via reflection and call it, passing
     * in the <code>MoxyEngine</code> that created the mock followed by any
     * other arguments the constructor requires.</p>
     *
     * <p>These constructors are generated primarily for use with partial
     * mocking. If you are mocking the entire class (i.e. all methods) you
     * may prefer to use {@link #mock(Class)} and friends instead.</p>
     *
     * @param clz The original class.
     * @param <I> The type being mocked.
     * @return The new <code>Class</code> object.
     * @see #getMockClass(ClassLoader, Class)
     * @since 1.0
     */
    <I> Class<? extends I> getMockClass(Class<I> clz);

    /**
     * <p>Create a mock instance of the given class.</p>
     *
     * <p>The mock will be created in using engine's default {@link ClassDefinitionStrategy}.
     * For the default engine, this means via reflective access to the same <code>ClassLoader</code> that
     * loaded the engine.</p>
     *
     * <p><strong>Note:</strong> This method will not call a constructor
     * on the generated instance. If you require a constructor be called,
     * please see {@link MoxyEngine#getMockClass(Class)} and friends.</p>
     *
     * @param clz The <code>Class</code> to mock.
     * @param <T> The type being mocked.
     * @return A new mock instance.
     * @see #mock(Class, PrintStream)
     * @since 1.0
     */
    <T> T mock(Class<T> clz);

    /**
     * <p>Create a mock instance of the given class.</p>
     *
     * <p>The mock will be created in using engine's default {@link ClassDefinitionStrategy}.
     * For the default engine, this means via reflective access to the same <code>ClassLoader</code> that
     * loaded the engine.</p>
     *
     * <p><strong>Note:</strong> This method will not call a constructor
     * on the generated instance. If you require a constructor be called,
     * please see {@link MoxyEngine#getMockClass(Class)} and friends.</p>
     *
     * <p>During creation of the mock, this method will also dump the generated
     * bytecode, in a debugging format, to the supplied <code>PrintStream</code>.</p>
     *
     * @param clz   The <code>Class</code> to mock.
     * @param trace If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
     * @param <T>   The type being mocked.
     * @return A new mock instance.
     * @see #mock(Class)
     * @since 1.0
     */
    <T> T mock(Class<T> clz, PrintStream trace);

    /**
     * <p>Create a mock instance of the given class using the supplied {@link ClassDefinitionStrategy}.</p>
     * <p>
     * The mock will be created in whichever <code>ClassLoader</code> is selected by the definition
     * strategy supplied.
     *
     * <p><strong>Note:</strong> This method will not call a constructor
     * on the generated instance. If you require a constructor be called,
     * please see {@link MoxyEngine#getMockClass(Class)} and friends.</p>
     *
     * @param clz                The <code>Class</code> to mock.
     * @param definitionStrategy The {@link ClassDefinitionStrategy} to use.
     * @param <T>                The type being mocked.
     * @return A new mock instance.
     * @see #mock(Class, PrintStream)
     * @since 1.0
     */
    <T> T mock(Class<T> clz, ClassDefinitionStrategy definitionStrategy);

    /**
     * <p>Create a mock instance of the given class using the supplied {@link ClassDefinitionStrategy}.</p>
     * <p>
     * The mock will be created in whichever <code>ClassLoader</code> is selected by the definition
     * strategy supplied.
     *
     * <p><strong>Note:</strong> This method will not call a constructor
     * on the generated instance. If you require a constructor be called,
     * please see {@link MoxyEngine#getMockClass(Class)} and friends.</p>
     *
     * <p>During creation of the mock, this method will also dump the generated
     * bytecode, in a debugging format, to the supplied <code>PrintStream</code>.</p>
     *
     * @param clz                The <code>Class</code> to mock.
     * @param definitionStrategy The {@link ClassDefinitionStrategy} to use.
     * @param trace              If non-null, the resulting class will be dumped (with a <code>TraceClassVisitor</code>) to the given stream.
     * @param <T>                The type being mocked.
     * @return A new mock instance.
     * @see #mock(Class)
     * @since 1.0
     */
    <T> T mock(Class<T> clz, ClassDefinitionStrategy definitionStrategy, PrintStream trace);

    /**
     * <p>Determines whether the supplied class is a mock class.</p>
     *
     * <p>How this determination is made is implementation-dependent, but may
     * rely on the fact that the class has the (mandatory)
     * {@link com.roscopeco.moxy.api.MoxyMock} annotation.</p>
     *
     * @param clz The class to query.
     * @return <code>true</code> if the class is a mock, <code>false</code> otherwise.
     * @see #isMock(Object)
     * @since 1.0
     */
    boolean isMock(Class<?> clz);

    /**
     * <p>Determines whether the supplied object is a mock instance.</p>
     *
     * <p>How this determination is made is implementation-dependent, but may
     * rely on the fact that the class has the (mandatory)
     * {@link com.roscopeco.moxy.api.MoxyMock} annotation.</p>
     *
     * @param obj The object to query.
     * @return <code>true</code> if the class is a mock, <code>false</code> otherwise.
     * @see #isMock(Class)
     * @since 1.0
     */
    boolean isMock(Object obj);

    /**
     * <p>Reset the supplied mock, removing all stubbing that was previously applied.</p>
     *
     * @param mock The mock to reset
     * @see MoxyEngine#reset()
     * @since 1.0
     */
    void resetMock(Object mock);

    /**
     * <p>Starts stubbing of the mock invocation in the supplied lambda
     * expression.</p>
     *
     * <p>The invocation of the mock is used to determine which method
     * and argument combination is to be stubbed and is not counted
     * toward mock invocation, return or throw counters.</p>
     *
     * <p>Example usage:</p>
     *
     * <pre><code>
     * engine.when(() -&gt; mock.someMethod("one", "two")).thenReturn("three");
     * </code></pre>
     *
     * <p>The arguments passed to the mock within the lambda may be either
     * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
     *
     * <p>This method begins a new chain of stubbing for the given invocation -
     * Any prior stubbing applied is discarded, to be replaced by the
     * stubbing applied on the returned {@link MoxyStubber}.</p>
     *
     * <p>See {@link MoxyStubber} for details on the stubbing methods
     * available.</p>
     *
     * @param invocation A lambda that will invoke the method to be stubbed.
     * @param <T>        The type being stubbed (the return type of the mocked method).
     * @return A {@link MoxyStubber} that will stub the given method.
     * @see #when(InvocationRunnable)
     * @see MoxyStubber
     * @since 1.0
     */
    <T> MoxyStubber<T> when(InvocationSupplier<T> invocation);

    /**
     * <p>Starts stubbing of the mock invocation in the supplied lambda
     * expression.</p>
     *
     * <p>The Java compiler will automatically select this overload of
     * the <code>when</code> method when the method to be stubbed is
     * void.</p>
     *
     * <p>The invocation of the mock is used to determine which method
     * and argument combination is to be stubbed and is not counted
     * toward mock invocation, return or throw counters.</p>
     *
     * <p>Example usage:</p>
     *
     * <pre><code>
     * engine.when(() -&gt; mock.voidMethod("one", "two")).thenThrow("three");
     * </code></pre>
     *
     * <p>The arguments passed to the mock within the lambda may be either
     * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
     *
     * <p>This method begins a new chain of stubbing for the given invocation -
     * Any prior stubbing applied is discarded, to be replaced by the
     * stubbing applied on the returned {@link MoxyVoidStubber}.</p>
     *
     * <p>See {@link MoxyStubber} for details on the stubbing methods
     * available.</p>
     *
     * @param invocation A lambda that will invoke the method to be stubbed.
     * @return A {@link MoxyVoidStubber} that will stub the given method.
     * @see #when(InvocationSupplier)
     * @see MoxyStubber
     * @since 1.0
     */
    MoxyVoidStubber when(InvocationRunnable invocation);

    /**
     * <p>Starts verification of the mock invocation in the supplied lambda
     * expression.</p>
     *
     * <p>The invocation of the mock is used to determine which method
     * and argument combination is to be verified and is not counted
     * toward mock invocation, return or throw counters.</p>
     *
     * <p>Example usage:</p>
     *
     * <pre><code>
     * engine.assertMock(() -&gt; mock.voidMethod(engine, "one", "two")).wasCalled();
     * </code></pre>
     *
     * <p>The arguments passed to the mock within the lambda may be either
     * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
     *
     * <p>See {@link MoxyVerifier} for details on the verifying methods
     * available.</p>
     *
     * @param invocation A lambda that will invoke the method to be stubbed.
     * @return A {@link MoxyVerifier} that will verify the given method.
     * @see MoxyVerifier
     * @since 1.0
     */
    MoxyVerifier assertMock(InvocationRunnable invocation);

    /**
     * <p>Starts verification of one or more mock invocations at the same
     * time. This allows multiple invocations to be checked together
     * to ensure ordering, for example.</p>
     *
     * <p>The invocation of the mock is used to determine which method
     * and argument combination is to be verified and is not counted
     * toward mock invocation, return or throw counters.</p>
     *
     * <p>Example usage:</p>
     *
     * <pre><code>
     * engine.assertMocks(() -&gt; {
     *   mock.voidMethod(engine, "one", "two")).wasCalled();
     *   mock.anotherMethod();
     *
     *   anotherMock.someMethod("five");
     *
     *   mock.finalMethod("Bees");
     * })
     *     .wereAllCalledOnce()
     *     .inThatOrder();
     * </code></pre>
     *
     * <p>The arguments passed to the mocks within the lambda may be either
     * immediate arguments, other mocks, or argument matchers (see {@link Matchers}).</p>
     *
     * <p>See {@link MoxyMultiVerifier} for details on the verifying methods
     * available.</p>
     *
     * @param invocation A lambda that will invoke the methods to be verified.
     * @return A {@link MoxyMultiVerifier} that will verify the invocations.
     * @see #assertMock(InvocationRunnable)
     * @since 1.0
     */
    MoxyMultiVerifier assertMocks(InvocationRunnable invocation);

    /**
     * <p>Register the given matcher with this <code>MoxyEngine</code>.</p>
     *
     * <p>This method triggers a two-step process, whereby the engine
     * takes care of any housekeeping required and then calls back to
     * the {@link MoxyMatcher#addToStack(java.util.Deque)}
     * method, passing in the appropriate stack.</p>
     *
     * <p>See the documentation on {@link MoxyMatcher} for more information.</p>
     *
     * @param matcher The {@link MoxyMatcher} to register.
     * @since 1.0
     */
    void registerMatcher(final MoxyMatcher<?> matcher);

    /**
     * <p>Obtain the default {@link com.roscopeco.moxy.api.ClassDefinitionStrategy} used by this engine.</p>
     *
     * @return The default class definition strategy.
     * @since 1.0
     */
    ClassDefinitionStrategy getDefaultClassDefinitionStrategy();
}