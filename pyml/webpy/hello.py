# -*- coding: utf-8 -*-
'''
Created on 2016年5月10日

@author: michael.wh
'''
import web

urls = ("/.*", "hello")
app = web.application(urls, globals())

class hello:
    def GET(self):
        return 'Hello, world!'

if __name__ == "__main__":
    app.run()