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
import net.kyori.lambda.function.MorePredicates;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

abstract class ChildNodeImpl<N extends ChildNode> extends NodeImpl<N> implements ChildNode {
  private final Predicate<CommandContext> requirement;

  ChildNodeImpl(final Builder<N, ?> builder) {
    this.requirement = builder.requirement;
  }

  @Override
  public boolean canUse(final @NonNull CommandContext context) {
    return this.requirement.test(context);
  }

  static abstract class Builder<N extends ChildNode, B extends ChildNode.Builder<N, B>> implements ChildNode.Builder<N, B> {
    @NonNull Predicate<CommandContext> requirement = MorePredicates.alwaysTrue();
    @MonotonicNonNull RootNode node;

    @Override
    @SuppressWarnings("unchecked")
    public @NonNull B requires(final @NonNull Predicate<CommandContext> requirement) {
      this.requirement = requirement;
      return (B) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NonNull B then(final @NonNull ChildNode node, final @Nullable BiConsumer<B, ChildNode> consumer) {
      this.checkThen();
      if(this.node == null) {
        this.node = Node.root();
      }
      this.node.add(node);
      if(consumer != null) {
        consumer.accept((B) this, node);
      }
      return (B) this;
    }

    protected void checkThen() {
    }

    @Override
    public final @NonNull N build() {
      final N node = this.create();
      if(this.node != null) {
        this.node.children().forEach(node::add);
      }
      return node;
    }

    abstract @NonNull N create();
  }
}
