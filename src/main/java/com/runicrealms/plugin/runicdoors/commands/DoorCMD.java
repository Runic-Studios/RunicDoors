package com.runicrealms.plugin.runicdoors.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Syntax;
import com.runicrealms.plugin.runicdoors.RunicDoors;
import com.runicrealms.plugin.runicdoors.config.ConfigSave;
import com.runicrealms.plugin.runicdoors.config.DoorDelete;
import com.runicrealms.plugin.runicdoors.door.Door;
import com.runicrealms.plugin.runicdoors.door.DoorBlock;
import com.runicrealms.plugin.runicdoors.door.DoorInteractor;
import com.runicrealms.plugin.runicdoors.utilities.ItemUtil;
import com.runicrealms.plugin.runicdoors.utilities.LocationUtil;
import com.runicrealms.plugin.runicdoors.utilities.Particles;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;

@CommandAlias("door|doors|d|doorcommand|runicdoors|runicdoor")
@CommandPermission("runic.op")
public class DoorCMD extends BaseCommand {
    @Subcommand("setanimation|animation|anim|a")
    @CommandCompletion("@Animations")
    public void onDoorAnimate(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            String text = "BASIC";
            if (RunicDoors.inst().getAnimator().animations.containsKey(range)) {
                text = range;
            }

            player.sendMessage("New animation of " + text);
            door.setAnimation(range);
            ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("close|q|quit|closedoor|lock|doorshut|doorclose")
    @CommandCompletion("")
    public void onDoorClose(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());

            if (RunicDoors.inst().getOpenDoors().containsKey(door.getId() + "")) {
                RunicDoors.inst().getOpenDoors().remove(door.getId() + "");
            }

            door.closeForPlayer(player);

        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("setcloseanimation|closeanimation|closeanim|ca")
    @CommandCompletion("@CloseAnimations")
    public void onDoorCloseAnimate(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            String text = "BASIC";
            if (RunicDoors.inst().getCloseAnimator().animations.containsKey(range)) {
                text = range;
            }

            player.sendMessage("New animation of " + text);
            door.setCloseAnimation(range);
            ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("closeid|ci|closedoorbyid|lockid|doorcloseid|closesesameid")
    @CommandCompletion("id|1234")
    public void onDoorCloseByID(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("No perms");
                return;
            }
            if (!RunicDoors.inst().getDoors().containsKey(args[0] + "")) {
                player.sendMessage("That isn't a real door! " + args[0]);
                return;
            }
            Door door = RunicDoors.inst().getDoors().get(args[0] + "");
            if (RunicDoors.inst().getOpenDoors().containsKey(door.getId() + "")) {
                RunicDoors.inst().getOpenDoors().remove(door.getId() + "");
            }

            door.closeForPlayer(player);

            return;
        }
        if (!RunicDoors.inst().getDoors().containsKey(args[0] + "")) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Tried to open a Non-existant door! " + args[0]);
            return;
        }
        Door door = RunicDoors.inst().getDoors().get(args[0] + "");
        if (RunicDoors.inst().getOpenDoors().containsKey(door.getId() + "")) {
            RunicDoors.inst().getOpenDoors().remove(door.getId() + "");
        }

        door.closeForPlayer(null);
    }

    //used letters C E S N D R T A O Q P H V
    @Subcommand("create|c")
    @CommandCompletion("")
    public void onDoorCreate(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        player.sendMessage("Creating a door for you!");
        short id = DoorInteractor.newId();
        Door door = new Door(player.getLocation(), id, "none", 5, new ArrayList<>(), false, 3, "BASIC", "BASIC", 1, "none");
        ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
        RunicDoors.inst().getDoorHandler().placeDoorInGrid(door);
        RunicDoors.inst().getDoors().put(door.getId() + "", door);
        player.sendMessage("Its id is " + id);
        RunicDoors.inst().getEditors().put(player.getUniqueId(), door);

        Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
    }

    @Subcommand("delete|d")
    @CommandCompletion("")
    public void onDoorDelete(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            player.sendMessage("Deleted ");
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            RunicDoors.inst().getDoorHandler().getDoorGrid().removeElementInGrid(RunicDoors.inst().getDoorHandler().getDoorGrid().getGridLocationFromLocation(door.getLocation()), door);
            DoorDelete.deleteNode(door.getId() + "", RunicDoors.inst().getDoorFileConfig());
            RunicDoors.inst().getDoors().remove(door.getId() + "");
            RunicDoors.inst().getEditors().remove(player.getUniqueId());
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("denymessage|deny")
    @CommandCompletion("none|&4You feel weak")
    public void onDoorDenyMessage(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());

            player.sendMessage("New message of " + range);
            door.setDenyMessage(range);
            ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("edit|e|disable|turnoffediting|editoff")
    @CommandCompletion("")
    public void onDoorEdit(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            player.sendMessage("Turned off editing mode");
            RunicDoors.inst().getEditors().remove(player.getUniqueId());
        } else {
            player.sendMessage("create a door, or select one to turn on editing!");
        }
    }

    @Subcommand("hide|h|showblocks")
    @CommandCompletion("")
    public void onDoorHide(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            DoorInteractor.hideBlocks(door);

            RunicDoors.inst().getViewing().remove(door.getId() + "");
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("knockback|kick|throwdistance")
    @CommandCompletion("1|2|3|4|5")
    public void onDoorKnockback(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            int number = 1;
            try {
                if (range != null)
                    number = Integer.parseInt(range);
            } catch (NumberFormatException e) {
                number = 3;
            }

            player.sendMessage("New knock back of " + number);
            door.setKnockback(number);
            ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("list|l")
    @CommandCompletion("")
    public void onDoorList(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        player.sendMessage(RunicDoors.inst().getDoors().size() + " Doors on the server");
    }

    @Subcommand("open|o|opendoor|unlock|dooropen|opensesame")
    @CommandCompletion("")
    public void onDoorOpen(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            if (!RunicDoors.inst().getOpenDoors().containsKey(door.getId() + "")) {
                door.setOpen(true);
                //always set open first
                door.openForPlayer(player);

            }
            door.setTimeOpen(door.getTimeOpenDefault());
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("openid|oi|opendoorbyid|unlockid|dooropenid|opensesameid")
    @CommandCompletion("id|1234")
    public void onDoorOpenByID(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("No perms");
                return;
            }
            if (!RunicDoors.inst().getDoors().containsKey(args[0] + "")) {
                player.sendMessage("That isn't a real door! " + args[0]);
                return;
            }
            Door door = RunicDoors.inst().getDoors().get(args[0] + "");
            if (!RunicDoors.inst().getOpenDoors().containsKey(door.getId() + "")) {
                door.setOpen(true);
                //always set open first
                door.openForPlayer(player);

            }
            int number = door.getTimeOpenDefault();
            if (args.length > 1) {
                try {
                    if (args[1] != null)
                        number = Integer.parseInt(args[1]);
                } catch (NumberFormatException exception) {
                    //stuff here
                }
            }
            door.setTimeOpen(number);
            return;
        }
        if (!RunicDoors.inst().getDoors().containsKey(args[0] + "")) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Tried to open a Non-existant door! " + args[0]);
            return;
        }
        Door door = RunicDoors.inst().getDoors().get(args[0] + "");
        if (!RunicDoors.inst().getOpenDoors().containsKey(door.getId() + "")) {
            door.setOpen(true);
            //always set open first
            door.openForPlayer(null);

        }
        int number = door.getTimeOpenDefault();
        if (args.length > 1) {

            try {
                if (args[1] != null)
                    number = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        door.setTimeOpen(number);
    }

    @Subcommand("perms|setpermissions|p|setperms")
    @CommandCompletion("Doors.|none")
    public void onDoorPerms(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());

            player.sendMessage("New permission of " + range);
            door.setPermission(range);
            ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("range|distance|proximity|r")
    @CommandCompletion("1|2|3|4|5")
    public void onDoorRange(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            int number = 5;
            try {
                if (range != null)
                    number = Integer.parseInt(range);
            } catch (NumberFormatException e) {
                number = 5;
            }

            player.sendMessage("New range of " + number);
            door.setDistance(number);
            ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("select|s|getclosest")
    @CommandCompletion("")
    public void onDoorSelect(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }

        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            player.sendMessage("You already have a door selected, but I'll swap it for you");
            Door selection = DoorInteractor.getClosestNode(player);
            if (selection == null) {
                player.sendMessage("No Doors Nearby, still working on " + RunicDoors.inst().getEditors().get(player.getUniqueId()).getId());
                return;
            }
            player.sendMessage("Now working on " + selection.getId());
            RunicDoors.inst().getEditors().put(player.getUniqueId(), selection);

            Particles.drawPinPoint(selection.getLocation(), Color.RED, Color.WHITE);

        } else {
            Door selection = DoorInteractor.getClosestNode(player);
            if (selection == null) {
                player.sendMessage("No Doors Nearby");
                return;
            }
            player.sendMessage("Now working on " + selection.getId());
            RunicDoors.inst().getEditors().put(player.getUniqueId(), selection);
            Particles.drawPinPoint(selection.getLocation(), Color.RED, Color.WHITE);
        }
    }

    @Subcommand("selectid|sid")
    @CommandCompletion("1234|id")
    @Syntax("<id> &e- select by id")
    public void onDoorSelectById(Player player, String id) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getDoors().get(id) != null) {
            Door selection = RunicDoors.inst().getDoors().get(id);
            player.sendMessage("Now working on " + RunicDoors.inst().getEditors().get(player.getUniqueId()).getId());
            RunicDoors.inst().getEditors().put(player.getUniqueId(), selection);

            Particles.drawPinPoint(selection.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("That Door is not in our list.");
        }

    }

    @Subcommand("speed|quick|speedmultiplier")
    @CommandCompletion("1|2|3|4|5")
    public void onDoorSpeed(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            int number = 1;
            try {
                if (range != null)
                    number = Integer.parseInt(range);
            } catch (NumberFormatException e) {
                number = 5;
            }

            player.sendMessage("New speed of " + number);
            door.setSpeed(number);
            ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("time|openfor|openforseconds|t")
    @CommandCompletion("1|2|3|4|5")
    public void onDoorTime(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            int number = 5;
            try {
                if (range != null)
                    number = Integer.parseInt(range);
            } catch (NumberFormatException e) {
                number = 5;
            }

            player.sendMessage("New time of " + number);
            door.setDefaultTime(number);
            ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("view|v|showblocks")
    @CommandCompletion("")
    public void onDoorView(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.inst().getEditors().get(player.getUniqueId());
            DoorInteractor.showBlocks(door, Material.BLUE_STAINED_GLASS);
            RunicDoors.inst().getViewing().add(door.getId() + "");
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("nearby|n|near|show")
    @CommandCompletion("")
    public void onNearbyDoors(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        int i = 0;
        for (Door door : RunicDoors.inst().getDoorHandler().getDoorGrid().getNearbyNodes(player.getLocation())) {
            i++;
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        }
        player.sendMessage("There is " + i + " Doors Nearby!");
    }

    @Subcommand("applyregion|ar|regionset|rset|regionapply")
    @CommandCompletion("")
    public void onRegionToolApply(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (!RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            player.sendMessage("You don't have a door selected");
            return;
        }
        if (!RunicDoors.inst().getRegionTools().containsKey(player.getUniqueId())) {
            player.sendMessage("You don't have a region selected");
            return;
        }
        if (RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner1() == null) {
            player.sendMessage("You don't have a region selected");
            return;
        }
        if (RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner2() == null) {
            player.sendMessage("You don't have a region selected");
            return;
        }
        player.sendMessage("Total Blocks " + LocationUtil.countBlocks(RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner1(), RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner2()));
        ArrayList<DoorBlock> blocks = LocationUtil.viewDoorBlocksBetweenLocation(RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner1(), RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner2(), Material.GREEN_STAINED_GLASS);
        RunicDoors.inst().getEditors().get(player.getUniqueId()).setConnections(blocks);

        ConfigSave.saveNode(RunicDoors.inst().getEditors().get(player.getUniqueId()), RunicDoors.inst().getDoorFileConfig());

    }

    @Subcommand("regionselect|rs|regionpick|rtool|regiontool")
    @CommandCompletion("")
    public void onRegionToolGive(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        player.sendMessage("this feature is under construction!");
        // code here
        // give item
        player.getInventory().addItem(ItemUtil.getRegionItem());
        // use namespaced key

    }
}
