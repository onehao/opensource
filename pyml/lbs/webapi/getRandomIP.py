#coding=utf-8  
'''
@author: Michael Wan
@since: 2014-11-13
'''
import random


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