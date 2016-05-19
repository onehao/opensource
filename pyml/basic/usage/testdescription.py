# -*- coding: utf-8 -*-
'''
Created on 2016年5月18日

@author: michael.wh
'''
import unittest
from unittest.runner import TextTestResult
import pytest
import sys
# sys.path.append('../plugins')
# pytest_plugins = "report"

TextTestResult.getDescription = lambda _, test: test.shortDescription()
class DemoTest(unittest.TestCase):
    def shortDescription(self):
        return unittest.TestCase.shortDescription(self)
    
    @pytest.mark.p1
    def test_foo(self):
        """Test foo this is michael"""
        test = "中文‘”".decode('US-ASCII',errors='ignore').encode('US-ASCII', errors='ignore')
        print(test)
        self.assertTrue(False)
        pass
    
    @pytest.mark.p1
    def test_foo2(self):
        self.assertTrue(False)
        pass
    
    @pytest.mark.p1
    def test_foo3(self):
        """Test foo this 
        is michael, 
        hah
        """
        self.assertTrue(False)
        pass