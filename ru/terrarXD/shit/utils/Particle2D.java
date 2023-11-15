package ru.terrarXD.shit.utils;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @date 14.11.2023 20:05
 */
public class Particle2D {
    float x;
    float y;
    float speed;
    float radian;
    float maxTime;
    long startTime;
    int color;


    public Particle2D(float circleX, float circleY, float holdProgress, float ofRange) {
        color = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), MathUtils.getRandomInRange(0.1f, 0.4f)).getRGB();
        this.speed = (float)Math.random() / 22.25F;
        this.radian = (float)Math.random() * 360.0F;
        this.maxTime = 650.0F;
        this.startTime = System.currentTimeMillis();
        float radian = (float)Math.toRadians((double)(holdProgress * 360.0F + 180.0F));
        /*
        if (holdProgress == 0.0F) {
            if (ofRange != 0.0F) {
                this.speed /= 1.3F;
            }

            if (ofRange == 1.0F) {
                this.speed /= 3.0F;
            }

            ofRange = 0.0F;
            this.maxTime /= 2.0F;
        } else {
        }

         */
        this.radian = (float)((double)radian / Math.PI * 180.0) - 60.0F - 10.0F + 20.0F * (float)Math.random();


        this.x = circleX + (float)Math.sin((double)radian) * ofRange;
        this.y = circleY + (float)Math.cos((double)radian) * ofRange;
        this.speed *= ofRange / 4.0F + 6.0F;
    }
    public Particle2D(float circleX, float circleY, float ofRange) {
        color = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), MathUtils.getRandomInRange(0.1f, 0.4f)).getRGB();
        this.speed = (float)Math.random() / 22.25F;
        this.radian = (float)Math.random() * 360.0F;
        this.maxTime = 650.0F;
        this.startTime = System.currentTimeMillis();
        /*
        if (holdProgress == 0.0F) {
            if (ofRange != 0.0F) {
                this.speed /= 1.3F;
            }

            if (ofRange == 1.0F) {
                this.speed /= 3.0F;
            }

            ofRange = 0.0F;
            this.maxTime /= 2.0F;
        } else {
        }

         */
        this.radian = (float)((double)radian / Math.PI * 180.0) - 60.0F - 10.0F + 20.0F * (float)Math.random();


        this.x = circleX;
        this.y = circleY;
        this.speed *= ofRange / 4.0F + 6.0F;
    }

    float timePC() {
        return MathUtils.clamp((float)(System.currentTimeMillis() - this.startTime) / this.maxTime, 0.0F, 1.0F);
    }

    float alphaPC() {
        return 1.0F - this.timePC();
    }

    public boolean toRemove() {
        return this.timePC() == 1.0F;
    }

    public void drawAndMovement() {
        this.x += (float)Math.sin(Math.toRadians((double)this.radian)) * this.speed;
        this.y += (float)Math.cos(Math.toRadians((double)this.radian)) * this.speed;
        float alphaPC = this.alphaPC();
        if (alphaPC != 0.0F) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 32772);
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glEnable(2832);
            GL11.glPointSize(alphaPC * alphaPC * 6.0F);
            RenderUtils.glColor(color);
            GL11.glBegin(0);
            GL11.glVertex2d((double)this.x, (double)this.y);
            GL11.glEnd();
            GL11.glPointSize(1.0F);
            GL11.glEnable(3008);
            GL11.glEnable(3553);
            GL11.glBlendFunc(770, 771);
            GlStateManager.resetColor();
        }
    }
}
