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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.swt.graphics.Image;

/**
 * LevelSubNodeProvider
 */
public class LevelSubNodeProvider extends CrossTabCellNodeProvider {

	@Override
	public String getNodeDisplayName(Object model) {
		return Messages.getString("MeasureSubNodeProvider.Header"); //$NON-NLS-1$
	}

	@Override
	public Image getNodeIcon(Object model) {
		return CrosstabUIHelper.getImage(CrosstabUIHelper.HEADER_IMAGE);
	}

}
