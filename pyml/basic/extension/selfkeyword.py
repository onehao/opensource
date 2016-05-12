#!/usr/bin/python
# -*- coding: utf-8 -*-
"""
-------------------------------------------------------------------------------
Function:
【整理】Python中：self和init__的含义 + 为何要有self和__init__
http://www.crifan.com/summary_the_meaning_of_self_and___init___in_python_and_why_need_them
  
Author:     Crifan
Verison:    2012-11-27
-------------------------------------------------------------------------------
"""
  
#注：此处全局的变量名，写成name，只是为了演示而用
#实际上，好的编程风格，应该写成gName之类的名字，以表示该变量是Global的变量
name = "whole global name";
  
class Person:
    name = "class global name"
  
    def __init__(self, newPersionName):
        #self.name = newPersionName;
          
        #此处，没有使用self.name
        #而使得此处的name，实际上仍是局部变量name
        #虽然此处赋值了，但是后面没有被利用到，属于被浪费了的局部变量name
        self.name = newPersionName;
  
    def sayYourName(self):
        #此处，之所以没有像之前一样出现：
        #AttributeError: Person instance has no attribute 'name'
        #那是因为，虽然当前的实例self中，没有在__init__中初始化对应的name变量，实例self中没有对应的name变量
        #但是由于实例所对应的类Person，有对应的name变量,所以也是可以正常执行代码的
        #对应的，此处的self.name，实际上是Person.name
        print 'My name is %s'%(self.name); # -> class global name
        print 'name within class Person is actually the global name: %s'%(name); #-> whole global name
        print "only access Person's name via Person.name=%s"%(Person.name); # -> class global name
    
    def changeGlobalName(self,newName):
        global name 
        name = "new class  global name"

class Child(Person):
    def say(self):
        #此处，之所以没有像之前一样出现：
        #AttributeError: Person instance has no attribute 'name'
        #那是因为，虽然当前的实例self中，没有在__init__中初始化对应的name变量，实例self中没有对应的name变量
        #但是由于实例所对应的类Person，有对应的name变量,所以也是可以正常执行代码的
        #对应的，此处的self.name，实际上是Person.name
        print 'My name is %s'%(self.name); # -> class global name
        print 'name within class Person is actually the global name: %s'%(name); #-> whole global name
        print "only access Person's name via Person.name=%s"%(Person.name); # -> class global name
    #def __init__(self):
        


  
def selfAndInitDemo():
    persionInstance = Person("crifan");
    persionInstance.sayYourName();
    personInstance2 = Person("michael")
    personInstance2.sayYourName()
    personInstance2.changeGlobalName("newName")
    personInstance2.sayYourName()
    print "whole global name is %s"%(name); # -> whole global name
    child = Child('child')
    child.say()

  
###############################################################################
if __name__=="__main__":
    selfAndInitDemo();
