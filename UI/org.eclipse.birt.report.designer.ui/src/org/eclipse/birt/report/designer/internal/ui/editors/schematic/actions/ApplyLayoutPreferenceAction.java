/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.jface.action.Action;

/**
 * Applies auto or fixed layout to a report design/template.
 */

public class ApplyLayoutPreferenceAction extends Action {

	public static final String AUTO_ACTION_TEXT = Messages.getString("ApplyLayoutPreferenceAction.autoLayout"); //$NON-NLS-1$
	public static final String FIXED_ACTION_TEXT = Messages.getString("ApplyLayoutPreferenceAction.fixedLayout"); //$NON-NLS-1$

	private ReportDesignHandle handle;
	private String layout;
	private String currentLayout;

	public ApplyLayoutPreferenceAction(ReportDesignHandle handle, String layout) {
		this.handle = handle;
		this.layout = layout;
		this.currentLayout = handle.getLayoutPreference();
		if (layout.equals(currentLayout)) {
			this.setChecked(true);
		}
		if (layout.equals(DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT)) {
			setText(AUTO_ACTION_TEXT);
			// setImageDescriptor( ReportPlatformUIImages.getImageDescriptor(
			// IReportGraphicConstants.ICON_LAYOUT_AUTO ) );

		} else {
			setText(FIXED_ACTION_TEXT);
			// setImageDescriptor( ReportPlatformUIImages.getImageDescriptor(
			// IReportGraphicConstants.ICON_LAYOUT_FIXED ) );
		}
	}

	@Override
	public void run() {
		try {
			handle.setLayoutPreference(layout);
		} catch (SemanticException e) {
		}
	}
}
