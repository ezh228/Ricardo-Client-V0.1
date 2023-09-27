package ru.terrarXD.shit.settings;

import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 13:27 of 30.05.2023
 */
public class ModeSetting extends Setting{
    ArrayList<String> modes = new ArrayList<>();
    String val;
    public ModeSetting(String name, ArrayList<String> modes, String select) {
        super(name);
        this.modes = modes;
        val = select;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public void setModes(ArrayList<String> modes) {
        this.modes = modes;
    }

    public String getVal() {
        return val;
    }

    public ArrayList<String> getModes() {
        return modes;
    }
}
