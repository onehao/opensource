# -*- coding:utf-8 -*-
################################################################################
#
# Copyright (c) 2015 Baidu.com, Inc. All Rights Reserved
#
################################################################################
'''
Created on 2015年3月12日

@author: wanhao01
'''

from Queue import PriorityQueue

from crawler.minispider.SpiderHtmlParser import SpiderHtmlParser


class Spiderhandler():
    '''
    crawling related logics.
    '''
    def __init__(self):
        self.htmlparser = SpiderHtmlParser()
        self.tempQueue = []
    
    def crawl_urls(self, urlQueue, target_url, output_directory, max_depth=1, 
                   crawl_interval=1, crawl_timeout=1,  thread_count=1):
        '''
        crawling using the DFS way.
        '''
        if(max_depth < 0 or len(urlQueue) == 0):
            return
        
        while(len(urlQueue) > 0):
            urls = self.htmlparser.parse_url(urlQueue.pop(), output_directory, target_url, crawl_interval, crawl_timeout)
            if(max_depth > 0):
                for url in urls:
                    self.tempQueue.append(url)
        
        urlQueue = self.tempQueue
        self.tempQueue = []
        max_depth = max_depth - 1
        
        self.crawl_urls(urlQueue, target_url, output_directory, max_depth, 
                   crawl_interval, crawl_timeout,  thread_count)
