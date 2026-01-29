/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.client.Minecraft;
import us.kuma.client.api.manager.command.trait.CommandManifest;

/**
 * @author xgraza
 * @since 1/27/26
 */
public abstract class Command
{
    protected static final Minecraft MC = Minecraft.getInstance();

    private final CommandManifest manifest;

    public Command()
    {
        if (!getClass().isAnnotationPresent(CommandManifest.class))
        {
            throw new RuntimeException("@CommandManifest must be present on %s"
                    .formatted(getClass().getSimpleName()));
        }
        manifest = getClass().getDeclaredAnnotation(CommandManifest.class);
    }

    protected abstract void build(final LiteralArgumentBuilder<CommandSource> builder);

    public CommandManifest getManifest()
    {
        return manifest;
    }

    public String getDefaultLiteral()
    {
        return manifest.aliases()[0];
    }

    public static LiteralArgumentBuilder<CommandSource> literal(final String literal)
    {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<CommandSource, T> arg(final String name, final ArgumentType<T> argumentType)
    {
        return RequiredArgumentBuilder.argument(name, argumentType);
    }
}
