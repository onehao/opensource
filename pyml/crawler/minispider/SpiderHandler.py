# -*- coding:utf-8 -*-
'''
Created on 2015年3月12日

@author: wanhao01
'''
import sys
sys.path.append('../..')
from crawler.minispider.SpiderHtmlParser import SpiderHtmlParser
from crawler.minispider.SpiderThreadPool import ThreadPool


class Spiderhandler(object):
    '''
    crawling related logics.
    '''
    def __init__(self):
        #self.tp = ThreadPool(100)
        self.htmlparser = SpiderHtmlParser()
        self.tempQueue = []
    
    def crawl_urls(self, urlQueue, config, max_depth):
        '''
        crawling using the DFS way.
        '''
        if(max_depth < 0 or len(urlQueue) == 0):
            return
        self.tp = ThreadPool(config.getThreadCount())
        while(len(urlQueue) > 0):
            try:
                #urls = self.htmlparser.parse_url(urlQueue.pop(), output_directory, target_url, crawl_interval, crawl_timeout)
                self.tp.add_job(self.htmlparser.parse_url, urlQueue.pop(), config.getOutputDir(), 
                                config.getTargetUrlPattern(), config.getCrawlInterval(), 
                                config.getTimeOut())
class Spiderhandler():
    '''
    crawling related logics.
    '''
    def __init__(self):
        #self.tp = ThreadPool(100)
        self.htmlparser = SpiderHtmlParser()
        self.tempQueue = []
    
    def crawl_urls(self, urlQueue, config, max_depth):
        '''
        crawling using the DFS way.
        '''
        if(max_depth < 0 or len(urlQueue) == 0):
            return
        self.tp = ThreadPool(config.getThreadCount())
        while(len(urlQueue) > 0):
            try:
                #urls = self.htmlparser.parse_url(urlQueue.pop(), output_directory, target_url, crawl_interval, crawl_timeout)
                self.tp.add_job(self.htmlparser.parse_url, urlQueue.pop(), config.getOutputDir(), 
                                config.getTargetUrlPattern(), config.getCrawlInterval(), config.getTimeOut())
                 
            except Exception as ex:
                continue
            
        self.tp.wait_for_complete() 
        
        if(max_depth > 0):
            while(not self.tp.resultQueue.empty()):
                for url in self.tp.resultQueue.get():
                    self.tempQueue.append(url)
        
        urlQueue = self.tempQueue
        self.tempQueue = []
        max_depth = max_depth - 1
        
        self.crawl_urls(urlQueue, config, max_depth)
