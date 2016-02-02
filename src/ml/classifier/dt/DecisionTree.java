/**
 * @(#)DecisionTree.java        1.5.2 09/03/29
 */

package ml.classifier.dt;

import java.util.Arrays;
import ml.dataset.DataSet;
import ml.dataset.Attribute;
import ml.dataset.ContinuousAttribute;
import ml.dataset.DiscreteAttribute;
import ml.classifier.TreeClassifier;
import ml.tree.*;

/**
 * A decision tree built with C4.5 algorithm.
 *
 * @author Ping He
 * @author Xiaohua Xu
 */
public class DecisionTree implements TreeClassifier {
    // The root of the decision tree
	private TreeNode root;
	// The DataSet contains all the information in the input files
	private DataSet dataSet;
	// The delegate of attributes to assist tree building and tree pruning
	private AttributeDelegate[] attributeDelegates;

	/**
	 * Build a decision tree with the specified data set
	 */
	public DecisionTree(DataSet dataSet) {
		this.dataSet = dataSet;
		build();
		root.setName(dataSet.getName());
	}

	public int size() {
        return treeSize(root);
	}

	/**
	 * Compute the number of tree nodes in the subtree started from the specified tree node.
	 */
    private int treeSize(TreeNode root) {
		if(root instanceof LeafNode) return 1;

		int sum = 0;
		int childrenCount = root.getChildrenCount();
		for(int i = 0; i < childrenCount; i ++) {
			sum += treeSize(root.getChildAt(i));
		}
		return sum + 1;
	}

	public int getTrainError() {
		return getTestError(dataSet.getTrainData());
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public int getTestError(String[][] testData){
		String[] classificationResults = classify(testData);
		int testError = 0;
		int classAttributeIndex = dataSet.getClassAttributeIndex();
		for(int i = 0; i < classificationResults.length; i ++)
			if(!classificationResults[i].equals(testData[i][classAttributeIndex]))
				testError ++;
		return testError;
	}
	
	public double getTestErrorRatio(String[][] testData){
		return 1.0*getTestError(testData)/testData.length;
	}
	
	public String[] classify(String[][] testData) {
		// Ready to record the classification results
		String[] results = new String[testData.length];

		// Get the number of different class values
		int numberOfClasses = dataSet.getClassCount();
		String[] classValues = dataSet.getClassValues();

		// Initialize the test error
		float[] testClassDistribution = new float[numberOfClasses];
		TreeNode node = root;

		for(int testIndex = 0; testIndex < testData.length; testIndex ++) {
			// Classify the test data into a specific class
			// Initialize the probability of the test data belonging to each class value as 0
			Arrays.fill(testClassDistribution, 0.0f);

			// Classify a single test data from top to bottom
			classifyDownward(root, testData[testIndex], testClassDistribution, 1.0f);

			// Select the branch whose probability is the greatest as the classification of the test data
			float max = -1.0f;
		    int maxIndex = -1;
			for(int i = 0; i < testClassDistribution.length; i ++) {
				if(testClassDistribution[i] > max) {
					maxIndex = i;
					max = testClassDistribution[i];
				}
			}
			results[testIndex] = classValues[maxIndex];
		}

		return results;
	}

	/**
	 * Classify a test data from top to bottom from one tree node to its offspring (if there is any).
	 * @param node the current tree node classify the test data
	 * @param record the test data with its attribute values extracted
	 * @param testClassDistribution actually the output of this method, recording the weight
	 *               distribution of the test data in different class values.
	 * @param weight the weight of the test data on the current tree node
	 */
	private void classifyDownward(TreeNode node, String[] record, float[] testClassDistribution, float weight){
		TreeNodeContent content = node.getContent();
		if(node instanceof LeafNode) {
			// If there is no train data distributed on this tree node,
			// then add the weight of the test data to its corresponding class branch
			if(content.getTrainWeight() <= 0){
				// Get the branch index of the tree node's classification
				int classificationIndex = indexOf(content.getClassification(), dataSet.getClassValues());
				testClassDistribution[classificationIndex] += weight;
			}
			// Otherwise, distribute the weight of the test data with the coefficient
			// of trainClassDistri[classValueIndex]/trainWeight
			else {
				float[] trainClassDistribution = content.getTrainClassDistribution();
			    for(int i = 0; i < testClassDistribution.length; i ++){
			    	testClassDistribution[i] += weight * trainClassDistribution[i]/content.getTrainWeight();
			    }
			}
		}
		// If the current tree node is an InternalNode
		else {
			// if the test attribute value of the test data is not missing, then
			// pass it to its child tree node for classification.
			Attribute testAttribute = ((InternalNode)node).getTestAttribute();
		    int testAttributeIndex = indexOf(testAttribute.getName(), dataSet.getMetaData().getAttributeNames());
			if(!record[testAttributeIndex].equals("?")) {
			    int branchIndex = findChildBranch(record[testAttributeIndex], (InternalNode)node);
			    classifyDownward(node.getChildAt(branchIndex), record, testClassDistribution, weight);
			}
			/* If the test attribute value of the test data is missing or not exists in declaration,
			 * the test data is then passed to all the children tree nodes with the partitioned weight
			 * of (weight*children[childindex].getTrainWeight()/trainWeight)
			 */
			else {
				TreeNode[] children = node.getChildren();
				for(int i = 0; i < children.length; i ++) {
					TreeNodeContent childContent = children[i].getContent();
					float childWeight = (float)weight*childContent.getTrainWeight()/content.getTrainWeight();
					classifyDownward(children[i], record, testClassDistribution, childWeight);
				}
			}
		}
	}

	/**
	 * Find the branch index of the child tree node to which the parent tree node should
	 * classify the test data to.
	 * @param value the attribute value of the test data on the parent tree node's test attribute
	 * @param node the parent tree node which need to classify the test data to its offspring.
	 */
	private int findChildBranch(String value, InternalNode node) {
		Attribute testAttribute = node.getTestAttribute();
		// If the test attribute is continuous, find the branch of the test data
		// belong to by comparing its test attribute value and the cut value.
		if(testAttribute instanceof ContinuousAttribute) {
			float continValue = Float.parseFloat(value);
			return (continValue < (node.getCut() + Parameter.PRECISION)) ? 0 : 1;
		}
		else{
			// If the test attribute is discrete, find the branch whose value is
			// the same as the test attribute value of the test data
			String[] nominalValues = ((DiscreteAttribute)testAttribute).getNominalValues();
			for(int i = 0; i < nominalValues.length; i ++) {
				if(nominalValues[i].equals(value)) return i;
			}
			// Not Found the test attribute value
			return -1;
		}
	}

	/**
	 * Build a decision tree.
	 */
	public void build() {

		class TreeBuilder {
			// The sequence of the cases used for tree construction
			private int[] cases;
			// The weight of each case used for tree construction
			private float[] weight;
			// The number of the candidate test attributes
			private int candidateTestAttrCount;
			// Whether the attributes are candidate for test attribute selection
			private boolean[] isCandidateTestAttr;

			/**
			 * Initialize a tree builder which build a decision tree.
			 */
			TreeBuilder() {
				// Create Attribute Delegate objects
				attributeDelegates = new AttributeDelegate[dataSet.getAttributeCount()];
				int attributeIndex = 0;
				for(Attribute attribute : dataSet.getAttributes()){
					if(attribute instanceof ContinuousAttribute)
						attributeDelegates[attributeIndex] = new ContinuousAttributeDelegate((ContinuousAttribute)attribute);
				    else
				    	attributeDelegates[attributeIndex] = new DiscreteAttributeDelegate((DiscreteAttribute)attribute);
				    attributeIndex ++;
				}

		    	// Initialize the qualification of candidate test attributes
				candidateTestAttrCount = dataSet.getAttributeCount()-1;
				this.isCandidateTestAttr = new boolean[dataSet.getAttributeCount()];
				Arrays.fill(isCandidateTestAttr, true);
				isCandidateTestAttr[dataSet.getClassAttributeIndex()] = false;

				// Initialize the data sequence and their weight
				initializeCasesWeight();

				root = constructTreeNode(0, dataSet.getCaseCount());
			}

			/**
			 * Initialize the sequence of the train data from 1 to n, and initialize their
			 * weight with all 1.0.
			 */
			void initializeCasesWeight(){
				int caseCount = dataSet.getCaseCount();
				this.cases = new int[caseCount];
				for(int i = 0; i < cases.length; i ++) cases[i] = i;
				this.weight = new float[caseCount];
				Arrays.fill(weight, 1.0f);

				// All the attribute delegates share the same cases and weight array
				for(AttributeDelegate attributeDelegate : attributeDelegates){
					attributeDelegate.setCasesWeight(cases, weight);
				}
			}

			/**
			 * Construct tree node from top to bottom.
			 * @param first the start(inclusive) index of the train data used for tree node
			 *              construction.
			 * @param last the end(exclusive) index of the train data used for tree node
			 *             construction.
			 * @return the constructed tree node.
			 */
		    private TreeNode constructTreeNode (int first, int last) {
		    	// Construct an initial Leaf tree node
		    	TreeNodeContent content = createContent(first, last);
		    	float errorAsLeafNode = content.getErrorAsLeafNode();
				// If any of the leaf conditions is satisfied, return the Leaf tree node
		        if(content.satisfyLeafNode(Parameter.MINWEIGHT) || candidateTestAttrCount <= 0) {
		        	return new LeafNode(content);
		        }

				// Select a test attribute from all the candidate attributes
				int[] testAttributeInfo = new int[2];
				Attribute testAttribute = selectTestAttribute(first, last, testAttributeInfo);
				int testAttributeIndex, cutRank;
				float cut;
				// If no attribute is selected as the final test attribute, return Leaf
				if(testAttribute == null) {
					return new LeafNode(content);
				}
				else{
					testAttributeIndex = testAttributeInfo[0];
					cutRank = testAttributeInfo[1];
				}
				// Change the type the tree node to an InternalNode
				InternalNode node = new InternalNode(content, testAttribute);

				AttributeDelegate testAttributeDelegate = attributeDelegates[testAttributeIndex];
				// Record the class weight distribution of the selected test attribute
				float[] testBranchDistri;
				// If the test attribute is a discrete attribute
				if(!(testAttribute instanceof ContinuousAttribute)){
					// 0 is kept for missing data
					testBranchDistri = new float[((DiscreteAttribute)testAttribute).getNominalValuesCount()+1];
					// A discrete attribute can not be test attribute again in its offspring tree nodes
					isCandidateTestAttr[testAttributeIndex] = false;
					candidateTestAttrCount --;
				}
				else{
					// 0 is kept for missing data
					testBranchDistri = new float[2+1];
					cut = testAttributeDelegate.findCut(cutRank);
					node.setCut(cut);
					node.setCutRank(cutRank);
				}

				/* 'missingBegin' records the begin index of the missing data if there is any,
				 *                otherwise it coordinates with beginIndex;
				 * 'groupBegin' records the begin index to group the cases for one branch
				 * 'nextGroupBegin' records the begin index group the cases for next branch
				 */
				int missingBegin = first;
				int groupBegin = first;

				TreeNode aChild;
				// Group the missing data to the most front
				if(testAttributeDelegate.hasMissingData()) {
					groupBegin = testAttributeDelegate.groupForward(first, last, -1, testBranchDistri);
				}
				// Classify the [first last) cases to the branches of the test attribute
				// except for the last branch, to construct the children tree nodes
				for(int index = 0; index < testBranchDistri.length-1; index ++) {
					// For a continuous attribute, the group criterion is cutRank;
					// For a discrete attribute, the group criterion is the branch value(or index)
					int split = testAttribute instanceof ContinuousAttribute ? cutRank : index;

					// For the first several branches, we need to group the specified branch values forward
					// near "groupBegin" and compute its branch weight
					int nextGroupBegin;
					if(index < testBranchDistri.length-2){
						nextGroupBegin = testAttributeDelegate.groupForward(groupBegin, last, split, testBranchDistri);
					}
					// For the last branch, the "nextGroupBegin" must be last and its branch weight must be
					// the rest weight of the total weight.
					else{
						nextGroupBegin = last;
						float lastWeight = content.getTrainWeight();
						for(int j = 0; j < testBranchDistri.length-1; j ++) {
							lastWeight -= testBranchDistri[j];
						}
						testBranchDistri[testBranchDistri.length-1] = lastWeight;
					}

					// If there is no cases distributed in this branch, construct a Leaf
					if(groupBegin == nextGroupBegin){
						// Add a child with its parent's class
						aChild = new LeafNode(new TreeNodeContent(0, null, content.getClassification(), 0));
					}
					// If the test attribute contains missing data and at the same time
					// there are some cases distributed in this branch
			        else if(groupBegin > missingBegin){
			        	// Compute the weight ratio of this branch
			            float ratio = testBranchDistri[index+1]/(content.getTrainWeight() - testBranchDistri[0]);
						// Update the weight of the cases with unknown value on this test
						// attribute with the above ratio
						for(int i = missingBegin; i < groupBegin; i ++) weight[cases[i]] *= ratio;

						// Construct a child tree node for this branch recursively
						aChild = constructTreeNode(missingBegin, nextGroupBegin);

						// Restore the original sequence of the cases after the recursive construction
				        missingBegin = testAttributeDelegate.groupBackward(missingBegin, nextGroupBegin);
						// Restore the weight of the unknown cases for the next iteration
			            for(int i = missingBegin; i < nextGroupBegin; i ++) weight[cases[i]] /= ratio;
			        }
			        // If the test attribute contains no missing data and at the same time
					// some cases are distributed in this branch
			        else {
			        	aChild = constructTreeNode(groupBegin, nextGroupBegin);
			        	//When there is no missing data, missingBegin moves together with groupBegin
			        	missingBegin = nextGroupBegin;
			        }
					// For next branch, group from nextGroupBegin index
					groupBegin = nextGroupBegin;

					node.addChild(aChild);
					//System.out.println("node = null ? " + (node == null));
				}

				// After the recursion construction of its offspring tree nodes, the qualification
				// of this discrete attribute as a candidate test attribute should be restored
				if(!(testAttribute instanceof ContinuousAttribute)){
			   	    isCandidateTestAttr[testAttributeIndex] = true;
					candidateTestAttrCount ++;
				}
				// Choose to be a Leaf or InternalNode
		        if(node.getTrainError() - content.getErrorAsLeafNode() >= -Parameter.PRECISION) {
		        	return new LeafNode(content);
		        }

			    return node;
			}

			/**
			 * Create a tree node content with the specified data.
			 * @param first the start(inclusive) index of the train data used for creating
			 *              the tree node content.
			 * @param last the end(exclusive) index of the train data used for creating the
			 *             tree node content.
			 * @return the created tree node content.
			 */
			private TreeNodeContent createContent(int first, int last) {
				// Compute the total weight of the cases from first to last
		        float totalWeight = 0;
		        AttributeDelegate classAttributeDelegate = attributeDelegates[dataSet.getClassAttributeIndex()];
		        // Compute the weight distribution of the cases in different classes
				float[] totalClassDistri = new float[dataSet.getClassCount()];
		        Arrays.fill(totalClassDistri, 0);

		        for(int i = first ; i < last; i ++) {
		        	int classLabel = classAttributeDelegate.getClassBranch(cases[i]);
		        	totalClassDistri[classLabel] += weight[cases[i]];
		        }

				// Find the index of the class with maximal weight distribution
		        int maxClassIndex = 0;
		        for(int i = 0; i < totalClassDistri.length; i ++) {
		        	totalWeight += totalClassDistri[i];
		        	if(totalClassDistri[i] > totalClassDistri[maxClassIndex]) maxClassIndex = i;
		        }

		        // Get the different class values of the dataSet
				String[] classValues = dataSet.getClassValues();
				String classification = classValues[maxClassIndex];

				// Compute the errorAsLeaf and construct an initial Leaf tree node
		        float leafError = totalWeight - totalClassDistri[maxClassIndex];

		        return new TreeNodeContent(totalWeight, totalClassDistri, classification, leafError);
			}

			/**
			 * Select a test attribute from the candidate test attributes.
			 *
			 * @param first the start(inclusive) index of the train data used for the selection
			 *              of test attribute.
			 * @param last the end(exclusive) index of the train data used for the selection of
			 *              test attribute.
			 * @param testAttrInfo actually an output of this method, its
			 *              1<sup>st</sup> element recording the index of the test attribute, its
			 *              2<sup>nd</sup> element recording the rank of the cut value if the test
			 *              attribute is a continuous attribute. If there is no test attribute
			 *              selected, this array keeps empty.
			 * @return the selected test attribute.<br> If there is no test attribute selected,
			 *              null is returned.
			 */
			private Attribute selectTestAttribute(int first, int last, int[] testAttrInfo){
				// Ready to record the gain and splitInfo of each available attribute
		        float[] gain = new float[candidateTestAttrCount];
			    float[] splitInfo = new float[candidateTestAttrCount];
			    // For continuous attributes, ready to record the two neighboring ranks of the best split
				int[] splitRank = new int[candidateTestAttrCount];
			    int[] preSplitRank = new int[candidateTestAttrCount];

				float averageGain = 0;
			    // The number of the candidate test attributes with comparable Gain values
			    int feasibleTestAttr= 0;

				AttributeDelegate classAttributeDelegate = attributeDelegates[dataSet.getClassAttributeIndex()];
				int gainIndex = 0;
				int attrIndex = 0;
				// Evaluate Gain and SplitInfo for each attribute
			    for(AttributeDelegate attributeDelegate : attributeDelegates) {
			    	// Omit the unavailable attribute
			    	if(!isCandidateTestAttr[attrIndex]) {
			    		attrIndex ++;
			    		continue;
			    	}

			    	/* Evaluate Gain and SplitInfo for each attribute
			    	 * For discrete attributes, just evaluate its nominal values as the test branches
			    	 * For continuous attributes, select the split test with the maximal Gain value
			    	 */
			    	float[] evaluation = attributeDelegate.evaluate(first, last, classAttributeDelegate);

			    	// If the current attribute is valid as test attribute here
			    	if(evaluation != null) {
			    		gain[gainIndex] = evaluation[0];
		            	splitInfo[gainIndex] = evaluation[1];
		            	splitRank[gainIndex] = (int)evaluation[2];
		            	preSplitRank[gainIndex] = (int)evaluation[3];
			    	}

			    	// If the current attribute is feasible
		     		if(gain[gainIndex] > 0 && attributeDelegate.getBranchCount() < 0.3*(dataSet.getCaseCount() + 1)) {
				    	// Increase the number of feasible test attributes
				    	feasibleTestAttr ++;
				    	// Prepare to compute the average Gain
				    	averageGain += gain[gainIndex];
				    }

				    gainIndex ++;
				    attrIndex ++;
			    }

			    // Compute the average Gain value
				// If there is no feasible test attribute, than average Gain is set very big
				averageGain = ((feasibleTestAttr==0) ? 100000 : averageGain/feasibleTestAttr);

				// Ready to select the test attribute with the maximal GainRatio
				float bestValue = -Parameter.EPSILON;
				int bestAttrIndex = -1;
				// If the test attribute is continuous, we need to record the two ranks which produce the split value
				int winSplitIndex = -1, winPreSplitIndex = -1;

				/* Select the best test attribute with the maximal GainRatio value
				 * attrIndex records the index of the attributes
				 * gainIndex records the index of the filled gain array  //
				 */
				gainIndex = 0;
				attrIndex = 0;
				Attribute testAttribute = null;
				for(Attribute attribute : dataSet.getAttributes()) {
					// neglect the unavailable attributes
					if(!isCandidateTestAttr[attrIndex]) {
						attrIndex ++;
						continue;
					}
					// neglect the attributes with Gain less than 0
					if(gain[gainIndex] <= -Parameter.EPSILON) {
						gainIndex ++;
						attrIndex ++;
						continue;
					}
					// compute the GainRatio value for feasible candidate attributes
					float gainRatio = GainCalculator.computeGainRatio(gain[gainIndex], splitInfo[gainIndex], averageGain);

					// Update the best attribute
					if(gainRatio >= bestValue + Parameter.PRECISION) {
						// Record the best test attribute index and its GainRatio value
						bestAttrIndex = attrIndex;
						bestValue = gainRatio;
						testAttribute = attribute;

						// If the selected test attribute is continuous, record the split ranks as well
						if(testAttribute instanceof ContinuousAttribute) {
							winSplitIndex = splitRank[gainIndex];
							winPreSplitIndex = preSplitRank[gainIndex];
						}
					}
					gainIndex ++;
					attrIndex ++;
				}
				// If no test attribute is selected
				if(testAttribute != null) {
					testAttrInfo[0] = bestAttrIndex;
					// If the test attribute is continuous, record its cutRank
					if(testAttribute instanceof ContinuousAttribute){
						testAttrInfo[1] = attributeDelegates[bestAttrIndex].findCutRank(winSplitIndex, winPreSplitIndex);
					}
					// Return the test attribute object
				}
				return testAttribute;
			}
		}

		TreeBuilder builder = new TreeBuilder();
	}

	/**
	 * Prune the built decision tree.
	 */
	public void prune(){

		class TreePruner {
			// The sequence of the cases used for tree construction
			private int[] cases;
			// The weight of each case used for tree construction
			private float[] weight;

			/**
			 * Initialize a tree pruner which prunes the built decision tree
			 */
			TreePruner() {
				// ReInitialize the data sequence and their weight
				int caseCount = dataSet.getCaseCount();
				this.cases = new int[caseCount];
				for(int i = 0; i < cases.length; i ++) cases[i] = i;
				this.weight = new float[caseCount];
				Arrays.fill(weight, 1.0f);

				// Reset the cases and weight array of all attributes delegate objects
				for(AttributeDelegate attributeDelegate : attributeDelegates){
					attributeDelegate.setCasesWeight(cases, weight);
				}

				float errorAfterPrune = ebpPrune(root, 0, caseCount, true);
			}

			/**
			 * Prune the decision tree from top to bottom with EBP strategy.
			 * @param node the current tree node to be pruned
			 * @param first the start(inclusive) index of the train data used for pruning.
			 * @param last the end(exclusive) index of the train data used for pruning.
			 * @param update whether the current pruning is a trial to retrieve the error
			 *               after pruning (update = false) or an actual pruning (update = true).
			 * @return the estimated error after completing pruning the subtree started from
			 *         the current tree node.
			 */
			private float ebpPrune (TreeNode node, int first, int last, boolean update) {
				TreeNodeContent content = createContent(first, last, node);
				float estimatedLeafError = content.getErrorAsLeafNode();
				// If this is an actual pruning instead of an error-estimation, reset the tree node information
				if(update) node.setContent(content);
				InternalNode internalNode;
				// If the current tree node is a Leaf, its pruning is finished
				if(node instanceof LeafNode) {
					return estimatedLeafError;
				}
				else {
					internalNode = (InternalNode)node;
				}

				/* Begin to estimate the errors of each branch to get the errorAsInternalNode of the tree node */

				// The estimated test error of the tree node as an InternalNode
				float estimatedTreeError = 0;
				// The branch index with the maximal weight distribution
				int maxBranch = -1;
				// The maximal branch weight
				float maxBranchWeight = 0;

				// The index of the test attribute on the tree node
				int testAttributeIndex = indexOf(internalNode.getTestAttribute().getName(), dataSet.getMetaData().getAttributeNames());
				AttributeDelegate testAttributeDelegate = attributeDelegates[testAttributeIndex];
				int testBranchCount = testAttributeDelegate.getBranchCount();

				// Record the class weight distribution of the selected test attribute
				float[] branchDistri = new float[testBranchCount+1];
				/* 'missingBegin' records the begin index of the missing data if there is any,
				 *                otherwise it coordinates with beginIndex;
				 * 'groupBegin' records the begin index to group the cases for one branch
				 * 'nextGroupBegin' records the begin index group the cases for next branch
				 */
				int missingBegin = first;
				int groupBegin = first;

				// Group the missing data to the most front
				if(testAttributeDelegate.hasMissingData()) {
					groupBegin = testAttributeDelegate.groupForward(first, last, -1, branchDistri);
				}
				// Classify the [first last) cases to the branches of the test attribute
				// except for the last branch, to construct the children tree nodes
				for(int index = 0; index < testBranchCount; index ++) {
					// For a continuous attribute, the group criterion is cutRank;
					// For a discrete attribute, the group criterion is the branch value(or index)
					int split = testAttributeDelegate instanceof ContinuousAttributeDelegate ? internalNode.getCutRank() : index;

					// For the first several branches, we need to group the specified branch values forward
					// near "groupBegin" and compute its branch weight
					int nextGroupBegin;
					if(index < testBranchCount - 1){
						nextGroupBegin = testAttributeDelegate.groupForward(groupBegin, last, split, branchDistri);
					}
					// For the last branch, the "nextGroupBegin" must be last and its branch weight must be
					// the rest weight of the total weight.
					else{
						nextGroupBegin = last;
						float lastWeight = content.getTrainWeight();
						for(int j = 0; j < branchDistri.length-1; j ++) {
							lastWeight -= branchDistri[j];
						}
						branchDistri[branchDistri.length-1] = lastWeight;
					}

					// If there is no cases distributed in this branch, omit
					if(groupBegin == nextGroupBegin){
						continue;
					}
					// If there is missing data
					else if(groupBegin > missingBegin){
						// Compute the weight ratio of this branch
			            float ratio = branchDistri[index+1]/(content.getTrainWeight() - branchDistri[0]);
						// split the weight of the missing data with by multiplying the ratio
			            for(int i = missingBegin; i < groupBegin; i ++) weight[cases[i]] *= ratio;

			            // Accumulate the estimated errorAsInternalNode
			            estimatedTreeError += ebpPrune(internalNode.getChildAt(index), missingBegin, nextGroupBegin, update);

			            // Restore the original sequence of the cases after the recursive construction
				        missingBegin = testAttributeDelegate.groupBackward(missingBegin, nextGroupBegin);
						// Restore the weight of the missing data with by dividing the ratio
			            for(int i = missingBegin; i < nextGroupBegin; i ++) weight[cases[i]] /= ratio;
					}
					else{
			            estimatedTreeError += ebpPrune(internalNode.getChildAt(index), missingBegin, nextGroupBegin, update);
						//When there is no missing data, missingBegin moves together with groupBegin
			        	missingBegin = nextGroupBegin;
					}
					// For next branch, group from nextGroupBegin index
					groupBegin = nextGroupBegin;

					// Select the biggest branch with maximal weight for branchError estimation
					if(branchDistri[index+1] > maxBranchWeight) {
						maxBranchWeight = branchDistri[index+1];
						maxBranch = index;
					}
				}

				// If this sentence is not present, it will lead to significant pruning!
				// Do not evaluate doubled subtree raising (i.e. subtree-raising of subtree-raising)
				if(!update) return estimatedTreeError;

				// Estimate the subtree-raising error
				float estimatedBranchError = ebpPrune(internalNode.getChildAt(maxBranch), first, last, false);

				TreeNode parent = (InternalNode)internalNode.getParent();
				// Select a strategy with the minimal error
				if(estimatedLeafError <= estimatedBranchError + 0.1 && estimatedLeafError <= estimatedTreeError + 0.1) {
					LeafNode newNode = new LeafNode(content);
					if(parent != null) {
						int childIndex = parent.indexOfChild(internalNode);
						parent.setChildAt(childIndex, newNode);
					}
					else setRoot(newNode);

					node = newNode;
				}
				else if(estimatedBranchError <= estimatedTreeError + 0.1) {
					ebpPrune(internalNode.getChildAt(maxBranch), first, last, true);
					TreeNode newNode = node.getChildAt(maxBranch);
					if(parent != null) {
						int childIndex = parent.indexOfChild(internalNode);
						parent.setChildAt(childIndex, newNode);
					}
					else setRoot(newNode);

					node = newNode;
				}
				else {
					node.setTrainError(estimatedTreeError);
				}

				return node.getTrainError();
			}

			/**
			 * Recreate a tree node content with the specified data based on the tree node's
			 * existing content.
			 *
			 * @param first the start(inclusive) index of the train data used for creating
			 *              the tree node content.
			 * @param last the end(exclusive) index of the train data used for creating the
			 *             tree node content.
			 * @param node the tree node whose content need to be recreated.
			 * @return the recreated tree node content.
			 */
			private TreeNodeContent createContent(int first, int last, TreeNode node) {
				// Compute the total weight and its class distribution of [first last) prune cases
				float totalWeight = 0;
				AttributeDelegate classAttributeDelegate = attributeDelegates[dataSet.getClassAttributeIndex()];
				float[] totalClassDistri = new float[dataSet.getClassCount()];
		        Arrays.fill(totalClassDistri, 0);
		        for(int i = first ; i < last; i ++) {
		        	int classLabel = classAttributeDelegate.getClassBranch(cases[i]);
		        	totalClassDistri[classLabel] += weight[cases[i]];
		        }

				// Find the original classification of the tree node
				String nodeClassification = node.getContent().getClassification();
				String[] classValues = dataSet.getClassValues();
				int maxClassIndex = indexOf(nodeClassification, classValues);

				// Find the most probable classification of the prune data on the current tree node
				for(int i = 0; i < totalClassDistri.length; i ++) {
					totalWeight += totalClassDistri[i];
					if(totalClassDistri[i] > totalClassDistri[maxClassIndex])
					    maxClassIndex = i;
				}

				String classification = classValues[maxClassIndex];

				// Estimate the leafError of the tree node with the [first last) prune data
				float basicLeafError = totalWeight - totalClassDistri[maxClassIndex];
				float extraLeafError = Estimator.getExtraError(totalWeight, basicLeafError);
				float estimatedLeafError = basicLeafError + extraLeafError;

				return new TreeNodeContent(totalWeight, totalClassDistri, classification, estimatedLeafError);
			}
		}

		TreePruner pruner = new TreePruner();
	}

	/**
	 * Find the index of a String value in a String array.
	 */
	private int indexOf(String target, String[] from){
		for(int i = 0; i < from.length; i ++) {
			if(from[i].equals(target)) return i;
		}
		return -1;
	}

}
