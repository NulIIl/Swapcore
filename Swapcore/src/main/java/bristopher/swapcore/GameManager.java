package bristopher.swapcore;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class GameManager {

    private static Game game;

    public static Game getRunningGame() {
        if (game != null)
            return game;
        else
            return null;
    }

    static long timer = 100;
    public static void setTimer(long time){
        timer = time;

    }
    public static void startGame(CommandSender sender) {
        if (game != null) {
            sender.sendMessage(Component.text("Error, game already in progress.").color(NamedTextColor.RED));
            return;
        }
        game = new Game();
        if(game.startUpGame(timer))
            sender.sendMessage(Component.text("Swapcore starting!.").color(NamedTextColor.GREEN));
        else {
            sender.sendMessage(Component.text("Error. Not enough valid players to start game.").color(NamedTextColor.RED));
            game = null;
        }
    }

    public static void stopGame(CommandSender sender) {
        if (game == null) {
            sender.sendMessage(Component.text("Error, game has not yet been started.").color(NamedTextColor.RED));
            return;
        }
        game.closeDownGame();
        game = null;
        sender.sendMessage(Component.text("Swapcore ending!.").color(NamedTextColor.GREEN));
    }

    public static void stopGame() {
        if (game == null)
            return;

        game.closeDownGame();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(Component.text("Swapcore ending!.").color(NamedTextColor.GREEN));
        }
    }

    public static void addPlayer(Player p) {
        if (game == null)
            return;
        game.addPlayer(p);
    }

    public static void removePlayer(Player p) {
        if (game == null)
            return;
        game.removePlayer(p);
    }

    public static boolean isPlayerPartOfGame(Player p) {
        if (game == null)
            return false;
        if (game.players.contains(p.getUniqueId()))
            return true;
        return false;
    }

}
