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
package com.roscopeco.moxy.impl.asm;

import static com.roscopeco.moxy.Moxy.*;
import static com.roscopeco.moxy.matchers.Matchers.*;
import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.api.InvocationRunnable;
import com.roscopeco.moxy.api.InvocationSupplier;
import com.roscopeco.moxy.api.MoxyEngine;
import com.roscopeco.moxy.api.MoxyException;
import com.roscopeco.moxy.matchers.PossibleMatcherUsageError;
import com.roscopeco.moxy.model.ClassWithPrimitiveReturns;
import com.roscopeco.moxy.model.DifferentAccessModifiers;
import com.roscopeco.moxy.model.MethodWithArguments;
import com.roscopeco.moxy.model.SimpleClass;

class TestASMMoxyEngine extends AbstractImplTest {
  @BeforeEach
  public void setUp() {
    Moxy.getMoxyEngine().reset();
  }

  @Test
  public void testConstruction() {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    assertThat(engine.getRecorder())
        .isNotNull()
        .isInstanceOf(ThreadLocalInvocationRecorder.class);

    assertThat(engine.getMatcherEngine())
        .isNotNull()
        .isInstanceOf(ASMMoxyMatcherEngine.class);

    assertThat(engine.getASMMatcherEngine())
        .isNotNull()
        .isInstanceOf(ASMMoxyMatcherEngine.class)
        .isEqualTo(engine.getMatcherEngine());
  }

  @Test
  public void testReset() {
    final ASMMoxyMatcherEngine matcherEngine = mock(ASMMoxyMatcherEngine.class);
    final ThreadLocalInvocationRecorder recorder = mock(ThreadLocalInvocationRecorder.class);

    final ASMMoxyEngine engine = new ASMMoxyEngine(recorder, matcherEngine);

    engine.reset();

    assertMock(() -> recorder.reset()).wasCalledOnce();
  }

  @Test
  public void testMockClassPassesThrough() throws Exception {
    final ASMMoxyEngine mockEngine = this.makePartialMock(true,
        ASMMoxyEngine.class.getDeclaredMethod("mock", Class.class, PrintStream.class));

    assertThat(mockEngine.mock(Object.class)).isNull();

    assertMock(() -> mockEngine.mock(Object.class, (PrintStream)null)).wasCalledOnce();
  }

  @Test
  public void testMockClassWithNull() throws Exception {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    assertThatThrownBy(() -> engine.mock(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Cannot mock null class");
  }

  @Test
  public void testMockClassPrintStreamHandlesErrors() throws Exception {
    final ASMMoxyEngine mockEngine = this.makePartialMock(true,
        ASMMoxyEngine.class.getDeclaredMethod("getMockClass", Class.class, Set.class, PrintStream.class));

    // Wraps non-moxy exceptions...
    final Exception marker = new Exception("MARKER");
    when(() -> mockEngine.getMockClass(Object.class, Collections.emptySet(), null))
        .thenThrow(marker);

    assertThatThrownBy(() -> mockEngine.mock(Object.class))
      .isInstanceOf(MoxyException.class)
      .hasMessage("Unrecoverable error: exception during mock generation")
      .extracting(e -> e.getCause())
        .hasSize(1)
        .allMatch(e -> e == marker);

    // But passes moxy exceptions straight through.
    final MoxyException moxyMarker = new MoxyException("MARKER");
    when(() -> mockEngine.getMockClass(Object.class, Collections.emptySet(), null))
        .thenThrow(moxyMarker);

    assertThatThrownBy(() -> mockEngine.mock(Object.class))
      .isInstanceOf(MoxyException.class)
      .hasMessage("MARKER");
  }

  @Test
  public void testGetMockClassLoaderClassSetPrintStreamNullArgs() throws Exception {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    // Throws on null classloader
    assertThatThrownBy(
        () -> engine.getMockClass(null, Object.class, Collections.emptySet(), null)
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage(
            "Implicit definition in the system classloader is unsupported.\n"
            + "Defining mocks here will almost certainly fail with NoClassDefFoundError for framework classes.\n"
            + "If you're sure this is what you want to do, pass system loader explicitly (rather than null)");

    // Throws on null class
    assertThatThrownBy(
        () -> engine.getMockClass(this.getClass().getClassLoader(), null, Collections.emptySet(), null)
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Cannot mock null class");

    // Null set is same as all methods
    final Class<? extends Object> mockClass = engine.getMockClass(this.getClass().getClassLoader(),
                                                            ClassWithPrimitiveReturns.class,
                                                            null,
                                                            null);

    try {
      assertThat(mockClass.getDeclaredMethod("returnByte")).isNotNull();
      assertThat(mockClass.getDeclaredMethod("returnChar")).isNotNull();
      assertThat(mockClass.getDeclaredMethod("returnShort")).isNotNull();
      assertThat(mockClass.getDeclaredMethod("returnInt")).isNotNull();
      assertThat(mockClass.getDeclaredMethod("returnLong")).isNotNull();
      assertThat(mockClass.getDeclaredMethod("returnFloat")).isNotNull();
      assertThat(mockClass.getDeclaredMethod("returnDouble")).isNotNull();
      assertThat(mockClass.getDeclaredMethod("returnBoolean")).isNotNull();
    } catch (final NoSuchMethodException e) {
      fail("All methods not declared: " + e.getMessage());
    }
  }

  @Test
  public void testGetMockClassLoaderClassSetPrintStreamDoesPrint() throws Exception {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    final ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
    final PrintStream ps = new PrintStream(baos);

    engine.getMockClass(this.getClass().getClassLoader(), Object.class, Collections.emptySet(), ps);

    final String output = new String(baos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);

    assertThat(output).contains("class Mock Object");
  }

  @Test
  public void testGetMockClassLoaderClassSetPrintStreamHandlesIOException() throws Exception {
    final ASMMoxyEngine engine = this.makePartialMock(false,
        ASMMoxyEngine.class.getDeclaredMethod("createMockClassNode", Class.class, Set.class, PrintStream.class));

    final IOException ioException = new IOException("Marker");

    when(() -> engine.createMockClassNode(any(), any(), any())).thenThrow(ioException);

    assertThatThrownBy(() ->
        engine.getMockClass(MoxyEngine.class.getClassLoader(),
                            SimpleClass.class,
                            MoxyEngine.ALL_METHODS,
                            null)
    )
        .isInstanceOf(MoxyException.class)
        .hasMessage("Unrecoverable Error")
          .extracting(e -> e.getCause())
          .hasSize(1)
          .hasSameElementsAs(Lists.newArrayList(ioException));
  }

  @Test
  public void testGetMockClassOtherPassThrough() throws Exception {
    final ASMMoxyEngine mockEngine = this.makePartialMock(false,
        ASMMoxyEngine.class.getDeclaredMethod("getMockClass", ClassLoader.class, Class.class, Set.class, PrintStream.class));

    final ClassLoader loader = ASMMoxyEngine.class.getClassLoader();
    final Class<Object> clz = Object.class;
    final Set<Method> methods = Collections.emptySet();
    final PrintStream stream = System.out;

    // ClassLoader, Class, Set
    assertThat(mockEngine.getMockClass(loader, clz, methods)).isNull();

    // Class, Set
    assertThat(mockEngine.getMockClass(clz, methods)).isNull();

    // ClassLoader, Class
    assertThat(mockEngine.getMockClass(loader, clz)).isNull();

    // Class, Set, PrintStream
    assertThat(mockEngine.getMockClass(clz, methods, stream)).isNull();

    // Class, PrintStream
    assertThat(mockEngine.getMockClass(clz, stream)).isNull();

    // Class
    assertThat(mockEngine.getMockClass(clz)).isNull();

    assertMock(() -> mockEngine.getMockClass(eq(loader),
                                             eq(clz),
                                             or(eq(methods), eq(null)),
                                             or(eq(stream), eq(null))))
        .wasCalled(6);
  }

  @Test
  public void testIsMock() {
    final SimpleClass mock = mock(SimpleClass.class);
    final Object nonMock = new Object();

    final ASMMoxyEngine engine = new ASMMoxyEngine();

    assertThat(engine.isMock(nonMock)).isFalse();
    assertThat(engine.isMock(nonMock.getClass())).isFalse();

    assertThat(engine.isMock(mock)).isTrue();
    assertThat(engine.isMock(mock.getClass())).isTrue();
  }

  @Test
  public void testIsMockCandidate() throws Exception {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    final Class<DifferentAccessModifiers> damClz = DifferentAccessModifiers.class;

    final Method publicMethod = damClz.getDeclaredMethod("publicMethod");
    final Method defaultMethod = damClz.getDeclaredMethod("defaultMethod");
    final Method protectedMethod = damClz.getDeclaredMethod("protectedMethod");
    final Method privateMethod = damClz.getDeclaredMethod("privateMethod");
    final Method finalMethod = damClz.getDeclaredMethod("finalMethod");

    assertThat(engine.isMockCandidate(publicMethod)).isTrue();
    assertThat(engine.isMockCandidate(defaultMethod)).isTrue();
    assertThat(engine.isMockCandidate(protectedMethod)).isTrue();
    assertThat(engine.isMockCandidate(privateMethod)).isFalse();
    assertThat(engine.isMockCandidate(finalMethod)).isFalse();
  }

  @Test
  public void testGatherAllMockableMethods() throws Exception {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    assertThat(engine.gatherAllMockableMethods(DifferentAccessModifiers.class))
        .hasSize(3)
        .containsAll(Lists.newArrayList(
            DifferentAccessModifiers.class.getDeclaredMethod("publicMethod"),
            DifferentAccessModifiers.class.getDeclaredMethod("defaultMethod"),
            DifferentAccessModifiers.class.getDeclaredMethod("protectedMethod")))
        .doesNotContainAnyElementsOf(Lists.newArrayList(
            DifferentAccessModifiers.class.getDeclaredMethod("privateMethod"),
            DifferentAccessModifiers.class.getDeclaredMethod("finalMethod")));
  }

  @Test
  public void testEnsureEngineConsistencyBeforeMonitoredInvocation() throws Exception {
    final ASMMoxyEngine engine = this.makePartialMock(true, MoxyEngine.NO_METHODS);

    final ThreadLocalInvocationRecorder mockRecorder = engine.getRecorder();
    final ASMMoxyMatcherEngine mockMatcherEngine = engine.getASMMatcherEngine();

    engine.ensureEngineConsistencyBeforeMonitoredInvocation();

    assertMock(() -> mockRecorder.clearLastInvocation()).wasCalledOnce();
    assertMock(() -> mockMatcherEngine.validateStackConsistency()).wasCalledOnce();
  }

  private void throwRTE(final Exception e) {
    if (e instanceof RuntimeException) {
      throw (RuntimeException)e;
    } else {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testNaivelyInvokeAndSwallowException() throws Exception {
    final ASMMoxyEngine mockEngine = this.makePartialMock(true,
        ASMMoxyEngine.class.getDeclaredMethod("ensureEngineConsistencyBeforeMonitoredInvocation"));

    final MoxyException moxyException = new MoxyException("MARKER");
    final NullPointerException nullPointerException = new NullPointerException("MARKER");
    final Exception generalException = new Exception("MARKER");

    assertThatThrownBy(() ->
        mockEngine.naivelyInvokeAndSwallowExceptions(() -> this.throwRTE(moxyException))
    ).isSameAs(moxyException);

    assertThatThrownBy(() ->
        mockEngine.naivelyInvokeAndSwallowExceptions(() -> this.throwRTE(nullPointerException))
    )
        .isInstanceOf(PossibleMatcherUsageError.class)
        .hasMessage("NPE in invocation: If you're using primitive matchers, ensure you're using the "
                  + "correct type (e.g. anyInt() rather than any()), especially when nesting.\n"
                  + "Otherwise, the causing exception may have more information.")
        .extracting(e -> e.getCause())
            .hasSize(1)
            .hasSameElementsAs(Lists.newArrayList(nullPointerException));

    try {
      mockEngine.naivelyInvokeAndSwallowExceptions(() -> this.throwRTE(generalException));
    } catch (final Exception e) {
      fail("Exception was not swallowed");
    }

    assertMock(() -> mockEngine.ensureEngineConsistencyBeforeMonitoredInvocation())
        .wasCalled(3);
  }

  @Test
  public void testDeleteLatestInvocationFromList() throws Exception {
    final ASMMoxyEngine mockEngine = this.makePartialMock(true, MoxyEngine.NO_METHODS);

    final ThreadLocalInvocationRecorder mockRecorder = mockEngine.getRecorder();
    final ASMMoxyMatcherEngine mockMatcherEngine = mockEngine.getASMMatcherEngine();

    mockEngine.deleteLatestInvocationFromList();

    assertMock(() -> mockRecorder.unrecordLastInvocation()).wasCalledOnce();
    assertMock(() -> mockMatcherEngine.validateStackConsistency()).wasCalledOnce();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testWhenSupplier() throws Exception {
    final ASMMoxyEngine mockEngine = this.makePartialMock(true,
        ASMMoxyEngine.class.getDeclaredMethod("naivelyInvokeAndSwallowExceptions", InvocationSupplier.class),
        ASMMoxyEngine.class.getDeclaredMethod("deleteLatestInvocationFromList"));

    final SimpleClass simpleMock = mockEngine.mock(SimpleClass.class);

    final Invocation testInvocation = new Invocation(simpleMock, "test", "test", Collections.emptyList());

    final ThreadLocalInvocationRecorder recorder = mockEngine.getRecorder();
    when(() -> recorder.getAndClearLastInvocation()).thenReturn(testInvocation);

    final ASMMoxyStubber<String> stubber = (ASMMoxyStubber<String>)
        mockEngine.when(() -> simpleMock.returnHello());

    // Ensure we got stubber
    assertThat(stubber)
      .isNotNull()
      .isInstanceOf(ASMMoxyStubber.class);

    // Ensure stubber got correct invocation
    assertThat(stubber.theInvocation.getReceiver()).isEqualTo(simpleMock);
    assertThat(stubber.theInvocation.getMethodName()).isEqualTo("test");
    assertThat(stubber.theInvocation.getMethodDesc()).isEqualTo("test");
    assertThat(stubber.theInvocation.getArgs()).isEqualTo(Collections.emptyList());

    // Ensure engine state consistency was maintained behind the scenes
    assertMock(() -> mockEngine.naivelyInvokeAndSwallowExceptions((InvocationSupplier<String>)any())).wasCalledOnce();
    assertMock(() -> recorder.replaceInvocationArgsWithMatchers()).wasCalledOnce();
    assertMock(() -> recorder.getAndClearLastInvocation()).wasCalledOnce();
    assertMock(() -> mockEngine.deleteLatestInvocationFromList()).wasCalledOnce();
  }

  @Test
  public void testWhenRunnable() throws Exception {
    final ASMMoxyEngine mockEngine = this.makePartialMock(true,
        ASMMoxyEngine.class.getDeclaredMethod("naivelyInvokeAndSwallowExceptions", InvocationRunnable.class),
        ASMMoxyEngine.class.getDeclaredMethod("deleteLatestInvocationFromList"));

    final MethodWithArguments voidReturnMock = mockEngine.mock(MethodWithArguments.class);

    final Invocation testInvocation = new Invocation(voidReturnMock, "test", "test", Collections.emptyList());

    final ThreadLocalInvocationRecorder recorder = mockEngine.getRecorder();
    when(() -> recorder.getAndClearLastInvocation()).thenReturn(testInvocation);

    final ASMMoxyVoidStubber stubber = (ASMMoxyVoidStubber)
        mockEngine.when(() -> voidReturnMock.hasArgs("one", "two"));

    // Ensure we got stubber
    assertThat(stubber)
      .isNotNull()
      .isInstanceOf(ASMMoxyVoidStubber.class);

    // Ensure stubber got correct invocation
    assertThat(stubber.theInvocation.getReceiver()).isEqualTo(voidReturnMock);
    assertThat(stubber.theInvocation.getMethodName()).isEqualTo("test");
    assertThat(stubber.theInvocation.getMethodDesc()).isEqualTo("test");
    assertThat(stubber.theInvocation.getArgs()).isEqualTo(Collections.emptyList());

    // Ensure engine state consistency was maintained behind the scenes
    assertMock(() -> mockEngine.naivelyInvokeAndSwallowExceptions((InvocationRunnable)any())).wasCalledOnce();
    assertMock(() -> recorder.replaceInvocationArgsWithMatchers()).wasCalledOnce();
    assertMock(() -> recorder.getAndClearLastInvocation()).wasCalledOnce();
    assertMock(() -> mockEngine.deleteLatestInvocationFromList()).wasCalledOnce();
  }

  @Test
  public void testAssertMockRunnable() throws Exception {
    final ASMMoxyEngine mockEngine = this.makePartialMock(true,
        ASMMoxyEngine.class.getDeclaredMethod("naivelyInvokeAndSwallowExceptions", InvocationRunnable.class),
        ASMMoxyEngine.class.getDeclaredMethod("deleteLatestInvocationFromList"));

    final MethodWithArguments voidReturnMock = mockEngine.mock(MethodWithArguments.class);

    final Invocation testInvocation = new Invocation(voidReturnMock, "test", "test", Collections.emptyList());

    final ThreadLocalInvocationRecorder recorder = mockEngine.getRecorder();
    when(() -> recorder.getAndClearLastInvocation()).thenReturn(testInvocation);

    final ASMMoxyVerifier verifier = (ASMMoxyVerifier)
        mockEngine.assertMock(() -> voidReturnMock.hasArgs("one", "two"));

    // Ensure we got verifier
    assertThat(verifier)
      .isNotNull()
      .isInstanceOf(ASMMoxyVerifier.class);

    // Ensure verifier got correct invocation
    assertThat(verifier.theInvocation.getReceiver()).isEqualTo(voidReturnMock);
    assertThat(verifier.theInvocation.getMethodName()).isEqualTo("test");
    assertThat(verifier.theInvocation.getMethodDesc()).isEqualTo("test");
    assertThat(verifier.theInvocation.getArgs()).isEqualTo(Collections.emptyList());

    // Ensure engine state consistency was maintained behind the scenes
    assertMock(() -> mockEngine.naivelyInvokeAndSwallowExceptions((InvocationRunnable)any())).wasCalledOnce();
    assertMock(() -> recorder.replaceInvocationArgsWithMatchers()).wasCalledOnce();
    assertMock(() -> recorder.getAndClearLastInvocation()).wasCalledOnce();
    assertMock(() -> mockEngine.deleteLatestInvocationFromList()).wasCalledOnce();
  }

  @Test
  public void testInitializeMock() {
    final ASMMoxyEngine engine = new ASMMoxyEngine();
    final SimpleClass mock = engine.mock(SimpleClass.class);

    final ASMMockSupport mockSupp = (ASMMockSupport)mock;

    final Map<StubMethod, Deque<StubReturn>> originalReturnMap = mockSupp.__moxy_asm_getReturnMap();
    final Map<StubMethod, Deque<StubThrow>> originalThrowMap = mockSupp.__moxy_asm_getThrowMap();
    final Map<StubMethod, Deque<StubSuper>> originalSuperMap = mockSupp.__moxy_asm_getCallSuperMap();

    engine.initializeMock(mock.getClass(), mock);

    assertThat(mockSupp.__moxy_asm_getReturnMap())
        .isNotNull()
        .isNotSameAs(originalReturnMap);

    assertThat(mockSupp.__moxy_asm_getThrowMap())
        .isNotNull()
        .isNotSameAs(originalThrowMap);

    assertThat(mockSupp.__moxy_asm_getCallSuperMap())
        .isNotNull()
        .isNotSameAs(originalSuperMap);

    assertThat(mockSupp.__moxy_asm_getEngine())
        .isNotNull()
        .isSameAs(engine);
  }

  @Test
  public void testInstantiateMock() {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    final Class<? extends SimpleClass> mockClass = engine.getMockClass(SimpleClass.class);

    final SimpleClass mock = engine.instantiateMock(mockClass);

    assertThat(mock).isNotNull();

    final ASMMockSupport mockSupp = (ASMMockSupport)mock;

    // Ensure fields are set up properly in instantiation
    assertThat(mockSupp.__moxy_asm_getEngine()).isSameAs(engine);

    assertThat(mockSupp.__moxy_asm_getReturnMap())
        .isNotNull()
        .isInstanceOf(HashMap.class)
        .isEmpty();

    assertThat(mockSupp.__moxy_asm_getReturnMap())
        .isNotNull()
        .isInstanceOf(HashMap.class)
        .isEmpty();
  }

  @Test
  public void testCreateMockClassNodeOneMethod() throws Exception {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    final ClassNode node = engine.createMockClassNode(ClassWithPrimitiveReturns.class,
        Arrays.asList(
            ClassWithPrimitiveReturns.class.getDeclaredMethod("returnByte")
            ).stream().collect(Collectors.toSet()),
        null
    );

    assertThat(node.superName).isEqualTo("com/roscopeco/moxy/model/ClassWithPrimitiveReturns");

    assertThat(node.methods)
      .hasSize(6)
      .hasOnlyElementsOfType(MethodNode.class)
      .extracting("name", "desc")
          .containsOnly(tuple("<init>",                  "(Lcom/roscopeco/moxy/api/MoxyEngine;)V"),
                        tuple("__moxy_asm_getEngine",    "()Lcom/roscopeco/moxy/impl/asm/ASMMoxyEngine;"),
                        tuple("__moxy_asm_getReturnMap", "()Ljava/util/Map;"),
                        tuple("__moxy_asm_getThrowMap",  "()Ljava/util/Map;"),
                        tuple("__moxy_asm_getCallSuperMap",  "()Ljava/util/Map;"),
                        tuple("returnByte",              "()B"));
  }

  @Test
  public void testCreateMockClassNodeAllMethods() throws Exception {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    final ClassNode node = engine.createMockClassNode(ClassWithPrimitiveReturns.class,
        MoxyEngine.ALL_METHODS,
        null
    );

    assertThat(node.superName).isEqualTo("com/roscopeco/moxy/model/ClassWithPrimitiveReturns");

    assertThat(node.methods)
      .hasSize(14)
      .hasOnlyElementsOfType(MethodNode.class)
      .extracting("name", "desc")
          .containsOnly(tuple("<init>",                  "(Lcom/roscopeco/moxy/api/MoxyEngine;)V"),
                        tuple("__moxy_asm_getEngine",    "()Lcom/roscopeco/moxy/impl/asm/ASMMoxyEngine;"),
                        tuple("__moxy_asm_getReturnMap", "()Ljava/util/Map;"),
                        tuple("__moxy_asm_getThrowMap",  "()Ljava/util/Map;"),
                        tuple("__moxy_asm_getCallSuperMap",  "()Ljava/util/Map;"),
                        tuple("returnByte",              "()B"),
                        tuple("returnChar",              "()C"),
                        tuple("returnShort",             "()S"),
                        tuple("returnInt",               "()I"),
                        tuple("returnLong",              "()J"),
                        tuple("returnFloat",             "()F"),
                        tuple("returnDouble",            "()D"),
                        tuple("returnBoolean",           "()Z"),
                        tuple("returnVoid",              "()V"));
  }

  @Test
  public void testCreateMockClassNodeNoMethods() throws Exception {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    final ClassNode node = engine.createMockClassNode(ClassWithPrimitiveReturns.class,
        MoxyEngine.NO_METHODS,
        null
    );

    assertThat(node.superName).isEqualTo("com/roscopeco/moxy/model/ClassWithPrimitiveReturns");

    assertThat(node.methods)
      .hasSize(5)
      .hasOnlyElementsOfType(MethodNode.class)
      .extracting("name", "desc")
          .containsOnly(tuple("<init>",                  "(Lcom/roscopeco/moxy/api/MoxyEngine;)V"),
                        tuple("__moxy_asm_getEngine",    "()Lcom/roscopeco/moxy/impl/asm/ASMMoxyEngine;"),
                        tuple("__moxy_asm_getReturnMap", "()Ljava/util/Map;"),
                        tuple("__moxy_asm_getThrowMap",  "()Ljava/util/Map;"),
                        tuple("__moxy_asm_getCallSuperMap",  "()Ljava/util/Map;"));
  }

  @Test
  public void testResetMockObject() throws Exception {
    final ASMMoxyEngine mockEngine = this.makePartialMock(false,
        ASMMoxyEngine.class.getDeclaredMethod("isMock", Object.class),
        ASMMoxyEngine.class.getDeclaredMethod("initializeMock", Class.class, Object.class));

    when(() -> mockEngine.isMock((Object)any())).thenCallRealMethod();
    when(() -> mockEngine.initializeMock(any(), any())).thenCallRealMethod();

    final SimpleClass mock = mockEngine.mock(SimpleClass.class);

    mockEngine.resetMock(mock);

    assertMock(() -> mockEngine.isMock(mock)).wasCalledOnce();

    // Called twice - once during mock instantiation, again during reset
    assertMock(() -> mockEngine.initializeMock(mock.getClass(), mock)).wasCalledTwice();
  }

  @Test
  public void testResetMockObjectNotAMock() throws Exception {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    final String notAMock = "Not a mock ;)";

    assertThatThrownBy(() ->
        engine.resetMock(notAMock)
    )
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Cannot reset 'Not a mock ;)' - Object is not a mock");
  }

  @Test
  public void testIsMockStubbingDisabledOnThisThread() {
    final ASMMoxyEngine engine = new ASMMoxyEngine();

    assertThat(engine.isMockStubbingDisabledOnThisThread()).isFalse();

    engine.disableMockStubbingOnThisThread();
    assertThat(engine.isMockStubbingDisabledOnThisThread()).isTrue();

    engine.enableMockStubbingOnThisThread();
    assertThat(engine.isMockStubbingDisabledOnThisThread()).isFalse();
  }
}
