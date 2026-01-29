/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.impl.module.movement;

import net.minecraft.client.player.ClientInput;
import net.minecraft.world.effect.MobEffects;
import us.kuma.client.api.event.Subscribe;
import us.kuma.client.api.manager.module.Module;
import us.kuma.client.api.manager.module.trait.ModuleCategory;
import us.kuma.client.api.manager.module.trait.ModuleManifest;
import us.kuma.client.api.setting.Setting;
import us.kuma.client.impl.event.entity.PlayerTickEvent;

/**
 * @author xgraza
 * @since 1/26/26
 */
@ModuleManifest(name = "Sprint",
        description = "Automatically sprints for you",
        category = ModuleCategory.MOVEMENT)
public final class SprintModule extends Module
{
    private final Setting<Boolean> omniSetting = builder("Omni", false)
            .setDescription("If to allow sprint in all directions")
            .build();

    @Subscribe
    public void onPlayerTick(final PlayerTickEvent event)
    {
        if (MC.player.hasEffect(MobEffects.BLINDNESS)
                || MC.player.onClimbable()
                || MC.player.isCrouching()
                || MC.player.isSprinting()
                || !MC.player.getFoodData().hasEnoughFood()
                || !hasProperMovement())
        {
            return;
        }
        MC.player.setSprinting(true);
    }

    private boolean hasProperMovement()
    {
        final ClientInput input = MC.player.input;
        return input.keyPresses.forward()
                || (omniSetting.getValue() && (input.keyPresses.left() || input.keyPresses.right()));
    }
}
