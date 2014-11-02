from numpy import *;
import kNN
from matplotlib import *;
import matplotlib.pyplot as plt

group,labels = kNN.createDatSet()

print(group)

print(labels)

print(kNN.classify0([0][0], group, labels, 3))


datingDataMat,datingLabels = kNN.file2matrix('datingTestSet2.txt')

print('-------datingDataMat-------------')
print(datingDataMat)
print('-------datingLabels-------------')
print(datingLabels)

print('-------datingLabels[0:20]-------------')
print(datingLabels)
print('-------datingLabels[0:-1]-------------')
print(datingLabels)
  
print(random.rand(4,4));


##matplotlib start
fig = plt.figure()
ax = fig.add_subplot(111)
# ax.scatter(datingDataMat[:,1],datingDataMat[:,2])
# plt.show()


ax.scatter(datingDataMat[:,1], datingDataMat[:,2],15.0*array(datingLabels), 15.0*array(datingLabels))
plt.show();

print('-------datingClassTest-------------')
kNN.datingClassTest();
print('-------classifyPerson-------------')
kNN.classifyPerson()


