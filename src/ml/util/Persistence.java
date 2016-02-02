/**
 * @(#)Persistence.java        1.5.3 09/04/22
 */

package ml.util;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Persistence of Object.
 *
 * @author Ping He
 * @author Xiaohua Xu
 */
public class Persistence {

	//to xml
	public static String toXml(Object obj) {
		XStream xstream = new XStream(new DomDriver());
		return xstream.toXML(obj);
	}

	//from xml
	public static Object fromXml(String str) {
		XStream xstream = new XStream(new DomDriver());

		String filename = "";

		// str as xml file name
		if (str.endsWith(".xml") || str.endsWith(".XML")) {
			filename = str;
		}
		else if (str.indexOf('<') < 0) {
			filename = str + ".xml";
		}

		if (!filename.equals("")) {
			InputStream inputStream = null;
		    try {
        	    inputStream = new FileInputStream(new File(filename));
            }
            catch (Exception e) {
        	    e.printStackTrace();
            }
            return xstream.fromXML(inputStream);
		}

        // str as xml string
		return xstream.fromXML(str);
	}

}