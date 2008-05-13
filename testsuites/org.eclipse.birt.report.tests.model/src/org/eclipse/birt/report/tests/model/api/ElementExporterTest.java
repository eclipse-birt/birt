package org.eclipse.birt.report.tests.model.api;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;


public class ElementExporterTest extends BaseTestCase
{
	private ReportDesignHandle designHandle;
	private LibraryHandle libaryHandle;

	/**
	 * @return
	 */
	public static Test suite( )
	{
		return new TestSuite( ElementExporterTest.class );

	}

	protected void setUp( ) throws Exception
	{
		designHandle=createDesign( ULocale.ENGLISH );
		libaryHandle=createLibrary( );
	}

	protected void tearDown( ) throws Exception
	{
	}

	public void testCanExport() throws SemanticException{
		
		//export autotext, simplemasterpage
		AutoTextHandle autotext=designHandle.getElementFactory( ).newAutoText( "autotext" );
		SimpleMasterPageHandle masterPage=(SimpleMasterPageHandle)designHandle.getElementFactory( ).newSimpleMasterPage( "mypage" );
		masterPage.getPageHeader( ).add( autotext );
		designHandle.getMasterPages( ).add( masterPage );
		assertTrue(ElementExportUtil.canExport( autotext, libraryHandle, false ));
		assertFalse(ElementExportUtil.canExport( masterPage, libraryHandle, false ));
	
		//export grid
		GridHandle grid=(GridHandle)designHandle.getElementFactory( ).newGridItem( "mygrid", 1, 1 );
		designHandle.getBody( ).add( grid );
		assertTrue(ElementExportUtil.canExport( grid, libraryHandle, false ));
		assertTrue(ElementExportUtil.canExport( grid, libraryHandle, true ));
		assertTrue(ElementExportUtil.canExport( grid ));
		
		//export table group, row, column, cell and filter
		TableHandle table=(TableHandle)designHandle.getElementFactory( ).newTableItem( "mytable");
		GroupHandle group=(GroupHandle)designHandle.getElementFactory( ).newTableGroup( );
		table.getGroups( ).add( group );
		designHandle.getBody( ).add( table );
		assertFalse(ElementExportUtil.canExport( group, libraryHandle, false ));
		assertFalse(ElementExportUtil.canExport( group ));
		
		RowHandle row=designHandle.getElementFactory( ).newTableRow( );
		CellHandle cell=designHandle.getElementFactory( ).newCell( );
		table.getHeader( ).add( row );
		row.getCells( ).add( cell );
		ColumnHandle column=designHandle.getElementFactory( ).newTableColumn( );
		table.getColumns( ).add( column );
		assertFalse(ElementExportUtil.canExport( row, libraryHandle, false ));
		assertFalse(ElementExportUtil.canExport( column, libraryHandle, false ));
		assertFalse(ElementExportUtil.canExport( cell, libraryHandle, false ));
		assertFalse(ElementExportUtil.canExport( row ));
		assertFalse(ElementExportUtil.canExport( column ));
		assertFalse(ElementExportUtil.canExport( cell ));

		FilterCondition filtercondition=StructureFactory.createFilterCond( );
		filtercondition.setExpr( "1" );
		table=(TableHandle)designHandle.findElement( "mytable" );
		PropertyHandle propHandle=table.getPropertyHandle( ListingElement.FILTER_PROP);
		propHandle.addItem( filtercondition );
		FilterConditionHandle fcHandle=(FilterConditionHandle)table.filtersIterator( ).next( );
		assertFalse(ElementExportUtil.canExport( fcHandle, libraryHandle, false ));
		assertFalse(ElementExportUtil.canExport( fcHandle ));

		//export action
		LabelHandle label=designHandle.getElementFactory( ).newLabel( "mylabel" );
		label.setAction( StructureFactory.createAction( ) );
		ActionHandle actionHandle=label.getActionHandle( );
		assertFalse(ElementExportUtil.canExport( actionHandle, libraryHandle, true ));
		assertFalse(ElementExportUtil.canExport( actionHandle ));

		//export config variable
		ConfigVariable cv=StructureFactory.createConfigVar( );
		cv.setName( "config1" );
		cv.setValue( "value" );
		designHandle.addConfigVariable( cv );
		PropertyHandle propertyHandle=designHandle.getPropertyHandle( ReportDesign.CONFIG_VARS_PROP );
		Iterator iter=propertyHandle.iterator( );
		ConfigVariableHandle cvHandle=(ConfigVariableHandle)iter.next( );
		assertTrue(ElementExportUtil.canExport( cvHandle, libraryHandle, true ));
		assertTrue(ElementExportUtil.canExport( cvHandle ));
		
		//export dimensionhandle
		DimensionHandle dimHandle=designHandle.getElementFactory( ).newTabularDimension( "mydim" );
		CubeHandle cubeHandle=designHandle.getElementFactory( ).newTabularCube( "mycube" );
		designHandle.getCubes( ).add( cubeHandle );
		assertTrue(ElementExportUtil.canExport( cubeHandle, libraryHandle, true ));
		assertTrue(ElementExportUtil.canExport( cubeHandle ));
	}
	
	public void testCanExport_invalid(){
		//NULL arguments
		DesignElementHandle handle=null;
		assertFalse(ElementExportUtil.canExport( handle, libraryHandle, false ));
		StructureHandle structure=null;
		assertFalse(ElementExportUtil.canExport( handle ));
		assertFalse(ElementExportUtil.canExport( structure ));
	}
}
