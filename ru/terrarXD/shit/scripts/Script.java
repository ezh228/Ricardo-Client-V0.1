package ru.terrarXD.shit.scripts;

/**
 * @author zTerrarxd_
 * @date 21.11.2023 20:11
 */
public class Script {
    String name;
    String author;
    String version;
    String clientVersion;
    String description;
    ScriptAPI inctance;

    boolean enabled = false;

    public Script(String name, String author, String version, String clientVersion, String description, ScriptAPI inctance){
        this.name = name;
        this.author = author;
        this.version = version;
        this.clientVersion = clientVersion;
        this.description = description;
        this.inctance = inctance;
        inctance.onLoad();
        System.out.println("Loaded script: " + name+" "+version);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public ScriptAPI getInctance() {
        return inctance;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled){
            inctance.onEnable();
        }else {
            inctance.onDisable();
        }
    }


}
