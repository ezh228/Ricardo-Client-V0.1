package ru.terrarXD.module.modules.Render;

import com.ibm.icu.text.ArabicShaping;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventPacketReceive;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.utils.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 1:11 of 19.06.2022
 */
public class XRay extends Module {
    //FloatSetting speed;
    public static ArrayList<BlockPos> xRayBlocks = new ArrayList<>();
    public XRay() {
        super("XRay", Category.Render);
        //add(speed = new FloatSetting("Speed", 1, 5000, 1000, 1));
    }

    @EventTarget
    public void onRender3D(EventRender3D event){

        for (BlockPos blockPos : xRayBlocks){
            //RenderUtils.blockEspFrame(blockPos, 227f / 255, 221f / 255, 58f / 255);

            int b = mc.world.getBlockState(blockPos).getMapColor(mc.world,blockPos).colorIndex;
            //Sera
            if(b == 38 || b == 46 || b == 42){
                RenderUtils.blockEspFrame(blockPos, 227f / 255, 221f / 255, 58f / 255);
            }else
                //Stone
                if(b == 36 || b == 44 || b == 40){
                    RenderUtils.blockEspFrame(blockPos, 0, 0, 0);
                }else
                    //Iron
                    if(b == 45 || b == 41 || b == 37){
                        RenderUtils.blockEspFrame(blockPos, 157f / 255, 156f / 255, 144f / 255);
                    }else {
                        //System.out.println(mc.world.getBlockState(blockPos).getMapColor(mc.world,blockPos).colorIndex);
                    }
        }
    }





    @EventTarget
    public void onUpdate(EventUpdate event){
        if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK){
            //System.out.println(mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock().getUnlocalizedName());
        }
        for (int i = 0; i < xRayBlocks.size(); i++) {
            int b = mc.world.getBlockState(xRayBlocks.get(i)).getMapColor(mc.world,xRayBlocks.get(i)).colorIndex;

            if (mc.world.getBlockState(xRayBlocks.get(i)).getBlock() != Blocks.STAINED_HARDENED_CLAY){
                //xRayBlocks.remove(i);
            }else if(b == 0){
                //xRayBlocks.remove(i);
            }


        }



    }
}
