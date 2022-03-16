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

package org.eclipse.birt.report.designer.internal.ui.layout;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * Help to layout the fixlayout.
 */

public interface IFixLayoutHelper {
	Dimension getFixPreferredSize(int w, int h);

	Dimension getFixMinimumSize(int w, int h);
}
