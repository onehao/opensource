from numpy import *;
import kNN
import matplotlib.pyplot as plt

'''
@author: Michael Wan
@since: 2014-11-01
'''

group, labels = kNN.createDatSet()

print(group)

print(labels)

print(group.shape)

#michael test classify
#training data :
#    group = array([[1.0,1.1],[1.0,1.0],[0,0],[0,0.1]])
#    labels = ['A','A','B','B']
def classifyTest():
    print(kNN.classify0([0,0], group, labels, 3))
    print(kNN.classify0([1,0], group, labels, 3))
    print(kNN.classify0([2,0], group, labels, 3))
    print(kNN.classify0([3,0], group, labels, 3))
    print(kNN.classify0([0,0.1], group, labels, 3))
    print(kNN.classify0([1,0.1], group, labels, 3))
    print(kNN.classify0([2,0.1], group, labels, 3))
    print(kNN.classify0([3,0.1], group, labels, 3))
    print(kNN.classify0([0,1], group, labels, 3))
    print(kNN.classify0([1,1], group, labels, 3))
    print(kNN.classify0([2,1], group, labels, 3))
    print(kNN.classify0([3,1], group, labels, 3))
    print(kNN.classify0([0,2], group, labels, 3))
    print(kNN.classify0([1,3], group, labels, 3))
    print(kNN.classify0([2,4], group, labels, 3))
    print(kNN.classify0([3,5], group, labels, 3))
    print(kNN.classify0([0,0.01], group, labels, 3))
    print(kNN.classify0([1,0.01], group, labels, 3))
    print(kNN.classify0([2,0.01], group, labels, 3))
    print(kNN.classify0([3,0.01], group, labels, 3))
    

classifyTest();

datingDataMat, datingLabels = kNN.file2matrix('datingTestSet2.txt')

print('-------datingDataMat-------------')
print(datingDataMat)
print('-------datingLabels-------------')
print(datingLabels)

print('-------datingLabels[0:20]-------------')
print(datingLabels)
print('-------datingLabels[0:-1]-------------')
print(datingLabels)
  
print(random.rand(4, 4));


# #matplotlib start
fig = plt.figure()
ax = fig.add_subplot(111)
#ax.scatter(datingDataMat[:, 1], datingDataMat[:, 2])
ax.scatter(datingDataMat[:, 1], datingDataMat[:, 2],
15.0 * array(datingLabels), 15.0 * array(datingLabels))
plt.show()

kNN.datingClassTest()

kNN.classifyPerson()

kNN.handwritingClassTest2()

print('-----------kNN.handwritingClassTest2()----------')
print('-----------to see the same training data and test data will still have some incorrect rate.----------')
#to see the same training data and test data will still have some incorrect rate.
kNN.handwritingClassTest2()

