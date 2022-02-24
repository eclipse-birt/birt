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
