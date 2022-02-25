/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editors;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.editor.IFormPage;

/**
 * IReportEditorPage is the base interface for a report editor page contribute
 * to BIRT report editors.
 */
public interface IReportEditorPage extends IFormPage {

	/**
	 * invoke on page was brought to report editor top.
	 *
	 * @param prePage the last top page.
	 * @return
	 */
	boolean onBroughtToTop(IReportEditorPage prePage);

	/**
	 * Set the page stale type.
	 *
	 * @param type
	 */
	void markPageStale(int type);

	/**
	 * Get the page stale type.
	 *
	 * @return
	 */
	int getStaleType();

	/**
	 * Set page input.
	 *
	 * @param input
	 */
	void setInput(IEditorInput input);
}
