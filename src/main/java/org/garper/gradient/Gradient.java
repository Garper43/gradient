package org.garper.gradient;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Random;

public class Gradient {
    private ArrayList<Material> materials;
    private float verticalSpread;
    private int verticalShift;
    private boolean inverted;

    public Gradient() {
        materials = new ArrayList<>();
        verticalSpread = 1;
        verticalShift = 0;
        inverted = false;
    }

    public Gradient(ArrayList<Material> materials, float horizontalSpread, int verticalShift, boolean inverted) {
        this.materials = materials;
        this.verticalSpread = horizontalSpread;
        this.verticalShift = verticalShift;
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

    public int getVerticalShift() {
        return verticalShift;
    }
    public void setVerticalShift(int verticalShift) {
        this.verticalShift = verticalShift;
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

        //edit blocks
        region.forEach((vector) -> {
            BlockVector3 v3 = (BlockVector3) vector;

            int x = v3.getX();
            int z = v3.getZ();
            int y = v3.getY();
            int relY = v3.getY() - region.getMinimumY();
            Block block = world.getBlockAt(x, y ,z);

            if(block.getType() != Material.AIR && (mask == null || mask.test(v3))) {
                //choose material
                Random rand2 = new Random();
                rand2.setSeed(Long.parseLong("" + (x*z+z*z/x) ));
                int shiftVal = 2*(rand2.nextInt(verticalShift + 1) - verticalShift/2);
                int index = (int) ( (double) (relY + shiftVal)/(height)*materials.size() + verticalSpread*(rand.nextDouble() - 0.5));

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
