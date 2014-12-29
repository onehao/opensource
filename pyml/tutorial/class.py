# -*- coding: utf-8 -*-

''' tutorial.pdf'''
''' python version 2.7 '''
'''
@author: Michael Wan
@since: 2014-12-29
'''

import glob
from math import sin, pi


class MyClass:
    """This is the first class written by Michael"""
    def __init__(self, test):
        self.i = test
    i = 12345
    def f(self):
        return 'Hello World'
    
class Base:
    def baseFunction1(self):
        print("this is calling from base bsseFunction1")
    def baseFunction2(self):
        return "base function2"
    
class Base2:
    def baseFunction1(self):
        print('this is calling from Base2 basefunction1')
    
class DerivedClass(Base2,Base):
    def callBase(self):
        Base.baseFunction1(self)
        print('this is calling from DerivedClass callBase')
 
class Reverse:
    """Iterator for looping over a sequence backwards."""
    def __init__(self, data):
        self.data = data
        self.index = len(data)
    def __iter__(self):
        return self
    def next(self):
        if self.index == 0:
            raise StopIteration
        self.index = self.index - 1
        return self.data[self.index]    
    
rev = Reverse('spam')
print(iter(rev)) 
for char in rev:
    print(char)

for line in open("class.py"):
    print line,
    
def reverse(data):
    for index in range(len(data)-1, -1, -1):
        yield data[index]
 
for char in reverse('golf'):
    print(char)
   

dc = DerivedClass();
dc.callBase()
'''depth-first, left-to-right.'''
dc.baseFunction1()

x = MyClass(1234)
print(x.f())
print(x.i)
print(MyClass.f(x))


xvec = [10, 20, 30]
yvec = [7, 5, 3]
print(sum(x*y for x,y in zip(xvec, yvec)))

sine_table = dict((x, sin(x*pi/180)) for x in range(0, 91))
print(sine_table)
