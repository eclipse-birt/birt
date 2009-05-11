/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.axis;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

/**
 * "Axis" subtask. Attention: the axis layout order must be consistent with axis
 * items in the navigator tree.
 * 
 */
public class AxisSheetImpl extends SubtaskSheetImpl
{

	private static final int HORIZONTAL_SPACING = 10;
	private boolean enableAxisPercent = false ;

	public void createControl( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_AXIS );
		
		enableAxisPercent = ChartUtil.isStudyLayout( getChart() );
		
		int columnNumber = 6;
		if ( enableAxisPercent )
			columnNumber++;
		
		cmpContent = new Composite( parent, SWT.NONE ) {

			public Point computeSize( int wHint, int hHint, boolean changed )
			{
				// Return a fixed height as preferred size of scrolled composite
				Point p = super.computeSize( wHint, hHint, changed );
				p.y = 200;
				return p;
			}
		};
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

			cmpScroll.setMinHeight( ( ChartUIUtil.getOrthogonalAxisNumber( getChart( ) ) + ( ChartUIUtil.is3DType( getChart( ) ) ? 2
					: 1 ) ) * 24 + 80 );
			cmpScroll.setExpandVertical( true );
			cmpScroll.setExpandHorizontal( true );
		}

		Composite cmpList = new Composite( cmpScroll, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( columnNumber, false );
			glContent.horizontalSpacing = 10;
			cmpList.setLayout( glContent );

			cmpScroll.setContent( cmpList );
		}

		Label lblAxis = new Label( cmpList, SWT.WRAP );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblAxis.setLayoutData( gd );
			lblAxis.setFont( JFaceResources.getBannerFont( ) );
			lblAxis.setText( Messages.getString( "AxisSheetImpl.Label.Axis" ) ); //$NON-NLS-1$
		}

		Label lblType = new Label( cmpList, SWT.WRAP );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblType.setLayoutData( gd );
			lblType.setFont( JFaceResources.getBannerFont( ) );
			lblType.setText( Messages.getString( "AxisSheetImpl.Label.Type" ) ); //$NON-NLS-1$
		}

		Label lblColor = new Label( cmpList, SWT.WRAP );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblColor.setLayoutData( gd );
			lblColor.setFont( JFaceResources.getBannerFont( ) );
			lblColor.setText( Messages.getString( "AxisSheetImpl.Label.Color" ) ); //$NON-NLS-1$
		}

		Label lblVisible = new Label( cmpList, SWT.WRAP | SWT.CENTER );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblVisible.setLayoutData( gd );
			lblVisible.setFont( JFaceResources.getBannerFont( ) );
			lblVisible.setText( Messages.getString( "AxisSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
		}

		Label lblAligned = new Label( cmpList, SWT.WRAP | SWT.CENTER );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblAligned.setLayoutData( gd );
			lblAligned.setFont( JFaceResources.getBannerFont( ) );
			lblAligned.setText( Messages.getString("AxisSheetImpl.Label.Aligned") ); //$NON-NLS-1$
		}

		Label lblSideBySide = new Label( cmpList, SWT.WRAP | SWT.CENTER );
		{
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblSideBySide.setLayoutData( gd );
			lblSideBySide.setFont( JFaceResources.getBannerFont( ) );
			lblSideBySide.setText( Messages.getString("AxisSheetImpl.Label.SideBySide") ); //$NON-NLS-1$
		}
		
		if ( enableAxisPercent )
		{
			Label lblAxisPercent = new Label( cmpList, SWT.WRAP | SWT.CENTER );
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.CENTER;
			lblAxisPercent.setLayoutData( gd );
			lblAxisPercent.setFont( JFaceResources.getBannerFont( ) );
			lblAxisPercent.setText( Messages.getString("AxisSheetImpl.Label.AxisPercent") ); //$NON-NLS-1$
		}

		int treeIndex = 0;

		// Category axis.
		new AxisOptionChoser( ChartUIUtil.getAxisXForProcessing( (ChartWithAxes) getChart( ) ),
				Messages.getString( "AxisSheetImpl.Label.CategoryX" ), //$NON-NLS-1$
				AngleType.X,
				treeIndex++ ).placeComponents( cmpList );

		// Y axes.
		int yaxisNumber = ChartUIUtil.getOrthogonalAxisNumber( getChart( ) );
		for ( int i = 0; i < yaxisNumber; i++ )
		{
			String text = Messages.getString( "AxisSheetImpl.Label.ValueY" ); //$NON-NLS-1$
			new AxisOptionChoser( ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChart( ),
					i ),
					yaxisNumber == 1 ? text : ( text + " - " + ( i + 1 ) ), AngleType.Y, treeIndex++ ).placeComponents( cmpList ); //$NON-NLS-1$
		}

		// Z axis.
		if ( ChartUIUtil.is3DType( getChart( ) ) )
		{
			new AxisOptionChoser( ChartUIUtil.getAxisZForProcessing( (ChartWithAxes) getChart( ) ),
					Messages.getString( "AxisSheetImpl.Label.AncillaryZ" ), //$NON-NLS-1$
					AngleType.Z,
					treeIndex++ ).placeComponents( cmpList );
		}

	}

	private class AxisOptionChoser implements SelectionListener, Listener
	{

		private Link linkAxis;
		private Combo cmbTypes;
		private Button btnVisible;
		private FillChooserComposite cmbColor;
// private IntegerSpinControl iscRotation;
		private Axis axis;
		private String axisName;
		private int angleType;

		// Index of tree item in the navigator tree
		private int treeIndex = 0;

		private Button btnAligned;
		private Button btnSideBySide;
		
		private TextEditorComposite compAxisPercent;

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
			linkAxis = new Link( parent, SWT.NONE );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				linkAxis.setLayoutData( gd );
				linkAxis.setText( "<a>" + axisName + "</a>" ); //$NON-NLS-1$//$NON-NLS-2$
				linkAxis.addSelectionListener( this );
			}

			cmbTypes = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalAlignment = SWT.CENTER;
				cmbTypes.setLayoutData( gd );
				NameSet ns = ChartUIUtil.getCompatibleAxisType( ( (SeriesDefinition) axis.getSeriesDefinitions( )
						.get( 0 ) ).getDesignTimeSeries( ) );
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
					| SWT.READ_ONLY,
					getContext( ),
					clrCurrent,
					false,
					false,
					true,
					true );
			{
				GridData gd = new GridData( GridData.FILL_HORIZONTAL );
				cmbColor.setLayoutData( gd );
				cmbColor.addListener( this );
			}

			btnVisible = new Button( parent, SWT.CHECK );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.CENTER;
				btnVisible.setLayoutData( gd );
				btnVisible.addSelectionListener( this );
				btnVisible.setSelection( axis.getLineAttributes( ).isVisible( ) );
			}

			btnAligned = new Button( parent, SWT.CHECK );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.CENTER;
				btnAligned.setLayoutData( gd );
				btnAligned.addSelectionListener( this );
				btnAligned.setSelection( axis.isAligned( ) );
				updateBtnAlignedStatus( );
			}

			btnSideBySide = new Button( parent, SWT.CHECK );
			{
				GridData gd = new GridData( );
				gd.horizontalAlignment = SWT.CENTER;
				btnSideBySide.setLayoutData( gd );
				btnSideBySide.addSelectionListener( this );
				btnSideBySide.setSelection( axis.isSideBySide( ) );
				updateBtnSideBySidStatus( );
			}

			if ( enableAxisPercent )
			{
				if ( this.angleType == AngleType.Y )
				{
					compAxisPercent = new TextEditorComposite( parent, SWT.BORDER, 
							TextEditorComposite.TYPE_NUMBERIC ) {
						public void keyPressed( KeyEvent e )
						{
							char c = e.character;
							if ( c == '0' && "".equals( compAxisPercent.getText( ).trim( ) ) ) //$NON-NLS-1$
							{
								e.doit = false;
								return;
							}
							
							super.keyPressed( e );
						}
					};
					compAxisPercent.setDefaultValue( null );
					GridData gd = new GridData( );
					gd.horizontalAlignment = SWT.CENTER;
					gd.widthHint = 30;
					compAxisPercent.setLayoutData( gd );
					if ( axis.isSetAxisPercent( ) )
					{
						compAxisPercent.setText( String.valueOf( axis.getAxisPercent( ) ) );
					}

					compAxisPercent.addListener( this );
				}
				else
				{
					new Label( parent, SWT.NONE );
				}
			}
		}

		private void updateBtnAlignedStatus( )
		{
			btnAligned.setEnabled( ( angleType == AngleType.Y )
					&& ( axis.getType( ).getValue( ) == AxisType.LINEAR ) );
		}

		private void updateBtnSideBySidStatus( )
		{
			btnSideBySide.setEnabled( ( angleType == AngleType.Y )
					&& ( ( (SeriesDefinition) axis.getSeriesDefinitions( )
							.get( 0 ) ).getDesignTimeSeries( ) instanceof BarSeries ) );
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
				updateBtnAlignedStatus( );
			}
			else if ( e.widget.equals( linkAxis ) )
			{
				switchTo( treeIndex );
			}
			else if ( e.widget.equals( btnAligned ) )
			{
				axis.setAligned( btnAligned.getSelection( ) );
			}
			else if ( e.widget.equals( btnSideBySide ) )
			{
				axis.setSideBySide( btnSideBySide.getSelection( ) );
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
			else if ( event.widget == compAxisPercent )
			{
				try
				{
					int value = Integer.valueOf( compAxisPercent.getText( ) )
							.intValue( );
					if ( value == 0 )
					{
						compAxisPercent.setText( "" ); //$NON-NLS-1$
						axis.unsetAxisPercent( );
					}
					else
					{
						axis.setAxisPercent( value );
					}
				}
				catch ( NumberFormatException e )
				{
					compAxisPercent.setText( "" ); //$NON-NLS-1$
					axis.unsetAxisPercent( );
				}
			}
		}

		private void convertSampleData( AxisType axisType )
		{
			if ( angleType == AngleType.X )
			{
				BaseSampleData bsd = (BaseSampleData) getChart( ).getSampleData( )
						.getBaseSampleData( )
						.get( 0 );
				bsd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
						bsd.getDataSetRepresentation( ),
						0 ) );
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
								osd.getDataSetRepresentation( ),
								i ) );
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