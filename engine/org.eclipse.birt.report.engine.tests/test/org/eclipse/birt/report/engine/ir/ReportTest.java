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

package org.eclipse.birt.report.engine.ir;

import java.util.Random;

import junit.framework.TestCase;

/**
 * Report test
 * 
 * @version $Revision: 1.6 $ $Date: 2005/11/11 06:26:43 $
 */
public class ReportTest extends TestCase
{

	private Random random = new Random( );

	/**
	 * Test add/getPageSetup and findMasterPage methods
	 * 
	 * add a random list of master pages into a page setup and set it into the
	 * report
	 * 
	 * then get it and find the master pages by one to test if they work
	 * correctly
	 */

	public void testPageSetup( )
	{
		Report report = new Report( );
		PageSetupDesign pagesetup = new PageSetupDesign( );
		MasterPageDesign[] masterpages = new MasterPageDesign[random
				.nextInt( 10 ) + 1];

		for ( int i = 0; i < masterpages.length; i++ )
		{
			masterpages[i] = new GraphicMasterPageDesign( );
			masterpages[i].setName( "Page" + i );
			pagesetup.addMasterPage( masterpages[i] );
		}

		//Set
		report.setPageSetup( pagesetup );

		//Get
		assertEquals( report.getPageSetup( ), pagesetup );

		//Find
		for ( int i = 0; i < masterpages.length; i++ )
		{
			assertEquals( report.findMasterPage( "Page" + i ), masterpages[i] );
		}

	}

	/**
	 * Test add/getContent methods
	 * 
	 * add a random list of report items into the report
	 * 
	 * then get the contents one by one to test if they work correctly
	 */
	public void testAddContent( )
	{
		Report report = new Report( );
		ReportItemSet set = new ReportItemSet( );

		//Set
		for ( int i = 0; i < set.length; i++ )
		{
			report.addContent( set.getItem( i ) );
		}

		//Get
		assertEquals( report.getContentCount( ), set.length );
		for ( int i = 0; i < set.length; i++ )
		{
			assertEquals( report.getContent( i ), set.getItem( i ) );
		}
		assertEquals( report.getContents( ), set.getItems( ) );
	}

	/**
	 * Test add/getUnit methods
	 * 
	 * set a unit
	 * 
	 * then get it to test if they work correctly
	 */
	public void testUnit( )
	{
		Report report = new Report( );
		String[] units = {EngineIRConstants.UNITS_CM,
				EngineIRConstants.UNITS_IN, EngineIRConstants.UNITS_MM,
				EngineIRConstants.UNITS_PT};

		for ( int i = 0; i < units.length; i++ )
		{
			//Set
			report.setUnit( units[i] );

			//Get
			assertEquals( report.getUnit( ), units[i] );
		}
	}

}
