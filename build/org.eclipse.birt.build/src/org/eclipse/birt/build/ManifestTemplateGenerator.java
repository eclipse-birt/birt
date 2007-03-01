/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;


import org.apache.tools.ant.BuildException;


/**
 * 
 * @author Farrah
 * 
 * Generate error/failure summary according into unitTestReport.properties
 * Properties format  pluginId.failure/error=<count>
 * eg:
 * org.eclipse.birt.tests.data.engine.AllTests.failure=1
 * org.eclipse.birt.tests.data.engine.AllTests.error=0
 */
public class ManifestTemplateGenerator{
	

	public ManifestTemplateGenerator(){
		
	}


	public static void main (String[] args){
				
		File manifestHead = new File( args[0]);
		File manifestBody = new File( args[1]);
		File mainfestRslt = new File( args[2]);
		
		/* Read MANIFEST template header*/
		try{
			FileInputStream f = new FileInputStream(manifestHead);
			int len = f.available();
			byte [] b = new byte[len+1];
			int j = f.read(b, 0, len);
			f.close();
			
			String ff = new String(b,0,len);
			/*System.out.println(ff);*/
			
			StringBuffer strb = new StringBuffer(ff);
			System.out.println(strb.toString());
			/* Read MANIFEST body and create new manifest.mf */
			FileReader freader=new FileReader(manifestBody);
			BufferedReader breader=new BufferedReader(freader);
			
			String myString;
			while((myString=breader.readLine())!=null)
			{
				if(myString.startsWith("#")) continue;
				if(myString.startsWith(" .")) continue;
				
			    strb.append(myString);
			    strb.append("\n"); 
			} 
			int lastDelimer = strb.lastIndexOf(";");
			String result = strb.substring(0, lastDelimer);
			result.concat("\n");
			
			FileOutputStream Result = new FileOutputStream (mainfestRslt);
			byte [] writeByte = new byte[strb.length()+1];
			writeByte = strb.toString().getBytes();
			Result.write(writeByte);
			
		}catch(Exception e){
			throw new BuildException("ManifestHead not found!");
		}
	

	}


}
