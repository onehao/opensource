# -*- coding:utf-8 -*-
'''
Created on 2015年3月23日

@author: wanhao01
'''
import re

import lbslogger


class LBSLogAnalyser(object):
    
    def __init__(self):
        self.__GEOCODER__ = '/geocoder/v2'
        self.__PLACE__ = '/place/v2'
        self.__PLACE_SUGGESTOIN__ = '/place/v2/suggestion'
        self.__DIRECTOIN__ = '/direction/v1'
        self.__ROUTE_MATRIX__ = '/direction/v1/routematrix'
        self.__LOCATION__ = '/location/ip'
        self.__GEOCONV__ = '/geoconv/v1'
        self.__logger = lbslogger
        self.__logger.__name__ = 'LBS log analyser'
    

    def __getCountByModule(self, file_read, module_name):
        count = 0
        for log in file_read:
            count = count + self.__analysebypattern(module_name, log)
        
        #print module_name + ' calling number: ' + str(count)
        self.__logger.loginfo(module_name + ' calling number: ' + str(count))

    def analyzelog(self, logfile):
        file_read = open(logfile, 'r').readlines()
        self.__getCountByModule(file_read, self.__GEOCONV__)
        self.__getCountByModule(file_read, self.__GEOCODER__)
        self.__getCountByModule(file_read, self.__PLACE__)
        self.__getCountByModule(file_read, self.__PLACE_SUGGESTOIN__)
        self.__getCountByModule(file_read, self.__DIRECTOIN__)
        self.__getCountByModule(file_read, self.__ROUTE_MATRIX__)
        self.__getCountByModule(file_read, self.__LOCATION__)
        
        
    def __analysebypattern(self, pattern, data):
        pattern = re.compile('\"GET ' + pattern + '.*?HTTP/1.1\"',re.DOTALL)
        match = pattern.findall(data)
        return len(match)       

if __name__ == '__main__':
    analyser = LBSLogAnalyser()
    analyser.analyzelog('D:\\document\\ftp\\lighttpd.log.2015032315')
    pass