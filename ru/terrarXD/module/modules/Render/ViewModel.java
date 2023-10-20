package ru.terrarXD.module.modules.Render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventTransformEatFirstPerson;
import ru.terrarXD.shit.settings.FloatSetting;

/**
 * @author zTerrarxd_
 * @date 17.10.2023 18:22
 */
public class ViewModel extends Module {
    FloatSetting posX;
    FloatSetting posY;
    FloatSetting posZ;

    public ViewModel() {
        super("ViewModel", Category.Render);
        add(posX = new FloatSetting("PosX", -2, 2, 0, 0.1f));
        add(posY = new FloatSetting("PosY", -2, 2, 0, 0.1f));
        add(posZ = new FloatSetting("PosZ", -2, 2, 0, 0.1f));

    }

    @EventTarget
    public void onArmsRender(EventTransformEatFirstPerson event){
        GlStateManager.translate(posX.getVal(), posY.getVal(), posZ.getVal());
    }
}
