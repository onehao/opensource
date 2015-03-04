# -*- coding:utf-8 -*-
################################################################################
#
# Copyright (c) 2015 Baidu.com, Inc. All Rights Reserved
#
################################################################################
'''
Created on 2015年3月4日

@author: wanhao01
'''

from ConfigParser import NoSectionError
from ConfigParser import NoOptionError
from ConfigParser import DuplicateSectionError
import ConfigParser
import sys

from crawler.minispider import logwarning

class SpiderConfig(object):
    '''
    spider configuration file encapsulation.
    '''
     
    def __init__(self, configName):
        '''
        Constructor
        '''
        self.__spiderSection = 'spider'
        self.__cf = ConfigParser.ConfigParser()
        self.__configFile = configName
        
        self.__options = {'url_list_file': './urls',
                         'output_directory': './output',
                         'max_depth': '1',
                         'crawl_interval': '1',
                         'crawl_timeout': '1',
                         'target_url': '.*\.(gif|png|jpg|bmp)$',
                         '__thread_count': '8',
                         'email': 'wanhao01@baidu.com'
                         }

    def loadConfigFile(self):
        '''
        load and initialize the attributes of the Spider Config entity.
        '''
        config_files = self.__cf.read(self.__configFile)
        if(len(config_files) == 0):
            raise NoConfigError(self.__cur_file_dir() + '\\' + self.__configFile)

        self.__initOption('url_list_file')
        self.__initOption('output_directory')
        self.__initOption('max_depth')
        self.__initOption('crawl_interval1')
        self.__initOption('crawl_timeout')
        self.__initOption('target_url')
        self.__initOption('thread_count')
        self.__initOption('email')
    
    def __initOption(self, option):
        '''
        initialize the option.whenever there's any exception using the default value for the specific option.
        '''
        exp = Exception()
        try:
            option_value =  self.__cf.get(self.__spiderSection, option)
            self.__options[option] = option_value
        except NoSectionError as exception:
            exp = exception
        except NoOptionError as exception:
            exp = exception
        except DuplicateSectionError as exception:
            exp = exception
        except Error as exception:
            exp = exception
            exp.message = exp.message + ', using the default option value for:'
        finally:
            if(type(exp) != Exception):
                logwarning(str(type(exp)) + ',' + exp.message + option)
     
    def getUrlListFile(self):
        '''
        get the seed dir.
        '''
        return self.__options['url_list_file']
    
    def getOutputDir(self):
        '''
        path to store the output the result.
        '''
        return self.__options['output_directory']
    
    def getMaxDepth(self):
        '''
        the max crawl depth.
        '''
        return int(self.__options['max_depth'])
        
    def getCrawlInterval(self):
        '''
        ge the crawling interval.
        '''
        return int(self.__options['crawl_interval'])
        
    def getTimeOut(self):
        '''
        get timeout.
        '''
        return int(self.__options['crawl_timeout'])
    
    def getTargetUrlPattern(self):
        '''
        get the target url pattern.
        '''
        return self.__options['target_url']
        
    def getThreadCount(self):
        '''
        get the max number of thread to crawl
        '''
        return int(self.__options['thread_count'])
    
    def getEmail(self):
        '''
        send the result to the specific email address,the email addresses should be separated by ';'
        '''
        return self.__options['output_directory']

    def __cur_file_dir(self):
        '''
        get the script file path.
        '''
        #script file path
        return sys.path[0]

class Error(Exception):
    '''
    Base class for SpiderConfigParser exceptions.
    '''

    def _get_message(self):
        '''Getter for 'message'; needed only to override deprecation in
        BaseException.
        '''
        return self.__message

    def _set_message(self, value):
        '''Setter for 'message'; needed only to override deprecation in
        BaseException.
        '''
        self.__message = value

    message = property(_get_message, _set_message)

    def __init__(self, msg='error during parsing the config file'):
        self.message = msg
        Exception.__init__(self, msg)

    def __repr__(self):
        return self.message

    __str__ = __repr__

class NoConfigError(Error):
    '''
    Raised when no configuration file has been loaded.
    '''

    def __init__(self, path):
        Error.__init__(self, 'config file can not be found or read in the path: ' + path)