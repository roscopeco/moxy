package com.roscopeco.moxy.impl.asm;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

final class TypeStringUtils {  
  static String inspectArg(Object arg) {
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
  
  static String buildArgsString(Invocation invocation) {
    String args = invocation.getArgs().stream()
        .map(TypeStringUtils::inspectArg)
        .collect(Collectors.joining(", "));
    
    if (args.isEmpty()) {
      return args;
    } else {
      return "with arguments (" + args + ") ";      
    }
  }

  static String ellipsisDesc(String descriptor) {    
    if (descriptor.contains("()")) {
      return "()";
    } else {
      return "(" + Arrays.stream(Type.getArgumentTypes(descriptor))
          .map(Type::getClassName)
          .collect(Collectors.joining(", ")) + ")";
    }
  }
  
  static String javaMethodSignature(Invocation invocation) {
    return javaMethodSignature(invocation.getMethodName(), invocation.getMethodDesc());
  }
  
  static String javaMethodSignature(String methodName, String methodDesc) {
    Type returnType = Type.getReturnType(methodDesc);
    Type[] argTypes = Type.getArgumentTypes(methodDesc);
    
    return returnType.getClassName() 
              + " " 
              + methodName 
              + "(" 
              + Arrays.stream(argTypes).map(type -> type.getClassName()).collect(Collectors.joining(", "))
              + ")";
  }
  
  private TypeStringUtils() { 
    throw new UnsupportedOperationException(
        "com.roscopeco.moxy.impl.asm.TypesAndDescriptors is not designed for instantiation");
  }
}
