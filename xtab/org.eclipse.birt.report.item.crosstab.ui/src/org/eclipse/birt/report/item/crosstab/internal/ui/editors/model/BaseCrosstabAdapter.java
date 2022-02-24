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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.core.runtime.IAdaptable;

/**
 * Abstract adapter class
 */

public abstract class BaseCrosstabAdapter implements IAdaptable {
	private AbstractCrosstabItemHandle handle;

	public BaseCrosstabAdapter(AbstractCrosstabItemHandle handle) {
		this.handle = handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		// return the true model same as DesignElementHandle
		if (adapter == DesignElementHandle.class) {
			// handle may bt null, int the additional editpart
			return getDesignElementHandle();
		}
		return null;
	}

	/**
	 * Copy the current aapter to the target
	 * 
	 * @param crossAdapt
	 * @return
	 */
	public BaseCrosstabAdapter copyToTarget(BaseCrosstabAdapter crossAdapt) {
		return crossAdapt;
	}

	/**
	 * @return
	 */
	public AbstractCrosstabItemHandle getCrosstabItemHandle() {
		return handle;
	}

	public abstract List getModelList();

	/**
	 * @return
	 */
	public DesignElementHandle getDesignElementHandle() {
		if (handle == null) {
			return null;
		}
		return handle.getModelHandle();
	}
}
