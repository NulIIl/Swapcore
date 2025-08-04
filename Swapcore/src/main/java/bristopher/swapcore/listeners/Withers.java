package bristopher.swapcore.listeners;

import bristopher.swapcore.Game;
import bristopher.swapcore.Swapcore;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.craftbukkit.v1_21_R5.entity.CraftWither;
import net.minecraft.world.entity.boss.wither.EntityWither;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import javax.naming.Name;
import java.util.*;
import java.util.stream.Collectors;

public class Withers implements Listener {

    private boolean SwapWitherChance = true; //swap wither toggle

    @EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof Wither && SwapWitherChance) {
            swapWither(entity);
        }
    }

    public void swapWither(LivingEntity entity) {
        // Store both the swapWither flag and for when it reaches half health
        entity.setMetadata("swapWither", new FixedMetadataValue(Swapcore.getInstance(), true));
        // Initialize half-health announcement tracker
        entity.setMetadata("halfHealthAnnounced", new FixedMetadataValue(Swapcore.getInstance(), false));
        //make all players swap with each other upon wither activation
        Bukkit.getScheduler().runTaskLater(Swapcore.getInstance(), Game::massSwap, 220L); //11 seconds AKA wither activation time
    }

    public void pushWitherOpposite(Wither wither) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 300 || wither.isDead()) {
                    this.cancel();
                    return;
                }

                // Get current velocity
                Vector currentVelocity = wither.getVelocity();

                // Create opposite vector with same magnitude
                Vector oppositeVelocity = currentVelocity.multiply(-1);

                // Apply the new velocity
                wither.setVelocity(oppositeVelocity);

                ticks++;
            }
        }.runTaskTimer(Swapcore.getInstance(), 0L, 1L); // 0L = start immediately, 1L = run every tick
    }


    @EventHandler
    public void onWitherDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Wither wither && wither.hasMetadata("swapWither")) {
            // Check if the wither has not had its half-health announced yet
            if (!wither.getMetadata("halfHealthAnnounced").get(0).asBoolean()) {
                // Calculate half of max health
                double halfHealth = wither.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue() / 2;
                
                // If the damage would bring the wither below half health
                if ((wither.getHealth() - event.getFinalDamage()) <= halfHealth) {
                    //mark as announced so it only happens once
                    wither.setMetadata("halfHealthAnnounced", new FixedMetadataValue(Swapcore.getInstance(), true));



                    pushWitherOpposite(wither);// calls the weird opposite velocity vector bukkit runnable

                    wither.setInvulnerabilityTicks(300); //redo summon animation

                    Bukkit.getScheduler().runTaskLater(Swapcore.getInstance(), () ->{ //run only after summon animation
                        Game.massSwap(); //make all players swap with each other upon wither activation
                    }, 300L);
                }
            } else {
                // makes a threshold that tracks every 20 hearts of damage
                double newHealth = wither.getHealth() - event.getFinalDamage();
                double currentThreshold = Math.ceil(wither.getHealth() / 40.0) * 40;
                double newThreshold = Math.ceil(newHealth / 40.0) * 40;
                Player damager = null;

                if (newThreshold < currentThreshold || wither.getHealth() <= 120) { //if the damage crossed a new threshold or wither has <= 60 hearts
                    if (event.getDamager() instanceof Player) { //if the player did damage, make them the damager
                        damager = (Player) event.getDamager();
                    } else if (event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player) { //if the player shot the arrow, make them the damager
                        damager = (Player) arrow.getShooter();
                    }

                    if (damager != null){
                        // Swap the damager with a random online survival mode player (excluding the damager)
                        //create list of survival players excluding damager
                        List<Player> onlinePlayers = new ArrayList<>();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.getGameMode() == GameMode.SURVIVAL && player != damager) {
                                onlinePlayers.add(player);
                            }
                        }
                        // picks random player from the list and swaps damager with random player
                        if (!onlinePlayers.isEmpty()) {
                            Random random = new Random();
                            Player randomPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
                            Location damagerLoc = damager.getLocation();
                            Location randomPlayerLoc = randomPlayer.getLocation();
                            damager.teleport(randomPlayerLoc);
                            randomPlayer.teleport(damagerLoc);
                        }
                    }
                }

            }
        }
    }

    @EventHandler
    public void onSkullProjHit(EntityDamageByEntityEvent event) {
        // Check if the damager is a wither skull
        if (event.getDamager() instanceof WitherSkull skull) {
            // Check if the shooter is a swap wither
            if (skull.getShooter() instanceof Wither wither && wither.hasMetadata("swapWither")) {
                event.setDamage(event.getDamage() * 0.6); //reduce damage by 40%
                // Check if the entity hit is a player
                if (event.getEntity() instanceof Player player) {
                    // If no damage was dealt, don't swap
                    if (event.getFinalDamage() <= 0) {
                        return;
                    }

                    // Get locations
                    Location witherLoc = wither.getLocation();
                    Location playerLoc = player.getLocation();
                    
                    // Perform the swap
                    wither.teleport(playerLoc);
                    player.teleport(witherLoc);
                }
            }
        }
    }




    public void setSwapWitherChance(boolean SwapWitherChance) {
        this.SwapWitherChance = SwapWitherChance;
    }
}