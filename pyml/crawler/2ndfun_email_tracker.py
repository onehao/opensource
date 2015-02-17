# -*- coding: utf-8 -*-
'''
程序：focus on Mi1 on ganji & 58
版本：0.1
@author: Michael Wan
日期：2015.02.16
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


#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
#sys.setdefaultencoding('utf8')
crawler.logger.info(sys.getdefaultencoding())
mailto_list=['onehaojacket@gmail.com', '174823192@qq.com','onehaobaidu@163.com','wanhao01@baidu.com'] 


def getGanjiItems(urls,title,page):
    #url = 'http://bbs.lbsyun.baidu.com/forum.php?mod=forumdisplay&fid=7&page='
    result = ""
    result2 = []
    crawler.logger.info("test")

    #section pattern for genji
    pattern = re.compile('<dl[\s]+class=\"list-bigpic[\s]+clearfix\" >.*?</dl>',re.DOTALL)
    patternTitle = re.compile('<a href.*?</a>',re.DOTALL)
    patternComment = re.compile("<p.*?</p>")
    patternDate = re.compile("<i class=\"mr8\">.*?</i>")
    for url in urls:
        for i in range(1, page):
            sName = string.zfill(i,5) #+ '.html'#自动填充成六位的文件名
            crawler.logger.info('正在下载第' + str(i) + '个网页，并将其存储为' + sName + '......')
            
            #f = open(sName,'w+')
            m = urllib2.urlopen(url + str(i)).read()
            
            a = "<dl class=\"list-bigpic clearfix\" >[]<dt>"
            # 将正则表达式编译成Pattern对象
    
            match = pattern.findall(m)
            
            print(len(match))
    #         match = re.findall(regex,m)
    #         print(match)
    #         match = re.compile(regex).search(m)
    #         print(match)
    
            #match = pattern2.findall(match)
            crawler.logger.info(match)
            result2 +=match
            for i in range(0,len(match)-1):
                itemDate = patternDate.findall(match[i])
                itemTitle = patternTitle.findall(match[i])
                try:
                    itemTitle[0] = itemTitle[0].replace(" class=\"ft-tit\"","")
                    comment = patternComment.findall(match[i])
                    
                    result += itemTitle[0] + " (" + comment[0] + " --- " + comment[1] +  ") Date: " + itemDate[0] + "<br />"
                except:
                    continue
    if send_mail(mailto_list, '小米1 _ ' + title+"_"+ time.strftime("%Y-%m-%d", time.localtime()), result):
        crawler.logger.info("发送成功".decode('utf8'))
    else:  
        crawler.logger.error('发送失败'.decode('utf8'))
    #send_mail(mailto_list, 'LBS WEB API forum _2' + time.strftime("%Y-%m-%d", time.localtime()), result2)
    
    
def get58Items(urls,title):
    #url = 'http://bbs.lbsyun.baidu.com/forum.php?mod=forumdisplay&fid=7&page='
    result = ""
    result2 = []
    crawler.logger.info("test")

    #section pattern for genji
    pattern = re.compile('<tr logr.*?</tr>',re.DOTALL)
    patternTitle = re.compile('<td class=\"t\">.*?</td>',re.DOTALL)
    patternPrice = re.compile("<b class=\'pri\'>.*?</b>")
    patternDate = re.compile("<i class=\"mr8\">.*?</i>")
    for url in urls:
        sName = string.zfill(1,5) #+ '.html'#自动填充成六位的文件名
        crawler.logger.info('正在下载第' + str(1) + '个网页，并将其存储为' + sName + '......')
        
        #f = open(sName,'w+')
        m = urllib2.urlopen(url).read()
        
        a = "<dl class=\"list-bigpic clearfix\" >[]<dt>"
        # 将正则表达式编译成Pattern对象

        match = pattern.findall(m)
        
        print(len(match))
#         match = re.findall(regex,m)
#         print(match)
#         match = re.compile(regex).search(m)
#         print(match)

        #match = pattern2.findall(match)
        crawler.logger.info(match)
        result2 +=match
        for i in range(0,len(match)-1):
            itemDate = patternDate.findall(match[i])
            itemTitle = patternTitle.findall(match[i])
            try:
                itemTitle[0] = itemTitle[0].replace(" class=\"ft-tit\"","")
                price = patternPrice.findall(match[i])
                
                result += itemTitle[0] + " (" + price[0] +  " 元)  " + "<br />"
            except:
                continue
    if send_mail(mailto_list, '小米1 _ ' + title+"_"+ time.strftime("%Y-%m-%d", time.localtime()), result):
        crawler.logger.info("发送成功".decode('utf8'))
    else:  
        crawler.logger.error('发送失败'.decode('utf8'))
        
m58urlsMIUI = ['http://bj.58.com/xiaomi/pve_5537_101_200/?PGTID=14241553799050.06778648379258811&ClickID=2','http://bj.58.com/haidian/xiaomi/pve_5537_101_200/?PGTID=14241558490290.5991021553054452&ClickID=2','http://bj.58.com/changping/xiaomi/pve_5537_101_200/?PGTID=14241568309340.7953142840415239&ClickID=2']
get58Items(m58urlsMIUI,'58.com -- MIUI')
m58urlsHTC = ['http://bj.58.com/changping/htc/pve_5537_101_200/?PGTID=14241570060020.31668903841637075&ClickID=2','http://bj.58.com/haidian/htc/pve_5537_101_200/?PGTID=14241570096550.9543016497045755&ClickID=2']
get58Items(m58urlsHTC,'58.com -- HTC')
ganjiurls = ['http://bj.ganji.com/mi/changping/p2/?p=','http://bj.ganji.com/mi/changping/p2/?p=']
getGanjiItems(ganjiurls,"ganjiMIUI",2)
ganjiurlsHTC = ['http://bj.ganji.com/htc/haidian/p2/', 'http://bj.ganji.com/htc/changping/p2/']
getGanjiItems(ganjiurlsHTC,"ganjiHTC",2)
#raw_input()