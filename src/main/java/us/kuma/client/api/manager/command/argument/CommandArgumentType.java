/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.command.argument;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import us.kuma.client.Kuma;
import us.kuma.client.api.manager.command.Command;
import us.kuma.client.api.manager.command.CommandManager;
import us.kuma.client.api.manager.command.CommandSource;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class CommandArgumentType implements ArgumentType<Command>
{
    @Override
    public Command parse(final StringReader reader) throws CommandSyntaxException
    {
        final String input = reader.readString().toLowerCase();
        final CommandManager commands = Kuma.INSTANCE.getCommandManager();
        for (final String alias : commands.getCommandAliases())
        {
            if (alias.equalsIgnoreCase(input) || alias.contains(input))
            {
                return Kuma.INSTANCE.getCommandManager().getCommand(alias);
            }
        }

        throw invalidCommand(reader, input);
    }

    private CommandSyntaxException invalidCommand(final StringReader reader, final String input)
    {
        return new SimpleCommandExceptionType(new LiteralMessage(
                "\"%s\" is not a valid command alias".formatted(input))).createWithContext(reader);
    }

    public static Command getCommand(final CommandContext<CommandSource> ctx, final String argumentName)
    {
        return ctx.getArgument(argumentName, Command.class);
    }

    public static CommandArgumentType command()
    {
        return new CommandArgumentType();
    }
}
