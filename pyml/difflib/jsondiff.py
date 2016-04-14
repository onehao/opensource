# -*- coding: utf-8 -*-
'''
Created on 2016年4月14日

@author: michael.wh
'''
from difflib import SequenceMatcher, Differ
from pprint import pprint


class MyJsonDiff:
    def __init__(self, str1, str2):
        self.str1 = str1
        self.str2 = str2
        self.sequenceMatcher = SequenceMatcher(lambda x: x == " ",self.str1, self.str2)
        self.differ = Differ()
        
    def diffratio(self):
        return round(self.sequenceMatcher.ratio(), 5)
    
    def getMatcher(self):
        return self.sequenceMatcher
    
    def getDiffList(self):
        return list(self.differ.compare(self.str1, self.str2))
      
if __name__ == '__main__':
    str1 = '{"code":1"items":[],"batch_id":"HQOYL2hF-9ay3-IFq8-mqCX-mfKGXk2P87j5","message":"Successful.","version":"m-1.0-SNAPSHOT-1921","timestamp":"1460537067","result":true,"done":1,"ver":"Vw4G66VNk2dhfn9f",}'
    str2 = '{"code":1,"message":"Successful.","timestamp":"1460537067.229","version":"m-1.0-SNAPSHOT-1921","result":true,"done":1,"ver":"Vw4G66VNk2dhfn9f","items":[],"batch_id":"HQOYL2hF-9ay3-IFq8-mqCX-mfKGXk2P87j4"}'
    jsondiff = MyJsonDiff(str1,str2);
    #结果受顺序影响
    print(jsondiff.diffratio())
    pprint(jsondiff.getDiffList())
