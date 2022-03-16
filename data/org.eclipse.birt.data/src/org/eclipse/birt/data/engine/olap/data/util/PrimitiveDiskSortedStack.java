
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;
import java.util.Comparator;

/**
 *
 */

public class PrimitiveDiskSortedStack extends BaseDiskSortedStack {
	public PrimitiveDiskSortedStack(int bufferSize, boolean isAscending, boolean forceDistinct) {
		super(bufferSize, isAscending, forceDistinct, null);
	}

	public PrimitiveDiskSortedStack(int bufferSize, boolean forceDistinct, Comparator comparator) {
		super(bufferSize, forceDistinct, comparator, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.util.BaseDiskSortedStack#saveToDisk(int,
	 * int)
	 */
	@Override
	protected void saveToDisk(int fromIndex, int toIndex) throws IOException {
		PrimitiveDiskArray diskList = new PrimitiveDiskArray();
		for (int i = fromIndex; i <= toIndex; i++) {
			diskList.add(buffer[i]);
		}
		segments.add(diskList);
	}

}
