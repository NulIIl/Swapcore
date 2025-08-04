package bristopher.swapcore.listeners;

import bristopher.swapcore.Swapcore;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Random;

public class WitherSkeletons implements Listener {

    private int swapWitherSkeletonChance = 50; //swap wither skel spawn percent chance

    @EventHandler
    public void spawnListener(CreatureSpawnEvent e) {
        LivingEntity entity = e.getEntity();
        Random random = new Random();
        if (entity instanceof WitherSkeleton) {
            if (random.nextInt(1, 100) < swapWitherSkeletonChance) {
                swapWitherSkeleton(entity);
            }
        }
    }

    public void swapWitherSkeleton(LivingEntity entity) {
        entity.setMetadata("swapWitherSkeleton", new FixedMetadataValue(Swapcore.getInstance(), true));

        //makes shiny purple helmet
        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) leatherHelmet.getItemMeta();
        meta.setColor(Color.PURPLE);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        leatherHelmet.setItemMeta(meta);
        entity.getEquipment().setHelmet(leatherHelmet);
        entity.getEquipment().setHelmetDropChance(0.02F);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof WitherSkeleton witherSkeleton && witherSkeleton.hasMetadata("swapWitherSkeleton")) {
            Location witherSkeletonLoc = witherSkeleton.getLocation();
            LivingEntity damager = null;
            Location damagerLoc; //declare outside if statement so we can do the swap outside
            if (event.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof LivingEntity shooter){
                damager = shooter; //the damager is whoever shot the arrow
                 damagerLoc = damager.getLocation(); //get the location of the arrow shooter
            } else { // otherwise just get whatever damaged the witherskel
                damager = (LivingEntity) event.getDamager();
                 damagerLoc = damager.getLocation();
            }
            //swap damager and witherskel
            witherSkeleton.teleport(damagerLoc);
            damager.teleport(witherSkeletonLoc);
        }
    }


    public void setSwapWitherSkeletonChance(int SwapWitherSkeletonChance) {
        this.swapWitherSkeletonChance = SwapWitherSkeletonChance;
    }
}