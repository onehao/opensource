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

import random
import unittest

from crawler.minispider.SpiderConfigParser import SpiderConfig, NoConfigError


class TestSequenceFunctions(unittest.TestCase):

    def setUp(self):
        configName = "../spider.conf"
        self.spiderConfig = SpiderConfig(configName)
        self.spiderConfig.loadConfigFile()

    def test_FileNotExist(self):
        # make sure the shuffled sequence does not lose any elements
        configName = "spider.abc"
        self.spiderConfig = SpiderConfig(configName)
        try:
            self.spiderConfig.loadConfigFile()
            self.assertTrue(False, 'NoConfigError should be thrown.')
        except NoConfigError as exception:
            self.assertTrue(True, 'test_FileNotExist pass.')
    
    def test_AttributeNotExist(self):
        configName = "spidertest.conf"
        self.spiderConfig = SpiderConfig(configName)
        try:
            self.spiderConfig.loadConfigFile()
            self.assertTrue(False, 'NoConfigError should be thrown.')
        except NoConfigError as exception:
            self.assertTrue(True, 'test_FileNotExist pass.')
    
        

if __name__ == '__main__':
    unittest.main()