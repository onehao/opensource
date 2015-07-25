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
 
import cookielib
import re
import string, urllib2
import sys
import time
import urllib
import zlib

import crawler
from crawler.myemail import send_mail


#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
#sys.setdefaultencoding('utf8')
crawler.logger.info(sys.getdefaultencoding())
mailto_list=['onehaojacket@gmail.com', 'wanhao01@baidu.com'] 

def getByrPositions(urls,title):
    result = ""
    result2 = []
    crawler.logger.info("test")

    #section pattern for posts
    pattern = re.compile('<td class=\"title_9\">.*?</td>',re.DOTALL)
    patternTitle = re.compile('<td class="title_10">.*?</td>',re.DOTALL)
    send_headers = {
        'Pragma':'no-cache',
        'Cookie':'nforum[BMODE]=2; nforum[XWJOKE]=hoho; Hm_lvt_38b0e830a659ea9a05888b924f641842=1435222944,1435902105,1436853516; Hm_lpvt_38b0e830a659ea9a05888b924f641842=1437619192; login-user=teragrid; nforum[UTMPUSERID]=guest; nforum[UTMPKEY]=45400464; nforum[UTMPNUM]=20385; left-index=0001000000',
        'Accept-Encoding': 'gzip, deflate, sdch',
        'Accept-Language': 'en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4',
        'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36',
        'Accept':'*/*',
        'Cache-Control': 'no-cache',
        'X-Requested-With': 'XMLHttpRequest',
        'Proxy-Connection': 'keep-alive',
        'Referer': 'http://bbs.byr.cn/'
    }

    for url in urls:
#         sName = string.zfill(1,5) #+ '.html'#自动填充成六位的文件名
        crawler.logger.info('正在下载第' + str(1) + '个网页，并将其存储为' + url + '......')
        req = urllib2.Request(url,headers=send_headers)

        urlopen = urllib2.urlopen(req)
        m = urlopen.read()
        #m = zlib.decompress(m, 16+zlib.MAX_WBITS);
        print(m)
        # 将正则表达式编译成Pattern对象

        match = pattern.findall(m)
        matchTitle = patternTitle.findall(m)
        
        print(len(match))
        print(len(matchTitle))
        crawler.logger.info(match)
        for i in range(0,len(match)-1):
            try:
                match[i] = match[i].replace("<td class=\"title_9\">","")
                match[i] = match[i].replace("</td>","")
                matchTitle[i*2] = matchTitle[i*2].replace("<td class=\"title_10\">","")
                matchTitle[i*2] = matchTitle[i*2].replace("</td>","")
                
                result += match[i].decode("GB2312") + ' ----- ' + matchTitle[i] + "<br />"
            except:
                continue
    result = result.replace('/article', 'http://bbs.byr.cn/article')
    print(result)
    if send_mail(mailto_list, 'BYR _ ' + title+"_"+ time.strftime("%Y-%m-%d", time.localtime()), result):
        crawler.logger.info("发送成功".decode('utf8'))
    else:  
        crawler.logger.error('发送失败'.decode('utf8'))
      

byrUrls = ['http://api.map.baidu.com/geodata/v3/poi/detail?geotable_id=106994&ak=Ow5fqi6DQXmgD5PGSB7QBdHF&id=949366265',
           'http://api.map.baidu.com/geodata/v3/poi/detail?geotable_id=106994&id=949366265' #test
           ]
getByrPositions(byrUrls, 'BYR - QA')
