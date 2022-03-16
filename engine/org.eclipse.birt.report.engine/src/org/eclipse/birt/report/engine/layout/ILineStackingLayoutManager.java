/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout;

import org.eclipse.birt.report.engine.content.ITextContent;

public interface ILineStackingLayoutManager extends IInlineStackingLayoutManager {

	/**
	 * End current line and submit the line to parent
	 *
	 * @return true if submit succeeded
	 */
	boolean endLine();

	boolean isEmptyLine();

	int getMaxLineWidth();

	void setTextIndent(ITextContent content);

}
