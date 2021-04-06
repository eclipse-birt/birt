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

package org.eclipse.birt.core.archive;

/**
 * used to get a IArchiveLockManager instance.
 */
public class ArchiveLockManager {

	protected static class ManagerHolder {
		public static IArchiveLockManager instance = new DocArchiveLockManager();
	}

	public static IArchiveLockManager getInstance() {
		return ManagerHolder.instance;
	}
}
