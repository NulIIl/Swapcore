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

    public SwapEditCommand(Spiders spiders, Skeletons skeletons, Zombies zombies, Endermen endermen, Pigmen pigmen, Ghasts ghasts, Creepers creepers, Blazes blazes, Withers withers, Dragons dragons) {
        this.spiders = spiders;
        this.skeletons = skeletons;
        this.zombies = zombies;
        this.endermen = endermen;
        this.pigmen = pigmen;
        this.ghasts = ghasts;
        this.creepers = creepers;
        this.blazes = blazes;
        this.withers = withers;
        this.dragons = dragons;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (args.length == 2) {
                try {
                    String variable = args[0];
                      //gets the variable to edit
                    int value = Integer.parseInt(args[1]);
                      //gets the value to set it to

                    switch (variable.toLowerCase()) {
                        case "endermanchance":
                            endermen.setSwapEndermanChanceChance(value);
                            sender.sendMessage("SwapEndermanChance set to " + value);
                            break;
                        case "spiderchance":
                            spiders.setSwapSpiderChance(value);
                            sender.sendMessage("SwapSpiderChance set to " + value);
                            break;
                        case "skeletonchance":
                            skeletons.setSwapSkelChance(value);
                            sender.sendMessage("SwapSkelChance set to " + value);
                            break;
                        case "skeletonpunch":
                            skeletons.setSwapSkelPunch(value);
                            sender.sendMessage("SwapSkelPunch set to " + value);
                            break;
                        case "zombiechance":
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
                        case "witherswapchance":
                            withers.setSwapWitherChance(value);
                            sender.sendMessage("WitherSwapChance set to " + value);
                        case "dragonswapchance":
                            dragons.setSwapDragonChance(value);
                            sender.sendMessage("SwapDragonChance set to " + value);
                            break;


                        default:
                            sender.sendMessage("Invalid variable");
                            return false;
                    }
                    return true;
                } catch (NumberFormatException e) {
                    sender.sendMessage("Invalid number format.");
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
            if ("skeletonchance".startsWith(partial)) {
                completions.add("skeletonchance");
            }
            if ("skeletonpunch".startsWith(partial)) {
                completions.add("skeletonpunch");
            }
            if ("zombiechance".startsWith(partial)) {
                completions.add("zombiechance");
            }
            if ("zombiespeed".startsWith(partial)) {
                completions.add("zombiespeed");
            }
            if ("pigmanSwapChance".startsWith(partial)) {
                completions.add("pigmanSwapChance");
            }
            if ("dragonchance".startsWith(partial)) {
                completions.add("dragonchance");
            }

        }
        // You could also add predictions for the second argument if desired (e.g., common values)
        return completions;
    }
}
