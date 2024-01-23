package ru.terrarXD.module.modules.Hud;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.HudModule;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventKeyBoard;
import ru.terrarXD.shit.event.events.EventPacketReceive;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.ColorSetting;
import ru.terrarXD.shit.utils.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 14:24 of 25.05.2023
 */
public class ExpInfo extends HudModule {
    int chouse = -1;
    AnimationUtils animChouse;
    AnimationUtils animSize;
    ArrayList<Ex> exes = new ArrayList<>();
    BooleanSetting clientColor;
    ColorSetting customColor;
    public ExpInfo() {
        super("ExpInfo", Category.Hud, 100, 15);
        animChouse = new AnimationUtils(0, 0, 0.1f);
        animSize = new AnimationUtils(15, 15, 0.1f);
        add(clientColor = new BooleanSetting("Client-Color", true));
        add(customColor = (ColorSetting) new ColorSetting("Color", new Color(47, 122, 229)).setVisible(()->!clientColor.getVal()));
    }

    @EventTarget
    public void keyBoard(EventKeyBoard event) {
        if (exes.size() > 0){
            if (event.getKey() == Keyboard.KEY_UP){
                if (chouse == -1){
                    chouse = exes.size()-1;
                }else {
                    chouse--;
                }
            }
            if (event.getKey() == Keyboard.KEY_DOWN){
                if (chouse == exes.size()-1){
                    chouse = -1;
                }else {
                    chouse++;
                }
            }
            if (event.getKey() == Keyboard.KEY_LEFT){
                if (chouse == -1){
                    for (Ex ex : exes){
                        ex.remove();
                    }
                }else {
                    exes.get(chouse).remove();
                }
            }
            if (event.getKey() == Keyboard.KEY_RIGHT){
                if (chouse == -1){

                }else {
                    exes.get(chouse).use = !exes.get(chouse).use;
                }
            }
        }
    }

    @EventTarget
    public void onPacket(EventPacketReceive event){
        if (event.getPacket() instanceof SPacketCustomSound) {

            SPacketCustomSound packet = (SPacketCustomSound) event.getPacket();

            if (mc.player.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) > 5) {

                //System.out.println("NO " +packet.getSoundName());

                if (((packet.getSoundName().contains("shoot") || packet.getSoundName().contains("explosion") || (packet.getSoundName().contains("separate") && packet.getSoundName().contains("grenade_launcher"))) && !packet.getSoundName().contains("prepare"))&&!packet.getSoundName().contains("fail") &&!packet.getSoundName().contains("rocket_launcher") ) {
                    //System.out.println(packet.getSoundName());
                    if (exes.size() > 0) {
                        boolean g = true;
                        for (Ex ex : exes) {
                            if (ex.id.replaceAll(".3p", "").equals(packet.getSoundName().replaceAll(".3p", "")) && Utils.getDistance(new Vec3d(packet.getX(), packet.getY(), packet.getZ()), ex.pos) < 80) {
                                ex.count++;
                                g = false;
                            }
                        }
                        if (g) {
                            exes.add(new Ex(new Vec3d(packet.getX(), packet.getY(), packet.getZ()), packet.getSoundName()));
                        }
                    } else {
                        exes.add(new Ex(new Vec3d(packet.getX(), packet.getY(), packet.getZ()), packet.getSoundName()));

                    }

                }
            }

        }
    }
    @EventTarget
    public void onRender3D(EventRender3D event){

        RenderUtils.glColor(Color.white.getRGB());

        mc.gameSettings.viewBobbing = false;
        mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 2);
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        //GL11.glEnable(3042);
        GL11.glLineWidth(0.000001f);

        for (Ex ex : exes) {
            if (ex.use){
                assert (mc.getRenderViewEntity() != null);
                double d = ex.pos.xCoord - mc.getRenderManager().viewerPosX;
                double d2 = ex.pos.yCoord - mc.getRenderManager().viewerPosY;
                double d3 = ex.pos.zCoord - mc.getRenderManager().viewerPosZ;
                Vec3d vec3d = new Vec3d(0.0, 0.0, 1.0);
                vec3d = vec3d.rotatePitch(-((float)Math.toRadians(mc.player.rotationPitch)));
                Vec3d vec3d2 = vec3d.rotateYaw(-((float)Math.toRadians(mc.player.rotationYaw)));

                GL11.glBegin(3);

                RenderUtils.glColor(Color.white.getRGB());


                GL11.glVertex3d(vec3d2.xCoord, (double)mc.player.getEyeHeight() + vec3d2.yCoord, vec3d2.zCoord);
                GL11.glVertex3d(d, d2 + 0, d3);
                //GL11.glVertex3d(d, d2 + ((EntityPlayer) entity).height, d3);
                GL11.glEnd();
            }

        }
        GL11.glDisable(3042);

        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
        RenderUtils.glColor(Color.white.getRGB());

    }
    @EventTarget
    public void render2D(EventRender2D event){
        float x = getPosX();
        float y = getPosY();
        if (chouse > exes.size()-1){
            if (exes.size() > 0){
                chouse = exes.size()-1;
            }else {
                chouse = -1;
            }
        }
        for (int i = 0; i < exes.size(); i++) {
            if (exes.get(i).anim.getAnim() == 0 && exes.get(i).anim.to == 0){
                exes.remove(i);
            }
        }
        setSizeY(15+exes.size()*16);
        animSize.to = getSizeY();
        int col = clientColor.getVal() ? Client.getColor() : customColor.getColor().getRGB();

        int colorMain =ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(col), 0.9d).getRGB();
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x + getSizeX(), y + animSize.getAnim(), 5, 0, colorMain, colorMain, colorMain, colorMain, false, true, false);
        Fonts.main_18.drawCenteredString(getName(), x+getSizeX()/2, y+15/2-Fonts.main_18.getHeight()/2, -1);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissor(x, y, 100, animSize.getAnim());
        //System.out.println(mc.player.getHeldItemMainhand().getDisplayName());
        //System.out.println(mc.player.getHeldItemMainhand().getTagCompound());
        animChouse.to = chouse * 16;
        float i = 15;
        for(Ex ex : exes){
            ex.render(x, i+y);
            i+=ex.getHeight();
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        RenderUtils.drawRect(x,  y+15+animChouse.getAnim()+16/2-2, x+1, y+15+animChouse.getAnim()+16/2+2, col);
    }

    public class Ex{
        Vec3d pos;
        String id;
        int count = 1;
        long time;
        boolean discontinuous = false;
        boolean use = false;
        AnimationUtils anim;
        public Ex(Vec3d pos, String id){
            anim = new AnimationUtils(0, 1, 0.05f);
            this.pos = pos;
            this.id = id;
            time = System.currentTimeMillis();
        }

        public void render(float x, float y){
            //System.out.println(discontinuous);
            int col = clientColor.getVal() ? Client.getColor() : customColor.getColor().getRGB();

            GL11.glPushMatrix();
            //RenderUtils.customScaledObject2D(x, y, 100, 13, 1, anim.getAnim());

            ItemStack itemStack = new ItemStack(Items.DIAMOND_SHOVEL);
            String name = id;
            if (id.contains("shoot") || id.contains("separate")){
                int dam = 0;
                if (id.contains("bow") && !id.contains("prepare")){
                    dam = 720;
                    name = "Лук";
                } else
                if (id.contains("semi_auto_rifle")){
                    dam = 713;
                    name = "Берда";
                }else
                if (id.contains("smg")){
                    dam = 710;
                    name = "SMG";
                }else
                if (id.contains("thompson")){
                    dam = 714;
                    name = "Томсон";
                }else
                if (id.contains("python")){
                    dam = 716;
                    name = "Питон";
                }else
                if (id.contains("assault_rifle")){
                    dam = 702;
                    name = "Калаш";
                }else
                if (id.contains("double")){
                    dam = 722;
                    name = "Двушка";
                }else
                if (id.contains("spas12")){
                    dam = 707;
                    name = "Spas12";
                }else
                if (id.contains("mp5")){
                    dam = 709;
                    name = "mp5";
                }else
                if (id.contains("pump")){
                    dam = 717;
                    name = "Pump";
                }else
                if (id.contains("pipe")){
                    dam = 700;
                    name = "Пайп";
                }else
                if (id.contains("bolt")){
                    dam = 703;
                    name = "Болт";
                }else
                if (id.contains("revolver")){
                    dam = 711;
                    name = "Револьвер";
                }else
                if (id.contains("hmlmg")){
                    dam = 724;
                    name = "Пулемёт";
                }else
                if (id.contains("grenade_launcher") ||id.contains("start") ){
                    dam = 725;
                    name = "Граник ";
                }else
                if (id.contains("semi_auto_pistol")){
                    dam = 701;
                    name = "Пешка";
                }else
                if (id.contains("m39")){
                    dam = 719;
                    name = "m39";
                }else
                if (id.contains("prototype")){
                    dam = 727;
                    name = "Prototype";
                }else
                if (id.contains("l96")){
                    dam = 715;
                    name = "L96";
                }else
                if (id.contains("nailgun")){
                    dam = 712;
                    name = "Гвоздемёт";
                }else
                if (id.contains("m249")){
                    dam = 724;
                    name = "О.Пулемёт";
                }else
                if (id.contains("lr300")){
                    dam = 706;
                    name = "LR-300";
                }else
                if (id.contains("m92")){
                    dam = 705;
                    name = "m92";
                }else
                if (id.contains("eoka")){
                    dam = 704;
                    name = "Самопал";
                }else
                if (id.contains("speargun") || id.contains("guntrap")){
                    dam = 16;
                    name = "Гантрап";
                }

                itemStack.itemDamage = dam;

            }else if (id.contains("explosion")){

                if (id.contains("explosive_rifle_bullet")){
                    itemStack = new ItemStack(Items.field_190930_cZ);
                    name = "Разрыва";
                }
                if (id.contains("satchel_charge")){
                    itemStack = new ItemStack(Items.FLINT);
                    name = "Сачель";
                }
                if (id.contains("grenade_f1")){
                    itemStack = new ItemStack(Items.BOOK);
                    name = "F1";
                }

                if (id.contains("beancan_grenade")){
                    itemStack = new ItemStack(Items.SPECKLED_MELON);
                    name = "Бобовка";
                }
                if (id.contains("timed_explosive_charge")){
                    itemStack = new ItemStack(Items.SLIME_BALL);
                    name = "Сишка";
                }
                if (id.contains("rocket")){
                    itemStack = new ItemStack(Items.PRISMARINE_SHARD);
                    name = "Ракета";
                }
                if (id.contains("grenade_40mm_he")){
                    itemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                    name = "Граник";
                    itemStack.itemDamage = 725;
                }

            }





            RenderUtils.renderItem(itemStack, (int) (x+1-100+anim.getAnim()*100), (int) y);
            if (id.contains("explosive_rifle_bullet")){
                GL11.glPushMatrix();
                RenderUtils.customScaledObject2D((int) (x+1-100+anim.getAnim()*100)+1+8, (int) y+1+8, 16, 16, 0.5f);
                RenderUtils.renderItem(new ItemStack(Items.field_190930_cZ), (int) (x+1-100+anim.getAnim()*100), (int) y);
                GL11.glPopMatrix();
            }

            Fonts.main_16.drawString(name, x+18-100+anim.getAnim()*100, y+16/2-Fonts.main_16.getHeight()/2, use ? col : -1);
            String text= Math.round(mc.player.getDistance(pos.xCoord, pos.yCoord, pos.zCoord))+"m";
            if (count > 1){
                text+="  x"+count;
            }
            Fonts.main_16.drawString(text, x+anim.getAnim()*100+-Fonts.main_16.getStringWidth(text+" "), y+16/2-Fonts.main_16.getHeight()/2, -1);


            GL11.glPopMatrix();
        }
        public void remove(){
            anim.to = 0;
        }
        public float getHeight(){
            return 16*anim.getAnim();
        }
    }
}