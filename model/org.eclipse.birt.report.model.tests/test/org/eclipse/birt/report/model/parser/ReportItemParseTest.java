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

package org.eclipse.birt.report.model.parser;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.BaseTestCase;

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
 * 
 * <tr>
 * <td>testParseProperties()</td>
 * <td>Gets visibility rules, parameter bindings in elements and tests whether
 * values match with those defined the design file.</td>
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
 * <td></td>
 * <td>The number of parameter bindings in the label.</td>
 * <td>The number is 0.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The number of parameter bindings in the data.</td>
 * <td>The number is 2.</td>
 * </tr>
 * 
 * <tr>
 * <td>testWriteProperties</td>
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
 * <tr>
 * <td></td>
 * <td>Sets "name" and "express" properties of a parameter binding.</td>
 * <td>"name" and "express" can be written out and the output file matches with
 * the golden file.</td>
 * </tr>
 * 
 */

public class ReportItemParseTest extends BaseTestCase
{

	DesignElement element;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		super.setUp( );

		openDesign( "ReportItemParseTest.xml" ); //$NON-NLS-1$ 
	}

	/**
	 * Test to read hide rules.
	 * 
	 * @throws Exception
	 *             if open the design file with errors.
	 */

	public void testParseProperties( ) throws Exception
	{
		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "bodyLabel" ); //$NON-NLS-1$

		assertEquals( "birt.js.labelHandler", labelHandle //$NON-NLS-1$
				.getEventHandlerClass( ) );

		// checks on-prepare, on-create and on-render values

		assertEquals( "hello, show me on create.", labelHandle.getOnCreate( ) ); //$NON-NLS-1$

		assertEquals( "hello, show me on render.", labelHandle.getOnRender( ) ); //$NON-NLS-1$

		assertEquals( "hello, show me on prepare.", labelHandle.getOnPrepare( ) ); //$NON-NLS-1$

		assertEquals(
				"hello, show me on page break.", labelHandle.getOnPageBreak( ) ); //$NON-NLS-1$

		Iterator rules = labelHandle.visibilityRulesIterator( );

		// checks with the first visibility rule.

		StructureHandle structHandle = (StructureHandle) rules.next( );
		assertNotNull( structHandle );

		MemberHandle memberHandle = structHandle
				.getMember( HideRule.FORMAT_MEMBER );
		assertEquals( DesignChoiceConstants.FORMAT_TYPE_PDF, memberHandle
				.getStringValue( ) );
		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		assertEquals( "word, 10 people", memberHandle.getStringValue( ) ); //$NON-NLS-1$

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

		// parameter binding on this label, no bindings in the list.

		Iterator bindings = labelHandle.paramBindingsIterator( );
		structHandle = (StructureHandle) bindings.next( );
		assertNull( structHandle );

		// tests visibility on the data item.

		DataItemHandle dataHandle = (DataItemHandle) designHandle
				.findElement( "bodyData" ); //$NON-NLS-1$		

		assertEquals( "birt.js.dataHandler", dataHandle //$NON-NLS-1$
				.getEventHandlerClass( ) );

		// checks on-prepare, on-create and on-render values

		assertEquals( "hello, show data on prepare.", dataHandle.getOnPrepare( ) ); //$NON-NLS-1$	
		assertEquals( "hello, show data on render.", dataHandle.getOnRender( ) ); //$NON-NLS-1$

		assertEquals( null, dataHandle.getOnCreate( ) ); 

		rules = dataHandle.visibilityRulesIterator( );
		structHandle = (StructureHandle) rules.next( );

		// if no format attribute, use the default value.

		memberHandle = structHandle.getMember( HideRule.FORMAT_MEMBER );
		assertEquals( DesignChoiceConstants.FORMAT_TYPE_ALL, memberHandle
				.getStringValue( ) );

		// if no expression, should be empty string

		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		assertNull( memberHandle.getStringValue( ) );

		// the second visibility rule for the data item

		structHandle = (StructureHandle) rules.next( );
		assertNotNull( structHandle );

		memberHandle = structHandle.getMember( HideRule.FORMAT_MEMBER );
		assertEquals( DesignChoiceConstants.FORMAT_TYPE_PDF, memberHandle
				.getStringValue( ) );

		// if no expression, should be empty string

		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		assertNull( memberHandle.getStringValue( ) );

		// reads bindings for data.

		bindings = dataHandle.paramBindingsIterator( );
		structHandle = (StructureHandle) bindings.next( );
		assertNotNull( structHandle );

		memberHandle = structHandle.getMember( ParamBinding.PARAM_NAME_MEMBER );
		assertEquals( "param1", memberHandle.getValue( ) ); //$NON-NLS-1$
		memberHandle = structHandle.getMember( ParamBinding.EXPRESSION_MEMBER );
		assertEquals( "value1", memberHandle.getValue( ) ); //$NON-NLS-1$

		structHandle = (StructureHandle) bindings.next( );
		assertNotNull( structHandle );

		memberHandle = structHandle.getMember( ParamBinding.PARAM_NAME_MEMBER );
		assertEquals( "param2", memberHandle.getValue( ) ); //$NON-NLS-1$
		memberHandle = structHandle.getMember( ParamBinding.EXPRESSION_MEMBER );
		assertEquals( "value2", memberHandle.getValue( ) ); //$NON-NLS-1$

		structHandle = (StructureHandle) bindings.next( );
		assertNull( structHandle );

		// tests toc on the free form item.

		assertEquals( "2005 Statistics", dataHandle.getTocExpression( ) ); //$NON-NLS-1$	

		FreeFormHandle form = (FreeFormHandle) designHandle
				.findElement( "free form" ); //$NON-NLS-1$	
		assertEquals( "\"This Section\"", form.getTocExpression( ) ); //$NON-NLS-1$	

	}

	/**
	 * Test to write hide rules to the design file.
	 * 
	 * @throws Exception
	 *             if open/write the design file with IO errors.
	 */

	public void testWriteProperties( ) throws Exception
	{
		LabelHandle labelHandle = (LabelHandle) designHandle
				.findElement( "bodyLabel" ); //$NON-NLS-1$

		labelHandle.setOnCreate( "my new label on create" ); //$NON-NLS-1$
		labelHandle.setOnRender( null );
		labelHandle.setOnPrepare( "my new label on prepare" ); //$NON-NLS-1$
		labelHandle.setOnPageBreak( "my new label on page break" ); //$NON-NLS-1$

		Iterator rules = labelHandle.visibilityRulesIterator( );

		// sets with the first visibility rule.

		StructureHandle structHandle = (StructureHandle) rules.next( );
		assertNotNull( structHandle );
		MemberHandle memberHandle = structHandle
				.getMember( HideRule.FORMAT_MEMBER );

		memberHandle.setValue( DesignChoiceConstants.FORMAT_TYPE_REPORTLET );

		// visibility rule now support user defined format, no exception

		memberHandle.setValue( "userDefinedformat" ); //$NON-NLS-1$
		
		memberHandle = structHandle.getMember( HideRule.VALUE_EXPR_MEMBER );
		memberHandle.setValue( "10*20" ); //$NON-NLS-1$

		labelHandle.setTocExpression( "new 2005 statistics" ); //$NON-NLS-1$

		DataItemHandle dataHandle = (DataItemHandle) designHandle
				.findElement( "bodyData" ); //$NON-NLS-1$	

		dataHandle.setOnCreate( "my new data on create" ); //$NON-NLS-1$
		dataHandle.setOnRender( "my new data on render" ); //$NON-NLS-1$
		dataHandle.setOnPrepare( null );
		dataHandle.setEventHandlerClass( "my new data handler class" ); //$NON-NLS-1$

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

		// writes bindings for data.

		Iterator bindings = dataHandle.paramBindingsIterator( );
		structHandle = (StructureHandle) bindings.next( );
		assertNotNull( structHandle );

		memberHandle = structHandle.getMember( ParamBinding.PARAM_NAME_MEMBER );
		memberHandle.setValue( "no paramter 1" ); //$NON-NLS-1$
		memberHandle = structHandle.getMember( ParamBinding.EXPRESSION_MEMBER );
		memberHandle.setValue( "setting value 1" ); //$NON-NLS-1$

		structHandle = (StructureHandle) bindings.next( );
		assertNotNull( structHandle );
		memberHandle = structHandle.getMember( ParamBinding.PARAM_NAME_MEMBER );
		memberHandle.setValue( "no paramter 2" ); //$NON-NLS-1$
		memberHandle = structHandle.getMember( ParamBinding.EXPRESSION_MEMBER );
		memberHandle.setValue( "setting value 2" ); //$NON-NLS-1$

		// clear toc on Data

		dataHandle.setTocExpression( null );

		saveAs( "ReportItemParseTest_out.xml" ); //$NON-NLS-1$
		assertTrue( compareTextFile( "ReportItemParseTest_golden.xml", //$NON-NLS-1$
				"ReportItemParseTest_out.xml" ) ); //$NON-NLS-1$
	}

}