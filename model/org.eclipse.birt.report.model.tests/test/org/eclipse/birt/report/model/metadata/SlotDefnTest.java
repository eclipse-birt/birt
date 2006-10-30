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
import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for <code>SlotDefn</code>.
 * 
 */
public class SlotDefnTest extends BaseTestCase
{

	SlotDefn slotDefn = null;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		MetaDataDictionary.reset( );
		MetaDataReader
				.read( ReportDesign.class.getResourceAsStream( "rom.def" ) ); //$NON-NLS-1$
		ThreadResources.setLocale( ULocale.ENGLISH );
	}

	/**
	 * Test getters and setters.
	 * 
	 * @throws DesignFileException
	 */

	public void testGetterAndSetter( ) throws DesignFileException
	{

		IElementDefn elementDefn = MetaDataDictionary.getInstance( )
				.getElement( "ReportDesign" ); //$NON-NLS-1$

		assertNotNull( elementDefn );

		slotDefn = (SlotDefn) elementDefn.getSlot( 0 );

		slotDefn.setMultipleCardinality( true );
		slotDefn.setDisplayNameID( "Element.ReportDesign.slot.styles" ); //$NON-NLS-1$
		slotDefn.setName( "Name" ); //$NON-NLS-1$
		slotDefn.setSlotID( 99 );

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

			slotDefn.build( );
			fail( );
		}
		catch ( MetaDataException e )
		{
		}

		try
		{
			// test for null displayNameID

			slotDefn.addType( ReportDesignConstants.FREE_FORM_ITEM );
			slotDefn.addType( "Label" ); //$NON-NLS-1$
			slotDefn.build( );
			fail( );
		}
		catch ( MetaDataException e1 )
		{
		}

		// succeed in building

		slotDefn.setDisplayNameID( "Element.ReportDesign.slot.body" ); //$NON-NLS-1$
		slotDefn.build( );
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
		MetaDataDictionary.reset( );
		ThreadResources.setLocale( ULocale.ENGLISH );
		MetaDataReader
				.read( ReportDesign.class.getResourceAsStream( "rom.def" ) ); //$NON-NLS-1$

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