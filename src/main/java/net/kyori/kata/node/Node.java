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

import net.kyori.kata.argument.Argument;
import net.kyori.lambda.examine.Examinable;
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

/**
 * A node.
 */
public interface Node extends Examinable {
  /**
   * Creates an argument node.
   *
   * @param argument the argument type
   * @param <T> the type
   * @return an argument node
   */
  static <T> ArgumentNode.@NonNull Builder argument(final @NonNull Argument<T> argument) {
    return new ArgumentNodeImpl.Builder<>(argument);
  }

  /**
   * Creates a literal node.
   *
   * @param name the name
   * @return a literal node
   */
  static LiteralNode.@NonNull Builder literal(final @NonNull String name) {
    return new LiteralNodeImpl.Builder(name);
  }

  /**
   * Creates a root node.
   *
   * @return a root node
   */
  static @NonNull RootNode root() {
    return new RootNodeImpl();
  }

  /**
   * Gets the children of this node.
   *
   * @return the children
   */
  @NonNull Collection<? extends ChildNode> children();

  /**
   * Gets the relevant children of this node.
   *
   * @param reader the string reader
   * @return the children
   */
  @NonNull Collection<? extends ChildNode> relevantChildren(final @NonNull StringReader reader);

  /**
   * Adds a child to this node.
   *
   * @param node the child node builder
   * @return this node
   */
  default @NonNull Node add(final ChildNode.@NonNull Builder<?, ?> node) {
    return this.add(node.build());
  }

  /**
   * Adds a child to this node.
   *
   * @param node the child node
   * @return this node
   */
  @NonNull Node add(final @NonNull ChildNode node);

  /**
   * Removes a child.
   *
   * @param name the child name
   */
  void remove(final @NonNull String name);
}
