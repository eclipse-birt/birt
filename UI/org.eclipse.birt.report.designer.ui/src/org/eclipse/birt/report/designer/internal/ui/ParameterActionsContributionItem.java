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

package org.eclipse.birt.report.designer.internal.ui;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.NewParameterAction;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.CompoundContributionItem;

/**
 * ParameterActionsContributionItem
 */
public class ParameterActionsContributionItem extends CompoundContributionItem {

	public final static String PARAMETER_ACTIONS_ID = "ParameterActions"; //$NON-NLS-1$

	@Override
	protected IContributionItem[] getContributionItems() {
		MenuManager dtItem = new MenuManager(Messages.getString("DesignerActionBarContributor.menu.data-NewParameter"), //$NON-NLS-1$
				PARAMETER_ACTIONS_ID);

		dtItem.add(new NewParameterAction(NewParameterAction.INSERT_SCALAR_PARAMETER,
				ReportDesignConstants.SCALAR_PARAMETER_ELEMENT,
				Messages.getString("ParametersNodeProvider.menu.text.parameter"))); //$NON-NLS-1$
		dtItem.add(new NewParameterAction(NewParameterAction.INSERT_CASCADING_PARAMETER_GROUP,
				ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT,
				Messages.getString("ParametersNodeProvider.menu.text.cascadingParameter"))); //$NON-NLS-1$
		dtItem.add(new NewParameterAction(NewParameterAction.INSERT_PARAMETER_GROUP,
				ReportDesignConstants.PARAMETER_GROUP_ELEMENT,
				Messages.getString("ParametersNodeProvider.menu.text.group"))); //$NON-NLS-1$

		return dtItem.getItems();
	}
}
