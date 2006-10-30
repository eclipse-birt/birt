/*******************************************************************************
 * All rights reserved. This program and the accompanying materials
 * Copyright (c) 2004 Actuate Corporation.
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.util.BaseTestCase;
import org.eclipse.birt.report.model.util.XMLParserException;

import com.ibm.icu.util.ULocale;

public class ModuleUtilTest extends BaseTestCase
{

	/**
	 * Test deserilaze an stream-represented data into an action structure.
	 * 
	 * @throws XMLParserException
	 * @throws IOException
	 * @throws DesignFileException
	 */

	public void testDeserialize( ) throws XMLParserException, IOException,
			DesignFileException
	{
		InputStream is = ModuleUtilTest.class
				.getResourceAsStream( "input/ActionDeserializeTest.xml" ); //$NON-NLS-1$
		ActionHandle action = ModuleUtil.deserializeAction( is );
		assertEquals( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH,
				action.getLinkType( ) );
		assertEquals( "Window3", action.getTargetWindow( ) ); //$NON-NLS-1$

		MemberHandle paramBindings = action.getParamBindings( );
		assertEquals( 2, paramBindings.getListValue( ).size( ) );
		ParamBindingHandle paramBinding1 = (ParamBindingHandle) paramBindings
				.getAt( 0 );
		assertEquals( "param1", paramBinding1.getParamName( ) ); //$NON-NLS-1$
		assertEquals( "1+1=3", paramBinding1.getExpression( ) ); //$NON-NLS-1$

		MemberHandle searchKeys = action.getSearch( );
		assertEquals( 2, searchKeys.getListValue( ).size( ) );
		SearchKeyHandle key1 = (SearchKeyHandle) searchKeys.getAt( 0 );
		assertEquals(
				"\"E001\".equals(row[\"studentId\"])", key1.getExpression( ) ); //$NON-NLS-1$

		// with chinese character inside.

		is = ModuleUtilTest.class
				.getResourceAsStream( "input/ActionDeserializeTest_1.xml" ); //$NON-NLS-1$

		action = ModuleUtil.deserializeAction( is );
		assertNotNull( action );
		assertEquals( "/BIRT/\u4e2d\u6587.html", action.getURI( ) ); //$NON-NLS-1$

		ActionHandle actionHandle = ModuleUtil
				.deserializeAction( (String) null );
		assertNotNull( actionHandle );
		assertEquals( "hyperlink", actionHandle.getLinkType( ) ); //$NON-NLS-1$

	}

	/**
	 * Test serialize an action instance.
	 * 
	 * @throws Exception
	 */

	public void testSerialize( ) throws Exception
	{
		openDesign( "ActionSerializeTest.xml" ); //$NON-NLS-1$
		ImageHandle image1 = (ImageHandle) designHandle.findElement( "image1" ); //$NON-NLS-1$,
		ActionHandle action1 = image1.getActionHandle( );

		ImageHandle image2 = (ImageHandle) designHandle.findElement( "image2" ); //$NON-NLS-1$
		ActionHandle action2 = image2.getActionHandle( );

		String outputPath = getClassFolder( ) + OUTPUT_FOLDER;

		String str = ModuleUtil.serializeAction( action1 );

		FileWriter writer1 = new FileWriter( new File( outputPath,
				"ActionSerializeTest1_out.xml" ) ); //$NON-NLS-1$
		writer1.write( str );
		writer1.close( );

		String str2 = ModuleUtil.serializeAction( action2 );
		FileWriter writer2 = new FileWriter( new File( outputPath,
				"ActionSerializeTest2_out.xml" ) ); //$NON-NLS-1$
		writer2.write( str2 );
		writer2.close( );

		compareTextFile(
				"ActionSerializeTest1_golden.xml", "ActionSerializeTest1_out.xml" ); //$NON-NLS-1$//$NON-NLS-2$
		compareTextFile(
				"ActionSerializeTest2_golden.xml", "ActionSerializeTest2_out.xml" ); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Test CheckModule.
	 * 
	 * Cases:
	 * <ul>
	 * <li>valid report design file
	 * <li>valid library file
	 * <li>invalid report design file
	 * <li>invalid library file
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testCheckModule( ) throws Exception
	{
		sessionHandle = new DesignEngine( null )
				.newSessionHandle( ULocale.ENGLISH );
		assertNotNull( sessionHandle );

		String fileName = getClassFolder( ) + INPUT_FOLDER
				+ "CellHandleTest.xml"; //$NON-NLS-1$
		InputStream inputStream = new BufferedInputStream( new FileInputStream(
				fileName ) );
		int rtnType = ModuleUtil.checkModule( sessionHandle, fileName,
				inputStream );
		assertEquals( ModuleUtil.REPORT_DESIGN, rtnType );

		fileName = getClassFolder( ) + INPUT_FOLDER + "Library_1.xml"; //$NON-NLS-1$
		inputStream = new BufferedInputStream( new FileInputStream( fileName ) );
		rtnType = ModuleUtil.checkModule( sessionHandle, fileName, inputStream );
		assertEquals( ModuleUtil.LIBRARY, rtnType );

		fileName = getClassFolder( ) + INPUT_FOLDER + "InValidDesign.xml"; //$NON-NLS-1$
		inputStream = new BufferedInputStream( new FileInputStream( fileName ) );
		rtnType = ModuleUtil.checkModule( sessionHandle, fileName, inputStream );
		assertEquals( ModuleUtil.INVALID_MODULE, rtnType );

		fileName = getClassFolder( ) + INPUT_FOLDER + "InValidLibrary.xml"; //$NON-NLS-1$
		inputStream = new BufferedInputStream( new FileInputStream( fileName ) );
		rtnType = ModuleUtil.checkModule( sessionHandle, fileName, inputStream );
		assertEquals( ModuleUtil.INVALID_MODULE, rtnType );
	}

	/**
	 * Test CheckVersion.
	 * 
	 * Cases:
	 * <ul>
	 * <li>test design file with version value "1".
	 * <li>test design file with same version value of currrent version.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testCheckVersion( ) throws Exception
	{
		List infos = ModuleUtil.checkVersion( getClassFolder( ) + INPUT_FOLDER
				+ "DesignWithoutLibrary.xml" );//$NON-NLS-1$
		assertEquals( 1, infos.size( ) );

		IVersionInfo versionInfo = (IVersionInfo) infos.get( 0 );
		assertEquals( "1", versionInfo.getDesignFileVersion( ) ); //$NON-NLS-1$
		assertEquals(
				"The design file was created by an earlier version of BIRT. Click OK to convert it to a format supported by the current version of the product.", versionInfo.getLocalizedMessage( ) ); //$NON-NLS-1$

		infos = ModuleUtil.checkVersion( getClassFolder( ) + INPUT_FOLDER
				+ "ScalarParameterHandleTest.xml" );
		assertEquals( 0, infos.size( ) );
	}
}
