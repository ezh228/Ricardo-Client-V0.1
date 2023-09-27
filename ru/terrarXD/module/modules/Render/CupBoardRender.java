package ru.terrarXD.module.modules.Render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.BooleanSetting;

/**
 * @author zTerrarxd_
 * @since 12:36 of 13.05.2023
 */
public class CupBoardRender extends Module {
    BooleanSetting glow;
    BooleanSetting wallHack;
    public CupBoardRender() {
        super("CupBoardRender", Category.Render);
        add(glow = new BooleanSetting("Glow", true));
        add(wallHack = new BooleanSetting("WallHack", false));

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
    public void onUpdate(EventUpdate event){


        for (Entity entity : mc.world.loadedEntityList){
            if(entity instanceof EntityArmorStand && glow.getVal()){
                if(!entity.isGlowing()){
                    entity.setGlowing(true);
                }
            }

            if(entity instanceof  EntityArmorStand && !glow.getVal()){
                if(entity.isGlowing()){
                    entity.setGlowing(false);
                }
            }
        }

    }

    @EventTarget
    public void onRender3D(EventRender3D event){
        ;
        GlStateManager.clear((int)256);
        RenderHelper.enableStandardItemLighting();
        if(wallHack.getVal()){
            for (Entity entity : mc.world.loadedEntityList) {
                if (entity instanceof EntityArmorStand && entity != mc.getRenderViewEntity()) {
                    this.render(entity, mc.getRenderPartialTicks());
                }
            }
        }

    }
}
