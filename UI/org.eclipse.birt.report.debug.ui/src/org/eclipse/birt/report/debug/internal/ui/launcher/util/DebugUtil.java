/*
 * Created on 2005-11-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.report.debug.internal.ui.launcher.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.internal.resources.ProjectDescriptionReader;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DebugUtil {

	/**
	 * Import exist project into current workspace.
	 * 
	 * @param prjFilePath
	 *            .project file path.
	 * @throws Exception
	 */
	public static void importProject(String prjFilePath) throws Exception {
		IPath path = new Path(prjFilePath+File.separator + ".project");
		final IProjectDescription newDescription = loadProjectDescription(path);
		final IProject project = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(newDescription.getName());
		if (project == null)
			throw new Exception("DebugUtil.importProject.fail"); //$NON-NLS-1$
		NullProgressMonitor monitor = new NullProgressMonitor();
		project.create(newDescription, monitor);
		project.open(monitor);
	}

	private static IProjectDescription loadProjectDescription(IPath path)
			throws IOException {
		IProjectDescription result = null;
		result = new ProjectDescriptionReader().read(path);
		if (result != null) {
			// check to see if we are using in the default area or not. use
			// java.io.File for
			// testing equality because it knows better w.r.t. drives and
			// case sensitivity
			IPath user = path.removeLastSegments(1);
			IPath platform = Platform.getLocation().append(result.getName());
			if (!user.toFile().equals(platform.toFile()))
				result.setLocation(user);
		}
		return result;
	}
	
	
	/**
	 * copy a special folder to current workspace folder.
	 * 
	 * @param folderPath
	 * @return the result folder path.
	 * @throws Exception
	 */
	public static String copyToWorkspace( String folderPath ) throws Exception
	{
		File folder = new File( folderPath );
		if ( !folder.isDirectory( ) )
			throw new Exception( );
		String newFolderName = folder.getName( );
		String workspaceRootPath = ResourcesPlugin.getWorkspace( )
				.getRoot( )
				.getLocation( )
				.toOSString( );
		String newFolderPath = workspaceRootPath
				+ File.separator
				+ newFolderName;
		File newFolder = new File( newFolderPath );
		if ( !newFolder.exists( ) )
			newFolder.mkdir( );

		copyFiles( folder, newFolder );

		return newFolderPath;
	}

	private static void copyFiles( File folder, File newFolder )
			throws IOException
	{
		File[] folderFiles = folder.listFiles( );
		if ( folderFiles != null )
			for ( int i = 0; i < folderFiles.length; i++ )
			{
				if ( folderFiles[i].isFile( ) )
				{
					File newFile = new File( newFolder,
							folderFiles[i].getName( ) );
					if ( !newFile.exists( ) )
						newFile.createNewFile( );
					copyFile( folderFiles[i], newFile );
				}
				else
				{
					File newSubFolder = new File( newFolder,
							folderFiles[i].getName( ) );
					if ( !newSubFolder.exists( ) )
						newSubFolder.mkdir( );
					copyFiles( folderFiles[i], newSubFolder );
				}
			}
	}

	private static void copyFile( File in, File out ) throws IOException
	{
		FileInputStream fis = new FileInputStream( in );
		FileOutputStream fos = new FileOutputStream( out );
		byte[] buf = new byte[1024];
		int i = 0;
		while ( ( i = fis.read( buf ) ) != -1 )
		{
			fos.write( buf, 0, i );
		}
		fis.close( );
		fos.close( );
	}
}