/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.elements.strategy;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

public class CellExportPropSearchStrategy extends CellPropSearchStrategy {

	private static final CellExportPropSearchStrategy instance = new CellExportPropSearchStrategy();

	protected CellExportPropSearchStrategy() {
	}

	public static CellExportPropSearchStrategy getInstance() {
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object getPropertyRelatedToContainer(Module module, DesignElement cell, ElementPropertyDefn prop) {
		// When exporting cells, should not export the properties related to
		// container
		return null;
	}
}
