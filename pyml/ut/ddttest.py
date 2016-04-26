#coding=utf-8  
'''
Created on 2016年4月14日

@author: michael.wh
'''
import unittest

import DataDrivenTestBase
from ddt import ddt, data, unpack, file_data

@ddt
class TestData(DataDrivenTestBase.DataDrivenTestBase):
    url =  'http://hostname:port/context?'
    @file_data('sample.xlsx')
    def test_file_data_list(self, dict1):
#         print('-------------------------')
        weburl = self.url + self.getURLParametersFromDataSource(dict1)
        print(weburl)
        
        #invoke request with the url  ${weburl}
        
        #verification response
        
    @file_data('sample.xlsx')
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
        # parsing url from data source.
        
    def getURLParametersFromDataSource(self, inputDict):
        urlParameters = ''
        k = inputDict.values()[0]
        for j,v in enumerate(k[0]):
            urlParameters += '&{0}={1}'.format(v, k[1][j])
        self.encodeUrl(urlParameters)
        return urlParameters[1:]
    
#     # parsing url from data source.
#     def getURLParametersFromDataSource(self, inputDict):
#         urlParameters = ''
#         for k in inputDict[0].keys():
#             for j,v in enumerate(k):
#                 urlParameters += '&{0}={1}'.format(v, inputDict[0][k][j])
#         self.encodeUrl(urlParameters)
#         return urlParameters[1:]
    
    def encodeUrl(self,url):
        url=url.replace('%3B',';')
        url=url.replace('%2C',',')
        
        