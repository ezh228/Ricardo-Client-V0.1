package ru.terrarXD.module.modules.Player;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.utils.RenderUtils;
import ru.terrarXD.shit.utils.TimerUtils;
import ru.terrarXD.shit.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author zTerrarxd_
 * @since 16:13 of 08.06.2023
 */
public class AutoMine extends Module {
    TimerUtils timer;
    public AutoMine() {
        super("AutoMine", Category.Player);
        timer = new TimerUtils();
    }
    BlockPos blockPos;

    @EventTarget
    public void onRender3D(EventRender3D event){
        RenderUtils.blockEspFrame(blockPos, 1, 0, 0);
    }



    @EventTarget
    public void onUpdate(EventUpdate event){
        for (Entity entity : mc.world.loadedEntityList){
            if(entity.getDistanceToEntity(mc.player) <= 4 && entity instanceof EntityArmorStand && (entity.getCustomNameTag().contains("✹") || entity.getCustomNameTag().contains("✘")) ){
                ArrayList<BlockPos> blockPoss = new ArrayList<>();
                int i = (int) entity.posX;
                int j = (int) entity.posZ;
                for (int x = -2; x < 2; x++) {
                    for (int z = -2; z < 2; z++) {
                        Block block = mc.world.getBlockState(new BlockPos(i + x, entity.posY, j + z)).getBlock();
                        if(block != Blocks.AIR && (block == Blocks.STAINED_HARDENED_CLAY || block == Blocks.LOG || block == Blocks.LOG2)){
                            blockPoss.add(new BlockPos(i + x, entity.posY, j + z));
                        }

                    }
                }
                blockPoss.sort(new Comparator<BlockPos>() {
                    @Override
                    public int compare(BlockPos o1, BlockPos o2) {
                        return (int) ((entity.getDistanceSq(o1) - entity.getDistanceSq(o2)));
                    }
                });
                blockPos = blockPoss.get(0);


                if(blockPos != null){
                    if (mc.player.getCooldownTracker().getCooldown(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem(), mc.getRenderPartialTicks()) == 0){
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        EntityArmorStand entityArmorStand = (EntityArmorStand) entity;
                        mc.getConnection().sendPacket(new CPacketUseEntity(entity, EnumHand.MAIN_HAND));
                        mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.UP));
                        timer.reset();
                    }

                    float[] rotation =  Utils.getNeededRotations(entity.posX, entity.posY + 0.37f, entity.posZ, (float) mc.player.posX, (float)mc.player.posY+mc.player.getEyeHeight(),(float) mc.player.posZ);
                    event.setRotationYaw(rotation[0]);
                    event.setRotationPitch(rotation[1]);
                }

            }
        }
    }
}
