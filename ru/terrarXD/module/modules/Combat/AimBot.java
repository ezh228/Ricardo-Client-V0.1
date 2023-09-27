package ru.terrarXD.module.modules.Combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.*;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventPostUpdate;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.utils.MathUtils;
import ru.terrarXD.shit.utils.TimerUtils;
import ru.terrarXD.shit.utils.Utils;

import javax.vecmath.Matrix4f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author zTerrarxd_
 * @since 15:57 of 26.04.2023
 */
public class AimBot extends Module {
    BooleanSetting bots;
    BooleanSetting silent;
    FloatSetting predict;
    BooleanSetting selfPredict;
    FloatSetting fov;
    BooleanSetting autoShoot;

    FloatSetting minCPS;
    FloatSetting maxCPS;

    TimerUtils timer;

    public boolean work = false;

    EntityLivingBase entityLivingBase;




    public AimBot() {
        super("AimBot", Category.Combat);
        add(bots = new BooleanSetting("Bots", false));
        add(silent = new BooleanSetting("Silent", true));
        add(predict = new FloatSetting("Predict", 0, 10, 5.5f, 0.1f));
        add(fov = new FloatSetting("Fov", 0, 360, 90, 1));
        add(autoShoot = new BooleanSetting("AutoShoot", true));
        add(minCPS = (FloatSetting) new FloatSetting("MinCPS", 0, 30, 13, 1).setVisible(()->autoShoot.getVal()));
        add(maxCPS = (FloatSetting) new FloatSetting("MaxCPS", 0, 30, 16, 1).setVisible(()->autoShoot.getVal()));

        add(selfPredict = new BooleanSetting("SelfPredict", false));
        timer = new TimerUtils();
    }
    public float getDistance(Vec3d vec3d1 , Vec3d vec3d2){
        float f = (float)(vec3d1.xCoord - vec3d2.xCoord);
        float f1 = (float)(vec3d1.yCoord - vec3d2.yCoord);
        float f2 = (float)(vec3d1.zCoord - vec3d2.zCoord);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public Vec3d getPos(EntityLivingBase target, Vec3d posPlayer){

        ArrayList<Vec3d> vec3ds = new ArrayList<>();
        for (float y = 0; y < target.height; y+=0.35f) {
            for (float x = 0; x < target.width; x+=0.1f) {
                for (float z = 0; z < target.width; z+=0.1f) {
                    if (Utils.canEntityBeSeen(new Vec3d(target.posX + x, target.posY + y, target.posZ + z), posPlayer)){
                        vec3ds.add(new Vec3d(x, y, z));
                    }
                }
            }
        }

        vec3ds.sort(new Comparator<Vec3d>() {
            @Override
            public int compare(Vec3d o1, Vec3d o2) {
                float d =  (getDistance(o1, new Vec3d(0, target.getEyeHeight(), 0))) - (getDistance(o2, new Vec3d(0, target.getEyeHeight(), 0)));
                return (int) (d * 100000);
            }
        });
        if(vec3ds.size() == 0){
            return null;
        }
        Vec3d pos = vec3ds.get(0);
        pos.xCoord = pos.xCoord+target.posX- (target.lastTickPosX-target.posX)*predict.getVal();
        pos.yCoord = pos.yCoord+target.posY;
        pos.zCoord = pos.zCoord+target.posZ-(target.lastTickPosZ-target.posZ)*predict.getVal();

        return pos;
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        //2658
        work = false;
        EntityLivingBase target = getTarget();
        if (target == null){
            work = false;
            return;
        }
        if(mc.player.height < 1){
            work = false;
            return;
        }
        entityLivingBase = target;
        work = true;
        Vec3d mepos = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
        mepos = new Vec3d(mc.player.posX, mc.player.posY+mc.player.getEyeHeight(), mc.player.posZ);
        if(selfPredict.getVal()){
            mepos = getPredict(mc.player, mc.getRenderPartialTicks());
        }

        EntityPlayer player = mc.player;
        if (Client.moduleManager.getModule("AntiAim").isEnabled()){
            //player = ((AntiAim)Client.moduleManager.getModule("AntiAim")).player;
        }
        Vec3d targetpos = getPos((EntityLivingBase) target, new Vec3d(player.posX, player.posY+player.getEyeHeight(), player.posZ));
        if (mc.player.getHeldItem(EnumHand.MAIN_HAND).itemDamage == 720 || mc.player.getHeldItem(EnumHand.MAIN_HAND).itemDamage == 721){
            targetpos.yCoord+=mc.player.getDistanceToEntity(target) / 100;
        }

        if (targetpos == null){
            return;
        }


        float[] rotations = Utils.getNeededRotations(targetpos.xCoord, targetpos.yCoord, targetpos.zCoord, mepos.xCoord, mepos.yCoord, mepos.zCoord);
        if (silent.getVal()){
            event.setRotationYaw(rotations[0]);
            event.setRotationPitch(rotations[1]);
            mc.player.rotationYawHead = rotations[0];
            mc.player.rotationPitchHead = rotations[1];
            mc.player.renderYawOffset = rotations[0];
        }else {
            mc.player.rotationYaw = rotations[0];
            mc.player.rotationPitch = rotations[1];
        }

    }

    @EventTarget
    public void onRender2D(EventRender2D event){

        EntityLivingBase target = getTarget();
        if (target == null){
            return;
        }
        //System.out.println("hasTarget");
        if (autoShoot.getVal()){
            if(timer.hasReached((long) MathUtils.getRandomInRange(1000/maxCPS.getVal(), 1000/minCPS.getVal()))){
            //if(mc.player.getCooldownTracker().getCooldown(mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem(), mc.getRenderPartialTicks()) == 0){
                if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ)).getBlock() != Blocks.AIR){
                    mc.playerController.clickBlock(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ), EnumFacing.UP);
                }
                //System.out.println("shoot");
                mc.player.swingArm(EnumHand.MAIN_HAND);
                if (mc.player.getDistanceToEntity(target) < 4){
                    mc.playerController.attackEntity(mc.player, target);
                }
                timer.reset();
            }

        }
    }


    @EventTarget
    public void onPostUpdate(EventPostUpdate event){

    }


    public EntityLivingBase getTarget(){
        ArrayList<Entity> entities = new ArrayList<>(mc.world.loadedEntityList);
        entities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return (int) (Utils.fovFromEntity(o1) - Utils.fovFromEntity(o2));
            }
        });
        EntityPlayer player = mc.player;
        /*
        if (Client.moduleManager.getModule("AntiAim").isEnabled()){
            player = ((AntiAim)Client.moduleManager.getModule("AntiAim")).player;
        }

         */
        for (Entity entity : entities) {
            if (entity instanceof EntityLivingBase && entity.getEntityId() != -7777 && !Client.friendsManager.isFriend(entity.getName()) && !entity.isInvisible()) {
                if ((entity instanceof EntityZombie && bots.getVal()) || (entity instanceof EntityPlayer && entity != mc.player) && ((EntityLivingBase  ) entity).getHealth()> 0) {
                    if (Utils.fov(entity, fov.getVal())){

                        Vec3d vec3d = getPos((EntityLivingBase) entity, new Vec3d(player.posX, player.posY+player.getEyeHeight(), player.posZ));
                        if (vec3d != null){
                            return (EntityLivingBase) entity;
                        }


                    }

                }
            }
        }
        return null;
    }


    public Vec3d getPredict(Entity entity, float pred){
        return new Vec3d((entity.posX - (entity.lastTickPosX-entity.posX)*pred), entity.posY,  (entity.posZ - (entity.lastTickPosZ-entity.posZ)*pred));
    }


}
