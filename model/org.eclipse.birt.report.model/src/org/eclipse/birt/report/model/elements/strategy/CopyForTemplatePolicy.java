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

package org.eclipse.birt.report.model.elements.strategy;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * This class implements "copy for template" policy, which need the target
 * element keeping the extends reference just like the orginal one.
 * 
 */

public class CopyForTemplatePolicy extends CopyPolicy {

	/**
	 * Private constructor.
	 */

	private CopyForTemplatePolicy() {
	}

	private final static CopyForTemplatePolicy instance = new CopyForTemplatePolicy();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.elements.strategy.CopyStrategy#execute(org.
	 * eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void execute(DesignElement from, DesignElement to) {
	}

	public static CopyForTemplatePolicy getInstance() {
		return instance;
	}

}
