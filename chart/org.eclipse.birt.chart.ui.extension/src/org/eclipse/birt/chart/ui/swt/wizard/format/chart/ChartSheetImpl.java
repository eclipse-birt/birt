/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainterBase;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.composites.LocalizedNumberEditorComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.BlockPropertiesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.CustomPropertiesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.GeneralPropertiesChartSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
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
import org.eclipse.swt.widgets.Spinner;

/**
 * Chart Area subtask
 * 
 */
public class ChartSheetImpl extends SubtaskSheetImpl
		implements
			SelectionListener,
			Listener
{

	private FillChooserComposite cmbBackground;

	private FillChooserComposite fccWall;

	private FillChooserComposite fccFloor;

	private Combo cmbStyle;

	private Button btnEnablePreview;

	private ExternalizedTextEditorComposite txtEmptyMsg;

	private Label lbTxtEmptyMsg;

	private Label lbFdcEmptyMsg;

	private FontDefinitionComposite fdcEmptyMsg;

	private Button btnAutoHide;

	private Button btnResetValue;

	private Button btnEnable;

	private AxisRotationChooser xChooser;

	private AxisRotationChooser yChooser;

	private AxisRotationChooser zChooser;

	private Spinner spnCorverage;

	private Button btnCoverageAuto;

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
			btnEnablePreview.setSelection( ChartPreviewPainterBase.isProcessorEnabled( ) );
			btnEnablePreview.addSelectionListener( this );
		}

		Group grpEmptyMsg = new Group( cmpBasic, SWT.NONE );
		{
			{
				grpEmptyMsg.setText( Messages.getString("ChartSheetImpl.Group.EmptyMessage") ); //$NON-NLS-1$
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalSpan = 3;
				grpEmptyMsg.setLayoutData( gd );
				grpEmptyMsg.setLayout( new GridLayout( 2, false ) );
			}

			org.eclipse.birt.chart.model.component.Label laEmptyMsg = getChart( ).getEmptyMessage( );

			btnAutoHide = new Button( grpEmptyMsg, SWT.CHECK );
			{
				btnAutoHide.setText( Messages.getString("ChartSheetImpl.Button.AutoHide") ); //$NON-NLS-1$
				GridData gd = new GridData( );
				gd.horizontalSpan = 2;
				btnAutoHide.setLayoutData( gd );
				btnAutoHide.setSelection( !laEmptyMsg.isVisible( ) );
				btnAutoHide.addListener( SWT.Selection, this );
			}

			lbTxtEmptyMsg = new Label( grpEmptyMsg, SWT.NONE );
			lbTxtEmptyMsg.setText( Messages.getString( "ChartSheetImpl.Label.Text" ) ); //$NON-NLS-1$

			List<String> keys = null;
			if ( getContext( ).getUIServiceProvider( ) != null )
			{
				keys = getContext( ).getUIServiceProvider( )
						.getRegisteredKeys( );
			}

			txtEmptyMsg = new ExternalizedTextEditorComposite( grpEmptyMsg,
					SWT.BORDER,
					-1,
					-1,
					keys,
					getContext( ).getUIServiceProvider( ),
					laEmptyMsg.getCaption( ).getValue( ) );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.widthHint = 200;
				txtEmptyMsg.setLayoutData( gd );
				txtEmptyMsg.addListener( this );
			}

			lbFdcEmptyMsg = new Label( grpEmptyMsg, SWT.NONE );
			lbFdcEmptyMsg.setText( Messages.getString( "ChartSheetImpl.Label.Font" ) ); //$NON-NLS-1$

			fdcEmptyMsg = new FontDefinitionComposite( grpEmptyMsg,
					SWT.NONE,
					getContext( ),
					laEmptyMsg.getCaption( ).getFont( ),
					laEmptyMsg.getCaption( ).getColor( ),
					true );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.widthHint = 200;
				gd.grabExcessVerticalSpace = false;
				fdcEmptyMsg.setLayoutData( gd );
				fdcEmptyMsg.addListener( this );
			}

			updateEmptyMessageUIStates( );
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
				btnResetValue.setSelection( ChartPreviewPainterBase.isProcessorEnabled( ) );
				btnResetValue.addSelectionListener( this );
			}
		}
		else if ( getChart( ) instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwa = (ChartWithoutAxes) getChart( );

			new Label( cmpBasic, SWT.NONE ).setText( Messages.getString("ChartSheetImpl.Label.Coverage") ); //$NON-NLS-1$
			
			Composite cmpCoverage = new Composite( cmpBasic, SWT.NONE );
			{
				cmpCoverage.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				GridLayout layout = new GridLayout( 2, false );
				layout.verticalSpacing = 0;
				layout.marginHeight = 0;
				layout.marginWidth = 0;
				cmpCoverage.setLayout( layout );
			}

			spnCorverage = new Spinner( cmpCoverage, SWT.BORDER );
			{
				int spnValue = (int) ( cwa.getCoverage( ) * 100 );
				spnCorverage.setValues( spnValue, 1, 100, 0, 1, 10 );
				spnCorverage.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				spnCorverage.setEnabled( cwa.isSetCoverage( ) );
				spnCorverage.addSelectionListener( this );
			}

			new Label( cmpCoverage, SWT.NONE ).setText( "%" ); //$NON-NLS-1$
			
			btnCoverageAuto = new Button( cmpBasic, SWT.CHECK );
			btnCoverageAuto.setText( Messages.getString("ChartSheetImpl.Buttom.Auto") ); //$NON-NLS-1$
			btnCoverageAuto.setSelection( !cwa.isSetCoverage( ) );
			btnCoverageAuto.addSelectionListener( this );
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
			cmp.setLayout( new GridLayout( 5, false ) );
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
				BUTTON_OUTLINE,
				Messages.getString( "ChartSheetImpl.Text.Outline&" ), //$NON-NLS-1$
				popup );
		btnBlockProp.addSelectionListener( this );

		popup = new GeneralPropertiesChartSheet( Messages.getString( "ChartSheetImpl.Text.GeneralProperties" ), //$NON-NLS-1$
				getContext( ) );
		Button btnGeneralProp = createToggleButton( cmp,
				BUTTON_GERNERAL,
				Messages.getString( "ChartSheetImpl.Text.GeneralProperties&" ), //$NON-NLS-1$
				popup );
		btnGeneralProp.addSelectionListener( this );

		popup = new CustomPropertiesSheet( Messages.getString( "ChartSheetImpl.Text.CustomProperties" ), //$NON-NLS-1$
				getContext( ) );
		Button btnCustomProp = createToggleButton( cmp,
				BUTTON_CUSTOM,
				Messages.getString( "ChartSheetImpl.Text.CustomProperties&" ), //$NON-NLS-1$
				popup );
		btnCustomProp.addSelectionListener( this );

		// Interactivity
		popup = new InteractivitySheet( Messages.getString( "ChartSheetImpl.Label.Interactivity" ), //$NON-NLS-1$
				getContext( ),
				getChart( ).getBlock( ).getTriggers( ),
				getChart( ).getBlock( ),
				TriggerSupportMatrix.TYPE_CHARTAREA,
				false,
				true );
		Button btnInteractivity = createToggleButton( cmp,
				BUTTON_INTERACTIVITY,
				Messages.getString( "SeriesYSheetImpl.Label.Interactivity&" ), //$NON-NLS-1$
				popup,
				getChart( ).getInteractivity( ).isEnable( ) );
		btnInteractivity.addSelectionListener( this );
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
		else if ( event.widget == txtEmptyMsg )
		{
			getChart( ).getEmptyMessage( )
					.getCaption( )
					.setValue( txtEmptyMsg.getText( ) );
		}
		else if ( event.widget == btnAutoHide )
		{
			getChart( ).getEmptyMessage( )
					.setVisible( !btnAutoHide.getSelection( ) );
			updateEmptyMessageUIStates( );
		}
		else if ( event.widget == fdcEmptyMsg )
		{
			Text caption = getChart( ).getEmptyMessage( ).getCaption( );
			caption.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
			caption.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
		}
	}

	private void updateEmptyMessageUIStates( )
	{
		boolean bEnabled = getChart( ).getEmptyMessage( ).isVisible( );
		txtEmptyMsg.setEnabled( bEnabled );
		fdcEmptyMsg.setEnabled( bEnabled );
		lbTxtEmptyMsg.setEnabled( bEnabled );
		lbFdcEmptyMsg.setEnabled( bEnabled );
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
			attachPopup( ( (Button) e.widget ).getData( ).toString( ) );
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
			ChartPreviewPainterBase.enableProcessor( btnEnablePreview.getSelection( ) );
			refreshPreview( );
		}
		else if ( e.widget.equals( btnEnable ) )
		{
			getChart( ).getInteractivity( )
					.setEnable( btnEnable.getSelection( ) );
			setToggleButtonEnabled( BUTTON_INTERACTIVITY,
					btnEnable.getSelection( ) );

			if ( getToggleButton( BUTTON_INTERACTIVITY ).getSelection( ) )
			{
				detachPopup( );
			}
		}
		else if ( e.widget.equals( btnResetValue ) )
		{
			setAxisAngle( AngleType.X, -20 );
			xChooser.txtRotation.setValue( -20 );
			setAxisAngle( AngleType.Y, 45 );
			yChooser.txtRotation.setValue( 45 );
			setAxisAngle( AngleType.Z, 0 );
			zChooser.txtRotation.setValue( 0 );
		}
		else if ( e.widget == btnCoverageAuto )
		{
			if ( getChart( ) instanceof ChartWithoutAxes )
			{
				ChartWithoutAxes cwa = (ChartWithoutAxes) getChart( );
				spnCorverage.setEnabled( !btnCoverageAuto.getSelection( ) );
				if ( btnCoverageAuto.getSelection( ) )
				{
					cwa.unsetCoverage( );
				}
				else
				{
					cwa.setCoverage( spnCorverage.getSelection( ) / 100d );
				}
			}
		}
		else if ( e.widget == spnCorverage )
		{
			if ( getChart( ) instanceof ChartWithoutAxes )
			{
				ChartWithoutAxes cwa = (ChartWithoutAxes) getChart( );
				cwa.setCoverage( spnCorverage.getSelection( ) / 100d );
			}
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

	private class AxisRotationChooser
			implements
				SelectionListener,
				ModifyListener
	{

		private Button btnAntiRotation;

		private Button btnRotation;

		private LocalizedNumberEditorComposite txtRotation;

		private int angleType;

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
				ChartUIUtil.setChartImageButtonSizeByPlatform( gd );
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
				setAxisAngle( angleType, (int) txtRotation.getValue( ) );
			}
		}

		private String getImagePath( int angleType, boolean bAntiRotation )
		{
			String basePath = "icons/obj16/"; //$NON-NLS-1$
			String filename = null;
			switch ( angleType )
			{
				case AngleType.X :
					filename = bAntiRotation
							? "x_rotation.gif" : "x_anti_rotation.gif"; //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case AngleType.Y :
					filename = bAntiRotation
							? "y_anti_rotation.gif" : "y_rotation.gif"; //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case AngleType.Z :
					filename = bAntiRotation
							? "z_rotation.gif" : "z_anti_rotation.gif"; //$NON-NLS-1$ //$NON-NLS-2$
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
		return ( (ChartWithAxes) getChart( ) ).getRotation( )
				.getAngles( )
				.get( 0 );
	}

}