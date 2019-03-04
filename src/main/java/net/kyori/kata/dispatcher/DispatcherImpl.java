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

import com.google.common.collect.ComparisonChain;
import net.kyori.kata.Usage;
import net.kyori.kata.context.CommandContext;
import net.kyori.kata.context.CommandStack;
import net.kyori.kata.exception.CommandException;
import net.kyori.kata.node.ChildNode;
import net.kyori.kata.node.ExecutableNode;
import net.kyori.kata.node.FlagNode;
import net.kyori.kata.node.LiteralNode;
import net.kyori.kata.node.Node;
import net.kyori.kata.node.RootNode;
import net.kyori.lambda.examine.ExaminableProperty;
import net.kyori.string.StringReader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class DispatcherImpl implements Dispatcher {
  private static final Comparator<Result> COMPARATOR = (a, b) -> ComparisonChain.start()
    .compareTrueFirst(a.reader.readable(), b.reader.readable())
    .compareFalseFirst(a.reader.readable(), b.reader.readable())
    .result();
  private final RootNode root = Node.root();

  @Override
  public @NonNull RootNode root() {
    return this.root;
  }

  @Override
  public @NonNull Dispatcher register(final @NonNull LiteralNode node) {
    this.root.add(node);
    return this;
  }

  @Override
  public @NonNull Dispatcher register(final @NonNull LiteralNode node, final @Nullable Consumer<LiteralNode> consumer) {
    this.root.add(node);
    if(consumer != null) {
      consumer.accept(node);
    }
    return this;
  }

  @Override
  public @NonNull Dispatcher register(final @NonNull LiteralNode node, final @Nullable BiConsumer<Dispatcher, LiteralNode> consumer) {
    this.root.add(node);
    if(consumer != null) {
      consumer.accept(this, node);
    }
    return this;
  }

  @Override
  public Dispatcher.@Nullable Result parse(final @NonNull StringReader reader, final @NonNull CommandContext context) throws CommandException {
    return this.parse(this.root, reader, context, CommandStack.builder(reader, context));
  }

  @Override
  public void execute(final @NonNull StringReader reader, final @NonNull CommandContext context) throws CommandException {
    final Result result = this.parse(reader, context);
    if(result != null) {
      this.execute(result);
    } else {
      throw new DispatcherException.UnknownCommand(reader);
    }
  }

  @Override
  public void execute(final @NonNull Result result) throws CommandException {
    if(result.reader.readable()) {
      if(result.stack.literalRange().isEmpty()) {
        throw new DispatcherException.UnknownCommand(result.reader);
      } else {
        throw new DispatcherException.UnknownArgument(result.reader);
      }
    }
    if(result.node instanceof ExecutableNode) {
      final ExecutableNode.@Nullable Executable executable = ((ExecutableNode) result.node).executable();
      if(executable != null) {
        executable.execute(result.stack.build());
      }
    }
  }

  @Override
  public @NonNull Map<ChildNode, String> usage(final @NonNull Node node, final @NonNull CommandContext context) {
    final Map<ChildNode, String> result = new LinkedHashMap<>();
    final boolean optional = node instanceof ExecutableNode && ((ExecutableNode) node).executable() != null;
    for(final ChildNode child : node.children()) {
      final String usage = this.usage(child, context, optional, false);
      if(usage != null) {
        result.put(child, usage);
      }
    }
    return result;
  }

  private @Nullable String usage(final ChildNode node, final CommandContext context, final boolean optional, final boolean deep) {
    if(!node.canUse(context)) {
      return null;
    }

    final String self = optional ? Usage.OPTIONAL_OPEN + node.usage() + Usage.OPTIONAL_CLOSE : node.usage();

    if(!deep) {
      final ChildNode redirect = node instanceof ExecutableNode ? ((ExecutableNode) node).redirect() : null;
      if(redirect != null) {
        return self + ARGUMENT_SEPARATOR + "-> " + redirect.usage();
      } else {
        final boolean childOptional = !(node instanceof ExecutableNode) || ((ExecutableNode) node).executable() != null;
        final String open = childOptional ? Usage.OPTIONAL_OPEN : Usage.REQUIRED_OPEN;
        final String close = childOptional ? Usage.OPTIONAL_CLOSE : Usage.REQUIRED_CLOSE;

        final Collection<ChildNode> children = node.children().stream().filter(child -> child.canUse(context)).collect(Collectors.toList());
        if(children.size() == 1) {
          final @Nullable String usage = this.usage(children.iterator().next(), context, childOptional, childOptional);
          if(usage != null) {
            return self + ARGUMENT_SEPARATOR + usage;
          }
        } else if(children.size() > 1) {
          final Set<String> childUsage = new LinkedHashSet<>();
          for(final ChildNode child : children) {
            final String usage = this.usage(child, context, childOptional, true);
            if(usage != null) {
              childUsage.add(usage);
            }
          }
          if(childUsage.size() == 1) {
            final String usage = childUsage.iterator().next();
            return self + ARGUMENT_SEPARATOR + (childOptional ? Usage.OPTIONAL_OPEN + usage + Usage.OPTIONAL_CLOSE : usage);
          } else if(childUsage.size() > 1) {
            final StringJoiner joiner = new StringJoiner(Usage.OR, self + ARGUMENT_SEPARATOR + open, close);
            for(final ChildNode child : children) {
              joiner.add(child.usage());
            }
            if(joiner.length() > 0) {
              return joiner.toString();
            }
          }
        }
      }
    }

    return self;
  }

  private @Nullable Result parse(final @NonNull Node node, final @NonNull StringReader reader, final @NonNull CommandContext context, final CommandStack.@NonNull Builder stack) throws CommandException {
    final List<Result> results = new ArrayList<>(1);
    for(final ChildNode child : node.relevantChildren(reader)) {
      if(!child.canUse(context)) {
        continue;
      }
      if(this.parse0(results, node, child, reader.copy(), context, stack.copy())) {
        break;
      }
    }
    final int size = results.size();
    if(size > 0) {
      if(size > 1) {
        results.sort(COMPARATOR);
      }
      return results.get(0);
    }
    return node instanceof ChildNode ? new Result(reader, stack, (ChildNode) node) : null;
  }

  private boolean parse0(final List<Result> results, final @NonNull Node parent, final @NonNull ChildNode child, final @NonNull StringReader reader, final @NonNull CommandContext context, final CommandStack.@NonNull Builder stack) throws CommandException {
    child.parse(stack, context, reader);

    if(reader.readable()) {
      if(reader.peek() != ARGUMENT_SEPARATOR) {
        throw new DispatcherException.IncompleteParse(reader);
      } else {
        reader.skip();
        if(this.redirect(results, child, reader, context, stack)) {
          return true;
        }
        results.add(this.parse(child, reader, context, stack));
      }
    } else {
      if(this.redirect(results, child, reader, context, stack)) {
        return true;
      }
      if(parent instanceof ChildNode && child instanceof FlagNode) {
        results.add(new Result(reader, stack, (ChildNode) parent));
      } else {
        results.add(new Result(reader, stack, child));
      }
    }
    return false;
  }

  private boolean redirect(final List<Result> results, final @NonNull ChildNode child, final @NonNull StringReader reader, final @NonNull CommandContext context, final CommandStack.@NonNull Builder stack) throws CommandException {
    final @Nullable ChildNode redirect = child instanceof ExecutableNode ? ((ExecutableNode) child).redirect() : null;
    if(redirect != null) {
      results.add(this.parse(redirect, reader, context, stack));
      return true;
    }
    return false;
  }

  @Override
  public @NonNull Stream<? extends ExaminableProperty> examinableProperties() {
    return Stream.of(ExaminableProperty.of("root", this.root));
  }
}
