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

package org.eclipse.birt.chart.computation.withaxes;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Rectangle;
import org.eclipse.birt.chart.computation.UserDataSetHints;
import org.eclipse.birt.chart.computation.Vector;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.render.ISeriesRenderingHints;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.NumberUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.util.Calendar;

/**
 * This class is capable of computing the content of a chart (with axes) based
 * on preferred sizes, text rotation, fit ability, scaling, etc and prepares it
 * for rendering.
 * 
 * WARNING: This is an internal class and subject to change
 */
public class PlotWith3DAxes extends PlotWithAxes {

	private final double SPACE_THRESHOLD;

	private Engine3D engine;

	private Bounds cachedAdjustedBounds;

	protected double dZAxisPlotSpacing = 0;

	/**
	 * @param _ids
	 * @param _cwa
	 * @param _rtc
	 * @throws IllegalArgumentException
	 * @throws ChartException
	 */
	public PlotWith3DAxes(IDisplayServer _ids, ChartWithAxes _cwa, RunTimeContext _rtc)
			throws IllegalArgumentException, ChartException {
		super(_ids, _rtc, _cwa);
		SPACE_THRESHOLD = 5 * dPointToPixel;

		if (_cwa.isTransposed()) {
			// Not support transposed for 3D chart.
			throw new ChartException(ChartEnginePlugin.ID, ChartException.COMPUTATION,
					"exception.no.transposed.3D.chart", //$NON-NLS-1$
					Messages.getResourceBundle(getRunTimeContext().getULocale()));
		}

		buildAxes(); // CREATED ONCE
	}

	private PlotWith3DAxes getPWA3D() {
		return this;
	}

	public Bounds getAdjustedPlotBounds(boolean refresh) throws ChartException {
		if (!refresh && cachedAdjustedBounds != null) {
			return cachedAdjustedBounds;
		}

		final Bounds bo = goFactory.adjusteBounds(getPlotBounds(), getPlotInsets());

		double h, w;
		Label la;

		// approximate estimation of axes label space
		// TODO better estimation
		la = goFactory.copyOf(aax.getPrimaryBase().getLabel());
		la.getCaption().setValue("X"); //$NON-NLS-1$
		h = cComp.computeHeight(ids, la);

		la = goFactory.copyOf(aax.getAncillaryBase().getLabel());
		la.getCaption().setValue("X"); //$NON-NLS-1$
		h = Math.max(h, cComp.computeHeight(ids, la));

		la = goFactory.copyOf(aax.getPrimaryOrthogonal().getLabel());
		la.getCaption().setValue("X"); //$NON-NLS-1$
		w = cComp.computeWidth(ids, la);

		// consider axes lable space.
		bo.adjust(goFactory.createInsets(0, 0, h, w));
		cachedAdjustedBounds = bo;
		return bo;
	}

	public final Location getPanningOffset() throws ChartException {
		final Bounds bo = getAdjustedPlotBounds(false);

		double xOff = bo.getLeft();
		double yOff = bo.getTop();

		// TODO read custom panning setting

		return goFactory.createLocation(xOff, yOff);
	}

	/**
	 * Returns the 3D engine for this render.
	 */
	public final Engine3D get3DEngine() {
		if (engine == null) {
			// TODO read custom light direction setting

			// Use a fixed light direction here.
			Vector lightDirection = new Vector(-1, 1, -1, false);

			final Bounds bo = goFactory.adjusteBounds(getPlotBounds(), getPlotInsets());

			double width = bo.getWidth();
			double height = bo.getHeight();

			engine = new Engine3D(getModel().getRotation(), lightDirection, width, height, 500 * dPointToPixel,
					1500 * dPointToPixel, 10 * dPointToPixel, 10000 * dPointToPixel, 100);
		}

		return engine;
	}

	private double detectZoomScale(Engine3D engine, double xdz, double xOff, double yOff, double width, double height) {
		double zlen = 1 * dPointToPixel;
		double xlen = xdz * dPointToPixel;
		double ylen = (xdz + 1) * dPointToPixel / 2d;

		List<Location3D> vertexList = new ArrayList<Location3D>();

		Location3D bbl = goFactory.createLocation3D(-xlen / 2, -ylen / 2, -zlen / 2);
		Location3D bbr = goFactory.createLocation3D(xlen / 2, -ylen / 2, -zlen / 2);
		Location3D bfl = goFactory.createLocation3D(-xlen / 2, -ylen / 2, zlen / 2);
		Location3D bfr = goFactory.createLocation3D(xlen / 2, -ylen / 2, zlen / 2);
		Location3D tbl = goFactory.createLocation3D(-xlen / 2, ylen / 2, -zlen / 2);
		Location3D tbr = goFactory.createLocation3D(xlen / 2, ylen / 2, -zlen / 2);
		Location3D tfl = goFactory.createLocation3D(-xlen / 2, ylen / 2, zlen / 2);
		Location3D tfr = goFactory.createLocation3D(xlen / 2, ylen / 2, zlen / 2);

		vertexList.add(bbl);
		vertexList.add(bbr);
		vertexList.add(bfl);
		vertexList.add(bfr);
		vertexList.add(tbl);
		vertexList.add(tbr);
		vertexList.add(tfl);
		vertexList.add(tfr);

		Text3DRenderEvent event = new Text3DRenderEvent(this);

		double maxLeft = Double.MAX_VALUE;
		double maxRight = -Double.MAX_VALUE;
		double maxTop = Double.MAX_VALUE;
		double maxBottom = -Double.MAX_VALUE;
		Location p2d;
		double x, y;

		for (Iterator<Location3D> itr = vertexList.iterator(); itr.hasNext();) {
			Location3D p3d = itr.next();

			event.setLocation3D(goFactory.createLocation3D(p3d.getX(), p3d.getY(), p3d.getZ()));
			if (engine.processEvent(event, xOff, yOff) != null) {
				p2d = event.getLocation();

				x = p2d.getX();
				y = p2d.getY();

				if (x < maxLeft) {
					maxLeft = x;
				}
				if (x > maxRight) {
					maxRight = x;
				}
				if (y < maxTop) {
					maxTop = y;
				}
				if (y > maxBottom) {
					maxBottom = y;
				}
			}
		}

		double vSpace = maxTop - yOff;
		double hSpace = maxLeft - xOff;

		if (yOff + height - maxBottom < maxTop - yOff) {
			vSpace = yOff + height - maxBottom;
		}

		if (xOff + width - maxRight < maxLeft - xOff) {
			hSpace = xOff + width - maxRight;
		}

		double minSpace = Math.min(hSpace, vSpace);
		double lastMinspace = 0;
		boolean fit = hSpace > 0 && vSpace > 0;
		double lastScale = 1;
		double scale = lastScale;
		boolean iterateStarted = false;

		if (!fit) {
			// if even the minimum scale failed, return with no iteration
			return 1;
		}

		while (ChartUtil.mathGT(Math.abs(minSpace - lastMinspace), 0) && (fit && minSpace > SPACE_THRESHOLD || !fit)) {
			if (fit && !iterateStarted) {
				// double zoomin
				scale = lastScale * 2;
			} else {
				// To avoid endless loop
				if (ChartUtil.mathEqual(scale, lastScale)) {
					break;
				}
				// dichotomia zoomin/out
				scale = (lastScale + scale) / 2;
			}

			maxLeft = Double.MAX_VALUE;
			maxRight = -Double.MAX_VALUE;
			maxTop = Double.MAX_VALUE;
			maxBottom = -Double.MAX_VALUE;

			boolean forceBreak = false;

			// check all 8 points.
			for (Iterator<Location3D> itr = vertexList.iterator(); itr.hasNext();) {
				Location3D p3d = itr.next();

				event.setLocation3D(
						goFactory.createLocation3D(p3d.getX() * scale, p3d.getY() * scale, p3d.getZ() * scale));
				if (engine.processEvent(event, xOff, yOff) != null) {
					p2d = event.getLocation();

					x = p2d.getX();
					y = p2d.getY();

					// System.out.println( "x: " + x + ", y: " + y );

					if (x < maxLeft) {
						maxLeft = x;
					}
					if (x > maxRight) {
						maxRight = x;
					}
					if (y < maxTop) {
						maxTop = y;
					}
					if (y > maxBottom) {
						maxBottom = y;
					}
				} else {
					fit = false;
					forceBreak = true;
					break;
				}
			}

			if (!forceBreak) {
				vSpace = maxTop - yOff;
				hSpace = maxLeft - xOff;

				if (yOff + height - maxBottom < maxTop - yOff) {
					vSpace = yOff + height - maxBottom;
				}

				if (xOff + width - maxRight < maxLeft - xOff) {
					hSpace = xOff + width - maxRight;
				}

				fit = vSpace > 0 && hSpace > 0;
			}

			if (fit) {
				lastMinspace = minSpace;
				minSpace = Math.min(hSpace, vSpace);
				double nextScale = 2 * scale - lastScale;
				lastScale = scale;
				scale = nextScale;
			} else if (!iterateStarted) {
				iterateStarted = true;
			}

		}

		return lastScale;
	}

	private double computeAxisZoomFactor(Engine3D engine, double start, double end, Location3D startVertext,
			Location3D endVertext, double xOff, double yOff) {
		Text3DRenderEvent event = new Text3DRenderEvent(this);
		Location p2d;
		double x1, y1, x2, y2;

		event.setLocation3D(startVertext);
		if (engine.processEvent(event, xOff, yOff) != null) {
			p2d = event.getLocation();

			x1 = p2d.getX();
			y1 = p2d.getY();

			event.setLocation3D(endVertext);

			if (engine.processEvent(event, xOff, yOff) != null) {
				p2d = event.getLocation();

				x2 = p2d.getX();
				y2 = p2d.getY();

				return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1)) / (end - start);
			}
		}

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#compute(org.eclipse.
	 * birt.chart.model.attribute.Bounds)
	 */
	public void compute(Bounds bo) throws ChartException, IllegalArgumentException {
		bo = goFactory.scaleBounds(bo, dPointToPixel); // CONVERSION

		boPlot = bo;
		boPlotBackground = goFactory.copyOf(bo);

		// MUST BE 3-D DIMENSION ONLY HERE.
		iDimension = getDimension(cm.getDimension());
		assert iDimension == IConstants.THREE_D;

		dXAxisPlotSpacing = cm.getPlot().getHorizontalSpacing() * dPointToPixel; // CONVERSION
		dYAxisPlotSpacing = cm.getPlot().getVerticalSpacing() * dPointToPixel; // CONVERSION
		dZAxisPlotSpacing = dXAxisPlotSpacing;

		PWA3DComputeContext context = new PWA3DComputeContext();

		initAxesDatasets(context);

		final Bounds adjustedBounds = getAdjustedPlotBounds(true);
		computeWithAdjBound(context, adjustedBounds);
		readjustBounds(context, adjustedBounds, bo);
		computeWithAdjBound(context, adjustedBounds);

	}

	private class PWA3DComputeContext {
		final Axis[] axa = getModel().getPrimaryBaseAxes();
		final Axis axPrimaryBase = axa[0];
		final Axis axPrimaryOrthogonal = getModel().getPrimaryOrthogonalAxis(axPrimaryBase);
		final Axis axAncillaryBase = getModel().getAncillaryBaseAxis(axPrimaryBase);
		int iPrimaryAxisType = getAxisType(axPrimaryBase);
		int iAncillaryAxisType = getAxisType(axAncillaryBase);
		int iOrthogonalAxisType = getAxisType(axPrimaryOrthogonal);
		OneAxis oaxPrimaryBase = aax.getPrimaryBase();
		OneAxis oaxPrimaryOrthogonal = aax.getPrimaryOrthogonal();
		OneAxis oaxAncillaryBase = aax.getAncillaryBase();
		DataSetIterator dsiPrimaryBase;
		DataSetIterator dsiOrthogonal;
		DataSetIterator dsiAncillary;
		double dX;
		double dY;
		double dZ;
	}

	// INITIALIZE AXES DATASETS
	private void initAxesDatasets(PWA3DComputeContext context) throws ChartException {
		Object oaData = null;

		// COMPUTE PRIMARY BASE DATA
		int iPrimaryAxisType = context.iPrimaryAxisType;
		if (iPrimaryAxisType == TEXT || context.oaxPrimaryBase.isCategoryScale()) {
			oaData = getTypedDataSet(context.axPrimaryBase, iPrimaryAxisType, 0);
		} else if ((iPrimaryAxisType & NUMERICAL) == NUMERICAL) {
			oaData = getMinMax(context.axPrimaryBase, iPrimaryAxisType);
		} else if ((iPrimaryAxisType & DATE_TIME) == DATE_TIME) {
			oaData = getMinMax(context.axPrimaryBase, iPrimaryAxisType);
		}
		context.dsiPrimaryBase = (oaData instanceof DataSetIterator) ? (DataSetIterator) oaData
				: new DataSetIterator(oaData, iPrimaryAxisType);
		oaData = null;

		// COMPUTE ANCILLARY BASE DATA
		int iAncillaryAxisType = context.iAncillaryAxisType;
		if (iAncillaryAxisType == TEXT || context.oaxAncillaryBase.isCategoryScale()) {
			oaData = getAncillaryDataSet(context.axAncillaryBase, context.axPrimaryOrthogonal, iAncillaryAxisType);
		} else if ((iAncillaryAxisType & NUMERICAL) == NUMERICAL) {
			oaData = getMinMax(context.axAncillaryBase, iAncillaryAxisType);
		} else if ((iAncillaryAxisType & DATE_TIME) == DATE_TIME) {
			oaData = getMinMax(context.axAncillaryBase, iAncillaryAxisType);
		}
		context.dsiAncillary = (oaData instanceof DataSetIterator) ? (DataSetIterator) oaData
				: new DataSetIterator(oaData, iAncillaryAxisType);
		oaData = null;

		// COMPUTE ORTHOGONAL DATA
		int iOrthogonalAxisType = context.iOrthogonalAxisType;
		if ((iOrthogonalAxisType & NUMERICAL) == NUMERICAL || (iOrthogonalAxisType & DATE_TIME) == DATE_TIME) {
			context.dsiOrthogonal = new DataSetIterator(getMinMax(context.axPrimaryOrthogonal, iOrthogonalAxisType),
					iOrthogonalAxisType);
		} else {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
					"exception.orthogonal.axis.numerical.datetime", //$NON-NLS-1$
					Messages.getResourceBundle(rtc.getULocale()));
		}

	}

	private void computeWithAdjBound(PWA3DComputeContext context, Bounds adjustedBounds) throws ChartException {
		double dXDZ = context.dsiPrimaryBase.size() * 1d / context.dsiAncillary.size();
		double xOffset = adjustedBounds.getLeft();
		double yOffset = adjustedBounds.getTop();

		double zoomScale = detectZoomScale(get3DEngine(), dXDZ, xOffset, yOffset, adjustedBounds.getWidth(),
				adjustedBounds.getHeight());

		double dWZ = zoomScale * dPointToPixel;
		double dW = dXDZ * zoomScale * dPointToPixel;
		double dH = (dW + dWZ) / 2;
		double dX = -dW / 2;
		double dY = -dH / 2;
		double dZ = -dWZ / 2;

		context.dX = dX;
		context.dY = dY;
		context.dZ = dZ;

		Location panningOffset = getPanningOffset();

		double xZoom = computeAxisZoomFactor(get3DEngine(), dX, dX + dW, goFactory.createLocation3D(dX, dY, dZ),
				goFactory.createLocation3D(dX + dW, dY, dZ), panningOffset.getX(), panningOffset.getY());
		double yZoom = computeAxisZoomFactor(get3DEngine(), dY, dY + dH, goFactory.createLocation3D(dX, dY, dZ),
				goFactory.createLocation3D(dX, dY + dH, dZ), panningOffset.getX(), panningOffset.getY());
		double zZoom = computeAxisZoomFactor(get3DEngine(), dZ, dZ + dWZ, goFactory.createLocation3D(dX, dY, dZ),
				goFactory.createLocation3D(dX, dY, dZ + dWZ), panningOffset.getX(), panningOffset.getY());

		double dStart, dEnd;

		// COMPUTE PRIMARY-BASE-AXIS PROPERTIES AND ITS SCALE
		AutoScale scPrimaryBase = null;
		dStart = dX;
		dEnd = dX + dW;
		scPrimaryBase = AutoScale.computeScale(ids, context.oaxPrimaryBase, context.dsiPrimaryBase,
				context.iPrimaryAxisType, dStart, dEnd, rtc, FORWARD, xZoom, 0, this);
		context.oaxPrimaryBase.set(scPrimaryBase); // UPDATE SCALE ON PRIMARY-BASE
		// AXIS

		// COMPUTE ANCILLARY-BASE-AXIS PROPERTIES AND ITS SCALE
		AutoScale scAncillaryBase = null;
		dStart = dZ;
		dEnd = dZ + dWZ;
		scAncillaryBase = AutoScale.computeScale(ids, context.oaxAncillaryBase, context.dsiAncillary,
				context.iAncillaryAxisType, dStart, dEnd, rtc, FORWARD, zZoom, 0, this);
		context.oaxAncillaryBase.set(scAncillaryBase); // UPDATE SCALE ON
		// ANCILLARY-BASE AXIS

		// COMPUTE PRIMARY-ORTHOGONAL-AXIS PROPERTIES AND ITS SCALE
		AutoScale scPrimaryOrthogonal = null;
		dStart = dY;
		dEnd = dY + dH;
		scPrimaryOrthogonal = AutoScale.computeScale(ids, context.oaxPrimaryOrthogonal, context.dsiOrthogonal,
				context.iOrthogonalAxisType, dStart, dEnd, rtc, FORWARD, yZoom, 0, this);
		context.oaxPrimaryOrthogonal.set(scPrimaryOrthogonal); // UPDATE SCALE ON

		setupFullDataSets(context);

		context.oaxPrimaryBase.getScale().resetShifts();
		context.oaxAncillaryBase.getScale().resetShifts();
		context.oaxPrimaryOrthogonal.getScale().resetShifts();

		setAxisCoordinatesForAll(context);

	}

	private double computeYAxisTitleThickness() throws ChartException {
		final OneAxis axxPV = aax.getPrimaryOrthogonal();
		Label laYAxisTitle = axxPV.getTitle();
		double dYAxisTitleThickness = 0;

		if (laYAxisTitle.isVisible()) {
			dYAxisTitleThickness = cComp
					.computeLabelSize(ids, laYAxisTitle,
							ChartUtil.computeHeightOfOrthogonalAxisTitle(getModel(), getDisplayServer()), null)
					.getWidth();
		}

		return dYAxisTitleThickness;
	}

	private double computeXAxisTitleThickness() throws ChartException {
		final OneAxis axxPB = aax.getPrimaryBase();
		Label laXAxisTitle = axxPB.getTitle();
		double dXAxisTitleThickness = 0;

		if (laXAxisTitle.isVisible()) {
			dXAxisTitleThickness = cComp
					.computeLabelSize(ids, laXAxisTitle,
							ChartUtil.computeHeightOfOrthogonalAxisTitle(getModel(), getDisplayServer()), null)
					.getHeight();
		}

		return dXAxisTitleThickness;
	}

	private void readjustBounds(PWA3DComputeContext context, Bounds adjustedBounds, Bounds bo) throws ChartException {
		Rectangle rectl = this.getBoundsOfAllAxisLabels();

		if (rectl == null) {
			return;
		}
		rectl.union(this.get3DGraphicBoudingRect());

		double new_top = adjustedBounds.getTop();
		double new_height = adjustedBounds.getHeight();
		double new_left = adjustedBounds.getLeft();
		double new_width = adjustedBounds.getWidth();

		double dYAxisTitleThickness = this.computeYAxisTitleThickness();
		double dXAxisTitleThickness = this.computeXAxisTitleThickness();

		double rect_width = rectl.width + 2 * dYAxisTitleThickness + 5;

		if (bo.getWidth() < rect_width) {
			new_left += bo.getLeft() - rectl.x + dYAxisTitleThickness; // subtract width of Y axis title.
			new_width += bo.getWidth() - rect_width; // Subtract double width of Y axis title.
		} else {
			new_left += bo.getLeft() - (rect_width - bo.getWidth()) / 2 - rectl.x;

		}

		double rect_height = rectl.height + dXAxisTitleThickness + 5;

		if (bo.getHeight() < rect_height) {
			new_top += bo.getTop() - rectl.y;
			new_height += bo.getHeight() - rect_height;
		} else {
			new_top += bo.getTop() - (rect_height - bo.getHeight()) / 2 - rectl.y;

		}

		adjustedBounds.setLeft(new_left);
		adjustedBounds.setTop(new_top);
		adjustedBounds.setWidth(new_width);
		adjustedBounds.setHeight(new_height);

	}

	private void setupFullDataSets(PWA3DComputeContext context) throws ChartException {
		// SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
		context.oaxPrimaryOrthogonal.getScale()
				.setData(getTypedDataSet(context.axPrimaryOrthogonal, context.iOrthogonalAxisType, 0));

		// Setup the full dataset for the ancillary base axis.
		if (context.iAncillaryAxisType != IConstants.TEXT) {
			context.oaxAncillaryBase.getScale()
					.setData(getTypedDataSet(context.axAncillaryBase, context.iAncillaryAxisType, 0));
		}

		// SETUP THE FULL DATASET FOR THE PRIMARY ORTHOGONAL AXIS
		if (context.iPrimaryAxisType != IConstants.TEXT) {
			context.oaxPrimaryBase.getScale()
					.setData(getTypedDataSet(context.axPrimaryBase, context.iPrimaryAxisType, 0));
		}
	}

	private void setAxisCoordinatesForAll(PWA3DComputeContext context) {
		// Here we ignore the intersection Value/Max setting, always
		// use the Intersection.Min for 3D chart.
		double dYAxisLocationOnX = context.dX;
		double dYAxisLocationOnZ = context.dZ;
		double dXAxisLocation = context.dY;
		double dZAxisLocation = context.dY;

		// UPDATE FOR OVERLAYS
		final OneAxis axPH = aax.getPrimaryBase();
		final OneAxis axPV = aax.getPrimaryOrthogonal();
		final OneAxis axAB = aax.getAncillaryBase();

		// keep old invocation to ensure compatibility.
		axPH.setAxisCoordinate(dXAxisLocation);
		axPV.setAxisCoordinate(dYAxisLocationOnX);
		axAB.setAxisCoordinate(dZAxisLocation);

		// set new 3D axis coordinate. this coordinate has been normalized to
		// Zero-coordinates.
		axPH.setAxisCoordinate3D(goFactory.createLocation3D(0, dXAxisLocation, dYAxisLocationOnZ));
		axPV.setAxisCoordinate3D(goFactory.createLocation3D(dYAxisLocationOnX, 0, dYAxisLocationOnZ));
		axAB.setAxisCoordinate3D(goFactory.createLocation3D(dYAxisLocationOnX, dZAxisLocation, 0));
	}

	/**
	 * @param ax
	 * @param orthogonalAxis
	 * @param iType
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	protected final DataSetIterator getAncillaryDataSet(Axis ax, Axis orthogonalAxis, int iType)
			throws ChartException, IllegalArgumentException {
		final Series[] sea = ax.getRuntimeSeries();
		final Series[] osea = orthogonalAxis.getRuntimeSeries();

		if (sea.length == 0 || osea.length == 0) // TBD: PULL FROM SAMPLE
		// DATA
		{
			if ((iType & NUMERICAL) == NUMERICAL) {
				// TODO consistent with orthogonal series length;
				return new DataSetIterator(new Double[] { new Double(1), new Double(2) });
			} else if ((iType & DATE_TIME) == DATE_TIME) {
				// TODO consistent with orthogonal series length;
				return new DataSetIterator(new Calendar[] { new CDateTime(), new CDateTime() });
			} else if ((iType & TEXT) == TEXT) {
				// use orthogonal series identifier instead.
				List<String> data = new ArrayList<String>();

				if (osea.length > 0) {
					// Revert the order since the last series is in the nearest
					// of origin.
					for (int i = osea.length - 1; i >= 0; i--) {
						data.add(String.valueOf(osea[i].getSeriesIdentifier()));
					}
				} else {
					data.add("A"); //$NON-NLS-1$
				}

				return new DataSetIterator(data.toArray(new String[data.size()]));
			}
		}

		// Assume always use the first ancillary axis.
		DataSetIterator dsi = getTypedDataSet(sea[0], iType);
		List<Object> data = new ArrayList<Object>();

		for (int i = 0; i < osea.length; i++) {
			if (dsi.hasNext()) {
				data.add(dsi.next());
			} else if ((iType & NUMERICAL) == NUMERICAL) {
				data.add(new Double(0));
			} else if ((iType & DATE_TIME) == DATE_TIME) {
				data.add(new CDateTime());
			} else if ((iType & TEXT) == TEXT) {
				data.add(osea[i].getSeriesIdentifier());
			}
		}

		if ((iType & NUMERICAL) == NUMERICAL) {
			return new DataSetIterator(NumberDataSetImpl.create(data));
		} else if ((iType & DATE_TIME) == DATE_TIME) {
			return new DataSetIterator(DateTimeDataSetImpl.create(data));
		} else if ((iType & TEXT) == TEXT) {
			return new DataSetIterator(TextDataSetImpl.create(data));
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#getMinMax(org.
	 * eclipse.birt.chart.model.component.Axis, int)
	 */
	@Override
	protected Object getMinMax(Axis ax, int iType) throws ChartException, IllegalArgumentException {
		final Series[] sea = ax.getRuntimeSeries();
		final int iSeriesCount = sea.length;
		DataSet ds;

		Object oV1, oV2, oMin = null, oMax = null;

		PluginSettings ps = PluginSettings.instance();
		IDataSetProcessor iDSP = null;

		for (int i = 0; i < iSeriesCount; i++) {
			if (sea[i].isStacked()) {
				// 3D chart can't be stacked.
				throw new IllegalArgumentException(MessageFormat.format(
						Messages.getResourceBundle(rtc.getULocale()).getString("exception.unstackable.is.stacked"), //$NON-NLS-1$
						new Object[] { sea[i] }));
			}

			iDSP = ps.getDataSetProcessor(sea[i].getClass());
			ds = sea[i].getDataSet();

			oV1 = iDSP.getMinimum(ds);
			oV2 = iDSP.getMaximum(ds);

			if ((iType & NUMERICAL) == NUMERICAL) {
				if (oV1 != null) // SETUP THE MINIMUM VALUE FOR ALL DATASETS
				{
					if (oMin == null) {
						oMin = oV1;
					} else {
						if (NumberUtil.isBigNumber(oV1)) {
							oMin = ((BigNumber) oMin).min((BigNumber) oV1);
						} else {
							final double dV1 = asDouble(oV1).doubleValue();
							if (Math.min(asDouble(oMin).doubleValue(), dV1) == dV1) {
								oMin = oV1;
							}
						}
					}
				}

				if (oV2 != null) // SETUP THE MAXIMUM VALUE FOR ALL DATASETS
				{
					if (oMax == null) {
						oMax = oV2;
					} else {
						if (NumberUtil.isBigNumber(oV2)) {
							oMax = ((BigNumber) oMax).max((BigNumber) oV2);
						} else {
							final double dV2 = asDouble(oV2).doubleValue();
							if (Math.max(asDouble(oMax).doubleValue(), dV2) == dV2) {
								oMax = oV2;
							}
						}
					}
				}
			} else if ((iType & DATE_TIME) == DATE_TIME) {
				if (oV1 != null) // SETUP THE MINIMUM VALUE FOR ALL DATASETS
				{
					if (oMin == null) {
						oMin = oV1;
					} else {
						final CDateTime cdtV1 = asDateTime(oV1);
						final CDateTime cdtMin = asDateTime(oMin);
						if (cdtV1.before(cdtMin)) {
							oMin = cdtV1;
						}
					}
				}

				if (oV2 != null) // SETUP THE MAXIMUM VALUE FOR ALL DATASETS
				{
					if (oMax == null) {
						oMax = oV2;
					} else {
						final CDateTime cdtV2 = asDateTime(oV2);
						final CDateTime cdtMax = asDateTime(oMax);
						if (cdtV2.after(cdtMax)) {
							oMax = cdtV2;
						}
					}
				}
			}
		}

		// ONLY NUMERIC VALUES ARE SUPPORTED IN STACKED ELEMENT COMPUTATIONS
		if (ax.isPercent()) {
			// 3D Chart axis doesn't support Percent.
			throw new IllegalArgumentException(MessageFormat.format(
					Messages.getResourceBundle(rtc.getULocale()).getString("exception.no.stack.percent.3D.chart"), //$NON-NLS-1$
					new Object[] { ax }));
		}

		// IF NO DATASET WAS FOUND BECAUSE NO SERIES WERE ATTACHED TO AXES,
		// SIMULATE MIN/MAX VALUES
		if (oMin == null && oMax == null) {
			if (iType == DATE_TIME) {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				oMin = new CDateTime(year, 1, 1);
				oMax = new CDateTime(year, 12, 31);
			} else if ((iType & NUMERICAL) == NUMERICAL) {
				if ((iType & PERCENT) == PERCENT) {
					oMin = new Double(0);
					oMax = new Double(99.99);
				} else if ((iType & LOGARITHMIC) == LOGARITHMIC) {
					oMin = new Double(1);
					oMax = new Double(999);
				} else {
					oMin = new Double(-0.9);
					oMax = new Double(0.9);
				}
			}
		}

		if (iType == DATE_TIME) {
			try {
				return new Calendar[] { asDateTime(oMin), asDateTime(oMax) };
			} catch (ClassCastException ex) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
						"exception.numerical.data.datetime.axis", //$NON-NLS-1$
						Messages.getResourceBundle(rtc.getULocale()));

			}
		} else if ((iType & NUMERICAL) == NUMERICAL) {
			try {
				if (NumberUtil.isBigNumber(oMin) || NumberUtil.isBigNumber(oMax)) {
					return new BigNumber[] { (BigNumber) oMin, (BigNumber) oMax };
				} else {
					return new double[] { asDouble(oMin).doubleValue(), asDouble(oMax).doubleValue() };
				}
			} catch (ClassCastException ex) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_SET,
						"exception.datetime.data.numerical.axis", //$NON-NLS-1$
						Messages.getResourceBundle(rtc.getULocale()));
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#
	 * getSeriesRenderingHints(org.eclipse.birt.chart.model.data.SeriesDefinition,
	 * org.eclipse.birt.chart.model.component.Series)
	 */
	public ISeriesRenderingHints getSeriesRenderingHints(SeriesDefinition sdOrthogonal, Series seOrthogonal)
			throws ChartException, IllegalArgumentException {
		if (seOrthogonal == null || seOrthogonal.getClass() == SeriesImpl.class) // EMPTY
		// PLOT
		// RENDERING
		// TECHNIQUE
		{
			return null;
		}
		OneAxis oaxOrthogonal = findOrthogonalAxis(seOrthogonal);
		if (oaxOrthogonal == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.NOT_FOUND,
					"exception.axis.series.link.broken", //$NON-NLS-1$
					new Object[] { seOrthogonal }, Messages.getResourceBundle(rtc.getULocale()));
		}
		final OneAxis oaxBase = aax.getPrimaryBase();
		final SeriesDefinition sdBase = oaxBase.getModelAxis().getSeriesDefinitions().get(0);

		final OneAxis oaxAncillaryBase = aax.getAncillaryBase();
		final AutoScale scBase = oaxBase.getScale();
		final AutoScale scOrthogonal = oaxOrthogonal.getScale();
		final AutoScale scAncillary = oaxAncillaryBase.getScale();
		final int iXTickCount = scBase.getTickCount();
		final int iZTickCount = scAncillary.getTickCount();
		final double dXUnitSize = scBase.getUnitSize();
		final double dZUnitSize = scAncillary.getUnitSize();
		final boolean bZCategoryTextStyle = scAncillary.isCategoryScale() || scAncillary.getType() == IConstants.TEXT;

		AxisTickCoordinates daXTickCoordinates = scBase.getTickCordinates();
		AxisTickCoordinates daZTickCoordinates = scAncillary.getTickCordinates();
		Object oDataBase = null;
		DataSetIterator dsiDataBase = scBase.getData();
		Object oDataOrthogonal;
		DataSetIterator dsiDataOrthogonal = getTypedDataSet(seOrthogonal, oaxOrthogonal.getScale().getType());
		DataSetIterator dsiDataAncillary = scAncillary.getData();
		double dOrthogonalZero = 0;
		if ((scOrthogonal.getType() & NUMERICAL) == NUMERICAL) {
			dOrthogonalZero = getLocation(scOrthogonal, 0);
		} else if (oaxOrthogonal.isTickBwtweenCategories()) {
			dOrthogonalZero = scOrthogonal.getStart();
		} else {
			dOrthogonalZero = scOrthogonal.getStart() + scOrthogonal.getStartShift();
		}

		double dAncillaryZero = 0;
		if ((scAncillary.getType() & NUMERICAL) == NUMERICAL) {
			dAncillaryZero = getLocation(scAncillary, 0);
		} else if (oaxAncillaryBase.isTickBwtweenCategories()) {
			dAncillaryZero = scAncillary.getStart();
		} else {
			dAncillaryZero = scAncillary.getStart() + scAncillary.getStartShift();
		}

		double dX = 0, dY = 0, dZ = 0, dXLength = 0, dZLength = 0;
		Location3D lo3d;

		final int iBaseCount = dsiDataBase.size();
		final int iOrthogonalCount = dsiDataOrthogonal.size();
		DataPointHints[] dpa = null;

		int seriesIndex = 0;
		Series[] rss = oaxOrthogonal.getModelAxis().getRuntimeSeries();
		for (int i = 0; i < rss.length; i++) {
			if (rss[i] == seOrthogonal) {
				seriesIndex = i;
				break;
			}
		}

		Object oDataAncillary = null;
		for (int i = 0; i < seriesIndex; i++) {
			if (dsiDataAncillary.hasNext()) {
				dsiDataAncillary.next();
			}
		}
		if (dsiDataAncillary.hasNext()) {
			oDataAncillary = dsiDataAncillary.next();
		}

		if (iBaseCount != iOrthogonalCount) // DO NOT COMPUTE DATA POINT
		// HINTS
		// FOR OUT-OF-SYNC DATA
		{
			logger.log(ILogger.INFORMATION, Messages.getString("exception.base.orthogonal.inconsistent.count", //$NON-NLS-1$
					new Object[] { Integer.valueOf(iBaseCount), Integer.valueOf(iOrthogonalCount) }, rtc.getULocale()));
		} else {
			dpa = new DataPointHints[iBaseCount];
			final boolean bScatter = (oaxBase.getScale().getType() != IConstants.TEXT && !oaxBase.isCategoryScale());

			FormatSpecifier fsSDAncillary = null;

			if (oaxAncillaryBase.getModelAxis().getSeriesDefinitions().size() > 0) {
				fsSDAncillary = oaxAncillaryBase.getModelAxis().getSeriesDefinitions().get(0).getFormatSpecifier();
			}

			// OPTIMIZED PRE-FETCH FORMAT SPECIFIERS FOR ALL DATA POINTS
			final DataPoint dp = seOrthogonal.getDataPoint();
			final EList<DataPointComponent> el = dp.getComponents();
			DataPointComponent dpc;
			DataPointComponentType dpct;
			FormatSpecifier fsBase = null, fsOrthogonal = null, fsSeries = null, fsPercentile = null;
			for (int i = 0; i < el.size(); i++) {
				dpc = el.get(i);
				dpct = dpc.getType();
				if (dpct == DataPointComponentType.BASE_VALUE_LITERAL) {
					fsBase = dpc.getFormatSpecifier();
					if (fsBase == null) // BACKUP
					{
						fsBase = sdBase.getFormatSpecifier();
					}
				} else if (dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL) {
					fsOrthogonal = dpc.getFormatSpecifier();
					if (fsOrthogonal == null && seOrthogonal.eContainer() instanceof SeriesDefinition) {
						fsOrthogonal = ((SeriesDefinition) seOrthogonal.eContainer()).getFormatSpecifier();
					}
				} else if (dpct == DataPointComponentType.SERIES_VALUE_LITERAL) {
					fsSeries = dpc.getFormatSpecifier();
					if (fsSeries == null) {
						fsSeries = fsSDAncillary;
					}
				} else if (dpct == DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL) {
					fsPercentile = dpc.getFormatSpecifier();
				}
			}

			dsiDataBase.reset();
			dsiDataOrthogonal.reset();

			UserDataSetHints udsh = new UserDataSetHints(seOrthogonal.getDataSets(), getModel().isReverseCategory());
			udsh.reset();

			double total = 0;
			boolean isZeroValue = true;

			// get total orthogonal value.
			for (int i = 0; i < iOrthogonalCount; i++) {
				Object v = dsiDataOrthogonal.next();

				if (v instanceof Number) {
					if (((Number) v).doubleValue() != 0.0) {
						isZeroValue = false;
					}
					total += ((Number) v).doubleValue();
				} else if (v instanceof NumberDataElement) {
					if (((NumberDataElement) v).getValue() != 0.0) {
						isZeroValue = false;
					}
					total += ((NumberDataElement) v).getValue();
				}
			}

			dsiDataOrthogonal.reset();

			for (int i = 0; i < iBaseCount; i++) {
				oDataBase = dsiDataBase.next();
				oDataOrthogonal = dsiDataOrthogonal.next();

				if (!bScatter) {
					if (aax.areAxesSwapped()) {
						dY = daXTickCoordinates.getStart() + dXUnitSize * i;

						if (bZCategoryTextStyle) {
							dZ = daZTickCoordinates.getEnd() - dZUnitSize * (seriesIndex + 1);
							if (!scAncillary.isTickBetweenCategories()) {
								dZ -= dZUnitSize / 2;
							}
						} else {
							try {
								dZ = getLocation(scAncillary, oDataAncillary);
							} catch (IllegalArgumentException e) {
								dZ = dAncillaryZero;
							} catch (ChartException e) {
								dZ = dAncillaryZero;
							}
						}

						try {
							dX = getLocation(scOrthogonal, oDataOrthogonal);
						} catch (IllegalArgumentException nvex) {
							dX = dOrthogonalZero;
							// dX = Double.NaN;
						} catch (ChartException dfex) {
							dX = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
						}
					} else {
						dX = daXTickCoordinates.getStart() + dXUnitSize * i;
						if (!oaxBase.isTickBwtweenCategories()) {
							dX += dXUnitSize / 2;
						}
						if (bZCategoryTextStyle) {
							dZ = daZTickCoordinates.getEnd() - dZUnitSize * (seriesIndex + 1);
							if (!scAncillary.isTickBetweenCategories()) {
								dZ -= dZUnitSize / 2;
							}
						} else {
							try {
								dZ = getLocation(scAncillary, oDataAncillary);
							} catch (IllegalArgumentException e) {
								dZ = dAncillaryZero;
							} catch (ChartException e) {
								dZ = dAncillaryZero;
							}
						}

						try {
							dY = getLocation(scOrthogonal, oDataOrthogonal);
						} catch (IllegalArgumentException nvex) {
							dY = dOrthogonalZero;
							// dY = Double.NaN;
						} catch (ChartException dfex) {
							dY = dOrthogonalZero; // FOR CUSTOM DATA ELEMENTS
						}
					}
				} else {
					// Do not support scatter for 3D chart.
					throw new ChartException(ChartEnginePlugin.ID, ChartException.COMPUTATION,
							"exception.scatter.3D.not.supported", //$NON-NLS-1$
							Messages.getResourceBundle(rtc.getULocale()));
				}

				lo3d = goFactory.createLocation3D(dX, dY, dZ);
				dXLength = (i < iXTickCount - 1)
						? daXTickCoordinates.getCoordinate(i + 1) - daXTickCoordinates.getCoordinate(i)
						: 0;
				dZLength = (seriesIndex < iZTickCount - 1) ? daZTickCoordinates.getCoordinate(seriesIndex + 1)
						- daZTickCoordinates.getCoordinate(seriesIndex) : 0;

				Object percentileValue = null;

				if (total != 0) {
					if (oDataOrthogonal instanceof Number) {
						percentileValue = new Double(((Number) oDataOrthogonal).doubleValue() / total);
					} else if (oDataOrthogonal instanceof NumberDataElement) {
						percentileValue = new Double(((NumberDataElement) oDataOrthogonal).getValue() / total);
					}
				} else if (isZeroValue == true) {
					percentileValue = new Double(1d / iOrthogonalCount);
				}

				dpa[i] = new DataPointHints(oDataBase, oDataOrthogonal, seOrthogonal.getSeriesIdentifier(),
						percentileValue, seOrthogonal.getDataPoint(), fsBase, fsOrthogonal, fsSeries, fsPercentile, i,
						lo3d, new double[] { dXLength, dZLength }, rtc);

				udsh.next(dpa[i]);
			}
		}

		return new SeriesRenderingHints3D(this, oaxBase.getAxisCoordinate(), oaxAncillaryBase.getAxisCoordinate(),
				scOrthogonal.getStart(), dOrthogonalZero, scOrthogonal.getEnd() - scOrthogonal.getStart(),
				daXTickCoordinates, daZTickCoordinates, dpa, scBase, scOrthogonal, scAncillary, dsiDataBase,
				dsiDataOrthogonal, dsiDataAncillary);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.computation.withaxes.PlotWithAxes#buildAxes()
	 */
	void buildAxes() throws IllegalArgumentException, ChartException {
		final Axis[] axa = getModel().getPrimaryBaseAxes();
		final Axis axPrimaryBase = axa[0]; // NOTE: FOR REL 1 AXIS RENDERS, WE
		// SUPPORT A SINGLE PRIMARY BASE AXIS
		// ONLY
		validateAxis(axPrimaryBase);

		final Axis axPrimaryOrthogonal = getModel().getPrimaryOrthogonalAxis(axPrimaryBase);
		validateAxis(axPrimaryOrthogonal);

		final Axis axAncillaryBase = getModel().getAncillaryBaseAxis(axPrimaryBase);
		validateAxis(axAncillaryBase);

		if (axPrimaryBase.getAssociatedAxes().size() > 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.COMPUTATION,
					"exception.multi.orthogonal.3D.not.supported", //$NON-NLS-1$
					Messages.getResourceBundle(rtc.getULocale()));
		}

		aax = new AllAxes(null);
		insCA = aax.getInsets();

		aax.swapAxes(getModel().isTransposed());

		// SETUP THE PRIMARY BASE-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxPrimaryBase = new OneAxis(axPrimaryBase, IConstants.BASE_AXIS);
		oaxPrimaryBase.set(getOrientation(IConstants.BASE),
				transposeLabelPosition(IConstants.BASE, getLabelPosition(axPrimaryBase.getLabelPosition())),
				transposeLabelPosition(IConstants.BASE, getLabelPosition(axPrimaryBase.getTitlePosition())),
				axPrimaryBase.isCategoryAxis(), axPrimaryBase.getScale().isTickBetweenCategories());
		oaxPrimaryBase.setGridProperties(axPrimaryBase.getMajorGrid().getLineAttributes(),
				axPrimaryBase.getMinorGrid().getLineAttributes(), axPrimaryBase.getMajorGrid().getTickAttributes(),
				axPrimaryBase.getMinorGrid().getTickAttributes(),
				transposeTickStyle(IConstants.BASE, getTickStyle(axPrimaryBase, MAJOR)),
				transposeTickStyle(IConstants.BASE, getTickStyle(axPrimaryBase, MINOR)),
				axPrimaryBase.getScale().getMinorGridsPerUnit());

		oaxPrimaryBase.set(axPrimaryBase.getLabel(), axPrimaryBase.getTitle());
		oaxPrimaryBase.set(getIntersection(axPrimaryBase));
		oaxPrimaryBase.set(axPrimaryBase.getLineAttributes());
		aax.definePrimary(oaxPrimaryBase); // ADD TO AXIS SET

		// SETUP THE PRIMARY ORTHOGONAL-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxPrimaryOrthogonal = new OneAxis(axPrimaryOrthogonal, IConstants.ORTHOGONAL_AXIS);
		oaxPrimaryOrthogonal.set(getOrientation(IConstants.ORTHOGONAL),
				transposeLabelPosition(IConstants.ORTHOGONAL, getLabelPosition(axPrimaryOrthogonal.getLabelPosition())),
				transposeLabelPosition(IConstants.ORTHOGONAL, getLabelPosition(axPrimaryOrthogonal.getTitlePosition())),
				axPrimaryOrthogonal.isCategoryAxis(), axPrimaryOrthogonal.getScale().isTickBetweenCategories());
		oaxPrimaryOrthogonal.setGridProperties(axPrimaryOrthogonal.getMajorGrid().getLineAttributes(),
				axPrimaryOrthogonal.getMinorGrid().getLineAttributes(),
				axPrimaryOrthogonal.getMajorGrid().getTickAttributes(),
				axPrimaryOrthogonal.getMinorGrid().getTickAttributes(),
				transposeTickStyle(IConstants.ORTHOGONAL, getTickStyle(axPrimaryOrthogonal, MAJOR)),
				transposeTickStyle(IConstants.ORTHOGONAL, getTickStyle(axPrimaryOrthogonal, MINOR)),
				axPrimaryOrthogonal.getScale().getMinorGridsPerUnit());

		oaxPrimaryOrthogonal.set(axPrimaryOrthogonal.getLabel(), axPrimaryOrthogonal.getTitle());
		oaxPrimaryOrthogonal.set(getIntersection(axPrimaryOrthogonal));
		oaxPrimaryOrthogonal.set(axPrimaryOrthogonal.getLineAttributes());
		aax.definePrimary(oaxPrimaryOrthogonal); // ADD TO AXIS SET

		// SETUP THE ANCILLARY BASE-AXIS PROPERTIES AND ITS SCALE
		final OneAxis oaxAncillaryBase = new OneAxis(axAncillaryBase, IConstants.ANCILLARY_AXIS);
		oaxAncillaryBase.set(IConstants.HORIZONTAL, getLabelPosition(axAncillaryBase.getLabelPosition()),
				getLabelPosition(axAncillaryBase.getTitlePosition()), axAncillaryBase.isCategoryAxis(),
				axAncillaryBase.getScale().isTickBetweenCategories());
		oaxAncillaryBase.setGridProperties(axAncillaryBase.getMajorGrid().getLineAttributes(),
				axAncillaryBase.getMinorGrid().getLineAttributes(), axAncillaryBase.getMajorGrid().getTickAttributes(),
				axAncillaryBase.getMinorGrid().getTickAttributes(), getTickStyle(axAncillaryBase, MAJOR),
				getTickStyle(axAncillaryBase, MINOR), axAncillaryBase.getScale().getMinorGridsPerUnit());
		oaxAncillaryBase.set(axAncillaryBase.getLabel(), axAncillaryBase.getTitle()); // ASSOCIATE FONT, ETC.

		oaxAncillaryBase.set(getIntersection(axAncillaryBase));
		oaxAncillaryBase.set(axAncillaryBase.getLineAttributes());
		aax.defineAncillaryBase(oaxAncillaryBase); // ADD TO AXIS SET
	}

	private Location3D[] get3DGraphicVeteces() {
		Location3D[] loa = new Location3D[8];

		AutoScale scPrimaryBase = aax.getPrimaryBase().getScale();
		AutoScale scPrimaryOrthogonal = aax.getPrimaryOrthogonal().getScale();
		AutoScale scAncillaryBase = aax.getAncillaryBase().getScale();

		double x0 = scPrimaryBase.getStart();
		double y0 = scPrimaryOrthogonal.getStart();
		double z0 = scAncillaryBase.getStart();

		double x1 = scPrimaryBase.getEnd();
		double y1 = scPrimaryOrthogonal.getEnd();
		double z1 = scAncillaryBase.getEnd();

		loa[0] = goFactory.createLocation3D(x0, y0, z0);
		loa[1] = goFactory.createLocation3D(x0, y0, z1);
		loa[2] = goFactory.createLocation3D(x0, y1, z0);
		loa[3] = goFactory.createLocation3D(x0, y1, z1);
		loa[4] = goFactory.createLocation3D(x1, y0, z0);
		loa[5] = goFactory.createLocation3D(x1, y0, z1);
		loa[6] = goFactory.createLocation3D(x1, y1, z0);
		loa[7] = goFactory.createLocation3D(x1, y1, z1);

		return loa;
	}

	public Rectangle get3DGraphicBoudingRect() throws ChartException {
		Text3DRenderEvent event = new Text3DRenderEvent(this);
		Location3D[] loa3D = get3DGraphicVeteces();
		Location loOff = this.getPanningOffset();

		double x_min = Double.MAX_VALUE;
		double x_max = -Double.MAX_VALUE;
		double y_min = Double.MAX_VALUE;
		double y_max = -Double.MAX_VALUE;

		for (int i = 0; i < loa3D.length; i++) {
			event.setLocation3D(loa3D[i]);
			engine.processEvent_noclip(event, loOff.getX(), loOff.getY());
			Location lo = event.getLocation();
			x_min = Math.min(x_min, lo.getX());
			x_max = Math.max(x_max, lo.getX());
			y_min = Math.min(y_min, lo.getY());
			y_max = Math.max(y_max, lo.getY());
		}

		return new Rectangle(x_min, y_min, x_max - x_min, y_max - y_min);
	}

	public Rectangle getAxisLabelBoundingRectXZ(OneAxis oax) throws ChartException {
		AxisLabelCanvasLocationProvider lProvider = new HAxisLabelCanvasLocationProvider(getPWA3D(), oax);
		AutoScale sc = oax.getScale();
		boolean bTextAxis = (sc.getType() & IConstants.TEXT) == IConstants.TEXT || sc.isCategoryScale();
		AxisTickCoordinates da = oax.getScale().getTickCordinates();
		final int length = bTextAxis ? da.size() - 1 : da.size();
		int iLabelLocation = oax.getLabelPosition();
		Label la = goFactory.copyOf(oax.getLabel());
		AxisLabelTextProvider textProvider = AxisLabelTextProvider.create(oax);

		Rectangle rect = null;

		for (int i = 0; i < length; i++) {
			if (sc.isTickLabelVisible(i)) {
				Location[] lo = lProvider.getLocation(i);
				String str = textProvider.getLabelText(i);
				la.getCaption().setValue(str);
				BoundingBox bb = cComp.computeBox(ids, iLabelLocation, la, lo[0].getX(), lo[0].getY());

				if (rect == null) {
					rect = new Rectangle(bb);
				} else {
					rect.union(new Rectangle(bb));
				}
			}

		}

		return rect;
	}

	public Rectangle[] getAxisLabelBoundingRectY(OneAxis oax) throws ChartException {
		AxisLabelCanvasLocationProvider lProvider = new VAxisLabelCanvasLocationProvider(getPWA3D(), oax);
		AutoScale sc = oax.getScale();
		boolean bTextAxis = (sc.getType() & IConstants.TEXT) == IConstants.TEXT || sc.isCategoryScale();
		AxisTickCoordinates da = oax.getScale().getTickCordinates();
		final int length = bTextAxis ? da.size() - 1 : da.size();
		Label la = goFactory.copyOf(oax.getLabel());

		AxisLabelTextProvider textProvider = AxisLabelTextProvider.create(oax);

		Rectangle[] rect = { null, null };

		for (int i = 0; i < length; i++) {
			if (sc.isTickLabelVisible(i)) {
				Location[] lo = lProvider.getLocation(i);
				String str = textProvider.getLabelText(i);
				la.getCaption().setValue(str);
				BoundingBox bb0 = cComp.computeBox(ids, TextRenderEvent.LEFT, la, lo[0].getX(), lo[0].getY());
				BoundingBox bb1 = cComp.computeBox(ids, TextRenderEvent.RIGHT, la, lo[1].getX(), lo[1].getY());

				if (rect[0] == null) {
					rect[0] = new Rectangle(bb0);
					rect[1] = new Rectangle(bb1);
				} else {
					rect[0].union(new Rectangle(bb0));
					rect[1].union(new Rectangle(bb1));
				}

			}
		}

		return rect;
	}

	public Rectangle getBoundsOfAllAxisLabels() throws ChartException {
		Rectangle rect_x = getAxisLabelBoundingRectXZ(aax.getPrimaryBase());
		Rectangle rect_z = getAxisLabelBoundingRectXZ(aax.getAncillaryBase());
		Rectangle rect = Rectangle.union(rect_x, rect_z);

		Rectangle[] rect_y = getAxisLabelBoundingRectY(aax.getPrimaryOrthogonal());
		rect = Rectangle.union(rect, rect_y[0]);
		rect = Rectangle.union(rect, rect_y[1]);

		return rect;
	}

	// for debug use
	public Rectangle[] getAllAxisLabelBounds() throws ChartException {
		Rectangle[] rects = new Rectangle[4];
		rects[0] = getAxisLabelBoundingRectXZ(aax.getPrimaryBase());
		rects[1] = getAxisLabelBoundingRectXZ(aax.getAncillaryBase());
		Rectangle[] rect_y = getAxisLabelBoundingRectY(aax.getPrimaryOrthogonal());
		rects[2] = rect_y[0];
		rects[3] = rect_y[1];

		return rects;
	}

	private abstract static class AxisLabelCanvasLocationProvider {
		protected PlotWith3DAxes pwa3D;
		protected Engine3D engine;
		protected OneAxis oax;
		protected AutoScale sc;
		protected AxisTickCoordinates da;
		// protected int length;
		protected int iMajorTickStyle;
		protected double dXEnd, dZEnd;
		protected double dTick1, dTick2;

		// Offset for Text axis type
		protected double dOffset;
		protected Location loOff;

		protected Text3DRenderEvent event = new Text3DRenderEvent(this);
		protected Location3D lo3d = goFactory.createLocation3D(0, 0, 0);

		// public int getLength( )
		// {
		// return length;
		// }

		protected AxisLabelCanvasLocationProvider(PlotWith3DAxes pwa3D, OneAxis oax) throws ChartException {
			this.pwa3D = pwa3D;
			this.engine = pwa3D.get3DEngine();
			this.oax = oax;
			this.sc = oax.getScale();
			this.da = sc.getTickCordinates();
			this.iMajorTickStyle = oax.getGrid().getTickStyle(IConstants.MAJOR);

			boolean bTextAxis = (sc.getType() & IConstants.TEXT) == IConstants.TEXT || sc.isCategoryScale();
			// length = bTextAxis ? da.size( ) - 1 : da.size( );
			boolean bTickBetweenCategories = oax.getModelAxis().getScale().isTickBetweenCategories();
			int iDirection = sc.getDirection() != IConstants.FORWARD ? -1 : 1;

			this.dOffset = bTextAxis && bTickBetweenCategories ? iDirection * sc.getUnitSize() / 2 : 0;
			this.loOff = pwa3D.getPanningOffset();

			dXEnd = pwa3D.getAxes().getPrimaryBase().getScale().getEnd();
			dZEnd = pwa3D.getAxes().getAncillaryBase().getScale().getEnd();
		}

		public abstract Location[] getLocation(int index);

	}

	private static class HAxisLabelCanvasLocationProvider extends AxisLabelCanvasLocationProvider {
		private double y;

		public HAxisLabelCanvasLocationProvider(PlotWith3DAxes pwa3D, OneAxis oax) throws ChartException {
			super(pwa3D, oax);

			double y_ax = oax.getAxisCoordinate3D().getY();
			dTick1 = ((iMajorTickStyle & IConstants.TICK_ABOVE) == IConstants.TICK_ABOVE) ? y_ax + pwa3D.getTickSize()
					: y_ax;
			dTick2 = ((iMajorTickStyle & IConstants.TICK_BELOW) == IConstants.TICK_BELOW) ? y_ax - pwa3D.getTickSize()
					: y_ax;
			y = (oax.getLabelPosition() == IConstants.ABOVE) ? dTick1 + 1 : dTick2 - 1;
		}

		public Location[] getLocation(int index) {
			Location[] los = new Location[1];

			if (oax.getAxisType() == IConstants.BASE_AXIS) {
				int x = (int) da.getCoordinate(index);

				lo3d.set(x + dOffset, y - pwa3D.getVerticalSpacingInPixels(),
						dZEnd + pwa3D.getVerticalSpacingInPixels());
			} else {
				int z = (int) da.getCoordinate(index);

				lo3d.set(dXEnd + pwa3D.getVerticalSpacingInPixels(), y - pwa3D.getVerticalSpacingInPixels(),
						z + dOffset);
			}
			event.setLocation3D(lo3d);

			engine.processEvent_noclip(event, loOff.getX(), loOff.getY());
			los[0] = event.getLocation();

			return los;
		}
	}

	private static class VAxisLabelCanvasLocationProvider extends AxisLabelCanvasLocationProvider {
		private double x;
		private double z_ax;

		public VAxisLabelCanvasLocationProvider(PlotWith3DAxes pwa3D, OneAxis oax) throws ChartException {
			super(pwa3D, oax);

			z_ax = oax.getAxisCoordinate3D().getZ();

			double x_ax = oax.getAxisCoordinate3D().getX();
			dTick1 = ((iMajorTickStyle & IConstants.TICK_LEFT) == IConstants.TICK_LEFT) ? x_ax - pwa3D.getTickSize()
					: x_ax;
			dTick2 = ((iMajorTickStyle & IConstants.TICK_RIGHT) == IConstants.TICK_RIGHT) ? x_ax + pwa3D.getTickSize()
					: x_ax;

			x = (oax.getLabelPosition() == IConstants.LEFT) ? dTick1 - 1 : dTick2 + 1;
		}

		public Location[] getLocation(int index) {
			int y = (int) da.getCoordinate(index);
			double sx = x;
			double sx2 = dXEnd;

			Location[] los = { null, null };

			// Left wall
			lo3d.set(sx - pwa3D.getHorizontalSpacingInPixels(), y + dOffset,
					dZEnd + pwa3D.getHorizontalSpacingInPixels());

			event.setLocation3D(lo3d);

			engine.processEvent_noclip(event, loOff.getX(), loOff.getY());
			los[0] = event.getLocation();

			// Right wall
			lo3d.set(sx2 + pwa3D.getHorizontalSpacingInPixels(), y + dOffset,
					z_ax - pwa3D.getHorizontalSpacingInPixels());

			event.setLocation3D(lo3d);

			engine.processEvent_noclip(event, loOff.getX(), loOff.getY());
			los[1] = event.getLocation();

			return los;

		}
	}

}
