# -*- coding: utf-8 -*-
'''
Created on 2016年5月9日

@author: michael.wh
'''

import unittest

class Base1(unittest.TestCase):
#     def __init__(self, methodName='runTest'):
#         unittest.TestCase.__init__(self,'runTest')
#         print('__init__ - this  is Base1')
    
    @classmethod
    def setUpClass(cls):
        print('setupClass - this is Base1')

    @classmethod
    def tearDownClass(cls):
        print('tearDownClass - this is Base1')