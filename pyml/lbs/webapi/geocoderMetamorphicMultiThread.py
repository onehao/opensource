#coding=utf-8  
'''
@author: Michael Wan
@since: 2014-11-13
'''

import httplib
import json
from math import fabs
import sys
import threading
import unittest

import lbslogger


reload(sys)
#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
sys.setdefaultencoding('utf8')
print(sys.getdefaultencoding())



def executeTest(results, lock, item,ak):
    var = item.split(",")
    context = "/geocoder/v2/?pois=0&output=json&ak=" + ak + "&location=" + var[1].replace('\n', '') + "," + var[0]
    result = getRequest("api.map.baidu.com", context)
    resultJsonObject = json.loads(result)
    if(resultJsonObject['status'] != 0):
        raise Exception(resultJsonObject['result'] + '--' + resultJsonObject['message'])
#         lbslogger.logerror(resultJsonObject['result'] + '--' + resultJsonObject['message'])
        print('')
    location = resultJsonObject['result']['formatted_address']
    reverseGeocodingContext = '/geocoder/v2/?output=json&ak=wOexvA0egnE0qUUWHYcyY4wX&address=' + location
    result = getRequest("api.map.baidu.com", context)
    coord = json.loads(result)
    try:
        lng = coord['result']['location']['lng']
        lat = coord['result']['location']['lat']
    except:
        print 'request: ' + reverseGeocodingContext
        print 'result: ' + result
        # make sure the shuffled sequence does not lose any elements
    if fabs(lng - float(var[0])) > 0.001 or fabs(lat - float(var[1])) > 0.0005:
#         if lock.acquire():
#             print '%s get the lock.' % threading.currentThread().getName()
#         results.write('ori:' + str(var[0]) + ',' + str(var[1]) + ' and des:' + str(lng) + ',' + str(lat) + '\n')
        lbslogger.logcritical('ori:' + str(var[0]) + ',' + str(var[1]) + ' and des:' + str(lng) + ',' + str(lat) + '\n')
        
        print '%s release lock...' % threading.currentThread().getName()
#             lock.release()

def geocoderTest():
    global results
    count = 0
    results = open("./results", 'w')
    lock = threading.Lock()
    #test aks
    aks = ['vECxaR3X2rxKiNhBNeuYtsWv','pII0GrTCm3QEUPAfGhDkKLOO','ZAx1xPgWDssewnwfbeVl3GBH','4ruNDn5crmwqugzhDMaTfQ8S'
           ,'HjyLoNejFfRWY6t620Bx98gm','aifExz5WqcfRC9rG1VNQFj2r','c1aFgdE2G3DaKwO2Vlzi5Gjz','ybshwoCMjCReo7sOGlqbbHkx'
           ,'kZhvkb6HvtZ4w88FxDnHOnyh','qBqxyhit2GF7yDMGVGMG469G','0bUIy97E5RBI0d1e3onFVeCd','ErcT11Tmqx0eT4XwU7cDEdWg'
           ,'7TcKG9u9N8P63Q3qtLiTG3DL','mWzLRt1YM8vaPcNkIW1oFnFm','H1PGz0wWBqnPHk3t0BaH25Sa','awQg1jKdklDsvL6SjEEGbPs6',
           'DDf9zAMN2O9v6FL1G0mH73eY','8PbxokWSDEP0mHi4LRopK7BG','5lSLAlSyHg3F1lhe7QAF2qzK','KpyESmRoTjBUU1pQ9aIicXuO'
           ,'onedLcl3rkVuUnGDLL6GWq7C','4u8ZcTzf0l5kTuA9BfwoklDG','1uBtSBDY9kwEkKYZDXtoli39','dQSv0mUbkrMu01VEEzYsMzhw'
           ,'0ZdXbwtbExnTAmBmTVamNKj7','QsGAKixvL0KvwwrVgsFhAS0L','BVpVhXeiPYme4MHhyeO24Tbn','NbSqgjrdH7OdiPGxgS1dHBCp'
           ,'r321GiV3ohcAm0DNfzsjL5FM','lyZqLQ8PDzSpjDYaDRTff1WU','BzBW7EpPcg1rZm5DYGE7Rwp8','ELvuz3dlZVTPmPmR1S365ZLH'
           ,'hdVYjG2ydkvUzrXoi2xLokEq','BpoUccQv4TIUscjsHoi5V8oi','mBHB7gELurL3vm4vKF0ltRjd','i6ybgEWRkodcfyBYYc0PKqzh'
           ,'Gvd9cQB7OhnC3mqTFa16Cpt7','KB63nhzczbXIrlAKOuoh9kqO','AtQLN1tbTT66GLFYH0loS1oE','sNGqUGyvZu2YZSUghxgwEYSI'
           ,'LEmEYgCL2f6zwsV3yRePg5zV','8C1rNZK2eohKp0oOpduDDC00','K2svGtQrzghWzFjCIdYApzWQ','dhFRpU9lmtX2vvZgwKjctkcX'
           ,'lSXoUcCoRqx0hk0wRWB4W17y','Ow5fqi6DQXmgD5PGSB7QBdHF']
    threadNum = 46
    for i in range(1, 2):
        file_read = open('locationbd09ll', 'r')
        lines = file_read.readlines()
        for i in range(0, len(lines)-1,threadNum):
            #file_write.write(list[i])
            threads = []
            for t in range(0, threadNum):
                try:
                    thread = threading.Thread(target=executeTest, args=(results,lock,lines[i+t],aks[threadNum % len(aks)]))
                except Exception as ex:
                    lbslogger.logerror(ex.Message)
                    continue
                threads.append(thread)
                thread.start()
                count = count + 1
#                 lbslogger.loginfo(str(count))
                print(str(count))
            waitforcomplete(threads)    
    results.close()   
    
def waitforcomplete(threads):
    while len(threads):  
            worker = threads.pop()  
            worker.join( )  
            if worker.isAlive():
                threads.append( worker ) 
    print('threads completed.')
        

def getRequest(requestUrl,context):
    conn = httplib.HTTPConnection(requestUrl)
    conn.request("GET", context)
    r1 = conn.getresponse()
#     print(r1.status, r1.reason)
    data1 = r1.read()
#     print(data1)
    conn.close()
=======
import socket


reload(sys)
#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
sys.setdefaultencoding('utf8')
print(sys.getdefaultencoding())



def executeTest(results, lock, item,ak):
    var = item.split(",")
    context = "/geocoder/v2/?pois=0&output=json&ak=" + ak + "&location=" + var[1].replace('\n', '') + "," + var[0]
    result = getRequest("api.map.baidu.com", context)
    resultJsonObject = json.loads(result)
    if(resultJsonObject['status'] != 0):
        lbslogger.logerror(resultJsonObject['result'] + '--' + resultJsonObject['message'])
        raise Exception(resultJsonObject['result'] + '--' + resultJsonObject['message'])
#         lbslogger.logerror(resultJsonObject['result'] + '--' + resultJsonObject['message'])
        print('')
    location = resultJsonObject['result']['formatted_address']
    reverseGeocodingContext = '/geocoder/v2/?output=json&ak=' + ak + '&address=' + location
    result = getRequest("api.map.baidu.com", context)
    coord = json.loads(result)
    try:
        lng = coord['result']['location']['lng']
        lat = coord['result']['location']['lat']
    except:
        lbslogger.logerror('request: ' + reverseGeocodingContext)
        lbslogger.logerror('result: ' + result)
        print 'request: ' + reverseGeocodingContext
        print 'result: ' + result
        # make sure the shuffled sequence does not lose any elements
    if fabs(lng - float(var[0])) > 0.001 or fabs(lat - float(var[1])) > 0.0005:
#         if lock.acquire():
#             print '%s get the lock.' % threading.currentThread().getName()
#         results.write('ori:' + str(var[0]) + ',' + str(var[1]) + ' and des:' + str(lng) + ',' + str(lat) + '\n')
        lbslogger.logcritical('ori:' + str(var[0]) + ',' + str(var[1]) + ' and des:' + str(lng) + ',' + str(lat) + '\n')
        
        print '%s release lock...' % threading.currentThread().getName()
#             lock.release()

def geocoderTest():
    global results
    count = 0
    coordfile = 'coreoutputcn'
    lbslogger.loginfo('starting .. ' + coordfile)
    results = open("./results", 'w')
    lock = threading.Lock()
    #test aks
    aks = ['wOexvA0egnE0qUUWHYcyY4wX','pII0GrTCm3QEUPAfGhDkKLOO','ZAx1xPgWDssewnwfbeVl3GBH','4ruNDn5crmwqugzhDMaTfQ8S'
           ,'HjyLoNejFfRWY6t620Bx98gm','aifExz5WqcfRC9rG1VNQFj2r','c1aFgdE2G3DaKwO2Vlzi5Gjz','ybshwoCMjCReo7sOGlqbbHkx'
           ,'kZhvkb6HvtZ4w88FxDnHOnyh','qBqxyhit2GF7yDMGVGMG469G','0bUIy97E5RBI0d1e3onFVeCd','ErcT11Tmqx0eT4XwU7cDEdWg'
           ,'7TcKG9u9N8P63Q3qtLiTG3DL','mWzLRt1YM8vaPcNkIW1oFnFm','H1PGz0wWBqnPHk3t0BaH25Sa','awQg1jKdklDsvL6SjEEGbPs6',
           'DDf9zAMN2O9v6FL1G0mH73eY','8PbxokWSDEP0mHi4LRopK7BG','5lSLAlSyHg3F1lhe7QAF2qzK','KpyESmRoTjBUU1pQ9aIicXuO'
           ,'onedLcl3rkVuUnGDLL6GWq7C','4u8ZcTzf0l5kTuA9BfwoklDG','1uBtSBDY9kwEkKYZDXtoli39','dQSv0mUbkrMu01VEEzYsMzhw'
           ,'0ZdXbwtbExnTAmBmTVamNKj7','QsGAKixvL0KvwwrVgsFhAS0L','BVpVhXeiPYme4MHhyeO24Tbn','NbSqgjrdH7OdiPGxgS1dHBCp'
           ,'r321GiV3ohcAm0DNfzsjL5FM','lyZqLQ8PDzSpjDYaDRTff1WU','BzBW7EpPcg1rZm5DYGE7Rwp8','ELvuz3dlZVTPmPmR1S365ZLH'
           ,'hdVYjG2ydkvUzrXoi2xLokEq','BpoUccQv4TIUscjsHoi5V8oi','mBHB7gELurL3vm4vKF0ltRjd','i6ybgEWRkodcfyBYYc0PKqzh'
           ,'Gvd9cQB7OhnC3mqTFa16Cpt7','KB63nhzczbXIrlAKOuoh9kqO','AtQLN1tbTT66GLFYH0loS1oE','sNGqUGyvZu2YZSUghxgwEYSI'
           ,'LEmEYgCL2f6zwsV3yRePg5zV','8C1rNZK2eohKp0oOpduDDC00','K2svGtQrzghWzFjCIdYApzWQ','dhFRpU9lmtX2vvZgwKjctkcX'
           ,'lSXoUcCoRqx0hk0wRWB4W17y','Ow5fqi6DQXmgD5PGSB7QBdHF']
    threadNum = 46
    for i in range(1, 2):
        file_read = open(coordfile, 'r')
        lines = file_read.readlines()
        for i in range(0, len(lines)-1,threadNum):
            #file_write.write(list[i])
            threads = []
            for t in range(0, threadNum):
                try:
                    thread = threading.Thread(target=executeTest, args=(results,lock,lines[i+t],aks[threadNum % len(aks)]))
                except Exception as ex:
                    lbslogger.logerror(ex.Message)
                    lbslogger.logerror(str(ex))
                    continue
                threads.append(thread)
                thread.start()
                count = count + 1
#                 lbslogger.loginfo(str(count))
                print(str(count))
            waitforcomplete(threads)    
    results.close()   
    
def waitforcomplete(threads):
    while len(threads):  
            worker = threads.pop()  
            worker.join( )  
            if worker.isAlive():
                threads.append( worker ) 
    print('threads completed.')
        

def getRequest(requestUrl,context):
    socket.setdefaulttimeout(10)
    try:
        conn = httplib.HTTPConnection(requestUrl)
        conn.request("GET", context)
        r1 = conn.getresponse()
    #     print(r1.status, r1.reason)
        data1 = r1.read()
    except Exception as ex:
        lbslogger.logerror("error url: " + requestUrl + context)
        lbslogger.logerror(str(ex))
        lbslogger.logerror(str(r1))
        
    finally:
        conn.close()
#     print(data1)
    
>>>>>>> branch 'master' of https://github.com/onehao/opensource.git
    return data1

if __name__ == '__main__':
    geocoderTest()
