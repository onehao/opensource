# -*- coding:utf-8 -*-
'''
Created on 2015年3月2日

@author: wanhao01
'''

import sys

from crawler.minispider import logerror
import main


reload(sys)
sys.setdefaultencoding('utf-8')

if __name__ == '__main__':
    try:
        main.main()
    except Exception as exception:
        logerror("error during running, details: " + str(exception))
    pass