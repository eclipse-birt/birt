/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.report.designer.internal.lib.views.outline;

import org.eclipse.birt.report.designer.internal.lib.views.outline.dnd.LibraryDropListener;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.DesignerDragListener;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.DesignerDropListener;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.DropTypeConstraint;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.IDropConstraint;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;


/**
 * Outline page to show the tree structure of library model.
 * 
 */
public class LibraryOutlinePage extends DesignerOutlinePage
{

	/**
	 * @param reportHandle
	 */
	public LibraryOutlinePage( ModuleHandle reportHandle )
	{
		super( reportHandle );
	}

	protected void addDragAndDropListener()
	{
		//add drag and drop support
		int ops = DND.DROP_MOVE | DND.DROP_COPY;
		Transfer[] transfers = new Transfer[]{
			TemplateTransfer.getInstance( )
		};
		getTreeViewer( ).addDragSupport( ops,
				transfers,
				new DesignerDragListener( getTreeViewer( ) ) );
		transfers = new Transfer[]{
			TemplateTransfer.getInstance( )
		};
		
		DesignerDropListener dropListener = new LibraryDropListener( getTreeViewer( ) );

		dropListener.addDropConstraint( ParameterGroupHandle.class,
				new DropTypeConstraint( ParameterHandle.class,
						ParameterGroupHandle.class,
						true ) );

		dropListener.addDropConstraint( ParameterGroupHandle.class,
				new DropTypeConstraint( Object.class,
						ParameterGroupHandle.class,
						false ) );

		dropListener.addDropConstraint( CascadingParameterGroupHandle.class,
				new IDropConstraint( ) {

					public int validate( Object transfer, Object target )
					{
						return RESULT_NO;
					}
				} );
		
		dropListener.addDropConstraint( ScalarParameterHandle.class,
				new IDropConstraint( ) {

					public int validate( Object transfer, Object target )
					{
						ScalarParameterHandle targetParameter = (ScalarParameterHandle) target;
						if ( targetParameter.getContainer( ) instanceof CascadingParameterGroupHandle )
							return RESULT_NO;
						return RESULT_UNKNOW;
					}
				} );
		
		getTreeViewer( ).addDropSupport( ops, transfers, dropListener );
	}
}
