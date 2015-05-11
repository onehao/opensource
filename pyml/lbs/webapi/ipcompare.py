#coding=utf-8  
'''
@author: Michael Wan
@since: 2015-02-13
'''
import httplib
import json
import random
import re
import sys
import time
import urllib2


reload(sys)
sys.setdefaultencoding('utf8')

def getRandomIP():
    file_read = open('china.ip', 'r')
    list = file_read.readlines()
    file_read.close()
    file_write = open('ipcompare.csv', 'w')
    file_write.write("ip,LBS ip定位API, IP138,133ip,http://ip.chinaz.com/ \n".encode("gb2312")) 
    file_write.flush()
    cookiecoiunt = 1
    for ipraw in list:
#         if(cookiecoiunt % 5 == 0):
#             time.sleep(2.34)
        print(ipraw.split("\n")[0].rstrip())
        ip = ipraw.split("\n")[0].rstrip()
        iplocation = "/location/ip?ak=wOexvA0egnE0qUUWHYcyY4wX&ip=" + ip
        result = getRequest("api.map.baidu.com", iplocation)
        location = json.loads(result)
        lbsiplocation = ""
        try:
            province = location['content']['address_detail']['province']
            city = location['content']['address_detail']['city']
            district = location['content']['address_detail']['district']
            street = location['content']['address_detail']['street']
            print(province + "，" + city + "，" + district + "，"+ street)
            lbsiplocation = province + "，" + city + "，" + dist rict + "，" + street
        except:
            print('request: ' + iplocation)
            print('result: ' + result)
        
        
        #http://www.133ip.com/gn/jk.php?an=1&q=61.135.169.78
        ip133context = "/gn/jk.php?an=1&q=" + ip
        result = getRequest("www.133ip.com", ip133context)
        location = json.loads(result)
        ip133location = ""
        try:
            ip133location = location['s1']
        except:
            print('request: ' + ip133location)
            print('result: ' + result)
        
        #<strong class="red">查询结果[1]: 222.199.0.4 ==&gt;&gt; 3737583620 ==&gt;&gt; 山西省太原市 山西工程职业技术学院</strong>
        chinazcontext = "http://ip.chinaz.com/?IP="
        chinazLocation = ""
        ip138Location = ""
        headers = { 'User-Agent' : 'Mozilla/4.0 (compatible; MSIE 5.5; Windows NT)', 'Cookie' : str(cookiecoiunt) + ""}
        try:
            
            #<ul class="ul1"><li>本站主数据：湖南省湘潭市  电信</li><li>参考数据一：湖南省湘潭市 电信</li></ul>
            ip138context = "http://www.ip138.com/ips138.asp?ip=" + ip + "&action=2"
            m = urllib2.urlopen(ip138context).read().decode("gb2312")
            #print(m)
            pattern = re.compile(r'<ul[\s]+class=\"ul1\">.*</ul>')
            match = pattern.findall(m)
            pattern = re.compile(r"<li>[^<li>]*</li>")
            result = pattern.findall(match[0])
            print(result[0][13:-5] + "---------------ip138--------------")
            ip138Location =result[0][13:-5] 
            
            
            
            request = urllib2.Request(chinazcontext + ip, headers={})
            m = urllib2.urlopen(request).read()
            #print(m)
            patternstr = "<strong[\s]+class=\"red\">查询结果.*</strong>"
            pattern = re.compile(r'<strong class=\"red\">查询结果.*</strong>')
            match = pattern.findall(m)
            pattern = re.compile(r"==>> .*</strong>")
            result = pattern.findall(match[0])
            print(result[0][20:-9] + "-----------chinaz----------------")
            chinazLocation = result[0][20:-9]
        except:
            print('result: ' + result[0])
            print(m)
            
        item= ""
        try:
            item = (ip + "," + lbsiplocation + "," + ip138Location + "," + ip133location + "," + chinazLocation + "\n").encode("gb2312")
        except:
            continue  
        
        file_write.write(item) 
        print("SUCCESS----------------------")
        file_write.flush()
        cookiecoiunt = cookiecoiunt+1
#         time.sleep(1)  
    #print(list)
#     file_write = open('random.ip', 'w')
#     size = len(list) - 1
#     for i in range(1, 100):
#         r = random.randint(0, size)
#         file_write.write(list[r]) 
    
    file_write.close()     
def getRequest(requestUrl,context):
    conn = httplib.HTTPConnection(requestUrl)
    conn.request("GET", context)
    r1 = conn.getresponse()
    print(r1.status, r1.reason)
    data1 = r1.read()
    print(data1)
    conn.close()
    return data1    
getRandomIP()