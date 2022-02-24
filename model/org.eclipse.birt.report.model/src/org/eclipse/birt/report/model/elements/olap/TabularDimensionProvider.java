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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Provides ODA extensibility.
 */

class TabularDimensionProvider extends DynamicLinkProvider {

	/**
	 * Constructs ODA extensibility provider with the element to extend and
	 * extension ID.
	 *
	 * @param element     the element to extend
	 * @param extensionID the ID of the extension which provides property
	 *                    definition.
	 */

	public TabularDimensionProvider(DesignElement element) {
		super(element);

		if (!(element instanceof TabularDimension)) {
			throw new IllegalArgumentException("element must be tabular dimension!"); //$NON-NLS-1$
		}
		cachedExtDefn = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.olap.DynamicLinkProvider#
	 * getTargetElement(org.eclipse.birt.report.model.core.Module)
	 */
	@Override
	protected DesignElement getTargetElement(Module module) {
		return ((TabularDimension) element).getSharedDimension(module);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.elements.olap.DynamicLinkProvider#isValidTarget
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */
	@Override
	protected boolean isValidTarget(DesignElement target) {
		if (target instanceof Dimension && target.getContainer() instanceof Module) {
			return true;
		}
		return false;
	}
}
