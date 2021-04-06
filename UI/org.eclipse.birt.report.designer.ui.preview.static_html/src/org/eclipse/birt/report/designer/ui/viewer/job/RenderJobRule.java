/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.viewer.job;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

/**
 * 
 */

public class RenderJobRule implements ISchedulingRule {

	private String designFile;

	public RenderJobRule(String designFile) {
		this.designFile = designFile;
	}

	public String getDesignFile() {
		return this.designFile;
	}

	public boolean contains(ISchedulingRule rule) {
		if (rule instanceof RenderJobRule && ((RenderJobRule) rule).getDesignFile().equals(getDesignFile())) {
			return true;
		}
		return false;
	}

	public boolean isConflicting(ISchedulingRule rule) {
		if (rule instanceof RenderJobRule && ((RenderJobRule) rule).getDesignFile().equals(getDesignFile())) {
			return true;
		}
		return false;
	}

}
