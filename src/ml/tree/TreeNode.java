/**
 * @(#)TreeNode.java        1.5.3 09/04/22
 */
package ml.tree;

import java.util.Arrays;

/**
 * A tree node of a tree
 *
 * @author Ping He
 * @author Xiaohua Xu
 */
public abstract class TreeNode {
	/**
	 * The content of the tree node
	 */
	protected TreeNodeContent content;
	/**
	 * The name of the tree node. 
	 * <p>Only root has a specified name of the tree.</p>
	 */
	protected String name;
	/**
	 * The parent tree node
	 */
	protected TreeNode parent;
	/**
	 * The children tree nodes
	 */
	protected TreeNode[] children;
	/**
	 * The number of the registered children tree nodes
	 */
	protected int childCount;

	/**
	 * Initialize a tree node with the specified tree node content
	 */
	protected TreeNode(TreeNodeContent content){
		this.content = content;
		this.childCount = 0;
	}

	/**
	 * Get the content of the tree node
	 */
	public TreeNodeContent getContent() {
		return this.content;
	}

	/**
	 * Set the content of the tree node
	 */
	public void setContent(TreeNodeContent content) {
		this.content = content;
	}

	/**
	 * Get the name of the tree node
	 */
	public String getName() {
		if(name != null) return this.name;
		else return this.toString();
	}

	/**
	 * Set the name of the tree node
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the parent tree node
	 */
	public TreeNode getParent(){
		return this.parent;
	}

	/**
	 * Set the parent tree node
	 */
    public void setParent(TreeNode parent){
    	this.parent = parent;
    }

    /**
     * Add a child tree node
	 */
	public void addChild(TreeNode aChild) {
		children[childCount ++] = aChild;
        aChild.setParent(this);
        setTrainError(getTrainError() + aChild.getTrainError());
	}

    /**
     * Get all the children tree nodes
     */
    public TreeNode[] getChildren(){
    	return this.children;
    }

	/**
	 * Set all the children tree nodes
	 */
    public void setChildren(TreeNode[] children){
    	this.children = children;
    }

	/**
	 * Get the index<sup>th</sup> child tree node
	 */
	public TreeNode getChildAt(int index){
		return children[index];
	}

	/**
	 * Set the index<sup>th</sup> child tree node
	 */
    public void setChildAt(int index, TreeNode child){
		children[index] = child;
		child.setParent(this);
	}

	/**
	 * Get the index of the specified tree node in the children tree nodes array
	 * @param child the specified tree node whose child index is to be retrieved
	 */
	public int indexOfChild(TreeNode child){
		for(int i = 0; i < children.length; i ++) {
			if(children[i] == child) return i;
		}
		return -1;
	}

	/**
	 * Get the number of the children tree nodes
	 */
	public int getChildrenCount() {
		return children.length;
	}

    /**
     * Query whether the tree node is root node
     */
    public boolean isRoot(){
		return parent == null;
	}
	
    /**
     * Query whether the tree node is leaf node
     */
    public boolean isLeaf(){
		return (children == null || children.length == 0);
	}

	/**
	 * Get the train error of the tree node
	 * @return If there is no missing data distributed on the tree node, the train error
	 *         should be an integer; <br>
	 *         Otherwise, the train error should be an estimated <i>float</i> value computed by
	 *         some statistical method.
	 *         
	 * @see ml.tree.LeafNode#getTrainError()
	 * @see ml.tree.InternalNode#getTrainError()
	 */
	public abstract float getTrainError();

	/**
	 * Set the train error of the tree node
	 *
	 * @see ml.tree.LeafNode#setTrainError(float error)
	 * @see ml.tree.InternalNode#setTrainError(float error)
	 */
	public abstract void setTrainError(float error);
}
