$path = 'HKLM:\SOFTWARE\Microsoft\Microsoft SQL Server\MSSQL17.MSSQLSERVER\MSSQLServer\SuperSocketNetLib\Tcp'

# 启用TCP/IP
Set-ItemProperty -Path $path -Name Enabled -Value 1

# 遍历IP配置，启用+设置端口1433
$ips = Get-ChildItem $path
foreach ($ip in $ips) {
    $ipPath = $ip.PSPath
    $enabled = (Get-ItemProperty -Path $ipPath -Name Enabled -ErrorAction SilentlyContinue).Enabled
    if ($enabled -ne $null) {
        Set-ItemProperty -Path $ipPath -Name Enabled -Value 1
        Set-ItemProperty -Path $ipPath -Name TcpPort -Value '1433'
        Write-Output ("  IP: " + $ip.Name + " → Enabled=1, Port=1433")
    }
}

Write-Output "TCP/IP Enabled: " + (Get-ItemProperty -Path $path -Name Enabled).Enabled

# 重启SQL Server服务
Restart-Service -Name MSSQLSERVER -Force
Write-Output "SQL Server restarted"
