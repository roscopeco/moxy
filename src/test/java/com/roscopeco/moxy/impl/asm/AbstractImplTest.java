package com.roscopeco.moxy.impl.asm;

import static com.roscopeco.moxy.Moxy.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.api.MoxyEngine;

public abstract class AbstractImplTest {
  protected ASMMoxyEngine makePartialMock(boolean injectMocks, Method... mockMethods)
  throws Exception {
    return makePartialMock(injectMocks, Arrays.stream(mockMethods).collect(Collectors.toSet()));    
  }
  
  protected ASMMoxyEngine makePartialMock(boolean injectMocks, Set<Method> mockMethods) 
  throws Exception {
    MoxyEngine realEngine = Moxy.getMoxyEngine();
    
    Class<? extends ASMMoxyEngine> mockClass = realEngine.getMockClass(
        ASMMoxyEngine.class, mockMethods);
    
    
    if (injectMocks) {
      Constructor<? extends ASMMoxyEngine> ctor = 
          mockClass.getDeclaredConstructor(MoxyEngine.class, 
                                           ThreadLocalInvocationRecorder.class, 
                                           ASMMoxyMatcherEngine.class);

      return ctor.newInstance(realEngine,
                              mock(ThreadLocalInvocationRecorder.class),
                              mock(ASMMoxyMatcherEngine.class));
    } else {
      Constructor<? extends ASMMoxyEngine> ctor = 
          mockClass.getDeclaredConstructor(MoxyEngine.class);
      
      return ctor.newInstance(realEngine);
    }
  }
}
