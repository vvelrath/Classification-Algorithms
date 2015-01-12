# To change this license header, choose License Headers in Project Properties.
# To change this template file, choose Tools | Templates
# and open the template in the editor.

__author__="Vivekanandh"
__date__ ="$Dec 14, 2014 2:41:53 AM$"

if __name__ == "__main__":
    print "Hello World"
    
import numpy
    
#Calculate entropy
def calculateEntropy(dataset):
    dataset = numpy.array(dataset)
    classes = set(dataset[:,-1])
    label_counts = dict()
    num_rows = len(dataset)
    num_attrs = len(dataset[0])
    entropy = 0
    
    for t_class in classes:
        label_counts[t_class] = 0
    
    for i in range(num_rows):
        label_counts[dataset[i][num_attrs - 1]] += 1
        
    for t_class in classes:
        p_class_j = float(label_counts[t_class])/num_rows
        entropy = entropy - (p_class_j * numpy.log2(p_class_j))
    return entropy
    
#Calculate information gain
def getInformationGain(dataset, feature):
    myEntropy = calculateEntropy(dataset)
    partitions = dict()
    dataset_array = numpy.array(dataset)
    uniqueSplit = set(dataset_array[:,feature])
    num_rows = len(dataset)
    
    for split in uniqueSplit:
        partitions[split] = []
            
    for i in range(num_rows):
        partitions[dataset[i][feature]].append(dataset[i])
    
    entropyForChildren = 0
    missing_values = 0
    
    for split in uniqueSplit:
        if split == '?':
            missing_values = 1
            continue
        size_of_split = len(partitions[split])
        childEntropy = calculateEntropy(partitions[split])
        entropyForChildren += float(size_of_split)/num_rows * (childEntropy)
        
    infoGain = myEntropy - entropyForChildren
    
    if missing_values == 1:
        no_of_non_missing_rows = num_rows - len(partitions['?'])
        return float(no_of_non_missing_rows)/num_rows * infoGain
    else:
        return infoGain

