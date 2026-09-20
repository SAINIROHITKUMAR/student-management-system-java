$ErrorActionPreference = "Stop"
Remove-Item -Recurse -Force out-test -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force out-test | Out-Null
$sources = Get-ChildItem -Recurse src\main\java,src\test\java -Filter *.java | ForEach-Object { $_.FullName }
javac -Xlint:all -d out-test $sources
java -cp out-test com.studentmanagement.StudentManagementTest
