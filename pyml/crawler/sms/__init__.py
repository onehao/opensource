# -*- coding: utf-8 -*-
'''
程序：根据关键字拿到相关优惠活动信息
版本：0.1
@author: Michael Wan
@since: 2014-11-20
语言：Python 2.7
'''
from ast import literal_eval
import json
import urllib2


#http://sms.xudan123.com/do.aspx?uid=baxiaoti&token=afb9075f64309b8991e584e762337425&version=1.5.2&action=getAllProjectToBind&key=%E7%99%BE%E5%BA%A6&version=1.5.2&r=750
def getProjects(key):
    url = 'http://sms.xudan123.com/do.aspx?uid=baxiaoti&token=afb9075f64309b8991e584e762337425&version=1.5.2&action=getAllProjectToBind&key=' + key + '&version=1.5.2&r=750'
    results = urllib2.urlopen(url).read()
    resultsList = results.split('},')
    projects = []
    for itemJson in resultsList:
        print(itemJson)
        itemJson = itemJson.replace('[','')
        itemJson = itemJson.replace('}]','')
        itemJson += '}'
        if itemJson:
            item = json.loads(itemJson)
            print(item['Title'])
            projects.append(item['Title'])
    return projects
    
    
print(getProjects('百度'))