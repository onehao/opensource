# -*- coding: utf-8 -*-

''' tutorial.pdf'''
''' python version 2.7 '''
'''
@author: Michael Wan
@since: 2014-12-29
'''
'''Working with Binary Data Record layouts'''

import bisect
from heapq import heapify, heappush, heappop
import logging
import threading, zipfile


class AsyncZip(threading.Thread):
    def __init__(self, infile, outfile):
        threading.Thread.__init__(self)
        self.infile = infile
        self.outfile = outfile
    def run(self):
        f = zipfile.ZipFile(self.outfile, 'w', zipfile.ZIP_DEFLATED)
        f.write(self.infile)
        f.close()
        print 'Finished background zip of: ', self.infile
background = AsyncZip('__init__.py', 'newmkdir/myarchive.zip')
background.start()
print 'The main program continues to run in foreground.'
background.join() # Wait for the background task to finish
print 'Main program waited until background was done.'


'''Logging'''
logging.debug('Debugging information')
logging.info('Informational message')
logging.warning('Warning:config file %s not found', 'server.conf')
logging.error('Error occurred')
logging.critical('Critical error -- shutting down')


'''Tools for Working with Lists'''
#In addition to alternative list implementations, the library also offers other tools such as the bisect module with
#functions for manipulating sorted lists:
scores = [(100, 'perl'), (200, 'tcl'), (400, 'lua'), (500, 'python')]
bisect.insort(scores, (300, 'ruby'))
print(scores)
data = [1, 3, 5, 7, 9, 2, 4, 6, 8, 0]
heapify(data) # rearrange the list into heap order
heappush(data, -5) # add a new entry
print([heappop(data) for i in range(3)]) # fetch the three smallest entries

