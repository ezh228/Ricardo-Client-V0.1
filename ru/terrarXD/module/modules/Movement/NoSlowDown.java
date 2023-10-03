package ru.terrarXD.module.modules.Movement;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.utils.Utils;

/**
 * @author zTerrarxd_
 * @since 21:42 of 25.04.2023
 */
public class NoSlowDown extends Module {
    public NoSlowDown() {
        super("NoSlowDown", Category.Movement);
    }

    @EventTarget
    public void onUpdate(EventUpdate event){


        if(mc.player.isPotionActive(Potion.getPotionById(2))){
            if(mc.player.onGround){
                Utils.setSpeed(0.22f);
            }else {
                Utils.setSpeed(0.25f);
            }

        }



    }
}
