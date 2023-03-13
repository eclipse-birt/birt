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

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.data.ui.property.AbstractDescriptionPropertyPage;
import org.eclipse.birt.report.designer.data.ui.util.ChoiceSetFactory;
import org.eclipse.birt.report.designer.data.ui.util.DataSetExpressionProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Presents dataset filters page of data set creation wizard
 *
 *
 */

public final class DataSetFiltersPage extends AbstractDescriptionPropertyPage implements Listener {

	private transient PropertyHandleTableViewer viewer = null;
	private transient DataSetViewData[] columns = null;
	private transient String[] columnExpressions = null;
	private transient PropertyHandle filters = null;
	private boolean modelChanged = true;

	private static String[] cellLabels = { Messages.getString("dataset.editor.title.expression"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.operator"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.value1"), //$NON-NLS-1$
			Messages.getString("dataset.editor.title.value2")//$NON-NLS-1$
	};

	private static String[] operators;
	private static String[] operatorDisplayNames;
	static {
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet(FilterCondition.FILTER_COND_STRUCT,
				FilterCondition.OPERATOR_MEMBER);
		IChoice[] chs = chset.getChoices();
		operators = new String[chs.length];
		operatorDisplayNames = new String[chs.length];

		for (int i = 0; i < chs.length; i++) {
			operators[i] = chs[i].getName();
			operatorDisplayNames[i] = chs[i].getDisplayName();
		}
	}

	/**
	 * Constructor.
	 */
	public DataSetFiltersPage() {
		super();
	}

	/*
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createContents(Composite parent) {
		initColumnNames();
		viewer = new PropertyHandleTableViewer(parent, true, true, true);
		TableColumn column = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		column.setText(cellLabels[0]);
		column.setWidth(150);
		column = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		column.setText(cellLabels[1]);
		column.setWidth(100);
		column = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		column.setText(cellLabels[2]);
		column.setWidth(100);
		column = new TableColumn(viewer.getViewer().getTable(), SWT.LEFT);
		column.setText(cellLabels[3]);
		column.setWidth(100);

		initializeFilters();

		viewer.getViewer().setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object inputElement) {
				ArrayList filterList = new ArrayList(10);
				Iterator iter = filters.iterator();
				if (iter != null) {
					while (iter.hasNext()) {
						filterList.add(iter.next());
					}
				}

				return filterList.toArray();
			}

			@Override
			public void dispose() {

			}

			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}
		});
		viewer.getViewer().setLabelProvider(new FilterTableProvider());
		viewer.getViewer().setInput(filters);
		addListeners();
		setToolTips();
		((DataSetHandle) getContainer().getModel()).addListener(this);

		return viewer.getControl();
	}

	private void addListeners() {
		viewer.getNewButton().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				doNew();
			}
		});

		viewer.getEditButton().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				doEdit();
			}
		});

		viewer.getViewer().getTable().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				doEdit();
			}
		});

		viewer.getViewer().getTable().addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					setPageProperties();
				}
			}

		});

		viewer.getRemoveButton().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageProperties();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		viewer.getRemoveMenuItem().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageProperties();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});

		viewer.getRemoveAllMenuItem().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				setPageProperties();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		viewer.getViewer().addSelectionChangedListener(new ViewerSelectionListener());
	}

	private void doNew() {
		doEdit(new FilterCondition());
	}

	private void doEdit() {
		int index = viewer.getViewer().getTable().getSelectionIndex();
		if (index == -1) {
			return;
		}

		FilterConditionHandle handle = (FilterConditionHandle) viewer.getViewer().getTable().getItem(index).getData();

		doEdit(handle);
	}

	private void doEdit(Object structureOrHandle) {
		FilterConditionBuilder dlg = new FilterConditionBuilder(((DataSetEditor) getContainer()).getShell(),
				this.getTitle(structureOrHandle), this.getTitle(structureOrHandle));
		dlg.setDataSetHandle((DataSetHandle) getContainer().getModel(),
				new DataSetExpressionProvider((DataSetHandle) getContainer().getModel()));
		dlg.showUpdateAggregationButton(false);
		dlg.setBindingParams(getParamBindingHandleArray());
		dlg.setInput(structureOrHandle);
		if (dlg.open() == Window.OK) {
			update(structureOrHandle);
		}
	}

	private ParamBindingHandle[] getParamBindingHandleArray() {
		Iterator parameters = ((DataSetHandle) getContainer().getModel()).paramBindingsIterator();
		List params = new ArrayList();

		while (parameters.hasNext()) {
			params.add(parameters.next());
		}

		ParamBindingHandle[] bindingParams = new ParamBindingHandle[params.size()];
		for (int i = 0; i < bindingParams.length; i++) {
			bindingParams[i] = (ParamBindingHandle) params.get(i);
		}
		return bindingParams;
	}

	private String getTitle(Object structureOrHandle) {
		if (structureOrHandle instanceof FilterCondition) {
			return Messages.getString("FilterConditionBuilder.DialogTitle.New"); //$NON-NLS-1$
		} else {
			return Messages.getString("FilterConditionBuilder.DialogTitle.Edit"); //$NON-NLS-1$
		}
	}

	private void update(Object structureOrHandle) {
		if (structureOrHandle instanceof FilterCondition) {
			viewer.getViewer().refresh();
		} else {
			viewer.getViewer().update(structureOrHandle, null);
		}
	}

	private FilterCondition getStructure(Object structureOrHandle) {
		FilterCondition structure = null;
		if (structureOrHandle instanceof FilterCondition) {
			structure = (FilterCondition) structureOrHandle;
		} else {
			structure = (FilterCondition) ((FilterConditionHandle) structureOrHandle).getStructure();
		}

		return structure;
	}

	private String getOperatorDisplayName(String name) {
		for (int i = 0; i < operators.length; i++) {
			if (operators[i].equals(name)) {
				return operatorDisplayNames[i];
			}
		}

		// should never get here
		return operatorDisplayNames[0];
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
		if (modelChanged && this.getContainer() != null && this.getContainer() instanceof DataSetEditor) {
			modelChanged = false;
			((DataSetEditor) getContainer()).updateDataSetDesign(this);
		}
		return true;
	}

	@Override
	public boolean performCancel() {
		((DataSetHandle) getContainer().getModel()).removeListener(this);
		return super.performCancel();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#performOk()
	 */
	@Override
	public boolean performOk() {
		((DataSetHandle) getContainer().getModel()).removeListener(this);
		return canLeave();
	}

	private void setToolTips() {
		viewer.getNewButton().setToolTipText(Messages.getString("DataSetFiltersPage.toolTipText.New"));//$NON-NLS-1$
		viewer.getEditButton().setToolTipText(Messages.getString("DataSetFiltersPage.toolTipText.Edit"));//$NON-NLS-1$
		viewer.getDownButton().setToolTipText(Messages.getString("DataSetFiltersPage.toolTipText.Down"));//$NON-NLS-1$
		viewer.getUpButton().setToolTipText(Messages.getString("DataSetFiltersPage.toolTipText.Up"));//$NON-NLS-1$
		viewer.getRemoveButton().setToolTipText(Messages.getString("DataSetFiltersPage.toolTipText.Remove"));//$NON-NLS-1$
	}

	private void initColumnNames() {
		try {
			columns = ((DataSetEditor) this.getContainer()).getCurrentItemModel(true);
			if (columns != null) {
				columnExpressions = new String[columns.length];
				for (int n = 0; n < columns.length; n++) {
					columnExpressions[n] = columns[n].getName();
				}
			}
		} catch (BirtException e) {
			e.printStackTrace();
		}
	}

	private void initializeFilters() {
		filters = ((DataSetHandle) getContainer().getModel()).getPropertyHandle(DataSetHandle.FILTER_PROP);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * pageActivated()
	 */
	@Override
	public void pageActivated() {
		getContainer().setMessage(Messages.getString("dataset.editor.filters"), IMessageProvider.NONE); //$NON-NLS-1$
		initializeFilters();
		initColumnNames();
		// The proeprties of the various controls on the page
		// will be set depending on the filters
		setPageProperties();

		viewer.getViewer().setInput(filters);
		viewer.getViewer().getTable().select(0);
	}

	/**
	 * Depending on the value of the Filters the properties of various controls on
	 * this page are set
	 */
	private void setPageProperties() {
		boolean filterConditionExists;

		filterConditionExists = (filters != null && filters.getListValue() != null
				&& filters.getListValue().size() > 0);
		viewer.getEditButton().setEnabled(filterConditionExists);
		viewer.getDownButton().setVisible(false);
		viewer.getUpButton().setVisible(false);
		viewer.getRemoveButton().setEnabled(filterConditionExists);
		viewer.getRemoveMenuItem().setEnabled(filterConditionExists);
		viewer.getRemoveAllMenuItem().setEnabled(filterConditionExists);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.
	 * AbstractDescriptionPropertyPage#getPageDescription()
	 */
	@Override
	public String getPageDescription() {
		return Messages.getString("DataSetFiltersPage.description"); //$NON-NLS-1$
	}

	private class FilterTableProvider implements ITableLabelProvider {

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int)
		 */
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int)
		 */
		@Override
		public String getColumnText(Object element, int columnIndex) {
			String value = null;
			FilterCondition filterCondition = getStructure(element);

			try {
				switch (columnIndex) {
				case 0: {
					value = filterCondition.getExpr();
					break;
				}
				case 1: {
					value = getOperatorDisplayName(filterCondition.getOperator());
					break;
				}
				case 2: {
					value = getValue1String(filterCondition);
					break;
				}
				case 3: {
					value = filterCondition.getValue2();
					break;
				}
				}
			} catch (Exception ex) {
				ExceptionHandler.handle(ex);
			}
			if (value == null) {
				value = ""; //$NON-NLS-1$
			}

			return value;
		}

		/**
		 * get the the filter
		 *
		 * @param filterCondition
		 * @return
		 */
		private String getValue1String(FilterCondition filterCondition) {
			if (DesignChoiceConstants.FILTER_OPERATOR_IN.equals(filterCondition.getOperator())
					|| DesignChoiceConstants.FILTER_OPERATOR_NOT_IN.equals(filterCondition.getOperator())) {
				List value1List = filterCondition.getValue1List();
				StringBuilder buf = new StringBuilder();
				for (Iterator i = value1List.iterator(); i.hasNext();) {
					String value = (String) i.next();
					buf.append(value + "; "); //$NON-NLS-1$
				}
				if (buf.length() > 1) {
					buf.delete(buf.length() - 2, buf.length());
				}
				return buf.toString();
			} else {
				return filterCondition.getValue1();
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
		 */
		@Override
		public void dispose() {
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java
		 * .lang.Object, java.lang.String)
		 */
		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
		 * .jface.viewers.ILabelProviderListener)
		 */
		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

	}

	private class ViewerSelectionListener implements ISelectionChangedListener {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.
		 * eclipse.jface.viewers.SelectionChangedEvent)
		 */
		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			// TODO Auto-generated method stub
			setPageProperties();
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
		return Messages.getString("DataSetFiltersPage.Filter.Tooltip"); //$NON-NLS-1$
	}

	@Override
	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		modelChanged = true;
	}
}
