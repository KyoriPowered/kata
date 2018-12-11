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

import net.kyori.kata.argument.Argument;
import net.kyori.string.StringRange;
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A command stack.
 */
public interface CommandStack {
  static @NonNull Builder builder(final @NonNull StringReader reader, final @NonNull CommandContext context) {
    return new CommandStackImpl.Builder(reader, context);
  }

  /**
   * Gets the context.
   *
   * @return the context
   */
  @NonNull CommandContext context();

  /**
   * Gets the arguments.
   *
   * @return the arguments
   */
  @NonNull CommandArguments arguments();

  /**
   * A command stack builder.
   */
  interface Builder {
    /**
     * Gets the literal range.
     *
     * @return the literal range
     */
    @NonNull StringRange literalRange();

    /**
     * Adds a literal to the stack.
     *
     * @param range the range
     * @return this builder
     */
    @NonNull Builder literal(final @NonNull StringRange range);

    /**
     * Adds an argument to the stack.
     *
     * @param argument the argument
     * @param range the range
     * @param value the value
     * @param <T> the value type
     * @return this builder
     */
    <T> @NonNull Builder argument(final @NonNull Argument<T> argument, final @NonNull StringRange range, final @NonNull T value);

    /**
     * Creates a copy.
     *
     * @return the copy
     */
    @NonNull Builder copy();

    /**
     * Creates a command stack.
     *
     * @return the command stack
     */
    @NonNull CommandStack build();
  }
}
