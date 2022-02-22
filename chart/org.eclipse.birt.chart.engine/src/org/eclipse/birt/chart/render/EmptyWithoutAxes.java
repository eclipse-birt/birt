/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.computation.withoutaxes.PlotWithoutAxes;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;

/**
 * This class implements an empty renderer for ChartWithoutAxes type.
 */
public final class EmptyWithoutAxes extends BaseRenderer {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/render"); //$NON-NLS-1$

	@Override
	public void renderSeries(IPrimitiveRenderer ipr, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		Boolean bDataEmpty = rtc.getState(RunTimeContext.StateKey.DATA_EMPTY_KEY);
		if (bDataEmpty == null) {
			bDataEmpty = false;
		}

		Label laAltText = getModel().getEmptyMessage();

		if (bDataEmpty && laAltText.isVisible()) {
			final PlotWithoutAxes pwoa = (PlotWithoutAxes) getComputations();
			renderEmptyPlot(ipr, p, pwoa.getPlotBounds());
		} else {
			// NOTE: NO-OP IMPL
			logger.log(ILogger.INFORMATION, Messages.getString("info.render.series", //$NON-NLS-1$
					new Object[] { getClass().getName(), Integer.valueOf(iSeriesIndex + 1),
							Integer.valueOf(iSeriesCount) },
					getRunTimeContext().getULocale()));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.render.BaseRenderer#compute(org.eclipse.birt.chart.
	 * model.attribute.Bounds, org.eclipse.birt.chart.model.layout.Plot,
	 * org.eclipse.birt.chart.render.ISeriesRenderingHints)
	 */
	@Override
	public void compute(Bounds bo, Plot p, ISeriesRenderingHints isrh) throws ChartException {
		// NOTE: This method is not used by the Empty renderer
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.render.BaseRenderer#renderLegendGraphic(org.eclipse.
	 * birt.chart.device.IPrimitiveRenderer,
	 * org.eclipse.birt.chart.model.layout.Legend,
	 * org.eclipse.birt.chart.model.attribute.Fill,
	 * org.eclipse.birt.chart.model.attribute.Bounds)
	 */
	@Override
	public void renderLegendGraphic(IPrimitiveRenderer ipr, Legend lg, Fill fPaletteEntry, Bounds bo)
			throws ChartException {
		if ((bo.getWidth() == 0) && (bo.getHeight() == 0)) {
			return;
		}
		final LineAttributes lia = goFactory.createLineAttributes(goFactory.GREY(), LineStyle.SOLID_LITERAL, 1);

		// COMPUTE THE FRONT FACE ONLY
		Location[] loaFrontFace;
		loaFrontFace = new Location[4];
		final double dOffset = bo.getWidth() > bo.getHeight() ? bo.getHeight() : bo.getWidth();
		loaFrontFace[0] = goFactory.createLocation(bo.getLeft(), bo.getTop());
		loaFrontFace[1] = goFactory.createLocation(bo.getLeft(), bo.getTop() + dOffset);
		loaFrontFace[2] = goFactory.createLocation(bo.getLeft() + dOffset, bo.getTop() + dOffset);
		loaFrontFace[3] = goFactory.createLocation(bo.getLeft() + dOffset, bo.getTop());

		// RENDER THE PLANE (INTERNALLY EXTRUDED IF NECESSARY)
		renderPlane(ipr, StructureSource.createLegend(lg), loaFrontFace, fPaletteEntry, lia, getModel().getDimension(),
				3 * getDeviceScale(), false);
	}

}
