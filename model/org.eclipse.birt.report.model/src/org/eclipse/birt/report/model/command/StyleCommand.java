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

package org.eclipse.birt.report.model.command;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.StyleUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Sets the style property of an element.
 * 
 */

public class StyleCommand extends AbstractElementCommand {

	private static final Logger LOG = Logger.getLogger(StyleCommand.class.getName());

	/**
	 * Constructor.
	 * 
	 * @param module the root of <code>obj</code>
	 * @param obj    the element to modify.
	 */

	public StyleCommand(Module module, DesignElement obj) {
		super(module, obj);
	}

	/**
	 * Sets the style of an element.
	 * 
	 * @param name the name of the style to set.
	 * @throws StyleException if the element can not have style or the style is not
	 *                        found.
	 */

	public void setStyle(String name) throws StyleException {
		name = StringUtil.trimString(name);

		// Ensure that the element can have a style.

		if (!element.getDefn().hasStyle())
			throw new StyleException(element, name, StyleException.DESIGN_EXCEPTION_FORBIDDEN);
		StyledElement obj = (StyledElement) element;

		// Ensure that the style exists.

		ElementPropertyDefn propDefn = obj.getPropertyDefn(IStyledElementModel.STYLE_PROP);

		if (name == null && obj.getStyleName() == null)
			return;

		Object retValue = null;

		try {
			retValue = propDefn.validateValue(module, element, name);
		} catch (PropertyValueException e) {
			LOG.log(Level.SEVERE, "Property value invalid", e);
			assert false;
		}

		// Make the change.

		doSetStyleRefValue((ElementRefValue) retValue);
	}

	/**
	 * Sets the style of an element given the style itself.
	 * 
	 * @param style the style element to set.
	 * @throws StyleException if the element can not have style or the style is not
	 *                        found.
	 */

	public void setStyleElement(DesignElement style) throws StyleException {
		// Make the change starting with the name. This will handle the
		// case where the application is trying to set a style that is
		// not part of the design.

		String name = null;
		if (style != null)
			name = style.getFullName();
		setStyle(name);
	}

	/**
	 * Sets the extends attribute for an element given the new parent element.
	 * 
	 * @param parent the new parent element.
	 * @throws StyleException if the element can not be extended or the base element
	 *                        is not on component slot, or the base element has no
	 *                        name.
	 */

	public void setStyleElement(StyleHandle parent) throws StyleException {
		if (parent == null) {
			setStyle(null);
			return;
		}

		setStyle(parent.getName());
	}

	/**
	 * Sets the theme with the given element reference value. Call this method when
	 * the theme name or theme element has been validated. Otherwise, uses
	 * {@link #setStyle(String)} or {@link #setStyleElement(Theme)}.
	 * 
	 * @param refValue the validated reference value
	 * @throws StyleException if the style is not found.
	 */

	protected void setStyleRefValue(ElementRefValue refValue) throws StyleException {

		if (refValue == null) {
			if ((element instanceof StyledElement) && (((StyledElement) element).getStyleName() == null)) {
				return;
			}
		}

		doSetStyleRefValue(refValue);
	}

	/**
	 * Does the work to set the new style with the given <code>newStyleValue</code>.
	 * 
	 * @param newStyleValue the validated <code>ElementRefValue</code>
	 */

	private void doSetStyleRefValue(ElementRefValue newStyleValue) throws StyleException {
		if (newStyleValue != null && !newStyleValue.isResolved()) {
			if (!StyleUtil.hasExternalCSSURI(module)) {
				throw new StyleException(element, newStyleValue.getName(), StyleException.DESIGN_EXCEPTION_NOT_FOUND);
			}
		}

		if (newStyleValue != null && newStyleValue.isResolved() && newStyleValue.getElement() == element.getStyle())
			return;

		// Make the change.

		StyledElement obj = (StyledElement) element;

		StyleRecord record = new StyleRecord(obj, newStyleValue);
		getActivityStack().execute(record);

	}
}
