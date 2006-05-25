/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.axis;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

/**
 * "Axis" subtask. Attention: the axis layout order must be consistent with axis
 * items in the naviagor tree.
 * 
 */
public class AxisSheetImpl extends SubtaskSheetImpl

{

	private static final int HORIZONTAL_SPACING = 30;

	private transient Cursor curHand = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_AXIS );
		
		final int COLUMN_NUMBER = ChartUIUtil.is3DType( getChart( ) ) ? 5 : 4;
		cmpContent = new Composite( parent, SWT.NONE ) {

			public Point computeSize( int wHint, int hHint, boolean changed )
			{
				// Return a fixed height as preferred size of scrolled composite
				Point p = super.computeSize( wHint, hHint, changed );
				p.y = 200;
				return p;
			}
		};;
		{
			GridLayout glContent = new GridLayout( 1, false );
			glContent.horizontalSpacing = HORIZONTAL_SPACING;
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		ScrolledComposite cmpScroll = new ScrolledComposite( cmpContent,
				SWT.V_SCROLL );
		{
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpScroll.setLayoutData( gd );

			cmpScroll.setMinHeight( ( ChartUIUtil.getOrthogonalAxisNumber( getChart( ) ) + ( ChartUIUtil.is3DType( getChart( ) )
					? 2 : 1 ) ) * 24 + 40 );
			cmpScroll.setExpandVertical( true );
			cmpScroll.setExpandHorizontal( true );
		}

		Composite cmpList = new Composite( cmpScroll, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( COLUMN_NUMBER, false );
			glContent.horizontalSpacing = 10;
			cmpList.setLayout( glContent );

			cmpScroll.setContent( cmpList );
		}

		Label lblAxis = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblAxis.setLayoutData( gd );
			lblAxis.setFont( JFaceResources.getBannerFont( ) );
			lblAxis.setText( Messages.getString( "AxisSheetImpl.Label.Axis" ) ); //$NON-NLS-1$
		}

		Label lblVisible = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblVisible.setLayoutData( gd );
			lblVisible.setFont( JFaceResources.getBannerFont( ) );
			lblVisible.setText( Messages.getString( "AxisSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
		}

		Label lblType = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblType.setLayoutData( gd );
			lblType.setFont( JFaceResources.getBannerFont( ) );
			lblType.setText( Messages.getString( "AxisSheetImpl.Label.Type" ) ); //$NON-NLS-1$
		}

		Label lblColor = new Label( cmpList, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblColor.setLayoutData( gd );
			lblColor.setFont( JFaceResources.getBannerFont( ) );
			lblColor.setText( Messages.getString( "AxisSheetImpl.Label.Color" ) ); //$NON-NLS-1$
		}

		if ( ChartUIUtil.is3DType( getChart( ) ) )
		{
			Label lblRotation = new Label( cmpList, SWT.NONE );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.BEGINNING;
				lblRotation.setLayoutData( gd );
				lblRotation.setFont( JFaceResources.getBannerFont( ) );
				lblRotation.setText( Messages.getString( "AxisSheetImpl.Label.Rotation" ) ); //$NON-NLS-1$
			}
		}

		int treeIndex = 0;

		new AxisOptionChoser( ChartUIUtil.getAxisXForProcessing( (ChartWithAxes) getChart( ) ),
				Messages.getString( "AxisSheetImpl.Label.CategoryX" ), //$NON-NLS-1$
				AngleType.X,
				treeIndex++ ).placeComponents( cmpList );

		int yaxisNumber = ChartUIUtil.getOrthogonalAxisNumber( getChart( ) );
		for ( int i = 0; i < yaxisNumber; i++ )
		{
			String text = Messages.getString( "AxisSheetImpl.Label.ValueY" ); //$NON-NLS-1$
			new AxisOptionChoser( ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChart( ),
					i ),
					yaxisNumber == 1 ? text : ( text + " - " + ( i + 1 ) ), AngleType.Y, treeIndex++ ).placeComponents( cmpList ); //$NON-NLS-1$
		}

		if ( ChartUIUtil.is3DType( getChart( ) ) )
		{
			new AxisOptionChoser( ChartUIUtil.getAxisZForProcessing( (ChartWithAxes) getChart( ) ),
					Messages.getString( "AxisSheetImpl.Label.AncillaryZ" ), //$NON-NLS-1$
					AngleType.Z,
					treeIndex++ ).placeComponents( cmpList );
		}

	}

	public void onShow( Object context, Object container )
	{
		super.onShow( context, container );
		curHand = new Cursor( Display.getDefault( ), SWT.CURSOR_HAND );
	}

	public Object onHide( )
	{
		curHand.dispose( );
		return super.onHide( );
	}

	private class AxisOptionChoser
			implements
				SelectionListener,
				Listener,
				MouseListener,
				MouseTrackListener
	{

		private transient Label lblAxis;
		private transient Combo cmbTypes;
		private transient Button btnVisible;
		private transient FillChooserComposite cmbColor;
		private transient IntegerSpinControl iscRotation;
		private transient Axis axis;
		private transient String axisName;
		private transient int angleType;

		// Index of tree item in the navigator tee
		private transient int treeIndex = 0;

		public AxisOptionChoser( Axis axis, String axisName, int angleType,
				int treeIndex )
		{
			this.axis = axis;
			this.axisName = axisName;
			this.angleType = angleType;
			this.treeIndex = treeIndex;
		}

		public void placeComponents( Composite parent )
		{
			lblAxis = new Label( parent, SWT.NONE );
			{
				lblAxis.setText( axisName );
				lblAxis.setForeground( Display.getDefault( )
						.getSystemColor( SWT.COLOR_DARK_BLUE ) );
				lblAxis.addMouseListener( this );
				lblAxis.addMouseTrackListener( this );
			}

			btnVisible = new Button( parent, SWT.CHECK );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.CENTER;
				btnVisible.setLayoutData( gd );
				btnVisible.addSelectionListener( this );
				btnVisible.setSelection( axis.getLineAttributes( ).isVisible( ) );
			}

			cmbTypes = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.CENTER;
				cmbTypes.setLayoutData( gd );
				NameSet ns = LiteralHelper.axisTypeSet;
				cmbTypes.setItems( ns.getDisplayNames( ) );
				cmbTypes.select( ns.getSafeNameIndex( axis.getType( ).getName( ) ) );
				cmbTypes.addSelectionListener( this );
			}

			ColorDefinition clrCurrent = null;
			if ( axis.eIsSet( ComponentPackage.eINSTANCE.getAxis_LineAttributes( ) ) )
			{
				clrCurrent = axis.getLineAttributes( ).getColor( );
			}
			cmbColor = new FillChooserComposite( parent, SWT.DROP_DOWN
					| SWT.READ_ONLY, getContext( ), clrCurrent, false, false );
			{
				GridData gd = new GridData( );
				gd.widthHint = 200;
				cmbColor.setLayoutData( gd );
				cmbColor.addListener( this );
			}

			if ( ChartUIUtil.is3DType( getChart( ) ) )
			{
				iscRotation = new IntegerSpinControl( parent,
						SWT.NONE,
						(int) getAxisAngle( angleType ) );
				{
					GridData gd = new GridData( GridData.FILL_HORIZONTAL );
					gd.heightHint = iscRotation.getPreferredSize( ).y;
					iscRotation.setLayoutData( gd );
					iscRotation.setMaximum( 360 );
					iscRotation.setMinimum( -360 );
					iscRotation.setIncrement( 4 );
					iscRotation.addListener( this );
				}
			}
		}

		public void widgetSelected( SelectionEvent e )
		{
			if ( e.widget.equals( btnVisible ) )
			{
				axis.getLineAttributes( )
						.setVisible( btnVisible.getSelection( ) );
			}
			else if ( e.widget.equals( cmbTypes ) )
			{
				AxisType axisType = AxisType.getByName( LiteralHelper.axisTypeSet.getNameByDisplayName( cmbTypes.getText( ) ) );

				// Update the Sample Data without event fired.
				boolean isNotificaionIgnored = ChartAdapter.isNotificationIgnored( );
				ChartAdapter.ignoreNotifications( true );
				convertSampleData( axisType );
				ChartAdapter.ignoreNotifications( isNotificaionIgnored );

				// Set type and refresh the preview
				axis.setType( axisType );
			}
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void handleEvent( Event event )
		{
			if ( cmbColor.equals( event.widget ) )
			{
				if ( event.type == FillChooserComposite.FILL_CHANGED_EVENT )
				{
					axis.getLineAttributes( )
							.setColor( (ColorDefinition) event.data );
				}
			}
			else if ( event.widget.equals( iscRotation ) )
			{
				setAxisAngle( angleType, ( (Integer) event.data ).intValue( ) );
			}
		}

		private double getAxisAngle( int angleType )
		{
			switch ( angleType )
			{
				case AngleType.X :
					return getAngle3D( ).getXAngle( );
				case AngleType.Y :
					return getAngle3D( ).getYAngle( );
				case AngleType.Z :
					return getAngle3D( ).getZAngle( );
				default :
					return 0;
			}
		}

		private void setAxisAngle( int angleType, int angleDegree )
		{
			Angle3D angle3D = getAngle3D( );
			angle3D.setType( AngleType.NONE_LITERAL );
			( (ChartWithAxes) getChart( ) ).getRotation( ).getAngles( ).clear( );
			( (ChartWithAxes) getChart( ) ).getRotation( )
					.getAngles( )
					.add( angle3D );

			switch ( angleType )
			{
				case AngleType.X :
					angle3D.setXAngle( angleDegree );
					break;
				case AngleType.Y :
					angle3D.setYAngle( angleDegree );
					break;
				case AngleType.Z :
					angle3D.setZAngle( angleDegree );
					break;
			}
		}

		private Angle3D getAngle3D( )
		{
			return (Angle3D) ( (ChartWithAxes) getChart( ) ).getRotation( )
					.getAngles( )
					.get( 0 );
		}

		private void convertSampleData( AxisType axisType )
		{
			if ( angleType == AngleType.X )
			{
				BaseSampleData bsd = (BaseSampleData) getChart( ).getSampleData( )
						.getBaseSampleData( )
						.get( 0 );
				bsd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
						bsd.getDataSetRepresentation( ) ) );
			}
			else if ( angleType == AngleType.Y )
			{
				// Run the conversion routine for ALL orthogonal sample data
				// entries related to series definitions for this axis
				// Get the start and end index of series definitions that fall
				// under this axis
				int iStartIndex = getFirstSeriesDefinitionIndexForAxis( );
				int iEndIndex = iStartIndex
						+ axis.getSeriesDefinitions( ).size( );
				// for each entry in orthogonal sample data, if the series index
				// for the entry is in this range...run conversion routine
				int iOSDSize = getChart( ).getSampleData( )
						.getOrthogonalSampleData( )
						.size( );
				for ( int i = 0; i < iOSDSize; i++ )
				{
					OrthogonalSampleData osd = (OrthogonalSampleData) getChart( ).getSampleData( )
							.getOrthogonalSampleData( )
							.get( i );
					if ( osd.getSeriesDefinitionIndex( ) >= iStartIndex
							&& osd.getSeriesDefinitionIndex( ) <= iEndIndex )
					{
						osd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
								osd.getDataSetRepresentation( ) ) );
					}
				}
			}
		}

		private int getFirstSeriesDefinitionIndexForAxis( )
		{
			int iTmp = 0;
			for ( int i = 0; i < getIndex( ); i++ )
			{
				iTmp += ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChart( ),
						i )
						.getSeriesDefinitions( )
						.size( );
			}
			return iTmp;
		}

		public void mouseDoubleClick( MouseEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void mouseDown( MouseEvent e )
		{
			switchTo( treeIndex );
		}

		public void mouseUp( MouseEvent e )
		{

		}

		public void mouseEnter( MouseEvent e )
		{
			lblAxis.setCursor( curHand );
		}

		public void mouseExit( MouseEvent e )
		{
			lblAxis.setCursor( null );
		}

		public void mouseHover( MouseEvent e )
		{
			// TODO Auto-generated method stub

		}

		private void switchTo( int index )
		{
			TreeItem currentItem = getParentTask( ).getNavigatorTree( )
					.getSelection( )[0];
			TreeItem[] children = currentItem.getItems( );
			if ( index < children.length )
			{
				// Switch to specified subtask
				getParentTask( ).switchToTreeItem( children[index] );
			}
		}
	}

}