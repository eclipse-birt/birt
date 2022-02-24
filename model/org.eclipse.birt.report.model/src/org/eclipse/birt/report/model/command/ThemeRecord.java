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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ThemeEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.AbstractTheme;
import org.eclipse.birt.report.model.elements.interfaces.ISupportThemeElementConstants;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Records a change to the theme of a report design.
 */

public class ThemeRecord extends SimpleRecord {

	/**
	 * The target Theme
	 */

	private ElementRefValue newTheme;

	/**
	 * The target Theme
	 */

	private ElementRefValue oldTheme;

	/**
	 * The element to operate. It must be ISupportThemeElement.
	 */

	protected DesignElement element;

	/**
	 * Constructs the library record.
	 * 
	 * @param module   the module
	 * @param newTheme the new theme
	 */

	ThemeRecord(DesignElement element, ElementRefValue newTheme) {
		this.element = element;
		this.newTheme = newTheme;

		oldTheme = (ElementRefValue) element.getLocalProperty(element.getRoot(),
				ISupportThemeElementConstants.THEME_PROP);

		label = CommandLabelFactory.getCommandLabel(MessageConstants.SET_THEME_MESSAGE);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform(boolean undo) {
		// if undo, must unresolve the current theme; if do/redo, must unresolve
		// the previous theme

		if (undo) {
			element.setProperty(ISupportThemeElementConstants.THEME_PROP, oldTheme);
			updateStyles(newTheme);
		} else {
			element.setProperty(ISupportThemeElementConstants.THEME_PROP, newTheme);
			updateStyles(oldTheme);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getEvent()
	 */

	public NotificationEvent getEvent() {
		return new ThemeEvent(element);
	}

	/**
	 * Unresolves references of styles of a theme.
	 * 
	 * @param theme the theme
	 */

	private void updateStyles(ElementRefValue theme) {
		// if the old theme is empty of not resolved. Do not need to unresolve.

		if (theme == null)
			return;

		if (!theme.isResolved())
			return;

		AbstractTheme t = (AbstractTheme) theme.getElement();
		List<StyleElement> styles = t.getAllStyles();
		Iterator<StyleElement> iter = styles.iterator();
		while (iter.hasNext()) {
			StyleElement style = iter.next();
			style.updateClientReferences();
		}
	}
}
