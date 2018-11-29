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

class ShortArgumentTypeTest {
  private static final ArgumentType<Short> ANY = ShortArgumentType.any();
  private static final ArgumentType<Short> MIN_100 = ShortArgumentType.min((short) 100);
  private static final ArgumentType<Short> MAX_100 = ShortArgumentType.max((short) 100);
  private static final ArgumentType<Short> BETWEEN_90_100 = ShortArgumentType.between((short) 90, (short) 100);

  @Test
  void testParseExpected() {
    assertThrows(NumberArgumentException.Expected.class, () -> ANY.parse(CommandContext.empty(), StringReader.create("potato")));
  }

  @Test
  void testParseAny() throws CommandException {
    assertEquals(Short.MIN_VALUE, (short) ANY.parse(CommandContext.empty(), StringReader.create(String.valueOf(Short.MIN_VALUE))));
    assertEquals((short) -0, (short) ANY.parse(CommandContext.empty(), StringReader.create("-0")));
    assertEquals((short) 0, (short) ANY.parse(CommandContext.empty(), StringReader.create("0")));
    assertEquals(Short.MAX_VALUE, (short) ANY.parse(CommandContext.empty(), StringReader.create(String.valueOf(Short.MAX_VALUE))));
  }

  @Test
  void testParseMin() throws CommandException {
    assertThrows(NumberArgumentException.TooLow.class, () -> MIN_100.parse(CommandContext.empty(), StringReader.create("99")));
    assertEquals((short) 100, (short) MIN_100.parse(CommandContext.empty(), StringReader.create("100")));
  }

  @Test
  void testParseMax() throws CommandException {
    assertEquals((short) 100, (short) MAX_100.parse(CommandContext.empty(), StringReader.create("100")));
    assertThrows(NumberArgumentException.TooHigh.class, () -> MAX_100.parse(CommandContext.empty(), StringReader.create("101")));
  }

  @Test
  void testParseBetween() throws CommandException {
    assertThrows(NumberArgumentException.TooLow.class, () -> BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("89")));
    assertEquals((short) 90, (short) BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("90")));
    assertEquals((short) 100, (short) BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("100")));
    assertThrows(NumberArgumentException.TooHigh.class, () -> BETWEEN_90_100.parse(CommandContext.empty(), StringReader.create("101")));
  }
}
