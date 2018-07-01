package com.roscopeco.moxy.impl.asm;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public class RemoveFinalTransformer implements ClassFileTransformer {
  private static final List<Pattern> NO_PROCESS_CLASSNAMES = Arrays.asList(
      /*Pattern.compile("^java/.*"),
      Pattern.compile("^javax/..*"),
      Pattern.compile("^javafx/.*"),
      Pattern.compile("^sun/.*"),
      Pattern.compile("^com/sun/.*"),
      Pattern.compile("^oracle/.*"),
      Pattern.compile("^jdk/.*"),
      Pattern.compile("^com/roscopeco/moxy/.*"),
      Pattern.compile("^org/objectweb/asm/.*"),
      Pattern.compile("Mock\\s+")*/
    );

  private static class RemoveFinalClassVisitor extends ClassVisitor {
    public RemoveFinalClassVisitor(final int api, final ClassVisitor classVisitor) {
      super(api, classVisitor);
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
      // TODO Auto-generated method stub
      super.visit(version,
                  access & ~Opcodes.ACC_FINAL,
                  name,
                  signature,
                  superName,
                  interfaces);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String descriptor, final String signature,
        final String[] exceptions) {
      int newAccess = access;

      if (((access & Opcodes.ACC_SYNTHETIC) == 0) && ((access & Opcodes.ACC_BRIDGE) == 0)) {
        newAccess = access & ~Opcodes.ACC_FINAL;
      }

      return super.visitMethod(newAccess,
                               name,
                               descriptor,
                               signature,
                               exceptions);
    }
  }

  @Override
  public byte[] transform(final ClassLoader loader, final String name, final Class<?> clz, final ProtectionDomain pd, final byte[] bytes)
      throws IllegalClassFormatException {

    for (final Pattern pattern : NO_PROCESS_CLASSNAMES) {
      if (pattern.matcher(name).find()) {
        return null;
      }
    }

    if (clz == null && name != null) {
      final ClassReader reader = new ClassReader(bytes);
      final ClassNode node = new ClassNode();

      final int access = reader.getAccess();

      if ((access & Opcodes.ACC_SYNTHETIC) > 0) {
        return null;
      }

      final ClassVisitor visitor = new RemoveFinalClassVisitor(Opcodes.ASM6, node);
      reader.accept(visitor, ClassReader.SKIP_DEBUG);

      final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
      node.accept(writer);

      return writer.toByteArray();
    } else {
      return null;
    }
  }
}
