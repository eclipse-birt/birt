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
import org.eclipse.birt.report.presentation.aggregation.control.ProgressBarFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.DialogContainerFragment;
import org.eclipse.birt.report.presentation.aggregation.dialog.ExceptionDialogFragment;

/**
 * Fragment for report tool bar.
 * <p>
 * @see BaseFragment
 */
public class DocumentFragment extends BaseFragment
{
	/**
	 * Build fragment by adding engine fragment as child.
	 */
	protected void build( )
	{
		engineFragment = new EngineFragment( );
		addChild( new ProgressBarFragment( ) );
		addChild( new DialogContainerFragment( new ExceptionDialogFragment( ) ) );
	}
}
