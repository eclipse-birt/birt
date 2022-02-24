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

package org.eclipse.birt.report.designer.ui.actions;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.Action;

/**
 * An action class to hide/show report designer margin.
 */

public class ToggleMarginVisibilityAction extends Action {

	/**
	 * Action ID.
	 */
	public static final String ID = "Toggle Margin Visibility"; //$NON-NLS-1$

	/**
	 * Action display label.
	 */
	public static final String LABEL = Messages.getString("ToggleMarginVisibilityAction.text.Label"); //$NON-NLS-1$

	/**
	 * Action display label.
	 */
	public static final String TOOLTIP = Messages.getString("ToggleMarginVisibilityAction.text.Tooltip"); //$NON-NLS-1$

	private GraphicalViewer diagramViewer;

	/**
	 * The constructor.
	 * 
	 * @param diagramViewer
	 */
	public ToggleMarginVisibilityAction(GraphicalViewer diagramViewer) {
		super(LABEL, AS_CHECK_BOX);
		this.diagramViewer = diagramViewer;
		setToolTipText(TOOLTIP);
		setId(ID);
		setActionDefinitionId(ID);
		setChecked(isChecked());
	}

	/**
	 * @see org.eclipse.jface.action.IAction#isChecked()
	 */
	public boolean isChecked() {
		Boolean val = ((Boolean) diagramViewer.getProperty(DeferredGraphicalViewer.PROPERTY_MARGIN_VISIBILITY));
		if (val != null) {
			return val.booleanValue();
		}
		return true;
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Toggle margin action >> Run ..."); //$NON-NLS-1$
		}
		diagramViewer.setProperty(DeferredGraphicalViewer.PROPERTY_MARGIN_VISIBILITY, Boolean.valueOf(!isChecked()));
	}

}
