#coding=utf-8  
'''
Created on 2016年4月14日

@author: michael.wh
'''
import unittest

import DataDrivenTestBase
from ddt import ddt, data, unpack, json_data, excel_data

@ddt
class TestData(DataDrivenTestBase.DataDrivenTestBase):
    url =  'http://hostname:port/context?'
    @excel_data('sample.xlsx')
    def test_file_data_list(self, dict1):
#         print('-------------------------')
        weburl = self.url + self.getURLParametersFromDataSource(dict1)
        print(weburl)
        
        #invoke request with the url  ${weburl}
        
        #verification response
        
    @excel_data('sample.xlsx')
    def testSample(self, dict1):
        acturlResult = self.mockFunction(dict1)
        expectedResult = self.getExpectValue(dict1)
        self.assertEqual(acturlResult, expectedResult, 'mockFunction 验证失败')
     
    # 从excel指定列读取期望值    
    def getExpectValue(self,dict1):
        expectValue = ''
        #从dict获取信息
        # dict键为testname， 值为2个元素的list， list[0]为excel 第一列值， list[1]为excel递归读取后续值。
        k = dict1.values()[0]
        # 获取第5列作为期望值
        expectValue = k[1][4]
        print(expectValue)
        return expectValue
    
    # 模拟使用excel中data构造数据               
    def mockFunction(self, dict1):
        urlParameters = ''
        k = dict1.values()[0]
        #从第一个cell到倒数第一个cell中数据构造。
        for v in k[1][:-1]:
            urlParameters += '+{0}'.format(v)
        print(urlParameters)
        return urlParameters[1:]        
#     @json_data('test_data_list.json')
#     def test_file_data_list2(self, value):
#         pass #self.assertTrue(is_a_greeting(value))
        
    @json_data("jsonsample.json")
    def test_file_data_dict_dict(self, city, keyword, response,test=''):
        print('---------------')
        print(test)
        print(city)
        print(keyword)
        print(response)
        