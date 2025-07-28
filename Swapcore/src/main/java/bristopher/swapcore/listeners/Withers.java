package bristopher.swapcore.listeners;

import bristopher.swapcore.Swapcore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class Withers implements Listener {

    private int SwapWitherChance = 100; //swap wither spawn percent chance

    @EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof Wither) {
            if (random.nextInt(1, 100) < SwapWitherChance) {
                swapWither(entity);
            }
        }
    }

    public void swapWither(LivingEntity entity) {
        // Store both the swapWither flag and its spawn location
        entity.setMetadata("swapWither", new FixedMetadataValue(Swapcore.getInstance(), true));
        entity.setMetadata("spawnLocation", new FixedMetadataValue(Swapcore.getInstance(), entity.getLocation().clone()));
        // Initialize half-health announcement tracker
        entity.setMetadata("halfHealthAnnounced", new FixedMetadataValue(Swapcore.getInstance(), false));
    }

    @EventHandler
    public void onWitherDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Wither wither && wither.hasMetadata("swapWither")) {
            // Check if the wither has not had its half-health announced yet
            if (!wither.getMetadata("halfHealthAnnounced").get(0).asBoolean()) {
                // Calculate half of max health
                double halfHealth = wither.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue() / 2;
                
                // If the damage would bring the wither below half health
                if ((wither.getHealth() - event.getFinalDamage()) <= halfHealth) {
                    // Mark as announced so it only happens once
                    wither.setMetadata("halfHealthAnnounced", new FixedMetadataValue(Swapcore.getInstance(), true));
                    // Make the announcement
                    Bukkit.broadcastMessage("The Swap Wither has fallen below half health!");
                    wither.setInvulnerabilityTicks(400);
                }
            }
        }
    }


    public void setSwapWitherChance(int SwapWitherChance) {
        this.SwapWitherChance = SwapWitherChance;
    }
}