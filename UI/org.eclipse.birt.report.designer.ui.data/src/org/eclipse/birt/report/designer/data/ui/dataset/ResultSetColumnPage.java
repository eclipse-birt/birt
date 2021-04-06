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

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.util.ControlProvider;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ResourceEditDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class ResultSetColumnPage extends Composite {

	String[] displayDataTypes;

	public interface ITreeRefreshListener {

		void treeChanged();
	}

	protected static class ResultSetColumnModel {

		protected String columnName;
		protected String analysisType;
		protected String analysisCoumn;
		protected boolean isIndexColumn;
		protected boolean removeDuplicatedValue;

		protected String alias;
		// default type is "string"
		protected int dataType = getTypeIndex(DesignChoiceConstants.PARAM_TYPE_STRING);
		protected String displayName;
		protected String displayNameKey;
		protected String helpText;

		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj instanceof ResultSetColumnModel) {
				ResultSetColumnModel model = (ResultSetColumnModel) obj;
				if (columnName == null) {
					return model.columnName == null;
				}
				return columnName.equals(model.columnName);
			}
			return false;
		}

		public int hashCode() {
			return this.columnName.hashCode() * 11 + this.dataType * 17;
		}

	}

	protected static IChoice[] dataTypes = DEUtil.getMetaDataDictionary()
			.getStructure(ComputedColumn.COMPUTED_COLUMN_STRUCT).getMember(ComputedColumn.DATA_TYPE_MEMBER)
			.getAllowedChoices().getChoices();

	protected static String COLUMN_NAME = Messages.getString("dataset.editor.title.name"); //$NON-NLS-1$
	protected static String COLUMN_DATA_TYPE = Messages.getString("dataset.editor.title.type"); //$NON-NLS-1$
	protected static String COLUMN_ALIAS = Messages.getString("dataset.editor.title.alias"); //$NON-NLS-1$
	protected static String COLUMN_DISPLAY_NAME = Messages.getString("dataset.editor.title.displayName"); //$NON-NLS-1$
	protected static String COLUMN_DISPLAY_NAME_KEY = Messages.getString("dataset.editor.title.displayNameKey"); //$NON-NLS-1$

	protected ArrayList<ResultSetColumnModel> columnList = new ArrayList<ResultSetColumnModel>();
	protected ArrayList listenerList = new ArrayList();

	protected IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getElements(Object inputElement) {
			return ((ArrayList) inputElement).toArray();
		}
	};

	protected ITableLabelProvider labelProvider;

	protected TableViewer columnTable;

	private Button add, edit, up, down, delete;

	private static int getTypeIndex(String typeName) {
		for (int i = 0; i < dataTypes.length; i++) {
			if (dataTypes[i].getName().equals(typeName)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * @param parent
	 * @param style
	 */
	public ResultSetColumnPage(Composite parent, int style) {
		super(parent, style);
		initPageInfos();
		initLabelProvider();
		setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 650;
		setLayoutData(gd);
		createTableArea();
		createButtonArea();
		updateButtons();
		initAccessible();
	}

	protected void initLabelProvider() {
		labelProvider = new ITableLabelProvider() {

			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				ResultSetColumnModel model = (ResultSetColumnModel) element;
				String text = null;
				switch (columnIndex) {
				case 1:
					text = model.columnName;
					break;
				case 2:
					if (model.dataType >= 0 && model.dataType < dataTypes.length) {
						text = dataTypes[model.dataType].getDisplayName();
					}
					break;
				case 3:
					text = model.alias;
					break;
				case 4:
					text = model.displayName;
					break;
				case 5:
					text = model.displayNameKey;
					break;
				}
				return Utility.convertToGUIString(text);
			}

			public void addListener(ILabelProviderListener listener) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {
			}

		};
	}

	private void initPageInfos() {
		displayDataTypes = new String[dataTypes.length];
		for (int i = 0; i < displayDataTypes.length; i++) {
			displayDataTypes[i] = dataTypes[i].getDisplayName();
		}

	}

	protected void createTableArea() {
		Table table = new Table(this, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addKeyListener(new KeyAdapter() {

			/**
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			public void keyReleased(KeyEvent e) {
				// If Delete pressed, delete the selected row
				if (e.keyCode == SWT.DEL) {
					buttonPressed(SWT.DEL);
				}
			}

		});

		String[] columns = new String[] { null, COLUMN_NAME, COLUMN_DATA_TYPE, COLUMN_ALIAS, COLUMN_DISPLAY_NAME,
				COLUMN_DISPLAY_NAME_KEY };
		int[] columnWidth = new int[] { 20, 100, 100, 100, 100, 100 };

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setResizable(columns[i] != null);
			if (columns[i] != null) {
				column.setText(columns[i]);
			}
			column.setWidth(columnWidth[i]);
		}
		columnTable = new TableViewer(table);
		columnTable.setColumnProperties(columns);
		columnTable.setContentProvider(contentProvider);
		columnTable.setLabelProvider(labelProvider);
		columnTable.setInput(columnList);
		columnTable.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				updateButtons();
			}

		});
		columnTable.getTable().addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateButtons();
			}

		});

		columnTable.getTable().addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				if (columnTable.getTable().getSelectionCount() == 1) {
					doEdit();
				}
			}
		});
	}

	private void createButtonArea() {
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER));
		GridLayout layout = Utility.createGridLayoutWithoutMargin();
		layout.verticalSpacing = 10;
		composite.setLayout(layout);

		add = new Button(composite, SWT.NONE);
		add.setText(Messages.getString("ResultSetColumnPage.button.add")); //$NON-NLS-1$
		add.setEnabled(true);
		add.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doNew();
			}

		});

		edit = new Button(composite, SWT.NONE);
		edit.setText(Messages.getString("ResultSetColumnPage.button.edit")); //$NON-NLS-1$
		edit.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doEdit();
			}

		});

		delete = new Button(composite, SWT.NONE);
		delete.setText(Messages.getString("ResultSetColumnPage.button.delete")); //$NON-NLS-1$
		delete.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				buttonPressed(SWT.DEL);
			}

		});

		up = new Button(composite, SWT.NONE);
		up.setText(Messages.getString("ResultSetColumnPage.button.up"));
		up.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				buttonPressed(SWT.UP);
			}

		});

		down = new Button(composite, SWT.NONE);
		down.setText(Messages.getString("ResultSetColumnPage.button.down"));
		down.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				buttonPressed(SWT.DOWN);
			}

		});

		int width = getMaxWidth(add, 55);
		width = getMaxWidth(edit, width);
		width = getMaxWidth(delete, width);
		width = getMaxWidth(up, width);
		width = getMaxWidth(down, width);

		GridData buttonGd = new GridData();
		buttonGd.widthHint = width;

		add.setLayoutData(buttonGd);
		edit.setLayoutData(buttonGd);
		delete.setLayoutData(buttonGd);
		up.setLayoutData(buttonGd);
		down.setLayoutData(buttonGd);
	}

	private int getMaxWidth(Control control, int size) {
		int width = control.computeSize(-1, -1).x;
		return width > size ? width : size;
	}

	protected void doNew() {
		ColumnInputDialog inputDialog = new ColumnInputDialog(getShell(),
				Messages.getString("ResultSetColumnPage.inputDialog.newColumn.title"), //$NON-NLS-1$
				new ResultSetColumnModel());
		if (inputDialog.open() == Window.OK) {
			ResultSetColumnModel columnModel = inputDialog.getResultSetColumnModel();
			columnList.add(columnModel);
			columnTable.refresh();
		}
		updateButtons();
	}

	protected void doEdit() {
		ColumnInputDialog inputDialog = new ColumnInputDialog(getShell(),
				Messages.getString("ResultSetColumnPage.inputDialog.editColumn.title"), //$NON-NLS-1$
				getSelectedColumn());
		if (inputDialog.open() == Window.OK) {
			inputDialog.getResultSetColumnModel();
			columnTable.refresh();
		}
		updateButtons();
	}

	protected void buttonPressed(int buttonId) {
		ResultSetColumnModel model = getSelectedColumn();
		int index = columnList.indexOf(model);
		columnList.remove(index);
		switch (buttonId) {
		case SWT.UP:
			columnList.add(index - 1, model);
			break;
		case SWT.DOWN:
			columnList.add(index + 1, model);
			break;
		}
		updateTable();
		updateButtons();
	}

	/**
	 * 
	 * @param handle
	 */
	public void saveResult(DataSetHandle handle) {
		PropertyHandle resultSetPropertyHandle = handle.getPropertyHandle(DataSetHandle.RESULT_SET_HINTS_PROP);
		PropertyHandle columnHintPropertyHandle = handle.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP);
		try {
			// Remove all defined columns
			resultSetPropertyHandle.setStringValue(null);
			ArrayList removeList = new ArrayList();
			// Apply new name
			for (Iterator iter = columnHintPropertyHandle.iterator(); iter.hasNext();) {
				ColumnHintHandle columnHintHandle = (ColumnHintHandle) iter.next();
				if (!isDuplicatedName(null, columnHintHandle.getColumnName())) {
					removeList.add(columnHintHandle);
				}
			}
			// Remove all deleted column hint
			columnHintPropertyHandle.removeItems(removeList);

			Iterator iter = columnList.iterator();
			for (int i = 0; iter.hasNext(); i++) {
				ResultSetColumnModel model = (ResultSetColumnModel) iter.next();
				ResultSetColumn column = StructureFactory.createResultSetColumn();
				column.setColumnName(model.columnName);
				column.setDataType(dataTypes[model.dataType].getName());
				column.setPosition(Integer.valueOf(i));
				resultSetPropertyHandle.addItem(column);
				ColumnHintHandle columnHintHandle = null;
				for (Iterator hintIter = columnHintPropertyHandle.iterator(); hintIter.hasNext();) {
					columnHintHandle = (ColumnHintHandle) hintIter.next();
					if (columnHintHandle.getColumnName().equals(model.columnName)) {
						break;
					}
					columnHintHandle = null;
				}
				if (columnHintHandle == null) {
					ColumnHint columnHint = StructureFactory.createColumnHint();
					columnHint.setProperty(ColumnHint.COLUMN_NAME_MEMBER, model.columnName);
					columnHint.setProperty(ColumnHint.DISPLAY_NAME_ID_MEMBER, model.displayNameKey);
					columnHintHandle = (ColumnHintHandle) columnHintPropertyHandle.addItem(columnHint);
				} else {
					columnHintHandle.setDisplayName(model.displayName);
					columnHintHandle.setDisplayNameKey(model.displayNameKey);
				}
				updateColumnHintProperties(model, columnHintHandle);
			}

		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	protected void updateColumnHintProperties(ResultSetColumnModel model, ColumnHintHandle columnHintHandle)
			throws SemanticException {
		columnHintHandle.setAnalysis(model.analysisType);
		columnHintHandle.setAlias(model.alias);
		columnHintHandle.setDisplayName(model.displayName);
		columnHintHandle.setHelpText(model.helpText);
	}

	public boolean isEmpty() {
		return columnList.isEmpty();
	}

	protected boolean isDuplicatedName(ResultSetColumnModel currentModel, String newName) {
		if (newName == null || newName.trim().length() == 0) {
			return false;
		}
		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			ResultSetColumnModel model = (ResultSetColumnModel) iter.next();
			if (model == currentModel) {
				continue;
			}
			if (newName.equals(model.columnName) || newName.equals(model.alias)) {
				return true;
			}
		}
		return false;
	}

	protected ResultSetColumnModel getSelectedColumn() {
		return (ResultSetColumnModel) ((IStructuredSelection) columnTable.getSelection()).getFirstElement();
	}

	public void addTreeRefreshListener(ITreeRefreshListener listener) {
		if (!listenerList.contains(listener)) {
			listenerList.add(listener);
		}
	}

	public void updateTable() {
		columnTable.refresh();
		for (Iterator iter = listenerList.iterator(); iter.hasNext();) {
			((ITreeRefreshListener) iter.next()).treeChanged();
		}
	}

	protected void updateButtons() {
		boolean upEnabled, downEnabled, deleteEnabled, editEnabled;
		upEnabled = downEnabled = deleteEnabled = editEnabled = false;
		if (!columnTable.getSelection().isEmpty()) {
			ResultSetColumnModel model = getSelectedColumn();
			int selectedIndex = columnList.indexOf(model);
			if (selectedIndex != -1) {
				upEnabled = (selectedIndex != 0);
				downEnabled = (selectedIndex != columnList.size() - 1);
				deleteEnabled = true;
				editEnabled = true;
			}
		}
		up.setEnabled(upEnabled);
		down.setEnabled(downEnabled);
		delete.setEnabled(deleteEnabled);
		edit.setEnabled(editEnabled);
	}

	/**
	 * make custom control accessible
	 * 
	 */
	void initAccessible() {
		getAccessible().addAccessibleListener(new AccessibleAdapter() {

			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {

			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(new Point(e.x, e.y));
				if (getBounds().contains(testPoint)) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = getBounds();
				Point pt = toDisplay(new Point(location.x, location.y));
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_COMBOBOX;
			}

			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_NORMAL;
			}
		});
	}

	private class ColumnInputDialog extends PropertyHandleInputDialog {

		private String title;
		private ResultSetColumnModel columnModel;

		private String columnName, alias, displayName, displayNameKey;
		private int dataType;
		private String EMPTY_STRING = ""; //$NON-NLS-1$

		public ColumnInputDialog(Shell shell, String title, ResultSetColumnModel columnModel) {
			super(shell);
			this.title = title;
			this.columnModel = columnModel;
			initColumnInfos();
		}

		protected void createCustomControls(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.numColumns = 3;
			layout.marginTop = 5;
			composite.setLayout(layout);
			GridData layoutData = new GridData(GridData.FILL_BOTH);
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
			if (dataType >= 0 && dataType < typeCombo.getItemCount()) {
				typeCombo.setText(typeCombo.getItem(dataType));
			}
			typeCombo.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					dataType = typeCombo.getSelectionIndex();
				}

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

				public void modifyText(ModifyEvent e) {
					displayName = displayNameText.getText().trim();
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

				public void modifyText(ModifyEvent e) {
					displayNameKey = tx.getText().trim();
				}

			});

			SelectionAdapter listener = new SelectionAdapter() {

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
			if (getBaseName() == null)
				bt.setEnabled(false);
		}

		private String[] getBaseNames() {
			List<String> resources = SessionHandleAdapter.getInstance().getReportDesignHandle().getIncludeResources();
			if (resources == null)
				return null;
			else
				return resources.toArray(new String[0]);
		}

		private URL[] getResourceURLs() {
			String[] baseNames = getBaseNames();
			if (baseNames == null)
				return null;
			else {
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

		protected boolean isResizable() {
			return true;
		}

		protected ResultSetColumnModel getResultSetColumnModel() {
			if (this.columnModel == null) {
				this.columnModel = new ResultSetColumnModel();
			}
			this.columnModel.columnName = columnName;
			this.columnModel.dataType = dataType;
			this.columnModel.alias = alias;
			this.columnModel.displayName = displayName;
			this.columnModel.displayNameKey = displayNameKey;

			return this.columnModel;
		}

		private void initColumnInfos() {
			if (this.columnModel != null) {
				columnName = resolveNull(this.columnModel.columnName);
				alias = resolveNull(this.columnModel.alias);
				displayName = resolveNull(this.columnModel.displayName);
				displayNameKey = resolveNull(this.columnModel.displayNameKey);
				this.dataType = this.columnModel.dataType;
			} else {
				this.columnName = EMPTY_STRING;
				this.alias = EMPTY_STRING;
				this.displayName = EMPTY_STRING;
				this.displayNameKey = EMPTY_STRING;
				this.dataType = -1;
			}
		}

		private String resolveNull(String value) {
			return value == null ? EMPTY_STRING : value.trim();
		}

		protected void rollback() {

		}

		protected IStatus validateSemantics(Object structureOrHandle) {
			return validateSyntax(structureOrHandle);
		}

		protected IStatus validateSyntax(Object structureOrHandle) {
			if (columnName == null || columnName.trim().length() == 0) {
				return getMiscStatus(IStatus.ERROR,
						Messages.getString("ResultSetColumnPage.inputDialog.warning.emptyColumnName"));//$NON-NLS-1$
			} else if (columnName.equals(alias)) {
				return getMiscStatus(IStatus.ERROR,
						Messages.getString("ResultSetColumnPage.inputDialog.error.sameValue.columnNameAndAlias"));//$NON-NLS-1$
			} else if (isDuplicatedName(this.columnModel, columnName)) {
				return getMiscStatus(IStatus.ERROR, Messages.getFormattedString(
						"ResultSetColumnPage.inputDialog.error.duplicatedColumnName", new Object[] { columnName }));// $NON-NLS-1$
			} else if (isDuplicatedName(this.columnModel, alias)) {
				return getMiscStatus(IStatus.ERROR, Messages.getFormattedString(
						"ResultSetColumnPage.inputDialog.error.duplicatedAlias", new Object[] { alias }));// $NON-NLS-1$
			}
			return getOKStatus();
		}

		protected String getTitle() {
			return title;
		}

	}

}
