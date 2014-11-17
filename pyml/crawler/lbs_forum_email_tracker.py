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

from myemail import send_mail

#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
#sys.setdefaultencoding('utf8')
print(sys.getdefaultencoding())
mailto_list=["wanhao01@baidu.com", 'onehaojacket@gmail.com'] 


def get4PageForum():
    url = 'http://bbs.lbsyun.baidu.com/forumdisplay.php?fid=7&page='
    result = ""
    result2 = []
    for i in range(1, 4):
        sName = string.zfill(i,5) #+ '.html'#自动填充成六位的文件名
        print '正在下载第' + str(i) + '个网页，并将其存储为' + sName + '......'
        #f = open(sName,'w+')
        m = urllib2.urlopen(url + str(i)).read()
        # 将正则表达式编译成Pattern对象
        pattern = re.compile('<a href=\"viewthread\.php\?tid=.*</a>')
        match = pattern.findall(m)
        print(match)
        result2 +=match
        for i in range(0,len(match)):
            result += match[i] + "<br />"
    if send_mail(mailto_list, 'LBS WEB API forum _ ' + time.strftime("%Y-%m-%d", time.localtime()), result):
        print "发送成功"  
    else:  
        print "发送失败"
    #send_mail(mailto_list, 'LBS WEB API forum _2' + time.strftime("%Y-%m-%d", time.localtime()), result2)

get4PageForum()