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

import net.kyori.string.StringReaderGetter;

/**
 * An exception thrown when a string argument could not be parsed.
 */
public abstract class StringArgumentException extends ArgumentParseException {
  protected StringArgumentException(final StringReaderGetter reader, final String message) {
    super(reader, message);
  }

  /**
   * An exception thrown when an end-of-quote character was expected while parsing a string argument.
   */
  public static final class ExpectedEndOfQuote extends StringArgumentException {
    public ExpectedEndOfQuote(final StringReaderGetter reader) {
      super(reader, "Expected quote to end a string");
    }
  }

  /**
   * An exception thrown when a start-of-quote character was expected while parsing a string argument.
   */
  public static final class ExpectedStartOfQuote extends StringArgumentException {
    public ExpectedStartOfQuote(final StringReaderGetter reader) {
      super(reader, "Expected quote to start a string");
    }
  }

  /**
   * An exception thrown when an invalid escape is encountered while parsing a string argument.
   */
  public static final class InvalidEscapeSequence extends StringArgumentException {
    private final char escape;

    public InvalidEscapeSequence(final StringReaderGetter reader, final char escape) {
      super(reader, "Invalid escape sequence '" + escape + "'");
      this.escape = escape;
    }

    public char escape() {
      return this.escape;
    }
  }
}
