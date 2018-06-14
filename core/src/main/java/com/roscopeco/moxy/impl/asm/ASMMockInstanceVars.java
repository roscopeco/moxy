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

import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Holds mock objects' "instance variables".
 *
 * This just keeps more of the code in Java, and means we only
 * have to generate one field/setter combo rather than five
 * as previously.
 */
public class ASMMockInstanceVars {
  private final ASMMoxyEngine engine;

  // Using Deque here for efficient add-at-front, so when
  // we stream we see the most recent stubbing...
  private final Map<StubMethod, Deque<StubReturn>> returnMap;
  private final Map<StubMethod, Deque<StubThrow>> throwMap;

  // Present and true: call super; otherwise, use stubbing.
  private final Map<StubMethod, Deque<StubSuper>> callSuperMap;

  private final Map<StubMethod, List<StubDoActions>> doActionsMap;

  public ASMMockInstanceVars(final ASMMoxyEngine engine) {
    this.engine = engine;
    this.returnMap = new HashMap<>();
    this.throwMap = new HashMap<>();
    this.callSuperMap = new HashMap<>();
    this.doActionsMap = new HashMap<>();
  }

  ASMMoxyEngine getEngine() {
    return this.engine;
  }

  Map<StubMethod, Deque<StubReturn>> getReturnMap() {
    return this.returnMap;
  }

  Map<StubMethod, Deque<StubThrow>> getThrowMap() {
    return this.throwMap;
  }

  Map<StubMethod, Deque<StubSuper>> getCallSuperMap() {
    return this.callSuperMap;
  }

  Map<StubMethod, List<StubDoActions>> getDoActionsMap() {
    return this.doActionsMap;
  }
}