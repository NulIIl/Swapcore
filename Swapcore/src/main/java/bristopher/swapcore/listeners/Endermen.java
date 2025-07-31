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


public class Endermen implements Listener {

		public int SwapEndermanChance = 100; //enderman swap percent chance


	@EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();

		if (entity instanceof Enderman) {
			if (random.nextInt(1, 100) < SwapEndermanChance) {
				swapEnderman(entity);
			}
		}
	}

	public void swapEnderman(LivingEntity entity) {
		((Enderman) entity).setMetadata("swapEnderman", new FixedMetadataValue(Swapcore.getInstance(), true)); //sets a tag to identify the enderman as a swap enderman
	}


	@EventHandler
	public void onEndermanTarget(EntityTargetEvent event) { //when an entity targets something
		if (event.getEntity() instanceof Enderman enderman && enderman.hasMetadata("swapEnderman")) { //if the entity is a swap enderman
			if (event.getTarget() instanceof Player player) { //and if the target is a player
				// gives the enderman the swapEnderman metadata and stores the player's UUID in that metadata
				enderman.setMetadata("swapEnderman", new FixedMetadataValue(Swapcore.getInstance(), player.getUniqueId().toString()));
				// if not already set, give the enderman the lastTeleport metadata and stores the coordinates of where that enderman was standing
				if (!enderman.hasMetadata("lastTeleport")) {
					enderman.setMetadata("lastTeleport", new FixedMetadataValue(Swapcore.getInstance(), enderman.getLocation().clone()));
				}

				// makes a scheduler that runs the code after 30 ticks
				Bukkit.getScheduler().runTaskLater(Swapcore.getInstance(), () -> {
					if (!enderman.isDead()) {
						// schedule teleports with small delays between them
						for (int i = 0; i < 8; i++) {
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


	public void setSwapEndermanChanceChance(int SwapEndermanChance) {
		this.SwapEndermanChance = SwapEndermanChance;
	}

}
