package ru.terrarXD.shit.event.events;


import ru.terrarXD.shit.event.Event;

/**
 * @author zTerrarxd_
 * @since 23:06 of 29.05.2022
 */
public class EventUpdate extends Event {
    double posX;
    double posY;
    double posZ;
    float rotationYaw;
    float rotationPitch;
    boolean onGround;
    public EventUpdate(double posX, double posY, double posZ, float rotationYaw, float rotationPitch, boolean onGround){
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.onGround = onGround;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosZ() {
        return posZ;
    }

    public float getRotationPitch() {
        return rotationPitch;
    }

    public float getRotationYaw() {
        return rotationYaw;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public void setRotationPitch(float rotationPitch) {
        this.rotationPitch = rotationPitch;
    }

    public void setRotationYaw(float rotationYaw) {
        this.rotationYaw = rotationYaw;
    }
}
