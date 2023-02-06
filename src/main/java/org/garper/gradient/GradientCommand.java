package org.garper.gradient;

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
import java.util.Scanner;

public class GradientCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //check if called by a player
        if(!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();
        Gradient gradient = new Gradient();

        //read radius
        gradient.setRadius(Integer.parseInt(args[0]));

        //read materials
        String[] materialsArr = args[1].split(",");
        for(String i : materialsArr) {
            gradient.addMaterial(Material.matchMaterial(i, false));
        }

        //read spread
        gradient.setHorizontalSpread(Float.parseFloat(args[2]));

        gradient.apply(location);

        return true;
    }
}
