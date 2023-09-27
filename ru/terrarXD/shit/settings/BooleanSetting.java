package ru.terrarXD.shit.settings;

/**
 * @author zTerrarxd_
 * @since 22:45 of 16.04.2023
 */
public class BooleanSetting extends Setting{
    boolean val;
    public BooleanSetting(String name, boolean val) {
        super(name);
        this.val = val;
    }
    public boolean getVal(){
        return val;
    }

    public void setVal(boolean val) {
        this.val = val;
    }
}
