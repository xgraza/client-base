/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.mixin.impl.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import us.kuma.client.Kuma;
import us.kuma.client.api.event.Stage;
import us.kuma.client.impl.event.entity.PlayerTickEvent;
import us.kuma.client.impl.event.entity.PlayerUpdateMovementEvent;

/**
 * @author xgraza
 * @since 1/26/26
 */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer
{
    @Shadow
    private double xLast;

    @Shadow
    private double yLast;

    @Shadow
    private double zLast;

    @Shadow
    private float yRotLast;

    @Shadow
    private float xRotLast;

    @Shadow
    private int positionReminder;

    @Shadow
    private boolean lastOnGround;

    @Shadow
    private boolean lastHorizontalCollision;

    @Shadow
    private boolean autoJumpEnabled;

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    @Final
    public ClientPacketListener connection;

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile)
    {
        super(clientLevel, gameProfile);
    }

    @Inject(method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
                    shift = At.Shift.AFTER))
    private void hook$tick(final CallbackInfo info)
    {
        Kuma.EVENT_BUS.post(new PlayerTickEvent());
    }

    @Inject(method = "sendPosition",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;isControlledCamera()Z",
                    shift = At.Shift.AFTER),
            cancellable = true)
    private void hook$sendPosition(final CallbackInfo info)
    {
        final Vec3 position = position();
        final PlayerUpdateMovementEvent event = new PlayerUpdateMovementEvent(Stage.PRE,
                position.x(), position.y(), position.z(), getXRot(), getYRot(), onGround());
        if (Kuma.EVENT_BUS.post(event))
        {
            info.cancel();
            kuma$sendPosition(event);
        }
    }

    @Inject(method = "sendPosition",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/player/LocalPlayer;autoJumpEnabled:Z",
                    shift = At.Shift.AFTER,
                    opcode = Opcodes.PUTFIELD))
    private void hook$sendPosition$tail(final CallbackInfo info)
    {
        // if the above (PRE) is not canceled, this will dispatch
        final Vec3 position = position();
        Kuma.EVENT_BUS.post(new PlayerUpdateMovementEvent(Stage.POST,
                position.x(), position.y(), position.z(), getXRot(), getYRot(), onGround()));
    }

    @Unique
    private void kuma$sendPosition(final PlayerUpdateMovementEvent event)
    {
        Vec3 position = new Vec3(event.getX(), event.getY(), event.getZ());

        double deltaX = position.x() - xLast;
        double deltaY = position.y() - yLast;
        double deltaZ = position.z() - zLast;
        double deltaRotY = event.getYRot() - yRotLast;
        double deltaRotX = event.getXRot() - xRotLast;

        positionReminder++;

        boolean moved = Mth.lengthSquared(deltaX, deltaY, deltaZ) > Mth.square(2.0E-4)
                || positionReminder >= 20;
        boolean rotated = deltaRotY != 0.0 || deltaRotX != 0.0;

        if (moved && rotated)
        {
            connection.send(new ServerboundMovePlayerPacket.PosRot(
                    position, event.getYRot(), event.getXRot(), event.isOnGround(), horizontalCollision));
        } else if (moved)
        {
            connection.send(new ServerboundMovePlayerPacket.Pos(
                    position, event.isOnGround(), horizontalCollision));
        } else if (rotated)
        {
            connection.send(new ServerboundMovePlayerPacket.Rot(
                    event.getYRot(), event.getXRot(), event.isOnGround(), horizontalCollision));
        } else if (lastOnGround != event.isOnGround() || lastHorizontalCollision != horizontalCollision)
        {
            connection.send(new ServerboundMovePlayerPacket.StatusOnly(event.isOnGround(), horizontalCollision));
        }

        if (moved)
        {
            xLast = position.x();
            yLast = position.y();
            zLast = position.z();
            positionReminder = 0;
        }

        if (rotated)
        {
            yRotLast = event.getYRot();
            xRotLast = event.getXRot();
        }

        lastOnGround = event.isOnGround();
        lastHorizontalCollision = horizontalCollision;
        autoJumpEnabled = minecraft.options.autoJump().get();

        // if we cancel PRE, we dispatch the updated movement
        Kuma.EVENT_BUS.post(new PlayerUpdateMovementEvent(Stage.POST,
                event.getX(), event.getY(), event.getZ(), event.getXRot(), event.getYRot(), event.isOnGround()));
    }
}
