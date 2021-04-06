/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.birt.report.data.adapter.impl.DataSetMetaDataHelper;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetPreviewer.PreviewType;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataSetExceptionHandler;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage;
import org.eclipse.birt.report.designer.ui.preferences.DateSetPreferencePage;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

/**
 * Property page to preview the resultset.
 * 
 */

public class ResultSetPreviewPage extends AbstractPropertyPage implements Listener {

	private TableViewer resultSetTableViewer = null;
	private transient Table resultSetTable = null;
	private boolean modelChanged = true;
	private boolean needsUpdateUI = true;
	private List recordList = null;
	private IResultMetaData metaData;

	private List errorList = new ArrayList();
	private String[] columnBindingNames;
	private int previousMaxRow = -1;
	private CLabel promptLabel;
	private DataSetHandle dataSetHandle;

	/**
	 * The constructor.
	 */
	public ResultSetPreviewPage() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#createPageControl(org.
	 * eclipse.swt.widgets.Composite)
	 */
	public Control createPageControl(Composite parent) {
		Composite resultSetComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.verticalSpacing = 15;
		resultSetComposite.setLayout(layout);
		resultSetComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		resultSetTable = new Table(resultSetComposite, SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL | SWT.BORDER);
		resultSetTable.setHeaderVisible(true);
		resultSetTable.setLinesVisible(true);
		resultSetTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		((DataSetHandle) getContainer().getModel()).addListener(this);

		resultSetTable.addMouseListener(new MouseAdapter() {

			public void mouseUp(MouseEvent e) {
				// if not mouse left button
				if (e.button != 1) {
					MenuManager menuManager = new MenuManager();

					ResultSetTableAction copyAction = ResultSetTableActionFactory
							.createResultSetTableAction(resultSetTable, ResultSetTableActionFactory.COPY_ACTION);
					ResultSetTableAction selectAllAction = ResultSetTableActionFactory
							.createResultSetTableAction(resultSetTable, ResultSetTableActionFactory.SELECTALL_ACTION);
					menuManager.add(copyAction);
					menuManager.add(selectAllAction);

					menuManager.update();

					copyAction.update();
					selectAllAction.update();

					Menu contextMenu = menuManager.createContextMenu(resultSetTable);

					contextMenu.setEnabled(true);
					contextMenu.setVisible(true);
				}
			}
		});

		createResultSetTableViewer();
		promptLabel = new CLabel(resultSetComposite, SWT.WRAP);
		GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
		promptLabel.setLayoutData(labelData);

		return resultSetComposite;
	}

	private void createResultSetTableViewer() {
		resultSetTableViewer = new TableViewer(resultSetTable);
		resultSetTableViewer.setSorter(null);
		resultSetTableViewer.setContentProvider(new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof List) {
					return ((List) inputElement).toArray();
				}

				return new Object[0];
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			public void dispose() {
			}
		});
		resultSetTableViewer.setLabelProvider(new ITableLabelProvider() {

			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			public String getColumnText(Object element, int columnIndex) {
				if ((element instanceof CellValue[]) && (((CellValue[]) element).length > 0)) {
					return ((CellValue[]) element)[columnIndex].getDisplayValue();
				} else {
					return null;
				}
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
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#pageActivated()
	 */
	public void pageActivated() {
		getContainer().setMessage(Messages.getString("dataset.editor.preview"), //$NON-NLS-1$
				IMessageProvider.NONE);

		if (modelChanged) {
			modelChanged = false;

			dataSetHandle = ((DataSetEditor) getContainer()).getHandle();

			int maxRow = this.getMaxRowPreference();
			if (dataSetHandle.getRowFetchLimit() <= 0) {
				ModuleHandle moduleHandle = ((Module) dataSetHandle.getRoot().copy()).getModuleHandle();
				SlotHandle dataSets = moduleHandle.getDataSets();
				for (int i = 0; i < dataSets.getCount(); i++) {
					if (dataSetHandle.getName().equals(dataSets.get(i).getName())) {
						dataSetHandle = (DataSetHandle) dataSets.get(i);
						try {
							dataSetHandle.setRowFetchLimit(maxRow);
						} catch (SemanticException e) {
						}
						break;
					}
				}
			}

			new UIJob("") { //$NON-NLS-1$

				public IStatus runInUIThread(IProgressMonitor monitor) {
					updateResultsProcess();
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}

	protected final void clearResultSetTable() {
		if (recordList == null)
			recordList = new ArrayList();
		else
			recordList.clear();

		// clear everything else
		resultSetTable.removeAll();
		if (this.errorList != null) {
			this.errorList.clear();
		}

		// Clear the columns
		TableColumn[] columns = resultSetTable.getColumns();
		for (int n = 0; n < columns.length; n++) {
			columns[n].dispose();
		}

	}

	private int getMaxRowPreference() {
		int maxRow;
		Preferences preferences = ReportPlugin.getDefault().getPluginPreferences();
		if (preferences.contains(DateSetPreferencePage.USER_MAXROW)) {
			maxRow = preferences.getInt(DateSetPreferencePage.USER_MAXROW);
		} else {
			maxRow = DateSetPreferencePage.DEFAULT_MAX_ROW;
			preferences.setValue(DateSetPreferencePage.USER_MAXROW, maxRow);
		}
		return maxRow;
	}

	/**
	 * Show ProgressMonitorDialog
	 * 
	 */
	private void updateResultsProcess() {
		needsUpdateUI = true;
		clearResultSetTable();

		IRunnableWithProgress runnable = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask("", IProgressMonitor.UNKNOWN); //$NON-NLS-1$

				if (resultSetTable != null && !resultSetTable.isDisposed()) {
					ModuleHandle handle = null;
					DataSetHandle dsHandle = dataSetHandle;
					handle = dsHandle.getModuleHandle();
					DataSetPreviewer previewer = new DataSetPreviewer(dsHandle, getMaxRowPreference(),
							PreviewType.RESULTSET);
					Map dataSetBindingMap = new HashMap();
					Map dataSourceBindingMap = new HashMap();
					Map appContext = new HashMap();
					try {
						clearProperyBindingMap(dataSetBindingMap, dataSourceBindingMap);

						ResourceIdentifiers identifiers = new ResourceIdentifiers();
						String resouceIDs = ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS;
						identifiers.setApplResourceBaseURI(DTPUtil.getInstance().getBIRTResourcePath());
						identifiers.setDesignResourceBaseURI(DTPUtil.getInstance().getReportDesignPath());
						appContext.put(resouceIDs, identifiers);

						if (ExtendedDataModelUIAdapterHelper.getInstance().getAdapter() != null) {
							appContext.putAll(
									ExtendedDataModelUIAdapterHelper.getInstance().getAdapter().getAppContext());
						}

						AppContextPopulator.populateApplicationContext(dsHandle, appContext);
						previewer.open(appContext, getEngineConfig(handle));
						IResultIterator itr = previewer.preview();
						metaData = itr.getResultMetaData();
						populateRecords(itr);
						monitor.done();
					} catch (BirtException e) {
						metaData = null;
						throw new InvocationTargetException(e);
					} finally {
						try {
							AppContextResourceReleaser.release(appContext);
							previewer.close();
						} catch (BirtException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};
		try {
			new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell()) {

				protected void cancelPressed() {
					super.cancelPressed();
					needsUpdateUI = false;
				}

			}.run(true, true, runnable);
		} catch (InvocationTargetException e) {
			// this ExceptionHandler can show exception stacktrace
			DataSetExceptionHandler.handle(Messages.getString("ExceptionDialog.title"), //$NON-NLS-1$
					e.getCause().getLocalizedMessage(), e.getCause());
		} catch (InterruptedException e) {
			// this ExceptionHandler can show exception stacktrace
			DataSetExceptionHandler.handle(Messages.getString("ExceptionDialog.title"), //$NON-NLS-1$
					e.getCause().getLocalizedMessage(), e);
		}

		updateResultSetTableUI();
	}

	private EngineConfig getEngineConfig(ModuleHandle handle) {
		EngineConfig ec = new EngineConfig();
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		if (parent == null) {
			parent = this.getClass().getClassLoader();
		}
		ClassLoader customClassLoader = DataSetProvider.getCustomScriptClassLoader(parent, handle);

//		"customerClassLoader" should not be set into engine appContext, which results in using wrong
//		classLoader later in JavascriptEvalUtil class. Comment the following lines can make data
//		preview work correctly.

//		ec.getAppContext( ).put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
//				customClassLoader );
		return ec;
	}

	/**
	 * Populate records to be retrieved when re-render resultSetTable
	 * 
	 * @param metaData
	 * @param query
	 * @throws BirtException
	 */
	private void populateRecords(IResultIterator iter) {
		try {
			if (iter != null) {
				IResultMetaData meta = iter.getResultMetaData();
				if (meta.getColumnCount() > 0) {
					while (iter.next()) {
						CellValue[] record = new CellValue[meta.getColumnCount()];
						for (int n = 0; n < record.length; n++) {
							CellValue cv = new CellValue();
							Object value = iter.getValue(meta.getColumnName(n + 1));
							String disp = null;
							if (value instanceof Number)
								disp = value.toString();
							else
								disp = iter.getString(meta.getColumnName(n + 1));
							cv.setDisplayValue(disp);
							cv.setRealValue(value);
							record[n] = cv;
						}
						recordList.add(record);
					}
				}
				setPromptLabelText();
				iter.close();
			}
		} catch (RuntimeException e) {
			errorList.add(e);
		} catch (BirtException e) {
			errorList.add(e);
		}
	}

	/**
	 * Set the prompt label text
	 * 
	 */
	private void setPromptLabelText() {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				String prompt = "";
				prompt = Messages.getFormattedString("dataset.resultset.preview.promptMessage.recordsNum",
						new Object[] { recordList.size() });
				if (recordList != null) {
					if (recordList.size() >= getMaxRowPreference()) {
						prompt += " " + Messages.getString("dataset.resultset.preview.promptMessage.MoreRecordsExist");
					}
				}
				if (promptLabel != null) {
					promptLabel.setText(prompt);
				}
			}
		});
	}

	private void updateResultSetTableUI() {
		if (!needsUpdateUI)
			return;

		if (!errorList.isEmpty()) {
			setPromptLabelText();
			ExceptionHandler.handle((Exception) errorList.get(0));
		} else {
			if (metaData != null)
				createColumns(metaData);
			insertRecords();
		}
	}

	private void createColumns(IResultMetaData rsMd) {
		TableColumn column = null;
		TableLayout layout = new TableLayout();

		for (int n = 1; n <= rsMd.getColumnCount(); n++) {
			column = new TableColumn(resultSetTable, SWT.LEFT);

			try {
				column.setText(rsMd.getColumnLabel(n));
			} catch (BirtException e) {
				// this ExceptionHandler can show exception stacktrace
				org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler.showException(
						resultSetTable.getShell(), Messages.getString("CssErrDialog.Error"), e.getLocalizedMessage(),
						e);
			}
			column.setResizable(true);
			layout.addColumnData(new ColumnPixelData(120, true));
			addColumnSortListener(column, n);
			column.pack();
		}
		resultSetTable.setLayout(layout);
		resultSetTable.layout(true);
	}

	private void insertRecords() {
		resultSetTableViewer.setInput(recordList);
	}

	private String getColumnDisplayName(DataSetViewData[] columnsModel, int index) {
		if (columnsModel == null || columnsModel.length == 0 || index < 0 || index > columnsModel.length) {
			return "";//$NON-NLS-1$
		}

		String externalizedName = columnsModel[index].getExternalizedName();
		if (externalizedName != null && (!externalizedName.equals("")))
			return externalizedName;

		return columnsModel[index].getDisplayName();
	}

	/**
	 * Add listener to a column
	 * 
	 * @param column
	 * @param n
	 */
	private void addColumnSortListener(TableColumn column, final int index) {
		assert index > 0;
		column.addSelectionListener(new SelectionListener() {

			private boolean asc = false;

			public void widgetSelected(SelectionEvent e) {
				sort(index - 1, asc);
				asc = !asc;
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	/**
	 * Carry out sort operation against certain column
	 * 
	 * @param columnIndex the column based on which the sort operation would be
	 *                    carried out
	 * @param asc         the sort direction
	 */
	private void sort(final int columnIndex, final boolean asc) {
		resultSetTable.setSortColumn(resultSetTable.getColumn(columnIndex));
		resultSetTable.setSortDirection(asc == true ? SWT.DOWN : SWT.UP);
		this.resultSetTableViewer.setSorter(new ViewerSorter() {

			// @Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				CellValue cv1 = ((CellValue[]) e1)[columnIndex];
				CellValue cv2 = ((CellValue[]) e2)[columnIndex];
				int result = 0;
				if (cv1 == null && cv2 != null)
					result = -1;
				else if (cv2 == null && cv1 != null)
					result = 1;
				else if (cv1 != null)
					result = cv1.compareTo(cv2);
				if (!asc)
					return result;
				else
					return result * -1;
			}

		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.model.
	 * api.DesignElementHandle, org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (focus.equals(getContainer().getModel()) || ((DataSetEditor) this.getContainer()).modelChanged()) {
			modelChanged = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * performCancel()
	 */
	public boolean performCancel() {
		((DataSetHandle) getContainer().getModel()).removeListener(this);
		return super.performCancel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * performOk()
	 */
	public boolean performOk() {
		((DataSetHandle) getContainer().getModel()).removeListener(this);
		return super.performOk();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#
	 * getToolTip()
	 */
	public String getToolTip() {
		return Messages.getString("dataset.resultset.preview.tooltip"); //$NON-NLS-1$
	}

	private void resetPropertyBinding(final Map dataSetBindingMap, final Map dataSourceBindingMap) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				try {
					DataSetHandle dsHandle = ((DataSetEditor) getContainer()).getHandle();
					DataSetMetaDataHelper.resetPropertyBinding(dsHandle, dataSetBindingMap, dataSourceBindingMap);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
				}
			}
		});
	}

	private void clearProperyBindingMap(final Map dataSetBindingMap, final Map dataSourceBindingMap) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				try {
					DataSetMetaDataHelper.clearPropertyBindingMap(dataSetHandle, dataSetBindingMap,
							dataSourceBindingMap);
				} catch (SemanticException e) {
					DataSetExceptionHandler.handle(e);
				}
			}
		});
	}
}

/**
 * The Action factory
 */
final class ResultSetTableActionFactory {

	public static final int COPY_ACTION = 1;
	public static final int SELECTALL_ACTION = 2;

	public static ResultSetTableAction createResultSetTableAction(Table resultSetTable, int operationID) {
		assert resultSetTable != null;

		ResultSetTableAction rsTableAction = null;

		if (operationID == COPY_ACTION) {
			rsTableAction = new CopyAction(resultSetTable);
		} else if (operationID == SELECTALL_ACTION) {
			rsTableAction = new SelectAllAction(resultSetTable);
		}

		return rsTableAction;
	}
}

/**
 * An implementation of Action
 */
abstract class ResultSetTableAction extends Action {

	protected Table resultSetTable = null;

	public ResultSetTableAction(Table resultSetTable, String actionName) {
		super(actionName);
		this.resultSetTable = resultSetTable;
	}

	/**
	 * This method update the state of the action. Particularly, it will disable the
	 * action under certain circumstance.
	 */
	public abstract void update();
}

/**
 * Copy action.
 */
final class CopyAction extends ResultSetTableAction {

	/**
	 * @param resultSetTable the ResultSetTable against which the action is applied
	 *                       to
	 */
	public CopyAction(Table resultSetTable) {
		super(resultSetTable, Messages.getString("CopyAction.text")); //$NON-NLS-1$
		this.setImageDescriptor(Utility.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
	}

	/*
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		StringBuffer textData = new StringBuffer();

		for (int i = 0; i < resultSetTable.getColumnCount(); i++) {
			textData.append(resultSetTable.getColumn(i).getText() + "\t"); //$NON-NLS-1$
		}
		textData.append("\n"); //$NON-NLS-1$

		TableItem[] tableItems = resultSetTable.getSelection();
		for (int i = 0; i < tableItems.length; i++) {
			for (int j = 0; j < resultSetTable.getColumnCount(); j++) {
				textData.append(tableItems[i].getText(j) + "\t"); //$NON-NLS-1$
			}
			textData.append("\n"); //$NON-NLS-1$
		}

		Clipboard clipboard = new Clipboard(resultSetTable.getDisplay());
		clipboard.setContents(new Object[] { textData.toString() }, new Transfer[] { TextTransfer.getInstance() });
		clipboard.dispose();
	}

	/*
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.ResultSetTableAction#
	 * update()
	 */
	public void update() {
		if (resultSetTable.getItems().length < 1 || resultSetTable.getSelectionCount() < 1) {
			this.setEnabled(false);
		}
	}
}

/**
 * Select All Action
 */
final class SelectAllAction extends ResultSetTableAction {

	/**
	 * @param resultSetTable the ResultSetTable against which the action is applied
	 *                       to
	 */
	public SelectAllAction(Table resultSetTable) {
		super(resultSetTable, Messages.getString("SelectAllAction.text")); //$NON-NLS-1$
	}

	/*
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		resultSetTable.selectAll();
	}

	/*
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dialogs.ResultSetTableAction#
	 * update()
	 */
	public void update() {
		if (resultSetTable.getItems().length < 1) {
			this.setEnabled(false);
		}
	}
}

final class CellValue implements Comparable {
	private Object realValue;
	private String displayValue;

	public int compareTo(Object o) {
		if (o == null) {
			return 1;
		}
		CellValue other = (CellValue) o;
		try {
			return ScriptEvalUtil.compare(this.realValue, other.realValue);
		} catch (DataException e) {
			// should never get here
			assert (false);
			return -1;
		}
	}

	public String toString() {
		return displayValue == null ? "" : displayValue; //$NON-NLS-1$
	}

	public void setRealValue(Object realValue) {
		this.realValue = realValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getDisplayValue() {
		return displayValue;
	}
}