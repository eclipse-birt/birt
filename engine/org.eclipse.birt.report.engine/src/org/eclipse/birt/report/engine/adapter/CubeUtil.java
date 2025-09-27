/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.adapter;

import java.util.ArrayList;
import java.util.List;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.CubeCursor;
import jakarta.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.api.EngineException;

/**
 * This is a utility class which is used by Engine to create a unique locator
 * for a cube cursor.
 */

public class CubeUtil {
	private static final String POSITION_DELIMITER = "::";

	/**
	 * Get the position id of a CubeCursor. The position id is decided by the
	 * combination of edge cursors.
	 *
	 * @param cursor
	 * @return
	 * @throws OLAPException
	 */
	public static String getPositionID(CubeCursor cursor) throws OLAPException {
		StringBuilder result = new StringBuilder();
		List ordinateEdge = getAllEdges(cursor);
		boolean isLeftTop = true;
		for (int i = 0; i < ordinateEdge.size(); i++) {
			EdgeCursor edge = (EdgeCursor) ordinateEdge.get(i);
			if (edge.getPosition() != -1) {
				isLeftTop = false;
			}
		}
		if (isLeftTop) {
			result.append(POSITION_DELIMITER);
			result.append(-1);
			result.append(POSITION_DELIMITER);
			result.append(-1);
			return result.toString();
		}
		for (int i = 0; i < ordinateEdge.size(); i++) {
			EdgeCursor edge = (EdgeCursor) ordinateEdge.get(i);
			result.append(POSITION_DELIMITER);
			result.append(edge.getPosition());
		}
		return result.toString();
	}

	/**
	 * Get all EdgeCursor of a CubeCursor.
	 *
	 * @param cursor
	 * @return
	 * @throws OLAPException
	 */
	private static List getAllEdges(CubeCursor cursor) throws OLAPException {
		List ordinateEdge = new ArrayList();
		if (cursor != null) {
			ordinateEdge.addAll(cursor.getOrdinateEdge());
			ordinateEdge.addAll(cursor.getPageEdge());
		}
		return ordinateEdge;
	}

	/**
	 * Set cube cursor to a given position. A cube cursor's position is decided by
	 * its edge cursors.
	 *
	 * @param cursor
	 * @param position
	 * @throws OLAPException
	 * @throws EngineException
	 */
	public static void positionCursor(CubeCursor cursor, String position) throws OLAPException, EngineException {
		if (position == null || position.trim().length() == 0) {
			return;
		}
		if (position.startsWith("::")) {
			position = position.substring(2);
		}
		String[] positions = position.split("\\Q" + POSITION_DELIMITER + "\\E");
		List edges = getAllEdges(cursor);

		for (int i = 0; i < edges.size(); i++) {
			((EdgeCursor) edges.get(i)).setPosition(Long.parseLong(positions[i]));
		}
	}
}
