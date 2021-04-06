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

package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action class for insert common report element, such as Text, Label, Table,
 * etc.
 */

public class GeneralInsertMenuAction extends BaseInsertMenuAction {

	/**
	 * ID for insert Text action.
	 */
	public static final String INSERT_TEXT_ID = "Insert Text"; //$NON-NLS-1$

	/**
	 * Display text for insert Text action.
	 */
	public static final String INSERT_TEXT_DISPLAY_TEXT = Messages.getString("GeneralInsertMenuAction.text.Text"); //$NON-NLS-1$

	/**
	 * ID for insert Label action.
	 */
	public static final String INSERT_LABEL_ID = "Insert Label"; //$NON-NLS-1$

	/**
	 * Display text for insert Label action.
	 */
	public static final String INSERT_LABEL_DISPLAY_TEXT = Messages.getString("GeneralInsertMenuAction.text.Label"); //$NON-NLS-1$

	/**
	 * ID for insert Data action.
	 */
	public static final String INSERT_DATA_ID = "Insert Data"; //$NON-NLS-1$

	/**
	 * Display text for insert Data action.
	 */
	public static final String INSERT_DATA_DISPLAY_TEXT = Messages.getString("GeneralInsertMenuAction.text.Data"); //$NON-NLS-1$

	/**
	 * ID for insert Image action.
	 */
	public static final String INSERT_IMAGE_ID = "Insert Image"; //$NON-NLS-1$

	/**
	 * Display text for insert Image action.
	 */
	public static final String INSERT_IMAGE_DISPLAY_TEXT = Messages.getString("GeneralInsertMenuAction.text.Image"); //$NON-NLS-1$

	/**
	 * ID for insert Grid action.
	 */
	public static final String INSERT_GRID_ID = "Insert Grid"; //$NON-NLS-1$

	/**
	 * Display text for insert Grid action.
	 */
	public static final String INSERT_GRID_DISPLAY_TEXT = Messages.getString("GeneralInsertMenuAction.text.Grid"); //$NON-NLS-1$

	/**
	 * ID for insert List action.
	 */
	public static final String INSERT_LIST_ID = "Insert List"; //$NON-NLS-1$

	/**
	 * Display text for insert List action.
	 */
	public static final String INSERT_LIST_DISPLAY_TEXT = Messages.getString("GeneralInsertMenuAction.text.List"); //$NON-NLS-1$

	/**
	 * ID for insert Table action.
	 */
	public static final String INSERT_TABLE_ID = "Insert Table"; //$NON-NLS-1$

	/**
	 * Display text for insert Table action.
	 */
	public static final String INSERT_TABLE_DISPLAY_TEXT = Messages.getString("GeneralInsertMenuAction.text.Table"); //$NON-NLS-1$

	/**
	 * ID for insert Table action.
	 */
	public static final String INSERT_DYNAMIC_TEXT_ID = "Insert Dynamic Text"; //$NON-NLS-1$

	/**
	 * Display text for insert Table action.
	 */
	public static final String INSERT_DYNAMIC_TEXT_DISPLAY_TEXT = Messages
			.getString("GeneralInsertMenuAction.text.DynamicText"); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param part parent workbench part
	 * @param ID   action ID
	 * @param type insert type
	 */
	public GeneralInsertMenuAction(IWorkbenchPart part, String ID, String type) {
		super(part, type);

		setId(ID);
	}

	/**
	 * The constructor.
	 * 
	 * @param part
	 * @param ID
	 * @param type
	 * @param label
	 */
	public GeneralInsertMenuAction(IWorkbenchPart part, String ID, String type, String label) {
		this(part, ID, type);

		setText(label);
	}

}