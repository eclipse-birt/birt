/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

/**
 * This class is responsible for updating chart value of extension chart
 * elements.
 *
 * @since 3.7
 */

public class ChartExtensionValueUpdater {

	private ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/trace"); //$NON-NLS-1$

	/**
	 * This set identifies which elements has visible attribute.
	 */
	private static Set<String> hasVisibleElementSet = new HashSet<>();
	static {
		hasVisibleElementSet.add(LineAttributes.class.getSimpleName());
		hasVisibleElementSet.add(Marker.class.getSimpleName());
		hasVisibleElementSet.add(Label.class.getSimpleName());
		hasVisibleElementSet.add(Series.class.getSimpleName());
		hasVisibleElementSet.add(Block.class.getSimpleName());
		hasVisibleElementSet.add(ClientArea.class.getSimpleName());
	}

	/**
	 * Returns <code>true</code> if specified class contains 'visible' attribute.
	 *
	 * @param clazz
	 * @return
	 */
	static boolean contanisVisibleElement(EClass clazz) {
		boolean contains = hasVisibleElementSet.contains(clazz.getName());
		if (contains) {
			return true;
		}
		EList<EClass> supers = clazz.getEAllSuperTypes();
		if (supers.size() > 0) {
			for (EClass eSuper : supers) {
				contains = hasVisibleElementSet.contains(eSuper.getName());
				if (contains) {
					return true;
				}
			}
		}

		return contains;
	}

	public static boolean isMapEntry(EClass eClass) {
		return (eClass.getInstanceClass() == Map.Entry.class);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void updateAttrs(EClass eClass, EObject eParentObj, EObject eObj, EObject eRef, EObject eDef) {

		List<EAttribute> listMany = new LinkedList<>();
		List<EAttribute> list = new LinkedList<>();

		for (EAttribute eAttr : eClass.getEAllAttributes()) {
			if (eAttr.isMany()) {
				listMany.add(eAttr);
			} else {
				list.add(eAttr);
			}
		}

		for (EAttribute eAttr : listMany) {
			List<?> vList = (List<?>) eObj.eGet(eAttr);
			if (vList.size() == 0) {
				if (eRef != null && ((List<?>) eRef.eGet(eAttr)).size() > 0) {
					vList.addAll((List) eRef.eGet(eAttr));
				} else if (eDef != null) {
					vList.addAll((List) eDef.eGet(eAttr));
				}
			}
		}

		for (EAttribute eAttr : list) {
			Object val = eObj.eGet(eAttr);
			if (eAttr.isUnsettable()) {
				if (!eObj.eIsSet(eAttr)) {
					if (eRef != null && eRef.eIsSet(eAttr)) {
						eObj.eSet(eAttr, eRef.eGet(eAttr));
					} else if (eDef != null && eDef.eIsSet(eAttr)) {
						eObj.eSet(eAttr, eDef.eGet(eAttr));
					}
				}
			} else if (val == null) {
				if (eRef != null && eRef.eGet(eAttr) != null) {
					eObj.eSet(eAttr, eRef.eGet(eAttr));
				} else if (eDef != null) {
					eObj.eSet(eAttr, eDef.eGet(eAttr));
				}
			}
		}
	}

	/**
	 * Updates chart element object.
	 *
	 * @param expected   class of expected chart element.
	 * @param name       chart element name
	 * @param eParentObj container of chart element object.
	 * @param eObj       chart element object.
	 * @param eRef       reference chart object to be used to update chart object's
	 *                   values.
	 * @param eDef       default chart object to be used to update chart object's
	 *                   values.
	 */
	public void update(EClass expected, String name, EObject eParentObj, EObject eObj, EObject eRef, EObject eDef) {
		if (eObj == null) {
			if (eRef != null) {
				if (eRef instanceof IChartObject) {
					eObj = ((IChartObject) eRef).copyInstance();
					ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				}
			} else if (eDef != null) {
				if (eDef instanceof IChartObject) {
					eObj = ((IChartObject) eDef).copyInstance();
					ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
					return;
				}
			}
		}
		if (eObj == null || (eRef == null && eDef == null)) {
			return;
		}

		// Process visible case.
		if (contanisVisibleElement(eObj.eClass())) {
			if (eObj.eIsSet(eObj.eClass().getEStructuralFeature("visible"))) //$NON-NLS-1$
			{
				if (eObj.eGet(eObj.eClass().getEStructuralFeature("visible")) != Boolean.TRUE) //$NON-NLS-1$
				{
					// If the visible attribute is set to false, directly return, no need
					// to update other attributes.
					return;
				}
			} else // If eObj isn't set visible and the visible attribute of
			// reference object is set to false, directly return, no need to
			// update other attributes.
			if (eRef != null && eRef.eIsSet(eRef.eClass().getEStructuralFeature("visible"))) //$NON-NLS-1$
			{
				if (eRef.eGet(eRef.eClass().getEStructuralFeature("visible")) != Boolean.TRUE) //$NON-NLS-1$
				{
					eObj.eSet(eRef.eClass().getEStructuralFeature("visible"), Boolean.FALSE); //$NON-NLS-1$
					return;
				}
			} else if (eDef != null && eDef.eIsSet(eDef.eClass().getEStructuralFeature("visible"))) //$NON-NLS-1$
			{
				if (eDef.eGet(eDef.eClass().getEStructuralFeature("visible")) != Boolean.TRUE) //$NON-NLS-1$
				{
					eObj.eSet(eDef.eClass().getEStructuralFeature("visible"), Boolean.FALSE); //$NON-NLS-1$
					return;
				}
			}
		}

		EClass eClass = eObj.eClass();

		// attributes
		updateAttrs(eClass, eParentObj, eObj, eRef, eDef);

		// list attributes

		// references
		for (EReference ref : eClass.getEAllReferences()) {
			String childName = ref.getName();
			Object child = eObj.eGet(ref);
			Object refChild = eRef != null ? eRef.eGet(ref) : null;
			Object defChild = eDef != null ? eDef.eGet(ref) : null;
			EObject eChildParntObj = eObj;
			if (child == null) {
				if (refChild != null) {
					if (refChild instanceof IChartObject) {
						child = updateFromReference(childName, refChild, eChildParntObj);
					}
				} else if (defChild != null) {
					if (defChild instanceof IChartObject) {
						child = ((IChartObject) defChild).copyInstance();
						ChartElementUtil.setEObjectAttribute(eChildParntObj, childName, child, false);
						continue;
					}
				}
			}

			if (child != null) {
				if (ref.isMany()) {
					int size = ((List<?>) child).size();
					for (int i = 0; i < size; i++) {
						Object item = ((List<?>) child).get(i);
						Object refItem = (refChild == null || (i >= ((List<?>) refChild).size())) ? null
								: ((List<?>) refChild).get(i);
						Object defItem = (defChild == null || (i >= ((List<?>) defChild).size())) ? null
								: ((List<?>) defChild).get(i);
						update(ref, eObj, (EObject) item, (EObject) refItem, (EObject) defItem);
					}
				} else {
					update(ref, eObj, (EObject) child, (EObject) refChild, (EObject) defChild);
				}
			}
		}
	}

	protected Object updateFromReference(String childName, Object refChild, EObject eChildParntObj) {
		Object child = ((IChartObject) refChild).copyInstance();
		ChartElementUtil.setEObjectAttribute(eChildParntObj, childName, child, false);
		return child;
	}

	private Map<String, EObject> defaultObjCache = new HashMap<>();

	/**
	 * Returns a chart element instance with default value.
	 *
	 * @param expected
	 * @param name
	 * @param eObj
	 * @return a chart element instance with default value.
	 */
	public EObject getDefault(EClass expected, String name, EObject eObj) {
		EObject def = defaultObjCache.get(eObj.getClass().getSimpleName());
		if (def != null) {
			return def;
		}

		Method m;
		try {
			m = eObj.getClass().getMethod("create"); //$NON-NLS-1$
			EObject object = (EObject) m.invoke(eObj);
			defaultObjCache.put(eObj.getClass().getSimpleName(), object);
			return object;
		} catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			logger.log(e);
		}
		return null;
	}

	private void update(EReference ref, EObject eParentObj, EObject eObj, EObject eRef, EObject eDef) {
		if (eObj instanceof DataPointComponent) {
			eDef = ChartDefaultValueUtil.getPercentileDataPointDefObj((DataPointComponent) eObj,
					(DataPointComponent) eDef);
		}

		update(ref.getEReferenceType(), ref.getName(), eParentObj, eObj, eRef, eDef);
	}
}
