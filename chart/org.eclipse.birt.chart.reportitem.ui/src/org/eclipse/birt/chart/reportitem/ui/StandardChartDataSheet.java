/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.ui.ChartExpressionButtonUtil.ExpressionDescriptor;
import org.eclipse.birt.chart.reportitem.ui.ChartExpressionButtonUtil.IExpressionDescriptor;
import org.eclipse.birt.chart.reportitem.ui.ReportDataServiceProvider.DataSetInfo;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartColumnBindingDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ExtendedItemFilterDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ReportItemParametersDialog;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartCubeFilterHandleProvider;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.provider.ChartFilterProviderDelegate;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.DefaultChartDataSheet;
import org.eclipse.birt.chart.ui.swt.SimpleTextTransfer;
import org.eclipse.birt.chart.ui.swt.composites.DataItemCombo;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.data.SelectDataDynamicArea;
import org.eclipse.birt.chart.ui.swt.wizard.preview.ChartLivePreviewThread;
import org.eclipse.birt.chart.ui.swt.wizard.preview.LivePreviewTask;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFilterHandleProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.action.NewCubeAction;
import org.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.ITableItemModel;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Data sheet implementation for Standard Chart
 */

public class StandardChartDataSheet extends DefaultChartDataSheet implements Listener {

	private static final String KEY_PREVIEW_DATA = "Preview Data"; //$NON-NLS-1$
	final protected ExtendedItemHandle itemHandle;
	final protected ReportDataServiceProvider dataProvider;

	private Button btnInherit = null;
	private Button btnUseData = null;
	private boolean bIsInheritSelected = true;

	private CCombo cmbInherit = null;
	private DataItemCombo cmbDataItems = null;

	protected StackLayout stackLayout = null;
	protected Composite cmpStack = null;
	protected Composite cmpCubeTree = null;
	private Composite cmpDataPreview = null;
	protected Composite cmpColumnsList = null;

	private CustomPreviewTable tablePreview = null;
	protected TreeViewer treeViewer = null;

	private Button btnFilters = null;
	private Button btnParameters = null;
	private Button btnBinding = null;
	private Object currentDataReference = null;
	private Object previousDataReference = null;

	public static final int SELECT_NONE = 1;
	public static final int SELECT_NEXT = 2;
	public static final int SELECT_DATA_SET = 4;
	public static final int SELECT_DATA_CUBE = 8;
	public static final int SELECT_REPORT_ITEM = 16;
	public static final int SELECT_NEW_DATASET = 32;
	public static final int SELECT_NEW_DATACUBE = 64;

	private final int iSupportedDataItems;

	private List<Integer> selectDataTypes = new ArrayList<Integer>();
	private Button btnShowDataPreviewA;
	private Button btnShowDataPreviewB;
	private TableViewer tableViewerColumns;
	protected Label columnListDescription;
	private Label dataPreviewDescription;
	protected ExpressionCodec exprCodec = null;
	protected ChartReportItemHelper chartItemHelper = ChartReportItemHelper.instance();

	private static final String HEAD_INFO = "HeaderInfo"; //$NON-NLS-1$

	private static final String DATA_LIST = "DataList"; //$NON-NLS-1$

	private Composite parentComposite;
	protected Label treeViewerTitle;
	protected Label treeViewerDescription;

	public StandardChartDataSheet(ExtendedItemHandle itemHandle, ReportDataServiceProvider dataProvider,
			int iSupportedDataItems) {
		this.itemHandle = itemHandle;
		this.dataProvider = dataProvider;
		this.iSupportedDataItems = iSupportedDataItems;
		this.exprCodec = chartItemHelper.createExpressionCodec(itemHandle);

		addListener(this);
	}

	public StandardChartDataSheet(ExtendedItemHandle itemHandle, ReportDataServiceProvider dataProvider) {
		this(itemHandle, dataProvider, 0);
	}

	@Override
	public Composite createActionButtons(Composite parent) {
		Composite composite = ChartUIUtil.createCompositeWrapper(parent);
		{
			composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END));
		}

		btnFilters = new Button(composite, SWT.NONE);
		{
			btnFilters.setAlignment(SWT.CENTER);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			btnFilters.setLayoutData(gridData);
			btnFilters.setText(Messages.getString("StandardChartDataSheet.Label.Filters")); //$NON-NLS-1$
			btnFilters.addListener(SWT.Selection, this);
		}

		btnParameters = new Button(composite, SWT.NONE);
		{
			btnParameters.setAlignment(SWT.CENTER);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			btnParameters.setLayoutData(gridData);
			btnParameters.setText(Messages.getString("StandardChartDataSheet.Label.Parameters")); //$NON-NLS-1$
			btnParameters.addListener(SWT.Selection, this);
		}

		btnBinding = new Button(composite, SWT.NONE);
		{
			btnBinding.setAlignment(SWT.CENTER);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			btnBinding.setLayoutData(gridData);
			btnBinding.setText(Messages.getString("StandardChartDataSheet.Label.DataBinding")); //$NON-NLS-1$
			btnBinding.addListener(SWT.Selection, this);
		}

		setEnabledForButtons();
		return composite;
	}

	private void setEnabledForButtons() {
		if (isCubeMode()) {
			// Inherit xtab means container has cube but chart has no cube
			boolean inheritXTab = getDataServiceProvider().checkState(IDataServiceProvider.INHERIT_CUBE)
					&& !(getDataServiceProvider().checkState(IDataServiceProvider.HAS_CUBE)
							&& !getDataServiceProvider().checkState(IDataServiceProvider.IN_MULTI_VIEWS));
			if (inheritXTab) {
				btnFilters.setEnabled(false);
				btnBinding.setEnabled(false);
			} else {
				boolean disabled = getDataServiceProvider().isInXTabAggrCell()
						|| getDataServiceProvider().isInXTabMeasureCell();
				btnFilters.setEnabled(!disabled && (getDataServiceProvider().checkState(IDataServiceProvider.HAS_CUBE)
						|| !getDataServiceProvider().isInheritColumnsGroups()));
				btnBinding.setEnabled((getDataServiceProvider().checkState(IDataServiceProvider.HAS_CUBE)
						|| !getDataServiceProvider().isInheritColumnsGroups())
						&& getDataServiceProvider().isInvokingSupported()
						|| getDataServiceProvider().isSharedBinding());
			}
			btnParameters.setEnabled(false);
		} else {
			boolean shareTable = getDataServiceProvider().checkState(IDataServiceProvider.SHARE_TABLE_QUERY);
			if (shareTable) {
				btnFilters.setEnabled(false);
				btnBinding.setEnabled(false);
			} else {
				btnFilters.setEnabled(hasDataSet() && !getDataServiceProvider().isInheritColumnsGroups());
				btnBinding.setEnabled(hasDataSet() && !getDataServiceProvider().isInheritColumnsGroups()
						&& (getDataServiceProvider().isInvokingSupported()
								|| getDataServiceProvider().isSharedBinding()));
			}
			// Bugzilla#177704 Chart inheriting data from container doesn't
			// support parameters due to limitation in DtE
			btnParameters.setEnabled(
					getDataServiceProvider().getDataSet() != null && getDataServiceProvider().isInvokingSupported());
		}
	}

	private boolean hasDataSet() {
		return getDataServiceProvider().getInheritedDataSet() != null || getDataServiceProvider().getDataSet() != null;
	}

	void fireEvent(Widget widget, int eventType) {
		Event event = new Event();
		event.data = this;
		event.widget = widget;
		event.type = eventType;
		notifyListeners(event);
	}

	private Composite createTreeViewComposite(Composite parent) {
		Composite cubeTreeComp = ChartUIUtil.createCompositeWrapper(parent);
		treeViewerTitle = new Label(cubeTreeComp, SWT.NONE);
		{
			treeViewerTitle.setText(Messages.getString("StandardChartDataSheet.Label.CubeTree")); //$NON-NLS-1$
			treeViewerTitle.setFont(JFaceResources.getBannerFont());
		}

		if (!dataProvider.isInXTabMeasureCell() && !dataProvider.isInMultiView()) {
			// No description if dnd is disabled
			treeViewerDescription = new Label(cubeTreeComp, SWT.WRAP);
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				treeViewerDescription.setLayoutData(gd);
				treeViewerDescription.setText(getCubeTreeViewNote());
			}
		}

		treeViewer = new TreeViewer(cubeTreeComp, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridData) treeViewer.getTree().getLayoutData()).heightHint = 120;

		ViewsTreeProvider provider = createCubeViewProvider();
		treeViewer.setLabelProvider(provider);
		treeViewer.setContentProvider(provider);
		treeViewer.setInput(getCube());

		if (!Platform.getOS().equals(Platform.OS_MACOSX)) {
			final DragSource dragSource = new DragSource(treeViewer.getTree(), DND.DROP_COPY);
			dragSource.setTransfer(new Transfer[] { SimpleTextTransfer.getInstance() });
			dragSource.addDragListener(new DragSourceListener() {

				private String text = null;

				public void dragFinished(DragSourceEvent event) {
					// TODO Auto-generated method stub

				}

				public void dragSetData(DragSourceEvent event) {
					event.data = text;
				}

				public void dragStart(DragSourceEvent event) {
					text = getDraggableCubeExpression();
					if (text == null) {
						event.doit = false;
					}
				}
			});
		}

		treeViewer.getTree().addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (event.button == 3 && event.widget instanceof Tree) {
					Tree tree = (Tree) event.widget;
					TreeItem treeItem = tree.getSelection()[0];
					if (dataProvider.checkState(IDataServiceProvider.SHARE_CHART_QUERY)) {
						tree.setMenu(null);
					} else {
						tree.setMenu(
								createMenuManager(getHandleFromSelection(treeItem.getData())).createContextMenu(tree));
						// tree.getMenu( ).setVisible( true );
					}

				}
			}
		});

		return cubeTreeComp;
	}

	private Composite createTableViewComposite(Composite parent) {
		Composite tabularDataViewComp = ChartUIUtil.createCompositeWrapper(parent);
		Label label = new Label(tabularDataViewComp, SWT.NONE);
		{
			label.setText(Messages.getString("StandardChartDataSheet.Label.DataPreview")); //$NON-NLS-1$
			label.setFont(JFaceResources.getBannerFont());
		}

		if (!dataProvider.isInXTabMeasureCell() && !dataProvider.isInMultiView()) {
			dataPreviewDescription = new Label(tabularDataViewComp, SWT.WRAP);
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				dataPreviewDescription.setLayoutData(gd);
				dataPreviewDescription.setText(getDataPreviewDescription());
			}
		}

		btnShowDataPreviewA = new Button(tabularDataViewComp, SWT.CHECK);
		btnShowDataPreviewA.setText(Messages.getString("StandardChartDataSheet.Label.ShowDataPreview")); //$NON-NLS-1$
		btnShowDataPreviewA.addListener(SWT.Selection, this);

		tablePreview = new CustomPreviewTable(tabularDataViewComp,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		{
			GridData gridData = new GridData(GridData.FILL_BOTH);
			gridData.widthHint = 400;
			gridData.heightHint = 120;
			tablePreview.setLayoutData(gridData);
			tablePreview.setHeaderAlignment(SWT.LEFT);
			tablePreview.addListener(CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE, this);
		}

		return tabularDataViewComp;
	}

	private Composite createColumnListViewComposite(Composite parent) {
		Composite columnsListDataViewComp = ChartUIUtil.createCompositeWrapper(parent);

		Label label = new Label(columnsListDataViewComp, SWT.NONE);
		{
			label.setText(Messages.getString("StandardChartDataSheet.Label.DataPreview")); //$NON-NLS-1$
			label.setFont(JFaceResources.getBannerFont());
		}

		if (!dataProvider.isInXTabMeasureCell()) {
			columnListDescription = new Label(columnsListDataViewComp, SWT.WRAP);
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				columnListDescription.setLayoutData(gd);
				columnListDescription.setText(getDataPreviewDescription());
			}
		}

		btnShowDataPreviewB = new Button(columnsListDataViewComp, SWT.CHECK);
		btnShowDataPreviewB.setText(Messages.getString("StandardChartDataSheet.Label.ShowDataPreview")); //$NON-NLS-1$
		btnShowDataPreviewB.addListener(SWT.Selection, this);

		// Add a list to display all columns.
		final Table table = new Table(columnsListDataViewComp,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(gd);
		table.setLinesVisible(true);
		tableViewerColumns = new TableViewer(table);
		tableViewerColumns.setUseHashlookup(true);
		new TableColumn(table, SWT.LEFT);

		table.addMouseMoveListener(new MouseMoveListener() {

			@SuppressWarnings("unchecked")
			public void mouseMove(MouseEvent e) {
				if (!dataProvider.isLivePreviewEnabled()) {
					table.setToolTipText(null);
					return;
				}

				String tooltip = null;
				TableItem item = ((Table) e.widget).getItem(new Point(e.x, e.y));
				if (item != null) {
					List<Object[]> data = (List<Object[]>) tableViewerColumns.getData(KEY_PREVIEW_DATA);
					if (data != null) {
						StringBuilder sb = new StringBuilder();

						int index = ((Table) e.widget).indexOf(item);
						int i = 0;
						for (; i < data.size(); i++) {
							if (sb.length() > 45) {
								break;
							}
							if (data.get(i)[index] != null) {
								if (i != 0)
									sb.append("; "); //$NON-NLS-1$
								sb.append(String.valueOf(data.get(i)[index]));
							}
						}

						if (i == 1 && sb.length() > 45) {
							sb = new StringBuilder(sb.substring(0, 45));
							sb.append("...");//$NON-NLS-1$
						} else if (i < data.size()) {
							sb.append(";..."); //$NON-NLS-1$
						}

						tooltip = sb.toString();
					}

				}
				table.setToolTipText(tooltip);

			}
		});

		table.addMenuDetectListener(new MenuDetectListener() {

			public void menuDetected(MenuDetectEvent arg0) {
				if (isCubeMode()) {
					// share cube
					table.setMenu(null);
				} else {
					TableItem item = table.getSelection()[0];
					if (item == null) {
						tableViewerColumns.getTable().select(-1);
					}
					// Bind context menu to each header button
					boolean isSharingChart = dataProvider.checkState(IDataServiceProvider.SHARE_CHART_QUERY);
					if (item != null && !isSharingChart) {
						if (table.getMenu() != null) {
							table.getMenu().dispose();
						}
						table.setMenu(createMenuManager(item.getData()).createContextMenu(table));
					} else {
						table.setMenu(null);
					}

					if (table.getMenu() != null && !isSharingChart) {
						table.getMenu().setVisible(true);
					}
				}

			}
		});

		table.addListener(SWT.Resize, new Listener() {

			public void handleEvent(Event event) {
				Table table = (Table) event.widget;
				int totalWidth = table.getClientArea().width;
				table.getColumn(0).setWidth(totalWidth);
			}
		});

		if (!Platform.getOS().equals(Platform.OS_MACOSX)) {
			// Set drag/drop.
			DragSource ds = new DragSource(table, DND.DROP_COPY | DND.DROP_MOVE);
			ds.setTransfer(new Transfer[] { SimpleTextTransfer.getInstance() });
			ColumnNamesTableDragListener dragSourceAdapter = new ColumnNamesTableDragListener(table, itemHandle);
			ds.addDragListener(dragSourceAdapter);
		}

		tableViewerColumns.setContentProvider(new IStructuredContentProvider() {

			/**
			 * Gets the food items for the list
			 * 
			 * @param arg0 the data model
			 * @return Object[]
			 */
			public Object[] getElements(Object arg0) {
				if (arg0 == null)
					return null;
				return (ColumnBindingInfo[]) arg0;
			}

			/**
			 * Disposes any created resources
			 */
			public void dispose() {
				// Do nothing
			}

			/**
			 * Called when the input changes
			 * 
			 * @param arg0 the viewer
			 * @param arg1 the old input
			 * @param arg2 the new input
			 */
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
				// Do nothing
			}
		});
		tableViewerColumns.setLabelProvider(new ILabelProvider() {

			/**
			 * images
			 * 
			 * @param arg0 the element
			 * @return Image
			 */
			public Image getImage(Object arg0) {
				String imageName = ((ColumnBindingInfo) arg0).getImageName();
				if (imageName == null)
					return null;
				return UIHelper.getImage(imageName);
			}

			/**
			 * Gets the text for an element
			 * 
			 * @param arg0 the element
			 * @return String
			 */
			public String getText(Object arg0) {
				return ((ColumnBindingInfo) arg0).getName();
			}

			/**
			 * Adds a listener
			 * 
			 * @param arg0 the listener
			 */
			public void addListener(ILabelProviderListener arg0) {
				// Throw it away
			}

			/**
			 * Disposes any resources
			 */
			public void dispose() {
				// Nothing to dispose
			}

			/**
			 * Returns whether changing the specified property for the specified element
			 * affect the label
			 * 
			 * @param arg0 the element
			 * @param arg1 the property
			 * @return boolean
			 */
			public boolean isLabelProperty(Object arg0, String arg1) {
				return false;
			}

			/**
			 * Removes a listener
			 * 
			 * @param arg0 the listener
			 */
			public void removeListener(ILabelProviderListener arg0) {
				// Ignore
			}
		});

		return columnsListDataViewComp;
	}

	@Override
	public Composite createDataDragSource(Composite parent) {
		cmpStack = new Composite(parent, SWT.NONE);
		cmpStack.setLayoutData(new GridData(GridData.FILL_BOTH));
		stackLayout = new StackLayout();
		stackLayout.marginHeight = 0;
		stackLayout.marginWidth = 0;
		cmpStack.setLayout(stackLayout);

		cmpCubeTree = this.createTreeViewComposite(cmpStack);
		cmpDataPreview = this.createTableViewComposite(cmpStack);
		cmpColumnsList = this.createColumnListViewComposite(cmpStack);

		updateDragDataSource();
		return cmpStack;
	}

	protected ViewsTreeProvider createCubeViewProvider() {
		ViewsTreeProvider provider = new ViewsTreeProvider() {

			@Override
			public Color getBackground(Object element) {
				if (element instanceof ReportElementHandle) {
					String key = getBindingNameFrom((ReportElementHandle) element);
					return ColorPalette.getInstance().getColor(key);
				}
				return super.getBackground(element);
			}
		};
		return provider;
	}

	/**
	 * Returns a note for cube tree view.
	 * 
	 * @return
	 */
	protected String getCubeTreeViewNote() {
		return Messages.getString("StandardChartDataSheet.Label.DragCube"); //$NON-NLS-1$
	}

	private void updateDragDataSource() {
		updateDataPreviewDescComposite();

		if (isCubeMode()) {
			updateDragDataSourceWithCubeData();
		} else {
			updateDragDataSourceWithTabularData();
		}
	}

	private void updateDataPreviewDescComposite() {
		if (dataProvider.checkState(IDataServiceProvider.SHARE_CHART_QUERY)) {// hide the description if share chart
			if (columnListDescription != null) {
				((GridData) columnListDescription.getLayoutData()).exclude = true;
				columnListDescription.setVisible(false);
				cmpColumnsList.layout();
			}
			if (dataPreviewDescription != null) {
				((GridData) dataPreviewDescription.getLayoutData()).exclude = true;
				dataPreviewDescription.setVisible(false);
				cmpDataPreview.layout();
			}

		}
	}

	protected void updateDragDataSourceWithTabularData() {
		if (columnListDescription != null) {
			if (!dataProvider.checkState(IDataServiceProvider.SHARE_CHART_QUERY)) {
				((GridData) columnListDescription.getLayoutData()).exclude = false;
				columnListDescription.setVisible(true);
				columnListDescription.setText(getDataPreviewDescription());
				cmpColumnsList.layout();
			}
		}
		btnShowDataPreviewB.setEnabled(true);

		// Clear data preview setting if current data item was changed.
		Object pValue = (previousDataReference == null) ? "" : previousDataReference; //$NON-NLS-1$
		Object cValue = (currentDataReference == null) ? "" : currentDataReference; //$NON-NLS-1$
		if (!pValue.equals(cValue)) {
			getContext().setShowingDataPreview(null);
		}
		previousDataReference = currentDataReference;

		try {
			// If it is initial state and the columns are equal and greater
			// than 6, do not use data preview, just use columns list view.
			if (!getContext().isSetShowingDataPreview()
					&& getDataServiceProvider().getPreviewHeadersInfo().length >= 6) {
				getContext().setShowingDataPreview(Boolean.FALSE);
			}
			ChartWizard.removeException(ChartWizard.StaChartDSh_gHeaders_ID);
		} catch (NullPointerException e) {
			// Do not do anything.
		} catch (ChartException e) {
			ChartWizard.showException(ChartWizard.StaChartDSh_gHeaders_ID, e.getMessage());
		}

		btnShowDataPreviewA.setSelection(getContext().isShowingDataPreview());
		btnShowDataPreviewB.setSelection(getContext().isShowingDataPreview());

		if (getContext().isShowingDataPreview()) {
			stackLayout.topControl = cmpDataPreview;
		} else {
			stackLayout.topControl = cmpColumnsList;
		}

		refreshDataPreviewPane();

		cmpStack.layout();
	}

	private void updateDragDataSourceWithCubeData() {
		treeViewerTitle.setText(Messages.getString("StandardChartDataSheet.Label.CubeTree")); //$NON-NLS-1$
		if (treeViewerDescription != null) {
			treeViewerDescription.setText(getCubeTreeViewNote());
		}
		if (getDataServiceProvider().checkState(IDataServiceProvider.SHARE_CROSSTAB_QUERY)) {// share cube

			if (!getDataServiceProvider().checkState(IDataServiceProvider.SHARE_CHART_QUERY)) {
				((GridData) columnListDescription.getLayoutData()).exclude = false;
				columnListDescription.setVisible(true);
				columnListDescription.setText(Messages.getString("StandardChartDataSheet.Label.ShareCrossTab")); //$NON-NLS-1$
				cmpColumnsList.layout();
			}

			getContext().setShowingDataPreview(Boolean.FALSE);
			btnShowDataPreviewB.setSelection(false);
			btnShowDataPreviewB.setEnabled(false);

			stackLayout.topControl = cmpColumnsList;
			refreshDataPreviewPane();
		} else if (getDataServiceProvider().checkState(IDataServiceProvider.INHERIT_CUBE)) {// inheritance
			stackLayout.topControl = cmpColumnsList;
			getContext().setShowingDataPreview(Boolean.FALSE);
			btnShowDataPreviewB.setSelection(false);
			btnShowDataPreviewB.setEnabled(false);
			refreshDataPreviewPane();
		} else {
			stackLayout.topControl = cmpCubeTree;
			ViewsTreeProvider provider = createCubeViewProvider();
			treeViewer.setLabelProvider(provider);
			treeViewer.setContentProvider(provider);
			treeViewer.setInput(getCube());
		}

		cmpStack.layout();
		ChartWizard.removeException(ChartWizard.StaChartDSh_dPreview_ID);
	}

	/**
	 * Check if chart is direct consuming cube, should exclude sharing
	 * cube/inheriting cube/xtab-chart with cube/multiview with cube cases.
	 * 
	 * @return
	 */
	protected boolean isCubeTreeView() {
		boolean bCube = this.getDataServiceProvider().getBindingCubeHandle() != null;
		if (bCube) {
			int selectedIndex = cmbDataItems.getSelectionIndex();
			if (selectedIndex < 0) {
				return false;
			}
			Integer selectState = selectDataTypes.get(selectedIndex);
			return selectState.intValue() == SELECT_DATA_CUBE;
		}
		return false;
	}

	/**
	 * 
	 */
	private void refreshDataPreviewPane() {
		if (isCubeTreeView()) {
			return;
		}
		if (getContext().isShowingDataPreview()) {
			refreshTablePreview();
		} else {
			refreshDataPreview();
		}
	}

	private void refreshDataPreview() {
		final boolean isTablePreview = getContext().isShowingDataPreview();
		LivePreviewTask lpt = new LivePreviewTask(Messages.getString("StandardChartDataSheet.Message.RetrieveData"), //$NON-NLS-1$
				null);
		// Add a task to retrieve data and bind data to chart.
		lpt.addTask(new LivePreviewTask() {

			public void run() {
				ColumnBindingInfo[] headers = null;
				List<?> dataList = null;
				try {
					// Get header and data in other thread.
					headers = getDataServiceProvider().getPreviewHeadersInfo();
					// Only when live preview is enabled, it retrieves data.
					if (!isCubeMode() && dataProvider.isLivePreviewEnabled()) {
						dataList = getPreviewData();
					}

					this.setParameter(HEAD_INFO, headers);
					this.setParameter(DATA_LIST, dataList);
				} catch (Exception e) {
					final ColumnBindingInfo[] headerInfo = headers;
					final List<?> data = dataList;

					// Catch any exception.
					final String message = e.getLocalizedMessage();
					Display.getDefault().syncExec(new Runnable() {

						/*
						 * (non-Javadoc)
						 * 
						 * @see java.lang.Runnable#run()
						 */
						public void run() {
							if (isTablePreview) {
								// Still update table preview in here to ensure
								// the
								// column headers of table preview can be
								// updated
								// and user can select expression from table
								// preview
								// even if there is no preview data.
								updateTablePreview(headerInfo, data);
							} else {
								updateColumnsTableViewer(headerInfo, data);
							}
							ChartWizard.showException(ChartWizard.StaChartDSh_dPreview_ID, message);
						}
					});
				}
			}
		});

		// Add a task to render chart.
		lpt.addTask(new LivePreviewTask() {

			public void run() {

				final ColumnBindingInfo[] headerInfo = (ColumnBindingInfo[]) this.getParameter(HEAD_INFO);
				if (headerInfo == null) {
					return;
				}

				final List<?> data = (List<?>) this.getParameter(DATA_LIST);
				// Execute UI operation in UI thread.
				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						if (isTablePreview) {
							updateTablePreview(headerInfo, data);
						} else {
							updateColumnsTableViewer(headerInfo, data);
						}
						ChartWizard.removeException(ChartWizard.StaChartDSh_dPreview_ID);
					}

				});
			}
		});

		// Add live preview tasks to live preview thread.
		((ChartLivePreviewThread) context.getLivePreviewThread()).setParentShell(parentComposite.getShell());
		((ChartLivePreviewThread) context.getLivePreviewThread()).add(lpt);
	}

	/**
	 * @param headerInfo
	 * @param data
	 */
	private void updateColumnsTableViewer(final ColumnBindingInfo[] headerInfo, final List<?> data) {
		if (tableViewerColumns.getTable().isDisposed()) {
			return;
		}
		// Set input.
		tableViewerColumns.setInput(headerInfo);
		tableViewerColumns.setData(KEY_PREVIEW_DATA, data);

		// Make the selected column visible and active.
		int index = tablePreview.getCurrentColumnIndex();
		if (index >= 0) {
			tableViewerColumns.getTable().setFocus();
			tableViewerColumns.getTable().select(index);
			tableViewerColumns.getTable().showSelection();
		}

		updateColumnsTableViewerColor();
	}

	@Override
	public Composite createDataSelector(Composite parent) {
		parentComposite = parent;
		// select the only data set
		if (itemHandle.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_NONE
				&& itemHandle.getContainer() instanceof ModuleHandle) {
			DataSetInfo[] dataSets = dataProvider.getAllDataSets();
			if (dataProvider.getAllDataCubes().length == 0 && dataSets.length == 1) {
				dataProvider.setDataSet(dataSets[0]);
			}
		}

		Composite cmpDataSet = ChartUIUtil.createCompositeWrapper(parent);
		{
			cmpDataSet.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		Label label = new Label(cmpDataSet, SWT.NONE);
		{
			label.setText(Messages.getString("StandardChartDataSheet.Label.SelectDataSet")); //$NON-NLS-1$
			label.setFont(JFaceResources.getBannerFont());
		}

		Composite cmpDetail = new Composite(cmpDataSet, SWT.NONE);
		{
			GridLayout gridLayout = new GridLayout(2, false);
			gridLayout.marginWidth = 10;
			gridLayout.marginHeight = 0;
			cmpDetail.setLayout(gridLayout);
			cmpDetail.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		Composite compRadios = ChartUIUtil.createCompositeWrapper(cmpDetail);
		{
			GridData gd = new GridData();
			gd.verticalSpan = 2;
			compRadios.setLayoutData(gd);
		}

		btnInherit = new Button(compRadios, SWT.RADIO);
		btnInherit.setText(Messages.getString("StandardChartDataSheet.Label.UseReportData")); //$NON-NLS-1$
		btnInherit.addListener(SWT.Selection, this);

		btnUseData = new Button(compRadios, SWT.RADIO);
		btnUseData.setText(Messages.getString("StandardChartDataSheet.Label.UseDataSet")); //$NON-NLS-1$
		btnUseData.addListener(SWT.Selection, this);

		cmbInherit = new CCombo(cmpDetail, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		cmbInherit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cmbInherit.addListener(SWT.Selection, this);

		cmbDataItems = new DataItemCombo(cmpDetail, SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER) {

			@Override
			public boolean triggerSelection(int index) {
				int selectState = selectDataTypes.get(index).intValue();
				if (selectState == SELECT_NEW_DATASET || selectState == SELECT_NEW_DATACUBE) {
					return false;
				}
				return true;
			}

			@Override
			public boolean skipSelection(int index) {
				// skip out of boundary selection
				if (index >= 0) {
					int selectState = selectDataTypes.get(index).intValue();
					if (selectState == SELECT_NEXT) {
						return true;
					}
				}

				return false;
			}
		};
		cmbDataItems.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cmbDataItems.addListener(SWT.Selection, this);
		cmbDataItems.setVisibleItemCount(30);

		initDataSelector();
		updatePredefinedQueries();
		checkDataBinding();
		if (dataProvider.checkState(IDataServiceProvider.IN_MULTI_VIEWS)) {
			autoSelect(false);
		}
		return cmpDataSet;
	}

	int invokeNewDataSet() {
		int count = getDataServiceProvider().getAllDataSets().length;
		DataService.getInstance().createDataSet();

		if (getDataServiceProvider().getAllDataSets().length == count) {
			// user cancel this operation
			return Window.CANCEL;
		}

		return Window.OK;
	}

	int invokeEditFilter() {
		ExtendedItemHandle handle = getItemHandle();
		handle.getModuleHandle().getCommandStack().startTrans(null);
		ExtendedItemFilterDialog page = new ExtendedItemFilterDialog(handle);

		AbstractFilterHandleProvider provider = ChartFilterProviderDelegate.createFilterProvider(handle, handle,
				dataProvider);
		if (provider instanceof ChartCubeFilterHandleProvider) {
			((ChartCubeFilterHandleProvider) provider).setContext(getContext());
		}
		page.setFilterHandleProvider(provider);

		int openStatus = page.open();
		if (openStatus == Window.OK) {
			handle.getModuleHandle().getCommandStack().commit();
		} else {
			handle.getModuleHandle().getCommandStack().rollback();
		}

		return openStatus;
	}

	int invokeEditParameter() {
		ReportItemParametersDialog page = new ReportItemParametersDialog(getItemHandle());
		return page.open();
	}

	int invokeDataBinding() {
		Shell shell = new Shell(Display.getDefault(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		// #194163: Do not register CS help in chart since it's registered in
		// super column binding dialog.
		// ChartUIUtil.bindHelp( shell,
		// ChartHelpContextIds.DIALOG_DATA_SET_COLUMN_BINDING );
		ExtendedItemHandle handle = getItemHandle();

		handle.getModuleHandle().getCommandStack().startTrans(null);
		ColumnBindingDialog page = new ChartColumnBindingDialog(handle, shell, getContext());

		ExpressionProvider ep = new ExpressionProvider(getItemHandle());
		ep.addFilter(new ExpressionFilter() {

			@Override
			public boolean select(Object parentElement, Object element) {
				// Remove unsupported expression. See bugzilla#132768
				return !(parentElement.equals(ExpressionProvider.BIRT_OBJECTS) && element instanceof IClassInfo
						&& ((IClassInfo) element).getName().equals("Total")); //$NON-NLS-1$
			}
		});

		// Add this filer to disable the reference of DataSet category and Cube
		// category.
		ep.addFilter(new ExpressionFilter() {

			@Override
			public boolean select(Object parentElement, Object element) {
				if (!ExpressionFilter.CATEGORY.equals(parentElement)) {
					return true;
				}

				if (element instanceof String) {
					if (ExpressionProvider.DATASETS.equals(element)
							|| ExpressionProvider.CURRENT_CUBE.equals(element)) {
						return false;
					}
					return true;
				}

				return true;
			}
		});
		page.setExpressionProvider(ep);

		// Make all bindings under share binding case read-only.
		((ChartColumnBindingDialog) page)
				.setReadOnly(getDataServiceProvider().isSharedBinding() || getDataServiceProvider().isInheritanceOnly()
						|| getDataServiceProvider().checkState(IDataServiceProvider.HAS_CUBE));

		int openStatus = page.open();
		if (openStatus == Window.OK) {
			handle.getModuleHandle().getCommandStack().commit();
			updatePredefinedQueries();
			checkDataBinding();
		} else {
			handle.getModuleHandle().getCommandStack().rollback();
		}

		return openStatus;
	}

	private void initDataSelector() {
		boolean isInheritingSummaryTable = isInheritingSummaryTable();

		// create Combo items
		cmbInherit.setItems(new String[] { Messages.getString("StandardChartDataSheet.Combo.InheritColumnsGroups"), //$NON-NLS-1$
				Messages.getString("StandardChartDataSheet.Combo.InheritColumnsOnly") //$NON-NLS-1$
		});

		if (isInheritingSummaryTable) {
			cmbInherit.select(0);
			getContext().setInheritColumnsOnly(true);
		} else if (dataProvider.isInheritColumnsSet()) {
			cmbInherit.select(dataProvider.isInheritColumnsOnly() ? 1 : 0);
		} else {
			// Set default inheritance value
			if (ChartItemUtil.hasAggregation(getChartModel())) {
				// If aggregations found, set inherit columns only
				cmbInherit.select(1);
				getContext().setInheritColumnsOnly(true);
			} else {
				// Default value is set as Inherit groups
				cmbInherit.select(0);
				getContext().setInheritColumnsOnly(false);
			}
		}
		cmbInherit.setEnabled(false);

		List<Object> dataItems = new ArrayList<Object>();
		cmbDataItems.setItems(createDataComboItems(dataItems));
		cmbDataItems.setData(dataItems);

		// Select report item reference
		// Since handle may have data set or data cube besides reference, always
		// check reference first
		String sItemRef = getDataServiceProvider().getReportItemReference();
		if (sItemRef != null) {
			btnUseData.setSelection(true);
			bIsInheritSelected = false;
			cmbDataItems.setText(sItemRef);
			currentDataReference = sItemRef;
			return;
		}

		// Select data set
		DataSetInfo sDataSet = getDataServiceProvider().getDataSetInfo();
		if (sDataSet != null && !getDataServiceProvider().isInheritanceOnly()) {
			btnUseData.setSelection(true);
			bIsInheritSelected = false;
			cmbDataItems.setText(sDataSet.getDisplayName());
			currentDataReference = sDataSet;
			return;
		}

		// Select data cube
		String sDataCube = getDataServiceProvider().getDataCube();
		if (sDataCube != null && !getDataServiceProvider().isInheritanceOnly()) {
			btnUseData.setSelection(true);
			bIsInheritSelected = false;
			cmbDataItems.setText(sDataCube);
			currentDataReference = sDataCube;
			return;
		}

		cmbInherit.setEnabled(!isInheritingSummaryTable && getDataServiceProvider().getInheritedDataSet() != null
				&& ChartItemUtil.isContainerInheritable(itemHandle));
		if (!cmbInherit.isEnabled()) {
			if (itemHandle.getContainer() instanceof MultiViewsHandle || itemHandle.getDataBindingReference() != null) {
				// If sharing or multi view, set inherit column groups.
				cmbInherit.select(0);
			} else {
				// If container is grid or anything else, set inherit columns
				// only.
				cmbInherit.select(1);
			}
		}
		btnInherit.setSelection(true);
		bIsInheritSelected = true;
		if (getDataServiceProvider().isInheritanceOnly()) {
			btnUseData.setSelection(false);
			btnUseData.setEnabled(false);
		}
		cmbDataItems.select(0);
		currentDataReference = null;
		cmbDataItems.setEnabled(false);
		// Initializes column bindings from container
		getDataServiceProvider().setDataSet(null);
	}

	/**
	 * 
	 */
	private boolean isInheritingSummaryTable() {
		if (ChartItemUtil.isContainerInheritable(itemHandle)) {
			// Copy aggregations from table container to chart
			TableHandle table = null;
			DesignElementHandle container = itemHandle.getContainer();
			while (container != null) {
				if (container instanceof TableHandle) {
					table = (TableHandle) container;
					break;
				}
				container = container.getContainer();
			}
			if (table != null) {
				return table.getBooleanProperty(ITableItemModel.IS_SUMMARY_TABLE_PROP);
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void handleEvent(Event event) {
		// When user select expression in drop&down list of live preview
		// area, the event will be handled to update related column color.
		if (event.type == IChartDataSheet.EVENT_QUERY) {
			if (event.detail == IChartDataSheet.DETAIL_UPDATE_COLOR_AND_TEXT) {
				updateColorAndText();
			} else if (event.detail == IChartDataSheet.DETAIL_UPDATE_COLOR
					&& event.data instanceof ISelectDataComponent) {
				refreshTableColor();
			}
			return;
		}
		// Right click to display the menu. Menu display by clicking
		// application key is triggered by os, so do nothing.
		// bug 261340, now we use the field doit to indicate whether it's menu
		// initialization or event triggering.
		if (event.type == CustomPreviewTable.MOUSE_RIGHT_CLICK_TYPE) {
			if (getDataServiceProvider().getDataSet() != null
					|| getDataServiceProvider().getInheritedDataSet() != null) {
				if (event.widget instanceof Button) {
					Button header = (Button) event.widget;

					// Bind context menu to each header button
					boolean isSharingChart = dataProvider.checkState(IDataServiceProvider.SHARE_CHART_QUERY);
					if (header.getMenu() == null && !isSharingChart) {
						header.setMenu(createMenuManager(event.data).createContextMenu(tablePreview));
					}

					if (event.doit && !isSharingChart) {
						header.getMenu().setVisible(true);
					}
				}
			}

		} else if (event.type == SWT.Selection) {
			if (event.widget instanceof MenuItem) {
				MenuItem item = (MenuItem) event.widget;
				IAction action = (IAction) item.getData();
				action.setChecked(!action.isChecked());
				action.run();
			} else if (event.widget == btnFilters) {
				if (invokeEditFilter() == Window.OK) {
					refreshDataPreviewPane();
					// Update preview via event
					fireEvent(btnFilters, EVENT_PREVIEW);
				}
			} else if (event.widget == btnParameters) {
				if (invokeEditParameter() == Window.OK) {
					refreshDataPreviewPane();
					// Update preview via event
					fireEvent(btnParameters, EVENT_PREVIEW);
				}
			} else if (event.widget == btnBinding) {
				if (invokeDataBinding() == Window.OK) {
					refreshDataPreviewPane();
					// Update preview via event
					fireEvent(btnBinding, EVENT_PREVIEW);
				}
			}

			try {
				if (event.widget == btnInherit) {
					ColorPalette.getInstance().restore();

					// Skip when selection is false
					if (!btnInherit.getSelection()) {
						return;
					}

					// Avoid duplicate loading data set.
					if (bIsInheritSelected) {
						return;
					}

					bIsInheritSelected = true;

					getDataServiceProvider().setReportItemReference(null);
					getDataServiceProvider().setDataCube(null);
					getDataServiceProvider().setDataSet(null);
					switchDataSet();

					cmbDataItems.select(0);
					currentDataReference = null;
					cmbDataItems.setEnabled(false);
					cmbInherit.setEnabled(
							!isInheritingSummaryTable() && getDataServiceProvider().getInheritedDataSet() != null
									&& ChartItemUtil.isContainerInheritable(itemHandle));
					setEnabledForButtons();
					updateDragDataSource();
					updatePredefinedQueries();
				} else if (event.widget == btnUseData) {
					// Skip when selection is false
					if (!btnUseData.getSelection()) {
						return;
					}

					// Avoid duplicate loading data set.
					if (!bIsInheritSelected) {
						return;
					}

					bIsInheritSelected = false;

					getDataServiceProvider().setReportItemReference(null);
					getDataServiceProvider().setDataSet(null);
					selectDataSet();
					cmbDataItems.setEnabled(true);
					cmbInherit.setEnabled(false);
					setEnabledForButtons();
					updateDragDataSource();
					updatePredefinedQueries();
				} else if (event.widget == cmbInherit) {
					getContext().setInheritColumnsOnly(cmbInherit.getSelectionIndex() == 1);
					setEnabledForButtons();

					// Fire event to update outside UI
					fireEvent(btnBinding, EVENT_QUERY);
					refreshDataPreviewPane();
				} else if (event.widget == cmbDataItems) {
					ColorPalette.getInstance().restore();
					int selectedIndex = cmbDataItems.getSelectionIndex();
					Integer selectState = selectDataTypes.get(selectedIndex);
					switch (selectState.intValue()) {
					case SELECT_NONE:
						// Inherit data from container
						btnInherit.setSelection(true);
						btnUseData.setSelection(false);
						btnInherit.notifyListeners(SWT.Selection, new Event());
						break;
					case SELECT_NEXT:
						selectedIndex++;
						selectState = selectDataTypes.get(selectedIndex);
						cmbDataItems.select(selectedIndex);
						break;
					}
					switch (selectState.intValue()) {
					case SELECT_DATA_SET:
						DataSetInfo dataInfo = (DataSetInfo) ((List<Object>) cmbDataItems.getData()).get(selectedIndex);

						if (getDataServiceProvider().getReportItemReference() == null
								&& getDataServiceProvider().getDataSet() != null
								&& getDataServiceProvider().getDataSetInfo().equals(dataInfo)) {
							return;
						}
						getDataServiceProvider().setDataSet(dataInfo);
						currentDataReference = dataInfo;
						switchDataSet();
						setEnabledForButtons();
						updateDragDataSource();
						break;
					case SELECT_DATA_CUBE:
						getDataServiceProvider().setDataCube(cmbDataItems.getText());
						currentDataReference = cmbDataItems.getText();
						// Since cube has no group, it needs to clear group
						// flag in category and optional Y of chart model.
						clearGrouping();
						updateDragDataSource();
						setEnabledForButtons();
						// Update preview via event
						DataDefinitionTextManager.getInstance().refreshAll();
						fireEvent(tablePreview, EVENT_PREVIEW);
						break;
					case SELECT_REPORT_ITEM:
						if (cmbDataItems.getText().equals(getDataServiceProvider().getReportItemReference())) {
							return;
						}
						if (getDataServiceProvider().isNoNameItem(cmbDataItems.getText())) {
							MessageDialog dialog = new MessageDialog(UIUtil.getDefaultShell(),
									org.eclipse.birt.report.designer.nls.Messages
											.getString("dataBinding.title.haveNoName"), //$NON-NLS-1$
									null,
									org.eclipse.birt.report.designer.nls.Messages
											.getString("dataBinding.message.haveNoName"), //$NON-NLS-1$
									MessageDialog.QUESTION, new String[] { org.eclipse.birt.report.designer.nls.Messages
											.getString("dataBinding.button.OK")//$NON-NLS-1$
									}, 0);
							dialog.open();
							btnInherit.setSelection(true);
							btnUseData.setSelection(false);
							btnInherit.notifyListeners(SWT.Selection, new Event());
							return;
						}
						getDataServiceProvider().setReportItemReference(cmbDataItems.getText());

						// TED 10163
						// Following calls will revise chart model for
						// report item sharing case, in older version of
						// chart, it is allowed to set grouping on category
						// series when sharing report item, but now it isn't
						// allowed, so this calls will revise chart model to
						// remove category series grouping flag for the
						// case.
						ChartReportItemUtil.reviseChartModel(ChartReportItemUtil.REVISE_REFERENCE_REPORT_ITEM,
								this.getContext().getModel(), itemHandle);

						// Bugzilla 265077.
						if (this.getDataServiceProvider().checkState(IDataServiceProvider.SHARE_CHART_QUERY)) {
							ChartAdapter.beginIgnoreNotifications();
							this.getDataServiceProvider().update(ChartUIConstants.COPY_SERIES_DEFINITION, null);
							ChartAdapter.endIgnoreNotifications();
						}

						currentDataReference = cmbDataItems.getText();
						// selectDataSet( );
						// switchDataSet( cmbDataItems.getText( ) );

						// Update preview via event
						DataDefinitionTextManager.getInstance().refreshAll();
						fireEvent(tablePreview, EVENT_PREVIEW);

						setEnabledForButtons();
						updateDragDataSource();
						break;
					case SELECT_NEW_DATASET:
						// Bring up the dialog to create a dataset
						int result = invokeNewDataSet();
						if (result == Window.CANCEL) {
							if (currentDataReference == null) {
								cmbDataItems.select(0);
							} else {
								cmbDataItems.setText(getDataSetName(currentDataReference));
							}
							return;
						}

						cmbDataItems.removeAll();
						List<Object> dataItems = new ArrayList<Object>();
						cmbDataItems.setItems(createDataComboItems(dataItems));
						List<Object> oldDataItems = (List<Object>) cmbDataItems.getData();
						cmbDataItems.setData(dataItems);

						// select the newly created data set for user
						DataSetInfo[] datasets = getDataServiceProvider().getAllDataSets();
						int index = 0;
						for (index = datasets.length - 1; index >= 0; index--) {
							if (!oldDataItems.contains(datasets[index])) {
								break;
							}
						}
						currentDataReference = datasets[index];
						getDataServiceProvider().setDataSet((DataSetInfo) currentDataReference);
						cmbDataItems.setText(getDataSetName(currentDataReference));
						setEnabledForButtons();
						updateDragDataSource();
						break;
					case SELECT_NEW_DATACUBE:
						if (getDataServiceProvider().getAllDataSets().length == 0) {
							invokeNewDataSet();
						}
						int count = getDataServiceProvider().getAllDataCubes().length;
						if (getDataServiceProvider().getAllDataSets().length != 0) {
							new NewCubeAction().run();
						}

						String[] datacubes = getDataServiceProvider().getAllDataCubes();
						cmbDataItems.removeAll();
						dataItems = new ArrayList<Object>();
						cmbDataItems.setItems(createDataComboItems(dataItems));
						cmbDataItems.setData(dataItems);
						if (datacubes.length == count) {
							if (currentDataReference == null) {
								cmbDataItems.select(0);
							} else {
								cmbDataItems.setText(getDataSetName(currentDataReference));
							}
							return;
						}

						// select the newly created data cube for user.
						currentDataReference = datacubes[datacubes.length - 1];
						getDataServiceProvider().setDataCube(currentDataReference.toString());
						cmbDataItems.setText(getDataSetName(currentDataReference));
						updateDragDataSource();
						setEnabledForButtons();
						// Update preview via event
						DataDefinitionTextManager.getInstance().refreshAll();
						fireEvent(tablePreview, EVENT_PREVIEW);
						break;
					}
					updatePredefinedQueries();
					// autoSelect( true );
				} else if (event.widget == btnShowDataPreviewA || event.widget == btnShowDataPreviewB) {
					Button w = (Button) event.widget;
					getContext().setShowingDataPreview(Boolean.valueOf(w.getSelection()));
					updateDragDataSource();
				}
				checkDataBinding();
				ChartWizard.removeException(ChartWizard.StaChartDSh_switch_ID);
			} catch (ChartException e1) {
				ChartWizard.showException(ChartWizard.StaChartDSh_switch_ID, e1.getLocalizedMessage());
			}
		}
	}

	private String getDataSetName(Object dataRef) {
		if (dataRef instanceof DataSetInfo) {
			return ((DataSetInfo) currentDataReference).getDisplayName();
		} else {
			return dataRef.toString();
		}
	}

	/**
	 * This method clears the flag of category grouping and optional Y grouping.
	 */
	private void clearGrouping() {
		SeriesDefinition sd = ChartUIUtil.getBaseSeriesDefinitions(getChartModel()).get(0);
		if (sd.getGrouping() != null && sd.getGrouping().isEnabled())
			sd.getGrouping().unsetEnabled();
		for (SeriesDefinition s : ChartUIUtil.getAllOrthogonalSeriesDefinitions(getChartModel())) {
			if (s.getQuery() != null && s.getQuery().getGrouping() != null && s.getQuery().getGrouping().isEnabled())
				s.getQuery().getGrouping().unsetEnabled();
		}
	}

	private void autoSelect(boolean force) {
		if (dataProvider.checkState(IDataServiceProvider.SHARE_CROSSTAB_QUERY)
				&& !dataProvider.checkState(IDataServiceProvider.SHARE_CHART_QUERY)) {
			// if only one item,select it for user
			Query query = ChartUIUtil.getAllOrthogonalSeriesDefinitions(getContext().getModel()).get(0)
					.getDesignTimeSeries().getDataDefinition().get(0);

			Object[] valueExprs = getContext().getPredefinedQuery(ChartUIConstants.QUERY_VALUE);

			if ((force || query.getDefinition() == null || query.getDefinition().trim().length() == 0)
					&& valueExprs != null && valueExprs.length == 1) {
				boolean isCube = true;
				IExpressionDescriptor desc = ExpressionDescriptor.getInstance(valueExprs[0], isCube);
				if (desc != null) {
					String expr = desc.getExpression();
					query.setDefinition(expr);
					if (dataProvider.update(ChartUIConstants.QUERY_VALUE, expr)) {
						Event e = new Event();
						e.type = IChartDataSheet.EVENT_QUERY;
						this.notifyListeners(e);
					}
					if (force) {
						fireEvent(tablePreview, EVENT_PREVIEW);
					}
				}
			}
		}
	}

	private void selectDataSet() {
		DataSetInfo currentDS = getDataServiceProvider().getDataSetInfo();
		if (currentDS == null) {
			cmbDataItems.select(0);
			currentDataReference = null;
		} else {
			cmbDataItems.setText(currentDS.getDisplayName());
			currentDataReference = currentDS;
		}
	}

	private void refreshTablePreview() {
		if (dataProvider.getDataSetFromHandle() == null) {
			return;
		}
		tablePreview.clearContents();
		switchDataTable();
		tablePreview.layout();
	}

	private void switchDataSet() throws ChartException {
		if (isCubeMode()) {
			return;
		}
		try {
			// Clear old dataset and preview data
			tablePreview.clearContents();
			tableViewerColumns.setInput(null);

			tablePreview.createDummyTable();
			tablePreview.layout();
		} catch (Throwable t) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING, t);
		}

		DataDefinitionTextManager.getInstance().refreshAll();
		// Update preview via event
		fireEvent(tablePreview, EVENT_PREVIEW);
	}

	/**
	 * Update column headers and data to table.
	 * 
	 * @param headers
	 * @param dataList
	 */
	private void updateTablePreview(final ColumnBindingInfo[] headers, final List<?> dataList) {
		fireEvent(tablePreview, EVENT_QUERY);

		if (tablePreview.isDisposed()) {
			return;
		}

		if (headers == null || headers.length == 0) {
			tablePreview.setEnabled(false);
			tablePreview.createDummyTable();
		} else {
			tablePreview.setEnabled(true);
			tablePreview.setColumns(headers);

			refreshTableColor();

			// Add data value
			if (dataList != null) {
				for (Iterator<?> iterator = dataList.iterator(); iterator.hasNext();) {
					String[] dataRow = (String[]) iterator.next();
					for (int i = 0; i < dataRow.length; i++) {
						tablePreview.addEntry(dataRow[i], i);
					}
				}
			}
		}
		tablePreview.layout();

		// Make the selected column visible and active.
		int index = tableViewerColumns.getTable().getSelectionIndex();
		if (index >= 0) {
			tablePreview.moveTo(index);
		}
	}

	private synchronized List<?> getPreviewData() throws ChartException {
		return getDataServiceProvider().getPreviewData();
	}

	private void switchDataTable() {
		if (isCubeMode()) {
			return;
		}
		refreshDataPreview();
	}

	private void refreshTableColor() {
		if (isCubeMode()) {
			return;
		}
		// Reset column color
		if (getContext().isShowingDataPreview()) {
			for (int i = 0; i < tablePreview.getColumnNumber(); i++) {
				tablePreview.setColumnColor(i, ColorPalette.getInstance().getColor(tablePreview.getColumnHeading(i)));
			}
		} else {
			updateColumnsTableViewerColor();
		}
	}

	private static Collection<TreeItem> getAllItems(TreeItem[] items) {
		List<TreeItem> list = new LinkedList<TreeItem>();

		for (TreeItem item : items) {
			list.add(item);
			list.addAll(getAllItems(item.getItems()));
		}

		return list;
	}

	private static Collection<TreeItem> getAllItems(Tree tree) {
		if (tree == null) {
			return Collections.emptyList();
		}
		List<TreeItem> list = new LinkedList<TreeItem>();
		list.addAll(getAllItems(tree.getItems()));
		return list;
	}

	private void refreshTreeViewColor() {
		if (treeViewer == null) {
			return;
		}

		Collection<TreeItem> items = getAllItems(treeViewer.getTree());

		for (TreeItem item : items) {
			String key = getBindingNameFrom(item);
			Color color = ColorPalette.getInstance().getColor(key);
			item.setBackground(color);
		}
		treeViewer.refresh();
	}

	/**
	 * 
	 */
	private void updateColumnsTableViewerColor() {
		for (TableItem item : tableViewerColumns.getTable().getItems()) {
			ColumnBindingInfo cbi = (ColumnBindingInfo) item.getData();
			Color c = ColorPalette.getInstance().getColor(cbi.getName());
			if (c == null) {
				c = Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
			}
			item.setBackground(c);
		}
	}

	/**
	 * Returns actual expression for common and sharing query case.
	 * 
	 * @param query
	 * @param expr
	 * @return
	 */
	private String getActualExpression(String expr) {
		if (!dataProvider.checkState(IDataServiceProvider.SHARE_QUERY)) {
			return expr;
		}

		// Convert to actual expression.
		Object obj = getCurrentColumnHeadObject();
		if (obj instanceof ColumnBindingInfo) {
			ColumnBindingInfo cbi = (ColumnBindingInfo) obj;
			int type = cbi.getColumnType();
			if (type == ColumnBindingInfo.GROUP_COLUMN || type == ColumnBindingInfo.AGGREGATE_COLUMN) {
				return cbi.getExpression();
			}
		}

		return expr;
	}

	protected void manageColorAndQuery(Query query, String expr) {
		// If it's not used any more, remove color binding
		if (DataDefinitionTextManager.getInstance().getNumberOfSameDataDefinition(query.getDefinition()) == 1) {
			ColorPalette.getInstance().retrieveColor(query.getDefinition());
		}

		// Update query, if it is sharing binding case, the specified expression
		// will be converted and set to query, else directly set specified
		// expression to query.
		// DataDefinitionTextManager.getInstance( ).updateQuery( query, expr );
		query.setDefinition(getActualExpression(expr));

		// Refresh all data definition text
		DataDefinitionTextManager.getInstance().refreshAll();

		// Reset table column color
		refreshTableColor();
		refreshTreeViewColor();
	}

	/**
	 * To refresh all color and text
	 */
	protected void updateColorAndText() {
		// Refresh all data definition text
		DataDefinitionTextManager.getInstance().refreshAll();

		// Reset table column color
		refreshTableColor();
		refreshTreeViewColor();
	}

	private class CategoryXAxisAction extends Action {

		private final IExpressionButton eb;
		private final String bindingName;

		CategoryXAxisAction(String bindingName) {
			super(getBaseSeriesTitle(getChartModel()));
			SeriesDefinition seriesDefintion = ChartUIUtil.getBaseSeriesDefinitions(getChartModel()).get(0);
			Query query = seriesDefintion.getDesignTimeSeries().getDataDefinition().get(0);
			this.bindingName = bindingName;

			this.eb = DataDefinitionTextManager.getInstance().findExpressionButton(query);

			if (eb != null) {
				exprCodec.setType(eb.getExpressionType());
				exprCodec.setBindingName(bindingName, eb.isCube());
			}

			setEnabled(eb != null
					&& DataDefinitionTextManager.getInstance().isAcceptableExpression(query, exprCodec.getExpression(),
							dataProvider.isSharedBinding() || dataProvider.isInheritColumnsGroups()));
		}

		@Override
		public void run() {
			if (eb != null) {
				eb.setBindingName(bindingName, true);
			}
		}
	}

	private class GroupYSeriesAction extends Action {

		private final IExpressionButton eb;
		private final String bindingName;
		private final String expr;

		GroupYSeriesAction(Query query, String bindingName, SeriesDefinition seriesDefinition) {
			super(getGroupSeriesTitle(getChartModel()));

			this.bindingName = bindingName;
			this.eb = DataDefinitionTextManager.getInstance().findExpressionButton(query);

			if (eb != null) {
				exprCodec.setType(eb.getExpressionType());
				exprCodec.setBindingName(bindingName, eb.isCube());
				this.expr = exprCodec.getExpression();
			} else {
				this.expr = null;
			}
			boolean enabled = eb != null && DataDefinitionTextManager.getInstance().isAcceptableExpression(query, expr,
					dataProvider.isSharedBinding() || dataProvider.isInheritColumnsGroups());
			setEnabled(enabled);
		}

		@Override
		public void run() {
			// Use the first group, and copy to the all groups
			ChartAdapter.beginIgnoreNotifications();
			ChartUIUtil.setAllGroupingQueryExceptFirst(getChartModel(), expr);
			ChartAdapter.endIgnoreNotifications();

			if (eb != null) {
				eb.setBindingName(bindingName, true);
			}
		}
	}

	private class ValueYSeriesAction extends Action {

		private final Query query;
		private final IExpressionButton eb;
		private final String bindingName;

		ValueYSeriesAction(Query query, String bindingName) {
			super(getOrthogonalSeriesTitle(getChartModel()));

			this.bindingName = bindingName;
			this.eb = DataDefinitionTextManager.getInstance().findExpressionButton(query);
			this.query = query;
			// Grouping expressions can't be set on value series.
			boolean enabled = true;
			if (dataProvider.isSharedBinding() || dataProvider.isInheritColumnsGroups()) {
				Object obj = getCurrentColumnHeadObject();
				if (obj instanceof ColumnBindingInfo
						&& ((ColumnBindingInfo) obj).getColumnType() == ColumnBindingInfo.GROUP_COLUMN) {
					enabled = false;
				}
			}

			setEnabled(enabled);
		}

		@Override
		public void run() {
			if (eb != null) {
				eb.setBindingName(bindingName, true);
			} else {
				exprCodec.setBindingName(bindingName, isCubeMode());
				query.setDefinition(exprCodec.encode());
				ColorPalette.getInstance().putColor(bindingName);
				updateColorAndText();
			}

		}
	}

	Object getCurrentColumnHeadObject() {
		if (getContext().isShowingDataPreview()) {
			return tablePreview.getCurrentColumnHeadObject();
		}
		int index = tableViewerColumns.getTable().getSelectionIndex();
		if (index < 0)
			return null;
		return tableViewerColumns.getTable().getItem(index).getData();
	}

	static class HeaderShowAction extends Action {

		HeaderShowAction(String header) {
			super(header);
			setEnabled(false);
		}
	}

	ExtendedItemHandle getItemHandle() {
		return this.itemHandle;
	}

	protected ReportDataServiceProvider getDataServiceProvider() {
		return this.dataProvider;
	}

	protected List<Object> getActionsForTableHead(String expr) {
		List<Object> actions = new ArrayList<Object>(3);
		actions.add(getBaseSeriesMenu(getChartModel(), expr));
		actions.add(getOrthogonalSeriesMenu(getChartModel(), expr));
		actions.add(getGroupSeriesMenu(getChartModel(), expr));
		return actions;
	}

	protected Object getMenuForMeasure(Chart chart, String expr) {
		return getOrthogonalSeriesMenu(getChartModel(), expr);
	}

	protected Object getMenuForDimension(Chart chart, String expr) {
		List<Object> menus = new ArrayList<Object>(2);
		// bug#220724
		if (((Boolean) dataProvider.checkData(ChartUIConstants.QUERY_CATEGORY, expr)).booleanValue()) {
			menus.add(getBaseSeriesMenu(getChartModel(), expr));
		}

		if (dataProvider.checkState(IDataServiceProvider.MULTI_CUBE_DIMENSIONS)
				&& ((Boolean) dataProvider.checkData(ChartUIConstants.QUERY_OPTIONAL, expr)).booleanValue()) {
			menus.add(getGroupSeriesMenu(getChartModel(), expr));
		}

		return menus;
	}

	protected MenuManager createMenuManager(final Object data) {
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				if (data instanceof ColumnBindingInfo) {
					// Menu for columns table.
					addMenu(manager, new HeaderShowAction(((ColumnBindingInfo) data).getName()));
					// String expr = ExpressionUtil.createJSRowExpression(
					// ((ColumnBindingInfo)data).getName( ) );
					List<Object> actions = getActionsForTableHead(((ColumnBindingInfo) data).getName());
					for (Object act : actions) {
						addMenu(manager, act);
					}
				} else if (data instanceof Integer) {
					// Menu for table
					addMenu(manager, new HeaderShowAction(tablePreview.getCurrentColumnHeading()));
					String expr = tablePreview.getCurrentColumnHeading();
					List<Object> actions = getActionsForTableHead(expr);
					for (Object act : actions) {
						addMenu(manager, act);
					}
				} else if (data instanceof MeasureHandle) {
					// Menu for Measure
					String expr = createCubeExpression();
					if (expr != null) {
						addMenu(manager, getMenuForMeasure(getChartModel(), expr));
					}
				} else if (data instanceof LevelHandle) {
					// Menu for Level
					String expr = createCubeExpression();
					if (expr != null) {
						addMenu(manager, getMenuForDimension(getChartModel(), expr));
					}
				} else if (data instanceof LevelAttributeHandle) {
					// Menu for LevelAttribute
					String expr = createCubeExpression();
					if (expr != null) {
						addMenu(manager, getMenuForDimension(getChartModel(), expr));
					}
				}
			}

			private void addMenu(IMenuManager manager, Object item) {
				if (item instanceof IAction) {
					manager.add((IAction) item);
				} else if (item instanceof IContributionItem) {
					manager.add((IContributionItem) item);
				} else if (item instanceof List<?>) {
					for (Object o : (List<?>) item) {
						addMenu(manager, o);
					}
				}

				// Do not allow customized query in xtab
				if (getDataServiceProvider().isPartChart()) {
					if (item instanceof IAction) {
						((IAction) item).setEnabled(false);
					}
				}
			}

		});
		return menuManager;
	}

	protected Object getBaseSeriesMenu(Chart chart, String expr) {
		EList<SeriesDefinition> sds = ChartUIUtil.getBaseSeriesDefinitions(chart);
		if (sds.size() == 1) {
			return new CategoryXAxisAction(expr);
		}
		return null;
	}

	protected Object getGroupSeriesMenu(Chart chart, String expr) {
		IMenuManager topManager = new MenuManager(getGroupSeriesTitle(getChartModel()));
		int axisNum = ChartUIUtil.getOrthogonalAxisNumber(chart);
		for (int axisIndex = 0; axisIndex < axisNum; axisIndex++) {
			List<SeriesDefinition> sds = ChartUIUtil.getOrthogonalSeriesDefinitions(chart, axisIndex);
			if (!sds.isEmpty()) {
				SeriesDefinition sd = sds.get(0);
				IAction action = new GroupYSeriesAction(sd.getQuery(), expr, sd);
				// ONLY USE FIRST GROUPING SERIES FOR CHART ENGINE SUPPORT
				// if ( axisNum == 1 && sds.size( ) == 1 )
				{
					// Simply cascade menu
					return action;
				}
				// action.setText( getSecondMenuText( axisIndex,
				// i,
				// sd.getDesignTimeSeries( ) ) );
				// topManager.add( action );
			}
		}
		return topManager;
	}

	protected Object getOrthogonalSeriesMenu(Chart chart, String expr) {
		IMenuManager topManager = new MenuManager(getOrthogonalSeriesTitle(getChartModel()));
		int axisNum = ChartUIUtil.getOrthogonalAxisNumber(chart);
		for (int axisIndex = 0; axisIndex < axisNum; axisIndex++) {
			List<SeriesDefinition> sds = ChartUIUtil.getOrthogonalSeriesDefinitions(chart, axisIndex);
			for (int i = 0; i < sds.size(); i++) {
				Series series = sds.get(i).getDesignTimeSeries();
				EList<Query> dataDefns = series.getDataDefinition();

				if (series instanceof StockSeries) {
					IMenuManager secondManager = new MenuManager(getSecondMenuText(axisIndex, i, series));
					topManager.add(secondManager);
					for (int j = 0; j < dataDefns.size(); j++) {
						IAction action = new ValueYSeriesAction(dataDefns.get(j), expr);
						action.setText(ChartUIUtil.getStockTitle(j)
								+ Messages.getString("StandardChartDataSheet.Label.Component")); //$NON-NLS-1$
						secondManager.add(action);
					}
				} else if (series instanceof BubbleSeries) {
					IMenuManager secondManager = new MenuManager(getSecondMenuText(axisIndex, i, series));
					topManager.add(secondManager);
					for (int j = 0; j < dataDefns.size(); j++) {
						IAction action = new ValueYSeriesAction(dataDefns.get(j), expr);
						action.setText(ChartUIUtil.getBubbleTitle(j)
								+ Messages.getString("StandardChartDataSheet.Label.Component")); //$NON-NLS-1$
						secondManager.add(action);
					}
				} else if (series instanceof DifferenceSeries) {
					IMenuManager secondManager = new MenuManager(getSecondMenuText(axisIndex, i, series));
					topManager.add(secondManager);
					for (int j = 0; j < dataDefns.size(); j++) {
						IAction action = new ValueYSeriesAction(dataDefns.get(j), expr);
						action.setText(ChartUIUtil.getDifferenceTitle(j)
								+ Messages.getString("StandardChartDataSheet.Label.Component")); //$NON-NLS-1$
						secondManager.add(action);
					}
				} else if (series instanceof GanttSeries) {
					IMenuManager secondManager = new MenuManager(getSecondMenuText(axisIndex, i, series));
					topManager.add(secondManager);
					for (int j = 0; j < dataDefns.size(); j++) {
						IAction action = new ValueYSeriesAction(dataDefns.get(j), expr);
						action.setText(ChartUIUtil.getGanttTitle(j)
								+ Messages.getString("StandardChartDataSheet.Label.Component")); //$NON-NLS-1$
						secondManager.add(action);
					}
				} else {
					IAction action = new ValueYSeriesAction(dataDefns.get(0), expr);
					if (axisNum == 1 && sds.size() == 1) {
						// Simplify cascade menu
						return action;
					}
					action.setText(getSecondMenuText(axisIndex, i, series));
					topManager.add(action);
				}
			}
		}
		return topManager;
	}

	private String getSecondMenuText(int axisIndex, int seriesIndex, Series series) {
		StringBuffer sb = new StringBuffer();
		if (ChartUIUtil.getOrthogonalAxisNumber(getChartModel()) > 1) {
			sb.append(Messages.getString("StandardChartDataSheet.Label.Axis")); //$NON-NLS-1$
			sb.append(axisIndex + 1);
			sb.append(" - "); //$NON-NLS-1$
		}
		sb.append(Messages.getString("StandardChartDataSheet.Label.Series") //$NON-NLS-1$
				+ (seriesIndex + 1) + " (" + series.getDisplayName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
		return sb.toString();
	}

	protected String getBaseSeriesTitle(Chart chart) {
		if (chart instanceof ChartWithAxes) {
			return Messages.getString("StandardChartDataSheet.Label.UseAsCategoryXAxis"); //$NON-NLS-1$
		}
		return Messages.getString("StandardChartDataSheet.Label.UseAsCategorySeries"); //$NON-NLS-1$
	}

	protected String getOrthogonalSeriesTitle(Chart chart) {
		if (chart instanceof ChartWithAxes) {
			return Messages.getString("StandardChartDataSheet.Label.PlotAsValueYSeries"); //$NON-NLS-1$
		} else if (chart instanceof DialChart) {
			return Messages.getString("StandardChartDataSheet.Label.PlotAsGaugeValue"); //$NON-NLS-1$
		}
		return Messages.getString("StandardChartDataSheet.Label.PlotAsValueSeries"); //$NON-NLS-1$
	}

	private String getGroupSeriesTitle(Chart chart) {
		if (chart instanceof ChartWithAxes) {
			return Messages.getString("StandardChartDataSheet.Label.UseToGroupYSeries"); //$NON-NLS-1$
		}
		return Messages.getString("StandardChartDataSheet.Label.UseToGroupValueSeries"); //$NON-NLS-1$
	}

	protected boolean isCubeMode() {
		boolean bCube = this.getDataServiceProvider().getBindingCubeHandle() != null;
		if (bCube) {
			// If current item doesn't support cube, referenced cube should be
			// invalid.
			return isDataItemSupported(SELECT_DATA_CUBE);
		}
		return false;
	}

	private CubeHandle getCube() {
		return getDataServiceProvider().getBindingCubeHandle();
	}

	private String getBindingNameFrom(ReportElementHandle handle) {
		String expr = null;
		ComputedColumnHandle binding = null;
		if (handle instanceof LevelHandle) {
			LevelHandle level = (LevelHandle) handle;
			String dimensionName = level.getContainer().getContainer().getName();
			binding = ChartCubeUtil.findLevelBinding(itemHandle, dimensionName, level.getName());
		} else if (handle instanceof MeasureHandle) {
			MeasureHandle measure = (MeasureHandle) handle;
			binding = ChartCubeUtil.findMeasureBinding(itemHandle, measure.getName());
		}
		if (binding != null) {
			expr = binding.getName();
		}

		return expr;
	}

	private String getBindingNameFrom(TreeItem treeItem) {
		Object selection = getHandleFromSelection(treeItem.getData());
		if (selection instanceof ReportElementHandle) {
			return getBindingNameFrom((ReportElementHandle) selection);
		} else if (selection instanceof LevelAttributeHandle) {
			LevelAttributeHandle la = (LevelAttributeHandle) selection;
			LevelHandle level = (LevelHandle) ((Level) la.getContext().getValueContainer()).getHandle(la.getModule());
			String dimensionName = level.getContainer().getContainer().getName();
			ComputedColumnHandle binding = ChartCubeUtil.findLevelAttrBinding(itemHandle, dimensionName,
					level.getName(), la.getName());
			if (binding != null) {
				return binding.getName();
			}
		}

		return null;
	}

	protected Object getHandleFromSelection(Object selection) {
		if (selection instanceof LevelHandle || selection instanceof MeasureHandle
				|| selection instanceof LevelAttributeHandle) {
			return selection;
		}
		return null;
	}

	/**
	 * This method returns a cube expression that is draggable.
	 * 
	 * @return
	 */
	protected String getDraggableCubeExpression() {
		return createCubeExpression();
	}

	/**
	 * Creates the cube expression
	 * 
	 * @return expression
	 */
	protected String createCubeExpression() {
		if (treeViewer == null) {
			return null;
		}
		TreeItem[] selection = treeViewer.getTree().getSelection();
		String expr = null;
		if (selection.length > 0 && !dataProvider.isSharedBinding() && !dataProvider.isPartChart()) {
			TreeItem treeItem = selection[0];
			expr = getBindingNameFrom(treeItem);
		}
		return expr;
	}

	private boolean isDataItemSupported(int type) {
		return iSupportedDataItems == 0 || (iSupportedDataItems & type) == type;
	}

	private String[] createDataComboItems(List<Object> dataItems) {
		List<Object> items = new ArrayList<Object>();
		if (dataItems == null) {
			dataItems = new ArrayList<Object>();
		}
		dataItems.clear();

		selectDataTypes.clear();

		if (isDataItemSupported(SELECT_NONE)) {
			String item = null;
			if (DEUtil.getDataSetList(itemHandle.getContainer()).size() > 0) {
				item = Messages.getString("ReportDataServiceProvider.Option.Inherits", //$NON-NLS-1$
						((DataSetHandle) DEUtil.getDataSetList(itemHandle.getContainer()).get(0)).getName());
			} else {
				item = ReportDataServiceProvider.OPTION_NONE;
			}
			items.add(item);
			dataItems.add(item);
			selectDataTypes.add(Integer.valueOf(SELECT_NONE));
		}

		if (isDataItemSupported(SELECT_DATA_SET)) {
			DataSetInfo[] dataSets = getDataServiceProvider().getAllDataSets();
			if (dataSets.length > 0) {
				if (isDataItemSupported(SELECT_NEXT)) {
					items.add(Messages.getString("StandardChartDataSheet.Combo.DataSets")); //$NON-NLS-1$
					dataItems.add(Messages.getString("StandardChartDataSheet.Combo.DataSets")); //$NON-NLS-1$
					selectDataTypes.add(Integer.valueOf(SELECT_NEXT));
				}

				for (int i = 0; i < dataSets.length; i++) {
					items.add(dataSets[i].getDisplayName());
					dataItems.add(dataSets[i]);
					selectDataTypes.add(Integer.valueOf(SELECT_DATA_SET));
				}
			}
			if (isDataItemSupported(SELECT_NEW_DATASET)) {
				items.add(Messages.getString("StandardChartDataSheet.NewDataSet")); //$NON-NLS-1$
				dataItems.add(Messages.getString("StandardChartDataSheet.NewDataSet")); //$NON-NLS-1$
				selectDataTypes.add(Integer.valueOf(SELECT_NEW_DATASET));
			}
		}

		if (isDataItemSupported(SELECT_DATA_CUBE)) {
			String[] dataCubes = getDataServiceProvider().getAllDataCubes();
			if (dataCubes.length > 0) {
				if (isDataItemSupported(SELECT_NEXT)) {
					items.add(Messages.getString("StandardChartDataSheet.Combo.DataCubes")); //$NON-NLS-1$
					dataItems.add(Messages.getString("StandardChartDataSheet.Combo.DataCubes")); //$NON-NLS-1$
					selectDataTypes.add(Integer.valueOf(SELECT_NEXT));
				}
				for (int i = 0; i < dataCubes.length; i++) {
					items.add(dataCubes[i]);
					dataItems.add(dataCubes[i]);
					selectDataTypes.add(Integer.valueOf(SELECT_DATA_CUBE));
				}
			}
			if (isDataItemSupported(SELECT_NEW_DATACUBE)) {
				items.add(Messages.getString("StandardChartDataSheet.NewDataCube")); //$NON-NLS-1$
				dataItems.add(Messages.getString("StandardChartDataSheet.NewDataCube")); //$NON-NLS-1$
				selectDataTypes.add(Integer.valueOf(SELECT_NEW_DATACUBE));
			}
		}

		if (isDataItemSupported(SELECT_REPORT_ITEM)) {
			String[] dataRefs = getDataServiceProvider().getAllReportItemReferences();
			if (dataRefs.length > 0) {
				int curSize = items.size();
				if (isDataItemSupported(SELECT_NEXT)) {
					items.add(Messages.getString("StandardChartDataSheet.Combo.ReportItems")); //$NON-NLS-1$
					dataItems.add(Messages.getString("StandardChartDataSheet.Combo.ReportItems")); //$NON-NLS-1$
					selectDataTypes.add(Integer.valueOf(SELECT_NEXT));
				}
				for (int i = 0; i < dataRefs.length; i++) {
					// if cube is not supported, do not list the report item
					// consuming a cube
					if (!isDataItemSupported(SELECT_DATA_CUBE)) {
						if (((ReportItemHandle) getDataServiceProvider().getReportDesignHandle()
								.findElement(dataRefs[i])).getCube() != null) {
							continue;
						}
					}
					items.add(dataRefs[i]);
					dataItems.add(dataRefs[i]);
					selectDataTypes.add(Integer.valueOf(SELECT_REPORT_ITEM));
				}
				// didn't add any reportitem reference
				if (items.size() == curSize + 1) {
					items.remove(curSize);
					dataItems.remove(curSize);
					selectDataTypes.remove(curSize);
				}
			}
		}
		return items.toArray(new String[items.size()]);
	}

	@SuppressWarnings("unchecked")
	protected void updatePredefinedQueriesForInheritXTab() {
		// Get all column bindings.
		List<String> dimensionExprs = new ArrayList<String>();
		List<String> measureExprs = new ArrayList<String>();
		ReportItemHandle reportItemHandle = dataProvider.getReportItemHandle();
		for (Iterator<ComputedColumnHandle> iter = reportItemHandle.getColumnBindings().iterator(); iter.hasNext();) {
			ComputedColumnHandle cch = iter.next();
			ChartItemUtil.loadExpression(exprCodec, cch);
			if (exprCodec.isDimensionExpresion()) {
				dimensionExprs.add(cch.getName());
			} else if (exprCodec.isMeasureExpresion()) {
				measureExprs.add(cch.getName());
			}
		}

		String[] valueExprs = measureExprs.toArray(new String[measureExprs.size()]);

		getContext().addPredefinedQuery(ChartUIConstants.QUERY_CATEGORY, null);
		getContext().addPredefinedQuery(ChartUIConstants.QUERY_OPTIONAL, null);
		getContext().addPredefinedQuery(ChartUIConstants.QUERY_VALUE, valueExprs);
	}

	@SuppressWarnings("unchecked")
	protected void updatePredefinedQueriesForSharingCube() {
		// Get all column bindings.
		List<String> dimensionExprs = new ArrayList<String>();
		List<String> measureExprs = new ArrayList<String>();
		ReportItemHandle reportItemHandle = dataProvider.getReportItemHandle();
		for (Iterator<ComputedColumnHandle> iter = reportItemHandle.getColumnBindings().iterator(); iter.hasNext();) {
			ComputedColumnHandle cch = iter.next();
			// String dataExpr = ExpressionUtil.createJSDataExpression(
			// cch.getName( ) );
			chartItemHelper.loadExpression(exprCodec, cch);
			if (exprCodec.isDimensionExpresion()) {
				dimensionExprs.add(cch.getName());
			} else if (exprCodec.isMeasureExpresion()) {
				// Fixed issue ED 28.
				// Underlying code was reverted to the earlier than
				// bugzilla 246683, since we have enhanced it to
				// support all available measures defined in shared
				// item.

				// Bugzilla 246683.
				// Here if it is sharing with crosstab or
				// multi-view, we just put the measure expression
				// whose aggregate-ons is most into prepared
				// expression query. It will keep correct value to
				// shared crosstab or multi-view.
				measureExprs.add(cch.getName());
			} else if (exprCodec.isCubeBinding(true)) {
				// Just it is under multiple view case, we add those computed measure bindings.
				String dataType = cch.getDataType();
				if (org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL
						.equals(dataType)
						|| org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT
								.equals(dataType)
						|| org.eclipse.birt.report.model.api.elements.DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER
								.equals(dataType)) {
					// It is computed measure binding.
					measureExprs.add(cch.getName());
				}
			}
		}
		String[] categoryExprs = dimensionExprs.toArray(new String[dimensionExprs.size()]);
		String[] yOptionalExprs = categoryExprs;
		String[] valueExprs = measureExprs.toArray(new String[measureExprs.size()]);

		ReportItemHandle referenceHandle = ChartItemUtil.getReportItemReference(itemHandle);
		ReportDataServiceProvider rdsp = this.getDataServiceProvider();
		if (referenceHandle instanceof ExtendedItemHandle && rdsp.isChartReportItemHandle(referenceHandle)) {
			// If the final reference handle is cube with other
			// chart, the valid category and Y optional expressions
			// only allow those expressions defined in shared chart.
			Object referenceCM = ChartItemUtil.getChartFromHandle((ExtendedItemHandle) referenceHandle);
			categoryExprs = rdsp.getSeriesExpressionsFrom(referenceCM, ChartUIConstants.QUERY_CATEGORY);
			yOptionalExprs = rdsp.getSeriesExpressionsFrom(referenceCM, ChartUIConstants.QUERY_OPTIONAL);
			valueExprs = rdsp.getSeriesExpressionsFrom(referenceCM, ChartUIConstants.QUERY_VALUE);

			Chart cm = this.getContext().getModel();
			if (categoryExprs.length > 0) {
				updateCategoryExpression(cm, categoryExprs[0]);
			}
			if (yOptionalExprs.length > 0) {
				updateYOptionalExpressions(cm, yOptionalExprs[0]);
			}
		} else if (dataProvider.checkState(IDataServiceProvider.SHARE_CROSSTAB_QUERY)) {
			// In sharing query with crosstab, the category
			// expression and Y optional expression is decided by
			// value series expression, so here set them to null.
			// And in UI, when the value series expression is
			// selected, it will trigger to set correct category and
			// Y optional expressions.
			categoryExprs = null;
			yOptionalExprs = null;
		}

		getContext().addPredefinedQuery(ChartUIConstants.QUERY_CATEGORY, categoryExprs);
		getContext().addPredefinedQuery(ChartUIConstants.QUERY_OPTIONAL, yOptionalExprs);
		getContext().addPredefinedQuery(ChartUIConstants.QUERY_VALUE, valueExprs);
	}

	private void updatePredefinedQueries() {
		if (dataProvider.isInXTabMeasureCell()) {
			try {
				CrosstabReportItemHandle xtab = ChartCubeUtil.getXtabContainerCell(itemHandle).getCrosstab();

				if (dataProvider.isPartChart()) {
					List<String> levels = ChartCubeUtil.getAllLevelsBindingName(xtab);
					String[] exprs = levels.toArray(new String[levels.size()]);
					if (exprs.length == 2 && dataProvider.isInXTabAggrCell()) {
						// Only one direction is valid for chart in total cell
						if (((ChartWithAxes) getChartModel()).isTransposed()) {
							exprs = new String[] { exprs[1] };
						} else {
							exprs = new String[] { exprs[0] };
						}
					}
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_CATEGORY, exprs);
				} else {
					Iterator<ComputedColumnHandle> columnBindings = ChartItemUtil
							.getAllColumnBindingsIterator(itemHandle);
					List<String> levels = ChartCubeUtil.getAllLevelsBindingName(columnBindings);
					String[] exprs = levels.toArray(new String[levels.size()]);
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_CATEGORY, exprs);
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_OPTIONAL, exprs);

					columnBindings = ChartItemUtil.getAllColumnBindingsIterator(itemHandle);
					List<String> measures = ChartCubeUtil.getAllMeasuresBindingName(columnBindings);
					exprs = measures.toArray(new String[measures.size()]);
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_VALUE, exprs);
				}

			} catch (BirtException e) {
				WizardBase.displayException(e);
			}
		} else {
			if (getCube() == null) {
				try {
					ColumnBindingInfo[] headers = dataProvider.getPreviewHeadersInfo();
					getDataServiceProvider().setPredefinedExpressions(headers);
				} catch (ChartException e) {
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_CATEGORY, null);
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_VALUE, null);
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_OPTIONAL, null);
				}

			} else if (isDataItemSupported(SELECT_DATA_CUBE)) {
				if (dataProvider.isInheritanceOnly() || dataProvider.isSharedBinding()) {
					updatePredefinedQueriesForSharingCube();
				} else if (dataProvider.isInXTabNonAggrCell() && dataProvider.isInheritCube()) {
					updatePredefinedQueriesForInheritXTab();
				}
				// TODO do we need to handle xtab inheritance case? currently we
				// just inherit the cube from xtab essentially
				// else if ( ChartXTabUIUtil.isInheritXTabCell( itemHandle ) )
				// {
				// // Chart in xtab cell and inherits its cube
				// List<String> measureExprs = new ArrayList<String>( );
				// for ( Iterator<ComputedColumnHandle> iter =
				// ChartReportItemUtil.getBindingHolder( itemHandle )
				// .getColumnBindings( )
				// .iterator( ); iter.hasNext( ); )
				// {
				// ComputedColumnHandle cch = iter.next( );
				// if ( ChartCubeUtil.isMeasureExpresion( cch.getExpression( ) )
				// )
				// {
				// measureExprs.add( ExpressionUtil.createJSDataExpression(
				// cch.getName( ) ) );
				// }
				// }
				// String[] valueExprs = measureExprs.toArray( new
				// String[measureExprs.size( )] );
				// getContext( ).addPredefinedQuery(
				// ChartUIConstants.QUERY_CATEGORY,
				// null );
				// getContext( ).addPredefinedQuery(
				// ChartUIConstants.QUERY_OPTIONAL,
				// null );
				// getContext( ).addPredefinedQuery(
				// ChartUIConstants.QUERY_VALUE,
				// valueExprs );
				// }
				else {
					// Updates cube bindings before updating available bindings
					// for chart.
					getDataServiceProvider().update(ChartUIConstants.UPDATE_CUBE_BINDINGS, null);

					Iterator<ComputedColumnHandle> columnBindings = ChartItemUtil
							.getAllColumnBindingsIterator(itemHandle);
					List<String> levels = ChartCubeUtil.getAllLevelsBindingName(columnBindings);
					String[] exprs = levels.toArray(new String[levels.size()]);
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_CATEGORY, exprs);
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_OPTIONAL, exprs);

					columnBindings = ChartItemUtil.getAllColumnBindingsIterator(itemHandle);
					List<String> measures = ChartCubeUtil.getAllMeasuresBindingName(columnBindings);
					exprs = measures.toArray(new String[measures.size()]);
					getContext().addPredefinedQuery(ChartUIConstants.QUERY_VALUE, exprs);
				}
			}
		}

		// Fire event to update predefined queries in outside UI
		fireEvent(btnBinding, EVENT_QUERY);
	}

	/**
	 * Update Y Optional expression with specified expression if current Y optional
	 * expression is null or empty.
	 * 
	 * @param cm   chart model.
	 * @param expr specified expression.
	 */
	protected void updateYOptionalExpressions(Chart cm, String expr) {
		this.dataProvider.update(ChartUIConstants.QUERY_OPTIONAL, expr);
	}

	/**
	 * Update category expression with specified expression if current category
	 * expression is null or empty.
	 * 
	 * @param cm   chart model.
	 * @param expr specified expression.
	 */
	protected void updateCategoryExpression(Chart cm, String expr) {
		this.dataProvider.update(ChartUIConstants.QUERY_CATEGORY, expr);
	}

	@SuppressWarnings("unchecked")
	protected void checkDataBinding() {
		if (getCube() != null && !chartItemHelper.checkCubeBindings(itemHandle,
				DEUtil.getBindingColumnIterator(DEUtil.getBindingHolder(itemHandle)))) {
			ChartWizard.showException(ChartWizard.StaChartDSh_checCube_ID,
					Messages.getString("StandardChartDataSheet.CheckCubeWarning")); //$NON-NLS-1$
		} else {
			ChartWizard.removeException(ChartWizard.StaChartDSh_checCube_ID);
		}
	}

	@Override
	public ISelectDataCustomizeUI createCustomizeUI(ITask task) {
		return new SelectDataDynamicArea(task);
	}

	@Override
	public List<String> getAllValueDefinitions() {
		List<String> dataDefinitions = new ArrayList<String>(2);
		for (SeriesDefinition sd : ChartUIUtil.getAllOrthogonalSeriesDefinitions(getChartModel())) {
			for (Query query : sd.getDesignTimeSeries().getDataDefinition()) {
				String name = exprCodec.getBindingName(query.getDefinition());
				if (name != null) {
					dataDefinitions.add(name);
				}
			}
		}

		return dataDefinitions;
	}

	protected String getDataPreviewDescription() {
		return Messages.getString("StandardChartDataSheet.Label.ToBindADataColumn"); //$NON-NLS-1$
	}
}
