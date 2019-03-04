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

import net.kyori.kata.context.CommandContext;
import net.kyori.kata.context.CommandStack;
import net.kyori.kata.exception.CommandException;
import net.kyori.kata.node.ChildNode;
import net.kyori.kata.node.ExecutableNode;
import net.kyori.kata.node.LiteralNode;
import net.kyori.kata.node.Node;
import net.kyori.kata.node.RootNode;
import net.kyori.lambda.examine.Examinable;
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A command dispatcher.
 */
public interface Dispatcher extends Examinable {
  char ARGUMENT_SEPARATOR = ' ';

  /**
   * Creates a dispatcher.
   *
   * @return a dispatcher
   */
  static @NonNull Dispatcher create() {
    return new DispatcherImpl();
  }

  /**
   * Gets the root node.
   *
   * @return the root node
   */
  @NonNull RootNode root();

  /**
   * Registers a node.
   *
   * @param node the node builder
   * @return this dispatcher
   */
  default @NonNull Dispatcher register(final LiteralNode.@NonNull Builder node) {
    return this.register(node.build());
  }

  /**
   * Registers a node.
   *
   * @param node the node
   * @return this dispatcher
   */
  @NonNull Dispatcher register(final @NonNull LiteralNode node);

  /**
   * Registers a node.
   *
   * @param node the node builder
   * @param consumer a consumer that consumes {@code node}
   * @return this dispatcher
   */
  default @NonNull Dispatcher register(final LiteralNode.@NonNull Builder node, final @Nullable Consumer<LiteralNode> consumer) {
    return this.register(node.build(), consumer);
  }

  /**
   * Registers a node.
   *
   * @param node the node
   * @param consumer a consumer that consumes {@code node}
   * @return this dispatcher
   */
  @NonNull Dispatcher register(final @NonNull LiteralNode node, final @Nullable Consumer<LiteralNode> consumer);

  /**
   * Registers a node.
   *
   * @param node the node builder
   * @param consumer a consumer that consumes this dispatcher and {@code node}
   * @return this dispatcher
   */
  default @NonNull Dispatcher register(final LiteralNode.@NonNull Builder node, final @Nullable BiConsumer<Dispatcher, LiteralNode> consumer) {
    return this.register(node.build(), consumer);
  }

  /**
   * Registers a node.
   *
   * @param node the node
   * @param consumer a consumer that consumes this dispatcher and {@code node}
   * @return this dispatcher
   */
  @NonNull Dispatcher register(final @NonNull LiteralNode node, final @Nullable BiConsumer<Dispatcher, LiteralNode> consumer);

  /**
   * Parses and returns a parse result.
   *
   * @param string the string
   * @param context the context
   * @return the parse result
   * @throws CommandException if an exception is encountered while parsing
   */
  default @Nullable Result parse(final @NonNull String string, final @NonNull CommandContext context) throws CommandException {
    return this.parse(StringReader.create(string), context);
  }

  /**
   * Parses and returns a parse result.
   *
   * @param reader the string reader
   * @param context the context
   * @return the parse result
   * @throws CommandException if an exception is encountered while parsing
   */
  @Nullable Result parse(final @NonNull StringReader reader, final @NonNull CommandContext context) throws CommandException;

  /**
   * Executes a command.
   *
   * @param string the string
   * @param context the context
   * @throws CommandException if an exception is encountered during execution
   */
  default void execute(final @NonNull String string, final @NonNull CommandContext context) throws CommandException {
    this.execute(StringReader.create(string), context);
  }

  /**
   * Executes a command.
   *
   * @param reader the string reader
   * @param context the context
   * @throws CommandException if an exception is encountered during execution
   */
  void execute(final @NonNull StringReader reader, final @NonNull CommandContext context) throws CommandException;

  /**
   * Executes a command.
   *
   * @param result the parse result
   * @throws CommandException if an exception is encountered during execution
   */
  void execute(final @NonNull Result result) throws CommandException;

  /**
   * Gets the usage for {@code node}.
   *
   * @param node the node
   * @param context the context
   * @return the usage
   */
  @NonNull Map<ChildNode, String> usage(final @NonNull Node node, final @NonNull CommandContext context);

  class Result {
    final StringReader reader;
    final CommandStack.Builder stack;
    final ChildNode node;

    Result(final StringReader reader, final CommandStack.Builder stack, final ChildNode node) {
      this.reader = reader;
      this.stack = stack;
      this.node = node;
    }

    public @NonNull StringReader reader() {
      return this.reader;
    }

    public @NonNull ChildNode node() {
      return this.node;
    }
  }
}
