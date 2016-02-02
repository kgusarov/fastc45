/**
 * @(#)C45Hibernation.java        1.5.3 09/04/23
 */

package ml.demo;

import ml.dataset.DataSet;
import ml.dataset.UciDataSet;
import ml.classifier.dt.DecisionTree;
import ml.util.Persistence;

/**
 * Fast C4.5 to xml.
 *
 * @author Ping He
 * @author Xiaohua Xu
 */
public class C45Hibernation {
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

	    String dataSetName = args[0];
	    DataSet dataSet = new UciDataSet(dataSetName);

	    //to xml string
		String xml = Persistence.toXml(new DecisionTree(dataSet));
		System.out.println(xml);
	}

	private static void usage(String... messages) {
 	    System.out.println("Usage: java " + C45Hibernation.class + " dataSetName ");
 	    for (String line : messages) {
 	    	System.out.println(line);
 	    }
	}
}