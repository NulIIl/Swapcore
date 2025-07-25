package bristopher.swapcore.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.util.datafix.fixes.DataConverterEntityZombifiedPiglinRename;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import bristopher.swapcore.Swapcore;

public class MobListener implements Listener {

		//Spawn Chance Varaibles
		private int SwapSkelChance = 100; //swap skeleton spawn chance, out of 100
		private int SwapZombChance = 50; //swap skeleton spawn chance, out of 100
		public int pigmanAutoaggroChance = 20; //pigman autoaggro chance, out of 100
    	public int pigmanSwapChance = 33;  //pigman swap chance, out of 100

		//Equipment Base Variables
		private double SwapZombSpeed = 2.5; //swap skeleton punch level, out of 5
		private int SwapSkelPunch = 2; //swap skeleton punch level, out of 5

	@EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
		if (entity instanceof Skeleton) {
			if (random.nextInt(1, 100) < SwapSkelChance) {
				SkeletonSwap(entity);
			}
		}
		else if (entity instanceof PigZombie) {
            // Determine pigman variant using a single random value (0–99)
            int chance = random.nextInt(100);
            // If within the first 20%...
            if (chance < pigmanAutoaggroChance) {
                autoaggroPigman((PigZombie) entity);
            }
            // Otherwise, of the remaining 80%, 33% become swap pigmen:
            else
				if (chance < pigmanAutoaggroChance + (int) ((100 - pigmanAutoaggroChance) * (pigmanSwapChance / 100.0))) {
                swapPigman((PigZombie) entity);
            }
            // Else, the pigman remains normal.
        }
		else if (entity instanceof Zombie) {
			if (random.nextInt(1, 100) < SwapZombChance) {
				ZombieSwap(entity);
			}
		}
		else{
			 //if not skeleton or zombie, do nothing
		}
    }

	public void SkeletonSwap(LivingEntity entity) {
		((Skeleton) entity).setMetadata("swapSkeleton", new FixedMetadataValue(Swapcore.getInstance(), true));
		ItemStack swapbow = new ItemStack(Material.BOW);
		swapbow.addUnsafeEnchantment(Enchantment.PUNCH, SwapSkelPunch);
		entity.getEquipment().setItemInMainHand(swapbow);
		entity.getEquipment().setItemInMainHandDropChance(0.04F);
		//creates knockback bow

		ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) leatherHelmet.getItemMeta();
		meta.setColor(Color.PURPLE);
		meta.addEnchant(Enchantment.MENDING, 1, true);
		leatherHelmet.setItemMeta(meta);
		entity.getEquipment().setHelmet(leatherHelmet);
		entity.getEquipment().setHelmetDropChance(0.04F);
		//makes shiny purple helmet
    }

	public void ZombieSwap(LivingEntity entity) {
		((Zombie) entity).setMetadata("swapZombie", new FixedMetadataValue(Swapcore.getInstance(), true));
		//sets a flag to identify the zombie as a swap zombie
	}

@EventHandler
	public void spawnAutoAggroPigman(EntityTargetLivingEntityEvent event) {
		Bukkit.getLogger().info(event.getReason().toString());
	}
	public void autoaggroPigman(PigZombie pigman) {
        // Mark as an autoaggro swap pigman.
        pigman.setMetadata("swapPigman", new FixedMetadataValue(Swapcore.getInstance(), "swap"));


		// Set purple leather boots.
        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) leatherBoots.getItemMeta();
        bootsMeta.setColor(Color.PURPLE);
        bootsMeta.addEnchant(Enchantment.MENDING, 1, true);
        leatherBoots.setItemMeta(bootsMeta);
        pigman.getEquipment().setBoots(leatherBoots);
        pigman.getEquipment().setBootsDropChance(0.04F);

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
		if (event.getDamager() instanceof Arrow arrow) {//if arrow
			if (arrow.getShooter() instanceof Skeleton skeleton && skeleton.hasMetadata("swapSkeleton")) {//if swap skeleton}
				Entity damaged = event.getEntity();
				if (damaged instanceof LivingEntity target) {//if player
					if (event.getFinalDamage() <= 0) {
						return;
					}
					Location skelLoc = skeleton.getLocation();
					Location targetLoc = target.getLocation();
						//store skeleton and player

					Vector knockbackVelocity = arrow.getVelocity().normalize().multiply(SwapSkelPunch);
					skeleton.teleport(targetLoc);
					target.teleport(skelLoc);
					Bukkit.getScheduler().runTask(Swapcore.getInstance(), () -> {target.setVelocity(knockbackVelocity);});
						//swap player and skeleton and keep momentum
				}
			}
		}

		if (event.getDamager() instanceof Zombie zombie && zombie.hasMetadata("swapZombie")) {
            if (event.getEntity() instanceof Player hitPlayer) {
                ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
                LeatherArmorMeta meta = (LeatherArmorMeta) leatherHelmet.getItemMeta();
                meta.setColor(Color.PURPLE);
                meta.addEnchant(Enchantment.MENDING, 1, true);
                leatherHelmet.setItemMeta(meta);
                zombie.getEquipment().setHelmet(leatherHelmet);
                zombie.getEquipment().setHelmetDropChance(0.04F);

                // Cosmetic addition: give a stick in the off-hand
                ItemStack cosmeticStick = new ItemStack(Material.STICK);
				meta.addEnchant(Enchantment.MENDING, 1, true);
                zombie.getEquipment().setItemInOffHand(cosmeticStick);
                zombie.getEquipment().setItemInOffHandDropChance(0.04F);

                // Speed boost: multiply the base movement speed by 3 (~300% speed)
                if (zombie.getAttribute(Attribute.MOVEMENT_SPEED) != null) {
                    double baseSpeed = zombie.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue();
                    zombie.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(baseSpeed * SwapZombSpeed);
                }

                // Swap the hit player with a random online player (excluding the hit player)
                List<Player> onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
                onlinePlayers.remove(hitPlayer);
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
                // Remove the metadata so this effect only triggers once.
                pigman.removeMetadata("swapPigman", Swapcore.getInstance());
            }
        }
	}

	@EventHandler
	public void onEndermanTarget(EntityTargetEvent event) { //when an entity targets something
		if (event.getEntity() instanceof Enderman enderman) { //if the entity is an enderman
			if (event.getTarget() instanceof Player player) { //and if the target is a player
				// gives the enderman the swapEnderman metadata and stores the player's UUID in that metadata
				enderman.setMetadata("swapEnderman", new FixedMetadataValue(Swapcore.getInstance(), player.getUniqueId().toString()));
				// if not already set, give the enderman the lastTeleport metadata and stores the coordinates of where that enderman was standing
				if (!enderman.hasMetadata("lastTeleport")) {
					enderman.setMetadata("lastTeleport", new FixedMetadataValue(Swapcore.getInstance(), enderman.getLocation().clone()));
				}

				// makes a scheduler that runs the code after 30 ticks
				Bukkit.getScheduler().runTaskLater(Swapcore.getInstance(), () -> {
					if (!enderman.isDead() && enderman.hasMetadata("swapEnderman")) {
						// schedule 5 teleports with small delays between them
						for (int i = 0; i < 5; i++) {
							int finalI = i;
							Bukkit.getScheduler().runTaskLater(Swapcore.getInstance(), () -> {
								if (!enderman.isDead()) {
									enderman.teleport();
									Bukkit.broadcastMessage(String.valueOf(finalI));
								}
							}, i * 10L); // 10 ticks (0.5 second) between each teleport
						}
					}
				}, 30L); // 20 ticks = 1 second






			} else {
				// if the target is not a player, clear the metadata
				enderman.removeMetadata("swapEnderman", Swapcore.getInstance());
				enderman.removeMetadata("lastTeleport", Swapcore.getInstance());
			}
		}
	}

	@EventHandler
	public void onEndermanTeleport(EntityTeleportEvent event) {
		//if an enderman has the swapEnderman metadata
		if (event.getEntity() instanceof Enderman enderman && enderman.hasMetadata("swapEnderman")) {
			// if the enderman has lastTeleport metadata
			if (enderman.hasMetadata("lastTeleport")) {
				//create the Location variable lastLoc to store the lastTeleport metadata (coords)
				Location lastLoc = (Location) enderman.getMetadata("lastTeleport").get(0).value();
				Bukkit.broadcastMessage(String.valueOf(lastLoc)); //broadcast the location in chat to test that it works, delete later
				// create the List variable targetMeta to store the swapEnderman metadata (player id)
				List<MetadataValue> targetMeta = enderman.getMetadata("swapEnderman");
				if (!targetMeta.isEmpty()) {
					String uuidStr = targetMeta.get(0).asString(); //turn the List into a String
					try {
						UUID targetUUID = UUID.fromString(uuidStr); //turn the String into a UUID
						Player target = Bukkit.getPlayer(targetUUID); //make the player with the targetUUID the target
						if (target != null && target.isOnline()) {
							target.teleport(lastLoc); //teleport the player to the last location
						}
					} catch (IllegalArgumentException e) {} //for invalid UUID, ignore
				}
			}
			// Update the last teleport location to the destination of this teleport.
			enderman.setMetadata("lastTeleport", new FixedMetadataValue(Swapcore.getInstance(), event.getTo().clone()));
		}
	}


	public int getSwapSkelChance() {
		return SwapSkelChance;
  	}
  	public void setSwapSkelChance(int SwapSkelChance) {
  		this.SwapSkelChance = SwapSkelChance;
 	}

	public int getSwapZombChance() {
		return SwapZombChance;
  	}
	public void setSwapZombChance(int SwapZombChance) {
  		this.SwapZombChance = SwapZombChance;
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



  	public int getSwapSkelPunch() {
      	return SwapSkelPunch;
  	}
  	public void setSwapSkelPunch(int SwapSkelPunch) {
    	this.SwapSkelPunch = SwapSkelPunch;
  	}
	public double getSwapZombSpeed() {
		return SwapZombSpeed;
  	}
  	public void setSwapZombSpeed(double SwapZombSpeed) {
  		this.SwapZombSpeed = SwapZombSpeed;
  	}
}
