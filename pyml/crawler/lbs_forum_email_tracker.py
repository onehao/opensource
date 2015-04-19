# -*- coding: utf-8 -*-
'''
程序：LBS WEB API吧爬虫
版本：0.1
@author: Michael Wan
日期：2014-11-15
语言：Python 2.7
操作：输入带分页的地址，去掉最后面的数字，设置一下起始页数和终点页数。
功能：下载对应页码内的所有页面并存储为html文件。
@see: http://blog.csdn.net/pleasecallmewhy/article/details/8927832
'''
 
import re
import string, urllib2
import sys
import time
import crawler
from crawler.myemail import send_mail
<<<<<<< HEAD

=======
>>>>>>> branch 'master' of https://github.com/onehao/opensource.git

#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
#sys.setdefaultencoding('utf8')
crawler.logger.info(sys.getdefaultencoding())
mailto_list=["wanhao01@baidu.com", 'onehaojacket@gmail.com','174823192@qq.com'] 


def get4PageForum(url,title):
    #url = 'http://bbs.lbsyun.baidu.com/forum.php?mod=forumdisplay&fid=7&page='
    result = ""
    result2 = []
    crawler.logger.info("test")
    for i in range(1, 4):
        sName = string.zfill(i,5) #+ '.html'#自动填充成六位的文件名
        crawler.logger.info('正在下载第' + str(i) + '个网页，并将其存储为' + sName + '......')
        
        #f = open(sName,'w+')
        m = urllib2.urlopen(url + str(i)).read()
        #print(m)
        regex = '<a[\s]+href=\"forum.php\?mod=viewthread.*\"[\s]+onclick=\"atarget\(this\)\"[\s]+class=\"s[\s]xst\"[\s]>.*</a>'
        # 将正则表达式编译成Pattern对象
        pattern = re.compile(r'<!--[\s]+主题名称[\s]+-->[\s]+<a[\s]+href=\"forum.php\?mod=viewthread.*\"[\s]+onclick=\"atarget\(this\)\"[\s]+class=\"s[\s]xst\"[\s]>.*</a>')
        pattern2 = re.compile('<a[\s]+href=\"forum.php\?mod=viewthread.*\"[\s]+onclick=\"atarget\(this\)\"[\s]+class=\"s[\s]xst\"[\s]>.*</a>')
        match = pattern.findall(m)
#         match = re.findall(regex,m)
#         print(match)
#         match = re.compile(regex).search(m)
#         print(match)

        #match = pattern2.findall(match)
        crawler.logger.info(match)
        result2 +=match
        for i in range(0,len(match)):
            test = pattern2.findall(match[i])
            match[i] = pattern2.findall(match[i])[0]
            result += match[i].replace("forum.php", "bbs.lbsyun.baidu.com/forum.php") + "<br />"
    if send_mail(mailto_list, 'LBS WEB API forum _ ' + title+"_"+ time.strftime("%Y-%m-%d", time.localtime()), result):
        crawler.logger.info("发送成功".decode('utf8'))
    else:  
        crawler.logger.error('发送失败'.decode('utf8'))
    #send_mail(mailto_list, 'LBS WEB API forum _2' + time.strftime("%Y-%m-%d", time.localtime()), result2)

get4PageForum('http://bbs.lbsyun.baidu.com/forum.php?mod=forumdisplay&fid=7&page=',"WEB_API")
get4PageForum('http://bbs.lbsyun.baidu.com/forum.php?mod=forumdisplay&fid=6&page=',"LBS云")
get4PageForum('http://bbs.lbsyun.baidu.com/forum.php?mod=viewthread&tid=81406&extra=page=',"鹰眼")
#raw_input()
