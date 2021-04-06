/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public boolean renameCheck(File file) {
		return ResourceCloseManagement.saveDirtyAndCloseOpenFile(file);
	}

}