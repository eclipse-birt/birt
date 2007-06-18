/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.samplesview.view;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.samplereports.description.DescriptionMessages;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.ISampleReportEntry;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.SampleIncludedSourceEntry;
import org.eclipse.birt.report.designer.ui.samplesview.util.SampleReportCanvas;
import org.eclipse.birt.report.designer.ui.samplesview.util.SampleReportsExplorerProvider;
import org.eclipse.birt.report.designer.ui.samplesview.util.SampleReportsSorter;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class ReportExamples
{

	private static final String CONTRIBUTION_ITEM_TEXT = Messages.getString( "SampleReportsView.Text.Contribute_Samples" );
	
	private Composite mainComposite;

	private SampleReportCanvas previewCanvas;

	private TreeViewer samplesTree;

	private Text description;

	private Object selectedElement;

	private ISampleReportEntry sampleEntry;

	public ReportExamples( Composite parent )
	{
		mainComposite = parent;
		createControl( );
	}

	public Shell getShell( )
	{
		return this.mainComposite.getShell( );
	}

	private void createControl( )
	{
		mainComposite.setLayout( new FillLayout( ) );
		SashForm hSash = new SashForm( mainComposite, SWT.HORIZONTAL
				| SWT.SMOOTH );
		createSamplesGroup( hSash );
		createDisplayGroup( hSash );
	}

	private void createSamplesGroup( SashForm hSash )
	{
		Group samplesGroup = new Group( hSash, SWT.NONE );
		samplesGroup.setText( Messages.getString( "SampleReportsView.Text.Explorer" ) );
		samplesGroup.setLayout( new GridLayout( 1, false ) );

		createSamplesExplorer( samplesGroup );
	}

	private void createDisplayGroup( SashForm hSash )
	{
		Group displayGroup = new Group( hSash, SWT.NONE );
		displayGroup.setLayout( new FillLayout( ) );
		SashForm vSash = new SashForm( displayGroup, SWT.VERTICAL | SWT.SMOOTH );

		createPreviewGroup( vSash );
		createDescriptionGroup( vSash );

		hSash.setWeights( new int[]{
				2, 3
		} );
	}

	private void createSamplesExplorer( Composite samplesGroup )
	{
		samplesTree = new TreeViewer( samplesGroup, SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL );

		samplesTree.getTree( )
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
						| GridData.FILL_VERTICAL ) );

		configTreeViewer( );
		refreshRoot( );
	}

	private void createPreviewGroup( SashForm vSash )
	{
		Group previewGroup = new Group( vSash, SWT.NONE );
		previewGroup.setLayout( new GridLayout( ) );
		previewGroup.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		previewGroup.setText( Messages.getString( "SampleReportsView.Text.Preview" ) );

		previewCanvas = new SampleReportCanvas( previewGroup, SWT.BORDER );
		previewCanvas.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		previewCanvas.setBackground( Display.getDefault( )
				.getSystemColor( SWT.COLOR_WHITE ) );

	}

	private void createDescriptionGroup( SashForm vSash )
	{
		Group descriptionGroup = new Group( vSash, SWT.NONE );
		descriptionGroup.setLayout( new GridLayout( ) );
		descriptionGroup.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		descriptionGroup.setText( Messages.getString( "SampleReportsView.Text.Description" ) );

		vSash.setWeights( new int[]{
				8, 1
		} );

		description = new Text( descriptionGroup, SWT.WRAP
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.READ_ONLY );
		description.setLayoutData( new GridData( GridData.FILL_VERTICAL
				| GridData.FILL_HORIZONTAL ) );
		description.setText( Messages.getString( "SampleReportsView.defautDescription" ) );
	}

	private void setSelectedElement( Object element )
	{
		this.selectedElement = element;
	}

	public Object getSelectedElement( )
	{
		return this.selectedElement;
	}

	private void configTreeViewer( )
	{
		ViewsTreeProvider provider = new SampleReportsExplorerProvider( );
		samplesTree.setContentProvider( provider );
		samplesTree.setLabelProvider( provider );
		samplesTree.getTree( ).addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				TreeItem item = (TreeItem) e.item;
				if ( item != null )
				{
					if ( item.getData( ) instanceof ReportDesignHandle )
					{
						String reportname = item.getText( ).substring( 0,
								item.getText( ).lastIndexOf( "." ) );
						description.setText( DescriptionMessages.getDescription( reportname ) );
						previewSampleReport( reportname );
						setSelectedElement( item );
					}
					else if ( item.getText( ).equals( CONTRIBUTION_ITEM_TEXT ) )
					{
						description.setText( DescriptionMessages.getDescription( "Contribute_Samples" ) );
						setSelectedElement( item );
					}
					else
					{
						description.setText( Messages.getString( "SampleReportsView.defautDescription" ) );
						setSelectedElement( item );
					}
				}
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}
		} );

		samplesTree.getTree( ).addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				Object input = samplesTree.getInput( );
				if ( input instanceof Object[] )
				{
					Object[] array = (Object[]) input;
					for ( int i = 0; i < array.length; i++ )
					{
						if ( array[i] instanceof ResourceEntry )
							( (ResourceEntry) array[i] ).dispose( );
					}
				}
			}
		} );

		samplesTree.setSorter( new SampleReportsSorter( ) );
	}

	private void previewSampleReport( String name )
	{

		InputStream imageStream = null;
		try
		{
			URL imagePath = SampleIncludedSourceEntry.getImagePath( name );
			if ( imagePath != null )
			{
				imageStream = imagePath.openStream( );
			}
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
		}
		if ( imageStream != null )
		{
			Image sampleImage = new Image( Display.getDefault( ), imageStream );
			previewCanvas.setSampleImage( sampleImage );
			previewCanvas.redraw( );
		}
	}

	private void refreshRoot( )
	{
		ResourceEntry contributionItem = new ResourceEntry( ) {

			public String getName( )
			{
				return CONTRIBUTION_ITEM_TEXT;
			}

			public String getDisplayName( )
			{
				return CONTRIBUTION_ITEM_TEXT;
			}

			public Image getImage( )
			{
				return PlatformUI.getWorkbench( )
						.getSharedImages( )
						.getImage( ISharedImages.IMG_OBJ_FILE );
			}

			public URL getURL( )
			{
				return null;
			}

			public ResourceEntry getParent( )
			{
				return null;
			}

			public ResourceEntry[] getChildren( )
			{
				return new ResourceEntry[0];
			}

			public ResourceEntry[] getChildren( Filter filter )
			{
				return new ResourceEntry[0];
			}

			public boolean isFile( )
			{
				return false;
			}

			public boolean isRoot( )
			{
				return false;
			}

			public void dispose( )
			{

			}

			public Object getAdapter( Class adapter )
			{
				return null;
			}
		};

		Object adapter = null;
		int status = Platform.getAdapterManager( ).queryAdapter( this,
				ISampleReportEntry.class.getName( ) );

		if ( status == IAdapterManager.LOADED )
		{
			adapter = Platform.getAdapterManager( ).getAdapter( this,
					ISampleReportEntry.class.getName( ) );
		}
		else if ( status == IAdapterManager.NOT_LOADED )
		{
			// Cause the plug-in loading first
			adapter = Platform.getAdapterManager( ).loadAdapter( this,
					ISampleReportEntry.class.getName( ) );
		}

		if ( adapter == null )
		{
			return;
		}

		sampleEntry = (ISampleReportEntry) adapter;
		if ( samplesTree != null && !samplesTree.getTree( ).isDisposed( ) )
		{
			int length = sampleEntry.getEntries( ).length;
			ResourceEntry[] sampleEntries = sampleEntry.getEntries( );
			ResourceEntry[] entries = new ResourceEntry[length + 1];
			for ( int i = 0; i < length; i++ )
			{
				entries[i] = sampleEntries[i];
			}
			entries[length] = contributionItem;
			samplesTree.setInput( entries );
			samplesTree.expandToLevel( 2 );
		}
	}

	public void setFocus( )
	{
		mainComposite.setFocus( );
	}

	public void dispose( )
	{
		if ( samplesTree != null
				&& samplesTree.getTree( ) != null
				&& !samplesTree.getTree( ).isDisposed( ) )
		{
			samplesTree.getTree( ).dispose( );
			samplesTree = null;
		}
		if ( previewCanvas != null && !previewCanvas.isDisposed( ) )
		{
			previewCanvas.dispose( );
			previewCanvas = null;
		}
		if ( description != null && !description.isDisposed( ) )
		{
			description.dispose( );
			description = null;
		}
	}
}
