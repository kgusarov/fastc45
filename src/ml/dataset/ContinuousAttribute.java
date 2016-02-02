/**
 * @(#)ContinuousAttribute.java        1.5.2 09/03/29
 */
package ml.dataset;

/**
 * A continuous attribute wrapping its continuous attribute values.
 *
 * @author 	    Ping He
 * @author 	    Xiaohua Xu
 */
public class ContinuousAttribute extends Attribute {
	/**
	 * Initialize a continuous attribute
	 * @param name the name of the attribute
	 * @param data the attribute values on the attribute
	 */
	public ContinuousAttribute(String name, String[] data) {
		super(name, data);
	}
	
	/**
	 * The String exhibition of the continuous attribute
	 * @return the String exhibition of the discrete attribute
 	 * @see ml.dataset.DiscreteAttribute#toString()
	 */
	public String toString() {
		return name + " : continuous";
	}

}