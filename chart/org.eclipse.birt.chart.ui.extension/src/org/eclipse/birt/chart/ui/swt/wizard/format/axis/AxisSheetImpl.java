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
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class AxisSheetImpl extends SubtaskSheetImpl

{

	private static final int HORIZONTAL_SPACING = 30;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		final int COLUMN_NUMBER = ChartUIUtil.is3DType( getChart( ) ) ? 5 : 4;
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( COLUMN_NUMBER, false );
			glContent.horizontalSpacing = HORIZONTAL_SPACING;
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Label lblAxis = new Label( cmpContent, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblAxis.setLayoutData( gd );
			lblAxis.setFont( JFaceResources.getBannerFont( ) );
			lblAxis.setText( Messages.getString( "AxisSheetImpl.Label.Axis" ) ); //$NON-NLS-1$
		}

		Label lblVisible = new Label( cmpContent, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblVisible.setLayoutData( gd );
			lblVisible.setFont( JFaceResources.getBannerFont( ) );
			lblVisible.setText( Messages.getString( "AxisSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
		}

		Label lblType = new Label( cmpContent, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblType.setLayoutData( gd );
			lblType.setFont( JFaceResources.getBannerFont( ) );
			lblType.setText( Messages.getString( "AxisSheetImpl.Label.Type" ) ); //$NON-NLS-1$
		}

		Label lblColor = new Label( cmpContent, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblColor.setLayoutData( gd );
			lblColor.setFont( JFaceResources.getBannerFont( ) );
			lblColor.setText( Messages.getString( "AxisSheetImpl.Label.Color" ) ); //$NON-NLS-1$
		}

		if ( ChartUIUtil.is3DType( getChart( ) ) )
		{
			Label lblRotation = new Label( cmpContent, SWT.NONE );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.BEGINNING;
				lblRotation.setLayoutData( gd );
				lblRotation.setFont( JFaceResources.getBannerFont( ) );
				lblRotation.setText( Messages.getString( "AxisSheetImpl.Label.Rotation" ) ); //$NON-NLS-1$
			}
		}

		new AxisOptionChoser( ChartUIUtil.getAxisXForProcessing( (ChartWithAxes) getChart( ) ),
				"X", //$NON-NLS-1$
				AngleType.X ).placeComponents( cmpContent );

		int yaxisNumber = ChartUIUtil.getOrthogonalAxisNumber( getChart( ) );
		for ( int i = 0; i < yaxisNumber; i++ )
		{
			String text = "Y"; //$NON-NLS-1$
			new AxisOptionChoser( ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChart( ),
					i ),
					yaxisNumber == 1 ? text : ( text + " - " + ( i + 1 ) ), AngleType.Y ).placeComponents( cmpContent ); //$NON-NLS-1$
		}

		if ( ChartUIUtil.is3DType( getChart( ) ) )
		{
			new AxisOptionChoser( ChartUIUtil.getAxisZForProcessing( (ChartWithAxes) getChart( ) ),
					"Z", //$NON-NLS-1$
					AngleType.Z ).placeComponents( cmpContent );
		}

	}

	class AxisOptionChoser implements SelectionListener, Listener
	{

		private transient Button btnVisible;
		private transient FillChooserComposite cmbColor;
		private transient IntegerSpinControl iscRotation;
		private transient Axis axis;
		private transient String axisName;
		private transient int angleType;

		AxisOptionChoser( Axis axis, String axisName, int angleType )
		{
			this.axis = axis;
			this.axisName = axisName;
			this.angleType = angleType;
		}

		public void placeComponents( Composite parent )
		{
			Label lblAxis = new Label( parent, SWT.NONE );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.CENTER;
				lblAxis.setLayoutData( gd );
				lblAxis.setText( axisName );
			}

			btnVisible = new Button( parent, SWT.CHECK );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.CENTER;
				btnVisible.setLayoutData( gd );
				btnVisible.addSelectionListener( this );
				btnVisible.setSelection( axis.getLineAttributes( ).isVisible( ) );
			}

			Label lblType = new Label( parent, SWT.NONE );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.CENTER;
				lblType.setLayoutData( gd );
				lblType.setText( axis.getType( ).getName( ) );
			}

			ColorDefinition clrCurrent = null;
			if ( axis.eIsSet( ComponentPackage.eINSTANCE.getAxis_LineAttributes( ) ) )
			{
				clrCurrent = axis.getLineAttributes( ).getColor( );
			}
			cmbColor = new FillChooserComposite( parent, SWT.DROP_DOWN
					| SWT.READ_ONLY, clrCurrent, false, false );
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
	}

}