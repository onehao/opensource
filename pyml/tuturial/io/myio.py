# -*- coding: utf-8 -*-
'''
程序：python tutorial for IO.
版本：0.1
@author: Michael Wan
日期：2014-12-09
语言：Python 2.7
@keyword str(),repr(): 
'''

def FancierOutputFormatting():
    mystr = "hello world"
    print(str(mystr))
    print(repr(mystr))
    print(str(1.0/7.0))
    print(repr(1.0/7.0))
    x = 10 * 3.25
    y = 200 * 200
    print(str(x) + '_' + str(y))
    print(repr(x) + '_' + repr(y))
    hello = 'hello, world\n'
    print(hello)
    print(repr(hello))
    print(str(hello))
    annomyobject = (x, y, ('spam', 'eggs'))
    print(str(annomyobject))
    print(repr(annomyobject))
    
    

FancierOutputFormatting()   