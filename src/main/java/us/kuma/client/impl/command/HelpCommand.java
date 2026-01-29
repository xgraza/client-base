/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.command;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import us.kuma.client.api.manager.command.Command;
import us.kuma.client.api.manager.command.CommandSource;
import us.kuma.client.api.manager.command.argument.CommandArgumentType;
import us.kuma.client.api.manager.command.trait.CommandManifest;
import us.kuma.client.util.chat.ChatUtil;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

/**
 * @author xgraza
 * @since 1/27/26
 */
@CommandManifest(aliases = { "help", "commands" },
        description = "Provides a list of commands and their syntax")
public final class HelpCommand extends Command
{
    private final com.mojang.brigadier.Command<CommandSource> showCommands = (ctx) ->
    {
        final List<Command> commandList = ctx.getSource().manager().getEntities();
        ChatUtil.send((component) ->
        {
            component.append("Commands (%s):\n".formatted(commandList.size()));
            for (final Command command : commandList)
            {
                final String name = command.getManifest().aliases()[0];
                component.append(Component.empty()
                        .setStyle(Style.EMPTY.withClickEvent(
                                new ClickEvent.SuggestCommand(".help %s".formatted(name))))
                        .append(name));
                component.append(" ");
            }
            component.append("\n\n");
            component.append("Tip: Click on a command name to see its usage");
        });
        return SINGLE_SUCCESS;
    };

    @Override
    protected void build(final LiteralArgumentBuilder<CommandSource> builder)
    {
        builder.then(arg("name", CommandArgumentType.command())
                        .executes((ctx) ->
                        {
                            final Command command = CommandArgumentType.getCommand(ctx, "name");
                            final List<String> usageList = getUsages(command, ctx.getSource());
                            if (usageList.isEmpty())
                            {
                                ChatUtil.send("No usage for this command.");
                                return SINGLE_SUCCESS;
                            }

                            ChatUtil.send((component) ->
                            {
                                final String literal = command.getDefaultLiteral();
                                component.append("Usages (%s)".formatted(usageList.size()));
                                component.append(" for ");
                                component.append(ChatUtil.withColor(ChatFormatting.LIGHT_PURPLE, literal));
                                component.append(":\n");
                                for (int i = 0; i < usageList.size(); ++i)
                                {
                                    final String fullUsage = ".%s %s".formatted(literal, usageList.get(i));
                                    component.append(Component.empty()
                                            .setStyle(Style.EMPTY
                                                    .withClickEvent(new ClickEvent.SuggestCommand(fullUsage)))
                                            .append(fullUsage));
                                    if (i < usageList.size() - 1)
                                    {
                                        component.append("\n");
                                    }
                                }
                            });

                            return SINGLE_SUCCESS;
                        }))
                .executes(showCommands);
    }

    private List<String> getUsages(final Command command, final CommandSource source)
    {
        final ParseResults<CommandSource> results = source.dispatcher().parse(command.getDefaultLiteral(), source);
        if (results == null)
        {
            return Collections.emptyList();
        }
        final Map<CommandNode<CommandSource>, String> smartUsages = source.dispatcher()
                .getSmartUsage(Iterables.getLast(results.getContext().getNodes()).getNode(), source);
        return new LinkedList<>(smartUsages.values());
    }
}
