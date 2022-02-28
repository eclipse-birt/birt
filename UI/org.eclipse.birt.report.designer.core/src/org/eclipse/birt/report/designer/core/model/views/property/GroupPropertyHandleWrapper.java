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

package org.eclipse.birt.report.designer.core.model.views.property;

import org.eclipse.birt.report.model.api.GroupPropertyHandle;

public class GroupPropertyHandleWrapper {

	private GroupPropertyHandle handle;

	public GroupPropertyHandleWrapper(GroupPropertyHandle handle) {
		this.handle = handle;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj == null || (!(obj instanceof GroupPropertyHandle) && !(obj instanceof GroupPropertyHandleWrapper))) {
			return false;
		}
		GroupPropertyHandle tmp = null;
		if (obj instanceof GroupPropertyHandleWrapper) {
			tmp = ((GroupPropertyHandleWrapper) obj).getModel();
			if (tmp.getPropertyDefn().getGroupNameKey() == null) {
				return false;
			}
			if (tmp.getPropertyDefn().getGroupNameKey().equals(handle.getPropertyDefn().getGroupNameKey())) {
				return true;
			}
		} else {
			tmp = (GroupPropertyHandle) obj;
			if (tmp.getDisplayValue() == null || tmp.getStringValue() == null) {
				return false;
			}
			if (tmp.getPropertyDefn().equals(handle.getPropertyDefn())
					&& tmp.getDisplayValue().equals(handle.getDisplayValue())
					&& tmp.getStringValue().equals(tmp.getStringValue())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = handle.getPropertyDefn().hashCode();
		if (handle.getDisplayValue() != null) {
			hashCode += handle.getDisplayValue().hashCode() * 7;
		}
		if (handle.getStringValue() != null) {
			hashCode += handle.getStringValue().hashCode() * 13;
		}
		return hashCode;
	}

	public GroupPropertyHandle getModel() {
		return handle;
	}
}
