package org.garper.gradient;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extension.factory.PatternFactory;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.MaskIntersection;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.factory.CuboidRegionFactory;
import com.sk89q.worldedit.session.SessionManager;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.checkerframework.checker.units.qual.A;
import sun.awt.OSInfo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Gradient {
    private ArrayList<Material> materials = new ArrayList<>();
    private float verticalSpread = 1;
    private boolean inverted = false;

    public Gradient() {
    }

    public Gradient(int radius, ArrayList<Material> materials, float horizontalSpread, boolean inverted) {
        this.materials = materials;
        this.verticalSpread = horizontalSpread;
        this.inverted = inverted;
    }

    public ArrayList<Material> getMaterials() {
        return materials;
    }
    public void setMaterials(ArrayList<Material> materials) {
        this.materials = materials;
    }
    public boolean addMaterial(Material material) {
        return materials.add(material);
    }

    public float getVerticalSpread() {
        return verticalSpread;
    }
    public void setVerticalSpread(float horizontalSpread) {
        this.verticalSpread = horizontalSpread;
    }

    public boolean isInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    public void apply(LocalSession session) throws IncompleteRegionException {
        CuboidRegion region = (CuboidRegion) session.getSelection();
        Mask mask = session.getMask();

        World world = BukkitAdapter.adapt(region.getWorld());
        Random rand = new Random();
        int height = region.getHeight();

        region.forEach((vector) -> {
            BlockVector3 v3 = (BlockVector3) vector;

            int x = v3.getX();
            int z = v3.getZ();
            int y = v3.getY();
            int relY = v3.getY() - region.getMinimumY();
            Block block = world.getBlockAt(x, y ,z);

            if(block.getType() != Material.AIR && (mask == null || mask.test(v3))) {
                //choose material
                int index = (int) ( (double) (relY)/(height)*materials.size() + verticalSpread*(rand.nextDouble() - 0.5) );

                if(index < 0) {
                    index = 0;
                } else if(index >= materials.size()) {
                    index = materials.size() - 1;
                }

                //invert index
                if(inverted) {
                    index = materials.size() - index - 1;
                }

                Material material = materials.get(index);

                block.setType(material);
            }
        });
    }
}
