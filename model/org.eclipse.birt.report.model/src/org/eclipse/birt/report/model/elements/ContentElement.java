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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.command.ContentElementInfo;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 *
 *
 */

public abstract class ContentElement extends DesignElement {

	/**
	 * if cube 1 extends cube 2, this value refers to cube 1. While, the "this"
	 * element is still in cube 2.
	 */

	private ContentElementInfo valueContainer;

	/**
	 *
	 */

	public ContentElement() {
		super();
	}

	/**
	 * @param theName
	 */

	public ContentElement(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getBaseId()
	 */

	@Override
	public final long getBaseId() {
		return NO_BASE_ID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#setBaseId(long)
	 */

	@Override
	public final void setBaseId(long baseId) {
		// do not set base id for such elements.
	}

	/**
	 * @return the valueContainer
	 */

	public ContentElementInfo getValueContainer() {
		return valueContainer;
	}

	/**
	 * @param valueContainer the valueContainer to set
	 */

	public void setValueContainer(ContentElementInfo valueContainer) {
		this.valueContainer = valueContainer;
	}

	/**
	 * Checks the element container is equal to the <code>valueContainer</code>. If
	 * they are equal, the command and records are perform on the element directly.
	 * Otherwise, it is an "extends" case.
	 *
	 * @return <code>true</code> if the element container is equal to the
	 *         <code>valueContainer</code>.
	 */

	public boolean isLocal() {
		// if the valueContainer is null, this element is created from element
		// factory.

		if (valueContainer == null) {
			return true;
		}

		ContentElementInfo tmpTarget = ModelUtil.getContentContainer(this,
				getContainer().getPropertyDefn(getContainerInfo().getPropertyName()));

		// the path information is used later for find corredponding content
		// element.

		valueContainer.copyPath(tmpTarget);

		// if the value container is null. The content element is created. So,
		// it is the local value.

		if ((tmpTarget == null) || (tmpTarget.getElement() != valueContainer.getElement())) {
			return false;
		}

		return true;
	}
}
