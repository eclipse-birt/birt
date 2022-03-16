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

package org.eclipse.birt.report.designer.core;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.core.runtime.Platform;

/**
 * This class collects commonly-used choice constants. These constants define
 * the internal value of choices for several property choice constants.
 */

public class DesignerConstants {

	private static final String FONT_FAMILY_COURIER_NEW = "Courier New"; //$NON-NLS-1$

	private static final String FONT_FAMILY_IMPACT = "Impact"; //$NON-NLS-1$

	private static final String FONT_FAMILY_COMIC_SANS_MS = "Comic Sans MS"; //$NON-NLS-1$

	private static final String FONT_FAMILY_ARIAL = "Arial"; //$NON-NLS-1$

	private static final String FONT_FAMILY_TIMES_NEW_ROMAN = "Times New Roman"; //$NON-NLS-1$

	public static final String KEY_NEWOBJECT = "newObject"; //$NON-NLS-1$

	public static final String NEWOBJECT_FROM_LIBRARY = "newObject from library"; //$NON-NLS-1$

	public static final String DIRECT_CREATEITEM = "direct create item"; //$NON-NLS-1$

	public static final String TABLE_ROW_NUMBER = "rowNumber"; //$NON-NLS-1$

	public static final String TABLE_COLUMN_NUMBER = "columnNumber"; //$NON-NLS-1$

	public static final String DATA_SOURCE_SCRIPT = "script"; //$NON-NLS-1$

	public static final String DATA_SET_SCRIPT = "ScriptSelectDataSet"; //$NON-NLS-1$

	public static final String DATA_SOURCE_XMLFILE = "org.eclipse.birt.report.data.oda.xml"; //$NON-NLS-1$

	/**
	 * Map between CSS style font family to system font family
	 */
	public static final Map familyMap = new HashMap();

	/**
	 * Map between CSS font to system font
	 */
	public static final Map fontMap = new HashMap();

	/**
	 * Static table stores the font families. It provides the font name and the
	 * family of the fonts.
	 */
	static {
		familyMap.put(DesignChoiceConstants.FONT_FAMILY_SERIF, FONT_FAMILY_TIMES_NEW_ROMAN);
		familyMap.put(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF, FONT_FAMILY_ARIAL);
		familyMap.put(DesignChoiceConstants.FONT_FAMILY_CURSIVE, FONT_FAMILY_COMIC_SANS_MS);
		familyMap.put(DesignChoiceConstants.FONT_FAMILY_FANTASY, FONT_FAMILY_IMPACT);
		familyMap.put(DesignChoiceConstants.FONT_FAMILY_MONOSPACE, FONT_FAMILY_COURIER_NEW);
	}

	/**
	 * Static table stores the font sizes. It provides the font name and the size of
	 * the fonts.
	 */
	public static final String[][] fontSizes = { { DesignChoiceConstants.FONT_SIZE_XX_SMALL, "7" }, //$NON-NLS-1$
			{ DesignChoiceConstants.FONT_SIZE_X_SMALL, "8" }, //$NON-NLS-1$
			{ DesignChoiceConstants.FONT_SIZE_SMALL, "10" }, //$NON-NLS-1$
			{ DesignChoiceConstants.FONT_SIZE_MEDIUM, "12" }, //$NON-NLS-1$
			{ DesignChoiceConstants.FONT_SIZE_LARGE, "14" }, //$NON-NLS-1$
			{ DesignChoiceConstants.FONT_SIZE_X_LARGE, "17" }, //$NON-NLS-1$
			{ DesignChoiceConstants.FONT_SIZE_XX_LARGE, "20" }, //$NON-NLS-1$
	};

	static {
		// initialize the font map, pair fonts with their size values.
		for (int i = 0; i < fontSizes.length; i++) {
			fontMap.put(fontSizes[i][0], fontSizes[i][1]);
		}
	}

	public static final boolean DEBUG = CorePlugin.getDefault().isDebugging();

	public static final boolean TRACING_COMMANDS = getDebugOption("commands"); //$NON-NLS-1$

	public static final boolean TRACING_MEDIATOR_COLLEAGUE_ADD = getDebugOption("mediator.addColleague"); //$NON-NLS-1$
	public static final boolean TRACING_MEDIATOR_COLLEAGUE_REMOVE = getDebugOption("mediator.removeColleague"); //$NON-NLS-1$
	public static final boolean TRACING_MEDIATOR_GLOBAL_COLLEAGUE_ADD = getDebugOption("mediator.addGlobalColleague"); //$NON-NLS-1$
	public static final boolean TRACING_MEDIATOR_GLOBAL_COLLEAGUE_REMOVE = getDebugOption(
			"mediator.removeGlobalColleague"); //$NON-NLS-1$
	public static final boolean TRACING_MEDIATOR_NOTIFY = getDebugOption("mediator.notifyRequest"); //$NON-NLS-1$
	public static final boolean TRACING_MEDIATOR_STATE_POP = getDebugOption("mediator.popState"); //$NON-NLS-1$
	public static final boolean TRACING_MEDIATOR_STATE_PUSH = getDebugOption("mediator.pushState"); //$NON-NLS-1$
	public static final boolean TRACING_MEDIATOR_STATE_RESTORE = getDebugOption("mediator.restoreState"); //$NON-NLS-1$
	public static final boolean TRACING_MEDIATOR_DISPOSE = getDebugOption("mediator.dispose"); //$NON-NLS-1$

	public static final boolean TRACING_IMAGE_MANAGER_IMAGE_ADD = getDebugOption("imageManger.addImage"); //$NON-NLS-1$
	public static final boolean TRACING_IMAGE_MANAGER_IMAGE_REMOVE = getDebugOption("imageManger.removeImage"); //$NON-NLS-1$

	/**
	 * Returns the debug option with the given id
	 *
	 * @param id the id of the debug option
	 * @return the debug option,or false if the id doesn't exist.
	 */
	public static boolean getDebugOption(String id) {
		boolean option = false;
		if (DEBUG) {
			option = "true".equalsIgnoreCase(Platform.getDebugOption("org.eclipse.birt.report.designer.core/tracing/" //$NON-NLS-1$ //$NON-NLS-2$
					+ id));
		}
		return option;
	}
}
