# -*- coding:utf-8 -*-
'''
Created on 2015年4月22日

@author: wanhao01
'''
import httplib
import json
import os
import re
import time

import timer

import lbslogger


class APIUserParser(object):
    __GEOCODER__ = ""
    __PLACE__ = ""
    __PLACE_SUGGESTOIN__= ""
    __DIRECTOIN__ = ""
    __ROUTE_MATRIX__ = ""
    __LOCATION__ = ""
    __GEOCONV__ = ""
    __TELEMATICS__ = ""
    __dict__ = {'a': 'a'}
    def __init__(self):
        self.__GEOCODER__ = '/geocoder/v2'
        self.__PLACE__ = '/place/v2'
        self.__PLACE_SUGGESTOIN__ = '/place/v2/suggestion'
        self.__DIRECTOIN__ = '/direction/v1'
        self.__ROUTE_MATRIX__ = '/direction/v1/routematrix'
        self.__LOCATION__ = '/location/ip'
        self.__GEOCONV__ = '/geoconv/v1'
        self.__TELEMATICS__ = '/telematics/v3'
        
        self.__dict__.clear()
        self.__dict__= {self.__GEOCODER__: 'geocoder',self.__PLACE__ : 'place',
                         self.__PLACE_SUGGESTOIN__: 'place_suggestion', self.__DIRECTOIN__ : 'direction',
                         self.__ROUTE_MATRIX__: 'routematrix', self.__LOCATION__ : 'location',
                         self.__GEOCONV__: 'geoconv', self.__TELEMATICS__ : 'telematrics'}
        
        self.__logger__ = lbslogger
        self.__logger__.__name__ = 'LBS user parser'
    

    def __getCountByModule(self, file_read, module_name):
        count = 0
        for log in file_read:
            if self.__TELEMATICS__ in log:
                print('in')
            self.__analysebypattern(module_name, log)
        
        #print module_name + ' calling number: ' + str(count)
        self.__logger.loginfo(module_name + ' calling number: ' + str(count))
        return count

    def analyzelog(self, logfile):
#         file_read = open(logfile, 'r').readlines()
        file_read = open(logfile, 'r').read()
#         self.__getCountByModule(file_read, self.__GEOCONV__)
#         self.__getCountByModule(file_read, self.__GEOCODER__)
#         self.__getCountByModule(file_read, self.__PLACE__)
#         self.__getCountByModule(file_read, self.__PLACE_SUGGESTOIN__)
#         self.__getCountByModule(file_read, self.__DIRECTOIN__)
#         self.__getCountByModule(file_read, self.__ROUTE_MATRIX__)
#         self.__getCountByModule(file_read, self.__LOCATION__)
#         self.__getCountByModule(file_read, self.__TELEMATICS__)
        self.__analysebypattern(self.__TELEMATICS__, file_read)
        self.__analysebypattern(self.__GEOCONV__, file_read)
        self.__analysebypattern(self.__GEOCODER__, file_read)
        self.__analysebypattern(self.__PLACE__, file_read)
        self.__analysebypattern(self.__PLACE_SUGGESTOIN__, file_read)
        self.__analysebypattern(self.__DIRECTOIN__, file_read)
        self.__analysebypattern(self.__ROUTE_MATRIX__, file_read)
        self.__analysebypattern(self.__LOCATION__, file_read)
        
    def analyzelogfolder(self, folder):
        count_geoconv = 0
        count_geocoder = 0
        count_place = 0
        count_place_suggestion = 0
        count_direction = 0
        count_route_matrix = 0
        count_location = 0
        
        for root, dirs, files in os.walk(folder):
            for f in files:
                filename = root + os.sep + f
                file_read = open(filename, 'r')
                lines = file_read.readlines()
#                 lines = file_read.read()
                count_geoconv = count_geoconv + self.__getCountByModule(lines, self.__GEOCONV__)
                count_geocoder = count_geocoder + self.__getCountByModule(lines, self.__GEOCODER__)
                count_place = count_place + self.__getCountByModule(lines, self.__PLACE__)
                count_place_suggestion = count_place_suggestion + self.__getCountByModule(lines, self.__PLACE_SUGGESTOIN__)
                count_direction = count_direction + self.__getCountByModule(lines, self.__DIRECTOIN__)
                count_route_matrix = count_route_matrix + self.__getCountByModule(lines, self.__ROUTE_MATRIX__)
                count_location = count_location + self.__getCountByModule(lines, self.__LOCATION__)
                file_read.close()
                lines = []
                time.sleep(10)
        self.__logger.loginfo(self.__GEOCONV__ + ' totally analyzed in folder calling number: ' + str(count_geoconv))     
        self.__logger.loginfo(self.__GEOCODER__ + ' totally analyzed in folder calling number: ' + str(count_geocoder))   
        self.__logger.loginfo(self.__PLACE__ + ' totally analyzed in folder calling number: ' + str(count_place))   
        self.__logger.loginfo(self.__PLACE_SUGGESTOIN__ + ' totally analyzed in folder calling number: ' + str(count_place_suggestion))   
        self.__logger.loginfo(self.__DIRECTOIN__ + ' totally analyzed in folder calling number: ' + str(count_direction))   
        self.__logger.loginfo(self.__ROUTE_MATRIX__ + ' totally analyzed in folder calling number: ' + str(count_route_matrix))   
        self.__logger.loginfo(self.__LOCATION__ + ' totally analyzed in folder calling number: ' + str(count_location))     
        
    def __analysebypattern(self, patternString, data):
        pattern = re.compile('GET ' + patternString + '.*?HTTP/1.1',re.DOTALL)
        match = pattern.findall(data)
        dic = {'a':0}
        dic.clear()
        file_write = open(self.__dict__[patternString] + '.csv', 'w')
        file_write.write("ak, count, developer_name, developer_alias , introduction, email \n".encode("gb2312")) 
        for request in match:
            pattern = re.compile('ak=.*?[\s&]',re.DOTALL)
            match2 = pattern.findall(request)
            if len(match2) > 0:
                ak = match2[0][3:-1]
                if ak in dic.keys():
                    dic[ak] = dic[ak] + 1
                else:
                    dic[ak] = 0
        dic = sorted(dic.iteritems(), key=lambda d:d[1], reverse = True)
        for key in dic:
            context = '/apics/app?method=search&app_ak=' + key[0];
            result = getRequest("m1-map-controlservice00.m1.baidu.com:8088", context)
            resultJsonObject = json.loads(result)
            if(resultJsonObject['status'] != 0):
                #lbslogger.logerror(resultJsonObject['result'] + '--' + resultJsonObject['message'])
                lbslogger.logerror('error')
        #         lbslogger.logerror(resultJsonObject['result'] + '--' + resultJsonObject['message'])
            else:
                user_id = resultJsonObject['result']['user_id']
                context = '/apics/user?method=userinfo&uid=' + str(user_id);
                result = getRequest("console.map.n.shifen.com", context)
                resultJsonObject = json.loads(result)
                item = ''
                try:
                    item = (key[0] + "," + str(key[1]) + "," + resultJsonObject['userInfo']['dev_info']['realname'] + "," + resultJsonObject['userInfo']['dev_info']['developer_alias'] + "," + resultJsonObject['userInfo']['dev_info']['introduction'] + 
                            "," + resultJsonObject['userInfo']['pass_info']['email'] + "\n").encode("gb2312")
                except Exception as e:

                    continue
                file_write.write(item) 
                
                   
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
    parser.analyzelog('Z:\\logs\\lighttpd.log.2015031815')
    
    pass