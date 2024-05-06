package bristopher.swapcore.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.net.http.WebSocket;
import java.util.Random;

public class MobListener implements Listener {
    @EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof Skeleton) {
            if (random.nextInt(1, 100) < 75) {
                ItemStack swapbow = new ItemStack(Material.BOW);
                swapbow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 4);
                    entity.getEquipment().setItemInMainHand(swapbow);
                    entity.getEquipment().setItemInMainHandDropChance(0.04F);
                    
            }
        }

    }
}
