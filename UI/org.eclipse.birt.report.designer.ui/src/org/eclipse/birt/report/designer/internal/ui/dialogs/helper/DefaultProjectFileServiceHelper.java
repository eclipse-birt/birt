/**
 * 
 */
package org.eclipse.birt.report.designer.internal.ui.dialogs.helper;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ProjectFileDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.FileDialog;

/**
 * @author cthronson
 *
 */
public class DefaultProjectFileServiceHelper implements IProjectFileServiceHelper {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.helper.
	 * IProjectFileServiceHelper#getLocation(boolean, java.lang.String, boolean,
	 * boolean, java.lang.String[], java.lang.String, boolean)
	 */
	@Override
	public String getUserSelection(final boolean isIDE, final String projectFolder, final boolean needFilter,
			final boolean projectMode, final String[] fileExt, final String selectedType,
			final boolean isRelativeToProjectRoot) {
		String filename = null;
		if (!isIDE || projectFolder == null) {
			FileDialog dialog = new FileDialog(UIUtil.getDefaultShell());
			if (needFilter) {
				dialog.setFilterExtensions(fileExt);
			}
			filename = dialog.open();
		} else {

			ProjectFileDialog dialog;
			if (needFilter) {
				dialog = new ProjectFileDialog(projectFolder, fileExt, selectedType, isRelativeToProjectRoot);
			} else {
				dialog = new ProjectFileDialog(projectFolder, selectedType, isRelativeToProjectRoot);
			}

			if (dialog.open() == Window.OK) {
				filename = dialog.getPath();
			}
		}
		return filename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.helper.
	 * IProjectFileServiceHelper#getFilePath(java.lang.String)
	 */
	@Override
	public String getFilePath(String userSelection) {
		return userSelection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.helper.
	 * IProjectFileServiceHelper#getTargetReportLocation(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public String getTargetReportLocation(String filename, String userSelection) {
		return filename;
	}

}
