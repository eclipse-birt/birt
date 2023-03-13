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

package org.eclipse.birt.report.designer.ui.samplesview.sampleslocator;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;

/**
 * Represents the resource item in samples fragment, which is either folder or
 * report design file in file system
 */
public interface ISampleReportEntry {
	ResourceEntry[] getEntries();
}
