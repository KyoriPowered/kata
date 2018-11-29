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
package net.kyori.kata.context;

import net.kyori.lambda.Maybe;
import net.kyori.lambda.examine.Examinable;
import net.kyori.lambda.examine.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A command context.
 */
public interface CommandContext extends Examinable {
  /**
   * Creates a command context builder.
   *
   * @return a command context builder
   */
  static @NonNull Builder builder() {
    return new CommandContextImpl.Builder();
  }

  /**
   * Gets an empty command context.
   *
   * @return an empty command context
   */
  static @NonNull CommandContext empty() {
    return CommandContextImpl.EMPTY;
  }

  /**
   * Creates a command context key.
   *
   * @param type the type class
   * @param name the name
   * @param <T> the type
   * @return a command context key
   */
  static <T> @NonNull Key<T> key(final @NonNull Class<T> type, final @NonNull String name) {
    return new Key<>(type, name);
  }

  /**
   * Gets the value associated with {@code key}.
   *
   * @param key the key
   * @param <T> the type
   * @return the value
   */
  <T> @NonNull Maybe<T> find(final @NonNull Key<T> key);

  /**
   * Gets the value associated with {@code key}.
   *
   * @param key the key
   * @param <T> the type
   * @return the value
   */
  <T> @Nullable T get(final @NonNull Key<T> key);

  /**
   * Gets the value associated with {@code key}.
   *
   * @param key the key
   * @param <T> the type
   * @return the value
   * @throws NoSuchElementException if the context does not have a value for {@code key}
   */
  <T> @NonNull T require(final @NonNull Key<T> key) throws NoSuchElementException;

  /**
   * A command context builder.
   */
  interface Builder {
    /**
     * Puts an entry into the context.
     *
     * @param key the key
     * @param value the value
     * @param <T> the type
     * @return this builder
     */
    <T> @NonNull Builder put(final @NonNull Key<T> key, final @NonNull T value);

    /**
     * Builds a command context.
     *
     * @return the command context
     */
    @NonNull CommandContext build();
  }

  /**
   * A context key.
   *
   * @param <T> the type
   */
  final class Key<T> implements Examinable {
    private final Class<T> type;
    private final String name;

    private Key(final Class<T> type, final String name) {
      this.type = type;
      this.name = name;
    }

    /**
     * Gets the type class.
     *
     * @return the type class
     */
    public @NonNull Class<T> type() {
      return this.type;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public @NonNull String name() {
      return this.name;
    }

    @Override
    public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(
        ExaminableProperty.of("type", this.type),
        ExaminableProperty.of("name", this.name)
      );
    }

    @Override
    public boolean equals(final Object other) {
      if(this == other) return true;
      if(other == null || this.getClass() != other.getClass()) return false;
      final Key<?> that = (Key<?>) other;
      return Objects.equals(this.type, that.type) && Objects.equals(this.name, that.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.type, this.name);
    }
  }
}
