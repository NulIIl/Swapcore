package bristopher.swapcore.listeners;

import bristopher.swapcore.Swapcore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class Creepers implements Listener {

    private int SwapCreeperChance = 100; //swap spider spawn percent chance

    @EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        // Skip if the spawn reason is a plugin
        if (e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM) {
            return;
        }


        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof Creeper) {
            if (random.nextInt(1, 100) < SwapCreeperChance) {
                swapCreeper(entity);
            }
        }
    }

    public void swapCreeper(LivingEntity entity) {
        entity.setMetadata("swapCreeper", new FixedMetadataValue(Swapcore.getInstance(), true));
    }

    @EventHandler
    public void beforeSwapCreeperExplosion(ExplosionPrimeEvent event) { // when the creeper decides to explode (before actually exploding)
        if (event.getEntity() instanceof Creeper creeper && creeper.hasMetadata("swapCreeper")) {
            Bukkit.broadcastMessage("get location!");
            Location creeperLoc = creeper.getLocation(); //get location and turn into metadata
            creeper.setMetadata("creeperLoc", new FixedMetadataValue(Swapcore.getInstance(), creeperLoc));
        }
    }

    @EventHandler
    public void onSwapCreeperExplosion(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper creeper && creeper.hasMetadata("swapCreeper")) {
            Bukkit.broadcastMessage("Creeper exploded!");


            Location creeperLoc = (Location) creeper.getMetadata("creeperLoc").get(0).value(); //get metadata and turn into location
            // Find the farthest player
            Player farthestPlayer = null;
            double maxDistance = -1;
            for (Player player : creeper.getWorld().getPlayers()) {
                double distance = player.getLocation().distanceSquared(creeperLoc);
                if (distance > maxDistance) {
                    maxDistance = distance;
                    farthestPlayer = player;
                }
            }
            Location farthestLoc = farthestPlayer.getLocation();
            farthestLoc.getWorld().spawn(farthestLoc, Creeper.class);
            farthestLoc.getWorld().playSound(farthestLoc, Sound.ENTITY_CREEPER_HURT, 3.1f, 2.4f);

            creeperLoc.setPitch(farthestPlayer.getLocation().getPitch());  //keep player's pitch & yaw by applying it to the creeper location
            creeperLoc.setYaw(farthestPlayer.getLocation().getYaw());
            //teleport the farthest player to the creeper
                farthestPlayer.teleport(creeperLoc);


        }
    }


    public void setSwapCreeperChance(int SwapCreeperChance) {
        this.SwapCreeperChance = SwapCreeperChance;
    }
}