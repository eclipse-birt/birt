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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.computation.BoundingBox;
import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.EllipsisHelper;
import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.computation.LabelLimiter;
import org.eclipse.birt.chart.computation.LegendEntryRenderingHints;
import org.eclipse.birt.chart.computation.LegendItemHints;
import org.eclipse.birt.chart.computation.LegendItemRenderingHints;
import org.eclipse.birt.chart.computation.LegendLayoutHints;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.PlotComputation;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.computation.withaxes.OneAxis;
import org.eclipse.birt.chart.computation.withaxes.PlotWithAxes;
import org.eclipse.birt.chart.computation.withoutaxes.Coordinates;
import org.eclipse.birt.chart.computation.withoutaxes.PlotWithoutAxes;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IPrimitiveRenderer;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.BlockGenerationEvent;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.Oval3DRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.MultipleActions;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.MultipleActionsImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.FillUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;

/**
 * Provides a generic framework that initiates the rendering sequence of the
 * various chart components. Series type extensions could subclass this class if
 * they plan on rendering everything for themselves in the plot area.
 */
public abstract class BaseRenderer implements ISeriesRenderer {

	/**
	 * This key is to reference the location array of last stacked series.
	 */
	protected final static String STACKED_SERIES_LOCATION_KEY = "stacked_series_location_key"; //$NON-NLS-1$
	/**
	 * This key is to reference the fixed location array of last stacked series.
	 */
	protected final static String FIXED_STACKED_SERIES_LOCATION_KEY = "fixed_stacked_series_location_key"; //$NON-NLS-1$
	/**
	 * This key is to reference the fixed index value of last stacked series.
	 */
	protected final static String FIXED_STACKED_SERIES_INDEX_KEY = "fixed_stacked_series_index_key"; //$NON-NLS-1$

	/**
	 * The key is to reference the size information of stacked cone or triangle
	 * series.
	 */
	protected final static String STACKED_SERIES_SIZE_KEY = "stacked_series_size_key"; //$NON-NLS-1$

	protected final static IGObjectFactory goFactory = GObjectFactory.instance();

	protected ISeriesRenderingHints srh;

	protected IDisplayServer xs;

	protected IDeviceRenderer ir;

	protected DeferredCache dc;

	protected Chart cm;

	protected PlotComputation oComputations;

	protected Series se;

	protected SeriesDefinition sd;

	protected IChartComputation cComp;

	/**
	 * All renders associated with the chart provided for convenience and
	 * inter-series calculations
	 */
	protected BaseRenderer[] brna;

	/**
	 * Identifies the series sequence # in the list of series renders
	 */
	protected transient int iSeriesIndex = -1;

	/**
	 * Identifies the series count in the list of series renders
	 */
	protected transient int iSeriesCount = 1;

	/**
	 * Internally used to simulate a translucent shadow
	 */
	protected static final ColorDefinition SHADOW = goFactory.createColorDefinition(64, 64, 64, 127);

	/**
	 * Internally used to darken a tiled image with a translucent dark grey color
	 */
	protected static final ColorDefinition DARK_GLASS = goFactory.createColorDefinition(64, 64, 64, 127);

	/**
	 * Internally used to brighten a tiled image with a translucent light grey color
	 */
	protected static final ColorDefinition LIGHT_GLASS = goFactory.createColorDefinition(196, 196, 196, 127);

	/**
	 * Transparency for translucent color. Should between 0 and 100.
	 */
	protected static final double OVERRIDE_TRANSPARENCY = 50;

	/**
	 * The associated runtimeContext.
	 */
	protected transient RunTimeContext rtc = null;

	/** The manager assures correct paint z-order of series for 2D case. */
	protected DeferredCacheManager fDeferredCacheManager;

	private static final ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/render"); //$NON-NLS-1$

	/** The comparator compares the order of SeriesDefinition objects. */
	static Comparator<SeriesDefinition> zOrderComparatorImpl = new Comparator<>() {

		@Override
		public int compare(SeriesDefinition o1, SeriesDefinition o2) {
			if (o1 != null && o2 != null) {
				return o1.getZOrder() - o2.getZOrder();
			}
			return 0;
		}
	};

	/** The comparator compares the order of BaseRender objects. */
	static Comparator<BaseRenderer> zOrderComparator = new Comparator<>() {

		@Override
		public int compare(BaseRenderer o1, BaseRenderer o2) {
			if (o1 != null && o2 != null) {
				return zOrderComparatorImpl.compare(o1.getSeriesDefinition(), o2.getSeriesDefinition());
			}
			return 0;
		}
	};

	/**
	 * The internal constructor that must be defined as public
	 *
	 * @param _ir
	 * @param _cm
	 */
	public BaseRenderer() {
	}

	/**
	 * Sets the context information for current renderer.
	 *
	 * @param _cm
	 * @param _o
	 * @param _se
	 * @param _ax
	 * @param _sd
	 */
	public void set(Chart _cm, PlotComputation _oComputation, Series _se, SeriesDefinition _sd) {
		cm = _cm;
		setComputation(_oComputation);
		se = _se;
		sd = _sd;
	}

	private void setComputation(PlotComputation _oComputation) {
		oComputations = _oComputation;
		cComp = _oComputation.getChartComputation();
	}

	/**
	 * Sets the deferred cache used by current renderer.
	 */
	public void set(DeferredCache _dc) {
		dc = _dc;
	}

	/**
	 * Sets the device renderer for current renderer.
	 */
	public final void set(IDeviceRenderer _ir) {
		ir = _ir;
	}

	/**
	 * Sets the diplay server for current renderer.
	 */
	public final void set(IDisplayServer _xs) {
		xs = _xs;
	}

	/**
	 * Sets the series rendering hints for current renderer.
	 */
	public final void set(ISeriesRenderingHints _srh) {
		srh = _srh;
	}

	/**
	 * Sets all associated renderers used for current chart rendering.
	 */
	public final void set(BaseRenderer[] _brna) {
		brna = _brna;
	}

	/**
	 * Sets the runtime context object for current renderer.
	 */
	public final void set(RunTimeContext _rtc) {
		rtc = _rtc;
	}

	/**
	 * @return Returns the series rendering hints for current renderer.
	 */
	public final ISeriesRenderingHints getSeriesRenderingHints() {
		return srh;
	}

	/**
	 * @return Returns the display server for current renderer.
	 */
	public final IDisplayServer getXServer() {
		return xs;
	}

	/**
	 * @return Returns the scale of current device against standard 72dpi (X/72).
	 */
	public final double getDeviceScale() {
		return xs.getDpiResolution() / 72d;
	}

	/**
	 * @return Returns the series definition associated with current renderer.
	 */
	public final SeriesDefinition getSeriesDefinition() {
		return sd;
	}

	/**
	 * Identifies the series sequence # in the list of series renders(start from 0).
	 *
	 * @return The index of the Series being rendered
	 */
	public final int getSeriesIndex() {
		return iSeriesIndex;
	}

	/**
	 * @return Returns the series count for current chart rendering.
	 */
	public final int getSeriesCount() {
		return iSeriesCount;
	}

	/**
	 * @return Returns the deferred cache associated with current renderer.
	 */
	public final DeferredCache getDeferredCache() {
		return dc;
	}

	/**
	 * Provides access to any other renderer in the group that participates in chart
	 * rendering
	 *
	 * @param iIndex
	 * @return renderer
	 */
	public final BaseRenderer getRenderer(int iIndex) {
		return brna[iIndex];
	}

	/**
	 * @return Returns the runtime context associated with current renderer.
	 */
	public final RunTimeContext getRunTimeContext() {
		return rtc;
	}

	/**
	 * Renders all blocks using the appropriate block z-order and the containment
	 * hierarchy.
	 *
	 * @param bo
	 */
	public void render(Map<Series, LegendItemRenderingHints> htRenderers, Bounds bo) throws ChartException {
		final boolean bFirstInSequence = (iSeriesIndex == 0);
		final boolean bLastInSequence = (iSeriesIndex == iSeriesCount - 1);
		boolean bStarted = bFirstInSequence;

		Block bl = cm.getBlock();
		final Enumeration<Block> e = bl.children(true);
		final BlockGenerationEvent bge = new BlockGenerationEvent(this);
		final IDeviceRenderer idr = getDevice();
		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();

		if (bFirstInSequence) {
			// ALWAYS RENDER THE OUTERMOST BLOCK FIRST
			ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
			bge.updateBlock(bl);
			renderChartBlock(idr, bl, StructureSource.createChartBlock(bl));
			ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
		}

		// RENDER ALL BLOCKS EXCEPT FOR THE LEGEND IN THIS ITERATIVE LOOP
		while (e.hasMoreElements()) {
			bl = e.nextElement();
			bge.updateBlock(bl);

			if (bl instanceof Plot) {
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
				renderPlot(ir, (Plot) bl);
				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
				if (bFirstInSequence && !bLastInSequence) {
					break;
				}

				if (!bStarted) {
					bStarted = true;
				}
			} else if (bl instanceof TitleBlock && bStarted) {
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
				renderTitle(ir, (TitleBlock) bl);
				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
			} else if (bl instanceof LabelBlock && bStarted) {
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
				renderLabel(ir, bl, StructureSource.createUnknown(bl));
				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
			} else if (bl instanceof Legend && bStarted && bLastInSequence) {
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
				renderLegend(idr, (Legend) bl, htRenderers);
				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
			} else if (bStarted) {
				ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_BLOCK, bl);
				renderBlock(ir, bl, StructureSource.createUnknown(bl));
				ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_BLOCK, bl,
						getRunTimeContext().getScriptContext());
				getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_BLOCK, bl);
			}
		}

		if (bLastInSequence) {
			try {
				fDeferredCacheManager.flushAll(); // FLUSH DEFERRED CACHE
			} catch (ChartException ex) {
				// NOTE: RENDERING EXCEPTION ALREADY BEING THROWN
				throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, ex);
			}

		}
	}

	/**
	 * compute legend item width, it will be the column width if vertical
	 *
	 * @param columnCache
	 * @param bo
	 * @param lih
	 * @param bVertical
	 * @return
	 */
	private double getItemWidth(Map<LegendItemHints, Double> columnCache, Bounds bo, LegendItemHints lih,
			boolean bVertical) {
		double itemWidth = lih.getWidth();

		if (bVertical) {
			Double cachedWidth = columnCache.get(lih);
			itemWidth = (cachedWidth != null) ? cachedWidth : bo.getWidth();
		}
		return itemWidth;
	}

	private void renderAllLegendItems(final IPrimitiveRenderer ipr, final Legend lg, final LegendLayoutHints lilh,
			final Map<Series, LegendItemRenderingHints> htRenderers, final Bounds bo, final double dBaseX,
			final double dBaseY) throws ChartException {
		final ClientArea ca = lg.getClientArea();
		final double dScale = getDeviceScale();
		LineAttributes lia = goFactory.copyOf(ca.getOutline());
		lia.setVisible(true); // SEPARATOR LINES MUST BE VISIBLE
		LineAttributes liSep = lg.getSeparator() == null ? lia : lg.getSeparator();

		// INITIALIZATION OF VARS USED IN FOLLOWING LOOPS
		Label la = goFactory.createLabel();
		la.setCaption(goFactory.copyOf(lg.getText()));
		la.getCaption().setValue("X"); //$NON-NLS-1$
		final double dItemHeight = cComp.computeFontHeight(xs, la) + la.getInsets().getTop()
				+ la.getInsets().getBottom();

		final double dHorizontalSpacing = 4;
		final Insets insCA = goFactory.scaleInsets(ca.getInsets(), dScale);

		final LegendItemHints[] liha = lilh.getLegendItemHints();
		final Orientation orientation = lg.getOrientation();
		final boolean bVertical = orientation.getValue() == Orientation.VERTICAL;
		final Map<LegendItemHints, Double> columnCache = bVertical ? searchMaxColumnWidth(liha, dItemHeight, insCA)
				: null;
		final Direction direction = lg.getDirection();
		final boolean bPaletteByCategory = (cm.getLegend().getItemType().getValue() == LegendItemType.CATEGORIES);

		// 1. validation checking
		if (lilh.getLegendItemHints() == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, "exception.null.legend.item.hints", //$NON-NLS-1$
					Messages.getResourceBundle(rtc.getULocale()));
		}

		if ((orientation.getValue() != Orientation.VERTICAL) && (orientation.getValue() != Orientation.HORIZONTAL)) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING,
					"exception.illegal.legend.orientation", //$NON-NLS-1$
					new Object[] { orientation.getName() }, Messages.getResourceBundle(rtc.getULocale()));
		}

		if ((direction.getValue() != Direction.TOP_BOTTOM) && (direction.getValue() != Direction.LEFT_RIGHT)) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING,
					"exception.illegal.legend.direction", //$NON-NLS-1$
					new Object[] { direction.getName() }, Messages.getResourceBundle(rtc.getULocale()));

		}

		// 3. render items
		for (int k = 0; k < liha.length; k++) {
			LegendItemHints lih = liha[k];
			double itemWidth = getItemWidth(columnCache, bo, lih, bVertical);

			if (lih.getType() == LegendItemHints.Type.LG_GROUPNAME) {
				la.getCaption().setValue(EllipsisHelper.ellipsisString(lih.getItemText(), lih.getValidItemLen()));
				renderLegendGroupName(ipr, lg, la, dBaseX + lih.getLeft(), dBaseY + lih.getTop(), itemWidth,
						lih.getItemHeight(), dHorizontalSpacing);
			} else if ((lih.getType() == LegendItemHints.Type.LG_ENTRY)
					|| (lih.getType() == LegendItemHints.Type.LG_MINSLICE)) {
				la.getCaption().setValue(lih.getItemText());
				Series se = lih.getSeries();
				LegendItemRenderingHints lirh = htRenderers.get(se);
				EList<Fill> elPaletteEntries = lih.getSeriesDefinition().getSeriesPalette().getEntries();

				Label valueLa = null;
				if (!bPaletteByCategory && lg.isShowValue()) {
					valueLa = goFactory.copyOf(se.getLabel());
					valueLa.getCaption()
							.setValue(EllipsisHelper.ellipsisString(lih.getValueText(), lih.getValidValueLen()));
					// Bugzilla #185885, make sure the label
					// will be drawn
					valueLa.setVisible(true);
				}

				// CYCLE THROUGH THE PALETTE
				Fill fPaletteEntry = FillUtil.getPaletteFill(elPaletteEntries, lih.getIndex());

				if (!bVertical) {
					itemWidth = getFullLegendItemWidth(itemWidth, dItemHeight, insCA);
				}

				renderLegendItem(ipr, lg, la, valueLa, lih, dBaseX + lih.getLeft(),
						dBaseY + lih.getTop() + insCA.getTop(), dItemHeight, itemWidth, insCA.getLeft(),
						dHorizontalSpacing, fPaletteEntry, lirh, dScale);
			} else if (lih.getType() == LegendItemHints.Type.LG_SEPERATOR) {
				double sepratorLength;
				Orientation sepratorOrientation;

				if (direction.getValue() == Direction.TOP_BOTTOM) {
					sepratorOrientation = Orientation.HORIZONTAL_LITERAL;
					if (orientation.getValue() == Orientation.VERTICAL) {
						sepratorLength = itemWidth;
					} else {
						sepratorLength = bo.getWidth();
					}
				} else {
					sepratorOrientation = Orientation.VERTICAL_LITERAL;
					if (orientation.getValue() == Orientation.VERTICAL) {
						sepratorLength = bo.getHeight();
					} else {
						sepratorLength = lih.getItemHeight();

					}
				}

				renderSeparator(ipr, lg, liSep, dBaseX + lih.getLeft(), dBaseY + lih.getTop(), sepratorLength,
						sepratorOrientation);
			}
		}

	}

	/**
	 * Returns the decorator renderer associated with current series, default is
	 * none.
	 */
	public IAxesDecorator getAxesDecorator(OneAxis ax) {
		return null;
	}

	/**
	 * Returns the panning offset for 3D engine.
	 */
	protected Location getPanningOffset() throws ChartException {
		return null;
	}

	/**
	 * Returns if current chart is transposed.
	 */
	public boolean isTransposed() {
		return false;
	}

	/**
	 * Returns the 3D engine for this render.
	 */
	protected Engine3D get3DEngine() {
		return null;
	}

	/**
	 * Convenient routine to render a marker
	 */
	protected final void renderMarker(Object oParent, IPrimitiveRenderer ipr, Marker m, Location lo, LineAttributes lia,
			Fill fPaletteEntry, DataPointHints dph, Integer markerSize, boolean bDeferred,
			boolean bConsiderTranspostion) throws ChartException {
		// If data point is invalid, simply return.
		if (dph != null && dph.getIndex() >= 0 && (isNaN(dph.getOrthogonalValue()) || dph.isOutside())) {
			return;
		}

		if (m != null) {
			Fill markerFill = m.getFill();
			m = goFactory.copyMarkerNoFill(m);

			// Convert Fill for negative value
			if (dph != null && dph.getOrthogonalValue() instanceof Double) {
				fPaletteEntry = FillUtil.convertFill(fPaletteEntry, ((Double) dph.getOrthogonalValue()).doubleValue(),
						null);
			}

			// Set fill before call Script
			// Only marker type isn't icon and marker fill don't be set, use
			// current fill.
			if (m.getType().getValue() != MarkerType.ICON && fPaletteEntry != null) {
				m.setFill(fPaletteEntry);
			} else {
				// use the original marker's fill
				m.setFill(goFactory.copyOf(markerFill));
			}
		}

		final AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_MARKER, m, dph,
				getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_MARKER, m);

		Series se = getSeries();

		Object oSource = (oParent instanceof Legend) ? (StructureSource.createLegend((Legend) oParent))
				: (WrappedStructureSource.createSeriesDataPoint(se, dph));
		boolean bTransposed = bConsiderTranspostion && isTransposed();
		PrimitiveRenderEvent preCopy = null;

		if (m == null || !m.isVisible()) {
			// When marker isn't set or is invisible, just get the marker size
			// or use default size to calculate a valid hot spot area for
			// interactivity operation.
			int iSize = 5;
			if (m != null && m.getSize() > 0) {
				iSize = m.getSize();
			}

			// prepare hot spot only
			if (lo instanceof Location3D) {
				final Oval3DRenderEvent ore = ((EventObjectCache) ipr).getEventObject(oSource, Oval3DRenderEvent.class);
				Location3D lo3d = (Location3D) lo;
				ore.setLocation3D(new Location3D[] {
						goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() + iSize, lo3d.getZ()),
						goFactory.createLocation3D(lo3d.getX() - iSize, lo3d.getY() - iSize, lo3d.getZ()),
						goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() - iSize, lo3d.getZ()),
						goFactory.createLocation3D(lo3d.getX() + iSize, lo3d.getY() + iSize, lo3d.getZ()) });
				preCopy = ore.copy();
			} else {
				final OvalRenderEvent ore = ((EventObjectCache) ipr).getEventObject(oSource, OvalRenderEvent.class);
				ore.setBounds(goFactory.createBounds(lo.getX() - iSize, lo.getY() - iSize, iSize * 2, iSize * 2));
				preCopy = ore.copy();
			}
		} else if (m.isVisible()) {
			final MarkerRenderer mr = new MarkerRenderer(this.getDevice(), oSource, lo, lia, m.getFill(), // Fill maybe
																											// changed
																											// in Script
					m, markerSize, getDeferredCache(), bDeferred, bTransposed, getSeriesDefinition().getZOrder());
			mr.draw(ipr);
			preCopy = mr.getRenderArea();
		}

		// Only render interactivity for data points here
		if (this.isInteractivityEnabled() && dph != null && !(oParent instanceof Legend)) {
			final Location panningOffset = this.getPanningOffset();
			final Engine3D engine3d = get3DEngine();
			if (!(lo instanceof Location3D) || panningOffset != null && engine3d != null
					&& engine3d.processEvent(preCopy, panningOffset.getX(), panningOffset.getY()) != null) {
				final EList<Trigger> elTriggers = se.getTriggers();
				if (!elTriggers.isEmpty()) {
					final StructureSource iSource = (WrappedStructureSource.createSeriesDataPoint(se, dph));
					final InteractionEvent iev = ((EventObjectCache) ipr).getEventObject(iSource,
							InteractionEvent.class);
					iev.setCursor(se.getCursor());
					Trigger tg;
					for (int t = 0; t < elTriggers.size(); t++) {
						tg = goFactory.copyOf(elTriggers.get(t));
						this.processTrigger(tg, iSource);
						iev.addTrigger(tg);
					}
					iev.setHotSpot(preCopy);
					iev.setZOrder((short) m.getSize());
					ipr.enableInteraction(iev);
				}
			}
		}
		ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_MARKER, m, dph, getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_MARKER, m);
	}

	/**
	 * Renders the legend block based on the legend rendering rules.
	 *
	 * @param ipr
	 * @param lg
	 * @param htRenderers
	 *
	 * @throws ChartException
	 */
	public void renderLegend(IPrimitiveRenderer ipr, Legend lg, Map<Series, LegendItemRenderingHints> htRenderers)
			throws ChartException {
		if (!lg.isVisible()) // CHECK VISIBILITY
		{
			return;
		}

		renderBlock(ipr, lg, StructureSource.createLegend(lg));
		final IDisplayServer xs = getDevice().getDisplayServer();
		final double dScale = getDeviceScale();
		Bounds bo = goFactory.scaleBounds(lg.getBounds(), dScale);

		Size sz = null;

		/* --- Start bound computing --- */

		double dX, dY;
		if (lg.getPosition() != Position.INSIDE_LITERAL) {
			try {
				sz = lg.getPreferredSize(xs, cm, rtc);
			} catch (Exception ex) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING, ex);
			}
			sz.scale(dScale);

			// USE ANCHOR IN POSITIONING THE LEGEND CLIENT AREA WITHIN THE BLOCK
			// SLACK SPACE
			dX = bo.getLeft() + (bo.getWidth() - sz.getWidth()) / 2;
			dY = 0;
			if (lg.isSetAnchor()) {
				int iAnchor = lg.getAnchor().getValue();

				// swap west/east
				if (isRightToLeft()) {
					if (iAnchor == Anchor.EAST) {
						iAnchor = Anchor.WEST;
					} else if (iAnchor == Anchor.NORTH_EAST) {
						iAnchor = Anchor.NORTH_WEST;
					} else if (iAnchor == Anchor.SOUTH_EAST) {
						iAnchor = Anchor.SOUTH_WEST;
					} else if (iAnchor == Anchor.WEST) {
						iAnchor = Anchor.EAST;
					} else if (iAnchor == Anchor.NORTH_WEST) {
						iAnchor = Anchor.NORTH_EAST;
					} else if (iAnchor == Anchor.SOUTH_WEST) {
						iAnchor = Anchor.SOUTH_EAST;
					}
				}

				switch (iAnchor) {
				case Anchor.NORTH:
				case Anchor.NORTH_EAST:
				case Anchor.NORTH_WEST:
					dY = bo.getTop();
					break;

				case Anchor.SOUTH:
				case Anchor.SOUTH_EAST:
				case Anchor.SOUTH_WEST:
					dY = bo.getTop() + bo.getHeight() - sz.getHeight();
					break;

				default: // CENTERED
					dY = bo.getTop() + (bo.getHeight() - sz.getHeight()) / 2;
					break;
				}

				switch (iAnchor) {
				case Anchor.WEST:
				case Anchor.NORTH_WEST:
				case Anchor.SOUTH_WEST:
					dX = bo.getLeft();
					break;

				case Anchor.EAST:
				case Anchor.SOUTH_EAST:
				case Anchor.NORTH_EAST:
					dX = bo.getLeft() + bo.getWidth() - sz.getWidth();
					break;

				default: // CENTERED
					dX = bo.getLeft() + (bo.getWidth() - sz.getWidth()) / 2;
					break;
				}
			} else {
				dX = bo.getLeft() + (bo.getWidth() - sz.getWidth()) / 2;
				dY = bo.getTop() + (bo.getHeight() - sz.getHeight()) / 2;
			}
		} else {
			// USE PREVIOUSLY COMPUTED POSITION IN THE GENERATOR FOR LEGEND
			// 'INSIDE' PLOT
			dX = bo.getLeft();
			dY = bo.getTop();
			sz = SizeImpl.create(bo.getWidth(), bo.getHeight());
		}

		// get cached legend info.
		final LegendLayoutHints lilh = rtc.getLegendLayoutHints();

		if (lilh == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.RENDERING,
					"exception.null.legend.layout.hints", //$NON-NLS-1$
					Messages.getResourceBundle(rtc.getULocale()));
		}

		// consider legend title size.
		Label lgTitle = lg.getTitle();
		double lgTitleWidth = 0, lgTitleHeight = 0;
		double yOffset = 0, xOffset = 0, wOffset = 0, hOffset = 0;

		final boolean bRenderLegendTitle = lgTitle != null && lgTitle.isVisible()
				&& !lilh.getLaTitle().getCaption().getValue().equals(""); //$NON-NLS-1$
		int iTitlePos = Position.ABOVE;

		if (bRenderLegendTitle) {
			// use cached value
			lgTitle = lilh.getLaTitle();
			Size titleSize = lilh.getTitleSize();

			lgTitleWidth = titleSize.getWidth();
			lgTitleHeight = titleSize.getHeight();

			iTitlePos = lg.getTitlePosition().getValue();

			// swap left/right
			if (isRightToLeft()) {
				if (iTitlePos == Position.LEFT) {
					iTitlePos = Position.RIGHT;
				} else if (iTitlePos == Position.RIGHT) {
					iTitlePos = Position.LEFT;
				}
			}

			switch (iTitlePos) {
			case Position.ABOVE:
				yOffset = lgTitleHeight;
				hOffset = -yOffset;
				break;
			case Position.BELOW:
				hOffset = -lgTitleHeight;
				break;
			case Position.LEFT:
				xOffset = lgTitleWidth;
				wOffset = -xOffset;
				break;
			case Position.RIGHT:
				wOffset = -lgTitleWidth;
				break;
			}
		}

		// RENDER THE LEGEND CLIENT AREA
		final ClientArea ca = lg.getClientArea();
		final Insets lgIns = goFactory.scaleInsets(lg.getInsets(), dScale);
		LineAttributes lia = ca.getOutline();
		bo = goFactory.createBounds(dX, dY, sz.getWidth(), sz.getHeight());
		bo = goFactory.adjusteBounds(bo, lgIns);
		dX = bo.getLeft();
		dY = bo.getTop();

		// Adjust bounds.
		bo.delta(xOffset, yOffset, wOffset, hOffset);
		dX = bo.getLeft();
		dY = bo.getTop();

		/* --- End of bounds computing --- */

		final double dBaseX = dX;
		final double dBaseY = dY;

		final RectangleRenderEvent rre = ((EventObjectCache) ir).getEventObject(StructureSource.createLegend(lg),
				RectangleRenderEvent.class);

		// render client area shadow.
		if (ca.getShadowColor() != null) {
			rre.setBounds(goFactory.translateBounds(bo, 3, 3));
			rre.setBackground(ca.getShadowColor());
			ipr.fillRectangle(rre);
		}

		// render client area
		rre.setBounds(bo);
		rre.setOutline(lia);
		rre.setBackground(ca.getBackground());
		ipr.fillRectangle(rre);
		ipr.drawRectangle(rre);
		lia = goFactory.copyOf(lia);
		lia.setVisible(true); // SEPARATOR LINES MUST BE VISIBLE

		final boolean bPaletteByCategory = (cm.getLegend().getItemType().getValue() == LegendItemType.CATEGORIES);

		// redering legend items
		// 1. special for category
		if (bPaletteByCategory) {
			// SeriesDefinition sdBase = null;
			if (cm instanceof ChartWithAxes) {
				// ONLY SUPPORT 1 BASE AXIS FOR NOW
				final Axis axPrimaryBase = ((ChartWithAxes) cm).getBaseAxes()[0];
				if (axPrimaryBase.getSeriesDefinitions().isEmpty()) {
					// NOTHING TO RENDER (BASE AXIS HAS NO SERIES
					// DEFINITIONS)
					return;
				}
			} else if (cm instanceof ChartWithoutAxes) {
				if (((ChartWithoutAxes) cm).getSeriesDefinitions().isEmpty()) {
					// NOTHING TO RENDER (BASE AXIS HAS NO SERIES
					// DEFINITIONS)
					return;
				}
			}
		}

		// 2. redering items
		handelLegendBehavior(lg);
		renderAllLegendItems(ipr, lg, lilh, htRenderers, bo, dBaseX, dBaseY);

		// Render legend title if defined.
		if (bRenderLegendTitle) {
			double lX = bo.getLeft();
			double lY = bo.getTop();

			switch (iTitlePos) {
			case Position.ABOVE:
				lX = bo.getLeft() + (bo.getWidth() - lgTitleWidth) / 2d;
				lY = bo.getTop() - lgTitleHeight;
				break;
			case Position.BELOW:
				lX = bo.getLeft() + (bo.getWidth() - lgTitleWidth) / 2d;
				lY = bo.getTop() + bo.getHeight();
				break;
			case Position.LEFT:
				lX = bo.getLeft() - lgTitleWidth;
				lY = bo.getTop() + (bo.getHeight() - lgTitleHeight) / 2d;
				break;
			case Position.RIGHT:
				lX = bo.getLeft() + bo.getWidth();
				lY = bo.getTop() + (bo.getHeight() - lgTitleHeight) / 2d;
				break;
			}

			final TextRenderEvent tre = ((EventObjectCache) ir)
					.getEventObject(WrappedStructureSource.createLegendTitle(lg, lgTitle), TextRenderEvent.class);
			tre.setBlockBounds(goFactory.createBounds(lX, lY, lgTitleWidth, lgTitleHeight));
			TextAlignment ta = TextAlignmentImpl.create();
			ta.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
			ta.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
			tre.setBlockAlignment(ta);
			tre.setLabel(lgTitle);
			tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
			// bidi_acgc added start
			if (rtc.isRightToLeftText()) {
				tre.setRtlCaption();
			}
			// bidi_acgc added end
			ipr.drawText(tre);
		}
	}

	private static double getFullLegendItemWidth(double dItemTextWidth, double dItemHeight, Insets insCA) {
		return dItemTextWidth + 1.5 * dItemHeight + 2 * insCA.getLeft();
	}

	/**
	 * Internally used to render a legend item separator
	 *
	 * @param ipr
	 * @param lg
	 * @param dX
	 * @param dY
	 * @param dLength
	 * @param o
	 */
	protected static final void renderSeparator(IPrimitiveRenderer ipr, Legend lg, LineAttributes lia, double dX,
			double dY, double dLength, Orientation o) throws ChartException {
		if (o.getValue() == Orientation.HORIZONTAL) {
			final LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
					LineRenderEvent.class);
			lre.setLineAttributes(lia);
			lre.setStart(goFactory.createLocation(dX, dY));
			lre.setEnd(goFactory.createLocation(dX + dLength, dY));
			ipr.drawLine(lre);
		} else if (o.getValue() == Orientation.VERTICAL) {
			final LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createLegend(lg),
					LineRenderEvent.class);
			lre.setLineAttributes(lia);
			lre.setStart(goFactory.createLocation(dX, dY));
			lre.setEnd(goFactory.createLocation(dX, dY + dLength));
			ipr.drawLine(lre);
		}
	}

	/**
	 * Search the width for each column when legend is vertical.
	 *
	 * @param liha
	 * @return
	 */
	protected Map<LegendItemHints, Double> searchMaxColumnWidth(LegendItemHints[] liha, double dItemHeight,
			Insets insCA) {
		Map<LegendItemHints, Double> rt = new HashMap<>();

		int start = -1;
		double x = 0;
		double maxWidth = 0;

		for (int i = 0; i < liha.length; i++) {
			double dWidth = liha[i].getWidth();
			if (liha[i].getType() == LegendItemHints.Type.LG_ENTRY
					|| liha[i].getType() == LegendItemHints.Type.LG_MINSLICE) {
				dWidth = getFullLegendItemWidth(dWidth, dItemHeight, insCA);
			}

			if (start < 0) {
				start = i;
				x = liha[i].getLeft();
				maxWidth = dWidth;
			} else if (liha[i].getLeft() != x) {
				for (int j = start; j < i; j++) {
					rt.put(liha[j], new Double(maxWidth));
				}

				start = i;
				x = liha[i].getLeft();
				maxWidth = dWidth;
			} else {
				maxWidth = Math.max(maxWidth, dWidth);
			}
		}

		for (int j = Math.max(start, 0); j < liha.length; j++) {
			rt.put(liha[j], new Double(maxWidth));
		}

		return rt;
	}

	private boolean checkActionType(Action action, ActionType actionType) {
		if (action instanceof MultipleActions) {
			for (Action ac : ((MultipleActions) action).getActions()) {
				if (ac.getType() == actionType) {
					return true;
				}
			}
			return false;
		}

		return (action != null && action.getType() == actionType);
	}

	private void handelLegendBehavior(Legend lg) {
		if (isInteractivityEnabled() && cm.getInteractivity() != null) {
			ActionType actionType = null;
			switch (cm.getInteractivity().getLegendBehavior().getValue()) {
			case LegendBehaviorType.HIGHLIGHT_SERIE:
				actionType = ActionType.HIGHLIGHT_LITERAL;
				break;
			case LegendBehaviorType.TOGGLE_SERIE_VISIBILITY:
				actionType = ActionType.TOGGLE_VISIBILITY_LITERAL;
				break;
			}

			if (actionType == null) {
				return;
			}

			Trigger tgOnClick = null;
			boolean customed = false;

			for (Trigger trigger : lg.getTriggers()) {
				if (trigger.getCondition() == TriggerCondition.ONCLICK_LITERAL) {
					tgOnClick = trigger;
					customed = checkActionType(trigger.getAction(), actionType);
					break;
				}
			}

			if (!customed && se != null) {
				Action action = goFactory.createAction(actionType,
						goFactory.createSeriesValue(String.valueOf(se.getSeriesIdentifier())));
				if (tgOnClick == null) {
					tgOnClick = goFactory.createTrigger(TriggerCondition.ONCLICK_LITERAL, action);
					lg.getTriggers().add(tgOnClick);
				} else {
					Action oldAction = tgOnClick.getAction();
					if (oldAction instanceof MultipleActions) {
						((MultipleActions) oldAction).getActions().add(action);
					} else {
						MultipleActions ma = MultipleActionsImpl.create();
						ma.getActions().add(action);
						ma.getActions().add(oldAction);
						tgOnClick.setAction(ma);
					}
				}
			}
		}
	}

	/**
	 * Internally provided to render a single legend entry
	 *
	 * @param ipr
	 * @param lg
	 * @param la
	 * @param dX
	 * @param dY
	 * @param dW
	 * @param dItemHeight
	 * @param dLeftInset
	 * @param dHorizontalSpacing
	 * @param se
	 * @param fPaletteEntry
	 * @param lirh
	 * @param i                  data row index
	 *
	 * @throws ChartException
	 */
	@SuppressWarnings("deprecation")
	protected final void renderLegendItem(IPrimitiveRenderer ipr, Legend lg, Label la, Label valueLa,
			LegendItemHints lih, double dX, double dY, double dItemHeight, double dColumnWidth, double dLeftInset,
			double dHorizontalSpacing, Fill fPaletteEntry, LegendItemRenderingHints lirh, double dScale)
			throws ChartException {
		double dFullHeight = lih.getItemHeight();
		double dExtraHeight = lih.getValueHeight();
		Series se = lih.getSeries();
		int dataIndex = lih.getIndex();
		// Copy correct font setting into current legend item label.
		if (la != null && la.getCaption() != null && valueLa != null && valueLa.getCaption() != null) {
			valueLa.getCaption().setFont(goFactory.copyOf(la.getCaption().getFont()));
			valueLa.getCaption().setColor(goFactory.copyOf(la.getCaption().getColor()));
		}

		updateTranslucency(fPaletteEntry, lih.getSeries());
		LegendEntryRenderingHints lerh = new LegendEntryRenderingHints(la, valueLa, dataIndex, fPaletteEntry);
		AbstractScriptHandler<?> sh = getRunTimeContext().getScriptHandler();
		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_LEGEND_ENTRY, la,
				getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_LEGEND_ENTRY, la);

		final Bounds bo = lirh.getLegendGraphicBounds();

		if (isRightToLeft()) {
			bo.setLeft((dX + dColumnWidth - dLeftInset - 1 - 3 * dItemHeight / 2) / dScale);
		} else {
			bo.setLeft((dX + dLeftInset + 1) / dScale);
		}
		bo.setTop((dY + 1 + (dFullHeight - dItemHeight) / 2) / dScale);
		bo.setWidth((3 * dItemHeight / 2) / dScale);
		bo.setHeight((dItemHeight - 2) / dScale);

		ScriptHandler.callFunction(sh, ScriptHandler.BEFORE_DRAW_LEGEND_ITEM, lerh, bo,
				getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_LEGEND_ITEM, lerh);

		// TODO: label text may be changed in script,
		// in such the ellipsis may need to be recalculated
		if (la != null) {
			la.getCaption().setValue(EllipsisHelper.ellipsisString(la.getCaption().getValue(), lih.getValidItemLen()));
		}

		cComp.applyWrapping(xs, la, lg.getWrappingSize());

		bo.setLeft(bo.getLeft() * dScale);
		bo.setTop(bo.getTop() * dScale);
		bo.setWidth(bo.getWidth() * dScale);
		bo.setHeight(bo.getHeight() * dScale);

		final BaseRenderer br = lirh.getRenderer();
		br.renderLegendGraphic(ipr, lg, fPaletteEntry, bo);

		// 1. Draw series identify label.
		final TextRenderEvent tre = ((EventObjectCache) ir).getEventObject(StructureSource.createLegend(lg),
				TextRenderEvent.class);

		double dDeltaHeight = 0;
		if (la != null) {
			double dLaAngle = la.getCaption().getFont().getRotation();
			if (isRightToLeft()) {
				dLaAngle = -dLaAngle;
			}

			if (dLaAngle > 0 && dLaAngle < 90) {
				dDeltaHeight = (bo.getHeight() + dFullHeight - dItemHeight) / 2;
			} else if (dLaAngle < 0 && dLaAngle > -90) {
				dDeltaHeight = (bo.getHeight() - dFullHeight + dItemHeight) / 2;
			} else if (dLaAngle == 0 || dLaAngle == 90 || dLaAngle == -90) {
				dDeltaHeight = bo.getHeight() / 2;
			}
		}
		if (isRightToLeft()) {
			tre.setLocation(
					goFactory.createLocation(dX + dColumnWidth - dLeftInset - 3 * dItemHeight / 2 - dHorizontalSpacing,
							bo.getTop() + dDeltaHeight));
			tre.setTextPosition(TextRenderEvent.LEFT);
		} else {
			tre.setLocation(goFactory.createLocation(dX + dLeftInset + (3 * dItemHeight / 2) + dHorizontalSpacing,
					bo.getTop() + dDeltaHeight));
			tre.setTextPosition(TextRenderEvent.RIGHT);
		}
		if (la != null && la.isVisible()) {
			tre.setLabel(la);
			tre.setAction(TextRenderEvent.RENDER_TEXT_AT_LOCATION);
			// bidi_acgc added start
			if (rtc.isRightToLeftText()) {
				tre.setRtlCaption();
			}
			// bidi_acgc added end
			ipr.drawText(tre);
		}

		// 2. Draw legend value label.
		if (valueLa != null) {
			final double dValueWidth = dColumnWidth - 2 * dLeftInset;

			Label tmpLa = goFactory.copyOf(valueLa);

			TextAlignment ta = TextAlignmentImpl.create();
			ta.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
			ta.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
			tre.setBlockAlignment(ta);
			tre.setBlockBounds(goFactory.createBounds(dX + dLeftInset + 1, dY + dFullHeight + 1, dValueWidth - 2,
					dExtraHeight - 1));
			tre.setLabel(tmpLa);
			tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
			// bidi_acgc added start
			if (rtc.isRightToLeftText()) {
				tre.setRtlCaption();
			}
			// bidi_acgc added end
			ipr.drawText(tre);
		}

		// restore the label caption changed due to ellipsis
		if (la != null) {
			la.getCaption().setValue(lih.getItemText());
		}

		if (isInteractivityEnabled()) {
			// PROCESS 'SERIES LEVEL' TRIGGERS USING SOURCE='bs'
			Trigger tg;
			EList<Trigger> elTriggers = lg.getTriggers();
			Location[] loaHotspot = new Location[4];

			// use the complete legend item area as the hotspot
			loaHotspot[0] = goFactory.createLocation(dX + 1, dY + 1);
			loaHotspot[1] = goFactory.createLocation(dX + dColumnWidth - 1, dY + 1);
			loaHotspot[2] = goFactory.createLocation(dX + dColumnWidth - 1, dY + dFullHeight + dExtraHeight - 1);
			loaHotspot[3] = goFactory.createLocation(dX + 1, dY + dFullHeight + dExtraHeight - 1);

			if (!elTriggers.isEmpty()) {
				StructureSource source;
				if (this.cm.getLegend().getItemType() == LegendItemType.CATEGORIES_LITERAL && la != null) {
					final DataPointHints dph = new DataPointHints(la.getCaption().getValue(), null, null, null, null,
							null, null, null, null, dataIndex, null, 0, null);
					source = WrappedStructureSource.createSeriesDataPoint(se, dph);
				} else {
					source = StructureSource.createSeries(se);
				}
				// If script is used, need to add legend entry data in source,
				// and put other source in parent, so both of them can be got in
				// interactivity
				source = new WrappedStructureSource(source, lih, StructureType.LEGEND_ENTRY);
				final InteractionEvent iev = ((EventObjectCache) ipr).getEventObject(source, InteractionEvent.class);
				iev.setCursor(lg.getCursor());

				for (int t = 0; t < elTriggers.size(); t++) {
					tg = goFactory.copyOf(elTriggers.get(t));
					processTrigger(tg, WrappedStructureSource.createLegendEntry(lg, lih));
					iev.addTrigger(tg);
				}

				final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(source,
						PolygonRenderEvent.class);
				pre.setPoints(loaHotspot);
				iev.setHotSpot(pre);
				ipr.enableInteraction(iev);
			}
		}

		ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_LEGEND_ITEM, lerh, bo,
				getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_LEGEND_ITEM, lerh);

		ScriptHandler.callFunction(sh, ScriptHandler.AFTER_DRAW_LEGEND_ENTRY, la,
				getRunTimeContext().getScriptContext());
		getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_LEGEND_ENTRY, la);
	}

	protected final void renderLegendGroupName(IPrimitiveRenderer ipr, Legend lg, Label la, double dX, double dY,
			double dWidth, double dHeight, double dLeftInset) throws ChartException {
		if (la.isVisible()) {
			final TextRenderEvent tre = ((EventObjectCache) ir).getEventObject(StructureSource.createLegend(lg),
					TextRenderEvent.class);
			Label tmpLa = goFactory.copyOf(la);
			TextAlignment ta = TextAlignmentImpl.create();
			ta.setHorizontalAlignment(HorizontalAlignment.CENTER_LITERAL);
			ta.setVerticalAlignment(VerticalAlignment.CENTER_LITERAL);
			tre.setBlockAlignment(ta);
			tre.setBlockBounds(goFactory.createBounds(dX + dLeftInset + 1, dY + 1, dWidth - 2, dHeight - 1));
			tre.setLabel(tmpLa);
			tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
			// bidi_acgc added start
			if (rtc.isRightToLeftText()) {
				tre.setRtlCaption();
			}
			// bidi_acgc added end
			ipr.drawText(tre);

		}

	}

	/**
	 * Renders the Plot
	 *
	 * @param ipr The Primitive Renderer of a Device Renderer
	 * @param p   The Plot to render
	 *
	 * @throws ChartException
	 */
	public void renderPlot(IPrimitiveRenderer ipr, Plot p) throws ChartException {
		if (!p.isVisible()) // CHECK VISIBILITY
		{
			return;
		}

		final boolean bFirstInSequence = (iSeriesIndex == 0);
		final boolean bLastInSequence = (iSeriesIndex == iSeriesCount - 1);

		if (bFirstInSequence) {
			renderBackground(ipr, p);
		}

		if (getSeries() != null) {
			ScriptHandler.callFunction(getRunTimeContext().getScriptHandler(), ScriptHandler.BEFORE_DRAW_SERIES,
					getSeries(), this, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.BEFORE_DRAW_SERIES, getSeries());
			renderSeries(ipr, p, srh); // CALLS THE APPROPRIATE SUBCLASS
			// FOR
			ScriptHandler.callFunction(getRunTimeContext().getScriptHandler(), ScriptHandler.AFTER_DRAW_SERIES,
					getSeries(), this, getRunTimeContext().getScriptContext());
			getRunTimeContext().notifyStructureChange(IStructureDefinitionListener.AFTER_DRAW_SERIES, getSeries());
		}

		if (bLastInSequence) {
			// RENDER OVERLAYS HERE IF ANY
		}
	}

	/**
	 * Renders the background.
	 *
	 * @param ipr
	 * @param p
	 *
	 * @throws ChartException
	 */
	protected void renderBackground(IPrimitiveRenderer ipr, Plot p) throws ChartException {
		final double dScale = getDeviceScale();
		final RectangleRenderEvent rre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
				RectangleRenderEvent.class);
		rre.updateFrom(p, dScale); // POINTS => PIXELS
		ipr.fillRectangle(rre);
		ipr.drawRectangle(rre);

		PlotComputation oComputations = getComputations();
		if (oComputations instanceof PlotWithoutAxes) {
			final PlotWithoutAxes pwoa = (PlotWithoutAxes) oComputations;
			final ClientArea ca = p.getClientArea();

			Bounds bo = pwoa.getPlotBounds();

			// render client area shadow
			if (ca.getShadowColor() != null) {
				rre.setBounds(goFactory.translateBounds(bo, 3, 3));
				rre.setBackground(ca.getShadowColor());
				ipr.fillRectangle(rre);
			}

			// render client area
			rre.setBounds(bo);
			rre.setBackground(ca.getBackground());
			ipr.fillRectangle(rre);

			if (ca.getOutline().isVisible()) {
				Size sz = pwoa.getCellSize();

				final LineRenderEvent lre = ((EventObjectCache) ipr).getEventObject(StructureSource.createPlot(p),
						LineRenderEvent.class);
				lre.setLineAttributes(ca.getOutline());

				int colCount = pwoa.getColumnCount();
				int rowCount = pwoa.getRowCount();

				ChartWithoutAxes cwoa = pwoa.getModel();
				if (cwoa instanceof DialChart && ((DialChart) cwoa).isDialSuperimposition()) {
					colCount = 1;
					rowCount = 1;
				}

				for (int i = 0; i < colCount + 1; i++) {
					lre.setStart(goFactory.createLocation(bo.getLeft() + i * sz.getWidth(), bo.getTop()));
					lre.setEnd(
							goFactory.createLocation(bo.getLeft() + i * sz.getWidth(), bo.getTop() + bo.getHeight()));
					ipr.drawLine(lre);
				}

				for (int j = 0; j < rowCount + 1; j++) {
					lre.setStart(goFactory.createLocation(bo.getLeft(), bo.getTop() + j * sz.getHeight()));
					lre.setEnd(
							goFactory.createLocation(bo.getLeft() + bo.getWidth(), bo.getTop() + j * sz.getHeight()));
					ipr.drawLine(lre);
				}
			}
		}

	}

	/**
	 * Renders the block.
	 *
	 * @param ipr
	 * @param b
	 *
	 * @throws ChartException
	 */
	protected void renderBlock(IPrimitiveRenderer ipr, Block b, Object oSource) throws ChartException {
		final double dScale = getDeviceScale();
		final RectangleRenderEvent rre = ((EventObjectCache) ipr).getEventObject(oSource, RectangleRenderEvent.class);
		rre.updateFrom(b, dScale);
		ipr.fillRectangle(rre);
		ipr.drawRectangle(rre);
	}

	/**
	 * Renders the chart block.
	 *
	 * @param ipr
	 * @param b
	 *
	 * @throws ChartException
	 */
	protected void renderChartBlock(IPrimitiveRenderer ipr, Block b, Object oSource) throws ChartException {
		final double dScale = getDeviceScale();
		final RectangleRenderEvent rre = ((EventObjectCache) ipr).getEventObject(oSource, RectangleRenderEvent.class);
		rre.updateFrom(b, dScale);
		try {
			ipr.fillRectangle(rre);
		} catch (Exception e) {
			logger.log(e);
		}
		ipr.drawRectangle(rre);

		if (isInteractivityEnabled()) {
			Trigger tg;
			EList<Trigger> elTriggers = b.getTriggers();
			Location[] loaHotspot = new Location[4];
			Bounds bo = goFactory.scaleBounds(b.getBounds(), dScale);
			double dLeft = bo.getLeft();
			double dTop = bo.getTop();
			double dWidth = bo.getWidth();
			double dHeight = bo.getHeight();
			loaHotspot[0] = goFactory.createLocation(dLeft, dTop);
			loaHotspot[1] = goFactory.createLocation(dLeft + dWidth, dTop);
			loaHotspot[2] = goFactory.createLocation(dLeft + dWidth, dTop + dHeight);
			loaHotspot[3] = goFactory.createLocation(dLeft, dTop + dHeight);

			if (!elTriggers.isEmpty()) {
				final InteractionEvent iev = ((EventObjectCache) ipr)
						.getEventObject(StructureSource.createChartBlock(b), InteractionEvent.class);
				iev.setCursor(b.getCursor());

				for (int t = 0; t < elTriggers.size(); t++) {
					tg = goFactory.copyOf(elTriggers.get(t));
					processTrigger(tg, StructureSource.createChartBlock(b));
					iev.addTrigger(tg);
				}

				final PolygonRenderEvent pre = ((EventObjectCache) ipr)
						.getEventObject(StructureSource.createChartBlock(b), PolygonRenderEvent.class);
				pre.setPoints(loaHotspot);
				iev.setHotSpot(pre);
				ipr.enableInteraction(iev);
			}

		}
	}

	/**
	 * Renders label of a LabelBlock.
	 *
	 * @param ipr
	 * @param b
	 *
	 * @throws ChartException
	 */
	public void renderLabel(IPrimitiveRenderer ipr, Block b, Object oSource) throws ChartException {
		if (!b.isVisible()) {
			return;
		}

		final LabelBlock lb = (LabelBlock) b;
		Label la = lb.getLabel();

		final String sPreviousValue = la.getCaption().getValue();
		la.getCaption().setValue(rtc.externalizedMessage(sPreviousValue));

		Map<Label, LabelLimiter> mapLimiter = rtc.getState(RunTimeContext.StateKey.LABEL_LIMITER_LOOKUP_KEY);
		LabelLimiter lbLimiter = mapLimiter.get(lb.getLabel());

		if (lbLimiter != null) {
			lbLimiter.computeWrapping(xs, lb.getLabel());
			lbLimiter = lbLimiter.limitLabelSize(cComp, xs, lb.getLabel());

			if (!lbLimiter.isSuccessed()) {
				return;
			}
		}

		renderBlock(ipr, b, oSource);
		final double dScale = getDeviceScale();
		final TextRenderEvent tre = ((EventObjectCache) ipr).getEventObject(oSource, TextRenderEvent.class);
		// need backup original non-externalized value.
		tre.updateFrom(lb, dScale, rtc);
		if (lb.getLabel().isVisible()) {
			if (rtc.isRightToLeftText()) {
				tre.setRtlCaption();
			}
			ipr.drawText(tre);
		}
		lb.getLabel().getCaption().setValue(sPreviousValue);
	}

	/**
	 * Renders the Chart Title Block
	 *
	 * @param ipr The Primitive Renderer of a Device Renderer
	 * @param b   The TitleBlock to render
	 *
	 * @throws ChartException
	 */
	public void renderTitle(IPrimitiveRenderer ipr, TitleBlock b) throws ChartException {
		Label la = b.getLabel();
		// switch lable alignment
		TextAlignment restoreValue = la.getCaption().getFont().getAlignment();
		la.getCaption().getFont().setAlignment(switchTextAlignment(restoreValue));

		renderLabel(ipr, b, StructureSource.createTitle(b));

		// restore original value
		b.getLabel().getCaption().getFont().setAlignment(restoreValue);

		if (isInteractivityEnabled()) {
			Trigger tg;
			EList<Trigger> elTriggers = b.getTriggers();
			Location[] loaHotspot = new Location[4];
			final double dScale = getDeviceScale();
			Bounds bo = goFactory.scaleBounds(b.getBounds(), dScale);
			double dLeft = bo.getLeft();
			double dTop = bo.getTop();
			double dWidth = bo.getWidth();
			double dHeight = bo.getHeight();
			loaHotspot[0] = goFactory.createLocation(dLeft, dTop);
			loaHotspot[1] = goFactory.createLocation(dLeft + dWidth, dTop);
			loaHotspot[2] = goFactory.createLocation(dLeft + dWidth, dTop + dHeight);
			loaHotspot[3] = goFactory.createLocation(dLeft, dTop + dHeight);

			if (!elTriggers.isEmpty()) {
				final InteractionEvent iev = ((EventObjectCache) ipr).getEventObject(StructureSource.createTitle(b),
						InteractionEvent.class);
				iev.setCursor(b.getCursor());

				for (int t = 0; t < elTriggers.size(); t++) {
					tg = goFactory.copyOf(elTriggers.get(t));
					processTrigger(tg, StructureSource.createTitle(b));
					iev.addTrigger(tg);
				}

				final PolygonRenderEvent pre = ((EventObjectCache) ipr).getEventObject(StructureSource.createTitle(b),
						PolygonRenderEvent.class);
				pre.setPoints(loaHotspot);
				iev.setHotSpot(pre);
				ipr.enableInteraction(iev);
			}

		}
	}

	/**
	 * Creates empty renderer instance.
	 *
	 * @param cm
	 * @param oComputations
	 * @return
	 */
	private static final BaseRenderer[] createEmptyInstance(Chart cm, RunTimeContext rtc,
			PlotComputation oComputations) {
		final BaseRenderer[] brna = new BaseRenderer[1];
		final AxesRenderer ar = new EmptyWithAxes();
		ar.iSeriesIndex = 0;
		ar.set(cm, oComputations, null, null, null);
		ar.set(rtc);
		brna[0] = ar;
		return brna;
	}

	/**
	 * This method returns appropriate renders for the given chart model. It uses
	 * extension points to identify a renderer corresponding to a custom series.
	 *
	 * @param cm
	 * @param rtc
	 * @param oComputations
	 *
	 * @return renderers
	 * @throws ChartException
	 */
	public static final BaseRenderer[] instances(Chart cm, RunTimeContext rtc, PlotComputation oComputations)
			throws ChartException {
		final PluginSettings ps = PluginSettings.instance();
		BaseRenderer[] brna = null;
		final boolean bPaletteByCategory = (cm.getLegend().getItemType().getValue() == LegendItemType.CATEGORIES);

		if (cm instanceof ChartWithAxes) {
			final ChartWithAxes cwa = (ChartWithAxes) cm;
			final Axis[] axa = cwa.getPrimaryBaseAxes();
			Axis axPrimaryBase = axa[0];
			Series se;
			AxesRenderer ar = null;
			List<AxesRenderer> al = new ArrayList<>();
			List<Series> alRunTimeSeries;
			EList<SeriesDefinition> elBase, elOrthogonal;
			SeriesDefinition sd = null;

			elBase = axPrimaryBase.getSeriesDefinitions();
			if (elBase.isEmpty()) // NO SERIES DEFINITIONS
			{
				return createEmptyInstance(cm, rtc, oComputations);
			} else {
				// ONLY 1 SERIES DEFINITION MAY BE
				// ASSOCIATED
				// WITH THE BASE AXIS
				final SeriesDefinition sdBase = elBase.get(0);

				alRunTimeSeries = sdBase.getRunTimeSeries();
				if (alRunTimeSeries.isEmpty()) {
					return createEmptyInstance(cm, rtc, oComputations);
				}
				// ONLY 1 SERIES MAY BE
				// ASSOCIATED WITH THE
				// BASE SERIES DEFINITION
				se = alRunTimeSeries.get(0);
				ar = (se.getClass() == SeriesImpl.class) ? new EmptyWithAxes()
						: (AxesRenderer) ps.getRenderer(se.getClass());
				// INITIALIZE THE RENDERER
				ar.set(cm, oComputations, se, axPrimaryBase, sdBase);
				ar.set(rtc);
				al.add(ar);

				final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);
				for (int i = 0; i < axaOrthogonal.length; i++) {
					elOrthogonal = axaOrthogonal[i].getSeriesDefinitions();
					for (int j = 0; j < elOrthogonal.size(); j++) {
						sd = elOrthogonal.get(j);
						alRunTimeSeries = sd.getRunTimeSeries();
						for (int k = 0; k < alRunTimeSeries.size(); k++) {
							se = alRunTimeSeries.get(k);
							ar = (se.getClass() == SeriesImpl.class) ? new EmptyWithAxes()
									: (AxesRenderer) ps.getRenderer(se.getClass());
							// INITIALIZE THE RENDERER
							ar.set(cm, oComputations, se, axaOrthogonal[i], bPaletteByCategory ? sdBase : sd);
							al.add(ar);
						}
					}
				}

				if (cm.getDimension() == ChartDimension.TWO_DIMENSIONAL_LITERAL) {
					Collections.sort(al, zOrderComparator);
				}

				// CONVERT INTO AN ARRAY AS REQUESTED
				brna = new BaseRenderer[al.size()];
				for (int i = 0; i < brna.length; i++) {
					ar = al.get(i);
					ar.iSeriesIndex = i;
					ar.iSeriesCount = brna.length;
					brna[i] = ar;
				}
			}
		} else if (cm instanceof ChartWithoutAxes) {
			final ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			EList<SeriesDefinition> elBase = cwoa.getSeriesDefinitions();
			EList<SeriesDefinition> elOrthogonal;
			SeriesDefinition sd, sdBase;
			List<Series> alRuntimeSeries;

			final Series[] sea = cwoa.getRunTimeSeries();

			Series se;
			final int iSeriesCount = sea.length;
			brna = new BaseRenderer[iSeriesCount];
			int iSI = 0; // SERIES INDEX COUNTER

			for (int i = 0; i < elBase.size(); i++) {
				sdBase = elBase.get(i);
				alRuntimeSeries = sdBase.getRunTimeSeries();

				if (alRuntimeSeries.isEmpty() || ChartUtil.isDataEmpty(rtc)) {
					brna = new BaseRenderer[1];
					brna[0] = new EmptyWithoutAxes();
					brna[0].set(cm, oComputations, sdBase.getSeries().get(0), sdBase);
					brna[0].set(rtc);
					brna[0].iSeriesIndex = 0;
					brna[0].iSeriesCount = 1;

					return brna;
				}

				// CHECK FOR A SINGLE BASE SERIES ONLY
				if (alRuntimeSeries.size() != 1) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.PLUGIN,
							"exception.illegal.base.runtime.series.count", //$NON-NLS-1$
							new Object[] { Integer.valueOf(alRuntimeSeries.size()) },
							Messages.getResourceBundle(rtc.getULocale()));
				}
				se = alRuntimeSeries.get(0);
				brna[iSI] = (se.getClass() == SeriesImpl.class) ? new EmptyWithoutAxes()
						: ps.getRenderer(se.getClass());
				// INITIALIZE THE RENDERER
				brna[iSI].set(cm, oComputations, se, sdBase);
				brna[iSI].set(rtc);
				brna[iSI].iSeriesIndex = iSI++;

				elOrthogonal = elBase.get(i).getSeriesDefinitions();
				for (int j = 0; j < elOrthogonal.size(); j++) {
					sd = elOrthogonal.get(j);
					alRuntimeSeries = sd.getRunTimeSeries();
					for (int k = 0; k < alRuntimeSeries.size(); k++) {
						se = alRuntimeSeries.get(k);
						brna[iSI] = (se.getClass() == SeriesImpl.class) ? new EmptyWithoutAxes()
								: ps.getRenderer(se.getClass());
						// INITIALIZE THE RENDERER
						brna[iSI].set(cm, oComputations, se, bPaletteByCategory ? sdBase : sd);
						brna[iSI].iSeriesIndex = iSI++;
					}
				}
			}

			for (int k = 0; k < iSI; k++) {
				brna[k].iSeriesCount = iSI;
			}
		}

		return brna;
	}

	/**
	 * @return Returns series associated with current renderer.
	 */
	public final Series getSeries() {
		return se;
	}

	/**
	 * @return Returns chart model associated with current renderer.
	 */
	public final Chart getModel() {
		return cm;
	}

	/**
	 * @return Returns computation object associated with current renderer.
	 */
	public final PlotComputation getComputations() {
		return oComputations;
	}

	/**
	 * @return Returns device renderer associated with current renderer.
	 */
	public final IDeviceRenderer getDevice() {
		return ir;
	}

	/**
	 * Renders a 2D or extruded 2D plane as necessary for a given front surface
	 * polygon. Takes into account the correct z-ordering of each plane and applies
	 * basic lighting. This convenience method may be used by series type rendering
	 * extensions if needed.
	 *
	 * @param ipr              A handle to the primitive rendering device
	 * @param oSource          The object wrapped in the polygon rendering event
	 * @param loaFront         The co-ordinates of the front face polygon
	 * @param f                The fill color for the front face
	 * @param lia              The edge color for the polygon
	 * @param dSeriesThickness The thickness or the extrusion level (for 2.5D or 3D)
	 *
	 * @throws ChartException
	 */
	protected final void renderPlane(IPrimitiveRenderer ipr, Object oSource, Location[] loaFront, Fill f,
			LineAttributes lia, ChartDimension cd, double dSeriesThickness, boolean bDeferred) throws ChartException {
		renderPlane(ipr, oSource, loaFront, f, lia, cd, dSeriesThickness, bDeferred, 0, null);
	}

	/**
	 * @param ipr
	 * @param oSource
	 * @param loaFront
	 * @param f
	 * @param lia
	 * @param cd
	 * @param dSeriesThickness
	 * @param bDeferred
	 * @param zorder_hint
	 * @param compareBounds    this bounds is used to adjust the order of polygon,
	 *                         if this bound isn't null, chart will use this bounds
	 *                         instead of actual bounds of polygon for order.
	 * @throws ChartException
	 */
	protected final void renderPlane(IPrimitiveRenderer ipr, Object oSource, Location[] loaFront, Fill f,
			LineAttributes lia, ChartDimension cd, double dSeriesThickness, boolean bDeferred, int zorder_hint,
			Bounds compareBounds) throws ChartException {
		PolygonRenderEvent pre;
		if (cd.getValue() == ChartDimension.TWO_DIMENSIONAL) {
			// RENDER THE POLYGON
			pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setPoints(loaFront);
			pre.setBackground(f);
			pre.setOutline(lia);
			if (bDeferred) {
				dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
			} else {
				ipr.fillPolygon(pre);
				ipr.drawPolygon(pre);
			}
			return;
		}

		final boolean bSolidColor = f instanceof ColorDefinition;
		Fill fDarker = null, fBrighter = null;
		if (cd.getValue() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH
				|| cd.getValue() == ChartDimension.THREE_DIMENSIONAL) {
			fDarker = f;
			if (fDarker instanceof ColorDefinition) {
				fDarker = goFactory.darker((ColorDefinition) fDarker);
			}
			fBrighter = f;
			if (!(getModel().getDimension() == ChartDimension.THREE_DIMENSIONAL_LITERAL)) {
				if (fBrighter instanceof ColorDefinition) {
					fBrighter = goFactory.brighter((ColorDefinition) fBrighter);
				}
			} else {
				// #192368
				// case of drawing legend graphics in 3d mode
				// readjusts the brightness to give a more consistenter appearance
				fBrighter = FillUtil.changeBrightness(f, 0.89);
				fDarker = FillUtil.changeBrightness(f, 0.65);
				f = FillUtil.changeBrightness(f, 0.91);
			}
		}

		final int nSides = loaFront.length;
		final Location[][] loaa = new Location[nSides + 1][];
		Location[] loa;
		double dY, dSmallestY = 0;
		for (int j, i = 0; i < nSides; i++) {
			j = i + 1;
			if (j >= loaFront.length) {
				j = 0;
			}
			loa = new Location[4];
			loa[0] = goFactory.createLocation(loaFront[i].getX(), loaFront[i].getY());
			loa[1] = goFactory.createLocation(loaFront[j].getX(), loaFront[j].getY());
			loa[2] = goFactory.createLocation(loaFront[j].getX() + dSeriesThickness,
					loaFront[j].getY() - dSeriesThickness);
			loa[3] = goFactory.createLocation(loaFront[i].getX() + dSeriesThickness,
					loaFront[i].getY() - dSeriesThickness);
			loaa[i] = loa;
		}
		loaa[nSides] = loaFront;

		// SORT ON MULTIPLE KEYS (GREATEST Y, SMALLEST X)
		double dI, dJ;
		Location[] loaI, loaJ;
		for (int i = 0; i < nSides - 1; i++) {
			loaI = loaa[i];
			for (int j = i + 1; j < nSides; j++) {
				loaJ = loaa[j];

				dI = getY(loaI, IConstants.AVERAGE);
				dJ = getY(loaJ, IConstants.AVERAGE);

				// Use fuzzy comparison here due to possible precision loss
				// during computation.
				if (ChartUtil.mathGT(dJ, dI)) // SWAP
				{
					loaa[i] = loaJ;
					loaa[j] = loaI;
					loaI = loaJ;
				} else if (ChartUtil.mathEqual(dJ, dI)) {
					dI = getX(loaI, IConstants.AVERAGE);
					dJ = getX(loaJ, IConstants.AVERAGE);
					if (ChartUtil.mathGT(dI, dJ)) {
						loaa[i] = loaJ;
						loaa[j] = loaI;
						loaI = loaJ;
					}
				}
			}
		}

		int iSmallestYIndex = 0;
		for (int i = 0; i < nSides; i++) {
			dY = getY(loaa[i], IConstants.AVERAGE);
			if (i == 0) {
				dSmallestY = dY;
			}
			// #192797: Use fuzzy comparison here due to possible precision
			// loss during computation.
			else if (ChartUtil.mathGT(dSmallestY, dY)) {
				dSmallestY = dY;
				iSmallestYIndex = i;
			}
		}

		ArrayList<PrimitiveRenderEvent> alModel = new ArrayList<>(nSides + 1);
		Fill fP;
		for (int i = 0; i <= nSides; i++) {
			pre = ((EventObjectCache) ipr).getEventObject(oSource, PolygonRenderEvent.class);
			pre.setOutline(lia);
			pre.setPoints(loaa[i]);
			if (i < nSides) // OTHER SIDES (UNKNOWN ORDER) ARE DEEP
			{
				pre.setDepth(-dSeriesThickness);
			} else
			// FRONT FACE IS NOT DEEP
			{
				pre.setDepth(0);
			}
			if (i == nSides) {
				fP = f;
			} else if (i == iSmallestYIndex) {
				fP = fBrighter;
			} else {
				fP = fDarker;
			}
			pre.setBackground(fP);
			if (bDeferred) {
				alModel.add(pre.copy());
			} else {
				ipr.fillPolygon(pre);
			}

			if (i == nSides) {
			} else if (i == iSmallestYIndex) {
				// DRAW A TRANSLUCENT LIGHT GLASS PANE OVER THE BRIGHTER SURFACE
				// (IF NOT A SOLID COLOR)
				if (!bSolidColor) {
					pre.setBackground(LIGHT_GLASS);
				}
				if (bDeferred) {
					alModel.add(pre.copy());
				} else {
					ipr.fillPolygon(pre);
				}
			} else {
				// DRAW A TRANSLUCENT DARK GLASS PANE OVER THE DARKER SURFACE
				// (IF NOT A SOLID COLOR)
				if (!bSolidColor) {
					pre.setBackground(DARK_GLASS);
				}
				if (bDeferred) {
					alModel.add(pre.copy());
				} else {
					ipr.fillPolygon(pre);
				}
			}
			if (!bDeferred) {
				ipr.drawPolygon(pre);
			}
		}
		if (!alModel.isEmpty()) {
			WrappedInstruction wi = new WrappedInstruction(getDeferredCache(), alModel, PrimitiveRenderEvent.FILL,
					zorder_hint);
			wi.setCompareBounds(compareBounds);
			dc.addModel(wi);
		}
	}

	/**
	 * Renders planes as 3D presentation.
	 *
	 * @param ipr
	 * @param oSource
	 * @param loaFace
	 * @param f
	 * @param lia
	 * @param dcache
	 * @throws ChartException
	 */
	protected final void render3DPlane(IPrimitiveRenderer ipr, Object oSource, List<Location3D[]> loaFace, Fill f,
			LineAttributes lia) throws ChartException {
		Polygon3DRenderEvent pre = ((EventObjectCache) ipr).getEventObject(oSource, Polygon3DRenderEvent.class);
		pre.setDoubleSided(false);

		int nSides = loaFace.size();

		for (int i = 0; i < nSides; i++) {
			pre.setOutline(lia);
			pre.setPoints3D(loaFace.get(i));
			pre.setBackground(f);
			dc.addPlane(pre, PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW);
		}
	}

	/**
	 * Finds particular Y value from given location list.
	 *
	 * @param loa       Location list.
	 * @param iProperty This value must be one of following:
	 *                  <ul>
	 *                  <li>IConstants.MIN
	 *                  <li>IConstants.MAX
	 *                  <li>IConstants.AVERAGE
	 *                  </ul>
	 *
	 */
	public static final double getY(Location[] loa, int iProperty) {
		int iCount = loa.length;
		double dY = 0;
		if (iProperty == IConstants.MIN) {
			dY = loa[0].getY();
			for (int i = 1; i < iCount; i++) {
				dY = Math.min(dY, loa[i].getY());
			}
		} else if (iProperty == IConstants.MAX) {
			dY = loa[0].getY();
			for (int i = 1; i < iCount; i++) {
				dY = Math.max(dY, loa[i].getY());
			}
		} else if (iProperty == IConstants.AVERAGE) {
			for (int i = 0; i < iCount; i++) {
				dY += loa[i].getY();
			}
			dY /= iCount;
		}
		return dY;
	}

	/**
	 * Finds particular X value from given location list.
	 *
	 * @param loa       Location list.
	 * @param iProperty This value must be one of following:
	 *                  <ul>
	 *                  <li>IConstants.MIN
	 *                  <li>IConstants.MAX
	 *                  <li>IConstants.AVERAGE
	 *                  </ul>
	 *
	 * @return x value
	 */
	public static final double getX(Location[] loa, int iProperty) {
		int iCount = loa.length;
		double dX = 0;
		if (iProperty == IConstants.MIN) {
			dX = loa[0].getX();
			for (int i = 1; i < iCount; i++) {
				dX = Math.min(dX, loa[i].getX());
			}
		} else if (iProperty == IConstants.MAX) {
			dX = loa[0].getX();
			for (int i = 1; i < iCount; i++) {
				dX = Math.max(dX, loa[i].getX());
			}
		} else if (iProperty == IConstants.AVERAGE) {
			for (int i = 0; i < iCount; i++) {
				dX += loa[i].getX();
			}
			dX /= iCount;
		}
		return dX;
	}

	/**
	 * post-process the triggers.
	 *
	 * @param tg     The Trigger to modify
	 * @param source The StructureSource associated with the Trigger
	 */
	public void processTrigger(Trigger tg, StructureSource source) {
		// use user action renderer first.
		IActionRenderer iar = getRunTimeContext().getActionRenderer();
		if (iar != null) {
			iar.processAction(tg.getAction(), source, rtc);
		}

		// internal processing.
		if (StructureType.LEGEND_ENTRY.equals(source.getType())) {
			if (tg.getAction().getType() == ActionType.URL_REDIRECT_LITERAL) {
				LegendItemHints lerh = (LegendItemHints) source.getSource();

				// BUILD URI
				assembleURLs(tg, lerh);
			}
		} else if (StructureType.SERIES_DATA_POINT.equals(source.getType())) {
			DataPointHints dph = (DataPointHints) source.getSource();

			if (tg.getAction().getType() == ActionType.SHOW_TOOLTIP_LITERAL) {
				// BUILD THE VALUE
				String toolText = ((TooltipValue) tg.getAction().getValue()).getText();

				// if it's null, then use DataPoint label automatically.
				// !!! DON'T check zero length string here, since this has a
				// particular meaning to avoid the tooltip.
				if (toolText == null)// || toolText.length( ) == 0 )
				{
					((TooltipValue) tg.getAction().getValue()).setText(dph.getDisplayValue());
				}
			} else if (tg.getAction().getType() == ActionType.URL_REDIRECT_LITERAL) {
				// BUILD URI
				assembleURLs(tg, dph);
			}
		}
	}

	/**
	 * @param tg
	 * @param valueHints
	 */
	private <T> void assembleURLs(Trigger tg, T valueHints) {
		if (tg.getAction() instanceof MultipleActions) {
			MultipleActions mas = (MultipleActions) tg.getAction();
			int size = mas.getActions().size();
			if (size == 0) {
				return;
			}
			if (size == 1 && mas.getActions().get(0).getValue() instanceof URLValue) {
				buildURI(valueHints, (URLValue) mas.getActions().get(0).getValue());
			} else {
				for (Action subAction : mas.getActions()) {
					ChartUtil.setLabelTo(subAction, rtc.getULocale());
					if (subAction.getValue() instanceof URLValue) {
						buildMultiURL(valueHints, (URLValue) subAction.getValue());
					}
				}
			}
		} else if (tg.getAction().getValue() instanceof MultiURLValues) {
			MultiURLValues muv = (MultiURLValues) tg.getAction().getValue();
			if (muv.getURLValues().size() == 0) {
				return;
			}
			if (muv.getURLValues().size() == 1) {
				buildURI(valueHints, muv.getURLValues().get(0));
			} else {
				for (URLValue uv : muv.getURLValues()) {
					buildMultiURL(valueHints, uv);
				}
			}
		} else if (tg.getAction().getValue() instanceof URLValue) {
			final URLValue uv = (URLValue) tg.getAction().getValue();
			buildURI(valueHints, uv);
		}

	}

	/**
	 * @param valueHints
	 * @param uv
	 */
	private <T> void buildURI(T valueHints, final URLValue uv) {
		String sBaseURL = uv.getBaseUrl();
		if (sBaseURL == null) {
			sBaseURL = ""; //$NON-NLS-1$
		}
		final StringBuilder sb = new StringBuilder(sBaseURL);
		char c = '?';
		if (sBaseURL.indexOf(c) != -1) {
			c = '&';
		}

		if (valueHints instanceof DataPointHints) {
			DataPointHints dph = (DataPointHints) valueHints;

			// It means current is building urls for series data.
			if (uv.getBaseParameterName() != null && uv.getBaseParameterName().length() > 0) {
				sb.append(c);
				c = '&';
				sb.append(URLValueImpl.encode(uv.getBaseParameterName()));
				sb.append('=');
				String urlValue = formatURLValue(dph.getBaseValue());
				sb.append(URLValueImpl.encode(urlValue));
			}

			if (uv.getValueParameterName() != null && uv.getValueParameterName().length() > 0) {
				sb.append(c);
				c = '&';
				sb.append(URLValueImpl.encode(uv.getValueParameterName()));
				sb.append('=');
				String urlValue = formatURLValue(dph.getOrthogonalValue());
				sb.append(URLValueImpl.encode(urlValue));
			}
		}
		if (uv.getSeriesParameterName() != null && uv.getSeriesParameterName().length() > 0) {
			sb.append(c);
			c = '&';
			sb.append(URLValueImpl.encode(uv.getSeriesParameterName()));
			sb.append('=');
			String urlValue;
			if (valueHints instanceof DataPointHints) {
				urlValue = formatURLValue(((DataPointHints) valueHints).getSeriesValue());
			} else {
				urlValue = ((LegendItemHints) valueHints).getItemText();
			}
			sb.append(URLValueImpl.encode(urlValue));
		}
		uv.setBaseUrl(sb.toString());
	}

	/**
	 * @param valueHints
	 * @param uv
	 */
	private <T> void buildMultiURL(T valueHints, final URLValue uv) {
		String sBaseURL = uv.getBaseUrl();
		if (sBaseURL == null) {
			sBaseURL = ""; //$NON-NLS-1$
		}
		final StringBuilder sb = new StringBuilder(""); //$NON-NLS-1$
		sb.append(sBaseURL);
		char c = '?';
		if (sBaseURL.indexOf(c) != -1) {
			c = '&';
		}
		boolean hasCategory = false;
		boolean hasValueData = false;
		boolean hasSeriesName = false;

		if (valueHints instanceof DataPointHints) {
			// It means current is building urls for series data.

			if (uv.getBaseParameterName() != null && uv.getBaseParameterName().length() > 0) {
				sb.append(c);
				c = '&';
				sb.append(URLValueImpl.encode(uv.getBaseParameterName()));
				sb.append('=');
				sb.append("\"+"); //$NON-NLS-1$
				sb.append(ScriptHandler.BASE_VALUE);
				hasCategory = true;
			}

			if (uv.getValueParameterName() != null && uv.getValueParameterName().length() > 0) {
				if (hasCategory) {
					sb.append("+\""); //$NON-NLS-1$
				}
				sb.append(c);
				c = '&';
				sb.append(URLValueImpl.encode(uv.getValueParameterName()));
				sb.append('=');
				sb.append("\"+"); //$NON-NLS-1$
				sb.append(ScriptHandler.ORTHOGONAL_VALUE);
				hasValueData = true;
			}
		}

		if (uv.getSeriesParameterName() != null && uv.getSeriesParameterName().length() > 0) {
			if (hasCategory || hasValueData) {
				sb.append("+\""); //$NON-NLS-1$
			}
			sb.append(c);
			c = '&';
			sb.append(URLValueImpl.encode(uv.getSeriesParameterName()));
			sb.append('=');
			sb.append("\"+"); //$NON-NLS-1$
			sb.append(ScriptHandler.SERIES_VALUE);
			hasSeriesName = true;
		}

		if (hasCategory || hasValueData || hasSeriesName) {
			sb.insert(0, "\""); //$NON-NLS-1$
		}
		uv.setBaseUrl(sb.toString());
	}

	/**
	 * Formats value in URL parameters so that it can be read in server
	 *
	 * @param value
	 * @return
	 */
	private String formatURLValue(Object value) {
		if (value instanceof Calendar) {
			// Bugzilla#215442 fix a parse issue to date
			// Bugzilla#245920 Just using default locale to format date string
			// to avoid passing locale-specific value for drill-through.
			return DateFormat.getDateInstance(DateFormat.LONG).format(value);
		}
		if (value instanceof Number) {
			// Do not output decimal for integer value, and also avoid double
			// precision error for double value
			Number num = (Number) value;
			if (ChartUtil.mathEqual(num.doubleValue(), num.intValue())) {
				return String.valueOf(num.intValue());
			}
			return String.valueOf(ValueFormatter.normalizeDouble(num.doubleValue()));
		}
		return ChartUtil.stringValue(value);
	}

	/**
	 * @return Returns the current cell bounds associated with current series.
	 * @see #getCellBounds(int)
	 */
	protected final Bounds getCellBounds() {
		return getCellBounds(iSeriesIndex);
	}

	/**
	 * Returns the bounds of an individual cell (if the rendered model is a
	 * ChartWithoutAxis and plot is to be split into a grid) or the entire plot
	 * bounds (if the rendered model is a ChartWithAxis).
	 *
	 * @return
	 */
	protected final Bounds getCellBounds(int seriesIndex) {
		PlotComputation obj = getComputations();

		Bounds bo = null;

		if (obj instanceof PlotWithoutAxes) {
			PlotWithoutAxes pwoa = (PlotWithoutAxes) obj;
			Coordinates co = pwoa.getCellCoordinates(seriesIndex - 1);
			Size sz = pwoa.getCellSize();

			bo = goFactory.copyOf(pwoa.getPlotBounds());
			bo.setLeft(bo.getLeft() + co.getColumn() * sz.getWidth());
			bo.setTop(bo.getTop() + co.getRow() * sz.getHeight());
			bo.setWidth(sz.getWidth());
			bo.setHeight(sz.getHeight());
			bo = goFactory.adjusteBounds(bo, pwoa.getPlotInsets());
		} else if (obj instanceof PlotWithAxes) {
			PlotWithAxes pwa = (PlotWithAxes) obj;

			bo = goFactory.copyOf(pwa.getPlotBounds());
			bo = goFactory.adjusteBounds(bo, pwa.getPlotInsets());
		}

		return bo;
	}

	/**
	 * Returns the bounds of the plot area, NOTE this bounds has reduced the insets.
	 *
	 * @return
	 */
	protected final Bounds getPlotBounds() {
		PlotComputation oComputation = getComputations();
		Bounds bo = goFactory.adjusteBounds(oComputation.getPlotBounds(), oComputation.getPlotInsets());
		return bo;
	}

	/**
	 * This convenience method renders the data point label along with the shadow If
	 * there's a need to render the data point label and the shadow separately, each
	 * call should be made separately by calling into the primitive rendering
	 * interface directly.
	 */
	public final void renderLabel(Object oSource, int iTextRenderType, Label laDataPoint, Position lp, Location lo,
			Bounds bo) throws ChartException {
		// Bugzilla#216718 data point labels should be inside area including axes
		limitDataPointLabelLocation(getModel(), xs, laDataPoint, getDeviceScale(), lo, lp);
		renderLabel(oSource, iTextRenderType, laDataPoint, lp, lo, bo, dc);
	}

	private void limitDataPointLabelLocation(Chart cm, IDisplayServer xs, Label laDataPoint, double dScale, Location lo,
			Position lp) throws ChartException {
		if (lo == null || cm instanceof ChartWithoutAxes) {
			return;
		}

		ChartWithAxes cwa = (ChartWithAxes) cm;
		Plot p = cwa.getPlot();

		BoundingBox bb = cComp.computeBox(xs, IConstants.ABOVE, laDataPoint, 0, 0);

		Bounds boCa = goFactory.scaleBounds(p.getBounds(), dScale);

		double rotation = laDataPoint.getCaption().getFont().getRotation();
		int state = 0;
		// use 1 to 8 to indicate the state, starts from the bottom
		// center,counter-clockwise
		switch (lp.getValue()) {
		case Position.ABOVE:
			if (rotation > 0 && rotation < 90) {
				state = 8;
			} else if (rotation < 0 && rotation > -90) {
				state = 2;
			} else {
				state = 1;
			}
			break;
		case Position.RIGHT:
			if (rotation > 0 && rotation < 90) {
				state = 8;
			} else if (rotation < 0 && rotation > -90) {
				state = 6;
			} else {
				state = 7;
			}
			break;
		case Position.BELOW:
			if (rotation > 0 && rotation < 90) {
				state = 4;
			} else if (rotation < 0 && rotation > -90) {
				state = 6;
			} else {
				state = 5;
			}
			break;
		case Position.LEFT:
			if (rotation > 0 && rotation < 90) {
				state = 4;
			} else if (rotation < 0 && rotation > -90) {
				state = 2;
			} else {
				state = 3;
			}
			break;
		}
		double dYmin, dYmax, dXmin, dXmax;
		switch (state) {
		case 1:
			dYmin = boCa.getTop() + bb.getHeight();
			dYmax = boCa.getTop() + boCa.getHeight();
			dXmin = boCa.getLeft() + bb.getWidth() / 2;
			dXmax = boCa.getLeft() + boCa.getWidth() - bb.getWidth() / 2;
			break;
		case 2:
			dYmin = boCa.getTop() + bb.getHeight();
			dYmax = boCa.getTop() + boCa.getHeight();
			dXmin = boCa.getLeft() + bb.getWidth();
			dXmax = boCa.getLeft() + boCa.getWidth();
			break;
		case 3:
			dYmin = boCa.getTop() + bb.getHeight() / 2;
			dYmax = boCa.getTop() + boCa.getHeight() - bb.getHeight() / 2;
			dXmin = boCa.getLeft() + bb.getWidth();
			dXmax = boCa.getLeft() + boCa.getWidth();
			break;
		case 4:
			dYmin = boCa.getTop();
			dYmax = boCa.getTop() + boCa.getHeight() - bb.getHeight();
			dXmin = boCa.getLeft() + bb.getWidth();
			dXmax = boCa.getLeft() + boCa.getWidth();
			break;
		case 5:
			dYmin = boCa.getTop();
			dYmax = boCa.getTop() + boCa.getHeight() - bb.getHeight();
			dXmin = boCa.getLeft() + bb.getWidth() / 2;
			dXmax = boCa.getLeft() + boCa.getWidth() - bb.getWidth() / 2;
			break;
		case 6:
			dYmin = boCa.getTop();
			dYmax = boCa.getTop() + boCa.getHeight() - bb.getHeight();
			dXmin = boCa.getLeft();
			dXmax = boCa.getLeft() + boCa.getWidth() - bb.getWidth();
			break;
		case 7:
			dYmin = boCa.getTop() + bb.getHeight() / 2;
			dYmax = boCa.getTop() + boCa.getHeight() - bb.getHeight() / 2;
			dXmin = boCa.getLeft();
			dXmax = boCa.getLeft() + boCa.getWidth() - bb.getWidth();
			break;
		case 8:
			dYmin = boCa.getTop() + bb.getHeight();
			dYmax = boCa.getTop() + boCa.getHeight();
			dXmin = boCa.getLeft();
			dXmax = boCa.getLeft() + boCa.getWidth() - bb.getWidth();
			break;
		default:
			dYmin = lo.getY();
			dYmax = lo.getY();
			dXmin = lo.getX();
			dXmax = lo.getX();
			break;
		}

		if (lo.getY() < dYmin) {
			lo.setY(dYmin);
		}
		if (lo.getY() > dYmax) {
			lo.setY(dYmax);
		}
		if (lo.getX() < dXmin) {
			lo.setX(dXmin);
		}
		if (lo.getX() > dXmax) {
			lo.setX(dXmax);
		}

	}

	/**
	 * Renderer label with specified <code>DeferredCache</code>.
	 *
	 * @param oSource
	 * @param iTextRenderType
	 * @param laDataPoint
	 * @param lp
	 * @param lo
	 * @param bo
	 * @param _dc
	 * @throws ChartException
	 */
	public final void renderLabel(Object oSource, int iTextRenderType, Label laDataPoint, Position lp, Location lo,
			Bounds bo, DeferredCache _dc) throws ChartException {
		final IDeviceRenderer idr = getDevice();
		TextRenderEvent tre = ((EventObjectCache) idr).getEventObject(oSource, TextRenderEvent.class);
		if (iTextRenderType != TextRenderEvent.RENDER_TEXT_IN_BLOCK) {
			tre.setTextPosition(Methods.getLabelPosition(lp));
			tre.setLocation(lo);
		} else {
			tre.setBlockBounds(bo);
			tre.setBlockAlignment(null);
		}
		tre.setLabel(laDataPoint);
		tre.setAction(iTextRenderType);
		if (_dc == null) {
			dc.addLabel(tre);
		} else {
			_dc.addLabel(tre);
		}
	}

	/**
	 * This method validates the given datapoints.
	 */
	protected void validateNullDatapoint(DataPointHints[] dphs) throws ChartException {
		if (dphs == null) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
					"exception.base.orthogonal.null.datapoint", //$NON-NLS-1$
					Messages.getResourceBundle(rtc.getULocale()));
		}
	}

	/**
	 * This method validates the dataset state from given series rendering hints.
	 */
	protected void validateDataSetCount(ISeriesRenderingHints isrh) throws ChartException {
		if ((isrh.getDataSetStructure()
				& ISeriesRenderingHints.BASE_ORTHOGONAL_OUT_OF_SYNC) == ISeriesRenderingHints.BASE_ORTHOGONAL_OUT_OF_SYNC) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
					"exception.base.orthogonal.inconsistent.count", //$NON-NLS-1$
					new Object[] { Integer.valueOf(isrh.getBaseDataSet().size()),
							Integer.valueOf(isrh.getOrthogonalDataSet().size()) },
					Messages.getResourceBundle(rtc.getULocale()));
		}
	}

	/**
	 * Filters the Null or invalid entry(contains NaN value) from the list. Each
	 * entry should be a double[2] or double[3] array object.
	 *
	 * @param ll
	 * @return
	 */
	protected List<double[]> filterNull(List<double[]> ll) {
		List<double[]> al = new ArrayList<>();
		for (int i = 0; i < ll.size(); i++) {
			double[] obj = ll.get(i);

			if (obj == null || Double.isNaN(obj[0]) || Double.isNaN(obj[1])) {
				continue;
			}

			al.add(obj);
		}

		return al;
	}

	/**
	 * Filters the Null or invalid entry(contains NaN value) from the array.
	 *
	 * @param ll
	 * @return
	 */
	protected Location[] filterNull(Location[] ll) {
		ArrayList<Location> al = new ArrayList<>();
		for (int i = 0; i < ll.length; i++) {
			if (Double.isNaN(ll[i].getX()) || Double.isNaN(ll[i].getY())) {
				continue;
			}

			al.add(ll[i]);
		}

		if (ll instanceof Location3D[]) {
			return al.toArray(new Location3D[al.size()]);
		}
		return al.toArray(new Location[al.size()]);
	}

	/**
	 * Filters the Null or invalid entry(contains NaN value) from the array in
	 * respect of DataPointHints.
	 *
	 * @param ll
	 * @return
	 */
	protected static Location[] filterNull(Location[] ll, DataPointHints[] dpha) throws ChartException {
		if (ll == null || dpha == null || ll.length != dpha.length) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION, new IllegalArgumentException());
		}

		int iLen = dpha.length;

		ArrayList<Location> al = new ArrayList<>(iLen);
		for (int i = 0; i < iLen; i++) {
			if (dpha[i].getBaseValue() != null && dpha[i].getOrthogonalValue() != null) {
				al.add(ll[i]);
			}
		}

		if (ll instanceof Location3D[]) {
			return al.toArray(new Location3D[al.size()]);
		}
		return al.toArray(new Location[al.size()]);
	}

	/**
	 * Check the if the given value is NaN.
	 *
	 * @param value
	 * @return
	 */
	protected static boolean isNaN(Object value) {
		return (value == null) || (value instanceof Number && Double.isNaN(((Number) value).doubleValue()));
	}

	/**
	 * Returns if the right-left mode is enabled.
	 *
	 */
	public boolean isRightToLeft() {
		if (rtc == null) {
			return false;
		}
		return rtc.isRightToLeft();
	}

	/**
	 * Returns if current palette is from the category series.
	 *
	 * @return
	 */
	protected boolean isPaletteByCategory() {
		return (cm.getLegend().getItemType().getValue() == LegendItemType.CATEGORIES);
	}

	/**
	 * Switch Anchor value due to right-left setting.
	 *
	 * @param anchor
	 */
	public Anchor switchAnchor(Anchor anchor) {
		if (anchor != null && isRightToLeft()) {
			switch (anchor.getValue()) {
			case Anchor.EAST:
				anchor = Anchor.WEST_LITERAL;
				break;
			case Anchor.NORTH_EAST:
				anchor = Anchor.NORTH_WEST_LITERAL;
				break;
			case Anchor.SOUTH_EAST:
				anchor = Anchor.SOUTH_WEST_LITERAL;
				break;
			case Anchor.WEST:
				anchor = Anchor.EAST_LITERAL;
				break;
			case Anchor.NORTH_WEST:
				anchor = Anchor.NORTH_EAST_LITERAL;
				break;
			case Anchor.SOUTH_WEST:
				anchor = Anchor.SOUTH_EAST_LITERAL;
				break;
			}
		}
		return anchor;
	}

	/**
	 * Switch Position value due to right-left setting.
	 *
	 * @param po
	 */
	public Position switchPosition(Position po) {
		if (po != null && isRightToLeft()) {
			if (po == Position.RIGHT_LITERAL) {
				po = Position.LEFT_LITERAL;
			} else if (po == Position.LEFT_LITERAL) {
				po = Position.RIGHT_LITERAL;
			}
		}
		return po;
	}

	/**
	 * Switch TextAlignment value due to right-left setting.
	 *
	 * @param ta
	 */
	public TextAlignment switchTextAlignment(TextAlignment ta) {
		if (ta != null && isRightToLeft()) {
			if (ta.getHorizontalAlignment() == HorizontalAlignment.LEFT_LITERAL) {
				ta.setHorizontalAlignment(HorizontalAlignment.RIGHT_LITERAL);
			} else if (ta.getHorizontalAlignment() == HorizontalAlignment.RIGHT_LITERAL) {
				ta.setHorizontalAlignment(HorizontalAlignment.LEFT_LITERAL);
			}
		}
		return ta;
	}

	/**
	 * Returns if interactivity is enabled on the model.
	 *
	 */
	public boolean isInteractivityEnabled() {
		return (cm.getInteractivity() == null || cm.getInteractivity().isEnable());
	}

	/**
	 * Returns if the corresponding category entry is filtered as minslice in
	 * legend. Subclass should override this method to implement their own legend
	 * strategy.
	 *
	 * @return return null if no minslice applied or minslice feature is not
	 *         supported.
	 */
	public Collection<Integer> getFilteredMinSliceEntry(DataSetIterator dsi) {
		// no filter by default.
		return null;
	}

	/**
	 * Updates the tranlucency of the fill according to series setting.
	 *
	 * @param fill
	 * @param se
	 */
	public void updateTranslucency(Fill fill, Series se) {
		if (se != null && se.isTranslucent()) {
			if (fill instanceof ColorDefinition) {
				((ColorDefinition) fill).setTransparency((int) (OVERRIDE_TRANSPARENCY * 255d / 100d));
			} else if (fill instanceof MultipleFill) {
				for (int i = 0; i < ((MultipleFill) fill).getFills().size(); i++) {
					updateTranslucency(((MultipleFill) fill).getFills().get(i), se);
				}
			}
		}
	}

	/**
	 * Set current <code>DeferredCacheManager</code> instance.
	 *
	 * @param dcm specified instance of <code>DeferredCacheMananger</code>.
	 */
	public void setDeferredCacheManager(DeferredCacheManager dcm) {
		fDeferredCacheManager = dcm;
	}

	/**
	 * Returns <code>DeferredCacheManager</code> instance.
	 *
	 * @return <code>DeferredCacheManager</code> instance.
	 */
	public DeferredCacheManager getDeferredCacheManager() {
		return fDeferredCacheManager;
	}

	/**
	 * Creates the interaction event for triggers list
	 *
	 * @param iSource
	 * @param elTriggers
	 * @param ipr
	 * @return
	 */
	protected final InteractionEvent createEvent(StructureSource iSource, List<Trigger> elTriggers,
			IPrimitiveRenderer ipr) {
		final InteractionEvent iev = new InteractionEvent(iSource);
		for (int t = 0; t < elTriggers.size(); t++) {
			Trigger tg = goFactory.copyOf(elTriggers.get(t));
			processTrigger(tg, iSource);
			iev.addTrigger(tg);
		}
		return iev;
	}

	/**
	 * Renders the interactivity hotspot for a data point
	 *
	 * @param ipr
	 * @param dph
	 * @param pre
	 * @throws ChartException
	 */
	protected final void renderInteractivity(IPrimitiveRenderer ipr, DataPointHints dph, PrimitiveRenderEvent pre)
			throws ChartException {
		if (isInteractivityEnabled() && dph != null && getSeries() != null) {
			// PROCESS 'SERIES LEVEL' TRIGGERS USING SOURCE='bs'
			final EList<Trigger> elTriggers = getSeries().getTriggers();
			if (!elTriggers.isEmpty()) {
				final StructureSource iSource = WrappedStructureSource.createSeriesDataPoint(getSeries(), dph);

				final InteractionEvent iev = createEvent(iSource, elTriggers, ipr);
				iev.setCursor(getSeries().getCursor());

				iev.setHotSpot(pre);
				ipr.enableInteraction(iev);
			}
		}
	}

	protected boolean isFirstVisibleSeries() {
		if (iSeriesIndex == 0) {
			return false;
		}
		for (int i = 1; i < iSeriesCount; i++) {
			BaseRenderer renderer = getRenderer(i);
			if (renderer.getSeries().isVisible()) {
				return i == iSeriesIndex;
			}
		}
		return false;
	}

	protected boolean isLastSeries() {
		if (iSeriesIndex == 0) {
			return false;
		}
		return (iSeriesIndex == iSeriesCount - 1);
	}

	protected Label getExternalizedCopy(Label la) {
		Label laCopy = goFactory.copyOf(getModel().getEmptyMessage());
		Text caption = laCopy.getCaption();
		caption.setValue(getRunTimeContext().externalizedMessage(caption.getValue()));
		return laCopy;
	}

	protected void renderEmptyPlot(IPrimitiveRenderer ipr, Plot p, Bounds bo) throws ChartException {

		StructureSource oSource = StructureSource.createPlot(p);

		// render text
		renderChartMessage(ipr, bo, oSource);

	}

	private void renderChartMessage(IPrimitiveRenderer ipr, Bounds bo, StructureSource oSource) throws ChartException {
		if (getModel().getEmptyMessage().isVisible()) {
			Label la = getExternalizedCopy(getModel().getEmptyMessage());

			EventObjectCache eoc = (EventObjectCache) ipr;
			final TextRenderEvent tre = eoc.getEventObject(oSource, TextRenderEvent.class);
			tre.setBlockBounds(bo);
			tre.setLabel(la);
			if (rtc.isRightToLeftText()) {
				tre.setRtlCaption();
			}

			LabelLimiter lbLimiter = new LabelLimiter(bo.getWidth(), bo.getHeight(), 0);
			lbLimiter.computeWrapping(xs, la);
			lbLimiter.limitLabelSize(cComp, xs, la);

			tre.setBlockAlignment(la.getCaption().getFont().getAlignment());
			tre.setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);

			// Rendering chart message after other chart elements are rendered
			// to make sure the chart message displays on top.
			fDeferredCacheManager.getLastDeferredCache().addLabel(tre);
		}
	}
}
