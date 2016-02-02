/**
 * @(#)TreeView.java   1.5.2 09/03/29
 */
package ml.util;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import ml.classifier.dt.DecisionTree;
import ml.tree.TreeNode;
import ml.tree.Tree;

/**
 * An adapter of the different views of trees.
 *
 * @author Xiaohua Xu
 * @author Ping He
 */
public class TreeViewAdapter implements TreeView {
	/**
	 * The head of the tree view
	 */
    protected StringBuilder head;
    /**
	 * The body of the tree view
	 */
    protected StringBuilder body;
    /**
	 * The tail of the tree view
	 */ 
    protected StringBuilder tail;

	/**
	 * Initialize an empty tree view
	 */
    protected TreeViewAdapter() {
        head = new StringBuilder();
        body = new StringBuilder();
        tail = new StringBuilder();
    }

    public String getHead() {
        return head.toString();
    }
   
    public String getTail() {
        return tail.toString();
    }
  
    public String getBody() {
        return body.toString();
    }
    
    public void setHead(String newHead) {
        head.setLength(0);
        head.append(newHead);
    }
    
    public void setBody(String newBody) {
        body.setLength(0);
        body.append(newBody);
    }
    
    public void setTail(String newTail) {
        tail.setLength(0);
        tail.append(newTail);
    }

    public TreeView insert(String line) {
        body.insert(0, line+TreeView.CR);
        return this;            
    }
    
    public TreeView append(String line) {
        body.append(line+TreeView.CR);
        return this;
    }  
            
    public TreeView union(TreeView treeView) {
        if (this.getClass() != treeView.getClass()) {
            throw new UnsupportedOperationException();
        } 
            
        body.append(treeView.getBody());
        return this;
    }
        
    /**
     * The whole tree view.
     */
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(head);
        buffer.append(body);
        buffer.append(tail);    
        return buffer.toString();    
    }

}