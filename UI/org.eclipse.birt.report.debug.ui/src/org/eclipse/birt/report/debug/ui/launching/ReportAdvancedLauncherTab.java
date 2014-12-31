/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.ui.launching;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.debug.internal.ui.launcher.IReportLauncherSettings;
import org.eclipse.birt.report.debug.internal.ui.launcher.util.ReportLauncherUtils;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.plugin.WorkspacePluginModelBase;
import org.eclipse.pde.internal.ui.PDELabelProvider;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.PDEPluginImages;
import org.eclipse.pde.internal.ui.elements.DefaultContentProvider;
import org.eclipse.pde.internal.ui.elements.NamedElement;
import org.eclipse.pde.internal.ui.util.SWTUtil;
import org.eclipse.pde.internal.ui.wizards.ListUtil;
import org.eclipse.pde.ui.launcher.AbstractLauncherTab;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * ReportAdvancedLauncherTab
 * 
 * @deprecated
 */
public class ReportAdvancedLauncherTab extends AbstractLauncherTab implements
		ILaunchConfigurationTab,
		IReportLauncherSettings
{

	protected static final Logger logger = Logger.getLogger( ReportAdvancedLauncherTab.class.getName( ) );
	private static final String REPORTPROJECTKID = "org.eclipse.birt.report.designer.ui.reportprojectnature"; //$NON-NLS-1$	
	private Label fUseListRadio;
	private CheckboxTreeViewer fPluginTreeViewer;
	private Label fVisibleLabel;
	private NamedElement[] fWorkspacePlugins;
	private IProject fWorkspaceBIRTModels[];
	private IProject fWorkspaceJavaModels[];
	private Button fDefaultsButton;
	private int fNumExternalChecked;
	private int fNumWorkspaceChecked;
	private int fNumWorkspaceBIRTChecked;
	private int fNumWorkspaceJavaChecked;
	private Image fImage;
	private boolean fShowFeatures;
	private Button fSelectAllButton;
	private Button fDeselectButton;

	class PluginContentProvider extends DefaultContentProvider implements
			ITreeContentProvider
	{

		PluginContentProvider( )
		{
			super( );
		}

		public boolean hasChildren( Object parent )
		{
			return !( parent instanceof IProject );
		}

		public Object[] getChildren( Object parent )
		{
			try
			{
				if ( parent == fWorkspacePlugins[0] )
					return fWorkspaceBIRTModels;
				else if ( parent == fWorkspacePlugins[1] )
					return fWorkspaceJavaModels;
				else if ( parent instanceof IProject
						&& ( (IProject) parent ).hasNature( REPORTPROJECTKID ) )
				{
					List retValue = getReportDesignFileFromProject( (IProject) parent );
					return (Object[]) retValue.toArray( new Object[retValue.size( )] );

				}
			}
			catch ( CoreException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}

			return new Object[0];
		}

		public Object getParent( Object child )
		{
			return null;
		}

		public Object[] getElements( Object input )
		{
			// return ( new Object[]{fWorkspacePlugins} );
			return fWorkspacePlugins;
		}

	}

	private List getReportDesignFileFromProject( IProject project )
	{
		List retValue = new ArrayList( );
		try
		{
			IResource[] resources = project.members( );
			if ( resources != null && resources.length > 0 )
			{
				List extesionNameList = ReportPlugin.getDefault( )
						.getReportExtensionNameList( );
				for ( int i = 0; i < resources.length; i++ )
				{
					IResource resource = resources[i];
					// System.out.println(resource.getFullPath(
					// ).toPortableString( ));
					if ( extesionNameList.contains( resource.getFileExtension( ) ) )
					{
						retValue.add( resource );
					}
				}
			}
		}
		catch ( CoreException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		return retValue;
	}

	public ReportAdvancedLauncherTab( )
	{
		this( true );
	}

	/**
	 * @param showFeatures
	 */
	public ReportAdvancedLauncherTab( boolean showFeatures )
	{
		fNumExternalChecked = 0;
		fNumWorkspaceChecked = 0;
		fShowFeatures = showFeatures;
		PDEPlugin.getDefault( ).getLabelProvider( ).connect( this );
		fImage = PDEPluginImages.DESC_REQ_PLUGINS_OBJ.createImage( );
		fWorkspaceBIRTModels = getInterestProject( REPORTPROJECTKID );
		fWorkspaceJavaModels = getInterestProject( JavaCore.NATURE_ID );
	}

	private IProject[] getInterestProject( String type )
	{
		List retValue = new ArrayList( );
		IProject[] allProjects = ResourcesPlugin.getWorkspace( )
				.getRoot( )
				.getProjects( );
		if ( allProjects == null )
		{
			return new IProject[]{};
		}
		int len = allProjects.length;
		for ( int i = 0; i < len; i++ )
		{
			IProject project = allProjects[i];
			try
			{
				if ( project.hasNature( type ) )
				{
					retValue.add( project );
				}
			}
			catch ( CoreException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}
		}
		IProject[] temp = new IProject[retValue.size( )];
		temp = (IProject[]) retValue.toArray( temp );
		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#dispose()
	 */
	public void dispose( )
	{
		PDEPlugin.getDefault( ).getLabelProvider( ).disconnect( this );
		fImage.dispose( );
		super.dispose( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl( Composite parent )
	{
		Composite composite = new Composite( parent, 0 );
		composite.setLayout( new GridLayout( ) );
		fUseListRadio = new Label( composite, 16 );
		fUseListRadio.setText( Messages.getString( "ReportAdvancedLauncherTab.UseListRadioText" ) ); //$NON-NLS-1$
		createPluginList( composite );
		hookListeners( );
		setControl( composite );
		Dialog.applyDialogFont( composite );
		UIUtil.bindHelp( composite,
				"org.eclipse.pde.doc.user.launcher_advanced" ); //$NON-NLS-1$
	}

	/**
	 * 
	 */
	private void hookListeners( )
	{
//		SelectionAdapter adapter = new SelectionAdapter( ) {
//
//			public void widgetSelected( SelectionEvent e )
//			{
//				useDefaultChanged( );
//			}
//		};

		fDefaultsButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				computeInitialCheckState( );
				updateStatus( );
			}
		} );

		fSelectAllButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				toggleGroups( true );
				updateStatus( );
			}
		} );

		fDeselectButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				toggleGroups( false );
				updateStatus( );
			}
		} );

	}

	/**
	 * @param select
	 */
	protected void toggleGroups( boolean select )
	{
		for ( int i = 0; i < fWorkspacePlugins.length; i++ )
		{
			handleGroupStateChanged( fWorkspacePlugins[i], select );
		}

	}

	private void useDefaultChanged( )
	{
		adjustCustomControlEnableState( true );
		updateStatus( );
	}

	/**
	 * @param enable
	 */
	private void adjustCustomControlEnableState( boolean enable )
	{
		fVisibleLabel.setVisible( enable );
		fPluginTreeViewer.getTree( ).setVisible( enable );
		fDefaultsButton.setVisible( enable );
		fSelectAllButton.setVisible( enable );
		fDeselectButton.setVisible( enable );
	}

	/**
	 * @param parent
	 */
	private void createPluginList( Composite parent )
	{
		Composite composite = new Composite( parent, 0 );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( 1808 ) );
		fVisibleLabel = new Label( composite, 0 );
		GridData gd = new GridData( );
		gd.horizontalSpan = 2;
		fVisibleLabel.setLayoutData( gd );
		fVisibleLabel.setText( Messages.getString( "ReportAdvancedLauncherTab.VisibleLabelText" ) ); //$NON-NLS-1$
		createPluginViewer( composite );
		createButtonContainer( composite );
	}

	private void computeSubset( )
	{
		Object checked[] = fPluginTreeViewer.getCheckedElements( );
		TreeMap map = new TreeMap( );
		for ( int i = 0; i < checked.length; i++ )
			if ( checked[i] instanceof IProject )
			{
				IProject model = (IProject) checked[i];
				addPluginAndDependencies( model, map );
			}

		checked = map.values( ).toArray( );
		fPluginTreeViewer.setCheckedElements( map.values( ).toArray( ) );
		fNumExternalChecked = 0;
		fNumWorkspaceBIRTChecked = 0;
		fNumWorkspaceJavaChecked = 0;
		fNumWorkspaceChecked = 0;
		for ( int i = 0; i < checked.length; i++ )
			if ( checked[i] instanceof WorkspacePluginModelBase )
			{
				if ( checked[i] instanceof IProject )
				{
					try
					{
						if ( ( (IProject) checked[i] ).hasNature( REPORTPROJECTKID ) )
						{
							fNumWorkspaceBIRTChecked++;
						}
						else if ( ( (IProject) checked[i] ).hasNature( JavaCore.NATURE_ID ) )
						{
							fNumWorkspaceJavaChecked++;
						}
					}
					catch ( CoreException e )
					{
						logger.log( Level.SEVERE, e.getMessage( ), e );
					}

				}
			}
			else
			{
				fNumExternalChecked++;
			}

		fNumWorkspaceChecked = fNumWorkspaceBIRTChecked
				+ fNumWorkspaceJavaChecked;
		adjustGroupState( );
	}

	private void addPluginAndDependencies( IProject model, TreeMap map )
	{
		if ( model == null )
			return;
		String id = model.getName( );
		if ( map.containsKey( id ) )
		{
			return;
		}
		else
		{
			map.put( id, model );
			return;
		}
	}

	private void adjustGroupState( )
	{

		int size = fWorkspaceBIRTModels.length;
		for ( int i = 0; i < size; i++ )
		{
			IProject project = fWorkspaceBIRTModels[i];
			List list = getReportDesignFileFromProject( project );
			int len = list.size( );
			int ori = 0;
			for ( int j = 0; j < len; j++ )
			{
				Object obj = list.get( j );
				if ( fPluginTreeViewer.getChecked( obj ) )
				{
					ori++;
				}
			}
			if ( ori > 0 )
			{
				if ( !fPluginTreeViewer.getChecked( project ) )
				{
					fNumWorkspaceBIRTChecked++;
					fPluginTreeViewer.setChecked( project, true );
				}
			}
			fPluginTreeViewer.setGrayed( project, ori > 0 && ori < len );
		}
		fPluginTreeViewer.setChecked( fWorkspacePlugins[0],
				fNumWorkspaceBIRTChecked > 0 );

		fPluginTreeViewer.setChecked( fWorkspacePlugins[1],
				fNumWorkspaceJavaChecked > 0 );

		fPluginTreeViewer.setGrayed( fWorkspacePlugins[0],
				fNumWorkspaceBIRTChecked > 0
						&& fNumWorkspaceBIRTChecked < fWorkspaceBIRTModels.length );

		fPluginTreeViewer.setGrayed( fWorkspacePlugins[1],
				fNumWorkspaceJavaChecked > 0
						&& fNumWorkspaceJavaChecked < fWorkspaceJavaModels.length );

	}

	/**
	 * @param composite
	 */
	private void createPluginViewer( Composite composite )
	{
		fPluginTreeViewer = new CheckboxTreeViewer( composite, 2048 );
		fPluginTreeViewer.setContentProvider( new PluginContentProvider( ) );
		fPluginTreeViewer.setLabelProvider( new PDELabelProvider( ) {

			public String getText( Object obj )
			{
				if ( obj instanceof IProject )
				{
					return ( (IProject) obj ).getName( );
				}
				if ( obj instanceof IResource )
				{
					return ( (IResource) obj ).getName( );
				}
				return super.getText( obj );
			}
		} );
		fPluginTreeViewer.setAutoExpandLevel( 2 );
		fPluginTreeViewer.addCheckStateListener( new ICheckStateListener( ) {

			public void checkStateChanged( final CheckStateChangedEvent event )
			{
				Object element = event.getElement( );
				if ( element instanceof IPluginModelBase )
				{
					handleCheckStateChanged( (IPluginModelBase) element,
							event.getChecked( ) );
				}
				else
				{
					handleGroupStateChanged( element, event.getChecked( ) );
				}
				updateLaunchConfigurationDialog( );
			}
		} );
		// fPluginTreeViewer.setSorter( new ListUtil.PluginSorter( ) {
		//
		// public int category( Object obj )
		// {
		// for ( int i = 0; i < fWorkspacePlugins.length; i++ )
		// {
		//
		// if ( obj == fWorkspacePlugins[i] )
		// return -1;
		// }
		// return 0;
		// }
		// } );
		fPluginTreeViewer.setComparator( new ListUtil.PluginComparator( ) {

			public int category( Object obj )
			{
				for ( int i = 0; i < fWorkspacePlugins.length; i++ )
				{

					if ( obj == fWorkspacePlugins[i] )
						return -1;
				}
				return 0;
			}
		} );
		fPluginTreeViewer.getTree( ).setLayoutData( new GridData( 1808 ) );
		Image pluginsImage = PDEPlugin.getDefault( )
				.getLabelProvider( )
				.get( PDEPluginImages.DESC_REQ_PLUGINS_OBJ );

		fWorkspacePlugins = new NamedElement[]{
				new NamedElement( Messages.getString( "ReportAdvancedLauncherTab.WorkspaceReportProjects" ),
						pluginsImage ),
				new NamedElement( Messages.getString( "ReportClasspathLaucnTab.WorkspaceJavaProjects" ),
						pluginsImage )
		};
	}

	/**
	 * @param parent
	 */
	private void createButtonContainer( Composite parent )
	{
		Composite composite = new Composite( parent, 0 );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = layout.marginWidth = 0;
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( 1040 ) );
		fSelectAllButton = new Button( composite, 8 );
		fSelectAllButton.setText( Messages.getString( "ReportAdvancedLauncherTab.SelectAllButtonText" ) ); //$NON-NLS-1$
		fSelectAllButton.setLayoutData( new GridData( 770 ) );
		SWTUtil.setButtonDimensionHint( fSelectAllButton );
		fDeselectButton = new Button( composite, 8 );
		fDeselectButton.setText( Messages.getString( "ReportAdvancedLauncherTab.DeselectButtonText" ) ); //$NON-NLS-1$
		fDeselectButton.setLayoutData( new GridData( 768 ) );
		SWTUtil.setButtonDimensionHint( fDeselectButton );
		fDefaultsButton = new Button( composite, 8 );
		fDefaultsButton.setText( Messages.getString( "ReportAdvancedLauncherTab.DefaultsButtonText" ) ); //$NON-NLS-1$
		fDefaultsButton.setLayoutData( new GridData( 768 ) );
		SWTUtil.setButtonDimensionHint( fDefaultsButton );
	}

	private void initWorkspacePluginsState( ILaunchConfiguration config )
			throws CoreException
	{
		fNumWorkspaceBIRTChecked = fWorkspaceBIRTModels.length;
		fNumWorkspaceJavaChecked = fWorkspaceJavaModels.length;

		for ( int i = 0; i < fWorkspacePlugins.length; i++ )
		{
			fPluginTreeViewer.setSubtreeChecked( fWorkspacePlugins[i], true );
		}

		TreeSet deselected = ReportLauncherUtils.parseDeselectedWSIds( config );
		for ( int i = 0; i < fWorkspaceBIRTModels.length; i++ )
		{
			if ( !deselected.contains( fWorkspaceBIRTModels[i].getName( ) ) )
			{
				if ( fPluginTreeViewer.setChecked( fWorkspaceBIRTModels[i],
						false ) )
				{
					fNumWorkspaceBIRTChecked--;
				}
			}
		}

		for ( int i = 0; i < fWorkspaceJavaModels.length; i++ )
		{
			if ( !deselected.contains( fWorkspaceJavaModels[i].getName( ) ) )
			{
				if ( fPluginTreeViewer.setChecked( fWorkspaceJavaModels[i],
						false ) )
				{
					fNumWorkspaceJavaChecked--;
				}
			}
		}

		fNumWorkspaceChecked = fNumWorkspaceBIRTChecked
				+ fNumWorkspaceJavaChecked;
		int[] checked = new int[]{
				fNumWorkspaceBIRTChecked, fNumWorkspaceJavaChecked
		};
		int[] length = new int[]{
				fWorkspaceBIRTModels.length, fWorkspaceJavaModels.length
		};
		for ( int i = 0; i < fWorkspacePlugins.length; i++ )
		{
			if ( checked[i] == 0 )
			{
				fPluginTreeViewer.setChecked( fWorkspacePlugins[i], false );
			}
			fPluginTreeViewer.setGrayed( fWorkspacePlugins[i], checked[i] > 0
					&& checked[i] < length[i] );
		}

		int size = fWorkspaceBIRTModels.length;
		deselected = ReportLauncherUtils.parseDeselectedOpenFileNames( config );
		for ( int i = 0; i < size; i++ )
		{
			IProject project = fWorkspaceBIRTModels[i];
			List list = getReportDesignFileFromProject( project );
			int len = list.size( );
			int ori = len;
			for ( int j = 0; j < len; j++ )
			{
				IResource resource = (IResource) list.get( j );
				String path = resource.getFullPath( ).toString( );
				if ( !deselected.contains( path ) )
				{
					if ( fPluginTreeViewer.setChecked( resource, false ) )
					{
						ori--;
					}
				}

			}
			if ( ori > 0 )
			{
				fPluginTreeViewer.setChecked( project, true );
			}
			fPluginTreeViewer.setGrayed( project, ori > 0 && ori < len );
		}
	}

	/**
	 * @param config
	 * @throws CoreException
	 */
	private void initExternalPluginsState( ILaunchConfiguration config )
			throws CoreException
	{
		fNumExternalChecked = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom( ILaunchConfiguration config )
	{
		try
		{
			if ( fPluginTreeViewer.getInput( ) == null )
			{
				fPluginTreeViewer.setUseHashlookup( true );
				fPluginTreeViewer.setInput( PDEPlugin.getDefault( ) );
				fPluginTreeViewer.reveal( fWorkspacePlugins );
			}
			initWorkspacePluginsState( config );
			initExternalPluginsState( config );
		}
		catch ( CoreException e )
		{
			PDEPlugin.logException( e );
		}
		adjustCustomControlEnableState( true );
		updateStatus( );
	}

	private void computeInitialCheckState( )
	{
		TreeSet wtable = new TreeSet( );
		fNumWorkspaceChecked = 0;
		fNumExternalChecked = 0;

		fNumWorkspaceBIRTChecked = 0;
		fNumWorkspaceJavaChecked = 0;

		for ( int i = 0; i < fWorkspaceBIRTModels.length; i++ )
		{
			IProject model = fWorkspaceBIRTModels[i];
			fNumWorkspaceBIRTChecked++;
			String id = model.getName( );
			if ( id != null )
				wtable.add( model.getName( ) );
		}

		for ( int i = 0; i < fWorkspaceJavaModels.length; i++ )
		{
			IProject model = fWorkspaceJavaModels[i];
			fNumWorkspaceJavaChecked++;
			String id = model.getName( );
			if ( id != null )
				wtable.add( model.getName( ) );
		}

		fNumWorkspaceChecked = fNumWorkspaceBIRTChecked
				+ fNumWorkspaceJavaChecked;

		for ( int i = 0; i < fWorkspacePlugins.length; i++ )
		{
			fPluginTreeViewer.setSubtreeChecked( fWorkspacePlugins[i], true );
		}

		adjustGroupState( );
	}

	/**
	 * @param model
	 * @param checked
	 */
	// should update later
	private void handleCheckStateChanged( IPluginModelBase model,
			boolean checked )
	{
		if ( model.getUnderlyingResource( ) == null )
		{
			if ( checked )
			{
				fNumExternalChecked += 1;
			}
			else
			{
				fNumExternalChecked -= 1;
			}
		}
		else
		{
			if ( model instanceof IProject )
			{
				try
				{
					if ( ( (IProject) model ).hasNature( REPORTPROJECTKID ) )
					{
						fNumWorkspaceBIRTChecked += checked ? 1 : -1;
					}
					else if ( ( (IProject) model ).hasNature( JavaCore.NATURE_ID ) )
					{
						fNumWorkspaceJavaChecked += checked ? 1 : -1;
					}
				}
				catch ( CoreException e )
				{
					logger.log( Level.SEVERE, e.getMessage( ), e );
				}

				fNumWorkspaceChecked = fNumWorkspaceBIRTChecked
						+ fNumWorkspaceJavaChecked;

			}
		}
		adjustGroupState( );
	}

	/**
	 * @param group
	 * @param checked
	 */
	private void handleGroupStateChanged( Object group, boolean checked )
	{
		fPluginTreeViewer.setSubtreeChecked( group, checked );
		fPluginTreeViewer.setGrayed( group, false );
		if ( group == fWorkspacePlugins[0] )
		{
			fNumWorkspaceBIRTChecked = checked ? fWorkspaceBIRTModels.length
					: 0;

		}
		if ( group == fWorkspacePlugins[1] )
		{
			fNumWorkspaceJavaChecked = checked ? fWorkspaceJavaModels.length
					: 0;
		}
		if ( group instanceof IProject )
		{
			try
			{
				if ( ( (IProject) group ).hasNature( REPORTPROJECTKID ) )
				{
					fNumWorkspaceBIRTChecked += checked ? 1 : -1;
				}
				else if ( ( (IProject) group ).hasNature( JavaCore.NATURE_ID ) )
				{
					fNumWorkspaceJavaChecked += checked ? 1 : -1;
				}
			}
			catch ( CoreException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}

			fNumWorkspaceChecked = fNumWorkspaceBIRTChecked
					+ fNumWorkspaceJavaChecked;

		}
		adjustGroupState( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults( ILaunchConfigurationWorkingCopy config )
	{
		if ( fShowFeatures )
		{
			config.setAttribute( "default", true ); //$NON-NLS-1$
			config.setAttribute( "usefeatures", false ); //$NON-NLS-1$
		}
		else
		{
			config.setAttribute( "default", true ); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	// here should be updated later
	public void performApply( ILaunchConfigurationWorkingCopy config )
	{
		StringBuffer wbuf = new StringBuffer( );
		int size = fWorkspaceBIRTModels.length;

		for ( int i = 0; i < size; i++ )
		{
			IProject model = fWorkspaceBIRTModels[i];
			String path = model.getLocation( ).toOSString( );
			if ( fPluginTreeViewer.getChecked( model ) )
				wbuf.append( PROPERTYSEPARATOR + path );

		}

		for ( int i = 0; i < fWorkspaceJavaModels.length; i++ )
		{
			IProject model = fWorkspaceJavaModels[i];
			String path = model.getLocation( ).toOSString( );
			if ( fPluginTreeViewer.getChecked( model ) )
				wbuf.append( PROPERTYSEPARATOR + path );

		}
		config.setAttribute( IMPORTPROJECT, wbuf.toString( ) );

		wbuf = new StringBuffer( );
		for ( int i = 0; i < size; i++ )
		{
			IProject project = fWorkspaceBIRTModels[i];
			List list = getReportDesignFileFromProject( project );
			for ( int j = 0; j < list.size( ); j++ )
			{
				IResource resource = (IResource) list.get( j );
				String path = resource.getFullPath( ).toString( );
				if ( fPluginTreeViewer.getChecked( resource ) )
				{
					wbuf.append( PROPERTYSEPARATOR + path );
				}
			}
		}

		config.setAttribute( OPENFILENAMES, wbuf.toString( ) );
		config.setAttribute( "clearws", true ); //$NON-NLS-1$

		config.setAttribute( "askclear", false ); //$NON-NLS-1$
		config.setAttribute( "location0", WORKESPACENAME ); //$NON-NLS-1$

	}

	private void updateStatus( )
	{
		updateStatus( validate( ) );
	}

	private IStatus validate( )
	{
		return createStatus( 0, "" ); //$NON-NLS-1$
	}

	protected void updateStatus( IStatus status )
	{
		applyToStatusLine( status );
	}

	/**
	 * Applies the status to a dialog page
	 */
	public void applyToStatusLine( IStatus status )
	{
		String errorMessage = null;
		String warningMessage = null;
		String statusMessage = status.getMessage( );
		if ( statusMessage.length( ) > 0 )
		{
			if ( status.matches( IStatus.ERROR ) )
			{
				errorMessage = statusMessage;
			}
			else if ( !status.isOK( ) )
			{
				warningMessage = statusMessage;
			}
		}

		setErrorMessage( errorMessage );
		setMessage( warningMessage );
		updateLaunchConfigurationDialog( );
	}

	public static IStatus getMoreSevere( IStatus s1, IStatus s2 )
	{
		return ( s1.getSeverity( ) >= s2.getSeverity( ) ) ? s1 : s2;
	}

	public static IStatus createStatus( int severity, String message )
	{
		return new Status( severity,
				PDEPlugin.getPluginId( ),
				severity,
				message,
				null );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName( )
	{
		return Messages.getString( "ReportAdvancedLauncherTab.Name" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getImage()
	 */
	public Image getImage( )
	{
		return fImage;
	}

	public void validateTab( )
	{
	}
}