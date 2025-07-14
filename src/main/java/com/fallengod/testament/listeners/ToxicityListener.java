package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerKickEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ToxicityListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final Map<UUID, Long> lastChatTime;
    private final Map<UUID, Integer> spamCount;
    private final List<String> toxicWords;
    
    public ToxicityListener(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.lastChatTime = new HashMap<>();
        this.spamCount = new HashMap<>();
        
        // Common toxic words/phrases to detect
        this.toxicWords = Arrays.asList(
            "noob", "trash", "garbage", "ez", "easy", "bad", "suck", "terrible",
            "idiot", "stupid", "dumb", "loser", "scrub", "git gud", "skill issue",
            "kys", "kill yourself", "uninstall", "delete game", "quit"
        );
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.getTitleManager().addDeath(player.getUniqueId());
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().toLowerCase();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Check for spam
        Long lastChat = lastChatTime.get(playerId);
        if (lastChat != null && (currentTime - lastChat) < 1000) { // Less than 1 second
            int spam = spamCount.getOrDefault(playerId, 0) + 1;
            spamCount.put(playerId, spam);
            
            if (spam >= 5) {
                plugin.getTitleManager().addToxicityPoint(playerId, "Spamming chat");
                spamCount.put(playerId, 0); // Reset spam count
            }
        } else {
            spamCount.put(playerId, 0); // Reset spam count if not spamming
        }
        
        lastChatTime.put(playerId, currentTime);
        
        // Check for toxic language
        for (String toxicWord : toxicWords) {
            if (message.contains(toxicWord)) {
                plugin.getTitleManager().addToxicityPoint(playerId, "Toxic language: " + toxicWord);
                break; // Only count one toxic word per message
            }
        }
        
        // Check for excessive caps
        if (message.length() > 10) {
            long capsCount = message.chars().filter(Character::isUpperCase).count();
            double capsRatio = (double) capsCount / message.length();
            
            if (capsRatio > 0.7) { // More than 70% caps
                plugin.getTitleManager().addToxicityPoint(playerId, "Excessive caps");
            }
        }
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        
        // Check if player rage quit (quit within 30 seconds of dying)
        // This would require tracking death times, but for now we'll just clean up
        lastChatTime.remove(playerId);
        spamCount.remove(playerId);
    }
    
    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        String reason = event.getReason().toLowerCase();
        
        // If kicked for toxic behavior, add toxicity points
        if (reason.contains("toxic") || reason.contains("spam") || reason.contains("grief")) {
            plugin.getTitleManager().addToxicityPoint(player.getUniqueId(), "Kicked for: " + reason);
        }
    }
}