/**
 * @(#)UciDataSet.java        1.5.2 09/03/29
 */
package ml.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

/**
 * A data set loaded from UCI-format files
 *
 * @author 	    Xiaohua Xu
 * @author 	    Ping He
 * @see         ml.dataset.UciDataSet
 */
public class UciDataSet extends DataSet{

	/**
	 * Initialize a UCI data set.
	 * @param baseName the base name of the input files (.names and .data).
	 */
	public UciDataSet(String baseName){
		super(baseName);
		addColumnSetView();
	}

	public void load(String baseName){
		setMetaData(loadMetaData(baseName + ".names"));
		setTrainData(loadData(baseName + ".data"));
    }

	/**
	 * Load meta data from the specified file.
	 * @param filename the name of the input file
	 * @return The loaded meta data
     */
	public MetaData loadMetaData(String filename) {
		/* The 'tempAttributeNames' ArrayList dynamically reads the attribute names in;
		 * The 'tempIsContinuous' ArrayList dynamically reads their continuous properties in;
		 * The 'tempNominalValues' ArrayList dynamically reads the nominal values of the discrete attributes in;
		 */
     	ArrayList<String> tempAttributeNames = new ArrayList<String>();
     	ArrayList<Boolean> tempIsContinuous = new ArrayList<Boolean>();
     	ArrayList<String[]> tempNominalValues = new ArrayList<String[]>();

 		try {
 			/* The default class attribute name
 			 * If in the .names file, there is no "the target attribute : ****" sentences
 			 * The default class attribute name "class" is used
 			 */
        	String decisionAttribute = "class";
			//This variable records the index of attributes
        	int attributeIndex = 0;

        	BufferedReader bf = new BufferedReader(new FileReader(filename));
        	String reader;
 		    while((reader = bf.readLine()) != null) {
 		    	if(reader.trim().length() == 0) continue;

 	    		StringTokenizer token = new StringTokenizer(reader,":\t\n,.");
    		    String word = token.nextToken().trim();
	        	// Read in the target attribute name
		        if(word.equals("the target attribute")){
				    decisionAttribute = token.nextToken().trim();
		    	}
	        	else {
	        		// Record the index of the class attribute
	        		if(word.equals(decisionAttribute))  {
	        			setClassAttributeIndex(attributeIndex);
	        		}

	        		// Add attribute name
	        		tempAttributeNames.add(word);

	        		word = token.nextToken().trim();
	        		// Construct and add a ContinuousAttribute object
	        		if(word.equals("continuous")) {
						tempIsContinuous.add(true);
						tempNominalValues.add(null);
	        		}
	        		// Construct and add a DiscreteAttribute object
	        		else{
	        			tempIsContinuous.add(false);
						// Get the nominal values of discrete attributes
	        			ArrayList<String> values = new ArrayList<String>();
	        			values.add(word);
	        			while(token.hasMoreTokens()) {
	        				values.add(token.nextToken().trim());
	        			}
	        			tempNominalValues.add((String[])values.toArray(new String[0]));
	        		}

	        		attributeIndex ++;
	        	}
 		    }
 	    	bf.close();
 		}
 		catch(IOException e) {
 			System.err.println(e);
 		}

		// Transform the ArrayList to arrays
		String[] attributeNames = (String[])tempAttributeNames.toArray(new String[0]);
		String[][] nominalValues = (String[][])tempNominalValues.toArray(new String[0][]);
		boolean[] isContinuous = new boolean[tempIsContinuous.size()];
		for(int i = 0; i < isContinuous.length; i ++) {
			isContinuous[i] = tempIsContinuous.get(i);
		}

        return new MetaData(attributeNames, isContinuous, nominalValues);
	}

	/**
	 * Load data from the specified file.
	 * @param filename the name of the input file
	 * @return The loaded data
     */
    public static String[][] loadData(String filename){
    	// Read in the test data in its original arrangement and extract its data fields
        ArrayList<String[]> testList = new ArrayList<String[]>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filename));
            String reader;
            for(; (reader = bf.readLine()) != null;) {
            	if(!reader.trim().equals("")) {
	                String[] line = extract(reader,",");
	                testList.add(line);
            	}
            }
            bf.close();
        }
        catch(IOException e){
            System.err.println(e);
        }

        // Transform the dynamic ArrayList to static Array
        return (String[][])testList.toArray(new String[0][]);
    }

	/**
	 * Extract the attribute values out from a single String separated with specified delimiter
	 * @param source the String to be extracted
	 * @param delimiterString the delimiter with which the attribute values are separated
	 * @return the extracted attribute values
	 */
	private static String[] extract(String source, String delimiterString) {
        String[] data = new String[source.length()];
        int count = 0;

        int splitPoint = 0;
        int j = 0;
        for(int length = source.length(); j < length; j ++){
        	// Compare char by char
            if (delimiterString.indexOf(source.charAt(j)) >= 0) {
                if (splitPoint != j) {
                    data[count ++] = source.substring(splitPoint, j).trim();
                    splitPoint = j;
                }
                splitPoint ++;
            }
        }
        if (splitPoint != j) {
            data[count ++] = source.substring(splitPoint, j).trim();
        }
		// Only number of "count" Strings are filled in data
        String[] result = new String[count];
        System.arraycopy(data,0,result,0,count);
        return result;
    }

}