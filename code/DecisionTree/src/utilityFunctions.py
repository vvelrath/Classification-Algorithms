# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__="Vivekanandh"
__date__ ="$Dec 14, 2014 2:48:54 AM$"

if __name__ == "__main__":
    print "Hello World"

import numpy
from collections import Counter

#Finding the number of unique values of a feature in a dataset
def numberOfUniqueVals(dataset, feature):
    dataset_array = numpy.array(dataset)
    return len(set(dataset_array[:,feature]))

#Checking for the same class in the dataset
def checkForSameClass(dataset):
    dataset_array = numpy.array(dataset)
    classIndex = len(dataset_array[0]) - 1
    uniqueClasses = set(dataset_array[:,classIndex])
    
    if(len(uniqueClasses) == 1):
        return 1
    else:
        return 0
    
#Getting the most frequent class in the dataset
def getMostFrequentLabel(dataset):
    dataset_array = numpy.array(dataset)
    classIndex = len(dataset_array[0]) - 1
    counts = Counter(dataset_array[:, classIndex])
    return counts.most_common(1)[0][0]

#Partition dataset using the given feature
def partitionDataSetUsingFeature(dataset, feature):
    partitions = dict()
    dataset_array = numpy.array(dataset)
    uniqueValues = set(dataset_array[:,feature])
    
    #Initialize the partitions(creating empty lists)
    for value in uniqueValues:
       partitions[value] = []
       
    for i in range(len(dataset)):
        partitions[dataset[i][feature]].append(dataset[i])
        
    return partitions