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

import net.kyori.kata.argument.Argument;
import net.kyori.kata.argument.type.string.StringArgumentType;
import net.kyori.kata.context.CommandContext;
import net.kyori.kata.exception.CommandException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.truth.Truth.assertThat;
import static net.kyori.kata.node.Node.argument;
import static net.kyori.kata.node.Node.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DispatcherTest {
  private static final Argument<String> THING = StringArgumentType.quoted("thing");
  private final Dispatcher dispatcher = Dispatcher.create();

  @BeforeEach
  void before() {
    assertThat(this.dispatcher.root().children()).hasSize(0);
  }

  @Test
  void testUnknownCommand() {
    assertThrows(DispatcherException.UnknownCommand.class, () -> this.dispatcher.execute("", CommandContext.empty()));
    assertThrows(DispatcherException.UnknownCommand.class, () -> this.dispatcher.execute("unknown", CommandContext.empty()));
  }

  @Test
  void testUnknownArgument() {
    this.dispatcher.register(literal("foo"));
    assertThrows(DispatcherException.UnknownArgument.class, () -> this.dispatcher.execute("foo abc", CommandContext.empty()));
  }

  @Test
  void testRegisterAndExecute() throws CommandException {
    final AtomicInteger executions = new AtomicInteger();
    this.dispatcher.register(literal("foo").executes(stack -> executions.incrementAndGet()));
    assertThat(this.dispatcher.root().children()).hasSize(1);
    this.dispatcher.execute("foo", CommandContext.empty());
    assertEquals(1, executions.get());
  }

  @Test
  void testMergedRegisterAndExecute() throws CommandException {
    final AtomicInteger barExecutions = new AtomicInteger();
    final AtomicInteger bazExecutions = new AtomicInteger();
    this.dispatcher.register(literal("foo").then(literal("bar").executes(stack -> barExecutions.incrementAndGet())));
    this.dispatcher.register(literal("foo").then(literal("baz").executes(stack -> bazExecutions.incrementAndGet())));
    assertThat(this.dispatcher.root().children()).hasSize(1);
    this.dispatcher.execute("foo bar", CommandContext.empty());
    this.dispatcher.execute("foo baz", CommandContext.empty());
    assertEquals(1, barExecutions.get());
    assertEquals(1, bazExecutions.get());
  }

  @Test
  void testRedirectedLiteral() throws CommandException {
    final AtomicInteger executions = new AtomicInteger();
    this.dispatcher.register(literal("foo").executes(stack -> executions.incrementAndGet()), (d, n) -> {
      d.register(literal("bar").redirect(n));
      d.register(literal("baz").redirect(n));
    });
    assertThat(this.dispatcher.root().children()).hasSize(3);
    this.dispatcher.execute("foo", CommandContext.empty());
    assertEquals(1, executions.get());
    this.dispatcher.execute("bar", CommandContext.empty());
    assertEquals(2, executions.get());
    this.dispatcher.execute("baz", CommandContext.empty());
    assertEquals(3, executions.get());
  }

  @Test
  void testRedirectedArgument() throws CommandException {
    final AtomicInteger executions = new AtomicInteger();
    this.dispatcher.register(literal("foo").then(
      argument(THING).executes(stack -> {
        if(stack.arguments().require(THING).equals("a thing")) {
          executions.incrementAndGet();
        }
      })
    ), (dispatcher, node) -> {
      dispatcher.register(literal("bar").redirect(node));
      dispatcher.register(literal("baz").redirect(node));
    });
    assertThat(this.dispatcher.root().children()).hasSize(3);
    this.dispatcher.execute("foo \"a thing\"", CommandContext.empty());
    assertEquals(1, executions.get());
    this.dispatcher.execute("bar \"a thing\"", CommandContext.empty());
    assertEquals(2, executions.get());
    this.dispatcher.execute("baz \"a thing\"", CommandContext.empty());
    assertEquals(3, executions.get());
  }
}
