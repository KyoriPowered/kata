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
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * A child node.
 */
public interface ChildNode extends Node {
  /**
   * Gets the name.
   *
   * @return the name
   */
  @NonNull String name();

  @Override
  @NonNull ChildNode add(final @NonNull ChildNode node);

  /**
   * Checks if this node can be used.
   *
   * @param context the context
   * @return {@code true} if this node can be used, {@code false} otherwise
   */
  boolean canUse(final @NonNull CommandContext context);

  /**
   * Gets the usage.
   *
   * @return the usage
   */
  @NonNull String usage();

  /**
   * Parses.
   *
   * @param stack the stack builder
   * @param context the context
   * @param reader the string reader
   * @throws CommandException if an exception was encountered while parsing
   * @return {@code true} if parsing was successful
   */
  boolean parse(final CommandStack.@NonNull Builder stack, final @NonNull CommandContext context, final @NonNull StringReader reader) throws CommandException;

  /**
   * A child node builder.
   */
  interface Builder<N extends ChildNode, B extends Builder<N, B>> {
    /**
     * Sets the requirement.
     *
     * @param requirement the requirement
     * @return this builder
     */
    @NonNull B requires(final @NonNull Predicate<CommandContext> requirement);

    /**
     * Adds a child to this node builder.
     *
     * @param node the child node builder
     * @return this builder
     */
    default @NonNull B then(final @NonNull Builder<?, ?> node) {
      return this.then(node, null);
    }

    /**
     * Adds a child to this node builder.
     *
     * @param node the child node builder
     * @param consumer a consumer that consumes this builder and {@code node}
     * @return this builder
     */
    default @NonNull B then(final @NonNull Builder<?, ?> node, final @Nullable BiConsumer<B, ChildNode> consumer) {
      return this.then(node.build(), consumer);
    }

    /**
     * Adds a child to this node builder.
     *
     * @param node the child node
     * @return this builder
     */
    default @NonNull B then(final @NonNull ChildNode node) {
      return this.then(node, null);
    }

    /**
     * Adds a child to this node builder.
     *
     * @param node the child node
     * @param consumer a consumer that consumes this builder and {@code node}
     * @return this builder
     */
    @NonNull B then(final @NonNull ChildNode node, final @Nullable BiConsumer<B, ChildNode> consumer);

    /**
     * Builds the node.
     *
     * @return the built node
     */
    @NonNull N build();
  }
}
