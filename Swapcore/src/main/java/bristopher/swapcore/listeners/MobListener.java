package bristopher.swapcore.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import bristopher.swapcore.Swapcore;

public class MobListener implements Listener {
    							
		//Spawn Chance Varaibles
		private int SwapSkelChance = 100; //swap skeleton spawn chance, out of 100
		private int SwapZombChance = 50; //swap skeleton spawn chance, out of 100

		//Equipment Base Variables
		private int SwapSkelPunch = 3; //swap skeleton punch level, out of 5
		private int SwapZombSpeed = 3; //swap skeleton punch level, out of 5

	@EventHandler
    public void spawnListener(CreatureSpawnEvent e) {				
        LivingEntity entity = e.getEntity();
        Random random = new Random();
		if (entity instanceof Skeleton) {
			if (random.nextInt(1, 100) < SwapSkelChance) {
				SkeletonSwap(entity);
			}
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
		swapbow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, SwapSkelPunch);
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
                if (zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
                    double baseSpeed = zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
                    zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(baseSpeed * SwapZombSpeed);
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
                
                // Remove the marker so the effect only triggers once
                zombie.removeMetadata("swapZombie", Swapcore.getInstance());
            }
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



  	public int getSwapSkelPunch() {
      	return SwapSkelPunch;
  	}
  	public void setSwapSkelPunch(int SwapSkelPunch) {
    	this.SwapSkelPunch = SwapSkelPunch;
  	}
	public int getSwapZombSpeed() {
		return SwapZombSpeed;
  	}
  	public void setSwapZombSpeed(int SwapZombSpeed) {
  		this.SwapZombSpeed = SwapZombSpeed;
  	}
}
