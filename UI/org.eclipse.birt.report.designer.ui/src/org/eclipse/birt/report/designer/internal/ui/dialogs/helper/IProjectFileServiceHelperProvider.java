package org.eclipse.birt.report.designer.internal.ui.dialogs.helper;

/**
 * 
 * @author cthronson
 *
 */
public interface IProjectFileServiceHelperProvider {

	/**
	 * Creates a helper for selecting project files.
	 * 
	 * @return A helper for selecting project files
	 */
	IProjectFileServiceHelper createHelper();

}
