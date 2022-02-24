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
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.BaseResourceEntity;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * FilePathEntry
 */
public class FilePathEntry extends BaseResourceEntity {

	protected Logger logger = Logger.getLogger(FilePathEntry.class.getName());

	private String path;
	private URL url;
	private String name;
	private String displayName;
	private FileFilter filter;
	private FilePathEntry parent;
	private boolean isFolder;
	private boolean isRoot;
	private LibraryHandle library;
	private ArrayList childrenList;
	private boolean isFile;

	public FilePathEntry(String filePath) {
		this(filePath, null, true);
	}

	public FilePathEntry(String filePath, final boolean showFiles) {
		this(filePath, null, showFiles);
	}

	public FilePathEntry(String filePath, final String[] filePattern) {
		this(filePath, filePattern, true);
	}

	public FilePathEntry(String filePath, final String[] filePattern, final boolean showFiles) {
		if (filePattern != null) {
			filter = new FileFilter() {

				public boolean accept(File pathname) {
					if (pathname.isDirectory())
						return true;
					for (int i = 0; i < filePattern.length; i++) {
						String[] regs = filePattern[i].split(";"); //$NON-NLS-1$
						for (int j = 0; j < regs.length; j++) {
							if (pathname.getName().toLowerCase().endsWith(regs[j].toLowerCase().substring(1)))
								return true;
						}
					}
					return false;
				}

			};
		} else {
			filter = new FileFilter() {

				public boolean accept(File pathname) {
					if (pathname.isDirectory())
						return true;
					return showFiles;
				}

			};
		}
		this.name = filePath;
		this.displayName = new File(filePath).getName();
		this.isRoot = true;
		initRoot(filePath);
	}

	private FilePathEntry(String path, String name, FilePathEntry parent) {
		this.path = path;
		this.name = name;
		this.parent = parent;
		this.filter = parent.filter;
		try {
			File file = new File(this.path);
			this.isFolder = file.isDirectory();
			this.url = file.toURL();
			this.isFile = file.isFile();
		} catch (MalformedURLException e) {
		}
	}

	private void initRoot(String path) {
		this.path = path;
		if (this.path != null) {
			try {
				File file = new File(this.path);
				this.isFolder = file.isDirectory();
				this.url = file.toURL();
			} catch (Exception e) {
			}
		}
	}

	public boolean hasChildren() {
		File file = new File(this.path);
		if (file.isDirectory()) {
			String[] list = file.list();
			if (list == null)
				return false;
			return file.list().length > 0;
		} else
			return false;
	}

	public ResourceEntry[] getChildren() {
		if (this.childrenList == null) {
			this.childrenList = new ArrayList();
			try {
				File file = new File(this.path);
				if (file.isDirectory()) {
					File[] children = file.listFiles(filter);
					if (children != null) {
						for (int i = 0; i < children.length; i++) {
							FilePathEntry child = new FilePathEntry(children[i].getAbsolutePath(),
									children[i].getName(), this);
							childrenList.add(child);
						}
					}
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return (ResourceEntry[]) childrenList.toArray(new ResourceEntry[childrenList.size()]);
	}

	public String getName() {
		return this.name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public Image getImage() {
		if (this.isRoot)
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_PROJECT);
		else if (this.isFolder)
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		return super.getImage();
	}

	public ResourceEntry getParent() {
		return this.parent;
	}

	public URL getURL() {
		return this.url;
	}

	public boolean isFile() {
		return this.isFile;
	}

	public boolean isRoot() {
		return this.isRoot;
	}

	public void dispose() {
		if (this.library != null) {
			this.library.close();
			this.library = null;
		}
		if (this.childrenList != null) {
			for (Iterator iterator = this.childrenList.iterator(); iterator.hasNext();) {
				ResourceEntry entry = (ResourceEntry) iterator.next();
				entry.dispose();
			}
		}
	}

	public Object getAdapter(Class adapter) {
		if (adapter == LibraryHandle.class && getURL().toString().toLowerCase().endsWith("library")) {
			if (!this.isFolder && this.library == null) {
				try {
					this.library = SessionHandleAdapter.getInstance().getSessionHandle()
							.openLibrary(getURL().toString());
				} catch (DesignFileException e) {
				}
			}
			return library;
		}
		return null;
	}

	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof FilePathEntry))
			return false;
		if (object == this)
			return true;
		else {
			FilePathEntry temp = (FilePathEntry) object;
			if (temp.path.equals(this.path))
				return true;
		}
		return false;
	}

	public int hashCode() {
		return this.path.hashCode();
	}
}
