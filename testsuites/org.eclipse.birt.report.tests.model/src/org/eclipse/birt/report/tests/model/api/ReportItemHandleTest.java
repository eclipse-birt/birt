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

package org.eclipse.birt.report.tests.model.api;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Test ReportItemHandle.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testDataSet()}</td>
 * <td>check free-form element which contains attribute data-set</td>
 * <td>dataset name is myDataSet</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>check list element which doesn't contain attribute data-set</td>
 * <td>null</td>
 * </tr>
 * 
 * <tr>
 * <td>testReadVisibilityRules()</td>
 * <td>Gets visibility rules in elements and tests whether values match with
 * those defined the design file.</td>
 * <td>Returned values match with the design file. If "format" values are not
 * defined, the default value "all" is used.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The number of visibility rules in elements.</td>
 * <td>The number is 2.</td>
 * </tr>
 * 
 * <tr>
 * <td>testWriteVisibilityRules</td>
 * <td>The default format value in the visibility rule.</td>
 * <td>The default value can be written out to the design file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets "format" and "valueExpr" properties of a visibility rule.</td>
 * <td>"format" and "valueExpr" can be written out and the output file matches
 * with the golden file.</td>
 * </tr>
 * 
 */

public class ReportItemHandleTest extends BaseTestCase
{

	/**
	 * @param name
	 */
	public ReportItemHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	DesignElement element;
	InnerReportItemHandle innerHandle;

	
	public static Test suite(){
		return new TestSuite(ReportItemHandleTest.class);	
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		copyResource_INPUT( "ReportItemHandleTest.xml" , "ReportItemHandleTest.xml" );
		openDesign( "ReportItemHandleTest.xml" ); //$NON-NLS-1$ 

	}

	public void tearDown( )
	{
		removeResource( );
	}
	
	/**
	 * 
	 */

	class InnerReportItemHandle extends ReportItemHandle
	{

		InnerReportItemHandle( ReportDesign design, DesignElement element )
		{
			super( design, element );
		}
	}

	/**
	 * test getDataSet().
	 * <p>
	 * Test Cases:
	 * 
	 * <ul>
	 * <li>check free-form element which contains attribute data-set</li>
	 * <li>check list element which doesn't contain attribute data-set</li>
	 * </ul>
	 * 
	 * Excepted:
	 * <ul>
	 * <li>dataset name is myDataSet</li>
	 * <li>null</li>
	 * </ul>
	 * 
	 * @throws SemanticException
	 */

	public void testDataSet( ) throws SemanticException
	{
		// the data set is not null referenced by name
		element = design.findElement( "free form" ); //$NON-NLS-1$
		assertNotNull( element );
		innerHandle = new InnerReportItemHandle( design, element );
		DesignElementHandle itemHandle = innerHandle.getDataSet( );
		assertNotNull( itemHandle );
		String name = innerHandle.getDataSet( ).getElement( ).getName( );
		assertEquals( "myDataSet", name ); //$NON-NLS-1$

		// the data set is null referenced by name
		element = design.findElement( "my list" ); //$NON-NLS-1$
		assertNotNull( element );
		innerHandle = new InnerReportItemHandle( design, element );
		itemHandle = innerHandle.getDataSet( );
		assertNotNull( itemHandle );

		DataSetHandle handle = designHandle.findDataSet( "myDataSet" ); //$NON-NLS-1$
		ListHandle list = (ListHandle) element.getHandle( design );
		list.setDataSet( handle );
		assertEquals( handle, list.getDataSet( ) );

		list.setDataSet( (DataSetHandle) null );
		assertNull( list.getDataSet( ) );
	}

	/**
	 * Test to read hide rules.
	 * 
	 * @throws Exception
	 *             if open the design file with errors.
	 */

	public void testReadVisibilityRules( ) throws Exception
	{
		openDesign( "ReportItemHandleTest.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "bodyLabel" ); //$NON-NLS-1$		
		Iterator rules = labelHandle.visibilityRulesIterator( );

		// checks with the first visibility rule.

		StructureHandle structHandle = (StructureHandle) rules.next( );
		assertNotNull( structHandle );

		MemberHandle memberHandle = structHandle
				.getMember( HideRule.FORMAT_MEMBER );
		assertEquals( DesignChoiceConstants.FORMAT_TYPE_PDF, memberHandle
				.getStringValue( ) );
		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		assertEquals( "pdf, 10 people", memberHandle.getStringValue( ) ); //$NON-NLS-1$

		// the second visibility rule

		structHandle = (StructureHandle) rules.next( );
		assertNotNull( structHandle );

		memberHandle = structHandle.getMember( HideRule.FORMAT_MEMBER );
		assertEquals( DesignChoiceConstants.FORMAT_TYPE_ALL, memberHandle
				.getStringValue( ) );
		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		assertEquals( "excel, 10 people", memberHandle.getStringValue( ) ); //$NON-NLS-1$

		// no third, must be null.

		structHandle = (StructureHandle) rules.next( );
		assertNull( structHandle );

		// tests visibility on the data item.

		DataItemHandle dataHandle = (DataItemHandle) designHandle
				.findElement( "bodyData" ); //$NON-NLS-1$		
		rules = dataHandle.visibilityRulesIterator( );

		structHandle = (StructureHandle) rules.next( );
		assertNotNull( structHandle );

		// if no format attribute, use the default value.

		memberHandle = structHandle.getMember( HideRule.FORMAT_MEMBER );
		assertEquals( DesignChoiceConstants.FORMAT_TYPE_ALL, memberHandle
				.getStringValue( ) );

		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		assertNull( memberHandle.getStringValue( ) ); //$NON-NLS-1$

		// the second visibility rule for the data item

		structHandle = (StructureHandle) rules.next( );
		assertNotNull( structHandle );

		memberHandle = structHandle.getMember( HideRule.FORMAT_MEMBER );
		assertEquals( DesignChoiceConstants.FORMAT_TYPE_PDF, memberHandle
				.getStringValue( ) );

		// if no expression, should be empty string

		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		assertNull( memberHandle.getStringValue( ) ); //$NON-NLS-1$

	}

	/**
	 * Tests to write hide rules to the design file.
	 * 
	 * @throws Exception
	 *             if open/write the design file with IO errors.
	 */

	public void testWriteVisibilityRules( ) throws Exception
	{
		openDesign( "ReportItemHandleTest.xml" ); //$NON-NLS-1$

		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "bodyLabel" ); //$NON-NLS-1$		
		Iterator rules = labelHandle.visibilityRulesIterator( );

		// sets with the first visibility rule.

		StructureHandle structHandle = (StructureHandle) rules.next( );
		assertNotNull( structHandle );
		MemberHandle memberHandle = structHandle
				.getMember( HideRule.FORMAT_MEMBER );

		memberHandle.setValue( DesignChoiceConstants.FORMAT_TYPE_REPORTLET );

		// invalid choice value.

		try
		{
			memberHandle.setValue( "noformat" ); //$NON-NLS-1$
		}
		catch ( PropertyValueException e )
		{
			assertEquals(
					PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e
							.getErrorCode( ) );
		}

		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		memberHandle.setValue( "10*20" ); //$NON-NLS-1$

		DataItemHandle dataHandle = (DataItemHandle) designHandle
				.findElement( "bodyData" ); //$NON-NLS-1$		
		rules = dataHandle.visibilityRulesIterator( );

		// get the second visibility rule handle.

		structHandle = (StructureHandle) rules.next( );
		structHandle = (StructureHandle) rules.next( );
		assertNotNull( structHandle );

		memberHandle = structHandle.getMember( HideRule.FORMAT_MEMBER );
		memberHandle.setValue( DesignChoiceConstants.FORMAT_TYPE_REPORTLET );

		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		memberHandle.setValue( "bodyData 2nd rule." ); //$NON-NLS-1$

		// no expression originally, now add one expression.

		structHandle = (StructureHandle) rules.next( );
		assertNull( structHandle );
	}

	/**
	 * Tests common properties on a report item.
	 * 
	 * 
	 * @throws SemanticException
	 */

	public void testProperties( ) throws SemanticException
	{
		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "bodyLabel" ); //$NON-NLS-1$

		labelHandle.setWidth( "15in" ); //$NON-NLS-1$
		assertEquals( "15in", labelHandle.getWidth( ).getStringValue( ) ); //$NON-NLS-1$

		labelHandle.setHeight( "5in" ); //$NON-NLS-1$
		assertEquals( "5in", labelHandle.getHeight( ).getStringValue( ) ); //$NON-NLS-1$

		labelHandle.setX( ".5in" ); //$NON-NLS-1$
		assertEquals( "0.5in", labelHandle.getX( ).getStringValue( ) ); //$NON-NLS-1$

		labelHandle.setY( "5.38in" ); //$NON-NLS-1$
		assertEquals( "5.38in", labelHandle.getY( ).getStringValue( ) ); //$NON-NLS-1$

	}

	/**
	 * Tests the undo operation for invalid style name.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testUndoInvalidStyle( ) throws Exception
	{
		TextItemHandle textHandle = (TextItemHandle) designHandle
				.findElement( "myText" ); //$NON-NLS-1$
		textHandle.setStyleName( "My Style" ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		assertEquals( "unknownStyle", textHandle //$NON-NLS-1$
				.getStringProperty( ReportItem.STYLE_PROP ) );
		assertEquals( null, textHandle //$NON-NLS-1$
				.getElementProperty( ReportItem.STYLE_PROP ) );
		assertEquals( null, textHandle //$NON-NLS-1$
				.getStyle( ) );
	}

	/**
	 * Tests the undo operation for invalid DataSet name.
	 * 
	 * @throws Exception
	 *             if any exception
	 */

	public void testUndoInvalidDataSet( ) throws Exception
	{
		TextItemHandle textHandle = (TextItemHandle) designHandle
				.findElement( "myText" ); //$NON-NLS-1$
		textHandle.setProperty( ReportItem.DATA_SET_PROP, "myDataSet" ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		assertEquals( "unknownDataSet", textHandle //$NON-NLS-1$
				.getStringProperty( ReportItem.DATA_SET_PROP ) );
		assertEquals( null, textHandle //$NON-NLS-1$
				.getElementProperty( ReportItem.DATA_SET_PROP ) );
		assertEquals( null, textHandle //$NON-NLS-1$
				.getDataSet( ) );
	}
}