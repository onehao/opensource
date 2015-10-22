# -*- coding: utf-8 -*-
'''
程序：focus on Mi1 on ganji & 58
版本：0.1
@author: Michael Wan
日期：2015.02.16
语言：Python 2.7
操作：输入带分页的地址，去掉最后面的数字，设置一下起始页数和终点页数。
功能：下载对应页码内的所有页面并存储为html文件。
@see: http://blog.csdn.net/pleasecallmewhy/article/details/8927832
'''
 
import re
import string, urllib2
import sys
import time
import crawler

test = 'a abc abcd abcdef'
matches = re.finditer(r'(\w+)\s+(\w+)', test)
results = [str(match.group(1)) for match in matches]
print(results)
