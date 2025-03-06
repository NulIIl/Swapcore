package bristopher.swapcore;

import bristopher.swapcore.commands.SwapEditCommand;
import bristopher.swapcore.commands.SwapCommand;
import bristopher.swapcore.listeners.MobListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public final class Swapcore extends JavaPlugin {
    private static Swapcore instance;
    private static GameManager gameManager;

    public static Swapcore getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;
        gameManager = new GameManager();
        registerCommand("swap", new SwapCommand());
        MobListener mobListener = new MobListener();
        registerCommand("SwapEdit", new SwapEditCommand(mobListener));
        registerEvent(mobListener);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvent(MobListener event) {
        this.getServer().getPluginManager().registerEvents(event, this);
    }

    private void registerCommand(String command, CommandExecutor executor) {
        this.getServer().getPluginCommand(command).setExecutor(executor);
    }

}
