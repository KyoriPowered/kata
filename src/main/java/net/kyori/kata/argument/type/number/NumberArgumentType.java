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

import net.kyori.kata.argument.ArgumentType;
import net.kyori.kata.argument.exception.NumberArgumentException;
import net.kyori.kata.context.CommandContext;
import net.kyori.kata.exception.CommandException;
import net.kyori.kata.util.NumberType;
import net.kyori.lambda.Comparables;
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;

public class NumberArgumentType<N extends Number & Comparable<N>> implements ArgumentType<N> {
  protected final NumberType<N> type;

  NumberArgumentType(final @NonNull NumberType<N> type) {
    this.type = type;
  }

  @Override
  public @NonNull N parse(final @NonNull CommandContext context, final @NonNull StringReader reader) throws CommandException {
    final int start = reader.index();
    while(reader.readable() && this.type.allowed(reader.peek())) {
      reader.skip();
    }
    final String string = reader.string(start, reader.index());
    if(string.isEmpty()) {
      throw new NumberArgumentException.Expected(this.type, reader);
    }
    return this.parse(reader, string, start);
  }

  protected @NonNull N parse(final @NonNull StringReader reader, final @NonNull String string, final int start) throws CommandException, NumberFormatException {
    try {
      return this.type.parse(string);
    } catch(final NumberFormatException e) {
      reader.index(start);
      throw new NumberArgumentException.Invalid(this.type, reader, string, e);
    }
  }

  public static class Range<N extends Number & Comparable<N>> extends NumberArgumentType<N> {
    private final N min;
    private final N max;

    Range(final @NonNull NumberType<N> type, final @NonNull N min, final @NonNull N max) {
      super(type);
      this.min = min;
      this.max = max;
    }

    @Override
    protected @NonNull N parse(final @NonNull StringReader reader, final @NonNull String string, final int start) throws CommandException, NumberFormatException {
      final N value = super.parse(reader, string, start);
      if(Comparables.lessThan(value, this.min)) {
        reader.index(start);
        throw new NumberArgumentException.TooLow(this.type, reader, this.min, value);
      } else if(Comparables.greaterThan(value, this.max)) {
        reader.index(start);
        throw new NumberArgumentException.TooHigh(this.type, reader, this.max, value);
      }
      return value;
    }
  }
}
