package bristopher.swapcore.listeners;

import bristopher.swapcore.Swapcore;
import org.bukkit.Location;
import org.bukkit.block.data.type.Fire;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class Ghasts implements Listener {

    private int SwapGhastChance = 100; //swap spider spawn percent chance

    @EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof Ghast) {
            if (random.nextInt(1, 100) < SwapGhastChance) {
                swapGhast(entity);
            }
        }
    }

    public void swapGhast(LivingEntity entity) {
        entity.setMetadata("swapGhast", new FixedMetadataValue(Swapcore.getInstance(), true));
    }

    @EventHandler
    public void onFireballHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Fireball){
            Fireball fireball = (Fireball) event.getDamager();
            if (fireball.getShooter() instanceof Ghast ghast && ghast.hasMetadata("swapGhast")){
                Entity damaged = event.getEntity();
                if (damaged instanceof LivingEntity target){
                    if (event.getFinalDamage() > 0){
                        damaged.teleport(ghast.getLocation());
                    }

                }
            }
        }
    }



    public void setSwapGhastChance(int SwapGhastChance) {
        this.SwapGhastChance = SwapGhastChance;
    }
}