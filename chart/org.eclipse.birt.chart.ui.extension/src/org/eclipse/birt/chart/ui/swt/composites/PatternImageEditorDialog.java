/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.PatternImage;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.PatternImageUtil;
import org.eclipse.birt.chart.util.PatternImageUtil.ByteColorModel;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 */

public class PatternImageEditorDialog extends TrayDialog
{

	private final Context context;
	private FillChooserComposite fccFore;
	private FillChooserComposite fccBack;

	protected PatternImageEditorDialog( Shell shell, Fill fill )
	{
		super( shell );
		this.context = new Context( fill );
	}

	public PatternImage getPatternImage( )
	{
		return context.getPatternImage( );
	}

	@Override
	protected Control createContents( Composite parent )
	{
		ChartUIUtil.bindHelp( parent,
				ChartHelpContextIds.DIALOG_COLOR_PATTERN_FILL );
		getShell( ).setText( Messages.getString( "PatternImageEditorDialog.Title.PatternFillEditor" ) ); //$NON-NLS-1$
		return super.createContents( parent );
	}

	@Override
	protected Control createDialogArea( Composite parent )
	{
		Composite control = (Composite) super.createDialogArea( parent );
		createPatternGroup( control );
		createColorGroup( control );
		return control;
	}

	private void createPatternGroup( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( Messages.getString( "PatternImageEditorDialog.Group.Pattern" ) ); //$NON-NLS-1$
		group.setLayout( new GridLayout( ) );
		new BitmapGallery( group, context );
	}

	private static class ColorEventListener implements Listener
	{

		private final ColorDefinition colorModel;
		private final Context context;

		ColorEventListener( ColorDefinition color, Context context )
		{
			colorModel = color;
			this.context = context;
		}

		public void handleEvent( Event event )
		{
			if ( event.type == FillChooserComposite.FILL_CHANGED_EVENT )
			{
				if ( event.data instanceof ColorDefinition )
				{
					ColorDefinition color = (ColorDefinition) event.data;
					colorModel.set( color.getRed( ),
							color.getGreen( ),
							color.getBlue( ),
							color.getTransparency( ) );
					context.notifyListeners( );
				}
			}
		}

	}

	private void createColorGroup( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( Messages.getString( "PatternImageEditorDialog.Group.Color" ) ); //$NON-NLS-1$
		GridLayout gl = new GridLayout( 4, false );
		group.setLayout( gl );

		int iFillOption = FillChooserComposite.DISABLE_PATTERN_FILL;
		PatternImage patternImage = context.getPatternImage( );
		new Label( group, SWT.NONE ).setText( Messages.getString( "PatternImageEditorDialog.Label.Foreground" ) ); //$NON-NLS-1$
		fccFore = new FillChooserComposite( group, SWT.DROP_DOWN
				| SWT.READ_ONLY, iFillOption, null, patternImage.getForeColor( ) );
		fccFore.addListener( new ColorEventListener( patternImage.getForeColor( ),
				context ) );

		new Label( group, SWT.NONE ).setText( Messages.getString( "PatternImageEditorDialog.Label.Background" ) ); //$NON-NLS-1$
		fccBack = new FillChooserComposite( group, SWT.DROP_DOWN
				| SWT.READ_ONLY, iFillOption, null, patternImage.getBackColor( ) );
		fccBack.addListener( new ColorEventListener( patternImage.getBackColor( ),
				context ) );
	}

	private static class DropDownControl implements Listener
	{

		public static final int ToggleDropDown = 128;

		protected Composite host;
		protected Composite dropDown;

		public void attachToHost( Composite host )
		{
			this.host = host;
			host.addListener( ToggleDropDown, this );
		}

		protected Composite createDropDown( Composite parent )
		{
			return new Composite( parent, SWT.NONE );
		}

		public void handleEvent( Event event )
		{
			switch ( event.type )
			{
				case SWT.FocusOut :
					onDropDonwFocusOut( );
					break;
				case ToggleDropDown :
					toggleDropDown( );
					break;
			}

		}

		private void onDropDonwFocusOut( )
		{
			Control currentControl = Display.getCurrent( ).getCursorControl( );

			while ( currentControl != null )
			{
				if ( currentControl == host )
				{
					return;
				}
				currentControl = currentControl.getParent( );
			}

			dropDown.getShell( ).close( );
		}

		public void toggleDropDown( )
		{
			if ( dropDown == null || dropDown.isDisposed( ) )
			{
				Point pt = UIHelper.getScreenLocation( host );
				int x = pt.x;
				int y = pt.y + host.getSize( ).y;

				Shell shell = new Shell( host.getShell( ), SWT.NONE );
				shell.setLayout( new GridLayout( ) );
				shell.setLocation( x, y );
				dropDown = createDropDown( shell );
				dropDown.addListener( SWT.KeyDown, this );
				dropDown.addListener( SWT.FocusOut, this );
				shell.layout( );
				shell.pack( );
				shell.open( );
			}
			else
			{
				dropDown.getShell( ).close( );
			}
		}

	}

	private static class PatternBitmapEditComposite extends DropDownControl implements
			Listener,
			PaintListener
	{

		private static final int MARGIN = 2;
		private static final int CELL_WIDTH = 12;
		private static final int CELL_HEIGHT = 12;
		private final Context context;

		PatternBitmapEditComposite( Context context )
		{
			this.context = context;
		}

		@Override
		public void handleEvent( Event event )
		{
			switch ( event.type )
			{
				case SWT.MouseDown :
					int iRow = ( event.y - MARGIN ) / CELL_WIDTH;
					int iCol = ( event.x - MARGIN ) / CELL_HEIGHT;
					long bitmap = context.getBitmap( );
					bitmap = PatternImageUtil.togglePixel( bitmap, iCol, iRow );
					context.updateBitmap( bitmap );
					context.notifyListeners( );
					dropDown.redraw( );
					break;
			}

			super.handleEvent( event );
		}

		private Color createColor( ColorDefinition cd )
		{
			return new Color( Display.getCurrent( ),
					cd.getRed( ),
					cd.getGreen( ),
					cd.getBlue( ) );
		}

		public void paintControl( PaintEvent event )
		{
			GC gc = event.gc;
			Color colorFore = createColor( context.getPatternImage( )
					.getForeColor( ) );
			Color colorBack = createColor( context.getPatternImage( )
					.getBackColor( ) );

			gc.setForeground( Display.getDefault( )
					.getSystemColor( SWT.COLOR_GRAY ) );

			long bitmap = context.getBitmap( );

			for ( int iRow = 0; iRow < 8; iRow++ )
			{
				int y = CELL_HEIGHT * iRow;
				for ( int iCol = 0; iCol < 8; iCol++ )
				{
					int x = CELL_WIDTH * iCol;
					gc.setBackground( PatternImageUtil.isPixelSet( bitmap,
							iCol,
							iRow ) ? colorFore : colorBack );
					gc.fillRectangle( x, y, CELL_WIDTH, CELL_HEIGHT );
					gc.drawRectangle( x, y, CELL_WIDTH, CELL_HEIGHT );
				}
			}

			colorFore.dispose( );
			colorBack.dispose( );
		}

		@Override
		protected Composite createDropDown( Composite parent )
		{
			Composite composite = new Composite( parent, SWT.BORDER
					| SWT.DOUBLE_BUFFERED );
			GridData gd = new GridData( );
			gd.widthHint = CELL_WIDTH * 8 + MARGIN;
			gd.heightHint = CELL_HEIGHT * 8 + MARGIN;
			composite.setLayoutData( gd );

			composite.addListener( SWT.MouseDown, this );
			composite.addListener( SWT.FocusOut, this );
			composite.addListener( SWT.KeyDown, this );
			composite.addListener( SWT.Traverse, this );
			composite.addPaintListener( this );
			return composite;
		}

	}

	private static class BitmapGallery extends Composite implements
			PaintListener,
			Listener
	{

		static final int VIEW_WIDTH = 32;
		static final int VIEW_HEIGHT = 24;
		static final int BUTTON_WIDTH = 14;
		static final int MARGIN = 1;

		private final Context context;
		private int columns = 6;
		private int itemWidth;
		private int itemHeight;
		private int margin;
		private ItemFrame itemFrame;
		private PatternBitmapEditComposite pbEditor;

		public BitmapGallery( Composite parent, Context context )
		{
			super( parent, SWT.DOUBLE_BUFFERED );
			this.context = context;

			itemFrame = new ItemFrame( this, context );
			itemFrame.pack( );
			Point sz = itemFrame.getSize( );
			itemWidth = sz.x;
			itemHeight = sz.y;
			margin = ( itemHeight - VIEW_HEIGHT ) / 2;
			int count = context.getBitmaps( ).size( );
			setLayoutData( new GridData( itemWidth * columns, itemHeight
					* ( count / columns + 1 ) ) );

			select( context.getIndex( ) );
			addListener( SWT.MouseDown, this );
			addPaintListener( this );
			context.addListener( this );

			pbEditor = new PatternBitmapEditComposite( context );
			pbEditor.attachToHost( itemFrame );
		}

		private static GridLayout createGridLayout( )
		{
			GridLayout gl = new GridLayout( 2, false );
			gl.marginWidth = 0;
			gl.marginHeight = 0;
			gl.horizontalSpacing = 0;
			gl.verticalSpacing = 0;
			return gl;
		}

		public void handleEvent( Event event )
		{
			switch ( event.type )
			{
				case SWT.MouseDown :
					int index = computeIndex( event.x, event.y );
					select( index );
					break;
				case SWT.Modify :
					redraw( );
					itemFrame.doRedraw( );
					break;
			}
		}

		private void select( int index )
		{
			if ( context.verifyIndex( index ) )
			{
				context.setIndex( index );
				itemFrame.setLocation( computePos( index ) );
				context.notifyListeners( );
			}
		}

		private int computeIndex( int x, int y )
		{
			int iCol = x / itemWidth;
			int iRow = y / itemHeight;
			return ( iRow * columns ) + iCol;
		}

		private Point computePos( int index )
		{
			int iCol = index % columns;
			int iRow = index / columns;
			return new Point( iCol * itemWidth, iRow * itemHeight );
		}

		private static Image createImageFromPattern( PatternImage patternImage )
		{
			Device device = Display.getCurrent( );

			PaletteData paletteData = new PaletteData( 0xFF00,
					0xFF0000,
					0xFF000000 );
			byte[] data = PatternImageUtil.createImageData( patternImage,
					ByteColorModel.BGRA );

			ImageData imageData = new ImageData( 8, 8, 32, paletteData, 4, data );

			return new Image( device, imageData );
		}

		private Point getItemPos( int index )
		{
			int y = index / columns * itemHeight;
			int x = ( index % columns ) * itemWidth;
			return new Point( x, y );
		}

		private void drawItem( GC gc, int index )
		{
			Point pt = getItemPos( index );
			drawItem( gc, context.getPatternImage( index ), pt.x + margin, pt.y
					+ margin );
		}

		private static void drawItem( GC gc, PatternImage patternImage, int x,
				int y )
		{
			int width = VIEW_WIDTH;
			int height = VIEW_HEIGHT;
			Device device = gc.getDevice( );
			Image image = createImageFromPattern( patternImage );
			Pattern pattern = new Pattern( device, image );
			gc.setBackgroundPattern( pattern );
			gc.fillRectangle( x, y, width, height );
			pattern.dispose( );
			image.dispose( );
		}

		public void paintControl( PaintEvent e )
		{
			GC gc = e.gc;
			int count = context.getBitmaps( ).size( );
			for ( int i = 0; i < count; i++ )
			{
				drawItem( gc, i );
			}
		}

		private static class ItemFrame extends Composite implements
				PaintListener,
				Listener
		{

			private final Context context;

			private Composite view;
			private Button btnDropDown;

			public ItemFrame( Composite parent, Context context )
			{
				super( parent, SWT.BORDER );

				this.context = context;
				GridLayout gl = createGridLayout( );
				gl.marginTop = MARGIN;
				gl.marginBottom = MARGIN;
				gl.marginLeft = MARGIN;
				gl.marginRight = MARGIN;
				setLayout( gl );
				view = new Composite( this, SWT.DOUBLE_BUFFERED );
				view.setLayoutData( new GridData( VIEW_WIDTH, VIEW_HEIGHT ) );
				view.addPaintListener( this );
				view.addListener( SWT.MouseDown, this );

				btnDropDown = new Button( this, SWT.ARROW | SWT.DOWN );
				btnDropDown.setLayoutData( new GridData( BUTTON_WIDTH,
						VIEW_HEIGHT ) );
				btnDropDown.addListener( SWT.Selection, this );
			}

			public void paintControl( PaintEvent e )
			{
				drawItem( e.gc, context.getPatternImage( ), 0, 0 );
			}

			public void doRedraw( )
			{
				view.redraw( );
			}

			public void handleEvent( Event event )
			{
				switch ( event.type )
				{
					case SWT.MouseDown :
					case SWT.Selection :
						event.type = DropDownControl.ToggleDropDown;
						event.widget = this;
						notifyListeners( DropDownControl.ToggleDropDown, event );
						break;
				}

			}

		}
	}

	private static class Context
	{

		private final List<Long> bitmaps = new ArrayList<Long>( );
		private final PatternImage patternImage;
		private int index;
		private Vector<Listener> listeners = new Vector<Listener>( );

		public Context( Fill fill )
		{
			this.patternImage = fill instanceof PatternImage ? (PatternImage) fill.copyInstance( )
					: AttributeFactory.eINSTANCE.createPatternImage( );

			Collections.addAll( bitmaps, getPredefinedBitmaps( ) );
			long bitmap = patternImage.getBitmap( );
			index = bitmaps.indexOf( bitmap );

			if ( index < 0 )
			{
				bitmaps.add( bitmap );
				index = bitmaps.size( ) - 1;
			}
		}

		public void addListener( Listener listener )
		{
			listeners.add( listener );
		}

		public void notifyListeners( )
		{
			Event event = new Event( );
			event.type = SWT.Modify;

			for ( Listener listener : listeners )
			{
				listener.handleEvent( event );
			}
		}

		public boolean verifyIndex( int index )
		{
			return index >= 0 && index < bitmaps.size( );
		}

		public PatternImage getPatternImage( )
		{
			return getPatternImage( index );
		}

		public PatternImage getPatternImage( int index )
		{
			if ( verifyIndex( index ) )
			{
				patternImage.setBitmap( bitmaps.get( index ) );
			}
			return patternImage;
		}

		private static Long[] getPredefinedBitmaps( )
		{
			Long[] bitmaps = new Long[]{
					0x8000000L,
					0x200000040000L,
					0x80000080000L,
					0x240000240000L,
					0xff000000L,
					0x808080808080808L,
					0x8040201008040201L,
					0x102040810204080L,
					0x8080808ff080808L,
					0x8142241818244281L,
					0x44ff444444ff4444L,
					0xff000000ff0000L,
					0x4444444444444444L,
					0xffffff0000L,
					0x1c1c1c1c1c1c1c1cL,
					0xf0f0f0f00f0f0f0fL,
			};
			return bitmaps;
		}

		public void updateBitmap( int index, long bitmap )
		{
			if ( verifyIndex( index ) )
			{
				bitmaps.set( index, bitmap );
			}
		}

		public void updateBitmap( long bitmap )
		{
			updateBitmap( index, bitmap );

		}

		public int getIndex( )
		{
			return index;
		}

		public void setIndex( int index )
		{
			this.index = index;
		}

		public List<Long> getBitmaps( )
		{
			return bitmaps;
		}

		public long getBitmap( )
		{
			return bitmaps.get( index );
		}

	}

}
