package ru.terrarXD.module.modules.Movement;

import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventPacketReceive;
import ru.terrarXD.shit.event.events.EventPacketSend;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.event.events.MoveEvent;
import ru.terrarXD.shit.utils.TimeVec3d;
import ru.terrarXD.shit.utils.TimerUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author zTerrarxd_
 * @date 24.10.2023 18:09
 */
public class Fly extends Module {
    public Fly() {
        super("Fly", Category.Movement);
    }



    private int teleportId;
    private CPacketPlayer.Position startingOutOfBoundsPos;
    private ArrayList<CPacketPlayer> packets = new ArrayList<CPacketPlayer>();;
    private Map<Integer, TimeVec3d> posLooks = new ConcurrentHashMap<Integer, TimeVec3d>();
    private int antiKickTicks;
    private int vDelay;
    private int hDelay;
    private boolean limitStrict;
    private int limitTicks;
    private int jitterTicks;
    private boolean oddJitter;
    double speedX;
    double speedY;
    double speedZ;
    private float postYaw;
    private float postPitch;
    private int factorCounter;
    private TimerUtils intervalTimer = new TimerUtils();
    private static final Random random;

    public  boolean phase = true;
    public boolean packetMode = true;


    public boolean isPlayerMoving() {
        return mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown();
    }
    public double[] directionSpeed(double speed) {
        float forward = mc.player.movementInput.field_192832_b;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }

        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[]{posX, posZ};
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null) {
            toggle();
            return;
        }
        if (mc.player.ticksExisted % 20 == 0) {
            cleanPosLooks();
        }
        mc.player.setVelocity(0.0, 0.0, 0.0);
        if (teleportId <= 0) {
            startingOutOfBoundsPos = new CPacketPlayer.Position(randomHorizontal(), 1.0, randomHorizontal(), mc.player.onGround);
            packets.add((CPacketPlayer) startingOutOfBoundsPos);
            mc.player.connection.sendPacket((Packet) startingOutOfBoundsPos);
            return;
        }
        final boolean phasing = checkCollisionBox();
        //final boolean phasing = false;
        speedX = 0.0;
        speedY = 0.0;
        speedZ = 0.0;
        if (mc.gameSettings.keyBindJump.isKeyDown() && (hDelay < 1)) {
            if (mc.player.ticksExisted % 20 == 0) {
                speedY = -0.032;
            } else speedY = 0.062;

            antiKickTicks = 0;
            vDelay = 5;
        } else if (mc.gameSettings.keyBindSneak.isKeyDown() && (hDelay < 1)) {
            speedY = -0.062;
            antiKickTicks = 0;
            vDelay = 5;
        }
        if (!mc.gameSettings.keyBindSneak.isKeyDown() || !mc.gameSettings.keyBindJump.isKeyDown()) {
            if (isPlayerMoving()) {
                final double[] dir = directionSpeed(((phasing && phase) ? 0.062 : 0.26) * 1f);
                if ((dir[0] != 0.0 || dir[1] != 0.0) && (vDelay < 1)) {
                    speedX = dir[0];
                    speedZ = dir[1];
                    hDelay = 5;
                }
            }
        }
        if (phasing && ((phase && mc.player.moveForward != 0.0) || (mc.player.moveStrafing != 0.0 && speedY != 0.0))) {
            speedY /= 2.5;
        }

        float rawFactor = 1.7f;

        int factorInt = (int) Math.floor(rawFactor);
        ++factorCounter;
        if (factorCounter > (int) (20.0 / ((rawFactor - (double) factorInt) * 20.0))) {
            ++factorInt;
            factorCounter = 0;
        }
        for (int i = 1; i <= factorInt; ++i) {
            mc.player.setVelocity(speedX * i, speedY * i, speedZ * i);
            sendPackets(speedX * i, speedY * i, speedZ * i, "BYPASS", true, false);
        }
        speedX = mc.player.motionX;
        speedY = mc.player.motionY;
        speedZ = mc.player.motionZ;

        --vDelay;
        --hDelay;
        ++limitTicks;
        ++jitterTicks;
        if (limitTicks > 3) {
            limitTicks = 0;
            limitStrict = !limitStrict;
        }
        if (jitterTicks > 7) {
            jitterTicks = 0;
        }
    }

    private void sendPackets(final double x, final double y, final double z, final String Client, final boolean sendConfirmTeleport, final boolean sendExtraCT) {
        final Vec3d nextPos = new Vec3d(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
        final Vec3d bounds = getBoundsVec(x, y, z, Client);
        final CPacketPlayer nextPosPacket = (CPacketPlayer)new CPacketPlayer.Position(nextPos.xCoord, nextPos.yCoord, nextPos.zCoord, mc.player.onGround);
        packets.add(nextPosPacket);
        mc.player.connection.sendPacket((Packet)nextPosPacket);
        final CPacketPlayer boundsPacket = (CPacketPlayer)new CPacketPlayer.Position(bounds.xCoord, bounds.yCoord, bounds.zCoord, mc.player.onGround);
        packets.add(boundsPacket);
        mc.player.connection.sendPacket((Packet)boundsPacket);
        if (sendConfirmTeleport) {
            ++teleportId;
            if (sendExtraCT) {
                mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(teleportId - 1));
            }
            mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(teleportId));
            posLooks.put(teleportId, new TimeVec3d(nextPos.xCoord, nextPos.yCoord, nextPos.zCoord, System.currentTimeMillis()));
            if (sendExtraCT) {
                mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(teleportId + 1));
            }
        }
    }

    private Vec3d getBoundsVec(final double x, final double y, final double z, final String Mode) {
        switch (Mode) {
            case "UP":{

            }
            case "ZERO":{

            }
            case "BYPASS":{
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY,mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY - 1,mc.player.posZ, mc.player.onGround));
            }
            default: {
                return new Vec3d(mc.player.posX + x, mc.player.posY - 1337.0, mc.player.posZ + z);
            }

        }
    }

    public double randomHorizontal() {
        final int randomValue = random.nextInt(29000000) + 500;
        if (random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }

    public static double randomLimitedVertical() {
        int randomValue = random.nextInt(22);
        randomValue += 70;
        if (random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }

    public static double randomLimitedHorizontal() {
        final int randomValue = random.nextInt(10);
        if (random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }

    private void cleanPosLooks() {

        posLooks.forEach((tp, timeVec3d) -> {
            if (System.currentTimeMillis() - timeVec3d.getTime() > TimeUnit.SECONDS.toMillis(30L)) {
                //posLooks.remove(tp);
            }
        });
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) {
            toggle();
            return;
        }
        packets.clear();
        posLooks.clear();
        teleportId = 0;
        vDelay = 0;
        hDelay = 0;
        postYaw = -400.0f;
        postPitch = -400.0f;
        antiKickTicks = 0;
        limitTicks = 0;
        jitterTicks = 0;
        speedX = 0.0;
        speedY = 0.0;
        speedZ = 0.0;
        oddJitter = false;
        startingOutOfBoundsPos = null;
        startingOutOfBoundsPos = new CPacketPlayer.Position(randomHorizontal(), 1.0, randomHorizontal(), mc.player.onGround);
        packets.add((CPacketPlayer)startingOutOfBoundsPos);
        mc.player.connection.sendPacket((Packet)startingOutOfBoundsPos);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.setVelocity(0.0, 0.0, 0.0);
        }
    }

    @EventTarget
    public void onPacketReceive(EventPacketReceive event ) {
        if (true)
            return;
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (!(mc.currentScreen instanceof GuiDownloadTerrain)) {
                final SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
                if (mc.player.isEntityAlive()) {
                    if (teleportId <= 0) {
                        teleportId = ((SPacketPlayerPosLook) event.getPacket()).getTeleportId();
                    }
                    else if (mc.world.isBlockLoaded(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ), false)) {
                        if (posLooks.containsKey(packet.getTeleportId())) {
                            final TimeVec3d vec = posLooks.get(packet.getTeleportId());
                            if (vec.xCoord == packet.getX() && vec.yCoord == packet.getY() && vec.zCoord == packet.getZ()) {
                                posLooks.remove(packet.getTeleportId());
                                event.setCancelled(true);

                                return;
                            }
                        }
                    }
                }
                //((ISPacketPlayerPosLook)packet).setYaw(mc.player.rotationYaw);
                //((ISPacketPlayerPosLook)packet).setPitch(mc.player.rotationPitch);
                packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.X_ROT);
                packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.Y_ROT);
                teleportId = packet.getTeleportId();
            }
            else {
                teleportId = 0;
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent event) {
        if (teleportId <= 0) return;


        event.motionX = speedX;
        event.motionY = speedY;
        event.motionZ = speedZ;

        ///if ((!phase.getValue().equalsIgnoreCase("NONE") && phase.getValue().equalsIgnoreCase("VANILLA")) || checkCollisionBox()) {
            mc.player.noClip = true;
       // }


    }

    private boolean checkCollisionBox() {
        /*
        return !mc.world.getCollisionBoxes((Entity) mc.player, mc.player.getEntityBoundingBox().expand(0.0, 0.0, 0.0)).isEmpty() || !mc.world.getCollisionBoxes((Entity) mc.player, mc.player.getEntityBoundingBox().offset(0.0, 2.0, 0.0).contract(0.0, 1.99, 0.0)).isEmpty();


         */
        return true;
    }

    @EventTarget
    public void onPacketSend( EventPacketSend event ) {
        if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
            event.setCancelled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer) event.getPacket();
            if (packets.contains(packet)) {
                packets.remove(packet);
                return;
            }
            event.setCancelled(true);
        }
    }
    static {
        random = new Random();
    }
}
