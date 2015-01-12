# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__="Vivekanandh"
__date__ ="$Dec 14, 2014 10:49:45 AM$"

import random
from utilityFunctions import *

#Code adapted from http://code.activestate.com/recipes/521906-k-fold-cross-validation-partition/ starts here
#Splitting the datasets randomly into training and testing sets
def getTrainingandTestingSplits(dataset):
    random.shuffle(dataset)

    slices = [dataset[i::5] for i in xrange(5)]

    for i in xrange(5):
        testing = slices[i]
        training = [item
                    for s in slices if s is not testing
                    for item in s]
        yield training, testing

#Code adapted from http://code.activestate.com/recipes/521906-k-fold-cross-validation-partition/ ends here


#Classifying the testing set using the DT formed
def classifyTestingSet(root, testing):
    num_rows = len(testing)
    num_matches = 0
    for i in range(num_rows):
        prediction = classifyInstanceUsingDT(root, testing[i])
        if prediction == testing[i][-1]:
            num_matches += 1
    accuracy = float(num_matches)/num_rows
    return accuracy
        
#Classifying an instance using a decision tree
def classifyInstanceUsingDT(root, test_instance):
    node = root
    while node.feature != -1:
        branch = test_instance[node.feature]
        if branch not in node.children:
            return getMostFrequentLabel(node.dataset)
        node = node.children[branch]
    return node.targetClass