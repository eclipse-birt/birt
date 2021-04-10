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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.command.ThemeException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.AbstractTheme;
import org.eclipse.birt.report.model.elements.ReportItemTheme;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElement;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;

/**
 * Sets the theme of the report design.
 */

public class ThemeCommand extends AbstractElementCommand {

	/**
	 * Constructor.
	 * 
	 * @param module the module to set the theme
	 */

	public ThemeCommand(Module module, DesignElement element) {
		super(module, element);
		if (element.getPropertyDefn(ISupportThemeElementConstants.THEME_PROP) == null)
			throw new IllegalArgumentException("The element does not support theme property!"); //$NON-NLS-1$
	}

	/**
	 * Sets the theme of an element.
	 * 
	 * @param name the name of the theme to set.
	 * @throws SemanticException if the element can not have theme or the theme is
	 *                           not found.
	 */

	public void setTheme(String name) throws SemanticException {
		Object retValue = doValidateValue(name);

		doSetThemeRefValue((ElementRefValue) retValue);
	}

	/**
	 * Sets the theme of an element given the theme itself.
	 * 
	 * @param theme the theme element to set.
	 * @throws SemanticException if the element can not have theme or the theme is
	 *                           not found.
	 */

	public void setThemeElement(AbstractThemeHandle theme) throws SemanticException {
		if (theme == null) {
			setTheme(null);
			return;
		}

		String name = null;
		if (theme != null)
			name = ReferenceValueUtil.needTheNamespacePrefix(theme.getElement(), theme.getModule(), module);

		Object retValue = doValidateValue(name);

		// if the return element and the input element is not same, throws
		// exception

		ElementRefValue refValue = (ElementRefValue) retValue;
		if (refValue.isResolved() && refValue.getElement() != theme.getElement())
			throw new SemanticError(element,
					new String[] { ISupportThemeElementConstants.THEME_PROP, refValue.getName() },
					SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF);

		doSetThemeRefValue((ElementRefValue) retValue);
	}

	/**
	 * Sets the theme with the given element reference value. Call this method when
	 * the theme name or theme element has been validated. Otherwise, uses
	 * {@link #setTheme(String)} or {@link #setThemeElement(ThemeHandle)}.
	 * 
	 * @param refValue the validated reference value
	 * @throws SemanticException if the element can not have theme or the theme is
	 *                           not found.
	 */

	protected void setThemeRefValue(ElementRefValue refValue) throws SemanticException {
		if (refValue == null && ((ISupportThemeElement) element).getThemeName() == null)
			return;

		doSetThemeRefValue(refValue);
	}

	/**
	 * Validates the value of the input theme name.
	 * 
	 * @param name the theme name
	 * @return the <code>ElementRefValue</code>. Can be resolved or unresolved.
	 * @throws PropertyValueException if the value is not valid
	 */

	private Object doValidateValue(String name) throws SemanticException {
		name = StringUtil.trimString(name);

		ElementPropertyDefn propDefn = element.getPropertyDefn(ISupportThemeElementConstants.THEME_PROP);
		if (propDefn == null)
			throw new PropertyNameException(element, ISupportThemeElementConstants.THEME_PROP);

		if (name == null && ((ISupportThemeElement) element).getThemeName() == null)
			return null;

		return propDefn.validateValue(module, element, name);
	}

	/**
	 * Do the work to set the new theme with the given <code>newThemeValue</code>.
	 * 
	 * @param newThemeValue the validated <code>ElementRefValue</code>
	 */

	private void doSetThemeRefValue(ElementRefValue newThemeValue) throws SemanticException {
		if (newThemeValue != null) {
			// can not set an unresoved value
			String name = ReferenceValueUtil.needTheNamespacePrefix(newThemeValue, element.getRoot());
			if (!newThemeValue.isResolved()) {

				throw new ThemeException(element, name, ThemeException.DESIGN_EXCEPTION_NOT_FOUND);
			}

			// can not set theme in report item to a wrong type
			AbstractTheme theme = (AbstractTheme) newThemeValue.getElement();
			String matchedType = MetaDataDictionary.getInstance().getThemeType(element.getDefn());

			if (theme instanceof ReportItemTheme
					&& (matchedType == null || !matchedType.equals(((ReportItemTheme) theme).getType(module)))) {
				throw new ThemeException(element, name, ThemeException.DESIGN_EXCEPTION_WRONG_TYPE);
			}

		}

		if (newThemeValue != null && newThemeValue.isResolved()
				&& newThemeValue.getElement() == ((ISupportThemeElement) element).getTheme())
			return;

		// adjust the back references for styles in the theme

		ThemeRecord themeRecord = new ThemeRecord(element, newThemeValue);
		getModule().getActivityStack().execute(themeRecord);
	}
}