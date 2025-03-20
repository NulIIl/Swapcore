package bristopher.swapcore;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import bristopher.swapcore.listeners.MobListener;

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
        MobListener mobListener = new MobListener();

        SwapCommand swapCommand = new SwapCommand();
        registerCommand("swap", swapCommand);
       
        SwapEditCommand swapEditCommand = new SwapEditCommand(mobListener);
        registerCommand("SwapEdit", swapEditCommand);
        this.getCommand("SwapEdit").setTabCompleter(swapEditCommand);

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
