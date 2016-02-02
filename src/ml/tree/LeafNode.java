/**
 * @(#)LeafNode.java        1.5.2 09/03/29
 */
package ml.tree;

import ml.dataset.Attribute;
import ml.dataset.DiscreteAttribute;
import ml.dataset.ContinuousAttribute;

/**
 * A leaf tree node.
 * <p>
 * A leaf node is a subset of a tree node, because it does not have any child tree node.
 * </p>
 *
 * @author Ping He
 * @author Xiaohua Xu

 */
public class LeafNode extends TreeNode {
	/**
	 * Initialize a leaf node with the specified tree node content
	 */
	public LeafNode(TreeNodeContent content){
		super(content);
	}

	public float getTrainError(){
		return content.getErrorAsLeafNode();
	}

	public void setTrainError(float error){
		content.setErrorAsLeaf(error);
	}

	/**
	 * The String exhibition of the leaf node
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
			sb.append(" : ");
		}
		else sb.append(name);

		sb.append(content.getClassification());

		return sb.toString();
	}
}