# -*- coding: utf-8 -*-

''' tutorial.pdf'''
''' python version 2.7 '''
'''
@author: Michael Wan
@since: 2014-12-29
'''
'''Operating System Interface'''

from datetime import date
import datetime
import doctest
import glob
import math
import os
import random
import re
import shutil
import sys
from timeit import Timer
import unittest
import urllib2
import zlib


print(os.getcwd())
os.system('mkdir newmkdir')
print(os.system('ipconfig'))
print(help(os))
print(dir(os))

shutil.copy('__init__.py', '__init__.copy')
shutil.move('__init__.copy', 'newmkdir/__init__.copy')

'''FileWildcards'''
print(glob.glob('*.py'))

'''Command Line Arguments'''
print(sys.argv)


'''Error Output Redirection and Program Termination'''
sys.stderr.write('this is testing stderror')


'''String Pattern Matching'''
print(re.findall(r'\bf[a-z]*', 'which foot or hand fell fastest'))


'''Mathematics'''
print(math.cos(math.pi / 4.0))
print(random.choice(['apple', 'pear', 'banana']))
print(random.sample(xrange(100), 10))
#random float
print(random.random())
# random integer chosen from range(6)
print(random.randrange(6))


'''Internet Access'''
for line in urllib2.urlopen('http://scripts.irssi.org/scripts/timer.pl'):
    if '<div id="time">' in line or 'EDT' in line: # look for Eastern Time
        print(line)

'''Dates and Times'''
print(date.today())
print(datetime.date(2014,12,29))
print(date.today().strftime("%m-%d-%y. %d %b %Y is a %A on the %d day of %B."))
# dates support calendar arithmetic
birthday = date(1983, 8, 29)
age = date.today() - birthday
print(age.days)
birthday = date(1985, 7, 14)
age = date.today() - birthday
print(age.days)


'''Data Compression'''
s = 'witch which has which witches wrist watch'
print(len(s))
t = zlib.compress(s)
print(len(t))
print(t)
print(zlib.decompress(t))
print(zlib.crc32(s))


'''Performance Measurement'''
print(Timer('t=a; a=b; b=t', 'a=1; b=2').timeit())
print(Timer('a,b = b,a', 'a=1; b=2').timeit())


'''Quality Control'''
def average(values):
    """Computes the arithmetic mean of a list of numbers.
    >>> print average([20, 30, 70])
    40.0
    """
    return sum(values, 0.0) / len(values)

doctest.testmod()

class TestStatisticalFunctions(unittest.TestCase):
    def test_average(self):
        self.assertEqual(average([20, 30, 70]), 40.0)
        self.assertEqual(round(average([1, 5, 7]), 1), 4.3)
        with self.assertRaises(ZeroDivisionError):
            average([])
        with self.assertRaises(TypeError):
            average(20, 30, 70)
unittest.main()