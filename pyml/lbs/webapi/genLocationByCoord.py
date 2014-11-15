#coding=utf-8  
'''
@author: Michael Wan
@since: 2014-11-13
'''

import httplib
import json
import unittest
import sys
reload(sys)
#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
sys.setdefaultencoding('utf8')
print(sys.getdefaultencoding())

jsonString = "{\"address\":\"CN|\u5185\u8499\u53e4|\u9521\u6797\u90ed\u52d2\u76df|None|UNICOM|None|None\",\"content\":{\"address\":\"\u5185\u8499\u53e4\u81ea\u6cbb\u533a\u9521\u6797\u90ed\u52d2\u76df\",\"address_detail\":{\"city\":\"\u9521\u6797\u90ed\u52d2\u76df\",\"city_code\":63,\"district\":\"\",\"province\":\"\u5185\u8499\u53e4\u81ea\u6cbb\u533a\",\"street\":\"\",\"street_number\":\"\"},\"point\":{\"x\":\"116.02733969\",\"y\":\"43.93970484\"}},\"status\":0}"

s = json.loads(jsonString)

print(s)
print(s.keys())

def getRequest(requestUrl,context):
    conn = httplib.HTTPConnection(requestUrl)
    conn.request("GET", context)
    r1 = conn.getresponse()
    print(r1.status, r1.reason)
    data1 = r1.read()
    print(data1)
    conn.close()
    return data1

def getLocation():
    file_read = open('locationbd09ll', 'r')
    list = file_read.readlines()
#     file_write = open('locationGeocoder', 'w')
    file_write = open('location', 'w')
    for i in range(0, len(list)-1):
        #file_write.write(list[i])
        var = list[i].split(",")
        context = "/geocoder/v2/?pois=0&output=json&ak=wOexvA0egnE0qUUWHYcyY4wX&location=" + var[1].replace('\n','') + "," + var[0]
        result = getRequest("api.map.baidu.com", context)
        resultJsonObject = json.loads(result)
        location = resultJsonObject['result']['formatted_address']
        file_write.write(location)
        file_write.write('\n')
#         file_write.write(var[1].replace('\n','') + "," + var[0] + '\n')
    file_write.close()

def geocoderMetamorphic():
    file_read = open('locationbd09ll', 'r')
    list = file_read.readlines()
    for i in range(0, len(list)-1):
        #file_write.write(list[i])
        var = list[i].split(",")
        context = "/geocoder/v2/?pois=0&output=json&ak=wOexvA0egnE0qUUWHYcyY4wX&location=" + var[1].replace('\n','') + "," + var[0]
        result = getRequest("api.map.baidu.com", context)
        resultJsonObject = json.loads(result)
        location = resultJsonObject['result']['formatted_address']
        reverseGeocodingContext = '/geocoder/v2/?output=json&ak=wOexvA0egnE0qUUWHYcyY4wX&address=' + location
        result = getRequest("api.map.baidu.com", context)
        

geocoderMetamorphic()        