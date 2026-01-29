/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.command;

import com.google.gson.JsonElement;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import us.kuma.client.api.event.Subscribe;
import us.kuma.client.api.manager.InstancedManager;
import us.kuma.client.api.manager.config.JSONSerializable;
import us.kuma.client.impl.command.HelpCommand;
import us.kuma.client.impl.event.network.ChatEvent;
import us.kuma.client.util.io.FileUtil;

import java.io.File;
import java.util.*;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class CommandManager implements InstancedManager<Command>, JSONSerializable
{
    private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher<>();
    private final Map<String, Command> commandToAliasMap = new LinkedHashMap<>();
    private final List<Command> commandList = new LinkedList<>();

    private String commandPrefix = ".";

    @Override
    public void init()
    {
        register(new HelpCommand());
    }

    @Subscribe
    public void onChat(final ChatEvent event)
    {
        String message = event.getMessage();
        if (!message.startsWith(commandPrefix))
        {
            return;
        }
        event.cancel();
        handleExecution(message.substring(commandList.size()).trim());
    }

    public void handleExecution(final String message)
    {
        final CommandSource source = new CommandSource(this, dispatcher);
        handleParsed(dispatcher.parse(message, source));
    }

    public void handleParsed(final ParseResults<CommandSource> parseResult)
    {
        if (!parseResult.getExceptions().isEmpty())
        {
            // handle
            return;
        }
        try
        {
            final int result = dispatcher.execute(parseResult);
        } catch (final CommandSyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(Command entity)
    {
        commandList.add(entity);
        for (final String alias : entity.getManifest().aliases())
        {
            final LiteralArgumentBuilder<CommandSource> builder = Command.literal(alias);
            entity.build(builder);
            dispatcher.register(builder);
            commandToAliasMap.put(alias, entity);
        }
    }

    @Override
    public void unregister(Command entity)
    {

    }

    public CommandDispatcher<CommandSource> getDispatcher()
    {
        return dispatcher;
    }

    @Override
    public List<Command> getEntities()
    {
        return commandList;
    }

    public void setCommandPrefix(final String commandPrefix)
    {
        this.commandPrefix = commandPrefix;
    }

    public String getCommandPrefix()
    {
        return commandPrefix;
    }

    public Set<String> getCommandAliases()
    {
        return commandToAliasMap.keySet();
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> T getCommand(final String alias)
    {
        return (T) commandToAliasMap.get(alias);
    }

    @Override
    public void fromJSON(JsonElement element)
    {

    }

    @Override
    public JsonElement toJSON()
    {
        return null;
    }

    @Override
    public File getLocation()
    {
        return FileUtil.resolve("command_manager.json");
    }
}
