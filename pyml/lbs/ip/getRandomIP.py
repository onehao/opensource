#coding=utf-8  
'''
@author: Michael Wan
@since: 2014-11-13
'''
import httplib, urllib
import json
import random
import sys


reload(sys)
#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
sys.setdefaultencoding('utf8')

def getRandomIP():
    file_read = open('china.ip', 'r')
    list = file_read.readlines()
    print(list)
    file_write = open('random.ip', 'w')
    size = len(list) - 1
    for i in range(1, 100):
        r = random.randint(0, size)
        file_write.write(list[r])      
    
getRandomIP()