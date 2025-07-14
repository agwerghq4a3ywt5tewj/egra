package com.fallengod.testament;

import com.fallengod.testament.commands.DatapackCommand;
import com.fallengod.testament.commands.FragmentCommand;
import com.fallengod.testament.commands.TestamentCommand;
import com.fallengod.testament.listeners.*;
import com.fallengod.testament.managers.ConfigManager;
import com.fallengod.testament.managers.FragmentManager;
import com.fallengod.testament.managers.PlayerDataManager;
import com.fallengod.testament.managers.AscensionManager;
import com.fallengod.testament.managers.WeaponAbilityManager;
import com.fallengod.testament.managers.TitleManager;
import com.fallengod.testament.rewards.RewardManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class TestamentPlugin extends JavaPlugin {
    
    private static TestamentPlugin instance;
    private Logger logger;
    
    private ConfigManager configManager;
    private PlayerDataManager playerDataManager;
    private FragmentManager fragmentManager;
    private RewardManager rewardManager;
    private AscensionManager ascensionManager;
    private WeaponAbilityManager weaponAbilityManager;
    private TitleManager titleManager;
    private HeartVeilListener heartVeilListener;
    
    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        
        logger.info("Fallen God Testament is awakening...");
        
        // Initialize managers
        configManager = new ConfigManager(this);
        playerDataManager = new PlayerDataManager(this);
        fragmentManager = new FragmentManager(this);
        rewardManager = new RewardManager(this);
        ascensionManager = new AscensionManager(this);
        weaponAbilityManager = new WeaponAbilityManager(this);
        titleManager = new TitleManager(this);
        
        // Register commands
        registerCommands();
        
        // Register listeners
        registerListeners();
        
        logger.info("Fallen God Testament has awakened! The gods await your fragments...");
    }
    
    @Override
    public void onDisable() {
        logger.info("Fallen God Testament is slumbering...");
        
        // Save all player data
        if (playerDataManager != null) {
            playerDataManager.saveAllData();
        }
        
        logger.info("Fallen God Testament has entered eternal slumber.");
    }
    
    private void registerCommands() {
        getCommand("testament").setExecutor(new TestamentCommand(this));
        getCommand("fragment").setExecutor(new FragmentCommand(this));
        getCommand("datapack").setExecutor(new DatapackCommand(this));
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChestListener(this), this);
        getServer().getPluginManager().registerEvents(new MobListener(this), this);
        getServer().getPluginManager().registerEvents(new AltarListener(this), this);
        getServer().getPluginManager().registerEvents(new WeaponAbilityListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemEffectListener(this), this);
        getServer().getPluginManager().registerEvents(new ToxicityListener(this), this);
        heartVeilListener = new HeartVeilListener(this);
        getServer().getPluginManager().registerEvents(heartVeilListener, this);
    }
    
    public static TestamentPlugin getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
    
    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }
    
    public RewardManager getRewardManager() {
        return rewardManager;
    }
    
    public HeartVeilListener getHeartVeilListener() {
        return heartVeilListener;
    }
    
    public AscensionManager getAscensionManager() {
        return ascensionManager;
    }
    
    public WeaponAbilityManager getWeaponAbilityManager() {
        return weaponAbilityManager;
    }
    
    public TitleManager getTitleManager() {
        return titleManager;
    }
}