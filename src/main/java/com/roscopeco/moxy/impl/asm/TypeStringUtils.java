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

import java.util.Arrays;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

final class TypeStringUtils {
  static String inspectArg(final Object arg) {
    if (arg == null) {
      return null;
    } else if (arg instanceof String) {
      return "\"" + arg + "\"";
    } else if (arg instanceof Character) {
      return "'" + arg + "'";
    } else {
      return arg.toString();
    }
  }

  static String buildArgsString(final Invocation invocation) {
    final String args = invocation.getArgs().stream()
        .map(TypeStringUtils::inspectArg)
        .collect(Collectors.joining(", "));

    if (args.isEmpty()) {
      return args;
    } else {
      return "with arguments (" + args + ") ";
    }
  }

  static String ellipsisDesc(final String descriptor) {
    if (descriptor.contains("()")) {
      return "()";
    } else {
      return "(" + Arrays.stream(Type.getArgumentTypes(descriptor))
          .map(Type::getClassName)
          .collect(Collectors.joining(", ")) + ")";
    }
  }

  static String javaMethodSignature(final Invocation invocation) {
    return javaMethodSignature(invocation.getMethodName(), invocation.getMethodDesc());
  }

  static String javaMethodSignature(final String methodName, final String methodDesc) {
    final Type returnType = Type.getReturnType(methodDesc);
    final Type[] argTypes = Type.getArgumentTypes(methodDesc);

    return returnType.getClassName()
              + " "
              + methodName
              + "("
              + Arrays.stream(argTypes).map(Type::getClassName).collect(Collectors.joining(", "))
              + ")";
  }

  private TypeStringUtils() {
    throw new UnsupportedOperationException(
        "com.roscopeco.moxy.impl.asm.TypesAndDescriptors is not designed for instantiation");
  }
}
