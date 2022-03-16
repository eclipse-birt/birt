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
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the data set of some special elements should be provided.
 *
 * <h3>Rule</h3> The rule is that the <code>ListingElement.DATA_SET_PROP</code>
 * should be set on the element itself or its container which is also a listing
 * element; the <code>ICubeModel.DATA_SET_PROP</code> should be set in the cube
 * element; the <code>IReportItemModel.DATA_SET_PROP</code> should be set in the
 * extended items.
 *
 * <h3>Applicability</h3> This validator is applied to
 * <code>ListingElement</code>, <code>Cube</code> and <code>ExtendedItem</code>.
 */

public class DataSetRequiredValidator extends AbstractElementValidator {

	private final static DataSetRequiredValidator instance = new DataSetRequiredValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static DataSetRequiredValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether the data set of the given listing element is provided.
	 *
	 * @param module  the module
	 * @param element the listing element to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	@Override
	public List<SemanticException> validate(Module module, DesignElement element) {
		if (!(element instanceof ListingElement || element instanceof Cube)) {
			return Collections.emptyList();
		}

		return doValidate(module, element);
	}

	private List<SemanticException> doValidate(Module module, DesignElement toValidate) {
		List<SemanticException> list = new ArrayList<>();

		DesignElement container = toValidate;
		ContainerContext containerInfo = null;

		boolean isDataBindingRef = false;
		boolean dataSetFound = false;
		if (toValidate instanceof Cube) {
			String propName = ITabularCubeModel.DATA_SET_PROP;
			if (toValidate.getReferenceProperty(module, propName) != null) {
				dataSetFound = true;
			} else {
				while (container.getContainer() != null) {
					containerInfo = container.getContainerInfo();
					container = container.getContainer();
				}
			}
		} else if (toValidate instanceof ListingElement) {
			while (container.getContainer() != null && !dataSetFound) {
				if (container instanceof ListingElement || container instanceof GridItem) {
					String propName = IReportItemModel.DATA_SET_PROP;
					if (container.getReferenceProperty(module, propName) != null) {
						dataSetFound = true;
						break;
					}
				}

				containerInfo = container.getContainerInfo();
				container = container.getContainer();
			}

			if (!dataSetFound) {
				dataSetFound = ((ListingElement) toValidate).isDataBindingReferring(module);
			}
		} else {
			// now the check is only employed to listing elements, extended
			// items.

			assert false;
		}

		// Since element in components slot is considered as incompletely
		// defined, the data set is not required on table in components.

		int slot = containerInfo == null ? IDesignElementModel.NO_SLOT : containerInfo.getSlotID();
		if (!dataSetFound && IModuleModel.COMPONENT_SLOT != slot
				&& IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT != slot && !isDataBindingRef) {
			list.add(new SemanticError(toValidate, SemanticError.DESIGN_EXCEPTION_MISSING_DATA_SET));
		}
		return list;
	}

}
