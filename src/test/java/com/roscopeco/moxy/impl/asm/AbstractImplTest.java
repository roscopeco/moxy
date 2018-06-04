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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.api.MoxyEngine;

/*
 * NOTE: When using this to mock ASMMoxyEngine, take care that you do the
 * following:
 *
 *   * All whens/asserts on the ENGINE should be done using Moxy.when/Moxy.assert.
 *     This is because the mock engine will have been created with the default
 *     engine.
 *
 *   * BUT, whens/assers on MOCKS created with the mock engine should be done
 *     using that mock engine, since that is the engine that will have created
 *     them.
 *
 * Failing to observe this may lead to rage, hair loss, depression, extended
 * periods of pointless debugging, and ultimately death (of your computer from
 * blunt-force trauma).
 */
public abstract class AbstractImplTest {
  protected ASMMoxyEngine makePartialMock(final boolean injectMocks, final Method... mockMethods)
  throws Exception {
    return this.makePartialMock(injectMocks, Arrays.stream(mockMethods).collect(Collectors.toSet()));
  }

  protected ASMMoxyEngine makePartialMock(final boolean injectMocks, final Set<Method> mockMethods)
  throws Exception {
    final MoxyEngine realEngine = Moxy.getMoxyEngine();

    final Class<? extends ASMMoxyEngine> mockClass = realEngine.getMockClass(
        ASMMoxyEngine.class, mockMethods);


    if (injectMocks) {
      final Constructor<? extends ASMMoxyEngine> ctor =
          mockClass.getDeclaredConstructor(MoxyEngine.class,
                                           ThreadLocalInvocationRecorder.class,
                                           ASMMoxyMatcherEngine.class);

      return ctor.newInstance(realEngine,
                              mock(ThreadLocalInvocationRecorder.class),
                              mock(ASMMoxyMatcherEngine.class));
    } else {
      final Constructor<? extends ASMMoxyEngine> ctor =
          mockClass.getDeclaredConstructor(MoxyEngine.class);

      return ctor.newInstance(realEngine);
    }
  }
}
