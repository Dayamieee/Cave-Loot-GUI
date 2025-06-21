# PowerShell script to organize CaveGame directory

# Create main directories if they don't exist
Write-Host "Creating directory structure..."
New-Item -ItemType Directory -Path "bin", "scripts", "docs", "assets" -Force

# Create subdirectories
New-Item -ItemType Directory -Path "src\games", "src\utils", "assets\images", "assets\sprites" -Force

# Move Java source files to appropriate directories if they're not already there
Write-Host "Organizing source files..."
if (Test-Path -Path "src\*.java") {
    # Move Java files from src root to src\games
    Get-ChildItem -Path "src\*.java" -Exclude "ItemRenderer.java" | ForEach-Object {
        Move-Item -Path $_.FullName -Destination "src\games" -Force
    }
    
    # Move ItemRenderer.java to src\utils if it exists in src root
    if (Test-Path -Path "src\ItemRenderer.java") {
        Move-Item -Path "src\ItemRenderer.java" -Destination "src\utils" -Force
    }
}

# Move batch files to scripts directory
Write-Host "Moving batch files..."
Get-ChildItem -Path "*.bat" -Exclude "OrganizeProject.bat", "OrganizeFiles.bat", "UpdatePaths.bat", "UpdateRunScripts.bat" | ForEach-Object {
    Move-Item -Path $_.FullName -Destination "scripts" -Force
}

# Move class files to bin directory
Write-Host "Moving compiled class files..."
if (Test-Path -Path "class") {
    Get-ChildItem -Path "class\*.class" | ForEach-Object {
        Move-Item -Path $_.FullName -Destination "bin" -Force
    }
    # Remove the empty class directory if it exists
    if ((Get-ChildItem -Path "class" | Measure-Object).Count -eq 0) {
        Remove-Item -Path "class" -Force
    }
}

# Move documentation to docs directory
Write-Host "Moving documentation..."
if (Test-Path -Path "README.md") {
    Move-Item -Path "README.md" -Destination "docs" -Force
}

# Move image assets to assets directory
Write-Host "Moving image assets..."
if (Test-Path -Path "images") {
    Move-Item -Path "images" -Destination "assets" -Force
}
if (Test-Path -Path "Background") {
    Move-Item -Path "Background" -Destination "assets" -Force
}
if (Test-Path -Path "Sprites") {
    Move-Item -Path "Sprites" -Destination "assets" -Force
}
if (Test-Path -Path "Sprites Assets") {
    Move-Item -Path "Sprites Assets" -Destination "assets" -Force
}

# Create a new README.md in the root directory
$readmeContent = @"
# Cave Game Project

## Directory Structure
- **src/** - Source code files
  - **games/** - Game implementations
  - **utils/** - Utility classes
- **bin/** - Compiled class files
- **scripts/** - Batch files to run the games
- **docs/** - Documentation
- **assets/** - Game assets
  - **images/** - Player and tile images
  - **Background/** - Background images
  - **Sprites/** - Sprite sheets
  - **Sprites Assets/** - Additional sprite assets

## Games Included
1. **Cave Loot Challenge** - A treasure collecting game
2. **Simple Mario Game** - A simplified Mario-style platformer
3. **Super Mario Game** - An enhanced Mario-style platformer

## How to Run
Use the batch files in the scripts directory to run the games:
- RunCaveLootChallenge.bat - Run the Cave Loot Challenge game
- RunSimpleMarioGame.bat - Run the Simple Mario Game
- RunMarioGame.bat - Run the Super Mario Game
- RunImageTest.bat - Test image loading functionality
"@

Set-Content -Path "README.md" -Value $readmeContent

Write-Host "Organization complete! The CaveGame directory has been restructured."