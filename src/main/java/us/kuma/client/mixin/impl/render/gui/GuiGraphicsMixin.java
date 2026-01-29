/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.render.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author xgraza
 * @since 1/27/26
 */
@Mixin(GuiGraphics.class)
public interface GuiGraphicsMixin
{
    @Accessor("guiRenderState")
    GuiRenderState hook$getRenderState();

    @Accessor("pose") @Final
    Matrix3x2fStack hook$getPose();
}
