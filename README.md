# Cave Game Project

This repository contains multiple Java-based games with a cave/adventure theme.

## Games Included

### 1. Cave Loot Challenge
A game where you play as an adventurer exploring a cave filled with valuable treasures. Your goal is to collect the most valuable loot, but your backpack has limited capacity. As you encounter treasures one by one, you must decide whether to take all of it, only part of it, or skip it entirely.

### 2. Simple Mario Game (Cave Adventure)
A platformer game where you control a character navigating through cave platforms, collecting treasures, and avoiding enemies. The game features:
- Orc enemies with animation
- Collectible treasure items with different values and weights
- Platform jumping mechanics
- Backpack inventory system

## Game Mechanics

### Cave Loot Challenge
1. **Queue of Items**: Treasures appear one at a time from a randomized queue.
2. **Item Information**: Each item displays its name, value, and weight.
3. **Player Decisions**:
   - Take All: Add the entire item to your backpack if there's enough space.
   - Take Partial: Take only what fits in your remaining backpack space (only available when the item won't completely fit).
   - Skip: Pass on the current item and move to the next one.
4. **Goal**: Maximize the total value of items in your backpack before it reaches capacity.

### Simple Mario Game
1. **Movement**: Use arrow keys to move left/right and jump.
2. **Platforms**: Navigate across platforms to collect treasures.
3. **Enemies**: Avoid or defeat orc enemies by jumping on them.
4. **Items**: Collect valuable items that appear throughout the level.
5. **Backpack System**: Manage your inventory with limited capacity.
6. **Goal**: Collect all treasures while avoiding or defeating enemies.

## How to Run the Games

### Prerequisites
- Java Development Kit (JDK) 8 or higher installed on your system

### Running Cave Loot Challenge
1. Open a command prompt or terminal
2. Navigate to the game directory
3. Use the provided batch file:
   ```
   WindowBatchFiles\RunCaveLootChallenge.bat
   ```
   
   Or compile and run manually:
   ```
   javac src\CaveLootChallenge.java
   java -cp src CaveLootChallenge
   ```

### Running Simple Mario Game
1. Open a command prompt or terminal
2. Navigate to the game directory
3. Use the provided batch file:
   ```
   WindowBatchFiles\RunSimpleMarioGame.bat
   ```
   
   Or compile and run manually:
   ```
   javac src\SimpleMarioGame.java
   java -cp src SimpleMarioGame
   ```

## Game Controls

### Cave Loot Challenge
The game is controlled entirely with mouse clicks on the three action buttons:
- **Take All**: Click to add the entire item to your backpack
- **Take Partial**: Click to add a portion of the item (only enabled when relevant)
- **Skip**: Click to pass on the current item

### Simple Mario Game
- **Left/Right Arrow Keys**: Move the character left or right
- **Up Arrow Key**: Jump
- **Mouse**: Interact with item collection dialogs

## Game Assets
The games use visual assets from various sources:

- **Background Images**: Cave backgrounds for immersive gameplay
- **Character Sprites**: Player character and animated orc enemies
- **Item Images**: Various treasure items with different appearances
- **Platform Textures**: Cave-themed platforms and ground elements

### Asset Credits
- Orc sprites from the "Orc Crew" collection
- Treasure item images from the "LootItems" collection

## Strategy Tips

### Cave Loot Challenge
- Items with high value-to-weight ratios are generally better choices
- Consider skipping low-value items to save space for more valuable treasures later
- The "Take Partial" option can be useful to maximize your backpack's value when it's nearly full

### Simple Mario Game
- Jump on enemies from above to defeat them
- Collect valuable items to increase your score
- Use platforms strategically to reach difficult areas
- Manage your backpack capacity wisely when collecting items

Enjoy your adventures in the Cave Game Project!