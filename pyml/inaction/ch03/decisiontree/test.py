#coding=utf-8  
'''
@author: Michael Wan
@since: 2014-11-08
'''
import trees as tr
import treePlotter as tp
import sys
reload(sys)
#http://www.pythoner.com/200.html
#运行时没问题， 但是编译有有错误提示， 可以忽略。  
sys.setdefaultencoding('utf8')
print(sys.getdefaultencoding())




#
fr = open('lensesCN.txt')
lenses = [unicode(inst, 'utf-8').strip().strip().split('\t') for inst in fr.readlines()]
#lensesLabels = ["年龄组" , "规定", "闪光", "泪液扫除率"]
lensesLabels = ['age' , 'prescript', 'astigmatic', 'tearRate']
lensesTree = tr.createTree(lenses,lensesLabels)
print(lensesTree)
tp.createPlot(lensesTree)

dataSet, labels = tr.createDataSet()

shannonEnt = tr.calcShannonEnt(dataSet)

print(shannonEnt)

print(tp.retrieveTree(1))

myTree = tp.retrieveTree(0)
numLeafs = tp.getNumLeafs(myTree)
treeDepth = tp.getTreeDepth(myTree)

print(numLeafs)
print(treeDepth)


myTree = tp.retrieveTree(0)
tp.createPlot(myTree)

myTree['no surfacing'][3] = 'maybe'
tp.createPlot(myTree)

myDat,labels = tr.createDataSet()
print(labels)
myTree = tp.retrieveTree(0)
print(myTree)
print(tr.classify(myTree, labels, [1,0]))
print(tr.classify(myTree, labels, [1,1]))


#restore the tree and print.
restoreTree = tr.grabTree('classifierStorage.txt')
print(restoreTree)


#
fr = open('lenses.txt')
lenses = [inst.strip().strip().split('\t') for inst in fr.readlines()]
lensesLabels = ['age' , 'prescript', 'astigmatic', 'tearRate']
lensesTree = tr.createTree(lenses,lensesLabels)
print(lensesTree)

tp.createPlot(lensesTree)


#
fr = open('lensesCN.txt')
lenses = [unicode(inst, 'utf-8').strip().strip().split('\t') for inst in fr.readlines()]
lensesLabels = ["年龄组" , "规定", "闪光", "泪液扫除率"]
lensesTree = tr.createTree(lenses,lensesLabels)
print(lensesTree)
tp.createPlot(lensesTree)