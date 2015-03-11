# -*- coding:utf-8 -*-
################################################################################
#
# Copyright (c) 2015 Baidu.com, Inc. All Rights Reserved
#
################################################################################
'''
Created on 2015年3月3日

@author: wanhao01
'''

import argparse
import sys

from crawler.minispider import logerror
from crawler.minispider import loginfo
from crawler.minispider.SpiderConfigParser import SpiderConfig
from crawler.minispider.SpiderConfigParser import NoConfigError

reload(sys)
sys.setdefaultencoding('utf-8')
parser = argparse.ArgumentParser(prog='mini_spider.py',
                                 description = 'mini_spider.py，using the BFS，to save the ' + 
                                               'web page matching the specific pattern to local',
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

options, _ = parser.parse_known_args()
args = parser.parse_args()

def initConfig():
    '''
    initialize the configuration file.
    '''
    configName = "spider.conf"
    if args.config:
        configName = args.config
    loginfo(configName) 
    spiderConfig = SpiderConfig(configName)
    try:
        spiderConfig.loadConfigFile()
    except NoConfigError as error:
        logerror(str(error))
        logerror(str(type(error)) + ',' + error.message + 
                 ', the program will be exit, please check the config file.')
        return 
    return spiderConfig


def main():
    '''
    the main function.
    '''
    print(spiderConfig.getCrawlInterval())
    print('finished')
