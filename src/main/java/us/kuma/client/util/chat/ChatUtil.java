/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.util.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import us.kuma.client.util.BuildConfig;

import java.util.function.Consumer;

/**
 * @author xgraza
 * @since 1/27/26
 */
public final class ChatUtil
{
    private static final Minecraft MC = Minecraft.getInstance();
    private static final Component CLIENT_PREFIX = Component.empty()
            .setStyle(Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE))
            .append(BuildConfig.NAME);

    public static void send(final Consumer<MutableComponent> builder)
    {
        final MutableComponent component = Component.empty()
                .setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY))
                .append(CLIENT_PREFIX)
                .append(" > ");
        builder.accept(component);
        MC.gui.getChat().addMessage(component);
    }

    public static void send(final String message)
    {
        send((component) -> component.append(message));
    }

    public static Component withColor(final ChatFormatting formatting, final String text)
    {
        return Component.empty()
                .setStyle(Style.EMPTY.withColor(formatting))
                .append(text);
    }
}
