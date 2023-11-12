package ru.terrarXD.module.modules.Player;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.module.modules.Combat.AimBot;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.settings.ModeSetting;
import ru.terrarXD.shit.utils.TimerUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * @author zTerrarxd_
 * @date 26.10.2023 22:12
 */
public class AutoHeal extends Module {
    int oldINV = 0;

    int state = 0;

    ModeSetting mode;

    FloatSetting timeToWait;

    TimerUtils wait;
    public AutoHeal() {
        super("AutoHeal", Category.Player);
        ArrayList<String> modes = new ArrayList<>();
        modes.add("Allways");
        modes.add("Aim Sync");
        add(mode = new ModeSetting("Mode", modes, "Aim Sync"));
        add(timeToWait = (FloatSetting) new FloatSetting("TimeToWait", 0, 5000, 3000, 1f).setVisible(()->mode.getVal().equals("Aim Sync")));
        wait = new TimerUtils();
    }


    @EventTarget
    public void onUpdate(EventUpdate event){
        AimBot aimBot = (AimBot) Client.moduleManager.getModule("AimBot");
        switch (mode.getVal()){
            case "Allways":
                if (isblooded() || (mc.player.getHealth() * 5) < 80){
                    heal();
                }
                break;
            case "Aim Sync":
                if (isblooded() && aimBot.timerLastSee.hasReached((long) timeToWait.getVal())){
                    heal();
                }
                break;
        }
    }

    public boolean isblooded(){
        Scoreboard scoreboard = mc.world.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(mc.player.getName());

        if (scoreplayerteam != null)
        {
            int i1 = scoreplayerteam.getChatFormat().getColorIndex();

            if (i1 >= 0)
            {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

        if (scoreobjective1 != null)
        {
            scoreboard = scoreobjective1.getScoreboard();
            Collection<Score> collection = scoreboard.getSortedScores(scoreobjective1);
            List<Score> list = Lists.newArrayList(Iterables.filter(collection, new Predicate<Score>()
            {
                public boolean apply(@Nullable Score p_apply_1_)
                {
                    return p_apply_1_.getPlayerName() != null && !p_apply_1_.getPlayerName().startsWith("#");
                }
            }));
            for (Score score : collection){
                if (score.getPlayerName().contains("⛙")){
                    return true;
                }
            }
        }
        return false;
    }

    public void heal(){
        if (!wait.hasReached(200)){
            return;
        }
        if (state == 0){
            for (int i = 36; i < 44; i++) {
                ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() == Items.DYE){
                    if (mc.player.getCooldownTracker().getCooldown(itemStack.getItem(), mc.getRenderPartialTicks())==0){
                        oldINV = mc.player.inventory.currentItem;
                        mc.player.inventory.changeCurrentItem(i);
                    }

                }
            }
        }
        if (state == 1){
            mc.rightClickMouse();
        }
        if (state == 2){
            mc.player.inventory.changeCurrentItem(oldINV);
            state = 0;
        }else{
            state++;
        }
        wait.reset();


    }



}
