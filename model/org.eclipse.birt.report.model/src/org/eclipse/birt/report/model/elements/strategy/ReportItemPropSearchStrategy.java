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

package org.eclipse.birt.report.model.elements.strategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Provides the specific property searching route for <code>ReportItem</code>,
 * especially about how to apply CSS rules on <code>ReportItem</code>.
 */

public class ReportItemPropSearchStrategy extends PropertySearchStrategy {

	private final static ReportItemPropSearchStrategy instance = new ReportItemPropSearchStrategy();

	/**
	 * Data binding properties for the listing elements.
	 */

	protected static Set<String> listingElementDataBindingProps;

	/**
	 * Data binding properties hash code for the listing elements.
	 */

	protected static Set<Integer> listingElementDataBindingPropsNameHash;

	/**
	 * Data binding properties for the extended elements.
	 */

	protected static Set<String> extendedItemDataBindingProps;

	/**
	 * Data binding properties hash code for the extended elements.
	 */

	protected static Set<Integer> extendedItemDataBindingPropsNameHash;

	/**
	 * Data binding properties for the report items.
	 */

	protected static Set<String> reportItemDataBindingProps;

	/**
	 * Data binding properties hash code for the report items.
	 */

	protected static Set<Integer> reportItemDataBindingPropsNameHash;

	static {
		Set<String> tmpSet = new HashSet<>();
		tmpSet.add(IReportItemModel.PARAM_BINDINGS_PROP);
		tmpSet.add(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		tmpSet.add(IReportItemModel.DATA_SET_PROP);
		tmpSet.add(IReportItemModel.CUBE_PROP);
		tmpSet.add(IListingElementModel.FILTER_PROP);
		tmpSet.add(IListingElementModel.SORT_PROP);
		listingElementDataBindingProps = tmpSet;

		Set<Integer> tmpIntegerSet = new HashSet<>();
		tmpIntegerSet.add(IReportItemModel.PARAM_BINDINGS_PROP.hashCode());
		tmpIntegerSet.add(IReportItemModel.BOUND_DATA_COLUMNS_PROP.hashCode());
		tmpIntegerSet.add(IReportItemModel.DATA_SET_PROP.hashCode());
		tmpIntegerSet.add(IReportItemModel.CUBE_PROP.hashCode());
		tmpIntegerSet.add(IListingElementModel.FILTER_PROP.hashCode());
		tmpIntegerSet.add(IListingElementModel.SORT_PROP.hashCode());
		listingElementDataBindingPropsNameHash = tmpIntegerSet;

		tmpSet = new HashSet<>();
		tmpSet.add(IReportItemModel.PARAM_BINDINGS_PROP);
		tmpSet.add(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		tmpSet.add(IReportItemModel.DATA_SET_PROP);
		tmpSet.add(IReportItemModel.CUBE_PROP);
		tmpSet.add(IExtendedItemModel.FILTER_PROP);
		extendedItemDataBindingProps = tmpSet;

		tmpIntegerSet = new HashSet<>();
		tmpIntegerSet.add(IReportItemModel.PARAM_BINDINGS_PROP.hashCode());
		tmpIntegerSet.add(IReportItemModel.BOUND_DATA_COLUMNS_PROP.hashCode());
		tmpIntegerSet.add(IReportItemModel.DATA_SET_PROP.hashCode());
		tmpIntegerSet.add(IReportItemModel.CUBE_PROP.hashCode());
		tmpIntegerSet.add(IExtendedItemModel.FILTER_PROP.hashCode());
		extendedItemDataBindingPropsNameHash = tmpIntegerSet;

		tmpSet = new HashSet<>();
		tmpSet.add(IReportItemModel.PARAM_BINDINGS_PROP);
		tmpSet.add(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		tmpSet.add(IReportItemModel.DATA_SET_PROP);
		tmpSet.add(IReportItemModel.CUBE_PROP);
		reportItemDataBindingProps = tmpSet;

		tmpIntegerSet = new HashSet<>();
		tmpIntegerSet.add(IReportItemModel.PARAM_BINDINGS_PROP.hashCode());
		tmpIntegerSet.add(IReportItemModel.BOUND_DATA_COLUMNS_PROP.hashCode());
		tmpIntegerSet.add(IReportItemModel.DATA_SET_PROP.hashCode());
		tmpIntegerSet.add(IReportItemModel.CUBE_PROP.hashCode());
		reportItemDataBindingPropsNameHash = tmpIntegerSet;
	}

	/**
	 * Protected constructor.
	 */

	protected ReportItemPropSearchStrategy() {
	}

	/**
	 * Returns the instance of <code>ReportItemPropSearchStrategy</code> which
	 * provide the specific property searching route for <code>ReportItem</code> .
	 *
	 * @return the instance of <code>ReportItemPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.PropertySearchStrategy#getPropertyFromSelf
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	@Override
	protected Object getPropertyFromSelf(Module module, DesignElement element, ElementPropertyDefn prop) {
		String propName = prop.getName();
		if (!isDataBindingProperty(element, propName)) {
			return super.getPropertyFromSelf(module, element, prop);
		}

		// the data binding reference property has high priority than local
		// properties.

		ElementRefValue refValue = (ElementRefValue) element.getLocalProperty(module,
				IReportItemModel.DATA_BINDING_REF_PROP);
		if (refValue == null || !refValue.isResolved()) {
			return super.getPropertyFromSelf(module, element, prop);
		}

		DesignElement refElement = refValue.getElement();
		ElementPropertyDefn refPropDefn = refElement.getPropertyDefn(propName);

		// if definition in the referred element is not found, return null: table refers
		// data, data not define filter/sort
		if ((refPropDefn == null) || prop.getTypeCode() != refPropDefn.getTypeCode() || prop.getStructDefn() != refPropDefn.getStructDefn()
				|| prop.getTargetElementType() != refPropDefn.getTargetElementType()) {
			return null;
		}

		return refElement.getProperty(module, prop);
	}

	/**
	 * Returns properties that are bound to data related values.
	 *
	 * @param tmpElement the design element
	 * @return a set containing property names in string
	 */

	public static Set<String> getDataBindingProperties(DesignElement tmpElement) {

		if (tmpElement instanceof ListingElement) {
			return listingElementDataBindingProps;
		} else if (tmpElement instanceof ExtendedItem) {
			return extendedItemDataBindingProps;
		} else if (tmpElement instanceof ReportItem) {
			return reportItemDataBindingProps;
		} else {
			return Collections.emptySet();
		}

	}

	/**
	 * Returns properties hash code that are bound to data related values.
	 *
	 * @param tmpElement the design element
	 * @return a set containing property names in string
	 */

	private static Set<Integer> getDataBindingPropertiesNameHash(DesignElement tmpElement) {

		if (tmpElement instanceof ListingElement) {
			return listingElementDataBindingPropsNameHash;
		} else if (tmpElement instanceof ExtendedItem) {
			return extendedItemDataBindingPropsNameHash;
		} else if (tmpElement instanceof ReportItem) {
			return reportItemDataBindingPropsNameHash;
		} else {
			return Collections.emptySet();
		}

	}

	/**
	 * Checks if the property is data binding property.
	 *
	 * @param element  the design element
	 * @param propName the property name
	 * @return true if this property is the data binding property, false otherwise
	 */
	public static boolean isDataBindingProperty(DesignElement element, String propName) {
		if (!(element instanceof ReportItem) || StringUtil.isBlank(propName)) {
			return false;
		}
		return getDataBindingPropertiesNameHash(element).contains(Integer.valueOf(propName.hashCode()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.PropertySearchStrategy#
	 * getPropertyExceptRomDefault(org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */
	@Override
	public Object getPropertyExceptRomDefault(Module module, DesignElement element, ElementPropertyDefn prop) {
		if (element instanceof ListingElement && IListingElementModel.REPEAT_HEADER_PROP.equals(prop.getName())) {
			Object value = super.getPropertyExceptRomDefault(module, element, prop);

			// if report-header is FALSE, return it
			if (Boolean.FALSE.equals(value)) {
				return value;
			}

			// if cascadeACL is false, return
			Object cascadeACL = element.getProperty(module, IReportItemModel.CASCADE_ACL_PROP);
			if (Boolean.FALSE.equals(cascadeACL)) {
				return value;
			}

			// cascadeACL is true, then check all the groups: if any group
			// element defines ACLExpression, then return FALSE
			ListingElement listingElement = (ListingElement) element;
			List<DesignElement> groups = listingElement.getGroups();
			for (DesignElement group : groups) {
				Object aclExpr = group.getProperty(module, IReportItemModel.ACL_EXPRESSION_PROP);
				if (aclExpr != null) {
					return Boolean.FALSE;
				}
			}

			return value;

		}

		return super.getPropertyExceptRomDefault(module, element, prop);
	}
}
