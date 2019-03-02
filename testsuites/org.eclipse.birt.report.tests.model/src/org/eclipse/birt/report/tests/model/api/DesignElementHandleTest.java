package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;


public class DesignElementHandleTest extends BaseTestCase
{
	final static String INPUT3 = "Improved_test3.xml";
	
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
		removeResource( );
		
		// retrieve two input files from tests-model.jar file
		copyResource_INPUT( INPUT3 , INPUT3 );
		
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

		assertFalse(nestedList.canContain( ListItem.GROUP_SLOT, listGroup ));
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
		listGroup.setName("Group4");
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
		openDesign( INPUT3 );
		SimpleMasterPageHandle mHandle = (SimpleMasterPageHandle) designHandle
				.findMasterPage( "Page1" ); //$NON-NLS-1$
		assertNotNull("should not be null", mHandle);
	    GridHandle grid = mHandle.getElementFactory().newGridItem("grid");
		assertFalse(mHandle.canContain(mHandle.getPageHeader().getSlotID(),grid));
		assertFalse(mHandle.canContain(mHandle.getPageFooter().getSlotID(),grid));
	}
	
	
}
