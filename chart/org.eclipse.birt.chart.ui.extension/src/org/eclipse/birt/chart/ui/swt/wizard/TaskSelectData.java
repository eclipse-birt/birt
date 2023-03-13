/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainterBase;
import org.eclipse.birt.chart.ui.swt.ColorPalette;
import org.eclipse.birt.chart.ui.swt.DataDefinitionTextManager;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartWizardContext;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPreviewable;
import org.eclipse.birt.chart.ui.swt.series.BubbleSeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent;
import org.eclipse.birt.chart.ui.swt.wizard.preview.ChartLivePreviewThread;
import org.eclipse.birt.chart.ui.swt.wizard.preview.LivePreviewTask;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * This task is used for data binding. The UI is mainly managed by
 * SelectDataDynamicArea. For the sake of customization, use IChartDataSheet
 * implementation to create specific UI sections.
 *
 */
public class TaskSelectData extends SimpleTask implements ITaskChangeListener, ITaskPreviewable, Listener {

	private final static int CENTER_WIDTH_HINT = 400;
	protected IChartPreviewPainter previewPainter = null;
	private Canvas previewCanvas = null;

	private ISelectDataCustomizeUI dynamicArea;

	private SashForm foSashForm;
	private Point fLeftSize;
	private Point fRightSize;
	private static final int DEFAULT_HEIGHT = 580;
	private Composite fHeaderArea;
	private ScrolledComposite fDataArea;
	private int[] aSashWeight;

	public TaskSelectData() {
		super(Messages.getString("TaskSelectData.TaskExp")); //$NON-NLS-1$
		setDescription(Messages.getString("TaskSelectData.Task.Description")); //$NON-NLS-1$
	}

	@Override
	public void createControl(Composite parent) {
		getDataSheet().setChartModel(getChartModel());
		getDataSheet().addListener(this);

		// Initialize chart types first.
		ChartUIUtil.populateTypeTable(getContext());

		if (topControl == null || topControl.isDisposed()) {
			topControl = new Composite(parent, SWT.NONE);
			GridLayout gridLayout = new GridLayout(3, false);
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			topControl.setLayout(gridLayout);
			topControl.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

			dynamicArea = createDataComponentsUI();
			getCustomizeUI().init();

			ScrolledComposite sc = new ScrolledComposite(topControl, SWT.H_SCROLL);
			{
				GridLayout layout = new GridLayout();
				sc.setLayout(layout);
				GridData gridData = new GridData(GridData.FILL_BOTH);
				sc.setLayoutData(gridData);
				sc.setExpandHorizontal(true);
				sc.setExpandVertical(true);

			}

			Composite cmp = new Composite(sc, SWT.None);
			cmp.setLayout(new GridLayout());
			cmp.setLayoutData(new GridData(GridData.FILL_BOTH));
			sc.setContent(cmp);
			sc.setMinWidth(800);

			foSashForm = new SashForm(cmp, SWT.VERTICAL);
			{
				GridLayout layout = new GridLayout();
				foSashForm.setLayout(layout);
				GridData gridData = new GridData(GridData.FILL_BOTH);
				// gridData.heightHint = DEFAULT_HEIGHT;
				gridData.widthHint = 800;
				foSashForm.setLayoutData(gridData);
			}
			foSashForm.addListener(SWT.Resize, this);
			placeComponents();
			previewPainter = createPreviewPainter();
			// init( );
			resize();
		} else {
			customizeUI();
		}
		if (getChartModel() instanceof ChartWithAxes) {
			checkDataTypeForChartWithAxes();
		}
		doPreview();
		// Refresh all data definition text
		DataDefinitionTextManager.getInstance().refreshAll();
		DataDefinitionTextManager.getInstance().setContext((IChartWizardContext) getContext());
		ChartUIUtil.checkGroupType((ChartWizardContext) getContext(), getChartModel());
		ChartUIUtil.checkAggregateType((ChartWizardContext) getContext());

		bindHelp();
	}

	protected void bindHelp() {
		ChartUIUtil.bindHelp(getControl(), ChartHelpContextIds.TASK_SELECT_DATA);
	}

	protected void customizeUI() {
		getCustomizeUI().init();
		refreshLeftArea();
		refreshRightArea();
		refreshBottomArea();
		getCustomizeUI().layoutAll();
		autoSash();
	}

	protected ISelectDataCustomizeUI createDataComponentsUI() {
		return getDataSheet().createCustomizeUI(this);
	}

	private void resize() {
		Point headerSize = computeHeaderAreaSize();
		int weight[] = foSashForm.getWeights();
		if (headerSize.y != DEFAULT_HEIGHT / 2) {
			weight[0] = headerSize.y;
			weight[1] = DEFAULT_HEIGHT / 2;
			foSashForm.setWeights(weight);
			((GridData) foSashForm.getLayoutData()).heightHint = weight[0] + weight[1];
		} else {
			weight[0] = 200;
			weight[1] = 200;
			foSashForm.setWeights(weight);
			((GridData) foSashForm.getLayoutData()).heightHint = DEFAULT_HEIGHT;
		}
		aSashWeight = weight;
	}

	private void refreshLeftArea() {
		getCustomizeUI().refreshLeftBindingArea();
		getCustomizeUI().selectLeftBindingArea(true, null);
	}

	private void refreshRightArea() {
		getCustomizeUI().refreshRightBindingArea();
		getCustomizeUI().selectRightBindingArea(true, null);
	}

	private void refreshBottomArea() {
		getCustomizeUI().refreshBottomBindingArea();
		getCustomizeUI().selectBottomBindingArea(true, null);
	}

	private void placeComponents() {
		ChartAdapter.beginIgnoreNotifications();
		try {
			createHeadArea();// place two rows

			createDataArea();

		} finally {
			// THIS IS IN A FINALLY BLOCK TO ENSURE THAT NOTIFICATIONS ARE
			// ENABLED EVEN IF ERRORS OCCUR DURING UI INITIALIZATION
			ChartAdapter.endIgnoreNotifications();
		}
	}

	private void createDataArea() {
		fDataArea = new ScrolledComposite(foSashForm, SWT.VERTICAL | SWT.V_SCROLL);
		{
			GridLayout gl = new GridLayout();
			fDataArea.setLayout(gl);
			GridData gd = new GridData(GridData.FILL_VERTICAL);
			fDataArea.setLayoutData(gd);
			fDataArea.setExpandHorizontal(true);
			fDataArea.setExpandVertical(true);

		}

		Composite dataComposite = new Composite(fDataArea, SWT.NONE);
		{
			GridLayout gl = new GridLayout(2, false);
			gl.marginLeft = fLeftSize.x;
			dataComposite.setLayout(gl);
			GridData gd = new GridData(GridData.FILL_BOTH);
			dataComposite.setLayoutData(gd);
		}
		fDataArea.setContent(dataComposite);

		getDataSheet().createDataSelector(dataComposite);
		GridData gd = new GridData();
		gd.widthHint = fRightSize.x - 40;
		new Label(dataComposite, SWT.NONE).setLayoutData(gd);

		getDataSheet().createDataDragSource(dataComposite);
		getDataSheet().createActionButtons(dataComposite);

		new Label(dataComposite, SWT.NONE);

		fDataArea.setMinHeight(fDataArea.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	}

	private void createHeadArea() {
		// Create header area.
		fHeaderArea = new Composite(foSashForm, SWT.NONE);
		{
			GridLayout layout = new GridLayout(3, false);
			fHeaderArea.setLayout(layout);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			fHeaderArea.setLayoutData(gd);
		}

		{
			Composite cmpLeftContainer = ChartUIUtil.createCompositeWrapper(fHeaderArea);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
			gridData.verticalSpan = 2;
			cmpLeftContainer.setLayoutData(gridData);
			getCustomizeUI().createLeftBindingArea(cmpLeftContainer);
			fLeftSize = cmpLeftContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
		createPreviewArea(fHeaderArea);
		{
			Composite cmpRightContainer = ChartUIUtil.createCompositeWrapper(fHeaderArea);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
			gridData.verticalSpan = 2;
			cmpRightContainer.setLayoutData(gridData);
			getCustomizeUI().createRightBindingArea(cmpRightContainer);
			fRightSize = cmpRightContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		}
		{
			Composite cmpBottomContainer = ChartUIUtil.createCompositeWrapper(fHeaderArea);
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
			cmpBottomContainer.setLayoutData(gridData);
			getCustomizeUI().createBottomBindingArea(cmpBottomContainer);
		}
	}

	private Point computeHeaderAreaSize() {
		return fHeaderArea.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	private Point computeDataAreaSize() {
		return fDataArea.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	}

	private void createPreviewArea(Composite parent) {
		Composite cmpPreview = ChartUIUtil.createCompositeWrapper(parent);
		{
			GridData gridData = new GridData(GridData.FILL_BOTH);
			gridData.widthHint = CENTER_WIDTH_HINT;
			gridData.heightHint = 200;
			cmpPreview.setLayoutData(gridData);
		}

		Label label = new Label(cmpPreview, SWT.NONE);
		{
			label.setFont(JFaceResources.getBannerFont());
			label.setText(Messages.getString("TaskSelectData.Label.ChartPreview")); //$NON-NLS-1$
		}

		previewCanvas = new Canvas(cmpPreview, SWT.BORDER);
		{
			GridData gd = new GridData(GridData.FILL_BOTH);
			previewCanvas.setLayoutData(gd);
			previewCanvas.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		}
	}

	@Override
	public IChartPreviewPainter createPreviewPainter() {
		ChartPreviewPainter painter = new ChartPreviewPainter((ChartWizardContext) getContext());
		getPreviewCanvas().addPaintListener(painter);
		getPreviewCanvas().addControlListener(painter);
		painter.setPreview(getPreviewCanvas());
		return painter;
	}

	protected Chart getChartModel() {
		if (getContext() == null) {
			return null;
		}
		return ((ChartWizardContext) getContext()).getModel();
	}

	protected IDataServiceProvider getDataServiceProvider() {
		return ((ChartWizardContext) getContext()).getDataServiceProvider();
	}

	@Override
	public void dispose() {
		super.dispose();
		// No need to dispose other widgets
		if (previewPainter != null) {
			previewPainter.dispose();
		}
		previewPainter = null;
		if (dynamicArea != null) {
			dynamicArea.dispose();
		}
		dynamicArea = null;

		// Restore color registry
		ColorPalette.getInstance().restore();

		// Remove all registered data definition text
		DataDefinitionTextManager.getInstance().removeAll();
		DataDefinitionTextManager.getInstance().setContext(null);
	}

	private ISelectDataCustomizeUI getCustomizeUI() {
		return dynamicArea;
	}

	@Override
	public void handleEvent(Event event) {
		if (event.data == getDataSheet() || event.data instanceof BaseDataDefinitionComponent) {
			if (event.type == IChartDataSheet.EVENT_PREVIEW) {
				if (getChartModel() instanceof ChartWithAxes) {
					checkDataTypeForChartWithAxes();
				}
				doPreview();
				updateApplyButton();
			} else if (event.type == IChartDataSheet.EVENT_QUERY) {
				getCustomizeUI().refreshBottomBindingArea();
				getCustomizeUI().refreshLeftBindingArea();
				getCustomizeUI().refreshRightBindingArea();

				// Above statements might create Text or Combo widgets for
				// expression, so here must invoke refreshAll to clear old
				// widgets info.
				DataDefinitionTextManager.getInstance().refreshAll();
			}
		}
		if (event.type == IChartDataSheet.EVENT_QUERY) {
			if (ChartUIConstants.QUERY_CATEGORY.equals(event.data)) {
				getCustomizeUI().refreshBottomBindingArea();

			} else if (ChartUIConstants.QUERY_OPTIONAL.equals(event.data)) {
				getCustomizeUI().refreshRightBindingArea();

			} else if (ChartUIConstants.QUERY_VALUE.equals(event.data)) {
				getCustomizeUI().refreshLeftBindingArea();
			}

			DataDefinitionTextManager.getInstance().refreshAll();
		} else if (event.type == SWT.Resize) {
			autoSash();
		}
	}

	private void autoSash() {
		int headerHeight = computeHeaderAreaSize().y;
		int dataHeight = computeDataAreaSize().y;
		int height = foSashForm.getClientArea().height;
		int weight[] = foSashForm.getWeights();
		int currentRatio = Math.round((float) weight[0] / (float) weight[1] * 100);
		int previousRatio = Math.round((float) aSashWeight[0] / (float) aSashWeight[1] * 100);
		if (currentRatio != previousRatio) {
			if (height > headerHeight && height > dataHeight) {
				if (height > headerHeight + dataHeight) {
					weight[0] = height - dataHeight;
					weight[1] = dataHeight + 1;
				} else {
					weight[0] = headerHeight;
					weight[1] = height - headerHeight;
				}
			}
			foSashForm.setWeights(weight);
			aSashWeight = weight;
		}
	}

	@Override
	public void changeTask(Notification notification) {
		if (previewPainter != null) {
			if (notification == null) {
				if (getChartModel() instanceof ChartWithAxes) {
					checkDataTypeForChartWithAxes();
				}
				return;
			}

			// if remove the seriesDefition, remove the error messages
			// associated to it
			if (notification.getEventType() == Notification.REMOVE && notification.getNotifier() instanceof Axis) {
				ChartWizard.removeAllExceptions("" + notification.getOldValue() //$NON-NLS-1$
						.hashCode());
			}

			if ((notification.getNotifier() instanceof Query
					&& ((Query) notification.getNotifier()).eContainer() instanceof Series)) {
				checkDataType((Query) notification.getNotifier(),
						(Series) ((Query) notification.getNotifier()).eContainer());
			}

			if (notification.getNotifier() instanceof SeriesDefinition && getChartModel() instanceof ChartWithAxes) {
				checkDataTypeForChartWithAxes();
			}

			// Notify change to customize UI
			getCustomizeUI().notifyChange(notification);

			// Query and series change need to update Live Preview
			if (notification.getNotifier() instanceof Query || notification.getNotifier() instanceof Axis
					|| notification.getNotifier() instanceof SeriesDefinition
					|| notification.getNotifier() instanceof SeriesGrouping) {
				doPreview();
			} else if (ChartPreviewPainterBase.isLivePreviewActive()) {
				ChartAdapter.beginIgnoreNotifications();
				ChartUIUtil.syncRuntimeSeries(getChartModel());
				ChartAdapter.endIgnoreNotifications();

				doPreview();
			} else {
				doPreview();
			}
		}
	}

	private void checkDataType(Query query, Series series) {
		String expression = query.getDefinition();

		Axis axis = null;
		for (EObject o = query; o != null;) {
			o = o.eContainer();
			if (o instanceof Axis) {
				axis = (Axis) o;
				break;
			}
		}

		Collection<ISeriesUIProvider> cRegisteredEntries = ChartUIExtensionsImpl.instance()
				.getSeriesUIComponents(((ChartWizardContext) getContext()).getIdentifier());
		Iterator<ISeriesUIProvider> iterEntries = cRegisteredEntries.iterator();

		String sSeries = null;
		while (iterEntries.hasNext()) {
			ISeriesUIProvider provider = iterEntries.next();
			sSeries = provider.getSeriesClass();

			if (sSeries.equals(series.getClass().getName())) {
				boolean isMagicAgg = false;
				if (getChartModel() instanceof ChartWithAxes) {
					DataType dataType = getDataServiceProvider().getDataType(expression);
					SeriesDefinition baseSD = (ChartUIUtil.getBaseSeriesDefinitions(getChartModel()).get(0));
					SeriesDefinition orthSD;
					orthSD = (SeriesDefinition) series.eContainer();

					String aggFunc = null;
					try {
						aggFunc = ChartUtil.getAggregateFuncExpr(orthSD, baseSD, query);
						ChartWizard.removeException(ChartWizard.PluginSet_getAggF_ID);
					} catch (ChartException e) {
						ChartWizard.showException(ChartWizard.PluginSet_getAggF_ID, e.getLocalizedMessage());
					}

					if (baseSD != orthSD && baseSD.eContainer() != axis // Is
					// not
					// without
					// axis
					// chart.
							&& ChartUtil.isMagicAggregate(aggFunc)) {
						// Only check aggregation is count in Y axis
						dataType = DataType.NUMERIC_LITERAL;
						isMagicAgg = true;
					}

					if (!isValidatedAxis(dataType, axis.getType())) {
						AxisType[] axisTypes = provider.getCompatibleAxisType(series);
						// do not check bubble size query for axis type
						int[] validationIndex = provider instanceof BubbleSeriesUIProvider ? new int[] { 0 }
								: series.getDefinedDataDefinitionIndex();
						boolean needValidate = false;
						for (int i = 0; i < validationIndex.length; i++) {
							if (query == series.getDataDefinition().get(i)) {
								needValidate = true;
								break;
							}
						}
						SeriesDefinition sd = (SeriesDefinition) series.eContainer();
						if (((Axis) sd.eContainer()).getSeriesDefinitions().indexOf(sd) > 0) {
							needValidate = false;
						}
						if (needValidate) {
							for (int i = 0; i < axisTypes.length; i++) {
								if (isValidatedAxis(dataType, axisTypes[i])) {
									axisNotification(axis, axisTypes[i]);
									axis.setType(axisTypes[i]);
									break;
								}
							}
						}
					}

				}

				try {
					if (!isMagicAgg) {
						provider.validateSeriesBindingType(series, getDataServiceProvider());
					}
					ChartWizard.removeException(ChartWizard.CheckSeriesBindingType_ID + series.eContainer().hashCode());
				} catch (ChartException ce) {
					ChartWizard.showException(ChartWizard.CheckSeriesBindingType_ID + series.eContainer().hashCode(),
							Messages.getFormattedString("TaskSelectData.Warning.TypeCheck", //$NON-NLS-1$
									new String[] { ce.getLocalizedMessage(), series.getDisplayName() }));
				}

				break;
			}
		}
	}

	private boolean isValidatedAxis(DataType dataType, AxisType axisType) {
		if (dataType == null) {
			return true;
		} else if ((dataType == DataType.DATE_TIME_LITERAL) && (axisType == AxisType.DATE_TIME_LITERAL)) {
			return true;
		} else if ((dataType == DataType.NUMERIC_LITERAL)
				&& ((axisType == AxisType.LINEAR_LITERAL) || (axisType == AxisType.LOGARITHMIC_LITERAL))) {
			return true;
		} else if ((dataType == DataType.TEXT_LITERAL) && (axisType == AxisType.TEXT_LITERAL)) {
			return true;
		}
		return false;
	}

	private void axisNotification(Axis axis, AxisType type) {
		ChartAdapter.beginIgnoreNotifications();
		{
			convertSampleData(axis, type);
			axis.setFormatSpecifier(null);

			EList<MarkerLine> markerLines = axis.getMarkerLines();
			for (int i = 0; i < markerLines.size(); i++) {
				(markerLines.get(i)).setFormatSpecifier(null);
			}

			EList<MarkerRange> markerRanges = axis.getMarkerRanges();
			for (int i = 0; i < markerRanges.size(); i++) {
				(markerRanges.get(i)).setFormatSpecifier(null);
			}
		}
		ChartAdapter.endIgnoreNotifications();
	}

	private void convertSampleData(Axis axis, AxisType axisType) {
		if ((axis.getAssociatedAxes() != null) && (axis.getAssociatedAxes().size() != 0)) {
			BaseSampleData bsd = getChartModel().getSampleData().getBaseSampleData().get(0);
			bsd.setDataSetRepresentation(
					ChartUIUtil.getConvertedSampleDataRepresentation(axisType, bsd.getDataSetRepresentation(), 0));
		} else {
			int iStartIndex = getFirstSeriesDefinitionIndexForAxis(axis);
			int iEndIndex = iStartIndex + axis.getSeriesDefinitions().size();

			int iOSDSize = getChartModel().getSampleData().getOrthogonalSampleData().size();
			for (int i = 0; i < iOSDSize; i++) {
				OrthogonalSampleData osd = getChartModel().getSampleData().getOrthogonalSampleData().get(i);
				if (osd.getSeriesDefinitionIndex() >= iStartIndex && osd.getSeriesDefinitionIndex() < iEndIndex) {
					osd.setDataSetRepresentation(ChartUIUtil.getConvertedSampleDataRepresentation(axisType,
							osd.getDataSetRepresentation(), i));
				}
			}
		}
	}

	private int getFirstSeriesDefinitionIndexForAxis(Axis axis) {
		List<Axis> axisList = ((ChartWithAxes) getChartModel()).getAxes().get(0).getAssociatedAxes();
		int index = 0;
		for (int i = 0; i < axisList.size(); i++) {
			if (axis.equals(axisList.get(i))) {
				index = i;
				break;
			}
		}
		int iTmp = 0;
		for (int i = 0; i < index; i++) {
			iTmp += ChartUIUtil.getAxisYForProcessing((ChartWithAxes) getChartModel(), i).getSeriesDefinitions().size();
		}
		return iTmp;
	}

	private void updateApplyButton() {
		if (container instanceof ChartWizard) {
			((ChartWizard) container).updateApplyButton();
		}
	}

	private void checkDataTypeForChartWithAxes() {
		List<SeriesDefinition> osds = ChartUIUtil.getAllOrthogonalSeriesDefinitions(getChartModel());
		for (int i = 0; i < osds.size(); i++) {
			SeriesDefinition sd = osds.get(i);
			Series series = sd.getDesignTimeSeries();
			checkDataType(ChartUIUtil.getDataQuery(sd, 0), series);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.ui.frameworks.taskwizard.SimpleTask#getImage()
	 */
	@Override
	public Image getImage() {
		return UIHelper.getImage(ChartUIConstants.IMAGE_TASK_DATA);
	}

	private IChartDataSheet getDataSheet() {
		return ((ChartWizardContext) getContext()).getDataSheet();
	}

	@Override
	public void doPreview() {
		if (getChartModel() instanceof ChartWithAxes) {
			checkDataTypeForChartWithAxes();
		} else {
			ChartWizard.removeAllExceptions(ChartWizard.CheckSeriesBindingType_ID);
		}
		LivePreviewTask lpt = new LivePreviewTask(Messages.getString("TaskFormatChart.LivePreviewTask.BindData"), null); //$NON-NLS-1$
		// Add a task to retrieve data and bind data to chart.
		lpt.addTask(new LivePreviewTask() {
			@Override
			public void run() {
				if (previewPainter != null) {
					setParameter(ChartLivePreviewThread.PARAM_CHART_MODEL,
							ChartUIUtil.prepareLivePreview(getChartModel(), getDataServiceProvider(),
									((ChartWizardContext) context).getActionEvaluator()));
				}
			}
		});

		// Add a task to render chart.
		lpt.addTask(new LivePreviewTask() {
			@Override
			public void run() {
				if (previewCanvas != null && previewCanvas.getDisplay() != null
						&& !previewCanvas.getDisplay().isDisposed()) {
					previewCanvas.getDisplay().syncExec(new Runnable() {

						@Override
						public void run() {
							// Repaint chart.
							if (previewPainter != null) {
								Chart cm = (Chart) getParameter(ChartLivePreviewThread.PARAM_CHART_MODEL);
								previewPainter.renderModel(cm);
							}
						}
					});
				}
			}
		});

		// Add live preview tasks to live preview thread.
		((ChartLivePreviewThread) ((ChartWizardContext) context).getLivePreviewThread())
				.setParentShell(getPreviewCanvas().getShell());
		((ChartLivePreviewThread) ((ChartWizardContext) context).getLivePreviewThread()).add(lpt);
	}

	@Override
	public Canvas getPreviewCanvas() {
		return previewCanvas;
	}

	@Override
	public boolean isPreviewable() {
		return true;
	}
}
