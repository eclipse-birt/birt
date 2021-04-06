/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.IRenderInstruction;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * The class is used to manage runtime DeferredCache of series, it assures the
 * correct painting z-order of series for 2D case.
 * 
 * @since 2.2.1
 */
public final class DeferredCacheManager {

	/** Handle of concrete device renderer, its type may be SWT, Swing or SVG... */
	private final IDeviceRenderer fDeviceRenderer;

	/** Handle of chart object. */
	private final Chart fChart;

	/**
	 * The first deferred cache, it will be executed before other
	 * <code>DeferredCache</code> .
	 */
	private DeferredCache fFirstDC;

	/**
	 * The last deferred cache, it will be executed after other
	 * <code>DeferredCache</code> .
	 */
	private DeferredCache fLastDC;

	/**
	 * The single deferred cache object for some chart whose element must be put
	 * into a signle <code>DeferredCache</code> object.
	 * <p>
	 * Currently the stacked bar series, bubble seires and area series must be put
	 * into single cache.
	 */
	private DeferredCache fSingleDC;

	/** The list stores painting z-order of deferred order for series. */
	private final List<DeferredCache> fDeferredCacheList = new ArrayList<DeferredCache>();

	/**
	 * Constructor of the class.
	 * 
	 * @param idr   specified device renderer.
	 * @param chart specified chart instance.
	 */
	public DeferredCacheManager(IDeviceRenderer idr, Chart chart) {
		this.fDeviceRenderer = idr;
		fChart = chart;
		fFirstDC = new DeferredCache(fDeviceRenderer, fChart, -1);
		fLastDC = new DeferredCache(fDeviceRenderer, fChart, 10000);
	}

	private boolean hasStackedSeries() {
		int count = 0;
		for (SeriesDefinition sd : ChartUtil.getAllOrthogonalSeriesDefinitions(fChart)) {
			if (sd.getDesignTimeSeries() != null && sd.getDesignTimeSeries().isStacked()) {
				count++;
			}
			if (count > 1) {
				return true;
			}
		}
		return false;
	}

	private boolean needSignleDeferredCache(BaseRenderer br) {
		if (fSingleDC != null) {
			return true;
		}

		// For 3D chart or chart has stacked series, it just uses single
		// deferred cache to store shapes of all series.
		boolean is2DDepth = (fChart.getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);
		if (br != null && br.getSeries() != null
				&& (ChartDimension.THREE_DIMENSIONAL == fChart.getDimension().getValue()
						|| br.getSeries().isSingleCache() || (fChart instanceof ChartWithoutAxes && is2DDepth)
						|| (is2DDepth && hasStackedSeries()))) {
			return true;
		}
		return false;
	}

	/**
	 * Create <code>DeferredCache</code> instance for current series.
	 * 
	 * @param br current renderer.
	 * @return instance of <code>DeferredCache</code>
	 */
	public DeferredCache createDeferredCache(BaseRenderer br, int cacheIndex) {
		if (needSignleDeferredCache(br)) {
			return createSingleDeferredCache();
		} else {
			return createDeferredCache(cacheIndex);
		}
	}

	/**
	 * Create new <code>DeferredCache</code> instance.
	 * 
	 * @return <code>DeferredCache</code> instance.
	 */
	DeferredCache createDeferredCache(int cacheIndex) {
		DeferredCache dc = new DeferredCache(fDeviceRenderer, fChart, cacheIndex);
		fDeferredCacheList.add(dc);
		return dc;
	}

	/**
	 * Create <code>DeferredCache</code> instance for single case, the related
	 * <code>DeferredCache</code> will be only created once.
	 * 
	 * @return instance of <code>DeferredCache</code>.
	 */
	DeferredCache createSingleDeferredCache() {
		if (fSingleDC != null) {
			return fSingleDC;
		}

		fSingleDC = new DeferredCache(fDeviceRenderer, fChart, 0);
		fDeferredCacheList.add(fSingleDC);

		return fSingleDC;
	}

	/**
	 * Flush all <code>DeferredCache</code> in the manager.
	 * 
	 * @throws ChartException
	 */
	public void flushAll() throws ChartException {
		int options = DeferredCache.FLUSH_PLANE_SHADOW | DeferredCache.FLUSH_PLANE | DeferredCache.FLUSH_LINE
				| DeferredCache.FLUSH_3D;

		// Flush specified blocks.
		flushOptions(options);

		// Flush markers and labels.
		flushMarkersNLinesNLabels();

		clearDC();
	}

	/**
	 * Flush specified blocks.
	 *
	 * @param options
	 * @throws ChartException
	 */
	public void flushOptions(int options) throws ChartException {
		// 1. Flush first common blocks.
		fFirstDC.flushOptions(options);

		// 2. Flush data points one by one.
		for (java.util.Iterator<DeferredCache> iter = fDeferredCacheList.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof DeferredCache) {
				((DeferredCache) obj).flushOptions(options);
			}
		}

		// 3. flush last blocks.
		fLastDC.flushOptions(options);
	}

	/**
	 * Flush markers, connection lines and labels in all caches.
	 * 
	 * @throws ChartException
	 */
	void flushMarkersNLinesNLabels() throws ChartException {
		fFirstDC.flushOptions(DeferredCache.FLUSH_CONNECTION_LINE);
		fFirstDC.getAllConnectionLines().clear();
		fFirstDC.flushOptions(DeferredCache.FLUSH_MARKER);
		fFirstDC.getAllMarkers().clear();

		Collections.sort(fDeferredCacheList);
		for (DeferredCache cache : fDeferredCacheList) {
			cache.flushOptions(DeferredCache.FLUSH_CONNECTION_LINE);
			cache.getAllConnectionLines().clear();
			cache.flushOptions(DeferredCache.FLUSH_MARKER);
			cache.getAllMarkers().clear();
		}
		fLastDC.flushOptions(DeferredCache.FLUSH_CONNECTION_LINE);
		fLastDC.getAllConnectionLines().clear();
		fLastDC.flushOptions(DeferredCache.FLUSH_MARKER);
		fLastDC.getAllMarkers().clear();

		fFirstDC.flushOptions(DeferredCache.FLUSH_LABLE);
		fFirstDC.getAllLabels().clear();
		for (DeferredCache cache : fDeferredCacheList) {
			cache.flushOptions(DeferredCache.FLUSH_LABLE);
			cache.getAllLabels().clear();
		}
		fLastDC.flushOptions(DeferredCache.FLUSH_LABLE);
		fLastDC.getAllLabels().clear();
	}

	/**
	 * Get markers and labels from all caches.
	 * 
	 * @param allMarkers
	 * @param allLabels
	 */
	public void getMarkersNLabels(List<IRenderInstruction> allMarkers, List<TextRenderEvent> allLabels) {
		allMarkers.addAll(fFirstDC.getAllMarkers());
		fFirstDC.getAllMarkers().clear();
		allLabels.addAll(fFirstDC.getAllLabels());
		fFirstDC.getAllLabels().clear();

		for (java.util.Iterator<DeferredCache> iter = fDeferredCacheList.iterator(); iter.hasNext();) {
			DeferredCache obj = iter.next();
			allMarkers.addAll(obj.getAllMarkers());
			obj.getAllMarkers().clear();
			allLabels.addAll(obj.getAllLabels());
			obj.getAllLabels().clear();
		}

		allMarkers.addAll(fLastDC.getAllMarkers());
		fLastDC.getAllMarkers().clear();
		allLabels.addAll(fLastDC.getAllLabels());
		fLastDC.getAllLabels().clear();
	}

	/**
	 * Clear all <code>DeferredCache</code> instances.
	 */
	public void clearDC() {
		fDeferredCacheList.clear();

		fFirstDC = null;
		fLastDC = null;
		fSingleDC = null;
	}

	/**
	 * Returns first <code>DeferredCache</code> instance.
	 * 
	 * @return first <code>DeferredCache</code> instance.
	 */
	public DeferredCache getFirstDeferredCache() {
		return fFirstDC;
	}

	/**
	 * Returns last <code>DeferredCache</code> instance.
	 * 
	 * @return last <code>DeferredCache</code> instance.
	 */
	public DeferredCache getLastDeferredCache() {
		return fLastDC;
	}

	/**
	 * Pre-process all the 3D rendering events. This must be called before
	 * {@link #flushAll()}.
	 * 
	 * @param deferredCache specified deferred cache instance.
	 * @param engine
	 * @param xOffset
	 * @param yOffset
	 * @since 2.3
	 */
	public void process3DEvent(DeferredCache deferredCache, Engine3D engine, double xOffset, double yOffset) {
		if (deferredCache != null) {
			deferredCache.process3DEvent(engine, xOffset, yOffset);
		} else {
			fFirstDC.process3DEvent(engine, xOffset, yOffset);

			for (java.util.Iterator<DeferredCache> iter = fDeferredCacheList.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof DeferredCache) {
					((DeferredCache) obj).process3DEvent(engine, xOffset, yOffset);
				}
			}

			fLastDC.process3DEvent(engine, xOffset, yOffset);
		}
	}
}
