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

package org.eclipse.birt.report.item.crosstab.core.re;

import javax.olap.OLAPException;

/**
 * 
 */

public class EmptyDimensionCursor extends DummyDimensionCursor {

	public EmptyDimensionCursor() {
		super(1);
	}

	public boolean isFirst() throws OLAPException {
		return false;
	}

	public boolean isLast() throws OLAPException {
		return false;
	}

	public boolean next() throws OLAPException {
		return false;
	}

	public long getPosition() throws OLAPException {
		return -1;
	}

	public long getEdgeEnd() throws OLAPException {
		return -1;
	}

	public long getEdgeStart() throws OLAPException {
		return -1;
	}
}
