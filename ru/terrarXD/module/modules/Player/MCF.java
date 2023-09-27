package ru.terrarXD.module.modules.Player;

import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Mouse;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;

/**
 * @author zTerrarxd_
 * @since 15:50 of 08.06.2023
 */
public class MCF extends Module {
    public boolean onFriend = true;

    public MCF() {
        super("MCF", Category.Player);
    }

    @EventTarget
    public void onUpdate(EventUpdate hook) {
        if (Mouse.isButtonDown(2) && mc.pointedEntity != null && mc.pointedEntity instanceof EntityLivingBase && this.onFriend) {
            this.onFriend = false;
            if(Client.friendsManager.isFriend(mc.objectMouseOver.entityHit.getName())){
                Client.friendsManager.delFriend(mc.objectMouseOver.entityHit.getName());
            }else {
                Client.friendsManager.addFriend(mc.objectMouseOver.entityHit.getName());
            }
        }

        if (!Mouse.isButtonDown(2)) {
            this.onFriend = true;
        }
    }
}
