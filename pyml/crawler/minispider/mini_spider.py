# -*- coding:utf-8 -*-
################################################################################
#
# Copyright (c) 2015 Baidu.com, Inc. All Rights Reserved
#
################################################################################
'''
Created on 2015年3月2日

@author: wanhao01
'''
import argparse
import sys

reload(sys)
sys.setdefaultencoding('utf-8')
parser = argparse.ArgumentParser(prog='mini_spider.py',
                                 description = '迷你定向抓取器mini_spider.py，实现对种子链接的广度优先抓取，并把URL长相符合特定pattern的网页保存到磁盘上。',
                                 epilog='bug reports:\n  mail: wanhao01@baidu.com',
                                 formatter_class=argparse.RawTextHelpFormatter)

parser.add_argument('-v',
                    '--version',
                    action='store_true',
                    dest='version',
                    default=False,
                    help='show version')

parser.add_argument('-c',
                    '--config',
                    dest='config',
                    default=False,
                    help="specify the config file")

if __name__ == '__main__':
    options, _ = parser.parse_known_args()
    args = parser.parse_args()
    configName = "spider.conf"
    if args.config:
        configName = args.config
    print(configName)
    pass