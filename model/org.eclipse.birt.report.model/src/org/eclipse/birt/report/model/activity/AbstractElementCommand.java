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

package org.eclipse.birt.report.model.activity;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * This class is the base class for commands that work directly with the
 * DesignElement class.
 * 
 */

public abstract class AbstractElementCommand extends Command {

	/**
	 * The element to modify.
	 */

	protected DesignElement element = null;

	/**
	 * Constructor.
	 * 
	 * @param module the module
	 * @param obj    the element to modify
	 */

	public AbstractElementCommand(Module module, DesignElement obj) {
		super(module);
		assert obj != null;
		element = obj;
	}

	/**
	 * Checks and adjusts the new position.
	 * 
	 * @param oldPosn the old position
	 * @param newPosn the new position
	 * @param size    the list size
	 * @return the adjusted new position.
	 */

	static protected int checkAndAdjustPosition(int oldPosn, int newPosn, int size) {
		if (newPosn < 0)
			newPosn = 0;
		if (newPosn > size - 1)
			newPosn = size - 1;

		if (oldPosn < 0 || oldPosn > size)
			throw new IndexOutOfBoundsException("From: " + oldPosn + ", List Size: " + size); //$NON-NLS-1$//$NON-NLS-2$

		if (newPosn < 0 || newPosn > size)
			throw new IndexOutOfBoundsException("To: " + newPosn + ", List Size: " + size); //$NON-NLS-1$//$NON-NLS-2$

		if (oldPosn == newPosn)
			return oldPosn;

		return newPosn;
	}
}
