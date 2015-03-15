# -*- coding:utf-8 -*-
################################################################################
#
# Copyright (c) 2015 Baidu.com, Inc. All Rights Reserved
#
################################################################################
'''
Created on 2015年3月14日

@author: Administrator
'''

import os
import unittest
import urllib
from urlparse import urljoin
import urlparse

from crawler.minispider.SpiderConfigParser import SpiderConfig
from crawler.minispider.SpiderHtmlParser import SpiderHtmlParser


class TestSequenceFunctions(unittest.TestCase):
    '''
    as for D level project, not exhausting all the possible exception attributes.
    '''

    def setUp(self):
        self.parser = SpiderHtmlParser()
        self.path = os.path.realpath(__file__)
        
    def tearDown(self):
        for filename in os.listdir(self.path[0:-23] + 'urls'):
            os.remove(self.path[0:-23] + 'urls' + os.sep + filename)
        
    def test_parse_url(self):
        '''
        test the logic of parse_url
        '''
        
        urls = self.parser.parse_url('http://pycm.baidu.com:8081/2/index.html',
                               './urls', '.*\.(gif|png|jpg|bmp|html)$',
                                1, 1)
        #http://pycm.baidu.com:8081/page2_1.html', 'http://pycm.baidu.com:8081/2/3/index.html'
        self.assertIn("http://pycm.baidu.com:8081/page2_1.html", urls)
        self.assertIn("http://pycm.baidu.com:8081/2/3/index.html", urls)
        self.assertTrue(os.path.exists(self.path[0:-23] + 'urls' + os.sep
                                         + 'http%3A__pycm.baidu.com%3A8081_2_index.html'), 
                        'http%3A__pycm.baidu.com%3A8081_2_index.html expecting to be created.')
        
    def test_parse_url_B(self):
        '''
        test the logic of parse_url
        '''
        
        urls = self.parser.parse_url('http://pycm.baidu.com:8081',
                               './urls', '.*\.(gif|png|jpg|bmp|html)$',
                                1, 1)
        #http://pycm.baidu.com:8081/page2_1.html', 'http://pycm.baidu.com:8081/2/3/index.html'
        self.assertEqual(5, len(urls), 'there should be 5 urls.')
        self.assertIn("http://pycm.baidu.com:8081/page1.html", urls)
        self.assertIn("http://pycm.baidu.com:8081/page2.html", urls)
        self.assertIn("http://pycm.baidu.com:8081/page3.html", urls)
        self.assertIn("http://pycm.baidu.com:8081/mirror/index.html", urls)
        self.assertIn("http://pycm.baidu.com:8081/page4.html", urls)
    
    def test_parse_url_404(self):
        '''
        test the logic of parse_url with the page 404
        and the exception should be thrown
        '''
        
        urls = self.parser.parse_url('http://pycm.baidu.com:8081/2/index333.html',
                               './urls', '.*\.(gif|png|jpg|bmp|html)$',
                                1, 1)
        #http://pycm.baidu.com:8081/page2_1.html', 'http://pycm.baidu.com:8081/2/3/index.html'
        self.assertTrue(len(urls) == 0, 'should not contain any element.')

if __name__ == '__main__':
    unittest.main()