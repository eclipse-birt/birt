/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.model.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This class provides static methods to check status of chart's elements.
 *
 */

public class ChartElementUtil {

	public static int PROPERTY_UPDATE = 1;
	public static int PROPERTY_UNSET = 1 << 1;

	/**
	 * This method uses reflection to set a value to EMF's property as default
	 * value, but avoid to update the related 'isSet' flag.
	 *
	 * @param obj
	 * @param fieldName
	 * @param value
	 * @throws ChartException
	 */
	@SuppressWarnings("rawtypes")
	public static void setDefaultValue(Object obj, String fieldName, Object value) throws ChartException {
		Field field = null;
		try {
			Class cc = obj.getClass();
			boolean existence = false;
			while (!existence && cc != null) {
				try {
					field = cc.getDeclaredField(fieldName);
					existence = true;
				} catch (NoSuchFieldException e) {
					cc = cc.getSuperclass();
				}
			}
			if (field == null) {
				return;
			}
			boolean accessible = field.isAccessible();
			if (!accessible) {
				field.setAccessible(true);
				field.set(obj, value);
				field.setAccessible(accessible);
			} else {
				field.set(obj, value);
			}
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.INVALID_DATA_TYPE, e);
		}
	}

	/**
	 * @param eContainer EMF model container
	 * @param attribute  the attribute that belongs to the container.
	 * @param value      value
	 * @param isUnset    indicates if executing unset action.
	 */
	public static void setEObjectAttribute(EObject eContainer, String attribute, Object value, boolean isUnset) {
		EStructuralFeature esf = eContainer.eClass().getEStructuralFeature(attribute);
		if (esf == null) {
			return;
		}
		if (isUnset) {
			eContainer.eUnset(esf);
		} else {
			eContainer.eSet(esf, value);
		}
	}

	/**
	 * @param eContainer
	 * @param attribute
	 * @return value
	 */
	public static Object getEObjectAttributeValue(EObject eContainer, String attribute) {
		EStructuralFeature esf = eContainer.eClass().getEStructuralFeature(attribute);
		if (esf == null) {
			return null;
		}
		return eContainer.eGet(esf);
	}

	/**
	 * Checks if a attribute is set value.
	 *
	 * @param eContainer
	 * @param attribute
	 * @return true if attribute is set value.
	 */
	public static boolean isSetEObjectAttribute(EObject eContainer, String attribute) {
		EStructuralFeature esf = eContainer.eClass().getEStructuralFeature(attribute);
		if (esf == null) {
			return false;
		}
		return eContainer.eIsSet(esf);
	}

	/**
	 * Check if series palette is set.
	 *
	 * @param chart
	 * @return true if chart specify series palette.
	 */
	public static boolean isSetSeriesPalette(Chart chart) {
		Palette p = ChartUtil.getCategorySeriesDefinition(chart).getSeriesPalette();
		if (p != null && p.getEntries().size() > 0) {
			return true;
		}
		return false;
	}

	public static boolean isSetInsets(Insets insets) {
		return (insets.isSetTop() && insets.isSetBottom() && insets.isSetLeft() && insets.isSetRight());
	}

	public static boolean isSetDataPointComponents(Series series) {
		return !series.getDataPoint().getComponents().isEmpty();
	}

	public static boolean isSetStringProperty(String s) {
		return !(s == null || s.trim().equals("")); //$NON-NLS-1$
	}

	/**
	 * Copy a list.
	 *
	 * @param <T>
	 * @param objs
	 * @return list of type T
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> copyInstance(List<T> objs) {
		List<T> lst = new ArrayList<>();
		for (Object o : objs) {
			if (o instanceof IChartObject) {
				lst.add((T) ((IChartObject) o).copyInstance());
			} else {
				lst.add((T) o);
			}
		}
		return lst;
	}

	/**
	 * Copy a map.
	 *
	 * @param <T>
	 * @param <J>
	 * @param objs
	 * @return map of type <T, J>
	 */
	@SuppressWarnings("unchecked")
	public static <T, J> Map<T, J> copyInstance(Map<T, J> objs) {
		Map<T, J> map = new HashMap<>();
		for (Entry<T, J> o : objs.entrySet()) {
			if (o.getValue() instanceof IChartObject) {
				map.put(o.getKey(), (J) ((IChartObject) o.getValue()).copyInstance());
			} else {
				map.put(o.getKey(), o.getValue());
			}
		}
		return map;
	}
}
