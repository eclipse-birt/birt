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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.validators.ValueRequiredValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.strategy.GroupPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * This class represents a grouping level within either a List or Table item.
 * Groups are defined by a grouping expression on the data set.
 * 
 */

public abstract class GroupElement extends DesignElement implements IGroupElementModel {

	/**
	 * The constants value representing no group level.
	 */

	private static final int LEVEL_NOT_SET = -1;

	/**
	 * The group level.
	 */

	protected int groupLevel = LEVEL_NOT_SET;

	/**
	 * Default constructor. Note that groups do not have names.
	 */

	public GroupElement() {
		initSlots();
		cachedPropStrategy = GroupPropSearchStrategy.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot(int slot) {
		assert slot >= 0 && slot < SLOT_COUNT;
		return slots[slot];
	}

	/**
	 * Returns the level of this group within the list. The grouping level is cached
	 * for performance.
	 * 
	 * @return the 1-based grouping level of this group
	 */

	public int getGroupLevel() {
		DesignElement container = getContainer();
		if (container == null)
			groupLevel = LEVEL_NOT_SET;
		else {
			groupLevel = getContainerInfo().indexOf(this) + 1;
		}
		return groupLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	public List<SemanticException> validate(Module module) {
		List<SemanticException> list = super.validate(module);

		list.addAll(validateStructureList(module, SORT_PROP));
		list.addAll(validateStructureList(module, FILTER_PROP));

		list.addAll(ValueRequiredValidator.getInstance().validate(module, this, KEY_EXPR_PROP));

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getNameForDisplayLabel()
	 */

	protected String getNameForDisplayLabel() {
		// This getting is not relative to design.

		ElementPropertyDefn propDefn = (ElementPropertyDefn) getDefn().getProperty(GROUP_NAME_PROP);
		return (String) getStrategy().getPropertyFromElement(null, this, propDefn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getFactoryProperty(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getFactoryProperty(Module module, ElementPropertyDefn prop) {
		if (!prop.isStyleProperty())
			return super.getFactoryProperty(module, prop);

		return getStrategy().getPropertyFromElement(module, this, prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#canDrop(org.eclipse.
	 * birt.report.model.core.Module)
	 */

	public boolean canDrop(Module module) {
		boolean retValue = super.canDrop(module);
		if (!retValue)
			return retValue;

		return !isReferredDataGroup(module);
	}

	/**
	 * Checks whether the group refers to groups in the other listing element.
	 * 
	 * @param module the root of the group element
	 * @return <code>true</code> if the group shares data with other groups.
	 *         Otherwise <code>false</code>.
	 */

	private boolean isReferredDataGroup(Module module) {
		ListingElement tmpContainer = (ListingElement) getContainer();
		if (tmpContainer == null)
			return false;

		return tmpContainer.isDataBindingReferring(module);
	}

	public void setName(String name) {
		this.setProperty(GROUP_NAME_PROP, name);
	}
}
