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

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.elements.strategy.ExtendedItemPropSearchStrategy;
import org.eclipse.birt.report.model.elements.strategy.GroupPropSearchStrategy;
import org.eclipse.birt.report.model.elements.strategy.ReportItemPropSearchStrategy;

/**
 * 
 *
 */

class PropertyHandleHelperImpl {

	public boolean isReadOnlyInContext(PropertyHandle propHandle) {
		DesignElementHandle element = propHandle.getElementHandle();
		Module module = propHandle.getModule();
		String propName = propHandle.getPropertyDefn().getName();
		if (element instanceof MasterPageHandle) {
			MasterPage masterPage = (MasterPage) element.getElement();
			if (!masterPage.isCustomType(module)) {

				if (IMasterPageModel.HEIGHT_PROP.equals(propName) || IMasterPageModel.WIDTH_PROP.equals(propName))
					return true;
			}
		} else if (element instanceof GroupHandle) {
			DesignElementHandle tmpContainer = element.getContainer();
			if (tmpContainer == null)
				return false;

			return (GroupPropSearchStrategy.getDataBindingPropties().contains(propName)
					&& ((ListingElement) tmpContainer.getElement()).isDataBindingReferring(module));
		} else if (element instanceof ReportItemHandle) {
			boolean containsProp = ReportItemPropSearchStrategy.isDataBindingProperty(element.getElement(), propName);

			boolean retValue = containsProp && ((ReportItem) element.getElement()).isDataBindingReferring(module);

			if (retValue)
				return true;

			if (!containsProp)
				containsProp = ExtendedItemPropSearchStrategy.isHostViewProperty(element.getElement(), propName);

			if (element instanceof ExtendedItemHandle)
				return (containsProp && (element.getContainer() instanceof MultiViewsHandle));
		} else if (element instanceof RowHandle && ITableRowModel.REPEATABLE_PROP.equals(propName)) {
			return !rowRepeatableVisibleInContext(element);
		} else if (element instanceof TabularDimensionHandle) {
			// can not edit any property in the cube dimension that has defined
			// share dimension
			TabularDimension dimension = (TabularDimension) element.getElement();
			if (dimension.hasSharedDimension(element.getModule()))
				return true;

		}

		// all the children in cube dimension is read-only
		DesignElementHandle container = element.getContainer();
		while (container != null) {
			if (container instanceof TabularDimensionHandle) {
				TabularDimension dimension = (TabularDimension) container.getElement();
				if (dimension.hasSharedDimension(container.getModule()))
					return true;
			}

			container = container.getContainer();
		}

		return false;
	}

	/**
	 * Returns whether the repeatable of the row is visible in the report context.
	 * 
	 * @param handle the design element handle.
	 * @return <code>true</code> if the value is visible. Otherwise
	 *         <code>false</code>.
	 */
	private boolean rowRepeatableVisibleInContext(DesignElementHandle handle) {
		assert handle instanceof RowHandle;
		boolean isVisible = false;
		DesignElementHandle container = handle.getContainer();
		if (container instanceof TableHandle) {
			int containerSlotID = handle.getContainerSlotHandle().getSlotID();
			if (IListingElementModel.HEADER_SLOT == containerSlotID
					|| IListingElementModel.FOOTER_SLOT == containerSlotID) {
				isVisible = true;
			}

		} else if (container instanceof TableGroupHandle) {
			isVisible = true;
		}

		return isVisible;
	}

	/**
	 * Returns whether the property value is visible in the report context.
	 * 
	 * @return <code>true</code> if the value is visible. Otherwise
	 *         <code>false</code>.
	 */
	public boolean isVisibleInContext(PropertyHandle propHandle) {
		boolean isVisible = true;
		DesignElementHandle element = propHandle.getElementHandle();
		String propName = propHandle.getPropertyDefn().getName();
		if (element instanceof RowHandle && ITableRowModel.REPEATABLE_PROP.equals(propName)) {
			isVisible = rowRepeatableVisibleInContext(element);
		}
		return isVisible;
	}

}
