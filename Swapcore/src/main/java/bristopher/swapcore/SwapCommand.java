package bristopher.swapcore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class SwapCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("swap")) {
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                    case "start": {
                        GameManager.startGame(sender);
                        break;
                    }
                    case "stop": {
                        GameManager.stopGame(sender);
                        break;
                    }
                    case "list": {
                        //list logic
                        break;
                    }
                    case "addp": {
                        //manual add player to game,
                    }
                    case "time": {
                        if (args.length > 1) {
                            GameManager.setTimer(Long.parseLong(args[1]));
                        }
                    }
                    default:
                        sender.sendMessage(ChatColor.RED + "Invalid sub-command. Valid sub-commands: start, stop, list");
                        return false;
                }
                return true;
            }
            sender.sendMessage(ChatColor.RED + "Invalid sub-command. Valid sub-commands: start, stop, list");
            return false;
        }
        return false;
    }
}
