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

public int pigmanSwapChance = 35;  //pigman swap percent chance

@EventHandler
public void spawnListener(CreatureSpawnEvent e) {
	LivingEntity entity = e.getEntity();
	Random random = new Random();
	if (entity instanceof PigZombie) {
		if (random.nextInt(1, 100) < pigmanSwapChance) {
			swapPigman((PigZombie) entity);
		}
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

	public void setSwapPigmanChance(int pigmanSwapChance) {
  		this.pigmanSwapChance = pigmanSwapChance;
	}
}