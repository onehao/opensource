# -*- coding: utf-8 -*-
'''
Created on 2016年5月9日

@author: michael.wh
'''
from Base1 import Base1
import unittest
import re


class Base2(Base1):
#     def __init__(self, methodName='runTest'):
#         Base1.__init__(self, 'runTest')
#         print('__init__ - this  is Base2')
    
    @classmethod
    def setUpClass(cls):
        print('setupClass - this is Base2')
        super(Base2, cls).setUpClass()

    @classmethod
    def tearDownClass(cls):
        print('tearDownClass - this is Base2')
        super(Base2, cls).tearDownClass()
    
    def test_default_size(self):
        self.assertEqual('a','a')

    def test_resize(self):
        self.assertEqual('a','b')
        
if __name__ == '__main__':
    str1 = 'string|sep|by:id:1'
    list1 = re.split('\||:',str1)
    print(str(list1))