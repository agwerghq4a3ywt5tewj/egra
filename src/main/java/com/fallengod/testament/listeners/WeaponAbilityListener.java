package com.fallengod.testament.listeners;

import com.fallengod.testament.TestamentPlugin;
import com.fallengod.testament.items.SpecialItems;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WeaponAbilityListener implements Listener {
    
    private final TestamentPlugin plugin;
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    private final Map<UUID, Long> lastMovement = new HashMap<>();
    
    public WeaponAbilityListener(TestamentPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) {
            return;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasCustomModelData()) {
            return;
        }
        
        int modelData = meta.getCustomModelData();
        
        // Check for specific testament weapons and activate their abilities
        switch (modelData) {
            case 100060 -> { // Forge Mace - Forge Mastery
                activateForgeAbility(player, item);
                event.setCancelled(true);
            }
            case 100061 -> { // Void Blade - Void Rip
                activateVoidRip(player, item);
                event.setCancelled(true);
            }
            case 100062 -> { // Chronos Staff - Time Dilation
                activateTimeDilation(player, item);
                event.setCancelled(true);
            }
            case 100064 -> { // Resonance Crystal - Ore Sense
                activateOreSense(player, item);
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasCustomModelData()) {
            return;
        }
        
        int modelData = item.getItemMeta().getCustomModelData();
        
        if (event.isSneaking()) {
            switch (modelData) {
                case 100064 -> { // Resonance Crystal - Crystal Shield
                    activateCrystalShield(player, item);
                }
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        // Check for Shadow Cloak umbral form - now works with phantom membrane in inventory
        if (hasShadowCloak(player)) {
            activateUmbralForm(player);
        }
    }
    
    @EventHandler
    public void onEntityFallDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }
        
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon != null && weapon.hasItemMeta() && weapon.getItemMeta().hasCustomModelData() 
            && weapon.getItemMeta().getCustomModelData() == 100060) { // Forge Mace
            
            // Wind Burst on fall damage
            activateWindBurst(player, weapon, event.getDamage());
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) {
            return;
        }
        
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon == null || !weapon.hasItemMeta() || !weapon.getItemMeta().hasCustomModelData()) {
            return;
        }
        
        int modelData = weapon.getItemMeta().getCustomModelData();
        
        switch (modelData) {
            case 100063 -> activateBloodFrenzy(player, weapon); // Crimson Blade
            case 100061 -> activatePhaseStrike(player, event); // Void Blade
            case 100064 -> activateSonicBoom(player, weapon); // Resonance Crystal - on attack
        }
        
        // Check for Shadow Strike (backstab damage)
        if (hasShadowCloak(player)) {
            activateShadowStrike(player, event);
        }
    }
    
    private void activateShadowStrike(Player player, EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof org.bukkit.entity.LivingEntity target)) {
            return;
        }
        
        // Check if attacking from behind
        var playerLoc = player.getLocation();
        var targetLoc = target.getLocation();
        var targetDirection = targetLoc.getDirection();
        var attackDirection = targetLoc.subtract(playerLoc).toVector().normalize();
        
        // Calculate angle between target's facing direction and attack direction
        double angle = targetDirection.angle(attackDirection);
        
        // If attacking from behind (within 60 degrees of target's back)
        if (angle < Math.toRadians(60)) {
            double originalDamage = event.getDamage();
            event.setDamage(originalDamage * 3.0); // Triple damage from behind
            
            // Visual effects
            player.getWorld().spawnParticle(Particle.CRIT, target.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
            player.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 0.5f);
            
            player.sendMessage("§8§lSHADOW STRIKE! §7Critical hit from the shadows!");
            player.sendActionBar("§8⚔ Shadow Strike: " + String.format("%.1f", event.getDamage()) + " damage");
        }
    }
    
    private void activateForgeAbility(Player player, ItemStack item) {
        if (isOnCooldown(player, "forge_mastery")) {
            player.sendMessage("§cForge Mastery is on cooldown!");
            return;
        }
        
        // Repair nearby items in inventory
        for (ItemStack invItem : player.getInventory().getContents()) {
            if (invItem != null && invItem.getType().getMaxDurability() > 0) {
                invItem.setDurability((short) 0);
            }
        }
        
        player.sendMessage("§6§lForge Mastery! §7Your items have been repaired by divine forging.");
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.2f);
        setCooldown(player, "forge_mastery", 30);
    }
    
    private void activateVoidRip(Player player, ItemStack item) {
        if (isOnCooldown(player, "void_rip")) {
            player.sendMessage("§cVoid Rip is on cooldown!");
            return;
        }
        
        // Teleport 10 blocks forward
        var direction = player.getLocation().getDirection().normalize();
        var targetLoc = player.getLocation().add(direction.multiply(10));
        
        // Ensure safe teleport
        if (targetLoc.getBlock().getType().isSolid()) {
            targetLoc.setY(targetLoc.getY() + 2);
        }
        
        // Create void particles at origin
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50, 1, 1, 1, 0.1);
        
        player.teleport(targetLoc);
        
        // Create void particles at destination
        player.getWorld().spawnParticle(Particle.PORTAL, targetLoc, 50, 1, 1, 1, 0.1);
        player.getWorld().playSound(targetLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
        
        player.sendMessage("§8§lVoid Rip! §7You tear through reality itself.");
        setCooldown(player, "void_rip", 5);
    }
    
    private void activateTimeDilation(Player player, ItemStack item) {
        if (isOnCooldown(player, "time_dilation")) {
            player.sendMessage("§cTime Dilation is on cooldown!");
            return;
        }
        
        // Buffed Time Dilation - more powerful effects
        player.getNearbyEntities(10, 10, 10).forEach(entity -> {
            if (entity instanceof org.bukkit.entity.LivingEntity living && !(entity instanceof Player)) {
                // Much stronger slowness effects
                living.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, 4)); // 20 seconds, level 5
                living.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 400, 3)); // 20 seconds, level 4
                living.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400, 2)); // 20 seconds, level 3
            }
        });
        
        // Give player time mastery effects
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 2)); // Speed 3 for 20 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 400, 3)); // Haste 4 for 20 seconds
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 1)); // Regen 2 for 20 seconds
        
        // Enhanced visual effects
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 0.5f);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.5f);
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1, 0), 50, 3, 3, 3, 0.1);
        
        player.sendMessage("§d§lTIME DILATION! §7You become the master of time itself!");
        player.sendActionBar("§d⏰ Time bends to your will - enemies frozen, you accelerated");
        setCooldown(player, "time_dilation", 25); // Reduced cooldown from 30 to 25
    }
    
    private void activateOreSense(Player player, ItemStack item) {
        if (isOnCooldown(player, "ore_sense")) {
            player.sendMessage("§cOre Sense is on cooldown!");
            return;
        }
        
        // Reveal ores within 20 blocks
        var center = player.getLocation();
        int oreCount = 0;
        
        for (int x = -20; x <= 20; x++) {
            for (int y = -20; y <= 20; y++) {
                for (int z = -20; z <= 20; z++) {
                    var loc = center.clone().add(x, y, z);
                    var block = loc.getBlock();
                    
                    if (isOre(block.getType())) {
                        // Send glowing effect to player
                        player.sendBlockChange(loc, Material.GLOWSTONE.createBlockData());
                        oreCount++;
                    }
                }
            }
        }
        
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, 1.0f, 1.5f);
        player.sendMessage("§b§lOre Sense! §7Detected " + oreCount + " ore blocks within 20 blocks.");
        setCooldown(player, "ore_sense", 30);
    }
    
    private void activateCrystalShield(Player player, ItemStack item) {
        if (isOnCooldown(player, "crystal_shield")) {
            player.sendMessage("§cCrystal Shield is on cooldown!");
            return;
        }
        
        // Give temporary resistance
        player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 200, 2));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 1));
        
        // Visual effects
        player.getWorld().spawnParticle(Particle.END_ROD, player.getLocation().add(0, 1, 0), 20, 1, 1, 1, 0.1);
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0f, 1.0f);
        
        player.sendMessage("§b§lCrystal Shield! §7Protective crystals surround you.");
        setCooldown(player, "crystal_shield", 45);
    }
    
    private void activateSonicBoom(Player player, ItemStack weapon) {
        if (isOnCooldown(player, "sonic_boom")) {
            return;
        }
        
        // Shatter blocks in cone
        var direction = player.getLocation().getDirection();
        for (int distance = 1; distance <= 8; distance++) {
            for (int angle = -30; angle <= 30; angle += 10) {
                var rotated = rotateVector(direction, angle);
                var targetLoc = player.getLocation().add(rotated.multiply(distance));
                var block = targetLoc.getBlock();
                
                if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK && 
                    !block.getType().name().contains("SHULKER") && !block.getType().name().contains("CHEST")) {
                    block.breakNaturally();
                }
            }
        }
        
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_SONIC_BOOM, 2.0f, 1.0f);
        player.getWorld().spawnParticle(Particle.SONIC_BOOM, player.getLocation(), 1);
        
        player.sendMessage("§b§lSonic Boom! §7Reality shatters before you.");
        setCooldown(player, "sonic_boom", 15);
    }
    
    private void activateBloodFrenzy(Player player, ItemStack weapon) {
        double healthPercent = player.getHealth() / player.getMaxHealth();
        
        if (healthPercent <= 0.25) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 4));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 3));
            player.sendMessage("§4§lBLOOD FRENZY! §cYour rage knows no bounds!");
        } else if (healthPercent <= 0.5) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, 200, 2));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
            player.sendMessage("§c§lBlood Frenzy! §7Your wounds fuel your fury.");
        }
        
        // Life steal effect
        if (player.getHealth() < player.getMaxHealth()) {
            double healAmount = Math.min(2.0, player.getMaxHealth() - player.getHealth());
            player.setHealth(player.getHealth() + healAmount);
        }
    }
    
    private void activatePhaseStrike(Player player, EntityDamageByEntityEvent event) {
        // Ignore armor by increasing damage
        double originalDamage = event.getDamage();
        event.setDamage(originalDamage * 1.5); // 50% more damage to simulate armor ignore
        
        // Visual effects
        player.getWorld().spawnParticle(Particle.PORTAL, event.getEntity().getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
        player.sendMessage("§8§lPhase Strike! §7Your attack phases through armor.");
    }
    
    private void activateWindBurst(Player player, ItemStack weapon, double fallDamage) {
        if (fallDamage < 3.0) return; // Only activate on significant falls
        
        // Create wind burst effect
        var center = player.getLocation();
        
        // Damage and knockback nearby entities
        for (var entity : player.getNearbyEntities(8, 8, 8)) {
            if (entity instanceof org.bukkit.entity.LivingEntity living && !(entity instanceof Player)) {
                // Calculate damage based on fall distance
                double damage = Math.min(fallDamage * 2, 40); // Cap at 40 damage
                living.damage(damage, player);
                
                // Knockback
                var direction = entity.getLocation().subtract(center).toVector().normalize();
                direction.setY(0.5); // Add upward component
                entity.setVelocity(direction.multiply(2));
            }
        }
        
        // Visual and sound effects
        player.getWorld().spawnParticle(Particle.EXPLOSION, center, 5, 2, 0.5, 2, 0);
        player.getWorld().spawnParticle(Particle.GUST, center, 20, 3, 1, 3, 0.2);
        player.getWorld().playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 2.0f, 0.8f);
        player.getWorld().playSound(center, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 2.0f, 1.0f);
        
        player.sendMessage("§6§lWIND BURST! §7Your impact creates a devastating shockwave!");
    }
    
    private void activateUmbralForm(Player player) {
        int lightLevel = player.getLocation().getBlock().getLightLevel();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        
        // Track player movement
        Long lastMove = lastMovement.get(playerId);
        if (lastMove == null || (currentTime - lastMove) > 2000) { // 2 seconds of no movement
            lastMovement.put(playerId, currentTime);
        }
        
        boolean isStill = (currentTime - lastMovement.get(playerId)) > 2000;
        
        // Check if player is in darkness and not moving much
        if (lightLevel < 7 && isStill) {
            // Apply invisibility
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 100, 0, true, false));
            
            // Silent movement
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0, true, false));
            
            if (player.getTicksLived() % 60 == 0) { // Every 3 seconds
                player.sendActionBar("§8👤 One with the darkness");
            }
        } else {
            // Update movement time if player moved
            if (player.getVelocity().lengthSquared() > 0.01) {
                lastMovement.put(playerId, currentTime);
            }
        }
    }
    
    private boolean isOre(Material material) {
        return switch (material) {
            case COAL_ORE, DEEPSLATE_COAL_ORE,
                 IRON_ORE, DEEPSLATE_IRON_ORE,
                 COPPER_ORE, DEEPSLATE_COPPER_ORE,
                 GOLD_ORE, DEEPSLATE_GOLD_ORE,
                 REDSTONE_ORE, DEEPSLATE_REDSTONE_ORE,
                 LAPIS_ORE, DEEPSLATE_LAPIS_ORE,
                 DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE,
                 EMERALD_ORE, DEEPSLATE_EMERALD_ORE,
                 NETHER_GOLD_ORE, NETHER_QUARTZ_ORE,
                 ANCIENT_DEBRIS -> true;
            default -> false;
        };
    }
    
    private boolean hasShadowCloak(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (SpecialItems.isShadowCloak(item)) {
                return true;
            }
        }
        return false;
    }
    
    private org.bukkit.util.Vector rotateVector(org.bukkit.util.Vector vector, double angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);
        
        double x = vector.getX() * cos - vector.getZ() * sin;
        double z = vector.getX() * sin + vector.getZ() * cos;
        
        return new org.bukkit.util.Vector(x, vector.getY(), z);
    }
    
    private boolean isOnCooldown(Player player, String ability) {
        UUID playerId = player.getUniqueId();
        Map<String, Long> playerCooldowns = cooldowns.get(playerId);
        
        if (playerCooldowns == null) {
            return false;
        }
        
        Long lastUsed = playerCooldowns.get(ability);
        if (lastUsed == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        long cooldownMs = getCooldownTime(ability) * 1000L;
        
        return (currentTime - lastUsed) < cooldownMs;
    }
    
    private int getCooldownTime(String ability) {
        return switch (ability) {
            case "forge_mastery" -> 30;
            case "void_rip" -> 5;
            case "time_dilation" -> 25;
            case "ore_sense" -> 30;
            case "crystal_shield" -> 45;
            case "sonic_boom" -> 15;
            default -> 10;
        };
    }
    
    private void setCooldown(Player player, String ability, int cooldownSeconds) {
        UUID playerId = player.getUniqueId();
        cooldowns.computeIfAbsent(playerId, k -> new HashMap<>())
                 .put(ability, System.currentTimeMillis());
    }
}