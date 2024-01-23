package ru.terrarXD.module.modules.Combat;

import net.minecraft.client.gui.GuiSleepMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventPostUpdate;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.utils.*;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Comparator;

public class AimBot extends Module {

    public static boolean work = false;
    BooleanSetting silent;
    BooleanSetting multipoint;

    BooleanSetting bots;
    int ticks = 0;

    BooleanSetting bow;
    BooleanSetting renderCross;

    BooleanSetting autoShoot;
    FloatSetting cps;
    BooleanSetting checkCoolDown;

    BooleanSetting multiTheadering;
    FloatSetting accuracy;

    FloatSetting fov;
    BooleanSetting autoPredict;
    FloatSetting predict;

    TimerUtils timer;
    TargetResult penis_slona;
    public TimerUtils timerLastSee;
    public AimBot() {
        super("AimBot", Category.Combat);
        add(bots = new BooleanSetting("Bots", false));
        add(silent = new BooleanSetting("Silent", true));
        add(bow = new BooleanSetting("Bow", false));
        add(multipoint = new BooleanSetting("MultiPoint", true));
        add(multiTheadering = (BooleanSetting) new BooleanSetting("MultiTheadering", true).setVisible(()->multipoint.getVal()));
        add(accuracy = new FloatSetting("Accuracy", 0.01f, 0.3f, 0.1f, 0.01f));
        add(fov = new FloatSetting("Fov", 0, 360, 100, 1));
        add(autoPredict = new BooleanSetting("AutoPredict", true));
        add(predict = (FloatSetting) new FloatSetting("Predict", 0, 10, 5.5f, 0.01f).setVisible(()-> !autoPredict.getVal()));
        add(autoShoot = new BooleanSetting("AutoShoot", true));
        add(checkCoolDown = (BooleanSetting) new BooleanSetting("CheckCoolDown", false).setVisible(()->autoShoot.getVal()));
        add(cps = (FloatSetting) new FloatSetting("CPS", 0, 20, 15, 1f).setVisible(()->autoShoot.getVal()));
        add(renderCross = (BooleanSetting) new BooleanSetting("Render Cross", true).setVisible(()->silent.getVal()));
        timer = new TimerUtils();
        timerLastSee = new TimerUtils();
    }
    AnimationUtils animPosX = new AnimationUtils(0, 0, 0.1f);
    AnimationUtils animPosY = new AnimationUtils(0, 0, 0.1f);
    AnimationUtils animEndX = new AnimationUtils(0, 0, 0.1f);
    AnimationUtils animEndY = new AnimationUtils(0, 0, 0.1f);

    @EventTarget
    public void render2D(EventRender2D event){
        if (penis_slona == null || !renderCross.getVal()){
            return;
        }
        GL11.glPushMatrix();
        float partialTicks = mc.getRenderPartialTicks();
        int scaleFactor = ScaledResolution.getScaleFactor();
        double scaling = scaleFactor / Math.pow(scaleFactor, 2.0D);
        GL11.glScaled(scaling, scaling, scaling);
        Color onecolor = new Color(0, 0, 0);
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color =  c.getRGB();
        float scale = 1F;
        float upscale = 1.0F / scale;
        RenderManager renderMng = mc.getRenderManager();
        EntityRenderer entityRenderer = mc.entityRenderer;


                double x = penis_slona.getTarget().posX+penis_slona.getPos().xCoord-((penis_slona.getTarget().lastTickPosX - penis_slona.getTarget().posX) * (predict.getVal()));
                double y = penis_slona.getTarget().posY+penis_slona.getPos().yCoord;
                double z = penis_slona.getTarget().posZ+penis_slona.getPos().zCoord-((penis_slona.getTarget().lastTickPosZ - penis_slona.getTarget().posZ) * (predict.getVal()));
                double width = 0.1d;
                double height = 0.1d;
                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                Vector3d[] vectors = new Vector3d[]{new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                        new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                        if (position == null)
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position != null) {
                    entityRenderer.setupOverlayRendering();
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;
                    animPosX.to = (float) posX;
                    animPosY.to = (float) posY;
                    animEndX.to = (float) endPosX;
                    animEndY.to = (float) endPosY;
                    //

                    //RenderUtils.drawRect(posX, posY, posX + 1, endPosY, col);
                    //RenderUtils.drawRect(posX, endPosY, endPosX, endPosY + 1, col);
                    //RenderUtils.drawRect(endPosX-1, posY, endPosX, endPosY, col);
                    //RenderUtils.drawRect(posX, posY, endPosX, endPosY, -1);
                    RenderUtils.drawImage("logo_new", (int) animPosX.getAnim(), (int) animPosY.getAnim(), (int) (animEndX.getAnim()-animPosX.getAnim()), (int) (animEndY.getAnim()-animPosY.getAnim()), -1);


                }
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableBlend();
        entityRenderer.setupOverlayRendering();
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, this.modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, this.projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, this.viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.vector))
            return new Vector3d((this.vector.get(0) / scaleFactor), ((Display.getHeight() - this.vector.get(1)) / scaleFactor), this.vector.get(2));
        return null;
    }
    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);

    @EventTarget
    public void render3D(EventRender3D event){


    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        if (mc.currentScreen instanceof GuiSleepMP){
            setEnabled(false);
        }
        TargetResult target = getTarget(predict.getVal());
        if (target == null){
            penis_slona = null;
            work = false;
            ticks = 0;
            return;
        }
        timerLastSee.reset();
        work = true;
        ticks++;

        aim(target, event);
        penis_slona = target;
        if (autoShoot.getVal() && ticks > 1){
            if (timer.hasReached((long) (1000l / cps.getVal()))){
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

    public float getPuliSpeed(){
        return 0;
    }






    public void  shoot(EntityLivingBase target){
        if ((checkCoolDown.getVal() && mc.player.getCooldownTracker().getCooldown(mc.player.inventory.getCurrentItem().getItem(), mc.getRenderPartialTicks()) == 0) || !checkCoolDown.getVal()){
            mc.playerController.clickBlock(new BlockPos(mc.player.posX, mc.player.posY + 1, mc.player.posZ), EnumFacing.UP);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            timer.reset();

        }
    }

    public float getBowOffset(Entity t) {
        double distY = Math.abs(mc.player.posY - t.posY) * Math.abs(mc.player.posY - t.posY) / 36f;
        return (float) (((mc.player.getDistanceToEntity(t) * mc.player.getDistanceToEntity(t)) / (360 * 5.5)) + ((bow.getVal()) ? distY : 0));
    }

    public void aim(TargetResult target, EventUpdate event){
        float pred = (mc.getConnection().getPlayerInfo(this.mc.player.getUniqueID()).getResponseTime()) / 20f;

        float[] rotations = getAim(target, autoPredict.getVal() ? pred : predict.getVal());
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
        predict+=(bow.getVal()
        ) ? mc.player.getDistanceToEntity(target.getTarget()) / 5 : 0;
        float bowOffset = (bow.getVal()) ? getBowOffset(target.getTarget()) : 0;

        double posX = (target.getTarget().lastTickPosX - target.getTarget().posX) * predict;
        double posZ = (target.getTarget().lastTickPosZ - target.getTarget().posZ) * predict;
        double posY = bowOffset;
            return Utils.getNeededRotations(target.getTarget().posX - posX + target.getPos().xCoord, target.getTarget().posY + target.getPos().yCoord+posY, target.getTarget().posZ - posZ + target.getPos().zCoord, mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
    }

    public void drawPoint2(Vec3d pos) {
        int mode = 2;
        double[] position = {
                pos.xCoord - mc.getRenderManager().viewerPosX,
                pos.yCoord - mc.getRenderManager().viewerPosY,
                pos.zCoord - mc.getRenderManager().viewerPosZ
        };
        float lineWidth = 5;
        GlStateManager.disableDepth();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_POINT_SMOOTH);
        GL11.glLineWidth(lineWidth);
        GL11.glPointSize(10);
        GL11.glPushMatrix();
        double distance = MathUtils.getDistance(pos, mc.player.getPositionVector()) / 3;

        GL11.glTranslated(position[0], position[1], position[2]);
        GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(this.mc.getRenderManager().playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScaled(distance, distance, 0);
        RenderUtils.glColor(Integer.MIN_VALUE);
        GL11.glBegin(mode);

        GL11.glVertex3d(-0.05f, -0.05f, 0);
        GL11.glVertex3d(0.05f, 0.05f, 0);

        GL11.glEnd();
        GL11.glBegin(mode);

        GL11.glVertex3d(0.05f, -0.05f, 0);
        GL11.glVertex3d(-0.05f, 0.05f, 0);

        GL11.glEnd();
        RenderUtils.glColor(-1);
        GL11.glLineWidth(1);
        GL11.glBegin(mode);

        GL11.glVertex3d(-0.045f, -0.045f, 0);
        GL11.glVertex3d(0.045f, 0.045f, 0);

        GL11.glEnd();
        GL11.glBegin(mode);

        GL11.glVertex3d(0.045f, -0.045f, 0);
        GL11.glVertex3d(-0.045f, 0.045f, 0);

        GL11.glEnd();
        RenderUtils.glColor(-1);
        GL11.glLineWidth(1);
        GL11.glPopMatrix();
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();

    }
    public void drawPoint(TargetResult target) {
        Vec3d vec3d = new Vec3d(target.getTarget().posX+target.getPos().xCoord-mc.getRenderManager().viewerPosX, target.getTarget().posY+target.getPos().yCoord-mc.getRenderManager().viewerPosY, target.getTarget().posY+target.getPos().zCoord-mc.getRenderManager().viewerPosZ);

        mc.entityRenderer.setupCameraTransform(mc.getRenderPartialTicks(), 2);
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        //GL11.glEnable(3042);
        GL11.glLineWidth(0.1f);


        GL11.glBegin(3);
        RenderUtils.glColor(Color.white.getRGB());
        GL11.glVertex3d(mc.player.posX, mc.player.posY, mc.player.posZ);
        GL11.glVertex3d(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
        //GL11.glVertex3d(d, d2 + ((EntityPlayer) entity).height, d3);
        GL11.glEnd();

        GL11.glDisable(3042);

        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
        RenderUtils.glColor(Color.white.getRGB());
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
            if (multiTheadering.getVal()){
                final int[] ready = {0};
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        for (float x = -target.width/2; x < 0; x+=accuracy.getVal()) {
                            for (float y = 0; y < target.height; y+=accuracy.getVal()) {
                                for (float z = -target.width/2; z < 0; z+=accuracy.getVal()) {
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
                        for (float x = 0; x < target.width/2; x+=accuracy.getVal()) {
                            for (float y = 0; y < target.height; y+=accuracy.getVal()) {
                                for (float z = -target.width/2; z < 0; z+=accuracy.getVal()) {
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
                        for (float x = -target.width/2; x < 0; x+=accuracy.getVal()) {
                            for (float y = 0; y < target.height; y+=accuracy.getVal()) {
                                for (float z = 0; z < target.width/2; z+=accuracy.getVal()) {
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
                        for (float x = 0; x < target.width/2; x+=accuracy.getVal()) {
                            for (float y = 0; y < target.height; y+=accuracy.getVal()) {
                                for (float z = 0; z < target.width/2; z+=accuracy.getVal()) {
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
                        //System.out.println("stop");
                        break;
                    }
                    //System.out.println(ready[0]);
                }
            }else {
                for (float x = -(target.width/2); x < target.width/2; x+=accuracy.getVal()) {
                    for (float y = 0; y < target.height; y+=accuracy.getVal()) {
                        for (float z = -(target.width/2); z < target.width/2; z+=accuracy.getVal()) {
                            if (mc.player.canEntityBeSeen((target.posX - posX)+x, (target.posY)+y, (target.posZ - posZ)+z)){
                                vec3ds.add(new Vec3d(x, y, z));
                            }
                        }
                    }
                }
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
                if (Utils.fov(entity, fov.getVal()) && !Client.friendsManager.isFriend(entity.getName()) && entity.getEntityId() != -7777 && ((EntityLivingBase) entity).getHealth()>0) {
                    if ((bots.getVal() && (entity instanceof EntityPlayer || entity instanceof EntityZombie)) || (entity instanceof EntityPlayer && !bots.getVal())){
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
