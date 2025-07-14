package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.items.SpecialItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartVeilListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final Map<UUID, Boolean> testModeUsers;
    private final Map<UUID, Boolean> heartEffectsActive;
    private final Map<UUID, Boolean> veilEffectsActive;
    
    public HeartVeilListener(TestamentPlugin plugin) {
        this.plugin = plugin;
        this.testModeUsers = new HashMap<>();
        this.heartEffectsActive = new HashMap<>();
        this.veilEffectsActive = new HashMap<>();
        
        // Start the effect checking task
        startEffectTask();
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Check effects when player joins
        checkAndApplyEffects(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Check effects periodically when player moves
        if (event.getPlayer().getTicksLived() % 20 == 0) { // Every second
            checkAndApplyEffects(event.getPlayer());
        }
    }
    
    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        // Check effects when player changes held item
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            checkAndApplyEffects(event.getPlayer());
        }, 1L);
    }
    
    private void startEffectTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    checkAndApplyEffects(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Every second
    }
    
    private void checkAndApplyEffects(Player player) {
        boolean hasHeart = hasHeartOfFallenGod(player);
        boolean hasVeil = hasVeilOfNullification(player);
        boolean veilNullifies = hasVeil && isVeilNullifying(player);
        
        UUID playerId = player.getUniqueId();
        boolean currentHeartActive = heartEffectsActive.getOrDefault(playerId, false);
        boolean currentVeilActive = veilEffectsActive.getOrDefault(playerId, false);
        
        // Determine new states
        boolean shouldHaveHeartEffects = hasHeart && !veilNullifies;
        boolean shouldHaveVeilEffects = hasVeil;
        
        // Apply Heart effects
        if (shouldHaveHeartEffects && !currentHeartActive) {
            applyHeartEffects(player);
            heartEffectsActive.put(playerId, true);
            
            if (testModeUsers.getOrDefault(playerId, false)) {
                player.sendMessage("§5§l❤ Heart of Fallen God effects activated!");
                player.sendActionBar("§5❤ Heart Active | §7+15 Hearts, Strength, Regen, Resistance");
            }
        } else if (!shouldHaveHeartEffects && currentHeartActive) {
            removeHeartEffects(player);
            heartEffectsActive.put(playerId, false);
            
            if (testModeUsers.getOrDefault(playerId, false)) {
                if (veilNullifies) {
                    player.sendMessage("§8§l🌫 Veil nullifies Heart effects!");
                    player.sendActionBar("§8🌫 Veil Nullifying | §7Heart effects suppressed");
                } else {
                    player.sendMessage("§7Heart effects removed (no longer holding Heart)");
                    player.sendActionBar("§7No active testament effects");
                }
            }
        }
        
        // Apply Veil effects
        if (shouldHaveVeilEffects && !currentVeilActive) {
            applyVeilEffects(player);
            veilEffectsActive.put(playerId, true);
            
            if (testModeUsers.getOrDefault(playerId, false)) {
                player.sendMessage("§8§l🌫 Veil of Nullification effects activated!");
                if (hasHeart) {
                    player.sendActionBar("§8🌫 Veil Active | §7Nullifying Heart + Veil effects");
                } else {
                    player.sendActionBar("§8🌫 Veil Active | §7Slow Fall, Night Vision, Speed");
                }
            }
        } else if (!shouldHaveVeilEffects && currentVeilActive) {
            removeVeilEffects(player);
            veilEffectsActive.put(playerId, false);
            
            if (testModeUsers.getOrDefault(playerId, false)) {
                player.sendMessage("§7Veil effects removed (no longer holding Veil)");
                if (hasHeart) {
                    player.sendActionBar("§5❤ Heart Active | §7+15 Hearts, Strength, Regen, Resistance");
                } else {
                    player.sendActionBar("§7No active testament effects");
                }
            }
        }
    }
    
    private boolean hasHeartOfFallenGod(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isHeartOfFallenGod(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasVeilOfNullification(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isVeilOfNullification(item)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isVeilNullifying(Player player) {
        if (!plugin.getConfigManager().isVeilEnabled()) {
            return false;
        }
        
        // Check if any nearby players have Heart effects that should be nullified
        double range = plugin.getConfigManager().getVeilRange();
        
        for (Player nearbyPlayer : player.getWorld().getPlayers()) {
            if (nearbyPlayer != player && 
                nearbyPlayer.getLocation().distance(player.getLocation()) <= range &&
                hasHeartOfFallenGod(nearbyPlayer)) {
                return true;
            }
        }
        
        // Also nullify own heart if player has both
        return hasHeartOfFallenGod(player);
    }
    
    private void applyHeartEffects(Player player) {
        if (!plugin.getConfigManager().isHeartEnabled()) {
            return;
        }
        
        // Set max health
        int extraHearts = plugin.getConfigManager().getHeartExtraHearts();
        player.setMaxHealth(20.0 + (extraHearts * 2.0));
        player.setHealth(player.getMaxHealth());
        
        // Apply potion effects
        int strengthLevel = plugin.getConfigManager().getHeartStrengthLevel();
        int regenLevel = plugin.getConfigManager().getHeartRegenerationLevel();
        int resistanceLevel = plugin.getConfigManager().getHeartResistanceLevel();
        
        player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, Integer.MAX_VALUE, strengthLevel - 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, regenLevel - 1, false, false));
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, Integer.MAX_VALUE, resistanceLevel - 1, false, false));
    }
    
    private void removeHeartEffects(Player player) {
        // Reset max health
        player.setMaxHealth(20.0);
        if (player.getHealth() > 20.0) {
            player.setHealth(20.0);
        }
        
        // Remove potion effects
        player.removePotionEffect(PotionEffectType.STRENGTH);
        player.removePotionEffect(PotionEffectType.REGENERATION);
        player.removePotionEffect(PotionEffectType.RESISTANCE);
    }
    
    private void applyVeilEffects(Player player) {
        if (!plugin.getConfigManager().isVeilEnabled()) {
            return;
        }
        
        // Apply veil effects
        if (plugin.getConfigManager().getVeilSlowFalling()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false));
        }
        
        if (plugin.getConfigManager().getVeilNightVision()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
        }
        
        int speedLevel = plugin.getConfigManager().getVeilSpeedLevel();
        if (speedLevel > 0) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, speedLevel - 1, false, false));
        }
    }
    
    private void removeVeilEffects(Player player) {
        player.removePotionEffect(PotionEffectType.SLOW_FALLING);
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.removePotionEffect(PotionEffectType.SPEED);
    }
    
    public void setTestMode(UUID playerId, boolean enabled) {
        testModeUsers.put(playerId, enabled);
        
        if (!enabled) {
            // Clean up when disabling test mode
            heartEffectsActive.remove(playerId);
            veilEffectsActive.remove(playerId);
        }
    }
}