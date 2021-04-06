/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableColumn;

/**
 * Tests utility for API package. These methods are provided for Model tests
 * plugins since test cases need to oberserve internal states.
 * 
 */

public class ApiTestUtil {

	/**
	 * Returns the design session for the given session handle.
	 * 
	 * @param sessionHandle the session handle
	 * @return the design session
	 */

	public static DesignSession getDesignSession(SessionHandle sessionHandle) {
		if (sessionHandle == null)
			return null;

		return sessionHandle.session;
	}

	/**
	 * Returns copied cells for the given column band data.
	 * 
	 * @param bandData the column band data
	 * @return a list containing copied cells
	 */

	public static List getCopiedCells(ColumnBandData bandData) {
		if (bandData == null)
			return null;

		return bandData.getCells();
	}

	/**
	 * Returns copied cells for the given column band data.
	 * 
	 * @param bandData the column band data
	 * @return a list containing copied cells
	 */

	public static Cell getCopiedCell(ColumnBandData bandData, int index) {
		if (bandData == null)
			return null;

		CellContextInfo contextInfo = (CellContextInfo) bandData.getCells().get(index);
		return contextInfo.getCell();
	}

	/**
	 * Returns copied column for the given column band data.
	 * 
	 * @param bandData the column band data
	 * @return the copied column
	 */

	public static TableColumn getCopiedColumn(ColumnBandData bandData) {
		if (bandData == null)
			return null;

		return bandData.getColumn();
	}

	/**
	 * Returns the font handle of the element.
	 * 
	 * @param element the design element
	 * @return the font handle
	 */

	public static FontHandle getFontProperty(DesignElementHandle element) {
		if (element == null)
			return null;

		return element.getFontProperty();
	}
}
