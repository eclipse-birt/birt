/*******************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntryFilter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceFilter;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.viewers.Viewer;

/**
 * Tree viewer content provider adapter for resource browser.
 *
 */

public class ResourceFileContentProvider implements IResourceContentProvider {

	private ResourceEntry.Filter filter = new ResourceEntry.Filter() {

		@Override
		public boolean accept(ResourceEntry entity) {
			return true;
		}
	};

	/**
	 * Constructor.
	 *
	 * @param showFiles show files.
	 */
	public ResourceFileContentProvider(final boolean showFiles) {
		setFilter(new ResourceEntry.Filter() {

			@Override
			public boolean accept(ResourceEntry entity) {
				ResourceEntryFilter filter = new ResourceEntryFilter(getResourceFilters());
				if (entity.hasChildren()) {
					return filter.accept(entity);
				}
				if (showFiles) {
					return filter.accept(entity);
				} else if (entity.isFile()) {
					return false;
				} else {
					return filter.accept(entity);
				}
			}
		});
	}

	/**
	 * Constructor.
	 *
	 * @param showFiles
	 * @param extension file extensions must be lowcase
	 */
	public ResourceFileContentProvider(final String[] extension) {
		setFilter(new ResourceEntry.Filter() {

			@Override
			public boolean accept(ResourceEntry entity) {

				ResourceEntryFilter filter = new ResourceEntryFilter(getResourceFilters());

				if (entity.hasChildren()) {
					return filter.accept(entity);
				}
				for (int i = 0; i < extension.length; i++) {
					if (entity.getName().toLowerCase().endsWith(extension[i])) {
						if (filter.accept(entity)) {
							return true;
						}
					}
				}
				return false;
			}
		});
	}

	public void setFileNamePattern(final String[] fileNamePattern) {
		setFilter(new ResourceEntry.Filter() {

			@Override
			public boolean accept(ResourceEntry entity) {
				ResourceEntryFilter filter = new ResourceEntryFilter(getResourceFilters());
				if (entity.hasChildren()) {
					return filter.accept(entity);
				}
				for (int i = 0; i < fileNamePattern.length; i++) {
					// FIXME
					//
					if (entity.getName().toLowerCase().endsWith(fileNamePattern[i].substring(1))) {
						if (filter.accept(entity)) {
							return true;
						}
					}
				}
				return false;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
	 * Object)
	 */
	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Object[]) {
			return (Object[]) parentElement;
		}
		if (parentElement instanceof ResourceEntry) {
			return ((ResourceEntry) parentElement).getChildren(this.filter);
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object )
	 */
	@Override
	public Object getParent(Object element) {
		if (element instanceof File) {
			return ((File) element).getParentFile();
		}
		if (element instanceof ResourceEntry) {
			return ((ResourceEntry) element).getParent();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof File) {
			return ((File) element).list() != null && ((File) element).list().length > 0;
		}
		if (element instanceof ResourceEntry) {
			return ((ResourceEntry) element).getChildren(filter).length > 0;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		// if ( inputElement instanceof String )
		// {
		// return new Object[]{
		// new File( inputElement.toString( ) )
		// };
		// }
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

	}

	/**
	 * Sets the filter for resource browser.
	 *
	 * @param filter the filter to set
	 */
	public void setFilter(ResourceEntry.Filter filter) {
		this.filter = filter;
	}

	public ResourceEntry.Filter getFilter() {
		return this.filter;
	}

	private int showEmptyFolderStatus = 0;

	@Override
	public int getEmptyFolderShowStatus() {
		return showEmptyFolderStatus;
	}

	@Override
	public void setEmptyFolderShowStatus(int showStatus) {
		this.showEmptyFolderStatus = showStatus;
	}

	public ResourceFilter[] getResourceFilters() {
		ResourceFilter[] filters;
		if (showEmptyFolderStatus == IResourceContentProvider.ALWAYS_SHOW_EMPTYFOLDER) {
			filters = (ResourceFilter[]) ReportPlugin.getFilterMap(false).values().toArray(new ResourceFilter[0]);
		} else if (showEmptyFolderStatus == IResourceContentProvider.ALWAYS_NOT_SHOW_EMPTYFOLDER) {
			List filterCollection = new ArrayList(ReportPlugin.getFilterMap(false).values());
			ResourceFilter filter = ResourceFilter.generateEmptyFolderFilter();
			filter.setEnabled(true);
			filterCollection.add(filter);
			filters = (ResourceFilter[]) filterCollection.toArray(new ResourceFilter[0]);
		} else {
			filters = (ResourceFilter[]) ReportPlugin.getFilterMap().values().toArray(new ResourceFilter[0]);
		}
		return filters;
	}
}
