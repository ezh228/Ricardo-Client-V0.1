package ru.terrarXD.shit.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import ru.terrarXD.Client;
import ru.terrarXD.shit.settings.ModeSetting;
import ru.terrarXD.shit.utils.RenderUtils;
import ru.terrarXD.shit.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 17:03 of 30.05.2023
 */
public class ConfigGuiIMG extends Config{
    public ArrayList<IMGGUI> imgguis = new ArrayList<>();
    public File IMG_FOLDER;

    public ConfigGuiIMG(String name, File CLIENT_FOLDER) {
        super(name, CLIENT_FOLDER);
        IMG_FOLDER = new File(CLIENT_FOLDER, name);
        IMG_FOLDER.mkdirs();
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL("https://ricardoclient.netlify.app/tyan.png").openStream());
             FileOutputStream fileOS = new FileOutputStream(IMG_FOLDER.getAbsolutePath()+"/Niko.png")) {
            byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
        } catch (IOException e) {
            // handles IO exceptions
        }
    }


    @Override
    public void load() {
        super.load();
        ArrayList<String> names = new ArrayList<>();

        for(File file : Utils.listFilesForFolder(IMG_FOLDER)){
            if (file.getName().contains(".png")){

                try {
                    names.add(file.getName().replace(".png", ""));
                    ResourceLocation icon = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(file.getName().replace(".png", ""), new DynamicTexture(ImageIO.read(file)));

                    imgguis.add(new IMGGUI(file.getName().replace(".png", ""), file, icon, ImageIO.read(file)));

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }

        ((ModeSetting)Client.moduleManager.getModule("ClickGui").getSettings().get(4)).setVal(names.get(0));
        ((ModeSetting)Client.moduleManager.getModule("ClickGui").getSettings().get(4)).setModes(names);

    }
    public class IMGGUI{
        ResourceLocation resourceLocation;
        File file;
        String name;
        BufferedImage bufferedImage;
        public IMGGUI(String name, File file, ResourceLocation resourceLocation, BufferedImage bufferedImage){
            this.name= name;
            this.file = file;
            this.resourceLocation = resourceLocation;
            this.bufferedImage = bufferedImage;
        }

        public String getName() {
            return name;
        }

        public File getFile() {
            return file;
        }

        public BufferedImage getBufferedImage() {
            return bufferedImage;
        }

        public ResourceLocation getResourceLocation() {
            return resourceLocation;
        }
    }
}
