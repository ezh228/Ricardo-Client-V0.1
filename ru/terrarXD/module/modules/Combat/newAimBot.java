package ru.terrarXD.module.modules.Combat;

import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventPostUpdate;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.utils.TimerUtils;
import ru.terrarXD.shit.utils.Utils;

import java.util.ArrayList;
import java.util.Comparator;

public class newAimBot extends Module {

    public static boolean work = false;
    BooleanSetting silent;
    BooleanSetting multipoint;
    int ticks = 0;

    BooleanSetting autoShoot;

    FloatSetting fov;
    FloatSetting predict;

    TimerUtils timer;
    public newAimBot() {
        super("AimBot", Category.Combat);
        add(silent = new BooleanSetting("Silent", true));
        add(multipoint = new BooleanSetting("MultiPoint", true));
        add(fov = new FloatSetting("Fov", 0, 360, 100, 1));
        add(predict = new FloatSetting("Predict", 0, 10, 5.5f, 0.01f));
        add(autoShoot = new BooleanSetting("AutoShoot", true));
        timer = new TimerUtils();
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        if (mc.currentScreen instanceof GuiSleepMP){
            setEnabled(false);
        }
        TargetResult target = getTarget(predict.getVal());
        if (target == null){
            work = false;
            ticks = 0;
            return;
        }

        work = true;
        ticks++;

        aim(target, event);
        if (autoShoot.getVal() && ticks > 3){
            if (timer.hasReached((long) (1000l*0.05289941f))){
                shoot(target.getTarget());
            }
        }

    }

    @EventTarget
    public void onPostUpdate(EventPostUpdate event) {
        /*
        if (autoShoot.getVal() && ticks > 3){
            TargetResult target = getTarget(predict.getVal());
            if (target == null){
                work = false;
                return;
            }
            work = true;

            if (timer.hasReached((long) (1000l*0.05289941f))){
                shoot(target.getTarget());
            }
        }

         */
    }


    @EventTarget
    public void onRender2D(EventRender2D event){}



    public void  shoot(EntityLivingBase target){
        if (mc.player.getCooldownTracker().getCooldown(mc.player.inventory.getCurrentItem().getItem(), mc.getRenderPartialTicks()) == 0){
            mc.playerController.clickBlock(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ), EnumFacing.UP);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            timer.reset();

        }
    }

    public void aim(TargetResult target, EventUpdate event){

        float[] rotations = getAim(target, predict.getVal());
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


    public float[] getAim(TargetResult target, float predict){


        double posX = (target.getTarget().lastTickPosX - target.getTarget().posX) * predict;
        double posZ = (target.getTarget().lastTickPosZ - target.getTarget().posZ) * predict;
        double posY = 0;
        return Utils.getNeededRotations(target.getTarget().posX - posX + target.getPos().xCoord, target.getTarget().posY + target.getPos().yCoord+posY, target.getTarget().posZ - posZ + target.getPos().zCoord, mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    }

    public Vec3d getMultipointPos(EntityLivingBase target, float predict){
        if (mc.player.canEntityBeSeen(target)){
            return new Vec3d(0, target.getEyeHeight(), 0);
        }
        ArrayList<Vec3d> vec3ds = new ArrayList<>();
        double posX = (target.lastTickPosX - target.posX) * predict;
        double posZ = (target.lastTickPosZ - target.posZ) * predict;
        if (mc.player.canEntityBeSeen((target.posX - posX), (target.posY)+target.getEyeHeight(), (target.posZ - posZ))){
            return new Vec3d(0, target.getEyeHeight(), 0);
        }else if (multipoint.getVal()){
            final int[] ready = {0};
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    for (float x = -target.width/2; x < 0; x+=0.1f) {
                        for (float y = 0; y < target.height; y+=0.1f) {
                            for (float z = -target.width/2; z < 0; z++) {
                                if (mc.player.canEntityBeSeen((target.posX - posX)+x, (target.posY)+y, (target.posZ - posZ)+z)){
                                    vec3ds.add(new Vec3d(x, y, z));
                                }
                            }
                        }
                    }
                    ready[0]++;
                }
            }.start();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    for (float x = 0; x < target.width/2; x+=0.1f) {
                        for (float y = 0; y < target.height; y+=0.1f) {
                            for (float z = -target.width/2; z < 0; z++) {
                                if (mc.player.canEntityBeSeen((target.posX - posX)+x, (target.posY)+y, (target.posZ - posZ)+z)){
                                    vec3ds.add(new Vec3d(x, y, z));
                                }
                            }
                        }
                    }
                    ready[0]++;
                }
            }.start();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    for (float x = -target.width/2; x < 0; x+=0.1f) {
                        for (float y = 0; y < target.height; y+=0.1f) {
                            for (float z = 0; z < target.width/2; z++) {
                                if (mc.player.canEntityBeSeen((target.posX - posX)+x, (target.posY)+y, (target.posZ - posZ)+z)){
                                    vec3ds.add(new Vec3d(x, y, z));
                                }
                            }
                        }
                    }
                    ready[0]++;
                }
            }.start();
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    for (float x = 0; x < target.width/2; x+=0.1f) {
                        for (float y = 0; y < target.height; y+=0.1f) {
                            for (float z = 0; z < target.width/2; z++) {
                                if (mc.player.canEntityBeSeen((target.posX - posX)+x, (target.posY)+y, (target.posZ - posZ)+z)){
                                    vec3ds.add(new Vec3d(x, y, z));
                                }
                            }
                        }
                    }
                    ready[0]++;
                }
            }.start();
            long time = System.currentTimeMillis();
            while (ready[0] != 4){
                if (System.currentTimeMillis()- time > 50){
                    System.out.println("stop");
                    break;
                }
                System.out.println(ready[0]);
            }
            if (vec3ds.size()>0){
                vec3ds.sort(new Comparator<Vec3d>() {
                    @Override
                    public int compare(Vec3d o1, Vec3d o2) {
                        return (int) ((Utils.getDistance(o1, new Vec3d(0, target.getEyeHeight(), 0)) - Utils.getDistance(o2, new Vec3d(0, target.getEyeHeight(), 0))) *1000);
                    }
                });
                return vec3ds.get(0);
            }else {
                return null;
            }
        }
        return null;

    }


    public TargetResult getTarget(float predict){
        ArrayList<TargetResult> targetResults = new ArrayList<>();
        for (Entity entity : mc.world.loadedEntityList){
            if (entity instanceof EntityLivingBase && mc.player != entity && !entity.isInvisible()) {
                if (Utils.fov(entity, fov.getVal()) && !Client.friendsManager.isFriend(entity.getName()) && entity.getEntityId() != -7777) {
                    if (entity instanceof EntityPlayer){
                        Vec3d vec3d = getMultipointPos((EntityLivingBase) entity, predict);
                        if (vec3d!=null){
                            targetResults.add(new TargetResult((EntityLivingBase) entity, vec3d));
                        }
                    }

                }
            }
        }
        targetResults.sort(new Comparator<TargetResult>() {
            @Override
            public int compare(TargetResult o1, TargetResult o2) {
                return (int) (mc.player.getDistanceToEntity(o1.getTarget()) - mc.player.getDistanceToEntity(o2.getTarget()));
            }
        });
        if (targetResults.size() > 0){
            return targetResults.get(0);
        }
        return null;
    }





}
class TargetResult{
    EntityLivingBase target;
    Vec3d pos;
    public TargetResult(EntityLivingBase target, Vec3d pos){
        this.pos = pos;
        this.target = target;
    }

    public EntityLivingBase getTarget() {
        return target;
    }

    public Vec3d getPos() {
        return pos;
    }
}
