$ErrorActionPreference = "Stop"
New-Item -ItemType Directory -Force out | Out-Null
$sources = Get-ChildItem -Recurse src\main\java -Filter *.java | ForEach-Object { $_.FullName }
javac -Xlint:all -d out $sources
java -cp out com.studentmanagement.Main
