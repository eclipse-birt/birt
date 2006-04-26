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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;


public class AutotextHandleTest extends BaseTestCase
{
   private ElementFactory factory = null;
   private AutoTextHandle autotext = null;
   private AutoTextHandle autotext1 = null;
   private AutoTextHandle autotext2 = null;
   private  SimpleMasterPageHandle masterpage = null;
     
	public AutotextHandleTest( String name )
	{
		super( name );
		// TODO Auto-generated constructor stub
	}
    public static Test suite(){
		
		return new TestSuite(AutotextHandleTest.class);
	}
	
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		SessionHandle designSession = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = designSession.createDesign( );
		factory = new ElementFactory(designHandle.getModule( ));
		autotext = factory.newAutoText( "autotext" );
		autotext1 = factory.newAutoText( "autotext1" );
		masterpage =  factory.newSimpleMasterPage( "masterpage" );
		designHandle.getMasterPages( ).add( masterpage );
	}
	
	public void testAddAndDeleteAutotext( ) throws Exception
	{
	   	   
		//add autotext to master page header/footer
		masterpage.getPageHeader( ).add( autotext );
		assertEquals(1,masterpage.getPageHeader( ).getContents( ).size( ));
	
		masterpage.getPageFooter( ).add( autotext1 );
		assertEquals(1,masterpage.getPageFooter( ).getContents( ).size( ));
	
		//add more autotext
		try{
			autotext2 = factory.newAutoText( "autotext2" );
			masterpage.getPageHeader( ).add( autotext2);
			fail();
		}
		catch (ContentException e)
		{
			assertNotNull(e);
		}
		
	    //delete autotext from master page header/footer
	    masterpage.getPageHeader( ).drop( autotext );
	    assertEquals(0,masterpage.getPageHeader( ).getContents( ).size( ));
		masterpage.getPageFooter( ).drop( autotext1 );
		assertEquals(0,masterpage.getPageFooter( ).getContents( ).size( ));
		
		//add autotext to the container
		TableHandle table = factory.newTableItem( "table", 3, 1, 1, 1 );
		RowHandle detail = (RowHandle)table.getSlot( TableItem.HEADER_SLOT ).get( 0 );
		CellHandle cell = (CellHandle)detail.getSlot( TableRow.CONTENT_SLOT ).get( 0 );
		cell.getSlot( Cell.CONTENT_SLOT ).add( autotext );
		
	}
	
	public void testAutotextType( ) throws Exception
	{
		autotext.setAutoTextType( DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER);
		autotext.setAutoTextType( DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE);
		assertEquals(DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE,autotext.getAutoTextType( ));
		try{
			autotext.setAutoTextType( "autotexttype" );
			fail();
		}
		catch(Exception e)
		{
			assertNotNull(e);
		}
		
	}
	
	public void testStyleOnAutotext( ) throws Exception
	{
	     //add selector "autotext"
		  
//		 SharedStyleHandle selector = factory.newStyle( "autotext" );
//		 
//	     selector.setStringProperty( IStyleModel.BACKGROUND_COLOR_PROP, "yellow" );
//	     selector.setStringProperty( IStyleModel.FONT_SIZE_PROP, "12pt" );
//	     
//	     masterpage.getPageHeader( ).add( autotext );
//	     designHandle.getStyles( ).add( selector );
//	     
//	     assertEquals("yellow",autotext.getStringProperty( IStyleModel.BACKGROUND_COLOR_PROP ));
//	     assertEquals("12points",autotext.getStringProperty( IStyleModel.BACKGROUND_COLOR_PROP ));
//	     
	    //add custom style
	     
	     SharedStyleHandle style = factory.newStyle( "style" );
	     
	     style.setStringProperty( IStyleModel.BACKGROUND_COLOR_PROP, "red" );
	     style.setStringProperty( IStyleModel.FONT_SIZE_PROP, "small");
	     
	     designHandle.getStyles( ).add( style );
	     autotext.setStyle( style );
	     
	     assertEquals("red",autotext.getStringProperty( IStyleModel.BACKGROUND_COLOR_PROP ));
	     assertEquals("small",autotext.getStringProperty( IStyleModel.FONT_SIZE_PROP ));
	       
	}
	
	
}
