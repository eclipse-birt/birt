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

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class LevelSubNodeProvider extends DefaultNodeProvider
{

	public Object[] getChildren( Object object )
	{
		CrosstabCellHandle cellHandle = (CrosstabCellHandle) object;
		return new Object[]{
			cellHandle.getModelHandle( )
		};
	}

	public Object getParent( Object model )
	{
		CrosstabCellHandle cellHandle = (CrosstabCellHandle) model;
		return cellHandle.getContainer( );
	}

	public boolean hasChildren( Object object )
	{
		return getChildren( object ).length > 0;
	}

	public String getNodeDisplayName( Object model )
	{
		return Messages.getString( "MeasureSubNodeProvider.Header" );
	}

	public Image getNodeIcon( Object model )
	{
		return CrosstabUIHelper.getImage( CrosstabUIHelper.HEADER_IMAGE );
	}

}
