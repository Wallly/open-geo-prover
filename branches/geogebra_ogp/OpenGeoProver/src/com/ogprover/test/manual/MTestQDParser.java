/* 
 * DISCLAIMER PLACEHOLDER 
 */

package com.ogprover.test.manual;

import java.io.IOException;
import java.io.StringReader;

import com.ogprover.main.OGPConfigurationSettings;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.prover_protocol.cp.OGPCP;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.utilities.io.CustomFileReader;
import com.ogprover.utilities.io.DocHandler;
import com.ogprover.utilities.io.FileLogger;
import com.ogprover.utilities.io.OGPDocHandler;
import com.ogprover.utilities.io.QDParser;


/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for manual testing of XML parsing with QDParser</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class MTestQDParser {
	public static void main (String[] args) {
		/*
		 * First of all adjust working directory.
		 */
		//System.out.println(System.getProperty("user.dir")); // this is path to current project
		System.setProperty("user.dir", System.getProperty("user.dir") + "/OpenGeoProver");	// adjust working directory (enter directory where input sub-directory resides);
																							// note: this is not a physical change of directory, only change of property that holds 
																							// information about current directory.
		//System.out.println(System.getProperty("user.dir")); // this is adjusted path
		
		OpenGeoProver.settings = new OGPConfigurationSettings();
		FileLogger logger = OpenGeoProver.settings.getLogger();
		OGPCP consProtocol = new OGPCP();
		
		// Read the name of XML file from command line (extension is optional); file is in "input" directory
		if (args.length != 1) {
			logger.error("Incorrect number of command line arguments");
			return;
		}
		
		// Extract only file name without extension
		String xmlFileNameWithExtension = args[0];
		int pointIndex = xmlFileNameWithExtension.indexOf('.');
		String xmlFileName;
		
		if (pointIndex == -1) { // no extension
			xmlFileName = xmlFileNameWithExtension;
		}
		else {
			xmlFileName = xmlFileNameWithExtension.substring(0, pointIndex);
		}
		
		// Create custom file reader for the file with name passed as argument; read its contents and copy to string
		String xmlString;
		try {
			CustomFileReader fileReader = new CustomFileReader(xmlFileName, "xml");
			
			// Read one by one line and append to string buffer
			StringBuffer sb = new StringBuffer();
			String line = null;
			
			while ((line = fileReader.readLine()) != null) {
				sb.append(line);
			}
			
			// Close file reader
			fileReader.close();
			
			xmlString = sb.toString();
		} catch (IOException e) {
			logger.error("I/O Exception caught: " + e.toString());
			e.printStackTrace();
			return;
		}
		
		// Use populated string as input stream for parser
		StringReader sr = new StringReader(xmlString);
		
		// Create document handler and call the parser
		DocHandler dh = new OGPDocHandler(consProtocol);
		QDParser qdParser = new QDParser();
		
		try {
			qdParser.parse(dh, sr);
		} catch (Exception e) {
			logger.error("Parser exception caught: " + e.toString());
			e.printStackTrace();
			return;
		}
		
		// Check results of parsing
		if (((OGPDocHandler)dh).isSuccess()) { // note: this is safe cast
			System.out.println();
			if (consProtocol.getTheoremName() != null) {
				System.out.println("Constructions for theorem \"" + consProtocol.getTheoremName() + "\"");
				System.out.println();
			}
			
			for (int ii = 0, jj = consProtocol.getConstructionSteps().size(); ii < jj; ii++) {
				GeoConstruction gc = consProtocol.getConstructionSteps().get(ii);
				System.out.println(gc.getConstructionDesc());
			}
			System.out.println();
		}
		else {
			String message = "Failed to parse xml input with geometry constructions.";
			logger.error(message);
			System.out.println(message);
		}
		
		OpenGeoProver.settings.getTimer().cancel(); // cancel default timer task
	}
}
