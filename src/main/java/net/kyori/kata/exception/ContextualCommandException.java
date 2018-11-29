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
package net.kyori.kata.exception;

import net.kyori.string.StringReaderGetter;

public class ContextualCommandException extends CommandException {
  private final String string;
  private final int index;

  public ContextualCommandException(final StringReaderGetter reader, final String message) {
    super(message);
    this.string = reader.asString();
    this.index = reader.index();
  }

  public ContextualCommandException(final StringReaderGetter reader, final String message, final Throwable cause) {
    super(message, cause);
    this.string = reader.asString();
    this.index = reader.index();
  }

  /**
   * Gets the exception message.
   *
   * <p>{@link #getMessage()} is overridden to provide context in addition to the message.</p>
   *
   * @return the exception message
   */
  public String message() {
    return super.getMessage();
  }

  /**
   * Gets the argument string.
   *
   * @return the argument string
   */
  public String string() {
    return this.string;
  }

  /**
   * Gets the index in the argument string where this exception occurred.
   *
   * @return the index in the argument string where this exception occurred
   */
  public int index() {
    return this.index;
  }

  @Override
  public String getMessage() {
    final int index = Math.min(this.string.length(), this.index);
    return this.message() + " at position " + this.index + ": '" + this.string.substring(0, index) + "'";
  }
}
