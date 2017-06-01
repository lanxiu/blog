
/etc/strongswan/ipsec.conf
/etc/ipsec.conf


export COMPROUTERIP="120.55.241.200"
export VPNROUTERIP="212.234.160.12"
export PRESHAREDKEY="HIV3k3y4ZT3@4s$c4cx"
export ETSITUN="10.101.26.1"
#ETSIMASK
export COMPTUN="10.101.26.2"
#COMPMASK
export ETSINEIGH="10.101.26.1"
export ETSIAS="65002"
export COMPAS="65026"
export COMPTESTNETWORK="192.168.13.20"

#IPSEC.CONF
conn %default
 ikelifetime=14400s
 keylife=60m
 rekeymargin=3m
 keyingtries=%forever
 keyexchange=ikev1
 authby=secret
conn etsi
 auto=add
 left=120.55.241.200
 leftid=120.55.241.200
 leftsubnet=120.55.241.200/32
 #leftfirewall=yes
 right=212.234.160.12
 rightsubnet=212.234.160.12/32
 rightid=212.234.160.12
 ike=aes256-sha256-modp1536!
 esp=3des-sha256-modp1536!
 leftprotoport=47
 rightprotoport=47
 compress=no


conn android
    keyexchange=ikev1
    aggressive=yes  
    left=120.55.241.200
    leftid=@tyr.so
    leftauth=psk
    leftsubnet=0.0.0.0/0
    right=%any
    rightauth=psk
    rightauth2=xauth
    rightsourceip=192.18.13.0/24
    auto=add


conn ios
	keyexchange=ikev1
	left=120.55.241.200
	leftauth=psk
	leftsubnet=0.0.0.0/0
	right=223.104.147.65
	rightauth=psk
	rightsourceip=192.18.15.0/24
	auto=add


#IPSEC.CONF
conn %default
 ikelifetime=14400s
 keylife=60m
 rekeymargin=3m
 keyingtries=%forever
 keyexchange=ikev1
 authby=secret
conn etsi
 auto=add
 left=$COMPROUTERIP
 leftid=$COMPROUTERIP
 leftsubnet=$COMPROUTERIP/32
 #leftfirewall=yes
 right=$VPNROUTERIP
 rightsubnet=$VPNROUTERIP/32
 rightid=$VPNROUTERIP
 ike=aes256-sha256-modp1536!
 esp=3des-sha256-modp1536!
 leftprotoport=47
 rightprotoport=47
 compress=no

#ipsec.secrets
$COMPROUTERIP $VPNROUTERIP : PSK $PRESHAREDKEY
120.55.241.200 122.96.41.205 : PSK "1234"

120.55.241.200 223.104.147.65 : PSK "1234"
test : XAUTH "test"
120.55.241.200  %any : PSK "1234"
@aa @bb : PSK "1234"

#ipsec.secrets
120.55.241.200 212.234.160.12 : PSK "HIV3k3y4ZT3@4s$c4cx"

sudo iptunnel add tun_etsi mode gre local $COMPROUTERIP remote $VPNROUTERIP
sudo ifconfig tun_etsi $COMPTUN pointopoint $ETSITUN mtu 1420

sudo iptunnel add tun_etsi mode gre local 120.55.241.200 remote 212.234.160.12
sudo ifconfig tun_etsi 10.101.26.2 pointopoint 10.101.26.1 mtu 1420
 ifconfig eth0:0 192.168.13.20 pointopoint 10.101.26.2 mtu 1420
 ifconfig eth0:1 192.168.15.20 pointopoint 10.101.26.2 mtu 1420
 ifconfig eth0:0 192.168.13.20 netmask 255.255.255.0 broadcast 192.168.13.255
 ifconfig eth0:1 192.168.15.20 netmask 255.255.255.0 broadcast 192.168.15.255
 ifconfig eth0:2 192.168.15.21 netmask 255.255.255.0 broadcast 192.168.15.255
 ifconfig eth0:3 192.168.15.22 netmask 255.255.255.0 broadcast 192.168.15.255
 ifconfig eth0:4 192.168.15.23 netmask 255.255.255.0 broadcast 192.168.15.255


route add 127.0.0.1 mask 255.255.255.255 10.42.20.129 metric 1
route add 10.42.20.162 mask 255.255.255.255 10.42.20.129 metric 1
route add 10.42.20.162 mask 255.255.255.255 10.42.20.129 metric 1
route add 10.42.20.162 mask 255.255.255.255 10.42.20.129 metric 1
route delete 10.42.20.162 mask 255.255.255.255 10.42.20.129 metric 1
route delete 10.42.20.162 mask 255.255.255.255 10.42.20.129 metric 1
route delete 10.42.20.162 mask 255.255.255.255 10.42.20.129 metric 1
route delete 10.42.20.162 mask 255.255.255.255 10.42.20.129 metric 1




 #ifconfig local_tun 192.168.13.20 pointopoint 10.101.26.2 mtu 1420

yum install bird

#bird.conf
log syslog all;
router id $COMPTUN;
listen bgp address $COMPTUN;
function is_local_net() {
 return net ~ [
 $COMPTESTNETWORK{24,32}
 ];
}
protocol kernel {
persist; # Don't remove routes on bird shutdown
scan time 20; # Scan kernel routing table every 20 seconds
export all; # Default is export none
}
protocol device {
scan time 10; # Scan interfaces every 10 seconds
}
protocol static static_bgp{
route $COMPTESTNETWORK via $COMPTUN;
}
protocol bgp {
description "ETSI Plugtests BGP Link";
local as $COMPAS;
neighbor $ETSINEIGH as $ETSIAS;
gateway direct;
export where proto = "static_bgp";
import filter {
 # accept every subnet, except our own advertised subnet
 if !is_local_net() then {
 accept;
 }
 reject;
 };
next hop self;
}

sudo ipsec start
sudo ipsec up etsi
sudo ipsec reload

sudo ip route add 10.100.0.0/16 dev tun_etsi
sudo ip route add 10.101.0.0/16 dev tun_etsi
sudo ip route add 192.168.170.0/24 dev tun_etsi

log syslog all;
router id 10.101.26.2;
listen bgp address 10.101.26.2;
function is_local_net() {
 return net ~ [
 192.168.13.0 /24{24,32}
 ];
}
protocol kernel {
persist; # Don't remove routes on bird shutdown
scan time 20; # Scan kernel routing table every 20 seconds
export all; # Default is export none
}
protocol device {
scan time 10; # Scan interfaces every 10 seconds
}
protocol static static_bgp{
route 192.168.13.0 via 10.101.26.2;
}
protocol bgp {
description "ETSI Plugtests BGP Link";
local as 65026;
neighbor 10.101.26.1 as 65002;
gateway direct;
export where proto = "static_bgp";
import filter {
 # accept every subnet, except our own advertised subnet
 if !is_local_net() then {
 accept;
 }
 reject;
 };
next hop self;
}


