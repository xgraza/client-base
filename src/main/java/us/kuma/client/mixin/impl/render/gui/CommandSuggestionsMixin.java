/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.render.gui;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.kuma.client.Kuma;
import us.kuma.client.api.manager.command.CommandManager;
import us.kuma.client.api.manager.command.CommandSource;
import us.kuma.client.mixin.duck.DuckCommandSuggestions;

import java.util.concurrent.CompletableFuture;

/**
 * @author xgraza
 * @since 1/29/26
 */
@Mixin(CommandSuggestions.class)
public abstract class CommandSuggestionsMixin implements DuckCommandSuggestions
{
    @Shadow
    private CommandSuggestions.SuggestionsList suggestions;
    @Shadow
    private CompletableFuture<Suggestions> pendingSuggestions;
    @Shadow
    private ParseResults<ClientSuggestionProvider> currentParse;
    @Shadow
    boolean keepSuggestions;
    @Shadow
    @Final
    EditBox input;

    @Shadow
    protected abstract void updateUsageInfo();

    @SuppressWarnings("unchecked")
    @Inject(method = "updateCommandInfo", at = @At("RETURN"))
    public void updateCommandInfo(CallbackInfo info, @Local StringReader stringReader)
    {
        CommandManager commands = Kuma.INSTANCE.getCommandManager();
        if (!stringReader.canRead() || !stringReader.getString().startsWith(commands.getCommandPrefix()))
        {
            return;
        }
        stringReader.skip();

        CommandDispatcher<CommandSource> dispatcher = commands.getDispatcher();
        ParseResults<CommandSource> parse = dispatcher.parse(stringReader, new CommandSource(commands, dispatcher));
        currentParse = (ParseResults<ClientSuggestionProvider>) (Object) parse;
        if (suggestions == null || !keepSuggestions)
        {
            pendingSuggestions = dispatcher.getCompletionSuggestions(parse, input.getCursorPosition());
            pendingSuggestions.thenRun(() ->
            {
                if (pendingSuggestions.isDone())
                {
                    updateUsageInfo();
                }
            });
        }
    }

    @Override
    public ParseResults<ClientSuggestionProvider> hook$getCurrentParse()
    {
        return currentParse;
    }
}
