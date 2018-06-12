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
  static String readableTimes(final int times) {
    if (times == 0) {
      return "zero times";
    } else if (times == 1) {
      return "once";
    } else if (times == 2) {
      return "twice";
    } else {
      return "" + times + " times";
    }
  }

  static String inspectArg(final Object arg) {
    if (arg == null) {
      return null;
    } else if (arg instanceof String) {
      return "\"" + arg + "\"";
    } else if (arg instanceof Byte) {
      return "(byte)" + arg;
    } else if (arg instanceof Character) {
      return "'" + arg + "'";
    } else if (arg instanceof Short) {
      return "(short)" + arg;
    } else if (arg instanceof Integer) {
      return arg.toString();
    } else if (arg instanceof Long) {
      return arg + "L";
    } else if (arg instanceof Float) {
      return arg + "f";
    } else if (arg instanceof Double) {
      return arg + "d";
    } else {
      return arg.toString();
    }
  }

  static String inspectArgs(final Invocation invocation) {
    final String args = invocation.getArgs().stream()
        .map(TypeStringUtils::inspectArg)
        .collect(Collectors.joining(", "));

    return args;
  }

  static String buildArgsString(final Invocation invocation) {
    final String args = inspectArgs(invocation);

    if (args.isEmpty()) {
      return args;
    } else {
      return "with arguments (" + args + ") ";
    }
  }

  private static String unqualifiedClassName(final Type type) {
    if (type.getDescriptor().length() == 1) {
      return type.getClassName();
    } else {
      final String className = type.getClassName();
      return className.substring(className.lastIndexOf('.') + 1, className.length());
    }
  }

  static String shortDescriptorSignature(final String descriptor) {
    if (descriptor.contains("()")) {
      return "()";
    } else {
      return "(" + Arrays.stream(Type.getArgumentTypes(descriptor))
          .map(TypeStringUtils::unqualifiedClassName)
          .collect(Collectors.joining(", ")) + ")";
    }
  }

  static String javaMethodSignature(final Invocation invocation) {
    return javaMethodSignature(invocation.getMethodName(), invocation.getMethodDesc());
  }

  static String javaMethodSignature(final String methodName, final String methodDesc) {
    return javaMethodSignature(new StringBuilder(), methodName, methodDesc).toString();
  }

  static StringBuilder javaMethodSignature(final StringBuilder sb, final String methodName, final String methodDesc) {
    final Type returnType = Type.getReturnType(methodDesc);
    final Type[] argTypes = Type.getArgumentTypes(methodDesc);

    return sb.append(returnType.getClassName())
             .append(" ")
             .append(methodName)
             .append("(")
             .append(Arrays.stream(argTypes).map(Type::getClassName).collect(Collectors.joining(", ")))
             .append(")");
  }

  private TypeStringUtils() {
    throw new UnsupportedOperationException(
        "com.roscopeco.moxy.impl.asm.TypeStringUtils is not designed for instantiation");
  }
}
