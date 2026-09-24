$ErrorActionPreference = "Stop"
New-Item -ItemType Directory -Force out | Out-Null
$sources = Get-ChildItem -Recurse src\main\java -Filter *.java | ForEach-Object { $_.FullName }
javac -Xlint:all -d out $sources
if ($args -contains "desktop") {
  java -cp out com.studentmanagement.DesktopMain
} else {
  java -cp out com.studentmanagement.Main
}
