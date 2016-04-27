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