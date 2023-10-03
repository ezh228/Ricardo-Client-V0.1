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
import ru.terrarXD.shit.event.events.EventPacketSend;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.event.events.EventSwingArm;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.utils.MathUtils;
import ru.terrarXD.shit.utils.RenderUtils;
import ru.terrarXD.shit.utils.TimerUtils;

import java.util.ArrayList;
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


    public FastPeak() {
        super("FastPeak", Category.Combat);
        add(radius = new FloatSetting("Radius", 0, 5, 1.3f, 0.1f));
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
    public void sendPacket(EventPacketSend event) {

        if (mc.player != null && state == 1 && !(event.getPacket() instanceof CPacketAnimation)) {
            packets.add(event.getPacket());
            event.setCancelled(true);
        }




    }


    @EventTarget
    public void onUpdate(EventUpdate event){
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
            setEnabled(false);
        }
        mc.player.noClip = true;

    }







    @Override
    public void onDisable() {
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
