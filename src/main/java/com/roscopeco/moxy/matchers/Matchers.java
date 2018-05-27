package com.roscopeco.moxy.matchers;

import java.util.Arrays;
import java.util.List;

import com.roscopeco.moxy.Moxy;
import com.roscopeco.moxy.api.MoxyEngine;

/**
 * <p>Provides static access to the standard Moxy argument matchers.</p>
 * 
 * <p>This class, along with the {@link com.roscopeco.moxy.Moxy} class, are the main top-level
 * classes most users will need to interact with when using Moxy. They are
 * designed so you can simply <code>import static</code> and start mocking.</p>
 * 
 * <p>The methods in this class should <strong>only</strong> be called in the 
 * context of a Moxy <em>when(...)</em> or <em>assertMock(...)</em> call.
 * Calling them out of context will result in {@link InconsistentMatchersException}
 * or {@link PossibleMatcherUsageError} exceptions when interacting with the framework.</p>
 * 
 * <p>For a mini example, see {@link com.roscopeco.moxy.Moxy}. For full details,
 * see <code>README.md</code>.</p>
 * 
 * <p><strong>Note:</strong> Many of the standard matchers provided with this class
 * come in both primitive and reference versions. It is important that you use the
 * appropriate primitive type if the method matchers are applied to expects 
 * primitive arguments. See README for more details.</p>
 *  
 * @author Ross Bamford &lt;roscopeco AT gmail DOT com&gt;
 * @since 1.0
 * @see com.roscopeco.moxy.Moxy
 *
 */
public class Matchers {
  /**
   * Create a matcher that will match any primitive <code>byte</code>
   * in the current default Moxy engine.
   * 
   * @return zero (ignored).
   * @see #anyByte(MoxyEngine)
   * @since 1.0
   */
  public static byte anyByte() {
    return anyByte(Moxy.getMoxyEngine());
  }
  
  /**
   * Create a matcher that will match any primitive byte in the specified
   * {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte anyByte(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Byte>());
    return 0;
  }

  /**
   * Create a matcher that will match any primitive <code>char</code>
   * in the current default Moxy engine.
   * 
   * @return zero (<code>NUL</code>) (ignored).
   * @see #anyChar(MoxyEngine)
   * @since 1.0
   */
  public static char anyChar() {
    return anyChar(Moxy.getMoxyEngine());
  }
  
  /**
   * Create a matcher that will match any primitive <code>char</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * 
   * @return zero (<code>NUL</code>) (ignored).
   * @since 1.0
   */
  public static char anyChar(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Character>());
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code>
   * in the current default Moxy engine.
   * 
   * @return zero (ignored).
   * @see #anyShort(MoxyEngine)
   * @since 1.0
   */
  public static short anyShort() {
    return anyShort(Moxy.getMoxyEngine());
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short anyShort(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Short>());
    return 0;
  }
    
  /**
   * Create a matcher that will match any primitive <code>int</code>
   * in the current default Moxy engine.
   * 
   * @return zero (ignored).
   * @see #anyInt(MoxyEngine)
   * @since 1.0
   */
  public static int anyInt() {
    return anyInt(Moxy.getMoxyEngine());
  }
  
  /**
   * Create a matcher that will match any primitive <code>int</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int anyInt(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Integer>());
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code>
   * in the current default Moxy engine.
   * 
   * @return zero (ignored).
   * @see #anyLong(MoxyEngine)
   * @since 1.0
   */
  public static long anyLong() {
    return anyLong(Moxy.getMoxyEngine());
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long anyLong(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Long>());
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code>
   * in the current default Moxy engine.
   * 
   * @return zero (ignored).
   * @see #anyFloat(MoxyEngine)
   * @since 1.0
   */
  public static float anyFloat() {
    return anyFloat(Moxy.getMoxyEngine());
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float anyFloat(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Float>());
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code>
   * in the current default Moxy engine.
   * 
   * @return zero (ignored).
   * @see #anyDouble(MoxyEngine)
   * @since 1.0
   */
  public static double anyDouble() {
    return anyDouble(Moxy.getMoxyEngine());
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double anyDouble(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Double>());
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code>
   * in the current default Moxy engine.
   * 
   * @return <code>false</code> (ignored).
   * @see #anyBool(MoxyEngine)
   * @since 1.0
   */
  public static boolean anyBool() {
    return anyBool(Moxy.getMoxyEngine());
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * 
   * @return <code>false</code> (ignored).
   * @since 1.0
   */
  public static boolean anyBool(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<Boolean>());
    return false;
  }
  
  /**
   * <p>Create a matcher that will match any <code>Object</code>
   * in the current default Moxy engine.</p>
   *
   * For notes, see {@link #any(MoxyEngine)}.
   * 
   * @return <code>null</code> (ignored).
   * @see #any(MoxyEngine)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on.
   */
  public static <T> T any() {
    return any(Moxy.getMoxyEngine());
  }
  
  /**
   * <p>Create a matcher that will match any <code>Object</code>
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p>Note that, outside the static type guarantees provided by 
   * compile-time generics, this matcher performs no type checks.
   * It really will match <strong>any</strong> <code>Object</code>
   * at runtime.</p>
   * 
   * <p>While this is unlikely to cause problems, it is worth noting.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * 
   * @return <code>null</code> (ignored).
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on.
   */
  public static <T> T any(MoxyEngine engine) {
    engine.getMatcherEngine().registerMatcher(new AnyMatcher<T>());
    return null;
  }

  /* ************ EQUALS ************** */

  /**
   * Create a matcher that will match the given primitive <code>byte</code>
   * in the current default Moxy engine.
   * 
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @see #eqByte(MoxyEngine, byte)
   * @since 1.0
   */
  public static byte eqByte(byte value) {
    return eqByte(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match the given primitive <code>byte</code>
   * in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte eqByte(MoxyEngine engine, byte value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Byte>(value));
    return 0;
  }

  /**
   * Create a matcher that will match the given primitive <code>char</code>
   * in the current default Moxy engine.
   * 
   * @param value The value to match.
   * 
   * @return zero (NUL) (ignored).
   * @see #eqChar(MoxyEngine, char)
   * @since 1.0
   */
  public static char eqChar(char value) {
    return eqChar(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match the given primitive <code>char</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return zero (NUL) (ignored).
   * @since 1.0
   */
  public static char eqChar(MoxyEngine engine, char value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Character>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match the given primitive <code>short</code>
   * in the current default Moxy engine.
   * 
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @see #eqShort(MoxyEngine, short)
   * @since 1.0
   */
  public static short eqShort(short value) {
    return eqShort(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match the given primitive <code>short</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short eqShort(MoxyEngine engine, short value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Short>(value));
    return 0;
  }
    
  /**
   * Create a matcher that will match the given primitive <code>int</code>
   * in the current default Moxy engine.
   * 
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @see #eqInt(MoxyEngine, int)
   * @since 1.0
   */
  public static int eqInt(int value) {
    return eqInt(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match the given primitive <code>int</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int eqInt(MoxyEngine engine, int value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Integer>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match the given primitive <code>long</code>
   * in the current default Moxy engine.
   * 
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @see #eqLong(MoxyEngine, long)
   * @since 1.0
   */
  public static long eqLong(long value) {
    return eqLong(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match the given primitive <code>long</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long eqLong(MoxyEngine engine, long value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Long>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match the given primitive <code>float</code>
   * in the current default Moxy engine.
   * 
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @see #eqFloat(MoxyEngine, float)
   * @since 1.0
   */
  public static float eqFloat(float value) {
    return eqFloat(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match the given primitive <code>float</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float eqFloat(MoxyEngine engine, float value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Float>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match the given primitive <code>double</code>
   * in the current default Moxy engine.
   * 
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @see #eqDouble(MoxyEngine, double)
   * @since 1.0
   */
  public static double eqDouble(double value) {
    return eqDouble(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match the given primitive <code>double</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double eqDouble(MoxyEngine engine, double value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Double>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match the given primitive <code>boolean</code>
   * in the current default Moxy engine.
   * 
   * @param value The value to match.
   * 
   * @return false (ignored).
   * @see #eqBool(MoxyEngine, boolean)
   * @since 1.0
   */
  public static boolean eqBool(boolean value) {
    return eqBool(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match the given primitive <code>boolean</code>
   * in the specified {@link MoxyEngine}.
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return false (ignored).
   * @since 1.0
   */
  public static boolean eqBool(MoxyEngine engine, boolean value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<Boolean>(value));
    return false;
  }
  
  /**
   * <p>Create a matcher that will match the given <code>T</code>
   * in the current default Moxy engine.</p>
   * 
   * <p>For notes, see {@link #eqShort(MoxyEngine, short)}.</p>
   * 
   * @param value The value to match.
   * 
   * @return null (ignored).
   * @see #eq(MoxyEngine, Object)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on.
   */
  public static <T> T eq(T value) {
    return eq(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * <p>Create a matcher that will match the given <code>T</code>
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p>The general matching rules used by this matcher are:</p>
   * 
   * <ul>
   * <li>match: null == null</li>
   * <li>match: value.equals(actualArgument)</li>
   * </ul>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return null (ignored).
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T> T eq(MoxyEngine engine, T value) {
    engine.getMatcherEngine().registerMatcher(new EqualsMatcher<T>(value));
    return null;
  }
  
  /* ************ NOTEQUALS ************** */

  /**
   * Create a matcher that will match any primitive <code>byte</code> except 
   * the given value in the current default Moxy engine.
   * 
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @see #neqByte(MoxyEngine, byte)
   * @since 1.0
   */
  public static byte neqByte(byte value) {
    return neqByte(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>byte</code> except 
   * the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte neqByte(MoxyEngine engine, byte value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Byte>(value));
    return 0;
  }

  /**
   * Create a matcher that will match any primitive <code>char</code> except 
   * the given value in the current default Moxy engine.
   * 
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @see #neqChar(MoxyEngine, char)
   * @since 1.0
   */
  public static char neqChar(char value) {
    return neqChar(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>char</code> except 
   * the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static char neqChar(MoxyEngine engine, char value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Character>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code> except 
   * the given value in the current default Moxy engine.
   * 
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @see #neqShort(MoxyEngine, short)
   * @since 1.0
   */
  public static short neqShort(short value) {
    return neqShort(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code> except 
   * the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short neqShort(MoxyEngine engine, short value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Short>(value));
    return 0;
  }
    
  /**
   * Create a matcher that will match any primitive <code>int</code> except 
   * the given value in the current default Moxy engine.
   * 
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @see #neqInt(MoxyEngine, int)
   * @since 1.0
   */
  public static int neqInt(int value) {
    return neqInt(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>int</code> except 
   * the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int neqInt(MoxyEngine engine, int value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Integer>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code> except 
   * the given value in the current default Moxy engine.
   * 
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @see #neqLong(MoxyEngine, long)
   * @since 1.0
   */
  public static long neqLong(long value) {
    return neqLong(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code> except 
   * the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long neqLong(MoxyEngine engine, long value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Long>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code> except 
   * the given value in the current default Moxy engine.
   * 
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @see #neqFloat(MoxyEngine, float)
   * @since 1.0
   */
  public static float neqFloat(float value) {
    return neqFloat(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code> except 
   * the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float neqFloat(MoxyEngine engine, float value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Float>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code> except 
   * the given value in the current default Moxy engine.
   * 
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @see #neqDouble(MoxyEngine, double)
   * @since 1.0
   */
  public static double neqDouble(double value) {
    return neqDouble(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code> except 
   * the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to exclude from matching.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double neqDouble(MoxyEngine engine, double value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Double>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code> except 
   * the given value in the current default Moxy engine.
   * 
   * @param value The value to exclude from matching.
   * 
   * @return false (ignored).
   * @see #neqBool(MoxyEngine, boolean)
   * @since 1.0
   */
  public static boolean neqBool(boolean value) {
    return neqBool(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code> except 
   * the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to exclude from matching.
   * 
   * @return false (ignored).
   * @since 1.0
   */
  public static boolean neqBool(MoxyEngine engine, boolean value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<Boolean>(value));
    return false;
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> except 
   * the given value in the current default Moxy engine.</p>
   * 
   * <p>For notes, see {@link #neq(MoxyEngine, Object)}.</p>
   * 
   * @param value The Object to exclude from matching.
   * 
   * @return null (ignored).
   * @see #neq(MoxyEngine, Object)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on.
   */
  public static <T> T neq(T value) {
    return neq(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any <code>T</code> except 
   * the given value in the specified {@link MoxyEngine}.
   *
   * <p>The general matching rules used by this matcher are:</p>
   * 
   * <ul>
   * <li>match: ! (null == null)</li>
   * <li>match: ! (value.equals(actualArgument))</li>
   * </ul>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to match.
   * 
   * @return null (ignored).
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T> T neq(MoxyEngine engine, T value) {
    engine.getMatcherEngine().registerMatcher(new NotEqualsMatcher<T>(value));
    return null;
  }
  
  /* ************ LESSTHAN ************** */

  /**
   * Create a matcher that will match any primitive <code>byte</code> that 
   * is strictly less-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #ltByte(MoxyEngine, byte)
   * @since 1.0
   */
  public static byte ltByte(byte value) {
    return ltByte(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>byte</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte ltByte(MoxyEngine engine, byte value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Byte>(value));
    return 0;
  }

  /**
   * Create a matcher that will match any primitive <code>char</code> that 
   * is strictly less-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #ltChar(MoxyEngine, char)
   * @since 1.0
   */
  public static char ltChar(char value) {
    return ltChar(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>char</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static char ltChar(MoxyEngine engine, char value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Character>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>char</code> that 
   * is strictly less-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #ltChar(MoxyEngine, char)
   * @since 1.0
   */
  public static short ltShort(short value) {
    return ltShort(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short ltShort(MoxyEngine engine, short value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Short>(value));
    return 0;
  }
    
  /**
   * Create a matcher that will match any primitive <code>int</code> that 
   * is strictly less-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #ltInt(MoxyEngine, int)
   * @since 1.0
   */
  public static int ltInt(int value) {
    return ltInt(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>int</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int ltInt(MoxyEngine engine, int value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Integer>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code> that 
   * is strictly less-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #ltLong(MoxyEngine, long)
   * @since 1.0
   */
  public static long ltLong(long value) {
    return ltLong(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long ltLong(MoxyEngine engine, long value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Long>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code> that 
   * is strictly less-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #ltFloat(MoxyEngine, float)
   * @since 1.0
   */
  public static float ltFloat(float value) {
    return ltFloat(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float ltFloat(MoxyEngine engine, float value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Float>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code> that 
   * is strictly less-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #ltDouble(MoxyEngine, double)
   * @since 1.0
   */
  public static double ltDouble(double value) {
    return ltDouble(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double ltDouble(MoxyEngine engine, double value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Double>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code> that 
   * is strictly less-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return false (ignored).
   * @see #ltBool(MoxyEngine, boolean)
   * @since 1.0
   */
  public static boolean ltBool(boolean value) {
    return ltBool(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return false (ignored).
   * @since 1.0
   */
  public static boolean ltBool(MoxyEngine engine, boolean value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<Boolean>(value));
    return false;
  }
  
  /**
   * <p>Create a matcher that will match any Comparable <code>T</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.</p>
   * 
   * <p>For notes, see {@link #lt(Comparable)}.</p>
   * 
   * @param value The value to compare to.
   * 
   * @return null (ignored).
   * @see #lt(MoxyEngine, Comparable)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T extends Comparable<T>> T lt(T value) {
    return lt(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * <p>Create a matcher that will match any Comparable <code>T</code> that 
   * is strictly less-than the given value in the specified {@link MoxyEngine}.</p>
   * 
   * <p>This matcher requires that objects implement <code>java.lang.Comparable</code>
   * and it follows the standard comparison rules for that class.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return null (ignored).
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T extends Comparable<T>> T lt(MoxyEngine engine, T value) {
    engine.getMatcherEngine().registerMatcher(new LessThanMatcher<T>(value));
    return null;
  }
  
  /* ************ GREATERTHAN ************** */

  /**
   * Create a matcher that will match any primitive <code>byte</code> that 
   * is strictly greater-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #gtByte(MoxyEngine, byte)
   * @since 1.0
   */
  public static byte gtByte(byte value) {
    return gtByte(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>byte</code> that 
   * is strictly greater-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte gtByte(MoxyEngine engine, byte value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Byte>(value));
    return 0;
  }

  /**
   * Create a matcher that will match any primitive <code>char</code> that 
   * is strictly greater-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #gtChar(MoxyEngine, char)
   * @since 1.0
   */
  public static char gtChar(char value) {
    return gtChar(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>char</code> that 
   * is strictly greater-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static char gtChar(MoxyEngine engine, char value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Character>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code> that 
   * is strictly greater-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #gtShort(MoxyEngine, short)
   * @since 1.0
   */
  public static short gtShort(short value) {
    return gtShort(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code> that 
   * is strictly greater-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short gtShort(MoxyEngine engine, short value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Short>(value));
    return 0;
  }
    
  /**
   * Create a matcher that will match any primitive <code>int</code> that 
   * is strictly greater-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #gtInt(MoxyEngine, int)
   * @since 1.0
   */
  public static int gtInt(int value) {
    return gtInt(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>int</code> that 
   * is strictly greater-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int gtInt(MoxyEngine engine, int value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Integer>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code> that 
   * is strictly greater-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #gtLong(MoxyEngine, long)
   * @since 1.0
   */
  public static long gtLong(long value) {
    return gtLong(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code> that 
   * is strictly greater-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long gtLong(MoxyEngine engine, long value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Long>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code> that 
   * is strictly greater-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #gtFloat(MoxyEngine, float)
   * @since 1.0
   */
  public static float gtFloat(float value) {
    return gtFloat(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code> that 
   * is strictly greater-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float gtFloat(MoxyEngine engine, float value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Float>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code> that 
   * is strictly greater-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @see #gtDouble(MoxyEngine, double)
   * @since 1.0
   */
  public static double gtDouble(double value) {
    return gtDouble(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code> that 
   * is strictly greater-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double gtDouble(MoxyEngine engine, double value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Double>(value));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code> that 
   * is strictly greater-than the given value in the current default Moxy engine.
   * 
   * @param value The value to compare to.
   * 
   * @return false (ignored).
   * @see #gtBool(MoxyEngine, boolean)
   * @since 1.0
   */
  public static boolean gtBool(boolean value) {
    return gtBool(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code> that 
   * is strictly greater-than the given value in the specified {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return false (ignored).
   * @since 1.0
   */
  public static boolean gtBool(MoxyEngine engine, boolean value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<Boolean>(value));
    return false;
  }
  
  /**
   * <p>Create a matcher that will match any Comparable <code>T</code> that 
   * is strictly greater-than the given value in the current default Moxy engine.</p>
   * 
   * <p>For notes, see {@link #gt(Comparable)}.</p>
   * 
   * @param value The value to compare to.
   * 
   * @return null (ignored).
   * @see #gt(MoxyEngine, Comparable)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T extends Comparable<T>> T gt(T value) {
    return gt(Moxy.getMoxyEngine(), value);
  }
  
  /**
   * <p>Create a matcher that will match any Comparable <code>T</code> that 
   * is strictly greater-than the given value in the specified {@link MoxyEngine}.</p>
   * 
   * <p>This matcher requires that objects implement <code>java.lang.Comparable</code>
   * and it follows the standard comparison rules for that class.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param value The value to compare to.
   * 
   * @return null (ignored).
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T extends Comparable<T>> T gt(MoxyEngine engine, T value) {
    engine.getMatcherEngine().registerMatcher(new GreaterThanMatcher<T>(value));
    return null;
  }
  
  /* ************ ANYOF ************** */

  /**
   * Create a matcher that will match any primitive <code>byte</code> that 
   * appears in the supplied arguments. The matcher is created in the default
   * Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfByte(MoxyEngine, List)
   * @since 1.0
   */
  public static byte anyOfByte(Byte... possibilities) {
    return anyOfByte(Arrays.asList(possibilities));
  }
  
  /**
   * Create a matcher that will match any primitive <code>byte</code> that 
   * appears in the supplied <code>List</code>. The matcher is created in the
   * default Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfByte(MoxyEngine, List)
   * @since 1.0
   */
  public static byte anyOfByte(List<Byte> possibilities) {
    return anyOfByte(Moxy.getMoxyEngine(), possibilities);
  }
  
  /**
   * Create a matcher that will match any primitive <code>byte</code> that 
   * appears in the supplied list. The matcher is created in the specified
   * {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte anyOfByte(MoxyEngine engine, List<Byte> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Byte>(possibilities));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>char</code> that 
   * appears in the supplied arguments. The matcher is created in the default
   * Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfChar(MoxyEngine, List)
   * @since 1.0
   */
  public static char anyOfChar(Character... possibilities) {
    return anyOfChar(Arrays.asList(possibilities));
  }

  /**
   * Create a matcher that will match any primitive <code>char</code> that 
   * appears in the supplied <code>List</code>. The matcher is created in the
   * default Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfChar(MoxyEngine, List)
   * @since 1.0
   */
  public static char anyOfChar(List<Character> possibilities) {
    return anyOfChar(Moxy.getMoxyEngine(), possibilities);
  }
  
  /**
   * Create a matcher that will match any primitive <code>char</code> that 
   * appears in the supplied list. The matcher is created in the specified
   * {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static char anyOfChar(MoxyEngine engine, List<Character> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Character>(possibilities));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code> that 
   * appears in the supplied arguments. The matcher is created in the default
   * Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfShort(MoxyEngine, List)
   * @since 1.0
   */
  public static short anyOfShort(Short... possibilities) {
    return anyOfShort(Arrays.asList(possibilities));
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code> that 
   * appears in the supplied <code>List</code>. The matcher is created in the
   * default Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfShort(MoxyEngine, List)
   * @since 1.0
   */
  public static short anyOfShort(List<Short> possibilities) {
    return anyOfShort(Moxy.getMoxyEngine(), possibilities);
  }
  
  /**
   * Create a matcher that will match any primitive <code>short</code> that 
   * appears in the supplied list. The matcher is created in the specified
   * {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short anyOfShort(MoxyEngine engine, List<Short> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Short>(possibilities));
    return 0;
  }
    
  /**
   * Create a matcher that will match any primitive <code>int</code> that 
   * appears in the supplied arguments. The matcher is created in the default
   * Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfInt(MoxyEngine, List)
   * @since 1.0
   */
  public static int anyOfInt(Integer... possibilities) {
    return anyOfInt(Arrays.asList(possibilities));
  }
  
  /**
   * Create a matcher that will match any primitive <code>int</code> that 
   * appears in the supplied <code>List</code>. The matcher is created in the
   * default Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfInt(MoxyEngine, List)
   * @since 1.0
   */
  public static int anyOfInt(List<Integer> possibilities) {
    return anyOfInt(Moxy.getMoxyEngine(), possibilities);
  }
  
  /**
   * Create a matcher that will match any primitive <code>int</code> that 
   * appears in the supplied list. The matcher is created in the specified
   * {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int anyOfInt(MoxyEngine engine, List<Integer> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Integer>(possibilities));
    return 0;
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code> that 
   * appears in the supplied arguments. The matcher is created in the default
   * Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfLong(MoxyEngine, List)
   * @since 1.0
   */
  public static long anyOfLong(Long... possibilities) {
    return anyOfLong(Arrays.asList(possibilities));
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code> that 
   * appears in the supplied <code>List</code>. The matcher is created in the
   * default Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfLong(MoxyEngine, List)
   * @since 1.0
   */
  public static long anyOfLong(List<Long> possibilities) {
    return anyOfLong(Moxy.getMoxyEngine(), possibilities);
  }
  
  /**
   * Create a matcher that will match any primitive <code>long</code> that 
   * appears in the supplied list. The matcher is created in the specified
   * {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long anyOfLong(MoxyEngine engine, List<Long> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Long>(possibilities));
    return 0l;
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code> that 
   * appears in the supplied arguments. The matcher is created in the default
   * Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfFloat(MoxyEngine, List)
   * @since 1.0
   */
  public static float anyOfFloat(Float... possibilities) {
    return anyOfFloat(Arrays.asList(possibilities));
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code> that 
   * appears in the supplied <code>List</code>. The matcher is created in the
   * default Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfFloat(MoxyEngine, List)
   * @since 1.0
   */
  public static float anyOfFloat(List<Float> possibilities) {
    return anyOfFloat(Moxy.getMoxyEngine(), possibilities);
  }
  
  /**
   * Create a matcher that will match any primitive <code>float</code> that 
   * appears in the supplied list. The matcher is created in the specified
   * {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float anyOfFloat(MoxyEngine engine, List<Float> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Float>(possibilities));
    return 0.0f;
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code> that 
   * appears in the supplied arguments. The matcher is created in the default
   * Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfDouble(MoxyEngine, List)
   * @since 1.0
   */
  public static double anyOfDouble(Double... possibilities) {
    return anyOfDouble(Arrays.asList(possibilities));
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code> that 
   * appears in the supplied <code>List</code>. The matcher is created in the
   * default Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @see #anyOfDouble(MoxyEngine, List)
   * @since 1.0
   */
  public static double anyOfDouble(List<Double> possibilities) {
    return anyOfDouble(Moxy.getMoxyEngine(), possibilities);
  }
  
  /**
   * Create a matcher that will match any primitive <code>double</code> that 
   * appears in the supplied list. The matcher is created in the specified
   * {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param possibilities The potential values to match.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double anyOfDouble(MoxyEngine engine, List<Double> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Double>(possibilities));
    return 0.0d;
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code> that 
   * appears in the supplied arguments. The matcher is created in the default
   * Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return false (ignored).
   * @see #anyOfBool(MoxyEngine, List)
   * @since 1.0
   */
  public static boolean anyOfBool(Boolean... possibilities) {
    return anyOfBool(Arrays.asList(possibilities));
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code> that 
   * appears in the supplied <code>List</code>. The matcher is created in the
   * default Moxy engine.
   *
   * @param possibilities The potential values to match.
   * 
   * @return false (ignored).
   * @see #anyOfBool(MoxyEngine, List)
   * @since 1.0
   */
  public static boolean anyOfBool(List<Boolean> possibilities) {
    return anyOfBool(Moxy.getMoxyEngine(), possibilities);
  }
  
  /**
   * Create a matcher that will match any primitive <code>boolean</code> that 
   * appears in the supplied list. The matcher is created in the specified
   * {@link MoxyEngine}.
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param possibilities The potential values to match.
   * 
   * @return false (ignored).
   * @since 1.0
   */
  public static boolean anyOfBool(MoxyEngine engine, List<Boolean> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<Boolean>(possibilities));
    return false;
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> that appears in 
   * the supplied arguments. The matcher is created in the default
   * Moxy engine.</p>
   * 
   * <p>For notes, see {@link #anyOf(MoxyEngine, List)}.</p>
   *
   * @param possibilities The potential values to match.
   * 
   * @return null (ignored).
   * @see #anyOf(MoxyEngine, List)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  @SafeVarargs
  public static <T> T anyOf(T... possibilities) {
    return anyOf(Arrays.asList(possibilities));
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> that appears in 
   * the supplied <code>List</code>. The matcher is created in the default
   * Moxy engine.</p>
   * 
   * <p>For notes, see {@link #anyOf(MoxyEngine, List)}.</p>
   *
   * @param possibilities The potential values to match.
   * 
   * @return null (ignored).
   * @see #anyOf(MoxyEngine, List)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T> T anyOf(List<? extends T> possibilities) {
    return anyOf(Moxy.getMoxyEngine(), possibilities);
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> that appears in 
   * the supplied <code>List</code>. The matcher is created in the default
   * Moxy engine.</p>
   * 
   * <p>Matching is performed using {@link java.util.List#contains} on the
   * supplied <code>List</code>.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param possibilities The potential values to match.
   * 
   * @return null (ignored).
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T> T anyOf(MoxyEngine engine, List<? extends T> possibilities) {
    engine.getMatcherEngine().registerMatcher(new AnyOfMatcher<T>(possibilities));
    return null;
  }

  /* ************ AND ************** */

  /**
   * <p>Create a matcher that will match any primitive <code>byte</code> for which
   * the supplied matchers also match that <code>byte</code>. The matcher is created
   * in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andByte(gtByte(1), ltByte(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #andByte(MoxyEngine, Byte...)
   * @since 1.0
   */
  public static byte andByte(Byte... fromMatchers) {
    return andByte(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>byte</code> for which
   * the supplied matchers also match that <code>byte</code>. The matcher is created
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andByte(engine, gtByte(engine, 1), ltByte(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte andByte(MoxyEngine engine, Byte... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Byte>(fromMatchers));
    return 0;
  }

  /**
   * <p>Create a matcher that will match any primitive <code>char</code> for which
   * the supplied matchers also match that <code>char</code>. The matcher is created
   * in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andChar(gtChar(1), ltChar(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #andChar(Character...)
   * @since 1.0
   */
  public static char andChar(Character... fromMatchers) {
    return andChar(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>char</code> for which
   * the supplied matchers also match that <code>char</code>. The matcher is created
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andChar(engine, gtChar(engine, 1), ltChar(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static char andChar(MoxyEngine engine, Character... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Character>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>short</code> for which
   * the supplied matchers also match that <code>short</code>. The matcher is created
   * in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andShort(gtShort(1), ltShort(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #andShort(MoxyEngine, Short...)
   * @since 1.0
   */
  public static short andShort(Short... fromMatchers) {
    return andShort(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>short</code> for which
   * the supplied matchers also match that <code>short</code>. The matcher is created
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andShort(engine, gtShort(engine, 1), ltShort(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short andShort(MoxyEngine engine, Short... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Short>(fromMatchers));
    return 0;
  }
    
  /**
   * <p>Create a matcher that will match any primitive <code>int</code> for which
   * the supplied matchers also match that <code>int</code>. The matcher is created
   * in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andInt(engine, gtInt(engine, 1), ltInt(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #andInt(MoxyEngine, Integer...)
   * @since 1.0
   */
  public static int andInt(Integer... fromMatchers) {
    return andInt(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>int</code> for which
   * the supplied matchers also match that <code>int</code>. The matcher is created
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andInt(engine, gtInt(engine, 1), ltInt(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int andInt(MoxyEngine engine, Integer... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Integer>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>long</code> for which
   * the supplied matchers also match that <code>long</code>. The matcher is created
   * in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andLong(gtLong(1), ltLong(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #andLong(MoxyEngine, Long...)
   * @since 1.0
   */
  public static long andLong(Long... fromMatchers) {
    return andLong(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>long</code> for which
   * the supplied matchers also match that <code>long</code>. The matcher is created
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andLong(engine, gtLong(engine, 1), ltLong(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long andLong(MoxyEngine engine, Long... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Long>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>float</code> for which
   * the supplied matchers also match that <code>float</code>. The matcher is created
   * in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andFloat(gtFloat(1), ltFloat(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #andFloat(MoxyEngine, Float...)
   * @since 1.0
   */
  public static float andFloat(Float... fromMatchers) {
    return andFloat(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>float</code> for which
   * the supplied matchers also match that <code>float</code>. The matcher is created
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andFloat(engine, gtFloat(engine, 1), ltFloat(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float andFloat(MoxyEngine engine, Float... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Float>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>double</code> for which
   * the supplied matchers also match that <code>double</code>. The matcher is created
   * in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andDouble(gtDouble(1), ltDouble(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #andDouble(MoxyEngine, Double...)
   * @since 1.0
   */
  public static double andDouble(Double... fromMatchers) {
    return andDouble(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>double</code> for which
   * the supplied matchers also match that <code>double</code>. The matcher is created
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andDouble(engine, gtDouble(engine, 1), ltDouble(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double andDouble(MoxyEngine engine, Double... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Double>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>boolean</code> for which
   * the supplied matchers also match that <code>boolean</code>. The matcher is created
   * in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andBool(gtBool(1), ltBool(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return false (ignored).
   * @see #andBool(MoxyEngine, Boolean...)
   * @since 1.0
   */
  public static boolean andBool(Boolean... fromMatchers) {
    return andBool(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>boolean</code> for which
   * the supplied matchers also match that <code>boolean</code>. The matcher is created
   * in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(andBool(engine, gtBool(engine, 1), ltBool(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static boolean andBool(MoxyEngine engine, Boolean... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<Boolean>(fromMatchers));
    return false;
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> for which the supplied 
   * matchers also match that <code>byte</code>. The matcher is created in the 
   * default Moxy engine.</p>
   * 
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return null (ignored).
   * @see #and(MoxyEngine, Object...)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  @SafeVarargs
  public static <T> T and(T... fromMatchers) {
    return and(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> for which the supplied 
   * matchers also match that <code>byte</code>. The matcher is created in the 
   * specified {@link MoxyEngine}.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return null (ignored).
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  @SafeVarargs
  public static <T> T and(MoxyEngine engine, T... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new AndMatcher<T>(fromMatchers));
    return null;
  }

  /* ************ OR ************** */

  /**
   * <p>Create a matcher that will match any primitive <code>byte</code> for which
   * any of the supplied matchers also match that <code>byte</code>. The matcher 
   * is created in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orByte(eqByte(1), eqByte(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #orByte(MoxyEngine, Byte...)
   * @since 1.0
   */
  public static byte orByte(Byte... fromMatchers) {
    return orByte(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>byte</code> for which
   * any of the supplied matchers also match that <code>byte</code>. The matcher 
   * is created in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orByte(engine, eqByte(engine, 1), eqByte(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte orByte(MoxyEngine engine, Byte... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new OrMatcher<Byte>(fromMatchers));
    return 0;
  }

  /**
   * <p>Create a matcher that will match any primitive <code>char</code> for which
   * any of the supplied matchers also match that <code>char</code>. The matcher 
   * is created in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orChar(eqChar(1), eqChar(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #orChar(MoxyEngine, Character...)
   * @since 1.0
   */
  public static char orChar(Character... fromMatchers) {
    return orChar(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>char</code> for which
   * any of the supplied matchers also match that <code>char</code>. The matcher 
   * is created in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orChar(engine, eqChar(engine, 1), eqChar(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static char orChar(MoxyEngine engine, Character... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new OrMatcher<Character>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>short</code> for which
   * any of the supplied matchers also match that <code>short</code>. The matcher 
   * is created in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orShort(eqShort(1), eqShort(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #orShort(MoxyEngine, Short...)
   * @since 1.0
   */
  public static short orShort(Short... fromMatchers) {
    return orShort(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>short</code> for which
   * any of the supplied matchers also match that <code>short</code>. The matcher 
   * is created in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orShort(engine, eqShort(engine, 1), eqShort(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short orShort(MoxyEngine engine, Short... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new OrMatcher<Short>(fromMatchers));
    return 0;
  }
    
  /**
   * <p>Create a matcher that will match any primitive <code>int</code> for which
   * any of the supplied matchers also match that <code>int</code>. The matcher 
   * is created in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orInt(eqInt(1), eqInt(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #orInt(MoxyEngine, Integer...)
   * @since 1.0
   */
  public static int orInt(Integer... fromMatchers) {
    return orInt(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>int</code> for which
   * any of the supplied matchers also match that <code>int</code>. The matcher 
   * is created in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orInt(engine, eqInt(engine, 1), eqInt(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int orInt(MoxyEngine engine, Integer... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new OrMatcher<Integer>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>long</code> for which
   * any of the supplied matchers also match that <code>long</code>. The matcher 
   * is created in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orLong(eqLong(1), eqLong(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #orLong(MoxyEngine, Long...)
   * @since 1.0
   */
  public static long orLong(Long... fromMatchers) {
    return orLong(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>long</code> for which
   * any of the supplied matchers also match that <code>long</code>. The matcher 
   * is created in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orLong(engine, eqLong(engine, 1), eqLong(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long orLong(MoxyEngine engine, Long... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new OrMatcher<Long>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>float</code> for which
   * any of the supplied matchers also match that <code>float</code>. The matcher 
   * is created in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orFloat(eqFloat(1), eqFloat(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #orFloat(MoxyEngine, Float...)
   * @since 1.0
   */
  public static float orFloat(Float... fromMatchers) {
    return orFloat(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>float</code> for which
   * any of the supplied matchers also match that <code>float</code>. The matcher 
   * is created in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orFloat(engine, eqFloat(engine, 1), eqFloat(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float orFloat(MoxyEngine engine, Float... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new OrMatcher<Float>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>double</code> for which
   * any of the supplied matchers also match that <code>double</code>. The matcher 
   * is created in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orDouble(eqDouble(1), eqDouble(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @see #orDouble(MoxyEngine, Double...)
   * @since 1.0
   */
  public static double orDouble(Double... fromMatchers) {
    return orDouble(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>double</code> for which
   * any of the supplied matchers also match that <code>double</code>. The matcher 
   * is created in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orDouble(engine, eqDouble(engine, 1), eqDouble(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double orDouble(MoxyEngine engine, Double... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new OrMatcher<Double>(fromMatchers));
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>boolean</code> for which
   * any of the supplied matchers also match that <code>boolean</code>. The matcher 
   * is created in the default Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orBool(eqBool(1), eqBool(10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return false (ignored).
   * @see #orBool(MoxyEngine, Boolean...)
   * @since 1.0
   */
  public static boolean orBool(Boolean... fromMatchers) {
    return orBool(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>boolean</code> for which
   * any of the supplied matchers also match that <code>boolean</code>. The matcher 
   * is created in the specified {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> all nested matchers must also be supplied using
   * the appropriate primitive matcher type methods, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(orBool(engine, eqBool(engine, 1), eqBool(engine, 10))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers (or primitive variants), you may find it more efficient to use
   * {@link #anyOf(Object...)} instead.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return false (ignored).
   * @since 1.0
   */
  public static boolean orBool(MoxyEngine engine, Boolean... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new OrMatcher<Boolean>(fromMatchers));
    return false;
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> for which
   * any of the supplied matchers also match that <code>byte</code>. The matcher 
   * is created in the default Moxy engine.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers, you may find it more efficient to use {@link #anyOf(Object...)} 
   * instead.</p>
   *
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return null (ignored).
   * @see #or(MoxyEngine, Object...)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  @SafeVarargs
  public static <T> T or(T... fromMatchers) {
    return or(Moxy.getMoxyEngine(), fromMatchers);
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> for which
   * any of the supplied matchers also match that <code>byte</code>. The matcher 
   * is created in the supplied {@link MoxyEngine}.</p>
   * 
   * <p>If you find that you are using this matcher with all {@link #eq(Object)} 
   * matchers, you may find it more efficient to use {@link #anyOf(Object...)} 
   * instead.</p>
   *
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatchers Matcher method invocations that should be matched.
   * 
   * @return null (ignored).
   * @see #or(MoxyEngine, Object...)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  @SafeVarargs
  public static <T> T or(MoxyEngine engine, T... fromMatchers) {
    engine.getMatcherEngine().registerMatcher(new OrMatcher<T>(fromMatchers));
    return null;
  }

  /* ************ NOT ************** */

  /**
   * <p>Create a matcher that will match any primitive <code>byte</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the default 
   * Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notByte(eqByte(1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @see #notByte(MoxyEngine, byte)
   * @since 1.0
   */
  public static byte notByte(byte fromMatcher) {
    return notByte(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>byte</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the specified 
   * {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notByte(engine, eqByte(engine, 1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte notByte(MoxyEngine engine, byte fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Byte>());
    return 0;
  }

  /**
   * <p>Create a matcher that will match any primitive <code>char</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the default 
   * Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notChar(eqChar(1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @see #notChar(MoxyEngine, char)
   * @since 1.0
   */
  public static char notChar(char fromMatcher) {
    return notChar(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>char</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the specified 
   * {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notChar(engine, eqChar(engine, 1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static char notChar(MoxyEngine engine, char fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Character>());
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>short</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the default 
   * Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notShort(eqShort(1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @see #notShort(MoxyEngine, short)
   * @since 1.0
   */
  public static short notShort(short fromMatcher) {
    return notShort(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>short</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the specified 
   * {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notShort(engine, eqShort(engine, 1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short notShort(MoxyEngine engine, short fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Short>());
    return 0;
  }
    
  /**
   * <p>Create a matcher that will match any primitive <code>int</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the default 
   * Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notInt(eqInt(1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @see #notInt(MoxyEngine, int)
   * @since 1.0
   */
  public static int notInt(int fromMatcher) {
    return notInt(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>int</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the specified 
   * {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notInt(engine, eqInt(engine, 1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int notInt(MoxyEngine engine, int fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Integer>());
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>long</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the default 
   * Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notLong(eqLong(1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @see #notLong(MoxyEngine, long)
   * @since 1.0
   */
  public static long notLong(long fromMatcher) {
    return notLong(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>long</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the specified 
   * {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notLong(engine, eqLong(engine, 1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long notLong(MoxyEngine engine, long fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Long>());
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>float</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the default 
   * Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notFloat(eqFloat(1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @see #notFloat(MoxyEngine, float)
   * @since 1.0
   */
  public static float notFloat(float fromMatcher) {
    return notFloat(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>float</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the specified 
   * {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notFloat(engine, eqFloat(engine, 1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float notFloat(MoxyEngine engine, float fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Float>());
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>double</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the default 
   * Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notDouble(eqDouble(1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @see #notDouble(MoxyEngine, double)
   * @since 1.0
   */
  public static double notDouble(double fromMatcher) {
    return notDouble(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>double</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the specified 
   * {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notDouble(engine, eqDouble(engine, 1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double notDouble(MoxyEngine engine, double fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Double>());
    return 0;
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>boolean</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the default 
   * Moxy engine.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notBool(eqBool(1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return false (ignored).
   * @see #notBool(MoxyEngine, boolean)
   * @since 1.0
   */
  public static boolean notBool(boolean fromMatcher) {
    return notBool(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  /**
   * <p>Create a matcher that will match any primitive <code>boolean</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the specified 
   * {@link MoxyEngine}.</p>
   * 
   * <p><strong>Note:</strong> the nested matcher must also be supplied using
   * the appropriate primitive matcher type method, e.g.:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(notBool(engine, eqBool(engine, 1))));
   * </code></pre>
   * 
   * <p>Failure to adhere to this rule will cause {@link PossibleMatcherUsageError}
   * exceptions to be thrown.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return false (ignored).
   * @since 1.0
   */
  public static boolean notBool(MoxyEngine engine, boolean fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<Boolean>());
    return false;
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the default 
   * Moxy engine.</p>
   * 
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return null (ignored).
   * @see #not(MoxyEngine, Object)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T> T not(T fromMatcher) {
    return not(Moxy.getMoxyEngine(), fromMatcher);
  }
  
  /**
   * <p>Create a matcher that will match any <code>T</code> for which
   * the supplied matcher <strong>does not</strong> match. In other words, this
   * matcher negates its nested matcher. The matcher is created in the specified 
   * {@link MoxyEngine}.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param fromMatcher Matcher method invocation to negate.
   * 
   * @return zero (ignored).
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T> T not(MoxyEngine engine, T fromMatcher) {
    engine.getMatcherEngine().registerMatcher(new NotMatcher<T>());
    return null;
  }
  
  /* ************ CUSTOM ************** */

  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>byte</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customByte((arg) -&gt; arg &gt;= (byte)3))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @see #customByte(MoxyEngine, MoxyMatcher)
   * @since 1.0
   */
  public static byte customByte(MoxyMatcher<Byte> matcher) {
    return customByte(Moxy.getMoxyEngine(), matcher);
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>byte</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customByte(engine, (arg) -&gt; arg &gt;= (byte)3))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static byte customByte(MoxyEngine engine, MoxyMatcher<Byte> matcher) {
    engine.getMatcherEngine().registerMatcher(matcher);
    return 0;
  }

  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>char</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customChar((arg) -&gt; arg != 'z'))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @see #customChar(MoxyEngine, MoxyMatcher)
   * @since 1.0
   */
  public static char customChar(MoxyMatcher<Character> matcher) {
    return customChar(Moxy.getMoxyEngine(), matcher);
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>char</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customChar(engine, (arg) -&gt; arg != 'z'))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static char customChar(MoxyEngine engine, MoxyMatcher<Character> matcher) {
    engine.getMatcherEngine().registerMatcher(matcher);
    return 0;
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>short</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customShort((arg) -&gt; arg &gt;= (short)3))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @see #customShort(MoxyEngine, MoxyMatcher)
   * @since 1.0
   */
  public static short customShort(MoxyMatcher<Short> matcher) {
    return customShort(Moxy.getMoxyEngine(), matcher);
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>short</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customShort(engine, (arg) -&gt; arg &gt;= (short)3))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static short customShort(MoxyEngine engine, MoxyMatcher<Short> matcher) {
    engine.getMatcherEngine().registerMatcher(matcher);
    return 0;
  }
    
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>int</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customInt((arg) -&gt; arg &gt;= 3))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @see #customInt(MoxyEngine, MoxyMatcher)
   * @since 1.0
   */
  public static int customInt(MoxyMatcher<Integer> matcher) {
    return customInt(Moxy.getMoxyEngine(), matcher);
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>int</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customInt(engine, (arg) -&gt; arg &gt;= 3))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static int customInt(MoxyEngine engine, MoxyMatcher<Integer> matcher) {
    engine.getMatcherEngine().registerMatcher(matcher);
    return 0;
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>long</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customLong((arg) -&gt; arg &gt;= 3L))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @see #customLong(MoxyEngine, MoxyMatcher)
   * @since 1.0
   */
  public static long customLong(MoxyMatcher<Long> matcher) {
    return customLong(Moxy.getMoxyEngine(), matcher);
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>long</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customLong(engine, (arg) -&gt; arg &gt;= 3L))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static long customLong(MoxyEngine engine, MoxyMatcher<Long> matcher) {
    engine.getMatcherEngine().registerMatcher(matcher);
    return 0;
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>float</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customFloat((arg) -&gt; arg &gt;= 3.0f))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @see #customFloat(MoxyEngine, MoxyMatcher)
   * @since 1.0
   */
  public static float customFloat(MoxyMatcher<Float> matcher) {
    return customFloat(Moxy.getMoxyEngine(), matcher);
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>float</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customFloat(engine, (arg) -&gt; arg &gt;= 3.0f))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static float customFloat(MoxyEngine engine, MoxyMatcher<Float> matcher) {
    engine.getMatcherEngine().registerMatcher(matcher);
    return 0;
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>double</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customDouble((arg) -&gt; arg &gt;= 3.0d))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @see #customDouble(MoxyEngine, MoxyMatcher)
   * @since 1.0
   */
  public static double customDouble(MoxyMatcher<Double> matcher) {
    return customDouble(Moxy.getMoxyEngine(), matcher);
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>double</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customDouble(engine, (arg) -&gt; arg &gt;= 3.0d))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param matcher Custom matcher to add.
   * 
   * @return zero (ignored).
   * @since 1.0
   */
  public static double customDouble(MoxyEngine engine, MoxyMatcher<Double> matcher) {
    engine.getMatcherEngine().registerMatcher(matcher);
    return 0;
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>boolean</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customBool((arg) -&gt; arg == true))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param matcher Custom matcher to add.
   * 
   * @return false (ignored).
   * @see #customBool(MoxyEngine, MoxyMatcher)
   * @since 1.0
   */
  public static boolean customBool(MoxyMatcher<Boolean> matcher) {
    return customBool(Moxy.getMoxyEngine(), matcher);
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against primitive <code>boolean</code> arguments (using the primitive 
   * wrapper classes and autoboxing).</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(customBool(engine, (arg) -&gt; arg == true))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param matcher Custom matcher to add.
   * 
   * @return false (ignored).
   * @since 1.0
   */
  public static boolean customBool(MoxyEngine engine, MoxyMatcher<Boolean> matcher) {
    engine.getMatcherEngine().registerMatcher(matcher);
    return false;
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against <code>T</code> arguments.</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(custom((arg) -&gt; "ok".equals(arg)))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param matcher Custom matcher to add.
   * 
   * @return null (ignored).
   * @see #custom(MoxyEngine, MoxyMatcher)
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T> T custom(MoxyMatcher<T> matcher) {
    return custom(Moxy.getMoxyEngine(), matcher);
  }
  
  /**
   * <p>Allows addition of a custom {@link MoxyMatcher} that will match
   * against <code>T</code> arguments.</p>
   * 
   * <p>For simple cases, this method accepts a Java 1.8 <em>lambda expression</em>,
   * into which the argument to be matched is passed. For example:</p>
   * 
   * <pre><code>
   * when(() -&gt; mock.method(custom(engine, (arg) -&gt; "ok".equals(arg)))).thenReturn("passed");
   * </code></pre>
   * 
   * <p>In more complex cases, such as when custom stack manipulation is required,
   * you can pass in a custom implementation of {@link MoxyMatcher}. See
   * the documentation on that interface for more details.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param matcher Custom matcher to add.
   * 
   * @return null (ignored).
   * @since 1.0
   * 
   * @param <T> The type this matcher will operate on
   */
  public static <T> T custom(MoxyEngine engine, MoxyMatcher<T> matcher) {
    engine.getMatcherEngine().registerMatcher(matcher);
    return null;
  }
  
  /* ************ STRING - STARTSWITH ************** */

  /**
   * <p>Creates a matcher for <code>String</code> arguments that matches
   * if the argument starts with the specified <code>String</code>.
   * The matcher is created in the default Moxy engine.</p>
   * 
   * @param string The string that arguments must start with in order to match.
   * 
   * @return null (ignored).
   * @see #startsWith(MoxyEngine, String)
   * @since 1.0
   */
  public static String startsWith(String string) {
    return startsWith(Moxy.getMoxyEngine(), string);
  }
  
  /**
   * <p>Creates a matcher for <code>String</code> arguments that matches
   * if the argument starts with the specified <code>String</code>.
   * The matcher is created in the specified {@link MoxyEngine}.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param string The string that arguments must start with in order to match.
   * 
   * @return null (ignored).
   * @since 1.0
   */
  public static String startsWith(MoxyEngine engine, String string) {
    engine.getMatcherEngine().registerMatcher(new StartsWithMatcher(string));
    return null;
  }
  
  /* ************ STRING - ENDSSWITH ************** */

  /**
   * <p>Creates a matcher for <code>String</code> arguments that matches
   * if the argument ends with the specified <code>String</code>.
   * The matcher is created in the default Moxy engine.</p>
   * 
   * @param string The string that arguments must end with in order to match.
   * 
   * @return null (ignored).
   * @see #endsWith(MoxyEngine, String)
   * @since 1.0
   */
  public static String endsWith(String string) {
    return endsWith(Moxy.getMoxyEngine(), string);
  }
  
  /**
   * <p>Creates a matcher for <code>String</code> arguments that matches
   * if the argument ends with the specified <code>String</code>.
   * The matcher is created in the specified {@link MoxyEngine}.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param string The string that arguments must end with in order to match.
   * 
   * @return null (ignored).
   * @since 1.0
   */
  public static String endsWith(MoxyEngine engine, String string) {
    engine.getMatcherEngine().registerMatcher(new EndsWithMatcher(string));
    return null;
  }
  
  /* ************ STRING - REGEX ************** */

  /**
   * <p>Creates a matcher for <code>String</code> arguments that matches
   * if the argument matches the specified regular expression.
   * The matcher is created in the default Moxy engine.</p>
   * 
   * <p>The matcher will match if any part of the argument matches
   * the regular expression. If you wish to match the whole string, 
   * you must use the regular expression anchor characters <code>^</code>
   * and <code>$</code>.</p>
   * 
   * @param regex The string containing the regular expression that arguments must match in order to match.
   * 
   * @return null (ignored).
   * @see #regexMatch(MoxyEngine, String)
   * @since 1.0
   */
  public static String regexMatch(String regex) {
    return regexMatch(Moxy.getMoxyEngine(), regex);
  }
  
  /**
   * <p>Creates a matcher for <code>String</code> arguments that matches
   * if the argument matches the specified regular expression.
   * The matcher is created in the specified {@link MoxyEngine}.</p>
   * 
   * <p>The matcher will match if any part of the argument matches
   * the regular expression. If you wish to match the whole string, 
   * you must use the regular expression anchor characters <code>^</code>
   * and <code>$</code>.</p>
   * 
   * @param engine The {@link MoxyEngine} to which this matcher applies.
   * @param regex The string containing the regular expression that arguments must match in order to match.
   * 
   * @return null (ignored).
   * @see #regexMatch(MoxyEngine, String)
   * @since 1.0
   */
  public static String regexMatch(MoxyEngine engine, String regex) {
    engine.getMatcherEngine().registerMatcher(new RegexMatcher(regex));
    return null;
  }
}
