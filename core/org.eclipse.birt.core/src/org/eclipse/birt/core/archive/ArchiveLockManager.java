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
