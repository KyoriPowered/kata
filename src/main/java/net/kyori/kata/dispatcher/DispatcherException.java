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
package net.kyori.kata.dispatcher;

import net.kyori.kata.exception.ContextualCommandException;
import net.kyori.string.StringReaderGetter;

public class DispatcherException extends ContextualCommandException {
  public DispatcherException(final StringReaderGetter reader, final String message) {
    super(reader, message);
  }

  public DispatcherException(final StringReaderGetter reader, final String message, final Throwable cause) {
    super(reader, message, cause);
  }

  /**
   * An exception thrown when an unknown argument is encountered.
   */
  public static class UnknownArgument extends DispatcherException {
    public UnknownArgument(final StringReaderGetter reader) {
      super(reader, "Unknown argument");
    }
  }

  /**
   * An exception thrown when an unknown command is encountered.
   */
  public static class UnknownCommand extends DispatcherException {
    public UnknownCommand(final StringReaderGetter reader) {
      super(reader, "Unknown command");
    }
  }

  /**
   * An exception thrown when a node has not been completely parsed.
   */
  public static class IncompleteParse extends DispatcherException {
    public IncompleteParse(final StringReaderGetter reader) {
      super(reader, "Expected argument separator ('" + Dispatcher.ARGUMENT_SEPARATOR + "'), but found a node that has not been completely parse");
    }
  }
}
