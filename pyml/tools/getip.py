#coding=utf-8 
'''
Created on 2016年4月27日

@author: michael.wh
'''

import socket
localIP = socket.gethostbyname(socket.gethostname())#这个得到本地ip 
print "local ip:%s "%localIP
ipList = socket.gethostbyname_ex(socket.gethostname())
for i in ipList:
    if i != localIP:
       print "external IP:%s"%i
       
#!/usr/bin/env python
import re,urllib2
class Get_public_ip:
    def getip(self):
        try:
            myip = self.visit("http://www.111cn.net/")
        except:
            try:
                myip = self.visit("http://www.ip138.com/ip2city.asp")
            except:
                myip = "So sorry!!!"
        return myip
    def visit(self,url):
        opener = urllib2.urlopen(url)
        if url == opener.geturl():
            str = opener.read()
        return re.search('d+.d+.d+.d+',str).group(0)
if __name__ == "__main__":
    getmyip = Get_public_ip()
    print getmyip.getip()