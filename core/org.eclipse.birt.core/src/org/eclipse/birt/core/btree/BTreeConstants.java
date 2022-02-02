/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.core.btree;

public interface BTreeConstants {

	// HEX value of 'BTREE'
	static final long MAGIC_TAG = 0x4254524545L;
	static final int BTREE_VERSION_0 = 0;

	static final int BLOCK_SIZE = 4096;

	static final int MAX_NODE_SIZE = 4088;

	static final int MIN_ENTRY_COUNT = 13;

	static final int NODE_INDEX = 1;
	static final int NODE_LEAF = 2;
	static final int NODE_VALUE = 3;
	static final int NODE_EXTRA = 4;

	static final int HEAD_BLOCK_ID = 0;
	static final int ROOT_BLOCK_ID = 0;
}
