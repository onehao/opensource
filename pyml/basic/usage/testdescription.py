# -*- coding: utf-8 -*-
'''
Created on 2016年5月18日

@author: michael.wh
'''
import unittest
from unittest.runner import TextTestResult
import pytest

TextTestResult.getDescription = lambda _, test: test.shortDescription()
class DemoTest(unittest.TestCase):
    def shortDescription(self):
        return unittest.TestCase.shortDescription(self)
    
    @pytest.mark.p1
    def test_foo(self):
        """Test foo this is michael"""
        self.assertTrue(False)
        pass