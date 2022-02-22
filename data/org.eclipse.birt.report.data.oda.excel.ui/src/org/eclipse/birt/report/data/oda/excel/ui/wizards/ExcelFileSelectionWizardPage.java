/*******************************************************************************
 * Copyright (c) 2012 Megha Nidhi Dahal and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
 *    Actuate Corporation - support of timestamp, datetime, time, and date data types
 *    Actuate Corporation - added support of relative file path
 *    Actuate Corporation - support defining an Excel input file path or URI as part of the data source definition
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.ui.wizards;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.data.oda.excel.ExcelODAConstants;
import org.eclipse.birt.report.data.oda.excel.impl.Driver;
import org.eclipse.birt.report.data.oda.excel.impl.util.ExcelFileReader;
import org.eclipse.birt.report.data.oda.excel.impl.util.ResourceLocatorUtil;
import org.eclipse.birt.report.data.oda.excel.impl.util.querytextutil.ColumnsInfoUtil;
import org.eclipse.birt.report.data.oda.excel.impl.util.querytextutil.QueryTextUtil;
import org.eclipse.birt.report.data.oda.excel.ui.i18n.Messages;
import org.eclipse.birt.report.data.oda.excel.ui.util.IHelpConstants;
import org.eclipse.birt.report.data.oda.excel.ui.util.Utility;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Extends the ODA design ui framework to provide a driver-specific custom
 * editor page to create or edit an ODA data set design instance.
 */
public class ExcelFileSelectionWizardPage extends DataSetWizardPage implements ISelectionChangedListener {

	private static String DEFAULT_MESSAGE = Messages.getString("wizard.defaultMessage.selectDataSet"); //$NON-NLS-1$

	private static final String queryTextDelimiter = ":"; //$NON-NLS-1$
	private static final String columnsInfoStartSymbol = "{"; //$NON-NLS-1$
	private static final String columnsInfoEndSymbol = "}"; //$NON-NLS-1$

	private static String name = Messages.getString("editor.title.name"); //$NON-NLS-1$
	private static String originalName = Messages.getString("editor.title.originalName"); //$NON-NLS-1$
	private static String dataType = Messages.getString("editor.title.type"); //$NON-NLS-1$

	private static String[] dataTypeDisplayNames = { Messages.getString("datatypes.dateTime"), //$NON-NLS-1$
			Messages.getString("datatypes.decimal"), //$NON-NLS-1$
			Messages.getString("datatypes.float"), //$NON-NLS-1$
			Messages.getString("datatypes.integer"), //$NON-NLS-1$
			Messages.getString("datatypes.date"), //$NON-NLS-1$
			Messages.getString("datatypes.time"), //$NON-NLS-1$
			Messages.getString("datatypes.string"), //$NON-NLS-1$
			Messages.getString("datatypes.boolean") //$NON-NLS-1$
	};

	private Map<Object, Object> dataTypeDisplayNameMap = new HashMap<>();

	private Map<Object, Object> dataTypeValueMape = new HashMap<>();

	private final int DEFAULT_WIDTH = 200;
	private final int DEFAULT_HEIGHT = 200;

	private transient ComboViewer worksheetsCombo = null;
	private transient List availableList = null;
	private transient TableViewer selectedColumnsViewer = null;
	private transient Button btnAdd = null;
	private transient Button btnRemove = null;
	private transient Button btnMoveUp = null;
	private transient Button btnMoveDown = null;

	private String uriPath;
	private String inclColumnNameLine;
	private String inclTypeLine;
	private String savedSelectedColumnsInfoString;

	/** store latest selected file */
	private Object selectedFile;

	/** store latest selected sheet name */
	private String currentSheetName;

	private java.util.List<String[]> originalFileColumnsInfoList = new ArrayList<>();
	private java.util.List<String[]> savedSelectedColumnsInfoList = new ArrayList<>();

	/**
	 * @param pageName
	 */
	public ExcelFileSelectionWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		createColumnTypeMap();
		setMessage(DEFAULT_MESSAGE);
		setPageComplete(false);
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public ExcelFileSelectionWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPageCustomControl(Composite parent) {
		setControl(createPageControl(parent));
		initializeControl();

		Utility.setSystemHelp(getControl(), IHelpConstants.CONEXT_ID_DATASET_EXCEL);
	}

	private void createColumnTypeMap() {
		dataTypeDisplayNameMap.put(new Integer(4), Messages.getString("datatypes.integer")); //$NON-NLS-1$
		dataTypeDisplayNameMap.put(new Integer(8), Messages.getString("datatypes.float")); //$NON-NLS-1$
		dataTypeDisplayNameMap.put(new Integer(12), Messages.getString("datatypes.string")); //$NON-NLS-1$
		dataTypeDisplayNameMap.put(new Integer(91), Messages.getString("datatypes.date")); //$NON-NLS-1$
		dataTypeDisplayNameMap.put(new Integer(92), Messages.getString("datatypes.time")); //$NON-NLS-1$
		dataTypeDisplayNameMap.put(new Integer(93), Messages.getString("datatypes.dateTime")); //$NON-NLS-1$
		dataTypeDisplayNameMap.put(new Integer(2), Messages.getString("datatypes.decimal")); //$NON-NLS-1$
		dataTypeDisplayNameMap.put(new Integer(16), Messages.getString("datatypes.boolean")); //$NON-NLS-1$

		dataTypeValueMape.put(Messages.getString("datatypes.integer"), "INT"); //$NON-NLS-1$ //$NON-NLS-2$
		dataTypeValueMape.put(Messages.getString("datatypes.float"), //$NON-NLS-1$
				"DOUBLE"); //$NON-NLS-1$
		dataTypeValueMape.put(Messages.getString("datatypes.string"), //$NON-NLS-1$
				"STRING"); //$NON-NLS-1$
		dataTypeValueMape.put(Messages.getString("datatypes.date"), "DATE"); //$NON-NLS-1$ //$NON-NLS-2$
		dataTypeValueMape.put(Messages.getString("datatypes.time"), "TIME"); //$NON-NLS-1$ //$NON-NLS-2$
		dataTypeValueMape.put(Messages.getString("datatypes.dateTime"), //$NON-NLS-1$
				"TIMESTAMP"); //$NON-NLS-1$
		dataTypeValueMape.put(Messages.getString("datatypes.decimal"), //$NON-NLS-1$
				"BIGDECIMAL"); //$NON-NLS-1$
		dataTypeValueMape.put(Messages.getString("datatypes.boolean"), //$NON-NLS-1$
				"BOOLEAN"); //$NON-NLS-1$
	}

	/**
	 *
	 *
	 */
	private void initializeControl() {
		/*
		 * Optionally restores the state of a previous design session. Obtains designer
		 * state, using getInitializationDesignerState();
		 */

		DataSetDesign dataSetDesign = getInitializationDesign();
		if (dataSetDesign == null) {
			return; // nothing to initialize
		}

		String queryText = dataSetDesign.getQueryText();
		if (queryText == null || selectedFile == null) {
			return; // nothing to initialize
		}

		updateValuesFromQuery(queryText);

		if (dataSetDesign.getPublicProperties() != null) {
			currentSheetName = dataSetDesign.getPublicProperties().getProperty(ExcelODAConstants.CONN_WORKSHEETS_PROP);
		}

		/*
		 * Optionally honor the request for an editable or read-only design session
		 * isSessionEditable();
		 */
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.
	 * DataSetDesign)
	 */
	@Override
	protected DataSetDesign collectDataSetDesign(DataSetDesign design) {
		// if this page in DataSetEditor hasn't been activated
		if (worksheetsCombo == null) {
			return design;
		}

		savePage(design);
		return design;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.internal.ui.DataSetWizardPage
	 * #collectResponseState()
	 */
	@Override
	protected void collectResponseState() {
		super.collectResponseState();
		/*
		 * Optionally assigns custom response state, for inclusion in the ODA design
		 * session response, using setResponseSessionStatus( SessionStatus status )
		 * setResponseDesignerState( DesignerState customState );
		 */
	}

	/**
	 *
	 * @param parent
	 * @return
	 */
	private Control createPageControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);

		FormLayout layout = new FormLayout();
		composite.setLayout(layout);

		FormData data = new FormData();
		data.left = new FormAttachment(0, 5);
		data.top = new FormAttachment(0, 5);

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.getString("label.selectworksheet")); //$NON-NLS-1$
		label.setLayoutData(data);

		createTopComposite(composite, label);

		createLeftComposite(composite);

		Composite btnComposite = createAddBtnComposite(composite);

		createRightComposite(composite, btnComposite);

		setupEditors();

		loadProperties();

		updateFileList();
		return composite;
	}

	/**
	 * Create the top composite of the page
	 *
	 * @param composite
	 * @param label
	 */
	private void createTopComposite(Composite composite, Label label) {
		FormData data = new FormData();
		data.left = new FormAttachment(label, 5);
		data.right = new FormAttachment(40, -5);
		worksheetsCombo = new ComboViewer(composite, SWT.BORDER | SWT.READ_ONLY);
		worksheetsCombo.getControl().setLayoutData(data);
		worksheetsCombo.setContentProvider(new ArrayContentProvider());
		worksheetsCombo.addSelectionChangedListener(this);
		worksheetsCombo.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				return getFileName(element);
			}
		});
		data = new FormData();
		data.left = new FormAttachment(worksheetsCombo.getControl(), 5);
		data.top = new FormAttachment(0, 5);
	}

	/**
	 * Create the left composite of the page
	 *
	 * @param composite
	 */
	private void createLeftComposite(Composite composite) {
		FormData data = new FormData();
		data.top = new FormAttachment(worksheetsCombo.getControl(), 10, SWT.BOTTOM);
		data.left = new FormAttachment(0, 5);
		data.right = new FormAttachment(40, -5);
		data.bottom = new FormAttachment(100, -5);
		data.width = DEFAULT_WIDTH;
		data.height = DEFAULT_HEIGHT;
		availableList = new List(composite, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		availableList.setLayoutData(data);
		availableList.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedColumnsViewer.getTable().deselectAll();
				btnAdd.setEnabled(true);
				btnRemove.setEnabled(false);
				btnMoveDown.setEnabled(false);
				btnMoveUp.setEnabled(false);
			}
		});

		availableList.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				addColumns();
			}
		});
	}

	/**
	 * Create the middle button composite that displays ADD button
	 *
	 * @param composite
	 * @return
	 */
	private Composite createAddBtnComposite(Composite composite) {
		FormData data = new FormData();
		data.top = new FormAttachment(50, 5);
		data.left = new FormAttachment(availableList, 3);

		Composite btnComposite = new Composite(composite, SWT.NONE);
		btnComposite.setLayoutData(data);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		btnComposite.setLayout(layout);

		btnAdd = new Button(btnComposite, SWT.NONE);
		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.FILL_HORIZONTAL);
		gridData.heightHint = 25;
		btnAdd.setLayoutData(gridData);
		btnAdd.setToolTipText(Messages.getString("tooltip.button.add")); //$NON-NLS-1$

		if (btnAdd.getStyle() == (btnAdd.getStyle() | SWT.LEFT_TO_RIGHT)) {
			btnAdd.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_FORWARD));
		} else {
			btnAdd.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_TOOL_BACK));
		}

		btnAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addColumns();
			}
		});
		btnAdd.setEnabled(false);
		return btnComposite;
	}

	/**
	 * Create the right composite of the page
	 *
	 * @param composite
	 * @param btnComposite
	 */
	private void createRightComposite(Composite composite, Composite btnComposite) {
		FormData data = new FormData();
		data.top = new FormAttachment(worksheetsCombo.getControl(), 10, SWT.BOTTOM);
		data.left = new FormAttachment(btnComposite, 3);
		data.right = new FormAttachment(100, -2);
		data.bottom = new FormAttachment(100, -5);

		Composite rightComposite = new Composite(composite, SWT.BORDER);
		rightComposite.setLayoutData(data);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		rightComposite.setLayout(layout);

		selectedColumnsViewer = new TableViewer(rightComposite,
				SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		selectedColumnsViewer.getTable().setHeaderVisible(true);
		selectedColumnsViewer.getTable().setLinesVisible(true);
		selectedColumnsViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));

		TableColumn column = new TableColumn(selectedColumnsViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("editor.title.name")); //$NON-NLS-1$
		column.setWidth(100);
		column = new TableColumn(selectedColumnsViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("editor.title.originalName")); //$NON-NLS-1$
		column.setWidth(100);
		column = new TableColumn(selectedColumnsViewer.getTable(), SWT.NONE);
		column.setText(Messages.getString("editor.title.type")); //$NON-NLS-1$
		column.setWidth(100);

		selectedColumnsViewer.getTable().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				btnAdd.setEnabled(false);
				availableList.deselectAll();

				int count = selectedColumnsViewer.getTable().getSelectionCount();
				int index = selectedColumnsViewer.getTable().getSelectionIndex();
				if (count == 1) {
					btnRemove.setEnabled(true);
					if (selectedColumnsViewer.getTable().getItemCount() == 1) {
						btnMoveUp.setEnabled(false);
						btnMoveDown.setEnabled(false);
					} else if (index == 0) {
						btnMoveUp.setEnabled(false);
						btnMoveDown.setEnabled(true);
					} else if (index == (selectedColumnsViewer.getTable().getItemCount() - 1)) {
						btnMoveUp.setEnabled(true);
						btnMoveDown.setEnabled(false);
					} else {
						btnMoveUp.setEnabled(true);
						btnMoveDown.setEnabled(true);
					}
				} else if (count > 1) {
					btnRemove.setEnabled(true);
					btnMoveUp.setEnabled(false);
					btnMoveDown.setEnabled(false);
				} else {
					btnRemove.setEnabled(false);
					btnMoveUp.setEnabled(false);
					btnMoveDown.setEnabled(false);
				}
			}
		});

		setColumnsViewerContent();

		setColumnsViewerLabels();

		selectedColumnsViewer.getTable().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				removeColumns();
			}
		});

		createEditBtnGroup(rightComposite);
	}

	/**
	 * Create the right button group that displays the UP,DOWN and REMOVE buttons
	 *
	 * @param rightComposite
	 */
	private void createEditBtnGroup(Composite rightComposite) {
		Composite btnComposite = new Composite(rightComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 5;
		btnComposite.setLayout(layout);

		btnMoveUp = new Button(btnComposite, SWT.NONE);
		btnMoveUp.setText(Messages.getString("button.moveUp")); //$NON-NLS-1$
		btnMoveUp.setToolTipText(Messages.getString("tooltip.button.up")); //$NON-NLS-1$
		btnMoveUp.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				moveUpItem();
			}
		});

		btnRemove = new Button(btnComposite, SWT.NONE);
		btnRemove.setText(Messages.getString("button.delete")); //$NON-NLS-1$
		btnRemove.setToolTipText(Messages.getString("tooltip.button.delete")); //$NON-NLS-1$
		btnRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				removeColumns();
			}
		});

		btnMoveDown = new Button(btnComposite, SWT.NONE);
		btnMoveDown.setText(Messages.getString("button.moveDown")); //$NON-NLS-1$
		btnMoveDown.setToolTipText(Messages.getString("tooltip.button.down")); //$NON-NLS-1$
		btnMoveDown.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				moveDownItem();
			}
		});

		resetButtonWidth();
	}

	private void resetButtonWidth() {
		int widthHint = Math.max(btnMoveUp.computeSize(SWT.DEFAULT, SWT.DEFAULT).x,
				btnRemove.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		widthHint = Math.max(widthHint, btnMoveDown.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);
		widthHint = Math.max(widthHint, 52);
		GridData btnGd = new GridData();
		btnGd.widthHint = widthHint;
		btnMoveUp.setLayoutData(btnGd);
		btnRemove.setLayoutData(btnGd);
		btnMoveDown.setLayoutData(btnGd);
	}

	/**
	 * Set the labels of the ColumnsViewer
	 *
	 */
	private void setColumnsViewerLabels() {
		selectedColumnsViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				return ((String[]) element)[columnIndex];
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

	/**
	 * Set the content of the ColumnsViewer
	 *
	 */
	private void setColumnsViewerContent() {
		selectedColumnsViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof java.util.List) {
					return ((java.util.List<?>) inputElement).toArray();
				}

				return new Object[0];
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
		});
	}

	/**
	 * setup the editors in the table viewer
	 *
	 */
	private void setupEditors() {
		CellEditor[] editors = new CellEditor[3];
		editors[0] = new TextCellEditor(selectedColumnsViewer.getTable(), SWT.NONE);
		editors[2] = new ComboBoxCellEditor(selectedColumnsViewer.getTable(), dataTypeDisplayNames, SWT.READ_ONLY);

		selectedColumnsViewer.setColumnProperties(new String[] { name, originalName, dataType });
		selectedColumnsViewer.setCellEditors(editors);
		selectedColumnsViewer.setCellModifier(new ICellModifier() {

			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}

			@Override
			public Object getValue(Object element, String property) {
				Object value = null;
				if (name.equals(property)) {
					value = ((String[]) element)[0];
				} else if (originalName.equals(property)) {
					value = ((String[]) element)[1];
				} else if (dataType.equals(property)) {
					String temp = ((String[]) element)[2];
					if (temp == null) {
						value = new Integer(0);
					} else {
						for (int i = 0; i < dataTypeDisplayNames.length; i++) {
							if (temp.equals(dataTypeDisplayNames[i])) {
								value = new Integer(i);
								break;
							}
						}
					}
				}
				return value;
			}

			@Override
			public void modify(Object element, String property, Object value) {
				String[] actualElement = (String[]) ((TableItem) element).getData();
				if (value != null) {
					if (name.equals(property)) {
						if (isUnique(actualElement, (String) value) && !isEmpty((String) value)) {
							replace(actualElement, property, (String) value);

							selectedColumnsViewer.refresh();
							setMessage(DEFAULT_MESSAGE);
						} else {
							setMessage(Messages.getString("error.duplicatedNameValueOrEmpty"), //$NON-NLS-1$
									WARNING);
						}
					} else if (dataType.equals(property)) {
						int index = ((Integer) value).intValue();
						if (!(dataTypeDisplayNames[index]).equals(actualElement[2])) {
							replace(actualElement, property, dataTypeDisplayNames[index]);
							selectedColumnsViewer.refresh();
						}
					}

				}

			}

		});
	}

	/**
	 * see if the value in the selected element of the saved selected columns
	 * information is unique or not
	 *
	 * @param element the selected element
	 * @param value   the value
	 * @return
	 */
	private boolean isUnique(String[] element, String value) {
		for (int i = 0; i < savedSelectedColumnsInfoList.size(); i++) {
			if (i != savedSelectedColumnsInfoList.indexOf(element)
					&& value.equalsIgnoreCase(((String[]) savedSelectedColumnsInfoList.get(i))[0])) {
				return false;
			}
		}
		return true;
	}

	private boolean isEmpty(String value) {
		return (value.trim().equals("")); //$NON-NLS-1$
	}

	/**
	 * get the count of hte existence of the given column name in the already saved
	 * selected columns information
	 *
	 * @param columnName given column name
	 * @return
	 */

	private int getExistenceCount(String columnName) {
		int count = 0;
		java.util.List<Object> existedColumns = new ArrayList<>();

		for (int i = 0; i < savedSelectedColumnsInfoList.size(); i++) {
			if (columnName.equals(((String[]) savedSelectedColumnsInfoList.get(i))[1])) {
				count++;
				existedColumns.add(savedSelectedColumnsInfoList.get(i));
			}
		}

		for (int j = 0; j < existedColumns.size(); j++) {
			if ((columnName + "_" + count).equals(((String[]) savedSelectedColumnsInfoList.get(j))[0])) //$NON-NLS-1$
			{
				count++;
				j = -1;
			}
		}

		return count;
	}

	/**
	 * replace the element in the saved selected columns information list
	 *
	 * @param element  the selected element
	 * @param property the element's selected property
	 * @param value    the value of the selected property in the element
	 */
	private void replace(String[] element, String property, String value) {
		int index = savedSelectedColumnsInfoList.indexOf((Object) element);

		if (name.equals(property)) {
			element[0] = value;
			savedSelectedColumnsInfoList.set(index, element);
		} else if (dataType.equals(property)) {
			element[2] = value;
			savedSelectedColumnsInfoList.set(index, element);
		}
	}

	private String getFileName(Object obj) {
		if (obj instanceof File) {
			return ((File) obj).getName();
		} else if (obj instanceof URI) {
			return ((URI) obj).getPath();
		}
		return obj.toString();
	}

	/*
	 * File Combo Viewer selection changed listener
	 *
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {

		String sheetName = (String) ((IStructuredSelection) event.getSelection()).getFirstElement();

		String queryText = this.getInitializationDesign().getQueryText();
		// When create a new data set, the quertText is an empty string, in this
		// condition
		// if the sheet name is equal, just return.
		// When edit a data set, we should check the sheet name is equal and the
		// file name
		// is in accordance with query text.
		if (sheetName.equalsIgnoreCase(currentSheetName)
				&& (queryText.trim().length() == 0 || isOldFile(queryText, getFileName(selectedFile)))) {
			return;
		}
		if (currentSheetName != null && !MessageDialog.openConfirm(worksheetsCombo.getControl().getShell(),
				Messages.getString("confirm.reselectWorksheetTitle"), //$NON-NLS-1$
				Messages.getString("confirm.reselectWorksheetMessage"))) //$NON-NLS-1$
		{
			worksheetsCombo.setSelection(new StructuredSelection(currentSheetName));

			return;
		}

		currentSheetName = sheetName;
		setPageComplete(false);
		selectedColumnsViewer.getTable().removeAll();
		savedSelectedColumnsInfoList.clear();
		availableList.removeAll();

		if (this.getShell() != null) {
			Cursor waitCursor = new Cursor(this.getShell().getDisplay(), SWT.CURSOR_WAIT);
			this.getControl().setCursor(waitCursor);
		}

		String fileName = getFileName(selectedFile);
		String[] columnNames = getFileColumnNames(selectedFile);

		if (columnNames != null && columnNames.length != 0) {
			enableListAndViewer();
			availableList.setItems(columnNames);
			availableList.select(0);
			btnAdd.setEnabled(true);
			btnRemove.setEnabled(false);
			btnMoveUp.setEnabled(false);
			btnMoveDown.setEnabled(false);
			if (!(fileName.endsWith(ExcelODAConstants.XLS_FORMAT)
					|| fileName.endsWith(ExcelODAConstants.XLSX_FORMAT))) {
				setMessage(Messages.getString("warning.fileExtensionInvalid"), //$NON-NLS-1$
						WARNING);
			} else if (selectedColumnsViewer.getTable().getItemCount() == 0) {
				setMessage(getEmptyColumnErrMsg(), ERROR);
			} else {
				setMessage(DEFAULT_MESSAGE);
			}

		}

		// reset cursor back to normal state
		if (this.getShell() != null) {
			Cursor normalCursor = new Cursor(this.getShell().getDisplay(), SWT.CURSOR_ARROW);
			this.getControl().setCursor(normalCursor);
		}

	}

	private String getEmptyColumnErrMsg() {
		return this.currentSheetName == null ? Messages.getString("error.selectWorksheet")
				: Messages.getString("error.selectColumns");
	}

	/**
	 * Load the custom properties
	 */
	private void loadProperties() {
		java.util.Properties dataSourceProps = null;
		try {
			dataSourceProps = DesignSessionUtil
					.getEffectiveDataSourceProperties(getInitializationDesign().getDataSourceDesign());
		} catch (OdaException e) {
			this.setMessage(e.getLocalizedMessage(), ERROR);
			return;
		}

		uriPath = dataSourceProps.getProperty(ExcelODAConstants.CONN_FILE_URI_PROP);
		inclColumnNameLine = dataSourceProps.getProperty(ExcelODAConstants.CONN_INCLCOLUMNNAME_PROP);
		inclTypeLine = dataSourceProps.getProperty(ExcelODAConstants.CONN_INCLTYPELINE_PROP);

		DataSetDesign dataSetDesign = getInitializationDesign();
		if (dataSetDesign == null) {
			return; // nothing to initialize
		}

		if (dataSetDesign.getPublicProperties() != null) {
			currentSheetName = dataSetDesign.getPublicProperties().getProperty(ExcelODAConstants.CONN_WORKSHEETS_PROP);
		}
	}

	static class ExcelFileFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String fileName) {
			File file = new File(dir + File.separator + fileName);
			if (file.isFile() && !file.isHidden()) {
				if (fileName.endsWith(ExcelODAConstants.XLS_FORMAT)
						|| fileName.endsWith(ExcelODAConstants.XLSX_FORMAT)) {
					return true;
				}

				return false;
			} else {
				return false;
			}
		}
	}

	/**
	 * When edit the excel data set, we can change the datasource. Sometimes,
	 * different excel file may have the some sheet name. This method is to check
	 * whether the data source(excel file) is changed.
	 *
	 * @param queryText
	 * @param selectedFile
	 * @return
	 */
	private boolean isOldFile(String queryText, String selectedFile) {
		return queryText.contains(selectedFile);
	}

	/**
	 * Update file list in combo viewer
	 */
	private void updateFileList() {
		if (uriPath == null) {
			disableAll();
			return;
		}
		ArrayList<Object> allFiles = new ArrayList<>();

		URI uri = null;
		try {
			ResourceIdentifiers ri = DesignSessionUtil.createRuntimeResourceIdentifiers(getHostResourceIdentifiers());
			uri = ResourceLocatorUtil.resolvePath(ri, uriPath);
		} catch (OdaException e) {
			setMessage(Messages.getString("ui.ExcelFileNotFound") + uri + //$NON-NLS-1$
					"; " + e.getLocalizedMessage()); // $NON-NLS-1$
			disableAll();
			return;
		}
		if (uri == null) {
			setMessage(Messages.getString("ui.ExcelFileNotFound") + uri); //$NON-NLS-1$
			disableAll();
			return;
		}
		try {
			ResourceLocatorUtil.validateFileURI(uri);
			allFiles.add(uri);
		} catch (Exception ignore) {
		}

		if (allFiles.size() > 0) {
			enableListAndViewer();
			selectedFile = allFiles.get(0);

			String extension = ExcelFileReader.getExtensionName(selectedFile);
			if (extension.equals(ExcelODAConstants.XLS_FORMAT) || extension.equals(ExcelODAConstants.XLSX_FORMAT)) {
				setMessage(DEFAULT_MESSAGE);
			} else {
				setMessage(Messages.getString("warning.fileExtensionInvalid"), //$NON-NLS-1$
						ERROR);
			}
			try {
				populateWorkSheetCombo();
				String queryText = getInitializationDesign().getQueryText();
				if (currentSheetName != null && isOldFile(queryText, getFileName(selectedFile))) {
					String[] columnNames = getFileColumnNames(selectedFile);
					availableList.setItems(columnNames);
				}
			} catch (Exception e) {
				setMessage(Messages.getString("ui.ExcelFileNotFound"), //$NON-NLS-1$
						ERROR);
			}
		} else {
			String decodedURIPath = uri.getPath();
			if (decodedURIPath == null) {
				decodedURIPath = uriPath;
			}
			setErrorMessage(Messages.getFormattedString("error.noFile", new Object[] { decodedURIPath })); //$NON-NLS-1$
			disableAll();
		}
	}

	/**
	 * Returns all the column names found in given excelfile.
	 *
	 * @param file
	 * @return
	 */
	private String[] getFileColumnNames(Object file) {
		java.util.List<String[]> propList = getQueryColumnsInfo(
				"select * from " + QueryTextUtil.getQuotedName(getFileName(file)), file, currentSheetName); //$NON-NLS-1$

		String[] result;
		if (propList != null) {
			originalFileColumnsInfoList = new ArrayList<>(propList);
			result = new String[propList.size()];
			for (int i = 0; i < propList.size(); i++) {
				result[i] = ((String[]) propList.get(i))[1];
			}
		} else {
			result = new String[0];
		}

		return result;
	}

	/**
	 *
	 * @param queryText
	 * @param file
	 * @return
	 */
	private java.util.List<String[]> getQueryColumnsInfo(String queryText, Object file, String sheetName) {
		IDriver excelDriver = new Driver();
		IConnection conn = null;
		java.util.List<String[]> columnList = new ArrayList<>();
		try {
			conn = excelDriver.getConnection(null);
			IResultSetMetaData metadata = getResultSetMetaData(queryText, file, conn, sheetName);

			int columnCount = metadata.getColumnCount();
			if (columnCount == 0) {
				return new ArrayList<>();
			}

			for (int i = 0; i < columnCount; i++) {
				String[] result = new String[3];
				result[0] = metadata.getColumnName(i + 1);

				result[1] = getOriginalColumnName(result[0], savedSelectedColumnsInfoString, metadata);

				result[2] = getDataTypeDisplayName(new Integer(metadata.getColumnType(i + 1)));
				columnList.add(result);
			}
			return columnList;
		} catch (OdaException e) {
			setMessage(e.getLocalizedMessage(), ERROR);
			updateExceptionInfo();
			return new ArrayList<>();
		} finally {
			closeConnection(conn);
		}

	}

	private void updateExceptionInfo() {
		if (availableList.getItemCount() == 0) {
			disableAvailableListAndButtons();
		}
	}

	/**
	 *
	 *
	 */
	private void disableAvailableListAndButtons() {
		availableList.setEnabled(false);
		btnAdd.setEnabled(false);
		btnRemove.setEnabled(false);
		btnMoveDown.setEnabled(false);
		btnMoveUp.setEnabled(false);
	}

	/**
	 * get the data type display name
	 *
	 * @param type
	 * @return
	 */
	private String getDataTypeDisplayName(Integer type) {
		if (dataTypeDisplayNameMap.get(type) != null) {
			return (String) dataTypeDisplayNameMap.get(type);
		} else {
			return Messages.getString("datatypes.string"); //$NON-NLS-1$
		}
	}

	/**
	 *
	 * @param displayName
	 * @return
	 */
	private String getDataTypeValue(String displayName) {
		if (dataTypeValueMape.get(displayName) != null) {
			return (String) dataTypeValueMape.get(displayName);
		} else {
			return "STRING"; //$NON-NLS-1$
		}
	}

	/**
	 * Get the original column name in the excelfile of the given column name
	 *
	 * @param name
	 * @param columnsInfo
	 * @param metadata
	 * @return
	 */
	private String getOriginalColumnName(String name, String columnsInfo, IResultSetMetaData metadata) {
		String originalName = null;
		if (columnsInfo.length() != 0) {
			ColumnsInfoUtil ciu = new ColumnsInfoUtil(columnsInfo);
			String[] names = ciu.getColumnNames();
			for (int i = 0; i < names.length; i++) {
				if (name.equals(names[i])) {
					originalName = ciu.getOriginalColumnNames()[i];
					break;
				}
			}
		}

		// if this name was not selected in the viewer
		if (originalName == null) {

			try {
				for (int j = 0; j < metadata.getColumnCount(); j++) {
					if (name.equals(metadata.getColumnName(j + 1))) {
						originalName = name;
					}
				}
			} catch (OdaException e) {
				e.printStackTrace();
			}

		}

		return originalName;
	}

	/**
	 * Attempts to close given ODA connection.
	 *
	 * @param conn
	 */
	private void closeConnection(IConnection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (OdaException e) {
			// ignore
		}
	}

	/**
	 *
	 * @param queryText
	 * @param file
	 * @param conn
	 * @param sheetName
	 * @return
	 * @throws OdaException
	 */
	private IResultSetMetaData getResultSetMetaData(String queryText, Object file, IConnection conn, String sheetName)
			throws OdaException {
		java.util.Properties prop = new java.util.Properties();
		if (file != null) {
			if (file instanceof File) {
				if (((File) file).getParent() == null) {
					throw new OdaException(Messages.getString("error.unexpectedError")); //$NON-NLS-1$
				}
				prop.put(ExcelODAConstants.CONN_FILE_URI_PROP, ((File) file).getAbsolutePath());
			}

			else if (file instanceof URI) {
				prop.put(ExcelODAConstants.CONN_FILE_URI_PROP, file.toString());
			}
		}
		if (inclColumnNameLine != null) {
			prop.put(ExcelODAConstants.CONN_INCLCOLUMNNAME_PROP, inclColumnNameLine);
		}
		if (inclTypeLine != null) {
			prop.put(ExcelODAConstants.CONN_INCLTYPELINE_PROP, inclTypeLine);
		}

		savedSelectedColumnsInfoString = (new QueryTextUtil(queryText)).getColumnsInfo();

		Map<String, Object> appContext = DesignSessionUtil
				.createResourceIdentifiersContext(getHostResourceIdentifiers());
		conn.setAppContext(appContext);

		conn.open(prop);

		IQuery query = conn.newQuery(null);
		query.setProperty(ExcelODAConstants.CONN_WORKSHEETS_PROP, sheetName);
		query.setMaxRows(1);
		query.prepare(queryText);
		query.executeQuery();

		return query.getMetaData();
	}

	/**
	 * Enable all control of this page
	 */
	private void enableListAndViewer() {
		availableList.setEnabled(true);
		selectedColumnsViewer.getTable().setEnabled(true);
	}

	/**
	 * Disable all control of this page
	 */
	private void disableAll() {
		availableList.setEnabled(false);
		selectedColumnsViewer.getTable().setEnabled(false);
		btnAdd.setEnabled(false);
		btnRemove.setEnabled(false);
		btnMoveUp.setEnabled(false);
		btnMoveDown.setEnabled(false);
		setPageComplete(false);
	}

	/**
	 * display the content of the list in the table viewer
	 *
	 * @param list    list that contains the dispay content
	 * @param tViewer the table viewer
	 */
	private void setDisplayContent(java.util.List<String[]> list, TableViewer tViewer) {
		tViewer.getTable().removeAll();
		tViewer.setInput(list);
		tViewer.getTable().select(tViewer.getTable().getTopIndex());
	}

	/**
	 * get the query text(select clause) of this data set
	 *
	 * @return query
	 *
	 */
	private String getQuery() {
		if (selectedColumnsViewer.getTable().getItemCount() == 0) {
			return ""; //$NON-NLS-1$
		}

		String tableName = null;
		StringBuilder buf = new StringBuilder();
		if (selectedFile != null) {
			tableName = getFileName(selectedFile);
		}
		if (tableName != null) {
			tableName = QueryTextUtil.getQuotedName(tableName);
			if (availableList.getItemCount() == 0) {
				buf.append("select * from ").append(tableName); //$NON-NLS-1$
			} else {
				buf.append("select "); //$NON-NLS-1$
				String[] columns = new String[selectedColumnsViewer.getTable().getItemCount()];
				for (int m = 0; m < columns.length; m++) {
					columns[m] = selectedColumnsViewer.getTable().getItem(m).getText(1);
				}

				for (int n = 0; n < columns.length; n++) {
					StringBuilder sb = new StringBuilder();
					char[] columnChars = columns[n].toCharArray();
					for (int i = 0; i < columnChars.length; i++) {
						if (columnChars[i] == '"') {
							sb.append("\\\""); //$NON-NLS-1$
						} else if (columnChars[i] == '\\') {
							sb.append("\\\\"); //$NON-NLS-1$
						} else {
							sb.append(columnChars[i]);
						}
					}

					buf.append(ExcelODAConstants.DELIMITER_DOUBLEQUOTE + sb.toString()
							+ ExcelODAConstants.DELIMITER_DOUBLEQUOTE);
					if (n < columns.length - 1) {
						buf.append(", "); //$NON-NLS-1$
					}
				}
				buf.append(" from ").append(tableName); //$NON-NLS-1$
			}
		}
		return buf.toString();
	}

	/**
	 * Update value of query text
	 *
	 * @param queryText
	 */

	private void updateValuesFromQuery(String queryText) {
		if (queryText.length() == 0) {
			validateHasSelectedColumns();
			return;
		}
		if (selectedFile != null && isOldFile(queryText, getFileName(selectedFile))) {
			updateColumnsFromQuery(queryText, selectedFile);
		}

		if (selectedColumnsViewer.getTable().getItemCount() == 0) {
			setPageComplete(false);
			setMessage(getEmptyColumnErrMsg(), ERROR);
		}
	}

	private boolean validateHasSelectedColumns() {
		// in an editing session with no selected columns
		if (selectedColumnsViewer.getTable().getItemCount() == 0
				&& this.getControl().getShell().getText().startsWith("Edit")) //$NON-NLS-1$
		{
			setPageComplete(false);
			setMessage(getEmptyColumnErrMsg(), ERROR);
			return false;
		}

		return true;
	}

	/**
	 *
	 * @param queryText
	 * @param file
	 */
	private void updateColumnsFromQuery(String queryText, Object file) {
		availableList.setItems(getFileColumnNames(file));
		selectedColumnsViewer.getTable().removeAll();

		savedSelectedColumnsInfoList.clear();

		savedSelectedColumnsInfoList = getQueryColumnsInfo(queryText, file, currentSheetName);

		setDisplayContent(savedSelectedColumnsInfoList, selectedColumnsViewer);

		setPageComplete(true);
		btnAdd.setEnabled(false);
		btnRemove.setEnabled(false);
		btnMoveUp.setEnabled(false);
		btnMoveDown.setEnabled(false);
		if (selectedColumnsViewer.getTable().getItemCount() == 0) {
			setPageComplete(false);
		}
	}

	private void moveUpItem() {
		int count = selectedColumnsViewer.getTable().getItemCount();
		int index = selectedColumnsViewer.getTable().getSelectionIndex();

		if (index > 0 && index < count) {
			if (!btnMoveDown.isEnabled()) {
				btnMoveDown.setEnabled(true);
			}

			String[] obj = savedSelectedColumnsInfoList.get(index);
			savedSelectedColumnsInfoList.set(index, savedSelectedColumnsInfoList.get(index - 1));
			savedSelectedColumnsInfoList.set(index - 1, obj);
			selectedColumnsViewer.refresh();
			selectedColumnsViewer.getTable().setSelection(index - 1);
		}

		if (index == 1) {
			btnMoveUp.setEnabled(false);
		}
	}

	private void moveDownItem() {
		int count = selectedColumnsViewer.getTable().getItemCount();
		int index = selectedColumnsViewer.getTable().getSelectionIndex();

		if (index > -1 && index < count - 1) {
			if (!btnMoveUp.isEnabled()) {
				btnMoveUp.setEnabled(true);
			}

			String[] obj = savedSelectedColumnsInfoList.get(index);
			savedSelectedColumnsInfoList.set(index, savedSelectedColumnsInfoList.get(index + 1));
			savedSelectedColumnsInfoList.set(index + 1, obj);
			selectedColumnsViewer.refresh();
			selectedColumnsViewer.getTable().setSelection(index + 1);
		}

		if (index == count - 2) {
			btnMoveDown.setEnabled(false);
		}
	}

	/**
	 * Add selectd columns
	 */

	private void addColumns() {
		java.util.List<String[]> addedItems = createAddedColumnsInfo(availableList.getSelection());

		for (int i = 0; i < addedItems.size(); i++) {
			savedSelectedColumnsInfoList.add(addedItems.get(i));
		}

		setDisplayContent(savedSelectedColumnsInfoList, selectedColumnsViewer);

		selectedColumnsViewer.getTable().setSelection(selectedColumnsViewer.getTable().getItemCount() - 1);

		int nextSelectionIndex = availableList.getSelectionIndex() + 1;
		if (nextSelectionIndex < availableList.getItemCount()) {
			availableList.setSelection(nextSelectionIndex);
			btnAdd.setEnabled(true);
		} else if (availableList.getSelectionCount() == 0) {
			btnAdd.setEnabled(false);
		}

		selectedColumnsViewer.getTable().setSelection(-1);

		setMessage(DEFAULT_MESSAGE);
		setPageComplete(true);
	}

	/**
	 * create a list of the columns info in accordience to the given column names
	 *
	 * @param addedColumnNames
	 * @return
	 */
	private java.util.List<String[]> createAddedColumnsInfo(String[] addedColumnNames) {
		java.util.List<String[]> addedColumnsInfo = new ArrayList<>();
		int count = 0;

		for (int i = 0; i < addedColumnNames.length; i++) {
			count = getExistenceCount(addedColumnNames[i]);
			String[] addedColumns;
			addedColumns = new String[3];
			if (count == 0) {
				addedColumns[0] = addedColumnNames[i];
			} else {
				addedColumns[0] = addedColumnNames[i] + "_" + count; //$NON-NLS-1$
			}

			addedColumns[1] = addedColumnNames[i];
			addedColumns[2] = getColumnTypeName(addedColumnNames[i]);

			addedColumnsInfo.add(addedColumns);
		}

		return addedColumnsInfo;
	}

	/**
	 *
	 * @param columnName
	 * @return
	 */
	private String getColumnTypeName(String columnName) {
		for (int i = 0; i < originalFileColumnsInfoList.size(); i++) {
			if (columnName.equals(((String[]) originalFileColumnsInfoList.get(i))[1])) {
				return ((String[]) originalFileColumnsInfoList.get(i))[2];
			}
		}
		return null;
	}

	/**
	 * Remove selected columns
	 */
	private void removeColumns() {
		TableItem[] tis = selectedColumnsViewer.getTable().getSelection();
		int index = selectedColumnsViewer.getTable().getSelectionIndex();
		String[] removedColumnInfo = null;

		java.util.List<String[]> removedItems = new ArrayList<>();
		for (int i = 0; i < tis.length; i++) {
			removedColumnInfo = new String[3];
			removedColumnInfo[0] = tis[i].getText(0);
			removedColumnInfo[1] = tis[i].getText(1);
			removedColumnInfo[2] = tis[i].getText(2);
			removedItems.add(removedColumnInfo);
		}

		removeItemsFromSelectedOnes(removedItems);

		selectedColumnsViewer.refresh();

		if (index > 0) {
			selectedColumnsViewer.getTable().setSelection(index - 1);
		} else {
			selectedColumnsViewer.getTable().setSelection(index);
		}

		if (selectedColumnsViewer.getTable().getSelectionCount() == 0) {
			btnRemove.setEnabled(false);
		}

		if (savedSelectedColumnsInfoList.size() <= 1) {
			btnMoveDown.setEnabled(false);
			btnMoveUp.setEnabled(false);
		}

		if (selectedColumnsViewer.getTable().getItemCount() == 0) {
			setPageComplete(false);
			setMessage(getEmptyColumnErrMsg(), ERROR);
		}
	}

	/**
	 * remove the given items from the saved selectedComlumnsInfo
	 *
	 * @param removedItemsList list that contains the given elements that are going
	 *                         to be removed
	 */
	private void removeItemsFromSelectedOnes(java.util.List<String[]> removedItemsList) {
		for (int i = 0; i < removedItemsList.size(); i++) {
			for (int j = 0; j < savedSelectedColumnsInfoList.size(); j++) {
				if (removedItemsList.get(i)[0].equals(savedSelectedColumnsInfoList.get(j)[0])
						&& ((String[]) removedItemsList.get(i))[1]
								.equals(((String[]) savedSelectedColumnsInfoList.get(j))[1])
						&& ((String[]) removedItemsList.get(i))[2]
								.equals(((String[]) savedSelectedColumnsInfoList.get(j))[2])) {
					savedSelectedColumnsInfoList.remove(j);
					break;
				}

			}
		}
	}

	/**
	 * Updates the given dataSetDesign with the query and its metadata defined in
	 * this page.
	 *
	 * @param dataSetDesign
	 */
	private void savePage(DataSetDesign dataSetDesign) {
		String queryText = getQueryText();
		if (queryText.equals(dataSetDesign.getQueryText())) {
			String sheetName = dataSetDesign.getPublicProperties().getProperty(ExcelODAConstants.CONN_WORKSHEETS_PROP);
			if (sheetName != null && sheetName.equals(this.currentSheetName)) {
				return;
			}
		}

		dataSetDesign.setQueryText(queryText);
		savePublicProperties(dataSetDesign);

		if ((selectedFile == null) || !validateHasSelectedColumns()) {
			// don't prepare query; simply reset result set definition
			dataSetDesign.setResultSets(null);
			return;
		}

		// obtain query's result set metadata, and update
		// the dataSetDesign with it
		IConnection conn = null;
		try {
			IDriver excelDriver = new Driver();
			conn = excelDriver.getConnection(null);
			IResultSetMetaData metadata = getResultSetMetaData(queryText, selectedFile, conn, currentSheetName);
			setResultSetMetaData(dataSetDesign, metadata);
		} catch (OdaException e) {
			// no result set definition available, reset in dataSetDesign
			dataSetDesign.setResultSets(null);
		} finally {
			closeConnection(conn);
		}

		/*
		 * Since this excelfile driver does not support query parameters and properties,
		 * there are no data set parameters and public/private properties to specify in
		 * the data set design instance
		 */
	}

	private void savePublicProperties(DataSetDesign dataSetDesign) {
		if (dataSetDesign.getPublicProperties() == null) {
			try {
				String dsID = dataSetDesign.getOdaExtensionDataSourceId();
				String dstID = dataSetDesign.getOdaExtensionDataSetId();
				Properties dsProp = DesignSessionUtil.createDataSetPublicProperties(dsID, dstID, getPageProperties());
				dataSetDesign.setPublicProperties(dsProp);
			} catch (OdaException e) {
				e.printStackTrace();
			}
		}

		else if (dataSetDesign.getPublicProperties() != null) {
			if (dataSetDesign.getPublicProperties().findProperty(ExcelODAConstants.CONN_WORKSHEETS_PROP) != null) {
				dataSetDesign.getPublicProperties().findProperty(ExcelODAConstants.CONN_WORKSHEETS_PROP)
						.setNameValue(ExcelODAConstants.CONN_WORKSHEETS_PROP, currentSheetName);
			}
		}
	}

	private java.util.Properties getPageProperties() {
		java.util.Properties prop = new java.util.Properties();
		if (inclColumnNameLine != null) {
			prop.put(ExcelODAConstants.CONN_INCLCOLUMNNAME_PROP, inclColumnNameLine);
		}
		if (inclTypeLine != null) {
			prop.put(ExcelODAConstants.CONN_INCLTYPELINE_PROP, inclTypeLine);
		}
		if (currentSheetName != null) {
			prop.put(ExcelODAConstants.CONN_WORKSHEETS_PROP, currentSheetName);
		}

		return prop;
	}

	/**
	 * Gets the query text
	 *
	 * @return query text
	 */
	private String getQueryText() {
		String query = getQuery();
		String queryText = query.length() > 0
				? query + ExcelODAConstants.DELIMITER_SPACE + queryTextDelimiter + ExcelODAConstants.DELIMITER_SPACE
						+ columnsInfoStartSymbol + createSelectedColumnsInfoString() + columnsInfoEndSymbol
				: ""; //$NON-NLS-1$
		return queryText;
	}

	/**
	 * create the SelectedColumnsinfo string
	 *
	 * @param dataSetDesign the current dataSetDesign
	 * @return String that contains the seleced columns infomation
	 */
	private String createSelectedColumnsInfoString() {
		StringBuilder prop = new StringBuilder(); // $NON-NLS-1$
		// If the length is equal to 2 then we have a valid query

		for (int i = 0; i < savedSelectedColumnsInfoList.size(); i++) {
			char[] columnNameChars = ((String[]) savedSelectedColumnsInfoList.get(i))[0].toCharArray();
			StringBuilder columnNameBuf = new StringBuilder();

			char[] originalColumnNameChars = ((String[]) savedSelectedColumnsInfoList.get(i))[1].toCharArray();
			StringBuilder originalColumnNameBuf = new StringBuilder();
			for (int m = 0; m < columnNameChars.length; m++) {
				if (ColumnsInfoUtil.isColumnsInfoKeyWord(columnNameChars[m])) {
					columnNameBuf.append("\\" + columnNameChars[m]); //$NON-NLS-1$
				} else {
					columnNameBuf.append(columnNameChars[m]);
				}

			}

			prop.append(ExcelODAConstants.DELIMITER_DOUBLEQUOTE).append(columnNameBuf.toString())
					.append(ExcelODAConstants.DELIMITER_DOUBLEQUOTE).append(ExcelODAConstants.DELIMITER_COMMA_VALUE);

			for (int m = 0; m < originalColumnNameChars.length; m++) {
				if (ColumnsInfoUtil.isColumnsInfoKeyWord(originalColumnNameChars[m])) {
					originalColumnNameBuf.append("\\" //$NON-NLS-1$
							+ originalColumnNameChars[m]);
				} else {
					originalColumnNameBuf.append(originalColumnNameChars[m]);
				}

			}

			prop.append(ExcelODAConstants.DELIMITER_DOUBLEQUOTE).append(originalColumnNameBuf.toString())
					.append(ExcelODAConstants.DELIMITER_DOUBLEQUOTE).append(ExcelODAConstants.DELIMITER_COMMA_VALUE);

			if (i != savedSelectedColumnsInfoList.size() - 1) {
				prop.append(getDataTypeValue(((String[]) savedSelectedColumnsInfoList.get(i))[2]))
						.append(ExcelODAConstants.DELIMITER_SEMICOLON_VALUE);
			} else {
				prop.append(getDataTypeValue(((String[]) savedSelectedColumnsInfoList.get(i))[2]));
			}

		}

		savedSelectedColumnsInfoString = prop.toString();

		return savedSelectedColumnsInfoString;

	}

	/**
	 *
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private void setResultSetMetaData(DataSetDesign dataSetDesign, IResultSetMetaData md) throws OdaException {
		ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign(md);

		ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition();
		// excel file does not support result set name
		resultSetDefn.setResultSetColumns(columns);

		// no exception; go ahead and assign to specified dataSetDesign
		dataSetDesign.setPrimaryResultSet(resultSetDefn);
		dataSetDesign.getResultSets().setDerivedMetaData(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		getControl().setFocus();
	}

	private void populateWorkSheetCombo() throws IOException {
		java.util.List<String> sheetNameList = ExcelFileReader.getSheetNamesInExcelFile(selectedFile);
		worksheetsCombo.setInput(sheetNameList.toArray());

		final String[] firstSheet = new String[1];

		for (String sheet : sheetNameList) {
			if (firstSheet[0] == null) {
				firstSheet[0] = sheet;
			}
			String queryText = getInitializationDesign().getQueryText();
			if (sheet.equals(currentSheetName) && isOldFile(queryText, getFileName(selectedFile))) {
				worksheetsCombo.setSelection(new StructuredSelection(currentSheetName));
			}
		}

		if (worksheetsCombo.getSelection().isEmpty()) {
			this.currentSheetName = null;
		}

		if (currentSheetName == null && worksheetsCombo.getSelection().isEmpty()) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					worksheetsCombo.setSelection(new StructuredSelection(firstSheet[0]));
					currentSheetName = firstSheet[0];
				}
			});
		}

	}

	private Shell shell;

	@Override
	public void createControl(Composite parent) {
		shell = parent.getShell();
		super.createControl(parent);
	}

	@Override
	public Shell getShell() {
		Shell shell = super.getShell();
		if (shell == null) {
			return this.shell;
		} else {
			return shell;
		}
	}

	private java.util.List<IChangeListener> pageStatusChangedListeners = new ArrayList<>();

	public void addPageChangedListener(IChangeListener listener) {
		if (listener != null && !pageStatusChangedListeners.contains(listener)) {
			pageStatusChangedListeners.add(listener);
		}
	}

	public void removePageChangedListener(IChangeListener listener) {
		pageStatusChangedListeners.remove(listener);
	}

	@Override
	public void setPageComplete(boolean complete) {
		super.setPageComplete(complete);
		for (int i = 0; i < pageStatusChangedListeners.size(); i++) {
			pageStatusChangedListeners.get(i).update(true);
		}
	}

	public String getSheetName() {
		return currentSheetName;
	}
}
