/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.script.internal;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.script.api.IChart;
import org.eclipse.birt.chart.script.api.IComponentFactory;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.attribute.IText;
import org.eclipse.birt.chart.script.api.component.ILegend;
import org.eclipse.birt.chart.script.internal.component.LegendImpl;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.MultiRowItem;
import org.eclipse.birt.report.model.api.util.DimensionUtil;

/**
 *
 */

public abstract class ChartImpl extends MultiRowItem implements IChart {

	protected static final ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$
	protected Chart cm;
	protected ExtendedItemHandle eih;

	protected ChartImpl(ExtendedItemHandle eih, Chart cm) {
		super(eih);
		this.eih = eih;
		this.cm = cm;
	}

	@Override
	public IText getDescription() {
		Text desc = cm.getDescription();
		if (desc == null) {
			desc = ChartComponentUtil.createEMFText();
			cm.setDescription(desc);
		}
		return ChartComponentUtil.convertText(desc);
	}

	@Override
	public ILegend getLegend() {
		return new LegendImpl(cm.getLegend());
	}

	@Override
	public String getOutputType() {
		return (String) eih.getProperty("outputFormat"); //$NON-NLS-1$
	}

	@Override
	public ILabel getTitle() {
		Label title = cm.getTitle().getLabel();
		if (title == null) {
			title = ChartComponentUtil.createEMFLabel();
			cm.getTitle().setLabel(title);
		}
		return ChartComponentUtil.convertLabel(title);
	}

	@Override
	public boolean isColorByCategory() {
		return cm.getLegend().getItemType() == LegendItemType.CATEGORIES_LITERAL;
	}

	@Override
	public void setColorByCategory(boolean byCategory) {
		cm.getLegend().setItemType(byCategory ? LegendItemType.CATEGORIES_LITERAL : LegendItemType.SERIES_LITERAL);
	}

	public void setDescription(IText label) {
		cm.setDescription(ChartComponentUtil.convertIText(label));
	}

	@Override
	public void setOutputType(String type) {
		try {
			if (!ChartUtil.isOutputFormatSupport(type)) {
				type = "SVG"; //$NON-NLS-1$
			}
			eih.setProperty("outputFormat", type);//$NON-NLS-1$
		} catch (SemanticException | ChartException e) {
			logger.log(e);
		}
	}

	public void setTitle(ILabel title) {
		cm.getTitle().setLabel(ChartComponentUtil.convertILabel(title));
	}

	@Override
	public String getDimension() {
		return cm.getDimension().getName();
	}

	@Override
	public void setDimension(String dimensionName) {
		cm.setDimension(ChartDimension.getByName(dimensionName));
	}

	@Override
	public void setWidth(double dimension) throws SemanticException {
		super.setWidth(dimension);

		// Update size in chart model
		double dWidth = convertDimensionToPoints(eih.getWidth());
		if (dWidth > 0) {
			cm.getBlock().getBounds().setWidth(dWidth);
		}
	}

	@Override
	public void setWidth(String dimension) throws SemanticException {
		super.setWidth(dimension);

		// Update size in chart model
		double dWidth = convertDimensionToPoints(eih.getWidth());
		if (dWidth > 0) {
			cm.getBlock().getBounds().setWidth(dWidth);
		}
	}

	@Override
	public void setHeight(double dimension) throws SemanticException {
		super.setHeight(dimension);

		// Update size in chart model
		double dHeight = convertDimensionToPoints(eih.getHeight());
		if (dHeight > 0) {
			cm.getBlock().getBounds().setHeight(dHeight);
		}
	}

	@Override
	public void setHeight(String dimension) throws SemanticException {
		super.setHeight(dimension);

		// Update size in chart model
		double dHeight = convertDimensionToPoints(eih.getHeight());
		if (dHeight > 0) {
			cm.getBlock().getBounds().setHeight(dHeight);
		}
	}

	protected final double convertDimensionToPoints(DimensionHandle dh) throws SemanticException {
		double dOriginalMeasure = dh.getMeasure();
		String sUnits = dh.getUnits();

		if (sUnits != null) {
			// Pixel unit is not supported in Simple API, because can't get DPI
			// from engine
			// Percentage unit is not supported in Simple API, because can't get
			// Figure's size to calculate relative value
			if (sUnits == DesignChoiceConstants.UNITS_PX || sUnits == DesignChoiceConstants.UNITS_PERCENTAGE) {
				throw new SemanticException(ChartReportItemPlugin.ID, "ChartImpl.error.DimensionUnitNotSupported", //$NON-NLS-1$
						new Object[] { sUnits }, Messages.getResourceBundle());
			}

			return DimensionUtil.convertTo(dOriginalMeasure, sUnits, DesignChoiceConstants.UNITS_PT).getMeasure();
		}
		return 0;
	}

	@Override
	public IComponentFactory getFactory() {
		return new ChartComponentFactory();
	}

}
