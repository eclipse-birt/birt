/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LocalizedNumberEditorComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.BlockPropertiesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.CustomPropertiesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.GeneralPropertiesChartSheet;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
/**
 * @author Actuate Corporation
 * 
 */
public class ChartSheetImpl extends SubtaskSheetImpl implements
		SelectionListener,
		Listener
{

	private transient FillChooserComposite cmbBackground;

	private transient FillChooserComposite fccWall;

	private transient FillChooserComposite fccFloor;

	private transient Combo cmbStyle;

	private transient Button btnEnablePreview;

	private transient Button btnResetValue;	

	private transient Button btnEnable;
	
	private transient Button btnInteractivity;
	
	private AxisRotationChooser xChooser;
	
	private AxisRotationChooser yChooser;
	
	private AxisRotationChooser zChooser;

	public void createControl( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_CHART );

		init( );

		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 3, true );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 3, false ) );
			GridData gd = new GridData( GridData.FILL_BOTH );
			gd.horizontalSpan = 2;
			cmpBasic.setLayoutData( gd );
		}

		Composite cmp3D = new Composite( cmpContent, SWT.NONE );
		{
			cmp3D.setLayout( new GridLayout( ) );
			cmp3D.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}

		Label lblBackground = new Label( cmpBasic, SWT.NONE );
		lblBackground.setText( Messages.getString( "ChartSheetImpl.Label.Background" ) ); //$NON-NLS-1$

		cmbBackground = new FillChooserComposite( cmpBasic,
				SWT.NONE,
				getContext( ),
				getChart( ).getBlock( ).getBackground( ),
				true,
				true );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			cmbBackground.setLayoutData( gridData );
			cmbBackground.addListener( this );
		}

		new Label( cmpBasic, SWT.NONE );

		if ( hasWallAndFloor( ) )
		{
			Label lblWall = new Label( cmpBasic, SWT.NONE );
			lblWall.setLayoutData( new GridData( ) );
			lblWall.setText( Messages.getString( "AttributeSheetImpl.Lbl.ChartWall" ) ); //$NON-NLS-1$

			fccWall = new FillChooserComposite( cmpBasic,
					SWT.NONE,
					getContext( ),
					( (ChartWithAxes) getChart( ) ).getWallFill( ),
					true,
					true,
					true,
					true );
			GridData gdFCCWall = new GridData( GridData.FILL_HORIZONTAL );
			fccWall.setLayoutData( gdFCCWall );
			fccWall.addListener( this );

			new Label( cmpBasic, SWT.NONE );

			Label lblFloor = new Label( cmpBasic, SWT.NONE );
			lblFloor.setLayoutData( new GridData( ) );
			lblFloor.setText( Messages.getString( "AttributeSheetImpl.Lbl.ChartFloor" ) ); //$NON-NLS-1$

			fccFloor = new FillChooserComposite( cmpBasic,
					SWT.NONE,
					getContext( ),
					( (ChartWithAxes) getChart( ) ).getFloorFill( ),
					true,
					true,
					true,
					true );
			GridData gdFCCFloor = new GridData( GridData.FILL_HORIZONTAL );
			fccFloor.setLayoutData( gdFCCFloor );
			fccFloor.addListener( this );

			new Label( cmpBasic, SWT.NONE );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartSheetImpl.Label.Style" ) ); //$NON-NLS-1$

		cmbStyle = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			cmbStyle.setLayoutData( gridData );
			cmbStyle.addSelectionListener( this );
		}

		btnEnablePreview = new Button( cmpBasic, SWT.CHECK );
		{
			btnEnablePreview.setText( Messages.getString( "ChartSheetImpl.Label.EnableInPreview" ) ); //$NON-NLS-1$
			btnEnablePreview.setSelection( ChartPreviewPainter.isProcessorEnabled( ) );
			btnEnablePreview.addSelectionListener( this );
		}

		if ( ( getChart( ) instanceof ChartWithAxes )
				&& ChartUIUtil.is3DType( getChart( ) ) )
		{
			Group cmpRotation = new Group( cmp3D, SWT.NONE );
			{
				GridLayout gl = new GridLayout( );
				gl.marginTop = 0;
				gl.verticalSpacing = 0;
				cmpRotation.setLayout( gl );
				cmpRotation.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				cmpRotation.setText( Messages.getString( "ChartLegendImpl.Group.Rotation" ) ); //$NON-NLS-1$
			}

			xChooser = new AxisRotationChooser( ChartUIUtil.getAxisXForProcessing( (ChartWithAxes) getChart( ) ),
					AngleType.X );
			xChooser.placeComponents( cmpRotation );

			yChooser = new AxisRotationChooser( ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChart( ),
						0 ),
						AngleType.Y );
			yChooser.placeComponents( cmpRotation );

			zChooser = new AxisRotationChooser( ChartUIUtil.getAxisZForProcessing( (ChartWithAxes) getChart( ) ),
					AngleType.Z );
			zChooser.placeComponents( cmpRotation );

			btnResetValue = new Button( cmpRotation, SWT.PUSH );
			{
				btnResetValue.setText( Messages.getString( "ChartSheetImpl.Label.ResetValue" ) ); //$NON-NLS-1$
				btnResetValue.setSelection( ChartPreviewPainter.isProcessorEnabled( ) );
				btnResetValue.addSelectionListener( this );
			}
		}	
		
		 btnEnable = new Button( cmpBasic, SWT.CHECK );
		{
			GridData gridData = new GridData( );
			gridData.horizontalSpan = 3;
			btnEnable.setLayoutData( gridData );
			btnEnable.setText( Messages.getString( "ChartSheetImpl.Label.InteractivityEnable" ) ); //$NON-NLS-1$
			btnEnable.setSelection( getChart( ).getInteractivity( ).isEnable( ) );
			btnEnable.addSelectionListener( this );
		}
		
		populateLists( );

		createButtonGroup( cmpContent );
	}

	private void init( )
	{
		// Make it compatible with old model
		if ( getChart( ).getInteractivity( ) == null )
		{
			Interactivity interactivity = InteractivityImpl.create( );
			interactivity.eAdapters( ).addAll( getChart( ).eAdapters( ) );
			getChart( ).setInteractivity( interactivity );
		}
	}

	private void populateLists( )
	{
		// POPULATE STYLE COMBO WITH AVAILABLE REPORT STYLES
		IDataServiceProvider idsp = getContext( ).getDataServiceProvider( );
		if ( idsp != null )
		{
			String[] allStyleNames = idsp.getAllStyles( );
			String[] displayNames = idsp.getAllStyleDisplayNames( );

			// Add None option to remove style
			String[] selection = new String[displayNames.length + 1];
			System.arraycopy( displayNames,
					0,
					selection,
					1,
					displayNames.length );
			selection[0] = Messages.getString( "ChartSheetImpl.Label.None" ); //$NON-NLS-1$
			cmbStyle.setItems( selection );
			cmbStyle.setData( allStyleNames );

			String sStyle = idsp.getCurrentStyle( );
			int idx = getStyleIndex( sStyle );
			cmbStyle.select( idx + 1 );
		}
	}

	private int getStyleIndex( String style )
	{
		String[] allStyleNames = (String[]) cmbStyle.getData( );

		if ( style != null && allStyleNames != null )
		{
			for ( int i = 0; i < allStyleNames.length; i++ )
			{
				if ( style.equals( allStyleNames[i] ) )
				{
					return i;
				}
			}
		}

		return -1;
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 4, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 3;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		ITaskPopupSheet popup;

		popup = new BlockPropertiesSheet( Messages.getString( "ChartSheetImpl.Text.Outline" ), //$NON-NLS-1$
				getContext( ) );
		Button btnBlockProp = createToggleButton( cmp,
				Messages.getString( "ChartSheetImpl.Text.Outline&" ), //$NON-NLS-1$
				popup );
		btnBlockProp.addSelectionListener( this );

		popup = new GeneralPropertiesChartSheet( Messages.getString( "ChartSheetImpl.Text.GeneralProperties" ), //$NON-NLS-1$
				getContext( ) );
		Button btnGeneralProp = createToggleButton( cmp,
				Messages.getString( "ChartSheetImpl.Text.GeneralProperties&" ), //$NON-NLS-1$
				popup );
		btnGeneralProp.addSelectionListener( this );

		popup = new CustomPropertiesSheet( Messages.getString( "ChartSheetImpl.Text.CustomProperties" ), //$NON-NLS-1$
				getContext( ) );
		Button btnCustomProp = createToggleButton( cmp,
				Messages.getString( "ChartSheetImpl.Text.CustomProperties&" ), //$NON-NLS-1$
				popup );
		btnCustomProp.addSelectionListener( this );
		
		// Interactivity
		popup = new InteractivitySheet( Messages.getString( "SeriesYSheetImpl.Label.Interactivity" ), //$NON-NLS-1$
				getContext( ),
				getChart( ).getBlock( ).getTriggers( ),
				false,
				true );
		btnInteractivity = createToggleButton( cmp,
				Messages.getString( "SeriesYSheetImpl.Label.Interactivity&" ), //$NON-NLS-1$
				popup );
		btnInteractivity.addSelectionListener( this );
		btnInteractivity.setEnabled( getChart( ).getInteractivity( ).isEnable( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( cmbBackground ) )
		{
			getChart( ).getBlock( ).setBackground( (Fill) event.data );
		}
		else if ( event.widget.equals( fccWall ) )
		{
			if ( hasWallAndFloor( ) )
			{
				( (ChartWithAxes) getChart( ) ).setWallFill( (Fill) event.data );
			}
		}
		else if ( event.widget.equals( fccFloor ) )
		{
			if ( hasWallAndFloor( ) )
			{
				( (ChartWithAxes) getChart( ) ).setFloorFill( (Fill) event.data );
			}
		}
	}

	public void widgetSelected( SelectionEvent e )
	{
		// Detach popup dialog if there's selected popup button.
		if ( detachPopup( e.widget ) )
		{
			return;
		}

		if ( isRegistered( e.widget ) )
		{
			attachPopup( ( (Button) e.widget ).getText( ) );
		}
		
		if ( e.widget.equals( cmbStyle ) )
		{
			String[] allStyleNames = (String[]) cmbStyle.getData( );
			String sStyle = null;
			int idx = cmbStyle.getSelectionIndex( );
			if ( idx > 0 )
			{
				sStyle = allStyleNames[idx - 1];
			}
			getContext( ).getDataServiceProvider( ).setStyle( sStyle );
			if ( btnEnablePreview.getSelection( ) )
			{
				refreshPreview( );
			}
		}
		else if ( e.widget.equals( btnEnablePreview ) )
		{
			ChartPreviewPainter.enableProcessor( btnEnablePreview.getSelection( ) );
			refreshPreview( );
		}
		else if ( e.widget.equals( btnEnable ) )
		{
			getChart( ).getInteractivity( )
					.setEnable( btnEnable.getSelection( ) );
			btnInteractivity.setEnabled( btnEnable.getSelection( ) );

			if ( btnInteractivity.getSelection( ) )
			{
				detachPopup( );
			}
		}
		else if ( e.widget.equals( btnResetValue ) )
		{
			setAxisAngle( AngleType.X, -20);
			xChooser.txtRotation.setValue( -20 );
			setAxisAngle( AngleType.Y, 45);
			yChooser.txtRotation.setValue( 45 );
			setAxisAngle( AngleType.Z, 0);
			zChooser.txtRotation.setValue( 0 );
		}
	}
	
	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Refreshes the preview by model modification. Used by non-model change.
	 * 
	 */
	private void refreshPreview( )
	{
		// Populate a model changed event to refresh the preview canvas.
		boolean currentValue = getChart( ).getTitle( ).isVisible( );
		ChartAdapter.ignoreNotifications( true );
		getChart( ).getTitle( ).setVisible( !currentValue );
		ChartAdapter.ignoreNotifications( false );
		getChart( ).getTitle( ).setVisible( currentValue );
	}

	private boolean hasWallAndFloor( )
	{
		return ( getChart( ) instanceof ChartWithAxes )
		&& ( getChart( ).getDimension( ).getValue( ) != ChartDimension.TWO_DIMENSIONAL );
	}

	private class AxisRotationChooser implements
			SelectionListener,
			ModifyListener
	{

		private transient Button btnAntiRotation;

		private transient Button btnRotation;

		private transient LocalizedNumberEditorComposite txtRotation;

		private transient int angleType;

		public AxisRotationChooser( Axis axis, int angleType )
		{
			this.angleType = angleType;
		}

		public void placeComponents( Composite parent )
		{
			Composite context = new Composite( parent, SWT.NONE );
			{
				context.setLayout( new GridLayout( 3, false ) );
				context.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			}

			btnAntiRotation = new Button( context, SWT.PUSH );
			{
				GridData gd = new GridData( );
				gd.widthHint = 20;
				gd.heightHint = 20;
				btnAntiRotation.setLayoutData( gd );
				btnAntiRotation.setImage( UIHelper.getImage( getImagePath( angleType,
						true ) ) );
				btnAntiRotation.addSelectionListener( this );
			}

			btnRotation = new Button( context, SWT.PUSH );
			{
				GridData gd = new GridData( );
				gd.widthHint = 20;
				gd.heightHint = 20;
				btnRotation.setLayoutData( gd );
				btnRotation.setImage( UIHelper.getImage( getImagePath( angleType,
						false ) ) );
				btnRotation.addSelectionListener( this );
			}

			txtRotation = new LocalizedNumberEditorComposite( context,
					SWT.BORDER | SWT.SINGLE );
			{
				txtRotation.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				txtRotation.setValue( getAxisAngle( angleType ) );
				txtRotation.addModifyListener( this );
			}		
		}

		public void widgetSelected( SelectionEvent e )
		{
			if ( e.widget.equals( btnAntiRotation ) )
			{
				setAxisAngle( angleType, (int) getAxisAngle( angleType ) - 10 );
				txtRotation.setValue( getAxisAngle( angleType ) );			
			}
			else if ( e.widget.equals( btnRotation ) )
			{
				setAxisAngle( angleType, (int) getAxisAngle( angleType ) + 10 );
				txtRotation.setValue( getAxisAngle( angleType ) );
			}
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
		 */
		public void modifyText( ModifyEvent e )
		{
			if ( e.widget.equals( txtRotation ) )
			{
				setAxisAngle( angleType, (int)txtRotation.getValue( ) );
			}
		}

		private String getImagePath( int angleType, boolean bAntiRotation )
		{
			String basePath = "icons/obj16/"; //$NON-NLS-1$
			String filename = null;
			switch ( angleType )
			{
				case AngleType.X :
					filename = bAntiRotation ? "x_rotation.gif" : "x_anti_rotation.gif"; //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case AngleType.Y :
					filename = bAntiRotation ? "y_rotation.gif" : "y_anti_rotation.gif"; //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case AngleType.Z :
					filename = bAntiRotation ? "z_rotation.gif" : "z_anti_rotation.gif"; //$NON-NLS-1$ //$NON-NLS-2$
					break;
			}
			return basePath + filename;
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