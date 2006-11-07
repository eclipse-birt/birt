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

package org.eclipse.birt.report.model.api;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test <code>DefaultSearchFileAlgorithm</code>
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>testFindFile</td>
 * <td>Get a <code>ReportDesign</code> instance, then find another file which
 * locates in the 'base' folder of the design.</td>
 * <td>If the file exists in the 'base' folder, returns the absolute path of
 * this file. If not, returns null.</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class DefaultSearchFileAlgorithmTest extends BaseTestCase
{

	private final String fileName = "SimpleMasterPageHandleTest.xml"; //$NON-NLS-1$
	private DefaultResourceLocator rl;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		ULocale locale = new ULocale( "en_US" );//$NON-NLS-1$
		openDesign( fileName, locale );
		rl = new DefaultResourceLocator( );
	}

	/**
	 * Tests the 'findFile' method of DefaultSearchFileAlgorithm.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */

	public void testFindFile( ) throws Exception
	{
		URL url = rl.findResource( designHandle,
				"1.xml", IResourceLocator.IMAGE ); //$NON-NLS-1$
		assertNull( url );

		url = rl.findResource( designHandle,
				"MasterPageHandleTest.xml", IResourceLocator.IMAGE ); //$NON-NLS-1$
		assertNotNull( url );

		designHandle.setStringProperty( ReportDesign.BASE_PROP,
				getClassFolder( ) + GOLDEN_FOLDER );
		url = rl.findResource( designHandle, "1.xml", IResourceLocator.IMAGE ); //$NON-NLS-1$
		assertNull( url );

		designHandle.setFileName( getClassFolder( ) + "/golden/" ); //$NON-NLS-1$
		url = rl.findResource( designHandle,
				"CustomColorHandleTest_golden.xml", IResourceLocator.IMAGE ); //$NON-NLS-1$
		assertNull( url );
		designHandle.setFileName( getClassFolder( ) + "/golden/filename" ); //$NON-NLS-1$
		url = rl.findResource( designHandle,
				"CustomColorHandleTest_golden.xml", IResourceLocator.IMAGE ); //$NON-NLS-1$
		assertNotNull( url );
		url = rl.findResource( designHandle, url.toString( ),
				IResourceLocator.IMAGE );
		assertNotNull( url );
	}

	/**
	 * Finds the message file from default resource locator.
	 * 
	 * @throws Exception
	 */

	public void testFindMessageFiles( ) throws Exception
	{
		String testFile = "ResourceLocator"; //$NON-NLS-1$

		URL resource = designHandle.findResource( testFile,
				IResourceLocator.MESSAGE_FILE );
		String strResource = resource.toString( );
		assertTrue( strResource.indexOf( "en_US" ) != -1 ); //$NON-NLS-1$
	}

	/**
	 * Tests the 'findFile' method of DefaultSearchFileAlgorithm.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */

	public void testFindResourceInJar( ) throws Exception
	{
		// TODO;
		
//		String resource = "testRead.jar!/test/testRead.rptdesign"; //$NON-NLS-1$
//		URL url = rl.findResource( designHandle, resource,
//				IResourceLocator.LIBRARY );
//		assertNotNull( url );
//
//		URLConnection jarConnection = url.openConnection( );
//		jarConnection.connect( );
//
//		InputStream inputStream = jarConnection.getInputStream( );
//		assertNotNull( inputStream );
	}

	// /**
	// * Tests search resources under fragments.
	// *
	// * <ul>
	// * <li>
	// * <li>case 1:
	// * <li>open a report which used resource in fragments.
	// * </ul>
	// *
	// * <ul>
	// * <li>case 2:
	// * <li>open a library with url in the form of "bundleresource://".
	// * </ul>
	// *
	// * @throws Exception
	// */
	//
	// public void testFindResourceInFragments( ) throws Exception
	// {
	// openDesign( "SearchFragmentsTest.xml" ); //$NON-NLS-1$
	//
	// assertNotNull( designHandle );
	// LabelHandle labelFromLib = (LabelHandle) designHandle
	// .findElement( "labelFromLib" ); //$NON-NLS-1$
	// assertNotNull( labelFromLib );
	// assertEquals( "library text", labelFromLib.getDisplayText( ) );
	// //$NON-NLS-1$
	//
	// LabelHandle externalizedLabel = (LabelHandle) designHandle
	// .findElement( "externalizedLabel" ); //$NON-NLS-1$
	// assertNotNull( externalizedLabel );
	// assertEquals( "label_localized", externalizedLabel.getDisplayText( ) );
	// //$NON-NLS-1$
	//
	// ImageHandle image = (ImageHandle) designHandle.findElement( "image" );
	// //$NON-NLS-1$
	//
	// URL url = rl.findResource( designHandle, image.getURI( ),
	// IResourceLocator.IMAGE );
	// assertEquals( "bundleresource", url.getProtocol( ) ); //$NON-NLS-1$
	// assertEquals( "/images/20063201445066811.gif", url.getPath( ) );
	// //$NON-NLS-1$
	//
	// url = rl.findResource( designHandle, "libs/lib.rptlibrary", //$NON-NLS-1$
	// IResourceLocator.LIBRARY );
	// assertNotNull( url );
	// assertNotNull( sessionHandle );
	// libraryHandle = sessionHandle.openLibrary( url.toString( ) );
	// assertNotNull( libraryHandle );
	// }

	/**
	 * 
	 * @throws Exception
	 */

	public void testResourceFolder( ) throws Exception
	{
		String testFile = "CustomColorHandleTest_golden.xml"; //$NON-NLS-1$
		URL resource = null;

		// set resource folder only in module

		designHandle
				.setResourceFolder( getResource( INPUT_FOLDER ).toString( ) );
		resource = rl.findResource( designHandle, testFile, 1 );
		assertNull( resource );

		// TODO:
//		// set in the session
//
//		sessionHandle.setResourceFolder( getResource( GOLDEN_FOLDER )
//				.toString( ) );
//		resource = rl.findResource( designHandle, testFile, 1 );
//		assertNotNull( resource );
//		assertTrue( resource.toString( ).endsWith( testFile ) );

	}

	/**
	 * Finds the jar file from default resource locator.
	 * 
	 * @throws Exception
	 */

	public void testFindJarFiles( ) throws Exception
	{
		sessionHandle
				.setResourceFolder( getResource( INPUT_FOLDER ).toString( ) );

		String testFile = "Resourcelocator_test.jar"; //$NON-NLS-1$
		URL resource = rl.findResource( designHandle, testFile,
				IResourceLocator.JAR_FILE );
		assertNotNull( resource );

	}
}