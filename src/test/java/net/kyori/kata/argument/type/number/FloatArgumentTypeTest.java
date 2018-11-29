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
package net.kyori.kata.argument.type.number;

import net.kyori.kata.argument.ArgumentType;
import net.kyori.kata.argument.exception.NumberArgumentException;
import net.kyori.kata.context.CommandContext;
import net.kyori.kata.exception.CommandException;
import net.kyori.string.StringReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FloatArgumentTypeTest {
  private static final ArgumentType<Float> ANY = FloatArgumentType.any();
  private static final ArgumentType<Float> MIN_100 = FloatArgumentType.min(100f);
  private static final ArgumentType<Float> MAX_100 = FloatArgumentType.max(100f);
  private static final ArgumentType<Float> BETWEEN_90_100 = FloatArgumentType.between(90f, 100f);

  @Test
  void testParseExpected() {
    assertThrows(NumberArgumentException.Expected.class, () -> ANY.parse(CommandContext.empty(), StringReader.create("potato")));
  }

  @Test
  void testParseAny() throws CommandException {
    assertEquals(-0f, (float) ANY.parse(CommandContext.empty(), StringReader.create("-0")));
    assertEquals(-5.7f, (float) ANY.parse(CommandContext.empty(), StringReader.create("-5.7")));
    assertEquals(0f, (float) ANY.parse(CommandContext.empty(), StringReader.create("0")));
    assertEquals(5.7f, (float) ANY.parse(CommandContext.empty(), StringReader.create("5.7")));
  }

  @Test
  void testParseMin() throws CommandException {
    assertThrows(NumberArgumentException.TooLow.class, () -> MIN_100.parse(CommandContext.empty(), StringReader.create("99")));
    assertThrows(NumberArgumentException.TooLow.class, () -> MIN_100.parse(CommandContext.empty(), StringReader.create("99.0")));
    assertEquals(100f, (float) MIN_100.parse(CommandContext.empty(), StringReader.create("100")));
    assertEquals(100f, (float) MIN_100.parse(CommandContext.empty(), StringReader.create("100.0")));
  }

  @Test
  void testParseMax() throws CommandException {
    assertEquals(100f, (float) MAX_100.parse(CommandContext.empty(), StringReader.create("100")));
    assertEquals(100f, (float) MAX_100.parse(CommandContext.empty(), StringReader.create("100.0")));
    assertThrows(NumberArgumentException.TooHigh.class, () -> MAX_100.parse(CommandContext.empty(), StringReader.create("101")));
  }

  @Test
  void testParseBetween() throws CommandException {
    assertThrows(NumberArgumentException.TooLow.class, () -> BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("89")));
    assertEquals(90f, (float) BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("90")));
    assertEquals(100f, (float) BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("100")));
    assertThrows(NumberArgumentException.TooHigh.class, () -> BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("101")));
  }
}
