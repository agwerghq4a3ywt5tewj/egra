package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.data.PlayerData;
import com.fallengod.testament.enums.GodType;
import com.fallengod.testament.items.FragmentItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AltarListener implements Listener {
    
    private final TestamentPlugin plugin;
    
    public AltarListener(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }
        
        // Only process right-click events
        if (event.getAction() != org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        
        // Enhanced debug logging
        plugin.getLogger().info("=== ALTAR INTERACTION DEBUG ===");
        plugin.getLogger().info("Player: " + player.getName());
        plugin.getLogger().info("Action: " + event.getAction());
        plugin.getLogger().info("Block: " + block.getType());
        
        // Check if player clicked an altar center block
        GodType altarGod = getAltarType(block.getType());
        
        plugin.getLogger().info("Detected altar god: " + altarGod);
        
        if (altarGod == null) {
            plugin.getLogger().info("Not an altar block, ignoring");
            return;
        }
        
        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player.getUniqueId());
        
        // Debug fragment status
        plugin.getLogger().info("Player fragments for " + altarGod + ": " + data.getFragments(altarGod).size() + "/7");
        plugin.getLogger().info("Fragment numbers: " + data.getFragments(altarGod));
        plugin.getLogger().info("Has all fragments: " + data.hasAllFragments(altarGod));
        plugin.getLogger().info("Already completed: " + data.hasCompletedTestament(altarGod));
        
        // Check if player has all fragments for this god
        if (!data.hasAllFragments(altarGod)) {
            plugin.getLogger().info("Missing fragments - showing message to player");
            // Only show message if player has at least 1 fragment to avoid spam
            if (data.getFragments(altarGod).size() > 0) {
                player.sendMessage("§cYou need all 7 " + altarGod.getColoredName() + " §cfragments to complete this testament!");
                player.sendMessage("§7You have " + data.getFragments(altarGod).size() + "/7 fragments.");
            }
            return;
        }
        
        // Check if already completed
        if (data.hasCompletedTestament(altarGod)) {
            plugin.getLogger().info("Testament already completed");
            // Only show message occasionally to avoid spam
            if (player.getTicksLived() % 100 == 0) {
                player.sendMessage("§7You have already mastered the " + altarGod.getColoredName() + " §7testament.");
            }
            return;
        }
        
        plugin.getLogger().info("All checks passed - proceeding with testament completion");
        
        // Check for conflicts
        plugin.getAscensionManager().checkForConflicts(player, altarGod);
        
        // Remove fragments from inventory
        plugin.getLogger().info("Removing fragments from inventory");
        removeFragmentsFromInventory(player, altarGod);
        
        // Complete testament
        plugin.getLogger().info("Completing testament");
        data.removeAllFragments(altarGod);
        data.completeTestament(altarGod);
        
        // Give rewards
        plugin.getLogger().info("Giving rewards");
        plugin.getRewardManager().giveTestamentReward(player, altarGod);
        
        // Check for ascension
        plugin.getLogger().info("Checking for ascension");
        plugin.getAscensionManager().checkForAscension(player);
        
        // Announce completion
        plugin.getLogger().info("Announcing completion");
        announceCompletion(player, altarGod);
        
        // Save data
        plugin.getLogger().info("Saving player data");
        plugin.getPlayerDataManager().savePlayerData(player.getUniqueId());
        
        plugin.getLogger().info("=== TESTAMENT COMPLETION FINISHED ===");
        event.setCancelled(true);
    }
    
    private GodType getAltarType(Material centerBlock) {
        return switch (centerBlock) {
            case CRYING_OBSIDIAN -> GodType.FALLEN;
            case MAGMA_BLOCK -> GodType.BANISHMENT;
            case DARK_PRISMARINE -> GodType.ABYSSAL;
            case OAK_LOG -> GodType.SYLVAN;
            case LIGHTNING_ROD -> GodType.TEMPEST;
            case END_PORTAL_FRAME -> GodType.VEIL;
            case AMETHYST_CLUSTER -> GodType.TIME;
            case ANVIL -> GodType.FORGE;
            case OBSIDIAN -> GodType.VOID;
            case REDSTONE_BLOCK -> GodType.BLOOD;
            case AMETHYST_BLOCK -> GodType.CRYSTAL;
            case SCULK_CATALYST -> GodType.SHADOW;
            default -> null;
        };
    }
    
    private void removeFragmentsFromInventory(Player player, GodType god) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (FragmentItem.isFragment(item) && FragmentItem.getFragmentGod(item) == god) {
                player.getInventory().remove(item);
            }
        }
    }
    
    private void announceCompletion(Player player, GodType god) {
        String message = "§6§l⚡ " + player.getName() + " has completed the " + 
                        god.getColoredName() + " §6§lTestament! ⚡";
        
        // Broadcast to server
        plugin.getServer().broadcastMessage("");
        plugin.getServer().broadcastMessage(message);
        plugin.getServer().broadcastMessage("§7" + god.getTitle() + " has blessed " + player.getName() + "!");
        plugin.getServer().broadcastMessage("");
        
        // Special message to player
        player.sendTitle(god.getColor() + "TESTAMENT COMPLETE!", 
                        "§7You have been blessed by the " + god.getDisplayName(), 
                        10, 70, 20);
    }
}