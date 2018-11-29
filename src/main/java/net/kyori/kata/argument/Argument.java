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
package net.kyori.kata.argument;

import net.kyori.lambda.examine.Examinable;
import net.kyori.lambda.examine.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * An argument.
 *
 * @param <T> the argument type
 */
public final class Argument<T> implements Examinable {
  private final String name;
  private final ArgumentType<T> type;

  /**
   * Creates an argument.
   *
   * @param <T> the type
   * @param name the name
   * @param type the type
   * @return an argument
   */
  public static <T> @NonNull Argument<T> of(final @NonNull String name, final @NonNull ArgumentType<T> type) {
    return new Argument<>(name, type);
  }

  private Argument(final String name, final ArgumentType<T> type) {
    this.name = name;
    this.type = type;
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
   * Gets the type.
   *
   * @return the type
   */
  public @NonNull ArgumentType<T> type() {
    return this.type;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(
      ExaminableProperty.of("name", this.name),
      ExaminableProperty.of("type", this.type)
    );
  }

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;
    if(other == null || this.getClass() != other.getClass()) return false;
    final Argument<?> that = (Argument<?>) other;
    return Objects.equals(this.name, that.name) && Objects.equals(this.type, that.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.type);
  }
}
