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
import java.util.List;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: * collapse" bordercolor="#111111" width="100%" id="AutoNumber6">
 * <tr>
 * <td width="33%"><b>Method </b></td>
 * <td width="33%"><b>Test Case </b></td>
 * <td width="34%"><b>Expected Result </b></td>
 * </tr>
 * <tr>
 * <td width="33%">{@link #testParser()}</td>
 * <td width="33%">Test labelItem properties in PageSetup and Body slots.</td>
 * <td width="34%">the correct value and slot ID returned.</td>
 * </tr>
 * <tr>
 * <td width="33%"></td>
 * <td width="33%">Test the element reference type property</td>
 * <td width="34%">the correct value returned.</td>
 * </tr>
 * <tr>
 * <td width="33%">{@link #testSemanticCheck()}</td>
 * <td width="33%">Test the Text property is required for labelItem</td>
 * <td width="34%">Semantic error</td>
 * </tr>
 * <tr>
 * <td width="33%">{@link #testWriter()}</td>
 * <td width="33%">Set new value to properties and save it.</td>
 * <td width="34%">new value should be save into the output file.</td>
 * </tr>
 * </table>
 * 
 */

public class LabelItemParserTest extends ParserTestCase
{

	String fileName = "LabelItemParseTest.xml"; //$NON-NLS-1$
	String goldenFileName = "LabelItemParseTest_golden.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "LabelItemParseTest_1.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/**
	 * Tests Label element in the PageSetup and Body slots.
	 * 
	 * @throws Exception
	 *             if opening design file failed.
	 */

	public void testParser( ) throws Exception
	{

		LabelHandle labelHandle = getLabel( );

		assertEquals( "6mm", labelHandle.getStringProperty( Label.X_PROP ) ); //$NON-NLS-1$
		assertEquals( "0.5mm", labelHandle.getStringProperty( Label.Y_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"0.25mm", labelHandle.getStringProperty( Label.HEIGHT_PROP ) ); //$NON-NLS-1$
		assertEquals( "1mm", labelHandle.getStringProperty( Label.WIDTH_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"Today's Date", labelHandle.getStringProperty( Label.TEXT_PROP ) ); //$NON-NLS-1$
		OdaDataSet dataSet = (OdaDataSet) design.findDataSet( "firstDataSet" ); //$NON-NLS-1$
		assertNotNull( dataSet );

		assertEquals( dataSet.getName( ), labelHandle
				.getProperty( Label.DATA_SET_PROP ) );

		// test the sytle property in label
		assertEquals( "red", labelHandle.getStringProperty( Style.COLOR_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"\"labelFace\"", labelHandle.getStringProperty( Style.FONT_FAMILY_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"4mm", labelHandle.getStringProperty( Style.FONT_SIZE_PROP ) ); //$NON-NLS-1$
		assertEquals(
				"bold", labelHandle.getStringProperty( Style.FONT_WEIGHT_PROP ) ); //$NON-NLS-1$

		// test the style property whcih was not defined on the label itself
		assertEquals(
				"none", labelHandle.getStringProperty( Style.TEXT_UNDERLINE_PROP ) ); //$NON-NLS-1$

		// test the style property which does not belong to label

		assertEquals( null, labelHandle
				.getStringProperty( Style.HIGHLIGHT_RULES_PROP ) );

		ActionHandle action = labelHandle.getActionHandle( );
		assertNotNull( action );
		assertEquals( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK, action
				.getLinkType( ) );
		assertEquals( "http://localhost:8080/", action.getURI( ) ); //$NON-NLS-1$
		
		// test default value of Role in Label
		assertEquals( "p", labelHandle.getTagType( ) ); //$NON-NLS-1$

		// reads in a lable that exists in the body.

		labelHandle = (LabelHandle) designHandle.findElement( "bodyLabel" ); //$NON-NLS-1$

		assertEquals( "<hello></hello><test><test1></test1></test>", //$NON-NLS-1$
				labelHandle.getCustomXml( ) );
		assertEquals(
				"yellow", labelHandle.getStringProperty( Style.COLOR_PROP ) ); //$NON-NLS-1$ 

		assertEquals( "Body's slot", labelHandle.getText( ) ); //$NON-NLS-1$
		assertEquals( "label help text", labelHandle.getHelpText( ) ); //$NON-NLS-1$
		assertEquals( "help key", labelHandle.getHelpTextKey( ) ); //$NON-NLS-1$
		assertEquals( "text key", labelHandle.getTextKey( ) ); //$NON-NLS-1$
		
		assertEquals( "Div", labelHandle.getTagType( ) ); //$NON-NLS-1$
		assertEquals( "English", labelHandle.getLanguage( ) ); //$NON-NLS-1$
		assertEquals( "Alt Text", labelHandle.getAltTextExpression( ).getStringExpression( ) ); //$NON-NLS-1$
		assertEquals( 1, labelHandle.getOrder( ) ); //$NON-NLS-1$

		// make sure that this label exists in the body slot.

		assertEquals( ReportDesign.BODY_SLOT, labelHandle.getContainer( )
				.findContentSlot( labelHandle ) );

		// reads in a label that exists in the component.

		labelHandle = (LabelHandle) designHandle.findElement( "child1" ); //$NON-NLS-1$

		assertEquals( "Today's Date", labelHandle.getText( ) ); //$NON-NLS-1$
		assertEquals( "label help text", labelHandle.getHelpText( ) ); //$NON-NLS-1$
		assertEquals( "help key", labelHandle.getHelpTextKey( ) ); //$NON-NLS-1$

		// reads in a label that exists in the freeform.

		labelHandle = (LabelHandle) designHandle.findElement( "label3" ); //$NON-NLS-1$

		assertEquals( "Customer Name", labelHandle.getText( ) ); //$NON-NLS-1$

		// reads in a label that exists in the list header.

		labelHandle = (LabelHandle) designHandle
				.findElement( "listHeaderLabel" ); //$NON-NLS-1$

		assertEquals( "list header", labelHandle.getText( ) ); //$NON-NLS-1$
		assertTrue( labelHandle.pushDown( ) );

		// reads in a label that exists in the list detail.

		labelHandle = (LabelHandle) designHandle
				.findElement( "listDetailLabel" ); //$NON-NLS-1$

		assertEquals( "list detail", labelHandle.getText( ) ); //$NON-NLS-1$
		assertTrue( labelHandle.pushDown( ) );

		// reads in a label that exists in the list footer.

		labelHandle = (LabelHandle) designHandle
				.findElement( "listFooterLabel" ); //$NON-NLS-1$

		assertEquals( "list footer", labelHandle.getText( ) ); //$NON-NLS-1$

		// reads in a label that exists in the table header.

		labelHandle = (LabelHandle) designHandle.findElement( "headerLabel" ); //$NON-NLS-1$

		assertEquals( "Customer Name", labelHandle.getText( ) ); //$NON-NLS-1$

		// reads in a label that exists in the table detail.

		labelHandle = (LabelHandle) designHandle.findElement( "detailLabel" ); //$NON-NLS-1$

		assertEquals( "Address", labelHandle.getText( ) ); //$NON-NLS-1$

		// reads in a label that exists in the table footer.

		labelHandle = (LabelHandle) designHandle.findElement( "footerLabel" ); //$NON-NLS-1$

		assertEquals( "Address4", labelHandle.getText( ) ); //$NON-NLS-1$

		// reads in a label that exists in the scratchpad.

		labelHandle = (LabelHandle) designHandle
				.findElement( "scratchpadLabel" ); //$NON-NLS-1$

		assertEquals( "scratch", labelHandle.getText( ) ); //$NON-NLS-1$

	}

	/**
	 * This test sets properties, writes the design file and compares it with
	 * golden file.
	 * 
	 * @throws Exception
	 *             if opening or saving design file failed.
	 */

	public void testWriter( ) throws Exception
	{
		LabelHandle labelHandle = getLabel( );

		// set x to 7
		labelHandle.setProperty( Label.X_PROP, "7mm" ); //$NON-NLS-1$
		labelHandle.setProperty( Label.HEIGHT_PROP, "0.5mm" ); //$NON-NLS-1$

		labelHandle.setProperty( Label.STYLE_PROP, null );

		OdaDataSet dataSet = (OdaDataSet) design.findDataSet( "secondDataSet" ); //$NON-NLS-1$
		assertNotNull( dataSet );

		// labelHandle.setProperty(Label.DATA_SET_PROP, dataSet);
		labelHandle.setProperty( Label.DATA_SET_PROP, null );

		labelHandle.setProperty( Label.NAME_PROP, "labelTestWriter" ); //$NON-NLS-1$

		ActionHandle action = labelHandle.getActionHandle( );
		assertNotNull( action );
		action.setURI( "www.myhost.com" ); //$NON-NLS-1$

		// reads in a label that exists in the body.

		labelHandle = (LabelHandle) designHandle.findElement( "bodyLabel" ); //$NON-NLS-1$
		assertEquals( ReportDesign.BODY_SLOT, labelHandle.getContainer( )
				.findContentSlot( labelHandle ) );

		labelHandle.setProperty( Style.COLOR_PROP, "blue" ); //$NON-NLS-1$ 
		assertEquals( ReportDesign.BODY_SLOT, labelHandle.getContainer( )
				.findContentSlot( labelHandle ) );
		labelHandle.setText( "Final day" ); //$NON-NLS-1$
		labelHandle.setWidth( "5.0mm" ); //$NON-NLS-1$
		labelHandle.setTextKey( "new text resource key" ); //$NON-NLS-1$
		labelHandle.setHelpText( "new help text" ); //$NON-NLS-1$
		labelHandle.setHelpTextKey( "new help text key" ); //$NON-NLS-1$
		
		labelHandle.setTagType( "Div" ); //$NON-NLS-1$
		labelHandle.setLanguage( "English" ); //$NON-NLS-1$
		labelHandle.setAltTextExpression( new Expression("Alt Text", ExpressionType.CONSTANT) ); //$NON-NLS-1$
		labelHandle.setOrder( 1 ); //$NON-NLS-1$

		labelHandle.setCustomXml( "new custom <text> </text> for bodyLabel" ); //$NON-NLS-1$

		// set widows and orphans
		labelHandle.setProperty( IStyleModel.WIDOWS_PROP, "5" ); //$NON-NLS-1$
		labelHandle.setProperty( IStyleModel.ORPHANS_PROP, "4" ); //$NON-NLS-1$

		// make sure that this label exists in the body slot.

		assertEquals( ReportDesign.BODY_SLOT, labelHandle.getContainer( )
				.findContentSlot( labelHandle ) );

		labelHandle = (LabelHandle) designHandle
				.findElement( "listDetailLabel" ); //$NON-NLS-1$
		labelHandle.setPushDown( true );

		save( );
		assertTrue( compareFile( goldenFileName ) );
	}

	/**
	 * Test semantic errors.
	 * 
	 */

	public void testSemanticCheck( )
	{
		try
		{
			openDesign( semanticCheckFileName );
		}
		catch ( DesignFileException e )
		{
			List list = e.getErrorList( );
			ErrorDetail detail = (ErrorDetail) list.get( 0 );
			assertEquals( detail.getErrorCode( ),
					NameException.DESIGN_EXCEPTION_DUPLICATE );
		}

	}

	/**
	 * Returns a lable handle by reading a label in design file.
	 * 
	 * @return a label handle.
	 * @throws Exception
	 *             if opening design file failed.
	 */

	private LabelHandle getLabel( ) throws Exception
	{
		openDesign( fileName );
		MasterPageHandle masterPageHandle = (MasterPageHandle) designHandle
				.getMasterPages( ).get( 0 );
		assertEquals( 2, masterPageHandle.getSlot( 0 ).getCount( ) );

		Iterator it = masterPageHandle.getSlot( 0 ).iterator( );

		assertEquals( "label1", ( (LabelHandle) it.next( ) ).getName( ) ); //$NON-NLS-1$
		LabelHandle labelHandle = (LabelHandle) it.next( );
		assertEquals( "label2", labelHandle.getName( ) ); //$NON-NLS-1$

		return labelHandle;
	}
}