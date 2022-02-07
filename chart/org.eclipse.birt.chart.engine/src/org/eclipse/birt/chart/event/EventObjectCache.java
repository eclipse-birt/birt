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

package org.eclipse.birt.chart.event;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.util.SecurityUtil;

/**
 * An internal cache that provides reusable primitive (and block) rendering
 * event objects. A local cache is created per generation sequence so issues
 * with multithreaded access shouldn't arise.
 */
public class EventObjectCache {

	private transient Hashtable<Class<? extends ChartEvent>, ChartEvent> _htEvents;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/event"); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public EventObjectCache() {
		super();
		_htEvents = SecurityUtil.newHashtable();
	}

	/**
	 * Creates or returns the requested event object from local cache.
	 * 
	 * @param oSource
	 * @param cType
	 * 
	 * @return An instance of the requested event object that encapsulates rendering
	 *         attributes
	 */
	@SuppressWarnings("unchecked")
	public final <T extends ChartEvent> T getEventObject(Object oSource, Class<T> cType) {
		T event = (T) _htEvents.get(cType);
		if (event == null) {
			try {
				final Constructor<T> co = SecurityUtil.getConstructor(cType, new Class[] { Object.class });
				event = co.newInstance(new Object[] { oSource });
				_htEvents.put(cType, event);
			} catch (NoSuchMethodException nsmex) {
				logger.log(nsmex);
			} catch (InvocationTargetException itex) {
				logger.log(itex);
			} catch (IllegalAccessException iaex) {
				logger.log(iaex);
			} catch (InstantiationException iex) {
				logger.log(iex);
			}
		} else {
			event.setSourceObject(oSource);
			event.reset();
		}
		return event;
	}

	/**
	 * Validates the line attributes.
	 * 
	 * @param oSource
	 * @param lia
	 * @return
	 * @throws ChartException
	 */
	protected final boolean validateLineAttributes(Object oSource, LineAttributes lia) throws ChartException {
		if (lia == null) {
			return false;
		}

		if (!lia.isVisible()) {
			return false;
		}

		return true;
	}

	/**
	 * Validates the color attributes.
	 * 
	 * @param cdEdge
	 * @param fBackground
	 * @param ids
	 * @return
	 */
	protected final Object validateEdgeColor(ColorDefinition cdEdge, Fill fBackground, IDisplayServer ids) {
		Object cFG = null;
		if (cdEdge == null) {
			if (!(fBackground instanceof ColorDefinition) || (((ColorDefinition) fBackground).isSetTransparency()
					&& ((ColorDefinition) fBackground).getTransparency() == 0)) {
				return null;
			} else {
				cFG = ids.getColor(((ColorDefinition) fBackground).darker());
			}
		} else {
			cFG = ids.getColor(cdEdge);
		}
		return cFG;
	}

	/**
	 * Checkes if given fill is fully transparent.
	 * 
	 * @param fill
	 * @return
	 */
	protected final boolean isFullTransparent(Fill fill) {
		if (fill == null) {
			return true;
		}

		if (fill instanceof ColorDefinition) {
			ColorDefinition cd = (ColorDefinition) fill;
			return cd.isSetTransparency() && cd.getTransparency() == 0;
		} else if (fill instanceof Gradient) {
			Gradient g = (Gradient) fill;
			return g.isSetTransparency() && g.getTransparency() == 0;
		}

		return false;
	}

	protected final Fill validateMultipleFill(Fill fill) {
		if (!(fill instanceof MultipleFill)) {
			return fill;
		}

		if (((MultipleFill) fill).getFills().size() > 0) {
			return validateMultipleFill(((MultipleFill) fill).getFills().get(0));
		} else {
			return null;
		}
	}
}
