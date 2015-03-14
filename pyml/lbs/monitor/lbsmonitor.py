# -*- coding: utf-8 -*-
'''
@author: Michael Wan
@since: 2015-01-14
@note: 
 cq01-map-api00.cq01.baidu.com:8000
北京机房
杭州机房：hz01-map-api00.hz01.baidu.com:8000
南京机房：nj02-map-api00.nj02.baidu.com:8000

http://api.map.baidu.com/geosearch/v3/nearbyPolygon?geotable_id=68379&location=12948032%2C4845090&radis=6666&coord_type=4&page_size=50&page_index=0&scope=1&ak=bWvpyBB70FswqwPA4nUkmoSW&sn=8c6e780d33ac77a588159018d4b52dd3

'''
import httplib
import urllib2


def getRequest(requestUrl,context,port=80):
    conn = httplib.HTTPConnection(requestUrl,port)
    conn.request("GET", context)
    
    r1 = conn.getresponse()
    print(r1.status, r1.reason)
    data1 = r1.read()
    print(data1)
    conn.close()
    
    #request.add_header('User-Agent', 'fake-client')
    print(urllib2.urlopen('http://api.map.baidu.com/geosearch/v3/nearbyPolygon?geotable_id=68379&location=12948032%2C4845090&radis=6666&coord_type=4&page_size=50&page_index=0&scope=1&ak=bWvpyBB70FswqwPA4nUkmoSW&sn=8c6e780d33ac77a588159018d4b52dd3').read())
    return data1


def getRequesturllib2(requestUrl,context,port=80):
    print('http://' +requestUrl + ':' + str(port) + context)
    response = urllib2.urlopen('http://' + requestUrl + ':' + str(port) + context)
    data1 = response.read()
    print(data1)
    return data1

def write(file,domain,result):
    file.write(domain + ":  " + result)
    file.write("\n")
    
casecontext = '/geosearch/v3/nearbyPolygon?geotable_id=68379&location=12948032%2C4845090&radis=6666&coord_type=4&page_size=50&page_index=0&scope=1&ak=bWvpyBB70FswqwPA4nUkmoSW&sn=8c6e780d33ac77a588159018d4b52dd3'

def mymonitor():
    file_object = open('monitor.txt', 'w')
    while 1:
    #for i in range(10):
        result = getRequesturllib2("api.map.baidu.com", casecontext)
        write(file_object,"api.map.baidu.com",result)
        result = getRequesturllib2("cq01-map-api00.cq01.baidu.com", casecontext,8000)
        write(file_object,"cq01-map-api00.cq01.baidu.com:8000",result)
        result = getRequesturllib2("hz01-map-api00.hz01.baidu.com", casecontext,8000)
        write(file_object,"hz01-map-api00.hz01.baidu.com:8000",result)
        result = getRequesturllib2("nj02-map-api00.nj02.baidu.com", casecontext,8000)
        write(file_object,"nj02-map-api00.nj02.baidu.com:8000",result)
    file_object.close()
        
mymonitor()