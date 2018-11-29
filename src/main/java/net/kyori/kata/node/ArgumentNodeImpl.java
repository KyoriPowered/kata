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
package net.kyori.kata.node;

import net.kyori.kata.Usage;
import net.kyori.kata.argument.Argument;
import net.kyori.kata.context.CommandContext;
import net.kyori.kata.context.CommandStack;
import net.kyori.kata.exception.CommandException;
import net.kyori.lambda.examine.ExaminableProperty;
import net.kyori.string.StringRange;
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.stream.Stream;

final class ArgumentNodeImpl<T> extends ChildNodeImpl<ArgumentNode> implements ArgumentNode {
  private final Argument<T> argument;

  private ArgumentNodeImpl(final Builder<T> builder) {
    super(builder);
    this.argument = builder.argument;
  }

  @Override
  public @NonNull String name() {
    return this.argument.name();
  }

  @Override
  public @NonNull String usage() {
    return Usage.ARGUMENT_OPEN + this.argument.name() + Usage.ARGUMENT_CLOSE;
  }

  @Override
  public boolean parse(final CommandStack.@NonNull Builder stack, final @NonNull CommandContext context, final @NonNull StringReader reader) throws CommandException {
    final int start = reader.index();
    final T result = this.argument.type().parse(context, reader);
    final int end = reader.index();
    stack.argument(this.argument, StringRange.between(start, end), result);
    return true;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(
        ExaminableProperty.of("name", this.argument.name()),
        ExaminableProperty.of("type", this.argument.type())
      ),
      super.examinableProperties()
    );
  }

  static final class Builder<T> extends ChildNodeImpl.Builder<ArgumentNode, ArgumentNode.Builder> implements ArgumentNode.Builder {
    private final Argument<T> argument;

    Builder(final @NonNull Argument<T> argument) {
      this.argument = argument;
    }

    @Override
    @NonNull ArgumentNode create() {
      return new ArgumentNodeImpl<>(this);
    }
  }
}
