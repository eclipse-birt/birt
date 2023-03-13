/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import org.eclipse.birt.chart.model.attribute.SortOption;

/**
 * Represents a grouping key
 */
public class GroupKey {

	private String key;
	private SortOption direction;
	private int index;

	public GroupKey(String key, SortOption direction) {
		this.key = key;
		this.direction = direction;
	}

	public String getKey() {
		return key;
	}

	public void setKeyIndex(int index) {
		this.index = index;
	}

	public int getKeyIndex() {
		return index;
	}

	public SortOption getDirection() {
		return direction;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GroupKey)) {
			return false;
		}

		if (key == null) {
			return ((GroupKey) obj).key == null && direction == ((GroupKey) obj).direction;
		}

		return key.equals(((GroupKey) obj).key) && direction == ((GroupKey) obj).direction;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (key == null) {
			if (direction == null) {
				return 0;
			} else {
				return direction.hashCode();
			}
		}

		if (direction == null) {
			return key.hashCode();
		} else {
			return key.hashCode() ^ direction.hashCode();
		}
	}
}
