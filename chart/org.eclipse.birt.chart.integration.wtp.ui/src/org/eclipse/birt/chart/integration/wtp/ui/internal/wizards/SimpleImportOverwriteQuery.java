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

package org.eclipse.birt.chart.integration.wtp.ui.internal.wizards;

import org.eclipse.ui.dialogs.IOverwriteQuery;

/**
 * This class is a simple implement of IOverwriteQuery. When handle overwrite
 * files, always return ALL.
 *
 */
public class SimpleImportOverwriteQuery implements IOverwriteQuery {
	/**
	 * Returns "ALL" return code constant declared on this interface.
	 * <p>
	 * 
	 * @see org.eclipse.ui.dialogs.IOverwriteQuery#queryOverwrite(java.lang.String)
	 */
	public String queryOverwrite(String pathString) {
		return ALL;
	}
}
