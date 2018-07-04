/*
 * TypesAndDescriptors.java -
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

package com.roscopeco.moxy.impl.asm.classmock;

import static com.roscopeco.moxy.impl.asm.TypesAndDescriptors.*;

import org.objectweb.asm.Type;

import com.roscopeco.moxy.Moxy;

/**
 * Types and descriptors specific to Classmocks.
 *
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 */
public class TypesAndDescriptors {
  private TypesAndDescriptors() {
    throw new UnsupportedOperationException(
        "com.roscopeco.moxy.impl.asm.classmock.TypesAndDescriptors is not designed for instantiation");
  }

  // General Java
  public static final String CLASS_DESCRIPTOR = Type.getDescriptor(Class.class);

  // Moxy class names/descriptors
  public static final String MOXY_INTERNAL_NAME = Type.getInternalName(Moxy.class);
  public static final String MOXY_DESCRIPTOR = "L" + MOXY_INTERNAL_NAME + ";";

  public static final String MOXY_SUPPORT_INTERFACE_DESCRIPTOR = "L" + MOXY_SUPPORT_INTERFACE_INTERNAL_NAME + ";";

  // General method descriptors
  public static final String SUPPORT_OBJECT_DESCRIPTOR = "(" + OBJECT_DESCRIPTOR + ")" + MOXY_SUPPORT_INTERFACE_DESCRIPTOR;
  public static final String SUPPORT_CLASS_DESCRIPTOR = "(" + CLASS_DESCRIPTOR + ")" + MOXY_SUPPORT_INTERFACE_DESCRIPTOR;
  public static final String VOID_OBJECT_SUPPORT_DESCRIPTOR = "(" + OBJECT_DESCRIPTOR + MOXY_SUPPORT_INTERFACE_DESCRIPTOR + ")V";
  public static final String ENGINE_VOID_DESCRIPTOR = "()" + MOXY_ENGINE_DESCRIPTOR;
  public static final String VOID_MOXYENGINE_DESCRIPTOR = "(" + MOXY_ASM_ENGINE_DESCRIPTOR + ")V";

  // Instance reg
  public static final Type INSTANCE_REGISTRY_TYPE = Type.getType(DelegateRegistry.class);
  public static final String INSTANCE_REGISTRY_INTERNAL_NAME = INSTANCE_REGISTRY_TYPE.getInternalName();

  public static final String REGISTRY_GET_DELEGATE_METHOD_NAME = "getDelegate";
  public static final String REGISTRY_GET_DELEGATE_DESCRIPTOR  = SUPPORT_OBJECT_DESCRIPTOR;

  public static final String REGISTRY_GET_STATIC_DELEGATE_METHOD_NAME = "getStaticDelegate";
  public static final String REGISTRY_GET_STATIC_DELEGATE_DESCRIPTOR  = SUPPORT_CLASS_DESCRIPTOR;

  public static final String REGISTRY_REGISTER_DELEGATE_METHOD_NAME = "registerDelegate";
  public static final String REGISTRY_REGISTER_DELEGATE_DESCRIPTOR = VOID_OBJECT_SUPPORT_DESCRIPTOR;

  // Moxy stuff
  public static final String MOXY_GETENGINE_METHOD_NAME = "getMoxyEngine";
  public static final String MOXY_GETENGINE_DESCRIPTOR = ENGINE_VOID_DESCRIPTOR;
}
