/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.extension.render;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.computation.Rectangle;
import org.eclipse.birt.chart.computation.RotatedRectangle;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.AutoScale.ScaleInfo;
import org.eclipse.birt.chart.computation.withaxes.AxisTickCoordinates;
import org.eclipse.birt.chart.computation.withoutaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.extension.i18n.Messages;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Needle;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.plugin.ChartEngineExtensionPlugin;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.DeferredCache;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.birt.chart.util.NumberUtil;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;

/**
 * DialRenderer
 */
public final class DialRenderer {

	private transient Insets insCA = null;

	private transient IDisplayServer xs = null;

	private transient Dial dial = null;

	private transient DialSeries ds = null;

	private transient DeferredCache dc = null;

	private DataPointHints dphValue;

	private SeriesRenderingHints srh;

	private double dRadius;

	private double dStartAngle;

	private double dStopAngle;

	private Scale sc;

	private double dValue;

	private AutoScale asc;

	private double dSafeSpacing = 10;

	private double dExtraSpacing = 0;

	private String[] sla;

	private Palette pa;

	private final double dScale;

	private boolean inverseScale = false;

	/** The first valid index of non-null value in series data sets. */
	private int iValueValidIndex = 0;

	private ChartUtil.CacheDecimalFormat cacheNumFormat;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine.extension/render"); //$NON-NLS-1$

	protected static final IGObjectFactory goFactory = GObjectFactory.instance();

	private final IChartComputation cComp;

	/**
	 * @param dct
	 * @param dial
	 * @param dpha
	 * @param da
	 * @param pa
	 * @throws ChartException
	 */
	DialRenderer(ChartWithoutAxes dct, Dial dial, SeriesRenderingHints srh, Palette pa) throws ChartException {
		xs = dial.getXServer();
		dScale = dial.getDeviceScale();

		this.dial = dial;
		this.pa = pa;
		this.srh = srh;
		this.cComp = dial.getComputations().getChartComputation();
		DataPointHints[] dpha = srh.getDataPoints();
		double[] da = srh.asPrimitiveDoubleValues();

		if (dpha == null || da == null || dpha.length < 1 || da.length < 1) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.invalid.datapoint.dial", //$NON-NLS-1$
					Messages.getResourceBundle(dial.getRunTimeContext().getULocale()));
		}

		int validIndex = -1;
		for (int i = 0; i < dpha.length; i++) {
			if (dpha[i].getOrthogonalValue() instanceof Number
					&& !Double.isNaN(((Number) dpha[i].getOrthogonalValue()).doubleValue())) {
				validIndex = i;
				break;
			}
		}

		if (validIndex == -1) {
			// If no data, do not throw exception but still display dummy dial
			logger.log(new ChartException(ChartEngineExtensionPlugin.ID, ChartException.RENDERING,
					"exception.invalid.datapoint.dial", //$NON-NLS-1$
					Messages.getResourceBundle(dial.getRunTimeContext().getULocale())));

			validIndex = 0;
		}

		// Save first valid index of non-value in series data set, it is used to set
		// needle color. - Henry
		iValueValidIndex = validIndex;

		dValue = da[validIndex];
		dphValue = dpha[validIndex];

		ds = (DialSeries) dial.getSeries();
		insCA = goFactory.scaleInsets(goFactory.createInsets(2, 2, 2, 2), dScale);
		dSafeSpacing *= dScale;
	}

	double getValue() {
		return dValue;
	}

	boolean isInverseScale() {
		return ds.getDial().isInverseScale();
	}

	double getDialRadius() {
		if (ds.getDial().isSetRadius()) {
			return Math.max(ds.getDial().getRadius() * dScale, 0);
		}

		return 0;
	}

	Scale getDialScale() {
		return ds.getDial().getScale();
	}

	double getDialStartAngle() {
		return ds.getDial().isSetStartAngle() ? ds.getDial().getStartAngle() : 0;
	}

	double getDialStopAngle() {
		return ds.getDial().isSetStopAngle() ? ds.getDial().getStopAngle() : 180;
	}

	AutoScale getAutoScale(double startAngle, double stopAngle, Scale sc, Bounds bo, BigDecimal divisor)
			throws ChartException {
		Bounds boCA = goFactory.copyOf(bo);
		boCA.adjust(insCA);

		double dFullRadius = Math.min(boCA.getWidth() / 2d, boCA.getHeight() / 2d);

		// pre-compute an intial scale.
		AutoScale isc = computeScale(ds.getDial().getLabel(),
				new DataSetIterator(new double[] { startAngle, stopAngle }, IConstants.NUMERICAL | IConstants.LINEAR),
				getValue(), startAngle, stopAngle, sc, ds.getDial().getFormatSpecifier(), dial.getRunTimeContext(),
				dFullRadius, divisor);
		// get initial extraSpacing
		double extraSpacing = getDialExtraSpacing(isc);

		// use the same logic as render() to get the dial bounds.

		// TODO refactor to use same code block both in render() and here.

		final boolean bFullDisc = dStopAngle < 0 || dStopAngle > 180 || dStartAngle < 0 || dStartAngle > 180;

		// make the extra spacing at most 1/3 of the full radius.
		if (extraSpacing > dFullRadius / 3) {
			extraSpacing = dFullRadius / 3;

			// shrink safe spacing to save space.
			if (dSafeSpacing > dFullRadius / 10d) {
				dSafeSpacing = dFullRadius / 10d;
			}
		}

		if (dRadius > 0 && (dRadius + extraSpacing + dSafeSpacing * 2) < dFullRadius) {
			dFullRadius = dRadius + extraSpacing + dSafeSpacing * 2;
		}

		// Dial full outline
		Bounds fullBounds = goFactory.createBounds(boCA.getLeft() + (boCA.getWidth() - 2 * dFullRadius) / 2,
				(boCA.getTop() + (boCA.getHeight() - (bFullDisc ? 2 : 1) * dFullRadius) / 2), 2 * dFullRadius,
				2 * dFullRadius);

		// Dial actual bounds
		Bounds dialBounds = goFactory.createBounds(fullBounds.getLeft() + extraSpacing + dSafeSpacing,
				fullBounds.getTop() + extraSpacing + dSafeSpacing,
				fullBounds.getWidth() - 2 * (extraSpacing + dSafeSpacing),
				fullBounds.getHeight() - 2 * (extraSpacing + dSafeSpacing));

		// re-compute the autoScale and scale labels as per real dial
		// bounds.
		return computeScale(ds.getDial().getLabel(),
				new DataSetIterator(new double[] { startAngle, stopAngle }, IConstants.NUMERICAL | IConstants.LINEAR),
				getValue(), startAngle, stopAngle, sc, ds.getDial().getFormatSpecifier(), dial.getRunTimeContext(),
				Math.min(dialBounds.getWidth() / 2d, dialBounds.getHeight() / 2d), divisor);
	}

	AutoScale getAutoScale(double startAngle, double stopAngle, Scale sc, Bounds bo) throws ChartException {
		return getAutoScale(startAngle, stopAngle, sc, bo, null);
	}

	double getDialExtraSpacing(AutoScale sc) throws ChartException {
		double ex = 0;

		if (ds.getDial().getMajorGrid().getTickAttributes().isVisible()) {
			TickStyle ts = ds.getDial().getMajorGrid().getTickStyle();
			if (ts == TickStyle.ABOVE_LITERAL) {
				ex = Math.max(ex, getTickSize());
			} else if (ts == TickStyle.ACROSS_LITERAL) {
				ex = Math.max(ex, getTickSize() / 2d);
			}
		}

		if (ds.getDial().getMinorGrid().getTickAttributes().isVisible()) {
			TickStyle ts = ds.getDial().getMinorGrid().getTickStyle();
			if (ts == TickStyle.ABOVE_LITERAL) {
				ex = Math.max(ex, getTickSize());
			} else if (ts == TickStyle.ACROSS_LITERAL) {
				ex = Math.max(ex, getTickSize() / 2d);
			}
		}

		if (ds.getDial().getLabel().isVisible()) {
			Size msz = computeLabelSize(sc, ds.getDial().getLabel());

			ex += Math.max(msz.getWidth(), msz.getHeight());
		}

		return ex;
	}

	void updateRadius(double radius) {
		this.dRadius = radius;
	}

	void updateStartAngle(double angle) {
		this.dStartAngle = angle;
	}

	void updateStopAngle(double angle) {
		this.dStopAngle = angle;
	}

	void updateInverseScale(boolean inverseScale) {
		this.inverseScale = inverseScale;
	}

	void updateScale(Scale sc) {
		this.sc = sc;
	}

	void updateAutoScale(AutoScale asc) {
		this.asc = asc;
	}

	void updateExtraSpacing(double spacing) {
		this.dExtraSpacing = spacing;
	}

	/**
	 * @param idr
	 * @param bo
	 * @throws ChartException
	 */
	public void render(IDeviceRenderer idr, Bounds bo) throws ChartException {
		dc = dial.getDeferredCache();

		Bounds boCA = goFactory.copyOf(bo);

		ChartWithoutAxes cwa = (ChartWithoutAxes) dial.getModel();
		if (cwa.isSetCoverage()) {
			double rate = cwa.getCoverage();
			double ww = 0.5 * (1d - rate) * bo.getWidth();
			double hh = 0.5 * (1d - rate) * bo.getHeight();
			insCA = goFactory.createInsets(hh, ww, hh, ww);
		}

		boCA.adjust(insCA);

		final boolean bDialSuperimposed = dial.isDialSuperimposed();
		final boolean bFirstSeries = dial.isFirstDial();

		final org.eclipse.birt.chart.model.component.Dial dialComponent = ds.getDial();
		final Needle needleComponent = ds.getNeedle();

		if (!bDialSuperimposed) {

			DataSetIterator dsi = new DataSetIterator(dial.getSeries().getDataSet());
			BigDecimal divisor = null;
			dsi.reset();
			while (dsi.hasNext()) {
				Object o = dsi.next();
				if (NumberUtil.isBigNumber(o)) {
					divisor = ((BigNumber) o).getDivisor();
					break;
				}
			}

			// initialize per dial series if not superimposed.
			dRadius = getDialRadius();
			dStartAngle = getDialStartAngle();
			dStopAngle = getDialStopAngle();
			inverseScale = isInverseScale();
			sc = getDialScale();
			asc = getAutoScale(dStartAngle, dStopAngle, sc, bo, divisor);
			dExtraSpacing = getDialExtraSpacing(asc);
		}

		if (dial.isRightToLeft()) {
			inverseScale = !inverseScale;
		}

		// The logic here to get the real dial bounds is the same as in
		// getAutoScale()

		final boolean bFullDisc = dStopAngle < 0 || dStopAngle > 180 || dStartAngle < 0 || dStartAngle > 180;

		double dFullRadius = Math.min(boCA.getWidth() / 2d, boCA.getHeight() / 2d);

		// make the extra spacing at most 1/3 of the full radius.
		if (dExtraSpacing > dFullRadius / 3) {
			dExtraSpacing = dFullRadius / 3;

			// shrink safe spacing to save space.
			if (dSafeSpacing > dFullRadius / 10d) {
				dSafeSpacing = dFullRadius / 10d;
			}
		}

		if (dRadius > 0 && (dRadius + dExtraSpacing + dSafeSpacing * 2) < dFullRadius) {
			dFullRadius = dRadius + dExtraSpacing + dSafeSpacing * 2;
		}

		final ArcRenderEvent are = ((EventObjectCache) idr).getEventObject(StructureSource.createUnknown(dialComponent),
				ArcRenderEvent.class);

		// Dial full outline
		Bounds fullBounds = goFactory.createBounds(boCA.getLeft() + (boCA.getWidth() - 2 * dFullRadius) / 2,
				(boCA.getTop() + (boCA.getHeight() - (bFullDisc ? 2 : 1) * dFullRadius) / 2), 2 * dFullRadius,
				2 * dFullRadius);

		if (!bDialSuperimposed || bFirstSeries) {
			final Fill dialFill = dialComponent.getFill();
			double safeAngle = Math.toDegrees(Math.abs(Math.asin(dSafeSpacing / dFullRadius)));
			are.setBounds(fullBounds);
			are.setStartAngle(bFullDisc ? 0 : -safeAngle);
			are.setAngleExtent(bFullDisc ? 360 : (180 + 2 * safeAngle));
			are.setOutline(dialComponent.getLineAttributes());
			are.setBackground(dialFill);
			are.setStyle(ArcRenderEvent.CLOSED);
			idr.fillArc(are);
			idr.drawArc(are);
		}

		// Dial background
		Bounds dialBounds = goFactory.createBounds(fullBounds.getLeft() + dExtraSpacing + dSafeSpacing,
				fullBounds.getTop() + dExtraSpacing + dSafeSpacing,
				fullBounds.getWidth() - 2 * (dExtraSpacing + dSafeSpacing),
				fullBounds.getHeight() - 2 * (dExtraSpacing + dSafeSpacing));

		if (!bDialSuperimposed || bFirstSeries) {
			double tsa = dStartAngle;
			double tpa = dStopAngle;

			if (tsa > tpa) {
				double temp = tsa;
				tsa = tpa;
				tpa = temp;
			}

			are.setBounds(dialBounds);
			are.setStartAngle(tsa);
			are.setAngleExtent(tpa - tsa);
			are.setOutline(dialComponent.getMajorGrid().getLineAttributes());
			are.setStyle(Math.abs(dStopAngle - dStartAngle) < 360 ? ArcRenderEvent.SECTOR : ArcRenderEvent.OPEN);
			idr.drawArc(are);
		}

		double xc = dialBounds.getLeft() + dialBounds.getWidth() / 2d;
		double yc = dialBounds.getTop() + dialBounds.getHeight() / 2d;

		double dDialRadius = dialBounds.getHeight() / 2d;

		// Regions
		for (int i = 0; i < dialComponent.getDialRegions().size(); i++) {
			DialRegion dregion = dialComponent.getDialRegions().get(i);

			double ascMinValue = Double.parseDouble(asc.getMinimum().toString());
			double ascMaxValue = Double.parseDouble(asc.getMaximum().toString());

			// If start/end isn't set, it just uses min/max as start/end.
			double drStartValue = dregion.getStartValue() == null ? ascMinValue
					: ((NumberDataElement) dregion.getStartValue()).getValue();
			double drEndValue = dregion.getEndValue() == null ? ascMaxValue
					: ((NumberDataElement) dregion.getEndValue()).getValue();

			if (!((Math.max(drStartValue, drEndValue) <= ascMinValue)
					|| (Math.min(drStartValue, drEndValue) >= (ascMaxValue)) || (drStartValue == drEndValue))) {

				double sAngle = Methods.getLocation(asc, drStartValue);
				double eAngle = Methods.getLocation(asc, drEndValue);

				if (sAngle < dStartAngle) {
					sAngle = dStartAngle;
				}
				if (sAngle > dStopAngle) {
					sAngle = dStopAngle;
				}
				if (eAngle < dStartAngle) {
					eAngle = dStartAngle;
				}
				if (eAngle > dStopAngle) {
					eAngle = dStopAngle;
				}

				double tsa = transformAngle(sAngle);
				double tpa = transformAngle(eAngle);

				if (tsa > tpa) {
					double temp = tsa;
					tsa = tpa;
					tpa = temp;
				}

				are.setBounds(dialBounds);
				are.setStartAngle(tsa);
				are.setAngleExtent(tpa - tsa);
				are.setBackground(dregion.getFill());
				are.setOutline(dregion.getOutline());
				are.setStyle(ArcRenderEvent.SECTOR);

				are.setInnerRadius(
						dregion.isSetInnerRadius() ? Math.min(dregion.getInnerRadius() * dScale, dDialRadius) : 0);
				if (dregion.isSetOuterRadius()) {
					if (dregion.getOuterRadius() == -1) {
						are.setOuterRadius(dDialRadius);
					} else {
						are.setOuterRadius(Math.min(dregion.getOuterRadius() * dScale, dDialRadius));
					}
				} else {
					are.setOuterRadius(0);
				}

				idr.fillArc(are);
				idr.drawArc(are);
			}
		}

		final LineRenderEvent lre = ((EventObjectCache) idr)
				.getEventObject(StructureSource.createUnknown(dialComponent), LineRenderEvent.class);

		AxisTickCoordinates tickCoord = asc.getTickCordinates();

		// Grid
		if (!bDialSuperimposed || bFirstSeries) {
			if (dialComponent.getMajorGrid().getTickAttributes().isVisible()
					|| dialComponent.getMinorGrid().getTickAttributes().isVisible()) {
				double[] minorCoord = asc.getMinorCoordinates(sc.getMinorGridsPerUnit());

				Location start = goFactory.createLocation(0, 0);
				Location end = goFactory.createLocation(0, 0);

				for (int i = 0; i < asc.getTickCount(); i++) {
					double mtd = tickCoord.getCoordinate(i);

					mtd = transformAngle(mtd);

					double mtr = Math.toRadians(mtd);

					if (dialComponent.getMajorGrid().getTickAttributes().isVisible()) {
						lre.setLineAttributes(dialComponent.getMajorGrid().getTickAttributes());

						switch (dialComponent.getMajorGrid().getTickStyle().getValue()) {
						case TickStyle.ABOVE:
							start.set(xc + dDialRadius * Math.cos(mtr), yc - dDialRadius * Math.sin(mtr));
							end.set(xc + (dDialRadius + getTickSize()) * Math.cos(mtr),
									yc - (dDialRadius + getTickSize()) * Math.sin(mtr));
							break;
						case TickStyle.ACROSS:
							start.set(xc + (dDialRadius + getTickSize() / 2d) * Math.cos(mtr),
									yc - (dDialRadius + getTickSize() / 2d) * Math.sin(mtr));
							end.set(xc + (dDialRadius - getTickSize() / 2d) * Math.cos(mtr),
									yc - (dDialRadius - getTickSize() / 2d) * Math.sin(mtr));
							break;
						case TickStyle.BELOW:
							start.set(xc + dDialRadius * Math.cos(mtr), yc - dDialRadius * Math.sin(mtr));
							end.set(xc + (dDialRadius - getTickSize()) * Math.cos(mtr),
									yc - (dDialRadius - getTickSize()) * Math.sin(mtr));
							break;
						default:
							throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING,
									"exception.invalid.tick.style.dial", //$NON-NLS-1$
									Messages.getResourceBundle(dial.getRunTimeContext().getULocale()));
						}

						lre.setStart(start);
						lre.setEnd(end);
						dc.addLine(lre);
					}

					if (dialComponent.getMinorGrid().getTickAttributes().isVisible() && i != asc.getTickCount() - 1) {
						lre.setLineAttributes(dialComponent.getMinorGrid().getTickAttributes());

						for (int j = 0; j < sc.getMinorGridsPerUnit() - 1; j++) {
							double minortd = minorCoord[j];

							if ((inverseScale && mtd + minortd >= transformAngle(tickCoord.getCoordinate(i + 1)))
									|| (!inverseScale
											&& mtd - minortd <= transformAngle(tickCoord.getCoordinate(i + 1)))) {
								// if current minor tick exceed the
								// range of current unit, skip
								continue;
							}

							double minortr = Math.toRadians(inverseScale ? (mtd + minortd) : (mtd - minortd));

							switch (dialComponent.getMinorGrid().getTickStyle().getValue()) {
							case TickStyle.ABOVE:
								start.set(xc + dDialRadius * Math.cos(minortr), yc - dDialRadius * Math.sin(minortr));
								end.set(xc + (dDialRadius + getTickSize()) * Math.cos(minortr),
										yc - (dDialRadius + getTickSize()) * Math.sin(minortr));
								break;
							case TickStyle.ACROSS:
								start.set(xc + (dDialRadius + getTickSize() / 2d) * Math.cos(minortr),
										yc - (dDialRadius + getTickSize() / 2d) * Math.sin(minortr));
								end.set(xc + (dDialRadius - getTickSize() / 2d) * Math.cos(minortr),
										yc - (dDialRadius - getTickSize() / 2d) * Math.sin(minortr));
								break;
							case TickStyle.BELOW:
								start.set(xc + dDialRadius * Math.cos(minortr), yc - dDialRadius * Math.sin(minortr));
								end.set(xc + (dDialRadius - getTickSize()) * Math.cos(minortr),
										yc - (dDialRadius - getTickSize()) * Math.sin(minortr));
								break;
							default:
								throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING,
										"exception.invalid.tick.style.dial", //$NON-NLS-1$
										Messages.getResourceBundle(dial.getRunTimeContext().getULocale()));
							}

							lre.setStart(start);
							lre.setEnd(end);
							dc.addLine(lre);
						}
					}
				}
			}
		}

		// Label
		if (!bDialSuperimposed || (bDialSuperimposed && bFirstSeries)) {
			if (dialComponent.getLabel().isVisible()) {
				final TextRenderEvent tre = ((EventObjectCache) idr)
						.getEventObject(StructureSource.createUnknown(dialComponent.getLabel()), TextRenderEvent.class);

				Label lbScale = goFactory.copyOf(dialComponent.getLabel());
				Location loc = goFactory.createLocation(0, 0);
				tre.setLabel(lbScale);
				tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
				tre.setLocation(loc);

				for (int i = 0; i < tickCoord.size(); i++) {
					if (dStopAngle - dStartAngle == 360 && i == tickCoord.size() - 1) {
						continue;
					}

					lbScale.getCaption().setValue(sla[i]);

					double tickAngle = dStartAngle + (tickCoord.getNormalizedCoordinate(i))
							/ (tickCoord.getEnd() - tickCoord.getStart()) * (dStopAngle - dStartAngle);

					tickAngle = transformAngle(tickAngle);

					int quad = ChartUtil.getQuadrant(tickAngle);

					double tickRadians = Math.toRadians(tickAngle);
					double tx = xc + dDialRadius * Math.cos(tickRadians);
					double ty = yc - dDialRadius * Math.sin(tickRadians);

					switch (quad) {
					case -1:
						loc.set(tx + dSafeSpacing / 2, ty);
						tre.setTextPosition(TextRenderEvent.RIGHT);
						break;
					case -2:
						loc.set(tx, ty - dSafeSpacing / 2);
						tre.setTextPosition(TextRenderEvent.ABOVE);
						break;
					case -3:
						loc.set(tx - dSafeSpacing / 2, ty);
						tre.setTextPosition(TextRenderEvent.LEFT);
						break;
					case -4:
						loc.set(tx, ty + dSafeSpacing / 2);
						tre.setTextPosition(TextRenderEvent.BELOW);
						break;
					case 1:
						loc.set(tx + dSafeSpacing / 2, ty - dSafeSpacing / 2);
						tre.setTextPosition(TextRenderEvent.RIGHT);
						break;
					case 2:
						loc.set(tx - dSafeSpacing / 2, ty - dSafeSpacing / 2);
						tre.setTextPosition(TextRenderEvent.LEFT);
						break;
					case 3:
						loc.set(tx - dSafeSpacing / 2, ty + dSafeSpacing / 2);
						tre.setTextPosition(TextRenderEvent.LEFT);
						break;
					case 4:
						loc.set(tx + dSafeSpacing / 2, ty + dSafeSpacing / 2);
						tre.setTextPosition(TextRenderEvent.RIGHT);
						break;
					}

					dc.addLabel(tre);
				}
			}
		}

		// Needle
		double needleAngle = Methods.getLocation(asc, dValue);

		if (needleAngle < dStartAngle || needleAngle > dStopAngle) {
			// clip the needle if value overflowed.
			return;
		}

		needleAngle = transformAngle(needleAngle);

		boolean bCategoryStyle = (((ChartWithoutAxes) dial.getModel()).getLegend()
				.getItemType() == LegendItemType.CATEGORIES_LITERAL);

		LineAttributes lia = goFactory.copyOf(needleComponent.getLineAttributes());

		Fill fl = bCategoryStyle ? (FillUtil.getPaletteFill(pa.getEntries(), iValueValidIndex))
				: (FillUtil.getPaletteFill(pa.getEntries(),
						(dial.getSeriesDefinition().getRunTimeSeries().indexOf(ds))));

		dial.updateTranslucency(fl, ds);

		final AbstractScriptHandler<?> sh = dial.getRunTimeContext().getScriptHandler();

		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_DATA_POINT, dphValue, fl,
				dial.getRunTimeContext().getScriptContext());
		dial.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_DATA_POINT, dphValue);

		// Convert MultipleFill if needed
		fl = FillUtil.convertFill(fl, ((Number) dphValue.getOrthogonalValue()).doubleValue(), null);
		if (fl instanceof ColorDefinition) {
			lia.setColor((ColorDefinition) fl);
		} else if (fl instanceof Gradient) {
			lia.setColor(((Gradient) fl).getStartColor());
		}

		final LineRenderEvent lreNeedle = ((EventObjectCache) idr)
				.getEventObject(WrappedStructureSource.createSeriesDataPoint(ds, dphValue), LineRenderEvent.class);
		lreNeedle.setLineAttributes(lia);
		lreNeedle.setStart(goFactory.createLocation(dialBounds.getLeft() + dialBounds.getWidth() / 2,
				dialBounds.getTop() + dialBounds.getHeight() / 2));

		double xOffset = dDialRadius * Math.cos(Math.toRadians(needleAngle));
		double yOffset = dDialRadius * Math.sin(Math.toRadians(needleAngle));
		lreNeedle.setEnd(
				goFactory.createLocation(lreNeedle.getStart().getX() + xOffset, lre.getStart().getY() - yOffset));
		dc.addLine(lreNeedle);

		if (dial.isInteractivityEnabled()) {
			final EList<Trigger> elTriggers = ds.getTriggers();
			if (!elTriggers.isEmpty()) {
				final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(ds, dphValue);
				final InteractionEvent iev = ((EventObjectCache) idr).getEventObject(iSource, InteractionEvent.class);
				iev.setCursor(ds.getCursor());

				Trigger tg;
				for (int t = 0; t < elTriggers.size(); t++) {
					tg = goFactory.copyOf(elTriggers.get(t));
					dial.processTrigger(tg, iSource);
					iev.addTrigger(tg);
				}
				iev.setHotSpot(lreNeedle);
				idr.enableInteraction(iev);
			}
		}

		// Needle decorator
		renderNeedleDecorator(idr, needleComponent, lreNeedle.getStart(), lreNeedle.getEnd(),
				lreNeedle.getLineAttributes(), fl, needleAngle, dialBounds.getWidth() / 2d, dphValue);

		ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_DATA_POINT, dphValue, fl,
				dial.getRunTimeContext().getScriptContext());
		dial.getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_DATA_POINT, dphValue);
	}

	private void renderNeedleDecorator(IDeviceRenderer idr, Needle ndl, Location startPoint, Location endPoint,
			LineAttributes lia, Fill fill, double angle, double dialRadius, DataPointHints dph) throws ChartException {
		double decSize = 4 * ((lia.getThickness() + 5) / 5) * dScale;

		// dynamically adjust the needle size.
		if (decSize > dialRadius / 5) {
			decSize = Math.max(dialRadius / 5d, 1);
		}

		switch (ndl.getDecorator().getValue()) {
		case LineDecorator.ARROW:
			final PolygonRenderEvent pre = ((EventObjectCache) idr)
					.getEventObject(WrappedStructureSource.createSeriesDataPoint(ds, dph), PolygonRenderEvent.class);

			Location[] loa = new Location[3];
			loa[0] = endPoint.copyInstance();
			loa[1] = goFactory.createLocation(endPoint.getX() - 2 * decSize * Math.cos(Math.toRadians(angle - 30)),
					endPoint.getY() + 2 * decSize * Math.sin(Math.toRadians(angle - 30)));
			loa[2] = goFactory.createLocation(endPoint.getX() - 2 * decSize * Math.sin(Math.toRadians(60 - angle)),
					endPoint.getY() + 2 * decSize * Math.cos(Math.toRadians(60 - angle)));
			pre.setPoints(loa);
			pre.setBackground(fill);
			pre.setOutline(lia);
			dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);

			if (dial.isInteractivityEnabled()) {
				final EList<Trigger> elTriggers = ds.getTriggers();
				if (!elTriggers.isEmpty()) {
					final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(ds, dph);
					final InteractionEvent iev = ((EventObjectCache) idr).getEventObject(iSource,
							InteractionEvent.class);
					iev.setCursor(ds.getCursor());

					Trigger tg;
					for (int t = 0; t < elTriggers.size(); t++) {
						tg = goFactory.copyOf(elTriggers.get(t));
						dial.processTrigger(tg, iSource);
						iev.addTrigger(tg);
					}
					iev.setHotSpot(pre);
					idr.enableInteraction(iev);
				}
			}
			break;

		case LineDecorator.CIRCLE:

			final OvalRenderEvent ore = ((EventObjectCache) idr)
					.getEventObject(WrappedStructureSource.createSeriesDataPoint(ds, dph), OvalRenderEvent.class);

			double radius = Math.sqrt(Math.pow(endPoint.getX() - startPoint.getX(), 2)
					+ Math.pow(endPoint.getY() - startPoint.getY(), 2));
			double x = startPoint.getX() + (radius - decSize) * Math.cos(Math.toRadians(angle));
			double y = startPoint.getY() - (radius - decSize) * Math.sin(Math.toRadians(angle));

			Bounds bo = goFactory.createBounds(x - decSize, y - decSize, 2 * decSize, 2 * decSize);
			ore.setBounds(bo);
			ore.setOutline(lia);
			ore.setBackground(fill);
			dc.addPlane(ore, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);

			if (dial.isInteractivityEnabled()) {
				final EList<Trigger> elTriggers = ds.getTriggers();
				if (!elTriggers.isEmpty()) {
					final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(ds, dph);
					final InteractionEvent iev = ((EventObjectCache) idr).getEventObject(iSource,
							InteractionEvent.class);
					iev.setCursor(ds.getCursor());

					Trigger tg;
					for (int t = 0; t < elTriggers.size(); t++) {
						tg = goFactory.copyOf(elTriggers.get(t));
						dial.processTrigger(tg, iSource);
						iev.addTrigger(tg);
					}
					iev.setHotSpot(ore);
					idr.enableInteraction(iev);
				}
			}
			break;

		case LineDecorator.NONE:
			break;
		}

	}

	private Size computeLabelSize(AutoScale sc, Label la) throws ChartException {
		if (!la.isVisible()) {
			return SizeImpl.create(0, 0);
		}

		RotatedRectangle rr;
		double x = 0, y = 0;
		double dw = 0;
		double dh = 0;
		AxisTickCoordinates da = sc.getTickCordinates();

		double dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue();
		final double dAxisStep = Methods.asDouble(sc.getStep()).doubleValue();
		DecimalFormat df = null;
		FormatSpecifier fs = sc.getFormatSpecifier();
		if (fs == null) {
			// df = new DecimalFormat( sc.getNumericPattern( ) );
		}
		final NumberDataElement nde = NumberDataElementImpl.create(0);
		String sText;

		sla = new String[da.size()];
		Double fontHeight = cComp.computeFontHeight(xs, la);

		for (int i = 0; i < da.size(); i++) {
			try {
				if (sc.isBigNumber()) {
					BigDecimal bdValue = sc.getBigNumberDivisor().multiply(BigDecimal.valueOf(dAxisValue),
							NumberUtil.DEFAULT_MATHCONTEXT);
					sText = ValueFormatter.format(bdValue, fs, dial.getRunTimeContext().getULocale(), df);
				} else {
					if (ChartUtil.mathEqual(dAxisValue, (int) dAxisValue)) {
						nde.setValue((int) dAxisValue);
					} else {
						nde.setValue(dAxisValue);
					}

					sText = ValueFormatter.format(nde, fs, dial.getRunTimeContext().getULocale(), df);
				}
			} catch (ChartException dfex) {
				sText = IConstants.NULL_STRING;
			}

			x = da.getCoordinate(i);

			la.getCaption().setValue(sText);
			try {
				rr = cComp.computePolygon(xs, IConstants.ABOVE, la, x, y, fontHeight);
			} catch (IllegalArgumentException uiex) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, uiex);
			}

			Rectangle rt = rr.getBounds();
			dw = Math.max(rt.getWidth(), dw);
			dh = Math.max(rt.getHeight(), dh);

			if (i == da.size() - 2) {
				// This is the last tick, use pre-computed value to handle
				// non-equal scale unit case.
				dAxisValue = Methods.asDouble(sc.getMaximum()).doubleValue();
			} else {
				dAxisValue += dAxisStep;
			}

			sla[i] = sText;
		}

		return SizeImpl.create(dw, dh);
	}

	private double transformAngle(double angle) {
		if (!inverseScale) {
			angle = dStopAngle + dStartAngle - angle;
		}
		return angle;
	}

	private double[] getMinMaxValue() throws ChartException {
		double[] values = srh.asPrimitiveDoubleValues();
		return getMinMaxValue(values);
	}

	public static double[] getMinMaxValue(double values[]) throws ChartException {
		if (values == null || values.length == 0) {
			throw new ChartException(ChartEngineExtensionPlugin.ID, ChartException.GENERATION, "Empty dataset found", //$NON-NLS-1$
					Messages.getResourceBundle());
		}
		double min = 0;
		double max = min;
		for (int i = 0; i < values.length; i++) {
			if (!Double.isNaN(values[i])) {
				min = Math.min(min, values[i]);
				max = Math.max(max, values[i]);
			}
		}

		if (min == max) {
			min = -1;
			max = 1;
		}

		return new double[] { min, max };
	}

	private AutoScale computeScale(Label lb, DataSetIterator dsi, double dValue, double dStart, double dEnd,
			Scale scModel, FormatSpecifier fs, RunTimeContext rtc, double fullRadius, BigDecimal divisor)
			throws ChartException {
		AutoScale sc;
		AutoScale scCloned = null;
		final DataElement oMinimum = scModel.getMin();
		final DataElement oMaximum = scModel.getMax();
		final Double oStep = scModel.isSetStep() ? new Double(scModel.getStep()) : null;
		final Integer oStepNumber = scModel.isSetStepNumber() ? scModel.getStepNumber() : null;

		final double[] minMax = getMinMaxValue();
		final double dMinValue = minMax[0], dMaxValue = minMax[1];

		// Set half difference as default step
		double dStep = Math.abs(dMaxValue - dMinValue) / 2;

		ScaleInfo info = new ScaleInfo(dial.getComputations(), IConstants.LINEAR | IConstants.NUMERICAL, rtc, fs, null,
				IConstants.FORWARD, true);
		sc = new AutoScale(info);
		sc.setBigNubmerDivisor(divisor);

		sc.setMinimum(Double.valueOf(dMinValue));
		sc.setMaximum(Double.valueOf(dMaxValue));
		sc.setStep(new Double(dStep));
		sc.setStepNumber(oStepNumber);
		sc.setData(dsi);

		// OVERRIDE MINIMUM IF SPECIFIED
		if (oMinimum != null) {
			if (oMinimum instanceof NumberDataElement) {
				sc.setMinimum(new Double(((NumberDataElement) oMinimum).getValue()));
			} else {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
						"exception.invalid.minimum.scale.value", //$NON-NLS-1$
						new Object[] { sc.getMinimum(), "" //$NON-NLS-1$
						}, Messages.getResourceBundle(rtc.getULocale()));
			}
			sc.setMinimumFixed(true);
		} else if (!Double.isNaN(dMinValue)) {
			sc.setMinimum(new Double(dMinValue));
			sc.setMinimumFixed(true);
		}

		// OVERRIDE MAXIMUM IF SPECIFIED
		if (oMaximum != null) {
			if (oMaximum instanceof NumberDataElement) {
				sc.setMaximum(new Double(((NumberDataElement) oMaximum).getValue()));
			} else {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
						"exception.invalid.maximum.scale.value", //$NON-NLS-1$
						new Object[] { sc.getMaximum(), "" //$NON-NLS-1$
						}, Messages.getResourceBundle(rtc.getULocale()));
			}
			sc.setMaximumFixed(true);
		} else if (!Double.isNaN(dMaxValue)) {
			sc.setMaximum(new Double(dMaxValue));
			sc.setMaximumFixed(true);
		}

		// OVERRIDE STEP IF SPECIFIED
		AutoScale.setStepToScale(sc, oStep, oStepNumber, rtc);

		// VALIDATE OVERRIDDEN MIN/MAX
		if (sc.isMaximumFixed() && sc.isMinimumFixed()) {
			if (((Double) sc.getMinimum()).doubleValue() > ((Double) sc.getMaximum()).doubleValue()) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
						"exception.min.largerthan.max", //$NON-NLS-1$
						new Object[] { sc.getMinimum(), sc.getMaximum() },
						Messages.getResourceBundle(rtc.getULocale()));
			}
		}

		final Object oMinValue = new Double(dMinValue);
		final Object oMaxValue = new Double(dMaxValue);
		sc.updateAxisMinMax(oMinValue, oMaxValue);

		sc.computeTicks(xs, lb, IConstants.ABOVE, IConstants.HORIZONTAL, dStart, dEnd, false, null);
		dStart = sc.getStart();
		dEnd = sc.getEnd();

		cacheNumFormat = new ChartUtil.CacheDecimalFormat(rtc.getULocale());
		boolean bFirstFit = checkFit(sc, xs, lb, IConstants.ABOVE, fullRadius);
		boolean bFits = bFirstFit;
		boolean bZoomSuccess = false;

		// THE AUTO ZOOM LOOP
		while (bFits == bFirstFit) {
			bZoomSuccess = true;
			scCloned = (AutoScale) sc.clone();
			if (sc.isStepFixed()) // DO NOT AUTO ZOOM IF STEP IS FIXED
			{
				break;
			}
			if (bFirstFit) {
				if (!bFits) {
					break;
				}
				bZoomSuccess = sc.zoomIn();
			} else {
				if (!bFits && sc.getTickCount() == 2) {
					break;
				}
				bZoomSuccess = sc.zoomOut();
			}
			if (!bZoomSuccess) {
				break;
			}

			sc.updateAxisMinMax(oMinValue, oMaxValue);
			sc.computeTicks(xs, lb, IConstants.ABOVE, IConstants.HORIZONTAL, dStart, dEnd, false, null);
			bFits = checkFit(sc, xs, lb, IConstants.ABOVE, fullRadius);
			if (!bFits && sc.getTickCount() == 2) {
				sc = scCloned;
				break;
			}
		}

		// RESTORE TO LAST SCALE BEFORE ZOOM
		if (scCloned != null && bFirstFit && bZoomSuccess) {
			sc = scCloned;
		}

		sc.setData(dsi);
		return sc;
	}

	/**
	 * Checks all labels for any overlap for a given axis' scale, SPECIFICALLY ONLY
	 * used for meter scale(numeric,linear).
	 *
	 * @param la
	 * @param iLabelLocation
	 *
	 * @return
	 */
	private boolean checkFit(AutoScale sc, IDisplayServer xs, Label la, int iLabelLocation, double fullRadius)
			throws ChartException {
		final double dAngleInDegrees = la.getCaption().getFont().getRotation();
		double x = 0, y = 0;
		int iPointToCheck = 0;
		if (iLabelLocation == IConstants.ABOVE || iLabelLocation == IConstants.BELOW) {
			if (sc.getDirection() == IConstants.BACKWARD) {
				iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 1 : 2;
			} else {
				iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 3 : 0;
			}
		} else if (iLabelLocation == IConstants.LEFT || iLabelLocation == IConstants.RIGHT) {
			if (sc.getDirection() == IConstants.FORWARD) {
				iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 0 : 1;
			} else {
				iPointToCheck = (dAngleInDegrees < 0 && dAngleInDegrees > -90) ? 2 : 3;
			}
		}
		AxisTickCoordinates angles = sc.getTickCordinates();
		double da;

		RotatedRectangle rrPrev = null, rrPrev2 = null, rr;

		// only need to handle this numerical/linear case
		if ((sc.getType() & (IConstants.NUMERICAL | IConstants.LINEAR)) == (IConstants.NUMERICAL | IConstants.LINEAR)) {
			double dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue();
			final double dAxisStep = Methods.asDouble(sc.getStep()).doubleValue();
			String sText;
			DecimalFormat df = null;
			if (sc.getFormatSpecifier() == null) // CREATE IF FORMAT
			// SPECIFIER IS UNDEFINED
			{
				df = cacheNumFormat.get(sc.getNumericPattern());
			}
			final NumberDataElement nde = NumberDataElementImpl.create(0);

			// compute full range length
			final double delta = Math.PI * fullRadius / 180d;

			Double fontHeight = cComp.computeFontHeight(xs, la);

			for (int i = 0; i < angles.size(); i++) {
				// compute real label postion as per the angle
				da = Math.abs(angles.getCoordinate(i) - sc.getStart()) * delta;

				// TODO special logic for last datapoint in non-equal scale unit
				// case.

				if (ChartUtil.mathEqual(dAxisValue, (int) dAxisValue)) {
					nde.setValue((int) dAxisValue);
				} else {
					nde.setValue(dAxisValue);
				}

				try {
					sText = ValueFormatter.format(nde, sc.getFormatSpecifier(), sc.getRunTimeContext().getULocale(),
							df);
				} catch (ChartException dfex) {
					logger.log(dfex);
					sText = IConstants.NULL_STRING;
				}

				if (iLabelLocation == IConstants.ABOVE || iLabelLocation == IConstants.BELOW) {
					x = da;
				} else if (iLabelLocation == IConstants.LEFT || iLabelLocation == IConstants.RIGHT) {
					y = da;
				}

				la.getCaption().setValue(sText);
				try {
					rr = cComp.computePolygon(xs, iLabelLocation, la, x, y, fontHeight);
				} catch (IllegalArgumentException uiex) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, uiex);
				}

				Point p = rr.getPoint(iPointToCheck);

				if (sc.isAxisLabelStaggered() && sc.isTickLabelStaggered(i)) {
					if (rrPrev2 != null && (rrPrev2.contains(p) || rrPrev2.getPoint(iPointToCheck).equals(p)
							|| ChartUtil.intersects(rr, rrPrev2))) {
						return false;
					}
					rrPrev2 = rr;
				} else {
					if (rrPrev != null && (rrPrev.contains(p) || rrPrev.getPoint(iPointToCheck).equals(p)
							|| ChartUtil.intersects(rr, rrPrev))) {
						return false;
					}
					rrPrev = rr;
				}
				dAxisValue += dAxisStep;
			}
		}
		return true;
	}

	// Returns the tick size according to the dpi
	public double getTickSize() {
		return IConstants.TICK_SIZE / 72d * xs.getDpiResolution();
	}
}
