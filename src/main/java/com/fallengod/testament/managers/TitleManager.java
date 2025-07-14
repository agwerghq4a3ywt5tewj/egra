package com.fallengod.testament.managers;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.enums.PlayerTitle;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TitleManager {
    private final TestamentPlugin plugin;
    private final Map<UUID, PlayerTitle> playerTitles;
    private final Map<UUID, Integer> toxicityScore;
    private final Map<UUID, Integer> deathCount;
    private final Map<UUID, Long> lastToxicAction;
    
    public TitleManager(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.playerTitles = new HashMap<>();
        this.toxicityScore = new HashMap<>();
        this.deathCount = new HashMap<>();
        this.lastToxicAction = new HashMap<>();
    }
    
    public PlayerTitle getPlayerTitle(UUID playerId) {
        return playerTitles.getOrDefault(playerId, PlayerTitle.NONE);
    }
    
    public void setPlayerTitle(UUID playerId, PlayerTitle title) {
        playerTitles.put(playerId, title);
        
        Player player = plugin.getServer().getPlayer(playerId);
        if (player != null) {
            updatePlayerDisplayName(player, title);
            
            if (title == PlayerTitle.FALLEN) {
                plugin.getServer().broadcastMessage("§4⚠ " + player.getName() + " has been marked as §4§lFALLEN§r§4! ⚠");
                plugin.getServer().broadcastMessage("§7Their toxic behavior and failures have corrupted their soul...");
            }
        }
    }
    
    public void addToxicityPoint(UUID playerId, String reason) {
        int currentScore = toxicityScore.getOrDefault(playerId, 0);
        toxicityScore.put(playerId, currentScore + 1);
        lastToxicAction.put(playerId, System.currentTimeMillis());
        
        Player player = plugin.getServer().getPlayer(playerId);
        if (player != null) {
            player.sendMessage("§c⚠ Toxic behavior detected: " + reason);
            player.sendMessage("§7Toxicity score: " + (currentScore + 1) + "/10");
            
            // Check if player should become Fallen
            checkForFallenStatus(playerId);
        }
    }
    
    public void addDeath(UUID playerId) {
        int currentDeaths = deathCount.getOrDefault(playerId, 0);
        deathCount.put(playerId, currentDeaths + 1);
        
        // Check if player should become Fallen
        checkForFallenStatus(playerId);
    }
    
    private void checkForFallenStatus(UUID playerId) {
        int toxicity = toxicityScore.getOrDefault(playerId, 0);
        int deaths = deathCount.getOrDefault(playerId, 0);
        
        // Become Fallen if:
        // - 10+ toxicity points, OR
        // - 50+ deaths, OR
        // - 5+ toxicity points AND 25+ deaths
        if (toxicity >= 10 || deaths >= 50 || (toxicity >= 5 && deaths >= 25)) {
            PlayerTitle currentTitle = getPlayerTitle(playerId);
            if (currentTitle != PlayerTitle.FALLEN) {
                setPlayerTitle(playerId, PlayerTitle.FALLEN);
            }
        }
    }
    
    public void resetToxicity(UUID playerId) {
        toxicityScore.put(playerId, 0);
        deathCount.put(playerId, 0);
        
        Player player = plugin.getServer().getPlayer(playerId);
        if (player != null) {
            player.sendMessage("§aYour toxicity and death counts have been reset.");
        }
    }
    
    public void updatePlayerDisplayName(Player player, PlayerTitle title) {
        Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getMainScoreboard();
        
        // Remove player from any existing title teams
        for (Team team : scoreboard.getTeams()) {
            if (team.getName().startsWith("title_")) {
                team.removeEntry(player.getName());
            }
        }
        
        if (title != PlayerTitle.NONE) {
            // Create or get team for this title
            String teamName = "title_" + title.name().toLowerCase();
            Team team = scoreboard.getTeam(teamName);
            
            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
                team.setColor(title.getColor());
                team.setPrefix(title.getPrefix());
            }
            
            // Add player to the team
            team.addEntry(player.getName());
        }
        
        // Set the scoreboard for the player
        player.setScoreboard(scoreboard);
    }
    
    public int getToxicityScore(UUID playerId) {
        return toxicityScore.getOrDefault(playerId, 0);
    }
    
    public int getDeathCount(UUID playerId) {
        return deathCount.getOrDefault(playerId, 0);
    }
}