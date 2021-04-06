/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.internal.wizards;

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
