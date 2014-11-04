from numpy import *;
import kNN
import matplotlib.pyplot as plt



group, labels = kNN.createDatSet()

print(group)

print(labels)

print(group.shape)

#michael test classify
def classifyTest():
    print(kNN.classify0([0][0], group, labels, 3))
    print(kNN.classify0([1][0], group, labels, 3))
    print(kNN.classify0([2][0], group, labels, 3))
    print(kNN.classify0([3][0], group, labels, 3))
    print(kNN.classify0([0][1], group, labels, 4))
    print(kNN.classify0([1][1], group, labels, 3))
    print(kNN.classify0([2][1], group, labels, 3))
    print(kNN.classify0([3][1], group, labels, 3))
    

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
kNN.handwritingClassTest2()

