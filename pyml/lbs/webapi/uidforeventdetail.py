#coding=utf-8  
'''
Created on 2014年12月2日

@author: wanhao01
'''

import json

from httprequest import getRequest

import sys
reload(sys)
sys.setdefaultencoding('utf-8')

def getUids():
    domain = "api.map.baidu.com"
    context = "/place/v2/eventsearch?event=groupon&page_size=10&ak=wOexvA0egnE0qUUWHYcyY4wX&bounds=39.939225%2c116.282243%2c40.028344%2c116.49913&query=购物中心&region=131&page_num=1&output=json"
    result = getRequest(domain, context)
    resultJsonObject = json.loads(result)
    totalstring = resultJsonObject['total']
    total = int(totalstring)
    file_write = open('uidforeventdetailkeyvalue', 'w')
    count = 0
    for i in range(total/20):
        context = "/place/v2/eventsearch?event=groupon&page_size=20&ak=wOexvA0egnE0qUUWHYcyY4wX&bounds=39.939225%2c116.282243%2c40.028344%2c116.49913&query=购物中心&region=131&page_num=" + str(i) +"&output=json"
        result = getRequest(domain, context)
        resultJsonObject = json.loads(result)
        
        for j in range(20):
            uids = resultJsonObject['results'][j]['uid']
            name = resultJsonObject['results'][j]['name']
            file_write.write(uids)
            file_write.write(name)
            file_write.write('\n')
            count = count+1;
    file_write.write('\n')
    file_write.write(str(count))
    file_write.close()

if __name__ == '__main__':
    getUids()