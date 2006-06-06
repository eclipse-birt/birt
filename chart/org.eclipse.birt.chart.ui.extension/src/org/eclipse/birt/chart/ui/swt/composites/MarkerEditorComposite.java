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

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.render.MarkerRenderer;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

/**
 * 
 */

public class MarkerEditorComposite extends Composite
		implements
			MouseListener,
			PaintListener
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

		cnvMarker = new Canvas( this, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.heightHint = MARKER_BLOCK_HEIGHT;
			gd.widthHint = MARKER_BLOCK_WIDTH;
			gd.verticalAlignment = SWT.CENTER;
			gd.grabExcessVerticalSpace = true;
			cnvMarker.setLayoutData( gd );
			cnvMarker.addPaintListener( this );
			cnvMarker.addMouseListener( this );
			cnvMarker.setToolTipText( getMarker( ).getType( ).getName( ) );
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
			idrSWT = PluginSettings.instance( ).getDevice( "dv.SWT" ); //$NON-NLS-1$
			idrSWT.getDisplayServer( );
		}
		catch ( ChartException pex )
		{
			WizardBase.displayException( pex );
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
			cmpDropDown.getShell( ).dispose( );
		}
	}

	private void createDropDownComponent( )
	{
		Point pLoc = UIHelper.getScreenLocation( btnDropDown.getParent( ) );
		int iXLoc = pLoc.x;
		int iYLoc = pLoc.y + btnDropDown.getParent( ).getSize( ).y;
		int iShellWidth = MARKER_BLOCK_HEIGHT * MARKER_ROW_MAX_NUMBER + 15;
		int iShellHeight = 150;
		
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
		shell.setSize( iShellWidth, iShellHeight );
		shell.setLocation( iXLoc, iYLoc );

		cmpDropDown = new MarkerDropDownEditorComposite( shell, SWT.NONE );

		shell.layout( );
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

	public void paintControl( PaintEvent e )
	{
		paintMarker( e.gc, getMarker( ), LocationImpl.create( 10, 10 ) );
	}

	private void paintMarker( GC gc, Marker currentMarker, Location location )
	{
		// Paint an icon sample, not a real icon in the Fill
		Marker renderMarker = currentMarker;
		if ( currentMarker.getType( ) == MarkerType.ICON_LITERAL )
		{
			renderMarker = (Marker) EcoreUtil.copy( currentMarker );
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
				4,
				null,
				false,
				false );
		try
		{
			mr.draw( idrSWT );
		}
		catch ( ChartException ex )
		{
			WizardBase.displayException( ex );
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
				SelectionListener,
				KeyListener,
				FocusListener,
				PaintListener,
				MouseListener
	{

		private Spinner iscMarkerSize;

		private Button btnMarkerVisible;

		private Composite cmpType;

		private Group grpSize;

		MarkerDropDownEditorComposite( Composite parent, int style )
		{
			super( parent, style );
			placeComponents( );
		}

		private void placeComponents( )
		{
			GridLayout glDropDown = new GridLayout( );
			this.setLayout( glDropDown );
			this.addKeyListener( this );
			this.addFocusListener( this );

			btnMarkerVisible = new Button( this, SWT.CHECK );
			{
				btnMarkerVisible.setText( Messages.getString( "LineSeriesAttributeComposite.Lbl.IsVisible" ) ); //$NON-NLS-1$
				btnMarkerVisible.setSelection( getMarker( ).isVisible( ) );
				btnMarkerVisible.addSelectionListener( this );
				btnMarkerVisible.addFocusListener( this );
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
			}

			String[] typeDisplayNameSet = LiteralHelper.markerTypeSet.getDisplayNames( );
			int modifiedSize = ( typeDisplayNameSet.length
					/ MARKER_ROW_MAX_NUMBER + 1 )
					* MARKER_ROW_MAX_NUMBER;
			for ( int i = 0; i < modifiedSize; i++ )
			{
				Canvas cnvType = new Canvas( cmpType, SWT.NONE );
				GridData gd = new GridData( );
				gd.heightHint = MARKER_BLOCK_HEIGHT;
				gd.widthHint = MARKER_BLOCK_WIDTH;
				cnvType.setLayoutData( gd );
				cnvType.setData( new Integer( i ) );
				cnvType.addPaintListener( this );

				if ( i < typeDisplayNameSet.length )
				{
					// Fake node to make borders more smooth
					cnvType.setToolTipText( typeDisplayNameSet[i] );
					cnvType.addMouseListener( this );
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
				iscMarkerSize.addSelectionListener( this );
				iscMarkerSize.addFocusListener( this );
				iscMarkerSize.setSelection( getMarker( ).getSize( ) );
			}

			setEnabledState( btnMarkerVisible.getSelection( ) );
		}

		public void widgetSelected( SelectionEvent e )
		{
			if ( e.widget.equals( btnMarkerVisible ) )
			{
				getMarker( ).setVisible( btnMarkerVisible.getSelection( ) );
				setEnabledState( btnMarkerVisible.getSelection( ) );
				cnvMarker.redraw( );
			}
			else if ( e.widget.equals( iscMarkerSize ) )
			{
				getMarker( ).setSize( iscMarkerSize.getSelection( ) );
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

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void keyPressed( KeyEvent e )
		{
			if ( !this.getShell( ).isDisposed( ) )
			{
				if ( e.keyCode == SWT.ESC )
				{
					this.getShell( ).dispose( );
					return;
				}
			}

		}

		public void keyReleased( KeyEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void focusGained( FocusEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void focusLost( FocusEvent e )
		{
			Control currentControl = Display.getDefault( ).getCursorControl( );
			// If current control is the dropdown button, that means users want
			// to close it manually. Otherwise, close it silently when clicking
			// other areas.
			if ( currentControl != btnDropDown
					&& currentControl != cnvMarker
					&& !isChildrenOfThis( currentControl ) )
			{
				this.getShell( ).dispose( );
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
			int markerLength = LiteralHelper.markerTypeSet.getNames( ).length;
			String typeName = null;
			if ( markerIndex < markerLength )
			{
				typeName = LiteralHelper.markerTypeSet.getNames( )[markerIndex];
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

		public void mouseDoubleClick( MouseEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void mouseDown( MouseEvent e )
		{
			if ( e.widget instanceof Canvas )
			{
				int markerIndex = ( (Integer) e.widget.getData( ) ).intValue( );
				MarkerType newType = MarkerType.getByName( LiteralHelper.markerTypeSet.getNames( )[markerIndex] );
				if ( newType == MarkerType.ICON_LITERAL )
				{
					MarkerIconDialog iconDialog = new MarkerIconDialog( this.getShell( ),
							getMarker( ).getFill( ) );

					if ( iconDialog.applyMarkerIcon( ) )
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
				updateMarkerPreview( );

				if ( !this.getShell( ).isDisposed( ) )
				{
					this.getShell( ).dispose( );
				}
			}
		}

		public void mouseUp( MouseEvent e )
		{

		}

	}

}
