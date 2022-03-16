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

package org.eclipse.birt.report.model.core;

/**
 * Class to store the names for style elements.
 */
public class CaseInsensitiveNameSpace extends NameSpace {

	/**
	 * Constructor.
	 */

	public CaseInsensitiveNameSpace() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.NameSpace#insert(org.eclipse.birt.
	 * report.model.core.DesignElement)
	 */

	@Override
	public void insert(DesignElement element) {
		String name = element.getName();

		name = name == null ? null : name.toLowerCase();

		assert names.get(name) == null;
		names.put(name, element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.NameSpace#remove(org.eclipse.birt.
	 * report.model.core.DesignElement)
	 */

	@Override
	public void remove(DesignElement element) {
		String name = element.getName();
		assert name != null;

		name = name.toLowerCase();

		assert names.get(name) == element;
		names.remove(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.NameSpace#rename(org.eclipse.birt.
	 * report.model.core.DesignElement, java.lang.String, java.lang.String)
	 */

	@Override
	public void rename(DesignElement element, String oldName, String newName) {
		if (oldName != null) {
			oldName = oldName.toLowerCase();
			assert names.get(oldName) == element;
			names.remove(oldName);
		}
		if (newName != null) {
			newName = newName.toLowerCase();
			assert names.get(newName) == null;
			names.put(newName, element);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.NameSpace#contains(java.lang.String)
	 */

	@Override
	public boolean contains(String name) {
		String styleName = name == null ? null : name.toLowerCase();
		return names.containsKey(styleName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.NameSpace#getElement(java.lang.String)
	 */

	@Override
	public DesignElement getElement(String name) {
		String styleName = name == null ? null : name.toLowerCase();
		return names.get(styleName);
	}
}
