# -*- coding:utf-8 -*-
'''
Created on 2015年3月23日

@author: wanhao01
'''
import os
import re
import time

import timer

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
        return count

    def analyzelog(self, logfile):
        file_read = open(logfile, 'r').readlines()
        self.__getCountByModule(file_read, self.__GEOCONV__)
        self.__getCountByModule(file_read, self.__GEOCODER__)
        self.__getCountByModule(file_read, self.__PLACE__)
        self.__getCountByModule(file_read, self.__PLACE_SUGGESTOIN__)
        self.__getCountByModule(file_read, self.__DIRECTOIN__)
        self.__getCountByModule(file_read, self.__ROUTE_MATRIX__)
        self.__getCountByModule(file_read, self.__LOCATION__)
        
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
        
    def __analysebypattern(self, pattern, data):
        pattern = re.compile('\"GET ' + pattern + '.*?HTTP/1.1\"',re.DOTALL)
        match = pattern.findall(data)
        return len(match)       

if __name__ == '__main__':
    analyser = LBSLogAnalyser()
    analyser.analyzelogfolder('Z:\\logs')
    #analyser.analyzelog('D:\\document\\ftp\\lighttpd.log.2015032315')
    
    pass