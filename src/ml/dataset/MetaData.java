/**
 * @(#)MetaData.java        1.5.2 09/03/29
 */
package ml.dataset;

/**
 * The meta data of a data set
 *

 * @author 	    Ping He
 * @author 	    Xiaohua Xu
 * @see         ml.dataset.DataSet
 * @see         ml.dataset.UciDataSet
 */
public class MetaData {
	// All the attribute names
	private String[] attributeNames;
	// Whether the attributes are continuous attribute or discrete attribute
	private boolean[] isAttributesContinuous;
	/* The nominal attribute values. If the attribute is discrete, the corresponding
	 * item records its nominal values; otherwise, the corresponding item is null.
	 */
	private String[][] attributeNominalValues;

	/**
	 * Initialize a MetaData.
	 * @param names the names of the attributes
	 * @param isContinuous whether the attributes are continuous attribute or not
	 * @param nominalValues the nominal values of the attributes. For continuous attributes,
	 *        their nominal values are set <i>null</i>.
	 */
	public MetaData(String[] names, boolean[] isContinuous, String[][] nominalValues){
		this.attributeNames = names;
		this.isAttributesContinuous = isContinuous;
		this.attributeNominalValues = nominalValues;
	}

	/**
	 * Get the names of the attributes
	 */
	public String[] getAttributeNames(){
		return this.attributeNames;
	}

	/**
	 * Get the name of the index<sup>th</sup> attribute
	 */
	public String getAttributeNameAt(int index){
		return this.attributeNames[index];
	}

	/**
	 * Set the names of the attributes
	 */
	public void setAttributeNames(String[] names) {
		this.attributeNames = names;
	}

	/**
	 * Set the name of the index<sup>th</sup> attribute
	 */
	public void setAttributeNameAt(int index, String name) {
		this.attributeNames[index] = name;
	}

	/**
	 * Query whether the attributes are continuous attribute or not
	 */
	public boolean[] isAttributesContinuous() {
		return this.isAttributesContinuous;
	}

	/**
	 * Query whether the index<sup>th</sup> attribute is continuous attribute or or
	 */
	public boolean isAttributeContinuousAt(int index) {
		return this.isAttributesContinuous[index];
	}

	/**
	 * Set whether the attributes are continuous attribute or not
	 */
	public void setAttributesContinuous(boolean[] isContinuous) {
		this.isAttributesContinuous = isContinuous;
	}

	/**
	 * Set whether the index<sup>th</sup> attribute is continuous attribute or not
	 */
	public void setAttributeContinuousAt(int index, boolean isContinuous) {
		this.isAttributesContinuous[index] = isContinuous;
	}

	/**
	 * Get the nominal values of the attributes
	 */
	public String[][] getAttributeNominalValues(){
		return this.attributeNominalValues;
	}

	/**
	 * Get the nominal values of the index<sup>th</sup> attribute
	 */
	public String[] getAttributeNominalValuesAt(int index){
		return this.attributeNominalValues[index];
	}

	/**
	 * Set the nominal values of the attributes
	 */
	public void setAttributeNominalValues(String[][] nominalValues){
		this.attributeNominalValues = nominalValues;
	}

	/**
	 * Set the nominal values of the index<sup>th</sup> attribute
	 */
	public void setAttributeNominalValuesAt(int index, String[] nominalValues){
		this.attributeNominalValues[index] = nominalValues;
	}

	/**
	 * Get the number of attributes in the meta data
	 */
	public int getAttributeCount(){
		return this.attributeNames.length;
	}

	/**
	 * The String exhibition of the meta data
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < attributeNames.length; i ++) {
			sb.append(attributeNames[i]).append(" : ");
			if(isAttributesContinuous[i]){
				sb.append("Continuous");
			}
			else{
				sb.append(java.util.Arrays.toString(attributeNominalValues[i]));
			}
		}
		return sb.toString();
	}
}