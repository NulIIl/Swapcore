package bristopher.swapcore.listeners;

import bristopher.swapcore.Swapcore;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
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


public class Zombies implements Listener {

		private int SwapZombChance = 50; //swap zombie spawn percent chance
		private double SwapZombSpeed = 2.0; //swap zombie speed


	@EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();

		if (entity instanceof Zombie) {
			if (random.nextInt(1, 100) < SwapZombChance) {
				swapZombie(entity);
			}
		}
	}

	public void swapZombie(LivingEntity entity) {
		((Zombie) entity).setMetadata("swapZombie", new FixedMetadataValue(Swapcore.getInstance(), true)); //sets a tag to identify the zombie as a swap zombie
	}




	@EventHandler
	public void onSwapZombieHit(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Zombie zombie && zombie.hasMetadata("swapZombie")) {
            if (event.getEntity() instanceof Player hitPlayer) {
				if (event.getFinalDamage() <= 0) {//if player doesn't take damage, cancel event
					return;
				}
				zombie.removeMetadata("swapZombie", Swapcore.getInstance()); //remove metadata so they won't keep teleporting players
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
                    double baseSpeed = zombie.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue();
                    zombie.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(baseSpeed * SwapZombSpeed);


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
	}

	public int getSwapZombChance() {
		return SwapZombChance;
  	}
	public void setSwapZombChance(int SwapZombChance) {
  		this.SwapZombChance = SwapZombChance;
	}
	public double getSwapZombSpeed() {
		return SwapZombSpeed;
  	}
  	public void setSwapZombSpeed(double SwapZombSpeed) {
  		this.SwapZombSpeed = SwapZombSpeed;
  	}
}
