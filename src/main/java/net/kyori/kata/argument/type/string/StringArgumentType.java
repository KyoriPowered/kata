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
package net.kyori.kata.argument.type.string;

import net.kyori.kata.argument.Argument;
import net.kyori.kata.argument.ArgumentType;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A string argument type.
 */
public final class StringArgumentType {
  private StringArgumentType() {
  }

  /**
   * Gets an argument type that parses the remaining string.
   *
   * @return the argument type
   */
  public static @NonNull ArgumentType<String> greedy() {
    return GreedyStringArgumentType.INSTANCE;
  }

  /**
   * Creates an argument that parses the remaining string.
   *
   * @param name the argument name
   * @return the argument
   */
  public static @NonNull Argument<String> greedy(final @NonNull String name) {
    return greedy().create(name);
  }

  /**
   * Gets an argument type that parses a quoted string.
   *
   * @return the argument type
   */
  public static @NonNull ArgumentType<String> quoted() {
    return QuotedStringArgumentType.INSTANCE;
  }

  /**
   * Creates an argument that parses a quoted string.
   *
   * @param name the argument name
   * @return the argument
   */
  public static @NonNull Argument<String> quoted(final @NonNull String name) {
    return quoted().create(name);
  }

  /**
   * Gets an argument type that returns a single word.
   *
   * @return the argument type
   */
  public static @NonNull ArgumentType<String> word() {
    return SingleWordStringArgumentType.INSTANCE;
  }

  /**
   * Creates an argument that parses a single word.
   *
   * @param name the argument name
   * @return the argument
   */
  public static @NonNull Argument<String> word(final @NonNull String name) {
    return word().create(name);
  }
}
