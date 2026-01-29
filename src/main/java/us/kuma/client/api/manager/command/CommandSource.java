/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.command;

import com.mojang.brigadier.CommandDispatcher;

public record CommandSource(CommandManager manager, CommandDispatcher<CommandSource> dispatcher)
{
}
