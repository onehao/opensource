# -*- coding: utf-8 -*-
'''
Created on 2016年6月13日

@author: michael.wh
'''

import unittest

import pytest

'''
proof from https://pytest.org/latest/unittest.html
Note
While pytest supports receiving fixtures via test function arguments for non-unittest test methods, 
unittest.TestCase methods cannot directly receive fixture function arguments as implementing that 
is likely to inflict on the ability to run general unittest.TestCase test suites. 
Maybe optional support would be possible, though. If unittest finally grows a plugin system that 
should help as well. In the meanwhile, the above usefixtures and autouse examples should help to 
mix in pytest fixtures into unittest suites. And of course you can also start to selectively 
leave away the unittest.TestCase subclassing, use plain asserts and get the unlimited pytest feature set.
'''

# works when outside the class.
def test_foo(ips):
    """Test foo this is michael"""
    test = "中文‘”".decode('US-ASCII', errors='ignore').encode('US-ASCII', errors='ignore')
    print(ips)
    assert(False)
    pass

class DemoTest(unittest.TestCase):
    def shortDescription(self):
        return unittest.TestCase.shortDescription(self)

    # doesn't work well with unittest.
    @pytest.mark.usefixtures('ips') 
    def test_foo(self, ips):
        """Test foo this is michael"""
        test = "中文‘”".decode('US-ASCII', errors='ignore').encode('US-ASCII', errors='ignore')
        print(ips)
        self.assertTrue(False)
        pass
    
class TestClass:
    def test_foo(self, ips):
        """Test foo this is michael"""
        test = "中文‘”".decode('US-ASCII', errors='ignore').encode('US-ASCII', errors='ignore')
        print(ips)
        assert(False)
        pass
    
    
