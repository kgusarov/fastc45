/**
 * @(#)HtmlTreeView.java   1.5.2 09/03/29
 */

package ml.util;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import ml.tree.TreeNode;
import ml.tree.Tree;
import ml.tree.LeafNode;

/**
 * The html view of trees.
 *
 * @author Xiaohua Xu
 * @author Ping He

 */
public class HtmlTreeView extends TreeViewAdapter {
    /**
     * The number of trees the current tree view contains
     */
    private static int count = 0;
    /**
     * The root of the tree to be viewed
     */
    private TreeNode root;
    
    /**
     * Initialize a html view for the specified tree.
     */
    public HtmlTreeView(Tree tree) {
        this(tree.getRoot());
    }
    
    /**
     * Initialize a html view for the tree with the specified tree node as its root.
     */
    public HtmlTreeView(TreeNode root) {
        this.root = root;
        initHead();
        initBody();
        initTail();
    }
    
    /**
     * Initialize the head of the tree view.
     */
    public void initHead() {
        String title = root.getName();
        StringBuilder buffer = new StringBuilder();
        
        buffer.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" ")
              .append("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">").append(TreeView.CR).append(TreeView.CR);
        buffer.append("<html>").append(TreeView.CR).append(TreeView.CR);
        buffer.append("<head>").append(TreeView.CR);
        buffer.append(TreeView.LEVEL_GAP).append("<title>").append(title).append("</title>").append(TreeView.CR);
        buffer.append(TreeView.LEVEL_GAP).append("<link rel=\"StyleSheet\" href=\"dTree/dtree.css\" type=\"text/css\" />").append(TreeView.CR);
        buffer.append(TreeView.LEVEL_GAP).append("<script type=\"text/javascript\" src=\"dTree/dtree.js\"></script>").append(TreeView.CR);
        buffer.append("</head>").append(TreeView.CR).append(TreeView.CR);
        buffer.append("<body>").append(TreeView.CR).append(TreeView.CR);
        setHead(buffer.toString());
    }
    
    /**
     * Initialize the body of the tree view.
     */
    public void initBody() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<div class=\"dtree\">").append(TreeView.CR);
        buffer.append(TreeView.LEVEL_GAP).append("<p><a href=\"javascript: dt"+count+".openAll();\">open all</a> ")
            .append("| <a href=\"javascript: dt"+count+".closeAll();\">close all</a></p>").append(TreeView.CR);
        buffer.append(TreeView.LEVEL_GAP).append("<script type=\"text/javascript\">").append(TreeView.CR);
        buffer.append(TreeView.LEVEL_GAP).append(TreeView.LEVEL_GAP).append("dt" + count + " = new dTree('dt" + count + "');").append(TreeView.CR);
        
        Map<TreeNode, Integer> map = new HashMap<TreeNode, Integer>();
        preorderToMap(root, map);
        
        Set<TreeNode> keySet = map.keySet();
        for (TreeNode node : keySet) {
            buffer.append(TreeView.LEVEL_GAP).append(TreeView.LEVEL_GAP).append("dt" + count + ".add(").append(map.get(node)).append(", ");
            if (node.isRoot()) {
                buffer.append("-1").append(", '")
                    .append(correct(node.toString())).append("');").append(TreeView.CR);
            } 
            else {
                buffer.append(correct(map.get(node.getParent()).toString())).append(", '")
                    .append(correct(node.toString())).append("');").append(TreeView.CR);
            }
            
        }
        
        buffer.append(TreeView.LEVEL_GAP).append(TreeView.LEVEL_GAP).append("document.write(dt"+count+")").append(TreeView.CR);
        buffer.append(TreeView.LEVEL_GAP).append(TreeView.LEVEL_GAP).append("dt"+count+".openAll()").append(TreeView.CR);
        buffer.append(TreeView.LEVEL_GAP).append("</script>").append(TreeView.CR);
        buffer.append("</div>").append(TreeView.CR).append(TreeView.CR);
        setBody(buffer.toString());
        
        count ++;
    }
    
    /**
     * Initialize the tail of the tree view.
     */
    public void initTail() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("</body>").append(TreeView.CR).append(TreeView.CR);
        buffer.append("</html>").append(TreeView.CR); 
        setTail(buffer.toString());   
    }
   
    /**
     * Correct the expression of the symbol "'" in the html output.
     */
    private String correct(String value) {
        return value.replace("'", "\\'");
    }

    /**
     * Map the tree nodes in the tree started from the specified tree node to a hash map preorderly.
     */
    private void preorderToMap(TreeNode from, Map<TreeNode, Integer> map) {
        //if (from == null) return;
        Integer id = map.size();
        map.put(from, id);
 
 		if (from instanceof LeafNode) return;
 		
        for (TreeNode child : from.getChildren()) {
            preorderToMap(child, map);
        }
    }     
}