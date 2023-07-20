package org.garper.gradient;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.SessionManager;
import jdk.tools.jlink.plugin.Plugin;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class GradientCommand implements CommandExecutor {

    private Map<String, String> legacyIDs;

    GradientCommand(Map<String, String> legacyIDs) {
        this.legacyIDs = legacyIDs;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //check if called by a player
        if(!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        //check if too many arguments
        if(args.length > 4) {
            player.sendMessage("§4Too many arguments");
            return false;
        }

        try {
            Location location = player.getLocation();
            Gradient gradient = new Gradient();

            //read materials
            String[] materialsArr = args[0].split(",");
            for(String i : materialsArr) {
                Material material = Material.matchMaterial(i);
                if(material == null) {
                    material = Material.matchMaterial(legacyIDs.get(i));
                }
                gradient.addMaterial(material);
            }

            //read spread
            gradient.setVerticalSpread(Float.parseFloat(args[1]));

            //read vertical shift
            if(args.length >= 3) {
                gradient.setVerticalShift(Integer.parseInt(args[2]));
            }

            //check whether inverted
            if(args.length == 4) {
                if(args[3].equals("-i")) {
                    gradient.setInverted(true);
                } else {
                    player.sendMessage("§4Invalid flag");
                }
            }

            //get player's local session
            SessionManager sessionManager = WorldEdit.getInstance().getSessionManager();
            LocalSession session = sessionManager.get(BukkitAdapter.adapt(player));
            CuboidRegion region = (CuboidRegion) session.getSelection();

            //apply gradient
            gradient.apply(session);
            player.sendMessage("§dSuccessfully applied gradient");

        } catch(Exception e) {
            e.printStackTrace();
            player.sendMessage("§4Invalid command");
            return false;
        }

        return true;
    }
}
