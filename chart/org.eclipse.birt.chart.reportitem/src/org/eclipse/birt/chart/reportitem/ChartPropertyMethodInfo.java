/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;

/**
 * ChartPropertyMethodInfo
 */
public final class ChartPropertyMethodInfo implements IMethodInfo {

	private String name;
	private String displayName;
	private String displayNameKey;
	private String tooltip;
	private String tooltipKey;
	private boolean isConstructor;
	private boolean isStatic;

	public ChartPropertyMethodInfo(String name, String displayName, String displayNameKey, String tooltip,
			String tooltipKey, boolean isConstructor, boolean isStatic) {
		this.name = name;
		this.displayName = displayName;
		this.displayNameKey = displayNameKey;
		this.tooltip = tooltip;
		this.tooltipKey = tooltipKey;
		this.isConstructor = isConstructor;
		this.isStatic = isStatic;
	}

	@Override
	public Iterator argumentListIterator() {
		return null;
	}

	@Override
	public IClassInfo getClassReturnType() {
		return null;
	}

	@Override
	public String getJavaDoc() {
		return null;
	}

	@Override
	public String getReturnType() {
		return null;
	}

	@Override
	public String getToolTip() {
		return tooltip;
	}

	@Override
	public String getToolTipKey() {
		return tooltipKey;
	}

	@Override
	public boolean isConstructor() {
		return isConstructor;
	}

	@Override
	public boolean isStatic() {
		return isStatic;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getDisplayNameKey() {
		return displayNameKey;
	}

	@Override
	public String getName() {
		return name;
	}

}
