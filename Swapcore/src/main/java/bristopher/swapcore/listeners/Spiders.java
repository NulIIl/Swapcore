package bristopher.swapcore.listeners;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.metadata.FixedMetadataValue;

import bristopher.swapcore.Swapcore;

public class Spiders implements Listener {

    private int SwapSpiderChance = 80; //swap spider spawn percent chance

    @EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof Spider) {
            if (random.nextInt(1, 100) < SwapSpiderChance) {
                swapSpider(entity);
            }
        }
    }

    public void swapSpider(LivingEntity entity) {
        // Store both the swapSpider flag and its spawn location
        entity.setMetadata("swapSpider", new FixedMetadataValue(Swapcore.getInstance(), true));
        entity.setMetadata("spawnLocation", new FixedMetadataValue(Swapcore.getInstance(), entity.getLocation().clone()));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Spider spider && spider.hasMetadata("swapSpider")) {
            if (event.getEntity() instanceof Player player) {
                //get the stored spawn location
                if (spider.hasMetadata("spawnLocation")) {
                    Location spawnLoc = (Location) spider.getMetadata("spawnLocation").get(0).value(); //transfer from metadata to Location
                    if (player.getLocation().distanceSquared(spawnLoc) > 5) { //if player's location is more than 5 blocks away from the cobweb
                        spawnLoc.setPitch(player.getLocation().getPitch());  //keep player's pitch & yaw by applying it to the spider spawn location
                        spawnLoc.setYaw(player.getLocation().getYaw());
                        player.teleport(spawnLoc);  //teleport the player to the spider's spawn location
                        spawnLoc.getBlock().setType(org.bukkit.Material.COBWEB); //
                    }
				}
            }
        }
    }


    public void setSwapSpiderChance(int SwapSpiderChance) {
        this.SwapSpiderChance = SwapSpiderChance;
    }
}