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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.TableHeaderContextContainmentValidator;
import org.eclipse.birt.report.model.core.Module;

/**
 * This class represents a List element. List is a free-form layout driven by
 * data from a data set. See the base class, <code>ListingElement</code> for
 * details.
 *
 * @see ListingElement
 */

public class ListItem extends ListingElement {

	/**
	 * Default constructor.
	 */

	public ListItem() {
	}

	/**
	 * Constructs the list item with an optional name.
	 *
	 * @param theName the optional name
	 */

	public ListItem(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitList(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.LIST_ITEM;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public ListHandle handle(Module module) {
		if (handle == null) {
			handle = new ListHandle(module, this);
		}
		return (ListHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	@Override
	public List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate(module);

		// Check header slot context containment of table.

		list.addAll(TableHeaderContextContainmentValidator.getInstance().validate(module, this));

		return list;
	}
}
