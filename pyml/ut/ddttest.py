#coding=utf-8  
'''
Created on 2016年4月14日

@author: michael.wh
'''
import math
import unittest

from numpy.testing.utils import assert_equal

from ddt import ddt, data, unpack, file_data
from mycode import is_a_greeting

@ddt
class TestData(unittest.TestCase):
    @data([2, 2, 4],[2, 3, 8],[1, 9, 1],[0, 9, 0])
    @unpack
    def test_pow(self, base, exponent, expected):
        print("start")
        assert_equal(math.pow(base, exponent), expected)
        print("pass")
        
    @data([3, 2], [4, 3], [5, 3])
    @unpack
    def test_list_extracted_with_doc(self, first_value, second_value):
        """Extract into args with first value {} and second value {}"""
        print("start")
        self.assertTrue(first_value > second_value)
        
    @file_data('test_data_list.json')
    def test_file_data_list(self, value):
        self.assertTrue(is_a_greeting(value))
        
    @file_data("test_data_dict_dict.json")
    def test_file_data_dict_dict(self, start, end, value):
        self.assertLess(start, end)
        self.assertLess(value, end)
        self.assertGreater(value, start)
        
    @data(u'ascii', u'non-ascii-\N{SNOWMAN}')
    def test_unicode(self, value):
        self.assertIn(value, (u'ascii', u'non-ascii-\N{SNOWMAN}'))
        
        
        
    