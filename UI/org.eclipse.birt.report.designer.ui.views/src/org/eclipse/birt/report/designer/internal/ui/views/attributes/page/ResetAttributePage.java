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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.Section;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * The sup-class of all attribute page, provides common register/unregister
 * implementation to DE model, and default refresh process after getting a
 * notify from DE.
 */
public abstract class ResetAttributePage extends AttributePage
{

	public void reset( )
	{
		if ( !canReset( ) )
			return;

		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getCommandStack( );
		stack.startTrans( Messages.getString("ResetAttributePage.Style.Restore.Transaction.Name") ); //$NON-NLS-1$

		Section[] sectionArray = getSections( );
		for ( int i = 0; i < sectionArray.length; i++ )
		{
			Section section = (Section) sectionArray[i];
			section.reset( );
		}
		stack.commit( );
	}

	public boolean canReset( )
	{
		return true;
	}

	class ResetAction extends Action
	{

		ResetAction( )
		{
			super( null, IAction.AS_PUSH_BUTTON );
			setImageDescriptor( ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_STYLE_RESOTRE ) );
			setToolTipText( Messages.getString( "ResetAttributePage.Style.Restore.TooltipText" ) ); //$NON-NLS-1$
		}

		public void run( )
		{
			reset( );
		}

	}

	public Object getAdapter( Class adapter )
	{
		if ( adapter == IAction.class && canReset( ) )
		{
			return new Action[]{
				new ResetAction( )
			};
		}
		return null;
	}
}