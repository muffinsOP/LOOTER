/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maxlooter;

import java.awt.Graphics2D;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.ui.RS2Widget;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.input.mouse.MainScreenTileDestination;
import static org.osbot.rs07.script.MethodProvider.random;
import static org.osbot.rs07.script.MethodProvider.sleep;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

/**
 *
 * @author Islah
 */
@ScriptManifest(name = "LooterMAX", author = "Muffins", version = 1.0, info = "", logo = "")
public class MaxLOOTER extends Script {

    Area wilderness = new Area(3110, 3523, 3039, 3548);

    private long startTime;

    private State s;

    public String status;

    @Override

    public void onStart() {

        startTime = System.currentTimeMillis();

    }

    private enum State {
        LOOT_ITEMS, USE_THE_BANK, WALK_TO_BANK, WALK_TO_WILDERNESS, BANK_FOR_FOOD, INTERFACE, RUN, UNDER_ATTACK;
    }

    private State getState() {

        if (Banks.EDGEVILLE.contains(myPlayer().getPosition()) && getSkills().getDynamic(Skill.HITPOINTS) >= 8 && getInventory().isFull()) {
            return State.USE_THE_BANK;
        }

        if (!Banks.EDGEVILLE.contains(myPlayer().getPosition()) && getSkills().getDynamic(Skill.HITPOINTS) < 8) {
            return State.BANK_FOR_FOOD;
        }

        if (!Banks.EDGEVILLE.contains(myPlayer().getPosition()) && getInventory().isFull()) {
            return State.WALK_TO_BANK;
        }

        if (!wilderness.contains(myPlayer().getPosition()) && !getInventory().isFull() && getSkills().getDynamic(Skill.HITPOINTS) >= 8) {
            return State.WALK_TO_WILDERNESS;
        }

        if (getSettings().getRunEnergy() > 10 && !getSettings().isRunning()) {
            return State.RUN;
        }

        if (wilderness.contains(myPlayer().getPosition()) && getSkills().getDynamic(Skill.HITPOINTS) > 8) {
            return State.LOOT_ITEMS;
        }
        if (myPlayer().isUnderAttack()) {
            return State.UNDER_ATTACK;
        }

        return null;
    }

    @Override

    public void onExit() {

    }

    @Override

    public int onLoop() throws InterruptedException {

        s = getState();

        switch (s) {
            case LOOT_ITEMS:
                Loot_ITEMS();
                break;
            case WALK_TO_WILDERNESS:
                WalkToWild();
                break;
            case USE_THE_BANK:
                Bank();
                break;
            case WALK_TO_BANK:
                WalkToBank();
                break;
            case BANK_FOR_FOOD:
                Bank_For_Food();
                break;
            case RUN:
                Run_ACTIVATE();
                break;
            case UNDER_ATTACK:
                Bank_For_Food();

        }
        return 100;
    }

    public void Run_ACTIVATE() {
        if (getSettings().getRunEnergy() > 10 && !getSettings().isRunning()) {
            getSettings().setRunning(true);
            Timing.waitCondition(() -> getSettings().getRunEnergy() < 10, 5000);
        }
    }

    private void Loot_ITEMS() throws InterruptedException {

        log("Looking for items to loot....");
        status = "Looking for items to loot...";
        GroundItem i = getGroundItems().closest(wilderness, "Dharok's greataxe 0", "Dharok's helm 0", "Dharok's platebody 0", "Dharok's platelegs 0",
                "Anglerfish", "Manta ray", "Cooked karambwan", "Dark crab", "Shark", "Monkfish", "Potato with cheese", "Tuna potato",
                "Nature rune", "Death rune", "Law rune", "Astral rune", "Chaos rune", "Blood rune",
                "Dragon arrow(p++)", "Rune arrow", "Adamant arrow", "Dragon arrow", "Bolt rack",
                "Rune dart", "Dragon dart", "Dragon dart(p++)",
                "Rune knife(p++)", "Rune knife",
                "Armadyl crossbow", "Rune crossbow",
                "Onyx bolts", "Onyx bolts(e)", "Dragon bolts (e)",
                "Sanfew serum(1)", "Sanfew serum(2)", "Sanfew serum(3)", "Sanfew serum(4)", "Anti-venom(1)", "Anti-venom(2)", "Anti-venom(3)", "Anti-venom(4)", "Super combat potion(1)", "Super combat potion(2)", "Super combat potion(3)", "Super combat potion(4)", "Saradomin brew(4)", "Saradomin brew(3)", "Saradomin brew(2)", "Saradomin brew(1)", "Superantipoison(1)", "Superantipoison(2)", "Superantipoison(3)", "Superantipoison(4)", "Super restore(4)", "Super restore(3)", "Super restore(2)", "Super restore(1)", "Prayer potion(1)", "Prayer potion(2)", "Prayer potion(3)", "Prayer potion(4)", "Ranging potion(4)", "Ranging potion(3)", "Ranging potion(2)", "Stamina potion(4)", "Stamina potion(3)", "Stamina potion(2)", "Stamina potion(1)", "Super strength(4)", "Super strength(3)", "Super strength(2)", "Super strength(1)",
                "Amulet of fury", "Berserker necklace", "Amulet of power", "Amulet of glory", "Amulet of glory(1)", "Amulet of glory(2)", "Amulet of glory(3)", "Amulet of glory(4)", "Amulet of glory(5)", "Amulet of glory(6)", "Amulet of strength",
                "Dark bow", "Magic shortbow",
                "Granite maul", "Tzhaar-ket-om",
                "Mysterious emblem", "Mysterious emblem (tier 2)", "Mysterious emblem (tier 3)", "Mysterious emblem (tier 4)", "Mysterious emblem (tier 5)", "Mysterious emblem (tier 6)", "Mysterious emblem (tier 7)", "Mysterious emblem (tier 8)", "Mysterious emblem (tier 9)", "Mysterious emblem (tier 10)",
                "Obsidian cape", "Team-18 cape",
                "Regen bracelet", "Ring of recoil", "Berserker ring", "Archers ring",
                "Berserker helm", "Villager hat",
                "Rune platebody", "Rune platelegs", "Rune kiteshield", "Rune full helm",
                "Bandos godsword", "Armadyl godsword", "Rune scimitar", "Dragon scimitar", "Dragon dagger", "Dragon dagger(p)", "Dragon dagger(p)+", "Dragon dagger(p)++",
                "Dragon boots", "Iron boots", "Rune boots",
                "Varrock teleport", "Falador teleport", "Camelot teleport", "Teleport to house", "Lumbridge teleport", "Monk robe", "Climbing boots", "Karambwan", "Abyssal whip",
                "Torag's platelegs 0",
                "Torag's helm 0",
                "Seers ring",
                "Warriors ring",
                "Zamorak platelegs",
                "Zamorak platebody",
                "Zamorak full helm",
                "Zamorak kiteshield",
                "Saradomin platelegs",
                "Saradomin platebody",
                "Saradomin full helm",
                "Verac's helm 0",
                "Iron platebody (g)",
                "Iron platelegs (g)",
                "Iron full helm (g)",
                "Iron platebody (t)",
                "Iron platelegs (t)",
                "Iron full helm (t)",
                "Green d'hide body",
                "Green d'hide chaps",
                "Green d'hide vambs",
                "Black d'hide body",
                "Black d'hide chaps",
                "Black d'hide vambs",
                "Red d'hide body",
                "Red d'hide chaps",
                "Red d'hide vambs",
                "Antifire potion(1)",
                "Antifire potion(2)",
                "Antifire potion(3)",
                "Antifire potion(4)",
                "Extended antifire (1)",
                "Extended antifire (2)",
                "Extended antifire (3)",
                "Extended antifire (4)",
                "Primordial boots",
                "Kraken tentacle",
                "Runite bolts",
                "Bandos chestplate",
                "Bandos tassets",
                "Toxic blowpipe",
                "Zulrah scale"
        );

        if (!myPlayer().isUnderAttack()) {
            if (i != null && !getInventory().isFull()) {
                i.interact("Take");
                sleep(random(600, 700));
                status = ("We've looted an item");
                log("We've looted an item");
            }
        }
        Timing.waitCondition(() -> !myPlayer().isMoving(), 600, 1000);

    }

    public void WalkToWild() {
        RS2Widget enterWildernessButton = getWidgets().singleFilter(getWidgets().getAll(), new WidgetActionFilter("Enter Wilderness"));
        if (enterWildernessButton != null) {
            enterWildernessButton.interact("Enter Wilderness");
        } else {
            log("Walking to the wilderness...");
            status = ("Walking to the wildernes...");
            WebWalkEvent webWalkEvent = new WebWalkEvent(wilderness);
            webWalkEvent.setBreakCondition(new Condition() {
                @Override
                public boolean evaluate() {
                    return getWidgets().getWidgetContainingText("Enter Wilderness") != null;
                }
            });
            getWalking().webWalk(wilderness);
        }

    }

    public void Bank() throws InterruptedException {
        log("Banking...");
        status = "Banking...";
        sleep(random(1000, 2000));
        getBank().open();
        Timing.waitCondition(() -> getBank().isOpen(), 2000, 3000);
        sleep(random(1000, 2000));
        log("Depositing Items...");
        status = ("Depositing items....");
        getBank().depositAll();
        Timing.waitCondition(() -> getInventory().isEmpty(), 5000, 8000);
        sleep(1000);
        getBank().close();

    }

    //196,244
    public void WalkToBank() {

        log("Walking to the bank...");
        status = ("Walking to the bank...");
        getWalking().webWalk(Banks.EDGEVILLE);
        Timing.waitCondition(() -> Banks.EDGEVILLE.contains(myPlayer().getPosition()), 2500, 3500);

    }

    public void Bank_For_Food() throws InterruptedException {
        status = ("Being Attacked!!!!!");
        log("Being Attacked, running away!");
        if (!Banks.EDGEVILLE.contains(myPlayer().getPosition())) {
            getWalking().webWalk(Banks.EDGEVILLE);

        } else if (Banks.EDGEVILLE.contains(myPlayer().getPosition())) {
            getBank().open();
            Timing.waitCondition(() -> getBank().isOpen(), 1000, 3000);
            sleep(random(1000, 1500));
            getBank().depositAll();
            Timing.waitCondition(() -> getInventory().isEmpty(), 2000, 3000);
            getBank().withdraw("Trout", 1);
            Timing.waitCondition(() -> getInventory().contains("Trout"), 2000, 3000);
            getBank().close();
            Timing.waitCondition(() -> !getBank().isOpen(), 2000, 2500);
            getInventory().interact("Eat", "Trout");
            Timing.waitCondition(() -> !getInventory().contains("Trout"), 1500, 2000);
        }
    }

    @Override

    public void onPaint(Graphics2D g) {

        long timePassed = System.currentTimeMillis() - startTime;
        int seconds = (int) (timePassed / 1000) % 60;
        int minutes = (int) ((timePassed / (1000 * 60)) % 60);
        int hours = (int) ((timePassed / (1000 * 60 * 60)));

        if (hours > 99) {
            g.drawString((hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":"
                    + (seconds < 10 ? "0" : "") + seconds, 450, 10);
            g.drawString("Status: " + status, 350, 20);

        } else {
            g.drawString((hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":"
                    + (seconds < 10 ? "0" : "") + seconds, 450, 10);
            g.drawString("Status: " + status, 350, 20);

        }
    }

}

class WidgetActionFilter implements Filter<RS2Widget> {

    private final String action;

    WidgetActionFilter(final String action) {
        this.action = action;
    }

    @Override
    public boolean match(RS2Widget rs2Widget) {
        if (rs2Widget == null) {
            return false;
        }
        if (rs2Widget.getInteractActions() == null) {
            return false;
        }
        for (String action : rs2Widget.getInteractActions()) {
            if (this.action.equals(action)) {
                return true;
            }
        }
        return false;
    }
}
