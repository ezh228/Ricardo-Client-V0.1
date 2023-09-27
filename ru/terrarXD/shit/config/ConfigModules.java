package ru.terrarXD.shit.config;

import ru.terrarXD.Client;
import ru.terrarXD.clickgui.set.Set;
import ru.terrarXD.module.BindType;
import ru.terrarXD.module.HudModule;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.settings.*;

import java.awt.*;
import java.io.*;

/**
 * @author zTerrarxd_
 * @since 21:51 of 29.05.2023
 */
public class ConfigModules extends Config{


    public ConfigModules(String name, File CLIENT_FOLDER) {
        super(name, CLIENT_FOLDER);
    }

    @Override
    public void load() {
        super.load();
        File file =  new File(Client.configManager.CLIENT_FOLDER, name+".txt");
        if (!file.exists()){
            return;
        }
        String cfg = "";
        try(FileReader reader = new FileReader( file))
        {
            int c;
            while((c=reader.read())!=-1){
                cfg+=(char)c;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line : cfg.split("\n")){
            Module module = Client.moduleManager.getModule(line.split(":")[0]);
            if (module != null) {
                String type = line.split(":")[1];
                String val = line.split(":")[2];
                if (type.equals("state")){
                    if (val.equals("true")){
                        module.setEnabled(true);
                    }else {
                        module.setEnabled(false);
                    }
                } else if (type.equals("key")){
                    module.setKey(Integer.parseInt(val));
                }else
                if (type.equals("type")){
                    if (val.equals("Toggle")){
                        module.setBindType(BindType.Toggle);
                    }else {
                        module.setBindType(BindType.Hold);
                    }
                }else
                if (module instanceof HudModule){
                    if (type.equals("posX")){
                        ((HudModule) module).setPosX(Float.parseFloat(val));
                    }
                    if (type.equals("posY")){
                        ((HudModule) module).setPosY(Float.parseFloat(val));
                    }

                }else for (Setting setting : module.getSettings()){
                    if (type.equals(setting.getName())){
                        if (setting instanceof BooleanSetting){
                            ((BooleanSetting) setting).setVal(val.equals("true"));
                        }else if (setting instanceof FloatSetting){
                            ((FloatSetting) setting).setVal(Float.parseFloat(val));
                        }else if (setting instanceof ColorSetting){
                            ((ColorSetting) setting).setColor(new Color( Integer.parseInt(val)));
                        }  else if (setting instanceof ModeSetting){
                            if (((ModeSetting)setting).getModes().contains(val)){
                                ((ModeSetting) setting).setVal(val);
                            }else {
                                ((ModeSetting) setting).setVal(((ModeSetting)setting).getModes().get(0));
                            }

                        }
                    }
                }
            }

        }
    }

    @Override
    public void save() {
        super.save();
        File file =  new File(Client.configManager.CLIENT_FOLDER, name+".txt");
        file.delete();
        try(FileWriter writer = new FileWriter(file, true))
        {
            writer.write(getCFG());

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
    public String getCFG(){
        String text = "";
        for (Module module : Client.moduleManager.modules){
            text+=module.getName()+":state:"+(module.isEnabled() ? "true" : "false")+"\n";
            text+=module.getName()+":key:"+module.getKey()+"\n";
            text+=module.getName()+":type:"+module.getBindType()+"\n";
            if (module instanceof HudModule){
                text+=module.getName()+":posX:"+((HudModule) module).getPosX()+"\n";
                text+=module.getName()+":posY:"+((HudModule) module).getPosY()+"\n";
            }
            for (Setting setting : module.getSettings()){
                if (setting instanceof BooleanSetting){
                    text+=module.getName()+":"+setting.getName()+":"+(((BooleanSetting) setting).getVal() ? "true" : "false")+"\n";
                }else if (setting instanceof FloatSetting){
                    text+=module.getName()+":"+setting.getName()+":"+((FloatSetting) setting).getVal()+"\n";
                }else if (setting instanceof ColorSetting){
                    text+=module.getName()+":"+setting.getName()+":"+((ColorSetting) setting).getColor().getRGB()+"\n";
                }else if (setting instanceof ModeSetting){
                    text+=module.getName()+":"+setting.getName()+":"+((ModeSetting) setting).getVal()+"\n";
                }

            }
        }
        return text;
    }
}
