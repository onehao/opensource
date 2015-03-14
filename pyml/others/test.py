#coding:utf-8
import os, sys
import random


file = sys.argv[1]
out_file = sys.argv[2]
out_file2 = sys.argv[3]
open(out_file, 'w').writelines(list(set(open(file).readlines())))
p = ["Python", "is", "powerful", "simple", "and so on..."]  
random.shuffle(p)  

open(out_file, 'w').writelines(list(set(open(file).readlines())))
x = [1, 2, 3, 4, 3, 4, 'a', 'b', 'c']
z = ["Python", "is", "powerful", "simple", "and so on..."]  
y = open(file).readlines()
random.shuffle(y)
open(out_file2, 'w').writelines(set(y))