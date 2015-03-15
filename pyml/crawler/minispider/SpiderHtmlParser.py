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
from urlparse import urljoin
from urlparse import urlparse

from crawler.minispider import logerror
from crawler.minispider.SpiderFileUtils import SpiderFileUtils


class SpiderHtmlParser(SGMLParser):
    '''
    the class used to handle the HTML parsing
    related operations.
    '''
    
    def __init__(self):
        self.filehandler = SpiderFileUtils()
        self.urls = [] 
        self.codes = [200, 201, 302, 301]
        SGMLParser.__init__(self)
    
    def start_a(self, attrs):
        '''
        parsing elements <a></a>
        ''' 
        href = [v for k, v in attrs if k == 'href'] 
        if href: 
            self.urls.extend(href) 

    def parse_url(self, url, outputdir, target_url, sleep=1, timeout=1): 
        '''
        parse the $url and the save the content url content
        to the $outputdir which match the $target_url patten. 
        '''  
        import socket
        socket.setdefaulttimeout(timeout)
        try:   
            response = urllib.urlopen(url)
            if(not response.code in self.codes):
                return []
            data = response.read() 
            
            urls = self.__parse_subpage_url(data, url, target_url)
            
            urlpattern = re.compile(target_url, re.DOTALL)
            match = urlpattern.findall(url)
            if(len(match) > 0):
                self.__save_page(data, url, outputdir)
            
            time.sleep(sleep)
             
            print(data)
        except IOError as ioex:
            logerror('[' + ioex.errorno + '] ' + ioex.strerror)  
        except Exception as ex:   
            logerror(ex.message)  
        return urls
     
    def __save_page(self, data, url, outputdir):
        '''
        save the page content with the specific url to the local path.
        '''
        if(not os.path.exists(outputdir)):
            os.makedirs(outputdir)
        filename = self.__validate_name(url)
        f = open(outputdir + os.sep + filename, 'w')
        f.writelines(data)
        f.close()
#     
    
    def __validate_name(self, url):
        '''
        \/:*?"<> are not valid file name, so convert and make the url suit for file name.
        on Linux and Windows the max length of file name is 260 along with the extension.
        '''
        url = urllib.quote(url)
        url = url.replace('\\', '')
        url = url.replace('/', '_')
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
        
    def __parse_subpage_url(self, data, url, target_url):
        '''
        get the url path from the specific $url, used for the crawling of the next iteration.
        '''
        lister = SpiderHtmlParser()
        lister.feed(data)
        
        #need to eliminate the duplicated urls
        rawurls = lister.urls
        urls = []
        
        for rawurl in rawurls:
            rawurl = rawurl.replace('javascript:location.href=\"', '')
            rawurl = rawurl.replace('\"', '')
            o = urlparse(rawurl)
            
            #if 0 then the url is a relative path
            if(len(o.netloc) == 0):
                urls.append(urljoin(url, rawurl))
            else:
                urls.append(rawurl)
        
        rawurls = []
        '''
        urlpattern = re.compile(target_url, re.DOTALL)
        
        urls = []
        for url in rawurls:
            match = urlpattern.findall(url)
            if(len(match) > 0):
                urls.append(url)
        '''
    
        return urls

if __name__ == '__main__':
    #parse_url('http://pycm.baidu.com:8081/2/3/index.html')
    response = urllib.urlopen('http://pycm.baidu.com:8081/1/page1_12.html')
    o = urlparse('http://pycm.baidu.com:8081/2/index.html')
    o2 = urlparse('3/index.html')
    up = urljoin('http://pycm.baidu.com:8081/2/index.html', '3/index.html')
    up2 = urljoin('http://pycm.baidu.com:8081/2/index.html', '/page2_1.html')
    print(up2)
    pass