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

package org.eclipse.birt.report.model.elements;

import java.net.URLDecoder;
import java.util.List;

import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.BaseTestCase;


/**
 * Unit test for class ReportDesign.
 * 
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: * collapse" bordercolor="#111111" width="100%" id="AutoNumber3" height="50">
 * <tr>
 * <td width="33%" height="16"><b>Method </b></td>
 * <td width="33%" height="16"><b>Test Case </b></td>
 * <td width="34%" height="16"><b>Expected Result </b></td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testMakeUniqueName()}</td>
 * <td>name is required and set name is null</td>
 * <td>get displayLabel value,value is "New SQL Query Data Set"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>name is required and set name isn't null ,but namespace contains this
 * name</td>
 * <td>value format is baseName + " " + ++index ,value is "firstDataSet 1"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>name is required and set name isn't null ,but namespace doesn't contain
 * this name</td>
 * <td>original value "firstDataSet10"</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class ReportDesignTest extends BaseTestCase
{

	private String fileName = "ReportDesignTest.xml"; //$NON-NLS-1$

	/**
	 * test makeUniqueName().
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>name is required but set name is null</li>
	 * <li>name is required and set name isn't null ,but namespace contains this
	 * name</li>
	 * <li>name is required and set name isn't null ,but namespace doesn't
	 * contain this name</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get displayLabel value,value is "New SQL Query Data Set"</li>
	 * <li>value format is baseName + " " + ++index ,value is "firstDataSet 1"</li>
	 * <li>original value "firstDataSet10"</li>
	 * </ul>
	 * 
	 * @throws DesignFileException
	 *             if there are errors in openDesign method
	 */
	public void testMakeUniqueName( ) throws DesignFileException
	{
		// get design and instance of ElementFactory class

		openDesign( fileName, ULocale.ENGLISH );
		ElementFactory elementFactory = new ElementFactory( design );

		// name is required but set name is null

		DataSetHandle dataSetHandle = elementFactory.newOdaDataSet( null );
		assertEquals( "NewOdaDataSet", dataSetHandle.getElement( ).getName( ) ); //$NON-NLS-1$

		// name is required and set name isn't null ,but namespace contain this
		// name

		dataSetHandle = elementFactory.newOdaDataSet( "firstDataSet" ); //$NON-NLS-1$
		assertEquals( "firstDataSet1", dataSetHandle.getElement( ).getName( ) ); //$NON-NLS-1$

		// name is required and set name isn't null ,but namespace doesn't
		// contain this name

		dataSetHandle = elementFactory.newOdaDataSet( "firstDataSet10" ); //$NON-NLS-1$
		assertEquals( "firstDataSet10", dataSetHandle.getElement( ).getName( ) ); //$NON-NLS-1$

	}

	/**
	 * Test the method 'isFileExist()'.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testIsFileExist( ) throws Exception
	{
		openDesign( fileName );
		String base = designHandle.getBase( );
		assertNull( base );

		assertTrue( design.isFileExist(
				"ReportDesignTest.xml", IResourceLocator.IMAGE ) ); //$NON-NLS-1$

		base = getResource( "input/ReportDesignTest.xml" ).getFile( ); //$NON-NLS-1$
		base = URLDecoder.decode( base, "UTF-8" ); //$NON-NLS-1$
		design.setFileName( base );
		assertTrue( design.isFileExist(
				"./ReportDesignTest.xml", IResourceLocator.IMAGE ) ); //$NON-NLS-1$
		assertFalse( design.isFileExist( "1.jpg", IResourceLocator.IMAGE ) ); //$NON-NLS-1$

	}

	/**
	 * Test method collectPropValues()
	 * 
	 * @throws Exception
	 */
	public void testCollectProperties( ) throws Exception
	{
		openDesign( fileName );
		List<Object> list = design.collectPropValues(
				IReportDesignModel.BODY_SLOT, IReportItemModel.BOOKMARK_PROP );
		assertEquals( 2, list.size( ) );
		assertTrue( list.get( 0 ) instanceof Expression );
		assertTrue( list.get( 1 ) instanceof Expression );
		assertEquals( "TableBookmark", ( (Expression) list.get( 0 ) ) //$NON-NLS-1$
				.getStringExpression( ) );
		assertEquals( "DataBookmark", ( (Expression) list.get( 1 ) ) //$NON-NLS-1$
				.getStringExpression( ) );

		list = design.collectPropValues( IReportDesignModel.BODY_SLOT,
				IReportItemModel.TOC_PROP );
		assertEquals( 2, list.size( ) );
		assertTrue( list.get( 0 ) instanceof TOC );
		assertTrue( list.get( 1 ) instanceof TOC );
		assertEquals( "TableToc", ( (TOC) list.get( 0 ) ).getExpression( ) ); //$NON-NLS-1$
		assertEquals( "DataToc", ( (TOC) list.get( 1 ) ).getExpression( ) ); //$NON-NLS-1$
	}
}