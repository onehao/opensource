# -*- coding: utf-8 -*-
'''
Created on 2016年6月13日

@author: michael.wh
'''

import unittest

import pytest

#works when outside the class.
def test_foo(self,ips):
        """Test foo this is michael"""
        test = "中文‘”".decode('US-ASCII',errors='ignore').encode('US-ASCII', errors='ignore')
        print(ips)
        self.assertTrue(False)
        pass

class DemoTest(unittest.TestCase):
    def shortDescription(self):
        return unittest.TestCase.shortDescription(self)

    #doesn't work.
    def test_foo(self,ips):
        """Test foo this is michael"""
        test = "中文‘”".decode('US-ASCII',errors='ignore').encode('US-ASCII', errors='ignore')
        print(ips)
        self.assertTrue(False)
        pass
    
    