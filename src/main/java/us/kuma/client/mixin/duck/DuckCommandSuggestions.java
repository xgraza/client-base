/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.duck;

import com.mojang.brigadier.ParseResults;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;

/**
 * @author xgraza
 * @since 1/29/26
 */
public interface DuckCommandSuggestions
{
    ParseResults<ClientSuggestionProvider> hook$getCurrentParse();
}
