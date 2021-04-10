/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.IHelpConstants;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage;
import org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPageContainer;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.JoinConditionHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.JoinCondition;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * This page is to define joint data set.Select one data set and column from
 * each side to define a join data set
 */

public class JointDataSetPage extends WizardPage implements ISelectionChangedListener, IPropertyPage {

	private final static transient String seperator = "::"; //$NON-NLS-1$

	private transient ComboViewer leftDataSetChooser = null;
	private transient ComboViewer rightDataSetChooser = null;
	private transient ListViewer leftColumnList = null;
	private transient ListViewer rightColumnList = null;
	private transient DataSetHandle leftHandle = null;
	private transient DataSetHandle rightHandle = null;

	private transient Group leftGroup;
	private transient Group centerGroup;
	private transient Group rightGroup;

	private transient Button innerJoinButton;
	private transient Button leftOuterJoinButton;
	private transient Button rightOuterJoinButton;
	private transient Button fullOuterJoinButton;

	private transient Composite topComposite;
	private transient Composite bottomComposite;

	private transient List dataSetList;
	private boolean leftSelected = true;
	private String leftColumnSelection;
	private String rightColumnSelection;
	private String leftDataSetName;
	private String rightDataSetName;

	private boolean selectionChanged = false;

	private IPropertyPageContainer propertyPageContainer;
	private Text nameEditor;
	private Label nameLabel;
	final private static String EMPTY_NAME = Messages.getString("error.DataSet.emptyName");//$NON-NLS-1$
	final private static String DUPLICATE_NAME = Messages.getString("error.duplicateName");//$NON-NLS-1$
	final private static String CREATE_PROMPT = Messages.getString("dataset.message.create");//$NON-NLS-1$

	final private static String TEXT_JOINTYYPE = Messages.getString("JointDataSetPage.joinType");//$NON-NLS-1$
	final private static String TEXT_INNORJOIN = Messages.getString("JointDataSetPage.button.innerJoin");//$NON-NLS-1$
	final private static String TEXT_LEFTJOIN = Messages.getString("JointDataSetPage.button.leftJoin");//$NON-NLS-1$
	final private static String TEXT_RIGHTJOIN = Messages.getString("JointDataSetPage.button.rightJoin");//$NON-NLS-1$
	final private static String TEXT_FULLJOIN = Messages.getString("JointDataSetPage.button.fullJoin");//$NON-NLS-1$

	private String joinType;

	final private static int LEFT_DATASET = 0;
	final private static int RIGHT_DATASET = -1;
	private PropertyHandle propertyHandle;

	private PropertyHandle columnHintHandle;

	private static Logger logger = Logger.getLogger(JointDataSetPage.class.getName());

	public JointDataSetPage(String pageName) {
		super(pageName);
		setTitle(pageName);// $NON-NLS-1$
		setPageMessage(Messages.getString("JointDataSetPage.page.detail"), IMessageProvider.NONE); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		setControl(this.createPageControl(parent));
		setPageComplete(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createPageControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		dataSetList = getDataSets();

		if (getPageDescription() != null) {
			Label pageDescription = new Label(composite, SWT.NONE);
			pageDescription.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			pageDescription.setText(getPageDescription());
			pageDescription.setToolTipText(getPageDescription());
		}

		topComposite = new Composite(composite, SWT.NONE);
		createTopComposite(topComposite);

		bottomComposite = new Composite(composite, SWT.NONE);
		createBottomComposite(bottomComposite);

		joinType = DesignChoiceConstants.JOIN_TYPE_INNER;

		selectionChanged = false;

		Utility.setSystemHelp(composite, IHelpConstants.CONEXT_ID_JOINTDATASET);
		return composite;
	}

	/**
	 * create top composite for page
	 * 
	 * @param parent
	 */
	private void createTopComposite(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		parent.setLayout(layout);

		GridData data = new GridData(GridData.FILL_BOTH);
		parent.setLayoutData(data);

		createLeftGroup(parent);
		createCenterGroup(parent);
		createRightGroup(parent);
	}

	/**
	 * create left composite for page
	 * 
	 * @param composite
	 */
	private void createLeftGroup(Composite composite) {
		leftGroup = new Group(composite, SWT.NONE);
		leftGroup.setLayout(new FormLayout());
		leftGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

		FormData data = new FormData();

		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(0, 5);
		data.right = new FormAttachment(100, -5);
		data.bottom = new FormAttachment(10, -5);

		leftDataSetChooser = new ComboViewer(leftGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		leftDataSetChooser.getCombo().setLayoutData(data);
		DataSetComboProvider provider = new DataSetComboProvider();
		leftDataSetChooser.setContentProvider(provider);
		leftDataSetChooser.setLabelProvider(provider);
		leftDataSetChooser.setInput(dataSetList);
		leftDataSetChooser.addSelectionChangedListener(this);

		data = new FormData();

		data.top = new FormAttachment(leftDataSetChooser.getCombo(), 10);
		data.left = new FormAttachment(0, 5);
		data.right = new FormAttachment(100, -5);
		data.bottom = new FormAttachment(100, -5);

		leftColumnList = new ListViewer(leftGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		leftColumnList.getControl().setLayoutData(data);
		ColumnProvider colProvider = new ColumnProvider();
		leftColumnList.setContentProvider(colProvider);
		leftColumnList.setLabelProvider(colProvider);
		leftColumnList.addSelectionChangedListener(this);

	}

	/**
	 * create center composite for page
	 * 
	 * @param composite
	 */
	private void createCenterGroup(Composite composite) {
		centerGroup = new Group(composite, SWT.NONE);
		centerGroup.setLayout(new GridLayout());
		GridData data = new GridData(GridData.FILL_VERTICAL);
		data.verticalAlignment = SWT.CENTER;
		centerGroup.setLayoutData(data);

		centerGroup.setText(TEXT_JOINTYYPE);
		createRadioButtonList(centerGroup);

	}

	/**
	 * create right composite for page
	 * 
	 * @param composite
	 */
	private void createRightGroup(Composite composite) {
		rightGroup = new Group(composite, SWT.NONE);
		rightGroup.setLayout(new FormLayout());
		rightGroup.setLayoutData(new GridData(GridData.FILL_BOTH));

		FormData data = new FormData();

		data.top = new FormAttachment(0, 5);
		data.left = new FormAttachment(0, 5);
		data.right = new FormAttachment(100, -5);
		data.bottom = new FormAttachment(10, -5);
		rightDataSetChooser = new ComboViewer(rightGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		rightDataSetChooser.getControl().setLayoutData(data);
		DataSetComboProvider provider = new DataSetComboProvider();
		rightDataSetChooser.setContentProvider(provider);
		rightDataSetChooser.setLabelProvider(provider);
		rightDataSetChooser.setInput(dataSetList);
		rightDataSetChooser.addSelectionChangedListener(this);

		data = new FormData();
		data.top = new FormAttachment(rightDataSetChooser.getControl(), 10);
		data.left = new FormAttachment(0, 5);
		data.right = new FormAttachment(100, -5);
		data.bottom = new FormAttachment(100, -5);
		rightColumnList = new ListViewer(rightGroup, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		rightColumnList.getControl().setLayoutData(data);
		ColumnProvider colProvider = new ColumnProvider();
		rightColumnList.setContentProvider(colProvider);
		rightColumnList.setLabelProvider(colProvider);
		rightColumnList.addSelectionChangedListener(this);
	}

	/**
	 * create bottom composite for page
	 * 
	 * @param parent
	 */
	private void createBottomComposite(Composite parent) {
		// initialize the dialog layout
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		parent.setLayout(layout);

		nameLabel = new Label(parent, SWT.RIGHT);
		nameLabel.setText(Messages.getString("dataset.wizard.label.datasetName"));//$NON-NLS-1$
		nameEditor = new Text(parent, SWT.BORDER);
		String name = ReportPlugin.getDefault().getCustomName(ReportDesignConstants.DATA_SET_ELEMENT);
		if (name != null) {
			nameEditor.setText(Utility.getUniqueDataSetName(name));
		} else
		// can't get defaut name
		{
			nameEditor.setText(Utility.getUniqueDataSetName(Messages.getString("dataset.new.defaultName")));//$NON-NLS-1$
		}

		nameEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameEditor.setToolTipText(Messages.getString("DataSetBasePage.tooltip")); //$NON-NLS-1$
		nameEditor.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (StringUtil.isBlank(nameEditor.getText().trim())) {
					setPageMessage(EMPTY_NAME, ERROR);
					setPageComplete(false);
				} else if (isDuplicateName()) {// name is duplicated
					setPageMessage(DUPLICATE_NAME, ERROR);
					setPageComplete(false);
				} else if (containInvalidCharactor(nameEditor.getText())) {// name contains invalid ".", "/", "\", "!",
																			// ";", "," charactor
					String msg = Messages.getFormattedString("error.invalidName", //$NON-NLS-1$
							new Object[] { nameEditor.getText() });
					setMessage(msg, ERROR);
					setPageComplete(false);
				} else {// everything is OK
					setPageComplete(canPageComplete());
					setPageMessage(CREATE_PROMPT, NONE);
				}
			}
		});

	}

	/**
	 * cread radio button list for center composite
	 * 
	 * @param composite
	 */
	private void createRadioButtonList(Composite composite) {
		GridData data = new GridData();
		data.grabExcessVerticalSpace = true;
		innerJoinButton = new Button(composite, SWT.RADIO | SWT.WRAP);
		innerJoinButton.setText(TEXT_INNORJOIN);
		innerJoinButton.setLayoutData(data);
		innerJoinButton.setSelection(true);

		data = new GridData();
		leftOuterJoinButton = new Button(composite, SWT.RADIO | SWT.WRAP);
		leftOuterJoinButton.setText(TEXT_LEFTJOIN);
		leftOuterJoinButton.setLayoutData(data);

		data = new GridData();
		rightOuterJoinButton = new Button(composite, SWT.RADIO | SWT.WRAP);
		rightOuterJoinButton.setText(TEXT_RIGHTJOIN);
		rightOuterJoinButton.setLayoutData(data);

		data = new GridData();
		data.grabExcessVerticalSpace = true;
		fullOuterJoinButton = new Button(composite, SWT.RADIO | SWT.WRAP);
		fullOuterJoinButton.setText(TEXT_FULLJOIN);
		fullOuterJoinButton.setLayoutData(data);

		RadioSelectionLister listener = new RadioSelectionLister();
		innerJoinButton.addSelectionListener(listener);
		leftOuterJoinButton.addSelectionListener(listener);
		rightOuterJoinButton.addSelectionListener(listener);
		fullOuterJoinButton.addSelectionListener(listener);
	}

	/**
	 * checks if the name is duplicate
	 * 
	 * @return Returns true if the name is duplicate,and false if it is duplicate
	 */
	private boolean isDuplicateName() {
		String name = nameEditor.getText().trim();
		return Utility.checkDataSetName(name);
	}

	/**
	 * whether name contains ".", "/", "\", "!", ";", "," charactors
	 * 
	 * @param name
	 * @return
	 */
	private boolean containInvalidCharactor(String name) {
		if (name == null)
			return false;
		else if (name.indexOf(".") > -1 || //$NON-NLS-1$
				name.indexOf("\\") > -1 || name.indexOf("/") > -1 || //$NON-NLS-1$ //$NON-NLS-2$
				name.indexOf("!") > -1 || name.indexOf(";") > -1 || //$NON-NLS-1$ //$NON-NLS-2$
				name.indexOf(",") > -1) //$NON-NLS-1$
			return true;
		else
			return false;
	}

	/**
	 * get all data set list including those that should be referenced from librarys
	 * 
	 * @return the list of data set
	 */
	private List getDataSets() {
		List dataSets = Utility.getVisibleDataSets();
		List relative = new ArrayList();

		Iterator handleIter = dataSets.iterator();
		Object handleObj;
		while (handleIter.hasNext()) {
			handleObj = handleIter.next();
			if (handleObj instanceof JointDataSetHandle) {
				List nameList = ((JointDataSetHandle) handleObj).getDataSetNames();
				for (int j = 0; j < nameList.size(); j++) {
					DataSetHandle dataSet = Utility.findDataSet(nameList.get(j).toString());

					if (dataSet != null && dataSet.getModuleHandle() instanceof LibraryHandle)
						relative.add(dataSet);
				}
			}
		}

		for (int i = 0; i < relative.size(); i++) {
			if (!dataSets.contains(relative.get(i)))
				dataSets.add(relative.get(i));
		}
		return dataSets;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		selectionChanged = true;

		if (event.getSource() instanceof ComboViewer) {
			this.setPageMessage(Messages.getString("JointDataSetPage.page.detail"), //$NON-NLS-1$
					IMessageProvider.NONE);
			if (((ComboViewer) event.getSource()).equals(rightDataSetChooser))
				leftSelected = false;
			else
				leftSelected = true;
			if (((IStructuredSelection) event.getSelection()).getFirstElement() instanceof DataSetHandle) {
				DataSetHandle handle = (DataSetHandle) ((IStructuredSelection) event.getSelection()).getFirstElement();
				if (leftSelected) {
					leftDataSetName = handle.getQualifiedName();
					leftHandle = handle;
				} else {
					rightDataSetName = handle.getQualifiedName();
					rightHandle = handle;
				}
				DataSetViewData[] columsItems = null;
				try {
					DataSessionContext context = new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION,
							handle.getModuleHandle());
					DataRequestSession session = DataRequestSession.newSession(context);

					columsItems = DataSetProvider.getCurrentInstance().populateAllCachedMetaData(handle, session);
					populateColumns(columsItems);
					session.shutdown();
				} catch (BirtException e) {
					ExceptionHandler.handle(e);
				}
			}
		} else if (event.getSource() instanceof ListViewer) {
			if (((ListViewer) event.getSource()).equals(rightColumnList))
				leftSelected = false;
			else
				leftSelected = true;
			if (((IStructuredSelection) event.getSelection()).getFirstElement() instanceof DataSetViewData) {
				DataSetViewData itemModel = (DataSetViewData) ((IStructuredSelection) event.getSelection())
						.getFirstElement();
				if (leftSelected)
					leftColumnSelection = itemModel.getName();
				else
					rightColumnSelection = itemModel.getName();
			}
		}
		if (!this.nameEditor.isDisposed())
			setPageComplete(canPageComplete());
	}

	/**
	 * populate columns for the selection changed of comboViewer
	 * 
	 * @param columsItems
	 */
	private void populateColumns(DataSetViewData[] columsItems) {
		if (leftSelected) {
			this.leftColumnList.setInput(columsItems);
			if (columsItems != null && leftColumnList.getElementAt(0) != null) {
				this.leftColumnList.setSelection(new StructuredSelection(leftColumnList.getElementAt(0)));
				this.leftColumnSelection = ((DataSetViewData) leftColumnList.getElementAt(0)).getName();
			}
		} else {
			this.rightColumnList.setInput(columsItems);
			if (columsItems != null && rightColumnList.getElementAt(0) != null) {
				this.rightColumnList.setSelection(new StructuredSelection(rightColumnList.getElementAt(0)));
				this.rightColumnSelection = ((DataSetViewData) rightColumnList.getElementAt(0)).getName();
			}
		}
	}

	/**
	 * create joint datasetHandle
	 * 
	 * @return
	 */
	public DataSetHandle createSelectedDataSet() {
		try {
			DataSetHandle dataSetHandle = createJointDataSet();
			DesignElementHandle parentHandle = Utility.getReportModuleHandle();
			//
			SlotHandle slotHandle = ((ModuleHandle) parentHandle).getDataSets();
			parentHandle.addElement(dataSetHandle, slotHandle.getSlotID());

			return dataSetHandle;
		} catch (SemanticException e) {
			logger.log(Level.FINE, e.getMessage(), e);
		}

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * create joint data set
	 * 
	 * @return
	 * @throws SemanticException
	 */
	private DataSetHandle createJointDataSet() throws SemanticException {

		String dataSetName = ""; //$NON-NLS-1$
		if (nameEditor != null)
			dataSetName = nameEditor.getText();
		JointDataSetHandle dsHandle = Utility.newJointDataSet(dataSetName);
		if (leftDataSetName.equals(rightDataSetName))
			dsHandle.addDataSet(leftDataSetName);
		else {
			dsHandle.addDataSet(leftDataSetName);
			dsHandle.addDataSet(rightDataSetName);
		}

		setParameters(dsHandle);

		propertyHandle = dsHandle.getPropertyHandle(JointDataSet.JOIN_CONDITONS_PROP);
		propertyHandle.addItem(createJoinCondition());

		addColumnHints(dsHandle);

		return dsHandle;
	}

	private void addColumnHints(JointDataSetHandle dsHandle) throws SemanticException {
		columnHintHandle = dsHandle.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP);
		if (columnHintHandle == null)
			return;

		columnHintHandle.clearValue();

		List<ColumnHint> rightColumns = new ArrayList<ColumnHint>();
		HashMap<String, ColumnHint> resultMap = new HashMap<String, ColumnHint>();

		String leftDsName = this.leftDataSetName;
		String rightDsName = this.rightDataSetName;

		try {
			DataSessionContext context;
			context = new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION, leftHandle.getModuleHandle());
			DataRequestSession session = DataRequestSession.newSession(context);
			IResultMetaData leftMetaData = session.getDataSetMetaData(leftHandle, false);

			IResultMetaData righMetaData;

			if (this.leftDataSetName != null && this.leftDataSetName.equalsIgnoreCase(this.rightDataSetName)) {
				leftDsName = leftDsName + "1";
				rightDsName = rightDsName + "2";
				righMetaData = leftMetaData;
			} else {
				righMetaData = session.getDataSetMetaData(rightHandle, false);
			}

			for (int i = 1; i <= leftMetaData.getColumnCount(); i++) {
				ColumnHint item = createColumnHint(leftMetaData, i, leftDsName);
				resultMap.put((String) item.getProperty(null, ColumnHint.ALIAS_MEMBER), item);
			}

			for (int i = 1; i <= righMetaData.getColumnCount(); i++) {
				ColumnHint item = createColumnHint(righMetaData, i, rightDsName);
				rightColumns.add(item);
			}
			session.shutdown();
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
		}

		resetColumnHintAlias(rightColumns, resultMap, leftDsName, rightDsName);

		for (Iterator<ColumnHint> iter = resultMap.values().iterator(); iter.hasNext();) {
			ColumnHint item = iter.next();
			if (item != null)
				columnHintHandle.addItem(item);
		}
	}

	private void resetColumnHintAlias(List<ColumnHint> right, HashMap<String, ColumnHint> resultMap, String leftDsName,
			String rightDsName) throws SemanticException {
		for (int i = 0; i < right.size(); i++) {
			ColumnHint item = right.get(i);
			String columnAlias = (String) item.getProperty(null, ColumnHint.ALIAS_MEMBER);
			if (columnAlias == null)
				columnAlias = (String) item.getProperty(null, ColumnHint.COLUMN_NAME_MEMBER);

			if (resultMap.containsKey(columnAlias)) {
				ColumnHint oldItem = resultMap.get(columnAlias);
				if (oldItem != null) {
					String newAlias = leftDsName + seperator + columnAlias;
					oldItem.setProperty(ColumnHint.ALIAS_MEMBER, newAlias);
					resultMap.put(newAlias, oldItem);

					resultMap.put(columnAlias, null);

					String currentAlias = rightDsName + seperator + columnAlias;
					item.setProperty(ColumnHint.ALIAS_MEMBER, currentAlias);
					resultMap.put(currentAlias, item);
				}
			} else {
				item.setProperty(ColumnHint.ALIAS_MEMBER, columnAlias);
				resultMap.put(columnAlias, item);
			}
		}
	}

	private ColumnHint createColumnHint(IResultMetaData item, int index, String dsName) throws BirtException {
		ColumnHint column = new ColumnHint();
		column.setProperty(ColumnHint.COLUMN_NAME_MEMBER, dsName + seperator + item.getColumnName(index));
		column.setProperty(ColumnHint.ALIAS_MEMBER,
				item.getColumnAlias(index) == null ? item.getColumnName(index) : item.getColumnAlias(index));
		column.setProperty(ColumnHint.DISPLAY_NAME_MEMBER, dsName + seperator + item.getColumnName(index));
		return column;
	}

	/**
	 * Add the parameters of left and right dataset to JointDatasetHandle.
	 * 
	 * @param dsHandle
	 * @throws SemanticException
	 */
	private void setParameters(JointDataSetHandle dsHandle) throws SemanticException {
		List<DataSetParameter> params = null;
		PropertyHandle dsParameterHandle = dsHandle.getPropertyHandle(IDataSetModel.PARAMETERS_PROP);
		if (leftDataSetName.equals(rightDataSetName)) {
			params = getDataSetParameters(leftDataSetName + "1",
					leftHandle.getPropertyHandle(IDataSetModel.PARAMETERS_PROP), rightDataSetName + "2",
					rightHandle.getPropertyHandle(IDataSetModel.PARAMETERS_PROP));
		} else {
			params = getDataSetParameters(leftDataSetName, leftHandle.getPropertyHandle(IDataSetModel.PARAMETERS_PROP),
					rightDataSetName, rightHandle.getPropertyHandle(IDataSetModel.PARAMETERS_PROP));
		}
		if (params.size() == 0) // parameter count turning to 0 case
		{
			dsParameterHandle.clearValue();
		} else {
			Iterator iter = dsParameterHandle.iterator();
			int i = 0;

			while (iter.hasNext() && i < params.size()) {
				DataSetParameterHandle parameterHandle = (DataSetParameterHandle) iter.next();
				updateDataSetParameterHandle(parameterHandle, params.get(i));
				i++;
			}

			// parameter count decreasing case
			if (dsParameterHandle.getListValue() != null)
				while (i < dsParameterHandle.getListValue().size()) {
					dsParameterHandle.removeItem(dsParameterHandle.getListValue().size() - 1);
				}

			// parameter count increasing case
			for (; i < params.size(); i++) {
				dsParameterHandle.addItem(params.get(i));
			}
		}
	}

	private static void updateDataSetParameterHandle(DataSetParameterHandle handle, DataSetParameter param) {
		try {
			handle.setDataType(param.getDataType());
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
		handle.setAllowNull(param.allowNull());
		handle.setDefaultValue(param.getDefaultValue());
		handle.setIsInput(param.isInput());
		handle.setIsOutput(param.isOutput());
		try {
			handle.setName(param.getName());
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
		handle.setIsOptional(param.isOptional());
		handle.setPosition(param.getPosition());
	}

	private static List<DataSetParameter> getDataSetParameters(String dataSetName1, PropertyHandle parameterHandle1,
			String dataSetName2, PropertyHandle parameterHandle2) {
		List<DataSetParameter> params1 = getDataSetParameters(dataSetName1, parameterHandle1, 0);
		List<DataSetParameter> params2 = getDataSetParameters(dataSetName2, parameterHandle2, params1.size());
		params1.addAll(params2);
		return params1;
	}

	private static List<DataSetParameter> getDataSetParameters(String dataSetName, PropertyHandle parameterHandle,
			int startPosition) {
		Iterator paramterIterator = parameterHandle.iterator();
		List<DataSetParameter> result = new ArrayList<DataSetParameter>();
		int position = startPosition;
		while (paramterIterator.hasNext()) {
			DataSetParameterHandle paramter = (DataSetParameterHandle) (paramterIterator.next());
			DataSetParameter dataSetParameter = toDataSetParameter((DataSetParameter) paramter.getStructure(),
					dataSetName, position);
			position++;
			result.add(dataSetParameter);
		}
		return result;
	}

	/**
	 * 
	 * @param parameter
	 * @return
	 */
	private static DataSetParameter toDataSetParameter(DataSetParameter parameter, String dataSetName,
			Integer position) {
		DataSetParameter dataSetParameter = new DataSetParameter();
		dataSetParameter.setDataType(parameter.getDataType());
		dataSetParameter.setAllowNull(parameter.allowNull());
		dataSetParameter.setDefaultValue(parameter.getDefaultValue());
		dataSetParameter.setIsInput(parameter.isInput());
		dataSetParameter.setIsOutput(parameter.isOutput());
		dataSetParameter.setName(getParameterName(dataSetName, parameter.getName()));
		dataSetParameter.setIsOptional(parameter.isOptional());
		dataSetParameter.setPosition(position);
		return dataSetParameter;
	}

	/**
	 * 
	 * @param dataSetName
	 * @param sourceParameterName
	 * @return
	 */
	public static String getParameterName(String dataSetName, String sourceParameterName) {
		return dataSetName + seperator + sourceParameterName;
	}

	/**
	 * Create joint condition
	 * 
	 * @return
	 * @see org.eclipse.birt.report.model.api.elements.structures.JoinCondition
	 */
	private JoinCondition createJoinCondition() {
		JoinCondition joinCondition = new JoinCondition();
		joinCondition.setJoinType(joinType);
		// TODO
		joinCondition.setOperator("eq"); //$NON-NLS-1$
		joinCondition.setLeftDataSet(leftDataSetName);
		joinCondition.setRightDataSet(rightDataSetName);
		joinCondition.setLeftExpression(ExpressionUtil.createJSDataSetRowExpression(this.leftColumnSelection));
		joinCondition.setRightExpression(ExpressionUtil.createJSDataSetRowExpression(this.rightColumnSelection));
		return joinCondition;
	}

	/**
	 * Whether the page can complete
	 * 
	 * @return
	 */
	private boolean canPageComplete() {
		if (leftDataSetName != null && rightDataSetName != null && leftColumnSelection != null
				&& rightColumnSelection != null && !nameEditor.isDisposed()
				&& !StringUtil.isBlank(nameEditor.getText().trim()) && !isDuplicateName())
			return true;
		else
			return false;
	}

	/**
	 * Provider class for comboViewer to view the list of data sets
	 * 
	 */
	static class DataSetComboProvider implements IStructuredContentProvider, ILabelProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return ((List) inputElement).toArray();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(Object element) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			if (element instanceof DataSetHandle) {
				return ((DataSetHandle) element).getQualifiedName();
			}
			return ""; //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
		 * .lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {
		}

	}

	/**
	 * 
	 * Provider class for listViewer to list the column items from data set
	 * 
	 */
	static class ColumnProvider implements IStructuredContentProvider, ILabelProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(
		 * java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Object[])
				return (Object[]) inputElement;
			else
				return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse
		 * .jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
		 */
		public Image getImage(Object element) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(Object element) {
			if (element instanceof DataSetViewData) {
				return ((DataSetViewData) element).getName();
			}
			return ""; //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void addListener(ILabelProviderListener listener) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
		 * .lang.Object, java.lang.String)
		 */
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		public void removeListener(ILabelProviderListener listener) {
		}

	}

	class RadioSelectionLister implements SelectionListener {

		public void widgetSelected(SelectionEvent e) {
			selectionChanged = true;

			if (e.getSource() instanceof Button) {
				if (((Button) e.getSource()).equals(innerJoinButton))
					joinType = DesignChoiceConstants.JOIN_TYPE_INNER;
				else if (((Button) e.getSource()).equals(leftOuterJoinButton))
					joinType = DesignChoiceConstants.JOIN_TYPE_LEFT_OUT;
				else if (((Button) e.getSource()).equals(rightOuterJoinButton))
					joinType = DesignChoiceConstants.JOIN_TYPE_RIGHT_OUT;
				else if (((Button) e.getSource()).equals(fullOuterJoinButton))
					joinType = DesignChoiceConstants.JOIN_TYPE_FULL_OUT;

			}
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * setContainer(org.eclipse.birt.report.designer.ui.dialogs.properties.
	 * IPropertyPageContainer)
	 */
	public void setContainer(IPropertyPageContainer parentContainer) {
		propertyPageContainer = parentContainer;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * canLeave()
	 */
	public boolean canLeave() {
		try {
			return modifyJointCondition();
		} catch (SemanticException e) {
			return false;
		}
	}

	/**
	 * 
	 * @return
	 * @throws SemanticException
	 */
	private boolean modifyJointCondition() throws SemanticException {
		JointDataSetHandle handle = null;
		if (leftDataSetName == null || rightDataSetName == null)
			return false;

		if (propertyPageContainer instanceof DataSetEditor) {
			handle = (JointDataSetHandle) ((DataSetEditor) propertyPageContainer).getModel();
			if (selectionChanged) {
				selectionChanged = false;

				List datasetName = handle.getDataSetNames();
				for (int i = 0; i < datasetName.size(); i++) {
					try {
						handle.removeDataSet((String) datasetName.get(i));
					} catch (SemanticException e) {
						// do nothing
					}
				}
				if (leftDataSetName.equals(rightDataSetName))
					handle.addDataSet(leftDataSetName);
				else {
					handle.addDataSet(leftDataSetName);
					handle.addDataSet(rightDataSetName);
				}
				setParameters(handle);
				addColumnHints(handle);

				if (propertyHandle != null) {
					JoinCondition condition = createJoinCondition();
					propertyHandle.removeItem(0);
					propertyHandle.addItem(condition);
					return true;
				} else
					return false;
			}
		}
		return true;
	}

	/**
	 * set page description
	 * 
	 * @return
	 */
	private String getPageDescription() {
		return Messages.getString("JointDataSetPage.pageDescription"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * performOk()
	 */
	public boolean performOk() {
		try {
			if (propertyHandle == null)
				return true;

			return modifyJointCondition();
		} catch (SemanticException e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * performCancel()
	 */
	public boolean performCancel() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * pageActivated()
	 */
	public void pageActivated() {
		if (this.propertyPageContainer != null) {
			setPageMessage(Messages.getString("JointDataSetPage.pageName"), IMessageProvider.NONE);//$NON-NLS-1$
			JointDataSetHandle handle = (JointDataSetHandle) this.propertyPageContainer.getModel();
			if (!nameEditor.isDisposed())
				this.nameEditor.dispose();
			if (!nameLabel.isDisposed())
				this.nameLabel.dispose();
			if (this.dataSetList != null) {
				for (int i = 0; i < dataSetList.size(); i++) {
					if (dataSetList.get(i) instanceof JointDataSetHandle && ((JointDataSetHandle) dataSetList.get(i))
							.getDataSetNames().equals(handle.getDataSetNames())) {
						this.dataSetList.remove(i);
					}
				}
			}
			this.leftDataSetChooser.setInput(this.dataSetList);
			this.rightDataSetChooser.setInput(this.dataSetList);
			propertyHandle = handle.getPropertyHandle(JointDataSet.JOIN_CONDITONS_PROP);
			Iterator conditionIter = handle.joinConditionsIterator();
			JoinConditionHandle condition;
			while (conditionIter.hasNext()) {
				condition = (JoinConditionHandle) conditionIter.next();
				populateDataSet(condition.getLeftDataSet(), condition.getLeftExpression(), LEFT_DATASET);
				populateDataSet(condition.getRightDataSet(), condition.getRightExpression(), RIGHT_DATASET);
				populateJoinType(condition.getJoinType());
			}

		}

		selectionChanged = false;

	}

	/**
	 * 
	 * @param joinType
	 */
	private void populateJoinType(String joinType) {
		this.joinType = joinType;
		if (joinType.equals(DesignChoiceConstants.JOIN_TYPE_INNER)) {
			innerJoinButton.setSelection(true);
			leftOuterJoinButton.setSelection(false);
			rightOuterJoinButton.setSelection(false);
			fullOuterJoinButton.setSelection(false);
		} else if (joinType.equals(DesignChoiceConstants.JOIN_TYPE_LEFT_OUT)) {
			innerJoinButton.setSelection(false);
			leftOuterJoinButton.setSelection(true);
			rightOuterJoinButton.setSelection(false);
			fullOuterJoinButton.setSelection(false);
		} else if (joinType.equals(DesignChoiceConstants.JOIN_TYPE_RIGHT_OUT)) {
			innerJoinButton.setSelection(false);
			leftOuterJoinButton.setSelection(false);
			rightOuterJoinButton.setSelection(true);
			fullOuterJoinButton.setSelection(false);
		} else if (joinType.equals(DesignChoiceConstants.JOIN_TYPE_FULL_OUT)) {
			innerJoinButton.setSelection(false);
			leftOuterJoinButton.setSelection(false);
			rightOuterJoinButton.setSelection(false);
			fullOuterJoinButton.setSelection(true);
		}
	}

	/**
	 * 
	 * @param name
	 * @param expression
	 * @param type
	 */
	private void populateDataSet(String name, String expression, int type) {
		int selectionIndex = -1;
		for (int i = 0; i < dataSetList.size(); i++) {
			DataSetHandle dataHandle = (DataSetHandle) dataSetList.get(i);
			if (dataHandle.getQualifiedName().equals(name)) {
				selectionIndex = i;
				break;
			}
		}

		if (type == LEFT_DATASET) {
			if (selectionIndex >= 0) {
				leftDataSetChooser.setSelection(new StructuredSelection(dataSetList.get(selectionIndex)));
				if (leftColumnList.getInput() != null && leftColumnList.getInput() instanceof DataSetViewData[]) {
					DataSetViewData[] dataSetItems = (DataSetViewData[]) leftColumnList.getInput();
					if (dataSetItems.length > 0) {
						String rowExpr;
						for (int i = 0; i < dataSetItems.length; i++) {
							rowExpr = ExpressionUtil.createJSDataSetRowExpression(dataSetItems[i].getName());
							if (rowExpr.equals(expression)) {
								selectionIndex = i;
								break;
							}
						}
						leftColumnList.setSelection(new StructuredSelection(dataSetItems[selectionIndex]));
					}
				}
			} else {
				setPageMessage(Messages.getString("JointDataSetPage.error.nodataset.left"), //$NON-NLS-1$
						IMessageProvider.ERROR);
			}
		}

		if (type == RIGHT_DATASET) {
			if (selectionIndex >= 0) {
				rightDataSetChooser.setSelection(new StructuredSelection(dataSetList.get(selectionIndex)));
				if (rightColumnList.getInput() != null && rightColumnList.getInput() instanceof DataSetViewData[]) {
					DataSetViewData[] dataSetItems = (DataSetViewData[]) rightColumnList.getInput();
					if (dataSetItems.length > 0) {
						String rowExpr;
						for (int i = 0; i < dataSetItems.length; i++) {
							rowExpr = ExpressionUtil.createJSDataSetRowExpression(dataSetItems[i].getName());
							if (rowExpr.equals(expression)) {
								selectionIndex = i;
								break;
							}
						}
						rightColumnList.setSelection(new StructuredSelection(dataSetItems[selectionIndex]));
					}
				}
			} else {
				setPageMessage(Messages.getString("JointDataSetPage.error.nodataset.right"), //$NON-NLS-1$
						IMessageProvider.ERROR);
			}
		}
	}

	/**
	 * 
	 * @param newMessage
	 * @param type
	 */
	private void setPageMessage(String newMessage, int type) {
		if (this.propertyPageContainer != null) {
			propertyPageContainer.setMessage(newMessage, type);
		} else {
			this.setMessage(newMessage, type);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * getToolTip()
	 */
	public String getToolTip() {
		return Messages.getString("JointDataSetPage.pageName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		getControl().setFocus();
	}
}
