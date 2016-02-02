/**
 * @(#)C45Spring.java        1.5.3 09/04/23
 */

package ml.demo;

import ml.dataset.DataSet;
import ml.dataset.UciDataSet;
import ml.classifier.dt.DecisionTree;
import ml.util.Persistence;
import ml.util.HtmlTreeView;

/**
 * Xml to Fast C4.5 .
 *
 * @author Ping He
 * @author Xiaohua Xu
 */
public class C45Spring {
	public static void main(String[] args) {
		// Illegal input
		if (args.length == 0) {
		    usage();
			return;
		}
		// Ask for help
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("-help")){
				usage();
				return;
			}
		}

        DecisionTree tree = (DecisionTree)Persistence.fromXml(args[0]);
		System.out.println(new HtmlTreeView(tree));
	}

	private static void usage(String... messages) {
 	    System.out.println("Usage: java " + C45Spring.class + " xmlFilename");
 	    for (String line : messages) {
 	    	System.out.println(line);
 	    }
	}
}