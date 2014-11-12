#coding=utf-8  
'''
@author: Michael Wan
@since: 2014-11-10
'''
import httplib, urllib
import json
import sys
reload(sys)
#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
sys.setdefaultencoding('utf8')
print(sys.getdefaultencoding())

json.loads('["foo", {"bar":["baz", null, 1.0, 2]}]')

def xpath_get(mydict, path):
    elem = mydict
    try:
        for x in path.strip("/").split("/"):
            elem = elem.get(x)
    except:
        pass

    return elem

def getRequest(requestUrl,context):
    conn = httplib.HTTPConnection(requestUrl)
    conn.request("GET", context)
    r1 = conn.getresponse()
    print(r1.status, r1.reason)
    data1 = r1.read()
    print(data1)
    conn.close()
    return data1
    
def postRequest(requesturl, context, params, headers):
    #params = urllib.urlencode({'name': "geotable", 'geotype': 1, 'is_published': 1, "ak" : "wOexvA0egnE0qUUWHYcyY4wX"})
#     headers = {"Content-type": "application/x-www-form-urlencoded",
#             "Accept": "application/json"}
    conn = httplib.HTTPConnection(requesturl)
    conn.request("POST", context, params, headers)
    response = conn.getresponse()
    print response.status, response.reason
    data = response.read()
    print(data)
    conn.close()

def myTest():
    getRequest("api.map.baidu.com", "/place/v2/search?&q=饭店&region=北京&output=json&ak=Xv6KPR3tnTvL40X7yXKB02U1")
    params = urllib.urlencode({'name': "michael1", 'geotype': 1, 'is_published': 1, "ak" : "wOexvA0egnE0qUUWHYcyY4wX"})
    headers = {"Content-type": "application/x-www-form-urlencoded",
            "Accept": "application/json"}
    #postRequest("api.map.baidu.com", "/geodata/v3/geotable/create", params, headers);
    file_object = open('result.txt', 'w')
    
    for i in range(1, 1000):
        result = getRequest("api.map.baidu.com", "/telematics/v3/movie?qt=hot_movie&out_coord_type=bd09mc&coord_type=gcj02&location=北京&output=xml&ak=MOCOlcworadtFqL6jbaIwFsq")
        file_object.write(result)
    file_object.close( )
    
myTest()  
    
