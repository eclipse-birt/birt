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

package org.eclipse.birt.report.designer.ui.dialogs;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;


/**
 * 
 */

public class TreeValueDialog extends ElementTreeSelectionDialog
{

	/**
	 * @param parent
	 * @param labelProvider
	 * @param contentProvider
	 */
	public TreeValueDialog( Shell parent, ILabelProvider labelProvider,
			ITreeContentProvider contentProvider )
	{
		super( parent, labelProvider, contentProvider );
		// TODO Auto-generated constructor stub
		setAllowMultiple( false );
	}	


}
