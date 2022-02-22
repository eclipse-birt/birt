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

package org.eclipse.birt.report.model.api.css;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.IllegalOperationException;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;

/**
 * Iterates over the styles of an include style sheet.
 */

public class StyleIterator implements Iterator {

	/**
	 * The cached iterator.
	 */

	protected Iterator iter;

	/**
	 * Module.
	 */

	protected Module module;

	/**
	 * Constructs a iterator to return the styles of the given style sheet.
	 *
	 * @param styleSheet handle to the style sheet for which styles are included.
	 *                   Must not be <code>null</code>.
	 */

	public StyleIterator(CssStyleSheetHandle styleSheet) {
		assert styleSheet != null;

		this.module = styleSheet.getModule();
		assert module != null;

		iter = styleSheet.getStyleSheet().getStyles().iterator();
	}

	/**
	 * Inherited method that is disabled in this iterator; the caller cannot remove
	 * styles using this class.
	 *
	 * @see java.util.Iterator#remove()
	 */

	@Override
	public void remove() {
		throw new IllegalOperationException();
	}

	/**
	 * Returns true if there is another style to retrieve.
	 *
	 * @return true if there is another style to retrieve, false otherwise
	 * @see java.util.Iterator#hasNext()
	 */

	@Override
	public boolean hasNext() {
		if (iter != null) {
			return iter.hasNext();
		}
		return false;
	}

	/**
	 * Returns a handle of the style.
	 *
	 * @return the handle of the style
	 *
	 * @see java.util.Iterator#next()
	 * @see SharedStyleHandle
	 */

	@Override
	public Object next() {
		if (iter != null) {
			StyleElement style = (StyleElement) iter.next();
			return style.getHandle(module);
		}
		return null;
	}

}
