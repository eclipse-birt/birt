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

package org.eclipse.birt.report.model.metadata;

import java.util.Iterator;
import java.util.List;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for <code>SlotDefn</code>.
 * 
 */
public class SlotDefnTest extends BaseTestCase
{

	SlotDefn slotDefn = null;

	
	/**
	 * Test getters and setters.
	 * 
	 * @throws DesignFileException
	 */

	public void testGetterAndSetter( ) throws DesignFileException
	{

		//IElementDefn elementDefn = MetaDataDictionary.getInstance( )
		//		.getElement( "ReportDesign" ); //$NON-NLS-1$

		//assertNotNull( elementDefn );

		slotDefn = new SlotDefn() ;
		MetadataTestUtil.setMultipleCardinality( slotDefn, true );
		MetadataTestUtil.setDisplayNameKey( slotDefn,
				"Element.ReportDesign.slot.styles" ); //$NON-NLS-1$
		MetadataTestUtil.setName( slotDefn, "Name" ); //$NON-NLS-1$
		MetadataTestUtil.setID( slotDefn, 99 );

		assertEquals( true, slotDefn.isMultipleCardinality( ) );

		assertEquals( "Styles", slotDefn.getDisplayName( ) ); //$NON-NLS-1$
		assertEquals(
				"Element.ReportDesign.slot.styles", slotDefn.getDisplayNameID( ) ); //$NON-NLS-1$
		assertEquals( "Name", slotDefn.getName( ) ); //$NON-NLS-1$
		assertEquals( 99, slotDefn.getSlotID( ) );

	}

	/**
	 * Tests the element type which slot definition allows to contain.
	 */

	public void testCanContain( )
	{

		IElementDefn elementDefn = MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.FREE_FORM_ITEM );
		assertNotNull( elementDefn );

		slotDefn = (SlotDefn) elementDefn.getSlot( FreeForm.REPORT_ITEMS_SLOT );

		FreeForm container = new FreeForm( );
		Label label = new Label( );
		Cell cell = new Cell( );

		assertTrue( slotDefn.canContain( container ) );
		assertTrue( slotDefn.canContain( label ) );

		assertFalse( slotDefn.canContain( cell ) );

		assertTrue( slotDefn.canContain( MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.FREE_FORM_ITEM ) ) );
		assertTrue( slotDefn.canContain( MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.LABEL_ITEM ) ) );
		assertFalse( slotDefn.canContain( MetaDataDictionary.getInstance( )
				.getElement( ReportDesignConstants.CELL_ELEMENT ) ) );

		assertTrue( slotDefn.getContentExtendedElements( ).size( ) > 0 );
	}

	/**
	 * Tests building slot definition.
	 * 
	 * @throws MetaDataException
	 *             if any exception
	 */

	public void testBuild( ) throws MetaDataException
	{
		slotDefn = new SlotDefn( );

		try
		{
			// Test for empty contents type

			MetadataTestUtil.build( slotDefn );
			fail( );
		}
		catch ( MetaDataException e )
		{
		}

		try
		{
			// test for null displayNameID

			MetadataTestUtil.addType( slotDefn,
					ReportDesignConstants.FREE_FORM_ITEM );
			MetadataTestUtil.addType( slotDefn, "Label" ); //$NON-NLS-1$
			MetadataTestUtil.build( slotDefn );
			fail( );
		}
		catch ( MetaDataException e1 )
		{
		}

		// succeed in building

		MetadataTestUtil.setDisplayNameKey( slotDefn,
				"Element.ReportDesign.slot.body" ); //$NON-NLS-1$
		MetadataTestUtil.build( slotDefn );
		assertFalse( slotDefn.canContain( MetaDataDictionary.getInstance( )
				.getElement( "DataSource" ) ) ); //$NON-NLS-1$
		assertTrue( slotDefn.canContain( MetaDataDictionary.getInstance( )
				.getElement( "Label" ) ) ); //$NON-NLS-1$
		assertEquals( "Body", slotDefn.getDisplayName( ) ); //$NON-NLS-1$
	}

	/**
	 * Tests getting semantic validators from Slot Definition.
	 * 
	 * @throws MetaDataParserException
	 *             if any exception
	 */

	public void testSemanticValidator( ) throws MetaDataParserException
	{
		IElementDefn groupDefn = MetaDataDictionary.getInstance( ).getElement(
				ReportDesignConstants.TABLE_GROUP_ELEMENT );
		SlotDefn headerDefn = (SlotDefn) groupDefn
				.getSlot( TableGroup.HEADER_SLOT );
		List validators = headerDefn.getTriggerDefnSet( ).getTriggerList( );
		assertTrue( hasValidator( validators, "InconsistentColumnsValidator" ) ); //$NON-NLS-1$
	}

	private boolean hasValidator( List validators, String name )
	{
		Iterator iter = validators.iterator( );
		while ( iter.hasNext( ) )
		{
			SemanticTriggerDefn defn = (SemanticTriggerDefn) iter.next( );

			if ( defn.getValidatorName( ).equals( name ) )
				return true;
		}

		return false;
	}

}