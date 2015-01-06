#!/usr/bin/python
# -*- coding: utf-8 -*-
################################################################################
#
# Copyright (c) 2014 Baidu.com, Inc. All Rights Reserved
#
# Author   :  Michael Wan
# Date     :   2015-01-06
#
################################################################################
 
import os
import random
import sys
import time
import urllib
import urllib2
 
class HiFamily:
 
    target_url = 'http://family.baidu.com/comment/comment/productAddComment.do'
    comment_list = ["good!", "ding~", "nice~"]
     
    def __init__(self, url, cookies, comment = "good!", change_comment = True, times = 10,
                interval = 15, min_interval = 15, max_interval = 30, random_interval = False):
        self.url = url
        self.cookies = cookies
        self.comment = comment
        self.change_comment = change_comment
        self.times = times
        self.interval = interval
        self.min_interval = min_interval
        self.max_interval = max_interval
        self.random_interval = random_interval
         
    def run(self):
        for i in range(self.times):
            if self.change_comment:
                self.post(random.choice(HiFamily.comment_list))
            else:
                self.post(self.comment)
            if self.random_interval:
                time.sleep(random.randrange(self.min_interval, self.max_interval))
            else:
                time.sleep(self.interval)
     
    def post(self, comment):
        values = {    'articleId':'707505151',
                    'from':'load',
                    'commentContent':comment,
                    'replyToFloor':'',
                    'commentSide':'1',
                    'extraContent':'',
                    'replyTo':'0'}
        data = urllib.urlencode(values)
        req = urllib2.Request(HiFamily.target_url, data)
 
        req.add_header('Cookie', self.cookies)
        req.add_header('Host','family.baidu.com')
        req.add_header('Origin','http://family.baidu.com')
        req.add_header('Referer', self.url)
 
        response = urllib2.urlopen(req)
        the_page = response.read()
        type = sys.getfilesystemencoding() 
        print the_page.decode("UTF-8").encode(type) 
         
if __name__ == '__main__':
    if len(sys.argv) < 2:
        print "Usage: python hifamily.py InputFile"
        print "In input file, first is article url address, second line is cookies."
        print "doc : http://family.baidu.com/portal/techForum/detail?articleId=707882268"
    elif not os.path.exists(sys.argv[1]):
        print "%s : file dose not exists."
    else:
        print "Thanks for using HiFamily, initing..."
        input_file = open(sys.argv[1], 'r')
        lines = input_file.readlines()
        url = lines[0][:-1]
        cookies = lines[1][:-1]
        hi_family = HiFamily(url, cookies)
        hi_family.run()
        print "=== Done ==="