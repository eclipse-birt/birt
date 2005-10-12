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

package org.eclipse.birt.report.designer.ui.newelement;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LineHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.RectangleHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * This class is a heritor of ElementFactory,which is used to new elements
 */

public class DesignElementFactory extends ElementFactory
{

	ElementFactory factory = null;

	/**
	 * @return An instance of DesignElementFactory
	 */
	public static DesignElementFactory getInstance( )
	{
		return new DesignElementFactory( SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( ) );
	}

	public static DesignElementFactory getInstance( ModuleHandle module )
	{
		return new DesignElementFactory( module );
	}

	/**
	 * constructor
	 * 
	 * @param module
	 */
	protected DesignElementFactory( ModuleHandle module )
	{
		super( module.getModule() );
		factory = new ElementFactory( module.getModule() );
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by
	 * the meta-data system.
	 * 
	 * @param elementTypeName
	 *            the element type name
	 * @param name
	 *            the optional element name
	 * 
	 * @return design element, <code>null</code> returned if the element
	 *         definition name is not a valid element type name.
	 */

	private String getNewName( String elementTypeName, String name )
	{
		if ( name == null )
		{
			name = ReportPlugin.getDefault( ).getCustomName( elementTypeName );
		}
		return name;
	}

	public DesignElementHandle newElement( String elementTypeName, String name )
	{
		String newName = getNewName( elementTypeName, name );
		return factory.newElement( elementTypeName, newName );

	}

	/**
	 * Creates a new free-form item.
	 * 
	 * @param name
	 *            the optional free-form name. Can be <code>null</code>.
	 * @return a handle to the free-form
	 */

	public FreeFormHandle newFreeForm( String name )
	{
		String newName = getNewName( ReportDesignConstants.FREE_FORM_ITEM, name );
		return factory.newFreeForm( newName );
	}

	/**
	 * Creates a new data item.
	 * 
	 * @param name
	 *            the optional data item name. Can be <code>null</code>.
	 * @return a handle to the data item
	 */

	public DataItemHandle newDataItem( String name )
	{
		String newName = getNewName( ReportDesignConstants.DATA_ITEM, name );
		return factory.newDataItem( newName );
	}

	/**
	 * Creates a new label item.
	 * 
	 * @param name
	 *            the optional label name. Can be <code>null</code>.
	 * @return a handle to the label
	 */

	public LabelHandle newLabel( String name )
	{
		String newName = getNewName( ReportDesignConstants.LABEL_ITEM, name );
		return factory.newLabel( newName );
	}

	/**
	 * Creates a new image item.
	 * 
	 * @param name
	 *            the optional image name. Can be <code>null</code>.
	 * @return a handle to the image
	 */

	public ImageHandle newImage( String name )
	{
		String newName = getNewName( ReportDesignConstants.IMAGE_ITEM, name );
		return factory.newImage( newName );
	}

	/**
	 * Creates a new list item.
	 * 
	 * @param name
	 *            the optional list name. Can be <code>null</code>.
	 * @return a handle to the list
	 */

	public ListHandle newList( String name )
	{
		String newName = getNewName( ReportDesignConstants.LIST_ITEM, name );
		return factory.newList( newName );
	}

	/**
	 * Creates a new graphic master page element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the required graphic master page name
	 * @return a handle to the graphic master page
	 */

	public GraphicMasterPageHandle newGraphicMasterPage( String name )
	{
		String newName = getNewName( ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT,
				name );
		return factory.newGraphicMasterPage( newName );
	}

	/**
	 * Creates a new simple master page element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the requirement simple master page name
	 * @return a handle to the simple master page.
	 */

	public SimpleMasterPageHandle newSimpleMasterPage( String name )
	{
		String newName = getNewName( ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT,
				name );
		return factory.newSimpleMasterPage( newName );
	}

	/**
	 * Creates a new parameter group element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the optional parameter group name. Can be <code>null</code>.
	 * @return a handle to the parameter group
	 */

	public ParameterGroupHandle newParameterGroup( String name )
	{
		String newName = getNewName( ReportDesignConstants.PARAMETER_GROUP_ELEMENT,
				name );
		return factory.newParameterGroup( newName );
	}

	/**
	 * Creates a new scalar parameter element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the required scalar parameter name
	 * @return a handle to the scalar parameter
	 */

	public ScalarParameterHandle newScalarParameter( String name )
	{
		String newName = getNewName( ReportDesignConstants.SCALAR_PARAMETER_ELEMENT,
				name );
		return factory.newScalarParameter( newName );
	}

	/**
	 * Creates a new style element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the required style name
	 * @return a handle to the style
	 */

	public SharedStyleHandle newStyle( String name )
	{
		String newName = getNewName( ReportDesignConstants.STYLE_ELEMENT, name );
		return factory.newStyle( newName );
	}

	/**
	 * Creates a new text item.
	 * 
	 * @param name
	 *            the optional text item name. Can be <code>null</code>.
	 * @return a handle to the text item
	 */

	public TextItemHandle newTextItem( String name )
	{
		String newName = getNewName( ReportDesignConstants.TEXT_ITEM, name );
		return factory.newTextItem( newName );
	}

	/**
	 * Creates a new table item.
	 * 
	 * @param name
	 *            the optional table item name. Can be <code>null</code>.
	 * @return a handle to the table item
	 */

	public TableHandle newTableItem( String name )
	{
		String newName = getNewName( ReportDesignConstants.TABLE_ITEM, name );
		return factory.newTableItem( newName );
	}

	/**
	 * Creates a new table item with the given name and given column number. The
	 * table will have one row for each band(header, footer, detail). Each row
	 * in band will be filled with cells, number of cells is equal to the
	 * specified column number.
	 * <p>
	 * It has the same effect by calling:
	 * <code>newTableItem( name, columnNum, 1, 1, 1 )</code>.
	 * 
	 * @param name
	 *            the optional table item name
	 * @param columnNum
	 *            column number of the table, if it is less than 0, then column
	 *            won't be defined for the table at this stage.
	 * @return A handle to the table item.
	 * @see #newTableItem(String, int, int, int, int)
	 */

	public TableHandle newTableItem( String name, int columnNum )
	{
		String newName = getNewName( ReportDesignConstants.TABLE_ITEM, name );
		return factory.newTableItem( newName, columnNum );
	}

	/**
	 * Creates a new table item with the given name and given column number. The
	 * table will have given number of rows for each band(header, footer,
	 * detail). Each row in band will be filled with cells, number of cells is
	 * equal to the specified column number.
	 * 
	 * @param name
	 *            the optional table item name
	 * @param columnNum
	 *            column number of the table, if it is less than 0, then column
	 *            won't be defined for the table at this stage.
	 * @param headerRow
	 *            number of rows that will be added for header band. If it is
	 *            less than 0, none row will be added to header band.
	 * @param detailRow
	 *            number of rows that will be added for detail band. If it is
	 *            less than 0, none row will be added to detail band.
	 * @param footerRow
	 *            number of rows that will be added for footer band. If it is
	 *            less than 0, none row will be added to footer band.
	 * @return A handle to the table item.
	 */

	public TableHandle newTableItem( String name, int columnNum, int headerRow,
			int detailRow, int footerRow )
	{
		String newName = getNewName( ReportDesignConstants.TABLE_ITEM, name );
		return factory.newTableItem( newName,
				columnNum,
				headerRow,
				detailRow,
				footerRow );
	}

	/**
	 * Creates a new grid item.
	 * 
	 * @param name
	 *            the optional grid item name. Can be <code>null</code>.
	 * @return a handle to the grid item
	 */

	public GridHandle newGridItem( String name )
	{
		String newName = getNewName( ReportDesignConstants.GRID_ITEM, name );
		return factory.newGridItem( newName );
	}

	/**
	 * Creates a new grid item with pre-defined columns and rows.
	 * 
	 * @param name
	 *            the optional grid item name.
	 * @param columnNum
	 *            column number of the grid. If it is less than 0, none column
	 *            will be added.
	 * @param rowNum
	 *            row number of the grid. If it is less than 0, none row will be
	 *            added.
	 * 
	 * @return a handle to the grid item
	 */

	public GridHandle newGridItem( String name, int columnNum, int rowNum )
	{
		String newName = getNewName( ReportDesignConstants.GRID_ITEM, name );
		return factory.newGridItem( newName, columnNum, rowNum );
	}

	/**
	 * Creates a new line item.
	 * 
	 * @param name
	 *            the optional line item name. Can be <code>null</code>.
	 * @return a handle to the line item
	 */

	public LineHandle newLineItem( String name )
	{
		String newName = getNewName( ReportDesignConstants.LINE_ITEM, name );
		return factory.newLineItem( newName );
	}

	/**
	 * Creates a new rectangle.
	 * 
	 * @param name
	 *            the optional rectangle name. Can be <code>null</code>.
	 * @return a handle to rectangle
	 */

	public RectangleHandle newRectangle( String name )
	{
		String newName = getNewName( ReportDesignConstants.RECTANGLE_ITEM, name );
		return factory.newRectangle( newName );
	}

	/**
	 * Creates a new multi line data item.
	 * 
	 * @param name
	 *            the optional multi line data name. Can be <code>null</code>.
	 * @return a handle to multi line data item
	 */

	public TextDataHandle newTextData( String name )
	{
		String newName = getNewName( ReportDesignConstants.TEXT_ITEM, name );
		return factory.newTextData( newName );
	}

	/**
	 * Creates a new extended item.
	 * 
	 * @param name
	 *            the optional extended item name. Can be <code>null</code>.
	 * @param extensionName
	 *            the required extension name
	 * @return a handle to extended item, return <code>null</code> if the
	 *         definition with the given extension name is not found
	 */

	public ExtendedItemHandle newExtendedItem( String name, String extensionName )
	{
		String newName = getNewName( ReportDesignConstants.EXTENDED_ITEM, name );
		return factory.newExtendedItem( newName, extensionName );
	}

	/**
	 * Creates a new script data source.
	 * 
	 * @param name
	 *            the required script data source name.
	 * @return a handle to script data source
	 */

	public ScriptDataSourceHandle newScriptDataSource( String name )
	{
		String newName = getNewName( ReportDesignConstants.SCRIPT_DATA_SOURCE,
				name );
		return factory.newScriptDataSource( newName );
	}

	/**
	 * Creates a new script data set. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the required script data set name.
	 * @return a handle to script data set
	 */

	public ScriptDataSetHandle newScriptDataSet( String name )
	{
		String newName = getNewName( ReportDesignConstants.SCRIPT_DATA_SET,
				name );
		return factory.newScriptDataSet( newName );
	}

	/**
	 * Creates a new oda data source. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the required oda data source name.
	 * @return a handle to oda data source
	 * @deprecated see
	 *             <code>newOdaDataSource( String name, String extensionID )</code>
	 */

	public OdaDataSourceHandle newOdaDataSource( String name )
	{
		String newName = getNewName( ReportDesignConstants.ODA_DATA_SOURCE,
				name );
		return factory.newOdaDataSource( newName );
	}

	/**
	 * Creates a new oda data source. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.The
	 * <code>extensionID</code> is used to find the extension definition.If
	 * the extension ID is not given, the oda data source will be created
	 * without extension. If the unknown extension ID is given,
	 * <code>null</code> will be returned.
	 * 
	 * @param name
	 *            the required oda data source name.
	 * @param extensionID
	 *            the extension ID
	 * @return a handle to oda data source
	 */

	public OdaDataSourceHandle newOdaDataSource( String name, String extensionID )
	{
		String newName = getNewName( ReportDesignConstants.ODA_DATA_SOURCE,
				name );
		return factory.newOdaDataSource( newName, extensionID );
	}

	/**
	 * Creates a new oda data set. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the required oda data set name.
	 * @return a handle to oda data set
	 * @deprecated see
	 *             <code>newOdaDataSet( String name, String extensionID )</code>
	 */

	public OdaDataSetHandle newOdaDataSet( String name )
	{
		String newName = getNewName( ReportDesignConstants.ODA_DATA_SET, name );
		return factory.newOdaDataSet( newName );
	}

	/**
	 * Creates a new oda data set. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it. The
	 * <code>extensionID</code> is used to find the extension definition.If
	 * the extension ID is not given, the oda data source will be created
	 * without extension. If the unknown extension ID is given,
	 * <code>null</code> will be returned.
	 * 
	 * @param name
	 *            the required oda data set name.
	 * @param extensionID
	 *            the extension ID
	 * @return a handle to oda data set
	 */

	public OdaDataSetHandle newOdaDataSet( String name, String extensionID )
	{
		String newName = getNewName( ReportDesignConstants.ODA_DATA_SET, name );
		return factory.newOdaDataSet( newName, extensionID );
	}

	/**
	 * Creates one new element based on the given element. The new element will
	 * extends the given one. The element must be extendable.
	 * 
	 * @param element
	 *            the base element.
	 * @param name
	 *            the optional new element name
	 * @return the handle to the new element.
	 */

	public DesignElementHandle newElementFrom( DesignElementHandle element,
			String name )
	{
		String newName = getNewName( element.getElement( ).getElementName( ),
				name );
		return factory.newElementFrom( element, newName );
	}
}
