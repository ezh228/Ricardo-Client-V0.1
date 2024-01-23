package ru.terrarXD.shit.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import ru.terrarXD.Client;
import ru.terrarXD.shit.Theme;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 21:43 of 25.04.2023
 */
public class Utils {
    public  static Minecraft mc = Minecraft.getMinecraft();

    public static void playSound(final String url) {

        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();

                AudioInputStream inputStream = AudioSystem.getAudioInputStream(Client.class.getResourceAsStream("/assets/minecraft/client/" + url));
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {

            }
        }).start();
    }

    public static void setSpeed(double speed) {
        double forward = MovementInput.field_192832_b;
        double strafe = MovementInput.moveStrafe;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            mc.player.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            mc.player.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }

    public static void openWebpage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static double fovFromEntity(Entity en) {
        return ((double)(Minecraft.getMinecraft().player.rotationYaw - fovToEntity(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
    }
    public static float fovToEntity(Entity ent) {
        double x = ent.posX - Minecraft.getMinecraft().player.posX;
        double z = ent.posZ - Minecraft.getMinecraft().player.posZ;
        double yaw = Math.atan2(x, z) * 57.2957795D;
        return (float)(yaw * -1.0D);
    }

    public static ArrayList<File> listFilesForFolder(final File folder) {
        ArrayList<File> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                /*
                for (File file : listFilesForFolder(fileEntry)){
                    files.add(file);
                }
                
                 */
            } else {
                files.add(fileEntry);
            }
        }
        return files;
    }
    public static boolean fov(Entity entity, float fov) {
        fov = (float)((double)fov * 0.5D);
        double v = ((double)(Minecraft.getMinecraft().player.rotationYaw - fovToEntity(entity)) % 360.0D + 540.0D) % 360.0D - 180.0D;
        return v >= 0.0D && v <= (double)fov || (double)(-fov) <= v && v <= 0.0D;
    }
    public static boolean canEntityBeSeen(Vec3d pos1, Vec3d pos2)
    {
        return mc.world.rayTraceBlocks(new Vec3d(pos1.xCoord, pos1.yCoord, pos1.zCoord), new Vec3d(pos2.xCoord, pos2.yCoord, pos2.zCoord), false, true, false) == null;
    }

    public static double getDistance(Vec3d vec3d1, Vec3d vec3d2){

        double d0 = vec3d1.xCoord - vec3d2.xCoord;
        double d1 = vec3d1.yCoord - vec3d2.yCoord;
        double d2 = vec3d1.zCoord - vec3d2.zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;

    }
    public static Vec3d interpolateEntity(final Entity entity) {
        final double partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks);
    }

    public static   float[] getNeededRotations(final double x, final double y, final double z, double playerX, double playerY, double playerZ) {
        double diffX = x - (playerX);
        double diffZ = z - (playerZ);
        double diffY = y - (playerY);
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (((Math.atan2(diffZ, diffX) * 180.0 / Math.PI) - 90.0f));
        float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0 / Math.PI));
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        return new float[] { yaw, pitch };
    }
    public static double[] getDirection(final float yaw) {
        return new double[] { -Math.sin(Math.toRadians(yaw)), Math.cos(Math.toRadians(yaw)) };
    }
}
