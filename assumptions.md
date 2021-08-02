# Assumptions

## Human Player/Game
- Player cannot change difficulty once game begins
- Player has access to instructions page from any screen
- Goals: The player must be carrying 10000 gold and have 406 experience points (level 10) to complete the game; Note: the player must carry 10 000 gold in the inventory to clear the game and its not accumulate 10 000 gold throughout the run
- No MovingEntity can share the same tile

## Character
- Defense calculation: Armour (50% damage reduction) -> defense stat
- If character's defense exceeds damage taken then character takes 1 damage
- Character's level starts at level 1
- The character's max level is level 10
- For each level up, the character receives 1 additional atk and 5 additional health
- The character requires 5 exp to level up to level 2, and any level after that requires 1.5 times more exp than the previous level

Stats at each level:

| Level     | Health     | Attack    | Defence | Attack order |
|-----------|------------|-----------|---------|--------------|
| 1         | 100        | 3         | 0       | 6            |
| 2         | 105        | 4         | 0       | 6            |
| 3         | 110        | 5         | 0       | 6            |
| 4         | 115        | 6         | 0       | 6            |
| 5         | 120        | 7         | 0       | 6            |
| 6         | 125        | 8         | 0       | 6            |
| 7         | 130        | 9         | 0       | 6            |
| 8         | 135        | 10        | 0       | 6            |
| 9         | 140        | 11        | 0       | 6            |
| 10        | 145        | 12        | 0       | 6            |


Experience required to get to each level:

| Level     | Exp Required to level up|
|-----------|-------------------------|
| 1         | 0                       |
| 2         | 5                       |
| 3         | 8                       |
| 4         | 12                      |
| 5         | 18                      |
| 6         | 27                      |
| 7         | 41                      |
| 8         | 62                      |
| 9         | 93                      |
| 10        | 140                     |

Note: Character will have to accumulate a total of 406 experience to reach max level. The exp required to level up is rounded up to the nearest integer

### Inventory
- Character can hold a maximum of 3 allied soliders
- Allied Soldiers have 40 hp, no armour, attack 3 each
- Character can hold a maximum of 16 items in inventory
- Character can equip one each of weapon, helmet, armour, shield, ring (not included in inventory slot)
- Character can only equip and unequip when not in battle
- Character can unequip when having a full inventory resulting in destruction of oldest item
- When an item is destroyed from a full inventory it gives the character 8 gold and 3 xp
- Character can swap between equipped and inventory items, not destroying the oldest item
- Character's swapped item into inventory is now the newest item
- Character's inventory is sorted by oldest first to track what will be deleted next

## Entities/Battle
- The enemys' support/battle radius based on the given requirements (table below)
- The enemys' attack and attack order based on the given requirements (table below)
- The order to attack during a battle is based on Attack order
- Critical hit chance from zombie and vampire is 30%
- If zombie critical strikes the allied soldier, create a zombie at full hp that joins the battle 
- The new zombie created from a crit hit does not attack in the current turn it is transformed in
- Attack order determines who attacks first, where 1 is slowest, 5 is fastest
- Entities target the lowest max hp opposing entity and receive damage from it one by one until either character or enemy dies, if multiple of the same entity then prioritises the one who has been in the battle for longer
- Every cycle in the game, 1-4 slugs will spawn in the world. A slug will spawn at least 2 blocks away from the character and there can only be 4 slugs in the world at a time. The number of slugs that spawn depends on the number of slugs currently in the world. If there are no slugs yet, 4 slugs will spawn. If there are 3 slugs, 1 slug will spawn. i.e. slugs will spawn until there are at most 4 slugs in the world.
- Doggie will always drop a DoggieCoin when killed in battle. The DoggieCoin is a special item that can be sold at the shop at a constantly fluctuating price (Such unstable currency much risk).
- Doggie will be able to spawn every 10 cycles in the world. Such that, Doggie and Elan Muske can spawn at the same time.
- ElanMuske has a 20% to heal himself or his allies / supports in battle. He will heal 10 lost hp when healing. If ElanMuske decides to heal in a turn, he will not attack.
- There can only be one Elan Muske in the world. When Elan Muske is killed, another Elan Muske can only spawn after another 15 cycles the character makes, given that the game is still ongoing and that spawn conditions are fullfilled.
- Doggie stun will also stun towers and allied soldiers

| Entity        | Health     | Attack    | Attack order | Effect                                                     | Drops                           | Battle radius | Support radius | Spawn conditions                                |
|---------------|------------|-----------|--------------|------------------------------------------------------------|---------------------------------|---------------|----------------|-------------------------------------------------|
| Slug          | 12         | 6         | 4            |                                                            | all items, 1 xp, 5 gold         | 2 tiles       | 2 tiles        | Every cycle naturally                           |
| Zombie        | 24         | 10        | 3            | transforms ally soldiers on critical hit                   | all items, 3 xp, 8-10 gold      | 3 tiles       | 2 tiles        | Every cycle from zombie pit                     |
| Vampire       | 100        | 18        | 5            | can crtical bite 1.2-1.5x dmg for 1-4 turns                | all items, 10 xp, 30-50 gold    | 5 tiles       | 7 tiles        | Once in 5 cycles from building                  |
| Doggie        | 230        | 46        | 1            | 10% chance to stun character on attack                     | all items, 50 xp, 50-100 gold   | 2 tiles       | 2 tiles        | Every 20 cycle naturally                        |
| ElanMuske     | 300        | 54        | 1            | 20% chance to heal self or allies (will not attack after)  | all items, 100 xp, 150-250 gold | 2 tiles       | 2 tiles        | Only after 40 cycles and character is max level |
| Allied solider| 20         | 6         | 1            | helps character                                            |                                 |               |                | When character passes barracks                  |

## Items
- We decide the item's stats based on the given requirements (shown in the shop)
- Each Health Potion takes up one inventory slot
- Health Potion on use removes oldest one
- Health Potion on use by key press of 'H'
- Can't use Health Potion when on maximum health
- The drop rates of items from card destruction and enemies (table below)
- The One Ring can be dropped when any battle ends at a 1% chance
- The One Ring is equippable and automatically activates upon death
- The One Ring disappears upon use
- The One Ring revives character to full hp and lets the battle continue with the state of the battle the same (next entity to attack is the same)
- Staff's Trance: 20% chance for effect, duration of trance is 3 turns (exlcuding current turn), turns enemy into allied soldier and drops the enemy's loot
- When transformed allied soldier's time has ran out, transform back into the transformed enemy at its prior hp
- If transformed allied soldier gets transformed into a zombie, then its prior enemy state (prior to it being tranced) will drop its loot
- If a transformed allied soldier dies during battle, their loot will be dropped
- This transformed allied soldier is prioritised over normal allied soldiers when taking damage (oldest one prioritised also)
- If a transformed allied soldier survives until the end of the battle, they will die and drop their prior enemy loot
- If character dies, all transformed allied soldiers will perish
- Health Potion spawns in single amounts 1-2 times (equal chances) on the walkable tiles every cycle for the character to collect
- Gold spawns in 5-10 amounts 1-3 times (equal chances) on the walkable tiles every cycle for the character to collect
- Health Potion and Gold spawns where there are no buildings placed on the walkable tiles (if no space then none spawns)
- Health Potion spawns is prioritised over Gold spawns (if only 1 empty walkable tile it will be a Health Potion)
- Multiple rare items can be dropped from one battle
- Each time the character unequips a rare item in confusion mode, its bonus effect will be rerolled for added confusion
- TheOneRing will be consumed first before it being consumed from an added rare item in confusing mode
- If a rare item has the bonus attribute of anduril in confusing mode, then the character's action will do a bonus 10 damage

### Hero's Castle
- Loopholing the spec: which says shop appears every 1 full cycle, 2 full cycles, 3 full cycles, we will cap it at 3 full cycles

Shop:

| Item/Card     | Sell price | Buy price | Shop rate | Slug   | Zombie | Vampire | Card destroyed | Static effect         |
|---------------|------------|-----------|-----------|--------|--------|---------|----------------|-----------------------|
| Sword         | 50         | 150       | 100%      | 10%    | 15%    | 20%     | 40%            |                       |
| Stake         | 60         | 180       | 60%       | 8%     | 12%    | 16%     | 30%            |                       |
| Staff         | 50         | 150       | 60%       | 9%     | 13%    | 18%     | 35%            |                       |
| Armour        | 200        | 600       | 50%       | 1%     | 2%     | 5%      | 5%             | (def * 2) +2          |
| Shield        | 150        | 450       | 50%       | 2%     | 5%     | 8%      | 5%             | def +1                |
| Helmet        | 150        | 450       | 50%       | 2%     | 5%     | 8%      | 5%             | def +3 atk -3         |
| Health Potion | 10         | 30        | 100%      | 15%    | 20%    | 30%     | 100%           |                       |
| The One Ring  | 400        |           | 0%        | 0%     | 0%     | 0%      | 0%             |                       |
| Anduril       | 400        |           | 0%        | 0%     | 0%     | 0%      | 0%             |                       |
| Tree Stump    | 400        |           | 0%        | 0%     | 0%     | 0%      | 0%             | def +3 if boss def +9 |

## Building
- All buildings take up 1 tile but they can be stacked on top of each other
- Character can hold max of 8 cards, when card amount is exceeded the oldest one is destroyed
- Card destroyed can drop any amount of items (1 of each max)
- 1 card is dropped per enemy defeat
- All cards have an equal chance of being dropped after a battle (only 1 card per battle)
- There are no restrictions on how many buildings the player can place aside from tile space

When a card is destroyed it drops:

| Item/xp/gold  | Chance to drop |
|---------------|----------------|
| Sword         | 40%            |
| Stake         | 30%            |
| Staff         | 35%            |
| Armour        | 5%             |
| Shield        | 5%             |
| Helmet        | 5%             |
| Health Potion | 100%           |
| 5 gold        | 100%           |
| 5 xp          | 100%           |

### Tower
- Tower's range is 3 tiles
- Tower deals 5 dmg

### Village
- Village heals Character's missing hp by 20

### Barracks
- When character visits this building (same tile), give 1 allied soldier
- If character already has 3 allied soldiers (max) replace the lowest hp one

### Trap
- Trap deals 10 damage to first enemy that touches
- Enemies killed by traps do not drop any loot, xp or gold.

### Campfire
- Campfire has radius of 2 tiles inclusive
- Doubles character's dmg after addition of all flat damage rates (items, base dmg)
- Does not double the damage of towers or allied soldiers
- If there are multiple campfires within the 2 tile radius of a battle, the effect of the campfire should not stack; i.e. the character's damage will still cap at two times more damage at most

### Vampire Castle
- Spawns 1-2 vampires every 5 cycles (world cycles not since placed) on nearby path within a 3 tile radius inclusive.
- There is an equal chance of spawning 1 or 2 vampires for each vampire castle (50% chance of 1 vampire and 50% chance of 2 vampires).

### Zombie Pit
- Spawns 2-3 zombies every cycle on nearby path within a 3 tile radius inclusive.
- There is an equal chance of spawning 2 or 3 zombies for each zombie pit(50% chance of 2 zombies and 50% chance of 3 zombies)


### Extemsion
- Battle screen
- Can't change equipped items whilst in a battle
- Have buttons for different actions
- Around 2-3 attacks
- Have a button to consume a health potion
- Each action will take up a turn
- Have PP for each action besides HP pot

### Unarmed Skills Table

| Actions    | PP | Accuracy | Effect         |
|------------|----|----------|----------------|
| Slap       |N/A | 100%     | Atk +1         |
| Punch      |N/A | 70%      | Atk +3         |
| Use HP Pot |N/A | 100%     | Use HP Pot     |
 
### Sword Skills Table

| Actions     | PP | Accuracy | Effect         |
|-------------|----|----------|----------------|
| Slash       |N/A | 100%     | Atk +5         |
| Strike      |5   | 70%      | Atk +8         |
| Heavy Slash |3   | 50%      | AOE Atk +7     |
| Use HP Pot  |N/A | 100%     | Use HP Pot     |

### Staff Skills Table

| Actions    | PP | Accuracy | Effect         |
|------------|----|----------|----------------|
| Jab        |N/A | 100%     | Atk +1         |
| Zap        |8   | 70%      | Atk +6         |
| Trance     |5   | 30%      | Trance Enemy   |
| Use HP Pot |N/A | 100%     | Use HP Pot     |

### Stake Skills Table

| Actions     | PP | Accuracy | Effect                     |
|-------------|----|----------|----------------------------|
| Stab        |N/A | 100%     | Atk +2                     |
| Garlic Stab |7   | 80%      | if vamp Atk +9 else Atk +3 |
| Use HP Pot  |N/A | 100%     | Use HP Pot                 |

### Anduril Skills Table

| Actions    | PP | Accuracy | Effect                       |
|------------|----|----------|------------------------------|
| Swing      |N/A | 100%     | Atk +10                      |
| Bad Boy    |10  | 80%      | if doge Atk +30 else Atk +11 |
| Space Ex   |10  | 80%      | if musk Atk +30 else Atk +11 |
| Use HP Pot |N/A | 100%     | Use HP Pot                   |
