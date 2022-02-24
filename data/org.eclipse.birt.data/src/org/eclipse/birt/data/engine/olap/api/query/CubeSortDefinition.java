
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
package org.eclipse.birt.data.engine.olap.api.query;

import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;

/**
 * 
 */

public class CubeSortDefinition extends SortDefinition implements ICubeSortDefinition {
	private ILevelDefinition[] axisQualifierLevel;
	private Object[] axisQualifierValue;
	private ILevelDefinition targetLevel = null;

	public void setAxisQualifierLevels(ILevelDefinition[] level) {
		if (level == null)
			this.axisQualifierLevel = new ILevelDefinition[0];
		this.axisQualifierLevel = level;
	}

	public void setAxisQualifierValues(Object[] value) {
		if (value == null)
			this.axisQualifierValue = new Object[0];
		this.axisQualifierValue = value;
	}

	public void setTargetLevel(ILevelDefinition targetLevel) {
		this.targetLevel = targetLevel;
	}

	public ILevelDefinition[] getAxisQualifierLevels() {
		return this.axisQualifierLevel == null ? new ILevelDefinition[0] : this.axisQualifierLevel;
	}

	public Object[] getAxisQualifierValues() {
		return this.axisQualifierValue == null ? new Object[0] : this.axisQualifierValue;
	}

	public ILevelDefinition getTargetLevel() {
		return this.targetLevel;
	}

}
