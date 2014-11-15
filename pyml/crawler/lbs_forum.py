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
 
import base64
import os
import re
import string, urllib2
import sys


reload(sys)
#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
sys.setdefaultencoding('utf8')
print(sys.getdefaultencoding())
mailto_list=["wanhao01@baidu.com", 'onehao333@gmail.com'] 
#定义百度函数
def lbs_forum(url,begin_page,end_page):   
    for i in range(begin_page, end_page+1):
        sName = string.zfill(i,5) #+ '.html'#自动填充成六位的文件名
        print '正在下载第' + str(i) + '个网页，并将其存储为' + sName + '......'
        #f = open(sName,'w+')
        m = urllib2.urlopen(url + str(i)).read()
        # 将正则表达式编译成Pattern对象
        pattern = re.compile('<a href=\"viewthread\.php\?tid=.*</a>')
        # 使用Pattern匹配文本，获得匹配结果，无法匹配时将返回None
        match = pattern.findall(m)
        print(match)
        for i in range(0, len(match)):
            
            urlPattern = re.compile('viewthread\.php\?tid=.*page%3D[\d$]')
            titlePattern = re.compile('>.*</a>') #'title=\"([^\"]*)\"')
            urlMatch = urlPattern.findall(match[i])
            print[urlMatch]
            titlematch = titlePattern.findall(match[i])
            title = titlematch[0][1:titlematch[0].index('</a>')]
            print(title)
            forumurl = urlMatch[0]
            try:
                m = urllib2.urlopen('http://bbs.lbsyun.baidu.com/' + forumurl,'w+').read()
            except:
                continue
            if not os.path.exists(sName):
                os.makedirs(sName)
            f = open(sName + '/' + base64.urlsafe_b64encode(title) + '.html','w')
            if len(m) > 0:
                if len(title) > 0:
                    f.write(m)
            f.close()



def enValidateFileName(name):
    #\/：*？“<>| file name shouldn't contain these chars.
    name = name.replace('\\','')
    name = name.replace('/','')
    name = name.replace(':','')
    name = name.replace('*','')
    name = name.replace('?','')
    name = name.replace('\"','')
    name = name.replace('<','')
    name = name.replace('>','')
    name = name.replace('|','')
    name = name.replace('\\','')

            
mystr = ">abc</a>"

start = mystr.index('>')
end = mystr.index('</a>')
print(mystr[1:end]) 
 
#-------- 在这里输入参数 ------------------

# LBS web api forum site:
#bdurl = 'http://bbs.lbsyun.baidu.com/forumdisplay.php?fid=7&page='
#iPostBegin = 1
#iPostEnd = 10
bdurl = str(raw_input(u'请LBS forum的地址，去掉page=后面的数字：\n'))
begin_page = int(raw_input(u'请输入开始的页数：\n'))
end_page = int(raw_input(u'请输入终点的页数：\n'))
#-------- 在这里输入参数 ------------------
 

#调用

lbs_forum(bdurl,begin_page,end_page)

