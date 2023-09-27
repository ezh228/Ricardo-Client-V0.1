package ru.terrarXD.module.modules.Render;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.*;
import ru.terrarXD.shit.settings.FloatSetting;

/**
 * @author zTerrarxd_
 * @since 0:03 of 15.06.2023
 */
public class FreeCam extends Module {
    FloatSetting speed;
    private float old;
    private double oldX;
    private double oldY;
    private double oldZ;
    public FreeCam() {
        super("FreeCam", Category.Render);
        add(speed = new FloatSetting("Speed",0.1f, 1, 0.5f, 0.1f));
    }
    @Override
    public void onDisable() {
        if (mc.player != null){
            mc.player.capabilities.isFlying = false;
            mc.player.capabilities.setFlySpeed(old);
            mc.player.noClip = false;
            mc.renderGlobal.loadRenderers();
            mc.player.noClip = false;
            mc.player.setPositionAndRotation(oldX, oldY, oldZ, mc.player.rotationYaw, mc.player.rotationPitch);
            mc.world.removeEntityFromWorld(-69);
            mc.player.motionZ = 0;
            mc.player.motionX = 0;
        }

        super.onDisable();
    }

    @Override
    public void onEnable() {
        if (mc.player != null){
            oldX = mc.player.posX;
            oldY = mc.player.posY;
            oldZ = mc.player.posZ;
            mc.player.noClip = true;
            EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.posY -= 0;
            fakePlayer.rotationYawHead = mc.player.rotationYawHead;
            mc.world.addEntityToWorld(-69, fakePlayer);
        }

        super.onEnable();
    }

    @EventTarget
    public void onFullCube(EventFullCube event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        mc.player.noClip = true;
        mc.player.onGround = false;
        mc.player.capabilities.setFlySpeed(speed.getVal() / 5);
        mc.player.capabilities.isFlying = true;
        event.setPosX(oldX);
        event.setPosY(oldY);
        event.setPosZ(oldZ);
    }



    @EventTarget
    public void onReceivePacket(EventPacketReceive event) {
        if (true) {
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventPacketSend event) {
        mc.player.setSprinting(false);
        /*
            if (event.getPacket() instanceof CPacketPlayer) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketPlayer.Position) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation) {
                event.setCancelled(true);
            }
            if (event.getPacket() instanceof CPacketEntityAction) {
                event.setCancelled(true);
            }

         */

    }
}
