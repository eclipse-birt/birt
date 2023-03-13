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

package org.eclipse.birt.report.model.api.util;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Interface to provide information for paste checks. It is used by
 * {@link CopyUtil#canPaste(IElementCopy, org.eclipse.birt.report.model.api.DesignElementHandle, int)
 * and CopyUtil#canPaste(IElementCopy,
 * org.eclipse.birt.report.model.api.DesignElementHandle, String)}.
 */
public interface IPasteStatus {

	/**
	 *
	 * @return
	 */
	boolean canPaste();

	/**
	 *
	 * @return
	 */
	List<SemanticException> getErrors();
}
