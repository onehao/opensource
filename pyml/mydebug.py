from numpy import *;
import kNN

group,labels = kNN.createDatSet()

print(group)

print(labels)

print(kNN.classify0([0][0], group, labels, 3))


datingDataMat,datingLabels = kNN.file2matrix('datingTestSet.txt')

    
print(random.rand(4,4));

