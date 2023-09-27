package ru.terrarXD.shit.settings;

import java.util.function.Supplier;

/**
 * @author zTerrarxd_
 * @since 13:51 of 16.04.2023
 */
public class Setting {
    String name;
    Supplier<Boolean> visible = ()->true;
    public Setting(String name){
        this.name = name;
    }

    public boolean isVisable(){
        return visible.get();
    }

    public Setting setVisible(Supplier<Boolean> visible) {
        this.visible = visible;
        return this;
    }

    public String getName() {
        return name;
    }
}
