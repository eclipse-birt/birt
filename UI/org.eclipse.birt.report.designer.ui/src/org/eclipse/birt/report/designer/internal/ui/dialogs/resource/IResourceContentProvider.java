/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import org.eclipse.jface.viewers.ITreeContentProvider;

/**
 * 
 */

public interface IResourceContentProvider extends ITreeContentProvider {

	public static final int ALWAYS_SHOW_EMPTYFOLDER = 1;
	public static final int ALWAYS_NOT_SHOW_EMPTYFOLDER = 2;

	public int getEmptyFolderShowStatus();

	public void setEmptyFolderShowStatus(int showStatus);
}
