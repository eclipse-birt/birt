/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
