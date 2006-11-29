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
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests the paste and canContain methods in the SlotHandle.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testPaste()}</td>
 * <td>Tests paste a data-set to another design.</td>
 * <td>DataSource referred by the data-set was invalid.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Tests paste a data-set to the same design.</td>
 * <td>DataSource referred by the data-set was valid and the back references
 * were changed.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Tests paste a text item with a shared style to another design.</td>
 * <td>The share style referred by the text item was invalid.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Tests paste a text item with a shared style to the same design.</td>
 * <td>The shared style referred by the text item was valid and the back
 * references were changed.</td>
 * </tr>
 * 
 * </table>
 *  
 */

public class SlotHandleTest extends BaseTestCase
{
	String fileName = "Improved_test4.xml";
	/**
	 * @param name
	 */
	public SlotHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public static Test suite(){
		return new TestSuite(SlotHandleTest.class);
		
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		removeResource( );
		copyResource_INPUT( fileName , fileName );
		SessionHandle sessionHandle = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = sessionHandle.createDesign( );
		design = designHandle.getDesign( );
	}

	public void tearDown( )
	{
		removeResource( );
	}
	/**
	 * Tests paste methods with element references.
	 * 
	 * @throws SemanticException
	 *  
	 */

	public void testPaste( ) throws SemanticException
	{
		ElementFactory factory = new ElementFactory( design );
		SharedStyleHandle style = factory.newStyle( "style" ); //$NON-NLS-1$
		style.getColor( ).setValue( IColorConstants.AQUA );
		designHandle.getStyles( ).add( style );

		designHandle.getDataSources( ).add(
				factory.newOdaDataSource( "DataSource1" ) ); //$NON-NLS-1$

		DataSetHandle dataset = factory.newOdaDataSet( "DataSet1" ); //$NON-NLS-1$
		designHandle.getDataSets( ).add( dataset );
		dataset.setDataSource( "DataSource1" ); //$NON-NLS-1$

		TextItemHandle text = factory.newTextItem( "text" ); //$NON-NLS-1$
		text.setStyle( style );

		// test on copy/paste for a datasource and a dataset

		SessionHandle sessionHandle = DesignEngine.newSession( ULocale.ENGLISH );
		ReportDesignHandle newDesignHandle = sessionHandle.createDesign( );
		ReportDesign newDesign = newDesignHandle.getDesign( );

		OdaDataSet newDataSet = (OdaDataSet) dataset.copy( );
		List errors = newDesignHandle.getDataSets( ).paste(
				newDataSet.getHandle( newDesignHandle.getDesign( ) ) );
		assertEquals( 1, errors.size( ) );
		assertEquals( SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF, ( (ErrorDetail) errors
				.get( 0 ) ).getErrorCode( ) );

		assertNull( ( (OdaDataSetHandle) newDataSet
				.getHandle( newDesignHandle.getDesign( ) ) ).getDataSource( ) );

		// if copy/paste to the same report, no errors.

		newDataSet = (OdaDataSet) dataset.copy( );
		designHandle.rename( newDataSet.getHandle( design ) );
		errors = designHandle.getDataSets( ).paste(
				newDataSet.getHandle( design ) );

		assertEquals( 0, errors.size( ) );
		assertNotNull( ( (OdaDataSetHandle) newDataSet
				.getHandle( newDesignHandle.getDesign( ) ) ).getDataSource( ) );

		Iterator iter = designHandle.findDataSource( "DataSource1" ) //$NON-NLS-1$
				.clientsIterator( );
		int count = 0;
		while ( iter.hasNext( ) )
		{
			count++;
			iter.next( );
		}
		assertEquals( 2, count );

		// test on copy/paste for a styledElement and Style

		TextItem newText = (TextItem) text.copy( );
		errors = newDesignHandle.getBody( ).paste(
				newText.getHandle( newDesign ) );
		assertEquals( 1, errors.size( ) );
		assertEquals( StyleException.DESIGN_EXCEPTION_NOT_FOUND,
				( (ErrorDetail) errors.get( 0 ) ).getErrorCode( ) );
		assertEquals( IColorConstants.BLACK, ( (TextItemHandle) newText
				.getHandle( newDesignHandle.getDesign( ) ) )
				.getProperty( Style.COLOR_PROP ) );

		//if copy/paste to the same report, no errors.

		newText = (TextItem) text.copy( );
		designHandle.rename( newText.getHandle( design ) );
		errors = designHandle.getBody( ).paste( newText.getHandle( design ) );
		assertEquals( 0, errors.size( ) );

		assertEquals( IColorConstants.AQUA, ( (TextItemHandle) newText
				.getHandle( newDesignHandle.getDesign( ) ) )
				.getProperty( Style.COLOR_PROP ) );

		iter = style.clientsIterator( );
		count = 0;
		while ( iter.hasNext( ) )
		{
			count++;
			iter.next( );
		}
		assertEquals( 2, count );

	}
	
	public void testcanContainGroupName( ) throws SemanticException
	{
		
		sessionHandle = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = sessionHandle.createDesign( );
		design = (ReportDesign) designHandle.getModule();

		ElementFactory factory = new ElementFactory( design );

		TableHandle table = factory.newTableItem( "table", 1 ); //$NON-NLS-1$

		// test two table group or list groups with same names without datasets.

		TableGroupHandle tableGroup = table.getElementFactory().newTableGroup( );
		tableGroup.setName( "Group1" ); //$NON-NLS-1$
		SlotHandle slot = table.getSlot(TableItem.GROUP_SLOT);
        assertTrue(slot.canContain(tableGroup));
		table.getGroups( ).add( tableGroup );
		assertFalse( slot.canContain(tableGroup));

		tableGroup = table.getElementFactory().newTableGroup( );
		tableGroup.setName( "Group2" ); //$NON-NLS-1$
		assertTrue( slot.canContain(tableGroup));
	}
	
	
	public void testcanContainSimpleMasterPage( ) throws Exception
	{
		openDesign( fileName );
		SimpleMasterPageHandle mHandle = (SimpleMasterPageHandle) designHandle
				.findMasterPage( "Page1" ); //$NON-NLS-1$
		assertNotNull("should not be null", mHandle);
		SlotHandle slot = mHandle.getPageHeader( );
		GridHandle grid = mHandle.getElementFactory().newGridItem("grid");
		assertEquals( 1, slot.getCount( ) );
		assertEquals( "text_1", slot.get( 0 ).getName( ) ); //$NON-NLS-1$
		assertFalse(slot.canContain(grid));
		slot = mHandle.getPageFooter( );
		assertEquals( 1, slot.getCount( ) );
		assertEquals( "text_2", slot.get( 0 ).getName( ) ); //$NON-NLS-1$
		assertFalse(slot.canContain(grid));
	}

	

}