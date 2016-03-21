# -*- coding:utf-8 -*-
'''
Created on 2015年3月23日

@author: wanhao01
'''
import os
import re
import time

import lbslogger


class LBSLogCaseExtracor(object):
    
    def __init__(self):
        self.__Maps__ = '/ws/valueadded/deepinfo'
        self.__logger = lbslogger
        self.__logger.__name__ = 'LBS log analyser'
    

    def __getCountByModule(self, file_read, module_name):
        count = 0
        list = []
        module_name1 = str(module_name).replace('/', '')
        file_write = open('cases'+ module_name1, 'w')
        for log in file_read:
            url = self.__analysebypattern(module_name, log)
            if url == '':
                continue
#             list.append(url)
            count = count + 1
            url = url[5:-6]
            file_write.writelines(url)
            file_write.write('\n')
        
        #print module_name + ' calling number: ' + str(count)
        self.__logger.loginfo(module_name + ' calling number: ' + str(count))
        file_write.close()
        return count

    def analyzelog(self, logfile):
        file_read = open(logfile, 'r').readlines()
        self.__getCountByModule(file_read, self.__Maps__)
        
        
    def __analysebypattern(self, pattern, data):
        pattern = re.compile('\"GET ' + pattern + '.*?http\"',re.DOTALL)
        match = pattern.findall(data)
        if len(match) > 0: 
            return match[0]  
        else:
            return ''     

if __name__ == '__main__':
    analyser = LBSLogCaseExtracor()
    #analyser.analyzelogfolder('Z:\\logs')
    analyser.analyzelog('E:\\AliDrive\\amap\\maps\\DeepinfoSearch.csv')
    #analyser.analyzelog('Z:\\logs\\lighttpd.log.2015032315')
    
    pass