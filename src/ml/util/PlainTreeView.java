/**
 * @(#)PlainTreeView.java   1.5.2 09/03/29
 */

package ml.util;

import ml.tree.TreeNode;
import ml.tree.Tree;
import ml.tree.LeafNode;

/**
 * The plain view of trees.
 *
 * @author Xiaohua Xu
 * @author Ping He
 */
 
public class PlainTreeView extends TreeViewAdapter {
    /**
     * The number of trees the current tree view contains
     */
    private static int count = 0;
    /**
     * The root of the tree to be viewed
     */
    private TreeNode root;
    
    /**
     * Initialize a plain view for the specified tree.
     */
    public PlainTreeView(Tree tree) {
        this(tree.getRoot());
    }
    
    /**
     * Initialize a plain view for the tree with the specified tree node as its root.
     */
    public PlainTreeView(TreeNode root) {
        this.root = root;
        initHead();
        initBody();
        initTail();
    }
    
    /**
     * Initialize the head of the tree view.
     */
    public void initHead() {
        StringBuilder buffer = new StringBuilder();
        setHead(buffer.toString());
    }
    
    /**
     * Initialize the body of the tree view.
     */
    public void initBody() {
        StringBuilder buffer = new StringBuilder();
        preorderToBuffer(root, buffer, TreeView.LEVEL_PREFIX, "");
        setBody(buffer.toString());
    }
    
    /**
     * Initialize the tail of the tree view.
     */
    public void initTail() {
        StringBuilder buffer = new StringBuilder();
        setTail(buffer.toString());   
    }

    /**
     * Construct the plain view of the tree started from the specified tree node preorderly.
     */
    private static void preorderToBuffer(TreeNode node, StringBuilder buffer, String prefix, String jointer) {
	    StringBuilder blank = new StringBuilder(80);
        buffer.append(prefix).append(jointer).append(node); 

        if(node instanceof LeafNode) {
        	buffer.append(CR);
        	return;
        }
       
        buffer.append("��").append(TreeView.CR);
        blank.setLength(node.toString().length());

        for(int i = 0; i < node.getChildrenCount(); i ++) {
            String childJointer = (i == node.getChildrenCount()-1 ? "��": "��");
            String extendJointer = jointer.equals("") ? jointer : jointer.equals("��") ? "  " : "��";
            preorderToBuffer(node.getChildAt(i), buffer, prefix + extendJointer + blank.toString(), childJointer);
        }
    }
    
}