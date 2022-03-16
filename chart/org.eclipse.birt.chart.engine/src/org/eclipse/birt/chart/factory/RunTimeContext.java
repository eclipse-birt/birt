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

package org.eclipse.birt.chart.factory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.chart.computation.IChartComputation;
import org.eclipse.birt.chart.computation.LabelLimiter;
import org.eclipse.birt.chart.computation.LegendItemRenderingHints;
import org.eclipse.birt.chart.computation.LegendLayoutHints;
import org.eclipse.birt.chart.computation.withaxes.SharedScaleContext;
import org.eclipse.birt.chart.device.IStructureDefinitionListener;
import org.eclipse.birt.chart.event.EventObjectCache;
import org.eclipse.birt.chart.event.StructureChangeEvent;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.chart.script.AbstractScriptHandler;
import org.eclipse.birt.chart.script.IScriptClassLoader;
import org.eclipse.birt.chart.script.IScriptContext;
import org.eclipse.birt.core.i18n.ResourceHandle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Encapsulates runtime information associated with each chart generation and
 * rendering session. It contains global objects that are defined per request.
 */
public final class RunTimeContext implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * The locale associated with the runtime context.
	 */
	private ULocale lcl = null;

	/**
	 * A chart script context associated with a chart model.
	 */
	private IScriptContext csc = null;

	/**
	 * A script handler associated with a chart model.
	 */
	private transient AbstractScriptHandler<?> sh = null;

	/**
	 * A resource handle capable of retrieving externalized messages.
	 */
	private transient ResourceHandle rh = null;

	/**
	 * A structure definition listener associated with this runtime context.
	 */
	private transient IStructureDefinitionListener isdl = null;

	/**
	 * An action renderer associated with this runtime context.
	 */
	private transient IActionRenderer iar = null;

	/**
	 * An script classLoader associated with this runtime context.
	 */
	private transient IScriptClassLoader iscl = null;

	/**
	 * A legend item layout hints asscociated with current context.
	 */
	private transient LegendLayoutHints lilh = null;

	/**
	 * A map holds all series renderers for current context.
	 */
	private transient Map<Series, LegendItemRenderingHints> seriesRenderers = null;

	/**
	 * A map to store user defined state object.
	 */
	private HashMap<Object, Object> stateStore = null;

	/**
	 * Specifies if enable scripting support in current context.
	 */
	private boolean enableScripting = true;

	/**
	 * Specifies if right-left mode is enabled.
	 */
	private int iRightToLeft = -1;
	// bidi_acgc added begin
	/**
	 * Specifies if the displayed text bidi reading order is Right To Left.
	 */
	private int iRightToLeftText = -1;
	// bidi_acgc added end
	/**
	 * Specified the shared context among multiple chart instances
	 */
	private transient SharedScaleContext sharedScale;

	/**
	 * The field indicates if current chart is sharing query with other
	 * module(table/crosstab...).
	 *
	 * @since 2.3
	 */
	private boolean isSharingQuery = false;

	private transient IResourceFinder resourceFinder = null;

	private transient IExternalizer externalizer = null;

	private transient Chart defaultValueChart;

	private TimeZone timeZone = null;

	private boolean useNonHierarchyCategoryData = false;

	/**
	 * A default zero-arg public constructor used for object creation.
	 */
	public RunTimeContext() {
		stateStore = new HashMap<>(2);
	}

	/**
	 * Puts a state object to the store.
	 *
	 * @param key
	 * @param state
	 */
	public void putState(Object key, Object state) {
		stateStore.put(key, state);
	}

	/**
	 * Returns the state object from store by the key.
	 *
	 * @param key
	 * @return
	 */
	public Object getState(Object key) {
		return stateStore.get(key);
	}

	/**
	 * Removes the state object by the key.
	 *
	 * @param key
	 * @return
	 */
	public Object removeState(Object key) {
		return stateStore.remove(key);
	}

	/**
	 * Clears all the stored states.
	 */
	public void clearState() {
		IChartComputation cComp = getState(StateKey.CHART_COMPUTATION_KEY);

		if (cComp != null) {
			cComp.dispose();
		}

		for (Iterator<Map.Entry<Object, Object>> iter = stateStore.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<Object, Object> entry = iter.next();
			Object key = entry.getKey();
			if (key instanceof StateKey<?>) {
				if (((StateKey<?>) key).needClear()) {
					iter.remove();
				}
			} else {
				iter.remove();
			}
		}
	}

	/**
	 * Returns if scriting is enabled in current context.
	 *
	 * @return
	 */
	public boolean isScriptingEnabled() {
		return enableScripting;
	}

	/**
	 * Sepcifies if to enable scripting in current context.
	 *
	 * @param value
	 */
	public void setScriptingEnabled(boolean value) {
		enableScripting = value;
	}

	/**
	 * Returns the script classLoader if available.
	 *
	 * @return
	 */
	public IScriptClassLoader getScriptClassLoader() {
		return iscl;
	}

	/**
	 * Sets the script classLoader.
	 *
	 * @param value
	 */
	public void setScriptClassLoader(IScriptClassLoader value) {
		iscl = value;
	}

	/**
	 * Internally sets an instance of the structure definition listener for device
	 * renderers that need a structure definition notification when rendering
	 * primitives.
	 *
	 * @param isdl The structure definition listener associated with the runtime
	 *             context.
	 */
	public void setStructureDefinitionListener(IStructureDefinitionListener isdl) {
		this.isdl = isdl;
	}

	/**
	 * Returns an instance of the structure definition listner for device renderers
	 * that need a structure definition notification when rendering primitives.
	 *
	 * @return The structure definition listener associated with the runtime
	 *         context.
	 */
	public IStructureDefinitionListener getStructureDefinitionListener() {
		return isdl;
	}

	/**
	 * Sets an IActionRenderer instance to current runtime context.
	 *
	 * @param iar
	 */
	public void setActionRenderer(IActionRenderer iar) {
		this.iar = iar;
	}

	/**
	 * Returns the IActionRenderer of current runtime context.
	 *
	 * @return
	 */
	public IActionRenderer getActionRenderer() {
		return this.iar;
	}

	/**
	 * Sets the LegendItemLayoutHints for current context.
	 *
	 * @param lilh
	 */
	public void setLegendLayoutHints(LegendLayoutHints lilh) {
		this.lilh = lilh;
	}

	/**
	 * Returns the LegendItemLayoutHints for current context.
	 *
	 * @return
	 */
	public LegendLayoutHints getLegendLayoutHints() {
		return lilh;
	}

	/**
	 * Sets the series renderers for current context.
	 *
	 * @param msr
	 */
	public void setSeriesRenderers(Map<Series, LegendItemRenderingHints> msr) {
		this.seriesRenderers = msr;
	}

	/**
	 * Returns the series renderers for current context.
	 *
	 * @return
	 */
	public Map<Series, LegendItemRenderingHints> getSeriesRenderers() {
		return seriesRenderers;
	}

	/**
	 * Notifies the structure definition listener of a change in the current running
	 * structure that defines a group of primitives being rendered and puts them
	 * into context with reference to the source object.
	 *
	 * @param sEventName Defines the structure being defined along with the event
	 *                   type
	 * @param oSource    The source object on which the structure is being defined
	 *
	 * @return 'true' if the structure definition listener exists and was notified
	 *         of the change or 'false' otherwise.
	 */
	public boolean notifyStructureChange(String sEventName, Object oSource) {
		if (isdl == null) {
			return false;
		}
		StructureChangeEvent scev = ((EventObjectCache) isdl).getEventObject(oSource, StructureChangeEvent.class);
		scev.setEventName(sEventName);
		isdl.changeStructure(scev);
		return true;
	}

	/**
	 * Returns the locale associated with this runtime context.
	 *
	 * @return The locale associated with this runtime context.
	 * @deprecated use {@link #getULocale()} instead.
	 */
	@Deprecated
	public Locale getLocale() {
		return lcl == null ? null : lcl.toLocale();
	}

	/**
	 * Sets the locale associated with this runtime context. This is usually done
	 * when chart generation begins.
	 *
	 * @param lcl The locale associated with the runtime context.
	 * @deprecated use {@link #setULocale(ULocale)} instead.
	 */
	@Deprecated
	public void setLocale(Locale lcl) {
		this.lcl = ULocale.forLocale(lcl);
	}

	/**
	 * Returns the locale associated with this runtime context.
	 *
	 * @return The locale associated with this runtime context.
	 * @since 2.1
	 */
	public ULocale getULocale() {
		return lcl;
	}

	/**
	 * Sets the locale associated with this runtime context. This is usually done
	 * when chart generation begins.
	 *
	 * @param lcl The locale associated with the runtime context.
	 * @since 2.1
	 */
	public void setULocale(ULocale lcl) {
		this.lcl = lcl;
	}

	/**
	 * Returns if current context is in a right-left platform. e.g. Arabic, Hebrew.
	 *
	 * @return
	 */
	public boolean isRightToLeft() {
		// Report direction does no longer depend on Locale, and so the code below is
		// commented out.
		// bidi_acgc deleted start
		/*
		 * if ( iRightToLeft == -1 ) { iRightToLeft = ChartUtil.isRightToLeftLocale( lcl
		 * ) ? 1 : 0; }
		 */
		// bidi_acgc deleted end
		return iRightToLeft == 1;
	}

	/**
	 * Sets the right-left mode for current context mandatorily.
	 *
	 * @param value
	 */
	public void setRightToLeft(boolean value) {
		iRightToLeft = value ? 1 : 0;
	}

	/**
	 * Returns an instance of the resource handle for which chart specific messages
	 * are externalized.
	 *
	 * @return An instance of the resource handle for which chart specific messages
	 *         are externalized.
	 */
	public ResourceHandle getResourceHandle() {
		return rh;
	}

	/**
	 * Specifies a resource handle that facilitates retrieval of chart specific
	 * externalized messages.
	 *
	 * @param rh The resource handle.
	 */
	public void setResourceHandle(ResourceHandle rh) {
		this.rh = rh;
	}

	/**
	 * Returns an instance of a transient script handler associated with the chart
	 * being generated. The script handler is capable of executing callback scripts
	 * defined in the chart model.
	 *
	 * @return An instance of the script handler.
	 */
	@SuppressWarnings("unchecked")
	public AbstractScriptHandler getScriptHandler() {
		return sh;
	}

	/**
	 * Sets an instance of a transient script handler associated with the chart
	 * being generated. The script handler is capable of executing callback scripts
	 * defined in the chart model.
	 *
	 * @param sh An instance of the script handler.
	 */
	@SuppressWarnings("unchecked")
	public void setScriptHandler(AbstractScriptHandler sh) {
		this.sh = sh;
	}

	/**
	 * Returns an instance of a script context associated with the chart being
	 * generated.
	 *
	 * @return An instance of the script context.
	 */
	public IScriptContext getScriptContext() {
		return csc;
	}

	/**
	 * Sets an instance of a chart script context associated with the chart being
	 * generated.
	 *
	 * @param csc An instance of the chart script context.
	 */
	public void setScriptContext(IScriptContext csc) {
		this.csc = csc;
	}

	/**
	 * Defines an externalized message lookup implementation per chart model being
	 * executed.
	 *
	 * @param iml The externalized message lookup implementation.
	 */
	public void setMessageLookup(IMessageLookup iml) {
	}

	/**
	 * A convenience method provided to lookup externalized messages associated with
	 * a given message key.
	 *
	 * @param sChartKey The key using which an externalized message is being looked
	 *                  up.
	 *
	 * @return The externalized message associated with the specified key.
	 */
	public String externalizedMessage(String sChartKey) {
		if (sChartKey == null) {
			return ""; //$NON-NLS-1$
		}
		// The key can be either alone, or with its default value:
		// "key=defaultvalue"

		/*
		 * Possible cases: chartkey lookup badkey nolookup
		 * ------------------------------------- a=b get(a) b b b get(b) b b =b b - b
		 */
		String sKey = sChartKey;
		String sDefaultValue = sChartKey;
		final int iKeySeparator = sChartKey.indexOf(IExternalizer.KEY_SEPARATOR);

		if (iKeySeparator != -1) {
			// VALUE ON RHS OF IMessageLookup.KEY_SEPARATOR
			sDefaultValue = sChartKey.substring(iKeySeparator + 1);

		}

		if (externalizer == null) {
			// no lookup cases
			return sDefaultValue;
		} else {
			// lookup cases
			if (iKeySeparator > 0) {
				// a=b case
				sKey = sChartKey.substring(0, iKeySeparator);
			} else if (iKeySeparator == 0) {
				// =b case
				return sDefaultValue;
			} else {
				// b case
				sKey = sDefaultValue;
			}

			return externalizer.externalizedMessage(sKey, sDefaultValue, this.getULocale());
		}

	}

	/**
	 * Sets the shared scale
	 *
	 * @param scale shared scale context
	 * @since 2.5
	 */
	public void setSharedScale(SharedScaleContext ssContext) {
		this.sharedScale = ssContext;
	}

	/**
	 * Returns the shared scale
	 *
	 * @return the shared scale context
	 * @since 2.5
	 */
	public SharedScaleContext getSharedScale() {
		return this.sharedScale;
	}

	public boolean isSharingQuery() {
		return isSharingQuery;
	}

	public void setSharingQuery(boolean isSharingQuery) {
		this.isSharingQuery = isSharingQuery;
	}

	// bidi_acgc added begin
	/**
	 * Returns true if current text reading order is Right To Left
	 *
	 * @return
	 */
	public boolean isRightToLeftText() {
		return iRightToLeftText == 1;
	}

	/**
	 * Sets the bidi text mode of the current chart object .
	 *
	 * @param value
	 */
	public void setRightToLeftText(boolean value) {
		iRightToLeftText = value ? 1 : 0;
	}
	// bidi_acgc added end

	/**
	 * @return Returns the resourceFinder.
	 */
	public IResourceFinder getResourceFinder() {
		return resourceFinder;
	}

	/**
	 * @param resourceFinder The resourceFinder to set.
	 */
	public void setResourceFinder(IResourceFinder resourceFinder) {
		this.resourceFinder = resourceFinder;
	}

	/**
	 * @return Returns the externalizer.
	 */
	public IExternalizer getExternalizer() {
		return externalizer;
	}

	/**
	 * @param externalizer The externalizer to set.
	 */
	public void setExternalizer(IExternalizer externalizer) {
		this.externalizer = externalizer;
	}

	@SuppressWarnings("unchecked")
	public <T> T getState(StateKey<T> key) {
		return (T) stateStore.get(key);
	}

	public <T> void putState(StateKey<T> key, T value) {
		stateStore.put(key, value);
	}

	/**
	 * Predifined static keys for states.
	 */
	public static class StateKey<T> {
		private boolean needClear = true;

		private StateKey(boolean needClear) {
			this.needClear = needClear;
		}

		/**
		 * Check if the state should be clear itself after chart rendering.
		 *
		 * @return
		 */
		public boolean needClear() {
			return needClear;
		}

		public static <T> StateKey<T> create(boolean needClear) {
			return new StateKey<>(needClear);
		}

		/**
		 * Key to reference if the data of chart is empty.
		 */
		public final static StateKey<Boolean> DATA_EMPTY_KEY = StateKey.create(false);

		/**
		 * Key to reference LabelLimiter lookup table.
		 */
		public final static StateKey<Map<Label, LabelLimiter>> LABEL_LIMITER_LOOKUP_KEY = StateKey.create(false);

		/**
		 * Key to reference Chart Computation.
		 */
		public final static StateKey<IChartComputation> CHART_COMPUTATION_KEY = StateKey.create(false);
	}

	/**
	 * Sets instance of default value chart.
	 *
	 * @param cm
	 */
	public void setDefaultValueChart(Chart cm) {
		this.defaultValueChart = cm;
	}

	/**
	 * Returns instance of default value chart.
	 *
	 * @return instance of default value chart.
	 */
	public Chart getDefaultValueChart() {
		return this.defaultValueChart;
	}

	/**
	 * @return Returns the timeZone.
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone The timeZone to set.
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	public void enableNonHierarchyCategoryData(boolean enabled) {
		useNonHierarchyCategoryData = enabled;
	}

	public boolean useNonHierarchyCategoryData() {
		return useNonHierarchyCategoryData;
	}
}
