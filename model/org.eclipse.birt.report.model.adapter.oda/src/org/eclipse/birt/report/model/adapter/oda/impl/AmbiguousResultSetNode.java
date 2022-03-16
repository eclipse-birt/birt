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

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousResultSetNode;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;

/**
 * Implements the interface {@link IAmbiguousResultSetNode}. It defines the oda
 * result set column handle and a list of <code>IAmbiguousAttribute</code>.
 *
 */
class AmbiguousResultSetNode implements IAmbiguousResultSetNode {

	private final OdaResultSetColumnHandle columnHandle;
	private final List<IAmbiguousAttribute> attributes;

	AmbiguousResultSetNode(OdaResultSetColumnHandle columnHandle, List<IAmbiguousAttribute> attributes) {
		if (columnHandle == null) {
			throw new IllegalArgumentException(
					"The oda data set parameter can not be null when creating AmbiguousParameterNode!"); //$NON-NLS-1$
		}
		this.columnHandle = columnHandle;
		if (attributes == null) {
			this.attributes = Collections.emptyList();
		} else {
			this.attributes = attributes;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousResultSetNode#
	 * getOdaResultSetColumnHandle()
	 */
	@Override
	public OdaResultSetColumnHandle getOdaResultSetColumnHandle() {
		return this.columnHandle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.adapter.oda.IAmbiguousResultSetNode#
	 * getAmbiguousAttributes()
	 */
	@Override
	public List<IAmbiguousAttribute> getAmbiguousAttributes() {
		return this.attributes;
	}
}
