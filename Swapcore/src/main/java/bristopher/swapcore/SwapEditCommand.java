package bristopher.swapcore;

import java.util.ArrayList;
import java.util.List;

import bristopher.swapcore.listeners.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class SwapEditCommand implements CommandExecutor, TabCompleter  {

    private final Spiders spiders;
    private final Skeletons skeletons;
    private final Zombies zombies;
    private final Endermen endermen;
    private final Pigmen pigmen;
    private final Ghasts ghasts;
    private final Creepers creepers;
    private final Blazes blazes;
    private final Withers withers;
    private final Dragons dragons;
    private final WitherSkeletons witherSkeletons;

    public SwapEditCommand(Spiders spiders, Skeletons skeletons, Zombies zombies, Endermen endermen, Pigmen pigmen, Ghasts ghasts, Creepers creepers, Blazes blazes, WitherSkeletons witherSkeletons, Withers withers, Dragons dragons) {
        this.spiders = spiders;
        this.skeletons = skeletons;
        this.zombies = zombies;
        this.endermen = endermen;
        this.pigmen = pigmen;
        this.ghasts = ghasts;
        this.creepers = creepers;
        this.blazes = blazes;
        this.witherSkeletons = witherSkeletons;
        this.withers = withers;
        this.dragons = dragons;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            String variable = args[0].toLowerCase();
            String inputValue = args[1];

            // Check if the variable requires boolean handling
            if (variable.equals("witherswapchance") || variable.equals("dragonswapchance")) {
                boolean valid = Boolean.parseBoolean(inputValue);
                switch (variable) {
                    case "witherswapchance":
                        withers.setSwapWitherChance(valid);
                        sender.sendMessage("Swap-Wither spawning set to: " + valid);
                        break;
                    case "dragonswapchance":
                        dragons.setSwapDragonChance(valid);
                        sender.sendMessage("Swap-Dragon spawning set to: " + valid);
                        break;
                }
                return true;
            }

            // Handle integer variables
            try {
                int value = Integer.parseInt(inputValue);
                switch (variable) {
                    case "endermanswapchance":
                        endermen.setSwapEndermanChanceChance(value);
                        sender.sendMessage("SwapEndermanChance set to " + value);
                        break;
                    case "spiderswapchance":
                        spiders.setSwapSpiderChance(value);
                        sender.sendMessage("SwapSpiderChance set to " + value);
                        break;
                    case "skeletonswapchance":
                        skeletons.setSwapSkelChance(value);
                        sender.sendMessage("SwapSkelChance set to " + value);
                        break;
                    case "skeletonswappunch":
                        skeletons.setSwapSkelPunch(value);
                        sender.sendMessage("SwapSkelPunch set to " + value);
                        break;
                    case "zombieswapchance":
                        zombies.setSwapZombChance(value);
                        sender.sendMessage("SwapZombChance set to " + value);
                        break;
                    case "zombiespeed":
                        zombies.setSwapZombSpeed(value);
                        sender.sendMessage("SwapZombSpeed set to " + value);
                        break;
                    case "pigmanswapchance":
                        pigmen.setSwapPigmanChance(value);
                        sender.sendMessage("PigmanSwapChance set to " + value);
                        break;
                    case "ghastswapchance":
                        ghasts.setSwapGhastChance(value);
                        sender.sendMessage("GhastSwapChance set to " + value);
                        break;
                    case "creeperswapchance":
                        creepers.setSwapCreeperChance(value);
                        sender.sendMessage("CreeperSwapChance set to " + value);
                        break;
                    case "blazeswapchance":
                        blazes.setSwapBlazeChance(value);
                        sender.sendMessage("BlazeSwapChance set to " + value);
                        break;
                    case "witherskeletonchance":
                        witherSkeletons.setSwapWitherSkeletonChance(value);
                        sender.sendMessage("WitherSkeletonSwapChance set to " + value);
                        break;
                    default:
                        sender.sendMessage("Invalid variable");
                        return false;
                }
                return true;
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid number format. This variable requires an integer value.");
                return false;
            }
        } else {
            sender.sendMessage("Usage: /SwapEdit <variable> <value>");
            return false;
        }
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            if ("skeletonswapchance".startsWith(partial)) {
                completions.add("skeletonswapchance");
            }
            if ("skeletonswappunch".startsWith(partial)) {
                completions.add("skeletonswappunch");
            }
            if ("zombieswapchance".startsWith(partial)) {
                completions.add("zombieswapchance");
            }
            if ("zombieswapspeed".startsWith(partial)) {
                completions.add("zombieswapspeed");
            }
            if ("pigmanswapchance".startsWith(partial)) {
                completions.add("pigmanSwapChance");
            }
            if ("dragonswapchance".startsWith(partial)) {
                completions.add("dragonswapchance");
            }
            if ("endermanswapchance".startsWith(partial)) {
                completions.add("endermanswapchance");
            }
            if ("spiderswapchance".startsWith(partial)) {
                completions.add("spiderswapchance");
            }
            if ("ghastswapchance".startsWith(partial)) {
                completions.add("ghastswapchance");
            }
            if ("creeperswapchance".startsWith(partial)) {
                completions.add("creeperswapchance");
            }
            if ("blazeswapchance".startsWith(partial)) {
                completions.add("blazeswapchance");
            }
            if ("witherswapchance".startsWith(partial)) {
                completions.add("witherswapchance");
            }
            if ("witherskeletonchance".startsWith(partial)) {
                completions.add("witherskeletonchance");
            }

        }
        // You could also add predictions for the second argument if desired (e.g., common values)
        return completions;
    }
}
