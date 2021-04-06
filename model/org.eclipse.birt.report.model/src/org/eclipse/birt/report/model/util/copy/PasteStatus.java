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

package org.eclipse.birt.report.model.util.copy;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.CopyUtil;
import org.eclipse.birt.report.model.api.util.IElementCopy;
import org.eclipse.birt.report.model.api.util.IPasteStatus;

/**
 * Implement to provide information for paste checks. It is used by
 * {@link CopyUtil#canPaste(IElementCopy, org.eclipse.birt.report.model.api.DesignElementHandle, int)
 * and CopyUtil#canPaste(IElementCopy,
 * org.eclipse.birt.report.model.api.DesignElementHandle, String)}.
 */
public class PasteStatus implements IPasteStatus {

	protected boolean canPaste = false;
	protected List<SemanticException> errors = null;

	/**
	 * Default constructor.
	 */
	public PasteStatus() {

	}

	/**
	 * 
	 * @return
	 */
	public boolean canPaste() {
		return canPaste;
	}

	/**
	 * 
	 * @param canPaste
	 */
	public void setPaste(boolean canPaste) {
		this.canPaste = canPaste;
	}

	/**
	 * 
	 * @return
	 */
	public List<SemanticException> getErrors() {
		if (errors == null)
			return Collections.emptyList();
		return errors;
	}

	/**
	 * 
	 * @param errors
	 */
	public void setErrors(List<SemanticException> errors) {
		this.errors = errors;
	}

}
