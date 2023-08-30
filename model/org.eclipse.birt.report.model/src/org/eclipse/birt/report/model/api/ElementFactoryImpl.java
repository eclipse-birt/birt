/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.OdaCubeHandle;
import org.eclipse.birt.report.model.api.olap.OdaDimensionHandle;
import org.eclipse.birt.report.model.api.olap.OdaHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.OdaLevelHandle;
import org.eclipse.birt.report.model.api.olap.OdaMeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.OdaMeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.AbstractTheme;
import org.eclipse.birt.report.model.elements.AccessControl;
import org.eclipse.birt.report.model.elements.AutoText;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataGroup;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.DerivedDataSet;
import org.eclipse.birt.report.model.elements.DynamicFilterParameter;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.FilterConditionElement;
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
import org.eclipse.birt.report.model.elements.MemberValue;
import org.eclipse.birt.report.model.elements.MultiViews;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.RectangleItem;
import org.eclipse.birt.report.model.elements.ReportItemTheme;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.elements.ScriptDataSource;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.SortElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.ValueAccessControl;
import org.eclipse.birt.report.model.elements.VariableElement;
import org.eclipse.birt.report.model.elements.interfaces.IDerivedExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IGridItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.OdaCube;
import org.eclipse.birt.report.model.elements.olap.OdaDimension;
import org.eclipse.birt.report.model.elements.olap.OdaHierarchy;
import org.eclipse.birt.report.model.elements.olap.OdaLevel;
import org.eclipse.birt.report.model.elements.olap.OdaMeasure;
import org.eclipse.birt.report.model.elements.olap.OdaMeasureGroup;
import org.eclipse.birt.report.model.elements.olap.TabularCube;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.elements.olap.TabularHierarchy;
import org.eclipse.birt.report.model.elements.olap.TabularLevel;
import org.eclipse.birt.report.model.elements.olap.TabularMeasure;
import org.eclipse.birt.report.model.elements.olap.TabularMeasureGroup;
import org.eclipse.birt.report.model.extension.oda.ODAProviderFactory;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;
import org.eclipse.birt.report.model.util.ElementFactoryUtil;
import org.eclipse.birt.report.model.util.ElementStructureUtil;

/**
 * Creates a new report elements and returns handles to it. Use this to create
 * elements. After creating an element, add it to the design using the
 * <code>add</code> method in the {@link SlotHandle}class. Obtain an instance of
 * this class by calling the <code>getElementFactory</code> method on any
 * element handle.
 *
 * @see SlotHandle
 */

class ElementFactoryImpl {

	/**
	 * The module.
	 */

	protected final Module module;

	/**
	 * Constructs a element factory with the given module.
	 *
	 * @param module the module
	 */

	public ElementFactoryImpl(Module module) {
		this.module = module;
	}

	/**
	 * Creates a design element specified by the element type name. Element type
	 * names are defined in rom.def or extension elements. They are managed by the
	 * meta-data system.
	 *
	 * @param elementTypeName the element type name
	 * @param name            the optional element name
	 *
	 * @return design element, <code>null</code> returned if the element definition
	 *         name is not a valid element type name.
	 */

	public DesignElementHandle newElement(String elementTypeName, String name) {

		return ElementFactoryUtil.newElement(module, elementTypeName, name, true);
	}

	/**
	 * Creates a new free-form item.
	 *
	 * @param name the optional free-form name. Can be <code>null</code>.
	 * @return a handle to the free-form
	 */

	public FreeFormHandle newFreeForm(String name) {
		FreeForm element = new FreeForm(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new data item.
	 *
	 * @param name the optional data item name. Can be <code>null</code>.
	 * @return a handle to the data item
	 */

	public DataItemHandle newDataItem(String name) {
		DataItem element = new DataItem(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new label item.
	 *
	 * @param name the optional label name. Can be <code>null</code>.
	 * @return a handle to the label
	 */

	public LabelHandle newLabel(String name) {
		Label element = new Label(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new specialfield item.
	 *
	 * @param name the optional data item name. Can be <code>null</code>.
	 * @return a handle to the data item
	 */

	public AutoTextHandle newAutoText(String name) {
		AutoText element = new AutoText(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new image item.
	 *
	 * @param name the optional image name. Can be <code>null</code>.
	 * @return a handle to the image
	 */

	public ImageHandle newImage(String name) {
		ImageItem element = new ImageItem(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new list group element. List groups cannot have a name.
	 *
	 * @return a handle to the list group
	 */

	public ListGroupHandle newListGroup() {
		ListGroup element = new ListGroup();
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new list item.
	 *
	 * @param name the optional list name. Can be <code>null</code>.
	 * @return a handle to the list
	 */

	public ListHandle newList(String name) {
		ListItem element = new ListItem(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new graphic master page element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name the required graphic master page name
	 * @return a handle to the graphic master page
	 */

	public GraphicMasterPageHandle newGraphicMasterPage(String name) {
		GraphicMasterPage element = new GraphicMasterPage(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new simple master page element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name the requirement simple master page name
	 * @return a handle to the simple master page.
	 */

	public SimpleMasterPageHandle newSimpleMasterPage(String name) {
		SimpleMasterPage element = new SimpleMasterPage(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new parameter group element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name the optional parameter group name. Can be <code>null</code>.
	 * @return a handle to the parameter group
	 */

	public ParameterGroupHandle newParameterGroup(String name) {
		ParameterGroup element = new ParameterGroup(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new parameter group element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name the optional parameter group name. Can be <code>null</code>.
	 * @return a handle to the parameter group
	 */

	public CascadingParameterGroupHandle newCascadingParameterGroup(String name) {
		CascadingParameterGroup element = new CascadingParameterGroup(name);
		module.makeUniqueName(element);
		return (CascadingParameterGroupHandle) element.handle(module);
	}

	/**
	 * Creates a new scalar parameter element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name the required scalar parameter name
	 * @return a handle to the scalar parameter
	 */

	public ScalarParameterHandle newScalarParameter(String name) {
		ScalarParameter element = new ScalarParameter(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new dynamic filter parameter element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name the required dynamic filter parameter name
	 * @return a handle to the dynamic filter parameter
	 */
	public DynamicFilterParameterHandle newDynamicFilterParameter(String name) {
		DynamicFilterParameter element = new DynamicFilterParameter(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new style element. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.
	 *
	 * @param name the required style name
	 * @return a handle to the style
	 */

	public SharedStyleHandle newStyle(String name) {
		Style element = new Style(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new style element, and the style element is supposed to be adding
	 * to some theme in Library. The name is required. If the <code>name</code> is
	 * null, we will make a unique name for it.
	 *
	 * @param theme the theme to add the style
	 * @param name  the required style name
	 * @return a handle to the style
	 */

	public SharedStyleHandle newStyle(AbstractThemeHandle theme, String name) {
		Style element = new Style(name);
		((AbstractTheme) theme.getElement()).makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new text item.
	 *
	 * @param name the optional text item name. Can be <code>null</code>.
	 * @return a handle to the text item
	 */

	public TextItemHandle newTextItem(String name) {
		TextItem element = new TextItem(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new table item.
	 *
	 * @param name the optional table item name. Can be <code>null</code>.
	 * @return a handle to the table item
	 */

	public TableHandle newTableItem(String name) {
		TableItem element = new TableItem(name);
		module.makeUniqueName(element);
		element.refreshRenderModel(module);
		return element.handle(module);
	}

	/**
	 * Creates a new table item with the given name and given column number. The
	 * table will have one row for each band(header, footer, detail). Each row in
	 * band will be filled with cells, number of cells is equal to the specified
	 * column number.
	 * <p>
	 * It has the same effect by calling:
	 * <code>newTableItem( name, columnNum, 1, 1, 1 )</code>.
	 *
	 * @param name      the optional table item name
	 * @param columnNum column number of the table, if it is less than 0, then
	 *                  column won't be defined for the table at this stage.
	 * @return A handle to the table item.
	 * @see #newTableItem(String, int, int, int, int)
	 */

	public TableHandle newTableItem(String name, int columnNum) {
		return newTableItem(name, columnNum, 1, 1, 1);
	}

	/**
	 * Creates a new table item with the given name and given column number. The
	 * table will have given number of rows for each band(header, footer, detail).
	 * Each row in band will be filled with cells, number of cells is equal to the
	 * specified column number.
	 *
	 * @param name      the optional table item name
	 * @param columnNum column number of the table, if it is less than 0, then
	 *                  column won't be defined for the table at this stage.
	 * @param headerRow number of rows that will be added for header band. If it is
	 *                  less than 0, none row will be added to header band.
	 * @param detailRow number of rows that will be added for detail band. If it is
	 *                  less than 0, none row will be added to detail band.
	 * @param footerRow number of rows that will be added for footer band. If it is
	 *                  less than 0, none row will be added to footer band.
	 * @return A handle to the table item.
	 */

	public TableHandle newTableItem(String name, int columnNum, int headerRow, int detailRow, int footerRow) {
		TableHandle tableHandle = newTableItem(name);
		TableItem table = (TableItem) tableHandle.getElement();

		columnNum = columnNum >= 0 ? columnNum : 0;
		headerRow = headerRow >= 0 ? headerRow : 0;
		footerRow = footerRow >= 0 ? footerRow : 0;
		detailRow = detailRow >= 0 ? detailRow : 0;

		for (int i = 0; i < columnNum; i++) {
			TableColumn column = new TableColumn();
			table.add(column, ITableItemModel.COLUMN_SLOT);
		}

		for (int i = 0; i < headerRow; i++) {
			TableRow row = (TableRow) newTableRow(columnNum).getElement();
			table.add(row, IListingElementModel.HEADER_SLOT);
		}

		for (int i = 0; i < footerRow; i++) {
			TableRow row = (TableRow) newTableRow(columnNum).getElement();
			table.add(row, IListingElementModel.FOOTER_SLOT);
		}

		for (int i = 0; i < detailRow; i++) {
			TableRow row = (TableRow) newTableRow(columnNum).getElement();
			table.add(row, IListingElementModel.DETAIL_SLOT);
		}

		table.refreshRenderModel(module);
		return tableHandle;
	}

	/**
	 * Creates a new table group element. Table groups cannot have a name.
	 *
	 * @return a handle to the table group
	 */

	public TableGroupHandle newTableGroup() {
		TableGroup element = new TableGroup();
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new column element. Columns cannot have a name.
	 *
	 * @return a handle to the column
	 */

	public ColumnHandle newTableColumn() {
		TableColumn element = new TableColumn();
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new row element. Rows cannot have a name.
	 *
	 * @return a handle to the row
	 */

	public RowHandle newTableRow() {
		TableRow element = new TableRow();
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new table row, filled the row with the given number of cells.
	 *
	 * @param cellNum Number of cells to be added to the row.
	 *
	 * @return a new table row.
	 */

	public RowHandle newTableRow(int cellNum) {
		RowHandle rowHandle = newTableRow();
		TableRow row = (TableRow) rowHandle.getElement();

		// fill the row with cells.

		for (int j = 0; j < cellNum; j++) {
			Cell cell = new Cell();
			row.add(cell, ITableRowModel.CONTENT_SLOT);
		}

		return rowHandle;
	}

	/**
	 * Creates a new cell element. Cells cannot have a name.
	 *
	 * @return a handle to the cell
	 */

	public CellHandle newCell() {
		Cell element = new Cell();
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new grid item.
	 *
	 * @param name the optional grid item name. Can be <code>null</code>.
	 * @return a handle to the grid item
	 */

	public GridHandle newGridItem(String name) {
		GridItem element = new GridItem(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new grid item with pre-defined columns and rows.
	 *
	 * @param name      the optional grid item name.
	 * @param columnNum column number of the grid. If it is less than 0, none column
	 *                  will be added.
	 * @param rowNum    row number of the grid. If it is less than 0, none row will
	 *                  be added.
	 *
	 * @return a handle to the grid item
	 */

	public GridHandle newGridItem(String name, int columnNum, int rowNum) {
		GridHandle gridHandle = newGridItem(name);
		GridItem grid = (GridItem) gridHandle.getElement();

		columnNum = columnNum >= 0 ? columnNum : 0;
		rowNum = rowNum >= 0 ? rowNum : 0;

		for (int i = 0; i < columnNum; i++) {
			TableColumn column = new TableColumn();
			grid.add(column, IGridItemModel.COLUMN_SLOT);
		}

		for (int i = 0; i < rowNum; i++) {
			TableRow row = (TableRow) newTableRow(columnNum).getElement();
			grid.add(row, IGridItemModel.ROW_SLOT);
		}

		return gridHandle;
	}

	/**
	 * Creates a new line item.
	 *
	 * @param name the optional line item name. Can be <code>null</code>.
	 * @return a handle to the line item
	 */

	public LineHandle newLineItem(String name) {
		LineItem element = new LineItem(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new rectangle.
	 *
	 * @param name the optional rectangle name. Can be <code>null</code>.
	 * @return a handle to rectangle
	 */

	public RectangleHandle newRectangle(String name) {
		RectangleItem element = new RectangleItem(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new multi line data item.
	 *
	 * @param name the optional multi line data name. Can be <code>null</code>.
	 * @return a handle to multi line data item
	 */

	public TextDataHandle newTextData(String name) {
		TextDataItem element = new TextDataItem(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new extended item.
	 *
	 * @param name          the optional extended item name. Can be
	 *                      <code>null</code>.
	 * @param extensionName the required extension name
	 * @return a handle to extended item, return <code>null</code> if the definition
	 *         with the given extension name is not found
	 */

	public ExtendedItemHandle newExtendedItem(String name, String extensionName) {
		try {
			return newExtendedItem(name, extensionName, null);
		} catch (ExtendsException e) {
			assert false;
			return null;
		}
	}

	/**
	 * Creates a new extended item which extends from a given parent.
	 *
	 * @param name          the optional extended item name. Can be
	 *                      <code>null</code>.
	 * @param extensionName the required extension name
	 * @param parent        a given parent element.
	 * @return a handle to extended item, return <code>null</code> if the definition
	 *         with the given extension name is not found
	 * @throws ExtendsException
	 */

	private ExtendedItemHandle newExtendedItem(String name, String extensionName, ExtendedItemHandle parent)
			throws ExtendsException {
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		ExtensionElementDefn extDefn = (ExtensionElementDefn) dd.getExtension(extensionName);
		if (extDefn == null) {
			return null;
		}

		if (parent != null) {
			assert ((ExtendedItem) parent.getElement()).getExtDefn() == extDefn;
		}

		if (!(extDefn instanceof PeerExtensionElementDefn)) {
			throw new IllegalOperationException("Only report item extension can be created through this method."); //$NON-NLS-1$
		}

		ExtendedItem element = new ExtendedItem(name);

		// init provider.

		element.setProperty(IExtendedItemModel.EXTENSION_NAME_PROP, extensionName);

		if (parent != null) {
			element.getHandle(module).setExtends(parent);
		}

		module.makeUniqueName(element);
		ExtendedItemHandle handle = element.handle(module);
		try {
			handle.loadExtendedElement();
		} catch (ExtendedElementException e) {
			// It's impossible to fail when deserializing.

			assert false;
		}
		return handle;
	}

	/**
	 * Creates a new script data source.
	 *
	 * @param name the required script data source name.
	 * @return a handle to script data source
	 */

	public ScriptDataSourceHandle newScriptDataSource(String name) {
		ScriptDataSource element = new ScriptDataSource(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new script data set. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.
	 *
	 * @param name the required script data set name.
	 * @return a handle to script data set
	 */

	public ScriptDataSetHandle newScriptDataSet(String name) {
		ScriptDataSet element = new ScriptDataSet(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new oda data source. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.
	 *
	 * @param name the required oda data source name.
	 * @return a handle to oda data source
	 * @deprecated see
	 *             <code>newOdaDataSource( String name, String extensionID )</code>
	 */

	@Deprecated
	public OdaDataSourceHandle newOdaDataSource(String name) {
		OdaDataSource element = new OdaDataSource(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new oda data source. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.The <code>extensionID</code> is
	 * used to find the extension definition.If the extension ID is not given, the
	 * oda data source will be created without extension. If the unknown extension
	 * ID is given, <code>null</code> will be returned.
	 *
	 * @param name        the required oda data source name.
	 * @param extensionID the extension ID
	 * @return a handle to oda data source
	 */

	public OdaDataSourceHandle newOdaDataSource(String name, String extensionID) {
		OdaDataSource element = new OdaDataSource(name);
		if (extensionID != null) {
			if ((ODAProviderFactory.getInstance().createODAProvider(element, extensionID) == null) || !ODAProviderFactory.getInstance().createODAProvider(element, extensionID).isValidExtensionID()) {
				return null;
			}
		}

		module.makeUniqueName(element);
		element.setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionID);

		return element.handle(module);
	}

	/**
	 * Creates a new oda data set. The name is required. If the <code>name</code> is
	 * null, we will make a unique name for it.
	 *
	 * @param name the required oda data set name.
	 * @return a handle to oda data set
	 * @deprecated see <code>newOdaDataSet( String name, String extensionID )</code>
	 */

	@Deprecated
	public OdaDataSetHandle newOdaDataSet(String name) {
		OdaDataSet element = new OdaDataSet(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new joint data set. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.
	 *
	 * @param name the required joint data set name.
	 * @return a handle to joint data set
	 */

	public JointDataSetHandle newJointDataSet(String name) {
		JointDataSet element = new JointDataSet(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new oda data set. The name is required. If the <code>name</code> is
	 * null, we will make a unique name for it. The <code>extensionID</code> is used
	 * to find the extension definition.If the extension ID is not given, the oda
	 * data source will be created without extension. If the unknown extension ID is
	 * given, <code>null</code> will be returned.
	 *
	 * @param name        the required oda data set name.
	 * @param extensionID the extension ID
	 * @return a handle to oda data set
	 */

	public OdaDataSetHandle newOdaDataSet(String name, String extensionID) {
		OdaDataSet element = new OdaDataSet(name);
		if (extensionID != null) {
			if ((ODAProviderFactory.getInstance().createODAProvider(element, extensionID) == null) || !ODAProviderFactory.getInstance().createODAProvider(element, extensionID).isValidExtensionID()) {
				return null;
			}
		}

		module.makeUniqueName(element);
		element.setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionID);

		return element.handle(module);
	}

	/**
	 * Creates a new derived data set. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name        the required derived data set name.
	 * @param extensionID extension id
	 * @return a handle to derived data set
	 */

	public DerivedDataSetHandle newDerivedDataSet(String name, String extensionID) {
		DerivedDataSet element = new DerivedDataSet(name);
		module.makeUniqueName(element);
		element.setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionID);
		return element.handle(module);
	}

	/**
	 * Creates one new element based on the given element. The new element will
	 * extends the given one. The element must be extendable.
	 *
	 * @param baseElement the base element.
	 * @param name        the optional new element name
	 * @return the handle to the new element.
	 * @throws ExtendsException if the the base element is in a library that is not
	 *                          included in this module, or if the "extends"
	 *                          relationship is illegal
	 */

	public DesignElementHandle newElementFrom(DesignElementHandle baseElement, String name) throws ExtendsException {
		if (baseElement == null) {
			return null;
		}

		// if the base element is in the module, just generate a child element

		if (baseElement.getRoot().getElement() == module) {
			return createElementFrom(name, baseElement);
		}

		// the base element is not in the module, check whether the root module
		// of the base element is included

		Module root = (Module) baseElement.getRoot().getElement();
		if (root instanceof Library) {
			// the library with the location path is never included

			Library lib = module.getLibraryByLocation(root.getLocation());
			if (lib == null) {
				throw new InvalidParentException(null, baseElement.getElement(),
						InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_INCLUDE);
			}

			DesignElement base = lib.getElementByID(baseElement.getID());

			// if the element with the name is not found or the element type
			// is inconsistent, throw an exception

			if (base == null || base.getDefn() != baseElement.getElement().getDefn()) {
				throw new InvalidParentException(null, baseElement.getName(),
						InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND);
			}

			DesignElementHandle newHandle = createElementFrom(name, base.getHandle(lib));

			return newHandle;
		}

		// if the root element is report design, return null

		return null;

	}

	/**
	 * Creates one new element based on the given element. The new element will
	 * extends the given one. The element must be extendable.
	 *
	 * @param name        the optional new element name
	 * @param baseElement the base element
	 * @return the handle to the new element.
	 * @throws ExtendsException if the "extends" relationship is illegal
	 */

	protected DesignElementHandle createElementFrom(String name, DesignElementHandle baseElement)
			throws ExtendsException {
		DesignElementHandle childElement = null;

		if (baseElement instanceof ExtendedItemHandle) {
			String extensionName = baseElement.getStringProperty(IExtendedItemModel.EXTENSION_NAME_PROP);
			childElement = newExtendedItem(name, extensionName, (ExtendedItemHandle) baseElement);
			ElementStructureUtil.refreshStructureFromParent(module, childElement.getElement());
		} else {
			childElement = newElement(baseElement.getElement().getElementName(), name);

			// for the special oda cases, the extension id must be set before
			// setExtends

			String extensionProperty = null;
			if (childElement.getElement() instanceof IOdaExtendableElementModel) {
				extensionProperty = IOdaExtendableElementModel.EXTENSION_ID_PROP;

			} else if (childElement.getElement() instanceof IDerivedExtendableElementModel) {
				extensionProperty = IDerivedExtendableElementModel.EXTENSION_ID_PROP;
			}

			if (extensionProperty != null) {
				String extensionId = (String) baseElement.getProperty(extensionProperty);
				childElement.getElement().setProperty(extensionProperty, extensionId);
			}

			childElement.setExtends(baseElement);
			ElementStructureUtil.refreshStructureFromParent(module, childElement.getElement());
		}
		module.rename(childElement.getElement());

		// check extends
		childElement.getElement().checkExtends(baseElement.getElement());

		return childElement;
	}

	/**
	 * Creates a new theme element. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.
	 *
	 * @param name the theme item name.
	 * @return a handle to the theme item
	 */

	public ThemeHandle newTheme(String name) {
		Theme element = new Theme(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new report item theme element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name the report item theme name.
	 * @return a handle to the theme item
	 */

	public ReportItemThemeHandle newReportItemTheme(String name) {
		ReportItemTheme element = new ReportItemTheme(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new cube element. The name is required. If the <code>name</code> is
	 * null, we will make a unique name for it.
	 *
	 * @param name the cube element name.
	 * @return a handle to the cube element
	 */

	public TabularCubeHandle newTabularCube(String name) {
		TabularCube element = new TabularCube(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new dimension element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name the dimension name
	 * @return a handle to the dimension element
	 */

	public TabularDimensionHandle newTabularDimension(String name) {
		TabularDimension element = new TabularDimension(name);
		module.makeUniqueName(element);

		// add a hierarchy element to the dimension
		TabularHierarchy hierarchy = new TabularHierarchy();
		element.add(module, hierarchy, IDimensionModel.HIERARCHIES_PROP);
		module.makeUniqueName(hierarchy);
		// set default hierarchy
		element.setDefaultHierarchy(hierarchy);
		return element.handle(module);
	}

	/**
	 * Creates a new hierarchy element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name hierarchy name
	 * @return a handle to the hierarchy element
	 */

	public TabularHierarchyHandle newTabularHierarchy(String name) {
		TabularHierarchy element = new TabularHierarchy(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new level element. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.
	 *
	 * @param name the level name
	 * @return a handle to the level element
	 * @deprecated replaced by
	 *             {@link #newTabularLevel(org.eclipse.birt.report.model.api.olap.DimensionHandle, String)}
	 */

	@Deprecated
	public TabularLevelHandle newTabularLevel(String name) {
		TabularLevel element = new TabularLevel(name);
		module.makeUniqueName(element);
		return element.handle(module);

	}

	/**
	 * Creates a new level element within the given dimension handle. The name is
	 * required. If the <code>name</code> is null, we will make a unique name with
	 * the given dimension scope for it.
	 *
	 * @param dimensionHandle the dimension handle where the level will be inserted
	 *
	 * @param name            the level name
	 * @return a handle to the level element
	 */

	public TabularLevelHandle newTabularLevel(org.eclipse.birt.report.model.api.olap.DimensionHandle dimensionHandle,
			String name) {
		TabularLevel element = new TabularLevel(name);
		if (dimensionHandle != null) {
			((Dimension) dimensionHandle.getElement()).makeUniqueName(element);
		}
		return element.handle(module);
	}

	/**
	 * Creates a new measure element. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.
	 *
	 * @param name the measure name
	 * @return a handle to the measure element
	 */

	public TabularMeasureHandle newTabularMeasure(String name) {
		TabularMeasure element = new TabularMeasure(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new measure group.
	 *
	 * @param name the optional measure group name.
	 * @return the measure group element
	 */
	public TabularMeasureGroupHandle newTabularMeasureGroup(String name) {
		TabularMeasureGroup element = new TabularMeasureGroup(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates an access control element.
	 *
	 * @return the access control element
	 *
	 * @deprecated
	 */

	@Deprecated
	public AccessControlHandle newAccessControl() {
		AccessControl element = new AccessControl();
		return element.handle(module);
	}

	/**
	 * Creates a value access control element.
	 *
	 * @return the value access control element
	 *
	 * @deprecated
	 */

	@Deprecated
	public ValueAccessControlHandle newValueAccessControl() {
		AccessControl element = new ValueAccessControl();
		return (ValueAccessControlHandle) element.handle(module);
	}

	/**
	 * Creates a new cube element. The name is required. If the <code>name</code> is
	 * null, we will make a unique name for it.
	 *
	 * @param name the cube element name.
	 * @return a handle to the cube element
	 */

	public OdaCubeHandle newOdaCube(String name) {
		OdaCube element = new OdaCube(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new dimension element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name the dimension name
	 * @return a handle to the dimension element
	 */

	public OdaDimensionHandle newOdaDimension(String name) {
		// add a hierarchy element to the dimension
		OdaDimension element = new OdaDimension(name);
		module.makeUniqueName(element);
		OdaHierarchy hierarchy = new OdaHierarchy();
		element.add(module, hierarchy, IDimensionModel.HIERARCHIES_PROP);
		module.makeUniqueName(hierarchy);
		return element.handle(module);
	}

	/**
	 * Creates a new hierarchy element. The name is required. If the
	 * <code>name</code> is null, we will make a unique name for it.
	 *
	 * @param name hierarchy name
	 * @return a handle to the hierarchy element
	 */

	public OdaHierarchyHandle newOdaHierarchy(String name) {
		OdaHierarchy element = new OdaHierarchy(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new level element. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.
	 *
	 * @param name the level name
	 * @return a handle to the level element
	 * @deprecated replaced by
	 *             {@link #newOdaLevel(org.eclipse.birt.report.model.api.olap.DimensionHandle, String)}
	 */

	@Deprecated
	public OdaLevelHandle newOdaLevel(String name) {
		OdaLevel element = new OdaLevel(name);
		module.makeUniqueName(element);
		return element.handle(module);

	}

	/**
	 * Creates a new oda level handle. The name is required. If given name is null,
	 * we will make a unique name within the dimension scope for it.
	 *
	 * @param dimensionHandle the dimension handle where the level will be inserted
	 * @param name            the level name
	 * @return a handle to the level element
	 */
	public OdaLevelHandle newOdaLevel(org.eclipse.birt.report.model.api.olap.DimensionHandle dimensionHandle,
			String name) {
		OdaLevel element = new OdaLevel(name);
		if (dimensionHandle != null) {
			((Dimension) dimensionHandle.getElement()).makeUniqueName(element);
		}
		return element.handle(module);
	}

	/**
	 * Creates a new measure element. The name is required. If the <code>name</code>
	 * is null, we will make a unique name for it.
	 *
	 * @param name the measure name
	 * @return a handle to the measure element
	 */

	public OdaMeasureHandle newOdaMeasure(String name) {
		OdaMeasure element = new OdaMeasure(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a new measure group.
	 *
	 * @param name the optional measure group name.
	 * @return the measure group element
	 */
	public OdaMeasureGroupHandle newOdaMeasureGroup(String name) {
		OdaMeasureGroup element = new OdaMeasureGroup(name);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a member value handle.
	 *
	 * @return a handle to the member value
	 */
	public MemberValueHandle newMemberValue() {
		MemberValue element = new MemberValue();
		return element.handle(module);
	}

	/**
	 * Creates a sort element handle.
	 *
	 * @return a handle to the sort element
	 */
	public SortElementHandle newSortElement() {
		SortElement element = new SortElement();
		return element.handle(module);
	}

	/**
	 * Creates a filter condition element handle.
	 *
	 * @return a handle to the filter condition element
	 */
	public FilterConditionElementHandle newFilterConditionElement() {
		FilterConditionElement element = new FilterConditionElement();
		return element.handle(module);
	}

	/**
	 * Creates a multiple view element handle.
	 *
	 * @return a handle to the multiple view element
	 */

	public MultiViewsHandle newMultiView() {
		MultiViews element = new MultiViews();
		return (MultiViewsHandle) element.getHandle(module);
	}

	/**
	 * Creates a variable element.
	 *
	 * @return the variable element
	 * @deprecated replaced by newVariableElement( String variableName )
	 */

	@Deprecated
	public VariableElementHandle newVariableElement() {
		return newVariableElement(null);
	}

	/**
	 * Creates a variable element and set the variable name.
	 *
	 * @param variableName the variable name.
	 * @return the variable element handle.
	 */
	public VariableElementHandle newVariableElement(String variableName) {
		VariableElement element = new VariableElement(variableName);
		module.makeUniqueName(element);
		return element.handle(module);
	}

	/**
	 * Creates a data group element.
	 *
	 * @return the generated data group element
	 */
	public DataGroupHandle newDataGroup() {
		DataGroup element = new DataGroup();
		return element.handle(module);
	}

}
