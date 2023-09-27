package ru.terrarXD.module.modules.Combat;

import net.minecraft.entity.player.EntityPlayer;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;

/**
 * @author zTerrarxd_
 * @since 20:20 of 25.04.2023
 */
public class AntiBot extends Module {
    public AntiBot() {
        super("AntiBot", Category.Combat);
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        for (int i = 0; i < mc.world.loadedEntityList.size(); i++) {
            if (mc.world.loadedEntityList.get(i) instanceof EntityPlayer){
                EntityPlayer player = (EntityPlayer) mc.world.loadedEntityList.get(i);
                if (player.isInvisible() && player != mc.player){
                    mc.world.removeEntity(player);
                }
            }

        }

    }
}
