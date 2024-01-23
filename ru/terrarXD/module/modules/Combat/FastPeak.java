package ru.terrarXD.module.modules.Combat;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.*;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.settings.ModeSetting;
import ru.terrarXD.shit.utils.MathUtils;
import ru.terrarXD.shit.utils.RenderUtils;
import ru.terrarXD.shit.utils.TimerUtils;
import ru.terrarXD.shit.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class FastPeak extends Module {

    int state = 0;
    //0 - отправка пакетов
    //1 - принятие и сохранение
    //2 - ожидание
    ArrayList<Packet> packets = new ArrayList<>();
    Vec3d pos = new Vec3d(0, 0, 0);
    FloatSetting radius;

    public EntityPlayer player;
    ModeSetting mode;


    public FastPeak() {
        super("FastPeak", Category.Combat);
        ArrayList<String> modes = new ArrayList<>();
        modes.add("Radius");
        modes.add("Smart");
        add(mode = new ModeSetting("Mode", modes, "Smart"));
        add(radius = (FloatSetting) new FloatSetting("Radius", 0, 5, 1.3f, 0.1f).setVisible(()->mode.getVal().equals("Radius")));
    }


    @EventTarget
    public void onSwing(EventSwingArm event){
        if (event.getHand() == EnumHand.MAIN_HAND){
            state = 0;
        }
    }

    @EventTarget
    public void render3D(EventRender3D event){
        RenderUtils.drawCircle3D(pos.xCoord, pos.yCoord, pos.zCoord, radius.getVal(), Client.getColor());
    }

    @EventTarget
    public void render2D(EventRender2D event){
        int now_count = getCountOfObstrels(new Vec3d(mc.player.posX, mc.player.posY+mc.player.getEyeHeight(), mc.player.posZ));
        float predict = 60f;
        double posX = (mc.player.lastTickPosX - mc.player.posX) * predict;
        double posZ = (mc.player.lastTickPosZ - mc.player.posZ) * predict;
        int will_count = getCountOfObstrels(new Vec3d(mc.player.posX+posX, mc.player.posY+mc.player.getEyeHeight(), mc.player.posZ+posZ));
        Fonts.main_25.drawString(""+now_count, 100, 100, -1);
        Fonts.main_25.drawString(""+will_count, 100, 120, -1);

    }

    @EventTarget
    public void sendPacket(EventPacketSend event) {

        if (mc.player != null && state == 1 && !(event.getPacket() instanceof CPacketAnimation)) {
            packets.add(event.getPacket());
            event.setCancelled(true);
        }




    }
    public int getCountOfObstrels(Vec3d playerPos){
        int count = 0;
        ArrayList<Vec3d> vec3ds = new ArrayList<>();
        vec3ds.add(new Vec3d(playerPos.xCoord-3, playerPos.yCoord, playerPos.zCoord));
        vec3ds.add(new Vec3d(playerPos.xCoord+3, playerPos.yCoord, playerPos.zCoord));
        vec3ds.add(new Vec3d(playerPos.xCoord, playerPos.yCoord, playerPos.zCoord-3));
        vec3ds.add(new Vec3d(playerPos.xCoord, playerPos.yCoord, playerPos.zCoord+3));
        for (Vec3d vec3d : vec3ds){
            if (Utils.canEntityBeSeen(playerPos,vec3d)){
                count++;
            }
        }
        return count;
    }



    @EventTarget
    public void onUpdate(EventUpdate event){
        if (mode.getVal().equals("Smart")){
            int now_count = getCountOfObstrels(new Vec3d(mc.player.posX, mc.player.posY+mc.player.getEyeHeight(), mc.player.posZ));
            float predict = 60f;
            double posX = (mc.player.lastTickPosX - mc.player.posX) * predict;
            double posZ = (mc.player.lastTickPosZ - mc.player.posZ) * predict;
            player.setPosition(mc.player.posX+posX, mc.player.posY+mc.player.getEyeHeight(), mc.player.posZ+posZ);
            int will_count = getCountOfObstrels(new Vec3d(mc.player.posX+posX, mc.player.posY+mc.player.getEyeHeight(), mc.player.posZ+posZ));
            if (will_count > now_count){
                state = 1;
                player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
            }else {
                state = 0;
            }
        }


        if (mc.player.getDistance(pos.xCoord, pos.yCoord, pos.zCoord) != 0){
            state = 1;
        }
        if (mc.player.getDistance(pos.xCoord, pos.yCoord, pos.zCoord) > radius.getVal()){
            state = 0;
        }
        if (mc.currentScreen != null){
            state = 0;
        }

        if (state == 0){
            for (Packet packet : packets){
                mc.getConnection().sendPacket(packet);
            }
            packets.clear();
            state = 1;
            player.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);
            pos = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
            state = 3;
        }
        mc.player.noClip = true;

    }







    @Override
    public void onDisable() {
        if (mc.world == null || mc.player == null){
            return;
        }
        super.onDisable();

        for (Packet packet : packets) {
            mc.getConnection().sendPacket(packet);
        }

        if (player != null && mc.world != null) {
            mc.world.removeEntityFromWorld(-7777);
            player = null;
        }
        packets.clear();
    }

    @Override
    public void onEnable() {
        if (mc.world == null || mc.player == null){
            setEnabled(false);
            return;
        }
        super.onEnable();

        pos = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);

        state = 3;

        if (mc.world != null && mc.player != null) {
            player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("6714531a-1c69-438e-b7d6-d6d41ca6838b"), "FastPeak"));
            player.copyLocationAndAnglesFrom(mc.player);
            player.inventory.copyInventory(mc.player.inventory);
            mc.world.addEntityToWorld(-7777, player);
        } else {
            setEnabled(false);
        }
    }
}
