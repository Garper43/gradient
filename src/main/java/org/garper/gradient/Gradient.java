package org.garper.gradient;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.LinkedList;
import java.util.Random;

public class Gradient {
    private LinkedList<Material> materials = new LinkedList<Material>();
    private float verticalSpread = 1;
    private int radius = 0;

    public Gradient() {
    }

    public Gradient(int radius, LinkedList<Material> materials, float horizontalSpread) {
        this.radius = radius;
        this.materials = materials;
        this.verticalSpread = horizontalSpread;
    }

    public int getRadius() {
        return radius;
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }

    public LinkedList<Material> getMaterials() {
        return materials;
    }
    public void setMaterials(LinkedList<Material> materials) {
        this.materials = materials;
    }
    public boolean addMaterial(Material material) {
        return materials.add(material);
    }

    public float getHorizontalSpread() {
        return verticalSpread;
    }
    public void setHorizontalSpread(float horizontalSpread) {
        this.verticalSpread = horizontalSpread;
    }

    public void apply(Location location) {

        World world = location.getWorld();
        Random rand = new Random();

        for(int a = -radius; a < radius; a++) {
            for(int b = -radius; b < radius; b++) {
                for(int c = -radius; c < radius; c++) {
                    int x = location.getBlockX() + a;
                    int z = location.getBlockZ() + b;
                    int y = location.getBlockY() + c;

                    Block block = world.getBlockAt(x, y ,z);
                    if(block.getType() == Material.AIR) {
                        continue;
                    }

                    //choose material
                    int index = (int) ( (double) (c+radius)/(radius*2)*materials.size() + rand.nextDouble()*verticalSpread - verticalSpread/2 );
                    if(index < 0) {
                        index = 0;
                    } else if(index >= materials.size()) {
                        index = materials.size() - 1;
                    }
                    Material material = materials.get(index);

                    block.setType(material);
                }
            }
        }

    }
}
