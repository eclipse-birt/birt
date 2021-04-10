/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IDimLevel;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.elements.olap.Measure;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.ContentExceptionFactory;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.eclipse.birt.report.model.util.copy.ContextCopyPastePolicy;
import org.eclipse.birt.report.model.util.copy.PasteStatus;

/**
 * The utility class for copy/paste. It is for the UI usage. Other uses should
 * use <code>DesignElementHandle.copy()</code>.
 * 
 */

public class CopyUtil {

	private static ContextCopyPastePolicy copyPastePolicy = new ContextCopyPastePolicy();

	/**
	 * Returns the copy of the current element.
	 * 
	 * @param source the given element
	 * 
	 * @return the copy of the given element
	 */

	public static IElementCopy copy(DesignElementHandle source) {
		return copyPastePolicy.createCopy(source.getElement(), source.getModule());
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not yet
	 * added to the design.
	 * 
	 * @param copy      the copy from the return value of <code>copy</code>
	 * @param container the target container
	 * @param slotID    the id of the target slot
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException if the element is not allowed in the slot
	 */

	public static List paste(IElementCopy copy, DesignElementHandle container, int slotID) throws SemanticException {
		ContainerContext context = new ContainerContext(container.getElement(), slotID);
		Module root = container.getModule();
		if (!canPaste(copy, container, slotID).canPaste())
			throw ContentExceptionFactory.createContentException(context,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED);

		IDesignElement chosen = copyPastePolicy.preWorkForPaste(context, copy, root);

		if (chosen == null)
			return Collections.EMPTY_LIST;

		DesignElementHandle target = chosen.getHandle(root);

		List<String> oldNames = null;
		DesignElement targetElement = target.getElement();
		if (targetElement instanceof Cube) {
			oldNames = collectOLAPNames(root, (Cube) targetElement);
		}

		container.getModuleHandle().rename(container, target);

		if (targetElement instanceof Cube) {
			updateCubeMeasure(root, (Cube) targetElement, oldNames);
		}

		container.getSlot(slotID).add(target);
		copyPastePolicy.copyPropertyBindings(copy, target);
		return checkPostPasteErrors(target.getElement(), root);
	}

	/**
	 * update the java script value on measure
	 * 
	 * @param module
	 * @param newCube
	 * @param oldNames
	 */
	private static void updateCubeMeasure(Module module, Cube newCube, List<String> oldNames) {
		List<String> newNames = collectOLAPNames(module, newCube);
		Map<String, String> nameMap = buildOLAPNameMap(oldNames, newNames);
		updateDerivedMeasure(module, newCube, nameMap);
	}

	private static void updateDerivedMeasure(Module module, Cube cube, Map<String, String> nameMap) {
		List<Measure> derivedMeasureList = new ArrayList<Measure>();

		LevelContentIterator iter = new LevelContentIterator(module, cube, 3);
		while (iter.hasNext()) {
			DesignElement innerElement = iter.next();
			if (innerElement instanceof Measure) {
				Measure measure = (Measure) innerElement;
				if (measure.getBooleanProperty(module, MeasureHandle.IS_CALCULATED_PROP)) {
					derivedMeasureList.add(measure);
				}
			}
		}
		if (!derivedMeasureList.isEmpty()) {
			for (Measure derivedMeasure : derivedMeasureList) {
				Expression expr = (Expression) derivedMeasure.getProperty(module,
						MeasureHandle.MEASURE_EXPRESSION_PROP);
				Expression newExpr = getUpdatedExpression(expr, nameMap);
				if (newExpr != null) {
					derivedMeasure.setProperty(MeasureHandle.MEASURE_EXPRESSION_PROP, newExpr);
				}
			}
		}
	}

	private static Expression getUpdatedExpression(Expression old, Map<String, String> nameMap) {
		if (old == null)
			return null;

		String expr = old.getStringExpression();
		String type = old.getType();
		Map<String, String> updateMap = getUpdateBindingMap(expr, nameMap, type);

		if (updateMap != null && !updateMap.isEmpty()) {
			String newExpr = expr;
			for (Entry<String, String> entry : updateMap.entrySet()) {
				String oldName = entry.getKey();
				String newName = entry.getValue();
				newExpr = newExpr.replaceAll("(\\W)" + oldName + "(\\W)", //$NON-NLS-1$ //$NON-NLS-2$
						"$1" + newName + "$2"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			return new Expression(newExpr, old.getUserDefinedType());
		}
		return null;
	}

	private static Map<String, String> getUpdateBindingMap(String expr, Map<String, String> nameMap, String type) {
		Map<String, String> updateMap = new HashMap<String, String>();
		if (IExpressionType.JAVASCRIPT.equalsIgnoreCase(type)) {
			// for the measure expression case
			Set<String> measureNameSet = null;

			try {
				measureNameSet = ExpressionUtil.getAllReferencedMeasures(expr);
			} catch (CoreException e) {
				// Do nothing
			}

			if (measureNameSet != null && !measureNameSet.isEmpty()) {
				for (String measureName : measureNameSet) {
					String newName = nameMap.get(measureName);
					if (newName != null)
						updateMap.put(measureName, newName);
				}
			} else {
				Set<IDimLevel> tmpSet = null;
				try {
					tmpSet = ExpressionUtil.getReferencedDimLevel(expr);
				} catch (CoreException e) {
					// do nothing
					return null;
				}

				Iterator<IDimLevel> dimLevels = tmpSet.iterator();
				while (dimLevels.hasNext()) {
					IDimLevel tmpObj = dimLevels.next();

					String oldName = tmpObj.getDimensionName();
					String newName = nameMap.get(oldName);
					if (newName == null)
						continue;

					if (!newName.equals(oldName))
						updateMap.put(oldName, newName);
				}
			}

		}
		return updateMap;
	}

	private static Map<String, String> buildOLAPNameMap(List<String> oldNames, List<String> newNames) {
		Map<String, String> retMap = new HashMap<String, String>();
		for (int i = 0; i < oldNames.size(); i++) {
			String oldName = oldNames.get(i);
			String newName = newNames.get(i);

			retMap.put(oldName, newName);
		}

		return retMap;
	}

	private static List<String> collectOLAPNames(Module module, DesignElement cube) {
		List<String> retMap = new ArrayList<String>();

		LevelContentIterator iter = new LevelContentIterator(module, cube, 3);
		while (iter.hasNext()) {
			DesignElement innerElement = iter.next();
			if (innerElement instanceof Dimension || innerElement instanceof Measure)
				retMap.add(innerElement.getName());
			else if (innerElement instanceof Level)
				retMap.add(((Level) innerElement).getFullName());
		}

		return retMap;
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not yet
	 * added to the design.
	 * 
	 * @param copy      the copy from the return value of <code>copy</code>
	 * @param container the target container
	 * @param slotID    the id of the target slot
	 * @param newPos    the target position
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException if the element is not allowed in the slot
	 */

	public static List paste(IElementCopy copy, DesignElementHandle container, int slotID, int newPos)
			throws SemanticException {
		ContainerContext context = new ContainerContext(container.getElement(), slotID);

		Module root = container.getModule();

		if (!canPaste(copy, container, slotID).canPaste())
			throw ContentExceptionFactory.createContentException(context,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED);

		IDesignElement chosen = copyPastePolicy.preWorkForPaste(context, copy, root);

		if (chosen == null)
			return Collections.EMPTY_LIST;

		DesignElementHandle target = chosen.getHandle(root);

		List<String> oldNames = null;
		DesignElement targetElement = target.getElement();
		if (targetElement instanceof Cube) {
			oldNames = collectOLAPNames(root, (Cube) targetElement);
		}

		container.getModuleHandle().rename(container, target);

		if (targetElement instanceof Cube) {
			updateCubeMeasure(root, (Cube) targetElement, oldNames);
		}

		container.getSlot(slotID).add(target, newPos);
		copyPastePolicy.copyPropertyBindings(copy, target);
		return checkPostPasteErrors(target.getElement(), root);
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not yet
	 * added to the design.
	 * 
	 * @param copy      the copy from the return value of <code>copy</code>
	 * @param container the target container
	 * @param propName  the property name of the target container
	 * 
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException if the element is not allowed in the slot
	 */

	public static List paste(IElementCopy copy, DesignElementHandle container, String propName)
			throws SemanticException {
		ContainerContext context = new ContainerContext(container.getElement(), propName);

		Module root = container.getModule();

		if (!canPaste(copy, container, propName).canPaste())
			throw ContentExceptionFactory.createContentException(context,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED);

		IDesignElement chosen = copyPastePolicy.preWorkForPaste(context, copy, root);

		if (chosen == null)
			return Collections.EMPTY_LIST;

		DesignElementHandle target = chosen.getHandle(root);
		List<String> oldNames = null;
		DesignElement targetElement = target.getElement();
		if (targetElement instanceof Cube) {
			oldNames = collectOLAPNames(root, (Cube) targetElement);
		}
		container.getModuleHandle().rename(container, target);
		if (targetElement instanceof Cube) {
			updateCubeMeasure(root, (Cube) targetElement, oldNames);
		}
		container.add(propName, target);
		copyPastePolicy.copyPropertyBindings(copy, target);
		return checkPostPasteErrors(target.getElement(), root);
	}

	/**
	 * Pastes a report item to the slot. The item must be newly created and not yet
	 * added to the design.
	 * 
	 * @param copy      the copy from the return value of <code>copy</code>
	 * @param container the target container
	 * @param propName  the property name of the target container
	 * @param newPos    the target position
	 * 
	 * @return a list containing all errors for the pasted element
	 * @throws SemanticException if the element is not allowed in the slot
	 */

	public static List paste(IElementCopy copy, DesignElementHandle container, String propName, int newPos)
			throws SemanticException {
		ContainerContext context = new ContainerContext(container.getElement(), propName);

		Module root = container.getModule();

		if (!canPaste(copy, container, propName).canPaste())
			throw ContentExceptionFactory.createContentException(context,
					ContentException.DESIGN_EXCEPTION_CONTENT_NOT_ALLOWED_PASTED);

		IDesignElement chosen = copyPastePolicy.preWorkForPaste(context, copy, root);

		if (chosen == null)
			return Collections.EMPTY_LIST;

		DesignElementHandle target = chosen.getHandle(root);
		List<String> oldNames = null;
		DesignElement targetElement = target.getElement();
		if (targetElement instanceof Cube) {
			oldNames = collectOLAPNames(root, (Cube) targetElement);
		}
		container.getModuleHandle().rename(container, target);
		if (targetElement instanceof Cube) {
			updateCubeMeasure(root, (Cube) targetElement, oldNames);
		}
		container.add(propName, target, newPos);
		copyPastePolicy.copyPropertyBindings(copy, target);
		return checkPostPasteErrors(target.getElement(), root);
	}

	/**
	 * /** Checks whether the given copy can be pasted into the given slot of the
	 * specified element.
	 * 
	 * @param copy      the copied instance
	 * @param container the target element
	 * @param slotID    the target slot id
	 * @return <code>true</code> is the copy is good for pasting. Otherwise
	 *         <code>false</code>.
	 */

	public static IPasteStatus canPaste(IElementCopy copy, DesignElementHandle container, int slotID) {
		int tmpIndex = container.getElement().getSlotIndex(slotID);

		// no such slot ID
		if (tmpIndex == -1) {
			PasteStatus status = new PasteStatus();
			status.setPaste(false);
			return status;
		}

		return copyPastePolicy.isValidCopy(new ContainerContext(container.getElement(), slotID), container.getModule(),
				copy);

	}

	/**
	 * /** Checks whether the given copy can be pasted into the given slot of the
	 * specified element.
	 * 
	 * @param copy      the copied instance
	 * @param container the target element
	 * @param propName  the target property name
	 * @return <code>true</code> is the copy is good for pasting. Otherwise
	 *         <code>false</code>.
	 */

	public static IPasteStatus canPaste(IElementCopy copy, DesignElementHandle container, String propName) {
		ElementPropertyDefn propDefn = (ElementPropertyDefn) container.getPropertyDefn(propName);

		// both element type and content element type can be pasted
		if (propDefn == null || !propDefn.isElementType()) {
			PasteStatus status = new PasteStatus();
			status.setPaste(false);
			return status;
		}

		return copyPastePolicy.isValidCopy(new ContainerContext(container.getElement(), propName),
				container.getModule(), copy);
	}

	/**
	 * Checks the element after the paste action.
	 * 
	 * @param content the pasted element
	 * 
	 * @return a list containing parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 */

	private static List checkPostPasteErrors(DesignElement content, Module root) {
		revisePropertyNameSpace(root, content, content.getDefn().getProperty(IDesignElementModel.EXTENDS_PROP));

		reviseNameSpace(root, content);

		List exceptionList = content.validateWithContents(root);
		List errorDetailList = ErrorDetail.convertExceptionList(exceptionList);

		return errorDetailList;
	}

	/**
	 * Uses the new name space of the current module for reference property values
	 * of the given element. This method checks the <code>content</code> and nested
	 * elements in it.
	 * 
	 * @param module    the module that <code>content</code> attaches.
	 * @param content   the element to revise
	 * @param nameSpace the new name space
	 */

	private static void reviseNameSpace(Module module, DesignElement content) {
		Iterator propNames = content.propertyWithLocalValueIterator();
		IElementDefn defn = content.getDefn();

		while (propNames.hasNext()) {
			String propName = (String) propNames.next();

			ElementPropertyDefn propDefn = (ElementPropertyDefn) defn.getProperty(propName);
			revisePropertyNameSpace(module, content, propDefn);
		}

		Iterator iter = new LevelContentIterator(module, content, 1);
		while (iter.hasNext()) {
			DesignElement item = (DesignElement) iter.next();
			reviseNameSpace(module, item);
		}
	}

	/**
	 * Uses the new name space of the current module for reference property values
	 * of the given element. This method checks the <code>content</code> and nested
	 * elements in it.
	 * 
	 * @param module    the module that <code>content</code> attaches.
	 * @param content   the element to revise
	 * @param propDefn  the property definition
	 * @param nameSpace the new name space
	 */

	private static void revisePropertyNameSpace(Module module, DesignElement content, IElementPropertyDefn propDefn) {
		if (propDefn == null || content == null)
			return;

		if (propDefn.getTypeCode() != IPropertyType.ELEMENT_REF_TYPE
				&& propDefn.getTypeCode() != IPropertyType.EXTENDS_TYPE)
			return;

		content.getLocalProperty(module, (ElementPropertyDefn) propDefn);
	}
}
