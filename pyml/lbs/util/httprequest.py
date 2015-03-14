#coding=utf-8 
'''
Created on 2014年12月2日

@author: wanhao01
'''
import httplib

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

        