# -*- coding:utf-8 -*-
'''
Created on 2015年4月22日

@author: wanhao01
'''
import httplib
import json
import os
import random
import re
import time

import timer

import lbslogger


class APIUserParser(object):
    __GEOSEARCH__ = ""
    __GEOSEARCHV2__=""
    __GEODATA__ = ""
    __GEOSEARCHNEARBY__ = ""
    __GEOSEARCHLOCAL__ = ""
    __GEOSEARCHBOUND__ = ""
    __GEOSEARCHDETAIL__ = ""
    
    __dict__ = {'a': 'a'}
    def __init__(self):
        self.__GEOSEARCH__ = '/geosearch/v3'
        self.__GEOSEARCHV2__ = '/geosearch/v2'
        self.__GEOSEARCHNEARBY__ = "/geosearch/v3/nearby"
        self.__GEOSEARCHLOCAL__ = "/geosearch/v3/local"
        self.__GEOSEARCHBOUND__ = "/geosearch/v3/bound"
        self.__GEOSEARCHDETAIL__ = "/geosearch/v3/detail"
        
        self.__GEODATA__ = ''
        
        self.__dict__.clear()
        self.__dict__= {self.__GEOSEARCH__: 'geosearch',self.__GEOSEARCHV2__ : 'geosearchv2',
                         self.__GEODATA__: 'geodata', self.__GEOSEARCHNEARBY__ : 'geosearchnearby',
                         self.__GEOSEARCHLOCAL__ : 'geosearchlocal', self.__GEOSEARCHBOUND__: 'geosearchbound',
                         self.__GEOSEARCHDETAIL__ : 'geosearchdetail'}
        
        self.__logger__ = lbslogger
        self.__logger__.__name__ = 'LBS user parser'
    

        
  
    #init for table, 106989, ak=Ow5fqi6DQXmgD5PGSB7QBdHF
    def getPois(self):
        file_write = open('geodatainit.csv', 'w')
        file_write.write("title,longitude,latitude,coord_type,metrotype2,metrotype,rgcurl,ak,geotable_id\n".encode("gb2312")) 
        for i in range(7):
            context = '/geosearch/v3/nearby?geotable_id=53127&ak=CF2fbb818a53d5ef10e702dadf5495e6&location=116.404772,39.938644&radius=200000&filter=ismetro:1,1&page_size=50&sortby=distance:1&page_index=' + str(i)
            result = getRequest("api.map.baidu.com", context)
            resultJsonObject = json.loads(result)
            
            for j in range(len(resultJsonObject['contents'])):
                title = resultJsonObject['contents'][j]['title'].encode('gb2312')
                longitude = str(resultJsonObject['contents'][j]['location'][0])
                latitude = str(resultJsonObject['contents'][j]['location'][1])
                coord_type = str(resultJsonObject['contents'][j]['coord_type'])
                metrotype2 = str(random.randint(10, 12))
                metrotype = str(random.randint(1, 3))
                rgcurl = resultJsonObject['contents'][j]['url']
                item = title + ',' + longitude + ',' + latitude + ',' + coord_type + ',' + metrotype2 + ',' + metrotype + ',' + rgcurl + ',Ow5fqi6DQXmgD5PGSB7QBdHF,106989' + '\n'
                file_write.writelines(item)
                continue
        file_write.close(); 
        
        
    #init for table, 106990, ak=Ow5fqi6DQXmgD5PGSB7QBdHF
    def getPoisTable7(self):
        
        #地铁数据
        file_write = open('geodatainitTable7Metro.csv', 'w')
        file_write.write("title,longitude,latitude,coord_type,metrotype,type2,category,category_en,url,ak,geotable_id\n".encode("gb2312")) 
        for i in range(7):
            context = '/geosearch/v3/nearby?geotable_id=53127&ak=CF2fbb818a53d5ef10e702dadf5495e6&location=116.404772,39.938644&radius=200000&filter=ismetro:1,1&page_size=50&sortby=distance:1&page_index=' + str(i)
            result = getRequest("api.map.baidu.com", context)
            resultJsonObject = json.loads(result)
            
            for j in range(len(resultJsonObject['contents'])):
                title = resultJsonObject['contents'][j]['title'].encode('gb2312')
                longitude = str(resultJsonObject['contents'][j]['location'][0])
                latitude = str(resultJsonObject['contents'][j]['location'][1])
                coord_type = str(resultJsonObject['contents'][j]['coord_type'])
                
                metrotype = str(random.randint(1, 3))
                type2 = str(1)
                category = u'地铁'.encode('gb2312')
                category_en = 'metro'
                url = resultJsonObject['contents'][j]['url']
                item = title + ',' + longitude + ',' + latitude + ',' + coord_type + ','  + metrotype + ',' + type2 + ',' + category +',' + category_en + ',' + url + ',Ow5fqi6DQXmgD5PGSB7QBdHF,106990' + '\n'
                file_write.writelines(item)
                continue
        file_write.close(); 
        
        #公交数据
        file_write = open('geodatainitTable7bus.csv', 'w')
        file_write.write("title,longitude,latitude,coord_type,metrotype,type2,category,category_en,url,ak,geotable_id\n".encode("gb2312")) 
        for i in range(81):
#             http://api.map.baidu.com/geosearch/v3/nearby?geotable_id=53127&ak=CF2fbb818a53d5ef10e702dadf5495e6&location=116.404772,39.938644&radius=100000&filter=stype:0&page_size=50&sortby=distance:1
            context = '/geosearch/v3/nearby?geotable_id=53127&ak=CF2fbb818a53d5ef10e702dadf5495e6&location=116.404772,39.938644&radius=100000&filter=stype:0&page_size=50&sortby=distance:1&page_index=' + str(i)
            result = getRequest("api.map.baidu.com", context)
            resultJsonObject = json.loads(result)
            
            for j in range(len(resultJsonObject['contents'])):
                try:
                    title = resultJsonObject['contents'][j]['title'].encode('gb2312')
                    longitude = str(resultJsonObject['contents'][j]['location'][0])
                    latitude = str(resultJsonObject['contents'][j]['location'][1])
                    coord_type = str(resultJsonObject['contents'][j]['coord_type'])
                    
                    metrotype = str(random.randint(1, 3))
                    type2 = str(2)
                    category = u'公交'.encode('gb2312')
                    category_en = 'bus'
                    url = resultJsonObject['contents'][j]['url']
                    item = title + ',' + longitude + ',' + latitude + ',' + coord_type + ','  + metrotype + ',' + type2 + ',' + category +',' + category_en + ',' + url + ',Ow5fqi6DQXmgD5PGSB7QBdHF,106990' + '\n'
                    file_write.writelines(item)
                    continue
                except:
                    continue
        file_write.close();
        
        
    #init for table, 106992, ak=Ow5fqi6DQXmgD5PGSB7QBdHF, Table8:  id: 106992,  name： geo表_点6景点
    def getPoisTable8(self):
        
        #地铁数据
        file_write = open('geodatainitTable8Scenic.csv', 'w')
        file_write.write("title,longitude,latitude,coord_type,type2,weight2,url,ak,geotable_id\n".encode("gb2312")) 
        for i in range(80):
            context = '/geosearch/v3/nearby?geotable_id=57350&ak=CF2fbb818a53d5ef10e702dadf5495e6&location=116.404772,39.938644&radius=10000000&page_size=50&sortby=distance:1&page_index=' + str(i)
            result = getRequest("api.map.baidu.com", context)
            resultJsonObject = json.loads(result)
            
            for j in range(len(resultJsonObject['contents'])):
                try:
                    title = resultJsonObject['contents'][j]['title'].encode('gb2312')
                    longitude = str(resultJsonObject['contents'][j]['location'][0])
                    latitude = str(resultJsonObject['contents'][j]['location'][1])
                    coord_type = str(resultJsonObject['contents'][j]['coord_type'])
                    
                    type2 = str(random.randint(0, 3))
                    weight2 = str(random.randint(0, 10))
                    url = resultJsonObject['contents'][j]['url']
                    item = title + ',' + longitude + ',' + latitude + ',' + coord_type + ',' + type2 + ',' + weight2 + ',' + url + ',Ow5fqi6DQXmgD5PGSB7QBdHF,106992' + '\n'
                    file_write.writelines(item)
                except:
                    continue
        file_write.close(); 
            
                   
def getRequest(requestUrl,context):
    conn = httplib.HTTPConnection(requestUrl)
    conn.request("GET", context)
    r1 = conn.getresponse()
#     print(r1.status, r1.reason)
    data1 = r1.read()
#     print(data1)
    conn.close()  
    return data1          
       

if __name__ == '__main__':
    parser = APIUserParser()
    #analyser.analyzelogfolder('Z:\\logs')
#     parser.analyzelog('D:\\document\\ftp\\lighttpd.log.wf.2014081310')
#     parser.analyzelog('Z:\\logs\\lighttpd.log.2015031815')
#     parser.getPoisTable7()
    parser.getPoisTable8()
    pass