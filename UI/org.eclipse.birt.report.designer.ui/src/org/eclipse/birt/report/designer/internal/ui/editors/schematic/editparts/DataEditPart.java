/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

/**
 * Data edit part
 *  
 */
public class DataEditPart extends LabelEditPart
{

	private static final String FIGURE_DEFAULT_TEXT = Messages.getString( "DataEditPart.Figure.Dafault" ); //$NON-NLS-1$

	private static final int TRUNCATE_LENGTH = 18;

	private static final String ELLIPSIS = "..."; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public DataEditPart( Object model )
	{
		super( model );
	}

	protected IFigure createFigure( )
	{
		LabelFigure label = new LabelFigure( );
		label.setLayoutManager( new StackLayout( ) );

		return label;
	}

	/**
	 * Popup the builder for Data element
	 */
	public void performDirectEdit( )
	{
		DataItemHandle handle = (DataItemHandle) getModel( );
		ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ),
				handle.getValueExpr( ) );
		dialog.setDataSetList( DEUtil.getDataSetList( handle ) );
		if ( dialog.open( ) == Dialog.OK )
		{
			try
			{
				( (DataItemHandle) getModel( ) ).setValueExpr( (String) dialog.getResult( ) );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
				return;
			}
			refreshVisuals( );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		super.refreshFigure( );

		( (LabelFigure) getFigure( ) ).setToolTipText( ( (DataItemHandle) getModel( ) ).getValueExpr( ) );
	}

	protected String getText( )
	{
		String text = ( (DataItemHandle) getModel( ) ).getValueExpr( );
		if ( text == null || text.length( ) == 0 )
		{
			text = FIGURE_DEFAULT_TEXT;
		}
		else
		{
			if ( text.length( ) > TRUNCATE_LENGTH )
			{
				text = text.substring( 0, TRUNCATE_LENGTH - 2 ) + ELLIPSIS;
			}
		}
		return text;
	}

	protected boolean hasText( )
	{
		String text = ( (DataItemHandle) getModel( ) ).getValueExpr( );

		return ( text != null && text.length( ) > 0 );
	}
}