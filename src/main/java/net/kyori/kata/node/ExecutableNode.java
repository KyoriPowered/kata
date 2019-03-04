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

import net.kyori.kata.context.CommandStack;
import net.kyori.kata.exception.CommandException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A executable node.
 */
public interface ExecutableNode extends ChildNode {
  @Override
  @NonNull ExecutableNode add(final @NonNull ChildNode node);

  /**
   * Gets the redirect.
   *
   * @return the redirect
   */
  @Nullable ExecutableNode redirect();

  /**
   * Gets the executable.
   *
   * @return the executable
   */
  @Nullable Executable executable();

  /**
   * An executable node builder.
   */
  interface Builder<N extends ExecutableNode, B extends Builder<N, B>> extends ChildNode.Builder<N, B> {
    /**
     * Sets the executable.
     *
     * @param executable the executable
     * @return this builder
     */
    @NonNull B executes(final @NonNull Executable executable);

    /**
     * Sets the redirect.
     *
     * @param target the target node
     * @return this builder
     */
    @NonNull B redirect(final @NonNull ExecutableNode target);
  }

  /**
   * An executable.
   */
  @FunctionalInterface
  interface Executable {
    /**
     * Executes.
     *
     * @param stack the stack
     * @throws CommandException if an exception is encountered during execution
     */
    void execute(final @NonNull CommandStack stack) throws CommandException;
  }
}
