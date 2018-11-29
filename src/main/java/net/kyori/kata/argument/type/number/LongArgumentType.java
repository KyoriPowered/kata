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
 * A long argument type.
 */
public final class LongArgumentType {
  private static final ArgumentType<Long> ANY = new NumberArgumentType<>(NumberType.LONG);

  private LongArgumentType() {
  }

  /**
   * Gets an argument type that parses any long.
   *
   * @return the argument type
   */
  public static @NonNull ArgumentType<Long> any() {
    return ANY;
  }

  /**
   * Creates an argument that parses any long.
   *
   * @param name the argument name
   * @return the argument
   */
  public static @NonNull Argument<Long> any(final @NonNull String name) {
    return any().create(name);
  }

  /**
   * Creates an argument type that parses a long that is greater than or equal to {@code min}.
   *
   * @param min the minimum value
   * @return the argument type
   */
  public static @NonNull ArgumentType<Long> min(final long min) {
    return between(min, Long.MAX_VALUE);
  }

  /**
   * Creates an argument that parses a long that is greater than or equal to {@code min}.
   *
   * @param name the argument name
   * @param min the minimum value
   * @return the argument
   */
  public static @NonNull Argument<Long> min(final @NonNull String name, final long min) {
    return min(min).create(name);
  }

  /**
   * Creates an argument type that parses a long that is less than or equal to {@code max}.
   *
   * @param max the maximum value
   * @return the argument type
   */
  public static @NonNull ArgumentType<Long> max(final long max) {
    return between(Long.MIN_VALUE, max);
  }

  /**
   * Creates an argument that parses a long that is less than or equal to {@code max}.
   *
   * @param name the argument name
   * @param max the maximum value
   * @return the argument
   */
  public static @NonNull Argument<Long> max(final @NonNull String name, final long max) {
    return max(max).create(name);
  }

  /**
   * Creates an argument type that parses a long that is between {@code min} and {@code max}.
   *
   * @param min the minimum value
   * @param max the maximum value
   * @return the argument type
   */
  public static @NonNull ArgumentType<Long> between(final long min, final long max) {
    return new NumberArgumentType.Range<>(NumberType.LONG, min, max);
  }

  /**
   * Creates an argument that parses a long that is between {@code min} and {@code max}.
   *
   * @param name the argument name
   * @param min the minimum value
   * @param max the maximum value
   * @return the argument
   */
  public static @NonNull Argument<Long> between(final @NonNull String name, final long min, final long max) {
    return between(min, max).create(name);
  }
}
