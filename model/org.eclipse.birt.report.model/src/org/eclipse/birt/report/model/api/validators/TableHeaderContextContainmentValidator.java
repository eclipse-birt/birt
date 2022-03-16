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
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the element is not allowed to appear in the specific slot of the
 * given container type in any level.
 *
 * <h3>Rule</h3> The rule is that whether the given element can recursively
 * resides in the specific slot of specific container type.
 *
 * <h3>Applicability</h3> This validator is only applied to
 * <code>TableItem</code> and <code>ListItem</code> currently.
 *
 */

public class TableHeaderContextContainmentValidator extends AbstractElementValidator {

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
		if (!(element instanceof ListingElement)) {
			return Collections.emptyList();
		}

		return doValidate(module, new ContainerContext(element, IDesignElementModel.NO_SLOT));
	}

	/**
	 * Checks whether the <code>toValidate</code> is or is in the table element and
	 * its slotId is <code>TableItem.HEADER_SLOT</code>.
	 *
	 * @param module     the module
	 * @param toValidate the element to validate
	 * @param slotId     the slot id
	 * @return <code>true</code> if <code>toValidate</code> is a table item or
	 *         nested in the table item and the table slot is
	 *         <code>TableItem.HEADER_SLOT</code>.
	 */

	private List<SemanticException> doValidate(Module module, ContainerContext containerInfo) {
		assert containerInfo != null;
		List<SemanticException> list = new ArrayList<>();

		// DesignElement curContainer = toValidate;
		// int curSlotID = slotId;
		//
		// if ( slotId <= IDesignElementModel.NO_SLOT )
		// {
		// curContainer = toValidate.getContainer( );
		// curSlotID = toValidate.getContainerSlot( );
		// }

		ContainerContext infor = containerInfo;
		if (containerInfo.getSlotID() == IDesignElementModel.NO_SLOT) {
			infor = containerInfo.getElement().getContainerInfo();
		}
		while (infor != null) {
			IElementDefn containerDefn = infor.getElement().getDefn();

			if (ReportDesignConstants.TABLE_ITEM.equalsIgnoreCase(containerDefn.getName())
					&& infor.getSlotID() == IListingElementModel.HEADER_SLOT) {
				list.add(new ContentException(infor.getElement(), infor.getSlotID(), containerInfo.getElement(),
						ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT));
			}

			infor = infor.getElement().getContainerInfo();
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
		if (!(toAdd instanceof ListingElement)
				&& !(ModelUtil.containElement(module, toAdd, ReportDesignConstants.LISTING_ITEM))) {
			return Collections.emptyList();
		}

		return doValidate(module, containerInfo);
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
		if (!(toAdd instanceof ListingElement)
				&& !(ModelUtil.containElement(module, toAdd, ReportDesignConstants.LISTING_ITEM))) {
			return Collections.emptyList();
		}

		return doValidate(module, new ContainerContext(element, slotId));
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
		ElementDefn listingDefn = (ElementDefn) MetaDataDictionary.getInstance()
				.getElement(ReportDesignConstants.LISTING_ITEM);
		if (!toAdd.isKindOf(listingDefn)) {
			return Collections.emptyList();
		}

		return doValidate(module, new ContainerContext(element, IDesignElementModel.NO_SLOT));
	}
}
