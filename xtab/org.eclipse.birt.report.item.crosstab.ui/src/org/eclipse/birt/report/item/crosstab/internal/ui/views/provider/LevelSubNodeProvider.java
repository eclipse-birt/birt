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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.swt.graphics.Image;

/**
 * LevelSubNodeProvider
 */
public class LevelSubNodeProvider extends CrossTabCellNodeProvider {

	public String getNodeDisplayName(Object model) {
		return Messages.getString("MeasureSubNodeProvider.Header"); //$NON-NLS-1$
	}

	public Image getNodeIcon(Object model) {
		return CrosstabUIHelper.getImage(CrosstabUIHelper.HEADER_IMAGE);
	}

}
