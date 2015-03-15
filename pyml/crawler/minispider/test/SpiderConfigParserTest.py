# -*- coding:utf-8 -*-
################################################################################
#
# Copyright (c) 2015 Baidu.com, Inc. All Rights Reserved
#
################################################################################
'''
Created on 2015年3月13日

@author: wanhao01
'''

import time
import unittest

from crawler.minispider.SpiderConfigParser import NoConfigError
from crawler.minispider.SpiderConfigParser import SpiderConfig


class TestSequenceFunctions(unittest.TestCase):
    '''
    as for D level project, not exhausting all the possible exception attributes.
    '''

    def setUp(self):
        configName = "../spider.conf"
        self.spiderConfig = SpiderConfig(configName)
        self.spiderConfig.loadConfigFile()
        
    def test_FileNotExist(self):
        '''
        test the condition that the 
        specified configuration file doesn't exist.
        the loadConfigFile function should handle
        the configuration not exist condition and log the error.
        '''
        # make sure the shuffled sequence does not lose any elements
        configName = "spider.abc"
        self.spiderConfig = SpiderConfig(configName)
        
        try:
            self.spiderConfig.loadConfigFile()
            self.assertTrue(False, 'NoConfigError should be thrown.')
        except NoConfigError as exception:
            self.assertTrue(True, 'test_FileNotExist pass.')
    
    def test_AttributeNotExist(self):
        '''
        test method for checking the condition 
        that the expecting attribute doesn't exist.
        '''
        configName = "spidertest.conf"
        self.spiderConfig = SpiderConfig(configName)

        self.spiderConfig.loadConfigFile()
        time.sleep(2)
        self.__checklog('test.log', 
                        'No option \'url_list_file\' in section: \'spider\'url_list_file')
        
    def __checklog(self, logfile, message):
        '''
        by default check the last line of the log file.
        '''
        file_object = open(logfile)
        try:
            all_the_text = file_object.readlines()
        finally:
            file_object.close()
        self.assertIn(message, all_the_text[-1], 
                      'the log file should contain the expect content: ' + message)
        

if __name__ == '__main__':
    unittest.main()