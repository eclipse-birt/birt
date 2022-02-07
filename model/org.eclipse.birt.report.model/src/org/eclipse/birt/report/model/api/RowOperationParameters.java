/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api;

/**
 * Prepares parameters for row operation , including copy , paste , insert and
 * shift operation.
 * 
 */

public class RowOperationParameters {

	/**
	 * slot id. When group id is smaller than zero , slot id stands for group
	 * header, or SLOT_GROUP_FOOTER; else slot id stands for SLOT_TABLE_HEADER ,
	 * SLOT_TABLE_DETAIL , SLOT_TABLE_FOOTER
	 * 
	 */

	private int slotId;

	/**
	 * group id. If table row in the group , group id is bigger than zero, else
	 * group id is -1.
	 */
	private int groupId;

	/**
	 * index of source table row. The range of sourceIndex is from zero to count
	 * plus 1.
	 */

	private int sourceIndex;

	/**
	 * index of target table row. The range of destIndex is from zero to count plus
	 * 1.
	 */

	private int destIndex;

	/**
	 * Default Constructor
	 *
	 */
	public RowOperationParameters() {

	}

	/**
	 * Constructor
	 * 
	 * @param slotId    slot id
	 * @param groupId   group id
	 * @param destIndex destination index
	 */

	public RowOperationParameters(int slotId, int groupId, int destIndex) {
		this.slotId = slotId;
		this.groupId = groupId;
		this.destIndex = destIndex;
	}

	/**
	 * Returns target index.
	 * 
	 * @return index.
	 */

	public int getDestIndex() {
		return destIndex;
	}

	/**
	 * Sets target index
	 * 
	 * @param destIndex target index
	 */

	public void setDestIndex(int destIndex) {
		this.destIndex = destIndex;
	}

	/**
	 * Returns group id
	 * 
	 * @return group id.
	 */

	public int getGroupId() {
		return groupId;
	}

	/**
	 * Sets group id
	 * 
	 * @param groupId groupd id
	 */

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	/**
	 * Returns slot id.
	 * 
	 * @return slot id.
	 */

	public int getSlotId() {
		return slotId;
	}

	/**
	 * Sets slot id.
	 * 
	 * @param slotId slot id.
	 */

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	/**
	 * Returns source index.
	 * 
	 * @return source index.
	 */

	public int getSourceIndex() {
		return sourceIndex;
	}

	/**
	 * Sets source index.
	 * 
	 * @param sourceIndex source index
	 */

	public void setSourceIndex(int sourceIndex) {
		this.sourceIndex = sourceIndex;
	}
}
