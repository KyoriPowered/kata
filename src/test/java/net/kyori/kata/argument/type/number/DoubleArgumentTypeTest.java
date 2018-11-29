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

class DoubleArgumentTypeTest {
  private static final ArgumentType<Double> ANY = DoubleArgumentType.any();
  private static final ArgumentType<Double> MIN_100 = DoubleArgumentType.min(100d);
  private static final ArgumentType<Double> MAX_100 = DoubleArgumentType.max(100d);
  private static final ArgumentType<Double> BETWEEN_90_100 = DoubleArgumentType.between(90d, 100d);

  @Test
  void testParseExpected() {
    assertThrows(NumberArgumentException.Expected.class, () -> ANY.parse(CommandContext.empty(), StringReader.create("potato")));
  }

  @Test
  void testParseAny() throws CommandException {
    assertEquals(-0d, (double) ANY.parse(CommandContext.empty(), StringReader.create("-0")));
    assertEquals(-5.7d, (double) ANY.parse(CommandContext.empty(), StringReader.create("-5.7")));
    assertEquals(0d, (double) ANY.parse(CommandContext.empty(), StringReader.create("0")));
    assertEquals(5.7d, (double) ANY.parse(CommandContext.empty(), StringReader.create("5.7")));
  }

  @Test
  void testParseMin() throws CommandException {
    assertThrows(NumberArgumentException.TooLow.class, () -> MIN_100.parse(CommandContext.empty(), StringReader.create("99")));
    assertThrows(NumberArgumentException.TooLow.class, () -> MIN_100.parse(CommandContext.empty(), StringReader.create("99.0")));
    assertEquals(100d, (double) MIN_100.parse(CommandContext.empty(), StringReader.create("100")));
    assertEquals(100d, (double) MIN_100.parse(CommandContext.empty(), StringReader.create("100.0")));
  }

  @Test
  void testParseMax() throws CommandException {
    assertEquals(100d, (double) MAX_100.parse(CommandContext.empty(), StringReader.create("100")));
    assertEquals(100d, (double) MAX_100.parse(CommandContext.empty(), StringReader.create("100.0")));
    assertThrows(NumberArgumentException.TooHigh.class, () -> MAX_100.parse(CommandContext.empty(), StringReader.create("101")));
  }

  @Test
  void testParseBetween() throws CommandException {
    assertThrows(NumberArgumentException.TooLow.class, () -> BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("89")));
    assertEquals(90d, (double) BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("90")));
    assertEquals(100d, (double) BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("100")));
    assertThrows(NumberArgumentException.TooHigh.class, () -> BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("101")));
  }
}
