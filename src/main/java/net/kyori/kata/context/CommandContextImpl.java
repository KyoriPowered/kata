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

import com.google.common.collect.ImmutableMap;
import net.kyori.lambda.Maybe;
import net.kyori.lambda.examine.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

final class CommandContextImpl implements CommandContext {
  static final CommandContext EMPTY = new CommandContextImpl(Collections.emptyMap());
  private final Map<Key<?>, Object> context;

  private CommandContextImpl(final Map<Key<?>, Object> context) {
    this.context = context;
  }

  @Override
  public @NonNull <T> Maybe<T> find(final @NonNull Key<T> key) {
    final @Nullable Object value = this.context.get(key);
    if(value == null) {
      return Maybe.nothing();
    }
    return Maybe.just(key.type().cast(value));
  }

  @Override
  public <T> @Nullable T get(final @NonNull Key<T> key) {
    return key.type().cast(this.context.get(key));
  }

  @Override
  public <T> @NonNull T require(final @NonNull Key<T> key) throws NoSuchElementException {
    final @Nullable Object value = this.context.get(key);
    if(value == null) {
      throw new NoSuchElementException();
    }
    return key.type().cast(value);
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("context", this.context));
  }

  static final class Builder implements CommandContext.Builder {
    private final ImmutableMap.Builder<Key<?>, Object> context = ImmutableMap.builder();

    Builder() {
    }

    @Override
    public <T> @NonNull Builder put(final @NonNull Key<T> key, final @NonNull T value) {
      this.context.put(key, value);
      return this;
    }

    @Override
    public @NonNull CommandContext build() {
      return new CommandContextImpl(this.context.build());
    }
  }
}
