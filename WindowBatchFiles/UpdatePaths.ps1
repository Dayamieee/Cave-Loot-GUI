# PowerShell script to update file paths in Java source files after reorganization

Write-Host "Updating file paths in Java source files..."

# Function to update paths in a file
function Update-Paths {
    param (
        [string]$filePath
    )
    
    Write-Host "Processing $filePath"
    
    # Read the file content
    $content = Get-Content -Path $filePath -Raw
    
    # Update image paths
    $content = $content -replace 'c:\\CaveGame\\Background\\', 'c:\\CaveGame\\assets\\Background\\'
    $content = $content -replace 'c:\\CaveGame\\images\\', 'c:\\CaveGame\\assets\\images\\'
    $content = $content -replace 'c:\\CaveGame\\Sprites\\', 'c:\\CaveGame\\assets\\Sprites\\'
    $content = $content -replace 'c:\\CaveGame\\Sprites Assets\\', 'c:\\CaveGame\\assets\\Sprites Assets\\'
    
    # Update relative paths
    $content = $content -replace '"images/', '"../../assets/images/'
    $content = $content -replace '"Background/', '"../../assets/Background/'
    $content = $content -replace '"Sprites/', '"../../assets/Sprites/'
    $content = $content -replace '"Sprites Assets/', '"../../assets/Sprites Assets/'
    
    # Write the updated content back to the file
    Set-Content -Path $filePath -Value $content
}

# Update paths in all Java files in games directory
if (Test-Path -Path "src\games") {
    $gameFiles = Get-ChildItem -Path "src\games" -Filter "*.java" -Recurse
    foreach ($file in $gameFiles) {
        Update-Paths -filePath $file.FullName
    }
}

# Update paths in all Java files in utils directory
if (Test-Path -Path "src\utils") {
    $utilFiles = Get-ChildItem -Path "src\utils" -Filter "*.java" -Recurse
    foreach ($file in $utilFiles) {
        Update-Paths -filePath $file.FullName
    }
}

Write-Host "Path updates complete!"