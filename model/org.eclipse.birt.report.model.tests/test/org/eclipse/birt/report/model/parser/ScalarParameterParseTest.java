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

import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The Test Case of scalar parameter parse.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * 
 * <tr>
 * <td>{@link #testProperties()}</td>
 * <td>parse the design file and check all the properties of scalar parameter
 * </td>
 * <td>Content of the property is consistent with the design file</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWrite()}</td>
 * <td>parse, write and parse, write again. The result of two writer files is
 * the same.</td>
 * <td>The two writer file is the same.</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class ScalarParameterParseTest extends BaseTestCase
{

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		openDesign( "ScalarParameterParseTest.xml" ); //$NON-NLS-1$ 	

	}

	/**
	 * Test the write for user-defined properties.
	 * 
	 * @throws Exception
	 */

	public void testWrite( ) throws Exception
	{

		SlotHandle params = designHandle.getParameters( );
		ScalarParameterHandle handle1 = (ScalarParameterHandle) params.get( 0 );

		assertTrue( handle1.isHidden( ) );
		handle1.setHidden( true );
		assertTrue( handle1.isHidden( ) );
		handle1.setValidate( "new validation test" ); //$NON-NLS-1$

		handle1.setDataType( DesignChoiceConstants.PARAM_TYPE_DATETIME );
		assertEquals( DesignChoiceConstants.PARAM_TYPE_DATETIME, handle1
				.getDataType( ) );

		handle1.setConcealValue( true );
		assertTrue( handle1.isConcealValue( ) );

		handle1.setDefaultValue( "new default value" ); //$NON-NLS-1$
		assertEquals( "new default value", handle1.getDefaultValue( ) ); //$NON-NLS-1$

		handle1.setAllowNull( false );
		assertFalse( handle1.allowNull( ) );

		handle1.setAllowBlank( true );
		assertTrue( handle1.allowBlank( ) );

		handle1.setCategory( "Custom" ); //$NON-NLS-1$
		assertEquals( "Custom", handle1.getCategory( ) ); //$NON-NLS-1$

		handle1.setPattern( "aaaaa" ); //$NON-NLS-1$
		assertEquals( "aaaaa", handle1.getPattern( ) ); //$NON-NLS-1$

		handle1.setFormat( "$***,***.**" ); //$NON-NLS-1$
		assertEquals( "$***,***.**", handle1.getFormat( ) ); //$NON-NLS-1$

		handle1
				.setControlType( DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON );
		assertEquals( DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON, handle1
				.getControlType( ) );

		handle1.setAlignment( DesignChoiceConstants.SCALAR_PARAM_ALIGN_RIGHT );
		assertEquals( DesignChoiceConstants.SCALAR_PARAM_ALIGN_RIGHT, handle1
				.getAlignment( ) );

		handle1.setDataSetName( "ds1" ); //$NON-NLS-1$
		handle1.setValueExpr( "new value column" ); //$NON-NLS-1$
		handle1.setLabelExpr( "new label column" ); //$NON-NLS-1$

		handle1.setMustMatch( false );
		assertFalse( handle1.isMustMatch( ) );

		handle1.setFixedOrder( false );

		// test properties of a parameter

		handle1.setHelpText( "new help text" ); //$NON-NLS-1$
		handle1.setHelpTextKey( "new resource key of the help text" ); //$NON-NLS-1$

		ScalarParameterHandle handle2 = (ScalarParameterHandle) params.get( 1 );
		assertEquals( "dynamic", handle2.getValueType( ) ); //$NON-NLS-1$
		assertEquals( "ds1", handle2.getDataSetName( ) ); //$NON-NLS-1$
		assertEquals( "row[\"test\"]", handle2.getValueExpr( ) ); //$NON-NLS-1$

		handle2.setValueType( DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC );
		handle2.setDataSetName( null );
		handle2.setValueExpr( null );
		handle2.setPromptText( "new Text" ); //$NON-NLS-1$
		handle2.setListlimit( 300 );

		saveAs( "ScalarParameterParseTest_out.xml" ); //$NON-NLS-1$
		assertTrue( compareTextFile( "ScalarParameterParseTest_golden.xml", //$NON-NLS-1$
				"ScalarParameterParseTest_out.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Test the properties for user-defined properties.
	 * 
	 * @throws Exception
	 */

	public void testProperties( ) throws Exception
	{
		SlotHandle params = designHandle.getSlot( ReportDesign.PARAMETER_SLOT );
		assertEquals( 3, params.getCount( ) );

		ScalarParameterHandle handle = (ScalarParameterHandle) params.get( 0 );

		assertEquals( "scalar para help", handle.getHelpText( ) ); //$NON-NLS-1$
		assertEquals( "help", handle.getHelpTextKey( ) ); //$NON-NLS-1$

		assertTrue( handle.isHidden( ) );
		assertEquals( DesignChoiceConstants.PARAM_TYPE_DECIMAL, handle
				.getDataType( ) );
		assertEquals( "the validation test", handle.getValidate( ) ); //$NON-NLS-1$
		assertFalse( handle.isConcealValue( ) );
		assertEquals( "State", handle.getDefaultValue( ) ); //$NON-NLS-1$

		assertTrue( handle.allowNull( ) );
		assertFalse( handle.allowBlank( ) );

		assertEquals( "##,###.##", handle.getFormat( ) ); //$NON-NLS-1$

		assertEquals( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX, handle
				.getControlType( ) );

		assertEquals( DesignChoiceConstants.SCALAR_PARAM_ALIGN_AUTO, handle
				.getAlignment( ) );

		assertFalse( handle.isMustMatch( ) ); //$NON-NLS-1$
		assertTrue( handle.isFixedOrder( ) ); //$NON-NLS-1$

		StructureHandle[] choices = new StructureHandle[3];
		int count = 0;

		Iterator iter = null;
		for ( iter = handle.choiceIterator( ); iter.hasNext( ); count++ )
			choices[count] = (StructureHandle) ( iter.next( ) );

		assertEquals( 3, count );

		assertEquals( "option 1", //$NON-NLS-1$
				choices[0].getMember( SelectionChoice.VALUE_MEMBER ).getValue( ) );
		assertEquals( "option 1 label", //$NON-NLS-1$
				choices[0].getMember( SelectionChoice.LABEL_MEMBER ).getValue( ) );
		assertEquals( "key 1 for label 1", //$NON-NLS-1$
				choices[0]
						.getMember( SelectionChoice.LABEL_RESOURCE_KEY_MEMBER )
						.getValue( ) );

		assertEquals( "option 3", //$NON-NLS-1$
				choices[2].getMember( SelectionChoice.VALUE_MEMBER ).getValue( ) );
		assertNull( choices[2].getMember( SelectionChoice.LABEL_MEMBER )
				.getValue( ) );
		assertNull( choices[2].getMember(
				SelectionChoice.LABEL_RESOURCE_KEY_MEMBER ).getValue( ) );

		handle = (ScalarParameterHandle) params.get( 1 );
		assertEquals( "City", handle.getPromptText( ) ); //$NON-NLS-1$

		handle.setPromptText( "new Text" ); //$NON-NLS-1$
		assertEquals( "new Text", handle.getPromptText( ) ); //$NON-NLS-1$

		assertEquals( 100, handle.getListlimit( ) ); //$NON-NLS-1$

		handle.setListlimit( 200 );
		assertEquals( 200, handle.getListlimit( ) ); //$NON-NLS-1$

	}

	/**
	 * Tests backward compatibility.
	 * 
	 * Case 1
	 * <ul>
	 * <li>"literal value" --> literal value
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testBackwardCompatibility( ) throws Exception
	{
		SlotHandle params = designHandle.getParameters( );
		ScalarParameterHandle param = (ScalarParameterHandle) params.get( 2 );
		assertEquals( "default value", param.getDefaultValue( ) );
	}
}
