/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.mongodb.ui.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.oda.mongodb.ui.i18n.Messages;
import org.eclipse.birt.data.oda.mongodb.ui.util.FieldEntryWrapper;
import org.eclipse.birt.data.oda.mongodb.ui.util.IHelpConstants;
import org.eclipse.birt.data.oda.mongodb.ui.util.UIHelper;
import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.DesignerStateContent;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.birt.data.oda.mongodb.impl.MDbQuery;
import org.eclipse.birt.data.oda.mongodb.impl.MongoDBDriver;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData.DocumentsMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData.FieldMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryModel;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties.CommandOperationType;

public class MongoDBDataSetWizardPage extends DataSetWizardPage {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private boolean modelChanged;
	private ScrolledComposite sComposite;
	private Group commandOpArea;
	private Text DBNameText, docNumText;
	private Combo opTypeCombo, collectionCombo;
	private Button addBtn, addAllBtn, removeBtn, removeAllBtn, upBtn, downBtn;
	private Button sysCollOption, refreshBtn, cmdExprBtn, findFieldsBtn, queryExprBtn, sortExprBtn;
	private TreeViewer availableFieldsViewer;
	private TableViewer selectedFieldsTable;
	private Label opTypeLabel, DBNameLabel;
	private Menu menu;
	private MenuItem menuRemove, menuRemoveAll;

	protected static Image warningImage;

	private List<String> collectionList;
	private List<FieldMetaData> selectedFields, allAvailableFields;

	private DataSetDesign dataSetDesign;
	private String collectionName, oldCollectionName, queryText, cmdExprValue, queryExpr, sortExpr;
	private CommandOperationType opType;
	private int searchLimit;
	private boolean includeSysColl;

	private MDbMetaData metaData;
	private FieldEntryWrapper treeEntry;
	private QueryProperties queryProps;

	private static String DEFAULT_MESSAGE = Messages.getString("MongoDBDataSetWizardPage.message.default"); //$NON-NLS-1$

	private static String DESIGNER_STATE_SEPARATOR = ","; //$NON-NLS-1$

	public MongoDBDataSetWizardPage(String pageName) {
		super(pageName);
		setTitle(pageName);
		setMessage(DEFAULT_MESSAGE);
	}

	/**
	 * Constructor
	 * 
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public MongoDBDataSetWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		setMessage(DEFAULT_MESSAGE);
	}

	public void createPageCustomControl(Composite parent) {
		sComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sComposite.setLayout(new GridLayout());
		sComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		sComposite.setMinWidth(600);
		sComposite.setExpandHorizontal(true);

		Composite mainComposite = new Composite(sComposite, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		GridData gridData = new GridData(GridData.FILL_BOTH);
		mainComposite.setLayoutData(gridData);

		createTopArea(mainComposite);

		createFieldsSelectionArea(mainComposite);

		createBottomArea(mainComposite);

		Point size = mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		mainComposite.setSize(size.x, size.y);

		sComposite.setContent(mainComposite);
		setControl(sComposite);
		setPageComplete(false);

		try {
			initPageInfos();
		} catch (final OdaException e) {
			initializeControl();
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					String errorMsg = UIHelper
							.getUserErrorMessage("MongoDBDataSetWizardPage.MessageDialog.ErrorMessage.InitPage", e); //$NON-NLS-1$
					ExceptionHandler.showException(sComposite.getShell(),
							Messages.getString("MongoDBDataSetWizardPage.MessageDialog.title.GeneralError"), //$NON-NLS-1$
							errorMsg, e);
				}
			});

			return;
		}

		initializeControl();

		resetLabelWidth();

		modelChanged = false;

		UIHelper.setSystemHelp(getControl(), IHelpConstants.CONTEXT_ID_WIZARD_DATASET_MONGODB);
	}

	private void createBottomArea(Composite mainComposite) {
		Composite bottomArea = new Composite(mainComposite, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = 5;
		layout.marginTop = 5;
		layout.horizontalSpacing = 15;
		bottomArea.setLayout(layout);
		bottomArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		queryExprBtn = new Button(bottomArea, SWT.PUSH);
		queryExprBtn.setText(Messages.getString("MongoDBDataSetWizardPage.Button.QueryExpression")); //$NON-NLS-1$
		queryExprBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				MDBQueryExpressionBuilder queryExprDialog = new MDBQueryExpressionBuilder(
						Display.getDefault().getActiveShell());
				queryExprDialog.setExpressionText(queryExpr);
				if (queryExprDialog.open() == Window.OK) {
					String oldQueryExpr = queryExpr;
					queryExpr = queryExprDialog.getExprText();
					if (queryExpr != null && !queryExpr.equals(oldQueryExpr)) {
						modelChanged = true;
						queryProps.setFindQueryExpr(queryExpr);
						validateData();
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		sortExprBtn = new Button(bottomArea, SWT.PUSH);
		sortExprBtn.setText(Messages.getString("MongoDBDataSetWizardPage.Button.SortExpression")); //$NON-NLS-1$
		sortExprBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				MDBSortExpressionBuilder sortExprDialog = new MDBSortExpressionBuilder(
						Display.getDefault().getActiveShell());
				sortExprDialog.setExpressionText(sortExpr);
				if (sortExprDialog.open() == Window.OK) {
					String oldSortExpr = sortExpr;
					sortExpr = sortExprDialog.getExprText();
					if (sortExpr != null && !sortExpr.equals(oldSortExpr)) {
						modelChanged = true;
						queryProps.setSortExpr(sortExpr);
						validateData();
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		Button advancedSettingsBtn = new Button(bottomArea, SWT.PUSH);
		advancedSettingsBtn.setText(Messages.getString("MongoDBDataSetWizardPage.Button.AdvancedSettings")); //$NON-NLS-1$
		advancedSettingsBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {

				MongoDBAdvancedSettingsDialog settingsDialog = new MongoDBAdvancedSettingsDialog(
						Display.getDefault().getActiveShell());

				settingsDialog.initQueryProps(queryProps);

				if (settingsDialog.open() == Window.OK) {
					settingsDialog.updateQueryProperties(queryProps);
					try {
						synchronizeSearchLimit();
					} catch (OdaException e1) {
						handleNoFieldsException(e1);
					}
					modelChanged = true;
					validateData();
				}

			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

	}

	private void synchronizeSearchLimit() throws OdaException {
		if (!queryProps.hasRuntimeMetaDataSearchLimit())
			return;

		int runtimeLimit = queryProps.getRuntimeMetaDataSearchLimit().intValue();
		if (runtimeLimit < this.searchLimit) {
			MessageDialog infoDialog = new MessageDialog(sComposite.getShell(),
					Messages.getString("MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.title"),
					null,
					Messages.getString("MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.message"),
					MessageDialog.INFORMATION,
					new String[] { Messages
							.getString("MongoDBAdvancedSettingsDialog.MessageDialog.synchronizeSearchLimit.button") },
					0);
			if (infoDialog.open() == Window.OK) {
				this.searchLimit = runtimeLimit;
				docNumText.setText(String.valueOf(searchLimit));
				updateAvailableFieldsList();

				refreshAvailableFieldsViewer();
				availableFieldsViewer.expandToLevel(2);

				refreshSelectedFieldsViewer();
				selectedFieldsTable.getTable().deselectAll();
				autoSelectRootItem();
			}
		}
	}

	private void createFieldsSelectionArea(Composite mainComposite) {
		Composite fieldsSelectionArea = new Composite(mainComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginRight = 20;
		layout.horizontalSpacing = 5;
		fieldsSelectionArea.setLayout(layout);
		fieldsSelectionArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createLeftAvailabeFieldsArea(fieldsSelectionArea);

		createCenterBtnOperationArea(fieldsSelectionArea);

		createRightSelectedFieldsArea(fieldsSelectionArea);

	}

	private void createLeftAvailabeFieldsArea(Composite fieldsSelectionArea) {
		Composite leftComposite = new Composite(fieldsSelectionArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		leftComposite.setLayout(layout);
		leftComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(leftComposite, SWT.NONE);
		label.setText(Messages.getString("MongoDBDataSetWizardPage.label.AvailableFields")); //$NON-NLS-1$

		availableFieldsViewer = new TreeViewer(leftComposite, SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 300;
		gd.widthHint = 180;
		availableFieldsViewer.getTree().setLayoutData(gd);

		FieldsTreeProvider provider = new FieldsTreeProvider();
		availableFieldsViewer.setContentProvider(provider);
		availableFieldsViewer.setLabelProvider(provider);

		availableFieldsViewer.getTree().addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

	}

	private void createRightSelectedFieldsArea(Composite fieldsSelectionArea) {
		Composite rightComposite = new Composite(fieldsSelectionArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		rightComposite.setLayout(layout);
		rightComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(rightComposite, SWT.NONE);
		label.setText(Messages.getString("MongoDBDataSetWizardPage.label.SelectedFields")); //$NON-NLS-1$

		selectedFieldsTable = new TableViewer(rightComposite,
				SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 300;
		gd.widthHint = 120;
		selectedFieldsTable.getTable().setLayoutData(gd);

		selectedFieldsTable.getTable().setHeaderVisible(true);

		TableColumn column1 = new TableColumn(selectedFieldsTable.getTable(), SWT.LEFT);
		column1.setResizable(true);
		column1.setWidth(20);

		TableColumn column2 = new TableColumn(selectedFieldsTable.getTable(), SWT.LEFT);
		column2.setText(Messages.getString("MongoDBDataSetWizardPage.HeaderText.SelectedFields"));
		column2.setResizable(true);
		column2.setWidth(180);

		TableProvider tableProvider = new TableProvider();
		selectedFieldsTable.setContentProvider(tableProvider);
		selectedFieldsTable.setLabelProvider(tableProvider);
		selectedFieldsTable.getTable().addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				updateButtons();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		selectedFieldsTable.getTable().addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {

			}

			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					doRemoveSelectedFields();
				}

			}

		});

		createMenuItems();
	}

	private void createMenuItems() {
		menu = new Menu(selectedFieldsTable.getTable());
		menu.addMenuListener(new MenuAdapter() {

			public void menuShown(MenuEvent e) {
				selectedFieldsTable.cancelEditing();
			}
		});
		menuRemove = new MenuItem(menu, SWT.NONE);
		menuRemove.setText(Messages.getString("MongoDBDataSetWizardPage.menuItem.remove")); //$NON-NLS-1$
		menuRemove.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doRemoveSelectedFields();
			}

		});
		menuRemoveAll = new MenuItem(menu, SWT.NONE);
		menuRemoveAll.setText(Messages.getString("MongoDBDataSetWizardPage.menuItem.removeAll")); //$NON-NLS-1$
		menuRemoveAll.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doRemoveAllFieldsFromTablePanel();
			}
		});

		selectedFieldsTable.getTable().setMenu(menu);
	}

	private void createCenterBtnOperationArea(Composite fieldsSelectionArea) {
		Composite btnComposite = new Composite(fieldsSelectionArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 12;
		layout.marginHeight = 60;
		btnComposite.setLayout(layout);
		btnComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		GridData gd = new GridData();
		gd.verticalAlignment = SWT.CENTER;
		gd.widthHint = 36;

		addBtn = new Button(btnComposite, SWT.PUSH);
		addBtn.setText(">"); //$NON-NLS-1$
		addBtn.setLayoutData(gd);
		addBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				doAddSelectedFields(availableFieldsViewer.getTree().getSelection());

				availableFieldsViewer.refresh();
				selectedFieldsTable.refresh();

				deselectAll();
				selectedFieldsTable.getTable().setSelection(selectedFieldsTable.getTable().getItemCount() - 1);
				selectedFieldsTable.getTable().setFocus();

				updateButtons();
				validateData();
				modelChanged = true;
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		addAllBtn = new Button(btnComposite, SWT.PUSH);
		addAllBtn.setText(">>"); //$NON-NLS-1$
		addAllBtn.setLayoutData(gd);
		addAllBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (availableFieldsViewer.getTree().getSelectionCount() <= 0) {
					return;
				}

				if (availableFieldsViewer.getTree().getSelection()[0].getData() instanceof FieldMetaData) {
					doAddAllChildrenField((FieldMetaData) availableFieldsViewer.getTree().getSelection()[0].getData(),
							false);
				} else if (availableFieldsViewer.getTree().getSelection()[0].getData() instanceof FieldEntryWrapper) {
					doAddAllFields();
				}

				updateButtons();
				availableFieldsViewer.refresh();
				selectedFieldsTable.refresh();

				deselectAll();
				selectedFieldsTable.getTable().setSelection(selectedFieldsTable.getTable().getItemCount() - 1);
				selectedFieldsTable.getTable().setFocus();

				updateButtons();
				validateData();
				modelChanged = true;
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		removeBtn = new Button(btnComposite, SWT.PUSH);
		removeBtn.setText("<"); //$NON-NLS-1$
		removeBtn.setLayoutData(gd);
		removeBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				doRemoveSelectedFields();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		removeAllBtn = new Button(btnComposite, SWT.PUSH);
		removeAllBtn.setText("<<"); //$NON-NLS-1$
		removeAllBtn.setLayoutData(gd);
		removeAllBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				doRemoveAllFieldsFromTablePanel();

			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		GridData upBtnGd = new GridData();
		upBtnGd.verticalIndent = 18;
		upBtnGd.widthHint = 36;

		upBtn = new Button(btnComposite, SWT.PUSH);
		upBtn.setText("^"); //$NON-NLS-1$
		upBtn.setLayoutData(upBtnGd);
		upBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (selectedFieldsTable.getTable().getSelectionCount() == 1) {
					int index = selectedFieldsTable.getTable().getSelectionIndex();
					if (index > 0) {
						FieldMetaData field = (FieldMetaData) selectedFields.get(index);
						selectedFields.set(index, selectedFields.get(index - 1));
						selectedFields.set(index - 1, field);
						selectedFieldsTable.refresh();
						selectedFieldsTable.getTable().select(index - 1);
						selectedFieldsTable.getTable().setFocus();
						updateButtons();
						modelChanged = true;
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		downBtn = new Button(btnComposite, SWT.PUSH);
		downBtn.setText("v"); //$NON-NLS-1$
		downBtn.setLayoutData(gd);
		downBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (selectedFieldsTable.getTable().getSelectionCount() == 1) {
					int index = selectedFieldsTable.getTable().getSelectionIndex();
					if (index >= 0 && index < (selectedFieldsTable.getTable().getItemCount() - 1)) {
						FieldMetaData field = (FieldMetaData) selectedFields.get(index);
						selectedFields.set(index, selectedFields.get(index + 1));
						selectedFields.set(index + 1, field);
						selectedFieldsTable.refresh();
						selectedFieldsTable.getTable().select(index + 1);
						selectedFieldsTable.getTable().setFocus();
						updateButtons();
						modelChanged = true;
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

	}

	private void doAddAllChildrenField(FieldMetaData field, boolean includeCurrent) {
		if (includeCurrent || !field.hasChildDocuments()) {
			addSingleField(field);
		}
		if (field.hasChildDocuments()) {
			DocumentsMetaData dmd = field.getChildMetaData();
			for (String name : dmd.getFieldNames()) {
				doAddAllChildrenField(dmd.getFieldMetaData(name), false);
			}
		}
	}

	private void doAddSelectedFields(TreeItem[] items) {
		for (int i = 0; i < items.length; i++) {
			addSingleTreeItem(items[i]);
		}
	}

	private void addSingleTreeItem(TreeItem item) {
		if (item.getData() instanceof FieldMetaData) {
			addSingleField((FieldMetaData) item.getData());
		}
	}

	private void addSingleField(FieldMetaData field) {
		for (int i = 0; i < selectedFields.size(); i++) {
			if (field.getFullDisplayName().equals(selectedFields.get(i).getFullDisplayName())) {
				return;
			}
		}
		selectedFields.add(field);
	}

	private void doAddAllFields() {
		for (int i = 0; i < allAvailableFields.size(); i++) {
			doAddAllChildrenField(allAvailableFields.get(i), false);
		}
	}

	private void doRemoveSelectedFields(TableItem[] items) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].getData() instanceof FieldMetaData) {
				doRemoveSelectedField((FieldMetaData) items[i].getData());
			}
		}
	}

	private void doRemoveSelectedField(FieldMetaData field) {
		this.selectedFields.remove(field);
	}

	private void doRemoveAllFieldsFromTablePanel() {
		doRemoveSelectedFields(selectedFieldsTable.getTable().getItems());

		availableFieldsViewer.refresh();
		selectedFieldsTable.refresh();
		deselectAll();
		validateData();
		updateButtons();
		modelChanged = true;
	}

	private void createTopArea(Composite mainComposite) {
		Composite topArea = new Composite(mainComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginLeft = 5;
		layout.marginRight = 30;
		topArea.setLayout(layout);
		topArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createDBNameArea(topArea);

		createCollectionSelectionArea(topArea);

		createCommandOpArea(topArea);

		createDocNumSettingArea(topArea);

		resetButtonsWidth();

	}

	private void createDBNameArea(Composite topArea) {
		Composite composite = new Composite(topArea, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.horizontalSpacing = 15;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		DBNameLabel = new Label(composite, SWT.NONE);
		DBNameLabel.setText(Messages.getString("MongoDBDataSetWizardPage.label.DBName")); //$NON-NLS-1$

		DBNameText = new Text(composite, SWT.NONE);
		DBNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		DBNameText.setEnabled(false);
	}

	private void resetLabelWidth() {
		int width = opTypeLabel.computeSize(-1, -1).x;
		width = getMaxWidth(sysCollOption, width);
		GridData labelGd = new GridData();
		labelGd.widthHint = width;

		DBNameLabel.setLayoutData(labelGd);
		opTypeLabel.setLayoutData(labelGd);
		sysCollOption.setLayoutData(labelGd);
	}

	private void resetButtonsWidth() {
		int width = getMaxWidth(refreshBtn, 60);
		width = getMaxWidth(cmdExprBtn, width);
		width = getMaxWidth(findFieldsBtn, width) + 10;

		GridData btnGd = new GridData();
		btnGd.widthHint = width;

		cmdExprBtn.setLayoutData(btnGd);
		refreshBtn.setLayoutData(btnGd);
		findFieldsBtn.setLayoutData(btnGd);

	}

	private int getMaxWidth(Control control, int width) {
		int size = control.computeSize(-1, -1).x;
		return size > width ? size : width;
	}

	private void createDocNumSettingArea(Composite topArea) {
		Composite composite = new Composite(topArea, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 12;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label docNumLabel = new Label(composite, SWT.NONE);
		docNumLabel.setText(Messages.getString("MongoDBDataSetWizardPage.label.DocumentNumber")); //$NON-NLS-1$

		docNumText = new Text(composite, SWT.BORDER);
		GridData txtGd = new GridData(GridData.FILL_HORIZONTAL);
		docNumText.setLayoutData(txtGd);
		docNumText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (isNumber(docNumText.getText().trim())) {
					searchLimit = Integer.parseInt(docNumText.getText().trim());
					modelChanged = true;
				}
				validateData();
			}

		});

		findFieldsBtn = new Button(composite, SWT.PUSH);
		findFieldsBtn.setText(Messages.getString("MongoDBDataSetWizardPage.Button.FindFields")); //$NON-NLS-1$
		findFieldsBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				try {
					updateAvailableFieldsList();

					refreshAvailableFieldsViewer();
					availableFieldsViewer.expandToLevel(2);

					refreshSelectedFieldsViewer();
					selectedFieldsTable.getTable().deselectAll();
					autoSelectRootItem();
				} catch (OdaException ex) {
					handleNoFieldsException(ex);
				}

				validateData();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});
	}

	private void createCollectionSelectionArea(Composite topArea) {
		Group collGroup = new Group(topArea, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 12;
		collGroup.setLayout(layout);
		collGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		collGroup.setText(Messages.getString("MongoDBDataSetWizardPage.label.DBCollection")); //$NON-NLS-1$

		sysCollOption = new Button(collGroup, SWT.CHECK);
		sysCollOption.setText(Messages.getString("MongoDBDataSetWizardPage.Button.text.IncludeSystemCollection")); //$NON-NLS-1$

		sysCollOption.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				includeSysColl = sysCollOption.getSelection();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

		collectionCombo = new Combo(collGroup, SWT.BORDER);
		collectionCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		collectionCombo.setVisibleItemCount(20);

		collectionSelectionListener = new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (selectedFields.size() == 0) {
					if (!collectionCombo.getText().equals(oldCollectionName)) {
						modelChanged = true;

						collectionName = collectionCombo.getText().trim();
						oldCollectionName = collectionName;

						resetOpTypeComboItems();
						doUpdateAvailableFieldsArea();

						validateData();
					}

				} else if (!collectionCombo.getText().equals(oldCollectionName)) {
					if (MessageDialog.open(MessageDialog.QUESTION, getShell(),
							Messages.getString("MongoDBDataSetWizardPage.MessageBox.title.PromptToKeepSelections"), //$NON-NLS-1$
							Messages.getString("MongoDBDataSetWizardPage.MessageBox.message.PromptToKeepSelections"), //$NON-NLS-1$
							SWT.NONE)) {
						selectedFields.clear();
						modelChanged = true;

						collectionName = collectionCombo.getText().trim();
						resetOpTypeComboItems();

						doUpdateAvailableFieldsArea();

						validateData();
					} else {
						collectionCombo.setText(oldCollectionName);
					}
				}

				resetExprBtnStatus();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		};

		collectionModifyListener = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				oldCollectionName = collectionName;
				collectionName = collectionCombo.getText().trim();

				resetOpTypeComboItems();

				modelChanged = true;
				resetExprBtnStatus();

				validateData();
			}

		};
		addCollectionComboListeners();

		refreshBtn = new Button(collGroup, SWT.PUSH);
		refreshBtn.setText(Messages.getString("MongoDBDataSetWizardPage.Button.Refresh")); //$NON-NLS-1$
		refreshBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

					public void run() {
						collectionList = metaData.getCollectionsList(!includeSysColl);
					}
				});

				resetCollectionComboItems();
				collectionName = collectionCombo.getText().trim();
				resetOpTypeComboItems();
				validateData();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

	}

	private void resetCollectionComboItems() {
		removeCollectionComboListeners();

		oldCollectionName = collectionCombo.getText().trim();
		collectionCombo.removeAll(); // clear the collection combo items list

		for (int i = 0; i < collectionList.size(); i++) {
			collectionCombo.add(collectionList.get(i));
		}

		if (collectionCombo.indexOf(oldCollectionName) >= 0) {
			collectionCombo.setText(oldCollectionName);
		} else {
			collectionCombo.setText(EMPTY_STRING);
		}

		addCollectionComboListeners();
	}

	private void addCollectionComboListeners() {
		Listener[] modifyListeners = collectionCombo.getListeners(SWT.Modify);
		if (modifyListeners == null || modifyListeners.length == 0) {
			collectionCombo.addModifyListener(collectionModifyListener);
		}
		Listener[] selectionListeners = collectionCombo.getListeners(SWT.Selection);
		if (selectionListeners == null || selectionListeners.length == 0) {
			collectionCombo.addSelectionListener(collectionSelectionListener);
		}
	}

	private void removeCollectionComboListeners() {
		Listener[] modifyListeners = collectionCombo.getListeners(SWT.Modify);
		for (int i = 0; i < modifyListeners.length; i++) {
			collectionCombo.removeListener(SWT.Modify, modifyListeners[i]);
		}

		Listener[] selectionListeners = collectionCombo.getListeners(SWT.Selection);
		for (int i = 0; i < selectionListeners.length; i++) {
			collectionCombo.removeListener(SWT.Selection, selectionListeners[i]);
		}
	}

	private void createCommandOpArea(Composite topArea) {
		commandOpArea = new Group(topArea, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 12;
		commandOpArea.setLayout(layout);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 3;
		commandOpArea.setLayoutData(layoutData);

		commandOpArea.setText(Messages.getString("MongoDBDataSetWizardPage.group.text")); //$NON-NLS-1$

		opTypeLabel = new Label(commandOpArea, SWT.NONE);
		opTypeLabel.setText(Messages.getString("MongoDBDataSetWizardPage.label.OperationType")); //$NON-NLS-1$

		opTypeCombo = new Combo(commandOpArea, SWT.BORDER | SWT.READ_ONLY);
		opTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		opTypeSelectionListener = new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (opType != null && opType.displayName().trim().equals(opTypeCombo.getText().trim())) {
					return;
				}

				doOpTypeChanged();
				handleCommandOperationSelection();

				if (opTypeCombo.getText().trim().length() == 0 && collectionCombo.getText().trim().length() > 0
						&& MessageDialog.open(MessageDialog.QUESTION, getShell(),
								Messages.getString(
										"MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields"), //$NON-NLS-1$
								Messages.getString(
										"MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields"), //$NON-NLS-1$
								SWT.NONE)) {
					doUpdateAvailableFieldsArea();
				}

				modelChanged = true;
				validateData();
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		};
		opTypeCombo.addSelectionListener(opTypeSelectionListener);

		cmdExprBtn = new Button(commandOpArea, SWT.PUSH);
		cmdExprBtn.setText(Messages.getString("MongoDBDataSetWizardPage.Button.text.CommandExpression")); //$NON-NLS-1$
		cmdExprBtn.setToolTipText(Messages.getString("MongoDBDataSetWizardPage.Button.tooltip.CommandExpression")); //$NON-NLS-1$
		cmdExprBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				MDBCommandExpressionBuilder cmdExprDialog = new MDBCommandExpressionBuilder(
						Display.getDefault().getActiveShell(), opType);
				cmdExprDialog.setExpressionText(cmdExprValue);
				if (cmdExprDialog.open() == Window.OK) {
					String oldCmdExpr = cmdExprValue;
					cmdExprValue = cmdExprDialog.getExprText();
					if (cmdExprValue != null && !cmdExprValue.equals(oldCmdExpr)) {
						modelChanged = true;
						queryProps.setOperationExpression(cmdExprValue);
						if (UIHelper.isEmptyString(collectionName) && allAvailableFields.size() == 0) {
							doUpdateAvailableFieldsArea();
						} else if (MessageDialog.open(MessageDialog.QUESTION, getShell(),
								Messages.getString(
										"MongoDBDataSetWizardPage.MessageBox.title.PromptToRefreshAvailableFields"), //$NON-NLS-1$
								Messages.getString(
										"MongoDBDataSetWizardPage.MessageBox.message.PromptToRefreshAvailableFields"), //$NON-NLS-1$
								SWT.NONE)) {
							doUpdateAvailableFieldsArea();
						}
						validateData();
					}

				}

			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}

		});

	}

	private void resetOpTypeComboItems() {
		String type = opType == null ? "" : opType.displayName(); //$NON-NLS-1$
		if (UIHelper.isEmptyString(this.collectionName)) {
			opTypeCombo.setItems(new String[] { EMPTY_STRING, CommandOperationType.RUN_DB_COMMAND.displayName() });
		} else {
			opTypeCombo.setItems(new String[] { EMPTY_STRING, CommandOperationType.AGGREGATE.displayName(),
					CommandOperationType.MAP_REDUCE.displayName(), CommandOperationType.RUN_DB_COMMAND.displayName() });
		}
		opTypeCombo.getItems();
		opTypeCombo.setText(type);
		doOpTypeChanged();

	}

	private void initPageInfos() throws OdaException {

		// Restores the last saved data set design
		dataSetDesign = getInitializationDesign();
		if (dataSetDesign == null)
			return; // nothing to initialize

		queryText = dataSetDesign.getQueryText();
		if (queryText == null)
			return; // nothing to initialize

		collectionList = new ArrayList<String>();
		selectedFields = new ArrayList<FieldMetaData>();
		allAvailableFields = new ArrayList<FieldMetaData>();

		queryProps = QueryProperties.deserialize(queryText);

		if (queryProps != null) {
			collectionName = queryProps.getCollectionName();
			cmdExprValue = queryProps.getOperationExpression();
			opType = queryProps.getOperationType();
			queryExpr = queryProps.getFindQueryExpr();
			sortExpr = queryProps.getSortExpr();
		}

		searchLimit = MDbMetaData.DEFAULT_META_DATA_SEARCH_LIMIT;
		this.includeSysColl = false;

		DesignerState odaState = getInitializationDesignerState();
		if (odaState != null) {
			String value = odaState.getStateContent().getStateContentAsString();
			String[] splits = value.split(DESIGNER_STATE_SEPARATOR);
			if (splits.length == 2) {
				this.includeSysColl = Boolean.parseBoolean(splits[0]);
				this.searchLimit = Integer.parseInt(splits[1]);
			}
		}

		java.util.Properties connProps = DesignSessionUtil
				.getEffectiveDataSourceProperties(getInitializationDesign().getDataSourceDesign());
		metaData = new MDbMetaData(connProps);
		collectionList = metaData.getCollectionsList(!includeSysColl);

	}

	private void initializeSelectedFields() {
		if (queryProps == null)
			return;

		List<String> selectedFieldNames = queryProps.getSelectedFieldNames();
		for (int i = 0; i < selectedFieldNames.size(); i++) {
			String fieldName = selectedFieldNames.get(i);
			if (UIHelper.isEmptyString(fieldName)) {
				continue;
			}
			for (int j = 0; j < allAvailableFields.size(); j++) {
				if (fieldName.equals(allAvailableFields.get(j).getFullName())) {
					this.selectedFields.add(allAvailableFields.get(j));
					break;
				}
			}
		}
	}

	private void initializeControl() {
		if (metaData != null && metaData.getDatabaseName() != null) {
			DBNameText.setText(metaData.getDatabaseName());
		}

		sysCollOption.setSelection(includeSysColl);

		removeCollectionComboListeners();
		opTypeCombo.removeSelectionListener(opTypeSelectionListener);

		resetCollectionComboItems();
		collectionCombo.setText(collectionName == null ? EMPTY_STRING : collectionName);

		resetOpTypeComboItems();

		opTypeCombo.setText(opType == null ? EMPTY_STRING : opType.displayName());
		handleCommandOperationSelection();
		resetOpTypeComboItems();

		docNumText.setText(String.valueOf(searchLimit));

		try {
			if (CommandOperationType.RUN_DB_COMMAND.name().equals(opType.name())
					|| !UIHelper.isEmptyString(collectionName)) {
				updateAvailableFieldsList();
			}
		} catch (final OdaException ex) {
			handleNoFieldsException(ex);
		} finally {
			initializeSelectedFields();

			refreshAvailableFieldsViewer();
			refreshSelectedFieldsViewer();

			availableFieldsViewer.getTree().setFocus();
			availableFieldsViewer.expandToLevel(2);

			updateButtons();
			validateData();
		}

		try {
			warningImage = UIHelper.getEmbeddedWarningImage();
		} catch (IOException e) {
			ExceptionHandler.showException(getShell(),
					Messages.getString("MongoDBDataSetWizardPage.MessageDialog.title.GeneralError"), //$NON-NLS-1$
					e.getMessage(), e);
		}

		addCollectionComboListeners();
		opTypeCombo.addSelectionListener(opTypeSelectionListener);
	}

	private void refreshAvailableFieldsViewer() {
		if (metaData == null)
			return;

		if (CommandOperationType.RUN_DB_COMMAND.name().equals(opType.name())
				|| !UIHelper.isEmptyString(collectionName)) {
			if (treeEntry == null) {
				treeEntry = new FieldEntryWrapper(collectionName, metaData, searchLimit, queryProps);
				availableFieldsViewer.setInput(new FieldEntryWrapper[] { treeEntry });
			} else if (availableFieldsViewer.getInput() == null) {
				treeEntry.setCollectionName(collectionName);
				treeEntry.setMetaData(metaData);
				availableFieldsViewer.setInput(new FieldEntryWrapper[] { treeEntry });
			} else {
				treeEntry.setCollectionName(collectionName);
				treeEntry.setMetaData(metaData);
				availableFieldsViewer.refresh();
			}
		}
	}

	private void refreshSelectedFieldsViewer() {
		selectedFieldsTable.setInput(selectedFields);
		selectedFieldsTable.refresh();
	}

	private void updateAvailableFieldsList() throws OdaException {
		allAvailableFields.clear();

		if (metaData == null) {
			throw new OdaException(Messages.getString("MongoDBDataSetWizardPage.ExceptionDialog.message.NoMetaData")); //$NON-NLS-1$
		}

		BusyIndicator.showWhile(Display.getDefault(), new Runnable() {

			public void run() {
				try {
					if (treeEntry == null) {
						treeEntry = new FieldEntryWrapper(collectionName, metaData, searchLimit, queryProps);
					} else {
						treeEntry.setCollectionName(collectionName);
						treeEntry.setSearchLimit(searchLimit);
						treeEntry.setQueryProps(queryProps);
					}

					treeEntry.updateAvailableFields();
					DocumentsMetaData dmd = treeEntry.getAvailableFields();
					addChildrenFieldsByDocument(dmd);
				} catch (Exception e) {
					handleNoFieldsException(e);
				}
			}
		});

	}

	private void addChildrenFieldsByDocument(DocumentsMetaData dmd) {
		if (dmd == null)
			return;

		for (String name : dmd.getFieldNames()) {
			FieldMetaData field = dmd.getFieldMetaData(name);
			allAvailableFields.add(field);
			if (field.hasChildDocuments()) {
				addChildrenFieldsByDocument(field.getChildMetaData());
			}
		}
	}

	private void updateButtons() {
		updateOperationButtons();
		updateMenuItems();
	}

	private void updateOperationButtons() {
		if (availableFieldsViewer.getTree().isFocusControl()) {
			if (availableFieldsViewer.getTree().getSelectionCount() > 0
					&& (availableFieldsViewer.getTree().getSelection()[0].getData() instanceof FieldEntryWrapper)) {
				addBtn.setEnabled(false);
			} else {
				addBtn.setEnabled(availableFieldsViewer.getTree().getSelectionCount() > 0
						&& !containsSelectedField(availableFieldsViewer.getTree().getSelection()));
			}
			addAllBtn.setEnabled(selectedFields.size() < allAvailableFields.size()
					&& containsUnselectedItem(availableFieldsViewer.getTree().getSelection()));
			removeBtn.setEnabled(false);
			upBtn.setEnabled(false);
			downBtn.setEnabled(false);
		} else if (selectedFieldsTable.getTable().isFocusControl()) {
			addBtn.setEnabled(false);
			addAllBtn.setEnabled(false);
			removeBtn.setEnabled(selectedFieldsTable.getTable().getSelectionCount() > 0);

			if (selectedFieldsTable.getTable().getSelectionCount() == 1) {
				int index = selectedFieldsTable.getTable().getSelectionIndex();
				int count = selectedFieldsTable.getTable().getItemCount();
				upBtn.setEnabled(index > 0 && index < count);
				downBtn.setEnabled(index >= 0 && index < (count - 1));
			} else {
				upBtn.setEnabled(false);
				downBtn.setEnabled(false);
			}
		} else {
			addBtn.setEnabled(false);
			addAllBtn.setEnabled(false);
			removeBtn.setEnabled(false);
			upBtn.setEnabled(false);
			downBtn.setEnabled(false);
		}

		removeAllBtn.setEnabled(selectedFields.size() > 0);
	}

	private void updateMenuItems() {
		if (selectedFieldsTable.getTable().getItemCount() == 0) {
			if (menu != null && !menu.isDisposed()) {
				menu.dispose();
				menuRemove.dispose();
				menuRemoveAll.dispose();
			}
		} else {
			createMenuItems();
			menuRemove.setEnabled(selectedFieldsTable.getTable().getSelection().length > 0);
			menuRemoveAll.setEnabled(selectedFieldsTable.getTable().getItemCount() > 0);
		}
	}

	private boolean containsSelectedField(TreeItem[] items) {
		for (int i = 0; i < items.length; i++) {
			if (isSelectedField(items[i])) {
				return true;
			}
		}
		return false;
	}

	private boolean isSelectedField(TreeItem item) {
		if (item.getData() instanceof FieldMetaData) {
			return isSelectedField(((FieldMetaData) item.getData()).getFullDisplayName());
		}
		return false;
	}

	private boolean isSelectedField(String filedName) {
		for (int i = 0; i < selectedFields.size(); i++) {
			if (filedName.equals(selectedFields.get(i).getFullDisplayName())) {
				return true;
			}
		}
		return false;
	}

	private boolean containsUnselectedItem(TreeItem[] items) {
		if (items.length > 0 && items[0].getData() instanceof FieldEntryWrapper) {
			return containsUnselectedItem(items[0].getItems(), true);
		} else {
			return containsUnselectedItem(items, false);
		}
	}

	private boolean containsUnselectedItem(TreeItem[] items, boolean includeCurrent) {
		for (int i = 0; i < items.length; i++) {
			if (items[i].getData() instanceof FieldMetaData) {
				FieldMetaData field = (FieldMetaData) items[i].getData();
				if (includeCurrent) {
					if (field.hasChildDocuments()) {
						if (!allChildrenSelected(field, false)) {
							return true;
						}
					} else if (!isSelectedField(field.getFullDisplayName())) {
						return true;
					}
				} else if (field.hasChildDocuments() && !allChildrenSelected(field, includeCurrent)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean allChildrenSelected(FieldMetaData fieldMataData, boolean includeCurrent) {
		if (fieldMataData.hasChildDocuments()) {
			DocumentsMetaData dmd = fieldMataData.getChildMetaData();
			for (String name : dmd.getFieldNames()) {
				FieldMetaData field = dmd.getFieldMetaData(name);
				if (!allChildrenSelected(field, false)) {
					return false;
				}
			}
		}
		if (includeCurrent || !fieldMataData.hasChildDocuments()) {
			return isSelectedField(fieldMataData.getFullDisplayName());
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.
	 * DataSetDesign)
	 */
	protected DataSetDesign collectDataSetDesign(DataSetDesign design) {
		// page control was never created or no change has taken place
		if (getControl() == null || !modelChanged) {
			return design; // no editing was done
		}

		if (hasValidData()) {
			savePage(design);
		}

		return design;
	}

	/**
	 * Validates the user-defined value in the page control exists and not a blank
	 * text. Set page message accordingly.
	 */
	private boolean validateData() {
		boolean isValid = true;

		if (!CommandOperationType.RUN_DB_COMMAND.name().equals(opType.name())
				&& UIHelper.isEmptyString(collectionName)) {
			isValid = false;
			setMessage(Messages.getString("MongoDBDataSetWizardPage.message.error.MissingCollectionName"), //$NON-NLS-1$
					ERROR);
		} else if (!isNumber(docNumText.getText().trim())) {
			isValid = false;
			setMessage(Messages.getString("MongoDBDataSetWizardPage.message.error.InvalidDocumentNumber"), //$NON-NLS-1$
					ERROR);
		} else if (!checkAllSelectedFields()) {
			isValid = false;
		} else if (!checkCommandExpression()) {
			isValid = false;
		} else if (this.selectedFields.size() == 0) {
			isValid = false;
			setMessage(Messages.getString("MongoDBDataSetWizardPage.message.error.MissingSelectedField"), //$NON-NLS-1$
					ERROR);
		}

		if (isValid)
			setMessage(DEFAULT_MESSAGE);

		setPageComplete(isValid);

		return isValid;
	}

	/**
	 * Indicates whether the custom page has valid data to proceed with defining a
	 * data set.
	 */
	private boolean hasValidData() {
		validateData();

		return canLeave();
	}

	private void savePage(DataSetDesign design) {
		IDriver driver = new MongoDBDriver();
		IConnection customConn = null;
		try {
			customConn = driver.getConnection(null);
			java.util.Properties connProps = DesignSessionUtil
					.getEffectiveDataSourceProperties(getInitializationDesign().getDataSourceDesign());
			customConn.open(connProps);
			updateDesign(design, customConn);
		} catch (Exception e) {
			handleNoFieldsException(e);
		} finally {
			closeConnection(customConn);
		}
	}

	/**
	 * Updates the given dataSetDesign with the queryText and its derived metadata
	 * obtained from the ODA runtime connection.
	 */
	private void updateDesign(DataSetDesign dataSetDesign, IConnection conn) throws OdaException {
		// save user-defined query properties in query text
		queryProps.setSelectedFields(selectedFields);
		queryProps.setCollectionName(collectionName);

		DesignFactory factory = DesignFactory.eINSTANCE;
		DesignerState odaState = factory.createDesignerState();
		DesignerStateContent content = factory.createDesignerStateContent();

		String value = String.valueOf(this.includeSysColl) + DESIGNER_STATE_SEPARATOR
				+ String.valueOf(this.searchLimit);
		content.setStateContentAsString(value);

		odaState.setStateContent(content);
		odaState.setVersion("1.0"); //$NON-NLS-1$

		setResponseDesignerState(odaState);

		String queryPropsText = queryProps.serialize();
		dataSetDesign.setQueryText(queryPropsText);

		// prepare the query text to get result set meta data
		MDbQuery query = (MDbQuery) conn.newQuery(MDbQuery.ODA_DATA_SET_ID);
		query.prepare(queryPropsText);

		try {
			// apply design-time search limit to collect result set metadata
			query.setMetaDataSearchLimit(searchLimit);
			IResultSetMetaData md = query.getMetaData();
			updateResultSetDesign(md, dataSetDesign);
		} catch (OdaException e) {
			// no result set definition available, reset previous derived
			// metadata
			dataSetDesign.setResultSets(null);

			String errorMsg = UIHelper.getUserErrorMessage("MongoDBDataSetWizardPage.ExceptionDialog.message.SavaPage", //$NON-NLS-1$
					e);
			ExceptionHandler.showException(sComposite.getShell(),
					Messages.getString("MongoDBDataSetWizardPage.ExceptionDialog.title"), errorMsg, e); //$NON-NLS-1$
			setPageComplete(false);
			// e.printStackTrace( );
		}

		// no parameter support in mongoDB ODA
	}

	/**
	 * Updates the specified data set design's result set definition based on the
	 * specified runtime metadata.
	 * 
	 * @param md            runtime result set metadata instance
	 * @param dataSetDesign data set design instance to update
	 * @throws OdaException
	 */
	private void updateResultSetDesign(IResultSetMetaData md, DataSetDesign dataSetDesign) throws OdaException {
		ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign(md);

		ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition();
		// resultSetDefn.setName( value ); // result set name
		resultSetDefn.setResultSetColumns(columns);

		// no exception in conversion; go ahead and assign to specified
		// dataSetDesign
		dataSetDesign.setPrimaryResultSet(resultSetDefn);
		dataSetDesign.getResultSets().setDerivedMetaData(true);
	}

	/**
	 * Attempts to close given ODA connection.
	 */
	private void closeConnection(IConnection conn) {
		try {
			if (conn != null && conn.isOpen())
				conn.close();
		} catch (OdaException e) {
			// ignore
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #collectResponseState()
	 */
	protected void collectResponseState() {
		super.collectResponseState();
		/*
		 * To optionally assign a custom response state, for inclusion in the ODA design
		 * session response, use setResponseSessionStatus( SessionStatus status );
		 * setResponseDesignerState( DesignerState customState );
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #canLeave()
	 */
	protected boolean canLeave() {
		int exprType = 1;
		try {
			// validate the command expression
			if (opType != CommandOperationType.DYNAMIC_QUERY) {
				exprType = 1;
				QueryModel.validateCommandSyntax(opType, cmdExprValue);
			}

			if (opType == CommandOperationType.DYNAMIC_QUERY || opType == CommandOperationType.MAP_REDUCE) {
				// validate the query expression, if exists
				if (!UIHelper.isEmptyString(queryExpr)) {
					exprType = 2;
					QueryModel.validateQuerySyntax(queryExpr);
				}

				// validate the sort expression, if exists
				if (!UIHelper.isEmptyString(sortExpr)) {
					exprType = 3;
					QueryModel.validateSortExprSyntax(sortExpr);
				}
			}
		} catch (OdaException e) {
			String usrMsgKey = null;
			switch (exprType) {
			case 1:
				usrMsgKey = "MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidCommandExpression"; //$NON-NLS-1$
				break;
			case 2:
				usrMsgKey = "MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidQueryExpression"; //$NON-NLS-1$
				break;
			case 3:
				usrMsgKey = "MongoDBDataSetWizardPage.ExceptionDialog.message.InvalidSortExpression"; //$NON-NLS-1$
				break;
			}
			String errorMsg = UIHelper.getUserErrorMessage(usrMsgKey, e);
			ExceptionHandler.showException(Display.getDefault().getActiveShell(),
					Messages.getString("MongoDBDataSetWizardPage.ExceptionDialog.title"), errorMsg, e); //$NON-NLS-1$
			setMessage(Messages.getString(usrMsgKey), ERROR);
			setPageComplete(false);
			return false;
		}

		return isPageComplete();
	}

	/**
	 * Test the text to see if it can be parsed to an integer.
	 * 
	 * @param text
	 * @return
	 */
	private boolean isNumber(String text) {
		if (UIHelper.isEmptyString(text)) {
			return false;
		}

		return text.matches("^[0-9]*[1-9][0-9]*$"); //$NON-NLS-1$
	}

	private boolean existsField(FieldMetaData field) {
		if (field == null)
			return false;

		for (int i = 0; i < allAvailableFields.size(); i++) {
			if (field.equals(allAvailableFields.get(i))
					|| field.getFullDisplayName().equals(allAvailableFields.get(i).getFullDisplayName())) {
				return true;
			}
		}
		return false;
	}

	private boolean checkAllSelectedFields() {
		for (int i = 0; i < selectedFields.size(); i++) {
			if (!existsField(selectedFields.get(i))) {
				setMessage(
						Messages.getFormattedString("MongoDBDataSetWizardPage.message.error.SelectedFieldNotFound", //$NON-NLS-1$
								new Object[] { selectedFields.get(i).getFullDisplayName() }), ERROR);
				return false;
			}
		}
		return true;
	}

	private boolean checkCommandExpression() {
		if (opType.displayName().trim().length() > 0) {
			if (cmdExprValue == null || cmdExprValue.trim().length() == 0) {
				setMessage(Messages.getString("MongoDBDataSetWizardPage.message.error.MissingCommandExpression"), //$NON-NLS-1$
						ERROR);
				return false;
			}
		}
		return true;
	}

	private void handleCommandOperationSelection() {
		cmdExprBtn.setEnabled(opType.displayName().trim().length() > 0);
		boolean collectionAreaEnabled = opType == null
				|| !CommandOperationType.RUN_DB_COMMAND.name().equals(opType.name());
		sysCollOption.setEnabled(collectionAreaEnabled);
		refreshBtn.setEnabled(collectionAreaEnabled);
		resetExprBtnStatus();

		if (collectionCombo.getEnabled() != collectionAreaEnabled) {
			collectionCombo.setEnabled(collectionAreaEnabled);
			if (collectionAreaEnabled) {
				collectionCombo.setText(oldCollectionName);
			} else {
				oldCollectionName = collectionName;
				collectionName = ""; //$NON-NLS-1$
			}
		}
	}

	private void resetExprBtnStatus() {
		boolean queryExprBtnEnabled = !CommandOperationType.AGGREGATE.name().equals(opType.name())
				&& !CommandOperationType.RUN_DB_COMMAND.name().equals(opType.name());
		boolean sortExprBtnEnabled = queryExprBtnEnabled;
		queryExprBtn.setEnabled(queryExprBtnEnabled);
		// now Sort Expression Button is enabled in the same case with the Query
		// Expression Button
		sortExprBtn.setEnabled(sortExprBtnEnabled);
	}

	private void deselectAll() {
		availableFieldsViewer.getTree().deselectAll();
		selectedFieldsTable.getTable().deselectAll();
	}

	private void autoSelectRootItem() {
		availableFieldsViewer.getTree().setFocus();
		if (availableFieldsViewer.getTree().getTopItem() != null) {
			availableFieldsViewer.getTree().select(availableFieldsViewer.getTree().getTopItem());
		}
		updateButtons();
	}

	private void doUpdateAvailableFieldsArea() {
		try {
			updateAvailableFieldsList();

			refreshAvailableFieldsViewer();
			availableFieldsViewer.expandToLevel(2);
			refreshSelectedFieldsViewer();

			autoSelectRootItem();
		} catch (OdaException ex) {
			availableFieldsViewer.refresh();
			refreshSelectedFieldsViewer();

		}

	}

	boolean flag = false;
	private SelectionListener collectionSelectionListener;
	private ModifyListener collectionModifyListener;
	private SelectionListener opTypeSelectionListener;

	private void handleNoFieldsException(final Exception e) {
		if (!flag) {
			flag = true;
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					String errorMsg = UIHelper
							.getUserErrorMessage("MongoDBDataSetWizardPage.ExceptionDialog.message.FindFields", e); //$NON-NLS-1$
					ExceptionHandler.showException(Display.getDefault().getActiveShell(),
							Messages.getString("MongoDBDataSetWizardPage.ExceptionDialog.title"), errorMsg, e); //$NON-NLS-1$
					flag = false;
				}
			});
		}
	}

	private void doOpTypeChanged() {
		opType = CommandOperationType.getType(opTypeCombo.getText().trim());
		queryProps.setOperationType(opType);
	}

	private void doRemoveSelectedFields() {
		int[] indices = selectedFieldsTable.getTable().getSelectionIndices();
		if (indices.length == 0)
			return;

		int lastIndex = indices[indices.length - 1];
		int focusIndex = lastIndex - indices.length + 1;

		doRemoveSelectedFields(selectedFieldsTable.getTable().getSelection());
		availableFieldsViewer.refresh();
		selectedFieldsTable.refresh();

		deselectAll();
		int tableFieldsCount = selectedFieldsTable.getTable().getItemCount();
		if (tableFieldsCount > 0) {
			selectedFieldsTable.getTable().setFocus();
			if (focusIndex < 0) {
				selectedFieldsTable.getTable().setSelection(0);
			} else if (focusIndex >= tableFieldsCount) {
				selectedFieldsTable.getTable().setSelection(tableFieldsCount - 1);
			} else {
				selectedFieldsTable.getTable().setSelection(focusIndex);
			}
		}
		updateButtons();
		validateData();
		modelChanged = true;
	}

	private class TableProvider implements IStructuredContentProvider, ITableLabelProvider {

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		public void addListener(ILabelProviderListener listener) {

		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {

		}

		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				try {
					if (element instanceof FieldMetaData) {
						if (!existsField((FieldMetaData) element)) {
							return UIHelper.getEmbeddedWarningImage();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Object[]) {
				return (Object[]) inputElement;
			} else if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}

			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 1) {
				if (element instanceof FieldMetaData) {
					return ((FieldMetaData) element).getFullDisplayName();
				}

				return element.toString();
			}
			return null;
		}

	}

	private class FieldsTreeProvider implements ITreeContentProvider, ILabelProvider {

		public void dispose() {

		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		public void addListener(ILabelProviderListener listener) {

		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {

		}

		public boolean hasChildren(Object element) {
			if (element instanceof FieldEntryWrapper) {
				return true;
			} else if (element instanceof Object[]) {
				return ((Object[]) element).length > 0;
			} else if (element instanceof FieldMetaData) {
				return ((FieldMetaData) element).hasChildDocuments();
			}
			return false;
		}

		public Image getImage(Object element) {
			try {
				if (element instanceof FieldEntryWrapper) {
					if (CommandOperationType.RUN_DB_COMMAND.name().equals(opType.name())
							|| UIHelper.isEmptyString(collectionName))
						return UIHelper.getDatabaseDisplayImage();

					return UIHelper.getCollectionDisplayImage();
				} else if (element instanceof FieldMetaData) {
					FieldMetaData fieldMetaData = (FieldMetaData) element;
					if (fieldMetaData.hasChildDocuments()) {
						if (isSelectedField(fieldMetaData.getFullDisplayName())) {
							return UIHelper.getSelectedDocumentDisplayImage();
						}
						return UIHelper.getDocumentDisplayImage();
					}
					if (isSelectedField(fieldMetaData.getFullDisplayName())) {
						return UIHelper.getSelectedFieldDisplayImage();
					}
					return UIHelper.getFieldDisplayImage();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
		}

		public String getText(Object element) {
			if (element instanceof FieldEntryWrapper) {
				if (CommandOperationType.RUN_DB_COMMAND.name().equals(opType.name())
						|| UIHelper.isEmptyString(collectionName))
					return metaData.getDatabaseName();

				return ((FieldEntryWrapper) element).getCollectionName();
			} else if (element instanceof FieldMetaData) {
				return ((FieldMetaData) element).getSimpleDisplayName();
			}

			return element == null ? EMPTY_STRING : element.toString();
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof FieldEntryWrapper[]) {
				return (FieldEntryWrapper[]) inputElement;
			}
			return new Object[0];
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof FieldEntryWrapper) {
				DocumentsMetaData dmd;
				try {
					dmd = ((FieldEntryWrapper) parentElement).getAvailableFields();
					return getFields(dmd);
				} catch (final OdaException e) {
					handleNoFieldsException(e);

					return new String[0];
				}
			} else if (parentElement instanceof DocumentsMetaData) {
				return getFields(parentElement);
			} else if (parentElement instanceof FieldMetaData) {
				return getFields(((FieldMetaData) parentElement).getChildMetaData());
			}
			return new String[0];
		}

		private Object[] getFields(Object parentElement) {
			List<FieldMetaData> entries = new ArrayList<FieldMetaData>();
			for (String name : ((DocumentsMetaData) parentElement).getFieldNames()) {
				entries.add(((DocumentsMetaData) parentElement).getFieldMetaData(name));
			}
			return entries.toArray();
		}

		public Object getParent(Object element) {
			return null;
		}

	}

}
