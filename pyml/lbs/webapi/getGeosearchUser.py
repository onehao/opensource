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
        self.__analysebypattern(self.__GEOSEARCH__, file_read)
        self.__analysebypattern(self.__GEOSEARCHV2__, file_read)
        self.__analysebypattern(self.__GEODATA__, file_read)
        
    def analyzelogfolderV2(self, folder):
        self.__analysebypatternfolder(self.__GEOSEARCH__, folder)
#         self.__analysebypatternfolder(self.__GEOSEARCHV2__, folder)
#         self.__analysebypatternfolder(self.__GEODATA__, folder)
        self.__analysebypatternfolder(self.__GEOSEARCHNEARBY__, folder)
        self.__analysebypatternfolder(self.__GEOSEARCHLOCAL__, folder)
        self.__analysebypatternfolder(self.__GEOSEARCHBOUND__, folder)
        self.__analysebypatternfolder(self.__GEOSEARCHDETAIL__, folder)
        
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
                count_location = count_location + self.u(lines, self.__LOCATION__)
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
                
    def __analysebypatternfolder(self, patternString, folder):
        pattern = re.compile('GET ' + patternString + '.*?HTTP/1.1',re.DOTALL)
        dic = {'a':0}
        dic.clear()
        file_write = open(self.__dict__[patternString] + '.csv', 'w')
        file_write.write("ak, count, developer_name, developer_alias , introduction, email \n".encode("gb2312")) 
        
        for root, dirs, files in os.walk(folder):
            for f in files:
                filename = root + os.sep + f
                file_read = open(filename, 'r')
                data = file_read.read()
                match = pattern.findall(data)
                for request in match:
                    pattern2 = re.compile('ak=.*?[\s&]',re.DOTALL)
                    match2 = pattern2.findall(request)
                    if len(match2) > 0:
                        ak = match2[0][3:-1]
                        if ak in dic.keys():
                            dic[ak] = dic[ak] + 1
                        else:
                            dic[ak] = 0
        diclist = sorted(dic.iteritems(), key=lambda d:d[1], reverse = True)
        for key in diclist:
            context = '/apics/app?method=search&app_ak=' + key[0];
#             result = getRequest("m1-map-controlservice00.m1.baidu.com:8088", context)
            result = getRequest("console.map.n.shifen.com", context)
            resultJsonObject = json.loads(result)
            
            #retry if the request failed by status not equal to 0.
            for i in range(3):
                if(resultJsonObject['status'] != 0):
                    result = getRequest("console.map.n.shifen.com", context)
                    time.sleep(0.1)
                    resultJsonObject = json.loads(result)
                else:
                    break;
            
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
#                     item = (key[0] + "," + str(key[1]) + "," + resultJsonObject['userInfo']['dev_info']['realname'] + "," + resultJsonObject['userInfo']['dev_info']['developer_alias'] + "," + resultJsonObject['userInfo']['dev_info']['introduction'] + 
#                             "," + resultJsonObject['userInfo']['pass_info']['email'] + "\n").encode("gb2312")
                    item = (key[0] + "," + str(key[1]) + "," + resultJsonObject['userInfo']['dev_info']['realname'] + "," + resultJsonObject['userInfo']['dev_info']['developer_alias'] + "," + resultJsonObject['userInfo']['dev_info']['introduction'] + 
                                 "\n").encode("gb2312")
                except Exception as e:
                    lbslogger.logerror(str(e))
                    continue
                file_write.write(item) 
        file_read.close();
                
                   
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
    parser.analyzelogfolderV2('Y:\\webservice\\geosearch');
    pass