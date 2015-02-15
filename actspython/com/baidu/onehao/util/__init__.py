# -*- coding: utf-8 -*-
import logging
import logging.config


# 创建一个logger
logger = logging.getLogger('mylogger')
logger.setLevel(logging.DEBUG)
# 创建一个handler，用于写入日志文件
fh = logging.FileHandler('test.log')
fh.setLevel(logging.DEBUG)
# 再创建一个handler，用于输出到控制台
ch = logging.StreamHandler()
ch.setLevel(logging.DEBUG)
# 定义handler的输出格式
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s',"%m-%d %H:%M:%S")
fh.setFormatter(formatter)
ch.setFormatter(formatter)
# 给logger添加handler
logger.addHandler(fh)
logger.addHandler(ch)

# CONF_LOG = "logging.conf"  
# logging.config.fileConfig(CONF_LOG);    # 采用配置文件   
# logger = logging.getLogger("mylogger")  
# logger.debug("Hello mylogger")  
#   
# logger = logging.getLogger()  
# logger.info("Hello root")  


# 记录一条日志
logger.info('logger -- init')
logger.warning('logger -- init')
logger.error('logger -- init')
logger.debug('logger -- init')
logger.critical('logger -- init')
