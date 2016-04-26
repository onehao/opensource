#coding=utf-8  
'''
Created on 2016年4月14日

@author: michael.wh
'''
import math
import unittest

from numpy.testing.utils import assert_equal

from ddt import ddt, data, unpack, file_data

@ddt
class TestData(unittest.TestCase):
    @data([2, 2, 4],[2, 3, 8],[1, 9, 1],[0, 9, 0])
    @unpack
    def test_pow(self, base, exponent, expected):
        print("start")
        assert_equal(math.pow(base, exponent), expected)
        print("pass")
#         
#     @data([3, 2], [4, 3], [5, 3])
#     @unpack
#     def test_list_extracted_with_doc(self, first_value, second_value):
#         """Extract into args with first value {} and second value {}"""
#         print("start")
#         self.assertTrue(first_value > second_value)
        
    @file_data('sample.xlsx')
    def test_file_data_list(self, dict1):
        print('-------------------------')
#         for key, value in enumerate(dict1):
#             print(key.value + '-----' + value[j].value)
        print(self.getURLParametersFromDataSource(dict1))
#         print(type(dict1))
#         print(type(dict1[0]))
#         for k in dict1[0].keys():
#             for j,v in enumerate(k):
#                 print(v.value + '-----' + dict1[0][k][j].value) #+ dict1[1].value[j].value)
                
    
    def getURLParametersFromDataSource(self, inputDict):
        urlParameters = ''
        for k in inputDict[0].keys():
            for j,v in enumerate(k):
                urlParameters += '&{0}={1}'.format(v.value, inputDict[0][k][j].value)
        self.encodeUrl(urlParameters)
        return urlParameters[1:]
    
    def encodeUrl(self,url):
        url=url.replace('%3B',';')
        url=url.replace('%2C',',')
                
        
#     @file_data("test_data_dict_dict.json")
#     def test_file_data_dict_dict(self, start, end, value):
#         self.assertLess(start, end)
#         self.assertLess(value, end)
#         self.assertGreater(value, start)
#         
#     @data(u'ascii', u'non-ascii-\N{SNOWMAN}')
#     def test_unicode(self, value):
#         self.assertIn(value, (u'ascii', u'non-ascii-\N{SNOWMAN}'))
#         
        
        
    