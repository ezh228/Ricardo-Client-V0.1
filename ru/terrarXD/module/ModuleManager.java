package ru.terrarXD.module;

import ru.terrarXD.module.modules.Combat.*;
import ru.terrarXD.module.modules.Hud.*;
import ru.terrarXD.module.modules.Movement.*;
import ru.terrarXD.module.modules.Player.*;
import ru.terrarXD.module.modules.Render.*;


import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 13:38 of 16.04.2023
 */
public class ModuleManager {
    public ArrayList<Module> modules = new ArrayList<>();
    public ModuleManager(){

        add(new WaterMark());
        add(new ClickGui());
        add(new ModuleList());
        add(new AutoSprint());
        add(new ESP());
        add(new NameTags());
        add(new FullBright());
        add(new MCF());
        add(new Tracers());
        add(new Timer());
        add(new AntiBot());
        add(new XRay());
        add(new GuiWalk());
        add(new NoSlowDown());
        //add(new AimBot());
        add(new Proverka());
        add(new AutoMine());
        add(new CupBoardRender());
        //add(new Knife());
        add(new DiscordRPC());
        add(new ExpInfo());
        add(new CameraClip());
        add(new WallHack());
        add(new AntiAim());
        add(new AutoPeak());
        add(new FreeCam());
        add(new NoClip());
        add(new KeyBinds());
        add(new StreamerMode());
        add(new NoHustCam());
        add(new Derp());
        add(new AimBot());
        add(new FastPeak());
        add(new ViewModel());
        add(new Notifications());
        //add(new AutoHeal());
        add(new NoServerRotation());
        //add(new Fly());
    }

    public void add(Module module){
        modules.add(module);
    }

    public Module getModule(String name){
        for (Module module : modules){
            if (module.getName().equals(name)){
                return module;
            }
        }
        return null;
    }
}
