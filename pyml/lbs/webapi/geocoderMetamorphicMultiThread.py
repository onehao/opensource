#coding=utf-8  
'''
@author: Michael Wan
@since: 2014-11-13
'''

import httplib
import json
from math import fabs
import sys
import threading
import unittest


reload(sys)
#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
sys.setdefaultencoding('utf8')
print(sys.getdefaultencoding())



def executeTest(results, lock, item):
    var = item.split(",")
    context = "/geocoder/v2/?pois=0&output=json&ak=wOexvA0egnE0qUUWHYcyY4wX&location=" + var[1].replace('\n', '') + "," + var[0]
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
        print 'request: ' + reverseGeocodingContext
        print 'result: ' + result
        # make sure the shuffled sequence does not lose any elements
    if fabs(lng - float(var[0])) > 0.000002 or fabs(lat - float(var[1])) > 0.000002:
        if lock.acquire():
#             print '%s get the lock.' % threading.currentThread().getName()
            results.write('ori:' + str(var[0]) + ',' + str(var[1]) + ' and des:' + str(lng) + ',' + str(lat) + '\n')
            
#             print '%s release lock...' % threading.currentThread().getName()
            lock.release()

def geocoderTest():
    global results
    results = open("./results", 'w')
    lock = threading.Lock()
    threadNum = 10
    for i in range(1, 2):
        file_read = open('locationbd09ll', 'r')
        lines = file_read.readlines()
        for i in range(0, len(lines)-1,threadNum):
            #file_write.write(list[i])
            threads = []
            for t in range(0, threadNum):
                try:
                    thread = threading.Thread(target=executeTest, args=(results,lock,lines[i+t]))
                except:
                    continue
                threads.append(thread)
                thread.start()
            waitforcomplete(threads)    
    results.close()   
    
def waitforcomplete(threads):
    while len(threads):  
            worker = threads.pop()  
            worker.join( )  
            if worker.isAlive():
                threads.append( worker ) 
    print('threads completed.')
        

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
    geocoderTest()