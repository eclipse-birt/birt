/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.internal.ui.dialogs.TextEditDialog;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.TextFigure;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.dialogs.Dialog;

/**
 * Text edit part class
 */
public class TextEditPart extends LabelEditPart
{

	private static final String DLG_TITLE_TEXT = Messages
			.getString( "TextEditPart.Dialog.Title" ); //$NON-NLS-1$

	private static final String FIGURE_DEFAULT_TEXT = Messages
			.getString( "TextEditPart.Figure.Dafault" ); //$NON-NLS-1$

	/**
	 * @param model
	 */
	public TextEditPart( Object model )
	{
		super( model );
	}

	/**
	 *  Perform direct edit.
	 */
	public void performDirectEdit( )
	{
		TextEditDialog dialog = new TextEditDialog( DLG_TITLE_TEXT,
				( (TextItemHandle) getModel( ) ).getContent( ) );

		dialog.setDesignElementHandle( (DesignElementHandle) getModel( ) );

		if ( dialog.open( ) == Dialog.OK )
		{
			try
			{
				( (TextItemHandle) getModel( ) ).setContent( (String) dialog
						.getResult( ) );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
			refreshVisuals( );
		}

	}

	protected IFigure createFigure( )
	{
		TextFigure text = new TextFigure( );
		return text;
	}

	protected String getText( )
	{
		TextItemHandle handle = (TextItemHandle) getModel( );
		String textContent = handle.getDisplayContent( );
		if ( textContent == null )
		{
			textContent = FIGURE_DEFAULT_TEXT;
		}
		return textContent;
	}

	protected boolean hasText( )
	{
		if ( ( (TextItemHandle) getModel( ) ).getContent( ) == null )
		{
			return false;
		}

		return true;
	}
}