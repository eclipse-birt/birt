/*******************************************************************************
 * Copyright (c) 2004, 2013 Actuate Corporation, 2024 and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *	Thomas Gutmann      - add query text search
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiTransform;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.ChildrenAllowedNode;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.DBNodeUtil;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.FilterConfig;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.IDBNode;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.RootNode;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.TableType;
import org.eclipse.birt.report.data.oda.jdbc.ui.preference.DateSetPreferencePage;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ExceptionHandler;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.IHelpConstants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentEvent;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

/**
 * The JDBC SQL DatasetEditor page which enable user to browse the catalog of
 * the selected data source and input the sql text. The page extends the
 * <code>DataSetWizardPage</code> it could be loaded as a custom page for jdbc
 * ui.
 */

public class SQLDataSetEditorPage extends DataSetWizardPage {

	// composite in editor page
	private Document doc = null;
	private SQLSourceViewer viewer = null;
	private Text searchTxt = null;
	private Text findQueryText = null;
	private ComboViewer filterComboViewer = null;
	private Combo schemaCombo = null;
	private Menu treeMenu = null;
	private ScrolledComposite sComposite;
	private Composite tablescomposite;

	private Label schemaLabel = null;
	private Tree availableDbObjectsTree = null;
	private Button identifierQuoteStringCheckBox = null;
	private Button showSystemTableCheckBox = null;
	private Button showAliasCheckBox = null;
	private Button includeSchemaCheckBox = null;
	private Button findQueryTextWholeWord = null;
	private Button findQueryTextCaseSensitive = null;
	private Exception prepareException = null;
	private Group sqlOptionGroup = null;
	private Group selectTableGroup = null;
	private Group findQueryTextGroup = null;

	private static String DEFAULT_MESSAGE = JdbcPlugin.getResourceString("dataset.new.query");//$NON-NLS-1$

	private int maxSchemaCount;
	private int maxTableCountPerSchema;
	private boolean enableCodeAssist;
	boolean prefetchSchema;

	private FilterConfig fc;

	String formerQueryTxt;

	protected int timeOutLimit;
	protected DataSetDesign dataSetDesign;
	private OdaConnectionProvider odaConnectionProvider;

	String metadataBidiFormatStr = null; // bidi_hcg

	private boolean continueConnect = true;

	private static final int DB_OBJECT_TREE_HEIGHT_MIN = 150;
	private static final int DB_OBJECT_TREE_WIDTH_MIN = 200;

	private static final String FIND_DIRECTION_SYMBOL_FORWARD = "\u25BC";
	private static final String FIND_DIRECTION_SYMBOL_BACKWARD = "\u25B2";

	/**
	 * constructor
	 *
	 * @param pageName
	 */
	public SQLDataSetEditorPage(String pageName) {
		super(pageName);
	}

	private void readPreferences() {
		setDefaultPereferencesIfNeed();
		Preferences preferences = JdbcPlugin.getDefault().getPluginPreferences();

		if (DateSetPreferencePage.ENABLED
				.equals(preferences.getString(DateSetPreferencePage.SCHEMAS_PREFETCH_CONFIG))) {
			prefetchSchema = true;
		}
		if (DateSetPreferencePage.ENABLED.equals(preferences.getString(DateSetPreferencePage.ENABLE_CODE_ASSIST))) {
			enableCodeAssist = true;
		}
		maxSchemaCount = preferences.getInt(DateSetPreferencePage.USER_MAX_NUM_OF_SCHEMA);
		maxTableCountPerSchema = preferences.getInt(DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA);
		timeOutLimit = preferences.getInt(DateSetPreferencePage.USER_TIMEOUT_LIMIT);
		if (maxSchemaCount <= 0) {
			maxSchemaCount = Integer.MAX_VALUE;
		}
		if (maxTableCountPerSchema <= 0) {
			maxTableCountPerSchema = Integer.MAX_VALUE;
		}
		if (timeOutLimit < 0) {
			timeOutLimit = 0;
		}
	}

	private void setDefaultPereferencesIfNeed() {
		Preferences preferences = JdbcPlugin.getDefault().getPluginPreferences();
		if (!preferences.contains(DateSetPreferencePage.SCHEMAS_PREFETCH_CONFIG)) {
			preferences.setValue(DateSetPreferencePage.SCHEMAS_PREFETCH_CONFIG, DateSetPreferencePage.ENABLED);
		}
		if (!preferences.contains(DateSetPreferencePage.ENABLE_CODE_ASSIST)) {
			preferences.setValue(DateSetPreferencePage.ENABLE_CODE_ASSIST, DateSetPreferencePage.ENABLED);
		}
		if (!preferences.contains(DateSetPreferencePage.USER_MAX_NUM_OF_SCHEMA)) {
			preferences.setValue(DateSetPreferencePage.USER_MAX_NUM_OF_SCHEMA,
					String.valueOf(DateSetPreferencePage.DEFAULT_MAX_NUM_OF_SCHEMA));
		}
		if (!preferences.contains(DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA)) {
			preferences.setValue(DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA,
					String.valueOf(DateSetPreferencePage.DEFAULT_MAX_NUM_OF_TABLE_EACH_SCHEMA));
		}
		if (!preferences.contains(DateSetPreferencePage.USER_TIMEOUT_LIMIT)) {
			preferences.setValue(DateSetPreferencePage.USER_TIMEOUT_LIMIT,
					String.valueOf(DateSetPreferencePage.DEFAULT_TIMEOUT_LIMIT));
		}
	}

	private void prepareJDBCMetaDataProvider(DataSetDesign dataSetDesign) {
		JdbcMetaDataProvider.createInstance(dataSetDesign, this.getHostResourceIdentifiers());

		class TempThread extends Thread {
			@Override
			public void run() {
				try {
					JdbcMetaDataProvider.getInstance().reconnect();
				} catch (Exception e) {
					prepareException = e;
				}
			}
		}

		TempThread tt = new TempThread();
		tt.start();

		try {
			tt.join(this.timeOutLimit * 1000);
			Thread.State state = tt.getState();
			if (state == Thread.State.TERMINATED) {
				if (prepareException != null) {
					ExceptionHandler.showException(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
							JdbcPlugin.getResourceString("exceptionHandler.title.error"),
							prepareException.getLocalizedMessage(), prepareException);
					prepareException = null;
				}
			} else {
				continueConnect = false;
				ExceptionHandler.showException(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						JdbcPlugin.getResourceString("exceptionHandler.title.error"),
						JdbcPlugin.getResourceString("connection.timeOut"), new Exception());
			}
		} catch (InterruptedException e) {
			ExceptionHandler.showException(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					JdbcPlugin.getResourceString("exceptionHandler.title.error"), e.getLocalizedMessage(), e);
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPageCustomControl(Composite parent) {
		this.dataSetDesign = this.getInitializationDesign();

		// bidi_hcg: initialize value of metadataBidiFormatStr
		try {
			Properties connProps = DesignSessionUtil
					.getEffectiveDataSourceProperties(dataSetDesign.getDataSourceDesign());
			metadataBidiFormatStr = connProps.getProperty(BidiConstants.METADATA_FORMAT_PROP_NAME);
		} catch (OdaException e) {
			metadataBidiFormatStr = null;
		}

		readPreferences();
		prepareJDBCMetaDataProvider(dataSetDesign);
		this.odaConnectionProvider = new OdaConnectionProvider(dataSetDesign.getDataSourceDesign());
		setControl(createPageControl(parent));
		initializeControl();
		this.formerQueryTxt = dataSetDesign.getQueryText();
		Utility.setSystemHelp(getControl(), IHelpConstants.CONEXT_ID_DATASET_JDBC);
	}

	/**
	 * create page control for sql edit page
	 *
	 * @param parent
	 * @return
	 */
	private Control createPageControl(Composite parent) {
		SashForm pageContainer = new SashForm(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 2;
		pageContainer.setLayout(layout);
		pageContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

		pageContainer.setSashWidth(3);

		Control left = createDBMetaDataSelectionComposite(pageContainer);
		Control right = createTextualQueryComposite(pageContainer);
		setWidthHints(pageContainer, left, right);

		return pageContainer;
	}

	/**
	 * @param pageContainer
	 * @param left
	 * @param right
	 */
	private void setWidthHints(SashForm pageContainer, Control left, Control right) {
		int leftWidth = left.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		int rightWidth = right.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;

		pageContainer.setWeights(new int[] { leftWidth, rightWidth });

	}

	/**
	 * initial dataset control
	 *
	 */
	private void initializeControl() {
		if (this.dataSetDesign.getOdaExtensionDataSourceId() != null
				&& this.dataSetDesign.getOdaExtensionDataSourceId().contains("hive")) {
			DEFAULT_MESSAGE = JdbcPlugin.getResourceString("dataset.new.query.hive");
		} else {
			DEFAULT_MESSAGE = JdbcPlugin.getResourceString("dataset.new.query");
		}
		setMessage(DEFAULT_MESSAGE, IMessageProvider.NONE);
		viewer.getTextWidget().setFocus();
	}

	/**
	 * Creates the composite, for displaying the list of available db objects
	 *
	 * @param parent
	 */
	private Control createDBMetaDataSelectionComposite(Composite parent) {
		sComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		sComposite.setExpandHorizontal(true);
		sComposite.setExpandVertical(true);
		sComposite.setMinHeight(500);
		sComposite.setMinWidth(250);

		sComposite.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				computeSize();
			}
		});

		boolean supportsSchema = false;
		boolean supportsProcedure = false;

		if (continueConnect) {
			supportsSchema = JdbcMetaDataProvider.getInstance().isSupportSchema();
			supportsProcedure = JdbcMetaDataProvider.getInstance().isSupportProcedure();
		}

		tablescomposite = new Composite(sComposite, SWT.NONE);

		tablescomposite.setLayout(new GridLayout());
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessVerticalSpace = true;
		tablescomposite.setLayoutData(data);

		createDBObjectTree(tablescomposite);
		createObjectTreeMenu();

		createSchemaFilterComposite(supportsSchema, supportsProcedure, tablescomposite);

		createSQLOptionGroup(tablescomposite);

		addDragSupportToTree();
		// bidi_hcg: pass value of metadataBidiFormatStr
		addFetchDbObjectListener(metadataBidiFormatStr);

		sComposite.setContent(tablescomposite);

		return tablescomposite;
	}

	private void computeSize() {
		if (this.getShell() != null) {
			availableDbObjectsTree.setBounds(availableDbObjectsTree.getBounds().x, availableDbObjectsTree.getBounds().y,
					this.getShell().getSize().x / 3, this.getShell().getSize().y / 4);
			sComposite.setMinSize(max(this.getShell().getSize().x / 3 - 30, DB_OBJECT_TREE_WIDTH_MIN),
					max(this.getShell().getSize().y / 4, DB_OBJECT_TREE_HEIGHT_MIN)
							+ selectTableGroup.getBounds().height + sqlOptionGroup.getBounds().height + 30);
			tablescomposite.layout();
		}

	}

	private int max(double d, double b) {
		return (int) Math.max(d, b);
	}

	private void createDBObjectTree(Composite tablescomposite) {
		// Available Items
		Label dataSourceLabel = new Label(tablescomposite, SWT.LEFT);
		dataSourceLabel.setText(JdbcPlugin.getResourceString("tablepage.label.availableItems"));//$NON-NLS-1$
		GridData labelData = new GridData();
		dataSourceLabel.setLayoutData(labelData);

		availableDbObjectsTree = new Tree(tablescomposite, SWT.BORDER | SWT.MULTI);
		GridData treeData = new GridData(GridData.FILL_BOTH);
		treeData.minimumHeight = DB_OBJECT_TREE_HEIGHT_MIN;
		availableDbObjectsTree.setLayoutData(treeData);

		availableDbObjectsTree.addMenuDetectListener(new MenuDetectListener() {

			@Override
			public void menuDetected(MenuDetectEvent e) {
				if (availableDbObjectsTree.getSelectionCount() > 0) {
					TreeItem item = availableDbObjectsTree.getSelection()[0];
					if (item.getParentItem() != null && treeMenu != null) {
						treeMenu.setLocation(e.x, e.y);
						return;
					}
				}
				e.doit = false;
			}
		});

		availableDbObjectsTree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				insertTreeItemText();
			}
		});
	}

	private void createObjectTreeMenu() {
		treeMenu = new Menu(availableDbObjectsTree);

		MenuItem insert = new MenuItem(treeMenu, SWT.NONE);
		insert.setText(JdbcPlugin.getResourceString("sqleditor.objectTree.menuItem.insert"));
		insert.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				insertTreeItemText();
			}

		});
		availableDbObjectsTree.setMenu(treeMenu);
	}

	private void createSchemaFilterComposite(boolean supportsSchema, boolean supportsProcedure,
			Composite tablescomposite) {
		// Group for selecting the Tables etc
		// Searching the Tables and Views
		selectTableGroup = new Group(tablescomposite, SWT.FILL);

		GridLayout groupLayout = new GridLayout();
		groupLayout.numColumns = 3;
		groupLayout.verticalSpacing = 10;
		selectTableGroup.setLayout(groupLayout);

		GridData selectTableData = new GridData(GridData.FILL_HORIZONTAL);
		selectTableGroup.setLayoutData(selectTableData);

		schemaLabel = new Label(selectTableGroup, SWT.LEFT);
		schemaLabel.setText(JdbcPlugin.getResourceString("tablepage.label.schema"));

		schemaCombo = new Combo(selectTableGroup, prefetchSchema ? SWT.READ_ONLY : SWT.DROP_DOWN);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		schemaCombo.setLayoutData(gd);
		schemaCombo.setVisibleItemCount(30);

		final Label filterLabel = new Label(selectTableGroup, SWT.LEFT);
		filterLabel.setText(JdbcPlugin.getResourceString("tablepage.label.filter"));
		filterLabel.addMouseTrackListener(new MouseTrackAdapter() {

			@Override
			public void mouseHover(MouseEvent event) {
				filterLabel.setToolTipText(JdbcPlugin.getResourceString("tablepage.label.filter.tooltip"));
			}
		});

		searchTxt = new Text(selectTableGroup, SWT.BORDER);
		GridData searchTxtData = new GridData(GridData.FILL_HORIZONTAL);
		searchTxtData.horizontalSpan = 2;
		searchTxt.setLayoutData(searchTxtData);
		searchTxt.addMouseTrackListener(new MouseTrackAdapter() {

			@Override
			public void mouseHover(MouseEvent event) {
				searchTxt.setToolTipText(JdbcPlugin.getResourceString("tablepage.label.filter.tooltip"));
			}
		});

		// Select Type
		Label selectTypeLabel = new Label(selectTableGroup, SWT.NONE);
		selectTypeLabel.setText(JdbcPlugin.getResourceString("tablepage.label.selecttype"));

		// Filter Combo
		filterComboViewer = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
		setFilterComboContents(filterComboViewer, supportsProcedure);
		GridData filterData = new GridData(GridData.FILL_HORIZONTAL);
		filterData.horizontalSpan = 2;
		filterComboViewer.getControl().setLayoutData(filterData);

		setupShowSystemTableCheckBox(selectTableGroup);
		setupShowAliasCheckBox(selectTableGroup);

		// Find Button
		Button findButton = new Button(selectTableGroup, SWT.NONE);
		GridData btnData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
		btnData.horizontalSpan = 3;
		findButton.setLayoutData(btnData);
		findButton.setText(JdbcPlugin.getResourceString("tablepage.button.filter"));//$NON-NLS-1$

		// Add listener to the find button
		findButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						fc = populateFilterConfig();
						// bidi_hcg: pass value of metadataBidiFormatStr
						DBNodeUtil.createTreeRoot(availableDbObjectsTree,
								new RootNode(dataSetDesign.getDataSourceDesign().getName()), fc, metadataBidiFormatStr,
								SQLDataSetEditorPage.this.timeOutLimit * 1000);
					}
				});
			}
		});

		String[] allSchemaNames = null;
		if (supportsSchema) {
			String allFlag = JdbcPlugin.getResourceString("tablepage.text.All");
			schemaCombo.add(allFlag);

			if (prefetchSchema) {

				allSchemaNames = JdbcMetaDataProvider.getInstance().getAllSchemaNames(timeOutLimit * 1000);

				for (String name : allSchemaNames) {
					schemaCombo.add(BidiTransform.transform(name, metadataBidiFormatStr,
							BidiConstants.DEFAULT_BIDI_FORMAT_STR));
				}
			}
			schemaCombo.select(0);
		} else {
			schemaCombo.removeAll();
			schemaCombo.setEnabled(false);
			schemaLabel.setEnabled(false);
		}
		if (prefetchSchema && continueConnect) {
			fc = populateFilterConfig();
			// bidi_hcg: pass value of metadataBidiFormatStr
			DBNodeUtil.createTreeRoot(availableDbObjectsTree,
					new RootNode(dataSetDesign.getDataSourceDesign().getName(), allSchemaNames), fc,
					metadataBidiFormatStr, SQLDataSetEditorPage.this.timeOutLimit * 1000);
		} else {
			// bidi_hcg: pass value of metadataBidiFormatStr
			DBNodeUtil.createRootTip(availableDbObjectsTree,
					new RootNode(dataSetDesign.getDataSourceDesign().getName()), metadataBidiFormatStr);
		}
	}

	private void createSQLOptionGroup(Composite tablescomposite) {
		sqlOptionGroup = new Group(tablescomposite, SWT.FILL);
		sqlOptionGroup.setText(JdbcPlugin.getResourceString("tablepage.group.title")); //$NON-NLS-1$
		GridLayout sqlOptionGroupLayout = new GridLayout();
		sqlOptionGroupLayout.verticalSpacing = 10;
		sqlOptionGroup.setLayout(sqlOptionGroupLayout);
		GridData sqlOptionGroupData = new GridData(GridData.FILL_HORIZONTAL);
		sqlOptionGroup.setLayoutData(sqlOptionGroupData);

		setupIdentifierQuoteStringCheckBox(sqlOptionGroup);

		setupIncludeSchemaCheckBox(sqlOptionGroup);
	}

	private FilterConfig populateFilterConfig() {
		String schemaName = null;
		if (schemaCombo.isEnabled() && schemaCombo.getSelectionIndex() != 0) {
			schemaName = schemaCombo.getText();
			schemaName = BidiTransform.transform(schemaName, BidiConstants.DEFAULT_BIDI_FORMAT_STR,
					metadataBidiFormatStr);
		}
		TableType type = getSelectedFilterType();
		String namePattern = searchTxt.getText();
		boolean isShowSystemTable = showSystemTableCheckBox.isEnabled() ? showSystemTableCheckBox.getSelection()
				: false;
		boolean isShowAlias = showAliasCheckBox.isEnabled() ? showAliasCheckBox.getSelection() : false;
		FilterConfig result = new FilterConfig(schemaName, type, namePattern, isShowSystemTable, isShowAlias,
				maxSchemaCount, maxTableCountPerSchema);
		return result;
	}

	/*
	 *
	 * @seeorg.eclipse.datatools.connectivity.oda.design.internal.ui.
	 * DataSetWizardPageCore
	 * #collectDataSetDesign(org.eclipse.datatools.connectivity
	 * .oda.design.DataSetDesign)
	 */
	@Override
	protected DataSetDesign collectDataSetDesign(DataSetDesign design) {
		// This method sometimes is called even if the whole page is ever not
		// presented
		if (doc != null) {
			design.setQueryText(doc.get());
			if (!design.getQueryText().equals(formerQueryTxt)) {
				MetaDataRetriever retriever = new MetaDataRetriever(odaConnectionProvider, design);
				IResultSetMetaData resultsetMeta = retriever.getResultSetMetaData();
				IParameterMetaData paramMeta = retriever.getParameterMetaData();
				SQLUtility.saveDataSetDesign(design, resultsetMeta, paramMeta);
				formerQueryTxt = design.getQueryText();
				retriever.close();
			}
		}
		return design;
	}

	/**
	 *
	 * @param group
	 */
	private void setupIdentifierQuoteStringCheckBox(Group group) {
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 3;
		identifierQuoteStringCheckBox = new Button(group, SWT.CHECK);
		identifierQuoteStringCheckBox.setText(JdbcPlugin.getResourceString("tablepage.button.dnd")); //$NON-NLS-1$
		identifierQuoteStringCheckBox.setSelection(false);
		identifierQuoteStringCheckBox.setLayoutData(layoutData);

		if (JdbcMetaDataProvider.getInstance().getIdentifierQuoteString().equals("")) {
			identifierQuoteStringCheckBox.setEnabled(false);
		}
	}

	/**
	 *
	 * @param group
	 */
	private void setupShowSystemTableCheckBox(Group group) {
		GridData layoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 2;
		showSystemTableCheckBox = new Button(group, SWT.CHECK);
		showSystemTableCheckBox.setText(JdbcPlugin.getResourceString("tablepage.button.showSystemTables")); //$NON-NLS-1$
		showSystemTableCheckBox.setSelection(false);
		showSystemTableCheckBox.setLayoutData(layoutData);
		showSystemTableCheckBox.setEnabled(true);
	}

	private void setupShowAliasCheckBox(Group group) {
		GridData layoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 2;
		showAliasCheckBox = new Button(group, SWT.CHECK);
		showAliasCheckBox.setText(JdbcPlugin.getResourceString("tablepage.button.showAlias")); //$NON-NLS-1$
		showAliasCheckBox.setSelection(true);
		showAliasCheckBox.setLayoutData(layoutData);
		showAliasCheckBox.setEnabled(true);
	}

	/**
	 *
	 * @param group
	 */
	private void setupIncludeSchemaCheckBox(Group group) {
		GridData layoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		layoutData.horizontalSpan = 2;
		includeSchemaCheckBox = new Button(group, SWT.CHECK);
		includeSchemaCheckBox.setText(JdbcPlugin.getResourceString("tablepage.button.includeSchemaInfo")); //$NON-NLS-1$
		includeSchemaCheckBox.setSelection(true);
		includeSchemaCheckBox.setLayoutData(layoutData);
		includeSchemaCheckBox.setEnabled(true);
	}

	/**
	 *
	 * @param filterComboViewer
	 */
	private void setFilterComboContents(ComboViewer filterComboViewer, boolean supportsProcedure) {
		if (filterComboViewer == null) {
			return;
		}
		filterComboViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				return ((List<?>) inputElement).toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

		});

		filterComboViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object inputElement) {
				TableType type = (TableType) inputElement;
				return type.getDisplayName();
			}

		});

		List<TableType> types = getTableTypes(supportsProcedure);
		filterComboViewer.setInput(types);

		// Set the Default selection to the First Item , which is "All"
		filterComboViewer.getCombo().select(0);
		filterComboViewer.getCombo().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TableType type = getSelectedFilterType();
				if (type == TableType.ALL || type == TableType.TABLE) {
					showSystemTableCheckBox.setEnabled(true);
					showAliasCheckBox.setEnabled(true);
				} else {
					showSystemTableCheckBox.setEnabled(false);
					showAliasCheckBox.setEnabled(false);
				}
			}
		});
	}

	protected List<TableType> getTableTypes(boolean supportsProcedure) {
		List<TableType> types = new ArrayList<>();

		// Populate the Types of Data bases objects which can be retrieved
		types.add(TableType.ALL);
		types.add(TableType.TABLE);
		types.add(TableType.VIEW);
		if (supportsProcedure) {
			types.add(TableType.PROCEDURE);
		}
		return types;
	}

	/**
	 *
	 * @return The Type of the object selected in the type combo
	 */
	private TableType getSelectedFilterType() {
		IStructuredSelection selection = (IStructuredSelection) filterComboViewer.getSelection();
		if (selection != null && selection.getFirstElement() != null) {
			return (TableType) selection.getFirstElement();
		}
		return TableType.ALL;
	}

	/**
	 *
	 */
	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi
	// transformations (if required)
	private void addFetchDbObjectListener(final String metadataBidiFormatStr) {

		availableDbObjectsTree.addListener(SWT.Expand, new Listener() {

			/*
			 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.
			 * widgets.Event)
			 */
			@Override
			public void handleEvent(final Event event) {
				TreeItem item = (TreeItem) event.item;
				BusyIndicator.showWhile(item.getDisplay(), new Runnable() {

					/*
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						// bidi_hcg: pass value of metadataBidiFormatStr
						listChildren(event, metadataBidiFormatStr);
					}
				});
			}

			/**
			 * @param event
			 */
			// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi
			// transformations (if required)
			private void listChildren(Event event, String metadataBidiFormatStr) {
				TreeItem item = (TreeItem) event.item;
				IDBNode node = (IDBNode) item.getData();
				if (node instanceof ChildrenAllowedNode) {
					ChildrenAllowedNode parent = (ChildrenAllowedNode) node;
					if (!parent.isChildrenPrepared()) {
						item.removeAll();
						// bidi_hcg: pass value of metadataBidiFormatStr
						parent.prepareChildren(fc, SQLDataSetEditorPage.this.timeOutLimit * 1000);
						if (parent.getChildren() != null) {
							for (IDBNode child : parent.getChildren()) {
								// bidi_hcg: pass value of metadataBidiFormatStr
								// to child element
								DBNodeUtil.createTreeItem(item, child, metadataBidiFormatStr);
							}
						}
					}
				}
			}
		});
	}

	/**
	 * Adds drag support to tree..Must set tree before execution.
	 */
	private void addDragSupportToTree() {
		DragSource dragSource = new DragSource(availableDbObjectsTree, DND.DROP_COPY);
		dragSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceAdapter() {

			private String textToInsert;

			@Override
			public void dragStart(DragSourceEvent event) {
				event.doit = false;
				this.textToInsert = getTextToInsert();
				if (textToInsert.length() > 0) {
					event.doit = true;
				}
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.dnd.DragSourceAdapter#dragSetData(org.eclipse
			 * .swt.dnd.DragSourceEvent)
			 */
			@Override
			public void dragSetData(DragSourceEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = textToInsert;
				}
			}
		});
	}

	private String getTextToInsert() {
		TreeItem[] selection = availableDbObjectsTree.getSelection();
		StringBuilder data = new StringBuilder();
		if (selection != null && selection.length > 0) {
			for (int i = 0; i < selection.length; i++) {
				IDBNode dbNode = (IDBNode) selection[i].getData();
				// bidi_hcg: pass value of metadataBidiFormatStr
				String sql = dbNode.getQualifiedNameInSQL(identifierQuoteStringCheckBox.getSelection(),
						includeSchemaCheckBox.getSelection(), metadataBidiFormatStr);
				if (sql != null) {
					data.append(sql).append(",");
				}
			}
		}
		String result = data.toString();
		if (result.length() > 0) {
			// remove the last ","
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * Adds drop support to viewer.Must set viewer before execution.
	 */
	private void addDropSupportToViewer() {
		final StyledText text = viewer.getTextWidget();
		DropTarget dropTarget = new DropTarget(text, DND.DROP_COPY | DND.DROP_DEFAULT);
		dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {

			@Override
			public void dragEnter(DropTargetEvent event) {
				text.setFocus();
				if (event.detail == DND.DROP_DEFAULT) {
					event.detail = DND.DROP_COPY;
				}
				if (event.detail != DND.DROP_COPY) {
					event.detail = DND.DROP_NONE;
				}
			}

			@Override
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				dragEnter(event);
			}

			@Override
			public void drop(DropTargetEvent event) {
				if (event.data instanceof String && !event.data.equals("")) {
					insertText((String) event.data);
				}
			}
		});
	}

	/**
	 * Insert a text string into the text area
	 *
	 * @param text
	 */
	private void insertText(String text) {
		if (text == null) {
			return;
		}

		StyledText textWidget = viewer.getTextWidget();
		int selectionStart = textWidget.getSelection().x;
		textWidget.insert(text);
		textWidget.setSelection(selectionStart + text.length());
		textWidget.setFocus();
	}

	/**
	 * Creates the textual query editor
	 *
	 * @param parent
	 */
	private Control createTextualQueryComposite(Composite parent) {

		Composite composite = new Composite(parent, SWT.FILL | SWT.LEFT_TO_RIGHT);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label queryTextLabel = new Label(composite, SWT.NONE);
		queryTextLabel.setText(JdbcPlugin.getResourceString("tablepage.label.queryText"));//$NON-NLS-1$

		CompositeRuler ruler = new CompositeRuler();
		LineNumberRulerColumn lineNumbers = new LineNumberRulerColumn();
		ruler.addDecorator(0, lineNumbers);
		viewer = new SQLSourceViewer(composite, ruler, SWT.H_SCROLL | SWT.V_SCROLL);
		SourceViewerConfiguration svc = new SQLSourceViewerConfiguration(dataSetDesign.getDataSourceDesign(),
				timeOutLimit * 1000, enableCodeAssist);
		viewer.configure(svc);

		// Find query text at source viewer
		findQueryTextGroup = new Group(composite, SWT.FILL);
		findQueryTextGroup.setText("Find"); //$NON-NLS-1$
		GridLayout findQueryTextGroupLayout = new GridLayout();
		findQueryTextGroupLayout.verticalSpacing = 10;
		findQueryTextGroupLayout.numColumns = 4;
		findQueryTextGroup.setLayout(findQueryTextGroupLayout);
		GridData fOptionGroupData = new GridData(GridData.FILL_HORIZONTAL);
		findQueryTextGroup.setLayoutData(fOptionGroupData);

		setupFindQueryTextBox(findQueryTextGroup);

		setupFindQueryTextButtons(findQueryTextGroup);

		setupFindQueryTextOptions(findQueryTextGroup);

		doc = new Document(getQueryText());
		FastPartitioner partitioner = new FastPartitioner(new SQLPartitionScanner(), new String[] {
				SQLPartitionScanner.QUOTE_STRING, SQLPartitionScanner.COMMENT, IDocument.DEFAULT_CONTENT_TYPE });
		partitioner.connect(doc);
		doc.setDocumentPartitioner(partitioner);
		viewer.setDocument(doc);
		viewer.getTextWidget().setFont(JFaceResources.getTextFont());
		viewer.getTextWidget().addBidiSegmentListener(new BidiSegmentListener() {

			/*
			 * @see org.eclipse.swt.custom.BidiSegmentListener#lineGetSegments
			 * (org.eclipse.swt.custom.BidiSegmentEvent)
			 */
			@Override
			public void lineGetSegments(BidiSegmentEvent event) {
				event.segments = SQLUtility.getBidiLineSegments(event.lineText);
			}
		});
		attachMenus(viewer);

		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 500;
		viewer.getControl().setLayoutData(data);

		// Add drop support to the viewer
		addDropSupportToViewer();

		// Add support of additional accelerated key
		viewer.getTextWidget().addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (isUndoKeyPress(e)) {
					viewer.doOperation(ITextOperationTarget.UNDO);
				} else if (isRedoKeyPress(e)) {
					viewer.doOperation(ITextOperationTarget.REDO);
				} else if (isFindQueryText(e)) {
					findQueryText.setFocus();
				} else if (isFindQueryTextForward(e)) {
					findQueryTextForward();
				} else if (isFindQueryTextBackward(e)) {
					findQueryTextBackward();
				}
			}

			private boolean isUndoKeyPress(KeyEvent e) {
				// CTRL + z
				return (e.stateMask == SWT.CONTROL && e.keyCode == 'z');
			}

			private boolean isRedoKeyPress(KeyEvent e) {
				// CTRL + y
				return (e.stateMask == SWT.CONTROL && e.keyCode == 'y');
			}

			private boolean isFindQueryText(KeyEvent e) {
				// CTRL + f
				return (e.stateMask == SWT.CONTROL && e.keyCode == 'f');
			}

			private boolean isFindQueryTextForward(KeyEvent e) {
				// CTRL + o
				return (e.stateMask == SWT.CONTROL && e.keyCode == 'o');
			}

			private boolean isFindQueryTextBackward(KeyEvent e) {
				// CTRL + b
				return (e.stateMask == SWT.CONTROL && e.keyCode == 'b');
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		return composite;
	}

	/**
	 *
	 * @param viewer
	 */
	private final void attachMenus(SourceViewer viewer) {
		StyledText widget = viewer.getTextWidget();
		TextMenuManager menuManager = new TextMenuManager(viewer);
		widget.setMenu(menuManager.getContextMenu(widget));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.datatools.connectivity.oda.design.internal.ui.
	 * DataSetWizardPageCore
	 * #refresh(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	@Override
	protected void refresh(DataSetDesign dataSetDesign) {
		this.dataSetDesign = dataSetDesign;
		initializeControl();
	}

	/*
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		getControl().setFocus();
	}

	/**
	 * return the query text. If the query text is empty then return the pre-defined
	 * pattern
	 *
	 * @return
	 */
	private String getQueryText() {
		String queryText = dataSetDesign.getQueryText();
		if (queryText != null && queryText.trim().length() > 0) {
			return queryText;
		}

		return SQLUtility.getQueryPresetTextString(this.dataSetDesign.getOdaExtensionDataSetId());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #cleanup()
	 */
	@Override
	protected void cleanup() {
		JdbcMetaDataProvider.release();
		if (odaConnectionProvider != null) {
			odaConnectionProvider.release();
			odaConnectionProvider = null;
		}
		dataSetDesign = null;
	}

	private void insertTreeItemText() {
		String text = getTextToInsert();
		if (text.length() > 0) {
			insertText(text);
		}
	}

	private void setupFindQueryTextBox(Group group) {
		findQueryText = new Text(group, SWT.BORDER);
		findQueryText.setToolTipText(JdbcPlugin.getResourceString("tablepage.querytext.find.text.tooltip"));
		GridData findQueryTextData = new GridData(GridData.FILL_HORIZONTAL);
		findQueryTextData.horizontalSpan = 2;
		findQueryText.setLayoutData(findQueryTextData);
		// add support of additional accelerated key
		findQueryText.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (isFindQueryTextForward(e)) {
					findQueryTextForward();
				} else if (isFindQueryTextBackward(e)) {
					findQueryTextBackward();
				}
			}

			private boolean isFindQueryTextForward(KeyEvent e) {
				// CTRL + o
				return (e.stateMask == SWT.CONTROL && e.keyCode == 'o');
			}

			private boolean isFindQueryTextBackward(KeyEvent e) {
				// CTRL + b
				return (e.stateMask == SWT.CONTROL && e.keyCode == 'b');
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});

	}

	private void setupFindQueryTextOptions(Group group) {

		GridData csLayoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		findQueryTextCaseSensitive = new Button(group, SWT.CHECK);
		findQueryTextCaseSensitive
				.setText(JdbcPlugin.getResourceString("tablepage.querytext.find.option.case.sensitive")); //$NON-NLS-1$
		findQueryTextCaseSensitive
				.setToolTipText(JdbcPlugin.getResourceString("tablepage.querytext.find.option.case.sensitive.tooltip")); //$NON-NLS-1$
		findQueryTextCaseSensitive.setSelection(false);
		findQueryTextCaseSensitive.setLayoutData(csLayoutData);
		findQueryTextCaseSensitive.setEnabled(true);

		GridData wwLayoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		findQueryTextWholeWord = new Button(group, SWT.CHECK);
		findQueryTextWholeWord.setText(JdbcPlugin.getResourceString("tablepage.querytext.find.option.whole.word")); //$NON-NLS-1$
		findQueryTextWholeWord
				.setToolTipText(JdbcPlugin.getResourceString("tablepage.querytext.find.option.whole.word.tooltip")); //$NON-NLS-1$
		findQueryTextWholeWord.setSelection(false);
		findQueryTextWholeWord.setLayoutData(wwLayoutData);
		findQueryTextWholeWord.setEnabled(true);
	}

	private void setupFindQueryTextButtons(Group group) {

		GridData fwButtonFindTextLayoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		Button fwButtonFindText = new Button(group, SWT.BUTTON1);
		fwButtonFindText.setText(
				FIND_DIRECTION_SYMBOL_FORWARD + " " + JdbcPlugin.getResourceString("tablepage.querytext.find.button.forward")); //$NON-NLS-1$
		fwButtonFindText.setToolTipText(JdbcPlugin.getResourceString("tablepage.querytext.find.button.forward.tooltip")); //$NON-NLS-1$
		fwButtonFindText.setLayoutData(fwButtonFindTextLayoutData);
		fwButtonFindText.setEnabled(true);
		// Add listener to the find query button "forward"
		fwButtonFindText.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				findQueryTextForward();
			}
		});

		GridData bwButtonFindTextLayoutData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		Button bwButtonFindText = new Button(group, SWT.BUTTON1);

		bwButtonFindText.setText(FIND_DIRECTION_SYMBOL_BACKWARD + " " //$NON-NLS-1$
				+ JdbcPlugin.getResourceString("tablepage.querytext.find.button.backward"));
		bwButtonFindText.setToolTipText(JdbcPlugin.getResourceString("tablepage.querytext.find.button.backward.tooltip")); //$NON-NLS-1$
		bwButtonFindText.setLayoutData(bwButtonFindTextLayoutData);
		bwButtonFindText.setEnabled(true);
		// Add listener to the find query button "forward"
		bwButtonFindText.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				findQueryTextBackward();
			}
		});
	}

	/**
	 * Setup the group label of the query text search option
	 *
	 * @param findResultMode mode of the search result
	 */
	private void setupLabelFindQueryTextGroup(int findResultMode) {
		String groupLabel = JdbcPlugin.getResourceString("tablepage.querytext.find.label");

		if (findResultMode == 1 || findResultMode == 2) {
			findQueryTextGroup.setForeground(JFaceColors.getErrorText(findQueryTextGroup.getDisplay()));
			groupLabel += ", " + JdbcPlugin.getResourceString(
					"tablepage.querytext.find." + (findResultMode == 1 ? "forward" : "backward") + ".unlocated.label");
		} else {
			findQueryTextGroup.setForeground(findQueryText.getForeground());
		}

		findQueryTextGroup.setText(groupLabel);
	}

	private void findQueryTextForward() {
		if (findQueryText != null) {
			boolean found = viewer.findQueryText(findQueryText.getText(), true,
					findQueryTextCaseSensitive.getSelection(), findQueryTextWholeWord.getSelection());
			setupLabelFindQueryTextGroup(found ? 0 : 1);
		}
	}

	private void findQueryTextBackward() {
		if (findQueryText != null) {
			boolean found = viewer.findQueryText(findQueryText.getText(), false,
					findQueryTextCaseSensitive.getSelection(), findQueryTextWholeWord.getSelection());
			setupLabelFindQueryTextGroup(found ? 0 : 2);
		}
	}
}
