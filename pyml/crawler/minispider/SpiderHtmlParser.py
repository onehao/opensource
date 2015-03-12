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

import os
import re
from sgmllib import SGMLParser
import time
import urllib

from crawler.minispider import logerror


class SpiderHtmlParser(SGMLParser):
    urls = []  
    def start_a(self, attrs):
        '''
        parsing elements <a></a>
        ''' 
        href = [v for k, v in attrs if k=='href'] 
        if href: 
            self.urls.extend(href) 

    def parse_url(self, url, outputdir, target_url, sleep=1, timeout=1): 
        '''
        test job 
        '''  
        import socket
        socket.setdefaulttimeout(timeout)
        try:   
            data = urllib.urlopen(url).read() 
            
            self.__save_page(data, url, outputdir)
            
            urls = self.__parse_subpage_url(data, url, target_url)
            
            time.sleep(sleep)
             
            print(data)
        except Exception as ex:   
            logerror(ex.Message)  
        return urls
    
    def __save_page(self, data, url, outputdir):
        '''
        save the page content with the specific url to the local path.
        '''
        if(not os.path.exists(outputdir)):
            os.makedirs(outputdir)
        filename = self.__validate_name(url)
        f = open(outputdir + os.sep + filename,'w')
        f.writelines(data)
        f.close()
    
    
    def __validate_name(self, url):
        '''
        \/:*?"<> are not valid file name, so convert and make the url suit for file name.
        on Linux and Windows the max length of file name is 260 along with the extension.
        '''
        url = urllib.quote(url)
        url = url.replace('\\', '')
        url = url.replace('/', '')
        url = url.replace(':', '')
        url = url.replace('*', '')
        url = url.replace('?', '')
        url = url.replace('\"', '')
        url = url.replace('<', '')
        url = url.replace('>', '')
        
        if(len(url) <= 255):
            return url
        else:
            return url[0:255]
        
    
    def __parse_subpage_url(self, data,url, target_url):
        '''
        get the url path from the specific $url, used for the crawling of the next iteration.
        '''
        lister = SpiderHtmlParser()
        lister.feed(data)
        
        #need to eliminate the duplicated urls
        rawurls = lister.urls

        
        urlpattern = re.compile(target_url, re.DOTALL)
        
        urls = []
        for url in rawurls:
            match = urlpattern.findall(url)
            if(len(match) > 0):
                urls.append(url)
                
        return urls

if __name__ == '__main__':
    #parse_url('http://pycm.baidu.com:8081/2/3/index.html')
    pass