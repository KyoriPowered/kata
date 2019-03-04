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
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

abstract class NodeImpl<N extends Node> implements Node {
  private static final IntPredicate NOT_SPACE = character -> character != ' ';
  private @MonotonicNonNull Map<String, ChildNode> nodes;
  private @MonotonicNonNull Map<String, LiteralNode> literals;
  private @MonotonicNonNull Map<String, ArgumentNode> arguments;
  private @MonotonicNonNull Map<String, FlagNode> flags;

  @Override
  public @NonNull Collection<? extends ChildNode> children() {
    if(this.nodes == null) {
      return Collections.emptySet();
    }
    return this.nodes.values();
  }

  @Override
  public @NonNull Collection<? extends ChildNode> relevantChildren(final @NonNull StringReader reader) {
    if(this.literals != null && !this.literals.isEmpty()) {
      final int start = reader.index();
      reader.skip(NOT_SPACE);
      final int end = reader.index(start);
      final @Nullable LiteralNode literal = this.literals.get(reader.string(start, end));
      if(literal != null) {
        return Collections.singleton(literal);
      }
    }
    Set<ChildNode> nodes = null;
    if(this.arguments != null) {
      if(nodes == null) {
        nodes = new LinkedHashSet<>();
      }
      nodes.addAll(this.arguments.values());
    }
    if(this.flags != null) {
      if(nodes == null) {
        nodes = new LinkedHashSet<>();
      }
      nodes.addAll(this.flags.values());
    }
    return nodes == null ? Collections.emptySet() : nodes;
  }

  @Override
  @SuppressWarnings("unchecked")
  public @NonNull N add(final @NonNull ChildNode node) {
    this.add(node, node.name());
    return (N) this;
  }

  private void add(final @NonNull ChildNode node, final @NonNull String name) {
    if(this.nodes != null) {
      final @Nullable ChildNode target = this.nodes.get(name);
      if(target != null) {
        if(target != node && target instanceof ExecutableNodeImpl<?> && node instanceof ExecutableNodeImpl<?>) {
          final ExecutableNode.@Nullable Executable executable = ((ExecutableNodeImpl<?>) node).executable;
          if(executable != null) {
            ((ExecutableNodeImpl<?>) target).executable(executable);
          }
        }
        node.children().forEach(target::add);
        return;
      }
    } else {
      this.nodes = new LinkedHashMap<>();
    }

    this.nodes.put(name, node);

    if(node instanceof LiteralNode) {
      if(this.literals == null) {
        this.literals = new LinkedHashMap<>(1);
      }
      this.literals.put(name, (LiteralNode) node);
    } else if(node instanceof ArgumentNode) {
      if(this.arguments == null) {
        this.arguments = new LinkedHashMap<>(1);
      }
      this.arguments.put(name, (ArgumentNode) node);
    } else if(node instanceof FlagNode) {
      if(this.flags == null) {
        this.flags = new LinkedHashMap<>(1);
      }
      this.flags.put(name, (FlagNode) node);
    } else {
      throw new IllegalArgumentException("Don't know how to add a " + node.getClass());
    }
  }

  @Override
  public void remove(final @NonNull String name) {
    if(this.nodes != null) {
      this.nodes.remove(name);
    }

    if(this.literals != null) {
      this.literals.remove(name);
    }

    if(this.arguments != null) {
      this.arguments.remove(name);
    }

    if(this.flags != null) {
      this.flags.remove(name);
    }
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("nodes", this.nodes));
  }
}
