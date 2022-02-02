/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.computation.withaxes.SharedScaleContext;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IExternalizer;
import org.eclipse.birt.chart.factory.IResourceFinder;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.api.IChartReportItem;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.script.IChartEventHandler;
import org.eclipse.birt.chart.script.internal.ChartWithAxesImpl;
import org.eclipse.birt.chart.script.internal.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TemplateParameterDefinitionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IChoiceDefinition;
import org.eclipse.birt.report.model.api.extension.ICompatibleReportItem;
import org.eclipse.birt.report.model.api.extension.IElementCommand;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItem;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.emf.common.util.EList;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;

import com.ibm.icu.util.ULocale;

/**
 * ChartReportItemImpl
 */
@SuppressWarnings("unchecked")
public class ChartReportItemImpl extends ReportItem
		implements IChartReportItem, ICompatibleReportItem, IResourceFinder, IExternalizer {

	protected Chart cm = null;

	private final Serializer serializer;

	private Object oDesignerRepresentation = null;

	private static final List<IChoiceDefinition> liLegendPositions = new LinkedList<IChoiceDefinition>();

	private static final List<IChoiceDefinition> liLegendAnchors = new LinkedList<IChoiceDefinition>();

	private static final List<IChoiceDefinition> liChartDimensions = new LinkedList<IChoiceDefinition>();

	protected transient ExtendedItemHandle handle = null;

	private transient SharedScaleContext sharedScale = null;

	private transient boolean bCopied = false;

	protected static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	static {
		// SUPPRESS EVERYTHING EXCEPT FOR ERRORS
		// DefaultLoggerImpl.instance().setVerboseLevel(ILogger.ERROR);

		// SETUP LEGEND POSITION CHOICE LIST
		IChoiceDefinition icd;
		String sName, sLowercaseName;
		for (Position position : Position.VALUES) {
			sName = position.getName();
			sLowercaseName = sName.toLowerCase(Locale.US);
			if (sLowercaseName.equals("outside")) //$NON-NLS-1$
			{
				continue;
			}
			icd = new ChartChoiceDefinitionImpl("choice.legend.position." + sLowercaseName, sName, null); //$NON-NLS-1$
			liLegendPositions.add(icd);
		}

		// SETUP LEGEND ANCHOR CHOICE LIST
		for (Anchor anchor : Anchor.VALUES) {
			sName = anchor.getName();
			sLowercaseName = sName.toLowerCase(Locale.US);
			icd = new ChartChoiceDefinitionImpl("choice.legend.anchor." + sLowercaseName, sName, null); //$NON-NLS-1$
			liLegendAnchors.add(icd);
		}

		// SETUP CHART DIMENSION CHOICE LIST
		for (ChartDimension dimension : ChartDimension.VALUES) {
			sName = dimension.getName();
			sLowercaseName = sName.toLowerCase(Locale.US);
			icd = new ChartChoiceDefinitionImpl("choice.chart.dimension." + sLowercaseName, sName, null); //$NON-NLS-1$
			liChartDimensions.add(icd);
		}
	};

	/**
	 * The constructor.
	 */
	public ChartReportItemImpl(ExtendedItemHandle handle) {
		this.handle = handle;
		this.serializer = ChartReportItemUtil.instanceSerializer(handle);
	}

	/**
	 * Set the chart directly (no command)
	 */
	public void setModel(Chart chart) {
		this.cm = chart;
	}

	/**
	 * Set the shared scale directly (no command)
	 */
	public void setSharedScale(SharedScaleContext sharedScale) {
		this.sharedScale = sharedScale;
	}

	/**
	 * Returns the design element handle.
	 */
	public ExtendedItemHandle getHandle() {
		return this.handle;
	}

	/**
	 * Sets the design element handle.
	 */
	public void setHandle(ExtendedItemHandle handle) {
		if (this.handle != handle) {
			this.handle = handle;
		} else {
			// When two handles are equal, hostChart reference should be
			// updated, so copy state should clean.
			this.bCopied = false;
		}
	}

	public void executeSetSimplePropertyCommand(DesignElementHandle eih, String propName, Object oldValue,
			Object newValue) {
		if (handle == null) {
			return;
		}
		IElementCommand command = new ChartSimplePropertyCommandImpl(eih, this, propName, newValue, oldValue);

		this.handle.getModuleHandle().getCommandStack().execute(command);
	}

	public void executeSetModelCommand(ExtendedItemHandle eih, Chart oldChart, Chart newChart) {
		if (handle == null) {
			return;
		}
		IElementCommand command = new ChartElementCommandImpl(eih, this, oldChart, newChart);

		this.handle.getModuleHandle().getCommandStack().execute(command);
	}

	/**
	 * @param oDesignerRepresentation
	 */
	public final void setDesignerRepresentation(Object oDesignerRepresentation) {
		this.oDesignerRepresentation = oDesignerRepresentation;
	}

	public final Object getDesignerRepresentation() {
		return oDesignerRepresentation;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#serialize(java.lang.String)
	 */
	public ByteArrayOutputStream serialize(String propName) {
		if (propName != null && propName.equalsIgnoreCase(ChartReportItemUtil.PROPERTY_XMLPRESENTATION)) {
			if (!ChartCubeUtil.isAxisChart(handle)) {
				// Do not serialize axis chart, since it always uses reference
				// as chart model
				try {
					return serializer.asXml(cm, true);
				} catch (Exception e) {
					logger.log(e);
					return new ByteArrayOutputStream();
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElement#deserialize(java.lang.
	 * String, java.io.ByteArrayInputStream)
	 */
	public void deserialize(String propName, ByteArrayInputStream data) throws ExtendedElementException {
		if (propName != null && propName.equalsIgnoreCase(
				org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants.PROPERTY_XMLPRESENTATION)) {
			try {
				cm = serializer.fromXml(data, true);
				doCompatibility(cm);

				// This fix is only for SCR 95978, for the version 3.2.10 of
				// report design file and previous version.
				String reportVer = handle.getModuleHandle().getVersion();
				adjustNumberFormat(reportVer);
			} catch (IOException e) {
				logger.log(e);
				cm = null;
			}
		}
	}

	private void doCompatibility(Chart cm) {
		// we use the base series' format specifier for category legend
		// before.
		// for compatibility after the fix of #237578
		if (cm.getLegend().getItemType() == LegendItemType.CATEGORIES_LITERAL) {
			SeriesDefinition sdBase = ChartUtil.getBaseSeriesDefinitions(cm).get(0);
			if (cm.getLegend().getFormatSpecifier() == null && sdBase.getFormatSpecifier() != null) {
				cm.getLegend().setFormatSpecifier(sdBase.getFormatSpecifier().copyInstance());
			}
		}

		// Create a default emptymessage if there is no.
		// Thus old report will behave just like before.
		if (cm.getEmptyMessage() == null) {
			Label la = LabelImpl.create();
			la.setVisible(false);
			cm.setEmptyMessage(la);
		}
	}

	/**
	 * Adjust number format specifier for old version number.
	 * 
	 * @param reportVer the version number of report.
	 */
	private void adjustNumberFormat(String reportVer) {
		// If the version of report design file is less than 3.2.10, use old
		// logic to make the multiplier to multiple 100 for % suffix.
		if (reportVer == null || compareVersion(reportVer, "3.2.9") > 0) //$NON-NLS-1$
		{
			// Version is larger than 3.2.9, directly return.
			return;
		}

		// Version is older than 3.2.10, change Number format to use old logic
		// to
		// format multiplier of 0.01.
		if (cm instanceof ChartWithAxes) {
			ChartWithAxes cwa = (ChartWithAxes) cm;
			Axis[] baseAxis = cwa.getBaseAxes();
			if (baseAxis.length <= 0) {
				return;
			}

			adjustSingleNumberFormat(baseAxis[0].getFormatSpecifier());

			Axis[] yAxis = cwa.getOrthogonalAxes(baseAxis[0], true);
			if (yAxis.length <= 0) {
				return;
			}

			for (int i = 0; i < yAxis.length; i++) {
				adjustSingleNumberFormat(yAxis[i].getFormatSpecifier());

				EList<SeriesDefinition> sds = yAxis[i].getSeriesDefinitions();
				for (int j = 0; j < sds.size(); j++) {
					SeriesDefinition sd = sds.get(j);
					adjustSingleNumberFormat(sd.getFormatSpecifier());

					EList<DataPointComponent> dpcs = sd.getDesignTimeSeries().getDataPoint().getComponents();
					for (int k = 0; k < dpcs.size(); k++) {
						adjustSingleNumberFormat(dpcs.get(k).getFormatSpecifier());
					}
				}
			}
		} else if (cm instanceof ChartWithoutAxes) {
			ChartWithoutAxes cwa = (ChartWithoutAxes) cm;
			EList<SeriesDefinition> categories = cwa.getSeriesDefinitions();
			if (categories.size() > 0) {
				EList<SeriesDefinition> sds = categories.get(0).getSeriesDefinitions();
				for (int j = 0; j < sds.size(); j++) {
					SeriesDefinition sd = sds.get(j);
					adjustSingleNumberFormat(sd.getFormatSpecifier());

					EList<DataPointComponent> dpcs = sd.getDesignTimeSeries().getDataPoint().getComponents();
					for (int k = 0; k < dpcs.size(); k++) {
						adjustSingleNumberFormat(dpcs.get(k).getFormatSpecifier());
					}
				}
			}
		}
	}

	private void adjustSingleNumberFormat(FormatSpecifier fs) {
		if (!(fs instanceof NumberFormatSpecifier)) {
			return;
		}
		NumberFormatSpecifier nfs = (NumberFormatSpecifier) fs;
		String suffix = nfs.getSuffix();
		if ("%".equals(suffix)) //$NON-NLS-1$
		{
			double multiplier = nfs.getMultiplier();
			if (!Double.isNaN(multiplier) && ChartUtil.mathEqual(multiplier, 0.01)) {
				nfs.setMultiplier(100 * multiplier);
			}
		}
	}

	/**
	 * Compare version number, the format of version number should be X.X.X style.
	 * 
	 * @param va version number 1.
	 * @param vb version number 2.
	 * @return
	 */
	private static int compareVersion(String va, String vb) {
		return ChartUtil.compareVersion(va, vb);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IReportItem#
	 * getScriptPropertyDefinition()
	 */
	public IPropertyDefinition getScriptPropertyDefinition() {
		if (cm == null) {
			logger.log(ILogger.WARNING, Messages.getString("ChartReportItemImpl.log.RequestForScriptPropertyDefn")); //$NON-NLS-1$
			return null;
		}

		return new ChartPropertyDefinitionImpl(null, "script", "property.script", false, //$NON-NLS-1$ //$NON-NLS-2$
				IPropertyType.STRING_TYPE, null, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#getPropertyDefinitions()
	 */
	public IPropertyDefinition[] getPropertyDefinitions() {
		if (cm == null) {
			return null;
		}

		return new IPropertyDefinition[] {

				new ChartPropertyDefinitionImpl(null, "title.value", "property.label.title.value", false, //$NON-NLS-1$ //$NON-NLS-2$
						IPropertyType.STRING_TYPE, null, null, null),

				new ChartPropertyDefinitionImpl(null, "title.font.rotation", "property.label.title.font.rotation", //$NON-NLS-1$ //$NON-NLS-2$
						false, IPropertyType.FLOAT_TYPE, null, null, null),

				new ChartPropertyDefinitionImpl(null, "legend.position", "property.label.legend.position", false, //$NON-NLS-1$ //$NON-NLS-2$
						IPropertyType.CHOICE_TYPE, liLegendPositions, null, null),

				new ChartPropertyDefinitionImpl(null, "legend.anchor", "property.label.legend.anchor", false, //$NON-NLS-1$ //$NON-NLS-2$
						IPropertyType.CHOICE_TYPE, liLegendAnchors, null, null),

				new ChartPropertyDefinitionImpl(null, "chart.dimension", "property.label.chart.dimension", false, //$NON-NLS-1$ //$NON-NLS-2$
						IPropertyType.CHOICE_TYPE, liChartDimensions, null, null),

				new ChartPropertyDefinitionImpl(null, "plot.transposed", "property.label.chart.plot.transposed", false, //$NON-NLS-1$ //$NON-NLS-2$
						IPropertyType.BOOLEAN_TYPE, null, null, null),

				new ChartPropertyDefinitionImpl(null, "script", "property.script", false, //$NON-NLS-1$ //$NON-NLS-2$
						IPropertyType.STRING_TYPE, null, null, null),

				new ChartPropertyDefinitionImpl(null, "onRender", "property.onRender", false, //$NON-NLS-1$ //$NON-NLS-2$
						IPropertyType.SCRIPT_TYPE, null, null, null, new ChartPropertyMethodInfo("onRender", //$NON-NLS-1$
								null, null, null, null, false, false)),

		};
	}

	public IMethodInfo[] getMethods(String scriptName) {
		if (scriptName != null && scriptName.equals(ChartReportItemUtil.PROPERTY_ONRENDER)) {
			ScriptClassInfo info = new ScriptClassInfo(IChartEventHandler.class);
			List<IMethodInfo> list = info.getMethods();
			Collections.sort(list, new Comparator<IMethodInfo>() {

				public int compare(IMethodInfo arg0, IMethodInfo arg1) {
					String name0 = arg0.getName();
					String name1 = arg1.getName();
					if (name0.startsWith("before") && name1.startsWith("after")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						return -1;
					}
					if (name0.startsWith("after") && name1.startsWith("before")) //$NON-NLS-1$ //$NON-NLS-2$
					{
						return 1;
					}
					return (name0.compareToIgnoreCase(name1));
				}
			});

			return list.toArray(new IMethodInfo[list.size()]);

		} else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElement#getProperty(java.lang.
	 * String)
	 */
	public final Object getProperty(String propName) {
		if (cm == null) {
			// Try to get host chart as model
			initHostChart();
			if (cm == null) {
				return null;
			}
		}

		if (propName.equals("title.value")) //$NON-NLS-1$
		{
			return cm.getTitle().getLabel().getCaption().getValue();
		} else if (propName.equals("title.font.rotation")) //$NON-NLS-1$
		{
			return new Double(cm.getTitle().getLabel().getCaption().getFont().getRotation());
		} else if (propName.equals("legend.position")) //$NON-NLS-1$
		{
			return cm.getLegend().getPosition().getName();
		} else if (propName.equals("legend.anchor")) //$NON-NLS-1$
		{
			return cm.getLegend().getAnchor().getName();
		} else if (propName.equals("chart.dimension")) //$NON-NLS-1$
		{
			return cm.getDimension().getName();
		} else if (propName.equals("plot.transposed")) //$NON-NLS-1$
		{
			return Boolean.valueOf((cm instanceof ChartWithAxes) ? ((ChartWithAxes) cm).isTransposed() : false);
		} else if (propName.equals(ChartReportItemUtil.PROPERTY_SCRIPT)
				|| propName.equals(ChartReportItemUtil.PROPERTY_ONRENDER)) {
			return cm.getScript();
		} else if (propName.equals(ChartReportItemUtil.PROPERTY_CHART)) {
			return cm;
		} else if (propName.equals(ChartReportItemUtil.PROPERTY_SCALE)) {
			return sharedScale;
		}
		return null;
	}

	private void initHostChart() {
		if (ChartCubeUtil.isAxisChart(handle)) {
			ExtendedItemHandle hostChartHandle = (ExtendedItemHandle) handle
					.getElementProperty(ChartReportItemUtil.PROPERTY_HOST_CHART);
			if (hostChartHandle == null || hostChartHandle == handle) {
				return;
			}

			// Use the reference if it references host chart
			cm = ChartReportItemUtil.getChartFromHandle(hostChartHandle);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#checkProperty(java.lang.
	 * String, java.lang.Object)
	 */
	public void checkProperty(String propName, Object value) throws ExtendedElementException {
		logger.log(ILogger.INFORMATION,
				Messages.getString("ChartReportItemImpl.log.checkProperty", new Object[] { propName, value })); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElement#setProperty(java.lang.
	 * String, java.lang.Object)
	 */
	public void setProperty(String propName, Object value) {
		logger.log(ILogger.INFORMATION,
				Messages.getString("ChartReportItemImpl.log.setProperty", new Object[] { propName, value })); //$NON-NLS-1$

		executeSetSimplePropertyCommand(handle, propName, getProperty(propName), value);
	}

	void basicSetProperty(String propName, Object value) {
		if (propName.equals("title.value")) //$NON-NLS-1$
		{
			cm.getTitle().getLabel().getCaption().setValue((String) value);
		} else if (propName.equals("title.font.rotation")) //$NON-NLS-1$
		{
			cm.getTitle().getLabel().getCaption().getFont().setRotation(((Double) value).doubleValue());
		} else if (propName.equals("legend.position")) //$NON-NLS-1$
		{
			cm.getLegend().setPosition(Position.get((String) value));
		} else if (propName.equals("legend.anchor")) //$NON-NLS-1$
		{
			cm.getLegend().setAnchor(Anchor.get((String) value));
		} else if (propName.equals("chart.dimension")) //$NON-NLS-1$
		{
			cm.setDimension(ChartDimension.get((String) value));
		} else if (propName.equals("plot.transposed")) //$NON-NLS-1$
		{
			if (cm instanceof ChartWithAxes) {
				((ChartWithAxes) cm).setTransposed(((Boolean) value).booleanValue());
			} else {
				logger.log(ILogger.ERROR, Messages.getString("ChartReportItemImpl.log.CannotSetState")); //$NON-NLS-1$
			}
		} else if (propName.equals(ChartReportItemUtil.PROPERTY_SCRIPT)
				|| propName.equals(ChartReportItemUtil.PROPERTY_ONRENDER)) {
			cm.setScript((String) value);
		} else if (propName.equals(ChartReportItemUtil.PROPERTY_CHART)) {
			this.cm = (Chart) value;
		}

	}

	protected void checkScriptSyntax(String string) throws RhinoException {
		if (string == null)
			return;

		if (!isJavaClassName(string)) {
			try {
				Context cx = Context.enter();
				cx.compileString(string, "chartScript", 1, null); //$NON-NLS-1$
			} finally {
				Context.exit();
			}
		}
	}

	protected boolean isJavaClassName(String string) {
		return (string.matches("\\w+(\\.\\w*)*")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElement#validate()
	 */
	public List<SemanticException> validate() {
		logger.log(ILogger.INFORMATION, Messages.getString("ChartReportItemImpl.log.validate")); //$NON-NLS-1$
		List<SemanticException> list = new ArrayList<SemanticException>();
		// Ignore the validation for templates
		DesignElementHandle container = handle.getContainer();
		do {
			if (container instanceof TemplateParameterDefinitionHandle) {
				return list;
			}
			container = container.getContainer();
		} while (container != null);

		if (cm != null) {
			ChartReportItemHelper itemHelper = ChartReportItemHelper.instance();
			if (itemHelper.getBindingDataSetHandle(handle) == null && itemHelper.getBindingCubeHandle(handle) == null) {
				list.add(new SemanticError(handle.getElement(), SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET));
			}

			List<SeriesDefinition> sdList = ChartUtil.getBaseSeriesDefinitions(cm);
			if (sdList == null || sdList.size() == 0) {
				ExtendedElementException exception = new ExtendedElementException(getHandle().getElement(),
						ChartReportItemPlugin.ID, "QueryHelper.dataDefnUndefined", //$NON-NLS-1$
						Messages.getResourceBundle());
				exception.setProperty(ExtendedElementException.LOCALIZED_MESSAGE,
						Messages.getString("QueryHelper.dataDefnUndefined", //$NON-NLS-1$
								Messages.getString(cm instanceof ChartWithAxes ? "QueryHelper.Text.XSeries"//$NON-NLS-1$
										: "QueryHelper.Text.CategroySeries")));//$NON-NLS-1$
				list.add(exception);
			} else {
				SeriesDefinition bsd = sdList.get(0);
				List<Query> bsQuery = bsd.getDesignTimeSeries().getDataDefinition();
				if (bsQuery.size() == 0 || bsQuery.get(0) == null || !bsQuery.get(0).isDefined()) {
					ExtendedElementException exception = new ExtendedElementException(getHandle().getElement(),
							ChartReportItemPlugin.ID, "QueryHelper.dataDefnUndefined", //$NON-NLS-1$
							Messages.getResourceBundle());
					exception.setProperty(ExtendedElementException.LOCALIZED_MESSAGE,
							Messages.getString("QueryHelper.dataDefnUndefined", //$NON-NLS-1$
									Messages.getString(cm instanceof ChartWithAxes ? "QueryHelper.Text.XSeries"//$NON-NLS-1$
											: "QueryHelper.Text.CategroySeries")));//$NON-NLS-1$
					list.add(exception);
				}
			}

			ExtendedElementException yException = new ExtendedElementException(getHandle().getElement(),
					ChartReportItemPlugin.ID, "QueryHelper.dataDefnUndefined", //$NON-NLS-1$
					Messages.getResourceBundle());
			yException.setProperty(ExtendedElementException.LOCALIZED_MESSAGE,
					Messages.getString("QueryHelper.dataDefnUndefined", //$NON-NLS-1$
							Messages.getString(cm instanceof ChartWithAxes ? "QueryHelper.Text.YSeries"//$NON-NLS-1$
									: "QueryHelper.Text.ValueSeries")));//$NON-NLS-1$
			List<SeriesDefinition> ysds = ChartUtil.getAllOrthogonalSeriesDefinitions(cm);
			if (ysds.size() == 0) {
				list.add(yException);
			} else {
				SD: for (SeriesDefinition vsd : ysds) {
					Series series = vsd.getDesignTimeSeries();
					if (series.getDataDefinition().size() == 0) {
						list.add(yException);
						break SD;
					}
					int[] validQueryIndex = series.getDefinedDataDefinitionIndex();
					List<Query> dataDefinitions = series.getDataDefinition();
					if (validQueryIndex[validQueryIndex.length - 1] >= dataDefinitions.size()) {
						// Avoid null data definition
						list.add(yException);
						break SD;
					}
					for (int i = 0; i < validQueryIndex.length; i++) {
						if (!dataDefinitions.get(validQueryIndex[i]).isDefined()) {
							// If must-defined query is undefined
							list.add(yException);
							break SD;
						}
					}
				}
			}

			try {
				checkScriptSyntax(cm.getScript());
			} catch (RhinoException e) {
				logger.log(e);
				ExtendedElementException extendedException = new ExtendedElementException(this.getHandle().getElement(),
						ChartReportItemPlugin.ID, "exception.script.syntaxError", //$NON-NLS-1$
						new Object[] { e.getLocalizedMessage() }, Messages.getResourceBundle());
				extendedException.setProperty(ExtendedElementException.LINE_NUMBER, String.valueOf(e.lineNumber()));
				extendedException.setProperty(ExtendedElementException.SUB_EDITOR, "script");//$NON-NLS-1$
				list.add(extendedException);
			}
		} else if (!ChartCubeUtil.isAxisChart(handle)) {
			// If chart is axis chart type and model is null, the validation
			// here should pass, since the plot chart model may be not loaded in
			// some cases such as in multi-view #27455
			list.add(new SemanticError(handle.getElement(), SemanticError.DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT));
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IElement#copy()
	 */
	public final IReportItem copy() {
		final ChartReportItemImpl crii = (ChartReportItemImpl) ChartReportItemUtil.instanceChartReportItem(handle);
		crii.bCopied = true;

		// Do not copy model for axis chart since it uses reference
		if (!ChartCubeUtil.isAxisChart(handle)) {
			if (cm == null) {
				crii.cm = null;
			} else {
				// Add lock to avoid concurrent exception from EMF. IReportItem
				// has one
				// design time chart model that could be shared by multiple
				// presentation instance, but only allows one copy per item
				// concurrently.
				synchronized (this) {
					// Must copy model here to generate runtime data later
					try {
						crii.cm = cm.copyInstance();
					} catch (ConcurrentModificationException e) {
						// Once concurrent exception is thrown, try again.
						crii.cm = cm.copyInstance();
					} catch (NullPointerException e) {
						// Once NPE is thrown in concurrent case, try again.
						crii.cm = cm.copyInstance();
					}
				}
			}
		}
		return crii;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#refreshPropertyDefinition()
	 */
	public boolean refreshPropertyDefinition() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.ICompatibleReportItem#
	 * getRowExpressions()
	 */
	public List<String> getRowExpressions() {
		try {
			// Bugzilla#283253 Do not replace raw expressions with evaluator
			// binding name during serializing chart model into report
			return Generator.instance().getRowExpressions(cm, new BIRTActionEvaluator(), false);
		} catch (ChartException e) {
			logger.log(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.ICompatibleReportItem#
	 * updateRowExpressions(java.util.Map)
	 */
	@SuppressWarnings("rawtypes")
	public void updateRowExpressions(Map newExpressions) {
		CompatibleExpressionUpdater.update(cm, newExpressions);
	}

	public org.eclipse.birt.report.model.api.simpleapi.IReportItem getSimpleElement() {
		try {
			if (cm instanceof ChartWithAxes) {
				return new ChartWithAxesImpl(handle, (ChartWithAxes) cm);
			}
			if (cm instanceof ChartWithoutAxes) {
				return new ChartWithoutAxesImpl(handle, (ChartWithoutAxes) cm);
			}
			return null;
		} catch (Exception e) {
			logger.log(e);
			return null;
		}
	}

	/**
	 * Returns if current report item is just copied
	 * 
	 * @since 2.3
	 */
	public boolean isCopied() {
		return this.bCopied;
	}

	public URL findResource(String fileName) {
		if (handle != null) {
			return handle.getModule().findResource(fileName, 0);
		}
		return null;
	}

	public String externalizedMessage(String sKey, String sDefaultValue, ULocale locale) {
		return ChartItemUtil.externalizedMessage(handle, sKey, sDefaultValue, locale);
	}

	@Override
	public boolean canExport() {
		// If chart is from multi-view or xtab part, do not allow to
		// export to library.
		if (handle.getContainer() instanceof MultiViewsHandle || ChartCubeUtil.isPlotChart(handle)
				|| ChartCubeUtil.isAxisChart(handle)) {
			return false;
		}
		return true;
	}

	/**
	 * Since model does not differentiate Layout-RTL and Text-RTL, but chart does.
	 * Currently we always retrieve the Layout-RTL from container.
	 * 
	 * @return RTL or not
	 */
	public boolean isLayoutDirectionRTL() {
		if (handle.getContainer() == null) {
			return false;
		}
		return handle.getContainer().isDirectionRTL();
	}

	/**
	 * Returns the current serializer.
	 * 
	 * @return serializer
	 */
	public Serializer getSerializer() {
		return serializer;
	}

	@Override
	public Iterator<ComputedColumnHandle> availableBindings() {
		// Get all bindings
		Iterator<ComputedColumnHandle> allBindings = ChartItemUtil.getAllColumnBindingsIterator(handle);
		ReportItemHandle bindingHolder = ChartItemUtil.getBindingHolder(handle);
		if (bindingHolder == null || bindingHolder.getCube() == null) {
			return allBindings;
		}

		// Get all query definitions from chart model
		List<Query> queries = new ArrayList<Query>();
		queries.addAll(ChartUtil.getBaseSeriesDefinitions(cm).get(0).getDesignTimeSeries().getDataDefinition());
		for (SeriesDefinition vsd : ChartUtil.getAllOrthogonalSeriesDefinitions(cm)) {
			queries.addAll(vsd.getDesignTimeSeries().getDataDefinition());
			queries.add(vsd.getQuery());
		}

		// Get all binding names from query definition
		ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();
		Set<String> bindingNames = new HashSet<String>();
		for (Query query : queries) {
			if (query.isDefined()) {
				bindingNames.addAll(exprCodec.getBindingNames(query.getDefinition()));
			}
		}

		// Compare the binding names
		List<ComputedColumnHandle> availableBindings = new ArrayList<ComputedColumnHandle>();
		while (allBindings.hasNext()) {
			ComputedColumnHandle binding = allBindings.next();
			if (bindingNames.contains(binding.getName())) {
				availableBindings.add(binding);
			}
		}

		return availableBindings.iterator();
	}

	@Override
	public boolean hasFixedSize() {
		if (ChartCubeUtil.isInXTabMeasureCell(getHandle())) {
			// If chart is in cross tab cell, plot chart and axis chart are
			// using dynamic size
			return !(ChartCubeUtil.isPlotChart(getHandle()) || ChartCubeUtil.isAxisChart(getHandle()));

		}
		return true;
	}
}
