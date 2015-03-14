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
import logging
import logging.config

__logger_context = ''
# 创建一个logger
logger = logging.getLogger('minispider')

logger.setLevel(logging.DEBUG)
# 创建一个handler，用于写入日志文件
fh = logging.FileHandler('test.log')
fh.setLevel(logging.DEBUG)
# 再创建一个handler，用于输出到控制台
ch = logging.StreamHandler()
ch.setLevel(logging.DEBUG)
# 定义handler的输出格式
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
fh.setFormatter(formatter)
ch.setFormatter(formatter)
# 给logger添加handler
logger.addHandler(fh)
logger.addHandler(ch)
logger.info('minispider -- init')    

def loginfo(message):
    '''
    encapsulate the log info
    '''
    logger.info(__logger_context + message)
    
    
def logerror(message):
    '''
    encapsulate the log error
    '''
    logger.error(__logger_context + message)
    
    
def logwarning(message):
    '''
    encapsulate the log warning
    '''
    logger.warning(__logger_context + message)
    
    
def logdebug(message):
    '''
    encapsulate the log debug
    '''
    logger.debug(__logger_context + message)
    
    
def logcritical(message):
    '''
    encapsulate the log critical
    '''
    logger.critical(__logger_context + message)