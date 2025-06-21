# Cave Loot Challenge

## Game Overview
Cave Loot Challenge is a Java-based game where you play as an adventurer exploring a cave filled with valuable treasures. Your goal is to collect the most valuable loot, but your backpack has limited capacity. As you encounter treasures one by one, you must decide whether to take all of it, only part of it, or skip it entirely.

## Game Mechanics
1. **Queue of Items**: Treasures appear one at a time from a randomized queue.
2. **Item Information**: Each item displays its name, value, and weight.
3. **Player Decisions**:
   - Take All: Add the entire item to your backpack if there's enough space.
   - Take Partial: Take only what fits in your remaining backpack space (only available when the item won't completely fit).
   - Skip: Pass on the current item and move to the next one.
4. **Goal**: Maximize the total value of items in your backpack before it reaches capacity.

## How to Run the Game

### Prerequisites
- Java Development Kit (JDK) 8 or higher installed on your system

### Running the Game
1. Open a command prompt or terminal
2. Navigate to the game directory
3. Compile the Java file:
   ```
   javac CaveLootChallenge.java
   ```
4. Run the compiled program:
   ```
   java CaveLootChallenge
   ```

## Game Controls
The game is controlled entirely with mouse clicks on the three action buttons:
- **Take All**: Click to add the entire item to your backpack
- **Take Partial**: Click to add a portion of the item (only enabled when relevant)
- **Skip**: Click to pass on the current item

## Game Assets
The game uses visual assets from the CaveGame resource pack created by Anokolisa. These assets include:
- Background images
- Item representations based on resource types

## Strategy Tips
- Items with high value-to-weight ratios are generally better choices
- Consider skipping low-value items to save space for more valuable treasures later
- The "Take Partial" option can be useful to maximize your backpack's value when it's nearly full

Enjoy your adventure in the Cave Loot Challenge!