# Fallen God Testament - Complete Minecraft Plugin

## 🎮 Overview
Epic Minecraft plugin where players collect 7 rare fragments from each of 12 fallen gods to unlock ultimate rewards and divine powers. Built for Paper 1.21.5 with Java 21.

## ⚡ Quick Start

### 1. Build the Plugin
```bash
mvn clean package
```

### 2. Install Plugin
```bash
# Copy the compiled JAR to your server
cp target/testament-1.7.0.jar /path/to/server/plugins/
```

### 3. Install Datapack
```bash
# Copy datapack to each world
cp -r datapack/ /path/to/world/datapacks/fallengod_testament/
```

### 4. Start Server
```bash
# Restart server or reload
/reload
```

## 🏛️ The Twelve Gods & Their Rewards

### **Core Six Gods** (Fully Implemented)

#### 1. **Fallen God** - Ultimate Defense
- **Theme**: Death, undeath, ultimate protection
- **Reward**: **Heart of the Fallen God** + Netherite armor set
- **Power**: 25 total hearts + Strength, Regeneration, Resistance
- **Biomes**: Swamps, Dark Forests, Deep Dark
- **Altar Center**: Crying Obsidian

#### 2. **Banishment God** - Ultimate Offense  
- **Theme**: Fire, exile, destruction
- **Reward**: Ultimate weapon and tool set (Sword, Axe, Pickaxe)
- **Power**: Overpowered enchanted weapons with fire aspects
- **Biomes**: Deserts, Badlands, Savannas
- **Altar Center**: Magma Block

#### 3. **Abyssal God** - Master of Depths
- **Theme**: Ocean depths, water mastery
- **Reward**: **Trident of the Endless Deep** + water abilities
- **Power**: Permanent Water Breathing, Dolphins Grace
- **Biomes**: All ocean types (including underwater)
- **Altar Center**: Dark Prismarine

#### 4. **Sylvan God** - Nature's Guardian
- **Theme**: Forests, nature, growth
- **Reward**: **Bow of the Ancient Forest** + nature powers
- **Power**: Permanent Regeneration II, Saturation
- **Biomes**: Forests, Jungles, Taigas
- **Altar Center**: Oak Log

#### 5. **Tempest God** - Storm Lord
- **Theme**: Sky, storms, lightning, flight
- **Reward**: **Wings of the Storm Lord** + storm powers
- **Power**: Enhanced elytra, Speed II, Jump Boost III
- **Biomes**: Mountains, Hills, Peaks
- **Altar Center**: Lightning Rod

#### 6. **Veil God** - Master of Dimensions
- **Theme**: Reality manipulation, void magic
- **Reward**: **Veil of Nullification** + dimension powers
- **Power**: Counters Heart of Fallen God, Night Vision, Speed
- **Biomes**: End dimension only
- **Altar Center**: End Portal Frame (with Eye)

### **Expansion Six Gods** (Ready for Activation)

#### 7. **Forge God** - Master of Creation
- **Theme**: Smithing, crafting, creation, molten metal
- **Reward**: **Mace of Divine Forging** + crafting mastery
- **Power**: Enhanced mace with Wind Burst, Forge Mastery ability
- **Biomes**: Mountains, Caves, Lava areas
- **Altar Center**: Anvil
- **Special**: Right-click to repair all items (30s cooldown)

#### 8. **Void God** - Harbinger of Nothingness
- **Theme**: Emptiness, teleportation, phase shifting
- **Reward**: **Void Walker's Blade** + teleportation abilities
- **Power**: Void Rip teleportation, Phase Strike ignores armor
- **Biomes**: End dimension, Deep caves
- **Altar Center**: Obsidian
- **Special**: Right-click to teleport 10 blocks forward (5s cooldown)

#### 9. **Time God** - Chronos Incarnate
- **Theme**: Time manipulation, aging, temporal magic
- **Reward**: **Chronos Staff** + time abilities
- **Power**: Time Dilation slows enemies, temporal mastery
- **Biomes**: Deep Dark, Dripstone Caves, Lush Caves
- **Altar Center**: Amethyst Cluster
- **Special**: Right-click for Time Dilation (25s cooldown)

#### 10. **Blood God** - Warrior's Patron
- **Theme**: Combat, sacrifice, berserker rage
- **Reward**: **Crimson Blade** + battle frenzy
- **Power**: Blood Frenzy (stronger when injured), Life Steal
- **Biomes**: Nether, dangerous areas
- **Altar Center**: Redstone Block
- **Special**: Damage increases as health decreases

#### 11. **Crystal God** - Resonance Master
- **Theme**: Crystals, sound, vibration, harmony
- **Reward**: **Resonance Crystal** + sonic abilities
- **Power**: Ore Sense, Crystal Shield, Sonic Boom
- **Biomes**: Geode caves, crystal formations
- **Altar Center**: Large Amethyst Cluster
- **Special**: Right-click for Ore Sense, Sneak for Crystal Shield

#### 12. **Shadow God** - Master of Darkness
- **Theme**: Stealth, darkness, assassination
- **Reward**: **Shadow Mantle** + stealth abilities
- **Power**: Umbral Form (invisible in darkness), Shadow Strike
- **Biomes**: Dark areas, caves, sculk biomes
- **Altar Center**: Sculk Catalyst
- **Special**: Stand still in darkness for invisibility

## ⚔️ Advanced Weapon Abilities

### **Active Abilities** (Right-click or Special Actions)
- **Forge Mastery**: Repair all items in inventory (Forge Mace)
- **Void Rip**: Teleport 10 blocks forward (Void Blade)
- **Time Dilation**: Slow all nearby enemies for 20 seconds (Chronos Staff)
- **Ore Sense**: Reveal ores within 20 blocks (Resonance Crystal)
- **Crystal Shield**: Resistance II + Absorption I (Resonance Crystal)
- **Sonic Boom**: Shatter blocks in cone shape (Resonance Crystal)

### **Passive Abilities**
- **Wind Burst**: Enhanced fall damage creates shockwave (Forge Mace)
- **Phase Strike**: Attacks ignore 50% armor (Void Blade)
- **Blood Frenzy**: Damage scales with missing health (Crimson Blade)
- **Life Steal**: Heal 2 hearts on successful attacks (Crimson Blade)
- **Umbral Form**: Invisibility when motionless in darkness (Shadow Mantle)
- **Shadow Strike**: 3x damage when attacking from behind (Shadow Mantle)

## 🌟 Divine Ascension System

### **Ascension Levels**
- **Mortal** (0 testaments): Base gameplay
- **Blessed** (1-2 testaments): Luck I, minor divine favor
- **Chosen** (3-4 testaments): Luck II, Hero of Village, significant abilities
- **Divine** (5-6 testaments): Luck III, enhanced effects, reality manipulation
- **Godlike** (7+ testaments): Maximum effects, ultimate cosmic powers

### **Testament Conflicts**
Opposing god pairs create strategic choices:
- **Fallen vs Veil**: Death vs Reality (Heart/Veil nullification)
- **Banishment vs Abyssal**: Fire vs Water
- **Sylvan vs Tempest**: Nature vs Storm
- **Forge vs Void**: Creation vs Destruction
- **Time vs Shadow**: Light vs Dark
- **Blood vs Crystal**: Chaos vs Order

## 🎯 Fragment Collection System

### **Rarity & Distribution**
- **Fragment 1**: 30% chance (most common)
- **Fragment 2**: 20% chance
- **Fragment 3**: 15% chance
- **Fragment 4**: 13% chance
- **Fragment 5**: 10% chance
- **Fragment 6**: 7% chance
- **Fragment 7**: 5% chance (rarest)

### **Spawn Mechanics**
- **Chest Spawning**: 2% chance after opening 50+ chests
- **Mob Drops**: 0.1% from dangerous mobs (Wither Skeleton, Blaze, Elder Guardian, etc.)
- **Cooldowns**: 2 hours between chest fragments, 1 hour between mob drops
- **Biome Influence**: Higher chances in god-appropriate biomes

## 🏗️ Altar System

### **Natural Generation**
- **Spacing**: 32 chunks between altars (512 blocks)
- **Separation**: 8 chunks minimum between different types
- **Biome Specific**: Each god spawns only in thematic biomes
- **Structure**: 7x7 base with unique center blocks and decorations

### **Manual Altar Building**
All altars follow this pattern:
```
Base Layer (7x7): God-specific material
Center Block: Unique interaction block per god
Walls: Themed walls around perimeter
Decorations: Atmospheric lighting and details
Beacon: Top beacon for visibility
```

## 📋 Commands

### **Player Commands**
- `/testament status` - View fragment progress and ascension level
- `/testament reunite <god>` - Check if ready to complete testament

### **Admin Commands**
- `/fragment spawn <god> <number> <here>` - Spawn specific fragments
- `/fragment heart` - Give Heart of Fallen God
- `/fragment veil` - Give Veil of Nullification
- `/fragment giveall <god>` - Give all 7 fragments for testing
- `/fragment testfragments <god>` - Give fragments + show altar info
- `/fragment testmode <on|off>` - Toggle Heart/Veil testing
- `/fragment title <player> <title|reset>` - Manage player titles
- `/datapack scan [radius]` - Scan for nearby altars
- `/datapack locate <god>` - Find specific altar type

## 🎮 Player Title System

### **Title Types**
- **Fallen**: Toxic behavior + excessive deaths (permanent consequences)
- **Toxic**: Detected toxic chat/behavior
- **Blessed**: Divine favor from gods
- **Champion**: Skilled warrior status
- **Legend**: Legendary achievements

### **Toxicity Detection**
- Chat spam detection
- Toxic language filtering
- Excessive caps monitoring
- Death count tracking
- Automatic "Fallen" status for severe cases

## ⚔️ Strategic PvP Balance

### **Heart vs Veil System**
- **Heart of Fallen God**: Ultimate power (25 hearts + effects)
- **Veil of Nullification**: Counters Heart within 16 blocks
- **Strategic Positioning**: Risk/reward gameplay
- **Test Mode**: `/fragment testmode on` for testing interactions

## 🔧 Configuration

Customize all settings in `config.yml`:
- Fragment spawn rates and cooldowns
- Heart of Fallen God effects and power levels
- Veil nullification range and effects
- God-specific biomes and altar blocks
- Ascension level requirements and effects

## 🏗️ Technical Implementation

### **Requirements**
- **Server**: Paper 1.21.5+
- **Java**: 21+
- **Maven**: 3.6+

### **Features**
- Async fragment spawning operations
- Persistent player data storage with YAML
- Real-time Heart/Veil interactions
- Natural altar generation via datapack
- Comprehensive admin tools and testing commands
- Performance optimizations with caching
- Weapon ability system with cooldowns
- Divine ascension progression tracking

### **File Structure**
```
src/main/java/com/fallengod/testament/
├── TestamentPlugin.java          # Main plugin class
├── enums/                        # God types, ascension levels, titles
├── data/PlayerData.java          # Player progress tracking
├── managers/                     # Core system managers
├── items/                        # Fragment and special items
├── commands/                     # Command handlers
├── listeners/                    # Event listeners
├── rewards/RewardManager.java    # Testament rewards
├── weapons/                      # Weapon ability system
└── quests/LegendaryQuest.java    # Future quest system
```

## 🎮 Gameplay Flow

1. **Exploration Phase**: Players explore, open chests, fight dangerous mobs
2. **Collection Phase**: Gather 7 unique fragments for chosen god(s)
3. **Reunification Phase**: Locate appropriate altar and complete testament
4. **Ascension Phase**: Gain divine powers and strategic advantages
5. **Mastery Phase**: Complete multiple testaments for ultimate power

## 🌟 End Game Content

### **Testament Completion Rewards**
- **Fallen God**: Ultimate survivability + Heart power
- **Banishment God**: Ultimate damage output with weapon set
- **Abyssal God**: Water world domination with trident
- **Sylvan God**: Nature harmony and archery mastery
- **Tempest God**: Sky mobility and storm power
- **Veil God**: Reality manipulation and Heart counter

### **Future Legendary Quests**
- **The Convergence**: Unite all divine powers (12 testaments)
- **The Sundering**: Choose one god, reject all others
- **The Balance**: Maintain equilibrium between opposing forces
- **The Transcendence**: Surpass the gods themselves

## 🚀 Getting Started

1. **Install** the plugin and datapack
2. **Explore** to find fragments (start with chests)
3. **Collect** 7 fragments from your chosen god
4. **Locate** the appropriate altar using `/datapack locate <god>`
5. **Complete** your first testament by right-clicking the altar center
6. **Ascend** through divine power levels
7. **Master** multiple gods for ultimate abilities

## 🎯 Design Philosophy

1. **Rarity Creates Value**: Low drop rates make fragments precious
2. **Exploration Rewards**: Dangerous areas have better fragment chances
3. **Strategic Depth**: Testament conflicts create tactical decisions
4. **Long-term Engagement**: Multiple testaments provide extended goals
5. **Server Community**: Epic announcements create shared experiences
6. **Balanced Power**: Each god offers unique advantages without being overpowered

## 📊 Performance & Compatibility

- **Minimal Impact**: Efficient caching and async operations
- **Thread Safe**: All implementations designed for server stability
- **Balanced Rates**: Spawn rates tuned for long-term engagement
- **Scalable**: Supports multiple players and extensive gameplay

## 🔍 Troubleshooting

### **Altars Not Generating**
- Ensure datapack is loaded: `/datapack list`
- Explore NEW chunks (altars don't generate in existing areas)
- Check biome requirements for each god type
- Use `/datapack scan 1000` to find nearby altars

### **Fragments Not Spawning**
- Open 50+ chests before fragments can spawn
- Check cooldown timers (2h chest, 1h mob)
- Fight dangerous mobs in appropriate biomes
- Verify plugin permissions

### **Testament Not Completing**
- Ensure you have all 7 fragments in inventory
- Right-click the correct center block for each god
- Check console for debug messages
- Use `/fragment testfragments <god>` for testing

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

**Transform your Minecraft server into an epic realm where players forge their own divine destinies!**