/**
 * @(#)InternalNode.java        1.5.2 09/03/29
 */

package ml.tree;

import ml.dataset.Attribute;
import ml.dataset.ContinuousAttribute;
import ml.dataset.DiscreteAttribute;

/**
 * An internal tree node.
 * <p>
 * An internal node is a super set of a tree node. <br>
 * It not only contains a basic tree node content, it also contains some information about 
 * its test attribute.
 * </p>
 *
 * @author Ping He
 * @author Xiaohua Xu

 */
public class InternalNode extends TreeNode {
	// The train error of the tree node as an internal tree node
	private float errorAsInternalNode;
	// The test attribute of the internal tree node
	private Attribute testAttribute;
	// The cut value of the test on the internal tree node if its test attribute is continuous
	private float cut;
	/**
	 * The rank of the cut value among all the attribute values of the train data on the
	 * test attribute.
	 * This is used for the speedup of C45 algorithm.
	 */
	private int cutRank;

	/**
	 * Initialize an internal tree node.
	 * @param content the content of the tree node
	 * @param testAttribute the test attribute of the internal node
	 */
	public InternalNode(TreeNodeContent content, Attribute testAttribute){
		super(content);

		errorAsInternalNode = 0;
		this.testAttribute = testAttribute;
		if(testAttribute instanceof ContinuousAttribute)
			this.children = new TreeNode[2];
		else
			this.children = new TreeNode[((DiscreteAttribute)testAttribute).getNominalValuesCount()];
		this.errorAsInternalNode = 0;
	}

	public float getTrainError() {
		return errorAsInternalNode;
	}

	public void setTrainError(float errorAsInternalNode) {
		this.errorAsInternalNode = errorAsInternalNode;
	}

	/**
	 * Get the cut value of the internal node, if its test attribute is continuous
	 */
	public float getCut(){
		return this.cut;
	}

	/**
	 * Set the cut value of the internal node, if its test attribute is continuous
	 */
	public void setCut(float cut){
		this.cut = cut;
	}

	/**
	 * Get the rank of the cut value of the internal node, if its test attribute is continuous
	 */
	public int getCutRank() {
		return this.cutRank;
	}

	/**
	 * Set the rank of the cut value of the internal node, if its test attribute is continuous
	 */
	public void setCutRank(int cutRank){
		this.cutRank = cutRank;
	}

	/**
	 * Get the test attribute of the internal node
	 */
	public Attribute getTestAttribute(){
		return this.testAttribute;
	}

	/**
	 * Set the test attribute of the internal node
	 */
	public void setTestAttribute(Attribute attribute) {
		this.testAttribute = attribute;
	}

	/**
	 * The String exhibition of the internal node
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();

		if(!isRoot()){
			Attribute parentTestAttribute = ((InternalNode)parent).getTestAttribute();
			int childIndex = parent.indexOfChild(this);

			sb.append(parentTestAttribute.getName());
			if(parentTestAttribute instanceof ContinuousAttribute) {
				sb.append(childIndex == 0 ? " <= " : " > ")
				  .append(((InternalNode)parent).getCut());
			}
			else{
				sb.append(" = ")
				  .append(((DiscreteAttribute)parentTestAttribute).getNominalValues()[childIndex]);
			}
		}
		else sb.append(name);

		return sb.toString();
	}
}