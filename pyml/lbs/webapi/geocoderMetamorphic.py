#coding=utf-8  
'''
@author: Michael Wan
@since: 2014-11-13
'''

import httplib
import json
from math import fabs
import sys
import unittest

reload(sys)
#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
sys.setdefaultencoding('utf8')
print(sys.getdefaultencoding())


class TestSequenceFunctions(unittest.TestCase):

    def setUp(self):
        self.seq = range(10)

    def test_shuffle(self):
        filtered = open("./results", 'w')
        for i in range(1, 2):
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
                coord = json.loads(result)
                try:
                    lng = coord['result']['location']['lng']
                    lat = coord['result']['location']['lat']
                except:
                    print('request: ' + reverseGeocodingContext)
                    print('result: ' + result)
                # make sure the shuffled sequence does not lose any elements
                self.assertLess(fabs(lng - float(var[0])), 0.000002, 'expect: ' + str(lng) + ', actual: ' + var[0] + '\n' + context + '\n' + reverseGeocodingContext)
                self.assertLess(fabs(lat - float(var[1])), 0.000002, 'expect: ' + str(lat) + ', actual: ' + var[1] + '\n' + context + '\n' + reverseGeocodingContext)
                if fabs(lng - float(var[0])) > 0.000002 or fabs(lat - float(var[1])) > 0.000002:
                    filtered.write('ori:' + var[0] + ',' + var[1] + ' and des:' + lng + ',' + lat +  '\n')
                
        filtered.close()   

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
    unittest.main()