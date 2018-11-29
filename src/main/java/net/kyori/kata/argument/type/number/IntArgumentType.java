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
 * An integer argument type.
 */
public final class IntArgumentType {
  private static final ArgumentType<Integer> ANY = new NumberArgumentType<>(NumberType.INT);

  private IntArgumentType() {
  }

  /**
   * Gets an argument type that parses any integer.
   *
   * @return the argument type
   */
  public static @NonNull ArgumentType<Integer> any() {
    return ANY;
  }

  /**
   * Creates an argument that parses any short.
   *
   * @param name the argument name
   * @return the argument
   */
  public static @NonNull Argument<Integer> any(final @NonNull String name) {
    return any().create(name);
  }

  /**
   * Creates an argument type that parses an integer that is greater than or equal to {@code min}.
   *
   * @param min the minimum value
   * @return the argument type
   */
  public static @NonNull ArgumentType<Integer> min(final int min) {
    return between(min, Integer.MAX_VALUE);
  }

  /**
   * Creates an argument that parses an integer that is greater than or equal to {@code min}.
   *
   * @param name the argument name
   * @param min the minimum value
   * @return the argument
   */
  public static @NonNull Argument<Integer> min(final @NonNull String name, final int min) {
    return min(min).create(name);
  }

  /**
   * Creates an argument type that parses an integer that is less than or equal to {@code max}.
   *
   * @param max the maximum value
   * @return the argument type
   */
  public static @NonNull ArgumentType<Integer> max(final int max) {
    return between(Integer.MIN_VALUE, max);
  }

  /**
   * Creates an argument that parses an integer that is less than or equal to {@code max}.
   *
   * @param name the argument name
   * @param max the maximum value
   * @return the argument
   */
  public static @NonNull Argument<Integer> max(final @NonNull String name, final int max) {
    return max(max).create(name);
  }

  /**
   * Creates an argument type that parses an integer that is between {@code min} and {@code max}.
   *
   * @param min the minimum value
   * @param max the maximum value
   * @return the argument type
   */
  public static @NonNull ArgumentType<Integer> between(final int min, final int max) {
    return new NumberArgumentType.Range<>(NumberType.INT, min, max);
  }

  /**
   * Creates an argument that parses an integer that is between {@code min} and {@code max}.
   *
   * @param name the argument name
   * @param min the minimum value
   * @param max the maximum value
   * @return the argument
   */
  public static @NonNull Argument<Integer> between(final @NonNull String name, final int min, final int max) {
    return between(min, max).create(name);
  }
}
