package bristopher.swapcore.listeners;

import bristopher.swapcore.Swapcore;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;


public class Pigmen implements Listener {

		public int pigmanAutoaggroChance = 20; //pigman autoaggro percent chance
    	public int pigmanSwapChance = 33;  //pigman swap percent chance

	@EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
		if (entity instanceof PigZombie) {
			// Determine pigman variant using a single random value (0–99)
			int chance = random.nextInt(100);
			// If within the first 20%...
			if (chance < pigmanAutoaggroChance) {
				autoAggroPigman((PigZombie) entity);
			}
			// Otherwise, of the remaining 80%, 33% become swap pigmen:
			else
			if (chance < pigmanAutoaggroChance + (int) ((100 - pigmanAutoaggroChance) * (pigmanSwapChance / 100.0))) {
				swapPigman((PigZombie) entity);
			}
			// Else, the pigman remains normal.
		}
	}

@EventHandler
public void onPigmanDamaged(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof PigZombie pigman && pigman.hasMetadata("swapPigman")) {
        if (event.getDamager() instanceof LivingEntity attacker) {
            // Store the attacker's UUID in the pigman's metadata
            pigman.setMetadata("lastAttacker", new FixedMetadataValue(Swapcore.getInstance(), attacker.getUniqueId().toString()));
        }
    }
}

@EventHandler
public void preventAutoAggroPigmanSpread(EntityTargetLivingEntityEvent event) {
    if (event.getEntity() instanceof PigZombie pigman && pigman.hasMetadata("swapPigman")) {
        if (event.getTarget() instanceof LivingEntity target) {
            // Check if this target previously attacked this pigman
            if (pigman.hasMetadata("lastAttacker")) {
                String lastAttackerUUID = (String) pigman.getMetadata("lastAttacker").get(0).value();
                if (target.getUniqueId().toString().equals(lastAttackerUUID)) {
                    return; // Allow targeting if this entity attacked our pigman
                }
            } else if (event.getReason() == EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
				// Cancel all other targeting events (anger spread)
				event.setCancelled(true);
			}

        }
    }
}



	public void autoAggroPigman(PigZombie pigman) {
        // Mark as an autoaggro swap pigman.
        pigman.setMetadata("swapPigman", new FixedMetadataValue(Swapcore.getInstance(), "swap"));



        // Give a stone sword in the main hand.
        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
        pigman.getEquipment().setItemInMainHand(stoneSword);
        pigman.getEquipment().setItemInMainHandDropChance(0.04F);


		// Schedule a task to assign a target.
		Bukkit.getScheduler().runTaskLater(Swapcore.getInstance(), () -> {
			List<Player> players = pigman.getWorld().getPlayers();
			if (!players.isEmpty()) {
				// Find the nearest player.
				Player nearest = null;
				double minDist = Double.MAX_VALUE;
				Location pigLoc = pigman.getLocation();
				for (Player p : players) {
					double dist = pigLoc.distanceSquared(p.getLocation());
					if (dist < minDist) {
						minDist = dist;
						nearest = p;
					}
				}
				if (nearest != null) {
					pigman.setTarget(nearest);
					Bukkit.broadcastMessage("Autoaggro Pigman Targeted");

				}
			}
		}, 1L);
    }

	public void swapPigman(PigZombie pigman) {
        // Mark as a swap pigman.
        pigman.setMetadata("swapPigman", new FixedMetadataValue(Swapcore.getInstance(), "swap"));
        // Set purple leather boots.
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) leatherBoots.getItemMeta();
        bootsMeta.setColor(Color.PURPLE);
        bootsMeta.addEnchant(Enchantment.MENDING, 1, true);
        leatherBoots.setItemMeta(bootsMeta);
        pigman.getEquipment().setBoots(leatherBoots);
        pigman.getEquipment().setBootsDropChance(0.04F);
    }



	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{


		if (event.getDamager() instanceof PigZombie pigman && pigman.hasMetadata("swapPigman")) {
            if (event.getEntity() instanceof Player hitPlayer) {
                // Gather all pigmen from every loaded world.
                List<PigZombie> pigmen = new ArrayList<>();
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (entity instanceof PigZombie p) {
                            pigmen.add(p);
                        }
                    }
                }
                // Optionally remove the attacker to avoid swapping with itself.
                pigmen.remove(pigman);
                if (!pigmen.isEmpty()) {
                    Random random = new Random();
                    PigZombie randomPigman = pigmen.get(random.nextInt(pigmen.size()));
                    Location pigmanLoc = randomPigman.getLocation();
                    Location playerLoc = hitPlayer.getLocation();
                    hitPlayer.teleport(pigmanLoc);
                    randomPigman.teleport(playerLoc);
                }
            }
        }
	}

	public int getPigmanAutoaggroChance() {
		return pigmanAutoaggroChance;
  	}
	public void setPigmanAutoaggroChance(int pigmanAutoaggroChance) {
  		this.pigmanAutoaggroChance = pigmanAutoaggroChance;
	}

	public int getPigmanSwapChance() {
		return pigmanSwapChance;
  	}
	public void setPigmanSwapChance(int pigmanSwapChance) {
  		this.pigmanSwapChance = pigmanSwapChance;
	}
}