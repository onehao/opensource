# -*- coding:utf-8 -*-
################################################################################
#
# Copyright (c) 2015 Baidu.com, Inc. All Rights Reserved
#
################################################################################
'''
Created on 2015年3月11日

@author: wanhao01
'''
import Queue
import sys
from threading import Thread 
import threading
import time
import urllib

from crawler.minispider import loginfo
from crawler.minispider import logerror


class CrawlerThread(threading.Thread):
    '''
    mini spider thread encapsulation.
    '''
    worker_count = 0   
    def __init__(self, workQueue, resultQueue, timeout=0, **kwds):   
        Thread.__init__(self, **kwds)   
        self.id = CrawlerThread.worker_count   
        CrawlerThread.worker_count += 1   
        self.setDaemon(True)   
        self.workQueue = workQueue   
        self.resultQueue = resultQueue   
        self.timeout = timeout   
        self.start()
         
    def run(self):   
        ''' the get-some-work, do-some-work main loop of worker threads '''   
        while True:   
            try:   
                callable, args, kwds = self.workQueue.get(timeout=self.timeout)   
                res = callable(*args, **kwds)   
                print(res)
                loginfo("worker[%2d]: %s" % (self.id, str(res)))
                self.resultQueue.put(res)   
            except Queue.Empty:   
                break   
            except:   
                #print 'worker[%2d]' % self.id, sys.exc_info()[:2]  
                logerror('worker[%2d]' % self.id) 
                logerror(str(sys.exc_info()[:2]))
                

class ThreadPool(object):
    '''
    thread pool used to handle multiple thread.
    '''
    def __init__(self, num_of_workers=10, timeout=1):   
        self.workQueue = Queue.Queue()   
        self.resultQueue = Queue.Queue()   
        self.workers = []   
        self.timeout = timeout   
        self._recruitThreads(num_of_workers) 
          
    def _recruitThreads(self, num_of_workers):   
        for i in range(num_of_workers):   
            worker = CrawlerThread(self.workQueue, self.resultQueue, self.timeout)   
            self.workers.append(worker) 
    
    def wait_for_complete(self):
        '''
        the function uses to wait for the exiting of all the threads.
        '''
        while len(self.workers):   
            worker = self.workers.pop()   
            worker.join()   
            if worker.isAlive() and not self.workQueue.empty():   
                self.workers.append(worker)   
        print "All jobs are are completed."  
    
    def add_job(self, callable, *args, **kwds):
        '''
        add job to the thread pool.
        '''
        self.workQueue.put((callable, args, kwds)) 
        
    def get_result(self, *args, **kwds):
        '''
        get the result from the result Queue.
        '''   
        return self.resultQueue.get(*args, **kwds) 
        
    
def save_url(id, sleep=0.001): 
    '''
    test job 
    '''  
    try:   
        data = urllib.urlopen('http://pycm.baidu.com:8081/1/page1_1.html').read()  
        #print(data)
    except:   
        print '[%4d]' % id, sys.exc_info()[:2]   
    return str(id) + data 

   
def test(): 
    '''
    test funciotn 
    '''  
    import socket   
    socket.setdefaulttimeout(10)   
    print 'start testing'   
    wm = ThreadPool(10)   
    for i in range(50):   
        wm.add_job(save_url, i, i*0.001)   
    wm.wait_for_complete()   
    print 'end testing'  
    
if __name__ == '__main__':
    test()
