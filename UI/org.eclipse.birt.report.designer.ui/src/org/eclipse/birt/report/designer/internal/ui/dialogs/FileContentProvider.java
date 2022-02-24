/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntryFilter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceFilter;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Tree viewer content provider adapter for resource browser.
 * 
 */

public class FileContentProvider implements ITreeContentProvider {

	private boolean showFiles;
	private FilePathEntry.Filter filter = new FilePathEntry.Filter() {

		public boolean accept(ResourceEntry entity) {
			return false;
		}
	};
	private String[] fileExtension;

	/**
	 * Constructor.
	 * 
	 * @param showFiles show files.
	 */
	public FileContentProvider(final boolean showFiles) {
		this.showFiles = showFiles;
		filter = new FilePathEntry.Filter() {

			public boolean accept(ResourceEntry entity) {
				ResourceEntryFilter filter = new ResourceEntryFilter(
						(ResourceFilter[]) ReportPlugin.getFilterMap().values().toArray(new ResourceFilter[0]));
				if (entity.hasChildren()) {
					if (filter.accept(entity))
						return true;
				}
				if (showFiles)
					return filter.accept(entity);
				else {
					if (entity.isFile())
						return false;
					else
						return filter.accept(entity);
				}
			}
		};
	}

	/**
	 * Constructor.
	 * 
	 * @param showFiles
	 * @param extension file extensions must be lowcase
	 */
	public FileContentProvider(final String[] extension) {
		this.showFiles = true;
		this.fileExtension = extension;
		filter = new FilePathEntry.Filter() {

			public boolean accept(ResourceEntry entity) {
				ResourceEntryFilter filter = new ResourceEntryFilter(
						(ResourceFilter[]) ReportPlugin.getFilterMap().values().toArray(new ResourceFilter[0]));
				if (entity.hasChildren()) {
					return filter.accept(entity);
				}
				for (int i = 0; i < extension.length; i++) {
					if (entity.getName().toLowerCase().endsWith(extension[i])) {
						if (filter.accept(entity))
							return true;
					}
				}
				return false;
			}
		};
	}

	public void setFileNamePattern(final String[] fileNamePattern) {
		this.filter = new FilePathEntry.Filter() {

			public boolean accept(ResourceEntry entity) {
				ResourceEntryFilter filter = new ResourceEntryFilter(
						(ResourceFilter[]) ReportPlugin.getFilterMap().values().toArray(new ResourceFilter[0]));
				if (entity.hasChildren()) {
					return filter.accept(entity);
				}
				for (int i = 0; i < fileNamePattern.length; i++) {
					// FIXME
					//
					if (entity.getName().toLowerCase().endsWith(fileNamePattern[i].substring(1))) {
						if (filter.accept(entity))
							return true;
					}
				}
				return false;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Object[]) {
			return (Object[]) parentElement;
		}
		if (parentElement instanceof FilePathEntry) {
			return ((FilePathEntry) parentElement).getChildren(this.filter);
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		if (element instanceof File) {
			return ((File) element).getParentFile();
		}
		if (element instanceof FilePathEntry) {
			return ((FilePathEntry) element).getParent();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof File) {
			return ((File) element).list() != null && ((File) element).list().length > 0;
		}
		if (element instanceof FilePathEntry) {
			return ((FilePathEntry) element).getChildren(filter).length > 0;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.
	 * Object)
	 */
	public Object[] getElements(Object inputElement) {
		// if ( inputElement instanceof String )
		// {
		// return new Object[]{
		// new File( inputElement.toString( ) )
		// };
		// }
		return getChildren(inputElement);
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

}
