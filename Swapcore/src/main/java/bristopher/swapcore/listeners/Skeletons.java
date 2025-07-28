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
import org.bukkit.util.Vector;
import java.util.Random;



public class Skeletons implements Listener {

		//Spawn Chance Varaible
		private int SwapSkelChance = 100; //swap skeleton spawn chance, out of 100
		//Equipment Base Variable
		private int SwapSkelPunch = 2; //swap skeleton punch level, out of 5

	@EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
		if (entity instanceof Skeleton || entity instanceof Stray || entity instanceof Bogged) {
			if (random.nextInt(1, 100) < SwapSkelChance) {
				swapSkeleton(entity);
			}
		}
	}

	public void swapSkeleton(LivingEntity entity) {
		(entity).setMetadata("swapSkeleton", new FixedMetadataValue(Swapcore.getInstance(), true)); //creates metadata
		//creates knockback bow
		ItemStack swapbow = new ItemStack(Material.BOW);
		swapbow.addUnsafeEnchantment(Enchantment.PUNCH, SwapSkelPunch);
		entity.getEquipment().setItemInMainHand(swapbow);
		entity.getEquipment().setItemInMainHandDropChance(0.04F);


		//makes shiny purple helmet
		ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta meta = (LeatherArmorMeta) leatherHelmet.getItemMeta();
		meta.setColor(Color.PURPLE);
		meta.addEnchant(Enchantment.MENDING, 1, true);
		leatherHelmet.setItemMeta(meta);
		entity.getEquipment().setHelmet(leatherHelmet);
		entity.getEquipment().setHelmetDropChance(0.04F);
    }

	@EventHandler
	public void onSkeletonShot(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Arrow arrow) {//if arrow
			if (arrow.getShooter() instanceof Entity skeleton && skeleton.hasMetadata("swapSkeleton")) {//if swap skeleton
				Entity damaged = event.getEntity();
				if (damaged instanceof LivingEntity target) {
					if (event.getFinalDamage() <= 0) {//if player doesn't take damage, cancel event
						return;
					}
					//store skeleton and player locations
					Location skelLoc = skeleton.getLocation();
					Location targetLoc = target.getLocation();

					Vector knockbackVelocity = arrow.getVelocity().normalize().multiply(SwapSkelPunch);
					skeleton.teleport(targetLoc);
					target.teleport(skelLoc);
					Bukkit.getScheduler().runTask(Swapcore.getInstance(), () -> {target.setVelocity(knockbackVelocity);});
						//swap player and skeleton and keep momentum
				}
			}
		}
	}




  	public void setSwapSkelChance(int SwapSkelChance) {
  		this.SwapSkelChance = SwapSkelChance;
 	}

  	public void setSwapSkelPunch(int SwapSkelPunch) {
    	this.SwapSkelPunch = SwapSkelPunch;
  	}
}
