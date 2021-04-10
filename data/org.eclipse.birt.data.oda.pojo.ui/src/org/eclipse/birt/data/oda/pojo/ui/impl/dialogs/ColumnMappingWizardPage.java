/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import java.lang.reflect.Member;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.impl.Driver;
import org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.querymodel.VariableParameter;
import org.eclipse.birt.data.oda.pojo.ui.Activator;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.ColumnDefinition;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.ToolTipFaker;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.TreeData;
import org.eclipse.birt.data.oda.pojo.ui.impl.providers.ClassTreeContentProvider;
import org.eclipse.birt.data.oda.pojo.ui.impl.providers.ClassTreeLabelProvider;
import org.eclipse.birt.data.oda.pojo.ui.impl.providers.ColumnMappingPageHelper;
import org.eclipse.birt.data.oda.pojo.ui.impl.providers.ColumnMappingTableProvider;
import org.eclipse.birt.data.oda.pojo.ui.util.HelpUtil;
import org.eclipse.birt.data.oda.pojo.ui.util.Utils;
import org.eclipse.birt.data.oda.pojo.util.ClassLister;
import org.eclipse.birt.data.oda.pojo.util.PojoQueryParser;
import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.CustomData;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ParameterMode;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

/**
 * 
 */

public class ColumnMappingWizardPage extends DataSetWizardPage {

	private static Logger logger = Logger.getLogger(ColumnMappingWizardPage.class.getName());
	private static final String DEFAULT_FILTER_STRING = "get*"; //$NON-NLS-1$
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private String pojoRootClass;
	private String pojoDataSetClass;

	private Text txtMethodNameRegex, pojoClassNameText;
	private TreeViewer classStructureTree;
	private TableViewer columnMappingsTable;
	private MenuItem menuRemove, menuRemoveAll;
	private Button addBtn, removeBtn, editButton, upButton, downButton;
	private DataSetDesign design;

	private ColumnMappingPageHelper helper;

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
		if (this.getControl() == null) {
			return super.collectDataSetDesign(design);
		}

		try {
			Utils.savePrivateProperty(design, Constants.METHOD_NAME_REGEX, txtMethodNameRegex.getText().trim());

			Utils.savePrivateProperty(design, Constants.POJO_CLASS, pojoRootClass);
		} catch (OdaException e1) {
			ExceptionHandler.showException(getControl().getShell(), Messages.getString("DataSet.FailedToSaveTitle"), //$NON-NLS-1$
					Messages.getString("DataSet.FailedToSaveMsg"), //$NON-NLS-1$
					e1);
		}

		PojoQuery pq = null;
		String query = design.getQueryText();
		if (query != null && query.length() > 0) {
			try {
				pq = PojoQueryParser.parse(query);
			} catch (OdaException e) {
				logger.log(Level.WARNING, Messages.getString("ColumnMappingWizardPage.error.getQueryText"), //$NON-NLS-1$
						e);
			}
		}

		ColumnDefinition[] inputs = helper.getColumnDefinitions().toArray(new ColumnDefinition[0]);

		if (pq == null) {
			pq = new PojoQuery(Constants.DEFAULT_VERSION, null, null);
			Utils.updateColumnMappings(pq, inputs);
			Utils.savePojoQuery(pq, design, getControl().getShell());
			populateResultSetMetaData(design);
		} else {
			Utils.updateColumnMappings(pq, inputs);
			Utils.savePojoQuery(pq, design, getControl().getShell());
			populateResultSetMetaData(design);
		}
		return design;
	}

	private void updateVariableParameters(ColumnDefinition[] inputs, DataSetDesign design) {
		if (design.getParameters() == null)
			return;

		List<ParameterDefinition> parameters = design.getParameters().getParameterDefinitions();
		for (int i = 0; i < inputs.length; i++) {
			List<VariableParameter> params = inputs[i].getVariableParameters();
			for (int j = 0; j < params.size(); j++) {
				for (int k = 0; k < parameters.size(); k++) {
					if (params.get(j).getName() != null
							&& params.get(j).getName().equals(parameters.get(k).getAttributes().getName())) {
						if (parameters.get(k).getDefaultValues() != null) {
							Object o = parameters.get(k).getDefaultValues().getValues().get(0);
							if (o != null && (o instanceof CustomData)) {
								params.get(j).setStringValue(((CustomData) o).getValue().toString());
							} else {
								params.get(j).setStringValue(parameters.get(k).getDefaultScalarValue());
							}
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #refresh(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	@Override
	protected void refresh(DataSetDesign dataSetDesign) {
		this.design = dataSetDesign;
		updateVariableParameters(helper.getColumnDefinitions().toArray(new ColumnDefinition[0]), dataSetDesign);
		super.refresh(dataSetDesign);
	}

	/**
	 * @param pageName
	 */
	public ColumnMappingWizardPage(String pageName) {
		super(pageName);
		helper = new ColumnMappingPageHelper();
		this.setMessage(Messages.getString("DataSet.ColumnMappingPageMsg")); //$NON-NLS-1$

	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public ColumnMappingWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		helper = new ColumnMappingPageHelper();
		setMessage(Messages.getString("DataSet.ColumnMappingPageMsg")); //$NON-NLS-1$
	}

	private void initPageControls() {
		txtMethodNameRegex.setText(EMPTY_STRING);
		super.refresh(design);
		String query = design.getQueryText();
		PojoQuery pq = null;
		if (query != null && query.length() > 0) {
			try {
				pq = PojoQueryParser.parse(query);
			} catch (OdaException e) {
				logger.log(Level.WARNING, Messages.getString("ColumnMappingWizardPage.error.getQueryText"), //$NON-NLS-1$
						e);
			}
		}
		if (pq == null) {
			pq = new PojoQuery(Constants.DEFAULT_VERSION, null, null);
		}
		helper.clearColumnDefinitions();
		try {
			ColumnDefinition[] defns = Utils.getColumnDefinitions(pq);
			helper.addColumnDefinitions(defns);
			this.columnMappingsTable.setInput(helper.getColumnDefinitions());
		} catch (OdaException e) {
			logger.log(Level.WARNING, EMPTY_STRING, e);
		}
		refreshColumnMappingTable();

		String prop = Utils.getPrivateProperty(design, Constants.METHOD_NAME_REGEX);
		if (prop != null) {
			txtMethodNameRegex.setText(prop.trim());
		}
		this.pojoDataSetClass = Utils.getPrivateProperty(design, Constants.POJO_CLASS);
		this.setPojoDataSetClass(pq.getDataSetClass());

		try {
			this.initClassStructure(design);
		} catch (Throwable t) {
			ExceptionHandler.showException(this.getControl().getShell(),
					Messages.getString("DataSet.FailedToLoadClassTitle"), //$NON-NLS-1$
					Messages.getFormattedString("DataSet.FailedToLoadPojoClassMsg", //$NON-NLS-1$
							new String[] { pojoRootClass }),
					t);
		}

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
		Composite pageComposite = new Composite(parent, SWT.NONE);
		pageComposite.setLayout(new FillLayout());

		SashForm sf = new SashForm(pageComposite, SWT.HORIZONTAL);
		createLeftComposite(pageComposite, sf);

		createRightComposite(sf);

		setControl(pageComposite);
		refreshColumnMappingTable();

		this.design = this.getInitializationDesign();
		initPageControls();
		updateButtonStatus();

		HelpUtil.setSystemHelp(pageComposite, HelpUtil.CONEXT_ID_DATASET_POJO_COLUMN_MAPPING);

	}

	private void createRightComposite(SashForm sf) {
		Composite right = new Composite(sf, SWT.NONE);
		right.setLayout(new GridLayout(1, false));

		Group classStructureGroup = new Group(right, SWT.NONE);
		classStructureGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		classStructureGroup.setText(Messages.getString("DataSet.ColumnMapping")); //$NON-NLS-1$
		classStructureGroup.setLayout(new GridLayout(2, false));

		createRightTableViewer(classStructureGroup);

		createRightButtonArea(classStructureGroup);

	}

	private void createRightTableViewer(Group classStructureGroup) {
		columnMappingsTable = new TableViewer(classStructureGroup,
				SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		columnMappingsTable.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		TableViewerColumn tc = new TableViewerColumn(columnMappingsTable, SWT.LEFT);
		tc.getColumn().setText(Messages.getString("ColumnMappingPage.table.head.columnName")); //$NON-NLS-1$
		tc.getColumn().setWidth(80);
		tc = new TableViewerColumn(columnMappingsTable, SWT.LEFT);
		tc.getColumn().setText(Messages.getString("ColumnMappingPage.table.head.MethodField")); //$NON-NLS-1$
		tc.getColumn().setWidth(140);
		tc = new TableViewerColumn(columnMappingsTable, SWT.LEFT);
		tc.getColumn().setText(Messages.getString("ColumnMappingPage.table.head.columnType")); //$NON-NLS-1$
		tc.getColumn().setWidth(80);
		columnMappingsTable.getTable().setHeaderVisible(true);
		columnMappingsTable.getTable().setLinesVisible(true);
		ColumnMappingTableProvider provider = new ColumnMappingTableProvider();
		columnMappingsTable.setContentProvider(provider.getTableContentProvider());
		columnMappingsTable.setLabelProvider(provider.getTableLabelProvider());
		this.columnMappingsTable.setInput(helper.getColumnDefinitions());

		createTableMenuItems();

	}

	private void createTableMenuItems() {
		Menu menu = new Menu(columnMappingsTable.getTable());
		menu.addMenuListener(new MenuAdapter() {

			public void menuShown(MenuEvent e) {
				columnMappingsTable.cancelEditing();
			}
		});

		menuRemove = new MenuItem(menu, SWT.NONE);
		menuRemove.setText(Messages.getString("ColumnMappingDialog.MenuItem.remove")); //$NON-NLS-1$
		menuRemove.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				removeColumnMappingsFromTable();
				refreshColumnMappingTable();
				updateButtonStatus();
			}

		});

		menuRemoveAll = new MenuItem(menu, SWT.NONE);
		menuRemoveAll.setText(Messages.getString("ColumnMappingDialog.MenuItem.removeAll")); //$NON-NLS-1$
		menuRemoveAll.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				removeAllColumnMappingsFromTable();
				refreshColumnMappingTable();
				updateButtonStatus();
			}
		});

		columnMappingsTable.getTable().setMenu(menu);
	}

	private void createRightButtonArea(Group classStructureGroup) {
		Composite buttonComposite = new Composite(classStructureGroup, SWT.NONE);
		buttonComposite.setLayoutData(new GridData());
		GridLayout layout = new GridLayout(1, false);
		layout.marginBottom = 30;
		layout.marginWidth = 5;
		buttonComposite.setLayout(layout);

		editButton = new Button(buttonComposite, SWT.NONE);
		editButton.setEnabled(false);
		editButton.setText(Messages.getString("DataSet.Edit")); //$NON-NLS-1$
		editButton.setToolTipText(Messages.getString("DataSet.button.tooltip.Edit")); //$NON-NLS-1$
		editButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				editColumnMapping();
			}
		});
		int maxWidth = computeMaxWidth(editButton, 52);

		upButton = new Button(buttonComposite, SWT.NONE);
		upButton.setEnabled(false);
		upButton.setText(Messages.getString("DataSet.Up")); //$NON-NLS-1$
		upButton.setToolTipText(Messages.getString("DataSet.button.tooltip.Up")); //$NON-NLS-1$
		upButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				upColumnMapping();
			}
		});
		maxWidth = computeMaxWidth(upButton, maxWidth);

		downButton = new Button(buttonComposite, SWT.NONE);
		downButton.setEnabled(false);
		downButton.setText(Messages.getString("DataSet.Down")); //$NON-NLS-1$
		downButton.setToolTipText(Messages.getString("DataSet.button.tooltip.Down")); //$NON-NLS-1$
		downButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				downColumnMapping();
			}
		});
		maxWidth = computeMaxWidth(downButton, maxWidth);
		GridData btnData = new GridData(GridData.FILL_HORIZONTAL);
		btnData.widthHint = maxWidth;

		editButton.setLayoutData(btnData);
		upButton.setLayoutData(btnData);
		downButton.setLayoutData(btnData);

		addDoubleClickListeners();
		addKeyListener();
		columnMappingsTable.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				updateButtonStatus();
			}

		});

	}

	private void createLeftComposite(Composite topComposite, SashForm sf) {
		Composite left = new Composite(sf, SWT.NONE);
		left.setLayout(new GridLayout(2, false));

		Group classStructureGroup = new Group(left, SWT.NONE);
		classStructureGroup.setText(Messages.getString("DataSet.ClassStructure")); //$NON-NLS-1$
		classStructureGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		classStructureGroup.setLayout(new GridLayout(1, false));

		createClassNameComposite(classStructureGroup);

		createClassStructureComposite(left, classStructureGroup);
	}

	private void createClassStructureComposite(Composite left, Group classStructureGroup) {
		Label label = new Label(classStructureGroup, SWT.NONE);
		label.setText(Messages.getString("DataSet.Label.MethodNameFilter")); //$NON-NLS-1$
		GridData lableData = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(lableData);

		txtMethodNameRegex = new Text(classStructureGroup, SWT.BORDER);
		txtMethodNameRegex.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtMethodNameRegex.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				try {
					initClassStructure(getInitializationDesign());
				} catch (Throwable t) {

				}
			}
		});

		txtMethodNameRegex.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent arg0) {

			}

			public void keyReleased(KeyEvent event) {
				if (event.keyCode == SWT.ARROW_DOWN) {
					classStructureTree.getTree().setFocus();
					if (classStructureTree.getTree().getItemCount() > 0) {
						classStructureTree.getTree().select(classStructureTree.getTree().getItem(0));
					}
				}

			}

		});

		classStructureTree = new TreeViewer(classStructureGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER | SWT.MULTI);
		classStructureTree.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));

		classStructureTree.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				updateCenterButtonStatus();
			}

		});

		new ToolTipFaker(classStructureTree).fakeToolTip();

		createCenterButtonArea(left);

	}

	private void createCenterButtonArea(Composite left) {
		Composite btnComposite = new Composite(left, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = 10;
		btnComposite.setLayout(layout);

		addBtn = new Button(btnComposite, SWT.NONE);
		addBtn.setText(">"); //$NON-NLS-1$
		addBtn.setToolTipText(Messages.getString("DataSet.button.addColumn.tooltip.mapColumn")); //$NON-NLS-1$

		GridData buttonData = new GridData();
		buttonData.widthHint = Math.max(32, addBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT).x);

		addBtn.setLayoutData(buttonData);
		addBtn.setEnabled(false);
		addBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addColumnMappingsFromTree();
				refreshColumnMappingTable();
			}
		});

		removeBtn = new Button(btnComposite, SWT.NONE);
		removeBtn.setText("<"); //$NON-NLS-1$
		removeBtn.setToolTipText(Messages.getString("DataSet.button.removeColumn.tooltip.mapColumn")); //$NON-NLS-1$
		removeBtn.setLayoutData(buttonData);
		removeBtn.setEnabled(false);
		removeBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removeColumnMappingsFromTable();
				refreshColumnMappingTable();
				updateButtonStatus();
			}
		});

	}

	private void createClassNameComposite(Composite parent) {
		Composite classNameComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginBottom = 5;
		classNameComposite.setLayout(layout);
		GridData compositeData = new GridData(GridData.FILL_HORIZONTAL);
		classNameComposite.setLayoutData(compositeData);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		Label classNameLabel = new Label(classNameComposite, SWT.NONE);
		classNameLabel.setLayoutData(data);
		classNameLabel.setText(Messages.getString("DataSet.Label.POJOClassName")); //$NON-NLS-1$
		classNameLabel.setFont(new Font(Display.getCurrent(), "Tahoma", //$NON-NLS-1$
				10, SWT.BOLD));

		Label prompLabel = new Label(classNameComposite, SWT.NONE);
		prompLabel.setText(Messages.getString("DataSet.Label.prompt.ClassName")); //$NON-NLS-1$
		prompLabel.setLayoutData(data);

		GridData textData = new GridData(GridData.FILL_HORIZONTAL);
		textData.horizontalSpan = 2;
		pojoClassNameText = new Text(classNameComposite, SWT.BORDER);
		pojoClassNameText.setLayoutData(textData);

		String className = Utils.getPrivateProperty(getInitializationDesign(), Constants.POJO_CLASS);
		pojoRootClass = className == null ? EMPTY_STRING : className;
		pojoClassNameText.setText(pojoRootClass);

		Button browseButton = new Button(classNameComposite, SWT.NONE);
		browseButton.setText(Messages.getString("DataSet.Browse1")); //$NON-NLS-1$
		browseButton.setToolTipText(Messages.getString("DataSet.Browse.tooltip")); //$NON-NLS-1$

		browseButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				ClassInputDialog cid = new ClassInputDialog(getControl().getShell(),
						ClassLister.listClasses(getPojoClassPath()), pojoClassNameText.getText().trim());

				if (cid.open() == Window.OK) {
					if (cid.getInput() != null) {
						pojoClassNameText.setText(cid.getInput().trim());
						setPojoRootClass(cid.getInput().trim());
						try {
							initClassStructure(getInitializationDesign());
						} catch (Throwable t) {
							ExceptionHandler.showException(getShell(),
									Messages.getString("DataSet.FailedToLoadClassTitle"), //$NON-NLS-1$
									Messages.getFormattedString("DataSet.FailedToLoadPojoClassMsg", //$NON-NLS-1$
											new String[] { pojoRootClass }),
									t);
						}
						txtMethodNameRegex.setText(DEFAULT_FILTER_STRING);
					}
				}
			}

		});

		final Button applyButton = new Button(classNameComposite, SWT.NONE);
		applyButton.setText(Messages.getString("ColumnMappingWizardPage.button.apply.text")); //$NON-NLS-1$
		applyButton.setToolTipText(Messages.getString("ColumnMappingWizardPage.button.apply.tooltip")); //$NON-NLS-1$
		applyButton.setEnabled(pojoClassNameText.getText().trim().length() > 0);

		applyButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				setPojoRootClass(pojoClassNameText.getText().trim());
				txtMethodNameRegex.setText(DEFAULT_FILTER_STRING);
			}

		});

		GridData buttonData = new GridData();
		int width = computeMaxWidth(applyButton, computeMaxWidth(browseButton, 80));
		buttonData.widthHint = width;
		browseButton.setLayoutData(buttonData);
		applyButton.setLayoutData(buttonData);

		pojoClassNameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				pojoRootClass = pojoClassNameText.getText().trim();
				applyButton.setEnabled(pojoRootClass.length() > 0);
			}
		});

	}

	private int computeMaxWidth(Button btn, int maxWidth) {
		int widthHint = btn.computeSize(-1, -1).x - btn.getBorderWidth();
		return widthHint > maxWidth ? widthHint : maxWidth;
	}

	private void addKeyListener() {
		columnMappingsTable.getTable().addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					removeColumnMappings();
				}
			}

		});
	}

	private URL[] getPojoClassPath() {
		if (design == null) {
			return null;
		}
		DataSourceDesign dataSource = design.getDataSourceDesign();
		String pojoClassPath = Utils.getPrivateProperty(dataSource, Constants.POJO_CLASS_PATH);
		try {
			return Utils.createURLParser(this.getHostResourceIdentifiers()).parse(pojoClassPath);
		} catch (OdaException e) {
			logger.log(Level.WARNING, "Failed to parse POJO Class Path", e); //$NON-NLS-1$
			return null;
		}
	}

	private String[] splitNameFilterRegex(String methodNameRegex) {
		if (methodNameRegex == null || methodNameRegex.trim().length() == 0)
			return null;

		if (!methodNameRegex.contains(".")) //$NON-NLS-1$
			return new String[] { methodNameRegex };

		methodNameRegex = methodNameRegex.replaceAll("\\(", EMPTY_STRING); //$NON-NLS-1$
		methodNameRegex = methodNameRegex.replaceAll("\\)", EMPTY_STRING); //$NON-NLS-1$

		String[] splits = methodNameRegex.split("\\."); //$NON-NLS-1$
		for (int i = 0; i < splits.length; i++) {
			if (splits[i].trim().length() == 0)
				return null;
		}
		return splits;
	}

	private boolean isFirstRow(Object o) {
		return helper.isFirstRow(o);
	}

	private boolean isLastRow(Object o) {
		return helper.isLastRow(o);
	}

	@SuppressWarnings("unchecked")
	public void initClassStructure(DataSetDesign dataSet) throws Throwable {
		String[] splits = splitNameFilterRegex(txtMethodNameRegex.getText().trim());

		classStructureTree.setContentProvider(new ClassTreeContentProvider(splits, null));
		classStructureTree.setInput(null); // clear old items;

		DataSourceDesign dataSource = dataSet.getDataSourceDesign();
		String pojoClassPath = Utils.getPrivateProperty(dataSource, Constants.POJO_CLASS_PATH);
		if (pojoRootClass != null && pojoRootClass.trim().length() > 0) {
			try {
				final URL[] urls = Utils.createURLParser(this.getHostResourceIdentifiers()).parse(pojoClassPath);

				ClassLoader cl = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
					public ClassLoader run() {
						return new URLClassLoader(urls,
								// so cl can also load classes in pojo driver plugin
								Activator.class.getClassLoader());
					}
				});
				Class c = cl.loadClass(pojoRootClass.trim());
				classStructureTree.setContentProvider(new ClassTreeContentProvider(splits, cl));
				classStructureTree.setLabelProvider(new ClassTreeLabelProvider());
				TreeData treeData = new TreeData(c, 0);
				classStructureTree.setInput(treeData);

				if (splits == null)
					classStructureTree.expandToLevel(2);
				else if (txtMethodNameRegex.getText().trim().endsWith(".")) //$NON-NLS-1$
					classStructureTree.expandToLevel(splits.length + 2);
				else
					classStructureTree.expandToLevel(splits.length + 1);

			} catch (Throwable e) {

				setErrorMessageInTree(Messages.getFormattedString("DataSet.FailedToLoadPojoClassMsg", //$NON-NLS-1$
						new String[] { pojoRootClass }));
				throw e;
			}
		} else {
			setErrorMessageInTree(Messages.getString("DataSet.MissPojoRootClass")); //$NON-NLS-1$
		}
	}

	private void setErrorMessageInTree(String error) {
		classStructureTree.setContentProvider(new ClassTreeContentProvider(null, null));
		classStructureTree.setInput(error);
	}

	/**
	 * @param pojoRootClass the pojoRootClass to set
	 */
	public void setPojoRootClass(String pojoRootClass) {
		this.pojoRootClass = pojoRootClass;
	}

	public String getPojoRootClass() {
		return this.pojoRootClass;
	}

	/**
	 * @param pojoDataSetClass the pojoDataSetClass to set
	 */
	public void setPojoDataSetClass(String pojoDataSetClass) {
		this.pojoDataSetClass = pojoDataSetClass;
	}

	public String getPojoDataSetClass() {
		return this.pojoDataSetClass;
	}

	public void setPOJORootClass(String rootClass) {
		pojoRootClass = rootClass;

		if (pojoClassNameText != null && !pojoClassNameText.isDisposed()) {
			pojoClassNameText.setText(pojoRootClass != null ? pojoRootClass : ""); //$NON-NLS-1$
		}
	}

	private ColumnDefinition[] getMappingsToAdd() {
		helper.clearParametersCache();
		List<ColumnDefinition> result = new ArrayList<ColumnDefinition>();

		for (TreeItem item : classStructureTree.getTree().getSelection()) {
			if (item.getData() instanceof TreeData
					&& ((TreeData) item.getData()).getWrappedObject() instanceof Member) {
				Member m = (Member) ((TreeData) item.getData()).getWrappedObject();
				ColumnDefinition cm = new ColumnDefinition(getMappingPath(item), Utils.getSuggestName(m),
						Utils.getSuggestOdaType(m));
				result.add(cm);
			}
		}
		return result.toArray(new ColumnDefinition[0]);
	}

	private IMappingSource[] getMappingPath(TreeItem item) {
		List<Member> backs = new ArrayList<Member>();
		while (item != null && item.getData() instanceof TreeData
				&& ((TreeData) item.getData()).getWrappedObject() instanceof Member) {
			backs.add((Member) ((TreeData) item.getData()).getWrappedObject());
			item = item.getParentItem();
		}
		IMappingSource[] result = helper.createMappingPath(backs);

		return result;
	}

	private void populateResultSetMetaData(DataSetDesign dataSetDesign) {
		IConnection conn = null;
		try {
			IDriver driver = new Driver();
			conn = driver.getConnection(null);

			Properties properties = new Properties();

			DataSourceDesign ds = dataSetDesign.getDataSourceDesign();

			String dsClassPath = Utils.getPublicProperty(ds, Constants.POJO_DATA_SET_CLASS_PATH);
			if (dsClassPath != null)
				properties.put(Constants.POJO_DATA_SET_CLASS_PATH, dsClassPath);

			conn.open(properties);

			IQuery query = conn.newQuery(null);
			query.prepare(dataSetDesign.getQueryText());

			IResultSetMetaData metadata = query.getMetaData();
			setParameterMetaData(dataSetDesign, query.getParameterMetaData());
			setResultSetMetaData(dataSetDesign, metadata);
		} catch (OdaException e) {
			// no result set definition available, reset in dataSetDesign
			dataSetDesign.setResultSets(null);
			ExceptionHandler.showException(getControl().getShell(), Messages.getString("DataSet.FailedToSaveTitle"), //$NON-NLS-1$
					Messages.getString("DataSet.FailedToSaveMsg"), //$NON-NLS-1$
					e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (OdaException e) {
				}
			}
		}

	}

	/**
	 * Set parameter metadata in dataset design
	 * 
	 * @param design
	 * @param query
	 */
	private void setParameterMetaData(DataSetDesign dataSetDesign, IParameterMetaData paramMetaData) {
		try {
			// set parameter metadata
			mergeParameterMetaData(dataSetDesign, paramMetaData);
		} catch (OdaException e) {
			// do nothing, to keep the parameter definition in dataset design
			// dataSetDesign.setParameters( null );
		}
	}

	/**
	 * merge paramter meta data between dataParameter and datasetDesign's parameter.
	 * 
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private void mergeParameterMetaData(DataSetDesign dataSetDesign, IParameterMetaData paramMetaData)
			throws OdaException {
		if (paramMetaData == null || dataSetDesign == null)
			return;

		DataSetParameters dataSetParameter = DesignSessionUtil.toDataSetParametersDesign(paramMetaData,
				ParameterMode.IN_LITERAL);

		if (dataSetParameter == null) {
			dataSetDesign.setParameters(null);
			return;
		}

		List<IMethodParameter> parameters = helper.getAllParameters();
		List<ParameterDefinition> params = dataSetParameter.getParameterDefinitions();
		for (int i = 0; i < params.size(); i++) {
			if (params.get(i).getAttributes() == null)
				continue;

			String paramName = params.get(i).getAttributes().getName();

			for (int j = 0; j < parameters.size(); j++) {
				IMethodParameter mp = parameters.get(j);
				if ((mp instanceof VariableParameter) && paramName != null
						&& paramName.equals(((VariableParameter) mp).getName())) {
					if (mp.getStringValue() != null && !mp.getStringValue().equals(""))
						params.get(i).setDefaultScalarValue(mp.getStringValue());
				}
			}
		}

		dataSetDesign.setParameters(dataSetParameter);
	}

	/**
	 * set resultset meta data
	 * 
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private static void setResultSetMetaData(DataSetDesign dataSetDesign, IResultSetMetaData md) throws OdaException {
		ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign(md);

		if (columns != null) {
			ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition();
			resultSetDefn.setResultSetColumns(columns);

			// no exception; go ahead and assign to specified dataSetDesign
			dataSetDesign.setPrimaryResultSet(resultSetDefn);
			dataSetDesign.getResultSets().setDerivedMetaData(true);
		} else {
			dataSetDesign.setResultSets(null);
		}
	}

	private void addColumnMappingsFromTree() {
		ColumnDefinition[] mappings = getMappingsToAdd();
		if (mappings.length == 1) {
			boolean containsParam = containsParameter(mappings[0]);
			ColumnMappingDialog dialog = new ColumnMappingDialog(getControl().getShell(), null, helper, false,
					containsParam);
			dialog.setColumnDefinition(mappings[0]);
			if (dialog.open() == Window.OK) {
				mappings[0] = dialog.getColumnDefinition();
				ColumnDefinition added = helper.addColumnDefinition(mappings[0]);
				refreshColumnMappingTable();
				columnMappingsTable.setSelection(new StructuredSelection(added));
			}
		} else if (mappings.length > 1) {
			ColumnDefinition[] added = helper.addColumnDefinitions(mappings);
			refreshColumnMappingTable();
			columnMappingsTable.setSelection(new StructuredSelection(added));
		}
		columnMappingsTable.getTable().setFocus();
	}

	private boolean containsParameter(ColumnDefinition mapping) {
		boolean containsParam = false;
		IMappingSource[] sources = mapping.getMappingPath();
		containsParam = containsParameter(sources);
		return containsParam;
	}

	private boolean containsParameter(IMappingSource[] sources) {
		boolean containsParam = false;
		for (int i = 0; i < sources.length; i++) {
			if (sources[i] instanceof MethodSource) {
				MethodSource method = (MethodSource) sources[i];
				if (method.getParameters() != null && method.getParameters().length > 0) {
					containsParam = true;
					break;
				}
			}
		}
		return containsParam;
	}

	private void removeColumnMappingsFromTable() {
		int[] indices = columnMappingsTable.getTable().getSelectionIndices();
		TableItem[] items = columnMappingsTable.getTable().getSelection();
		removeColumnMappings(items);
		updateViewerSelectionStatus(indices);
		updateButtonStatus();
	}

	private void removeAllColumnMappingsFromTable() {
		TableItem[] items = columnMappingsTable.getTable().getItems();
		removeColumnMappings(items);
		updateButtonStatus();
	}

	private void removeColumnMappings(TableItem[] items) {
		for (int i = 0; i < items.length; i++) {
			helper.removeColumnDefinition((ColumnDefinition) items[i].getData());
		}
		columnMappingsTable.refresh();
		columnMappingsTable.getTable().getItems();
		removeBtn.setEnabled(columnMappingsTable.getTable().getSelectionCount() > 0);
	}

	private void updateViewerSelectionStatus(int[] indices) {
		for (int i = 0; i < indices.length; i++) {
			int index = indices[i];
			if (index >= 0 && (columnMappingsTable.getTable().getItemCount() > index)) {
				columnMappingsTable.getTable().select(index);
			} else if (columnMappingsTable.getTable().getItemCount() <= index
					&& columnMappingsTable.getTable().getItemCount() > 0) {
				columnMappingsTable.getTable().select(columnMappingsTable.getTable().getItemCount() - 1);
			}
		}
	}

	private void editColumnMapping() {
		IStructuredSelection ss = (IStructuredSelection) (columnMappingsTable.getSelection());
		Object o = ss.getFirstElement();
		int index = helper.getElementIndex(o);
		if (index < 0) {
			return;
		}
		ColumnMappingDialog cmd = new ColumnMappingDialog(this.getControl().getShell(), (ColumnDefinition) o, helper,
				true, containsParameter((ColumnDefinition) o));
		if (cmd.open() == Window.OK) {
			ColumnDefinition cd = cmd.getColumnDefinition();
			ColumnDefinition newDef = helper.setColumnDefinition(index, cd);
			refreshColumnMappingTable();
			columnMappingsTable.setSelection(new StructuredSelection(newDef));
		}
	}

	@SuppressWarnings("unchecked")
	private void removeColumnMappings() {
		IStructuredSelection ss = (IStructuredSelection) (columnMappingsTable.getSelection());
		Iterator itr = ss.iterator();
		while (itr.hasNext()) {
			Object value = itr.next();
			if (value instanceof ColumnDefinition)
				helper.removeColumnDefinition((ColumnDefinition) value);
		}
		refreshColumnMappingTable();
		updateButtonStatus();
	}

	private void upColumnMapping() {
		IStructuredSelection ss = (IStructuredSelection) (columnMappingsTable.getSelection());
		Object o = ss.getFirstElement();
		int index = helper.getElementIndex(o);
		if (index >= 1) {
			helper.moveColumnDefinitionUp(index);
			refreshColumnMappingTable();
			columnMappingsTable.setSelection(new StructuredSelection(o));
		}
	}

	private void downColumnMapping() {
		IStructuredSelection ss = (IStructuredSelection) (columnMappingsTable.getSelection());
		Object o = ss.getFirstElement();
		int index = helper.getElementIndex(o);
		if (index >= 0 && index < helper.getColumnDefnCount() - 1) {
			helper.moveColumnDefinitionDown(index);
			refreshColumnMappingTable();
			columnMappingsTable.setSelection(new StructuredSelection(o));
		}
	}

	private void addDoubleClickListeners() {
		columnMappingsTable.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				editColumnMapping();
			}
		});

		classStructureTree.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent arg0) {
				addColumnMappingsFromTree();
			}
		});
	}

	private void refreshColumnMappingTable() {
		columnMappingsTable.refresh();
		if (helper.getColumnDefnCount() > 0) {
			this.setPageComplete(true);
			this.setErrorMessage(null);
			this.setMessage(Messages.getString("DataSet.ColumnMappingPageMsg")); //$NON-NLS-1$
		} else {
			this.setPageComplete(false);
			this.setErrorMessage(Messages.getString("DataSet.NoColumnMappingErrorMsg")); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#
	 * canLeave()
	 */
	@Override
	protected boolean canLeave() {
		if (columnMappingsTable == null) {
			return true;
		}
		return helper.getColumnDefnCount() > 0;
	}

	private void updateCenterButtonStatus() {
		if (classStructureTree.getTree().isFocusControl() && classStructureTree.getTree().getSelectionCount() > 0) {
			addBtn.setEnabled(classStructureTree.getTree().getSelection()[0].getParentItem() != null);
		} else {
			addBtn.setEnabled(false);
		}

		removeBtn.setEnabled(columnMappingsTable.getTable().getSelectionCount() > 0);
	}

	private void updateButtonStatus() {
		updateCenterButtonStatus();
		updateTableEditorButtons();
	}

	private void updateTableEditorButtons() {
		menuRemoveAll.setEnabled(columnMappingsTable.getTable().getItemCount() > 0);

		IStructuredSelection ss = (IStructuredSelection) columnMappingsTable.getSelection();
		editButton.setEnabled(false);
		upButton.setEnabled(false);
		downButton.setEnabled(false);
		menuRemove.setEnabled(ss.size() > 0);

		if (ss.size() == 1) {
			Object o = ss.getFirstElement();
			editButton.setEnabled(true);
			if (!isFirstRow(o)) {
				upButton.setEnabled(true);
			}
			if (!isLastRow(o)) {
				downButton.setEnabled(true);
			}
		}
	}

}
