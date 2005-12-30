/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ResourceBundle;
import java.util.Vector;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.api.*;
import org.eclipse.core.runtime.Platform;	//keep it wei 2005-12-29

//import org.eclipse.core.runtime.Platform;


public abstract class EngineCase extends TestCase {

	private String caseName;
	
	protected static final String BUNDLE_NAME = "org.eclipse.birt.report.tests.engine.messages";//$NON-NLS-1$

	protected static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle( BUNDLE_NAME );
    
	protected static final String PLUGIN_NAME = "org.eclipse.birt.report.tests.engine";
	/*
	 *  The plugin location   
	 */
//	protected static final String PLUGINLOC = Platform.getBundle(PLUGIN_NAME).getLocation();
	protected static final String PLUGINLOC = "/org.eclipse.birt.report.tests.engine/";	
	
//	protected static final String PLUGIN_PATH = System.getProperty("user.dir")+ "/plugins/" 
//                                                    +PLUGINLOC.substring(
// 		                                                   PLUGINLOC.indexOf("/")+1);
	protected static final String PLUGIN_PATH = System.getProperty("user.dir")+"/bin/";

	
//	protected static final String PLUGIN_PATH = "D:/BirtAPI/org.eclipse.birt.report.tests.engine/bin/";
	
	protected static final String OUTPUT_FOLDER = "output";
	protected static final String INPUT_FOLDER = "input";
	protected static final String GOLDEN_FOLDER = "golden";
	
	
	/* 
	protected static final String PLUGIN_PATH =PLUGINLOC.substring(PLUGINLOC.indexOf("/")+1);
	*/
	public static void main(String[] args) {
		junit.awtui.TestRunner.run(EngineCase.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Constructor for DemoCase.
	 * @param name
	 */
	public EngineCase(String name) {
		super(name);
		System.out.println("name is " + name);
	}
	
	protected void setCase(String caseName){
		//set the case and emitter manager accroding to caseName.		
		this.caseName = caseName;
	}
	protected void runCase(String args[]){
		Vector runArgs = new Vector();
		//invoke the report runner.
		String input = PLUGIN_PATH+System.getProperty("file.separator")+RESOURCE_BUNDLE.getString("CASE_INPUT");
		input += System.getProperty("file.separator") + caseName + ".rptdesign";
		System.out.println("input is : " + input);
		
		//run report runner.
		
		if(args != null){
			for(int i = 0;i < args.length;i++){
				runArgs.add(args[i]);
			}
		}
		runArgs.add("-f");
		runArgs.add("test");
		runArgs.add(input);
		
		args = (String[])runArgs.toArray(new String[runArgs.size()]);
		ReportRunner.main(args);
	}

	/*
	 * Add below three methods to test RunTask
	 */
	public void copyStream( String src, String tgt )
	{
		InputStream in = getClass( ).getClassLoader( )
				.getResourceAsStream( src );
		assertTrue( in != null );
		try
		{
			int size = in.available( );
			byte[] buffer = new byte[size];
			in.read( buffer );
			OutputStream out = new FileOutputStream( tgt );
			out.write( buffer );
			out.close( );
			in.close( );
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			fail( );
		}
	}

	public void removeFile( File file )
	{
		if ( file.isDirectory( ) )
		{
			File[] children = file.listFiles( );
			for ( int i = 0; i < children.length; i++ )
			{
				removeFile( children[i] );
			}
		}
		if ( file.exists( ) )
		{
			if (!file.delete( ) )
			{
				System.out.println(file.toString()  + " can't be removed");
			}
		}
	}

	public void removeFile( String file )
	{
		removeFile( new File( file ) );
	}
	
	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path where the test java source file locates.
	 */
	protected String getBaseFolder( )
	{
		
		String className = getClass( ).getName( );
		int lastDotIndex = className.lastIndexOf( "." );
		className = className.substring( 0, lastDotIndex );
		String path=PLUGIN_PATH+className.replace( '.', '/' );
		//File folder = new File(className.replace( '.', '/' ), INPUT_FOLDER );
		return path;
	}
	
	
}
