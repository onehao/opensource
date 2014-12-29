# -*- coding: utf-8 -*-

''' tutorial.pdf'''
''' python version 2.7 '''
'''
@author: Michael Wan
@since: 2014-12-29
'''

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
    

dc = DerivedClass();
dc.callBase()
'''depth-first, left-to-right.'''
dc.baseFunction1()

x = MyClass(1234)
print(x.f())
print(x.i)
print(MyClass.f(x))

