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

import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Choose the Line decorator of needle
 */
public class HeadStyleChooserComposite extends Composite implements
		SelectionListener,
		MouseListener
{

	private transient Composite cmpContentInner = null;

	private transient Composite cmpContentOuter = null;

	private transient Composite cmpDropDown = null;

	private transient HeadStyleCanvas cnvSelection = null;

	private transient Button btnDown = null;

	private final int[] iLineDecorator = new int[]{
			LineDecorator.ARROW, LineDecorator.NONE, LineDecorator.CIRCLE
	};

	private transient int iCurrentStyle = LineDecorator.ARROW;

	private transient Vector vListeners = null;

	public static final int STYLE_CHANGED_EVENT = 1;

	private transient int iSize = 18;

	private boolean bJustFocusLost = false;

	/**
	 * @param parent
	 * @param style
	 */
	public HeadStyleChooserComposite( Composite parent, int style,
			int iLineDecorator )
	{
		super( parent, style );
		this.iCurrentStyle = iLineDecorator;
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
		// THE LAYOUT OF THIS COMPOSITE (FILLS EVERYTHING INSIDE IT)
		FillLayout flMain = new FillLayout( );
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;
		setLayout( flMain );

		// THE LAYOUT OF THE OUTER COMPOSITE (THAT GROWS VERTICALLY BUT ANCHORS
		// ITS CONTENT NORTH)
		cmpContentOuter = new Composite( this, SWT.NONE );
		GridLayout glContentOuter = new GridLayout( );
		glContentOuter.verticalSpacing = 0;
		glContentOuter.horizontalSpacing = 0;
		glContentOuter.marginHeight = 0;
		glContentOuter.marginWidth = 0;
		glContentOuter.numColumns = 1;
		cmpContentOuter.setLayout( glContentOuter );

		// THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
		// THE CANVAS + BUTTON)
		cmpContentInner = new Composite( cmpContentOuter, SWT.BORDER );
		GridLayout glContentInner = new GridLayout( );
		glContentInner.verticalSpacing = 0;
		glContentInner.horizontalSpacing = 0;
		glContentInner.marginHeight = 0;
		glContentInner.marginWidth = 0;
		glContentInner.numColumns = 2;
		cmpContentInner.setLayout( glContentInner );
		GridData gdContentInner = new GridData( GridData.FILL_HORIZONTAL );
		cmpContentInner.setLayoutData( gdContentInner );

		// THE CANVAS
		cnvSelection = new HeadStyleCanvas( cmpContentInner,
				SWT.NONE,
				LineDecorator.ARROW );
		GridData gdCNVSelection = new GridData( GridData.FILL_BOTH );
		gdCNVSelection.heightHint = iSize;
		cnvSelection.setLayoutData( gdCNVSelection );
		cnvSelection.setHeadStyle( iCurrentStyle );
		cnvSelection.addMouseListener( this );

		// THE BUTTON
		btnDown = new Button( cmpContentInner, SWT.ARROW | SWT.DOWN );
		GridData gdBDown = new GridData( GridData.FILL );
		gdBDown.verticalAlignment = GridData.BEGINNING;
		gdBDown.widthHint = iSize - 2;
		gdBDown.heightHint = iSize;
		btnDown.setLayoutData( gdBDown );
		btnDown.addSelectionListener( this );
		
		Listener listener = new Listener( ) {

			public void handleEvent( Event event )
			{
				handleEventCanvas( event );
				return;
			}
		};

		int[] textEvents = {
				SWT.KeyDown,
				SWT.Traverse,
				SWT.FocusIn,
				SWT.FocusOut
		};
		for ( int i = 0; i < textEvents.length; i++ )
		{
			cnvSelection.addListener( textEvents[i], listener );
		}
	}
	
	void handleEventCanvas( Event event )
	{
		switch ( event.type )
		{
			case SWT.FocusIn :
			{
				cnvSelection.redraw( );
				break;
			}
			case SWT.FocusOut :
			{
				cnvSelection.redraw( );
				break;
			}
			case SWT.KeyDown :
			{
				// At this point the widget may have been disposed.
				// If so, do not continue.
				if ( isDisposed( ) )
					break;

				if ( event.keyCode == SWT.ARROW_DOWN )
				{
					event.doit = true;
					toggleDropDown( );
					break;
				}
			}
			case SWT.Traverse :
			{
				switch ( event.detail )
				{
					case SWT.TRAVERSE_RETURN :
					case SWT.TRAVERSE_TAB_NEXT :
					case SWT.TRAVERSE_TAB_PREVIOUS :
					case SWT.TRAVERSE_ARROW_PREVIOUS :
					case SWT.TRAVERSE_ARROW_NEXT :
						event.doit = true;
						cnvSelection.redraw( );
				}

				break;
			}
		}
	}
	
	void handleEventCmpDropDown( Event event )
	{
		switch ( event.type )
		{
			case SWT.KeyDown :
			{
				keyPressed( new KeyEvent( event ) );
				break;
			}
			case SWT.FocusOut :
			{
				focusLost( new FocusEvent( event ) );
				break;
			}
		}
	}

	/**
	 * Create the content within the drop down button
	 */
	private void createDropDownComponent( int iXLoc, int iYLoc )
	{
		Shell shell = new Shell( this.getShell( ), SWT.NONE );
		shell.setLayout( new FillLayout( ) );
		shell.setSize( this.getSize( ).x, 150 );
		shell.setLocation( iXLoc, iYLoc );

		FillLayout fillDropDown = new FillLayout( );
		fillDropDown.type = SWT.VERTICAL;

		cmpDropDown = new Composite( shell, SWT.NONE );
		cmpDropDown.setLayout( fillDropDown );
		
		Listener listenerCmpDropDown = new Listener( ) {

			public void handleEvent( Event event )
			{
				handleEventCmpDropDown( event );
			}
		};		
		cmpDropDown.addListener( SWT.KeyDown, listenerCmpDropDown );
		cmpDropDown.addListener( SWT.FocusOut, listenerCmpDropDown );

		for ( int iC = 0; iC < this.iLineDecorator.length; iC++ )
		{
			HeadStyleCanvas cnv = new HeadStyleCanvas( cmpDropDown,
					SWT.NONE,
					iLineDecorator[iC] );
			cnv.setSize( cmpDropDown.getSize( ).x, cnvSelection.getSize( ).y );
			cnv.addMouseListener( this );
		}
		shell.layout( );
		shell.open( );
	}

	/**
	 * Returns the current selected head style as an integer corresponding to
	 * the appropriate SWT constants.
	 * 
	 */
	public int getHeadStyle( )
	{
		return this.iCurrentStyle;
	}

	public void setHeadStyle( int iStyle )
	{
		iCurrentStyle = iStyle;
		cnvSelection.setHeadStyle( iCurrentStyle );
		cnvSelection.redraw( );
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	private void toggleDropDown( )
	{
		if ( bJustFocusLost )
		{
			bJustFocusLost = false;
			return;
		}

		if ( cmpDropDown == null
				|| cmpDropDown.isDisposed( )
				|| !cmpDropDown.isVisible( ) )
		{
			Point pLoc = UIHelper.getScreenLocation( this );
			createDropDownComponent( pLoc.x, pLoc.y + this.getSize( ).y );
		}
		else
		{
			cmpDropDown.getShell( ).dispose( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		Object oSource = e.getSource( );
		if ( oSource.equals( btnDown ) )
		{
			toggleDropDown( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	public Point getPreferredSize( )
	{
		return new Point( 100, 24 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick( MouseEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown( MouseEvent e )
	{
		if ( e.getSource( ) instanceof HeadStyleCanvas )
		{
			if ( e.getSource( ).equals( cnvSelection ) )
			{
				toggleDropDown( );
			}
			else
			{
				this.iCurrentStyle = ( (HeadStyleCanvas) e.getSource( ) ).getHeadStyle( );
				this.cnvSelection.setHeadStyle( iCurrentStyle );
				this.cnvSelection.redraw( );
				this.cmpDropDown.getShell( ).dispose( );
				fireEvent( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp( MouseEvent e )
	{
	}

	private void fireEvent( )
	{
		Event e = new Event( );
		e.widget = this;
		e.data = new Integer( this.iCurrentStyle );
		e.type = STYLE_CHANGED_EVENT;
		for ( int i = 0; i < vListeners.size( ); i++ )
		{
			( (Listener) vListeners.get( i ) ).handleEvent( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	void keyPressed( KeyEvent e )
	{
		if ( cmpDropDown != null && !cmpDropDown.getShell( ).isDisposed( ) )
		{
			if ( e.keyCode == SWT.ESC || e.keyCode == SWT.ARROW_UP )
			{
				cmpDropDown.getShell( ).close( );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.FocusListener#focusLost(org.eclipse.swt.events.FocusEvent)
	 */
	void focusLost( FocusEvent e )
	{
		if ( e.getSource( ).equals( cmpDropDown ) )
		{
			Control cTmp = Display.getCurrent( ).getCursorControl( );

			if ( cTmp != null )
			{
				if ( cTmp.equals( cnvSelection ) || cTmp.equals( btnDown ) )
				{
					bJustFocusLost = true;
				}
			}

			cmpDropDown.getShell( ).dispose( );
		}
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
