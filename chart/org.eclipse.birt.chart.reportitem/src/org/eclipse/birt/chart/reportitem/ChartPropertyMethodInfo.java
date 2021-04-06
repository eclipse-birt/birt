/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public Iterator argumentListIterator() {
		return null;
	}

	public IClassInfo getClassReturnType() {
		return null;
	}

	public String getJavaDoc() {
		return null;
	}

	public String getReturnType() {
		return null;
	}

	public String getToolTip() {
		return tooltip;
	}

	public String getToolTipKey() {
		return tooltipKey;
	}

	public boolean isConstructor() {
		return isConstructor;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDisplayNameKey() {
		return displayNameKey;
	}

	public String getName() {
		return name;
	}

}
