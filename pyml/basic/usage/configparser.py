# -*- coding: utf-8 -*-
'''
Created on 2016年5月12日

@author: michael.wh
'''
import json


str1 = '[1,1,2,3,5,8,13]'
list = json.loads(str1)
print(list[0])
print(str(list))
str2 = '["省","市","区","州","村","镇","乡"]' #"["1/","2","3","4","5"]"
list2 = json.loads(str2,encoding='utf-8')
print(str(list2))
