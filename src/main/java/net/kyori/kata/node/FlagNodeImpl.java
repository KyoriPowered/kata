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

import net.kyori.kata.context.CommandContext;
import net.kyori.kata.context.CommandStack;
import net.kyori.kata.exception.CommandException;
import net.kyori.lambda.examine.ExaminableProperty;
import net.kyori.string.StringRange;
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.stream.Stream;

final class FlagNodeImpl extends ChildNodeImpl<FlagNode> implements FlagNode {
  private final char flag;

  private FlagNodeImpl(final Builder builder) {
    super(builder);
    this.flag = builder.flag;
  }

  @Override
  public @NonNull String name() {
    return "-" + this.flag;
  }

  @Override
  public @NonNull String usage() {
    return "-" + this.flag;
  }

  @Override
  public boolean parse(final CommandStack.@NonNull Builder stack, final @NonNull CommandContext context, final @NonNull StringReader reader) throws CommandException {
    final String name = this.name();
    if(reader.readable(name.length())) {
      final int start = reader.index();
      final int end = start + name.length();
      if(reader.string(StringRange.between(start, end)).equals(name)) {
        reader.skip(end - start);
        stack.flag(StringRange.between(start, end), this.flag);
        return true;
      }
    }
    return false;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(ExaminableProperty.of("flag", this.flag)),
      super.examinableProperties()
    );
  }

  static final class Builder extends ChildNodeImpl.Builder<FlagNode, FlagNode.Builder> implements FlagNode.Builder {
    private final char flag;

    Builder(final char flag) {
      this.flag = flag;
    }

    @Override
    @NonNull FlagNode create() {
      return new FlagNodeImpl(this);
    }
  }
}
