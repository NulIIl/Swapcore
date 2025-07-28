package bristopher.swapcore.listeners;

import bristopher.swapcore.Swapcore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.Material; // Import Material

import java.util.Random;

public class Blazes implements Listener {

    private int SwapBlazeChance = 100; //swap spider spawn percent chance

    @EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof Blaze) {
            if (random.nextInt(1, 100) < SwapBlazeChance) {
                swapBlaze(entity);
            }
        }
    }

    public void swapBlaze(LivingEntity entity) {
        // Store the swapBlaze flag
        entity.setMetadata("swapBlaze", new FixedMetadataValue(Swapcore.getInstance(), true));
    }

    @EventHandler
    public void onSwapBlazeHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof SmallFireball fireball && fireball.getShooter() instanceof Blaze blaze && blaze.hasMetadata("swapBlaze") //if a blaze with swap metadata hits player and it actually does damage
                && event.getEntity() instanceof Player player && event.getFinalDamage() > 0) {
            Bukkit.broadcastMessage("Blazefireball hit!");
            Location playerLoc = player.getLocation();
            Location nearestFire = null;
            double closestDistance = Double.MAX_VALUE;
            
            // Check blocks in a 20 block radius
            for (int x = -20; x <= 20; x++) {
                for (int y = -20; y <= 20; y++) {
                    for (int z = -20; z <= 20; z++) {
                        Location checkLoc = playerLoc.clone().add(x, y, z);
                        if (checkLoc.getBlock().getType() == Material.FIRE) {
                            double distance = playerLoc.distanceSquared(checkLoc);
                            if (distance < closestDistance) {
                                closestDistance = distance;
                                nearestFire = checkLoc;
                            }
                        }
                    }
                }
            }
            
            // If we found a fire block, teleport the player there
            if (nearestFire != null) {
                // Adjust location to be safe for teleport (center of block and slightly above)
                nearestFire.add(0.5, 0.1, 0.5);
                player.teleport(nearestFire);
            }
        }
    }


    public void setSwapBlazeChance(int SwapBlazeChance) {
        this.SwapBlazeChance = SwapBlazeChance;
    }
}