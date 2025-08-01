package bristopher.swapcore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Game {

    BukkitTask task;
    List<UUID> players = new LinkedList<>();
    List<Integer> offline = new LinkedList<Integer>();
    private boolean gameRunning = false;
    long timer = 100;

    public void setTimer(long time){
        timer = time;

    }

    public boolean startUpGame(long time) {
        timer = time;
        gameRunning = true;
        if (!addValidPlayers())
            return false;
        else {
            task = new SwapTask(players).runTaskTimer(Swapcore.getInstance(), 20 * timer, 20 * timer);
            return true;
        }
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void closeDownGame() {
        gameRunning = false;
        task.cancel();
        //cleanup();
    }

    public boolean addValidPlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode() != GameMode.SURVIVAL)
                continue;
            players.add(p.getUniqueId());
        }
        Bukkit.getServer().broadcast("num of players: " + players.size(), "bukkit.broadcast");
        if (players.size() < 1) { //set to 1 so null can test solo, probably will break everything
            players.clear();
            return false;
        }
        return true;
    }

    public void addPlayer(Player p) {
        players.add(p.getUniqueId());
    }

    public void removePlayer(Player p) {
        players.remove(p.getUniqueId());
    }

    public void swapPlayerWithRandomPlayer(Player p) {
        for (UUID id : players) {
            if (Bukkit.getPlayer(id) == null)
                offline.add(players.indexOf(id));
            continue;
        }
        if (Bukkit.getOnlinePlayers().size() < 2 || players.size() - offline.size() < 2) {
            Bukkit.getLogger().info(ChatColor.RED + "Error. Not enough players online.");
            return;
        }
        int rand1 = (int) (Math.random() * players.size());
        int rand2 = (int) (Math.random() * players.size());
        while (rand1 == rand2 || offline.contains(rand2)) {
            rand2 = (int) (Math.random() * players.size());
        }

        Player p1 = Bukkit.getPlayer(players.get(rand1));
        Player p2 = Bukkit.getPlayer(players.get(rand2));
        Location p1Loc = p1.getLocation();
        Location p2Loc = p2.getLocation();
        p1.teleport(p2Loc);
        p2.teleport(p1Loc);
        offline.clear();
    }

    public static void massSwap() {
        //create a list of players in survival mode
        List<Player> survivalPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getGameMode() == GameMode.SURVIVAL)
                .collect(Collectors.toList());
        Collections.shuffle(survivalPlayers); //shuffle the list

        //confirms there are at least 2 players
        if (survivalPlayers.size() < 2) return;

        //store first player location for wrap-around swap
        Location firstPlayerLoc = survivalPlayers.get(0).getLocation();

        //swap each player with the next one
        for (int i = 0; i < survivalPlayers.size(); i++) {
            Player currentPlayer = survivalPlayers.get(i);

            //for the wrap around swap (last player goes to first players location)
            if (i == survivalPlayers.size() - 1) {
                currentPlayer.teleport(firstPlayerLoc);
            } else {
                //swap with the next player in the list
                Player nextPlayer = survivalPlayers.get(i + 1);
                currentPlayer.teleport(nextPlayer.getLocation());
            }
        }
    }




}
