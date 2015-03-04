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
import ConfigParser
import sys


reload(sys)
sys.setdefaultencoding('utf-8')
parser = argparse.ArgumentParser(prog='mini_spider.py',
                                 description = 'mini_spider.py，using the BFS，to save the web page ' + 
                                               'matching the specific pattern to local',
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


class SpiderConfig(object):
    '''
    spider configuration file encapsulation.
    '''
    __url_list_file = ''
    def __init__(self, configName):
        '''
        Constructor
        '''
        __spiderSection = 'spider'
        cf = ConfigParser.ConfigParser()
        cf.read(configName)
        self.__url_list_file = cf.get(__spiderSection, 'url_list_file')
        self.__output_dir = cf.get(__spiderSection, 'output_directory')
        self.__max_depth = cf.get(__spiderSection, 'max_depth')
        self.__crawl_interval = cf.get(__spiderSection, 'crawl_interval')
        self.__crawl_timeout = cf.get(__spiderSection, 'crawl_timeout')
        self.__target_url = cf.get(__spiderSection, 'target_url')
        self.__thread_count = cf.get(__spiderSection, 'thread_count')
    
    def getUrlListFile(self):
        '''
        get the seed dir.
        '''
        return self.__url_list_file
    
    def getOutputDir(self):
        '''
        path to store the output the result.
        '''
        return self.__output_dir
    
    def getMaxDepth(self):
        '''
        the max crawl depth.
        '''
        return self.__max_depth
        
    def getCrawlInterval(self):
        '''
        ge the crawling interval.
        '''
        return self.__crawl_interval
        
    def getTimeOut(self):
        '''
        get timeout.
        '''
        return self.__crawl_timeout
    
    def getTargetUrlPattern(self):
        '''
        get the target url pattern.
        '''
        return self.__target_url
        
    def getThreadCount(self):
        '''
        get the max number of thread to crawl
        '''
        return self.__thread_count


def main():
    '''
    the main function.
    '''
    options, _ = parser.parse_known_args()
    args = parser.parse_args()
    configName = "spider.conf"
    if args.config:
        configName = args.config
    print(configName) 
    spiderConfig = SpiderConfig(configName)
