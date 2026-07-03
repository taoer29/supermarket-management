$path = 'HKLM:\SOFTWARE\Microsoft\Microsoft SQL Server\MSSQL17.MSSQLSERVER\MSSQLServer'

# 切换为混合认证模式 (1=Windows only, 2=Mixed)
Set-ItemProperty -Path $path -Name LoginMode -Value 2
$mode = (Get-ItemProperty -Path $path -Name LoginMode).LoginMode
Write-Output "LoginMode: $mode (2 = Mixed Mode)"

# 重启SQL Server
Restart-Service -Name MSSQLSERVER -Force
Write-Output "SQL Server restarted"
