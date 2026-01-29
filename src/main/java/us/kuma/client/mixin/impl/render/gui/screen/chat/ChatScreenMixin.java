/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.render.gui.screen.chat;

import com.mojang.brigadier.ParseResults;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.kuma.client.Kuma;
import us.kuma.client.api.manager.command.CommandManager;
import us.kuma.client.api.manager.command.CommandSource;
import us.kuma.client.mixin.duck.DuckCommandSuggestions;

/**
 * @author xgraza
 * @since 1/29/26
 */
@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen
{
    @Shadow public abstract String normalizeChatMessage(String string);

    @Shadow
    CommandSuggestions commandSuggestions;

    protected ChatScreenMixin(Component component)
    {
        super(component);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "handleChatInput", at = @At("HEAD"), cancellable = true)
    private void hook$handleChatInput(String input, boolean save, CallbackInfo info)
    {
        input = normalizeChatMessage(input);
        if (input.isEmpty())
        {
            return;
        }

        final CommandManager commands = Kuma.INSTANCE.getCommandManager();
        if (input.startsWith(commands.getCommandPrefix()))
        {
            info.cancel();
            if (save)
            {
                minecraft.gui.getChat().addRecentChat(input);
            }
            ParseResults<ClientSuggestionProvider> parseResults = ((DuckCommandSuggestions) commandSuggestions)
                    .hook$getCurrentParse();
            if (parseResults != null)
            {
                // this will be ours, because of the CommandSuggestionsMixin
                commands.handleParsed((ParseResults<CommandSource>) (Object) parseResults);
            }
        }
    }
}
