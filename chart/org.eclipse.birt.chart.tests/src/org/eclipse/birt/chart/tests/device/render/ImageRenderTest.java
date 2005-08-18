/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.device.render;

import java.io.File;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The test suite for core
 */

public class ImageRenderTest
{
	protected static final String OUTDIR = "output";
	protected static final String INDIR = "input";
	protected static final String CONTROLDIR = "golden";
	protected static final String DRAWEXT = ".drw";
	protected String fixedDir;
	protected String workspaceDir;
	protected String type;
	protected TestSuite suite;
	
	public ImageRenderTest(TestSuite suite){
		this.suite = suite;
	}
	
	protected void processDir(File dir, String dirName) throws Exception{
		File[] files = dir.listFiles();
		
		for (int x = 0; x < files.length; x++){
			File file = files[x];
			if (file.isDirectory()){
				File outFileDir = new File(workspaceDir+File.separator+OUTDIR+dirName+File.separator+file.getName());
				outFileDir.mkdirs();
				processDir(file, dirName+File.separator+file.getName());
			}
			else if (file.getName().endsWith(DRAWEXT)){
				String fileName = file.getName();
				fileName = fileName.substring(0, fileName.length()-(DRAWEXT.length()));
				suite.addTest(new ImageOutputBaseTest(file, dirName, fileName, workspaceDir));
			}
		}		
	}
	
	public void process( ) throws Exception
	{
		Properties p = System.getProperties();
		//This is a standlone test
		System.setProperty("STANDALONE", "true");
		fixedDir = File.separator+"src"+File.separator+"org"
		+File.separator+"eclipse"+File.separator+"birt"+File.separator+"chart"
		+File.separator+"tests"+File.separator+"device";
		workspaceDir = (String)p.get("user.dir")+fixedDir;
		File inputDir = new File(workspaceDir+File.separator+INDIR);
		processDir(inputDir, "");
	}	

	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for render devices" );
		ImageRenderTest processSuite =  new ImageRenderTest(suite);
		try {
			processSuite.process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return suite;
	}
}