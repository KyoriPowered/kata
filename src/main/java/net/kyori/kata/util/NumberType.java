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
package net.kyori.kata.util;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;
import java.util.function.IntPredicate;

/**
 * A number type.
 *
 * @param <N> the number type
 */
public final class NumberType<N extends Number> {
  private static final IntPredicate ALLOWED = character -> character >= '0' && character <= '9' || character == '+' || character == '-';
  private static final IntPredicate ALLOWED_FLOATING = character -> character >= '0' && character <= '9' || character == '+' || character == '-' || character == '.' || character == 'e' || character == 'E';

  public static final NumberType<Byte> BYTE = new NumberType<>("byte", ALLOWED, Byte::parseByte);
  public static final NumberType<Double> DOUBLE = new NumberType<>("double", ALLOWED_FLOATING, Double::parseDouble);
  public static final NumberType<Float> FLOAT = new NumberType<>("float", ALLOWED_FLOATING, Float::parseFloat);
  public static final NumberType<Integer> INT = new NumberType<>("integer", ALLOWED, Integer::parseInt);
  public static final NumberType<Long> LONG = new NumberType<>("long", ALLOWED, Long::parseLong);
  public static final NumberType<Short> SHORT = new NumberType<>("short", ALLOWED, Short::parseShort);

  private final String name;
  private final IntPredicate allowed;
  private final Function<String, N> parser;

  private NumberType(final String name, final IntPredicate allowed, final Function<String, N> parser) {
    this.name = name;
    this.allowed = allowed;
    this.parser = parser;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public @NonNull String name() {
    return this.name;
  }

  /**
   * Checks if {@code character} can be part of the number.
   *
   * @param character the character
   * @return {@code true} if {@code character} can be part of the number, {@code false} otherwise
   */
  public boolean allowed(final char character) {
    return this.allowed.test(character);
  }

  /**
   * Parses a number.
   *
   * @param string the string
   * @return the number
   * @throws NumberFormatException if the string could not be parsed into a number
   */
  public @NonNull N parse(final String string) throws NumberFormatException {
    return this.parser.apply(string);
  }
}
