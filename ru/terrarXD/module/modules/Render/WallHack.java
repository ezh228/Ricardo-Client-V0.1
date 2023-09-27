package ru.terrarXD.module.modules.Render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.settings.BooleanSetting;

/**
 * @author zTerrarxd_
 * @since 21:05 of 30.05.2023
 */
public class WallHack extends Module {
    BooleanSetting friends;
    public WallHack() {
        super("WallHack", Category.Render);
        add(friends = new BooleanSetting("Frinds", true));
    }

    void render(Entity entity, float ticks) {
        try {
            if (entity == null || entity == mc.player) {
                return;
            }
            if (entity == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
                return;
            }
            mc.entityRenderer.disableLightmap();
            mc.getRenderManager().renderEntityStatic(entity, ticks, false);
            mc.entityRenderer.enableLightmap();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D event){
        GlStateManager.clear((int)256);
        RenderHelper.enableStandardItemLighting();
        for (Entity entity : mc.world.loadedEntityList) {
            if (entity instanceof EntityPlayer && entity != mc.getRenderViewEntity()) {
                if (Client.friendsManager.isFriend(entity.getName()) && friends.getVal()) {
                    this.render(entity, mc.getRenderPartialTicks());
                    continue;
                }
                this.render(entity, mc.getRenderPartialTicks());
                continue;
            }
        }
    }
}
