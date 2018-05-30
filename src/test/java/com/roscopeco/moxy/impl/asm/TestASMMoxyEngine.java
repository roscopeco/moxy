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
import java.util.HashMap;
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
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
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
    ASMMoxyMatcherEngine matcherEngine = mock(ASMMoxyMatcherEngine.class);
    ThreadLocalInvocationRecorder recorder = mock(ThreadLocalInvocationRecorder.class);
    
    ASMMoxyEngine engine = new ASMMoxyEngine(recorder, matcherEngine);
    
    engine.reset();
    
    assertMock(() -> recorder.reset()).wasCalledOnce();    
  }
  
  @Test
  public void testMockClassPassesThrough() throws Exception {
    ASMMoxyEngine mockEngine = makePartialMock(true, 
        ASMMoxyEngine.class.getDeclaredMethod("mock", Class.class, PrintStream.class));    
    
    assertThat(mockEngine.mock(Object.class)).isNull();
    
    assertMock(() -> mockEngine.mock(Object.class, (PrintStream)null)).wasCalledOnce();
  }
  
  @Test
  public void testMockClassWithNull() throws Exception {
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
    assertThatThrownBy(() -> engine.mock(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Cannot mock null class");
  }
  
  @Test
  public void testMockClassPrintStreamHandlesErrors() throws Exception {
    ASMMoxyEngine mockEngine = makePartialMock(true, 
        ASMMoxyEngine.class.getDeclaredMethod("getMockClass", Class.class, Set.class, PrintStream.class));
    
    // Wraps non-moxy exceptions...
    Exception marker = new Exception("MARKER");
    when(() -> mockEngine.getMockClass(Object.class, Collections.emptySet(), null))
        .thenThrow(marker);
    
    assertThatThrownBy(() -> mockEngine.mock(Object.class))
      .isInstanceOf(MoxyException.class)
      .hasMessage("Unrecoverable error: exception during mock generation")
      .extracting(e -> e.getCause())
        .hasSize(1)
        .allMatch(e -> e == marker);

    // But passes moxy exceptions straight through.
    MoxyException moxyMarker = new MoxyException("MARKER");
    when(() -> mockEngine.getMockClass(Object.class, Collections.emptySet(), null))
        .thenThrow(moxyMarker);
    
    assertThatThrownBy(() -> mockEngine.mock(Object.class))
      .isInstanceOf(MoxyException.class)
      .hasMessage("MARKER");
  }
  
  @Test
  public void testGetMockClassLoaderClassSetPrintStreamNullArgs() throws Exception {
    ASMMoxyEngine engine = new ASMMoxyEngine();

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
    Class<? extends Object> mockClass = engine.getMockClass(this.getClass().getClassLoader(), 
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
    } catch (NoSuchMethodException e) {
      fail("All methods not declared: " + e.getMessage());
    }
  }

  @Test
  public void testGetMockClassLoaderClassSetPrintStreamDoesPrint() throws Exception {
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);    
    PrintStream ps = new PrintStream(baos);
    
    engine.getMockClass(this.getClass().getClassLoader(), Object.class, Collections.emptySet(), ps);
    
    String output = new String(baos.toByteArray(), java.nio.charset.StandardCharsets.UTF_8);
    
    assertThat(output).contains("class Mock Object");
  }
  
  @Test
  public void testGetMockClassLoaderClassSetPrintStreamHandlesIOException() throws Exception {
    ASMMoxyEngine engine = makePartialMock(false,
        ASMMoxyEngine.class.getDeclaredMethod("createMockClassNode", Class.class, Set.class, PrintStream.class));
    
    IOException ioException = new IOException("Marker");
    
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
    ASMMoxyEngine mockEngine = makePartialMock(false,
        ASMMoxyEngine.class.getDeclaredMethod("getMockClass", ClassLoader.class, Class.class, Set.class, PrintStream.class));
    
    ClassLoader loader = ASMMoxyEngine.class.getClassLoader();
    Class<Object> clz = Object.class;
    Set<Method> methods = Collections.emptySet();
    PrintStream stream = System.out;
    
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
    SimpleClass mock = mock(SimpleClass.class);
    Object nonMock = new Object();
    
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
    assertThat(engine.isMock(nonMock)).isFalse();
    assertThat(engine.isMock(nonMock.getClass())).isFalse();
    
    assertThat(engine.isMock(mock)).isTrue();
    assertThat(engine.isMock(mock.getClass())).isTrue();
  }
  
  @Test
  public void testIsMockCandidate() throws Exception {
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
    Class<DifferentAccessModifiers> damClz = DifferentAccessModifiers.class;
    
    Method publicMethod = damClz.getDeclaredMethod("publicMethod"); 
    Method defaultMethod = damClz.getDeclaredMethod("defaultMethod"); 
    Method protectedMethod = damClz.getDeclaredMethod("protectedMethod"); 
    Method privateMethod = damClz.getDeclaredMethod("privateMethod"); 
    Method finalMethod = damClz.getDeclaredMethod("finalMethod");
    
    assertThat(engine.isMockCandidate(publicMethod)).isTrue();
    assertThat(engine.isMockCandidate(defaultMethod)).isTrue();
    assertThat(engine.isMockCandidate(protectedMethod)).isTrue();
    assertThat(engine.isMockCandidate(privateMethod)).isFalse();
    assertThat(engine.isMockCandidate(finalMethod)).isFalse();
  }
  
  @Test
  public void testGatherAllMockableMethods() throws Exception {
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
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
    ASMMoxyEngine engine = makePartialMock(true, MoxyEngine.NO_METHODS);
    
    ThreadLocalInvocationRecorder mockRecorder = engine.getRecorder();
    ASMMoxyMatcherEngine mockMatcherEngine = engine.getASMMatcherEngine();
    
    engine.ensureEngineConsistencyBeforeMonitoredInvocation();
    
    assertMock(() -> mockRecorder.clearLastInvocation()).wasCalledOnce();
    assertMock(() -> mockMatcherEngine.validateStackConsistency()).wasCalledOnce();
  }

  private void throwRTE(Exception e) {
    if (e instanceof RuntimeException) {
      throw (RuntimeException)e;
    } else {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testNaivelyInvokeAndSwallowException() throws Exception {    
    ASMMoxyEngine mockEngine = makePartialMock(true,
        ASMMoxyEngine.class.getDeclaredMethod("ensureEngineConsistencyBeforeMonitoredInvocation"));
    
    MoxyException moxyException = new MoxyException("MARKER");
    NullPointerException nullPointerException = new NullPointerException("MARKER");
    Exception generalException = new Exception("MARKER");

    assertThatThrownBy(() ->
        mockEngine.naivelyInvokeAndSwallowExceptions(() -> throwRTE(moxyException))
    ).isSameAs(moxyException);

    assertThatThrownBy(() ->
        mockEngine.naivelyInvokeAndSwallowExceptions(() -> throwRTE(nullPointerException))
    )
        .isInstanceOf(PossibleMatcherUsageError.class)
        .hasMessage("NPE in invocation: If you're using primitive matchers, ensure you're using the " 
                  + "correct type (e.g. anyInt() rather than any()), especially when nesting.\n"
                  + "Otherwise, the causing exception may have more information.")
        .extracting(e -> e.getCause())
            .hasSize(1)
            .hasSameElementsAs(Lists.newArrayList(nullPointerException));    

    try {
      mockEngine.naivelyInvokeAndSwallowExceptions(() -> throwRTE(generalException));
    } catch (Exception e) {
      fail("Exception was not swallowed");
    }

    assertMock(() -> mockEngine.ensureEngineConsistencyBeforeMonitoredInvocation())
        .wasCalled(3);
  }

  @Test
  public void testDeleteLatestInvocationFromList() throws Exception {
    ASMMoxyEngine mockEngine = makePartialMock(true, MoxyEngine.NO_METHODS);

    ThreadLocalInvocationRecorder mockRecorder = mockEngine.getRecorder();
    ASMMoxyMatcherEngine mockMatcherEngine = mockEngine.getASMMatcherEngine();

    mockEngine.deleteLatestInvocationFromList();

    assertMock(() -> mockRecorder.unrecordLastInvocation()).wasCalledOnce();
    assertMock(() -> mockMatcherEngine.validateStackConsistency()).wasCalledOnce();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testWhenSupplier() throws Exception {
    ASMMoxyEngine mockEngine = makePartialMock(true, 
        ASMMoxyEngine.class.getDeclaredMethod("naivelyInvokeAndSwallowExceptions", InvocationSupplier.class),
        ASMMoxyEngine.class.getDeclaredMethod("deleteLatestInvocationFromList"));

    SimpleClass simpleMock = mockEngine.mock(SimpleClass.class);
    
    Invocation testInvocation = new Invocation(simpleMock, "test", "test", Collections.emptyList());
    
    ThreadLocalInvocationRecorder recorder = mockEngine.getRecorder();    
    when(() -> recorder.getAndClearLastInvocation()).thenReturn(testInvocation);    

    ASMMoxyStubber<String> stubber = (ASMMoxyStubber<String>)
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
    ASMMoxyEngine mockEngine = makePartialMock(true, 
        ASMMoxyEngine.class.getDeclaredMethod("naivelyInvokeAndSwallowExceptions", InvocationRunnable.class),
        ASMMoxyEngine.class.getDeclaredMethod("deleteLatestInvocationFromList"));

    MethodWithArguments voidReturnMock = mockEngine.mock(MethodWithArguments.class);
    
    Invocation testInvocation = new Invocation(voidReturnMock, "test", "test", Collections.emptyList());
    
    ThreadLocalInvocationRecorder recorder = mockEngine.getRecorder();    
    when(() -> recorder.getAndClearLastInvocation()).thenReturn(testInvocation);    

    ASMMoxyVoidStubber stubber = (ASMMoxyVoidStubber)
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
    ASMMoxyEngine mockEngine = makePartialMock(true, 
        ASMMoxyEngine.class.getDeclaredMethod("naivelyInvokeAndSwallowExceptions", InvocationRunnable.class),
        ASMMoxyEngine.class.getDeclaredMethod("deleteLatestInvocationFromList"));

    MethodWithArguments voidReturnMock = mockEngine.mock(MethodWithArguments.class);
    
    Invocation testInvocation = new Invocation(voidReturnMock, "test", "test", Collections.emptyList());
    
    ThreadLocalInvocationRecorder recorder = mockEngine.getRecorder();    
    when(() -> recorder.getAndClearLastInvocation()).thenReturn(testInvocation);    

    ASMMoxyVerifier verifier = (ASMMoxyVerifier)
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
  public void testInstantiateMock() {
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
    Class<? extends SimpleClass> mockClass = engine.getMockClass(SimpleClass.class);
    
    SimpleClass mock = engine.instantiateMock(mockClass);
    
    assertThat(mock).isNotNull();
    
    ASMMockSupport mockSupp = (ASMMockSupport)mock;
    
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
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
    ClassNode node = engine.createMockClassNode(ClassWithPrimitiveReturns.class,
        Arrays.asList(
            ClassWithPrimitiveReturns.class.getDeclaredMethod("returnByte")
            ).stream().collect(Collectors.toSet()),
        null
    );
    
    assertThat(node.superName).isEqualTo("com/roscopeco/moxy/model/ClassWithPrimitiveReturns");
    
    assertThat(node.methods)
      .hasSize(5)
      .hasOnlyElementsOfType(MethodNode.class)
      .extracting("name", "desc")
          .containsOnly(tuple("<init>",                  "(Lcom/roscopeco/moxy/api/MoxyEngine;)V"),
                        tuple("__moxy_asm_getEngine",    "()Lcom/roscopeco/moxy/impl/asm/ASMMoxyEngine;"),
                        tuple("__moxy_asm_getReturnMap", "()Ljava/util/HashMap;"),
                        tuple("__moxy_asm_getThrowMap",  "()Ljava/util/HashMap;"),
                        tuple("returnByte",              "()B"));    
  }
  
  @Test
  public void testCreateMockClassNodeAllMethods() throws Exception {
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
    ClassNode node = engine.createMockClassNode(ClassWithPrimitiveReturns.class,
        MoxyEngine.ALL_METHODS,
        null
    );
    
    assertThat(node.superName).isEqualTo("com/roscopeco/moxy/model/ClassWithPrimitiveReturns");
    
    assertThat(node.methods)
      .hasSize(13)
      .hasOnlyElementsOfType(MethodNode.class)
      .extracting("name", "desc")
          .containsOnly(tuple("<init>",                  "(Lcom/roscopeco/moxy/api/MoxyEngine;)V"),
                        tuple("__moxy_asm_getEngine",    "()Lcom/roscopeco/moxy/impl/asm/ASMMoxyEngine;"),
                        tuple("__moxy_asm_getReturnMap", "()Ljava/util/HashMap;"),
                        tuple("__moxy_asm_getThrowMap",  "()Ljava/util/HashMap;"),
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
    ASMMoxyEngine engine = new ASMMoxyEngine();
    
    ClassNode node = engine.createMockClassNode(ClassWithPrimitiveReturns.class,
        MoxyEngine.NO_METHODS,
        null
    );
    
    assertThat(node.superName).isEqualTo("com/roscopeco/moxy/model/ClassWithPrimitiveReturns");
    
    assertThat(node.methods)
      .hasSize(4)
      .hasOnlyElementsOfType(MethodNode.class)
      .extracting("name", "desc")
          .containsOnly(tuple("<init>",                  "(Lcom/roscopeco/moxy/api/MoxyEngine;)V"),
                        tuple("__moxy_asm_getEngine",    "()Lcom/roscopeco/moxy/impl/asm/ASMMoxyEngine;"),
                        tuple("__moxy_asm_getReturnMap", "()Ljava/util/HashMap;"),
                        tuple("__moxy_asm_getThrowMap",  "()Ljava/util/HashMap;"));
  }
}
