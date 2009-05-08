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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.impl.AttributeFactoryImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.render.MarkerRenderer;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/**
 * 
 */

public class MarkerEditorComposite extends Composite implements MouseListener
{

	/** Holds the width of each marker UI block */
	private final static int MARKER_BLOCK_WIDTH = 20;

	/** Holds the width of each marker UI block */
	private final static int MARKER_BLOCK_HEIGHT = 20;

	/** Holds the max number of each row */
	private final static int MARKER_ROW_MAX_NUMBER = 6;

	private transient Marker editingMarker;

	private transient IDeviceRenderer idrSWT = null;

	private transient Canvas cnvMarker;

	private transient Button btnDropDown;

	private transient Composite cmpDropDown;

	public MarkerEditorComposite( Composite parent, Marker marker )
	{
		super( parent, SWT.BORDER );
		this.editingMarker = marker;
		placeComponents( );
		initAccessible( );
		updateMarkerPreview( );
	}

	private void placeComponents( )
	{
		GridLayout layout = new GridLayout( 2, false );
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		setLayout( layout );

		cnvMarker = new Canvas( this, SWT.DOUBLE_BUFFERED );
		{
			GridData gd = new GridData( );
			gd.heightHint = MARKER_BLOCK_HEIGHT;
			gd.widthHint = MARKER_BLOCK_WIDTH;
			gd.verticalAlignment = SWT.CENTER;
			gd.grabExcessVerticalSpace = true;
			cnvMarker.setLayoutData( gd );
			cnvMarker.addMouseListener( this );
			cnvMarker.setToolTipText( getMarker( ).getType( ).getName( ) );

			Listener listener = new Listener( ) {

				public void handleEvent( Event event )
				{
					canvasEvent( event );
				}
			};

			int[] textEvents = {
					SWT.KeyDown,
					SWT.KeyUp,
					SWT.Traverse,
					SWT.FocusIn,
					SWT.FocusOut,
					SWT.Paint
			};
			for ( int i = 0; i < textEvents.length; i++ )
			{
				cnvMarker.addListener( textEvents[i], listener );
			}
		}

		btnDropDown = new Button( this, SWT.ARROW | SWT.DOWN );
		{
			GridData gd = new GridData( );
			gd.heightHint = 20;
			gd.widthHint = 16;
			btnDropDown.setLayoutData( gd );
			btnDropDown.addMouseListener( this );
		}

		try
		{
			idrSWT = ChartEngine.instance( ).getRenderer( "dv.SWT" ); //$NON-NLS-1$
		}
		catch ( ChartException pex )
		{
			WizardBase.displayException( pex );
		}
		
		addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				if ( idrSWT != null )
				{
					idrSWT.dispose( );
					idrSWT = null;
				}
			}
		} );
	}
	

	private void canvasEvent( Event event )
	{
		switch ( event.type )
		{
			case SWT.FocusIn :
			{
				cnvMarker.redraw( );
				break;
			}
			case SWT.FocusOut :
			{
				cnvMarker.redraw( );
				break;
			}
			case SWT.KeyDown :
			{
				// At this point the widget may have been disposed.
				// If so, do not continue.
				if ( isDisposed( ) )
					break;

				if ( event.keyCode == SWT.ARROW_DOWN
						|| event.keyCode == SWT.CR
						|| event.keyCode == SWT.KEYPAD_CR )
				{
					event.doit = true;
					toggleDropDown( );
				}
				break;
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
						cnvMarker.redraw( );
				}

				break;
			}
			case SWT.Paint :
				paintMarker( event.gc, getMarker( ), LocationImpl.create( 10,
						10 ) );
				break;
		}
	}

	public void setMarker( Marker marker )
	{
		this.editingMarker = marker;
		updateMarkerPreview( );
	}

	private void updateMarkerPreview( )
	{
		this.cnvMarker.setToolTipText( getMarker( ).getType( ).getName( ) );
		this.cnvMarker.redraw( );
	}

	public Marker getMarker( )
	{
		return editingMarker;
	}

	private void toggleDropDown( )
	{
		if ( cmpDropDown == null || cmpDropDown.isDisposed( ) )
		{
			createDropDownComponent( );
		}
		else
		{
			cmpDropDown.getShell( ).close( );
		}
	}

	private void createDropDownComponent( )
	{
		Point pLoc = UIHelper.getScreenLocation( btnDropDown.getParent( ) );
		int iXLoc = pLoc.x;
		int iYLoc = pLoc.y + btnDropDown.getParent( ).getSize( ).y;
		int iShellWidth = MARKER_BLOCK_HEIGHT * MARKER_ROW_MAX_NUMBER + 15;

		if ( ( getStyle( ) & SWT.RIGHT_TO_LEFT ) != 0 )
		{
			iXLoc -= iShellWidth;
		}

		// Avoid the right boundary out of screen
		if ( iXLoc + iShellWidth > this.getDisplay( ).getClientArea( ).width )
		{
			iXLoc = this.getDisplay( ).getClientArea( ).width - iShellWidth;
		}

		Shell shell = new Shell( this.getShell( ), SWT.NONE );
		shell.setLayout( new FillLayout( ) );
		// shell.setSize( iShellWidth, iShellHeight );
		shell.setLocation( iXLoc, iYLoc );

		cmpDropDown = new MarkerDropDownEditorComposite( shell, SWT.NONE );

		shell.layout( );
		shell.pack( );
		shell.open( );
	}

	public void mouseDoubleClick( MouseEvent e )
	{
		// TODO Auto-generated method stub

	}

	public void mouseDown( MouseEvent e )
	{
		toggleDropDown( );
	}

	public void mouseUp( MouseEvent e )
	{
		// TODO Auto-generated method stub

	}

	private void paintMarker( GC gc, Marker currentMarker, Location location )
	{
		// Paint an icon sample, not a real icon in the Fill
		Marker renderMarker = currentMarker;
		if ( currentMarker.getType( ) == MarkerType.ICON_LITERAL )
		{
			renderMarker = currentMarker.copyInstance( );
			renderMarker.setFill( ImageImpl.create( UIHelper.getURL( "icons/obj16/marker_icon.gif" ).toString( ) ) ); //$NON-NLS-1$
		}

		idrSWT.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, gc );
		final MarkerRenderer mr = new MarkerRenderer( idrSWT,
				StructureSource.createUnknown( null ),
				location,
				LineAttributesImpl.create( getMarker( ).isVisible( )
						? ColorDefinitionImpl.BLUE( )
						: ColorDefinitionImpl.GREY( ),
						LineStyle.SOLID_LITERAL,
						1 ),
				getMarker( ).isVisible( ) ? ColorDefinitionImpl.create( 80,
						168,
						218 ) : ColorDefinitionImpl.GREY( ),
				renderMarker,
				Integer.valueOf( 4 ),
				null,
				false,
				false );
		boolean bException = false;
		try
		{
			mr.draw( idrSWT );
		}
		catch ( ChartException ex )
		{
			bException = true;
			WizardBase.showException( ex.getLocalizedMessage( ) );
		}

		if ( !bException )
		{
			WizardBase.removeException( );
		}

		// Render a boundary line to indicate focus
		if ( cnvMarker.isFocusControl( ) )
		{
			gc.setLineStyle( SWT.LINE_DOT );
			gc.setForeground( Display.getCurrent( )
					.getSystemColor( SWT.COLOR_BLACK ) );
			gc.drawRectangle( 0, 0, getSize( ).x - 21, this.getSize( ).y - 5 );
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

	private class MarkerDropDownEditorComposite extends Composite
			implements
				PaintListener,
				Listener
	{

		private Spinner iscMarkerSize;

		private Button btnMarkerVisible;
		
		private Button btnOutline;
		
		private Composite cmpType;

		private Group grpSize;

		boolean isPressingKey = false;

		private final String[] typeDisplayNameSet = LiteralHelper.markerTypeSet.getDisplayNames( );
		private final String[] typeNameSet = LiteralHelper.markerTypeSet.getNames( );

		private int markerTypeIndex = -1;

		MarkerDropDownEditorComposite( Composite parent, int style )
		{
			super( parent, style );
			placeComponents( );
		}

		private void placeComponents( )
		{
			GridLayout glDropDown = new GridLayout( );
			this.setLayout( glDropDown );

			btnMarkerVisible = new Button( this, SWT.CHECK );
			{
				btnMarkerVisible.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.IsVisible" ) ); //$NON-NLS-1$
				btnMarkerVisible.setSelection( getMarker( ).isVisible( ) );
				btnMarkerVisible.addListener( SWT.Selection, this );
				btnMarkerVisible.addListener( SWT.FocusOut, this );
				btnMarkerVisible.addListener( SWT.KeyDown, this );
				btnMarkerVisible.addListener( SWT.Traverse, this );
				btnMarkerVisible.setFocus( );
			}

			cmpType = new Composite( this, SWT.NONE );
			{
				GridLayout layout = new GridLayout( );
				layout.numColumns = MARKER_ROW_MAX_NUMBER;
				layout.marginHeight = 0;
				layout.marginWidth = 0;
				layout.horizontalSpacing = 0;
				layout.verticalSpacing = 0;
				cmpType.setLayout( layout );
				cmpType.setLayoutData( new GridData( GridData.FILL_BOTH ) );
				cmpType.addListener( SWT.Traverse, this );
				cmpType.addListener( SWT.KeyDown, this );
				cmpType.addListener( SWT.FocusOut, this );
			}

			int modifiedSize = ( typeDisplayNameSet.length
					/ MARKER_ROW_MAX_NUMBER + 1 )
					* MARKER_ROW_MAX_NUMBER;
			for ( int i = 0; i < modifiedSize; i++ )
			{
				Canvas cnvType = new Canvas( cmpType, SWT.DOUBLE_BUFFERED );
				GridData gd = new GridData( );
				gd.heightHint = MARKER_BLOCK_HEIGHT;
				gd.widthHint = MARKER_BLOCK_WIDTH;
				cnvType.setLayoutData( gd );
				cnvType.setData( Integer.valueOf( i ) );
				cnvType.addPaintListener( this );

				if ( i < typeDisplayNameSet.length )
				{
					// Fake node to make borders more smooth
					cnvType.setToolTipText( typeDisplayNameSet[i] );
					cnvType.addListener( SWT.MouseDown, this );
				}
			}

			grpSize = new Group( this, SWT.NONE );
			{
				grpSize.setLayout( new GridLayout( ) );
				grpSize.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				grpSize.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.Size" ) ); //$NON-NLS-1$
			}

			iscMarkerSize = new Spinner( grpSize, SWT.BORDER );
			{
				iscMarkerSize.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				iscMarkerSize.setMinimum( 0 );
				iscMarkerSize.setMaximum( 100 );
				iscMarkerSize.addListener( SWT.Selection, this );
				iscMarkerSize.addListener( SWT.FocusOut, this );
				iscMarkerSize.addListener( SWT.Traverse, this );
				iscMarkerSize.setSelection( getMarker( ).getSize( ) );
			}
			
			btnOutline = new Button( this, SWT.CHECK );
			{
				btnOutline.setText( Messages.getString("MarkerEditorComposite.Button.Outline") ); //$NON-NLS-1$
				btnOutline.addListener( SWT.Selection, this );
				btnOutline.addListener( SWT.FocusOut, this );
				btnOutline.addListener( SWT.KeyDown, this );
				btnOutline.addListener( SWT.Traverse, this );
				
				LineAttributes la =  getMarker().getOutline( );
				if ( la == null )
				{
					ChartAdapter.beginIgnoreNotifications( );
					la = AttributeFactoryImpl.eINSTANCE.createLineAttributes( );
					la.eAdapters( ).addAll( getMarker().eAdapters( ) );
					EObject o = getMarker();
					while( !( o instanceof LineSeries ) )
					{
						o = o.eContainer( );
						if ( o == null )
							 break;
					}
					if ( o instanceof LineSeries ) {
						la.setVisible( ((LineSeries)o).getLineAttributes( ).isVisible( ) );
					}
					ChartAdapter.endIgnoreNotifications( );
				}
				
				getMarker().setOutline( la );
				btnOutline.setSelection( la.isVisible( ) );
				updateOutlineBtn();
			}
			
			setEnabledState( btnMarkerVisible.getSelection( ) );
		}

		void widgetSelected( SelectionEvent e )
		{
			if ( e.widget.equals( btnMarkerVisible ) )
			{
				getMarker( ).setVisible( btnMarkerVisible.getSelection( ) );
				setEnabledState( btnMarkerVisible.getSelection( ) );
				cnvMarker.redraw( );
				updateOutlineBtn( );
			}
			else if ( e.widget.equals( iscMarkerSize ) )
			{
				getMarker( ).setSize( iscMarkerSize.getSelection( ) );
			}
			else if ( e.widget == btnOutline )
			{
				// Initialize default outline visible state to true.
				LineAttributes la =  getMarker().getOutline( );
				la.setVisible( btnOutline.getSelection( ) );
				cnvMarker.redraw( );
			}
		}

		private void setEnabledState( boolean isEnabled )
		{
			cmpType.setEnabled( isEnabled );
			grpSize.setEnabled( isEnabled );
			iscMarkerSize.setEnabled( isEnabled );

			Control[] cnvTypes = cmpType.getChildren( );
			for ( int i = 0; i < cnvTypes.length; i++ )
			{
				cnvTypes[i].setEnabled( isEnabled );
				cnvTypes[i].redraw( );
			}
		}

		void focusLost( FocusEvent e )
		{
			Control currentControl = isPressingKey ? Display.getCurrent( )
					.getFocusControl( ) : Display.getCurrent( )
					.getCursorControl( );
			// Set default value back
			isPressingKey = false;

			// If current control is the dropdown button, that means users want
			// to close it manually. Otherwise, close it silently when clicking
			// other areas.
			if ( currentControl != btnDropDown
					&& currentControl != cnvMarker
					&& !isChildrenOfThis( currentControl ) )
			{
				this.getShell( ).close( );
			}
		}

		private boolean isChildrenOfThis( Control control )
		{
			while ( control != null )
			{
				if ( control == this )
				{
					return true;
				}
				control = control.getParent( );
			}
			return false;
		}

		public void paintControl( PaintEvent e )
		{
			GC gc = e.gc;
			int markerIndex = ( (Integer) e.widget.getData( ) ).intValue( );
			int markerLength = typeNameSet.length;
			String typeName = null;
			if ( markerIndex < markerLength )
			{
				typeName = typeNameSet[markerIndex];
				gc.setBackground( Display.getDefault( )
						.getSystemColor( SWT.COLOR_INFO_BACKGROUND ) );
				gc.fillRectangle( 0, 0, MARKER_BLOCK_WIDTH, MARKER_BLOCK_HEIGHT );
			}

			int lineWidth = 1;
			if ( getMarker( ).isVisible( ) )
			{
				gc.setForeground( Display.getDefault( )
						.getSystemColor( SWT.COLOR_BLACK ) );
			}
			else
			{
				gc.setForeground( Display.getDefault( )
						.getSystemColor( SWT.COLOR_GRAY ) );
			}
			gc.setLineWidth( lineWidth );

			int x = lineWidth - 1;
			int y = lineWidth - 1;
			int width = MARKER_BLOCK_WIDTH + 1 - 2 * lineWidth;
			int height = MARKER_BLOCK_HEIGHT + 1 - 2 * lineWidth;

			if ( markerIndex / MARKER_ROW_MAX_NUMBER < markerLength
					/ MARKER_ROW_MAX_NUMBER )
			{
				// Remove the bottom border if not in the last row
				height++;
			}
			if ( ( markerIndex + 1 ) % MARKER_ROW_MAX_NUMBER != 0 )
			{
				// Remove the right border if not in the rightmost column
				width++;
			}
			if ( typeName == null )
			{
				if ( markerIndex > markerLength )
				{
					// Remove the left and right border of the fake node unless
					// it's next to the last
					x = -1;
					width += 2;
				}
				// Remove the bottom border of the fake node
				height++;
			}
			// Draw the border
			gc.drawRectangle( x, y, width, height );

			// Draw the boarder of current marker
			if ( getMarker( ).getType( ).getName( ).equals( typeName ) )
			{
				markerTypeIndex = markerIndex;
				gc.setForeground( Display.getDefault( )
						.getSystemColor( SWT.COLOR_RED ) );
				gc.drawRectangle( 1,
						1,
						MARKER_BLOCK_WIDTH - 2,
						MARKER_BLOCK_HEIGHT - 2 );
			}

			// Draw the marker
			if ( typeName != null )
			{
				paintMarker( gc,
						MarkerImpl.create( MarkerType.getByName( typeName ), 4 ),
						LocationImpl.create( MARKER_BLOCK_WIDTH / 2,
								MARKER_BLOCK_HEIGHT / 2 ) );
			}
		}

		private void switchMarkerType( int newMarkerTypeIndex )
		{
			MarkerType newType = MarkerType.getByName( typeNameSet[newMarkerTypeIndex] );
			if ( newType == MarkerType.ICON_LITERAL )
			{
				MarkerIconDialog iconDialog = new MarkerIconDialog( this.getShell( ),
						getMarker( ).getFill( ) );
				if ( iconDialog.open( ) == Window.OK )
				{
					Fill resultFill = iconDialog.getFill( );
					if ( resultFill.eAdapters( ).isEmpty( ) )
					{
						// Add adapters to new EObject
						resultFill.eAdapters( )
								.addAll( getMarker( ).eAdapters( ) );
					}
					getMarker( ).setFill( resultFill );
				}
				else
				{
					// Without saving
					return;
				}
			}

			getMarker( ).setType( newType );
			Control[] children = cmpType.getChildren( );
			children[newMarkerTypeIndex].redraw( );
			children[markerTypeIndex].redraw( );
			updateMarkerPreview( );
		}

		void mouseDown( MouseEvent e )
		{
			if ( e.widget instanceof Canvas )
			{
				int markerIndex = ( (Integer) e.widget.getData( ) ).intValue( );
				switchMarkerType( markerIndex );

				if ( !this.getShell( ).isDisposed( ) )
				{
					this.getShell( ).close( );
				}
			}
		}

		public void handleEvent( Event event )
		{
			switch ( event.type )
			{
				case SWT.FocusOut :
					focusLost( new FocusEvent( event ) );
					break;

				case SWT.MouseDown :
					mouseDown( new MouseEvent( event ) );
					break;

				case SWT.Selection :
					widgetSelected( new SelectionEvent( event ) );
					break;

				case SWT.KeyDown :
					if ( event.keyCode == SWT.ESC )
					{
						getShell( ).close( );
					}
					else if ( event.widget == cmpType )
					{
						if ( event.keyCode == SWT.ARROW_LEFT )
						{
							if ( markerTypeIndex - 1 >= 0 )
							{
								switchMarkerType( markerTypeIndex - 1 );
							}
						}
						else if ( event.keyCode == SWT.ARROW_RIGHT )
						{
							if ( markerTypeIndex + 1 < typeNameSet.length )
							{
								switchMarkerType( markerTypeIndex + 1 );
							}
						}
						else if ( event.keyCode == SWT.ARROW_UP )
						{
							if ( markerTypeIndex - MARKER_ROW_MAX_NUMBER >= 0 )
							{
								switchMarkerType( markerTypeIndex
										- MARKER_ROW_MAX_NUMBER );
							}
						}
						else if ( event.keyCode == SWT.ARROW_DOWN )
						{
							if ( markerTypeIndex + MARKER_ROW_MAX_NUMBER < typeNameSet.length )
							{
								switchMarkerType( markerTypeIndex
										+ MARKER_ROW_MAX_NUMBER );
							}
						}
					}
					break;

				case SWT.Traverse :
					switch ( event.detail )
					{
						case SWT.TRAVERSE_TAB_NEXT :
						case SWT.TRAVERSE_TAB_PREVIOUS :
							// Indicates getting focus control rather than
							// cursor control
							event.doit = true;
							isPressingKey = true;
					}
					break;

			}
		}
		
		private void updateOutlineBtn()
		{
			btnOutline.setEnabled( btnMarkerVisible.getSelection( ) );
		}

	}

}