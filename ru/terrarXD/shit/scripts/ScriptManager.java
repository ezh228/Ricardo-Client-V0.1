package ru.terrarXD.shit.scripts;

import ru.terrarXD.Client;
import ru.terrarXD.shit.utils.Utils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

/**
 * @author zTerrarxd_
 * @date 21.11.2023 20:16
 */
public class ScriptManager {
    public ArrayList<Script> scripts = new ArrayList<>();
    public ScriptManager(){
        for (File file : Utils.listFilesForFolder(Client.configManager.SCRIPTS_FOLDER)){
            if (file.getName().contains(".jar")){
                try {
                    loadJAR(file);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void loadJAR(File file) throws Exception {
        JarFile jarFile = new JarFile(file);
        Attributes attrs = jarFile.getManifest().getMainAttributes();
        URLClassLoader child = new URLClassLoader(
                new URL[] {file.toURI().toURL()},
                this.getClass().getClassLoader()
        );
        Class classToLoad = Class.forName(attrs.getValue("Class-init"), true, child);
        Object instance = classToLoad.newInstance();
        scripts.add(new Script(attrs.getValue("Name"), attrs.getValue("Author"), attrs.getValue("Version"), attrs.getValue("Client-Version"), attrs.getValue("Description"), (ScriptAPI) instance));
    }



}
