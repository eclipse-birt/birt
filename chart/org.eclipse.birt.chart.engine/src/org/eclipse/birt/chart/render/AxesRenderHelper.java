/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.render;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.computation.LabelLimiter;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.computation.RotatedRectangle;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withaxes.AllAxes;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.AxisTickCoordinates;
import org.eclipse.birt.chart.computation.withaxes.IntersectionValue;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.PlotWith3DAxes;
import org.eclipse.birt.chart.computation.withaxes.PlotWithAxes;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.internal.factory.DateFormatWrapperFactory;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.NumberUtil;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DecimalFormat;

/**
 * Helper class for AxesRenderer. By providing interface and inner classes, to
 * refactor the complicated method in AxesRenderer.
 */

public final class AxesRenderHelper {

	static final ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/render"); //$NON-NLS-1$

	protected final static IGObjectFactory goFactory = GObjectFactory.instance();

	AxesRenderer renderer;
	OneAxis ax;
	int iWhatToDraw;
	IPrimitiveRenderer ipr;

	private Axis axModel;
	private PlotWithAxes pwa;
	private IChartComputation cComp;
	private Insets insCA;
	private AbstractScriptHandler sh;
	private double dLocation;
	private AutoScale sc;
	private IntersectionValue iv;
	private int iMajorTickStyle;
	private int iMinorTickStyle;
	private int iLabelLocation;
	private int iOrientation;
	private IDisplayServer xs;
	private Label la;

	private double[] daEndPoints;
	private AxisTickCoordinates da;
	private double[] daMinor;
	private String sText;

	private int iDimension;
	private double dSeriesThickness;
	private NumberDataElement nde;
	private boolean bAxisLabelStaggered;

	private DecimalFormat df;
	private LineAttributes lia;
	private LineAttributes liaMajorTick;
	private LineAttributes liaMinorTick;

	private boolean bRenderAxisLabels;
	private boolean bRenderAxisTitle;
	private Location lo;

	private TransformationEvent trae;
	private TextRenderEvent tre;
	private LineRenderEvent lre;

	// Prepare 3D rendering variables.
	private boolean bRendering3D;
	private boolean bRenderOrthogonal3DAxis;
	private boolean bRenderBase3DAxis;
	private boolean bRenderAncillary3DAxis;

	private DeferredCache dc;
	private int axisType;
	private Location panningOffset;
	private boolean bTransposed;

	private double[] daEndPoints3D;
	private AxisTickCoordinates da3D;
	private Location3D lo3d;
	private Text3DRenderEvent t3dre;
	private Line3DRenderEvent l3dre;

	private boolean bTickBetweenCategories;
	private boolean bLabelWithinAxes;

	AxesRenderHelper(AxesRenderer renderer, IPrimitiveRenderer ipr, Plot pl, OneAxis ax, int iWhatToDraw)
			throws ChartException {
		init(renderer, ipr, pl, ax, iWhatToDraw);
	}

	private void init(AxesRenderer renderer, IPrimitiveRenderer ipr, Plot pl, OneAxis ax, int iWhatToDraw)
			throws ChartException {
		this.renderer = renderer;
		this.ax = ax;
		this.iWhatToDraw = iWhatToDraw;
		this.ipr = ipr;

		axModel = ax.getModelAxis();
		pwa = (PlotWithAxes) renderer.getComputations();
		cComp = pwa.getChartComputation();
		insCA = pwa.getAxes().getInsets();
		sh = getRunTimeContext().getScriptHandler();
		dLocation = ax.getAxisCoordinate();
		sc = ax.getScale();
		iv = ax.getIntersectionValue();
		iMajorTickStyle = ax.getGrid().getTickStyle(IConstants.MAJOR);
		iMinorTickStyle = ax.getGrid().getTickStyle(IConstants.MINOR);
		iLabelLocation = ax.getLabelPosition();
		iOrientation = ax.getOrientation();
		xs = renderer.getDevice().getDisplayServer();
		la = goFactory.copyOf(ax.getLabel());

		daEndPoints = sc.getEndPoints();
		da = sc.getTickCordinates();
		daMinor = sc.getMinorCoordinates(ax.getGrid().getMinorCountPerMajor());
		sText = null;

		iDimension = pwa.getDimension();
		nde = NumberDataElementImpl.create(0);
		dSeriesThickness = pwa.getSeriesThickness();
		bAxisLabelStaggered = sc.isAxisLabelStaggered();

		df = null;
		lia = ax.getLineAttributes();
		liaMajorTick = ax.getGrid().getTickAttributes(IConstants.MAJOR);
		liaMinorTick = ax.getGrid().getTickAttributes(IConstants.MINOR);

		bRenderAxisLabels = ax.isShowLabels()
				&& ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS && la.isVisible());
		bRenderAxisTitle = ((iWhatToDraw & IConstants.LABELS) == IConstants.LABELS);
		lo = goFactory.createLocation(0, 0);

		trae = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel), TransformationEvent.class);
		tre = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel), TextRenderEvent.class);
		lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel), LineRenderEvent.class);

		// Prepare 3D rendering variables.
		bRendering3D = iDimension == IConstants.THREE_D;
		bRenderOrthogonal3DAxis = (iWhatToDraw & IConstants.ORTHOGONAL_AXIS) == IConstants.ORTHOGONAL_AXIS
				&& bRendering3D;
		bRenderBase3DAxis = (iWhatToDraw & IConstants.BASE_AXIS) == IConstants.BASE_AXIS && bRendering3D;
		bRenderAncillary3DAxis = (iWhatToDraw & IConstants.ANCILLARY_AXIS) == IConstants.ANCILLARY_AXIS && bRendering3D;

		dc = renderer.getDeferredCache();
		axisType = ax.getAxisType();
		panningOffset = renderer.getPanningOffset();
		bTransposed = renderer.isTransposed();

		daEndPoints3D = null;
		da3D = null;
		lo3d = null;
		t3dre = null;
		l3dre = null;

		bTickBetweenCategories = ax.isTickBwtweenCategories();
		bLabelWithinAxes = ax.getModelAxis().isLabelWithinAxes();
	}

	private RunTimeContext getRunTimeContext() {
		return renderer.getRunTimeContext();
	}

	private void processTrigger(Trigger tg, StructureSource source) {
		renderer.processTrigger(tg, source);
	}

	private final static class ComputationContext {

		// General
		boolean isVertical;
		double dTick1, dTick2;

		// Vertical
		double dX;
		double y3d;

		// Horizontal
		double dY;
		double x3d, z3d;

		public ComputationContext(boolean isVertical) {
			this.isVertical = isVertical;

		}
	}

	/** Interface for abstract method of different axes types */
	interface IAxisTypeComputation {

		/**
		 * Initializes when rendering an axes
		 * 
		 * @throws ChartException
		 */
		void initialize() throws ChartException;

		/**
		 * Last method of rendering an axes
		 * 
		 * @throws ChartException
		 */
		void close() throws ChartException;

		/**
		 * Handles computation before rendering each axes tick
		 * 
		 * @param i tick index
		 * @throws ChartException
		 */
		void handlePreEachTick(int i) throws ChartException;

		/**
		 * Handles computation after rendering each axes tick
		 * 
		 * @param i tick index
		 * @throws ChartException
		 */
		void handlePostEachTick(int i) throws ChartException;
	}

	private final class TextAxisTypeComputation implements IAxisTypeComputation {

		ComputationContext context;

		TextAxisTypeComputation(ComputationContext context) {
			this.context = context;
		}

		public void initialize() throws ChartException {
			sc.getData().reset();
		}

		public void close() throws ChartException {
			try {
				// ONE LAST TICK
				if (bTickBetweenCategories) {
					if (context.isVertical) {
						int y = (int) da.getEnd();
						if (bRendering3D) {
							context.y3d = (int) da3D.getEnd();
						}
						if (!ChartUtil.mathEqual(context.dTick1, context.dTick2)) {
							if (bRenderOrthogonal3DAxis) {
								// !NOT RENDER TICKS FOR 3D AXES
								// l3dre.setLineAttributes( liaMajorTick );
								// l3dre.setStart3D( dXTick1, y3d, dZ );
								// l3dre.setEnd3D( dXTick2, y3d, dZ );
								// dc.addLine( l3dre );
							} else {
								lre.setLineAttributes(liaMajorTick);
								lre.getStart().set(context.dTick1, y);
								lre.getEnd().set(context.dTick2, y);
								ipr.drawLine(lre);
							}

							if (iv != null && iDimension == IConstants.TWO_5_D && iv.getType() == IConstants.VALUE) {
								lre.setStart(goFactory.createLocation(context.dX, y));
								lre.setEnd(
										goFactory.createLocation(context.dX + dSeriesThickness, y - dSeriesThickness));
								ipr.drawLine(lre);
							}
						}
					} else {
						int x = (int) da.getEnd();
						if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS) {
							if (!ChartUtil.mathEqual(context.dTick1, context.dTick2)) {
								if (bRenderBase3DAxis) {
									// !NOT RENDER TICKS FOR 3D AXES
									// l3dre.setLineAttributes( liaMajorTick );
									// l3dre.setStart3D( x3d, dYTick1, dZ );
									// l3dre.setEnd3D( x3d, dYTick2, dZ );
									// dc.addLine( l3dre );
								} else if (bRenderAncillary3DAxis) {
									// !NOT RENDER TICKS FOR 3D AXES
									// l3dre.setLineAttributes( liaMajorTick );
									// l3dre.setStart3D( dX, dYTick1, z3d );
									// l3dre.setEnd3D( dX, dYTick2, z3d );
									// dc.addLine( l3dre );
								} else {
									lre.setLineAttributes(liaMajorTick);
									lre.getStart().set(x, context.dTick1);
									lre.getEnd().set(x, context.dTick2);
									ipr.drawLine(lre);
								}

								if (iv != null && iDimension == IConstants.TWO_5_D
										&& iv.getType() == IConstants.VALUE) {
									lre.getStart().set(x, context.dY);
									lre.getEnd().set(x + dSeriesThickness, context.dY - dSeriesThickness);
									ipr.drawLine(lre);
								}
							}
						}
					}
				}

			} finally {
				// itmText.dispose( ); // DISPOSED
			}
		}

		public void handlePostEachTick(int i) throws ChartException {
			// TODO Auto-generated method stub

		}

		public void handlePreEachTick(int i) throws ChartException {
			if (bRenderAxisLabels) {
				if (!bTickBetweenCategories && i == 0) {
					la.getCaption().setValue(""); //$NON-NLS-1$
				} else {
					la.getCaption().setValue(sc.getComputedLabelText(i));
				}
			}

		}

	}

	private final class LinearAxisTypeComputation implements IAxisTypeComputation {

		double dAxisValue;
		double dAxisStep;
		BigDecimal bdAxisValue;
		BigDecimal bdAxisStep;

		public void close() throws ChartException {
			// TODO Auto-generated method stub

		}

		public void handlePostEachTick(int i) throws ChartException {
			if (i == da.size() - 2 && !sc.isSetFactor()) {
				// This is the last tick, use pre-computed value to
				// handle non-equal scale unit case.
				dAxisValue = Methods.asDouble(sc.getMaximum()).doubleValue();
			} else {
				dAxisValue += dAxisStep;
			}

			if (sc.isBigNumber()) {
				bdAxisValue = BigDecimal.valueOf(dAxisValue).multiply(sc.getBigNumberDivisor(),
						NumberUtil.DEFAULT_MATHCONTEXT);
			}

		}

		public void handlePreEachTick(int i) throws ChartException {
			if (bRenderAxisLabels && sc.isTickLabelVisible(i)) {
				try {
					if (sc.isBigNumber()) {
						sText = ValueFormatter.format(bdAxisValue, axModel.getFormatSpecifier(),
								ax.getRunTimeContext().getULocale(), df);
					} else {
						nde.setValue(dAxisValue);
						sText = ValueFormatter.format(nde, axModel.getFormatSpecifier(),
								ax.getRunTimeContext().getULocale(), df);
					}

				} catch (ChartException dfex) {
					logger.log(dfex);
					sText = IConstants.NULL_STRING;
				}
				la.getCaption().setValue(sText);
			}
		}

		public void initialize() throws ChartException {
			dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue();
			dAxisStep = Methods.asDouble(sc.getStep()).doubleValue();

			if (sc.isBigNumber()) {
				bdAxisValue = BigDecimal.valueOf(dAxisValue).multiply(sc.getBigNumberDivisor(),
						NumberUtil.DEFAULT_MATHCONTEXT);
				bdAxisStep = BigDecimal.valueOf(dAxisStep);
				if (axModel.getFormatSpecifier() == null) {
					df = sc.computeDecimalFormat(bdAxisValue,
							bdAxisStep.multiply(sc.getBigNumberDivisor(), NumberUtil.DEFAULT_MATHCONTEXT));
				}
			} else {
				if (axModel.getFormatSpecifier() == null) {
					df = sc.computeDecimalFormat(dAxisValue, dAxisStep);
				}
			}
		}

	}

	private final class LogAxisTypeComputation implements IAxisTypeComputation {

		double dAxisValue;
		double dAxisStep;
		BigDecimal bdAxisValue;
		BigDecimal bdAxisStep;

		public void close() throws ChartException {
			// TODO Auto-generated method stub

		}

		public void handlePostEachTick(int i) throws ChartException {
			if (sc.isBigNumber()) {
				bdAxisValue = bdAxisValue.multiply(bdAxisStep, NumberUtil.DEFAULT_MATHCONTEXT);
			} else {
				dAxisValue *= dAxisStep;
			}
		}

		public void handlePreEachTick(int i) throws ChartException {
			// PERFORM COMPUTATIONS ONLY IF AXIS LABEL IS VISIBLE
			if (bRenderAxisLabels) {
				try {
					if (sc.isBigNumber()) {
						sText = ValueFormatter.format(bdAxisValue, axModel.getFormatSpecifier(),
								ax.getRunTimeContext().getULocale(), df);
					} else {
						nde.setValue(dAxisValue);
						sText = ValueFormatter.format(nde, axModel.getFormatSpecifier(),
								ax.getRunTimeContext().getULocale(), df);
					}
				} catch (ChartException dfex) {
					logger.log(dfex);
					sText = IConstants.NULL_STRING;
				}
				la.getCaption().setValue(sText);
			}
		}

		public void initialize() throws ChartException {
			dAxisValue = Methods.asDouble(sc.getMinimum()).doubleValue();
			dAxisStep = Methods.asDouble(sc.getStep()).doubleValue();
			if (sc.isBigNumber()) {
				bdAxisValue = BigDecimal.valueOf(dAxisValue).multiply(sc.getBigNumberDivisor(),
						NumberUtil.DEFAULT_MATHCONTEXT);
				bdAxisStep = BigDecimal.valueOf(dAxisStep);
				if (axModel.getFormatSpecifier() == null) {
					df = sc.computeDecimalFormat(bdAxisValue, bdAxisStep);
				}
			} else {
				if (axModel.getFormatSpecifier() == null) {
					df = sc.computeDecimalFormat(dAxisValue, dAxisStep);
				}
			}
		}
	}

	private final class DatetimeAxisTypeComputation implements IAxisTypeComputation {

		CDateTime cdt, cdtAxisValue;
		int iUnit, iStep;
		IDateFormatWrapper sdf;

		public void close() throws ChartException {
			// TODO Auto-generated method stub

		}

		public void handlePostEachTick(int i) throws ChartException {
			// ALWAYS W.R.T START VALUE
			if (i == da.size() - 2 && !sc.isSetFactor()) {
				// This is the last tick, use pre-computed value to
				// handle non-equal scale unit case.
				cdt = Methods.asDateTime(sc.getMaximum());
			} else {
				cdt = cdtAxisValue.forward(iUnit, iStep * (i + 1));
			}
		}

		public void handlePreEachTick(int i) throws ChartException {
			try {
				sText = ValueFormatter.format(cdt, axModel.getFormatSpecifier(), ax.getRunTimeContext().getULocale(),
						sdf);
			} catch (ChartException dfex) {
				logger.log(dfex);
				sText = IConstants.NULL_STRING;
			}
			la.getCaption().setValue(sText);
		}

		public void initialize() throws ChartException {
			cdtAxisValue = Methods.asDateTime(sc.getMinimum());
			iUnit = Methods.asInteger(sc.getUnit());
			iStep = Methods.asInteger(sc.getStep());
			if (axModel.getFormatSpecifier() == null) {
				sdf = DateFormatWrapperFactory.getPreferredDateFormat(iUnit, getRunTimeContext().getULocale());
			}
			cdt = cdtAxisValue;
		}

	}

	IAxisTypeComputation createAxisTypeComputation(ComputationContext context) throws ChartException {
		if ((sc.getType() & IConstants.TEXT) == IConstants.TEXT || sc.isCategoryScale()) {
			return new TextAxisTypeComputation(context);
		} else if ((sc.getType() & IConstants.LINEAR) == IConstants.LINEAR) {
			return new LinearAxisTypeComputation();
		} else if ((sc.getType() & IConstants.LOGARITHMIC) == IConstants.LOGARITHMIC) {
			return new LogAxisTypeComputation();
		} else if ((sc.getType() & IConstants.DATE_TIME) == IConstants.DATE_TIME) {
			return new DatetimeAxisTypeComputation();
		}
		throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, "exception.undefined.axis.type", //$NON-NLS-1$
				Messages.getResourceBundle(getRunTimeContext().getULocale()));
	}

	private List<TextRenderEvent> renderVerticalAxisTickLabels(ComputationContext context, double dXEnd, double dZEnd,
			double dZ, double dStaggeredLabelOffset, boolean deferredAxisLabel) throws ChartException {
		List<TextRenderEvent> deferfedRenderList = new ArrayList<TextRenderEvent>();

		// The vertical axis direction, -1 means bottom->top, 1 means
		// top->bottom.
		final int iDirection = sc.getDirection() != IConstants.FORWARD ? -1 : 1;
		IAxisTypeComputation computation = createAxisTypeComputation(context);
		computation.initialize();

		// Offset for Text axis type
		final double dOffset = computation instanceof TextAxisTypeComputation && bTickBetweenCategories
				? iDirection * sc.getUnitSize() / 2
				: 0;
		// Tick size
		final int length = computation instanceof TextAxisTypeComputation ? da.size() - 1 : da.size();

		final double x = (iLabelLocation == IConstants.LEFT) ? context.dTick1 - 1 : context.dTick2 + 1;

		double yLast = Integer.MIN_VALUE;
		Location loMinorStart = goFactory.createLocation(0, 0);
		Location loMinorEnd = goFactory.createLocation(0, 0);
		for (int i = 0; i < length; i++) {
			computation.handlePreEachTick(i);

			double y = da.getCoordinate(i);
			boolean bSkipTickLine = (Math.abs(yLast - y) < 1);
			if (!bSkipTickLine) {
				yLast = y;
			}

			if (bRendering3D) {
				context.y3d = (int) da3D.getCoordinate(i);
			}

			if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS) {
				double dXMinorTick1 = ((iMinorTickStyle & IConstants.TICK_LEFT) == IConstants.TICK_LEFT)
						? (context.dX - pwa.getTickSize())
						: context.dX;
				double dXMinorTick2 = ((iMinorTickStyle & IConstants.TICK_RIGHT) == IConstants.TICK_RIGHT)
						? context.dX + pwa.getTickSize()
						: context.dX;
				if (!ChartUtil.mathEqual(dXMinorTick1, dXMinorTick2)) {
					// RENDER THE MINOR TICKS FIRST (For ALL but the
					// last Major tick)
					if (i != da.size() - 1) {
						if (bRenderOrthogonal3DAxis) {
							// !NOT RENDER TICKS FOR 3D AXES
						} else {
							if (!bSkipTickLine && ((i > 0 && i < length - 1) || !axModel.isCategoryAxis()
									|| bTickBetweenCategories)) {
								LineRenderEvent lreMinor = null;
								int minorStep = (int) (1d / da.getStep());
								if (minorStep < 1) {
									minorStep = 1;
								}
								for (int k = 0; k < daMinor.length - 1; k += minorStep) {
									if (computation instanceof LinearAxisTypeComputation) {
										// Special case for linear type
										if ((iDirection == -1 && y - daMinor[k] <= da.getCoordinate(i + 1))
												|| (iDirection == 1 && y + daMinor[k] >= da.getCoordinate(i + 1))) {
											// if current minor tick exceed
											// the range of current unit, skip
											continue;
										}
									}

									lreMinor = ((EventObjectCache) ipr)
											.getEventObject(StructureSource.createAxis(axModel), LineRenderEvent.class);
									lreMinor.setLineAttributes(liaMinorTick);
									loMinorStart.set(dXMinorTick1, y + iDirection * daMinor[k]);
									lreMinor.setStart(loMinorStart);

									loMinorEnd.set(dXMinorTick2, y + iDirection * daMinor[k]);
									lreMinor.setEnd(loMinorEnd);

									ipr.drawLine(lreMinor);
								}
							}
						}
					}
				}

				if (computation instanceof TextAxisTypeComputation && !bTickBetweenCategories && i == 0) {
					continue;
				}

				if (!bSkipTickLine && !ChartUtil.mathEqual(context.dTick1, context.dTick2)) {
					if (bRenderOrthogonal3DAxis) {
						// !NOT RENDER TICKS FOR 3D AXES
					} else {
						lre.setLineAttributes(liaMajorTick);
						lre.getStart().set(context.dTick1, y);
						lre.getEnd().set(context.dTick2, y);
						ipr.drawLine(lre);
					}

					if (iv != null && iDimension == IConstants.TWO_5_D && iv.getType() == IConstants.VALUE) {
						lre.setStart(goFactory.createLocation(context.dX, y));
						lre.setEnd(goFactory.createLocation(context.dX + dSeriesThickness, y - dSeriesThickness));
						ipr.drawLine(lre);
					}
				}
			}

			// Render axis labels
			if (bRenderAxisLabels && sc.isTickLabelVisible(i)) {
				double sx = x;
				double sx2 = dXEnd;
				if (bAxisLabelStaggered && sc.isTickLabelStaggered(i)) {
					if (iLabelLocation == IConstants.LEFT) {
						sx -= dStaggeredLabelOffset;
						sx2 += dStaggeredLabelOffset;
					} else {
						sx += dStaggeredLabelOffset;
						sx2 -= dStaggeredLabelOffset;
					}
				}

				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_AXIS_LABEL, axModel, la,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL, la);

				if (ax.getLabel().isVisible() && la.isVisible()) {
					if (bRendering3D) {
						// Left wall
						lo3d.set(sx - pwa.getHorizontalSpacingInPixels(), context.y3d + dOffset,
								dZEnd + pwa.getHorizontalSpacingInPixels());
						t3dre.setLocation3D(lo3d);
						t3dre.setTextPosition(TextRenderEvent.LEFT);
						t3dre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
						dc.addLabel(t3dre);

						// Right wall
						lo3d.set(sx2 + pwa.getHorizontalSpacingInPixels(), context.y3d + dOffset,
								dZ - pwa.getHorizontalSpacingInPixels());
						t3dre.setLocation3D(lo3d);
						t3dre.setTextPosition(TextRenderEvent.RIGHT);
						t3dre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
						// bidi_acgc added start
						if (this.renderer.rtc.isRightToLeftText()) {
							t3dre.setRtlCaption();
						}
						// bidi_acgc added end
						dc.addLabel(t3dre);
					} else {
						lo.set(sx, y + dOffset);
						tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
						// Adjust the first label's position if labels should be
						// within axes
						if (bLabelWithinAxes) {
							if (i == 0) {
								tre.setTextPosition(iLabelLocation | IConstants.POSITION_MOVE_ABOVE);
							} else {
								tre.setTextPosition(iLabelLocation);
							}
						}
						// bidi_acgc added start
						if (this.renderer.rtc.isRightToLeftText()) {
							tre.setRtlCaption();
						}
						// bidi_acgc added end
						if (deferredAxisLabel) {
							deferfedRenderList.add((TextRenderEvent) tre.copy());
						} else {
							ipr.drawText(tre);
						}
					}
				}

				addAxisLabelIA(lo.getX(), lo.getY());

				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_AXIS_LABEL, axModel, la,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL, la);
			}

			computation.handlePostEachTick(i);
		}

		computation.close();

		return deferfedRenderList;
	}

	private void addAxisLabelIA(double x, double y) throws ChartException {
		if (renderer.isInteractivityEnabled()) {
			EList<Trigger> elTriggers = ax.getModelAxis().getTriggers();
			Location[] loaHotspot = new Location[4];

			RotatedRectangle rr = cComp.computePolygon(xs, iLabelLocation, la, x, y, null);

			List<Point> pts = rr.getPoints();
			for (int k = 0; k < 4; k++) {
				Point pt = pts.get(k);
				loaHotspot[k] = goFactory.createLocation(pt.getX(), pt.getY());
			}

			StructureSource iSource = WrappedStructureSource.createAxisLabel(ax.getModelAxis(), la);

			final InteractionEvent iev = ((EventObjectCache) ipr).getEventObject(iSource, InteractionEvent.class);
			iev.setCursor(ax.getModelAxis().getCursor());

			for (int t = 0; t < elTriggers.size(); t++) {
				Trigger tg = goFactory.copyOf(elTriggers.get(t));
				processTrigger(tg, iSource);
				iev.addTrigger(tg);
			}

			final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(iSource, PolygonRenderEvent.class);
			pre.setPoints(loaHotspot);
			iev.setHotSpot(pre);
			ipr.enableInteraction(iev);
		}

	}

	private List<TextRenderEvent> renderHorizontalAxisTickLabels(ComputationContext context, double dXEnd, double dZEnd,
			double dZ, double dStaggeredLabelOffset, boolean deferredAxisLabel) throws ChartException {
		List<TextRenderEvent> deferfedRenderList = new ArrayList<TextRenderEvent>();

		// The horizontal axis direction. -1 means right->left, 1 means
		// left->right.
		final int iDirection = sc.getDirection() == IConstants.BACKWARD ? -1 : 1;
		IAxisTypeComputation computation = createAxisTypeComputation(context);
		computation.initialize();

		// Offset for Text axis type
		final double dOffset = computation instanceof TextAxisTypeComputation && bTickBetweenCategories
				? iDirection * sc.getUnitSize() / 2
				: 0;

		// Tick size
		final int length = computation instanceof TextAxisTypeComputation ? da.size() - 1 : da.size();

		double y = (iLabelLocation == IConstants.ABOVE) ? (bRendering3D ? context.dTick1 + 1 : context.dTick1 - 1)
				: (bRendering3D ? context.dTick2 - 1 : context.dTick2 + 1);

		double xLast = Integer.MIN_VALUE;
		Location loMinorStart = goFactory.createLocation(0, 0);
		Location loMinorEnd = goFactory.createLocation(0, 0);
		for (int i = 0; i < length; i++) {

			computation.handlePreEachTick(i);

			double x = da.getCoordinate(i);

			boolean bSkipTickLine = (Math.abs(x - xLast) < 1);
			if (!bSkipTickLine) {
				xLast = x;
			}

			if (bRendering3D) {
				context.x3d = (int) da3D.getCoordinate(i);
				context.z3d = (int) da3D.getCoordinate(i);
			}
			if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS) {
				double dYMinorTick1 = ((iMinorTickStyle & IConstants.TICK_ABOVE) == IConstants.TICK_ABOVE)
						? (bRendering3D ? context.dY + pwa.getTickSize() : context.dY - pwa.getTickSize())
						: context.dY;
				double dYMinorTick2 = ((iMinorTickStyle & IConstants.TICK_BELOW) == IConstants.TICK_BELOW)
						? (bRendering3D ? context.dY - pwa.getTickSize() : context.dY + pwa.getTickSize())
						: context.dY;
				if (!ChartUtil.mathEqual(dYMinorTick1, -dYMinorTick2)) {
					// RENDER THE MINOR TICKS FIRST (For ALL but the
					// last Major tick)
					if (i != da.size() - 1) {
						if (bRenderBase3DAxis) {
							// !NOT RENDER TICKS FOR 3D AXES
						} else if (bRenderAncillary3DAxis) {
							// !NOT RENDER TICKS FOR 3D AXES
						} else {
							if (!bSkipTickLine && ((i > 0 && i < length - 1) || !axModel.isCategoryAxis()
									|| bTickBetweenCategories)) {
								LineRenderEvent lreMinor = null;
								int minorStep = (int) (1d / da.getStep());
								if (minorStep < 1) {
									minorStep = 1;
								}
								for (int k = 0; k < daMinor.length - 1; k += minorStep) {
									// Special case for linear type
									if (computation instanceof LinearAxisTypeComputation) {
										if ((iDirection == 1 && x + daMinor[k] >= da.getCoordinate(i + 1))
												|| (iDirection == -1 && x - daMinor[k] <= da.getCoordinate(i + 1))) {
											// if current minor tick exceed the
											// range of current unit, skip
											continue;
										}
									}

									lreMinor = ((EventObjectCache) ipr)
											.getEventObject(StructureSource.createAxis(axModel), LineRenderEvent.class);
									lreMinor.setLineAttributes(liaMinorTick);

									loMinorStart.set(x + iDirection * daMinor[k], dYMinorTick1);
									lreMinor.setStart(loMinorStart);
									loMinorEnd.set(x + iDirection * daMinor[k], dYMinorTick2);
									lreMinor.setEnd(loMinorEnd);
									ipr.drawLine(lreMinor);
								}
							}
						}
					}
				}

				if (computation instanceof TextAxisTypeComputation && !bTickBetweenCategories && i == 0) {
					continue;
				}

				if (!bSkipTickLine && !ChartUtil.mathEqual(context.dTick1, context.dTick2)) {
					if (bRenderBase3DAxis) {
						// !NOT RENDER TICKS FOR 3D AXES
					} else if (bRenderAncillary3DAxis) {
						// !NOT RENDER TICKS FOR 3D AXES
					} else {
						lre.setLineAttributes(liaMajorTick);
						lre.getStart().set(x, context.dTick1);
						lre.getEnd().set(x, context.dTick2);
						ipr.drawLine(lre);
					}

					if (iv != null && iDimension == IConstants.TWO_5_D && iv.getType() == IConstants.VALUE) {
						lre.getStart().set(x, context.dY);
						lre.getEnd().set(x + dSeriesThickness, context.dY - dSeriesThickness);
						ipr.drawLine(lre);
					}
				}
			}

			if (bRenderAxisLabels && sc.isTickLabelVisible(i)) {
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_AXIS_LABEL, axModel, la,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_AXIS_LABEL, la);

				double sy = y;

				if (bAxisLabelStaggered && sc.isTickLabelStaggered(i)) {
					if (iLabelLocation == IConstants.ABOVE) {
						sy -= dStaggeredLabelOffset;
					} else {
						sy += dStaggeredLabelOffset;
					}
				}
				if (ax.getLabel().isVisible() && la.isVisible()) {
					if (bRendering3D) {
						if (axisType == IConstants.BASE_AXIS) {
							lo3d.set(context.x3d + dOffset, sy - pwa.getVerticalSpacingInPixels(),
									dZEnd + pwa.getVerticalSpacingInPixels());
						} else {
							lo3d.set(dXEnd + pwa.getVerticalSpacingInPixels(), sy - pwa.getVerticalSpacingInPixels(),
									context.z3d + dOffset);
						}
						t3dre.setLocation3D(lo3d);
						t3dre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
						// bidi_acgc added start
						if (this.renderer.rtc.isRightToLeftText()) {
							t3dre.setRtlCaption();
						}
						// bidi_acgc added end
						dc.addLabel(t3dre);
					} else {
						lo.set(x + dOffset, sy);
						tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
						// Adjust the first label's position if labels should be
						// within axes
						if (bLabelWithinAxes) {
							if (i == 0) {
								tre.setTextPosition(iLabelLocation | IConstants.POSITION_MOVE_RIGHT);
							} else {
								tre.setTextPosition(iLabelLocation);
							}
						}
						// bidi_acgc added start
						if (this.renderer.rtc.isRightToLeftText()) {
							tre.setRtlCaption();
						}
						// bidi_acgc added end
						if (deferredAxisLabel) {
							deferfedRenderList.add((TextRenderEvent) tre.copy());
						} else {
							ipr.drawText(tre);
						}
					}
				}

				addAxisLabelIA(lo.getX(), lo.getY());

				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_AXIS_LABEL, axModel, la,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_AXIS_LABEL, la);
			}

			computation.handlePostEachTick(i);
		}

		computation.close();

		return deferfedRenderList;
	}

	/**
	 * Renders the axis.
	 * 
	 * @throws ChartException
	 */
	public final void renderEachAxis() throws ChartException {
		final double dStaggeredLabelOffset = sc.computeStaggeredAxisLabelOffset(xs, la, iOrientation);

		tre.setLabel(la);
		tre.setTextPosition(iLabelLocation);
		tre.setLocation(lo);

		lre.setLineAttributes(lia);
		lre.setStart(goFactory.createLocation(0, 0));
		lre.setEnd(goFactory.createLocation(0, 0));

		double dXStart = 0;
		double dXEnd = 0;
		double dZStart = 0;
		double dZEnd = 0;

		if (iDimension == IConstants.THREE_D) {
			AllAxes aax = pwa.getAxes();
			dXEnd = aax.getPrimaryBase().getScale().getEnd();
			dZEnd = aax.getAncillaryBase().getScale().getEnd();
			dXStart = aax.getPrimaryBase().getScale().getStart();
			dZStart = aax.getAncillaryBase().getScale().getStart();

			daEndPoints3D = sc.getEndPoints();
			da3D = sc.getTickCordinates();

			lo3d = goFactory.createLocation3D(0, 0, 0);

			t3dre = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel),
					Text3DRenderEvent.class);
			t3dre.setLabel(la);
			t3dre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
			t3dre.setTextPosition(iLabelLocation);
			t3dre.setLocation3D(lo3d);

			l3dre = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel),
					Line3DRenderEvent.class);
			l3dre.setLineAttributes(lia);
			l3dre.setStart3D(goFactory.createLocation3D(0, 0, 0));
			l3dre.setEnd3D(goFactory.createLocation3D(0, 0, 0));
		}

		if (iOrientation == IConstants.VERTICAL) {
			renderVerticalAxis(dXStart, dXEnd, dZStart, dZEnd, dStaggeredLabelOffset);
		} else if (iOrientation == IConstants.HORIZONTAL) {
			renderHorizontalAxis(dXEnd, dZEnd, dStaggeredLabelOffset);
		}
	}

	private void renderHorizontalAxis(double dXEnd, double dZEnd, final double dStaggeredLabelOffset)
			throws ChartException {
		final ComputationContext context = new ComputationContext(false);

		context.x3d = 0;
		context.z3d = 0;
		context.dY = dLocation;
		double dX = 0;
		double dZ = 0;

		if (bRendering3D) {
			Location3D l3d = ax.getAxisCoordinate3D();

			dX = l3d.getX();
			context.dY = l3d.getY();
			dZ = l3d.getZ();
		}

		context.dTick1 = ((iMajorTickStyle & IConstants.TICK_ABOVE) == IConstants.TICK_ABOVE)
				? (bRendering3D ? context.dY + pwa.getTickSize() : context.dY - pwa.getTickSize())
				: context.dY;
		context.dTick2 = ((iMajorTickStyle & IConstants.TICK_BELOW) == IConstants.TICK_BELOW)
				? (bRendering3D ? context.dY - pwa.getTickSize() : context.dY + pwa.getTickSize())
				: context.dY;

		if (iv != null && iDimension == IConstants.TWO_5_D
				&& ((bTransposed && renderer.isRightToLeft() && iv.getType() == IConstants.MIN)
						|| (!renderer.isRightToLeft() && iv.getType() == IConstants.MAX))) {
			trae.setTransform(TransformationEvent.TRANSLATE);
			trae.setTranslation(dSeriesThickness, -dSeriesThickness);
			ipr.applyTransformation(trae);
		}

		// First render ticks.
		List<TextRenderEvent> deferredEvents = renderHorizontalAxisTickLabels(context, dXEnd, dZEnd, dZ,
				dStaggeredLabelOffset, true);

		// Next render axis line.
		if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS && lia.isVisible()) {
			renderHorizontalAxisLine(context, dX, dZ);
		}

		// Then render axis labels.
		if (deferredEvents != null && deferredEvents.size() > 0) {
			for (TextRenderEvent evt : deferredEvents) {
				ipr.drawText(evt);
			}
		}

		// Last render axis title
		renderHorizontalAxisTitle(context, dXEnd, dZEnd, dZ);

		if (iv != null && iDimension == IConstants.TWO_5_D
				&& ((bTransposed && renderer.isRightToLeft() && iv.getType() == IConstants.MIN)
						|| (!renderer.isRightToLeft() && iv.getType() == IConstants.MAX))) {
			trae.setTranslation(-dSeriesThickness, dSeriesThickness);
			ipr.applyTransformation(trae);
		}
	}

	private void renderHorizontalAxisTitle(final ComputationContext context, double dXEnd, double dZEnd, double dZ)
			throws ChartException {
		la = goFactory.copyOf(ax.getTitle()); // TEMPORARILY USE
		// FOR AXIS TITLE
		if (la.isVisible() && bRenderAxisTitle) {
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_AXIS_TITLE, axModel, la,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_AXIS_TITLE, la);
			final String sRestoreValue = la.getCaption().getValue();
			la.getCaption().setValue(getRunTimeContext().externalizedMessage(sRestoreValue)); // EXTERNALIZE
			la.getCaption().getFont()
					.setAlignment(renderer.switchTextAlignment(la.getCaption().getFont().getAlignment()));

			if (ax.getTitle().isVisible() && la.isVisible()) {
				if (bRendering3D) {
					BoundingBox bb = cComp.computeLabelSize(xs, la, Math.abs(daEndPoints[1] - daEndPoints[0]), null);

					Angle3D a3D = ((ChartWithAxes) renderer.cm).getRotation().getAngles().get(0);

					if (axisType == IConstants.BASE_AXIS) {
						t3dre = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel),
								Text3DRenderEvent.class);

						IAxisTypeComputation computation = createAxisTypeComputation(context);
						computation.initialize();
						final int length = computation instanceof TextAxisTypeComputation ? da.size() - 1 : da.size();

						OneAxis axxPB = pwa.getAxes().getPrimaryBase();
						double xLabelThickness = cComp.computeHeight(xs, axxPB.getLabel());

						int xStart = (int) da3D.getCoordinate(0);
						int xEnd = (int) da3D.getCoordinate(length - 1);
						int x = xStart + (xEnd - xStart) / 2;
						Location3D location = goFactory.createLocation3D(x, context.dY - xLabelThickness - (dZEnd - dZ),
								dZEnd + pwa.getHorizontalSpacingInPixels() + xLabelThickness);
						t3dre.setLocation3D(location);
						t3dre.setLabel(la);
						double yAngle = a3D.getYAngle() % 360;
						if (yAngle > 0 && yAngle <= 180) {
							t3dre.setTextPosition(TextRenderEvent.LEFT);
						} else {
							t3dre.setTextPosition(TextRenderEvent.RIGHT);
						}

						t3dre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
					} else {
						double yAngle = a3D.getYAngle();
						Location3D location = goFactory.createLocation3D(dXEnd + (dZEnd - dZ),
								context.dY - (dZEnd - dZ) / 2 - bb.getHeight() * (1 + Math.sin(Math.abs(yAngle))),
								dZ + (dZEnd - dZ) / 2);
						t3dre.setLocation3D(location);
						t3dre.setLabel(la);
						double angle = a3D.getZAngle() % 360;
						if (angle >= 0 && angle < 180) {
							t3dre.setTextPosition(TextRenderEvent.RIGHT);
						} else {
							t3dre.setTextPosition(TextRenderEvent.LEFT);
						}

						t3dre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
					}

					renderAxisTitleWith3DTextevent(bb);
				} else {
					LabelLimiter lbLimit = pwa.getLabellLimiter(ax.getModelAxis().getTitle());
					double dWidthWithinAxis = Math.abs(daEndPoints[1] - daEndPoints[0]);
					if (lbLimit.getMaxWidth() > dWidthWithinAxis) {
						lbLimit.setMaxWidth(dWidthWithinAxis);
					}
					lbLimit.computeWrapping(xs, la);
					lbLimit = lbLimit.limitLabelSize(cComp, xs, la);

					if (lbLimit.isSuccessed()) {
						BoundingBox bb = lbLimit.getBounding(null);

						final Bounds bo = goFactory.createBounds(daEndPoints[0], ax.getTitleCoordinate(),
								daEndPoints[1] - daEndPoints[0], bb.getHeight());

						tre.setBlockBounds(bo);
						tre.setLabel(la);
						TextAlignment ta = goFactory.copyOf(la.getCaption().getFont().getAlignment());

						if (ax.getModelAxis().getAssociatedAxes().size() != 0) {
							tre.setBlockAlignment(ta);
						} else {
							tre.setBlockAlignment(ChartUtil.transposeAlignment(ta));
						}
						la.getCaption().getFont().getAlignment()
								.setHorizontalAlignment(HorizontalAlignment.LEFT_LITERAL);
						tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
						// bidi_acgc added start
						if (this.renderer.rtc.isRightToLeftText()) {
							tre.setRtlCaption();
						}
						// bidi_acgc added end
						ipr.drawText(tre);
					}
				}
			}

			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_AXIS_TITLE, axModel, la,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_AXIS_TITLE, la);
		}
		la = goFactory.copyOf(ax.getLabel()); // RESTORE BACK TO
	}

	private void renderHorizontalAxisLine(final ComputationContext context, double dX, double dZ)
			throws ChartException {
		if (bRenderBase3DAxis) {
			final double dStart = daEndPoints3D[0];
			final double dEnd = daEndPoints3D[1];
			l3dre.setLineAttributes(lia);
			l3dre.setStart3D(dStart, context.dY, dZ);
			l3dre.setEnd3D(dEnd, context.dY, dZ);
			addLine3DEvent(l3dre, renderer.getRightWallEvent(), dc);

			if (renderer.isInteractivityEnabled()) {
				Trigger tg;
				EList<Trigger> elTriggers = axModel.getTriggers();

				if (!elTriggers.isEmpty()) {
					final Polygon3DRenderEvent pre3d = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createAxis(axModel), Polygon3DRenderEvent.class);

					Location3D[] loaHotspot = new Location3D[4];
					loaHotspot[0] = goFactory.createLocation3D(dStart, context.dY - IConstants.LINE_EXPAND_DOUBLE_SIZE,
							dZ);
					loaHotspot[1] = goFactory.createLocation3D(dStart, context.dY + IConstants.LINE_EXPAND_DOUBLE_SIZE,
							dZ);
					loaHotspot[2] = goFactory.createLocation3D(dEnd, context.dY + IConstants.LINE_EXPAND_DOUBLE_SIZE,
							dZ);
					loaHotspot[3] = goFactory.createLocation3D(dEnd, context.dY - IConstants.LINE_EXPAND_DOUBLE_SIZE,
							dZ);
					pre3d.setPoints3D(loaHotspot);
					pre3d.setDoubleSided(true);

					if (renderer.get3DEngine().processEvent(pre3d, panningOffset.getX(),
							panningOffset.getY()) != null) {
						final InteractionEvent iev = ((EventObjectCache) ipr)
								.getEventObject(StructureSource.createAxis(axModel), InteractionEvent.class);
						iev.setCursor(axModel.getCursor());

						for (int t = 0; t < elTriggers.size(); t++) {
							tg = goFactory.copyOf(elTriggers.get(t));
							processTrigger(tg, StructureSource.createAxis(axModel));
							iev.addTrigger(tg);
						}

						iev.setHotSpot(pre3d);
						ipr.enableInteraction(iev);
					}
				}
			}

		} else if (bRenderAncillary3DAxis) {
			final double dStart = daEndPoints3D[0];
			final double dEnd = daEndPoints3D[1];
			l3dre.setLineAttributes(lia);
			l3dre.setStart3D(dX, context.dY, dStart);
			l3dre.setEnd3D(dX, context.dY, dEnd);
			addLine3DEvent(l3dre, renderer.getLeftWallEvent(), dc);

			if (renderer.isInteractivityEnabled()) {
				Trigger tg;
				EList<Trigger> elTriggers = axModel.getTriggers();

				if (!elTriggers.isEmpty()) {
					final Polygon3DRenderEvent pre3d = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createAxis(axModel), Polygon3DRenderEvent.class);

					Location3D[] loaHotspot = new Location3D[4];
					loaHotspot[0] = goFactory.createLocation3D(dX, context.dY - IConstants.LINE_EXPAND_DOUBLE_SIZE,
							dStart);
					loaHotspot[1] = goFactory.createLocation3D(dX, context.dY + IConstants.LINE_EXPAND_DOUBLE_SIZE,
							dStart);
					loaHotspot[2] = goFactory.createLocation3D(dX, context.dY + IConstants.LINE_EXPAND_DOUBLE_SIZE,
							dEnd);
					loaHotspot[3] = goFactory.createLocation3D(dX, context.dY - IConstants.LINE_EXPAND_DOUBLE_SIZE,
							dEnd);
					pre3d.setPoints3D(loaHotspot);
					pre3d.setDoubleSided(true);

					if (renderer.get3DEngine().processEvent(pre3d, panningOffset.getX(),
							panningOffset.getY()) != null) {
						final InteractionEvent iev = ((EventObjectCache) ipr)
								.getEventObject(StructureSource.createAxis(axModel), InteractionEvent.class);
						iev.setCursor(axModel.getCursor());

						for (int t = 0; t < elTriggers.size(); t++) {
							tg = goFactory.copyOf(elTriggers.get(t));
							processTrigger(tg, StructureSource.createAxis(axModel));
							iev.addTrigger(tg);
						}

						iev.setHotSpot(pre3d);
						ipr.enableInteraction(iev);
					}
				}
			}

		} else {
			double dStart = daEndPoints[0] - insCA.getLeft(), dEnd = daEndPoints[1] + insCA.getRight();

			if (sc.getDirection() == IConstants.BACKWARD) {
				dStart = daEndPoints[1] - insCA.getLeft();
				dEnd = daEndPoints[0] + insCA.getRight();
			}

			if (iv != null && iv.getType() == IConstants.VALUE && iDimension == IConstants.TWO_5_D) {
				// Zero plane.
				final Location[] loa = new Location[4];
				loa[0] = goFactory.createLocation(dStart, context.dY);
				loa[1] = goFactory.createLocation(dStart + dSeriesThickness, context.dY - dSeriesThickness);
				loa[2] = goFactory.createLocation(dEnd + dSeriesThickness, context.dY - dSeriesThickness);
				loa[3] = goFactory.createLocation(dEnd, context.dY);

				final PolygonRenderEvent pre = ((EventObjectCache) ipr)
						.getEventObject(StructureSource.createAxis(axModel), PolygonRenderEvent.class);
				pre.setPoints(loa);
				pre.setBackground(goFactory.createColorDefinition(255, 255, 255, 127));
				pre.setOutline(lia);
				ipr.fillPolygon(pre);
			}
			lre.setLineAttributes(lia);
			lre.getStart().set(dStart, context.dY);
			lre.getEnd().set(dEnd, context.dY);
			ipr.drawLine(lre);

			if (renderer.isInteractivityEnabled()) {
				Trigger tg;
				EList<Trigger> elTriggers = axModel.getTriggers();

				if (!elTriggers.isEmpty()) {
					final InteractionEvent iev = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createAxis(axModel), InteractionEvent.class);
					iev.setCursor(axModel.getCursor());

					for (int t = 0; t < elTriggers.size(); t++) {
						tg = goFactory.copyOf(elTriggers.get(t));
						processTrigger(tg, StructureSource.createAxis(axModel));
						iev.addTrigger(tg);
					}

					Location[] loaHotspot = new Location[4];

					loaHotspot[0] = goFactory.createLocation(dStart, context.dY - IConstants.LINE_EXPAND_SIZE);
					loaHotspot[1] = goFactory.createLocation(dEnd, context.dY - IConstants.LINE_EXPAND_SIZE);
					loaHotspot[2] = goFactory.createLocation(dEnd, context.dY + IConstants.LINE_EXPAND_SIZE);
					loaHotspot[3] = goFactory.createLocation(dStart, context.dY + IConstants.LINE_EXPAND_SIZE);

					final PolygonRenderEvent pre = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createAxis(axModel), PolygonRenderEvent.class);
					pre.setPoints(loaHotspot);
					iev.setHotSpot(pre);
					ipr.enableInteraction(iev);
				}
			}

		}
	}

	private void renderVerticalAxis(double dXStart, double dXEnd, double dZStart, double dZEnd,
			final double dStaggeredLabelOffset) throws ChartException {
		final ComputationContext context = new ComputationContext(true);

		context.y3d = 0;
		context.dX = dLocation;
		double dZ = 0;

		if (bRendering3D) {
			Location3D l3d = ax.getAxisCoordinate3D();
			context.dX = l3d.getX();
			dZ = l3d.getZ();
		}

		if (iv != null && iv.getType() == IConstants.MAX && iDimension == IConstants.TWO_5_D) {
			trae.setTransform(TransformationEvent.TRANSLATE);
			trae.setTranslation(dSeriesThickness, -dSeriesThickness);
			ipr.applyTransformation(trae);
		}

		context.dTick1 = ((iMajorTickStyle & IConstants.TICK_LEFT) == IConstants.TICK_LEFT)
				? context.dX - pwa.getTickSize()
				: context.dX;
		context.dTick2 = ((iMajorTickStyle & IConstants.TICK_RIGHT) == IConstants.TICK_RIGHT)
				? context.dX + pwa.getTickSize()
				: context.dX;

		// First render axis ticks.
		List<TextRenderEvent> deferredEvents = renderVerticalAxisTickLabels(context, dXEnd, dZEnd, dZ,
				dStaggeredLabelOffset, true);

		// Next render axis line.
		if ((iWhatToDraw & IConstants.AXIS) == IConstants.AXIS && lia.isVisible()) {
			renderVerticalAxisLine(context, dXStart, dXEnd, dZStart, dZEnd, dZ);
		}

		// Then render axis labels.
		if (deferredEvents != null && deferredEvents.size() > 0) {
			for (TextRenderEvent evt : deferredEvents) {
				ipr.drawText(evt);
			}
		}

		// Last render axis title.
		renderVerticalAxisTitle(context, dXEnd, dZEnd, dZ, dStaggeredLabelOffset);

		if (iv != null && iv.getType() == IConstants.MAX && iDimension == IConstants.TWO_5_D) {
			trae.setTranslation(-dSeriesThickness, dSeriesThickness);
			ipr.applyTransformation(trae);
		}
	}

	private void renderVerticalAxisTitle(final ComputationContext context, double dXEnd, double dZEnd, double dZ,
			final double dStaggeredLabelOffset) throws ChartException {
		la = goFactory.copyOf(ax.getTitle()); // TEMPORARILY USE
		// FOR AXIS TITLE
		if (la.isVisible() && bRenderAxisTitle) {
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_AXIS_TITLE, axModel, la,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_AXIS_TITLE, la);

			final String sRestoreValue = la.getCaption().getValue();
			la.getCaption().setValue(getRunTimeContext().externalizedMessage(sRestoreValue));

			if (ax.getTitle().isVisible() && la.isVisible()) {
				if (bRendering3D) {
					BoundingBox bb = null;
					// Buzilla#206093: Indicates if the axis title is within
					// axis,
					// otherwise it uses Y axis plus the axis corner to
					// display
					boolean bWithinAxis = false;
					// Indicates the axis title is horizontal
					final boolean bTitleHorizontal = Math.abs(la.getCaption().getFont().getRotation()) <= 30;
					final double dYAxisHeightPC = ChartUtil
							.computeHeightOfOrthogonalAxisTitle((ChartWithAxes) this.renderer.cm, xs);

					if (bTitleHorizontal) {
						// Horizontal title always starts with axis and
						// within
						// axis. It shouldn't display outside axis.
						bWithinAxis = true;
					} else {
						final BoundingBox bbWoWrap = cComp.computeLabelSize(xs, la, dYAxisHeightPC, null);
						bWithinAxis = bbWoWrap.getHeight() < daEndPoints[0] - daEndPoints[1];
					}
					// Keep the same behavior with PlotWithAxes. If
					// title is
					// horizontal, only wrap if title exceeds height
					// plus
					// corner.
					bb = cComp.computeLabelSize(xs, la,
							bWithinAxis && !bTitleHorizontal ? daEndPoints[0] - daEndPoints[1] : dYAxisHeightPC, null);

					// Bounds cbo = renderer.getPlotBounds( );
					//
					// tre.setBlockBounds( goFactory.createBounds(
					// cbo.getLeft( )
					// + ( cbo.getWidth( ) / 3d - bb.getWidth( ) )
					// / 2d,
					// cbo.getTop( ) + 30,
					// bb.getWidth( ),
					// bb.getHeight( ) ) );
					//
					// tre.setLabel( la );
					// tre.setBlockAlignment( la.getCaption( )
					// .getFont( )
					// .getAlignment( ) );
					// tre.setAction( TextRenderEvent.RENDER_TEXT_IN_BLOCK
					// );
					// ipr.drawText( tre );
					//
					// tre.setBlockBounds( goFactory.createBounds(
					// cbo.getLeft( )
					// + cbo.getWidth( )
					// - bb.getWidth( ),
					// cbo.getTop( ) + 30 * 2,
					// bb.getWidth( ),
					// bb.getHeight( ) ) );
					//
					// ipr.drawText( tre );

					// Comment above code and add following code to partial
					// fix bugzilla bug 192833. This fix only made that the
					// axis title position is decided by its axis location.
					// Create 3D text location for axis title.

					// Get position values.
					Angle3D a3D = ((ChartWithAxes) renderer.cm).getRotation().getAngles().get(0);
					double yAxisAngle = a3D.getYAngle() * Math.PI / 180;
					double yCenter = daEndPoints3D[0] + ((daEndPoints3D[1] - daEndPoints3D[0]) / 2);
					double leftYAxisTitlePosition = context.dX;
					double rightYAxisTitlePosition = dXEnd;

					if (bAxisLabelStaggered) {
						if (iLabelLocation == IConstants.LEFT) {
							leftYAxisTitlePosition -= dStaggeredLabelOffset;
							rightYAxisTitlePosition += dStaggeredLabelOffset;
						} else {
							leftYAxisTitlePosition += dStaggeredLabelOffset;
							rightYAxisTitlePosition -= dStaggeredLabelOffset;
						}
					}

					OneAxis axxPV = pwa.getAxes().getPrimaryOrthogonal();
					double yLabelThickness = axxPV.getScale().computeAxisLabelThickness(xs, axxPV.getLabel(),
							IConstants.VERTICAL);

					// Render left and right Y axis titles.
					double offset = pwa.getVerticalSpacingInPixels();
					double minAngle = 35 * Math.PI / 180;
					;

					double angle = yAxisAngle;
					if (Math.abs(yAxisAngle) < minAngle) {
						angle = minAngle;
					}
					double leftTitleYDelta = (pwa.getVerticalSpacingInPixels() + yLabelThickness + bb.getWidth()
							+ offset) * (1 + Math.abs(Math.sin(angle)));

					double leftTitleZDelta = (pwa.getVerticalSpacingInPixels() + bb.getWidth() + offset)
							* (1 + Math.sin(Math.abs(yAxisAngle)));
					double zPosition = dZ;
					if (yAxisAngle >= 0) {
						zPosition = dZEnd + leftTitleZDelta + (dZEnd - dZ) * (1 + Math.sin(Math.abs(yAxisAngle)));
					} else {
						zPosition = dZ - leftTitleZDelta;
					}
					t3dre = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel),
							Text3DRenderEvent.class);
					t3dre.setLocation3D(
							goFactory.createLocation3D(leftYAxisTitlePosition - leftTitleYDelta, yCenter, zPosition));

					t3dre.setLabel(la);
					t3dre.setTextPosition(TextRenderEvent.LEFT);
					t3dre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
					renderAxisTitleWith3DTextevent(bb);

					t3dre = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel),
							Text3DRenderEvent.class);
					angle = yAxisAngle;
					if (Math.abs(yAxisAngle) < minAngle) {
						angle = minAngle;
					}
					double rightTitleYDelta = (pwa.getVerticalSpacingInPixels() + yLabelThickness + offset)
							* (1 + Math.abs(Math.sin(angle)));
					double rightTitleZDelta = (pwa.getVerticalSpacingInPixels() + yLabelThickness + offset)
							* Math.abs(Math.sin(angle));
					zPosition = dZ;
					if (yAxisAngle <= 0) {
						zPosition = dZEnd + rightTitleZDelta;
					} else {
						zPosition = dZ - rightTitleZDelta;
					}
					t3dre.setLocation3D(
							goFactory.createLocation3D(rightYAxisTitlePosition + rightTitleYDelta, yCenter, zPosition));
					t3dre.setLabel(la);
					t3dre.setTextPosition(TextRenderEvent.RIGHT);
					t3dre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
					renderAxisTitleWith3DTextevent(bb);
				} else {
					LabelLimiter lbLimit = pwa.getLabellLimiter(ax.getModelAxis().getTitle());
					lbLimit.computeWrapping(xs, la);
					lbLimit = lbLimit.limitLabelSize(cComp, xs, la);

					if (lbLimit.isSuccessed()) {
						BoundingBox bb = lbLimit.getBounding(null);

						// Buzilla#206093: Indicates if the axis title is
						// within
						// axis,
						// otherwise it uses Y axis plus the axis corner to
						// display
						boolean bWithinAxis = false;
						// Indicates the axis title is horizontal
						final boolean bTitleHorizontal = Math.abs(la.getCaption().getFont().getRotation()) <= 30;
						double dYAxisHeightPC = ChartUtil
								.computeHeightOfOrthogonalAxisTitle((ChartWithAxes) this.renderer.cm, xs);
						double dHeightWithinAxis = daEndPoints[0] - daEndPoints[1];

						if (ChartUtil.isStudyLayout(renderer.cm)) {
							// If it is study layout, the axis title height should use the orthogonal axis
							// scale range as height.
							dYAxisHeightPC = dHeightWithinAxis;
						}

						bWithinAxis = bTitleHorizontal || (lbLimit.getMaxHeight() < dHeightWithinAxis);

						// #190266 Axis title layout adjustment
						// final Bounds boundsTitle = ( (ChartWithAxes)
						// this.renderer.cm ).getTitle( )
						// .getBounds( );
						double dTop = computeTopOfOrthogonalAxisTitle();

						if (ChartUtil.isStudyLayout(renderer.cm)) {
							// If it is study layout, the axis title top should use the scale end of
							// orthogonal axis scale as top.
							dTop = daEndPoints[1];
						}

						final Bounds bo = goFactory.createBounds(ax.getTitleCoordinate(),
								bWithinAxis ? daEndPoints[1] : dTop, bb.getWidth(),
								bWithinAxis ? dHeightWithinAxis : dYAxisHeightPC);

						tre.setBlockBounds(bo);
						tre.setLabel(la);
						TextAlignment ta = goFactory.copyOf(la.getCaption().getFont().getAlignment());
						if (ax.getModelAxis().getAssociatedAxes().size() != 0) {
							tre.setBlockAlignment(ChartUtil.transposeAlignment(ta));
						} else {
							tre.setBlockAlignment(ta);
						}
						la.getCaption().getFont().getAlignment()
								.setHorizontalAlignment(HorizontalAlignment.LEFT_LITERAL);
						tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
						if (ax.getTitle().isVisible()) {
							// bidi_acgc added start
							if (this.renderer.rtc.isRightToLeftText()) {
								tre.setRtlCaption();
							}
							// bidi_acgc added end
							ipr.drawText(tre);
						}
					}
				}
			}

			la.getCaption().setValue(sRestoreValue);
			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_AXIS_TITLE, axModel, la,
					getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_AXIS_TITLE, la);
		}
		la = goFactory.copyOf(ax.getLabel());
	}

	private void renderVerticalAxisLine(final ComputationContext context, double dXStart, double dXEnd, double dZStart,
			double dZEnd, double dZ) throws ChartException {
		if (bRenderOrthogonal3DAxis) {
			final double dStart = daEndPoints3D[0];
			final double dEnd = daEndPoints3D[1];
			l3dre.setLineAttributes(lia);

			// center
			l3dre.setStart3D(context.dX, dStart, dZ);
			l3dre.setEnd3D(context.dX, dEnd, dZ);
			addLine3DEvent(l3dre, renderer.getRightWallEvent(), dc);

			// left
			l3dre.setStart3D(context.dX, dStart, dZEnd);
			l3dre.setEnd3D(context.dX, dEnd, dZEnd);
			addLine3DEvent(l3dre, renderer.getLeftWallEvent(), dc);

			// right
			l3dre.setStart3D(dXEnd, dStart, dZ);
			l3dre.setEnd3D(dXEnd, dEnd, dZ);
			addLine3DEvent(l3dre, renderer.getRightWallEvent(), dc);

			if (renderer.isInteractivityEnabled()) {
				Trigger tg;
				EList<Trigger> elTriggers = axModel.getTriggers();

				if (!elTriggers.isEmpty()) {
					ArrayList<Trigger> cachedTriggers = null;
					Location3D[] loaHotspot = new Location3D[4];
					Polygon3DRenderEvent pre3d = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createAxis(axModel), Polygon3DRenderEvent.class);

					// process center y-axis.
					loaHotspot[0] = goFactory.createLocation3D(context.dX - IConstants.LINE_EXPAND_DOUBLE_SIZE, dStart,
							dZ + IConstants.LINE_EXPAND_DOUBLE_SIZE);
					loaHotspot[1] = goFactory.createLocation3D(context.dX + IConstants.LINE_EXPAND_DOUBLE_SIZE, dStart,
							dZ - IConstants.LINE_EXPAND_DOUBLE_SIZE);
					loaHotspot[2] = goFactory.createLocation3D(context.dX + IConstants.LINE_EXPAND_DOUBLE_SIZE, dEnd,
							dZ - IConstants.LINE_EXPAND_DOUBLE_SIZE);
					loaHotspot[3] = goFactory.createLocation3D(context.dX - IConstants.LINE_EXPAND_DOUBLE_SIZE, dEnd,
							dZ + IConstants.LINE_EXPAND_DOUBLE_SIZE);
					pre3d.setPoints3D(loaHotspot);
					pre3d.setDoubleSided(true);

					if (renderer.get3DEngine().processEvent(pre3d, panningOffset.getX(),
							panningOffset.getY()) != null) {
						final InteractionEvent iev = ((EventObjectCache) ipr)
								.getEventObject(StructureSource.createAxis(axModel), InteractionEvent.class);
						iev.setCursor(axModel.getCursor());
						cachedTriggers = new ArrayList<Trigger>();
						for (int t = 0; t < elTriggers.size(); t++) {
							tg = goFactory.copyOf(elTriggers.get(t));
							processTrigger(tg, StructureSource.createAxis(axModel));
							cachedTriggers.add(tg);
							iev.addTrigger(goFactory.copyOf(tg));
						}

						iev.setHotSpot(pre3d);
						ipr.enableInteraction(iev);
					}

					// process left y-axis.
					pre3d = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel),
							Polygon3DRenderEvent.class);
					loaHotspot = new Location3D[4];

					loaHotspot[0] = goFactory.createLocation3D(dXStart - IConstants.LINE_EXPAND_DOUBLE_SIZE, dStart,
							dZEnd + IConstants.LINE_EXPAND_DOUBLE_SIZE);
					loaHotspot[1] = goFactory.createLocation3D(dXStart + IConstants.LINE_EXPAND_DOUBLE_SIZE, dStart,
							dZEnd - IConstants.LINE_EXPAND_DOUBLE_SIZE);
					loaHotspot[2] = goFactory.createLocation3D(dXStart + IConstants.LINE_EXPAND_DOUBLE_SIZE, dEnd,
							dZEnd - IConstants.LINE_EXPAND_DOUBLE_SIZE);
					loaHotspot[3] = goFactory.createLocation3D(dXStart - IConstants.LINE_EXPAND_DOUBLE_SIZE, dEnd,
							dZEnd + IConstants.LINE_EXPAND_DOUBLE_SIZE);
					pre3d.setPoints3D(loaHotspot);
					pre3d.setDoubleSided(true);

					if (renderer.get3DEngine().processEvent(pre3d, panningOffset.getX(),
							panningOffset.getY()) != null) {
						final InteractionEvent iev = ((EventObjectCache) ipr)
								.getEventObject(StructureSource.createAxis(axModel), InteractionEvent.class);
						iev.setCursor(axModel.getCursor());

						if (cachedTriggers == null) {
							cachedTriggers = new ArrayList<Trigger>();
							for (int t = 0; t < elTriggers.size(); t++) {
								tg = goFactory.copyOf(elTriggers.get(t));
								processTrigger(tg, StructureSource.createAxis(axModel));
								cachedTriggers.add(tg);
								iev.addTrigger(goFactory.copyOf(tg));
							}

						} else {
							for (int t = 0; t < cachedTriggers.size(); t++) {
								iev.addTrigger(goFactory.copyOf(cachedTriggers.get(t)));
							}
						}

						iev.setHotSpot(pre3d);
						ipr.enableInteraction(iev);
					}

					// process right y-axis.
					pre3d = ((EventObjectCache) ipr).getEventObject(StructureSource.createAxis(axModel),
							Polygon3DRenderEvent.class);
					loaHotspot = new Location3D[4];

					loaHotspot[0] = goFactory.createLocation3D(dXEnd - IConstants.LINE_EXPAND_DOUBLE_SIZE, dStart,
							dZStart + IConstants.LINE_EXPAND_DOUBLE_SIZE);
					loaHotspot[1] = goFactory.createLocation3D(dXEnd + IConstants.LINE_EXPAND_DOUBLE_SIZE, dStart,
							dZStart - IConstants.LINE_EXPAND_DOUBLE_SIZE);
					loaHotspot[2] = goFactory.createLocation3D(dXEnd + IConstants.LINE_EXPAND_DOUBLE_SIZE, dEnd,
							dZStart - IConstants.LINE_EXPAND_DOUBLE_SIZE);
					loaHotspot[3] = goFactory.createLocation3D(dXEnd - IConstants.LINE_EXPAND_DOUBLE_SIZE, dEnd,
							dZStart + IConstants.LINE_EXPAND_DOUBLE_SIZE);
					pre3d.setPoints3D(loaHotspot);
					pre3d.setDoubleSided(true);

					if (renderer.get3DEngine().processEvent(pre3d, panningOffset.getX(),
							panningOffset.getY()) != null) {
						final InteractionEvent iev = ((EventObjectCache) ipr)
								.getEventObject(StructureSource.createAxis(axModel), InteractionEvent.class);
						iev.setCursor(axModel.getCursor());

						if (cachedTriggers == null) {
							for (int t = 0; t < elTriggers.size(); t++) {
								tg = goFactory.copyOf(elTriggers.get(t));
								processTrigger(tg, StructureSource.createAxis(axModel));
								iev.addTrigger(tg);
							}
						} else {
							for (int t = 0; t < cachedTriggers.size(); t++) {
								iev.addTrigger(cachedTriggers.get(t));
							}
						}

						iev.setHotSpot(pre3d);
						ipr.enableInteraction(iev);
					}
				}
			}

		} else {
			double dStart = daEndPoints[0] + insCA.getBottom(), dEnd = daEndPoints[1] - insCA.getTop();

			if (sc.getDirection() == IConstants.FORWARD) {
				dStart = daEndPoints[1] + insCA.getBottom();
				dEnd = daEndPoints[0] - insCA.getTop();
			}

			if (iv != null && iv.getType() == IConstants.VALUE && iDimension == IConstants.TWO_5_D) {
				final Location[] loa = new Location[4];
				loa[0] = goFactory.createLocation(context.dX, dStart);
				loa[1] = goFactory.createLocation(context.dX + dSeriesThickness, dStart - dSeriesThickness);
				loa[2] = goFactory.createLocation(context.dX + dSeriesThickness, dEnd - dSeriesThickness);
				loa[3] = goFactory.createLocation(context.dX, dEnd);

				final PolygonRenderEvent pre = ((EventObjectCache) ipr)
						.getEventObject(StructureSource.createAxis(axModel), PolygonRenderEvent.class);
				pre.setPoints(loa);
				pre.setBackground(goFactory.createColorDefinition(255, 255, 255, 127));
				pre.setOutline(lia);
				ipr.fillPolygon(pre);
			}
			lre.setLineAttributes(lia);
			lre.getStart().set(context.dX, dStart);
			lre.getEnd().set(context.dX, dEnd);
			ipr.drawLine(lre);

			if (renderer.isInteractivityEnabled()) {
				Trigger tg;
				EList<Trigger> elTriggers = axModel.getTriggers();

				if (!elTriggers.isEmpty()) {
					final InteractionEvent iev = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createAxis(axModel), InteractionEvent.class);
					iev.setCursor(axModel.getCursor());

					for (int t = 0; t < elTriggers.size(); t++) {
						tg = goFactory.copyOf(elTriggers.get(t));
						processTrigger(tg, StructureSource.createAxis(axModel));
						iev.addTrigger(tg);
					}

					Location[] loaHotspot = new Location[4];

					loaHotspot[0] = goFactory.createLocation(context.dX - IConstants.LINE_EXPAND_SIZE, dStart);
					loaHotspot[1] = goFactory.createLocation(context.dX + IConstants.LINE_EXPAND_SIZE, dStart);
					loaHotspot[2] = goFactory.createLocation(context.dX + IConstants.LINE_EXPAND_SIZE, dEnd);
					loaHotspot[3] = goFactory.createLocation(context.dX - IConstants.LINE_EXPAND_SIZE, dEnd);

					final PolygonRenderEvent pre = ((EventObjectCache) ipr)
							.getEventObject(StructureSource.createAxis(axModel), PolygonRenderEvent.class);
					pre.setPoints(loaHotspot);
					iev.setHotSpot(pre);
					ipr.enableInteraction(iev);
				}
			}

		}
	}

	private void renderAxisTitleWith3DTextevent(BoundingBox bb) throws ChartException {
		Location lo = get3DTextLocation(t3dre);
		tre.setLocation(lo);
		tre.setTextPosition(t3dre.getTextPosition());
		tre.setLabel(la);
		limitAxisTitleLocation(tre, bb);
		tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
		// bidi_acgc added start
		if (this.renderer.rtc.isRightToLeftText()) {
			tre.setRtlCaption();
		}
		// bidi_acgc added end
		ipr.drawText(tre);
	}

	private Location get3DTextLocation(Text3DRenderEvent t3dre) throws ChartException {
		PlotWith3DAxes pwa3D = (PlotWith3DAxes) pwa;
		Engine3D engine = pwa3D.get3DEngine();
		Location lo_off = pwa3D.getPanningOffset();
		engine.processEvent_noclip(t3dre, lo_off.getX(), lo_off.getY());
		return t3dre.getLocation();
	}

	private void limitAxisTitleLocation(TextRenderEvent tre, BoundingBox bb) {
		Location lo = tre.getLocation();
		Bounds cbo = renderer.getPlotBounds();
		double xmin = cbo.getLeft();
		double ymin = cbo.getTop();
		double xmax = xmin + cbo.getWidth();
		double ymax = ymin + cbo.getHeight();

		int pos = tre.getTextPosition();

		if (pos == TextRenderEvent.RIGHT) {
			xmax -= bb.getWidth();
			ymin += bb.getHeight() / 2;
			ymax -= bb.getHeight() / 2;
		} else if (pos == TextRenderEvent.LEFT) {
			xmin += bb.getWidth();
			ymin += bb.getHeight() / 2;
			ymax -= bb.getHeight() / 2;
		} else if (pos == TextRenderEvent.ABOVE) {
			ymin += bb.getHeight();
		} else
		// ( pos == TextRenderEvent.BELOW )
		{
			ymax -= bb.getHeight();
		}

		if (lo.getX() < xmin) {
			lo.setX(xmin);
		} else if (lo.getX() > xmax) {
			lo.setX(xmax);
		}

		if (lo.getY() < ymin) {
			lo.setY(ymin);
		} else if (lo.getY() > ymax) {
			lo.setY(ymax);
		}
	}

	private double computeTopOfOrthogonalAxisTitle() {
		Bounds titleBounds = this.renderer.cm.getTitle().getBounds();
		Bounds legendBounds = this.renderer.cm.getLegend().getBounds();
		if (this.renderer.cm.getTitle().getAnchor().getValue() == Anchor.NORTH) {
			if (this.renderer.cm.getLegend().getPosition().getValue() == Position.ABOVE) {
				return (legendBounds.getTop() + legendBounds.getHeight()) / 72 * xs.getDpiResolution();
			} else {
				return (titleBounds.getTop() + titleBounds.getHeight()) / 72 * xs.getDpiResolution();
			}
		} else {
			if (this.renderer.cm.getLegend().getPosition().getValue() == Position.ABOVE) {
				return (legendBounds.getTop() + legendBounds.getHeight()) / 72 * xs.getDpiResolution();
			} else {
				return this.renderer.cm.getBlock().getBounds().getTop() / 72 * xs.getDpiResolution();
			}
		}
	}

	/**
	 * Adds Line2D event to deferred cache, if parent event is specified, this event
	 * should be added into parent event.
	 * 
	 * @param lre3d
	 * @param parentEvent
	 * @param dc
	 */
	public static void addLine3DEvent(Line3DRenderEvent lre3d, Object parentEvent, DeferredCache dc) {
		if (parentEvent != null && parentEvent instanceof WrappedInstruction) {
			if (((WrappedInstruction) parentEvent).getSubDeferredCache() == null) {
				((WrappedInstruction) parentEvent).setSubDeferredCache(dc.deriveNewDeferredCache());
			}
			((WrappedInstruction) parentEvent).getSubDeferredCache().addLine(lre3d);
		} else {
			if (parentEvent != null)
				lre3d.setObject3DParent(Engine3D.getObjectFromEvent(parentEvent));
			dc.addLine(lre3d);
		}
	}
}
