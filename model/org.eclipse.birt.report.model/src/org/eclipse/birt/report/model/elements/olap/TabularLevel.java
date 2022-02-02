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

package org.eclipse.birt.report.model.elements.olap;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;

/**
 * This class represents a Level element. Level is the real element which
 * defines the column expression from the dataset.Use the
 * {@link org.eclipse.birt.report.model.api.olap.LevelHandle}class to change the
 * properties.
 * 
 */

public class TabularLevel extends Level {

	/**
	 * Default constructor.
	 */

	public TabularLevel() {

	}

	/**
	 * Constructs the level with an optional name.
	 * 
	 * @param name the optional name for the level element
	 */

	public TabularLevel(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */
	public void apply(ElementVisitor visitor) {
		visitor.visitTabularLevel(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName() {
		return ReportDesignConstants.TABULAR_LEVEL_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.
	 * birt.report.model.core.Module)
	 */
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the module of the level
	 * 
	 * @return an API handle for this element.
	 */

	public TabularLevelHandle handle(Module module) {
		if (handle == null) {
			handle = new TabularLevelHandle(module, this);
		}
		return (TabularLevelHandle) handle;
	}
}
