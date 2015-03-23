# -*- coding:utf-8 -*-
'''
Created on 2015年3月23日

@author: wanhao01
'''
import os


def remove_recrusive(folder):
    size = 0.0
    count = 0
    for root, dirs, files in os.walk(folder):
        for f in files:
            if('(1)' in f or '(2)' in f):
                filename = root + os.sep + f
                print(filename)
                
                try:
                    size = size + os.path.getsize(root + os.sep + f)
                    count = count + 1
                    os.remove(filename)
                except:
                    continue
    return size, count          


if __name__ == '__main__':
    size, count = remove_recrusive('D:\document\onedrive')
    print('total file size: ' + str(size / 1048576.0) + 'MB')
    print('there are ' + str(count) + ' files deleted.')
    pass