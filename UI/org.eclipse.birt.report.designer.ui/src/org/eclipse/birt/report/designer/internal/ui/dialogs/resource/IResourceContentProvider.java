/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
