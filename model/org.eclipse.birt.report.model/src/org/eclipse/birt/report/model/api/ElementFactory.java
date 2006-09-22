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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.AutoText;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.LineItem;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.RectangleItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.elements.ScriptDataSource;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.oda.ODAProviderFactory;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PeerExtensionLoader;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.ElementStructureUtil;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Creates a new report elements and returns handles to it. Use this to create
 * elements. After creating an element, add it to the design using the
 * <code>add</code> method in the {@link SlotHandle}class. Obtain an instance
 * of this class by calling the <code>getElementFactory</code> method on any
 * element handle.
 * 
 * @see SlotHandle
 */

public class ElementFactory
{

	/**
	 * The module.
	 */

	protected final Module module;

	/**
	 * Constructs a element factory with the given module.
	 * 
	 * @param module
	 *            the module
	 */

	public ElementFactory( Module module )
	{
		this.module = module;
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

	public DesignElementHandle newElement( String elementTypeName, String name )
	{

		ElementDefn elemDefn = (ElementDefn) MetaDataDictionary.getInstance( )
				.getElement( elementTypeName );

		if ( elemDefn == null )
		{
			return newExtensionElement( elementTypeName, name );
		}

		DesignElement element = ModelUtil.newElement( module, elementTypeName,
				name );
		if ( element == null )
			return null;
		return element.getHandle( module );
	}

	/**
	 * Creates an extension element specified by the extension type name.
	 * 
	 * @param elementTypeName
	 *            the element type name
	 * @param name
	 *            the optional element name
	 * 
	 * @return design element, <code>null</code> returned if the extension
	 *         with the given type name is not found
	 */

	private DesignElementHandle newExtensionElement( String elementTypeName,
			String name )
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd
				.getExtension( elementTypeName );
		if ( extDefn == null )
			return null;
		String extensionPoint = extDefn.getExtensionPoint( );
		if ( PeerExtensionLoader.EXTENSION_POINT
				.equalsIgnoreCase( extensionPoint ) )
			return newExtendedItem( name, elementTypeName );

		return null;
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
		FreeForm element = new FreeForm( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		DataItem element = new DataItem( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		Label element = new Label( name );
		module.makeUniqueName( element );
		return element.handle( module );
	}

	/**
	 * Creates a new specialfield item.
	 * 
	 * @param name
	 *            the optional data item name. Can be <code>null</code>.
	 * @return a handle to the data item
	 */

	public AutoTextHandle newAutoText( String name )
	{
		AutoText element = new AutoText( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		ImageItem element = new ImageItem( name );
		module.makeUniqueName( element );
		return element.handle( module );
	}

	/**
	 * Creates a new list group element. List groups cannot have a name.
	 * 
	 * @return a handle to the list group
	 */

	public ListGroupHandle newListGroup( )
	{
		ListGroup element = new ListGroup( );
		module.makeUniqueName( element );
		return element.handle( module );
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
		ListItem element = new ListItem( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		GraphicMasterPage element = new GraphicMasterPage( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		SimpleMasterPage element = new SimpleMasterPage( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		ParameterGroup element = new ParameterGroup( name );
		module.makeUniqueName( element );
		return element.handle( module );
	}

	/**
	 * Creates a new parameter group element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the optional parameter group name. Can be <code>null</code>.
	 * @return a handle to the parameter group
	 */

	public CascadingParameterGroupHandle newCascadingParameterGroup( String name )
	{
		CascadingParameterGroup element = new CascadingParameterGroup( name );
		module.makeUniqueName( element );
		return (CascadingParameterGroupHandle) element.handle( module );
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
		ScalarParameter element = new ScalarParameter( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		Style element = new Style( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		TextItem element = new TextItem( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		TableItem element = new TableItem( name );
		module.makeUniqueName( element );
		element.refreshRenderModel( module );
		return element.handle( module );
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
		return newTableItem( name, columnNum, 1, 1, 1 );
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
		TableHandle tableHandle = newTableItem( name );
		TableItem table = (TableItem) tableHandle.getElement( );

		columnNum = columnNum >= 0 ? columnNum : 0;
		headerRow = headerRow >= 0 ? headerRow : 0;
		footerRow = footerRow >= 0 ? footerRow : 0;
		detailRow = detailRow >= 0 ? detailRow : 0;

		for ( int i = 0; i < columnNum; i++ )
		{
			TableColumn column = new TableColumn( );
			table.getSlot( TableItem.COLUMN_SLOT ).add( column );
			column.setContainer( table, TableItem.COLUMN_SLOT );
		}

		for ( int i = 0; i < headerRow; i++ )
		{
			TableRow row = (TableRow) newTableRow( columnNum ).getElement( );
			table.getSlot( TableItem.HEADER_SLOT ).add( row );
			row.setContainer( table, TableItem.HEADER_SLOT );
		}

		for ( int i = 0; i < footerRow; i++ )
		{
			TableRow row = (TableRow) newTableRow( columnNum ).getElement( );
			table.getSlot( TableItem.FOOTER_SLOT ).add( row );
			row.setContainer( table, TableItem.FOOTER_SLOT );
		}

		for ( int i = 0; i < detailRow; i++ )
		{
			TableRow row = (TableRow) newTableRow( columnNum ).getElement( );
			table.getSlot( TableItem.DETAIL_SLOT ).add( row );
			row.setContainer( table, TableItem.DETAIL_SLOT );
		}

		table.refreshRenderModel( module );
		return tableHandle;
	}

	/**
	 * Creates a new table group element. Table groups cannot have a name.
	 * 
	 * @return a handle to the table group
	 */

	public TableGroupHandle newTableGroup( )
	{
		TableGroup element = new TableGroup( );
		module.makeUniqueName( element );
		return element.handle( module );
	}

	/**
	 * Creates a new column element. Columns cannot have a name.
	 * 
	 * @return a handle to the column
	 */

	public ColumnHandle newTableColumn( )
	{
		TableColumn element = new TableColumn( );
		module.makeUniqueName( element );
		return element.handle( module );
	}

	/**
	 * Creates a new row element. Rows cannot have a name.
	 * 
	 * @return a handle to the row
	 */

	public RowHandle newTableRow( )
	{
		TableRow element = new TableRow( );
		module.makeUniqueName( element );
		return element.handle( module );
	}

	/**
	 * Creates a new table row, filled the row with the given number of cells.
	 * 
	 * @param cellNum
	 *            Number of cells to be added to the row.
	 * 
	 * @return a new table row.
	 */

	public RowHandle newTableRow( int cellNum )
	{
		RowHandle rowHandle = newTableRow( );
		TableRow row = (TableRow) rowHandle.getElement( );

		// fill the row with cells.

		for ( int j = 0; j < cellNum; j++ )
		{
			Cell cell = new Cell( );
			row.getSlot( TableRow.CONTENT_SLOT ).add( cell );
			cell.setContainer( row, TableRow.CONTENT_SLOT );
		}

		return rowHandle;
	}

	/**
	 * Creates a new cell element. Cells cannot have a name.
	 * 
	 * @return a handle to the cell
	 */

	public CellHandle newCell( )
	{
		Cell element = new Cell( );
		module.makeUniqueName( element );
		return element.handle( module );
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
		GridItem element = new GridItem( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		GridHandle gridHandle = newGridItem( name );
		GridItem grid = (GridItem) gridHandle.getElement( );

		columnNum = columnNum >= 0 ? columnNum : 0;
		rowNum = rowNum >= 0 ? rowNum : 0;

		for ( int i = 0; i < columnNum; i++ )
		{
			TableColumn column = new TableColumn( );
			grid.getSlot( GridItem.COLUMN_SLOT ).add( column );
			column.setContainer( grid, GridItem.COLUMN_SLOT );
		}

		for ( int i = 0; i < rowNum; i++ )
		{
			TableRow row = (TableRow) newTableRow( columnNum ).getElement( );
			grid.getSlot( GridItem.ROW_SLOT ).add( row );
			row.setContainer( grid, GridItem.ROW_SLOT );
		}

		return gridHandle;
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
		LineItem element = new LineItem( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		RectangleItem element = new RectangleItem( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		TextDataItem element = new TextDataItem( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		try
		{
			return newExtendedItem( name, extensionName, null );
		}
		catch ( ExtendsException e )
		{
			assert false;
			return null;
		}
	}

	/**
	 * Creates a new extended item which extends from a given parent.
	 * 
	 * @param name
	 *            the optional extended item name. Can be <code>null</code>.
	 * @param extensionName
	 *            the required extension name
	 * @param parent
	 *            a given parent element.
	 * @return a handle to extended item, return <code>null</code> if the
	 *         definition with the given extension name is not found
	 * @throws ExtendsException
	 */

	private ExtendedItemHandle newExtendedItem( String name,
			String extensionName, ExtendedItemHandle parent )
			throws ExtendsException
	{
		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd
				.getExtension( extensionName );
		if ( extDefn == null )
			return null;

		if ( parent != null )
			assert ( (ExtendedItem) parent.getElement( ) ).getExtDefn( ) == extDefn;

		if ( !( extDefn instanceof PeerExtensionElementDefn ) )
			throw new IllegalOperationException(
					"Only report item extension can be created through this method." ); //$NON-NLS-1$

		ExtendedItem element = new ExtendedItem( name );

		if ( parent != null )
		{
			element.getHandle( module ).setExtends( parent );
		}

		element.setProperty( ExtendedItem.EXTENSION_NAME_PROP, extensionName );
		module.makeUniqueName( element );
		ExtendedItemHandle handle = element.handle( module );
		try
		{
			handle.loadExtendedElement( );
		}
		catch ( ExtendedElementException e )
		{
			// It's impossible to fail when deserializing.

			assert false;
		}
		return handle;
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
		ScriptDataSource element = new ScriptDataSource( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		ScriptDataSet element = new ScriptDataSet( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		OdaDataSource element = new OdaDataSource( name );
		module.makeUniqueName( element );
		return element.handle( module );
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

		if ( extensionID != null )
		{
			if ( ODAProviderFactory.getInstance( ).createODAProvider( null,
					extensionID ) == null )
				return null;
			if ( !ODAProviderFactory.getInstance( ).createODAProvider( null,
					extensionID ).isValidODADataSourceExtensionID( extensionID ) )
				return null;
		}
		OdaDataSource element = new OdaDataSource( name );
		module.makeUniqueName( element );
		element.setProperty( OdaDataSource.EXTENSION_ID_PROP, extensionID );

		return element.handle( module );
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
		OdaDataSet element = new OdaDataSet( name );
		module.makeUniqueName( element );
		return element.handle( module );
	}

	/**
	 * Creates a new joint data set. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the required joint data set name.
	 * @return a handle to joint data set
	 */

	public JointDataSetHandle newJointDataSet( String name )
	{
		JointDataSet element = new JointDataSet( name );
		module.makeUniqueName( element );
		return element.handle( module );
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
		if ( extensionID != null )
		{
			if ( ODAProviderFactory.getInstance( ).createODAProvider( null,
					extensionID ) == null )
				return null;
			if ( !ODAProviderFactory.getInstance( ).createODAProvider( null,
					extensionID ).isValidODADataSetExtensionID( extensionID ) )
				return null;
		}
		OdaDataSet element = new OdaDataSet( name );
		module.makeUniqueName( element );
		element.setProperty( OdaDataSet.EXTENSION_ID_PROP, extensionID );

		return element.handle( module );
	}

	/**
	 * Creates one new element based on the given element. The new element will
	 * extends the given one. The element must be extendable.
	 * 
	 * @param baseElement
	 *            the base element.
	 * @param name
	 *            the optional new element name
	 * @return the handle to the new element.
	 * @throws ExtendsException
	 *             if the the base element is in a library that is not included
	 *             in this module, or if the "extends" relationship is illegal
	 */

	public DesignElementHandle newElementFrom( DesignElementHandle baseElement,
			String name ) throws ExtendsException
	{
		if ( baseElement == null )
			return null;

		// if the base element is in the module, just generate a child element

		if ( baseElement.getRoot( ).getElement( ) == module )
			return newElementFrom( name, baseElement );

		// the base element is not in the module, check whether the root module
		// of the base element is included

		Module root = (Module) baseElement.getRoot( ).getElement( );
		if ( root instanceof Library )
		{
			// the library with the location path is never included

			Library lib = module.getLibraryByLocation( root.getLocation( ) );
			if ( lib == null )
			{
				throw new InvalidParentException(
						null,
						baseElement.getElement( ),
						InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_INCLUDE );
			}

			DesignElement base = lib.getElementByID( baseElement.getID( ) );

			// if the element with the name is not found or the element type
			// is inconsistent, throw an exception

			if ( base == null
					|| base.getDefn( ) != baseElement.getElement( ).getDefn( ) )
			{
				throw new InvalidParentException(
						null,
						baseElement.getName( ),
						InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND );
			}

			return newElementFrom( name, base.getHandle( lib ) );
		}

		// if the root element is report design, return null

		return null;

	}

	/**
	 * Creates one new element based on the given element. The new element will
	 * extends the given one. The element must be extendable.
	 * 
	 * @param name
	 *            the optional new element name
	 * @param baseElement
	 *            the base element
	 * @return the handle to the new element.
	 * @throws ExtendsException
	 *             if the "extends" relationship is illegal
	 */

	private DesignElementHandle newElementFrom( String name,
			DesignElementHandle baseElement ) throws ExtendsException
	{
		DesignElementHandle childElement = null;

		if ( baseElement instanceof ExtendedItemHandle )
		{
			String extensionName = baseElement
					.getStringProperty( IExtendedItemModel.EXTENSION_NAME_PROP );
			childElement = newExtendedItem( name, extensionName,
					(ExtendedItemHandle) baseElement );
			ElementStructureUtil.refreshStructureFromParent( module,
					childElement.getElement( ) );
		}
		else
		{
			childElement = newElement( baseElement.getElement( )
					.getElementName( ), name );

			// for the special oda cases, the extension id must be set before
			// setExtends

			if ( childElement.getElement( ) instanceof IOdaExtendableElementModel )
			{
				String extensionId = (String) baseElement
						.getProperty( IOdaExtendableElementModel.EXTENSION_ID_PROP );

				childElement.getElement( ).setProperty(
						IOdaExtendableElementModel.EXTENSION_ID_PROP,
						extensionId );
			}
			childElement.setExtends( baseElement );
			ElementStructureUtil.refreshStructureFromParent( module,
					childElement.getElement( ) );
			renameForVirtualElements( childElement.getElement( ) );
		}

		return childElement;
	}

	/**
	 * Rename for the virtual elements inside the new element. *
	 * 
	 * @param element
	 *            the new created element.
	 */
	private void renameForVirtualElements( DesignElement element )
	{

		Iterator contentIter = new ContentIterator( element );
		while ( contentIter.hasNext( ) )
		{
			DesignElement virtualElement = (DesignElement) contentIter.next( );
			module.makeUniqueName( virtualElement );
		}
	}

	/**
	 * Creates a new theme element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 * 
	 * @param name
	 *            the theme item name.
	 * @return a handle to the theme item
	 */

	public ThemeHandle newTheme( String name )
	{
		Theme element = new Theme( name );
		module.makeUniqueName( element );
		return element.handle( module );
	}
}