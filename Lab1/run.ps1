param(
    [switch]$Console,
    [string]$Student = 'Andrei Toma'
)
$ErrorActionPreference = 'Stop'
Push-Location $PSScriptRoot
try {
    New-Item -ItemType Directory -Force -Path out | Out-Null
    $sources = @(Get-ChildItem -LiteralPath src -Filter '*.java' | ForEach-Object FullName)
    & javac -encoding UTF-8 -d out $sources
    if ($LASTEXITCODE -ne 0) { throw 'Compilarea a esuat.' }
    if ($Console) {
        & java -cp out Main --console $Student
    } else {
        & java -cp out Main $Student
    }
    if ($LASTEXITCODE -ne 0) { throw 'Programul s-a terminat cu eroare.' }
} finally {
    Pop-Location
}
