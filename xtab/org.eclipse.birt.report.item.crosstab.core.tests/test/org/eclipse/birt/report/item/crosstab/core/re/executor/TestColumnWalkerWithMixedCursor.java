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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.report.item.crosstab.core.re.DummyCubeCursor;
import org.eclipse.birt.report.item.crosstab.core.re.SimpleMixedEdgeCursor;

/**
 *
 */

public class TestColumnWalkerWithMixedCursor extends TestColumnWalker {

	@Override
	protected CubeCursor createSimpleCubeCursor() {
		DummyCubeCursor dcc = new DummyCubeCursor();
		dcc.addOrdinateEdgeCursor(new SimpleMixedEdgeCursor());

		return dcc;
	}
}
