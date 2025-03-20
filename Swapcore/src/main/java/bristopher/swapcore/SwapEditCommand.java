package bristopher.swapcore;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import bristopher.swapcore.listeners.MobListener;

public class SwapEditCommand implements CommandExecutor, TabCompleter  {

    private final MobListener mobListener;

    public SwapEditCommand(MobListener mobListener) {
        this.mobListener = mobListener;
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
                        case "skeletonchance":
                            mobListener.setSwapSkelChance(value);
                            sender.sendMessage("SwapSkelChance set to " + value);
                            break;
                        case "skeletonpunch":
                            mobListener.setSwapSkelPunch(value);
                            sender.sendMessage("SwapSkelPunch set to " + value);
                            break;
                        case "zombiechance":
                            mobListener.setSwapZombChance(value);
                            sender.sendMessage("SwapZombChance set to " + value);
                            break;
                        case "zombiespeed":
                            mobListener.setSwapZombSpeed(value);
                            sender.sendMessage("SwapZombSpeed set to " + value);
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
        }
        // You could also add predictions for the second argument if desired (e.g., common values)
        return completions;
    }
}
