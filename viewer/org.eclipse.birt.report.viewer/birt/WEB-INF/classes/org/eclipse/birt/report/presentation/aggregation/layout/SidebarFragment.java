/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.presentation.aggregation.layout;

import org.eclipse.birt.report.presentation.aggregation.BaseFragment;
import org.eclipse.birt.report.presentation.aggregation.control.TocFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.DialogContainerFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.SimpleExportDataDialogFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.ParameterDialogFragment;

/**
 * Navigation fragment.
 * <p>
 * @see BaseFragment
 */
public class SidebarFragment extends BaseFragment
{
	/**
	 * Build fragment by adding navigation fragment root.
	 */
	protected void build( )
	{
		addChild( new TocFragment( ) );
		
//		addChild( new ContextMenuFragment( ) );
		
		addChild( new DialogContainerFragment( new ParameterDialogFragment( ) ) );
//		addChild( new DialogContainerFragment( new PrintDialogFragment( ) ) );
//		addChild( new DialogContainerFragment( new FontDialogFragment( ) ) );
//		addChild( new DialogContainerFragment( new ExportReportDialogFragment( ) ) );
//		addChild( new DialogContainerFragment( new ExportDataDialogFragment( ) ) );
		addChild( new DialogContainerFragment( new SimpleExportDataDialogFragment( ) ) );
	}
}