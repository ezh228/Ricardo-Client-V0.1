package ru.terrarXD.module.modules.Combat;

import net.minecraft.util.math.Vec3d;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;

/**
 * @author zTerrarxd_
 * @since 17:16 of 07.06.2023
 */
public class AutoPeak extends Module {
    Vec3d pos = new Vec3d(0, 0, 0);
    public AutoPeak() {
        super("AutoPeak", Category.Combat);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if(mc.player != null){
            pos = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if(mc.player != null) {
            mc.player.motionX = (pos.xCoord - mc.player.posX) / mc.player.getDistance(pos.xCoord, pos.yCoord, pos.zCoord);
            mc.player.motionZ = (pos.zCoord - mc.player.posZ) / mc.player.getDistance(pos.xCoord, pos.yCoord, pos.zCoord);
        }
    }
}
