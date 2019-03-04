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
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.truth.Truth.assertThat;
import static net.kyori.kata.node.Node.literal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LiteralNodeTest {
  @Test
  void testBuildEmpty() {
    final LiteralNode foo = literal("foo").build();
    assertThat(foo.children()).isEmpty();
  }

  @Test
  void testBuild() {
    final LiteralNode foo = literal("foo")
      .then(literal("bar"))
      .then(literal("baz").build())
      .build();
    assertThat(foo.children()).hasSize(2);
  }

  @Test
  void testRedirect() throws CommandException {
    final AtomicInteger executions = new AtomicInteger();
    final LiteralNode target = literal("target").executes(stack -> executions.incrementAndGet()).build();
    final LiteralNode literal = literal("test").redirect(target).build();
    final @Nullable ExecutableNode redirect = literal.redirect();
    if(redirect != null) {
      final ExecutableNode.@Nullable Executable executable = redirect.executable();
      if(executable != null) {
        executable.execute(CommandStack.builder(StringReader.create(""), CommandContext.empty()).build());
      }
    }
    assertEquals(1, executions.get());
  }

  @Test
  void testExecutable() throws CommandException {
    final AtomicInteger executions = new AtomicInteger();
    final LiteralNode literal = literal("test").executes(stack -> executions.incrementAndGet()).build();
    final ExecutableNode.@Nullable Executable executable = literal.executable();
    if(executable != null) {
      executable.execute(CommandStack.builder(StringReader.create(""), CommandContext.empty()).build());
    }
    assertEquals(1, executions.get());
  }
}
