package org.eclipse.birt.report.designer.ui.ide.wizards;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IDETemplateEditor;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;


public class NewTemplateWizard extends NewReportWizard
{
	private static final String WIZARDPAGE = Messages.getString( "NewTemplateWizard.title.WizardPage" ); //$NON-NLS-1$
	private static final String OPENING_FILE_FOR_EDITING = Messages.getString( "NewTemplateWizard.text.OpenFileForEditing" ); //$NON-NLS-1$
	private static final String CREATING = Messages.getString( "NewTemplateWizard.text.Creating" ); //$NON-NLS-1$
	private static final String NEW_TEMPLATE_FILE_NAME_PREFIX = Messages.getString( "NewTemplateWizard.displayName.NewReportFileNamePrefix" ); //$NON-NLS-1$
	private static final String NEW_TEMPLATE_DESCRIPTION = Messages.getString( "NewTemplateWizard.pageDescription.createNewTemplate" ); //$NON-NLS-1$
	private static final String NEW_TEMPLATE_TITLE = Messages.getString( "NewTemplateWizard.title.Template" ); //$NON-NLS-1$
	
	public NewTemplateWizard()
	{
		super( "." + IReportElementConstants.TEMPLATE_FILE_EXTENSION ); //$NON-NLS-1$
	}
	
	public void init( IWorkbench workbench, IStructuredSelection selection )
	{
		super.init(workbench,selection);
		setWindowTitle( Messages.getString( "NewTemplateWizard.title.New" ) ); //$NON-NLS-1$
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#addPages()
	 */
	public void addPages( )
	{
		newReportFileWizardPage = new WizardNewReportCreationPage( WIZARDPAGE,
				getSelection(),IReportElementConstants.TEMPLATE_FILE_EXTENSION );
		addPage( newReportFileWizardPage );

		resetUniqueCount( );
		newReportFileWizardPage.setFileName( getUniqueReportName(NEW_TEMPLATE_FILE_NAME_PREFIX, getFileExtension())); //$NON-NLS-1$ //$NON-NLS-2$
		newReportFileWizardPage.setContainerFullPath( getDefaultContainerPath( ) );
		newReportFileWizardPage.setDescription( NEW_TEMPLATE_DESCRIPTION );
		newReportFileWizardPage.setTitle(NEW_TEMPLATE_TITLE);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#canFinish()
	 */
	public boolean canFinish( )
	{
		return newReportFileWizardPage.isPageComplete( );
	}
	
	public boolean performFinish( )
	{
		final IPath containerName = newReportFileWizardPage.getContainerFullPath( );
		String fn = newReportFileWizardPage.getFileName( );
		final String fileName;
		if ( !fn.endsWith( getFileExtension() ) ) //$NON-NLS-1$
		{
			fileName = fn +  getFileExtension(); //$NON-NLS-1$
		}
		else
		{
			fileName = fn;
		}
		InputStream streamFromPage = null;
		URL url = Platform.find( Platform.getBundle( ReportPlugin.REPORT_UI ),
				new Path( "/templates/blank_report.rptdesign" ) );//$NON-NLS-1$
		if ( url != null )
		{
			try
			{
				streamFromPage = url.openStream( );
			}
			catch ( IOException e1 )
			{
				//ignore.
			}
		}
		final InputStream stream = streamFromPage;
		IRunnableWithProgress op = new IRunnableWithProgress( ) {

			public void run( IProgressMonitor monitor )
					throws InvocationTargetException
			{
				try
				{
					doFinish( containerName,
							fileName,
							stream,
							monitor );
				}
				catch ( CoreException e )
				{
					throw new InvocationTargetException( e );
				}
				finally
				{
					monitor.done( );
				}
			}
		};
		try
		{
			getContainer( ).run( true, false, op );
		}
		catch ( InterruptedException e )
		{
			return false;
		}
		catch ( InvocationTargetException e )
		{
			Throwable realException = e.getTargetException( );
			ExceptionHandler.handle( realException );
			return false;
		}
		return true;
	}
	
	/**
	 * The worker method. It will find the container, create the file if missing
	 * or just replace its contents, and open the editor on the newly created
	 * file.
	 * 
	 * @param cheatSheetId
	 * 
	 * @param containerName
	 * @param fileName
	 * @param showCheatSheet
	 * @param monitor
	 */

	private void doFinish( IPath containerName, String fileName,
			InputStream stream,
			IProgressMonitor monitor ) throws CoreException
	{
		// create a sample file
		monitor.beginTask( CREATING + fileName, 2 );
		IResource resource = (IContainer) ResourcesPlugin.getWorkspace( )
				.getRoot( )
				.findMember( containerName );
		IContainer container = null;
		if ( resource == null
				|| !resource.exists( ) || !( resource instanceof IContainer ) )
		{
			// create folder if not exist
			IFolder folder = createFolderHandle( containerName );
			UIUtil.createFolder( folder, monitor );
			container = folder;
		}
		else
		{
			container = (IContainer) resource;
		}
		final IFile file = container.getFile( new Path( fileName ) );
		try
		{
			if ( file.exists( ) )
			{
				file.setContents( stream, true, true, monitor );
			}
			else
			{
				file.create( stream, true, monitor );
			}
			stream.close( );

		}
		catch ( IOException e )
		{
		}
		monitor.worked( 1 );
		monitor.setTaskName( OPENING_FILE_FOR_EDITING );
		getShell( ).getDisplay( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				IWorkbench workbench = PlatformUI.getWorkbench( );
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow( );

				IWorkbenchPage page = window.getActivePage( );
				try
				{
					IDE.openEditor( page, file, IDETemplateEditor.ID,true );
					BasicNewProjectResourceWizard.updatePerspective( getConfigElement() );
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}
		} );

		monitor.worked( 1 );
	}
	
}
