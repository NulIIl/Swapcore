package bristopher.swapcore.listeners;

import bristopher.swapcore.Swapcore;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Dragons implements Listener {

    private boolean SwapDragonChance = true; //swap dragon toggle

    @EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof EnderDragon && SwapDragonChance) {

                swapDragon(entity);

        }
    }

    public void swapDragon(LivingEntity entity) {
        // Store the metadata
        entity.setMetadata("swapDragon", new FixedMetadataValue(Swapcore.getInstance(), true));
    }




    public void setSwapDragonChance(boolean SwapDragonChance) {
        this.SwapDragonChance = SwapDragonChance;
    }

    @EventHandler
    public void onDragonBreathDamage(EntityDamageByEntityEvent event) {
        // Check if the damage was caused by dragon's breath, and that it hit a player
        if (event.getDamager() instanceof AreaEffectCloud cloud && cloud.getParticle() == Particle.DRAGON_BREATH && event.getEntity() instanceof Player hitPlayer) {
            Bukkit.broadcastMessage("" + cloud.getParticle());
                // Create list of survival players excluding hit player
                List<Player> onlinePlayers = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getGameMode() == GameMode.SURVIVAL && player != hitPlayer) {
                        onlinePlayers.add(player);
                    }
                }

                // Pick random player from the list and swap with hit player
                if (!onlinePlayers.isEmpty()) {
                    Random random = new Random();
                    Player randomPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));
                    Location hitPlayerLoc = hitPlayer.getLocation();
                    Location randomPlayerLoc = randomPlayer.getLocation();
                    hitPlayer.teleport(randomPlayerLoc);
                    randomPlayer.teleport(hitPlayerLoc);

                }

        }
    }


    @EventHandler
    public void onCrystalDestroy(EntityDamageByEntityEvent event){
        if (event.getEntity() instanceof EnderCrystal crystal) {
            Player player;

            //if hit by player
            if (event.getDamager() instanceof Player){
                player = (Player) event.getDamager();
            //if hit by arrow shot by player
            } else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player){
                player = (Player) projectile.getShooter();
            } else {
                player = null;
            }
            // Schedule the teleport to run on the next tick to ensure it happens after the crystal explosion
            crystal.getServer().getScheduler().runTask(crystal.getServer().getPluginManager().getPlugin("Swapcore"), () -> {
                player.teleport(crystal.getLocation());
            });

        }
    }
}