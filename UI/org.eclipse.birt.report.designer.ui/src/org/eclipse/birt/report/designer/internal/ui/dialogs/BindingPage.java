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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

/**
 * The Binding attribute page of DE element. Note: Binding Not support
 * multi-selection.
 */
public class BindingPage extends Composite implements Listener {

	public static class ContentProvider implements IStructuredContentProvider {

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			} else if (inputElement instanceof Object[]) {
				return (Object[]) inputElement;
			}
			return new Object[0];
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	protected List input = new ArrayList();
	/**
	 * The Binding properties table.
	 */
	// private Table table;
	/**
	 * The DataSet choose control.
	 */

	private ComboViewer datasetCombo;

	private Button bindingButton;
	/**
	 * The TableViewer of the table widget.
	 */

	private static final String CHOICE_DATASET_FROM_CONTAINER = Messages
			.getString("ColumnBindingDialog.Choice.DatasetFromContainer");//$NON-NLS-1$

	private static final String CHOICE_NONE = Messages.getString("ColumnBindingDialog.NONE");//$NON-NLS-1$

	private static final String CHOICE_REPORTITEM_FROM_CONTAINER = Messages
			.getString("ColumnBindingDialog.Choice.ReportItemFromContainer");//$NON-NLS-1$

	private BindingInfo NullDatasetChoice = new BindingInfo(ReportItemHandle.DATABINDING_TYPE_NONE, null, true);

	private String NullReportItemChoice = null;

	private transient boolean enableAutoCommit = true;

	private static final String DATA_SET_LABEL = Messages.getString("BindingPage.Dataset.Label"); //$NON-NLS-1$
	private static final String REPORT_ITEM__LABEL = Messages.getString("BindingPage.ReportItem.Label"); //$NON-NLS-1$
	private static final String BUTTON_BINDING = Messages.getString("parameterBinding.title"); //$NON-NLS-1$
	private DataSetColumnBindingsFormPage columnBindingsFormPage;

	private ModuleHandle model;
	private Button datasetButton;
	private Button reportItemButton;
	private CCombo reportItemCombo;
	private boolean canAggregation = true;

	/**
	 * @param parent A widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param style  The style of widget to construct
	 */
	public BindingPage(Composite parent, int style) {
		super(parent, style);
		buildUI();
	}

	public BindingPage(Composite parent, int style, boolean canAggregation) {
		super(parent, style);
		this.canAggregation = canAggregation;
		buildUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * AttributePage#buildUI()
	 */
	protected void buildUI() {
		// sets the layout
		FormLayout layout = new FormLayout();
		layout.marginHeight = WidgetUtil.SPACING;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		setLayout(layout);

		FormData data;

		datasetButton = new Button(this, SWT.RADIO);
		datasetButton.setText(DATA_SET_LABEL);
		datasetButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				refreshBinding();
				if (datasetButton.getSelection()
						&& getReportItemHandle()
								.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF
						&& (DEUtil.getBindingHolder(getReportItemHandle(), true) == null
								|| DEUtil.getBindingHolder(getReportItemHandle(), true)
										.getDataBindingType() != ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF))
					saveBinding();
			}

		});

		datasetCombo = new ComboViewer(new CCombo(this, SWT.READ_ONLY | SWT.BORDER));
		datasetCombo.setLabelProvider(new LabelProvider() {

			public String getText(Object element) {
				BindingInfo info = (BindingInfo) element;
				String datasetName = info.getBindingValue();
				if (!info.isDataSet() && info != NullDatasetChoice) {
					datasetName += Messages.getString("BindingGroupDescriptorProvider.Flag.DataModel"); //$NON-NLS-1$
				}
				return datasetName;
			}
		});
		datasetCombo.setContentProvider(new ContentProvider());
		datasetCombo.getCCombo()
				.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		datasetCombo.getCCombo().addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				saveBinding();
			}
		});

		bindingButton = new Button(this, SWT.PUSH);
		bindingButton.setText(BUTTON_BINDING);
		bindingButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				ParameterBindingDialog dialog = new ParameterBindingDialog(UIUtil.getDefaultShell(),
						((DesignElementHandle) input.get(0)));
				startTrans("Edit ParamBinding"); //$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					commit();
				} else {
					rollback();
				}
			}
		});
		data = new FormData();
		data.left = new FormAttachment(datasetCombo.getCCombo(), 0, SWT.RIGHT);
		data.top = new FormAttachment(datasetButton, 0, SWT.CENTER);
		// data.right = new FormAttachment( 50 );
		bindingButton.setLayoutData(data);

		reportItemButton = new Button(this, SWT.RADIO);
		reportItemButton.setText(REPORT_ITEM__LABEL);
		data = new FormData();
		data.top = new FormAttachment(datasetButton, 0, SWT.BOTTOM);
		data.left = new FormAttachment(datasetButton, 0, SWT.LEFT);
		reportItemButton.setLayoutData(data);
		reportItemButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				refreshBinding();
				if (reportItemButton.getSelection()
						&& getReportItemHandle().getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_DATA
						&& (DEUtil.getBindingHolder(getReportItemHandle(), true) == null
								|| DEUtil.getBindingHolder(getReportItemHandle(), true)
										.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF))
					saveBinding();
			}

		});

		data = new FormData();
		if (UIUtil.getStringWidth(datasetButton.getText(), datasetButton) > UIUtil
				.getStringWidth(reportItemButton.getText(), reportItemButton))
			data.left = new FormAttachment(datasetButton, 0, SWT.RIGHT);
		else
			data.left = new FormAttachment(reportItemButton, 0, SWT.RIGHT);
		data.top = new FormAttachment(datasetButton, 0, SWT.CENTER);
		data.right = new FormAttachment(50);
		datasetCombo.getCCombo().setLayoutData(data);

		reportItemCombo = new CCombo(this, SWT.READ_ONLY | SWT.BORDER);
		reportItemCombo.setBackground(PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		data = new FormData();
		data.top = new FormAttachment(reportItemButton, 0, SWT.CENTER);
		data.left = new FormAttachment(datasetCombo.getCCombo(), 0, SWT.LEFT);
		data.right = new FormAttachment(datasetCombo.getCCombo(), 0, SWT.RIGHT);
		reportItemCombo.setLayoutData(data);
		reportItemCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				saveBinding();
			}
		});

		try {
			columnBindingsFormPage = new DataSetColumnBindingsFormPage(this,
					new DataSetColumnBindingsFormHandleProvider(canAggregation));
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
		data = new FormData();
		data.top = new FormAttachment(reportItemCombo, 0, SWT.BOTTOM);
		data.left = new FormAttachment(reportItemButton, 0, SWT.LEFT);
		data.right = new FormAttachment(100);
		data.bottom = new FormAttachment(100);
		columnBindingsFormPage.setLayoutData(data);
	}

	private void saveBinding() {
		BindingInfo info = new BindingInfo();
		if (datasetButton.getSelection()) {
			info = (BindingInfo) ((StructuredSelection) datasetCombo.getSelection()).getFirstElement();
		} else {
			info.setBindingType(ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF);
			info.setBindingValue(reportItemCombo.getText());
		}
		try {
			this.oldInfo = info;
			save(info);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	private void refreshBinding() {
		if (datasetButton.getSelection()) {
			datasetButton.setSelection(true);
			datasetCombo.getCCombo().setEnabled(true);
			bindingButton.setEnabled(datasetCombo.getCCombo().getText().trim().length() != 0);
			reportItemButton.setSelection(false);
			reportItemCombo.setEnabled(false);
			if (datasetCombo.getCCombo().getSelectionIndex() == -1) {
				BindingInfo[] infos = getAvailableDatasetItems();
				datasetCombo.setInput(infos);
				datasetCombo.setSelection(new StructuredSelection(infos[0]));
			}
		} else {
			datasetButton.setSelection(false);
			datasetCombo.getCCombo().setEnabled(false);
			bindingButton.setEnabled(false);
			reportItemButton.setSelection(true);
			reportItemCombo.setEnabled(true);
			if (reportItemCombo.getSelectionIndex() == -1) {
				reportItemCombo.setItems(getReferences());
				reportItemCombo.select(0);
			}
		}
	}

	public List<BindingInfo> getVisibleDataSetHandles(ModuleHandle handle) {
		ArrayList<BindingInfo> list = new ArrayList<BindingInfo>();
		for (Iterator iterator = handle.getVisibleDataSets().iterator(); iterator.hasNext();) {
			DataSetHandle dataSetHandle = (DataSetHandle) iterator.next();
			BindingInfo info = new BindingInfo(ReportItemHandle.DATABINDING_TYPE_DATA, dataSetHandle.getQualifiedName(),
					true);
			list.add(info);
		}
		LinkedDataSetAdapter adapter = new LinkedDataSetAdapter();
		for (Iterator iterator = adapter.getVisibleLinkedDataSetsDataSetHandles(handle).iterator(); iterator
				.hasNext();) {
			DataSetHandle dataSetHandle = (DataSetHandle) iterator.next();
			BindingInfo info = new BindingInfo(ReportItemHandle.DATABINDING_TYPE_DATA, dataSetHandle.getQualifiedName(),
					false);
			list.add(info);
		}
		return list;
	}

	protected BindingInfo[] getAvailableDatasetItems() {
		BindingInfo[] dataSets = getVisibleDataSetHandles(SessionHandleAdapter.getInstance().getModule())
				.toArray(new BindingInfo[0]);
		BindingInfo[] newList = new BindingInfo[dataSets.length + 1];
		newList[0] = NullDatasetChoice;
		System.arraycopy(dataSets, 0, newList, 1, dataSets.length);
		return newList;
	}

	protected Map<String, ReportItemHandle> referMap = new HashMap<String, ReportItemHandle>();

	protected List getAvailableDataBindingReferenceList(ReportItemHandle element) {
		List bindingRef = new ArrayList();
		bindingRef.addAll(element.getAvailableDataSetBindingReferenceList());

		if (ExtendedDataModelUIAdapterHelper.getInstance().getAdapter() != null) {
			List temp = (ExtendedDataModelUIAdapterHelper.getInstance().getAdapter()
					.getAvailableBindingReferenceList(element));
			bindingRef.removeAll(temp);
			bindingRef.addAll(temp);
		}
		return bindingRef;
	}

	protected String[] getReferences() {
		ReportItemHandle element = getReportItemHandle();
		List referenceList = getAvailableDataBindingReferenceList(element);

		String[] references = new String[referenceList.size() + 1];
		references[0] = NullReportItemChoice;
		referMap.put(references[0], null);
		int j = 0;
		for (int i = 0; i < referenceList.size(); i++) {
			ReportItemHandle item = ((ReportItemHandle) referenceList.get(i));
			if (item.getName() != null) {
				references[++j] = item.getQualifiedName();
				referMap.put(references[j], item);
			}
		}
		int tmp = j + 1;
		Arrays.sort(references, 1, tmp);
		for (int i = 0; i < referenceList.size(); i++) {
			ReportItemHandle item = ((ReportItemHandle) referenceList.get(i));
			if (item.getName() == null) {
				references[++j] = item.getElement().getDefn().getDisplayName() + " (ID " //$NON-NLS-1$
						+ item.getID() + ") - " //$NON-NLS-1$
						+ Messages.getString("BindingPage.ReportItem.NoName"); //$NON-NLS-1$
				referMap.put(references[j], item);
			}
		}
		Arrays.sort(references, tmp, referenceList.size() + 1);
		return references;
	}

	/**
	 * Creates the TableViewer and set all kinds of processors.
	 */
	// private void createTableViewer( )
	// {
	// tableViewer = new TableViewer( table );
	// tableViewer.setUseHashlookup( true );
	// tableViewer.setColumnProperties( columnNames );
	// expressionCellEditor = new ExpressionDialogCellEditor( table );
	// tableViewer.setCellEditors( new CellEditor[]{
	// null, null, expressionCellEditor
	// } );
	// tableViewer.setContentProvider( new BindingContentProvider( ) );
	// tableViewer.setLabelProvider( new BindingLabelProvider( ) );
	// tableViewer.setCellModifier( new BindingCellModifier( ) );
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * AttributePage#refreshValues(java.util.Set)
	 */
	/*
	 * protected void refreshValues( ) { // Binding Not support multi-selection. if
	 * ( input.size( ) != 1 ) { datasetCombo.setEnabled( false );
	 * datasetCombo.deselectAll( ); // table.removeAll( ); // table.setEnabled(
	 * false ); return; } datasetCombo.setEnabled( true ); // table.setEnabled( true
	 * );
	 * 
	 * String selectedDataSetName = datasetCombo.getText( ); String[] oldList =
	 * datasetCombo.getItems( ); String[] dataSets = ChoiceSetFactory.getDataSets(
	 * ); String[] newList = new String[dataSets.length + 1]; newList[0] = NONE;
	 * System.arraycopy( dataSets, 0, newList, 1, dataSets.length ); if (
	 * !Arrays.asList( oldList ).equals( Arrays.asList( newList ) ) ) {
	 * datasetCombo.setItems( newList ); datasetCombo.setText( selectedDataSetName
	 * ); } String dataSetName = getDataSetName( ); if ( !dataSetName.equals(
	 * selectedDataSetName ) ) { datasetCombo.deselectAll( ); datasetCombo.setText(
	 * dataSetName ); } bindingButton.setEnabled( !dataSetName.equals( NONE ) ); //
	 * reconstructTable( ); columnBindingsFormPage.setInput( input ); }
	 */

	private ReportItemHandle getReportItemHandle() {
		return (ReportItemHandle) input.get(0);
	}

	/**
	 * reconstruct the content of the table to show the last parameters in DataSet.
	 */
	// private void reconstructTable( )
	// {
	// ReportItemHandle reportItemHandle = (ReportItemHandle) input.get( 0 );
	// tableViewer.refresh( );
	// expressionCellEditor.setDataSetList( DEUtil.getDataSetList(
	// reportItemHandle ) );
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.
	 * model.api.DesignElementHandle,
	 * org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (this.isDisposed())
			return;
		if (ev.getEventType() == NotificationEvent.PROPERTY_EVENT) {
			PropertyEvent event = (PropertyEvent) ev;
			String propertyName = event.getPropertyName();
			if (ReportItemHandle.PARAM_BINDINGS_PROP.equals(propertyName)
					|| ReportItemHandle.DATA_SET_PROP.equals(propertyName)
					|| ReportItemHandle.DATA_BINDING_REF_PROP.equals(propertyName)) {
				load();
			}
		}

		// report design 's oda data set change event.
		if (ev.getEventType() == NotificationEvent.CONTENT_EVENT) {
			if (ev instanceof ContentEvent) {
				ContentEvent ce = (ContentEvent) ev;
				if (ce.getContent() instanceof DataSet) {
					load();
				}
			}
		}
	}

	/**
	 * Gets the DE CommandStack instance
	 * 
	 * @return CommandStack instance
	 */
	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	private void startTrans(String name) {
		if (isEnableAutoCommit()) {
			getActionStack().startTrans(name);
		}
	}

	private void commit() {
		if (isEnableAutoCommit()) {
			getActionStack().commit();
		}
	}

	private void rollback() {
		if (isEnableAutoCommit()) {
			getActionStack().rollback();
		}
	}

	/**
	 * @return Returns the enableAutoCommit.
	 */
	public boolean isEnableAutoCommit() {
		return enableAutoCommit;
	}

	/**
	 * @param enableAutoCommit The enableAutoCommit to set.
	 */
	public void setEnableAutoCommit(boolean enableAutoCommit) {
		this.enableAutoCommit = enableAutoCommit;
	}

	public void setInput(List elements) {
		deRegisterListeners();
		input = elements;
		ReportItemHandle container = DEUtil
				.getBindingHolder(((ReportItemHandle) DEUtil.getInputFirstElement(input)).getContainer());
		if (container != null && (container.getDataSet() != null || container.columnBindingsIterator().hasNext())) {
			NullDatasetChoice.bindingValue = CHOICE_DATASET_FROM_CONTAINER;
		} else {
			NullDatasetChoice.bindingValue = CHOICE_NONE;
		}

		if (container != null && container.getDataBindingReference() != null) {
			NullReportItemChoice = CHOICE_REPORTITEM_FROM_CONTAINER;
		} else {
			NullReportItemChoice = CHOICE_NONE;
		}
		load();
		registerListeners();
		columnBindingsFormPage.setInput(elements);
		this.model = SessionHandleAdapter.getInstance().getReportDesignHandle();
	}

	protected void registerListeners() {
		if (input == null)
			return;
		for (int i = 0; i < input.size(); i++) {
			Object obj = input.get(i);
			if (obj instanceof DesignElementHandle) {
				DesignElementHandle element = (DesignElementHandle) obj;
				element.addListener(this);
			}
		}
		SessionHandleAdapter.getInstance().getReportDesignHandle().addListener(this);
	}

	protected void deRegisterListeners() {
		if (input == null)
			return;
		for (int i = 0; i < input.size(); i++) {
			Object obj = input.get(i);
			if (obj instanceof DesignElementHandle) {
				DesignElementHandle element = (DesignElementHandle) obj;
				element.removeListener(this);
			}
		}
		if (this.model != null) {
			this.model.removeListener(this);
		}
	}

	public void dispose() {
		deRegisterListeners();
		super.dispose();
	}

	public static class BindingInfo {

		private int bindingType;
		private String bindingValue;
		private boolean isDataSet;

		public void setDataSet(boolean isDataSet) {
			this.isDataSet = isDataSet;
		}

		public boolean isDataSet() {
			return isDataSet;
		}

		public BindingInfo(int type, String value) {
			this.bindingType = type;
			this.bindingValue = value;
		}

		public BindingInfo(int type, String value, boolean isDataSet) {
			this.bindingType = type;
			this.bindingValue = value;
			this.isDataSet = isDataSet;
		}

		public BindingInfo() {
		}

		public int getBindingType() {
			return bindingType;
		}

		public String getBindingValue() {
			return bindingValue;
		}

		public void setBindingType(int bindingType) {
			this.bindingType = bindingType;
		}

		public void setBindingValue(String bindingValue) {
			this.bindingValue = bindingValue;
		}

		public boolean equals(Object obj) {
			if (!(obj instanceof BindingInfo)) {
				return false;
			} else {
				BindingInfo info = (BindingInfo) obj;
				if ((this.bindingValue == null && info.bindingValue != null)
						|| (this.bindingValue != null && !this.bindingValue.equals(info.bindingValue))) {
					return false;
				}
				if (this.bindingType != info.bindingType) {
					return false;
				}
				if (this.isDataSet != info.isDataSet) {
					return false;
				}
				return true;
			}
		}

		public int hashCode() {
			int code = 13;
			if (this.bindingValue != null)
				code += this.bindingValue.hashCode() * 7;
			code += this.bindingType * 5;
			code += Boolean.valueOf(this.isDataSet()).hashCode() * 3;
			return code;
		}
	}

	public void load() {
		datasetButton.setEnabled(true);
		reportItemButton.setEnabled(true);
		BindingInfo info = loadValue();
		if (info != null) {
			refreshBindingInfo(info);
		}
		columnBindingsFormPage.refresh();
	}

	private BindingInfo oldInfo;

	private void refreshBindingInfo(BindingInfo info) {
		int type = info.getBindingType();
		Object value = info.getBindingValue();
		datasetCombo.setInput(getAvailableDatasetItems());
		reportItemCombo.setItems(getReferences());
		if (type == ReportItemHandle.DATABINDING_TYPE_NONE) {
			if (DEUtil.getBindingHolder(getReportItemHandle(), true) != null
					&& DEUtil.getBindingHolder(getReportItemHandle(), true)
							.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF)
				type = ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF;
		}
		switch (type) {
		case ReportItemHandle.DATABINDING_TYPE_NONE:
			if (oldInfo != null) {
				if (oldInfo.getBindingType() == ReportItemHandle.DATABINDING_TYPE_DATA) {
					selectDatasetType(info);
				} else if (oldInfo.getBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF) {
					selectReferenceType(value);
				} else {
					datasetCombo.setSelection(new StructuredSelection(info));
				}
				break;
			}
		case ReportItemHandle.DATABINDING_TYPE_DATA:
			selectDatasetType(info);
			break;
		case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF:
			selectReferenceType(value);
		}
	}

	private void selectReferenceType(Object value) {
		datasetButton.setSelection(false);
		datasetCombo.getCCombo().setEnabled(false);
		bindingButton.setEnabled(false);
		reportItemButton.setSelection(true);
		reportItemCombo.setEnabled(true);
		reportItemCombo.setText(value.toString());
	}

	private void selectDatasetType(BindingInfo value) {
		datasetButton.setSelection(true);
		datasetCombo.getCCombo().setEnabled(true);
		datasetCombo.setSelection(new StructuredSelection(value));
		bindingButton.setEnabled(!value.toString().equals(NullDatasetChoice));
		reportItemButton.setSelection(false);
		reportItemCombo.setEnabled(false);
	}

	public BindingInfo loadValue() {
		ReportItemHandle element = getReportItemHandle();
		String value;
		boolean isDataSet = false;
		;
		int type = element.getDataBindingType();
		if (type == ReportItemHandle.DATABINDING_TYPE_NONE)
			type = DEUtil.getBindingHolder(element).getDataBindingType();
		switch (type) {
		case ReportItemHandle.DATABINDING_TYPE_DATA:
			DataSetHandle dataset = element.getDataSet();
			if (dataset == null) {
				value = NullDatasetChoice.bindingValue;
				type = NullDatasetChoice.bindingType;
				isDataSet = true;
			} else {
				List datasets = element.getModuleHandle().getAllDataSets();
				if (datasets != null) {
					for (int i = 0; i < datasets.size(); i++) {
						if (datasets.get(i) == dataset) {
							isDataSet = true;
							break;
						}
					}
				}
				value = dataset.getQualifiedName();
			}
			break;
		case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF:
			ReportItemHandle reference = element.getDataBindingReference();
			if (reference == null)
				value = NullReportItemChoice;
			else
				value = reference.getQualifiedName();
			break;
		default: {
			value = NullDatasetChoice.bindingValue;
			isDataSet = true;
		}

		}
		BindingInfo info = new BindingInfo(type, value, isDataSet);
		return info;
	}

	public void save(Object saveValue) throws SemanticException {
		if (saveValue instanceof BindingInfo) {
			BindingInfo info = (BindingInfo) saveValue;
			int type = info.getBindingType();
			BindingInfo oldValue = (BindingInfo) loadValue();
			switch (type) {
			case ReportItemHandle.DATABINDING_TYPE_NONE:
			case ReportItemHandle.DATABINDING_TYPE_DATA:
				if (info.equals(NullDatasetChoice)) {
					info = null;
				}
				int ret = 0;
				if (!NullDatasetChoice.equals(info))
					ret = 4;
				if ((!NullDatasetChoice.equals(oldValue)
						|| getReportItemHandle().getColumnBindings().iterator().hasNext())
						&& !(info != null && info.equals(oldValue))) {
					MessageDialog prefDialog = new MessageDialog(UIUtil.getDefaultShell(),
							Messages.getString("dataBinding.title.changeDataSet"), //$NON-NLS-1$
							null, Messages.getString("dataBinding.message.changeDataSet"), //$NON-NLS-1$
							MessageDialog.QUESTION,
							new String[] { Messages.getString("AttributeView.dialg.Message.Yes"), //$NON-NLS-1$
									Messages.getString("AttributeView.dialg.Message.No"), //$NON-NLS-1$
									Messages.getString("AttributeView.dialg.Message.Cancel") }, //$NON-NLS-1$
							0);

					ret = prefDialog.open();
				}

				switch (ret) {
				// Clear binding info
				case 0:
					resetDataSetReference(info, true);
					break;
				// Doesn't clear binding info
				case 1:
					resetDataSetReference(info, false);
					break;
				// Cancel.
				case 2:
					load();
					break;
				case 4:
					updateDataSetReference(info);
					break;
				}
				break;
			case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF:
				String value = info.getBindingValue().toString();
				if (value.equals(NullReportItemChoice)) {
					value = null;
				} else if (referMap.get(value).getName() == null) {
					MessageDialog dialog = new MessageDialog(UIUtil.getDefaultShell(),
							Messages.getString("dataBinding.title.haveNoName"), //$NON-NLS-1$
							null, Messages.getString("dataBinding.message.haveNoName"), //$NON-NLS-1$
							MessageDialog.QUESTION, new String[] { Messages.getString("dataBinding.button.OK")//$NON-NLS-1$
							}, 0);

					dialog.open();
					load();
					return;
				}
				int ret1 = 0;
				if (!NullReportItemChoice.equals(((BindingInfo) loadValue()).getBindingValue().toString())
						|| getReportItemHandle().getColumnBindings().iterator().hasNext()) {
					MessageDialog prefDialog = new MessageDialog(UIUtil.getDefaultShell(),
							Messages.getString("dataBinding.title.changeDataSet"), //$NON-NLS-1$
							null, Messages.getString("dataBinding.message.changeReference"), //$NON-NLS-1$
							MessageDialog.QUESTION,
							new String[] { Messages.getString("AttributeView.dialg.Message.Yes"), //$NON-NLS-1$
									Messages.getString("AttributeView.dialg.Message.Cancel") }, //$NON-NLS-1$
							0);

					ret1 = prefDialog.open();
				}

				switch (ret1) {
				// Clear binding info
				case 0:
					resetReference(value);
					break;
				// Cancel.
				case 1:
					load();
				}
			}
		}
	}

	private void resetDataSetReference(BindingInfo info, boolean clearHistory) {
		try {
			startTrans("Reset Reference"); //$NON-NLS-1$
			DataSetHandle dataSet = null;
			if (info != null && info.isDataSet()) {
				dataSet = SessionHandleAdapter.getInstance().getReportDesignHandle()
						.findDataSet(info.getBindingValue());
			}
			if (getReportItemHandle().getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF) {
				getReportItemHandle().setDataBindingReference(null);
			}
			boolean isExtendedDataModel = false;
			if (dataSet == null && info != null) {
				isExtendedDataModel = new LinkedDataSetAdapter().setLinkedDataModel(getReportItemHandle(),
						info.getBindingValue());
				getReportItemHandle().setDataSet(null);
			} else {
				new LinkedDataSetAdapter().setLinkedDataModel(getReportItemHandle(), null);
				getReportItemHandle().setDataSet(dataSet);
			}
			if (clearHistory) {
				getReportItemHandle().getColumnBindings().clearValue();
				getReportItemHandle().getPropertyHandle(ReportItemHandle.PARAM_BINDINGS_PROP).clearValue();
			}

			if (info != null) {
				DataSetBindingSelector selector = new DataSetBindingSelector(UIUtil.getDefaultShell(),
						isExtendedDataModel ? Messages.getString("BindingPage.DataSetBindingSelector.Title.LinkModel") : //$NON-NLS-1$
								Messages.getString("BindingPage.DataSetBindingSelector.Title.DataSet")); //$NON-NLS-1$
				selector.setDataSet(info.getBindingValue(), info.isDataSet());
				if (selector.open() == Dialog.OK) {
					Object[] columns = (Object[]) ((Object[]) selector.getResult())[1];
					columnBindingsFormPage.generateBindingColumns(columns);
				}
			}

			commit();
		} catch (SemanticException e) {
			rollback();
			ExceptionHandler.handle(e);
		}
		load();
	}

	private void updateDataSetReference(BindingInfo info) {
		try {
			startTrans("Reset Reference"); //$NON-NLS-1$
			DataSetHandle dataSet = null;
			if (info != null && info.isDataSet()) {
				dataSet = SessionHandleAdapter.getInstance().getReportDesignHandle()
						.findDataSet(info.getBindingValue());
			}
			if (getReportItemHandle().getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF) {
				getReportItemHandle().setDataBindingReference(null);
			}
			boolean isExtendedDataModel = false;
			if (dataSet == null && info != null) {
				getReportItemHandle().setDataSet(null);
				isExtendedDataModel = new LinkedDataSetAdapter().setLinkedDataModel(getReportItemHandle(),
						info.getBindingValue());
			} else {
				new LinkedDataSetAdapter().setLinkedDataModel(getReportItemHandle(), null);
				getReportItemHandle().setDataSet(dataSet);
			}

			if (info != null) {
				DataSetBindingSelector selector = new DataSetBindingSelector(UIUtil.getDefaultShell(),
						isExtendedDataModel ? Messages.getString("BindingPage.DataSetBindingSelector.Title.LinkModel") : //$NON-NLS-1$
								Messages.getString("BindingPage.DataSetBindingSelector.Title.DataSet")); //$NON-NLS-1$
				selector.setDataSet(info.getBindingValue(), info.isDataSet());
				Iterator bindings = getReportItemHandle().getColumnBindings().iterator();
				List<String> columnNames = new ArrayList<String>();
				while (bindings.hasNext()) {
					columnNames.add(((ComputedColumnHandle) bindings.next()).getName());
				}
				if (!columnNames.isEmpty())
					selector.setColumns(columnNames.toArray(new String[0]));
				if (selector.open() == Dialog.OK) {
					clearBinding(getReportItemHandle().getColumnBindings(),
							(Object[]) ((Object[]) selector.getResult())[2]);
					Object[] columns = (Object[]) ((Object[]) selector.getResult())[1];
					columnBindingsFormPage.generateBindingColumns(columns);
				}
			}

			commit();
		} catch (SemanticException e) {
			rollback();
			ExceptionHandler.handle(e);
		}
		load();
	}

	private void clearBinding(PropertyHandle columnBindings, Object[] objects) {
		if (objects != null && columnBindings.getItems() != null) {
			List list = Arrays.asList(objects);
			for (int i = columnBindings.getItems().size() - 1; i >= 0; i--) {
				ComputedColumnHandle handle = (ComputedColumnHandle) columnBindings.getAt(i);
				String name = handle.getName();
				if (list.contains(name)) {
					try {
						columnBindings.removeItem(i);
					} catch (PropertyValueException e) {
						ExceptionHandler.handle(e);
					}
				}
			}
		}

	}

	private void resetReference(Object value) {
		if (value == null
				&& this.getReportItemHandle().getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_DATA) {
			resetDataSetReference(null, true);
		} else {
			try {
				startTrans("Reset Reference"); //$NON-NLS-1$
				ReportItemHandle element = null;
				if (value != null) {
					element = (ReportItemHandle) SessionHandleAdapter.getInstance().getReportDesignHandle()
							.findElement(value.toString());
				}
				getReportItemHandle().setDataBindingReference(element);
				commit();
			} catch (SemanticException e) {
				rollback();
				ExceptionHandler.handle(e);
			}
			load();
		}
	}

}
