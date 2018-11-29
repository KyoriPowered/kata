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
package net.kyori.kata.argument.exception;

import net.kyori.kata.util.NumberType;
import net.kyori.string.StringReaderGetter;
import org.checkerframework.checker.nullness.qual.NonNull;

public class NumberArgumentException extends ArgumentParseException {
  private final NumberType<?> type;

  public NumberArgumentException(final NumberType<?> type, final StringReaderGetter reader, final String message) {
    super(reader, message);
    this.type = type;
  }

  public NumberArgumentException(final NumberType<?> type, final StringReaderGetter reader, final String message, final Throwable cause) {
    super(reader, message, cause);
    this.type = type;
  }

  public @NonNull NumberType<?> type() {
    return this.type;
  }

  public static class Expected extends NumberArgumentException {
    public Expected(final NumberType<?> type, final StringReaderGetter reader) {
      super(type, reader, "Expected " + type.name());
    }
  }

  public static class Invalid extends NumberArgumentException {
    public Invalid(final NumberType<?> type, final StringReaderGetter reader, final String string, final Throwable cause) {
      super(type, reader, "Invalid " + type.name() + " '" + string + "'", cause);
    }
  }

  public static class TooLow extends NumberArgumentException {
    public <N extends Number> TooLow(final NumberType<N> type, final StringReaderGetter reader, final N min, final N value) {
      super(type, reader, "Found " + type.name() + " with value of " + value + ", expected a value of " + min + " or greater");
    }
  }

  public static class TooHigh extends NumberArgumentException {
    public <N extends Number> TooHigh(final NumberType<N> type, final StringReaderGetter reader, final N max, final N value) {
      super(type, reader, "Found " + type.name() + " with value of " + value + ", expected a value of " + max + " or less");
    }
  }
}
