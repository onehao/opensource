# -*- coding:utf-8 -*-
'''
Created on 2015年3月23日

@author: wanhao01
'''
import os


def remove_recrusive(folder):
    for root, dirs, files in os.walk(folder):
        for f in files:
            if('(1)' in f or '(2)' in f):
                filename = root + os.sep + f
                print(filename)
                try:
                    os.remove(filename)
                except:
                    continue
                


if __name__ == '__main__':
    remove_recrusive('D:\document\onedrive')
    pass