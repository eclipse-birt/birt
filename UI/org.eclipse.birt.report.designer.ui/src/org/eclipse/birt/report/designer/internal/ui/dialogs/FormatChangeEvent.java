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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.EventObject;

import org.eclipse.core.runtime.Assert;

/**
 * An event object describing a change to a format property.
 */

public class FormatChangeEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	/**
	 * The name of the changed format.
	 */
	private String formatName;

	/**
	 * The new category of the changed format.
	 */
	private String category;

	/**
	 * The new pattern of the changed format.
	 */
	private String pattern;

	private String locale;

	/**
	 * Creates a new format change event.
	 *
	 * @param source      the object whose format has changed
	 * @param name        the format that has changed (must not be
	 *                    <code>null</code>)
	 * @param newCategory the new category of the format
	 * @param newPattern  the new pattern of the format
	 */
	public FormatChangeEvent(Object source, String name, String newCategory, String newPattern, String newLocale) {
		super(source);
		Assert.isNotNull(name);
		this.formatName = name;
		this.category = newCategory;
		this.pattern = newPattern;
		this.locale = newLocale;
	}

	public String getLocale() {
		return locale;
	}

	/**
	 * Returns the format name.
	 *
	 * @return Returns the formatName.
	 */
	public String getName() {
		return formatName;
	}

	/**
	 * Returns the format category.
	 *
	 * @return Returns the category.
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Returns the format pattern.
	 *
	 * @return Returns the pattern.
	 */
	public String getPattern() {
		return pattern;
	}
}
