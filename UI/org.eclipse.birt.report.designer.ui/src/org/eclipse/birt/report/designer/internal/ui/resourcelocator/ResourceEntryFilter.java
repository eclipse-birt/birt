/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.resourcelocator;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry.Filter;

public class ResourceEntryFilter implements Filter {

	private ResourceFilter[] filters;

	public ResourceEntryFilter(ResourceFilter[] filters) {
		this.filters = filters;
	}

	public boolean accept(ResourceEntry entity) {
		// File file = new File(entity.getName( ));
		// if(!file.exists( ))return true;
		if (filters == null || filters.length == 0)
			return true;
		for (int i = 0; i < filters.length; i++) {
			ResourceFilter filter = filters[i];
			if (!filter.isEnabled())
				continue;
			if (filter.getType().equals(ResourceFilter.FILTER_CVS_RESOURCES)) {
				if (entity.getName().startsWith("CVS"))//$NON-NLS-1$
					return false;
			} else if (filter.getType().equals(ResourceFilter.FILTER_DOT_RESOURCES)) {
				if (entity.getName().startsWith(".") //$NON-NLS-1$
						&& !entity.getName().endsWith("." + IReportElementConstants.LIBRARY_FILE_EXTENSION))//$NON-NLS-1$
					return false;
			} else if (filter.getType().equals(ResourceFilter.FILTER_EMPTY_FOLDERS)) {
				if (!entity.isFile() && !entity.hasChildren())
					return false;
			} else if (filter instanceof ExtendedResourceFilter) {
				return ((ExtendedResourceFilter) filter).accept(entity);
			}
			// TODO: case 4, add a getAdapte method in the interface
			// ResourceEntry, and then can get the File filter info.
			/*
			 * else if ( file.isDirectory( ) && filter.getType( ) .equals(
			 * ResourceFilter.FILTER_FOLDERS_WITHOUTRESOURCE ) ) { boolean flag =
			 * hasResource( file ); if ( !flag ) return true; }
			 */
		}
		return true;
	}

	/**
	 * Case 4, maybe it can implement in future
	 */
	/*
	 * private boolean hasResource( File file ) { if ( file.isFile( ) ) return
	 * false; else { if ( file.listFiles( filter ).length > 0 ) return true; File
	 * files[] = file.listFiles( ); for ( int i = 0; i < file.length( ); i++ ) { if
	 * ( hasResource( files[i] ) ) return true; } } return false; }
	 */

}
