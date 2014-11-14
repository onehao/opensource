#coding=utf-8  
'''
@author: Michael Wan
@since: 2014-11-13
'''
import json


jsonString = "{\"address\":\"CN|\u5185\u8499\u53e4|\u9521\u6797\u90ed\u52d2\u76df|None|UNICOM|None|None\",\"content\":{\"address\":\"\u5185\u8499\u53e4\u81ea\u6cbb\u533a\u9521\u6797\u90ed\u52d2\u76df\",\"address_detail\":{\"city\":\"\u9521\u6797\u90ed\u52d2\u76df\",\"city_code\":63,\"district\":\"\",\"province\":\"\u5185\u8499\u53e4\u81ea\u6cbb\u533a\",\"street\":\"\",\"street_number\":\"\"},\"point\":{\"x\":\"116.02733969\",\"y\":\"43.93970484\"}},\"status\":0}"

s = json.loads(jsonString)

print(s)
print(s.keys())