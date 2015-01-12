# Implementation of the Decision tree algorithm
# File Name : decisiontree.py
# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__="Vivekanandh"
__date__ ="$Dec 12, 2014 9:11:57 PM$"

from informationGain import *
from utilityFunctions import *
from cross_validation import *
from Queue import Queue
import os
import csv
import sys

#Class for a node in decision tree
class DecisionNode:
    def __init__(self, dataset, ancestors):
        self.dataset = dataset
        self.ancestors = ancestors
        self.children = dict()
        self.targetClass = None

#Finding the best feature from the dataset to split
def findBestFeatureToSplit(node):
    num_attrs = len(node.dataset[0])
    maxInfoGain = 0.0
    optimumFeature = -1
    
    for i in range(num_attrs - 1):
        if i in node.ancestors:
            continue
        else:
            if numberOfUniqueVals(node.dataset, i) > 20:
                continue
            infoGain = getInformationGain(node.dataset, i)
            if infoGain > maxInfoGain:
                maxInfoGain = infoGain
                optimumFeature = i
    return optimumFeature

    
#Creating the decision tree starting from the root node
def createDecisionTree(dataset):
    #Building the decision tree recursively from the root node
    root = DecisionNode(dataset, [])
    q = Queue()
    q.put(root)
    
    while q.qsize() > 0:
        
        #Removing the oldest node from the queue
        node = q.get()
        
        #If all the records in the dataset belongs to the same class, then just classify the node
        sameClass = checkForSameClass(node.dataset)
        if sameClass ==  1:
            node.feature = -1 #Leaf node
            node.targetClass = node.dataset[0][-1]
            continue

        #If the number of ancestors are more than 4, I am returing the most frequent label
        if len(node.ancestors) >= 4:
            node.feature = -1 #Leaf Node
            node.targetClass = getMostFrequentLabel(node.dataset)
            continue

        #Select the optimum feature to split
        optimumFeature = findBestFeatureToSplit(node)
        node.feature = optimumFeature
        
        #Creating the partitions using the optimum feature
        partitions = partitionDataSetUsingFeature(node.dataset, node.feature)

        #Creating the new ancestry list for child nodes
        ancestorForChild = []
        ancestorForChild.extend(node.ancestors)
        ancestorForChild.append(node.feature)

        for key in partitions:    
            childNode = DecisionNode(partitions[key], ancestorForChild)
            q.put(childNode)
            node.children[key] = childNode
        
    return root

#Main code starts
#Parsing Arguments
len_of_argv = len(sys.argv)
for a in range(len_of_argv):
    if sys.argv[a] == '-f':
        fileName = str(sys.argv[a + 1])
    else:
        continue


with open(os.path.join(fileName)) as file:
    reader = csv.reader(file)
    dataset = list(reader)
    num_rows = len(dataset) - 1
    num_cols = len(dataset[0])

#Labels for classification
labels = dataset[0]
del(dataset[0])
total_accuracy = 0.0
accuracy = 0.0
i = 1

#Removing useless features
dataset_array = numpy.array(dataset)
dataset_array = dataset_array[:,[2,6,7,8,9,14,21,23,24,30,41,47,-1]] 
dataset = dataset_array.tolist()

print("----------------Five Fold cross validation------------------");

for training, testing in getTrainingandTestingSplits(dataset):
    root = createDecisionTree(training)
    accuracy = classifyTestingSet(root, testing)
    print "Classification Accuracy ",i,": ",accuracy * 100,"%"
    total_accuracy += classifyTestingSet(root, testing)
    i = i + 1
    
print "Average Accuracy for Decision Tree: ",float(total_accuracy)/5 * 100, "%"   