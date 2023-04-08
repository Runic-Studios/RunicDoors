package com.runicrealms.runicdoors.commands;

import com.runicrealms.libs.acf.BaseCommand;
import com.runicrealms.libs.acf.annotation.CommandAlias;
import com.runicrealms.libs.acf.annotation.CommandCompletion;
import com.runicrealms.libs.acf.annotation.Subcommand;
import com.runicrealms.libs.acf.annotation.Syntax;
import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.config.ConfigSave;
import com.runicrealms.runicdoors.config.DoorDelete;
import com.runicrealms.runicdoors.doorStuff.Door;
import com.runicrealms.runicdoors.doorStuff.DoorBlock;
import com.runicrealms.runicdoors.doorStuff.DoorInteractor;
import com.runicrealms.runicdoors.utility.ItemUtil;
import com.runicrealms.runicdoors.utility.LocationUtil;
import com.runicrealms.runicdoors.utility.Particles;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.logging.Level;

@CommandAlias("door|doors|d|doorcommand|runicdoors|runicdoor")
public class DoorCommand extends BaseCommand {
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
        Door door = new Door(player.getLocation(), id, "none", 5, new ArrayList<>(), false, 3, "BASIC","BASIC",1,"none");
        ConfigSave.saveNode(door, RunicDoors.getRunicDoors().getDoorFileConfig());
        RunicDoors.getRunicDoors().getDoorHandler().placeDoorInGrid(door);
        RunicDoors.getRunicDoors().getDoors().put(door.getId() + "", door);
        player.sendMessage("Its id is " + id);
        RunicDoors.getRunicDoors().getEditors().put(player.getUniqueId(), door);

        Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
    }

    @Subcommand("list|l")
    @CommandCompletion("")
    public void onDoorList(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        player.sendMessage(RunicDoors.getRunicDoors().getDoors().size()+ " Doors on the server");
    }
    @Subcommand("edit|e|disable|turnoffediting|editoff")
    @CommandCompletion("")
    public void onDoorEdit(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            player.sendMessage("Turned off editing mode");
            RunicDoors.getRunicDoors().getEditors().remove(player.getUniqueId());
        } else {
            player.sendMessage("create a door, or select one to turn on editing!");
        }
    }

    @Subcommand("select|s|getclosest")
    @CommandCompletion("")
    public void onDoorSelect(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }

        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            player.sendMessage("You already have a door selected, but I'll swap it for you");
            Door selection = DoorInteractor.getClosestNode(player);
            if (selection == null) {
                player.sendMessage("No Doors Nearby, still working on " + RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId()).getId());
                return;
            }
            player.sendMessage("Now working on " + selection.getId());
            RunicDoors.getRunicDoors().getEditors().put(player.getUniqueId(), selection);

            Particles.drawPinPoint(selection.getLocation(), Color.RED, Color.WHITE);

        } else {
            Door selection = DoorInteractor.getClosestNode(player);
            if (selection == null) {
                player.sendMessage("No Doors Nearby");
                return;
            }
            player.sendMessage("Now working on " + selection.getId());
            RunicDoors.getRunicDoors().getEditors().put(player.getUniqueId(), selection);
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
        if (RunicDoors.getRunicDoors().getDoors().get(id) != null) {
            Door selection = RunicDoors.getRunicDoors().getDoors().get(id);
            player.sendMessage("Now working on " + RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId()).getId());
            RunicDoors.getRunicDoors().getEditors().put(player.getUniqueId(), selection);

            Particles.drawPinPoint(selection.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("That Door is not in our list.");
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
        for (Door door : RunicDoors.getRunicDoors().getDoorHandler().getDoorGrid().getNearbyNodes(player.getLocation())) {
            i++;
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        }
        player.sendMessage("There is " + i + " Doors Nearby!");
    }

    @Subcommand("delete|d")
    @CommandCompletion("")
    public void onDoorDelete(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            player.sendMessage("Deleted ");
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            RunicDoors.getRunicDoors().getDoorHandler().getDoorGrid().removeElementInGrid(RunicDoors.getRunicDoors().getDoorHandler().getDoorGrid().getGridLocationFromLocation(door.getLocation()), door);
            DoorDelete.deleteNode(door.getId() + "", RunicDoors.getRunicDoors().getDoorFileConfig());
            RunicDoors.getRunicDoors().getDoors().remove(door.getId() + "");
            RunicDoors.getRunicDoors().getEditors().remove(player.getUniqueId());
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
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            int number = 5;
            try {
                if (range != null)
                    number = Integer.parseInt(range);
            } catch (NumberFormatException e) {
                number = 5;
            }

            player.sendMessage("New range of " + number);
            door.setDistance(number);
            ConfigSave.saveNode(door, RunicDoors.getRunicDoors().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("speed|quick|speedmultiplier")
    @CommandCompletion("1|2|3|4|5")
    public void onDoorSpeed(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            int number = 1;
            try {
                if (range != null)
                    number = Integer.parseInt(range);
            } catch (NumberFormatException e) {
                number = 5;
            }

            player.sendMessage("New speed of " + number);
            door.setSpeed(number);
            ConfigSave.saveNode(door, RunicDoors.getRunicDoors().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
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
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            int number = 1;
            try {
                if (range != null)
                    number = Integer.parseInt(range);
            } catch (NumberFormatException e) {
                number = 3;
            }

            player.sendMessage("New knock back of " + number);
            door.setKnockback(number);
            ConfigSave.saveNode(door, RunicDoors.getRunicDoors().getDoorFileConfig());
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
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            int number = 5;
            try {
                if (range != null)
                    number = Integer.parseInt(range);
            } catch (NumberFormatException e) {
                number = 5;
            }

            player.sendMessage("New time of " + number);
            door.setDefaultTime(number);
            ConfigSave.saveNode(door, RunicDoors.getRunicDoors().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("setanimation|animation|anim|a")
    @CommandCompletion("@Animations")
    public void onDoorAnimate(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            String text = "BASIC";
            if (RunicDoors.getRunicDoors().getAnimator().animations.containsKey(range)) {
                text = range;
            }

            player.sendMessage("New animation of " + text);
            door.setAnimation(range);
            ConfigSave.saveNode(door, RunicDoors.getRunicDoors().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
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
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            String text = "BASIC";
            if (RunicDoors.getRunicDoors().getCloseAnimator().animations.containsKey(range)) {
                text = range;
            }

            player.sendMessage("New animation of " + text);
            door.setCloseAnimation(range);
            ConfigSave.saveNode(door, RunicDoors.getRunicDoors().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("perms|setpermissions|p|setperms")
    @CommandCompletion("Doors.|none")
    public void onDoorPerms(Player player, String range) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());

            player.sendMessage("New permission of " + range);
            door.setPermission(range);
            ConfigSave.saveNode(door, RunicDoors.getRunicDoors().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
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
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());

            player.sendMessage("New message of " + range);
            door.setDenyMessage(range);
            ConfigSave.saveNode(door, RunicDoors.getRunicDoors().getDoorFileConfig());
            Particles.drawPinPoint(door.getLocation(), Color.RED, Color.WHITE);
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("open|o|opendoor|unlock|dooropen|opensesame")
    @CommandCompletion("")
    public void onDoorOpen(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            if (!RunicDoors.getRunicDoors().getOpenDoors().containsKey(door.getId() + "")) {
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
        if(args.length==0){return;}
        if(sender instanceof  Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("No perms");
                return;
            }
            if (!RunicDoors.getRunicDoors().getDoors().containsKey(args[0] + "")) {player.sendMessage("That isn't a real door! "+args[0]); return;}
            Door door =RunicDoors.getRunicDoors().getDoors().get(args[0] + "");
            if(!RunicDoors.getRunicDoors().getOpenDoors().containsKey(door.getId()+"")) {
                door.setOpen(true);
                //always set open first
                door.openForPlayer(player);

            }
            int number = door.getTimeOpenDefault();
            if(args.length>1){
            try {
                if (args[1] != null)
                    number = Integer.parseInt(args[1]);
            } catch (NumberFormatException exception) {
                //stuff here
            }}
            door.setTimeOpen(number);
            return;
        }
        if (!RunicDoors.getRunicDoors().getDoors().containsKey(args[0] + "")) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Tried to open a Non-existant door! "+args[0]);
            return;
        }
        Door door =RunicDoors.getRunicDoors().getDoors().get(args[0] + "");
        if(!RunicDoors.getRunicDoors().getOpenDoors().containsKey(door.getId()+"")) {
            door.setOpen(true);
            //always set open first
            door.openForPlayer(null);

        }
        int number = door.getTimeOpenDefault();
        if(args.length>1) {

            try {
                if (args[1] != null)
                    number = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
            }
        }
        door.setTimeOpen(number);
    }

    @Subcommand("closeid|ci|closedoorbyid|lockid|doorcloseid|closesesameid")
    @CommandCompletion("id|1234")
    public void onDoorCloseByID(CommandSender sender, String[] args) {
        if(args.length==0){return;}
        if(sender instanceof  Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage("No perms");
                return;
            }
            if (!RunicDoors.getRunicDoors().getDoors().containsKey(args[0] + "")) {player.sendMessage("That isn't a real door! "+args[0]); return;}
            Door door =RunicDoors.getRunicDoors().getDoors().get(args[0] + "");
            if (RunicDoors.getRunicDoors().getOpenDoors().containsKey(door.getId() + "")) {
                RunicDoors.getRunicDoors().getOpenDoors().remove(door.getId() + "");
            }

            door.closeForPlayer(player);

            return;
        }
        if (!RunicDoors.getRunicDoors().getDoors().containsKey(args[0] + "")) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Tried to open a Non-existant door! "+args[0]);
            return;
        }
        Door door =RunicDoors.getRunicDoors().getDoors().get(args[0] + "");
        if (RunicDoors.getRunicDoors().getOpenDoors().containsKey(door.getId() + "")) {
            RunicDoors.getRunicDoors().getOpenDoors().remove(door.getId() + "");
        }

        door.closeForPlayer(null);
    }

    @Subcommand("view|v|showblocks")
    @CommandCompletion("")
    public void onDoorView(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            DoorInteractor.showBlocks(door, Material.BLUE_STAINED_GLASS);
            RunicDoors.getRunicDoors().getViewing().add(door.getId()+"");
        } else {
            player.sendMessage("You don't have a door selected!");
        }
    }

    @Subcommand("hide|h|showblocks")
    @CommandCompletion("")
    public void onDoorHide(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());
            DoorInteractor.hideBlocks(door);

            RunicDoors.getRunicDoors().getViewing().remove(door.getId()+"");
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
        if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) {
            Door door = RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId());

            if (RunicDoors.getRunicDoors().getOpenDoors().containsKey(door.getId() + "")) {
                RunicDoors.getRunicDoors().getOpenDoors().remove(door.getId() + "");
            }

            door.closeForPlayer(player);

        } else {
            player.sendMessage("You don't have a door selected!");
        }
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

    @Subcommand("applyregion|ar|regionset|rset|regionapply")
    @CommandCompletion("")
    public void onRegionToolApply(Player player) {
        if (!player.isOp()) {
            player.sendMessage("No perms");
            return;
        }
        if(!RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())){
            player.sendMessage("You don't have a door selected");
            return;
        }
        if(!RunicDoors.getRunicDoors().getRegionTools().containsKey(player.getUniqueId())){
            player.sendMessage("You don't have a region selected");
            return;
        }
        if(RunicDoors.getRunicDoors().getRegionTools().get(player.getUniqueId()).getCorner1()==null){
            player.sendMessage("You don't have a region selected");
            return;
        }
        if(RunicDoors.getRunicDoors().getRegionTools().get(player.getUniqueId()).getCorner2()==null){
            player.sendMessage("You don't have a region selected");
            return;
        }
        player.sendMessage("Total Blocks "+LocationUtil.countBlocks(RunicDoors.getRunicDoors().getRegionTools().get(player.getUniqueId()).getCorner1(),RunicDoors.getRunicDoors().getRegionTools().get(player.getUniqueId()).getCorner2()));
        ArrayList<DoorBlock> blocks = LocationUtil.viewDoorBlocksBetweenLocation(RunicDoors.getRunicDoors().getRegionTools().get(player.getUniqueId()).getCorner1(),RunicDoors.getRunicDoors().getRegionTools().get(player.getUniqueId()).getCorner2(),Material.GREEN_STAINED_GLASS);
        RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId()).setConnections(blocks);

        ConfigSave.saveNode(RunicDoors.getRunicDoors().getEditors().get(player.getUniqueId()), RunicDoors.getRunicDoors().doorFileConfig);

    }
}
