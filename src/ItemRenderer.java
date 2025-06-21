import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * This class handles rendering of item images for the Cave Loot Challenge game.
 * It loads and caches images from the resource folders and provides methods to draw
 * them on the game panel.
 */
public class ItemRenderer {
    // Cache for loaded images to avoid reloading
    private static HashMap<String, BufferedImage> imageCache = new HashMap<>();
    
    // Paths to different resource types with absolute paths
    private static final String RESOURCES_PATH = "c:\\CaveGame\\Sprites Assets\\Environment\\Props\\Static\\Resources.png";
    private static final String DUNGEON_PROPS_PATH = "c:\\CaveGame\\Sprites Assets\\Environment\\Props\\Static\\Dungeon_Props.png";
    private static final String ESOTERIC_PATH = "c:\\CaveGame\\Sprites Assets\\Environment\\Props\\Static\\Esoteric.png";
    private static final String TOOLS_PATH = "c:\\CaveGame\\Sprites Assets\\Environment\\Props\\Static\\Tools.png";
    
    // Default image size
    private static final int DEFAULT_ITEM_SIZE = 32;
    
    /**
     * Draws an item image on the specified graphics context
     * 
     * @param g The graphics context to draw on
     * @param item The item to draw
     * @param x The x-coordinate to draw at
     * @param y The y-coordinate to draw at
     * @param width The width to draw the image
     * @param height The height to draw the image
     */
    public static void drawItem(Graphics g, CaveLootChallenge.Item item, int x, int y, int width, int height) {
        BufferedImage itemImage = getItemImage(item.getImageType());
        
        if (itemImage != null) {
            // For sprite sheets, we need to extract a specific item based on type
            BufferedImage specificItem = extractSpecificItem(itemImage, item.getName());
            g.drawImage(specificItem, x, y, width, height, null);
        } else {
            // Fallback if image couldn't be loaded
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, width, height);
        }
    }
    
    /**
     * Gets the image for a specific item type, loading it if necessary
     * 
     * @param imageType The type of image to load
     * @return The loaded image or null if it couldn't be loaded
     */
    private static BufferedImage getItemImage(String imageType) {
        // Check if the image is already cached
        if (imageCache.containsKey(imageType)) {
            return imageCache.get(imageType);
        }
        
        // Determine the path based on image type
        String imagePath;
        switch (imageType) {
            case "Resources":
                imagePath = RESOURCES_PATH;
                break;
            case "Dungeon_Props":
                imagePath = DUNGEON_PROPS_PATH;
                break;
            case "Esoteric":
                imagePath = ESOTERIC_PATH;
                break;
            case "Tools":
                imagePath = TOOLS_PATH;
                break;
            default:
                imagePath = RESOURCES_PATH; // Default to resources
        }
        
        // Load the image with better error handling
        try {
            File imageFile = new File(imagePath);
            System.out.println("Loading item image from: " + imageFile.getAbsolutePath());
            System.out.println("File exists: " + imageFile.exists());
            
            if (!imageFile.exists()) {
                System.out.println("WARNING: Image file does not exist: " + imagePath);
                return null;
            }
            
            BufferedImage image = ImageIO.read(imageFile);
            if (image == null) {
                System.out.println("WARNING: ImageIO.read returned null for: " + imagePath);
                return null;
            }
            
            System.out.println("Successfully loaded image: " + imagePath);
            imageCache.put(imageType, image);
            return image;
        } catch (IOException e) {
            System.out.println("Could not load image: " + imagePath + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected error loading image: " + imagePath + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Extracts a specific item from a sprite sheet based on the item name
     * 
     * @param spriteSheet The sprite sheet to extract from
     * @param itemName The name of the item to extract
     * @return The extracted item image
     */
    private static BufferedImage extractSpecificItem(BufferedImage spriteSheet, String itemName) {
        // The sprite sheets are organized in a grid, so we need to determine
        // which cell to extract based on the item name
        
        // For simplicity, we'll use a deterministic approach based on the item name
        // In a real game, you would have specific coordinates for each item
        
        int itemsPerRow = spriteSheet.getWidth() / DEFAULT_ITEM_SIZE;
        int itemsPerColumn = spriteSheet.getHeight() / DEFAULT_ITEM_SIZE;
        
        // Use the hash code of the item name to determine a position
        int hashCode = Math.abs(itemName.hashCode());
        int row = (hashCode % itemsPerColumn);
        int col = ((hashCode / itemsPerColumn) % itemsPerRow);
        
        // Calculate the coordinates
        int x = col * DEFAULT_ITEM_SIZE;
        int y = row * DEFAULT_ITEM_SIZE;
        
        // Make sure we don't go out of bounds
        x = Math.min(x, spriteSheet.getWidth() - DEFAULT_ITEM_SIZE);
        y = Math.min(y, spriteSheet.getHeight() - DEFAULT_ITEM_SIZE);
        
        // Extract the specific item
        return spriteSheet.getSubimage(x, y, DEFAULT_ITEM_SIZE, DEFAULT_ITEM_SIZE);
    }
}