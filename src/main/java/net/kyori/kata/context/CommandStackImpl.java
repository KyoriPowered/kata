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
import net.kyori.kata.argument.Argument;
import net.kyori.lambda.Maybe;
import net.kyori.string.StringRange;
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final class CommandStackImpl implements CommandStack {
  private final CommandContext context;
  private final CommandArguments arguments;

  private CommandStackImpl(final CommandContext context, final CommandArguments arguments) {
    this.context = context;
    this.arguments = arguments;
  }

  @Override
  public @NonNull CommandContext context() {
    return this.context;
  }

  @Override
  public @NonNull CommandArguments arguments() {
    return this.arguments;
  }

  static class CommandArgumentsImpl implements CommandArguments {
    private final Map<Argument<?>, ParsedArgument<?>> arguments;

    CommandArgumentsImpl(final Map<Argument<?>, ParsedArgument<?>> arguments) {
      this.arguments = arguments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NonNull <V> Maybe<V> find(final @NonNull Argument<V> key) {
      final ParsedArgument<?> argument = this.arguments.get(key);
      if(argument != null) {
        return Maybe.just((V) argument.result);
      }
      return Maybe.nothing();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> @NonNull V require(final @NonNull Argument<V> key) {
      final ParsedArgument<?> argument = this.arguments.get(key);
      if(argument != null) {
        return (V) argument.result;
      }
      throw new IllegalArgumentException("No such argument '" + key + "' exists on this command");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> @NonNull V get(final @NonNull Argument<V> key, @NonNull final V defaultValue) {
      final ParsedArgument<?> argument = this.arguments.get(key);
      if(argument != null) {
        return (V) argument.result;
      }
      return defaultValue;
    }
  }

  static final class Builder implements CommandStack.Builder {
    final Map<Argument<?>, ParsedArgument<?>> arguments = new HashMap<>();
    final CommandContext context;
    StringRange literalRange;

    Builder(final @NonNull StringReader reader, final @NonNull CommandContext context) {
      this.context = context;
      this.literalRange = StringRange.between(reader.index(), reader.index());
    }

    Builder(final @NonNull CommandContext context) {
      this.context = context;
    }

    @Override
    public @NonNull StringRange literalRange() {
      return this.literalRange;
    }

    @Override
    public @NonNull Builder literal(final @NonNull StringRange range) {
      this.literalRange = this.literalRange.expand(range);
      return this;
    }

    @Override
    public <T> @NonNull Builder argument(final @NonNull Argument<T> argument, final @NonNull StringRange range, final @NonNull T value) {
      this.arguments.put(argument, new ParsedArgument<>(range, value));
      return this;
    }

    @Override
    public @NonNull Builder copy() {
      final Builder that = new Builder(this.context);
      that.arguments.putAll(this.arguments);
      that.literalRange = this.literalRange;
      return that;
    }

    @Override
    public @NonNull CommandStack build() {
      return new CommandStackImpl(this.context, new CommandArgumentsImpl(ImmutableMap.copyOf(this.arguments)));
    }
  }

  static class ParsedArgument<T> {
    private final StringRange range;
    private final T result;

    ParsedArgument(final StringRange range, final T result) {
      this.range = range;
      this.result = result;
    }

    @Override
    public boolean equals(final Object o) {
      if(this == o) {
        return true;
      }
      if(!(o instanceof ParsedArgument)) {
        return false;
      }
      final ParsedArgument<?> that = (ParsedArgument<?>) o;
      return Objects.equals(this.range, that.range) && Objects.equals(this.result, that.result);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.range, this.result);
    }
  }
}
