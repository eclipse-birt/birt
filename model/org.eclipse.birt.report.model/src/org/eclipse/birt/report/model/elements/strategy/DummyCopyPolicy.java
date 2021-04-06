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
 * This policy is a copy policy for pasting, which means, after copying, the
 * original object is deeply cloned, and the target object can be pasted to
 * every where.
 */

public class DummyCopyPolicy extends CopyPolicy {

	/**
	 * Private constructor.
	 */

	private DummyCopyPolicy() {
	}

	private final static DummyCopyPolicy instance = new DummyCopyPolicy();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.elements.strategy.CopyStrategy#execute(org.
	 * eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void execute(DesignElement source, DesignElement destination) {
		return;
	}

	/**
	 * Returns the instance of this class.
	 * 
	 * @return the instance of this class
	 */

	public static DummyCopyPolicy getInstance() {
		return instance;
	}

}
