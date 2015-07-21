# -*- coding: utf-8 -*-
'''
程序：focus on positions
版本：0.1
@author: Michael Wan
日期：2015.07.20
语言：Python 2.7
操作：输入带分页的地址，去掉最后面的数字，设置一下起始页数和终点页数。
功能：下载对应页码内的所有页面并存储为html文件。
@see: http://blog.csdn.net/pleasecallmewhy/article/details/8927832
'''
 
import re
import string, urllib2
import sys
import time
import urllib

import crawler
from crawler.myemail import send_mail


#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
#sys.setdefaultencoding('utf8')
crawler.logger.info(sys.getdefaultencoding())
mailto_list=['onehaojacket@gmail.com', '174823192@qq.com','onehaobaidu@163.com','wanhao01@baidu.com'] 

class MyHTTPRedirectHandler(urllib2.HTTPRedirectHandler):
    def http_error_302(self, req, fp, code, msg, headers):
        print "Cookie Manip Right Here"
        return urllib2.HTTPRedirectHandler.http_error_302(self, req, fp, code, msg, headers)

    http_error_301 = http_error_303 = http_error_307 = http_error_302


def getByrPositions(urls,title):
    #url = 'http://bbs.lbsyun.baidu.com/forum.php?mod=forumdisplay&fid=7&page='
    result = ""
    result2 = []
    crawler.logger.info("test")
    cookieprocessor = urllib2.HTTPCookieProcessor()

    opener = urllib2.build_opener(MyHTTPRedirectHandler, cookieprocessor)
    urllib2.install_opener(opener)

    #section pattern for posts
    pattern = re.compile('<td class=\"title_9\">.*?</td>',re.DOTALL)
    patternTitle = re.compile('<td class="title_10">.*?</td>',re.DOTALL)
    for url in urls:
#         sName = string.zfill(1,5) #+ '.html'#自动填充成六位的文件名
        crawler.logger.info('正在下载第' + str(1) + '个网页，并将其存储为' + url + '......')
        
        #f = open(sName,'w+')
        m = urllib2.urlopen(url).read()
        
        a = "<dl class=\"list-bigpic clearfix\" >[]<dt>"
        # 将正则表达式编译成Pattern对象

        match = pattern.findall(m)
        matchTitle = patternTitle.findall(m)
        
        print(len(match))
        crawler.logger.info(match)
        for i in range(0,len(match)-1):
            try:
                match[i] = match[i].replace("<td class=\"title_9\">","")
                match[i] = match[i].replace("</td>","")
                matchTitle[i] = matchTitle[i].replace("<td class=\"title_10\">","")
                matchTitle[i] = matchTitle[i].replace("</td>","")
                
                result += match[i].decode("GB2312") + ' ----- ' + matchTitle[i] + "<br />"
            except:
                continue
    result = result.replace('/nForum', 'http://newsmth.net/nForum')
    if send_mail(mailto_list, 'BYR _ ' + title+"_"+ time.strftime("%Y-%m-%d", time.localtime()), result):
        crawler.logger.info("发送成功".decode('utf8'))
    else:  
        crawler.logger.error('发送失败'.decode('utf8'))
    
    
def getNewsmthPositions(urls,title):
    #url = 'http://bbs.lbsyun.baidu.com/forum.php?mod=forumdisplay&fid=7&page='
    result = ""
    result2 = []
    crawler.logger.info("test")

    #section pattern for posts
    pattern = re.compile('<td class=\"title_9\">.*?</td>',re.DOTALL)
    patternTitle = re.compile('<td class="title_10">.*?</td>',re.DOTALL)
    for url in urls:
#         sName = string.zfill(1,5) #+ '.html'#自动填充成六位的文件名
        crawler.logger.info('正在下载第' + str(1) + '个网页，并将其存储为' + url + '......')
        
        #f = open(sName,'w+')
        m = urllib2.urlopen(url).read()
        
        a = "<dl class=\"list-bigpic clearfix\" >[]<dt>"
        # 将正则表达式编译成Pattern对象

        match = pattern.findall(m)
        matchTitle = patternTitle.findall(m)
        
        print(len(match))
        crawler.logger.info(match)
        for i in range(0,len(match)-1):
            try:
                match[i] = match[i].replace("<td class=\"title_9\">","")
                match[i] = match[i].replace("</td>","")
                matchTitle[i] = matchTitle[i].replace("<td class=\"title_10\">","")
                matchTitle[i] = matchTitle[i].replace("</td>","")
                
                result += match[i].decode("GB2312") + ' ----- ' + matchTitle[i] + "<br />"
            except:
                continue
    result = result.replace('/nForum', 'http://newsmth.net/nForum')
    if send_mail(mailto_list, '水木 _ ' + title+"_"+ time.strftime("%Y-%m-%d", time.localtime()), result):
        crawler.logger.info("发送成功".decode('utf8'))
    else:  
        crawler.logger.error('发送失败'.decode('utf8'))
      
smthUrls = ['http://newsmth.net/nForum/s/article?t1=%25E6%25B5%258B%25E8%25AF%2595&au=&b=Career_Upgrade'
            ,'http://newsmth.net/nForum/s/article?ajax=&t1=%25E6%25B5%258B%25E8%25AF%2595&au=&b=Career_Upgrade&p=2'
            ,'http://newsmth.net/nForum/s/article?t1=amazon&au=&b=Career_Upgrade'
            ,'http://newsmth.net/nForum/s/article?t1=vmware&au=&b=Career_Upgrade'
            ,'http://newsmth.net/nForum/s/article?t1=microsoft&au=&b=Career_Upgrade'
            ,'http://newsmth.net/nForum/s/article?t1=%25E8%2585%25BE%25E8%25AE%25AF&au=&b=Career_Upgrade' #腾讯
            ,'http://newsmth.net/nForum/s/article?t1=%25E9%2598%25BF%25E9%2587%258C&au=&b=Career_Upgrade' #阿里
            ,'http://newsmth.net/nForum/s/article?t1=%25E7%2599%25BE%25E5%25BA%25A6&au=&b=Career_Upgrade'] #baidu
getNewsmthPositions(smthUrls,'newsmth - QA')

byrUrls = ['http://bbs.byr.cn/s/article?t1=%25E6%25B5%258B%25E8%25AF%2595&au=&b=JobInfo' #test
           ,'http://bbs.byr.cn/s/article?t1=%25E6%25B5%258B%25E8%25AF%2595&au=&b=JobInfo&_uid=teragrid&p=2'
           ,'http://bbs.byr.cn/s/article?t1=%25E6%25B5%258B%25E8%25AF%2595&au=&b=JobInfo&_uid=teragrid&p=3'
           ,'http://bbs.byr.cn/s/article?t1=amazon&au=&b=JobInfo'
           ,'http://bbs.byr.cn/s/article?t1=vmware&au=&b=JobInfo'
           ,'http://bbs.byr.cn/s/article?t1=%25E7%2599%25BE%25E5%25BA%25A6&au=&b=JobInfo'
           ,'http://bbs.byr.cn/s/article?t1=microsoft&au=&b=JobInfo']
getByrPositions(byrUrls, 'BYR - QA')
# m58urlsHTC = ['http://bj.58.com/changping/htc/pve_5537_101_200/?PGTID=14241570060020.31668903841637075&ClickID=2','http://bj.58.com/haidian/htc/pve_5537_101_200/?PGTID=14241570096550.9543016497045755&ClickID=2']
# getNewsmthPositions(m58urlsHTC,'58.com -- HTC')
# ganjiurls = ['http://bj.ganji.com/mi/changping/p2/?p=','http://bj.ganji.com/mi/changping/p2/?p=']
# getGanjiItems(ganjiurls,"ganjiMIUI",2)
# ganjiurlsHTC = ['http://bj.ganji.com/htc/haidian/p2/', 'http://bj.ganji.com/htc/changping/p2/']
# getGanjiItems(ganjiurlsHTC,"ganjiHTC",2)
#raw_input()