# Fast C45

###### A fast implementation of c4.5 algorithm.
###### Imported from Google Code: [https://code.google.com/archive/p/fastc45/](https://code.google.com/archive/p/fastc45/)

C4.5 is a well-known machine learning algorithm used widely, but its runtime performance is sacrificed for the consideration of the limited main memory at that time. We present a fast implementation of C4.5 algorithm, named FC4.5(Fast C4.5). It organizes novel data structures, uses the indirect bucket-sort combined with the bit-parallel technique, and confines the binary-search of the cutoff within the narrowest range. The combination of these techniques enables FC4.5 greatly accelerate the tree construction process of C4.5 algorithm. Experiments show that FC4.5 can build the same decision tree as C4.5(Release 8) system with a runtime performance gain up to 5.8 times. Besides, FC4.5 also achieves a good scalability on different kinds of datasets.