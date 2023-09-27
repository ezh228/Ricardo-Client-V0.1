package ru.terrarXD.module.modules.Combat;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.Event;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventPacketSend;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.settings.ModeSetting;
import ru.terrarXD.shit.utils.MathUtils;
import ru.terrarXD.shit.utils.TimerUtils;
import ru.terrarXD.shit.utils.Utils;

import java.util.*;

/**
 * @author zTerrarxd_
 * @since 23:23 of 04.06.2023
 */
public class AntiAim extends Module {
    float ticks = 3;
    TimerUtils timer = new TimerUtils();
    int state = 0;
    //0 - отправка пакетов
    //1 - принятие и сохранение
    //2 - ожидание
    ArrayList<Packet> packets = new ArrayList<>();
    Vec3d pos = new Vec3d(0, 0, 0);
    ModeSetting mode;
    BooleanSetting aimSync;
    public EntityPlayer player;

    public AntiAim() {
        super("AntiAim", Category.Combat);
        ArrayList<String> modes = new ArrayList<>();
        modes.add("FakeLag");
        modes.add("Spin");
        modes.add("Lelf-Right");
        add(mode = new ModeSetting("Mode", modes, "FakeLag"));
        add(aimSync = (BooleanSetting) new BooleanSetting("Aim-Sync", true).setVisible(()->mode.getVal().equals("FakeLag")));
    }

    public void setMotion(double speed, float pseudoYaw, double aa, double po4) {
        double forward = po4;
        double strafe = aa;
        float yaw = pseudoYaw;
        if (po4 != 0.0) {
            if (aa > 0.0) {
                yaw = pseudoYaw + (float)(po4 > 0.0 ? -45 : 45);
            } else if (aa < 0.0) {
                yaw = pseudoYaw + (float)(po4 > 0.0 ? 45 : -45);
            }
            strafe = 0.0;
            if (po4 > 0.0) {
                forward = 1.0;
            } else if (po4 < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        } else if (strafe < 0.0) {
            strafe = -1.0;
        }
        double kak = Math.cos(Math.toRadians(yaw + 90.0f));
        double nety = Math.sin(Math.toRadians(yaw + 90.0f));
        player.motionX = (forward * speed * kak + strafe * speed * nety);
        player.motionZ = (forward * speed * nety - strafe * speed * kak);
        //System.out.println(player.motionX);

    }

    public static int direction;
    float range = 0.2f;


    @EventTarget
    public void onUpdate(EventUpdate event){
        mc.player.noClip = true;

        boolean can = aimSync.getVal() ? (Client.moduleManager.getModule("AimBot").isEnabled() ? !((AimBot)Client.moduleManager.getModule("AimBot")).work : true) : true;
        if (mode.getVal().equals("FakeLag") && can){


            if (mc.currentScreen != null){
                state = 0;
            }
            if (timer.hasReached(((long) ticks*50))){
                state = 0;
                timer.reset();
                ticks = MathUtils.getRandomInRange(10, 5);
            }
            if (state == 0){
                for (Packet packet : packets){
                    mc.getConnection().sendPacket(packet);
                }
                packets.clear();
                state = 1;
                player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
                pos = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
            }
            mc.player.noClip = true;

        }else if (mode.getVal().equals("Spin")) {
            range = 1.5f;

            final float[] arrf = Utils.getNeededRotations(mc.player.posX, mc.player.posY, mc.player.posZ, player.posX, player.posY, player.posZ);
            if (mc.player.getDistanceToEntity(player)>range){
                setMotion(0.2, arrf[0], 1, 100);
            }else {
                setMotion(0.2, arrf[0], 1, 0);
            }
            player.rotationYaw = mc.player.rotationYaw;
            player.rotationPitch = mc.player.rotationPitch;

            player.moveEntity(MoverType.PLAYER, player.motionX, player.motionY, player.motionZ);
            event.setPosX(player.posX);
            event.setPosY(player.posY);
            event.setPosZ(player.posZ);

        }else if (mode.getVal().equals("Lelf-Right")){
            double move = 0.05d*state;
            if (state >0){
                double[] pos = Utils.getDirection(mc.player.rotationYaw-90);
                pos = new double[] {pos[0]*move, pos[1]*move};
                event.setPosX(mc.player.posX+pos[0]);
                event.setPosZ(mc.player.posZ+pos[1]);
                state+=1;
                if (state>20){
                    state=-1;
                }
            }else if (state<0){
                double[] pos = Utils.getDirection(mc.player.rotationYaw+90);
                pos = new double[] {pos[0]*move, pos[1]*move};
                event.setPosX(mc.player.posX+pos[0]);
                event.setPosZ(mc.player.posZ+pos[1]);
                state -=1;
                if (state<-20){
                    state=1;
                }
            }else {
                state=1;
            }
            player.setPosition(event.getPosX(), event.getPosY(), event.getPosZ());
        }


    }

    @EventTarget
    public void render3D(EventRender3D event){

        //mc.getRenderManager().doRenderEntity(mc.player, pos.xCoord, pos.yCoord, pos.zCoord, mc.player.rotationYaw, mc.getRenderPartialTicks(), false);
    }

    @EventTarget
    public void sendPacket(EventPacketSend event) {
        boolean can = aimSync.getVal() ? (Client.moduleManager.getModule("AimBot").isEnabled() ? !((AimBot)Client.moduleManager.getModule("AimBot")).work : true) : true;

        if (mode.getVal().equals("FakeLag") && can) {
            if (mc.player != null && state == 1 && !(event.getPacket() instanceof CPacketAnimation)) {
                packets.add(event.getPacket());
                event.setCancelled(true);
            }
        }



    }



    @Override
    public void onDisable() {
        super.onDisable();
        if (mode.getVal().equals("FakeLag")) {
            for (Packet packet : packets) {
                mc.getConnection().sendPacket(packet);
            }
        }
        if (player != null && mc.world != null) {
            mc.world.removeEntityFromWorld(-7777);
            player = null;
        }
        packets.clear();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (mode.getVal().equals("Spin")){

        }
        if (mc.world != null && mc.player != null) {
            player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("6714531a-1c69-438e-b7d6-d6d41ca6838b"), "BOT"));
            player.copyLocationAndAnglesFrom(mc.player);
            player.inventory.copyInventory(mc.player.inventory);
            mc.world.addEntityToWorld(-7777, player);
        } else {
            setEnabled(false);
        }
    }
}
