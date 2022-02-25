/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.File;

import org.eclipse.birt.report.designer.ui.ide.navigator.ResourceCloseManagement;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.IRenameChecker;

/**
 *
 */

public class ResourceViewRenameChecker implements IRenameChecker {

	@Override
	public boolean renameCheck(File file) {
		return ResourceCloseManagement.saveDirtyAndCloseOpenFile(file);
	}

}
