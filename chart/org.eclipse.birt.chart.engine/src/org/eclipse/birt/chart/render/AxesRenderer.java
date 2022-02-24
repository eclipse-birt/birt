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

package org.eclipse.birt.chart.render;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.LegendItemRenderingHints;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.computation.PlotComputation;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withaxes.AllAxes;
import org.eclipse.birt.chart.computation.withaxes.AutoScale;
import org.eclipse.birt.chart.computation.withaxes.AxisTickCoordinates;
import org.eclipse.birt.chart.computation.withaxes.Grid;
import org.eclipse.birt.chart.computation.withaxes.IntersectionValue;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.PlotWith2DAxes;
import org.eclipse.birt.chart.computation.withaxes.PlotWith3DAxes;
import org.eclipse.birt.chart.computation.withaxes.PlotWithAxes;
import org.eclipse.birt.chart.computation.withaxes.SeriesRenderingHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.BlockGenerationEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.I3DRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.internal.model.FittingCalculator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.StringFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.BigNumberDataElement;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataElement;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Provides a base framework for custom series rendering extensions that are
 * interested in being rendered in a pre-computed plot containing axes. Series
 * type extensions could subclass this class to participate in the axes
 * rendering framework.
 */
public abstract class AxesRenderer extends BaseRenderer {
	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/render"); //$NON-NLS-1$

	private Axis ax;

	private boolean leftWallFill = false;
	private boolean rightWallFill = false;
	private boolean floorFill = false;

	private Object3D o3dLeftWall = null;
	private Object3D o3dRightWall = null;
	private Object3D o3dFloor = null;

	private Object o3dLeftWallEvent;

	private Object o3dRightWallEvent;

	private Object o3dFloorEvent;

	/**
	 * The constructor.
	 */
	public AxesRenderer() {
		super();
	}

	public Object3D getLeftWall() {
		return o3dLeftWall;
	}

	public Object getLeftWallEvent() {
		return o3dLeftWallEvent;
	}

	public Object3D getRightWall() {
		return o3dRightWall;
	}

	public Object getRightWallEvent() {
		return o3dRightWallEvent;
	}

	public Object3D getFloor() {
		return o3dFloor;
	}

	public Object getFloorEvent() {
		return o3dFloorEvent;
	}

	/**
	 * Overridden behavior for graphic element series that are plotted along axes
	 * 
	 * @param bo
	 */
	@Override
	public final void render(Map<Series, LegendItemRenderingHints> htRenderers, Bounds bo) throws ChartException {
		final boolean bFirstInSequence = (iSeriesIndex == 0);
		final boolean bLastInSequence = (iSeriesIndex == iSeriesCount - 1);
		final Chart cm = getModel();
		final IDeviceRenderer idr = getDevice();
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();

		if (bFirstInSequence) // SEQUENCE OF MULTIPLE SERIES RENDERERS
		// (POSSIBLY PARTICIPATING IN A COMBINATION CHART)
		{
			// RENDER THE CHART BY WALKING THROUGH THE RECURSIVE BLOCK STRUCTURE
			Block bl = cm.getBlock();
			final Enumeration<Block> e = bl.children(true);
			final BlockGenerationEvent bge = new BlockGenerationEvent(bl);

			// ALWAYS RENDER THE OUTERMOST BLOCK FIRST
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
			bge.updateBlock(bl);
			renderChartBlock(idr, bl, StructureSource.createChartBlock(bl));
			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);

			while (e.hasMoreElements()) {
				bl = e.nextElement();

				bge.updateBlock(bl);
				if (bl instanceof Plot) {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderPlot(idr, (Plot) bl);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
					if (!bLastInSequence) {
						// STOP AT THE PLOT IF NOT ALSO THE LAST IN THE
						// SEQUENCE
						break;
					}
				} else if (bl instanceof TitleBlock) {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderTitle(idr, (TitleBlock) bl);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				} else if (bl instanceof LabelBlock) {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderLabel(idr, bl, StructureSource.createUnknown(bl));
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				} else if (bl instanceof Legend) {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderLegend(idr, (Legend) bl, htRenderers);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				} else {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderBlock(idr, bl, StructureSource.createUnknown(bl));
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				}
			}
		} else if (bLastInSequence) {
			Block bl = cm.getBlock();
			final Enumeration<Block> e = bl.children(true);
			final BlockGenerationEvent bge = new BlockGenerationEvent(this);

			boolean bStarted = false;
			while (e.hasMoreElements()) {
				bl = e.nextElement();
				if (!bStarted && !bl.isPlot()) {
					continue; // IGNORE ALL BLOCKS UNTIL PLOT IS ENCOUNTERED
				}
				bStarted = true;

				bge.updateBlock(bl);
				if (bl instanceof Plot) {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderPlot(idr, (Plot) bl);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				} else if (bl instanceof TitleBlock) {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderTitle(idr, (TitleBlock) bl);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				} else if (bl instanceof LabelBlock) {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderLabel(idr, bl, StructureSource.createUnknown(bl));
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				} else if (bl instanceof Legend) {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderLegend(idr, (Legend) bl, htRenderers);
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				} else {
					ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
					renderBlock(idr, bl, StructureSource.createUnknown(bl));
					ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
							getRunTimeContext().getScriptContext());
					getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				}
			}
		} else {
			// FOR ALL SERIES IN-BETWEEN, ONLY RENDER THE PLOT
			final BlockGenerationEvent bge = new BlockGenerationEvent(this);
			Plot p = cm.getPlot();
			bge.updateBlock(p);
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, p, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, p);
			renderPlot(idr, p);
			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, p, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, p);
		}

		if (bLastInSequence) {
			Object obj = getComputations();

			if (obj instanceof PlotWith2DAxes) {
				final PlotWith2DAxes pw2da = (PlotWith2DAxes) getComputations();
				pw2da.getStackedSeriesLookup().resetSubUnits();
			}
		}
	}

	private final int compare(DataElement de1, DataElement de2) throws ChartException {
		if (de1 == null && de2 == null)
			return IConstants.EQUAL;
		if (de1 == null || de2 == null)
			return IConstants.SOME_NULL;
		final Class<? extends DataElement> c1 = de1.getClass();
		final Class<? extends DataElement> c2 = de2.getClass();
		if (c1.equals(c2)) {
			if (de1 instanceof BigNumberDataElement) {
				return (((BigNumberDataElement) de1).getValue().compareTo(((BigNumberDataElement) de2).getValue()));
			} else if (de1 instanceof NumberDataElement) {
				return Double.compare(((NumberDataElement) de1).getValue(), ((NumberDataElement) de2).getValue());
			} else if (de1 instanceof DateTimeDataElement) {
				final long l1 = ((DateTimeDataElement) de1).getValue();
				final long l2 = ((DateTimeDataElement) de1).getValue();
				return (l1 < l2 ? IConstants.LESS : (l1 == l2 ? IConstants.EQUAL : IConstants.MORE));

			} else if (de1 instanceof TextDataElement) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
						"exception.unsupported.compare.text", //$NON-NLS-1$
						Messages.getResourceBundle(getRunTimeContext().getULocale()));
			} else {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
						"exception.unsupported.compare.unknown.objects", //$NON-NLS-1$
						new Object[] { de1, de2 }, Messages.getResourceBundle(getRunTimeContext().getULocale())); // i18n_CONCATENATIONS_REMOVED
			}
		}
		throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_FORMAT,
				"exception.unsupported.compare.different.objects", //$NON-NLS-1$
				new Object[] { de1, de2 }, Messages.getResourceBundle(getRunTimeContext().getULocale())); // i18n_CONCATENATIONS_REMOVED
	}

	private static final TextAlignment anchorToAlignment(Anchor anc) {
		final TextAlignment ta = TextAlignmentImpl.create(); // SET AS
		// CENTERED
		// HORZ/VERT
		if (anc == null) {
			return ta;
		}

		// SETUP VERTICAL ALIGNMENT
		switch (anc.getValue()) {
		case Anchor.NORTH:
		case Anchor.NORTH_EAST:
		case Anchor.NORTH_WEST:
			ta.setVerticalAlignment(VerticalAlignment.TOP_LITERAL);
			break;
		case Anchor.SOUTH:
		case Anchor.SOUTH_EAST:
		case Anchor.SOUTH_WEST:
			ta.setVerticalAlignment(VerticalAlignment.BOTTOM_LITERAL);
			break;
		default:
			ta.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
		}

		// SETUP HORIZONTAL ALIGNMENT
		switch (anc.getValue()) {
		case Anchor.EAST:
		case Anchor.NORTH_EAST:
		case Anchor.SOUTH_EAST:
			ta.setHorizontalAlignment(HorizontalAlignment.RIGHT_LITERAL);
			break;
		case Anchor.WEST:
		case Anchor.NORTH_WEST:
		case Anchor.SOUTH_WEST:
			ta.setHorizontalAlignment(HorizontalAlignment.LEFT_LITERAL);
			break;
		default:
			ta.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
		}

		return ta;
	}

	private static class FittingCurveHelper {

		private final double[] baseArray;
		private final double[] orthogonalArray;

		private FittingCurveHelper(double[][] sa, int iSize) {
			iSize = Math.min(iSize, sa.length);
			baseArray = new double[iSize];
			orthogonalArray = new double[iSize];

			for (int i = 0; i < iSize; i++) {
				baseArray[i] = sa[i][0];
				orthogonalArray[i] = sa[i][1];
			}
		}

		public double[] getBaseArray() {
			return baseArray;
		}

		public double[] getOrthogonalArray() {
			return orthogonalArray;
		}

		public static FittingCurveHelper instance(double[] xArray, double[] yArray, boolean isTransposed) {
			double[][] sa = sort(xArray, yArray, isTransposed);
			int iSize = mergeBase(sa);

			return new FittingCurveHelper(sa, iSize);
		}

		private static int mergeBase(double[][] sa) {
			int iLen = sa.length;
			int iDst = 0;

			for (int iSrc = 0; iSrc < iLen;) {
				int iEq = 1;
				double dOrthTotal = sa[iSrc][1];
				double dBase = sa[iSrc][0];

				while (iSrc + iEq < iLen && sa[iSrc + iEq][0] == dBase) {
					dOrthTotal += sa[iSrc + iEq][1];
					iEq++;
				}

				sa[iDst][1] = dOrthTotal / iEq;
				sa[iDst][0] = dBase;
				iDst++;
				iSrc += iEq;
			}

			return iDst;
		}

		private static double[][] sort(double[] a, double[] b, final boolean isTransposed) {
			double[] baseArray = isTransposed ? b : a;
			double[] orthogonalArray = isTransposed ? a : b;

			double[][] sa = new double[a.length][2];

			for (int i = 0; i < a.length; i++) {
				double[] ca = new double[2];

				ca[0] = baseArray[i];
				ca[1] = orthogonalArray[i];
				sa[i] = ca;
			}

			Arrays.sort(sa, new Comparator<double[]>() {

				public int compare(double[] l1, double[] l2) {
					if (l1[0] == l2[0]) {
						return 0;
					}

					if (l1[0] < l2[0]) {
						return -1;
					}

					return 1;
				}
			});

			return sa;
		}

	}

	/**
	 * Renders the FittingCurve if defined for supported series.
	 * 
	 * @param ipr
	 * @param points
	 * @param curve
	 * @param bDeferred
	 * @throws ChartException
	 */
	protected final void renderFittingCurve(IPrimitiveRenderer ipr, Location[] points, CurveFitting curve,
			boolean bShowAsTape, boolean bDeferred) throws ChartException {
		boolean isTransposed = ((ChartWithAxes) getModel()).isTransposed();

		if (curve.getLineAttributes().isVisible()) {
			ScriptHandler.callFunction(getRunTimeContext().getScriptHandler(), ScriptHandler.BEFORE_DRAW_FITTING_CURVE,
					curve, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_FITTING_CURVE, curve);

			// Render curve.
			double[] xArray = new double[points.length];
			double[] yArray = new double[points.length];

			for (int i = 0; i < xArray.length; i++) {
				xArray[i] = points[i].getX();
				yArray[i] = points[i].getY();
			}

			FittingCurveHelper fch = FittingCurveHelper.instance(xArray, yArray, isTransposed);
			double[] baseArray = fch.getBaseArray();
			double[] orthogonalArray = fch.getOrthogonalArray();

			FittingCalculator fc = new FittingCalculator(baseArray, orthogonalArray, 0.33);

			double[] fitYarray = fc.getFittedValue();

			orthogonalArray = fitYarray;

			if (isTransposed) {
				orthogonalArray = baseArray;
				baseArray = fitYarray;
			}

			if (curve.getLineAttributes().getColor() != null) {
				CurveRenderer crdr = new CurveRenderer((ChartWithAxes) getModel(), this, curve.getLineAttributes(),
						goFactory.createLocations(baseArray, orthogonalArray), bShowAsTape, -1, bDeferred, false, null,
						false, true);
				crdr.draw(ipr);
			}

			// Render curve label.
			if (curve.getLabel().isVisible()) {
				Label lb = goFactory.copyOf(curve.getLabel());

				// handle external resource string
				final String sPreviousValue = lb.getCaption().getValue();
				lb.getCaption().setValue(getRunTimeContext().externalizedMessage(sPreviousValue));

				BoundingBox bb = cComp.computeBox(getXServer(), IConstants.LEFT/* DONT-CARE */, lb, 0, 0);

				Anchor lbAnchor = curve.getLabelAnchor();

				if (lbAnchor == null) {
					lbAnchor = Anchor.NORTH_LITERAL;
				}

				int horizontal = IConstants.CENTER;
				int vertical = IConstants.ABOVE;

				// convert anchor to position.
				switch (lbAnchor.getValue()) {
				case Anchor.WEST:
				case Anchor.NORTH_WEST:
				case Anchor.SOUTH_WEST:
					horizontal = IConstants.LEFT;
					break;
				case Anchor.NORTH:
				case Anchor.SOUTH:
					horizontal = IConstants.CENTER;
					break;
				case Anchor.EAST:
				case Anchor.NORTH_EAST:
				case Anchor.SOUTH_EAST:
					horizontal = IConstants.RIGHT;
					break;
				}

				switch (lbAnchor.getValue()) {
				case Anchor.NORTH:
				case Anchor.NORTH_WEST:
				case Anchor.NORTH_EAST:
				case Anchor.WEST:
				case Anchor.EAST:
					vertical = IConstants.ABOVE;
					break;
				case Anchor.SOUTH:
				case Anchor.SOUTH_WEST:
				case Anchor.SOUTH_EAST:
					vertical = IConstants.BELOW;
					break;
				}

				double xs, ys;

				if (isTransposed) {
					if (horizontal == IConstants.LEFT) {
						ys = orthogonalArray[orthogonalArray.length - 1] - bb.getHeight();
						// switch left/right
						horizontal = IConstants.RIGHT;
					} else if (horizontal == IConstants.RIGHT) {
						ys = orthogonalArray[0];
						// switch left/right
						horizontal = IConstants.LEFT;
					} else {
						ys = orthogonalArray[0]
								+ (orthogonalArray[orthogonalArray.length - 1] - orthogonalArray[0]) / 2d
								- bb.getHeight() / 2d;
					}

					xs = getFitYPosition(orthogonalArray, baseArray, horizontal, bb.getHeight(), bb.getWidth(),
							vertical == IConstants.BELOW);
				} else {
					if (horizontal == IConstants.LEFT) {
						xs = xArray[0];
					} else if (horizontal == IConstants.RIGHT) {
						xs = xArray[xArray.length - 1] - bb.getWidth();
					} else {
						xs = xArray[0] + (xArray[xArray.length - 1] - xArray[0]) / 2d - bb.getWidth() / 2d;
					}

					ys = getFitYPosition(xArray, fitYarray, horizontal, bb.getWidth(), bb.getHeight(),
							vertical == IConstants.ABOVE);
				}

				bb.setLeft(xs);
				bb.setTop(ys);

				// Bugzilla bug 182675
				// Remove code below to avoid painting additional shadow of
				// label. This code is not needed, the shadow of label will be
				// painted when painting whole label. - Henry
				// ***
//				if ( ChartUtil.isShadowDefined( lb ) )
//				{
//					renderLabel( StructureSource.createSeries( getSeries( ) ),
//							TextRenderEvent.RENDER_SHADOW_AT_LOCATION,
//							lb,
//							Position.RIGHT_LITERAL,
//							LocationImpl.create( bb.getLeft( ), bb.getTop( ) ),
				// goFactory.createBounds( bb.getLeft( ),
//									bb.getTop( ),
//									bb.getWidth( ),
//									bb.getHeight( ) ) );
//				}
				// ***

				renderLabel(StructureSource.createSeries(getSeries()), TextRenderEvent.RENDER_TEXT_IN_BLOCK, lb,
						Position.RIGHT_LITERAL, goFactory.createLocation(bb.getLeft(), bb.getTop()),
						goFactory.createBounds(bb.getLeft(), bb.getTop(), bb.getWidth(), bb.getHeight()),
						fDeferredCacheManager.getLastDeferredCache());

			}

			ScriptHandler.callFunction(getRunTimeContext().getScriptHandler(), ScriptHandler.AFTER_DRAW_FITTING_CURVE,
					curve, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_FITTING_CURVE, curve);

		}

	}

	/**
	 * 
	 * @param xa     xa must be sorted from smallest to largest.
	 * @param ya
	 * @param center
	 * @param width
	 * @param height
	 */
	private double getFitYPosition(double[] xa, double[] ya, int align, double width, double height, boolean above) {
		int gap = 10;

		double rt = 0;

		if (align == IConstants.LEFT) {
			rt = ya[0];
		} else if (align == IConstants.RIGHT) {
			rt = ya[ya.length - 1];
		} else {
			if (ya.length % 2 == 1) {
				rt = ya[ya.length / 2];
			} else {
				int x = ya.length / 2;
				rt = (ya[x] + ya[x - 1]) / 2;
			}
		}

		return above ? (rt - height - gap) : (rt + gap);
	}

	/**
	 * Renders all marker ranges associated with all axes (base and orthogonal) in
	 * the plot Marker ranges are drawn immediately (not rendered as deferred) at an
	 * appropriate Z-order immediately after the plot background is drawn.
	 * 
	 * @param oaxa             An array containing all axes
	 * @param boPlotClientArea The bounds of the actual client area
	 * 
	 * @throws ChartException
	 */
	private final void renderMarkerRanges(OneAxis[] oaxa, Bounds boPlotClientArea) throws ChartException {
		Axis ax;
		int iRangeCount, iAxisCount = oaxa.length;
		MarkerRange mr;
		RectangleRenderEvent rre;
		DataElement deStart, deEnd;
		AutoScale asc;
		double dMin = 0, dMax = 0;
		int iOrientation, iCompare = IConstants.EQUAL;

		final Bounds bo = goFactory.createBounds(0, 0, 0, 0);
		final IDeviceRenderer idr = getDevice();
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		final boolean bTransposed = ((ChartWithAxes) getModel()).isTransposed();
		final PlotWithAxes pwa = (PlotWithAxes) getComputations();
		final StringBuffer sb = new StringBuffer();
		Bounds boText = goFactory.createBounds(0, 0, 0, 0);
		Anchor anc = null;
		Label la = null;
		TextRenderEvent tre;
		double dOriginalAngle = 0;

		for (int i = 0; i < iAxisCount; i++) {
			ax = oaxa[i].getModelAxis();
			iOrientation = ax.getOrientation().getValue();
			if (bTransposed) // TOGGLE ORIENTATION
			{
				iOrientation = (iOrientation == Orientation.HORIZONTAL) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
			}

			asc = oaxa[i].getScale();
			EList<MarkerRange> el = ax.getMarkerRanges();
			iRangeCount = el.size();

			for (int j = 0; j < iRangeCount; j++) {
				mr = el.get(j);
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_MARKER_RANGE, ax, mr,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_MARKER_RANGE, mr);

				deStart = mr.getStartValue();
				deEnd = mr.getEndValue();
				try {
					iCompare = compare(deStart, deEnd);
				} catch (ChartException dfex) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, dfex);
				}

				// IF OUT OF ORDER, SWAP
				if (iCompare == IConstants.MORE) {
					final DataElement deTemp = deStart;
					deStart = deEnd;
					deEnd = deTemp;
				}

				if (isDimension3D()) {
					// TODO render 3D marker range
					return;
				}

				// COMPUTE THE START BOUND
				try {
					dMin = (deStart == null)
							? ((iOrientation == Orientation.HORIZONTAL) ? boPlotClientArea.getLeft()
									: boPlotClientArea.getTop() + boPlotClientArea.getHeight())
							: Methods.getLocation(asc, deStart);
				} catch (Exception ex) {
					logger.log(ILogger.WARNING, Messages.getString("exception.cannot.locate.start.marker.range", //$NON-NLS-1$
							new Object[] { deStart, mr }, getRunTimeContext().getULocale()));
					continue; // TRY NEXT MARKER RANGE
				}

				// COMPUTE THE END BOUND
				try {
					dMax = (deEnd == null)
							? ((iOrientation == Orientation.HORIZONTAL)
									? boPlotClientArea.getLeft() + boPlotClientArea.getWidth()
									: boPlotClientArea.getTop())
							: Methods.getLocation(asc, deEnd);
				} catch (Exception ex) {
					logger.log(ILogger.WARNING, Messages.getString("exception.cannot.locate.end.marker.range", //$NON-NLS-1$
							new Object[] { deEnd, mr }, getRunTimeContext().getULocale()));
					continue; // TRY NEXT MARKER RANGE
				}

				rre = ((EventObjectCache) idr).getEventObject(StructureSource.createMarkerRange(mr),
						RectangleRenderEvent.class);

				if (iOrientation == Orientation.HORIZONTAL) {
					double maxLimit = boPlotClientArea.getLeft() + boPlotClientArea.getWidth();
					double minLimit = boPlotClientArea.getLeft();
					if (pwa.getDimension() == IConstants.TWO_5_D) {
						maxLimit -= pwa.getSeriesThickness();
						minLimit -= pwa.getSeriesThickness();
					}

					dMax = Math.min(dMax, maxLimit);
					dMin = Math.min(dMin, maxLimit);

					dMax = Math.max(dMax, minLimit);
					dMin = Math.max(dMin, minLimit);

					bo.set(dMin, boPlotClientArea.getTop(), dMax - dMin, boPlotClientArea.getHeight());
				} else {
					double minLimit = boPlotClientArea.getTop();
					double maxLimit = boPlotClientArea.getTop() + boPlotClientArea.getHeight();
					if (pwa.getDimension() == IConstants.TWO_5_D) {
						maxLimit += pwa.getSeriesThickness();
						minLimit += pwa.getSeriesThickness();
					}

					dMax = Math.min(dMax, maxLimit);
					dMin = Math.min(dMin, maxLimit);

					dMax = Math.max(dMax, minLimit);
					dMin = Math.max(dMin, minLimit);

					bo.set(boPlotClientArea.getLeft(), dMax, boPlotClientArea.getWidth(), dMin - dMax);
				}

				if (pwa.getDimension() == IConstants.TWO_5_D) {
					if (iOrientation == Orientation.HORIZONTAL) {
						bo.translate(pwa.getSeriesThickness(), 0);
					} else {
						bo.translate(0, -pwa.getSeriesThickness());
					}
				}

				// DRAW THE MARKER RANGE (RECTANGULAR AREA)
				rre.setBounds(bo);
				rre.setOutline(mr.getOutline());
				rre.setBackground(mr.getFill());
				idr.fillRectangle(rre);
				idr.drawRectangle(rre);

				la = goFactory.copyOf(mr.getLabel());
				if (la.isVisible()) {
					if (la.getCaption().getValue() != null
							&& !IConstants.UNDEFINED_STRING.equals(la.getCaption().getValue())
							&& la.getCaption().getValue().length() > 0) {
						la.getCaption()
								.setValue(oaxa[i].getRunTimeContext().externalizedMessage(la.getCaption().getValue()));
					} else {
						try {
							sb.delete(0, sb.length());
							sb.append(Messages.getString("prefix.marker.range.caption", //$NON-NLS-1$
									getRunTimeContext().getULocale()));
							sb.append(ValueFormatter.format(deStart, mr.getFormatSpecifier(),
									oaxa[i].getRunTimeContext().getULocale(),
									getDataElementDefaultFormat(deStart, asc)));
							sb.append(Messages.getString("separator.marker.range.caption", //$NON-NLS-1$
									getRunTimeContext().getULocale()));
							sb.append(ValueFormatter.format(deEnd, mr.getFormatSpecifier(),
									oaxa[i].getRunTimeContext().getULocale(), getDataElementDefaultFormat(deEnd, asc)));
							sb.append(Messages.getString("suffix.marker.range.caption", //$NON-NLS-1$
									getRunTimeContext().getULocale()));
							la.getCaption().setValue(sb.toString());
						} catch (ChartException dfex) {
							throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, dfex);
						}
					}

					// DETERMINE THE LABEL ANCHOR (TRANSPOSE IF NEEDED)
					anc = switchAnchor(mr.getLabelAnchor());
					if (bTransposed) {
						dOriginalAngle = la.getCaption().getFont().getRotation();
						try {
							la.getCaption().getFont().setRotation(pwa.getTransposedAngle(dOriginalAngle));
							anc = ChartUtil.transposeAnchor(anc);
						} catch (IllegalArgumentException uiex) {
							throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, uiex);
						}
					}

					BoundingBox bb = null;
					try {
						bb = cComp.computeBox(idr.getDisplayServer(), IConstants.LEFT, la, 0, 0);
					} catch (IllegalArgumentException uiex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, uiex);
					}

					boText.set(0, 0, bb.getWidth(), bb.getHeight());

					// NOW THAT WE COMPUTED THE BOUNDS, RENDER THE ACTUAL TEXT
					tre = ((EventObjectCache) idr).getEventObject(StructureSource.createMarkerRange(mr),
							TextRenderEvent.class);
					tre.setBlockBounds(bo);
					tre.setBlockAlignment(anchorToAlignment(anc));
					tre.setLabel(la);
					tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
					getDeferredCache().addLabel(tre);
				}

				if (isInteractivityEnabled()) {
					Trigger tg;
					EList<Trigger> elTriggers = mr.getTriggers();

					if (!elTriggers.isEmpty()) {
						final InteractionEvent iev = ((EventObjectCache) idr)
								.getEventObject(StructureSource.createMarkerRange(mr), InteractionEvent.class);
						iev.setCursor(mr.getCursor());

						for (int t = 0; t < elTriggers.size(); t++) {
							tg = goFactory.copyOf(elTriggers.get(t));
							processTrigger(tg, StructureSource.createMarkerRange(mr));
							iev.addTrigger(tg);
						}

						iev.setHotSpot(rre);
						idr.enableInteraction(iev);
					}
				}

				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_MARKER_RANGE, ax, mr,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_MARKER_RANGE, mr);
			}
		}
	}

	private OneAxis[] getAllOneAxes() {
		PlotWithAxes pwa = (PlotWithAxes) getComputations();
		AllAxes aax = pwa.getAxes();
		OneAxis[] oaxa = new OneAxis[2 + aax.getOverlayCount() + (aax.getAncillaryBase() != null ? 1 : 0)];
		oaxa[0] = aax.getPrimaryBase();
		oaxa[1] = aax.getPrimaryOrthogonal();
		for (int i = 0; i < aax.getOverlayCount(); i++) {
			oaxa[2 + i] = aax.getOverlay(i);
		}
		if (aax.getAncillaryBase() != null) {
			oaxa[2 + aax.getOverlayCount()] = aax.getAncillaryBase();
		}
		return oaxa;
	}

	/**
	 * This background is the first component rendered within the plot block. This
	 * is rendered with Z-order=0
	 */
	@Override
	protected void renderBackground(IPrimitiveRenderer ipr, Plot p) throws ChartException {
		// PLOT BLOCK STUFF
		super.renderBackground(ipr, p);

		final ChartWithAxes cwa = (ChartWithAxes) getModel();
		final PlotWithAxes pwa = (PlotWithAxes) getComputations();

		// PLOT CLIENT AREA
		final ClientArea ca = p.getClientArea();
		if (!ca.isVisible()) {
			return;
		}
		Bounds bo = pwa.getPlotBounds();
		final RectangleRenderEvent rre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
				RectangleRenderEvent.class);

		if (!isDimension3D()) {
			// render client area shadow
			if (ca.getShadowColor() != null) {
				rre.setBounds(goFactory.translateBounds(bo, 3, 3));
				rre.setBackground(ca.getShadowColor());
				ipr.fillRectangle(rre);
			}

			// render client area
			rre.setBounds(bo);
			rre.setOutline(ca.getOutline());
			rre.setBackground(ca.getBackground());
			ipr.fillRectangle(rre);
		}

		// NOW THAT THE AXES HAVE BEEN COMPUTED, FILL THE INTERNAL PLOT AREA
		double dSeriesThickness = pwa.getSeriesThickness();
		double[] daX = { bo.getLeft() - dSeriesThickness, bo.getLeft() + bo.getWidth() - dSeriesThickness };
		double[] daY = { bo.getTop() + bo.getHeight() + dSeriesThickness, bo.getTop() + dSeriesThickness };

		final AllAxes aax = pwa.getAxes();
		AutoScale scPrimaryBase = null;
		AutoScale scPrimaryOrthogonal = null;
		AutoScale scAncillaryBase = null;
		double dXStart = 0;
		double dYStart = 0;
		double dZStart = 0;
		double dXEnd = 0;
		double dYEnd = 0;
		double dZEnd = 0;
		int baseTickCount = 0;
		int ancillaryTickCount = 0;
		int orthogonalTickCount = 0;
		double xStep = 0;
		double yStep = 0;
		double zStep = 0;
		// Location panningOffset = null;

		if (isDimension3D()) {
			scPrimaryBase = aax.getPrimaryBase().getScale();
			scPrimaryOrthogonal = aax.getPrimaryOrthogonal().getScale();
			scAncillaryBase = aax.getAncillaryBase().getScale();

			dXStart = scPrimaryBase.getStart();
			dYStart = scPrimaryOrthogonal.getStart();
			dZStart = scAncillaryBase.getStart();

			dXEnd = scPrimaryBase.getEnd();
			dYEnd = scPrimaryOrthogonal.getEnd();
			dZEnd = scAncillaryBase.getEnd();

			baseTickCount = scPrimaryBase.getTickCordinates().size();
			ancillaryTickCount = scAncillaryBase.getTickCordinates().size();
			orthogonalTickCount = scPrimaryOrthogonal.getTickCordinates().size();

			xStep = scPrimaryBase.getUnitSize();
			yStep = scPrimaryOrthogonal.getUnitSize();
			zStep = scAncillaryBase.getUnitSize();

			// panningOffset = getPanningOffset( );
		}

		if (pwa.getDimension() == IConstants.TWO_5_D) {
			Location[] loa = null;

			// DRAW THE LEFT WALL
			if (cwa.getWallFill() == null) {
				renderPlane(ipr, StructureSource.createPlot(p),
						new Location[] { goFactory.createLocation(daX[0], daY[0]),
								goFactory.createLocation(daX[0], daY[1]) },
						ca.getBackground(), ca.getOutline(), cwa.getDimension(), dSeriesThickness, false);
			} else {
				loa = new Location[4];
				loa[0] = goFactory.createLocation(daX[0], daY[0]);
				loa[1] = goFactory.createLocation(daX[0], daY[1]);
				loa[2] = goFactory.createLocation(daX[0] + dSeriesThickness, daY[1] - dSeriesThickness);
				loa[3] = goFactory.createLocation(daX[0] + dSeriesThickness, daY[0] - dSeriesThickness);
				final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
						PolygonRenderEvent.class);
				pre.setPoints(loa);
				pre.setBackground(cwa.getWallFill());
				pre.setOutline(ca.getOutline());
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}

			// DRAW THE FLOOR
			if (cwa.getFloorFill() == null) {
				renderPlane(ipr, StructureSource.createPlot(p),
						new Location[] { goFactory.createLocation(daX[0], daY[0]),
								goFactory.createLocation(daX[1], daY[0]) },
						ca.getBackground(), ca.getOutline(), cwa.getDimension(), dSeriesThickness, false);
			} else {
				if (loa == null) {
					loa = new Location[4];
				}
				loa[0] = goFactory.createLocation(daX[0], daY[0]);
				loa[1] = goFactory.createLocation(daX[1], daY[0]);
				loa[2] = goFactory.createLocation(daX[1] + dSeriesThickness, daY[0] - dSeriesThickness);
				loa[3] = goFactory.createLocation(daX[0] + dSeriesThickness, daY[0] - dSeriesThickness);
				final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
						PolygonRenderEvent.class);
				pre.setPoints(loa);
				pre.setBackground(cwa.getFloorFill());
				pre.setOutline(ca.getOutline());
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
		} else if (pwa.getDimension() == IConstants.THREE_D) {
			Location3D[] loa = null;

			final Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
					Polygon3DRenderEvent.class);
			pre.setDoubleSided(true);

			// DRAW THE WALL
			loa = new Location3D[4];

			// Left Wall
			loa[0] = goFactory.createLocation3D(dXStart, dYStart, dZStart);
			loa[1] = goFactory.createLocation3D(dXStart, dYEnd, dZStart);
			loa[2] = goFactory.createLocation3D(dXStart, dYEnd, dZEnd);
			loa[3] = goFactory.createLocation3D(dXStart, dYStart, dZEnd);
			pre.setPoints3D(loa);
			pre.setBackground(cwa.getWallFill());
			pre.setDoubleSided(true);
			pre.setOutline(ca.getOutline());
			Object event_new = getDeferredCache().addPlane(pre, PrimitiveRenderEvent.DRAW | PrimitiveRenderEvent.FILL);

			o3dLeftWall = Engine3D.getObjectFromEvent(event_new);
			o3dLeftWallEvent = event_new;

			// Right Wall
			loa[0] = goFactory.createLocation3D(dXStart, dYStart, dZStart);
			loa[1] = goFactory.createLocation3D(dXEnd, dYStart, dZStart);
			loa[2] = goFactory.createLocation3D(dXEnd, dYEnd, dZStart);
			loa[3] = goFactory.createLocation3D(dXStart, dYEnd, dZStart);
			pre.setPoints3D(loa);
			pre.setBackground(cwa.getWallFill());
			pre.setDoubleSided(true);
			pre.setOutline(ca.getOutline());
			event_new = getDeferredCache().addPlane(pre, PrimitiveRenderEvent.DRAW | PrimitiveRenderEvent.FILL);

			o3dRightWall = Engine3D.getObjectFromEvent(event_new);
			o3dRightWallEvent = event_new;

			if ((cwa.getWallFill() instanceof ColorDefinition
					&& ((ColorDefinition) cwa.getWallFill()).getTransparency() > 0)
					|| (!(cwa.getWallFill() instanceof ColorDefinition) && cwa.getWallFill() != null)) {
				leftWallFill = true;
				rightWallFill = true;
			}

			// DRAW THE FLOOR
			loa[0] = goFactory.createLocation3D(dXStart, dYStart, dZStart);
			loa[1] = goFactory.createLocation3D(dXStart, dYStart, dZEnd);
			loa[2] = goFactory.createLocation3D(dXEnd, dYStart, dZEnd);
			loa[3] = goFactory.createLocation3D(dXEnd, dYStart, dZStart);
			pre.setPoints3D(loa);
			pre.setBackground(cwa.getFloorFill());
			pre.setDoubleSided(true);
			pre.setOutline(ca.getOutline());
			event_new = getDeferredCache().addPlane(pre, PrimitiveRenderEvent.DRAW | PrimitiveRenderEvent.FILL);

			o3dFloor = Engine3D.getObjectFromEvent(event_new);
			o3dFloorEvent = event_new;
			if ((cwa.getFloorFill() instanceof ColorDefinition
					&& ((ColorDefinition) cwa.getFloorFill()).getTransparency() > 0)
					|| (!(cwa.getFloorFill() instanceof ColorDefinition) && cwa.getFloorFill() != null)) {
				floorFill = true;
			}
		}

		// SETUP AXIS ARRAY
		final OneAxis[] oaxa = getAllOneAxes();

		// RENDER MARKER RANGES (MARKER LINES ARE DRAWN LATER)
		renderMarkerRanges(oaxa, bo);

		// RENDER MARKER LINES
		// MarkerLines will be drawn at the foreground.
		// renderMarkerLines( oaxa, bo );

		// RENDER GRID LINES (MAJOR=DONE; MINOR=DONE)
		double x = 0, y = 0, vnext = 0;
		LineAttributes lia;
		LineRenderEvent lre;
//		final Insets insCA = aax.getInsets( );

		// RENDER MINOR GRID LINES FIRST
		int iCount;
		Grid g;
		double[] doaMinor = null;
		for (int i = 0; i < oaxa.length; i++) {
			g = oaxa[i].getGrid();
			iCount = g.getMinorCountPerMajor();

			lia = oaxa[i].getGrid().getLineAttributes(IConstants.MINOR);
			if (lia == null || !lia.isVisible()) {
				continue;
			}

			if (iCount <= 0) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, "exception.cannot.split.major", //$NON-NLS-1$
						new Object[] { Integer.valueOf(iCount) },
						Messages.getResourceBundle(getRunTimeContext().getULocale()));
			}

			AutoScale sc = oaxa[i].getScale();
			doaMinor = sc.getMinorCoordinates(iCount);

			if (isDimension3D()) {
				Line3DRenderEvent lre3d = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
						Line3DRenderEvent.class);
				lre3d.setLineAttributes(lia);

				switch (oaxa[i].getAxisType()) {
				case IConstants.BASE_AXIS:

					AxisTickCoordinates xa = scPrimaryBase.getTickCordinates();
					if (floorFill) {
						for (int k = 0; k < xa.size() - 1; k++) {
							for (int j = 0; j < doaMinor.length - 1; j++) {
								if (ChartUtil.mathGE(xa.getCoordinate(k) + doaMinor[j], xa.getCoordinate(k + 1))) {
									// if current minor tick exceeds the
									// range of current unit, skip
									continue;
								}

								lre3d.setStart3D(goFactory.createLocation3D(xa.getCoordinate(k) + doaMinor[j], dYStart,
										dZStart));
								lre3d.setEnd3D(goFactory.createLocation3D(xa.getCoordinate(k) + doaMinor[j], dYStart,
										dZStart + (ancillaryTickCount - 1) * zStep));
								AxesRenderHelper.addLine3DEvent(lre3d, o3dFloorEvent, getDeferredCache());
							}
						}
					}

					if (rightWallFill) {
						for (int k = 0; k < xa.size() - 1; k++) {
							for (int j = 0; j < doaMinor.length - 1; j++) {
								if (ChartUtil.mathGE(xa.getCoordinate(k) + doaMinor[j], xa.getCoordinate(k + 1))) {
									// if current minor tick exceeds the
									// range of current unit, skip
									continue;
								}

								lre3d.setStart3D(goFactory.createLocation3D(xa.getCoordinate(k) + doaMinor[j], dYStart,
										dZStart));
								lre3d.setEnd3D(goFactory.createLocation3D(xa.getCoordinate(k) + doaMinor[j],
										dYStart + (orthogonalTickCount - 1) * yStep, dZStart));
								AxesRenderHelper.addLine3DEvent(lre3d, o3dRightWallEvent, getDeferredCache());
							}
						}
					}
					break;
				case IConstants.ORTHOGONAL_AXIS:
					AxisTickCoordinates ya = scPrimaryOrthogonal.getTickCordinates();
					if (leftWallFill) {
						for (int k = 0; k < ya.size() - 1; k++) {
							for (int j = 0; j < doaMinor.length - 1; j++) {
								if (ChartUtil.mathGE(ya.getCoordinate(k) + doaMinor[j], ya.getCoordinate(k + 1))) {
									// if current minor tick exceeds the
									// range of current unit, skip
									continue;
								}

								lre3d.setStart3D(goFactory.createLocation3D(dXStart, ya.getCoordinate(k) + doaMinor[j],
										dZStart));
								lre3d.setEnd3D(goFactory.createLocation3D(dXStart, ya.getCoordinate(k) + doaMinor[j],
										dZStart + (ancillaryTickCount - 1) * zStep));
								AxesRenderHelper.addLine3DEvent(lre3d, o3dLeftWallEvent, getDeferredCache());
							}
						}
					}

					if (rightWallFill) {
						for (int k = 0; k < ya.size() - 1; k++) {
							for (int j = 0; j < doaMinor.length - 1; j++) {
								if (ChartUtil.mathGE(ya.getCoordinate(k) + doaMinor[j], ya.getCoordinate(k + 1))) {
									// if current minor tick exceeds the
									// range of current unit, skip
									continue;
								}

								lre3d.setStart3D(goFactory.createLocation3D(dXStart, ya.getCoordinate(k) + doaMinor[j],
										dZStart));
								lre3d.setEnd3D(goFactory.createLocation3D(dXStart + (baseTickCount - 1) * xStep,
										ya.getCoordinate(k) + doaMinor[j], dZStart));
								AxesRenderHelper.addLine3DEvent(lre3d, o3dRightWallEvent, getDeferredCache());
							}
						}
					}
					break;
				case IConstants.ANCILLARY_AXIS:
					AxisTickCoordinates za = scAncillaryBase.getTickCordinates();
					if (leftWallFill) {
						for (int k = 0; k < za.size() - 1; k++) {
							for (int j = 0; j < doaMinor.length - 1; j++) {
								if (ChartUtil.mathGE(za.getCoordinate(k) + doaMinor[j], za.getCoordinate(k + 1))) {
									// if current minor tick exceeds the
									// range of current unit, skip
									continue;
								}

								lre3d.setStart3D(goFactory.createLocation3D(dXStart, dYStart,
										za.getCoordinate(k) + doaMinor[j]));
								lre3d.setEnd3D(
										goFactory.createLocation3D(dXStart, dYStart + (orthogonalTickCount - 1) * yStep,
												za.getCoordinate(k) + doaMinor[j]));
								AxesRenderHelper.addLine3DEvent(lre3d, o3dLeftWallEvent, getDeferredCache());
							}
						}
					}

					if (floorFill) {
						for (int k = 0; k < za.size() - 1; k++) {
							for (int j = 0; j < doaMinor.length - 1; j++) {
								if (ChartUtil.mathGE(za.getCoordinate(k) + doaMinor[j], za.getCoordinate(k + 1))) {
									// if current minor tick exceeds the
									// range of current unit, skip
									continue;
								}

								lre3d.setStart3D(goFactory.createLocation3D(dXStart, dYStart,
										za.getCoordinate(k) + doaMinor[j]));
								lre3d.setEnd3D(goFactory.createLocation3D(dXStart + (baseTickCount - 1) * xStep,
										dYStart, za.getCoordinate(k) + doaMinor[j]));
								AxesRenderHelper.addLine3DEvent(lre3d, o3dFloorEvent, getDeferredCache());
							}
						}
					}
					break;
				default:
					break;
				}
			} else if (oaxa[i].getOrientation() == IConstants.HORIZONTAL) {
				int iDirection = sc.getDirection() == IConstants.BACKWARD ? -1 : 1;
				AxisTickCoordinates da = sc.getTickCordinates();
				double dY2 = bo.getTop() + 1, dY1 = bo.getTop() + bo.getHeight() - 2;
				if (pwa.getDimension() == IConstants.TWO_5_D) {
					for (int j = 0; j < da.size() - 1; j++) {
						// skip the first and the last for category
						// non-crossBetweenCategorgies axis
						if ((j == 0 || j == da.size() - 2) && sc.isCategoryScale() && !sc.isTickBetweenCategories()) {
							continue;
						}
						x = da.getCoordinate(j);
						for (int k = 0; k < doaMinor.length; k++) {
							if ((iDirection == 1 && ChartUtil.mathGE(x + doaMinor[k], da.getCoordinate(j + 1)))
									|| (iDirection == -1
											&& ChartUtil.mathLE(x - doaMinor[k], da.getCoordinate(j + 1)))) {
								// if current minor tick exceeds the
								// range of current unit, skip
								continue;
							}

							lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
									LineRenderEvent.class);
							lre.setLineAttributes(lia);
							lre.setStart(goFactory.createLocation(x + iDirection * doaMinor[k],
									dY1 + pwa.getSeriesThickness()));
							lre.setEnd(goFactory.createLocation(x + iDirection * doaMinor[k] + pwa.getSeriesThickness(),
									dY1));
							ipr.drawLine(lre);
						}
					}
				}

				for (int j = 0; j < da.size() - 1; j++) {
					// skip the first and the last for category
					// non-crossBetweenCategorgies axis
					if ((j == 0 || j == da.size() - 2) && sc.isCategoryScale() && !sc.isTickBetweenCategories()) {
						continue;
					}
					x = da.getCoordinate(j);
					vnext = da.getCoordinate(j + 1);
					if (pwa.getDimension() == IConstants.TWO_5_D) {
						x += pwa.getSeriesThickness();
						vnext += pwa.getSeriesThickness();
					}
					for (int k = 0; k < doaMinor.length; k++) {
						if ((iDirection == 1 && ChartUtil.mathGE(x + doaMinor[k], vnext))
								|| (iDirection == -1 && ChartUtil.mathLE(x - doaMinor[k], vnext))) {
							// if current minor tick exceeds the
							// range of current unit, skip
							continue;
						}

						lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
								LineRenderEvent.class);
						lre.setLineAttributes(lia);
						lre.setStart(goFactory.createLocation(x + iDirection * doaMinor[k], dY1));
						lre.setEnd(goFactory.createLocation(x + iDirection * doaMinor[k], dY2));
						ipr.drawLine(lre);
					}
				}
			} else if (oaxa[i].getOrientation() == IConstants.VERTICAL) {
				int iDirection = sc.getDirection() != IConstants.FORWARD ? -1 : 1;
				AxisTickCoordinates da = sc.getTickCordinates();
				double dX1 = bo.getLeft() + 1, dX2 = bo.getLeft() + bo.getWidth() - 2;
				if (pwa.getDimension() == IConstants.TWO_5_D) {
					for (int j = 0; j < da.size() - 1; j++) {
						// skip the first and the last for category
						// non-crossBetweenCategorgies axis
						if ((j == 0 || j == da.size() - 2) && sc.isCategoryScale() && !sc.isTickBetweenCategories()) {
							continue;
						}
						y = da.getCoordinate(j) - pwa.getSeriesThickness();
						vnext = da.getCoordinate(j + 1) - pwa.getSeriesThickness();
						for (int k = 0; k < doaMinor.length; k++) {
							if ((iDirection == 1 && ChartUtil.mathGE(y + doaMinor[k], vnext))
									|| (iDirection == -1 && ChartUtil.mathLE(y - doaMinor[k], vnext))) {
								// if current minor tick exceeds the
								// range of current unit, skip
								continue;
							}

							lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
									LineRenderEvent.class);
							lre.setLineAttributes(lia);
							lre.setStart(goFactory.createLocation(dX1, y + iDirection * doaMinor[k]));
							lre.setEnd(goFactory.createLocation(dX1 - pwa.getSeriesThickness(),
									y + iDirection * doaMinor[k] + pwa.getSeriesThickness()));
							ipr.drawLine(lre);
						}
					}
				}
				for (int j = 0; j < da.size() - 1; j++) {
					// skip the first and the last for category
					// non-crossBetweenCategorgies axis
					if ((j == 0 || j == da.size() - 2) && sc.isCategoryScale() && !sc.isTickBetweenCategories()) {
						continue;
					}
					y = da.getCoordinate(j);
					vnext = da.getCoordinate(j + 1);
					if (pwa.getDimension() == IConstants.TWO_5_D) {
						y -= pwa.getSeriesThickness();
						vnext -= pwa.getSeriesThickness();
					}
					for (int k = 0; k < doaMinor.length; k++) {
						if ((iDirection == 1 && ChartUtil.mathGE(y + doaMinor[k], vnext))
								|| (iDirection == -1 && ChartUtil.mathLE(y - doaMinor[k], vnext))) {
							// if current minor tick exceeds the
							// range of current unit, skip
							continue;
						}

						lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
								LineRenderEvent.class);
						lre.setLineAttributes(lia);
						lre.setStart(goFactory.createLocation(dX1, y + iDirection * doaMinor[k]));
						lre.setEnd(goFactory.createLocation(dX2, y + iDirection * doaMinor[k]));
						ipr.drawLine(lre);
					}
				}
			}
		}

		// RENDER MAJOR GRID LINES NEXT
		for (int i = 0; i < oaxa.length; i++) {
			final int STEP_NUMBER = oaxa[i].getModelAxis().getScale().getMajorGridsStepNumber();
			lia = oaxa[i].getGrid().getLineAttributes(IConstants.MAJOR);
			if (lia == null || !lia.isVisible()) // GRID
			// LINE
			// UNDEFINED
			{
				continue;
			}

			AutoScale sc = oaxa[i].getScale();
			if (isDimension3D()) {
				Line3DRenderEvent lre3d = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
						Line3DRenderEvent.class);
				lre3d.setLineAttributes(lia);

				switch (oaxa[i].getAxisType()) {
				case IConstants.BASE_AXIS:

					AxisTickCoordinates xa = scPrimaryBase.getTickCordinates();
					if (floorFill) {
						for (int k = 0; k < xa.size(); k += STEP_NUMBER) {
							lre3d.setStart3D(goFactory.createLocation3D(xa.getCoordinate(k), dYStart, dZStart));
							lre3d.setEnd3D(goFactory.createLocation3D(xa.getCoordinate(k), dYStart,
									dZStart + (ancillaryTickCount - 1) * zStep));
							AxesRenderHelper.addLine3DEvent(lre3d, o3dFloorEvent, getDeferredCache());
						}
					}

					if (rightWallFill) {
						for (int k = 0; k < xa.size(); k += STEP_NUMBER) {
							lre3d.setStart3D(goFactory.createLocation3D(xa.getCoordinate(k), dYStart, dZStart));
							lre3d.setEnd3D(goFactory.createLocation3D(xa.getCoordinate(k),
									dYStart + (orthogonalTickCount - 1) * yStep, dZStart));
							AxesRenderHelper.addLine3DEvent(lre3d, o3dRightWallEvent, getDeferredCache());
						}
					}
					break;
				case IConstants.ORTHOGONAL_AXIS:
					AxisTickCoordinates ya = scPrimaryOrthogonal.getTickCordinates();
					if (leftWallFill) {
						for (int k = 0; k < ya.size(); k += STEP_NUMBER) {
							lre3d.setStart3D(goFactory.createLocation3D(dXStart, ya.getCoordinate(k), dZStart));
							lre3d.setEnd3D(goFactory.createLocation3D(dXStart, ya.getCoordinate(k),
									dZStart + (ancillaryTickCount - 1) * zStep));
							AxesRenderHelper.addLine3DEvent(lre3d, o3dLeftWallEvent, getDeferredCache());
						}
					}

					if (rightWallFill) {
						for (int k = 0; k < ya.size(); k += STEP_NUMBER) {
							lre3d.setStart3D(goFactory.createLocation3D(dXStart, ya.getCoordinate(k), dZStart));
							lre3d.setEnd3D(goFactory.createLocation3D(dXStart + (baseTickCount - 1) * xStep,
									ya.getCoordinate(k), dZStart));
							AxesRenderHelper.addLine3DEvent(lre3d, o3dRightWallEvent, getDeferredCache());
						}
					}
					break;
				case IConstants.ANCILLARY_AXIS:
					AxisTickCoordinates za = scAncillaryBase.getTickCordinates();
					if (leftWallFill) {
						for (int k = 0; k < za.size(); k += STEP_NUMBER) {
							lre3d.setStart3D(goFactory.createLocation3D(dXStart, dYStart, za.getCoordinate(k)));
							lre3d.setEnd3D(goFactory.createLocation3D(dXStart,
									dYStart + (orthogonalTickCount - 1) * yStep, za.getCoordinate(k)));
							AxesRenderHelper.addLine3DEvent(lre3d, o3dLeftWallEvent, getDeferredCache());
						}
					}

					if (floorFill) {
						for (int k = 0; k < za.size(); k += STEP_NUMBER) {
							lre3d.setStart3D(goFactory.createLocation3D(dXStart, dYStart, za.getCoordinate(k)));
							lre3d.setEnd3D(goFactory.createLocation3D(dXStart + (baseTickCount - 1) * xStep, dYStart,
									za.getCoordinate(k)));
							AxesRenderHelper.addLine3DEvent(lre3d, o3dFloorEvent, getDeferredCache());
						}
					}
					break;
				default:
					break;
				}
			} else if (oaxa[i].getOrientation() == IConstants.HORIZONTAL) {
				AxisTickCoordinates da = sc.getTickCordinates();
				double dY2 = bo.getTop() + 1, dY1 = bo.getTop() + bo.getHeight() - 2;
				if (pwa.getDimension() == IConstants.TWO_5_D) {
					for (int j = 0; j < da.size(); j++) {
//						if ( j == 0 && insCA.getLeft( ) < lia.getThickness( ) )
//							continue;
						if (j == 0 && !needDrawingFirstMajorGridLine(oaxa[i], oaxa)) {
							continue;
						}
						// if ( j == da.size( ) - 1
						// && insCA.getTop( ) < lia.getThickness( ) )
						// continue;
						if (j == da.size() - 1 && sc.isCategoryScale() && !sc.isTickBetweenCategories())
							continue;

						x = da.getCoordinate(j);
						lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
								LineRenderEvent.class);
						lre.setLineAttributes(lia);
						lre.setStart(goFactory.createLocation(x, dY1 + pwa.getSeriesThickness()));
						lre.setEnd(goFactory.createLocation(x + pwa.getSeriesThickness(), dY1));
						ipr.drawLine(lre);
					}
				}

				for (int j = 0; j < da.size(); j += STEP_NUMBER) {
//					 if ( j == 0 && insCA.getLeft( ) < lia.getThickness( ) )
//						continue;
					if (j == 0 && !needDrawingFirstMajorGridLine(oaxa[i], oaxa)) {
						continue;
					}
					// if ( j == da.size( ) - 1
					// && insCA.getTop( ) < lia.getThickness( ) )
					// continue;
					if (j == da.size() - 1 && sc.isCategoryScale() && !sc.isTickBetweenCategories())
						continue;

					x = da.getCoordinate(j);
					if (pwa.getDimension() == IConstants.TWO_5_D) {
						x += pwa.getSeriesThickness();
					}
					lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p), LineRenderEvent.class);
					lre.setLineAttributes(lia);
					lre.setStart(goFactory.createLocation(x, dY1));
					lre.setEnd(goFactory.createLocation(x, dY2));
					ipr.drawLine(lre);
				}
			} else if (oaxa[i].getOrientation() == IConstants.VERTICAL) {
				AxisTickCoordinates da = sc.getTickCordinates();
				double dX1 = bo.getLeft() + 1, dX2 = bo.getLeft() + bo.getWidth() - 2;
				if (pwa.getDimension() == IConstants.TWO_5_D) {
					for (int j = 0; j < da.size(); j++) {
//						if ( j == 0 && insCA.getBottom( ) < lia.getThickness( ) )
//							continue;
						if (j == 0 && !needDrawingFirstMajorGridLine(oaxa[i], oaxa)) {
							continue;
						}
						// if ( j == da.size( ) - 1
						// && insCA.getRight( ) < lia.getThickness( ) )
						// continue;
						if (j == da.size() - 1 && sc.isCategoryScale() && !sc.isTickBetweenCategories())
							continue;

						y = (da.getCoordinate(j) - pwa.getSeriesThickness());
						lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
								LineRenderEvent.class);
						lre.setLineAttributes(lia);
						lre.setStart(goFactory.createLocation(dX1, y));
						lre.setEnd(
								goFactory.createLocation(dX1 - pwa.getSeriesThickness(), y + pwa.getSeriesThickness()));
						ipr.drawLine(lre);
					}
				}
				for (int j = 0; j < da.size(); j += STEP_NUMBER) {
//					if ( j == 0 && insCA.getBottom( ) < lia.getThickness( ) )
//						continue;
					if (j == 0 && !needDrawingFirstMajorGridLine(oaxa[i], oaxa)) {
						continue;
					}
					// if ( j == da.size( ) - 1
					// && insCA.getRight( ) < lia.getThickness( ) )
					// continue;
					if (j == da.size() - 1 && sc.isCategoryScale() && !sc.isTickBetweenCategories())
						continue;

					y = da.getCoordinate(j);
					if (pwa.getDimension() == IConstants.TWO_5_D) {
						y -= pwa.getSeriesThickness();
					}
					lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p), LineRenderEvent.class);
					lre.setLineAttributes(lia);
					lre.setStart(goFactory.createLocation(dX1, y));
					lre.setEnd(goFactory.createLocation(dX2, y));
					ipr.drawLine(lre);
				}
			}
		}

		if (!isDimension3D() && p.getClientArea().getOutline().isVisible()) {
			rre.setBounds(bo);
			rre.setOutline(ca.getOutline());
			ipr.drawRectangle(rre);
		}
	}

	private boolean needDrawingFirstMajorGridLine(OneAxis axis, OneAxis[] refAxes) {
		// If the position of intersection axis is VALUE/MAX type, it should still
		// draw first major grid.
		int orientation = axis.getOrientation();
		for (OneAxis ax : refAxes) {
			if (ax != axis && ax.getOrientation() != orientation
					&& (ax.getIntersectionValue().getType() == IConstants.VALUE
							|| ax.getIntersectionValue().getType() == IConstants.MAX)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * The axes correspond to the lines/planes being rendered within the plot block.
	 * This is rendered with Z-order=2
	 */
	private final void renderAxesStructure(IPrimitiveRenderer ipr, Plot p) throws ChartException {
		final PlotWithAxes pwa = (PlotWithAxes) getComputations();
		final AllAxes aax = pwa.getAxes();

		if (pwa.getDimension() == IConstants.THREE_D) {
			renderEachAxis(ipr, p, aax.getPrimaryBase(), IConstants.BASE_AXIS);
			renderEachAxis(ipr, p, aax.getAncillaryBase(), IConstants.ANCILLARY_AXIS);
			renderEachAxis(ipr, p, aax.getPrimaryOrthogonal(), IConstants.ORTHOGONAL_AXIS);
		} else {
			final int iCount = aax.getOverlayCount() + 2;
			final OneAxis[] oaxa = new OneAxis[iCount];
			oaxa[0] = aax.getPrimaryBase();
			oaxa[1] = aax.getPrimaryOrthogonal();
			for (int i = 0; i < iCount - 2; i++) {
				oaxa[i + 2] = aax.getOverlay(i);
				IntersectionValue iv = oaxa[i + 2].getIntersectionValue();

				if (iv.getType() == IConstants.VALUE) {
					double dOrigin = Methods.getLocation(aax.getPrimaryBase().getScale(), iv.getValue());
					if (!ChartUtil.isStudyLayout(cm)) {
						oaxa[i + 2].setAxisCoordinate(dOrigin);
						oaxa[i + 2].setTitleCoordinate(dOrigin + oaxa[i + 2].getTitleCoordinate());
					}
				}
			}

			// RENDER THE AXIS LINES FOR EACH AXIS IN THE PLOT
			for (int i = 0; i < iCount; i++) {
				renderEachAxis(ipr, p, oaxa[i], IConstants.AXIS);
			}
		}

	}

	/**
	 * The axes correspond to the lines/planes being rendered within the plot block.
	 * This is rendered with Z-order=2
	 */
	private final void renderAxesLabels(IPrimitiveRenderer ipr, Plot p, OneAxis[] oaxa) throws ChartException {
		// RENDER THE AXIS LINES FOR EACH AXIS IN THE PLOT
		for (int i = 0; i < oaxa.length; i++) {
			renderEachAxis(ipr, p, oaxa[i], IConstants.LABELS);
		}
	}

	/**
	 * This method renders the bar graphic elements superimposed over the plot
	 * background and any previously rendered series' graphic elements.
	 */
	@Override
	public void renderPlot(IPrimitiveRenderer ipr, Plot p) throws ChartException {
		if (!p.isVisible()) // CHECK VISIBILITY
		{
			return;
		}

		final boolean bFirstInSequence = (iSeriesIndex == 0);
		final boolean bLastInSequence = (iSeriesIndex == iSeriesCount - 1);

		final PlotWithAxes pwa = (PlotWithAxes) getComputations();

		if (bFirstInSequence) {
			renderBackground(ipr, p);
			renderAxesStructure(ipr, p);
		}

		if (getSeries() != null || ChartUtil.isDataEmpty(rtc)) {
			ScriptHandler.callFunction(getRunTimeContext().getScriptHandler(), ScriptHandler.BEFORE_DRAW_SERIES,
					getSeries(), this, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_SERIES, getSeries());

			// CALLS THE APPROPRIATE SUBCLASS FOR GRAPHIC ELEMENT RENDERING
			if (p.getClientArea().isVisible()) {
				// Only render plot within axes when it's visible
				renderSeries(ipr, p, srh);
			}

			ScriptHandler.callFunction(getRunTimeContext().getScriptHandler(), ScriptHandler.AFTER_DRAW_SERIES,
					getSeries(), this, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_SERIES, getSeries());

			// render axes decoration for each series.
			renderAxesDecoration(ipr, srh);
		}

		if (bLastInSequence) {
			// RENDER MARKER LINES
			renderMarkerLines();

			final Location panningOffset = getPanningOffset();

			try {
				if (isDimension3D()) {
					// Null of first argument means all deferred caches should be
					// processed, else only process specified.
					fDeferredCacheManager.process3DEvent(null, get3DEngine(), panningOffset.getX(),
							panningOffset.getY());
				}
				fDeferredCacheManager.flushAll(); // FLUSH DEFERRED CACHE
			} catch (ChartException ex) {
				// NOTE: RENDERING EXCEPTION ALREADY BEING THROWN
				throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, ex);
			}

			// SETUP AXIS ARRAY
			final AllAxes aax = pwa.getAxes();
			final OneAxis[] oaxa = new OneAxis[2 + aax.getOverlayCount() + (aax.getAncillaryBase() != null ? 1 : 0)];
			oaxa[0] = aax.getPrimaryBase();
			oaxa[1] = aax.getPrimaryOrthogonal();
			for (int i = 0; i < aax.getOverlayCount(); i++) {
				oaxa[2 + i] = aax.getOverlay(i);
			}
			if (aax.getAncillaryBase() != null) {
				oaxa[2 + aax.getOverlayCount()] = aax.getAncillaryBase();
			}

			// RENDER AXIS LABELS LAST
			renderAxesLabels(ipr, p, oaxa);

			try {
				if (isDimension3D()) {
					getDeferredCache().process3DEvent(get3DEngine(), panningOffset.getX(), panningOffset.getY());
				}
				getDeferredCache().flush(); // FLUSH DEFERRED CACHE
			} catch (ChartException ex) {
				// NOTE: RENDERING EXCEPTION ALREADY BEING THROWN
				throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, ex);
			}

		}
	}

	/**
	 * Render axes decoration by each series.
	 */
	protected void renderAxesDecoration(IPrimitiveRenderer ipr, ISeriesRenderingHints srh) throws ChartException {
		final PlotWithAxes pwa = (PlotWithAxes) getComputations();
		final AllAxes aax = pwa.getAxes();

		if (pwa.getDimension() == IConstants.THREE_D) {
			// not apply to 3d chart.
		} else {
			final int iCount = aax.getOverlayCount() + 2;
			final OneAxis[] oaxa = new OneAxis[iCount];
			oaxa[0] = aax.getPrimaryBase();
			oaxa[1] = aax.getPrimaryOrthogonal();
			for (int i = 0; i < iCount - 2; i++) {
				oaxa[i + 2] = aax.getOverlay(i);
			}

			// RENDER THE AXIS LINES FOR EACH AXIS IN THE PLOT
			for (int i = 0; i < iCount; i++) {
				IAxesDecorator iad = getAxesDecorator(oaxa[i]);

				if (iad != null) {
					iad.decorateAxes(ipr, srh, oaxa[i]);
				}
			}
		}
	}

	/**
	 * Returns the decorator renderer associated with current series, default is
	 * none.
	 */
	@Override
	public IAxesDecorator getAxesDecorator(OneAxis ax) {
		return null;
	}

	private static FormatSpecifier getValidMarkerLineFormat(MarkerLine mkl) {
		DataElement de = mkl.getValue();
		FormatSpecifier fs = mkl.getFormatSpecifier();

		boolean bValid = de instanceof DateTimeDataElement
				&& (fs instanceof DateFormatSpecifier || fs instanceof JavaDateFormatSpecifier)
				|| de instanceof NumberDataElement && (fs instanceof FractionNumberFormatSpecifier
						|| fs instanceof JavaNumberFormatSpecifier || fs instanceof NumberFormatSpecifier)
				|| de instanceof TextDataElement && fs instanceof StringFormatSpecifier;

		if (bValid) {
			return fs;
		}
		return null;
	}

	private Object getDataElementDefaultFormat(DataElement de, AutoScale as) {
		if (de instanceof BigNumberDataElement) {
			return as.computeDefaultDecimalFormat(((BigNumberDataElement) de).getValue());
		}
		return null;
	}

	/**
	 * Renders all marker lines (and labels at requested positions) associated with
	 * every axis in the plot Note that marker lines are drawn immediately (not
	 * rendered as deferred) at the appropriate Z-order
	 * 
	 * @param oaxa
	 * @param boPlotClientArea
	 * 
	 * @throws ChartException
	 */
	private final void renderMarkerLines() throws ChartException {
		PlotWithAxes pwa = (PlotWithAxes) getComputations();
		Bounds boPlotClientArea = pwa.getPlotBounds();
		Axis ax;
		OneAxis[] oaxa = getAllOneAxes();
		int iLineCount, iAxisCount = oaxa.length;
		MarkerLine ml;
		LineRenderEvent lre;
		DataElement deValue;
		AutoScale asc;
		double dCoordinate = 0;
		int iOrientation;

		final IDeviceRenderer idr = getDevice();
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		final Location loStart = goFactory.createLocation(0, 0);
		final Location loEnd = goFactory.createLocation(0, 0);

		Anchor anc;
		TextRenderEvent tre = null;
		Label la = null;
		double dOriginalAngle = 0;
		final boolean bTransposed = ((ChartWithAxes) getModel()).isTransposed();
		final Bounds boText = goFactory.createBounds(0, 0, 0, 0);

		for (int i = 0; i < iAxisCount; i++) {
			ax = oaxa[i].getModelAxis();
			iOrientation = ax.getOrientation().getValue();
			if (bTransposed) // TOGGLE ORIENTATION
			{
				iOrientation = (iOrientation == Orientation.HORIZONTAL) ? Orientation.VERTICAL : Orientation.HORIZONTAL;
			}
			asc = oaxa[i].getScale();
			EList<MarkerLine> el = ax.getMarkerLines();
			iLineCount = el.size();

			for (int j = 0; j < iLineCount; j++) {
				ml = el.get(j);
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_MARKER_LINE, ax, ml,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_MARKER_LINE, ml);

				deValue = ml.getValue();
				// Don't check null case again, like the logic of marker range,
				// if marker value is null, still use min value to set.
//				if ( deValue == null )
//				{
//					throw new ChartException( ChartEnginePlugin.ID,
//							ChartException.RENDERING,
//							"exception.marker.line.null.value", //$NON-NLS-1$
//							new Object[]{
//								ml
//							},
//							Messages.getResourceBundle( getRunTimeContext( ).getULocale( ) ) );
//				}

				// UPDATE THE LABEL CONTENT ASSOCIATED WITH THE MARKER LINE
				la = goFactory.copyOf(ml.getLabel());

				if (la.getCaption().getValue() != null
						&& !IConstants.UNDEFINED_STRING.equals(la.getCaption().getValue())
						&& la.getCaption().getValue().length() > 0) {
					la.getCaption()
							.setValue(oaxa[i].getRunTimeContext().externalizedMessage(la.getCaption().getValue()));
				} else {
					try {
						la.getCaption()
								.setValue(ValueFormatter.format(deValue, getValidMarkerLineFormat(ml),
										oaxa[i].getRunTimeContext().getULocale(),
										getDataElementDefaultFormat(ml.getValue(), asc)));
					} catch (ChartException dfex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, dfex);
					}
				}

				if (isDimension3D()) {
					// TODO render 3D marker line
					return;
				}

				// COMPUTE THE LOCATION
				try {
					dCoordinate = (deValue == null)
							? ((iOrientation == Orientation.HORIZONTAL) ? boPlotClientArea.getLeft()
									: boPlotClientArea.getTop() + boPlotClientArea.getHeight())
							: Methods.getLocation(asc, deValue);
				} catch (Exception ex) {
					logger.log(ILogger.WARNING, Messages.getString("exception.cannot.locate.value.marker.line", //$NON-NLS-1$
							new Object[] { deValue, ml }, getRunTimeContext().getULocale()));
					continue; // TRY NEXT MARKER RANGE
				}

				lre = ((EventObjectCache) idr).getEventObject(StructureSource.createMarkerLine(ml),
						LineRenderEvent.class);
				if (iOrientation == Orientation.HORIZONTAL) {
					double dLeft = pwa.getDimension() == IConstants.TWO_5_D
							? boPlotClientArea.getLeft() - pwa.getSeriesThickness()
							: boPlotClientArea.getLeft();

					// RESTRICT RIGHT EDGE
					if (dCoordinate > dLeft + boPlotClientArea.getWidth()) {
						dCoordinate = dLeft + boPlotClientArea.getWidth();
					}

					// RESTRICT LEFT EDGE
					if (dCoordinate < dLeft) {
						dCoordinate = dLeft;
					}

					// SETUP THE TWO POINTS
					loStart.set(dCoordinate, boPlotClientArea.getTop());
					loEnd.set(dCoordinate, boPlotClientArea.getTop() + boPlotClientArea.getHeight());
				} else {
					double dTop = pwa.getDimension() == IConstants.TWO_5_D
							? boPlotClientArea.getTop() + pwa.getSeriesThickness()
							: boPlotClientArea.getTop();

					// RESTRICT TOP EDGE
					if (dCoordinate < dTop) {
						dCoordinate = dTop;
					}

					// RESTRICT BOTTOM EDGE
					if (dCoordinate > dTop + boPlotClientArea.getHeight()) {
						dCoordinate = dTop + boPlotClientArea.getHeight();
					}

					// SETUP THE TWO POINTS
					loStart.set(boPlotClientArea.getLeft(), dCoordinate);
					loEnd.set(boPlotClientArea.getLeft() + boPlotClientArea.getWidth(), dCoordinate);
				}

				// ADJUST FOR 2D PLOTS AS NEEDED
				if (pwa.getDimension() == IConstants.TWO_5_D) {
					if (iOrientation == Orientation.HORIZONTAL) {
						loStart.translate(0, pwa.getSeriesThickness());
						loEnd.translate(0, pwa.getSeriesThickness());
					} else {
						loStart.translate(-pwa.getSeriesThickness(), 0);
						loEnd.translate(-pwa.getSeriesThickness(), 0);
					}
				}

				// DRAW THE MARKER LINE
				lre.setStart(loStart);
				lre.setEnd(loEnd);
				lre.setLineAttributes(ml.getLineAttributes());
				idr.drawLine(lre);

				// DRAW THE MARKER LINE LABEL AT THE APPROPRIATE LOCATION
				if (la.isVisible()) {
					// DETERMINE THE LABEL ANCHOR (TRANSPOSE IF NEEDED)
					anc = switchAnchor(ml.getLabelAnchor());
					if (bTransposed) {
						// la = ml.getLabel( );
						dOriginalAngle = la.getCaption().getFont().getRotation();
						try {
							la.getCaption().getFont().setRotation(pwa.getTransposedAngle(dOriginalAngle));
							anc = ChartUtil.transposeAnchor(anc);
						} catch (IllegalArgumentException uiex) {
							throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, uiex);
						}
					}

					BoundingBox bb = null;
					try {
						bb = cComp.computeBox(idr.getDisplayServer(), IConstants.LEFT, la, 0, 0);
					} catch (IllegalArgumentException uiex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, uiex);
					}
					boText.set(0, 0, bb.getWidth(), bb.getHeight());

					if (iOrientation == Orientation.VERTICAL) {
						if (anc != null) {
							switch (anc.getValue()) {
							case Anchor.NORTH:
							case Anchor.NORTH_EAST:
							case Anchor.NORTH_WEST:
								boText.setTop(loStart.getY() - boText.getHeight());
								break;

							case Anchor.SOUTH:
							case Anchor.SOUTH_EAST:
							case Anchor.SOUTH_WEST:
								boText.setTop(loStart.getY());
								break;

							default:
								boText.setTop(
										loStart.getY() + (loEnd.getY() - loStart.getY() - boText.getHeight()) / 2);
								break;
							}

							switch (anc.getValue()) {
							case Anchor.NORTH_EAST:
							case Anchor.SOUTH_EAST:
							case Anchor.EAST:
								boText.setLeft(loEnd.getX() - boText.getWidth());
								break;

							case Anchor.NORTH_WEST:
							case Anchor.SOUTH_WEST:
							case Anchor.WEST:
								boText.setLeft(loStart.getX());
								break;

							default:
								boText.setLeft(
										loStart.getX() + (loEnd.getX() - loStart.getX() - boText.getWidth()) / 2);
								break;
							}
						} else
						// CENTER ANCHORED
						{
							boText.setLeft(loStart.getX() + (loEnd.getX() - loStart.getX() - boText.getWidth()) / 2);
							boText.setTop(loStart.getY() + (loEnd.getY() - loStart.getY() - boText.getHeight()) / 2);
						}
					} else {
						if (anc != null) {
							switch (anc.getValue()) {
							case Anchor.NORTH:
							case Anchor.NORTH_EAST:
							case Anchor.NORTH_WEST:
								boText.setTop(loStart.getY());
								break;

							case Anchor.SOUTH:
							case Anchor.SOUTH_EAST:
							case Anchor.SOUTH_WEST:
								boText.setTop(loEnd.getY() - boText.getHeight());
								break;

							default:
								boText.setTop(
										loStart.getY() + (loEnd.getY() - loStart.getY() - boText.getHeight()) / 2);
								break;
							}

							switch (anc.getValue()) {
							case Anchor.NORTH_EAST:
							case Anchor.SOUTH_EAST:
							case Anchor.EAST:
								boText.setLeft(loStart.getX());
								break;

							case Anchor.NORTH_WEST:
							case Anchor.SOUTH_WEST:
							case Anchor.WEST:
								boText.setLeft(loEnd.getX() - boText.getWidth());
								break;

							default:
								boText.setLeft(
										loStart.getX() + (loEnd.getX() - loStart.getX() - boText.getWidth()) / 2);
								break;
							}
						} else
						// CENTER ANCHORED
						{
							boText.setLeft(loStart.getX() + (loEnd.getX() - loStart.getX() - boText.getWidth()) / 2);
							boText.setTop(loStart.getY() + (loEnd.getY() - loStart.getY() - boText.getHeight()) / 2);
						}
					}

					// NOW THAT WE COMPUTED THE BOUNDS, RENDER THE ACTUAL TEXT
					tre = ((EventObjectCache) idr).getEventObject(StructureSource.createMarkerLine(ml),
							TextRenderEvent.class);
					tre.setBlockBounds(boText);
					tre.setBlockAlignment(null);
					tre.setLabel(la);
					tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
					getDeferredCache().addLabel(tre);
				}

				if (isInteractivityEnabled()) {
					Trigger tg;
					EList<Trigger> elTriggers = ml.getTriggers();

					if (!elTriggers.isEmpty()) {
						final InteractionEvent iev = ((EventObjectCache) idr)
								.getEventObject(StructureSource.createMarkerLine(ml), InteractionEvent.class);
						iev.setCursor(ml.getCursor());

						for (int t = 0; t < elTriggers.size(); t++) {
							tg = goFactory.copyOf(elTriggers.get(t));
							processTrigger(tg, StructureSource.createMarkerLine(ml));
							iev.addTrigger(tg);
						}

						Location[] loaHotspot = new Location[4];

						if (iOrientation == Orientation.HORIZONTAL) {
							loaHotspot[0] = goFactory.createLocation(loStart.getX() - IConstants.LINE_EXPAND_SIZE,
									loStart.getY());
							loaHotspot[1] = goFactory.createLocation(loStart.getX() + IConstants.LINE_EXPAND_SIZE,
									loStart.getY());
							loaHotspot[2] = goFactory.createLocation(loEnd.getX() + IConstants.LINE_EXPAND_SIZE,
									loEnd.getY());
							loaHotspot[3] = goFactory.createLocation(loEnd.getX() - IConstants.LINE_EXPAND_SIZE,
									loEnd.getY());
						} else {
							loaHotspot[0] = goFactory.createLocation(loStart.getX(),
									loStart.getY() - IConstants.LINE_EXPAND_SIZE);
							loaHotspot[1] = goFactory.createLocation(loEnd.getX(),
									loEnd.getY() - IConstants.LINE_EXPAND_SIZE);
							loaHotspot[2] = goFactory.createLocation(loEnd.getX(),
									loEnd.getY() + IConstants.LINE_EXPAND_SIZE);
							loaHotspot[3] = goFactory.createLocation(loStart.getX(),
									loStart.getY() + IConstants.LINE_EXPAND_SIZE);
						}

						final PolygonRenderEvent pre = ((EventObjectCache) idr)
								.getEventObject(StructureSource.createMarkerLine(ml), PolygonRenderEvent.class);
						pre.setPoints(loaHotspot);
						iev.setHotSpot(pre);
						idr.enableInteraction(iev);
					}
				}

				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_MARKER_LINE, ax, ml,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_MARKER_LINE, ml);
			}
		}
	}

	/**
	 * Renders the axis.
	 * 
	 * @param ipr
	 * @param pl
	 * @param ax
	 * @param iWhatToDraw
	 * 
	 * @throws ChartException
	 */
	public final void renderEachAxis(IPrimitiveRenderer ipr, Plot pl, OneAxis ax, int iWhatToDraw)
			throws ChartException {
		new AxesRenderHelper(this, ipr, pl, ax, iWhatToDraw).renderEachAxis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.render.BaseRenderer#set(org.eclipse.birt.chart.model.
	 * Chart, java.lang.Object, org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.model.component.Axis,
	 * org.eclipse.birt.chart.model.data.SeriesDefinition)
	 */
	public void set(Chart _cm, PlotComputation _o, Series _se, Axis _ax, SeriesDefinition _sd) {
		super.set(_cm, _o, _se, _sd);
		ax = _ax; // HOLD AXIS HERE
	}

	/**
	 * Returns if its a 3D rendering.
	 * 
	 */
	public final boolean isDimension3D() {
		return (getModel().getDimension() == ChartDimension.THREE_DIMENSIONAL_LITERAL);
	}

	/**
	 * Returns if current chart is transposed.
	 */
	@Override
	public boolean isTransposed() {
		return ((ChartWithAxes) getModel()).isTransposed();
	}

	/**
	 * Returns previous visible series index by given index.
	 * 
	 * @param currentIndex
	 * @return
	 */
	protected int getPrevVisibleSiblingSeriesIndex(int currentIndex) {
		SeriesDefinition sd = null;

		Series se = getSeries();

		if (se.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) se.eContainer();
		}

		if (sd != null) {
			int count = 0;
			int idx = sd.getRunTimeSeries().indexOf(se);
			if (idx > 0) {
				for (int i = idx - 1; i >= 0; i--) {
					count++;
					if (sd.getRunTimeSeries().get(i).isVisible()) {
						return currentIndex - count;
					}
				}
			}

			Axis cax = getAxis();

			int iDefintionIndex = cax.getSeriesDefinitions().indexOf(sd);
			int iDefinitionCount = cax.getSeriesDefinitions().size();

			if (iDefinitionCount > 0) {
				for (int i = iDefintionIndex - 1; i >= 0; i--) {
					sd = cax.getSeriesDefinitions().get(i);

					int runtimeSeriesCount = sd.getRunTimeSeries().size();

					for (int j = runtimeSeriesCount - 1; j >= 0; j--) {
						count++;
						if (sd.getRunTimeSeries().get(j).isVisible()) {
							return currentIndex - count;
						}
					}
				}
			}
		}

		return -1;

	}

	/**
	 * @return Returns if current rendering is the last series in associated axis.
	 */
	public final boolean isLastRuntimeSeriesInAxis() {
		SeriesDefinition sd = null;

		Series se = getSeries();

		if (se.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) se.eContainer();
		}

		if (sd != null) {
			Axis cax = getAxis();

			int iDefintionIndex = cax.getSeriesDefinitions().indexOf(sd);
			int iDefinitionCount = cax.getSeriesDefinitions().size();

			if (iDefinitionCount > 0 && iDefintionIndex == iDefinitionCount - 1) {
				int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(se);
				int iSeriesCount = sd.getRunTimeSeries().size();

				if (iSeriesCount > 0 && iThisSeriesIndex == iSeriesCount - 1) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * To judge whether current series is the last runtime series in a series
	 * definition or not .
	 * 
	 * @return true current series is the last in series definition
	 * @since 2.3
	 */
	public final boolean isLastRuntimeSeriesInGroup() {
		SeriesDefinition sd = null;

		Series se = getSeries();

		if (se.eContainer() instanceof SeriesDefinition) {
			sd = (SeriesDefinition) se.eContainer();
		}

		if (sd != null) {
			Axis cax = getAxis();

			int iDefintionIndex = cax.getSeriesDefinitions().indexOf(sd);
			int iDefinitionCount = cax.getSeriesDefinitions().size();

			if (sd.getRunTimeSeries().size() == 1) {
				if (iDefinitionCount > 0 && iDefintionIndex == iDefinitionCount - 1) {
					int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(se);
					int iSeriesCount = sd.getRunTimeSeries().size();

					if (iSeriesCount > 0 && iThisSeriesIndex == iSeriesCount - 1) {
						return true;
					}
				}
			} else {
				if (iDefinitionCount > 0) {
					int iThisSeriesIndex = sd.getRunTimeSeries().indexOf(se);
					int iSeriesCount = sd.getRunTimeSeries().size();

					if (iSeriesCount > 0 && iThisSeriesIndex == iSeriesCount - 1) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Returns the 3D engine for this render.
	 */
	protected Engine3D get3DEngine() {
		if (isDimension3D()) {
			// delegate to 3d computations.
			return ((PlotWith3DAxes) oComputations).get3DEngine();
		}

		return null;
	}

	/**
	 * Returns the panning offset for 3D engine.
	 */
	protected Location getPanningOffset() throws ChartException {
		if (isDimension3D()) {
			// delegate to 3d computations.
			return ((PlotWith3DAxes) oComputations).getPanningOffset();
		}

		return null;
	}

	/**
	 * Gets current model Axis
	 * 
	 * @return Returns the axis associated with current renderer.
	 */
	public final Axis getAxis() {
		return ax;
	}

	/**
	 * Gets current internal primary orthogonal OneAxis
	 * 
	 * @return internal OneAxis
	 */
	protected final OneAxis getInternalOrthogonalAxis() {
		final AllAxes allAxes = ((PlotWithAxes) getComputations()).getAxes();
		if (allAxes.getOverlayCount() == 0) {
			return allAxes.getPrimaryOrthogonal();
		}
		EList<Axis> axesList = ((Axis) getAxis().eContainer()).getAssociatedAxes();
		int index = axesList.indexOf(getAxis());
		if (index == 0) {
			return allAxes.getPrimaryOrthogonal();
		}
		return allAxes.getOverlay(index - 1);
	}

	/**
	 * Gets current internal base OneAxis
	 * 
	 * @return internal OneAxis
	 */
	protected final OneAxis getInternalBaseAxis() {
		final AllAxes allAxes = ((PlotWithAxes) getComputations()).getAxes();
		return allAxes.getPrimaryBase();
	}

	/**
	 * Checks if current series can show outside values.
	 * 
	 * @return
	 */
	protected boolean isShowOutside() {
		// If it's percent type, always to clip rather than change outside
		// values
		return getAxis().isPercent() ? false : getAxis().getScale().isShowOutside();
	}

	/**
	 * Checks out-of-range of each data point. If outside data is visible, adjust
	 * the coordinates; otherwise, clip the plot area. Note that coordinates array
	 * may be modified.
	 * 
	 * @param ipr         renderer
	 * @param srh         SeriesRenderingHints
	 * @param faX         X coordinates
	 * @param faY         Y coordinates
	 * @param bShowAsTape indicates if it's 2d+ chart
	 */
	protected final void handleOutsideDataPoints(final IPrimitiveRenderer ipr, final SeriesRenderingHints srh,
			final double[] faX, final double[] faY, final boolean bShowAsTape) {
		final AutoScale scaleOrth = getInternalOrthogonalAxis().getScale();
		final Bounds clipArea = srh.getClientAreaBounds(true);
		final Bounds boClientArea = goFactory.copyOf(clipArea);
		// Adjust the position in 2d+
		if (bShowAsTape) {
			final double dSeriesThickness = srh.getSeriesThickness();
			clipArea.delta(-dSeriesThickness, 0, 2 * dSeriesThickness, dSeriesThickness);
			boClientArea.delta(-dSeriesThickness, dSeriesThickness, 0, 0);
		}

		renderClipping(ipr, clipArea);

		if ((scaleOrth.getType() & IConstants.PERCENT) == IConstants.PERCENT) {
			// Always clip in percent type
			return;
		}

		final boolean bHideOutside = !isShowOutside();
		final DataPointHints[] dpha = srh.getDataPoints();
		final boolean isCategory = srh.isCategoryScale();
		final boolean bTransposed = isTransposed();

		for (int i = 0; i < dpha.length; i++) {
			// Skip out-of-X-range data when non-category scale
			if (!isCategory && dpha[i].getBaseValue() == null) {
				dpha[i].markOutside();
				continue;
			}

			// 0 inside, 1 left outside, 2 right outside
			int iYOutside = 0;

			// Check orthogonal value
			if (dpha[i].getStackOrthogonalValue() != null) {
				// Check stack orthogonal value
				double value = dpha[i].getStackOrthogonalValue().doubleValue();
				double min = Methods.asDouble(scaleOrth.getMinimum()).doubleValue();
				double max = Methods.asDouble(scaleOrth.getMaximum()).doubleValue();
				if (value < min) {
					iYOutside = 1;
				} else if (value > max) {
					iYOutside = 2;
				}
			} else {
				// Check non-stack orthogonal value
				iYOutside = checkEntryByType(scaleOrth, dpha[i].getOrthogonalValue());
			}

			// Check base value(only for non-category)
			final OneAxis axisBase = getInternalBaseAxis();
			int iXOutside = 0;
			if (!srh.isCategoryScale()) {
				iXOutside = checkEntryByType(axisBase.getScale(), dpha[i].getBaseValue());
			}

			// The deltaValue is used to adjust X or Y coordinate to make the connection
			// line visible of outside data
			// in 'showoutside' mode.
			final double deltaValue = 1.0d;

			if (iXOutside > 0) {
				if (!baseIsShowOutside()) {
					dpha[i].markOutside();
					continue;
				}

				// Only set the location for non-null data
				if (bTransposed) {
					if (!Double.isNaN(faY[i])) {
						faY[i] = iXOutside == 1 ? boClientArea.getTop() + boClientArea.getHeight() - deltaValue
								: boClientArea.getTop() + deltaValue;
					}
				} else {
					if (!Double.isNaN(faX[i])) {
						faX[i] = iXOutside == 1 ? boClientArea.getLeft() + deltaValue
								: boClientArea.getLeft() + boClientArea.getWidth() - deltaValue;
					}
				}
			}

			if (iYOutside > 0) {
				if (bHideOutside) {
					dpha[i].markOutside();
					continue;
				}
				// Only set the location for non-null data
				if (bTransposed) {
					if (!Double.isNaN(faX[i])) {
						faX[i] = iYOutside == 1 ? boClientArea.getLeft() + deltaValue
								: boClientArea.getLeft() + boClientArea.getWidth() - deltaValue;
					}
				} else {
					if (!Double.isNaN(faY[i])) {
						faY[i] = iYOutside == 1 ? boClientArea.getTop() + boClientArea.getHeight() - deltaValue
								: boClientArea.getTop() + deltaValue;
					}
				}
			}
		}
	}

	/**
	 * Clips the renderer. Need to restore the clipping after the use.
	 * 
	 * @param ipr
	 * @param boClientArea
	 */
	protected final void renderClipping(final IPrimitiveRenderer ipr, final Bounds boClientArea) {
		// Only start clipping in the first series
		final boolean bFirstInSequence = isFirstVisibleSeries();
		// Need to check Y scale and X scale.
		if (bFirstInSequence && !isDimension3D() && (!isShowOutside() || !baseIsShowOutside())) {
			ClipRenderEvent clip = new ClipRenderEvent(this);
			Location[] locations = new Location[4];
			locations[0] = goFactory.createLocation(boClientArea.getLeft(), boClientArea.getTop());
			locations[1] = goFactory.createLocation(boClientArea.getLeft(),
					boClientArea.getTop() + boClientArea.getHeight());
			locations[2] = goFactory.createLocation(boClientArea.getLeft() + boClientArea.getWidth(),
					boClientArea.getTop() + boClientArea.getHeight());
			locations[3] = goFactory.createLocation(boClientArea.getLeft() + boClientArea.getWidth(),
					boClientArea.getTop());
			clip.setVertices(locations);
			ipr.setClip(clip);
		}
	}

	/**
	 * Check if base axis's scale is set to 'Showoutside value'.
	 * 
	 * @return <code>true</code> if base axis's scale is set to 'Showoutside value'.
	 */
	protected boolean baseIsShowOutside() {
		return getInternalBaseAxis().getModelAxis().getScale().isShowOutside();
	}

	/**
	 * Restores the clipping
	 * 
	 * @param ipr
	 * @throws ChartException
	 */
	protected void restoreClipping(final IPrimitiveRenderer ipr) throws ChartException {
		// Only restore clipping in the last renderer
		final boolean bLastInSequence = isLastSeries();
		if (bLastInSequence && !isDimension3D() && (!isShowOutside() || !baseIsShowOutside())) {
			flushClipping();
			ClipRenderEvent clip = new ClipRenderEvent(this);
			clip.setVertices(null);
			ipr.setClip(clip);
		}
	}

	/**
	 * Flushes render event of all Series before clipping
	 * 
	 * @throws ChartException
	 */
	protected void flushClipping() throws ChartException {
		// Flush all deferred caches to avoid clipping error.
		getDeferredCacheManager().flushOptions(DeferredCache.FLUSH_LINE | DeferredCache.FLUSH_PLANE
				| DeferredCache.FLUSH_PLANE_SHADOW | DeferredCache.FLUSH_MARKER);
	}

	/**
	 * Checks data point entry by types
	 * 
	 * @param scale AutoScale for min/max value
	 * @param entry data point entry
	 * @return int indicates if data point entry is in the range of plot area. 0
	 *         inside, 1 left side, 2 outside
	 */
	protected final int checkEntryByType(AutoScale scale, Object entry) {
		int iOutside = 0;
		if (entry == null) {
			// Category style: it's inside.
			// Non-category style: null entry displays in the base line
			iOutside = scale.isCategoryScale() ? 0 : 1;
		} else if (scale.getMinimum() == null || scale.getMaximum() == null) {
			// Category entry
			iOutside = 0;
		} else if (entry instanceof Number) {
			// Double entry
			iOutside = checkEntryInNumberRange((Number) entry, scale.getMinimum(), scale.getMaximum());
		} else if (entry instanceof CDateTime) {
			// Datetime entry
			iOutside = checkEntryInDatetimeRange((CDateTime) entry, scale.getMinimum(), scale.getMaximum());
		} else {
			// Custom entry
			iOutside = checkEntryInRange(entry, scale.getMinimum(), scale.getMaximum());
		}
		return iOutside;
	}

	/**
	 * Checks if the number data point entry is in the range of plot area. Default
	 * result is 0, inside.
	 * 
	 * @param entry data point entry
	 * @param min   scale min
	 * @param max   scale max
	 * @return int indicates if data point entry is in the range of plot area. 0
	 *         inside, 1 left side, 2 outside
	 */
	protected final int checkEntryInNumberRange(Number entry, Object min, Object max) {
		int iOutside = 0;
		double value = entry.doubleValue();
		double dmin = Methods.asDouble(min).doubleValue();
		double dmax = Methods.asDouble(max).doubleValue();
		if (value < dmin) {
			iOutside = 1;
		} else if (value > dmax) {
			iOutside = 2;
		}
		return iOutside;
	}

	/**
	 * Checks if the datetime data point entry is in the range of plot area. Default
	 * result is 0, inside.
	 * 
	 * @param entry data point entry
	 * @param min   scale min
	 * @param max   scale max
	 * @return int indicates if data point entry is in the range of plot area. 0
	 *         inside, 1 left side, 2 outside
	 */
	protected final int checkEntryInDatetimeRange(CDateTime entry, Object min, Object max) {
		int iOutside = 0;
		CDateTime cmin = Methods.asDateTime(min);
		CDateTime cmax = Methods.asDateTime(max);
		if (entry.before(cmin)) {
			if (CDateTime.getDifference(entry, cmin) != 0) {
				iOutside = 1;
			}
		} else if (entry.after(cmax)) {
			if (CDateTime.getDifference(entry, cmax) != 0) {
				iOutside = 2;
			}
		}
		return iOutside;
	}

	/**
	 * Checks if the data point entry is in the range of plot area. Usually this
	 * method is overridden for complex entry. Default result is 0, inside.
	 * 
	 * @param entry data point entry
	 * @param min   scale min
	 * @param max   scale max
	 * @return int indicates if data point entry is in the range of plot area. 0
	 *         inside, 1 left side, 2 outside
	 */
	protected int checkEntryInRange(Object entry, Object min, Object max) {
		return 0;
	}

	protected void addInteractivity(IPrimitiveRenderer ipr, DataPointHints dph, PrimitiveRenderEvent event)
			throws ChartException {
		// interactivity
		final EList<Trigger> elTriggers = se.getTriggers();
		if (!elTriggers.isEmpty()) {
			final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(se, dph);
			final InteractionEvent iev = ((EventObjectCache) ipr).getEventObject(iSource, InteractionEvent.class);
			iev.setCursor(se.getCursor());

			Trigger tg;
			for (int t = 0; t < elTriggers.size(); t++) {
				tg = goFactory.copyOf(elTriggers.get(t));
				this.processTrigger(tg, iSource);
				iev.addTrigger(tg);
			}

			if (event instanceof I3DRenderEvent) {
				final Location panningOffset = getPanningOffset();

				PrimitiveRenderEvent copy = event.copy();
				if (get3DEngine().processEvent(copy, panningOffset.getX(), panningOffset.getY()) != null) {
					iev.setHotSpot(copy);
					ipr.enableInteraction(iev);
				}
			} else {
				iev.setHotSpot(event);
				ipr.enableInteraction(iev);
			}
		}
	}

	protected final DataPointHints createDummyDataPointHintsForLegendItem() throws ChartException {
		if (getSeries() == null) {
			return null;
		}
		return new DataPointHints(null, null, getSeries().getSeriesIdentifier(), null, getSeries().getDataPoint(), null,
				null, null, null, -1, // avoid invalid check
				null, 0, rtc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.BaseRenderer#isFirstVisibleSeries()
	 */
	@Override
	protected boolean isFirstVisibleSeries() {
		// The study layout for multiple Y axes, we just check if the series is
		// first visible in current axes.
		if (ChartUtil.hasMultipleYAxes(getModel()) && ((ChartWithAxes) getModel()).isStudyLayout()) {
			List<SeriesDefinition> sdList = new ArrayList<SeriesDefinition>(ax.getSeriesDefinitions());
			Collections.sort(sdList, zOrderComparatorImpl);
			List<Series> seList = new ArrayList<Series>();
			for (SeriesDefinition sd : sdList) {
				seList.addAll(sd.getRunTimeSeries());
			}

			BaseRenderer renderer = getRenderer(iSeriesIndex);
			Series s = renderer.getSeries();
			for (Series series : seList) {
				if (!series.isVisible()) {
					continue;
				}

				return (s == series);
			}
		} else {
			if (iSeriesIndex == 0) {
				return false;
			}
			for (int i = 1; i < iSeriesCount; i++) {
				BaseRenderer renderer = getRenderer(i);
				if (renderer.getSeries().isVisible()) {
					return (i == iSeriesIndex);
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.render.BaseRenderer#isLastVisibleSeries()
	 */
	@Override
	protected boolean isLastSeries() {
		// The study layout for multiple Y axes, we just check if the series is
		// last visible in current axes.
		if (ChartUtil.hasMultipleYAxes(getModel()) && ((ChartWithAxes) getModel()).isStudyLayout()) {
			List<SeriesDefinition> sdList = new ArrayList<SeriesDefinition>(ax.getSeriesDefinitions());
			Collections.sort(sdList, zOrderComparatorImpl);
			List<Series> seList = new ArrayList<Series>();
			for (SeriesDefinition sd : sdList) {
				seList.addAll(sd.getRunTimeSeries());
			}

			BaseRenderer renderer = getRenderer(iSeriesIndex);
			Series s = renderer.getSeries();
			return (s == seList.get(seList.size() - 1));
		}
		if (iSeriesIndex == 0) {
			return false;
		}
		return iSeriesIndex == (iSeriesCount - 1);
	}
}
