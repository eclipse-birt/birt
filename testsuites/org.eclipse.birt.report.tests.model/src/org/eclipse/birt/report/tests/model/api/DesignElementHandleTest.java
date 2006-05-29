package org.eclipse.birt.report.tests.model.api;

import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;


public class DesignElementHandleTest extends BaseTestCase
{
	String fileName = "DesignElementHandle_GetXPath.xml";
	String fileName2 = "Improved_test3.xml";
	
	public DesignElementHandleTest(String name) 
	{	
		super(name);
	}
    public static Test suite()
    {
		
		return new TestSuite(DesignElementHandleTest.class);
	}
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}
	
	public void testGetXPath( ) throws Exception
	{
		openDesign(fileName);
	    
		TextItemHandle textHandle = (TextItemHandle)designHandle.findElement( "myText" );
		assertNotNull("Text should not be null", textHandle); 
		TableHandle tableHandle = (TableHandle)designHandle.findElement( "myTable" );
		assertNotNull("Table should not be null", tableHandle);
		StyleHandle styleHandle = (StyleHandle)designHandle.findStyle( "myStyle" );
		assertNotNull("Style should not be null", styleHandle);
		DesignElementHandle parameterHandle = designHandle.findElement( "myPara" );
		assertNotNull("Parameter should not be null", parameterHandle);
		LabelHandle labelHandle = (LabelHandle)designHandle.findElement( "myLabel" );
		assertNotNull("Label should not be null", labelHandle);
		ImageHandle imageHandle = (ImageHandle)designHandle.findElement( "myImage" );
		assertNotNull("Image should not be null", imageHandle);
		DataItemHandle dataHandle = (DataItemHandle)designHandle.findElement( "myData" );
		assertNotNull("Data should not be null", dataHandle); 
		MasterPageHandle masterHeader = designHandle.findMasterPage( "myMasterPage" );
		assertNotNull("MasterHeader should not be null", masterHeader);
		
		assertEquals( "/report/body[1]/text[1]", textHandle.getXPath( ) ); 
		assertEquals( "/report/body[1]/table[1]", tableHandle.getXPath( ) ); 
		assertEquals( "/report/styles[1]/style[1]", styleHandle.getXPath( ) ); 
		assertEquals( "/report/body[1]/data[2]", parameterHandle.getXPath( ) ); 
		assertEquals( "/report/body[1]/label[1]", labelHandle.getXPath( ) ); 
	    assertEquals( "/report/body[1]/image[1]", imageHandle.getXPath( ) ); 
		assertEquals( "/report/body[1]/data[1]", dataHandle.getXPath( ) ); 
		assertEquals( "/report/page-setup[1]/simple-master-page[1]", masterHeader.getXPath() );
	}
	
	public void testElementValidation( ) throws Exception
	{
		openDesign( "Improved_test2.xml" );

		TableHandle table = (TableHandle) designHandle
				.findElement( "MyTable" ); 
		assertNotNull("should not be null", table);
		table.setValid(false);
		boolean error = table.hasSemanticError( ) || !table.isValid( );
		assertTrue(error);
		assertTrue(table.showError());
	  }
	
	public void testcanContainTableHeader( ) throws SemanticException
	{

		sessionHandle = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = sessionHandle.createDesign( );
		design = (ReportDesign) designHandle.getModule();

		ElementFactory factory = new ElementFactory( design );

		assertTrue( designHandle.canContain( ReportDesign.DATA_SET_SLOT,
				factory.newOdaDataSet( null, null ) ) );
		assertFalse( designHandle.canContain( ReportDesign.BODY_SLOT, factory
				.newOdaDataSet( null, null ) ) );
		assertFalse( designHandle.canContain( ReportDesign.DATA_SET_SLOT,
				factory.newOdaDataSource( null, null ) ) );

		// normal cases in FreeForm

		FreeFormHandle form = factory.newFreeForm( null );
		designHandle.getBody( ).add( form );

		assertFalse( form.canContain( FreeForm.REPORT_ITEMS_SLOT, factory
				.newOdaDataSet( null, null ) ) );
		assertFalse( form.canContain( FreeForm.REPORT_ITEMS_SLOT, factory
				.newCell( ) ) );
		assertTrue( form.canContain( FreeForm.REPORT_ITEMS_SLOT, factory
				.newList( null ) ) );

		// test special values.

		assertFalse( form
				.canContain( FreeForm.REPORT_ITEMS_SLOT, (String) null ) );
		assertFalse( form.canContain( FreeForm.REPORT_ITEMS_SLOT,
				(DesignElementHandle) null ) );
		assertFalse( form.canContain( FreeForm.NO_SLOT,
				(DesignElementHandle) null ) );

		// table is nested in the table header slot.

		TableHandle table = factory.newTableItem( null, 1 );
		RowHandle row = (RowHandle) ( table.getHeader( ).get( 0 ) );

		// row element can contain cell element.

		assertTrue( row.canContain( TableRow.CONTENT_SLOT,
				ReportDesignConstants.CELL_ELEMENT ) );
		assertFalse( row.canContain( TableRow.NO_SLOT, factory.newCell( ) ) );

		// The row cannot be inserted into the table header

		row = factory.newTableRow( );
		CellHandle cell = factory.newCell( );
		cell.addElement( factory.newTableItem( null ), Cell.CONTENT_SLOT );
		row.addElement( cell, TableRow.CONTENT_SLOT );
		assertFalse( table.canContain( TableItem.HEADER_SLOT, row ) );

		// table-header cell element can not contain table item.
		
		row = (RowHandle) ( table.getHeader( ).get( 0 ) );
		cell = (CellHandle) ( row.getSlot( TableRow.CONTENT_SLOT ).get( 0 ) );

		assertFalse( cell.canContain( Cell.CONTENT_SLOT,
				ReportDesignConstants.TABLE_ITEM ) );
		assertFalse( cell.canContain( Cell.CONTENT_SLOT, factory
				.newTableItem( null ) ) );
		assertTrue( cell.canContain( Cell.CONTENT_SLOT, factory
				.newFreeForm( null ) ) );

		// table-header cell element can not contain list item.

		assertFalse( cell
				.canContain( Cell.CONTENT_SLOT, factory.newList( null ) ) );
		assertFalse( cell.canContain( Cell.CONTENT_SLOT,
				ReportDesignConstants.LIST_ITEM ) );

		// table-header cell element can not contain a free-form that contains a
		// table. However, it can contain a single free-form.

		form.addElement( factory.newTableItem( null ),
				FreeForm.REPORT_ITEMS_SLOT );
		assertFalse( cell.canContain( Cell.CONTENT_SLOT, form ) );
		assertTrue( cell.canContain( Cell.CONTENT_SLOT,
				ReportDesignConstants.FREE_FORM_ITEM ) );

		// add a free-from to table-header cell element. Then this freeform
		// cannot contain table items.

		form = factory.newFreeForm( null );
		cell.addElement( form, Cell.CONTENT_SLOT );
		assertFalse( form.canContain( FreeForm.REPORT_ITEMS_SLOT, factory
				.newTableItem( null ) ) );
		assertFalse( form.canContain( FreeForm.REPORT_ITEMS_SLOT,
				ReportDesignConstants.TABLE_ITEM ) );

		// table is allowed to be nested in the table footer slot.

		row = (RowHandle) ( table.getFooter( ).get( 0 ) );
		cell = (CellHandle) ( row.getSlot( TableRow.CONTENT_SLOT ).get( 0 ) );
		assertTrue( cell.canContain( Cell.CONTENT_SLOT,
				ReportDesignConstants.TABLE_ITEM ) );

		// table-footer cell element can contain a free-form that contains a
		// table. And, it can contain a single free-form.

		form = factory.newFreeForm( null );
		form.addElement( factory.newTableItem( null ),
				FreeForm.REPORT_ITEMS_SLOT );
		assertTrue( cell.canContain( Cell.CONTENT_SLOT, form ) );
		assertTrue( cell.canContain( Cell.CONTENT_SLOT,
				ReportDesignConstants.FREE_FORM_ITEM ) );

		// add a free-from to table-footer cell element. Then this free-form
		// can contain table items.

		form = factory.newFreeForm( null );
		cell.addElement( form, Cell.CONTENT_SLOT );
		assertTrue( form.canContain( FreeForm.REPORT_ITEMS_SLOT, factory
				.newTableItem( null ) ) );
		assertTrue( form.canContain( FreeForm.REPORT_ITEMS_SLOT,
				ReportDesignConstants.TABLE_ITEM ) );

	}

	/**
	 * Tests canContain() method for duplicate group names in the listing
	 * element.
	 * 
	 * @throws SemanticException
	 */

	public void testcanContainGroupName( ) throws SemanticException
	{
		sessionHandle = DesignEngine.newSession( ULocale.ENGLISH );
		designHandle = sessionHandle.createDesign( );
		design = (ReportDesign) designHandle.getModule();

		ElementFactory factory = new ElementFactory( design );

		TableHandle table = factory.newTableItem( "table", 1 ); //$NON-NLS-1$

		// test two table group or list groups with same names without datasets.

		TableGroupHandle tableGroup = factory.newTableGroup( );
		tableGroup.setName( "Group1" ); //$NON-NLS-1$

		assertTrue( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		table.getGroups( ).add( tableGroup );

		assertFalse( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );

		tableGroup = factory.newTableGroup( );
		tableGroup.setName( "Group2" ); //$NON-NLS-1$
		assertTrue( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );

		// establishes the relationship between data set and table.

		DataSourceHandle dataSource = factory.newOdaDataSource( null, null );
		DataSetHandle dataSet = factory.newOdaDataSet( null, null );
		dataSet.setDataSource( dataSource.getName( ) );
		table.setDataSet( dataSet );
		designHandle.getDataSources( ).add( dataSource );
		designHandle.getDataSets( ).add( dataSet );

		// for a group with different names. It is OK.

		assertTrue( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		table.getGroups( ).add( tableGroup );

		// for an existed group name, cannot be contained.

		assertFalse( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		tableGroup = factory.newTableGroup( );
		tableGroup.setName( "Group1" ); //$NON-NLS-1$

		assertFalse( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );

		// test cases for nested tables.

		// adds a nested table to the table

		TableHandle nestedTable = factory.newTableItem( "nested", 1 ); //$NON-NLS-1$
		RowHandle row = (RowHandle) ( table.getDetail( ).get( 0 ) );
		CellHandle cell = (CellHandle) ( row.getCells( ).get( 0 ) );
		cell.getContent( ).add( nestedTable );

		tableGroup = factory.newTableGroup( );
		tableGroup.setName( "Group3" ); //$NON-NLS-1$

		assertTrue( nestedTable.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		nestedTable.getGroups( ).add( tableGroup );

		// after add, cannot contain any more.

		assertFalse( nestedTable.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		//assertFalse( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );

		tableGroup = factory.newTableGroup( );
		tableGroup.setName( "Group1" ); //$NON-NLS-1$
		assertFalse( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );

		// focus on list now.

		ListHandle nestedList = factory.newList( null );
		cell.getContent( ).add( nestedList );

		// an existed group name. Forbidden.

		ListGroupHandle listGroup = factory.newListGroup( );
		listGroup.setName( "Group1" ); //$NON-NLS-1$

		assertTrue(nestedList.canContain( ListItem.GROUP_SLOT, listGroup ));
		assertFalse(table.canContain( TableItem.GROUP_SLOT, listGroup ));
	
		// creates a table with semantic error.

		tableGroup = factory.newTableGroup( );
		tableGroup.setName( "Group3" ); //$NON-NLS-1$
		try
		{
			nestedTable.getGroups( ).add( tableGroup );
			fail( );
		}
		catch( NameException e )
		{
			assertEquals(NameException.DESIGN_EXCEPTION_DUPLICATE,e.getErrorCode( ));
		}

		// trick tests for add a semantic error.
		tableGroup.setName( "TrickGroup" ); //$NON-NLS-1$
		nestedTable.getGroups( ).add( tableGroup );
		
		// listGroup with the name "Group4"

		assertTrue( nestedList.canContain( ListItem.GROUP_SLOT, listGroup ) );

		tableGroup = factory.newTableGroup( );
		tableGroup.setName( "Group4" ); //$NON-NLS-1$
		assertTrue( nestedTable.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		assertTrue( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );

		// groups without names

		tableGroup = factory.newTableGroup( );
		assertTrue( nestedTable.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		assertTrue( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		table.getGroups( ).add( tableGroup );

		tableGroup = factory.newTableGroup( );
		assertTrue( nestedTable.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		assertTrue( table.canContain( TableItem.GROUP_SLOT, tableGroup ) );
		table.getGroups( ).add( tableGroup );
	}
	
	public void testcanContainSimpleMasterPage( ) throws Exception
	{
		openDesign( fileName2 );
		SimpleMasterPageHandle mHandle = (SimpleMasterPageHandle) designHandle
				.findMasterPage( "Page1" ); //$NON-NLS-1$
		assertNotNull("should not be null", mHandle);
	    GridHandle grid = mHandle.getElementFactory().newGridItem("grid");
		assertFalse(mHandle.canContain(mHandle.getPageHeader().getSlotID(),grid));
		assertFalse(mHandle.canContain(mHandle.getPageFooter().getSlotID(),grid));
	}
}
