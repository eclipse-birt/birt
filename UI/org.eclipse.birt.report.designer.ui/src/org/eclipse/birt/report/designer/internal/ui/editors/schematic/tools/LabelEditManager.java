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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Manager for label editor.
 *  
 */
public class LabelEditManager extends DirectEditManager
{

	Font scaledFont;

	/**
	 * Constructor.
	 * 
	 * @param source
	 * @param editorType
	 * @param locator
	 */
	public LabelEditManager( GraphicalEditPart source, Class editorType,
			CellEditorLocator locator )
	{
		super( source, editorType, locator );
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	protected void bringDown( )
	{
		//This method might be re-entered when super.bringDown() is called.
		Font disposeFont = scaledFont;
		scaledFont = null;
		super.bringDown( );
		if ( disposeFont != null )
			disposeFont.dispose( );
	}

	protected void initCellEditor( )
	{
		Text text = (Text) getCellEditor( ).getControl( );

		LabelFigure labelFigure = (LabelFigure) getEditPart( ).getFigure( );
		String initialLabelText = ( (LabelHandle) ( getEditPart( ).getModel( ) ) )
				.getText( );
		if ( initialLabelText == null )
		{
			initialLabelText = ""; //$NON-NLS-1$
		}
		getCellEditor( ).setValue( initialLabelText );
		IFigure figure = getEditPart( ).getFigure( );
		scaledFont = figure.getFont( );
		FontData data = scaledFont.getFontData( )[0];
		Dimension fontSize = new Dimension( 0, data.getHeight( ) );
		labelFigure.translateToAbsolute( fontSize );
		data.setHeight( fontSize.height );
		scaledFont = new Font( null, data );

		text.setFont( scaledFont );
		text.selectAll( );
	}

	/**
	 * Creates the cell editor on the given composite. The cell editor is
	 * created by instantiating the cell editor type passed into this
	 * DirectEditManager's constuctor.
	 * 
	 * @param composite
	 *            the composite to create the cell editor on
	 * @return the newly created cell editor
	 */
	protected CellEditor createCellEditorOn( Composite composite )
	{

		TextCellEditor editor = new TextCellEditor( composite, SWT.SINGLE );
		final Control c = editor.getControl( );
		c.addMouseTrackListener( new MouseTrackAdapter( )
		{

			public void mouseEnter( MouseEvent e )
			{
				c.setCursor( SharedCursors.IBEAM );
			}

		} );
		return editor;
	}
}