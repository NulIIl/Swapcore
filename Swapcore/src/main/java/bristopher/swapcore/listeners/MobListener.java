package bristopher.swapcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import java.net.http.WebSocket;
import java.util.Random;

import bristopher.swapcore.Swapcore;

public class MobListener implements Listener {
    							
		//Spawn Chance Varaibles
		private int SwapSkelChance = 100; //swap skeleton spawn chance, out of 100

		//Equipment Base Variables
		private int SwapSkelPunch = 3; //swap skeleton punch level, out of 5

	@EventHandler
    public void spawnListener(CreatureSpawnEvent e) {				
        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof Skeleton) {
            if (random.nextInt(1, 100) < SwapSkelChance) {
							
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
        }
    }
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) 
	{
		if (event.getDamager() instanceof Arrow arrow) {//if arrow           
			if (arrow.getShooter() instanceof Skeleton skeleton) {//if skeleton     
				ItemStack inHand = skeleton.getEquipment().getItemInMainHand();
				if (inHand != null && inHand.getType() == Material.BOW && inHand.containsEnchantment(Enchantment.ARROW_KNOCKBACK)) {//if holding swap bow                  
						Entity damaged = event.getEntity();
					if (damaged instanceof LivingEntity target) {//if player
						Location skelLoc = skeleton.getLocation();
						Location targetLoc = target.getLocation();
							//store skeleton and player
					
						Vector knockbackVelocity = arrow.getVelocity().normalize().multiply(inHand.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK));
						skeleton.teleport(targetLoc);
						target.teleport(skelLoc);
						Bukkit.getScheduler().runTask(Swapcore.getInstance(), () -> {target.setVelocity(knockbackVelocity);});
							//swap player and skeleton and keep momentum			
					}
				}
			}
		}
	}
	

	public int getSwapSkelChance() {
		return SwapSkelChance;
  	}
  	public void setSwapSkelChance(int SwapSkelChance) {
  		this.SwapSkelChance = SwapSkelChance;
 	}
	
  	public int getSwapSkelPunch() {
      	return SwapSkelPunch;
  	}
  	public void setSwapSkelPunch(int SwapSkelPunch) {
    	this.SwapSkelPunch = SwapSkelPunch;
  	}
}
