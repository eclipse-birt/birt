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

package org.eclipse.birt.report.model.api.scripts;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the definition of class member. The class member defines the
 * member type besides name, display name ID and tool tip ID.
 */

public class MemberInfo implements IMemberInfo {

	private final Field field;

	protected MemberInfo(Field field) {
		this.field = field;
	}

	public String getDataType() {
		return field.getType().getName();
	}

	/**
	 * Returns whether this member is static.
	 * 
	 * @return <code>true</code> if this member is true.
	 */

	public boolean isStatic() {
		return Modifier.isStatic(field.getModifiers());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayName ()
	 */

	public String getToolTip() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getToolTipKey ()
	 */

	public String getToolTipKey() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayName ()
	 */

	public String getDisplayName() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getDisplayNameKey
	 * ()
	 */

	public String getDisplayNameKey() {
		return StringUtil.EMPTY_STRING;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.ILocalizableInfo#getName()
	 */

	public String getName() {
		return field.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IMemberInfo#getClassType()
	 */
	public IClassInfo getClassType() {
		return null;
	}
}
