/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.property.AbstractDescriptionPropertyPage;
import org.eclipse.birt.report.designer.data.ui.util.ControlProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ResourceEditDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * Property page to edit script dataset column definition.
 *
 */
public class OutputColumnDefnPage extends AbstractDescriptionPropertyPage implements Listener {

	private boolean modelChanged = true;
	protected PropertyHandle rsColumns;
	protected PropertyHandle columnHints;
	protected ColumnHandles columnHandles;
	protected Map rsColumnMap = new HashMap();
	protected Map columnHintMap = new HashMap();

	protected OutputColumnTableViewer viewer;
	private static Logger logger = Logger.getLogger(OutputColumnDefnPage.class.getName());

	protected static IChoice[] dataTypes = DEUtil.getMetaDataDictionary()
			.getStructure(ComputedColumn.COMPUTED_COLUMN_STRUCT).getMember(ComputedColumn.DATA_TYPE_MEMBER)
			.getAllowedChoices().getChoices();

	private static String NAME = "name"; //$NON-NLS-1$
	private static String TYPE = "dataType"; //$NON-NLS-1$
	private static String ALIAS = "alias"; //$NON-NLS-1$
	private static String DISPLAY_NAME = "displayName"; //$NON-NLS-1$
	private static String HELP_TEXT = "helpText"; //$NON-NLS-1$

	protected ColumnDefn newDefn = null;

	private static String DEFAULT_MESSAGE = Messages.getString("dataset.editor.outputColumns");//$NON-NLS-1$
	private static String DEFAULT_COLUMN_NAME = "Column"; //$NON-NLS-1$

	protected String defaultDataTypeDisplayName;
	protected int defaultDataTypeIndex;
	protected String[] displayDataTypes;

	/**
	 *
	 */
	public OutputColumnDefnPage() {
		super();
		this.defaultDataTypeDisplayName = getTypeDisplayName(DesignChoiceConstants.PARAM_TYPE_STRING);
		this.defaultDataTypeIndex = getTypeIndex(DesignChoiceConstants.PARAM_TYPE_STRING);
		this.displayDataTypes = getDataTypeDisplayNames();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.
	 * AbstractDescriptionPropertyPage#createContents(org.eclipse.swt.widgets.
	 * Composite)
	 */
	@Override
	public Control createContents(Composite parent) {
		rsColumns = ((DataSetHandle) getContainer().getModel()).getPropertyHandle(DataSetHandle.RESULT_SET_HINTS_PROP);
		columnHints = ((DataSetHandle) getContainer().getModel()).getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP);
		createCachedMap();

		columnHandles = new ColumnHandles(rsColumns, columnHints);
		viewer = new OutputColumnTableViewer(parent);
		createTableColumns();

		setTableContentProvider();

		setTableLabelProvider();

		addListeners();

		viewer.getViewer().setInput(columnHandles);
		viewer.updateButtons();

		((DataSetHandle) getContainer().getModel()).addListener(this);
		return viewer.getControl();
	}

	protected void addListeners() {
		viewer.getAddButton().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				doNew();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

		});

		viewer.getEditButton().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				doEdit();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

		});
	}

	protected void doNew() {
		ColumnInputDialog inputDialog = new ColumnInputDialog(viewer.getControl().getShell(),
				Messages.getString("ResultSetColumnPage.inputDialog.newColumn.title"), //$NON-NLS-1$
				new ColumnDefn());
		if (inputDialog.open() == Window.OK) {
			ColumnDefn newColumn = inputDialog.getColumnDefn();
			try {
				addNewDefn(newColumn);
				viewer.getViewer().refresh();
				updateMessage();
			} catch (SemanticException e) {
				getContainer().setMessage(Messages.getString("OutputColumnPage.error.createNewColumn"), //$NON-NLS-1$
						IMessageProvider.ERROR);
				ExceptionHandler.handle(e);
			}
		}
		updateButtons();
	}

	protected void doEdit() {
		int index = viewer.getViewer().getTable().getSelectionIndex();
		if (index >= 0 && index < viewer.getViewer().getTable().getItemCount()) {
			ColumnDefn currentColumn = (ColumnDefn) viewer.getViewer().getTable().getItem(index).getData();
			String oldName = currentColumn.getColumnName();
			ColumnInputDialog inputDialog = new ColumnInputDialog(viewer.getControl().getShell(),
					Messages.getString("ResultSetColumnPage.inputDialog.editColumn.title"), //$NON-NLS-1$
					currentColumn);
			if (inputDialog.open() == Window.OK) {
				updateColumnDefMap(oldName, inputDialog.getColumnDefn());
				viewer.getViewer().refresh();
				updateMessage();
			}
		} else {
			getContainer().setMessage(Messages.getString("OutputColumnPage.error.invalidSelection"), //$NON-NLS-1$
					IMessageProvider.ERROR);
		}
		updateButtons();
	}

	/**
	 * Updates the buttons and menu items on this page are set
	 */
	protected void updateButtons() {
		viewer.getRemoveAllMenuItem().setEnabled(viewer.getViewer().getTable().getItemCount() > 0);
		if (viewer.getViewer().getTable().getSelectionCount() == 1) {
			viewer.getEditButton().setEnabled(true);
			viewer.getRemoveButton().setEnabled(true);
			viewer.getRemoveMenuItem().setEnabled(true);

			int index = viewer.getViewer().getTable().getSelectionIndex();
			viewer.getUpButton().setEnabled(index != 0);
			viewer.getDownButton().setEnabled(index != (viewer.getViewer().getTable().getItemCount() - 1));
		} else {
			viewer.getEditButton().setEnabled(false);
			viewer.getUpButton().setEnabled(false);
			viewer.getRemoveButton().setEnabled(false);
			viewer.getDownButton().setEnabled(false);
			viewer.getRemoveMenuItem().setEnabled(false);
		}
	}

	protected void setTableContentProvider() {
		viewer.getViewer().setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement == null || !(inputElement instanceof ColumnHandles)) {
					return new Object[0];
				}

				return ((ColumnHandles) inputElement).getColumnDefn().toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

		});
	}

	protected void setTableLabelProvider() {
		viewer.getViewer().setLabelProvider(new ITableLabelProvider() {

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				String value = null;
				ColumnDefn defn = null;
				if (element instanceof ColumnDefn) {
					defn = (ColumnDefn) element;
				} else {
					return ""; //$NON-NLS-1$
				}

				switch (columnIndex) {
				case 1: {
					value = defn.getColumnName();
					break;
				}
				case 2: {
					if (defn != newDefn) {
						value = getTypeDisplayName(defn.getDataType());
					}
					break;
				}
				case 3: {
					value = defn.getAlias();
					break;
				}
				case 4: {
					value = defn.getDisplayName();
					break;
				}
				case 5: {
					value = defn.getDisplayNameKey();
					break;
				}
				}

				if (value == null) {
					value = ""; //$NON-NLS-1$
				}
				return value;
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
		});
	}

	protected void createTableColumns() {
		TableColumn column = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		column.setText(" "); //$NON-NLS-1$
		column.setResizable(false);
		column.setWidth(19);

		column = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		column.setText(Messages.getString("dataset.editor.title.name")); //$NON-NLS-1$
		column.setWidth(100);

		column = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		column.setText(Messages.getString("dataset.editor.title.type")); //$NON-NLS-1$
		column.setWidth(100);

		column = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		column.setText(Messages.getString("dataset.editor.title.alias")); //$NON-NLS-1$
		column.setWidth(100);

		column = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		column.setText(Messages.getString("dataset.editor.title.displayName")); //$NON-NLS-1$
		column.setWidth(100);

		TableColumn displayNameKeyColumn = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		displayNameKeyColumn.setText(Messages.getString("dataset.editor.title.displayNameKey")); //$NON-NLS-1$
		displayNameKeyColumn.setWidth(100);
	}

	protected ColumnHintHandle addNewDefn(ColumnDefn defn) throws SemanticException {
		ColumnHintHandle column = null;
		String name = defn.getColumnName();
		if (rsColumnMap != null) {
			if (rsColumnMap.get(name) != null) {
				name = getUniqueColumnName();
				defn.setColumnName(name);
			}
			ResultSetColumnHandle rsHandle;
			if (rsColumns != null && columnHints != null) {
				rsColumns.addItem(defn.getResultSetColumn());
				column = (ColumnHintHandle) columnHints.addItem(defn.getColumnHint());

				rsColumnMap.put(name, defn.getResultSetColumn());
				columnHintMap.put(name, defn.getColumnHint());
			}
		}
		return column;
	}

	protected void updateColumnDefMap(String oldName, ColumnDefn column) {
		if (rsColumnMap != null && oldName != null && rsColumnMap.get(oldName) != null) {
			String newName = column.getColumnName();
			if (!oldName.equals(newName)) {
				rsColumnMap.remove(oldName);
				columnHintMap.remove(oldName);
			}
			rsColumnMap.put(newName, column.getResultSetColumn());
			columnHintMap.put(newName, column.getColumnHint());

		}
	}

	protected final int getTypeIndex(String dataTypeName) {
		for (int n = 0; n < dataTypes.length; n++) {
			if (dataTypes[n].getName().equals(dataTypeName)) {
				return n;
			}
		}

		return this.defaultDataTypeIndex;
	}

	protected final String getTypeString(int index) {
		if (index > -1 && index < dataTypes.length) {
			return dataTypes[index].getName();
		}

		return null;
	}

	protected final String getTypeDisplayName(String typeName) {
		for (int n = 0; n < dataTypes.length; n++) {
			if (dataTypes[n].getName().equals(typeName)) {
				return dataTypes[n].getDisplayName();
			}
		}

		return this.defaultDataTypeDisplayName;
	}

	private String[] getDataTypeDisplayNames() {
		String[] dataTypeDisplayNames = new String[dataTypes.length];
		for (int i = 0; i < dataTypes.length; i++) {
			dataTypeDisplayNames[i] = dataTypes[i].getDisplayName();
		}
		return dataTypeDisplayNames;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * pageActivated()
	 */
	@Override
	public void pageActivated() {
		getContainer().setMessage(DEFAULT_MESSAGE, IMessageProvider.NONE);
		if (modelChanged) {
			modelChanged = false;
			refreshColumns();
		}
	}

	/**
	 * Refreshes the column list adds any new column retrieved. This method doesn't
	 * clear unused column. It is the users responsibility to delete individual
	 * column through the UI.
	 */
	private void refreshColumns() {
		try {
			// Bugzilla#104185
			// It's caused by conflict position data. Position from model is
			// staring with 0 in initialization, but from dte is starting
			// with 1. In global perspective, starting with 1 is wider used, and
			// refreshPosition will reset position starting with 1 to model after page
			// activated
//			DataSetItemModel[] items = ( (DataSetEditorDialog) this.getContainer( ) ).getCurrentItemModel( false,
//					true );
//			if ( items != null )
//			{
//				for ( int i = 0; i < items.length; i++ )
//				{
//					DataSetItemModel dsItem = items[i];
//
//					ColumnDefn defn = null;
//					if ( dsItem.getPosition( ) > 0 )
//					{
//						defn = findColumnByPosition( dsItem.getPosition( ) );
//					}
//					else
//					{
//						defn = findColumnByName( dsItem.getName( ) );
//					}
//					if ( defn == null )
//					{
//						defn = new ColumnDefn( );
//						defn.setDataType( dsItem.getDataTypeName( ) );
//						defn.setDisplayName( dsItem.getDisplayName( ) );
//						defn.setHelpText( dsItem.getHelpText( ) );
//						defn.setAlias( dsItem.getAlias( ) );
//						defn.setColumnName( dsItem.getName( ) );
//
//						if ( defn.getColumnName( ) == null )
//						{
//							defn.setColumnName( getUniqueName( ) );
//						}
//						addNewDefn( defn );
//					}
//				}
//			}
			refreshPositions();
			viewer.getViewer().refresh();
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.Listener#elementChanged(org.eclipse.birt.
	 * report.model.api.DesignElementHandle,
	 * org.eclipse.birt.report.model.activity.NotificationEvent)
	 */
	@Override
	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		modelChanged = true;
	}

	/**
	 * Re-indexes the parameters starting at 1 from the 1st in the list.
	 */
	protected final void refreshPositions() {
		if (rsColumns == null) {
			return;
		}

		int position = 1;
		Iterator iter = rsColumns.iterator();
		if (iter != null && rsColumns.isLocal()) {
			while (iter.hasNext()) {
				ResultSetColumnHandle column = (ResultSetColumnHandle) iter.next();
				column.setPosition(position++);
			}
		}
	}

	protected String getUniqueColumnName() {
		String name = DEFAULT_COLUMN_NAME;
		int index = 0;
		while (!isUniqeColumnName(name)) {
			index++;
			name = DEFAULT_COLUMN_NAME + "_" + index;//$NON-NLS-1$
		}
		return name;
	}

	private boolean isUniqeColumnName(String name) {
		Iterator iter = columnHints.iterator();
		while (iter.hasNext()) {
			ColumnHintHandle hint = (ColumnHintHandle) iter.next();
			if (hint.getColumnName() != null && hint.getColumnName().equals(name)) {
				return false;
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage#
	 * performCancel()
	 */
	@Override
	public boolean performCancel() {
		disposeAll();
		return super.performCancel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage#
	 * performOk()
	 */
	@Override
	public boolean performOk() {
		if (!modelChanged) {
			disposeAll();
			return super.performOk();
		}
		if (isValid()) {
			refreshPositions();
			disposeAll();
			return super.performOk();
		} else {
			disposeAll();
			return false;
		}
	}

	/**
	 * Check the alias names whether is valid. The invalid situation may be the same
	 * name of alias or the same name between column name and alias name.
	 *
	 */
	private boolean isValid() {
		boolean validate = true;
		String newColumnNameOrAlias;

		if (columnHints == null) {
			columnHints = ((DataSetHandle) getContainer().getModel())
					.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP);
		}

		Iterator iterator1 = columnHints.iterator();
		for (int i = 0; iterator1.hasNext() && validate; i++) {
			ColumnHintHandle columnHint = (ColumnHintHandle) iterator1.next();

			newColumnNameOrAlias = columnHint.getAlias();
			Iterator iterator2 = columnHints.iterator();
			if (newColumnNameOrAlias != null && newColumnNameOrAlias.length() > 0) {
				for (int n = 0; iterator2.hasNext(); n++) {
					ColumnHintHandle columnHint2 = (ColumnHintHandle) iterator2.next();
					if (i == n) {
						continue;
					}

					if ((columnHint2.getColumnName() != null
							&& columnHint2.getColumnName().equals(newColumnNameOrAlias))
							|| (columnHint2.getAlias() != null
									&& columnHint2.getAlias().equals(newColumnNameOrAlias))) {
						validate = false;
						getContainer().setMessage(
								Messages.getFormattedString("dataset.editor.error.columnOrAliasNameAlreadyUsed", //$NON-NLS-1$
										new Object[] { newColumnNameOrAlias,
												n > i ? Integer.valueOf(i + 1) : Integer.valueOf(n + 1),
												n > i ? Integer.valueOf(n + 1) : Integer.valueOf(i + 1) }),
								IMessageProvider.ERROR);
						break;
					}
				}
			}
		}
		return validate;
	}

	protected void updateMessage() {
		if (isValid()) {
			getContainer().setMessage(Messages.getString("dataset.editor.outputColumns"), //$NON-NLS-1$
					IMessageProvider.NONE);
		}
	}

	private void disposeAll() {
		rsColumnMap = null;
		columnHintMap = null;
		((DataSetHandle) getContainer().getModel()).removeListener(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.
	 * AbstractDescriptionPropertyPage#getPageDescription()
	 */
	@Override
	public String getPageDescription() {
		return Messages.getString("OutputColumnDefnPage.description"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage#
	 * canLeave()
	 */
	@Override
	public boolean canLeave() {
		if (!this.modelChanged) {
			return true;
		}
		if (isValid()) {
			refreshPositions();
			return super.canLeave();
		} else {
			return false;
		}
	}

	private void refreshCachedMap() {
		columnHintMap.clear();
		rsColumnMap.clear();

		for (Iterator iterator = columnHints.iterator(); iterator.hasNext();) {
			ColumnHintHandle handle = (ColumnHintHandle) iterator.next();
			columnHintMap.put(handle.getColumnName(), handle.getStructure());
		}

		IStructure toDelete = null;
		if (rsColumns == null) {
			return;
		}

		for (Iterator iterator = rsColumns.iterator(); iterator.hasNext();) {
			ResultSetColumnHandle handle = (ResultSetColumnHandle) iterator.next();
			if (columnHintMap.get(handle.getColumnName()) == null) {
				toDelete = handle.getStructure();
				continue;
			}
			rsColumnMap.put(handle.getColumnName(), handle.getStructure());
		}

		if (toDelete != null) {
			try {
				rsColumns.removeItem(toDelete);
			} catch (PropertyValueException e) {
				ExceptionHandler.handle(e);
			}
		}

	}

	/**
	 *
	 *
	 */
	private void createCachedMap() {
		if (rsColumns == null) {
			return;
		}

		for (Iterator iterator = rsColumns.iterator(); iterator.hasNext();) {
			ResultSetColumnHandle handle = (ResultSetColumnHandle) iterator.next();
			rsColumnMap.put(handle.getColumnName(), handle.getStructure());
		}
		for (Iterator iterator = columnHints.iterator(); iterator.hasNext();) {
			ColumnHintHandle handle = (ColumnHintHandle) iterator.next();
			columnHintMap.put(handle.getColumnName(), handle.getStructure());
		}
	}

	/**
	 * A class that contain one ResultSetColumnHandle and one ColumnHintHandle.
	 *
	 * @author lzhu
	 *
	 */
	private static class ColumnHandles {
		private PropertyHandle rsColumnHandle;
		private PropertyHandle chHandle;
		private List colList = null;

		ColumnHandles(PropertyHandle rsch, PropertyHandle chh) {
			this.rsColumnHandle = rsch;
			this.chHandle = chh;
		}

		public List getColumnDefn() {
			colList = new ArrayList();
			Iterator rsIter = this.rsColumnHandle.iterator();
			Iterator hintIter = this.chHandle.iterator();
			while (rsIter.hasNext()) {
				colList.add(new ColumnDefn((ResultSetColumnHandle) rsIter.next(), (ColumnHintHandle) hintIter.next()));
			}
			return colList;
		}

		public int size() {
			if (colList == null) {
				getColumnDefn();
			}
			return this.colList.size();
		}
	}

	/**
	 * The class which serves as input data of one single table item in column
	 * definition table.
	 *
	 * @author lzhu
	 *
	 */
	protected static class ColumnDefn {

		private ResultSetColumnHandle rsColumnHandle;
		private ColumnHintHandle columnHintHandle;

		private ResultSetColumn rsColumn;
		private ColumnHint columnHint;

		public ColumnDefn() {
			rsColumn = new ResultSetColumn();
			columnHint = new ColumnHint();
			// default type is "string"
			this.setDataType(DesignChoiceConstants.PARAM_TYPE_STRING);
		}

		public ColumnDefn(ResultSetColumnHandle rsHandle, ColumnHintHandle colHintHandle) {
			this.rsColumnHandle = rsHandle;
			this.columnHintHandle = colHintHandle;
		}

		public ResultSetColumn getResultSetColumn() {
			if (this.rsColumnHandle != null) {
				return (ResultSetColumn) this.rsColumnHandle.getStructure();
			} else {
				return this.rsColumn;
			}
		}

		public ColumnHint getColumnHint() {
			if (this.columnHintHandle != null) {
				return (ColumnHint) this.columnHintHandle.getStructure();
			} else {
				return this.columnHint;
			}
		}

		public ActionHandle setAction(Action action) throws SemanticException {
			if (this.columnHintHandle != null) {
				return this.columnHintHandle.setAction(action);
			}

			return null;
		}

		public ActionHandle getActionHandle() {
			if (this.columnHintHandle != null) {
				return this.columnHintHandle.getActionHandle();
			}

			return null;
		}

		public String getColumnName() {
			if (this.rsColumnHandle != null) {
				return this.rsColumnHandle.getColumnName();
			} else if (this.rsColumn != null) {
				return this.rsColumn.getColumnName();
			}
			return null;
		}

		/**
		 * @param columnName The columnName to set.
		 */
		public void setColumnName(String columnName) {
			if (this.rsColumnHandle != null && this.columnHintHandle != null) {
				try {
					rsColumnHandle.setColumnName(columnName);
					columnHintHandle.setColumnName(columnName);
				} catch (SemanticException e) {
				}
			} else if (this.rsColumn != null && this.columnHint != null) {
				rsColumn.setColumnName(columnName);
				columnHint.setProperty(ColumnHint.COLUMN_NAME_MEMBER, columnName);
			}
		}

		/**
		 * Gets the analysis type
		 *
		 * @return
		 */
		public String getAnalysisType() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getAnalysis();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.ANALYSIS_MEMBER);
			}
		}

		/**
		 * Sets the analysis type
		 *
		 * @param analysis
		 * @throws SemanticException
		 */
		public void setAnalysis(String analysis) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setAnalysis(analysis);
			} else {
				columnHint.setProperty(ColumnHint.ANALYSIS_MEMBER, analysis);
			}
		}

		/**
		 * Gets the analysis column
		 *
		 * @return
		 */
		public String getAnalysisColumn() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getAnalysisColumn();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.ANALYSIS_COLUMN_MEMBER);
			}
		}

		/**
		 * Sets the analysis column
		 *
		 * @param analysisColumn
		 * @throws SemanticException
		 */
		public void setAnalysisColumn(String analysisColumn) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setAnalysisColumn(analysisColumn);
			} else {
				columnHint.setProperty(ColumnHint.ANALYSIS_COLUMN_MEMBER, analysisColumn);
			}
		}

		/**
		 * @return Returns the alias.
		 */
		public String getAlias() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getAlias();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.ALIAS_MEMBER);
			}
		}

		/**
		 * @param alias The alias to set.
		 */
		public void setAlias(String alias) {
			if (this.columnHintHandle != null) {
				columnHintHandle.setAlias(alias);
			} else {
				columnHint.setProperty(ColumnHint.ALIAS_MEMBER, alias);
			}
		}

		/**
		 * @return Returns the dataType.
		 */
		public String getDataType() {
			if (this.rsColumnHandle != null) {
				return this.rsColumnHandle.getDataType();
			} else {
				return this.rsColumn.getDataType();
			}
		}

		/**
		 * @param dataType The dataType to set.
		 */
		public void setDataType(String dataType) {
			try {
				if (rsColumnHandle != null) {
					rsColumnHandle.setDataType(dataType);
				} else {
					rsColumn.setDataType(dataType);
				}
			} catch (SemanticException e) {
				logger.log(Level.FINE, e.getMessage(), e);
			}
		}

		/**
		 * @return Returns the displayName.
		 */
		public String getDisplayName() {
			if (this.columnHintHandle != null) {
				return this.columnHintHandle.getDisplayName();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.DISPLAY_NAME_MEMBER);
			}
		}

		/**
		 * @param displayName The displayName to set.
		 */
		public void setDisplayName(String displayName) {
			if (this.columnHintHandle != null) {
				columnHintHandle.setDisplayName(displayName);
			} else if (displayName != null && displayName.trim().length() > 0) {
				columnHint.setProperty(ColumnHint.DISPLAY_NAME_MEMBER, displayName);
			}
		}

		public String getDisplayNameKey() {
			if (this.columnHintHandle != null) {
				return this.columnHintHandle.getDisplayNameKey();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.DISPLAY_NAME_ID_MEMBER);
			}
		}

		/**
		 *
		 * @param displayName
		 */
		public void setDisplayNameKey(String displayNameKey) {
			if (this.columnHintHandle != null) {
				columnHintHandle.setDisplayNameKey(displayNameKey);
			} else {
				columnHint.setProperty(ColumnHint.DISPLAY_NAME_ID_MEMBER, displayNameKey);
			}
		}

		/**
		 * @return Returns the helpText.
		 */
		public String getHelpText() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getHelpText();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.HELP_TEXT_MEMBER);
			}
		}

		/**
		 * @param format The format to set.
		 */
		public void setFormat(String format) {
			if (this.columnHintHandle != null) {
				columnHintHandle.setFormat(format);
			} else {
				columnHint.setProperty(ColumnHint.FORMAT_MEMBER, format);
			}
		}

		/**
		 * @return Returns the format.
		 */
		public String getFormat() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getFormat();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.FORMAT_MEMBER);
			}
		}

		/**
		 * @param display length The display length to set.
		 */
		public void setDisplayLength(int length) {
			if (this.columnHintHandle != null) {
				columnHintHandle.setDisplayLength(length);
			} else {
				columnHint.setProperty(ColumnHint.DISPLAY_LENGTH_MEMBER, length);
			}
		}

		/**
		 * @return Returns the display length.
		 */
		public int getDisplayLength() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getDisplayLength();
			} else {
				Object value = columnHint.getProperty(null, ColumnHint.DISPLAY_LENGTH_MEMBER);
				if (value instanceof Integer) {
					return (Integer) value;
				}
				return 0;
			}
		}

		/**
		 * @param heading The heading to set.
		 */
		public void setHeading(String heading) {
			if (this.columnHintHandle != null) {
				columnHintHandle.setHeading(heading);
			} else {
				columnHint.setProperty(ColumnHint.HEADING_MEMBER, heading);
			}
		}

		/**
		 * @return Returns the heading.
		 */
		public String getHeading() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getHeading();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.HEADING_MEMBER);
			}
		}

		/**
		 * @param horizontalAlign The horizontal alignment to set.
		 * @throws SemanticException
		 */
		public void setHorizontalAlign(String horizontalAlign) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setHorizontalAlign(horizontalAlign);
			} else {
				columnHint.setProperty(ColumnHint.HORIZONTAL_ALIGN_MEMBER, horizontalAlign);
			}
		}

		/**
		 * @return Returns the horizontal alignment.
		 */
		public String getHorizontalAlign() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getHorizontalAlign();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.HORIZONTAL_ALIGN_MEMBER);
			}
		}

		/**
		 * @param textFormat The text format to set.
		 * @throws SemanticException
		 */
		public void setTextFormat(String textFormat) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setTextFormat(textFormat);
			} else {
				columnHint.setProperty(ColumnHint.TEXT_FORMAT_MEMBER, textFormat);
			}
		}

		/**
		 * @return Returns the text format.
		 */
		public String getTextFormat() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getTextFormat();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.TEXT_FORMAT_MEMBER);
			}
		}

		/**
		 *
		 *
		 * @return
		 */
		public FormatValue getFormatValue() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getValueFormat();
			} else {
				return (FormatValue) columnHint.getProperty(null, ColumnHint.VALUE_FORMAT_MEMBER);
			}
		}

		/**
		 *
		 *
		 * @param format
		 * @throws SemanticException
		 */
		public void setFormatValue(FormatValue format) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setValueFormat(format);
			} else {
				columnHint.setProperty(ColumnHint.VALUE_FORMAT_MEMBER, format);
			}
		}

		/**
		 * @param description The description to set.
		 * @throws SemanticException
		 */
		public void setDescription(String description) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setDescription(description);
			} else {
				columnHint.setProperty(ColumnHint.DESCRIPTION_MEMBER, description);
			}
		}

		/**
		 * @return Returns the description.
		 */
		public String getDescription() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getDescription();
			} else {
				return (String) columnHint.getProperty(null, ColumnHint.DESCRIPTION_MEMBER);
			}
		}

		/**
		 * @param wordWrap The boolean value of word wrap to set.
		 * @throws SemanticException
		 */
		public void setWordWrap(boolean wordWrap) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setWordWrap(wordWrap);
			} else {
				columnHint.setProperty(ColumnHint.WORD_WRAP_MEMBER, wordWrap);
			}
		}

		/**
		 * @return Returns the boolean value of word wrap.
		 */
		public boolean isWordWrap() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.wordWrap();
			} else {
				Object value = columnHint.getProperty(null, ColumnHint.WORD_WRAP_MEMBER);

				if (value instanceof Boolean) {
					return (Boolean) value;
				}

				return false;
			}
		}

		/**
		 * @param helpText The helpText to set.
		 */
		public void setHelpText(String helpText) {
			if (this.columnHintHandle != null) {
				columnHintHandle.setHelpText(helpText);
			} else {
				columnHint.setProperty(ColumnHint.HELP_TEXT_MEMBER, helpText);
			}
		}

		public Object getACLExpression() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.getACLExpression();
			} else {
				return columnHint.getExpressionProperty(ColumnHint.ACL_EXPRESSION_MEMBER);
			}

		}

		public void setACLExpression(Expression expr) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setExpressionProperty(ColumnHint.ACL_EXPRESSION_MEMBER, expr);
			} else {
				columnHint.setProperty(ColumnHint.ACL_EXPRESSION_MEMBER, expr);
			}
		}

		public boolean isIndexColumn() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.isIndexColumn();
			} else {
				if (columnHint.getProperty(null, ColumnHint.INDEX_COLUMN_MEMBER) instanceof Boolean) {
					return (Boolean) columnHint.getProperty(null, ColumnHint.INDEX_COLUMN_MEMBER);
				}
				return false;
			}
		}

		public void setIndexColumn(boolean indexColumn) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setIndexColumn(indexColumn);
			} else {
				columnHint.setProperty(ColumnHint.INDEX_COLUMN_MEMBER, indexColumn);
			}
		}

		public boolean removeDuplicatedValues() {
			if (this.columnHintHandle != null) {
				return columnHintHandle.isCompressed();
			} else {
				if (columnHint.getProperty(null, ColumnHint.COMPRESSED_MEMBER) instanceof Boolean) {
					return (Boolean) columnHint.getProperty(null, ColumnHint.COMPRESSED_MEMBER);
				}
				return false;
			}
		}

		public void setRemoveDuplicatedValues(boolean shouldRemoveDuplicatedColumn) throws SemanticException {
			if (this.columnHintHandle != null) {
				columnHintHandle.setCompresssed(shouldRemoveDuplicatedColumn);
			} else {
				columnHint.setProperty(ColumnHint.COMPRESSED_MEMBER, shouldRemoveDuplicatedColumn);
			}
		}

		public void setProperty(Object property, Object value) {
			if (property.equals(NAME)) {
				setColumnName((String) value);
			} else if (property.equals(TYPE)) {
				setDataType((String) value);
			} else if (property.equals(ALIAS)) {
				setAlias((String) value);
			} else if (property.equals(DISPLAY_NAME)) {
				setDisplayName((String) value);
			} else if (property.equals(HELP_TEXT)) {
				setHelpText((String) value);
			}
		}

		public Object getProperty(Object property) {
			if (property.equals(NAME)) {
				return getColumnName();
			} else if (property.equals(TYPE)) {
				return getDataType();
			} else if (property.equals(ALIAS)) {
				return getAlias();
			} else if (property.equals(DISPLAY_NAME)) {
				return getDisplayName();
			} else if (property.equals(HELP_TEXT)) {
				return getHelpText();
			}
			return null;
		}

	}

	protected class OutputColumnTableViewer {
		private TableViewer viewer;
		private Composite mainControl;
		private Button btnRemove;
		private Button btnUp;
		private Button btnDown;
		private Button btnAdd;
		private Button btnEdit;
		private MenuItem itmRemove;
		private MenuItem itmRemoveAll;
		private Menu menu;

		public OutputColumnTableViewer(Composite parent) {
			mainControl = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			mainControl.setLayout(layout);

			GridData data;
			viewer = new TableViewer(mainControl, SWT.FULL_SELECTION | SWT.BORDER);
			data = new GridData(GridData.FILL_BOTH);
			viewer.getControl().setLayoutData(data);

			viewer.getTable().setHeaderVisible(true);
			viewer.getTable().setLinesVisible(true);
			viewer.getTable().addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					updateButtons();
				}
			});
			viewer.getTable().addMouseListener(new MouseAdapter() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					if (viewer.getTable().getSelectionCount() == 1) {
						doEdit();
					}
				}
			});

			Composite btnComposite = new Composite(mainControl, SWT.NONE);
			data = new GridData();
			data.verticalAlignment = SWT.CENTER;
			btnComposite.setLayoutData(data);
			GridLayout btnLayout = new GridLayout();
			layout.verticalSpacing = 20;
			btnComposite.setLayout(btnLayout);

			btnAdd = new Button(btnComposite, SWT.NONE);
			btnAdd.setText(Messages.getString("ResultSetColumnPage.button.add")); //$NON-NLS-1$
			btnAdd.setEnabled(true);

			btnEdit = new Button(btnComposite, SWT.NONE);
			btnEdit.setText(Messages.getString("ResultSetColumnPage.button.edit")); //$NON-NLS-1$

			btnRemove = new Button(btnComposite, SWT.NONE);
			btnRemove.setText(Messages.getString("ResultSetColumnPage.button.delete")); //$NON-NLS-1$
			btnRemove.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					removeSelectedItem();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

			});

			btnUp = new Button(btnComposite, SWT.NONE);
			btnUp.setText(Messages.getString("ResultSetColumnPage.button.up")); //$NON-NLS-1$
			btnUp.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					doMoveUp();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

			});

			btnDown = new Button(btnComposite, SWT.NONE);
			btnDown.setText(Messages.getString("ResultSetColumnPage.button.down")); //$NON-NLS-1$
			btnDown.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					doMoveDown();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}

			});

			int width = getMaxWidth(btnAdd, 55);
			width = getMaxWidth(btnEdit, width);
			width = getMaxWidth(btnRemove, width);
			width = getMaxWidth(btnUp, width);
			width = getMaxWidth(btnDown, width);

			GridData btnData = new GridData(GridData.CENTER);
			btnData.widthHint = width;

			btnAdd.setLayoutData(btnData);
			btnEdit.setLayoutData(btnData);
			btnRemove.setLayoutData(btnData);
			btnUp.setLayoutData(btnData);
			btnDown.setLayoutData(btnData);

			menu = new Menu(viewer.getTable());
			menu.addMenuListener(new MenuAdapter() {

				@Override
				public void menuShown(MenuEvent e) {
					viewer.cancelEditing();
				}
			});
			itmRemove = new MenuItem(menu, SWT.NONE);
			itmRemove.setText(Messages.getString("PropertyHandleTableViewer.Menu.Remove")); //$NON-NLS-1$
			itmRemove.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					removeSelectedItem();
				}

			});
			itmRemoveAll = new MenuItem(menu, SWT.NONE);
			itmRemoveAll.setText(Messages.getString("PropertyHandleTableViewer.Menu.RemoveAll")); //$NON-NLS-1$
			itmRemoveAll.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					doRemoveAll();
				}
			});

			viewer.getTable().setMenu(menu);

			viewer.getTable().addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
					viewer.getTable();
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (e.keyCode == SWT.DEL) {
						removeSelectedItem();
					}
				}

			});
		}

		private int getMaxWidth(Control control, int size) {
			int width = control.computeSize(-1, -1).x;
			return width > size ? width : size;
		}

		/**
		 * Updates the buttons and menu items on this page are set
		 */
		private void updateButtons() {
			this.itmRemoveAll.setEnabled(viewer.getTable().getItemCount() > 0);
			if (viewer.getTable().getSelectionCount() == 1) {
				this.btnEdit.setEnabled(true);
				this.btnRemove.setEnabled(true);
				this.itmRemove.setEnabled(true);

				int index = viewer.getTable().getSelectionIndex();
				this.btnUp.setEnabled(index != 0);
				this.btnDown.setEnabled(index != (viewer.getTable().getItemCount() - 1));
			} else {
				this.btnEdit.setEnabled(false);
				this.btnUp.setEnabled(false);
				this.btnRemove.setEnabled(false);
				this.btnDown.setEnabled(false);
				this.itmRemove.setEnabled(false);
			}
		}

		public TableViewer getViewer() {
			return viewer;
		}

		public Composite getControl() {
			return mainControl;
		}

		public Button getAddButton() {
			return this.btnAdd;
		}

		public Button getEditButton() {
			return this.btnEdit;
		}

		public Button getDownButton() {
			return this.btnDown;
		}

		public Button getUpButton() {
			return this.btnUp;
		}

		public Button getRemoveButton() {
			return this.btnRemove;
		}

		public MenuItem getRemoveMenuItem() {
			return this.itmRemove;
		}

		public MenuItem getRemoveAllMenuItem() {
			return this.itmRemoveAll;
		}

		private final void removeSelectedItem() {
			int index = viewer.getTable().getSelectionIndex();
			// Do not allow deletion of the last item.
			if (index > -1 && index < columnHandles.size()) {
				try {
					if (rsColumns != null) {
						rsColumns.removeItem(index);
					}
					columnHints.removeItem(index);
				} catch (Exception e1) {
					ExceptionHandler.handle(e1);
				}
				viewer.refresh();
				viewer.getTable().select(index);
				refreshCachedMap();
			}
			updateButtons();
		}

		private void doMoveUp() {
			// Get the current selection and delete that row
			int index = viewer.getTable().getSelectionIndex();
			if (index - 1 >= 0 && index < columnHandles.size()) {
				viewer.cancelEditing();
				try {
					if (rsColumns != null) {
						rsColumns.moveItem(index, index - 1);
					}
					if (columnHints != null) {
						columnHints.moveItem(index, index - 1);
					}
				} catch (Exception e1) {
					ExceptionHandler.handle(e1);
				}
				viewer.refresh();
				viewer.getTable().select(index - 1);
				updateButtons();
			}
		}

		private void doMoveDown() {
			// Get the current selection and delete that row
			int index = viewer.getTable().getSelectionIndex();

			if (index > -1 && index < columnHandles.size() - 1) {
				viewer.cancelEditing();
				try {
					if (rsColumns != null) {
						rsColumns.moveItem(index, index + 1);
					}
					if (columnHints != null) {
						columnHints.moveItem(index, index + 1);
					}

				} catch (Exception e1) {
					ExceptionHandler.handle(e1);
				}
				viewer.refresh();
				viewer.getTable().select(index + 1);
				updateButtons();
			}
		}

		private void doRemoveAll() {
			try {
				if (rsColumns != null) {
					rsColumns.clearValue();
				}
				columnHints.clearValue();
				viewer.refresh();
			} catch (Exception e1) {
				logger.log(Level.FINE, e1.getMessage(), e1);
			}
			columnHintMap.clear();
			rsColumnMap.clear();
			updateButtons();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * getToolTip()
	 */
	@Override
	public String getToolTip() {
		return Messages.getString("OutputColumnDefnPage.OutputColumns.Tooltip"); //$NON-NLS-1$
	}

	private class ColumnInputDialog extends PropertyHandleInputDialog {

		private String title;
		private ColumnDefn columnDefn;

		private String columnName, alias, displayName, displayNameKey;
		private int dataType;
		private String EMPTY_STRING = "";

		public ColumnInputDialog(Shell shell, String title, ColumnDefn columnModel) {
			super(shell);
			this.title = title;
			this.columnDefn = columnModel;
			initColumnInfos();
		}

		@Override
		protected void createCustomControls(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = 3;
			layout.marginTop = 5;
			composite.setLayout(layout);
			GridData layoutData = new GridData(GridData.FILL_BOTH);
			layoutData.widthHint = 320;
			layoutData.heightHint = 200;
			composite.setLayoutData(layoutData);

			createDialogContents(composite);
		}

		private void createDialogContents(Composite composite) {
			GridData labelData = new GridData();
			labelData.horizontalSpan = 1;

			GridData textData = new GridData(GridData.FILL_HORIZONTAL);
			textData.horizontalSpan = 2;

			Label columnNameLabel = new Label(composite, SWT.NONE);
			columnNameLabel.setText(Messages.getString("ResultSetColumnPage.inputDialog.label.columnName")); //$NON-NLS-1$
			columnNameLabel.setLayoutData(labelData);

			final Text columnNameText = new Text(composite, SWT.BORDER);
			columnNameText.setLayoutData(textData);
			columnNameText.setText(columnName);
			columnNameText.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					columnName = columnNameText.getText().trim();
					validateSyntax();
				}

			});

			Label typeLabel = new Label(composite, SWT.NONE);
			typeLabel.setText(Messages.getString("ResultSetColumnPage.inputDialog.label.dataType")); //$NON-NLS-1$
			typeLabel.setLayoutData(labelData);

			final Combo typeCombo = ControlProvider.createCombo(composite, SWT.BORDER | SWT.READ_ONLY);
			typeCombo.setItems(displayDataTypes);
			typeCombo.setLayoutData(textData);
			typeCombo.setText(typeCombo.getItem(this.dataType));

			typeCombo.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					dataType = typeCombo.getSelectionIndex();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {

				}

			});

			Label aliasLabel = new Label(composite, SWT.NONE);
			aliasLabel.setText(Messages.getString("ResultSetColumnPage.inputDialog.label.alias")); //$NON-NLS-1$
			aliasLabel.setLayoutData(labelData);

			final Text aliasText = new Text(composite, SWT.BORDER);
			aliasText.setLayoutData(textData);
			aliasText.setText(alias);
			aliasText.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					alias = aliasText.getText().trim();
					validateSyntax();
				}

			});

			Label displayNameLabel = new Label(composite, SWT.NONE);
			displayNameLabel.setText(Messages.getString("ResultSetColumnPage.inputDialog.label.displayName")); //$NON-NLS-1$
			displayNameLabel.setLayoutData(labelData);

			final Text displayNameText = new Text(composite, SWT.BORDER);
			displayNameText.setLayoutData(textData);
			displayNameText.setText(displayName);
			displayNameText.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					displayName = displayNameText.getText().trim();
					validateSyntax();
				}

			});

			createDisplayNameKeyArea(composite);
		}

		private void createDisplayNameKeyArea(Composite parent) {
			Label displayNameKeyLabel = new Label(parent, SWT.NONE);
			displayNameKeyLabel.setText(Messages.getString("ResultSetColumnPage.inputDialog.label.displayNameKey")); //$NON-NLS-1$
			displayNameKeyLabel.setLayoutData(new GridData());

			final Text tx = ControlProvider.createText(parent, displayNameKey);
			tx.setLayoutData(ControlProvider.getGridDataWithHSpan(1));
			tx.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					displayNameKey = tx.getText().trim();
					validateSyntax();
				}

			});

			SelectionAdapter listener = new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent event) {
					ResourceEditDialog dlg = new ResourceEditDialog(getShell(),
							Messages.getString("ResourceKeyDescriptor.title.SelectKey")); //$NON-NLS-1$

					dlg.setResourceURLs(getResourceURLs());

					if (dlg.open() == Window.OK) {
						tx.setText((String) dlg.getResult());
					}
				}
			};
			Button bt = new Button(parent, SWT.PUSH);
			bt.setText("..."); //$NON-NLS-1$
			bt.addSelectionListener(listener);
			if (getBaseName() == null) {
				bt.setEnabled(false);
			}
		}

		private String[] getBaseNames() {
			List<String> resources = SessionHandleAdapter.getInstance().getReportDesignHandle().getIncludeResources();
			if (resources == null) {
				return null;
			} else {
				return resources.toArray(new String[0]);
			}
		}

		private URL[] getResourceURLs() {
			String[] baseNames = getBaseNames();
			if (baseNames == null) {
				return null;
			} else {
				URL[] urls = new URL[baseNames.length];
				for (int i = 0; i < baseNames.length; i++) {
					urls[i] = SessionHandleAdapter.getInstance().getReportDesignHandle().findResource(baseNames[i],
							IResourceLocator.MESSAGE_FILE);
				}
				return urls;
			}
		}

		private String getBaseName() {
			return SessionHandleAdapter.getInstance().getReportDesignHandle().getIncludeResource();
		}

		@Override
		protected boolean isResizable() {
			return true;
		}

		protected ColumnDefn getColumnDefn() {
			if (this.columnDefn == null) {
				this.columnDefn = new ColumnDefn();
			}
			this.columnDefn.setColumnName(columnName);
			this.columnDefn.setDataType(getTypeString(dataType));
			this.columnDefn.setAlias(alias);
			this.columnDefn.setDisplayName(displayName);
			this.columnDefn.setDisplayNameKey(displayNameKey);

			return this.columnDefn;
		}

		private void initColumnInfos() {
			if (this.columnDefn != null) {
				columnName = resolveNull(this.columnDefn.getColumnName());
				alias = resolveNull(this.columnDefn.getAlias());
				displayName = resolveNull(this.columnDefn.getDisplayName());
				displayNameKey = resolveNull(this.columnDefn.getDisplayNameKey());
				this.dataType = getTypeIndex(this.columnDefn.getDataType());
			} else {
				columnName = EMPTY_STRING;
				alias = EMPTY_STRING;
				displayName = EMPTY_STRING;
				displayNameKey = EMPTY_STRING;
				this.dataType = defaultDataTypeIndex;
			}
		}

		@Override
		protected void rollback() {

		}

		@Override
		protected IStatus validateSyntax(Object structureOrHandle) {
			if (columnName == null || columnName.trim().length() == 0) {
				return getMiscStatus(IStatus.ERROR,
						Messages.getString("ResultSetColumnPage.inputDialog.warning.emptyColumnName"));//$NON-NLS-1$
			} else if (columnName.equals(alias)) {
				return getMiscStatus(IStatus.ERROR,
						Messages.getString("ResultSetColumnPage.inputDialog.error.sameValue.columnNameAndAlias"));//$NON-NLS-1$
			} else if (isDuplicated(columnName)) {
				return getMiscStatus(IStatus.ERROR,
						Messages.getFormattedString("ResultSetColumnPage.inputDialog.error.duplicatedColumnName", //$NON-NLS-1$
								new Object[] { columnName }));
			} else if (isDuplicated(alias)) {
				return getMiscStatus(IStatus.ERROR,
						Messages.getFormattedString("ResultSetColumnPage.inputDialog.error.duplicatedAlias", //$NON-NLS-1$
								new Object[] { alias }));
			}
			return getOKStatus();
		}

		@Override
		protected IStatus validateSemantics(Object structureOrHandle) {
			return validateSyntax(structureOrHandle);
		}

		@Override
		protected String getTitle() {
			return title;
		}

		private String resolveNull(String value) {
			return value == null ? EMPTY_STRING : value.trim();
		}

		private boolean isDuplicated(String newName) {
			if (newName == null || newName.trim().length() == 0) {
				return false;
			}
			Iterator iter = columnHintMap.keySet().iterator();
			while (iter.hasNext()) {
				Object value = columnHintMap.get(iter.next());
				if (value instanceof ColumnHint) {
					ColumnHint column = (ColumnHint) value;
					if (!column.equals(this.columnDefn.getColumnHint())) {
						if (newName.equals(column.getProperty(null, ColumnHint.ALIAS_MEMBER))
								|| newName.equals(column.getProperty(null, ColumnHint.COLUMN_NAME_MEMBER))) {
							return true;
						}
					}
				}
			}
			return false;
		}

	}

}
