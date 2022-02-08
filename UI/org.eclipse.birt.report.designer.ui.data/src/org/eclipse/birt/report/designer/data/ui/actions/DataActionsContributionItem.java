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

package org.eclipse.birt.report.designer.data.ui.actions;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.CompoundContributionItem;

/**
 * DataActionsContributionItem
 */
public class DataActionsContributionItem extends CompoundContributionItem {

	@Override
	protected IContributionItem[] getContributionItems() {
		IContributionItem dsItem = new ActionContributionItem(
				new NewDataSourceAction(Messages.getString("designerActionBarContributor.menu.data-newdatasource"))); //$NON-NLS-1$

		MenuManager dtItem = new MenuManager(
				Messages.getString("DesignerActionBarContributor.menu.data-NewDataSetParent")); //$NON-NLS-1$
		dtItem.add(new NewDataSetAction(Messages.getString("designerActionBarContributor.menu.data-newdataset"))); //$NON-NLS-1$
		dtItem.add(new NewJointDataSetAction(
				Messages.getString("designerActionBarContributor.menu.data-newJointDataset"))); //$NON-NLS-1$

		return new IContributionItem[] { dsItem, dtItem };
	}
}
