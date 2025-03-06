package bristopher.swapcore.commands;

import bristopher.swapcore.listeners.MobListener;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SwapEditCommand implements CommandExecutor {

    private final MobListener mobListener;

    public SwapEditCommand(MobListener mobListener) {
        this.mobListener = mobListener;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2) {
                try {
                    String variable = args[0];
                      //gets the variable to edit
                    int value = Integer.parseInt(args[1]);
                      //gets the value to set it to

                    switch (variable.toLowerCase()) {
                        case "skeletonchance":
                            mobListener.setSwapSkelChance(value);
                            player.sendMessage("SwapSkelChance set to " + value);
                            break;
                        case "skeletonpunch":
                            mobListener.setSwapSkelPunch(value);
                            player.sendMessage("SwapSkelPunch set to " + value);
                            break;
                        default:
                            player.sendMessage("Invalid variable");
                            return false;
                    }
                    return true;
                } catch (NumberFormatException e) {
                    player.sendMessage("Invalid number format.");
                    return false;
                }
            } else {
                player.sendMessage("Usage: /SwapEdit <variable> <value>");
                return false;
            }
        } else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }
    }
}
