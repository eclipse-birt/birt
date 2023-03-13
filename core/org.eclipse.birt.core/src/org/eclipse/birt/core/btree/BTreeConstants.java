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
	long MAGIC_TAG = 0x4254524545L;
	int BTREE_VERSION_0 = 0;

	int BLOCK_SIZE = 4096;

	int MAX_NODE_SIZE = 4088;

	int MIN_ENTRY_COUNT = 13;

	int NODE_INDEX = 1;
	int NODE_LEAF = 2;
	int NODE_VALUE = 3;
	int NODE_EXTRA = 4;

	int HEAD_BLOCK_ID = 0;
	int ROOT_BLOCK_ID = 0;
}
