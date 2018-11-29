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

import net.kyori.lambda.Maybe;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static net.kyori.kata.context.CommandContext.empty;
import static net.kyori.kata.context.CommandContext.key;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandContextTest {
  private static final CommandContext.Key<String> KEY = key(String.class, "test");

  @Test
  void testFind() {
    assertEquals(Maybe.nothing(), empty().find(KEY));
    assertEquals(Maybe.just("abc"), only(KEY, "abc").find(KEY));
  }

  @Test
  void testGet() {
    assertNull(empty().get(KEY));
    assertEquals("abc", only(KEY, "abc").get(KEY));
  }

  @Test
  void testRequire() {
    assertThrows(NoSuchElementException.class, () -> empty().require(KEY));
    assertEquals("abc", only(KEY, "abc").require(KEY));
  }

  @Test
  void testKeyEquals() {
    assertEquals(key(String.class, "test"), key(String.class, "test"));
  }

  @Test
  void testKeyHashCode() {
    assertEquals(key(String.class, "test").hashCode(), key(String.class, "test").hashCode());
  }

  private static <T> CommandContext only(final CommandContext.Key<T> key, final T value) {
    return CommandContext.builder().put(key, value).build();
  }
}
