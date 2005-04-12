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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the element is not allowed to appear in the specific slot of the
 * given container type in any level.
 * 
 * <h3>Rule</h3>
 * The rule is that whether the given element can recursively resides in the
 * specific slot of specific container type.
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to <code>TableItem</code> and
 * <code>ListItem</code> currently.
 *  
 */

public class TableHeaderContextContainmentValidator extends
		AbstractElementValidator {

	private final static TableHeaderContextContainmentValidator instance = new TableHeaderContextContainmentValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static TableHeaderContextContainmentValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether the given element can recursively resides in the
	 * specific slot of specific container type.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the element to validate
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate(ReportDesign design, DesignElement element) {
		if (!(element instanceof ListingElement))
			return Collections.EMPTY_LIST;

		return doValidate(design, element);
	}

	private List doValidate(ReportDesign design, DesignElement toValidate) {
		List list = new ArrayList();

		DesignElement curContainer = toValidate.getContainer();
		int curSlotID = toValidate.getContainerSlot();

		while (curContainer != null) {
			IElementDefn containerDefn = curContainer.getDefn();

			if (ReportDesignConstants.TABLE_ITEM.equalsIgnoreCase(containerDefn
					.getName())
					&& curSlotID == TableItem.HEADER_SLOT) {
				list
						.add(new ContentException(
								curContainer,
								curSlotID,
								toValidate,
								ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT));
			}

			curSlotID = curContainer.getContainerSlot();
			curContainer = curContainer.getContainer();
		}
		return list;
	}

	/**
	 * Validates whether the given element can recursively resides in the
	 * specific slot of specific container type when trying to add an element.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the container element
	 * @param toAdd
	 *            the element to add
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validateForAdding(ReportDesign design, DesignElement element,
			DesignElement toAdd) {
		if (!containsListingElement(toAdd))
			return Collections.EMPTY_LIST;

		return doValidate(design, element);
	}

	/**
	 * Validates whether the given element can recursively resides in the
	 * specific slot of specific container type when trying to add an element.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the container element
	 * @param toAdd
	 *            the element definition to add
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validateForAdding(ReportDesign design, DesignElement element,
			IElementDefn toAdd) {
		ElementDefn listingDefn = (ElementDefn) MetaDataDictionary
				.getInstance().getElement(ReportDesignConstants.LISTING_ITEM);
		if (!listingDefn.isKindOf(toAdd))
			return Collections.EMPTY_LIST;

		return doValidate(design, element);
	}

	/**
	 * Checks whether the <code>element</code> recursively contains a
	 * <code>ListingItem</code>.
	 * 
	 * @param element
	 *            the element to check
	 * 
	 * @return <code>true</code> if the <code>element</code> recursively
	 *         contains a <code>ListingItem</code>. Otherwise
	 *         <code>false</code>.
	 */

	private static boolean containsListingElement(DesignElement element) {
		if (element instanceof ListingElement)
			return true;

		// Check contents.

		int count = element.getDefn().getSlotCount();
		for (int i = 0; i < count; i++) {
			Iterator iter = element.getSlot(i).iterator();
			while (iter.hasNext()) {
				DesignElement e = (DesignElement) iter.next();

				if (e instanceof ListingElement)
					return true;

				if (containsListingElement(e))
					return true;
			}
		}

		return false;
	}
}