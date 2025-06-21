# PowerShell script to update run scripts after reorganization

Write-Host "Updating run scripts..."

# Function to update a batch file
function Update-BatchFile {
    param (
        [string]$filePath,
        [string]$javaFile,
        [string]$additionalFiles = ""
    )
    
    # Check if the file exists
    if (-not (Test-Path -Path $filePath)) {
        Write-Host "Warning: $filePath does not exist. Skipping..."
        return
    }
    
    Write-Host "Processing $filePath"
    
    # Read the file content
    $content = Get-Content -Path $filePath -Raw
    
    # Update javac command
    if ($additionalFiles -eq "") {
        $content = $content -replace "javac\s+($javaFile)", "javac -cp . -d ..\bin ..\src\games\$1"
        $content = $content -replace "javac -cp . ($javaFile)", "javac -cp . -d ..\bin ..\src\games\$1"
    } else {
        $content = $content -replace "javac\s+($javaFile)\s+($additionalFiles)", "javac -cp . -d ..\bin ..\src\games\$1 ..\src\utils\$2"
        $content = $content -replace "javac -cp . ($javaFile)\s+($additionalFiles)", "javac -cp . -d ..\bin ..\src\games\$1 ..\src\utils\$2"
    }
    
    # Update java command
    $content = $content -replace "java\s+([^\s.]+)", "java -cp ..\bin $1"
    $content = $content -replace "java -cp . ([^\s.]+)", "java -cp ..\bin $1"
    
    # Write the updated content back to the file
    Set-Content -Path $filePath -Value $content
}

# Check if scripts directory exists
if (-not (Test-Path -Path "scripts")) {
    Write-Host "Creating scripts directory..."
    New-Item -ItemType Directory -Path "scripts" -Force
}

# Update specific batch files
$batchFiles = @{
    "RunSimpleMarioGame.bat" = @{"javaFile" = "SimpleMarioGame.java"; "additionalFiles" = ""}
    "RunMarioGame.bat" = @{"javaFile" = "SuperMarioGame.java"; "additionalFiles" = ""}
    "RunMarioGameDebug.bat" = @{"javaFile" = "SuperMarioGame.java"; "additionalFiles" = ""}
    "RunCaveLootChallenge.bat" = @{"javaFile" = "CaveLootChallenge.java"; "additionalFiles" = "ItemRenderer.java"}
    "RunImageTest.bat" = @{"javaFile" = "ImageLoadTest.java"; "additionalFiles" = ""}
    "RunGame.bat" = @{"javaFile" = "CaveLootChallenge.java"; "additionalFiles" = "ItemRenderer.java"}
}

foreach ($batchFile in $batchFiles.Keys) {
    # Check if the batch file is in the root directory
    if (Test-Path -Path $batchFile) {
        # Move it to the scripts directory if it's not already there
        Move-Item -Path $batchFile -Destination "scripts\$batchFile" -Force
        Write-Host "Moved $batchFile to scripts directory"
    }
    
    # Update the batch file in the scripts directory
    Update-BatchFile -filePath "scripts\$batchFile" -javaFile $batchFiles[$batchFile]["javaFile"] -additionalFiles $batchFiles[$batchFile]["additionalFiles"]
}

Write-Host "Run script updates complete!"