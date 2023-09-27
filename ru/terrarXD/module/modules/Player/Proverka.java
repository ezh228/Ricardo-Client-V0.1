package ru.terrarXD.module.modules.Player;

import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;

/**
 * @author zTerrarxd_
 * @since 16:11 of 08.06.2023
 */
public class Proverka extends Module {
    public static boolean proverka = false;
    public Proverka() {
        super("Проверка", Category.Player);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        proverka = true;
        Client.configManager.remove();
        mc.displayGuiScreen(null);
        setEnabled(false);
    }
}
