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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.ContentExceptionFactory;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the table/list is not allowed to appear in header/footer/contents
 * slot of master page in any level.
 *
 * <h3>Rule</h3> The rule is that whether the table/list can recursively resides
 * in the header/footer/contents slot of master page.
 *
 * <h3>Applicability</h3> This validator is only applied to
 * <code>MasterPage</code> currently.
 *
 */

public class MasterPageContextContainmentValidator extends AbstractElementValidator {

	private final static MasterPageContextContainmentValidator instance = new MasterPageContextContainmentValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static MasterPageContextContainmentValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether the given element can recursively resides in the specific
	 * slot of specific container type.
	 *
	 * @param module  the module
	 * @param element the element to validate
	 *
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	@Override
	public List<SemanticException> validate(Module module, DesignElement element) {
		if (!(element instanceof MasterPage)) {
			return Collections.emptyList();
		}

		return doValidate(module, element, false);
	}

	/**
	 * Checks whether the <code>toValidate</code> is or is in the table element and
	 * its slotId is <code>TableItem.HEADER_SLOT</code>.
	 *
	 * @param module       the module
	 * @param toValidate   the element to validate
	 * @param isAddListing true if adding a table/list or the adding element
	 *                     containing a table/list in any level to the validate
	 *                     element, otherwise false
	 * @return an error list if <code>toValidate</code> is master page and one of
	 *         its child is a listing element.
	 */

	private List<SemanticException> doValidate(Module module, DesignElement toValidate, boolean isAddListing) {
		DesignElement container = toValidate;
		MasterPage page = null;
		while (container != null) {
			if (container instanceof MasterPage) {
				page = (MasterPage) container;
				break;
			}
			container = container.getContainer();
		}

		if (page == null) {
			return Collections.emptyList();
		}

		List<SemanticException> list = new ArrayList<>();

		if (ModelUtil.containElement(module, page, ReportDesignConstants.LISTING_ITEM) || isAddListing) {
			list.add(new SemanticError(toValidate,
					SemanticError.DESIGN_EXCEPTION_INVALID_MASTER_PAGE_CONTEXT_CONTAINMENT));
		}

		return list;
	}

	/**
	 * Validates whether the given element can recursively resides in the specific
	 * slot of specific container type when trying to add an element.
	 *
	 * @param module        the module
	 * @param containerInfo the container information
	 * @param toAdd         the element to add
	 *
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validateForAdding(Module module, ContainerContext containerInfo,
			DesignElement toAdd) {
		boolean isAddListing = false;
		if (toAdd instanceof ListingElement || toAdd instanceof ExtendedItem
				|| ModelUtil.containElement(module, toAdd, ReportDesignConstants.LISTING_ITEM)) {
			isAddListing = true;
		}

		List<SemanticException> errors = doValidate(module, containerInfo.getElement(), isAddListing);
		if (!errors.isEmpty()) {
			errors.clear();
			errors.add(ContentExceptionFactory.createContentException(containerInfo, toAdd,
					ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT));
		}
		return errors;
	}

	/**
	 * Validates whether the given element can recursively resides in the specific
	 * slot of specific container type when trying to add an element.
	 *
	 * @param module  the module
	 * @param element the container element
	 * @param slotId  the slot where the new element to insert
	 * @param toAdd   the element to add
	 *
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 * @deprecated since birt2.2, replaced by
	 *             {@link #validateForAdding(Module, ContainerContext, DesignElement)}
	 */

	@Deprecated
	public List<SemanticException> validateForAdding(Module module, DesignElement element, int slotId,
			DesignElement toAdd) {
		boolean isAddListing = false;
		if (toAdd instanceof ListingElement || toAdd instanceof ExtendedItem
				|| ModelUtil.containElement(module, toAdd, ReportDesignConstants.LISTING_ITEM)) {
			isAddListing = true;
		}

		List<SemanticException> errors = doValidate(module, element, isAddListing);
		if (!errors.isEmpty()) {
			errors.clear();
			errors.add(new ContentException(element, slotId, toAdd,
					ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT));
		}
		return errors;
	}

	/**
	 * Validates whether the given element can recursively resides in the specific
	 * slot of specific container type when trying to add an element.
	 *
	 * @param module  the root module of the element to validate
	 * @param element the container element
	 * @param toAdd   the element definition to add
	 *
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validateForAdding(Module module, DesignElement element, IElementDefn toAdd) {
		IElementDefn ListingDefn = MetaDataDictionary.getInstance().getElement(ReportDesignConstants.LISTING_ITEM);

		boolean isAddListing = toAdd.isKindOf(ListingDefn);
		if (isAddListing) {
			return doValidate(module, element, isAddListing);
		}

		// see bugzilla 188196. chart and crosstab can't be inserted into master
		// page
		// now every extended item is forbidden
		IElementDefn extendedItemDefn = MetaDataDictionary.getInstance().getExtension(toAdd.getName());
		isAddListing = toAdd.isKindOf(extendedItemDefn);

		return doValidate(module, element, isAddListing);
	}
}
