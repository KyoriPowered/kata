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

import net.kyori.lambda.examine.ExaminableProperty;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.stream.Stream;

final class LiteralNodeImpl extends ExecutableNodeImpl<LiteralNode> implements LiteralNode {
  private final String name;

  private LiteralNodeImpl(final Builder builder) {
    super(builder);
    this.name = builder.name;
  }

  @Override
  public @NonNull String name() {
    return this.name;
  }

  @Override
  public @NonNull String usage() {
    return this.name;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.concat(
      Stream.of(ExaminableProperty.of("name", this.name)),
      super.examinableProperties()
    );
  }

  static final class Builder extends ExecutableNodeImpl.Builder<LiteralNode, LiteralNode.Builder> implements LiteralNode.Builder {
    private final String name;

    Builder(final @NonNull String name) {
      this.name = name;
    }

    @Override
    @NonNull LiteralNode create() {
      return new LiteralNodeImpl(this);
    }
  }
}
