package bristopher.swapcore;

import bristopher.swapcore.listeners.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

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
        Spiders spiders = new Spiders();
        Skeletons skeletons = new Skeletons();
        Zombies zombies = new Zombies();
        Endermen endermen = new Endermen();
        Pigmen pigmen = new Pigmen();
        Ghasts ghasts = new Ghasts();
        Creepers creepers = new Creepers();
        Blazes blazes = new Blazes();
        WitherSkeletons witherSkeletons = new WitherSkeletons();
        Withers withers = new Withers();
        Dragons dragons = new Dragons();

        SwapCommand swapCommand = new SwapCommand();
        registerCommand("swap", swapCommand);
   
        SwapEditCommand swapEditCommand = new SwapEditCommand(spiders, skeletons, zombies, endermen, 
        pigmen, ghasts, creepers, blazes, witherSkeletons, withers, dragons);
        registerCommand("SwapEdit", swapEditCommand);
        this.getCommand("SwapEdit").setTabCompleter(swapEditCommand);

        registerEvent(skeletons, zombies, endermen, pigmen, spiders, ghasts, creepers, blazes, witherSkeletons, withers, dragons);  // Add dragons here
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvent(Skeletons skeletons, Zombies zombies, Endermen endermen, 
    Pigmen pigmen, Spiders spiders, Ghasts ghasts, Creepers creepers, Blazes blazes, WitherSkeletons witherSkeletons,
    Withers withers, Dragons dragons) {
        this.getServer().getPluginManager().registerEvents(skeletons, this);
        this.getServer().getPluginManager().registerEvents(zombies, this);
        this.getServer().getPluginManager().registerEvents(endermen, this);
        this.getServer().getPluginManager().registerEvents(pigmen, this);
        this.getServer().getPluginManager().registerEvents(spiders, this);
        this.getServer().getPluginManager().registerEvents(ghasts, this);
        this.getServer().getPluginManager().registerEvents(creepers, this);
        this.getServer().getPluginManager().registerEvents(blazes, this);
        this.getServer().getPluginManager().registerEvents(witherSkeletons, this);
        this.getServer().getPluginManager().registerEvents(withers, this);
        this.getServer().getPluginManager().registerEvents(dragons, this);
    }

    private void registerCommand(String command, CommandExecutor executor) {
        this.getServer().getPluginCommand(command).setExecutor(executor);
    }

}