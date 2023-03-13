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

package org.eclipse.birt.report.item.crosstab.core.re;

import java.util.Iterator;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

/**
 *
 */

public class TestMixedCursor {

	public static void main(String[] args) {
		try {
			EdgeCursor ec = new SimpleMixedEdgeCursor();

			List dcs = ec.getDimensionCursor();

			ec.beforeFirst();
			while (ec.next()) {
				for (Iterator itr = dcs.iterator(); itr.hasNext();) {
					DimensionCursor dc = (DimensionCursor) itr.next();
					System.out.print("=== " + dc.getPosition()); //$NON-NLS-1$
					System.out.print(", === " + dc.getEdgeStart()); //$NON-NLS-1$
					System.out.println(", === " + dc.getEdgeEnd()); //$NON-NLS-1$
				}

				System.out.println("============="); //$NON-NLS-1$
			}
		} catch (OLAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
