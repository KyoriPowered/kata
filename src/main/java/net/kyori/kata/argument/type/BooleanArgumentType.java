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
package net.kyori.kata.argument.type;

import net.kyori.kata.argument.Argument;
import net.kyori.kata.argument.ArgumentType;
import net.kyori.kata.argument.exception.BooleanArgumentException;
import net.kyori.kata.context.CommandContext;
import net.kyori.kata.exception.CommandException;
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * A boolean argument type.
 */
public final class BooleanArgumentType implements ArgumentType<Boolean> {
  private static final ArgumentType<Boolean> INSTANCE = new BooleanArgumentType();

  private BooleanArgumentType() {
  }

  /**
   * Gets an argument type that parses a boolean.
   *
   * @return the argument type
   */
  public static @NonNull ArgumentType<Boolean> any() {
    return INSTANCE;
  }

  /**
   * Creates an argument that parses a boolean.
   *
   * @param name the argument name
   * @return the argument
   */
  public static @NonNull Argument<Boolean> any(final @NonNull String name) {
    return any().create(name);
  }

  @Override
  public @NonNull Boolean parse(final @NonNull CommandContext context, final @NonNull StringReader reader) throws CommandException {
    final int start = reader.index();
    while(reader.readable() && !Character.isWhitespace(reader.peek())) {
      reader.skip();
    }
    final String string = reader.string(start, reader.index());
    if(string.equals("true")) {
      return true;
    } else if(string.equals("false")) {
      return false;
    } else {
      reader.index(start);
      throw new BooleanArgumentException.Invalid(reader, string);
    }
  }
}
