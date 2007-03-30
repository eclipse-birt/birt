/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class FontDefinitionComposite extends Composite
{
	private static final String TOOLTIP = Messages.getString( "FontDefinitionComposite.Tooltip.FontDialog" ); //$NON-NLS-1$

	private transient Composite cmpContent = null;

	private transient FontCanvas cnvSelection = null;

	private transient Button btnFont = null;

	private transient FontDefinition fdCurrent = null;

	private transient ColorDefinition cdCurrent = null;

	private transient Vector vListeners = null;

	public static final int FONT_CHANTED_EVENT = 1;

	public static final int FONT_DATA = 0;

	public static final int COLOR_DATA = 1;

	private transient int iSize = 18;

	private transient boolean bEnabled = true;

	private transient boolean isAlignmentEnabled = true;

	private transient ChartWizardContext wizardContext;

	public FontDefinitionComposite( Composite parent, int style,
			ChartWizardContext wizardContext, FontDefinition fdSelected,
			ColorDefinition cdSelected, boolean isAlignmentEnabled )
	{
		super( parent, style );
		this.wizardContext = wizardContext;
		this.fdCurrent = fdSelected;
		this.cdCurrent = cdSelected;
		this.isAlignmentEnabled = isAlignmentEnabled;
		init( );
		placeComponents( );
		initAccessible( );
	}

	/**
	 * 
	 */
	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
		vListeners = new Vector( );
	}

	/**
	 * 
	 */
	private void placeComponents( )
	{
		FillLayout flMain = new FillLayout( );
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;

		GridLayout glContent = new GridLayout( );
		glContent.verticalSpacing = 0;
		glContent.horizontalSpacing = 2;
		glContent.marginHeight = 0;
		glContent.marginWidth = 0;
		glContent.numColumns = 2;

		this.setLayout( flMain );

		cmpContent = new Composite( this, SWT.NONE );
		cmpContent.setLayout( glContent );

		cnvSelection = new FontCanvas( cmpContent,
				SWT.BORDER,
				fdCurrent,
				cdCurrent,
				false,
				true,
				false );
		GridData gdCNVSelection = new GridData( GridData.FILL_HORIZONTAL );
		gdCNVSelection.heightHint = iSize;
		cnvSelection.setLayoutData( gdCNVSelection );
		cnvSelection.setToolTipText( TOOLTIP );
		cnvSelection.addMouseListener( new MouseAdapter( ) {

			public void mouseUp( MouseEvent e )
			{
				openFontDialog( );
			}

		} );

		btnFont = new Button( cmpContent, SWT.NONE );
		GridData gdBEllipsis = new GridData( );
		gdBEllipsis.widthHint = 20;
		gdBEllipsis.heightHint = 20;
		btnFont.setLayoutData( gdBEllipsis );
		btnFont.setText( "A" ); //$NON-NLS-1$
		btnFont.setFont(  new Font( Display.getCurrent( ), "Times New Roman", 14, SWT.BOLD ) ); //$NON-NLS-1$
//		btnFont.setImage( UIHelper.getImage( "icons/obj16/fonteditor.gif" ) ); //$NON-NLS-1$
		btnFont.setToolTipText( TOOLTIP ); 
		btnFont.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				openFontDialog( );
			}

		} );
	}

	public void setEnabled( boolean bState )
	{
		this.btnFont.setEnabled( bState );
		this.cnvSelection.setEnabled( bState );
		cnvSelection.redraw( );
		this.bEnabled = bState;
	}

	public boolean isEnabled( )
	{
		return this.bEnabled;
	}

	public FontDefinition getFontDefinition( )
	{
		return this.fdCurrent;
	}

	public ColorDefinition getFontColor( )
	{
		return this.cdCurrent;
	}

	public void setFontDefinition( FontDefinition fd )
	{
		this.fdCurrent = fd;
		cnvSelection.setFontDefinition( fdCurrent );
		cnvSelection.redraw( );
	}

	public void setFontColor( ColorDefinition cd )
	{
		this.cdCurrent = cd;
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	void openFontDialog( )
	{
		// Launch the font selection dialog
		FontDefinitionDialog fontDlg = new FontDefinitionDialog( this.getShell( ),
				wizardContext,
				fdCurrent,
				cdCurrent,
				isAlignmentEnabled );
		if ( fontDlg.open( ) == Window.OK )
		{
			fdCurrent = fontDlg.getFontDefinition( );
			cdCurrent = fontDlg.getFontColor( );
			cnvSelection.setFontDefinition( fdCurrent );
			cnvSelection.setColor( cdCurrent );
			cnvSelection.redraw( );
			fireEvent( );
		}
	}

	private void fireEvent( )
	{
		for ( int iL = 0; iL < vListeners.size( ); iL++ )
		{
			Event se = new Event( );
			se.widget = this;
			Object[] data = new Object[]{
					fdCurrent, cdCurrent
			};
			se.data = data;
			se.type = FONT_CHANTED_EVENT;
			( (Listener) vListeners.get( iL ) ).handleEvent( se );
		}
	}

	public Point getPreferredSize( )
	{
		return new Point( 120, 24 );
	}
	
	void initAccessible( )
	{
		getAccessible( ).addAccessibleListener( new AccessibleAdapter( ) {

			public void getHelp( AccessibleEvent e )
			{
				e.result = getToolTipText( );
			}
		} );

		getAccessible( ).addAccessibleControlListener( new AccessibleControlAdapter( ) {

			public void getChildAtPoint( AccessibleControlEvent e )
			{
				Point testPoint = toControl( new Point( e.x, e.y ) );
				if ( getBounds( ).contains( testPoint ) )
				{
					e.childID = ACC.CHILDID_SELF;
				}
			}

			public void getLocation( AccessibleControlEvent e )
			{
				Rectangle location = getBounds( );
				Point pt = toDisplay( new Point( location.x, location.y ) );
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}

			public void getChildCount( AccessibleControlEvent e )
			{
				e.detail = 0;
			}

			public void getRole( AccessibleControlEvent e )
			{
				e.detail = ACC.ROLE_COMBOBOX;
			}

			public void getState( AccessibleControlEvent e )
			{
				e.detail = ACC.STATE_NORMAL;
			}
		} );
	}
}