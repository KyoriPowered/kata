/*
 * This file is part of kata, licensed under the MIT License.
 *
 * Copyright (c) 2018 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.kyori.kata.argument.type.number;

import net.kyori.kata.argument.Argument;
import net.kyori.kata.argument.ArgumentType;
import net.kyori.kata.util.NumberType;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A float argument type.
 */
public final class FloatArgumentType {
  private static final ArgumentType<Float> ANY = new NumberArgumentType<>(NumberType.FLOAT);

  private FloatArgumentType() {
  }

  /**
   * Gets an argument type that parses any float.
   *
   * @return the argument type
   */
  public static @NonNull ArgumentType<Float> any() {
    return ANY;
  }

  /**
   * Creates an argument that parses any float.
   *
   * @param name the argument name
   * @return the argument
   */
  public static @NonNull Argument<Float> any(final @NonNull String name) {
    return any().create(name);
  }

  /**
   * Creates an argument type that parses a float that is greater than or equal to {@code min}.
   *
   * @param min the minimum value
   * @return the argument type
   */
  public static @NonNull ArgumentType<Float> min(final float min) {
    return between(min, Float.MAX_VALUE);
  }

  /**
   * Creates an argument that parses a float that is greater than or equal to {@code min}.
   *
   * @param name the argument name
   * @param min the minimum value
   * @return the argument
   */
  public static @NonNull Argument<Float> min(final @NonNull String name, final float min) {
    return min(min).create(name);
  }

  /**
   * Creates an argument type that parses a float that is less than or equal to {@code max}.
   *
   * @param max the maximum value
   * @return the argument type
   */
  public static @NonNull ArgumentType<Float> max(final float max) {
    return between(Float.MIN_VALUE, max);
  }

  /**
   * Creates an argument that parses a float that is less than or equal to {@code max}.
   *
   * @param name the argument name
   * @param max the maximum value
   * @return the argument
   */
  public static @NonNull Argument<Float> max(final @NonNull String name, final float max) {
    return max(max).create(name);
  }

  /**
   * Creates an argument type that parses a float that is between {@code min} and {@code max}.
   *
   * @param min the minimum value
   * @param max the maximum value
   * @return the argument type
   */
  public static @NonNull ArgumentType<Float> between(final float min, final float max) {
    return new NumberArgumentType.Range<>(NumberType.FLOAT, min, max);
  }

  /**
   * Creates an argument that parses a float that is between {@code min} and {@code max}.
   *
   * @param name the argument name
   * @param min the minimum value
   * @param max the maximum value
   * @return the argument
   */
  public static @NonNull Argument<Float> between(final @NonNull String name, final float min, final float max) {
    return between(min, max).create(name);
  }
}
