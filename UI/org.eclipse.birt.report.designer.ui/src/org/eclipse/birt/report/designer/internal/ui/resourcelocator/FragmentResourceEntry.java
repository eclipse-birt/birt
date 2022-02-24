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

package org.eclipse.birt.report.designer.internal.ui.resourcelocator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.osgi.framework.Bundle;

/**
 * FragmentResourceEntry
 */
public class FragmentResourceEntry extends BaseResourceEntity {

	public static final String TEMPLATE_ROOT = "templates"; //$NON-NLS-1$
	public static final String RESOURCE_ROOT = "resources"; //$NON-NLS-1$

	protected Bundle bundle;

	protected String name;

	protected String displayName;

	private FragmentResourceEntry parent;

	private String path;

	private List children = new ArrayList();

	private LibraryHandle library;

	private CssStyleSheetHandle cssStyleHandle;

	protected boolean isRoot;

	private boolean isFile;

	/** The entries been parsed, saves them to avoid to parsed repeatly. */
	private final Collection<URL> parsedEntries = new HashSet<URL>();
	private FileFilter filter;

	public FragmentResourceEntry() {
		this(null);
	}

	public FragmentResourceEntry(final String[] filePattern, String name, String displayName, String path) {
		this(filePattern, name, displayName, path, Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST));
	}

	public FragmentResourceEntry(final String[] filePattern, String name, String displayName, String path,
			Bundle bundle) {
		this(name, displayName, path, null, false, true); // $NON-NLS-1$//$NON-NLS-2$
		if (filePattern != null)
			filter = new FileFilter(filePattern);
		this.bundle = bundle;
		if (bundle != null) {
			Enumeration<URL> enumeration = findEntries(path);
			parseResourceEntry(this, enumeration);
			parsedEntries.clear();
		}
	}

	public FragmentResourceEntry(String[] filePattern) {
		this(filePattern, Messages.getString("FragmentResourceEntry.RootName"), //$NON-NLS-1$
				Messages.getString("FragmentResourceEntry.RootDisplayName"), RESOURCE_ROOT); //$NON-NLS-1$
	}

	private void parseResourceEntry(FragmentResourceEntry parent, Enumeration<URL> enumeration) {
		while (enumeration != null && enumeration.hasMoreElements()) {
			URL element = enumeration.nextElement();
			String path = element.getPath();
			File file = null;

			try {
				file = new File(FileLocator.toFileURL(element).getPath());
			} catch (IOException e) {
				continue;
			}

			FragmentResourceEntry entry = new FragmentResourceEntry(file.getName(), path, parent, file.isFile());
			entry.bundle = Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST);

			// Saves the element, avoid to be parsed repeatedly.
			parsedEntries.add(element);

			Enumeration<URL> children = findEntries(path);

			if (children != null) {
				parseResourceEntry(entry, children);
			}
		}
	}

	protected FragmentResourceEntry(String name, String displayName, String path, FragmentResourceEntry parent,
			boolean isFile, boolean isRoot) {
		this(name, path, parent, isFile);
		this.isRoot = isRoot;
		this.displayName = displayName; // $NON-NLS-1$

	}

	protected FragmentResourceEntry(String name, String path, FragmentResourceEntry parent, boolean isFile) {
		this.name = name;
		this.path = path;
		this.parent = parent;
		if (parent != null)
			parent.addChild(this);
		this.isFile = isFile;
	}

	private void addChild(FragmentResourceEntry entry) {
		this.children.add(entry);
	}

	private FragmentResourceEntry getChild(String name) {
		for (Iterator iter = this.children.iterator(); iter.hasNext();) {
			FragmentResourceEntry entry = (FragmentResourceEntry) iter.next();
			if (entry.getName().equals(name))
				return entry;
		}
		return null;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public ResourceEntry[] getChildren() {
		return (ResourceEntry[]) this.children.toArray(new ResourceEntry[this.children.size()]);
	}

	public String getName() {
		return this.name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public Image getImage() {
		if (this.isRoot || !isFile())
			return ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJ_FOLDER);
		return super.getImage();
	}

	public ResourceEntry getParent() {
		return this.parent;
	}

	public URL getURL() {
		if (bundle != null)
			return bundle.getResource(this.path);
		return null;
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

		if (this.cssStyleHandle != null) {
			// according to Xingjie, GUI needn't close() it.
			this.cssStyleHandle = null;
		}

		ResourceEntry[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}
	}

	public Object getAdapter(Class adapter) {
		if (adapter == LibraryHandle.class && getURL().toString().toLowerCase().endsWith("library")) {
			if (!hasChildren() && this.library == null) // $NON-NLS-1$
			{
				try {
					this.library = SessionHandleAdapter.getInstance().getSessionHandle()
							.openLibrary(FileLocator.toFileURL(getURL()).toString());
				} catch (Exception e) {
				}
			}
			return library;
		} else if (adapter == CssStyleSheetHandle.class) {
			if (this.cssStyleHandle == null && getURL().toString().toLowerCase().endsWith(".css")) //$NON-NLS-1$
			{
				try {
					cssStyleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle()
							.openCssStyleSheet(FileLocator.toFileURL(getURL()).toString());
				} catch (Exception e) {
				}

			}
			return cssStyleHandle;
		}
		return super.getAdapter(adapter);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (!(object instanceof FragmentResourceEntry || object instanceof String))
			return false;
		if (object == this)
			return true;
		else {
			if (object instanceof FragmentResourceEntry) {
				FragmentResourceEntry temp = (FragmentResourceEntry) object;

				if (temp.path.equals(this.path)) {
					return true;
				}
			} else if (object instanceof String) {
				if (object.equals(this.path)) {
					return true;
				}
			}
		}
		return false;
	}

	public int hashCode() {
		return this.path.hashCode();
	}

	/**
	 * Returns an enumeration of URL objects for each matching entry.
	 * 
	 * @param path     The path name in which to look.
	 * @param patterns The file name pattern for selecting entries in the specified
	 *                 path.
	 * @return an enumeration of URL objects for each matching entry.
	 */
	private Enumeration<URL> findEntries(String path) {
		Set<URL> entries = new HashSet<URL>();
		Enumeration<URL> children = bundle.findEntries(path, null, false);

		while (children != null && children.hasMoreElements()) {
			URL url = children.nextElement();
			if (filter == null || (filter != null && filter.accept(url)))
				entries.add(url);
		}

		children = bundle.findEntries(path, null, false);
		while (children != null && children.hasMoreElements()) {
			URL child = children.nextElement();

			if (!isParsed(child) && hasChildren(child)) {
				entries.add(child);
			}
		}

		return new Vector<URL>(entries).elements();
	}

	/**
	 * Tests whether the specified URL contains children.
	 * 
	 * @param url the URL to test.
	 * @return <code>true</code> if the specified URL contains children,
	 *         <code>false</code> otherwise.
	 */
	private boolean hasChildren(URL url) {
		Enumeration<URL> children = bundle.findEntries(url.getPath(), null, false);

		return children != null && children.hasMoreElements();
	}

	/**
	 * Tests whether the specified URL has been parsed.
	 * 
	 * @param url the URL to test.
	 * @return <code>true</code> if the specified URL has been parsed,
	 *         <code>false</code> otherwise.
	 */
	private boolean isParsed(URL url) {
		return parsedEntries.contains(url);
	}

	private static class FileFilter {

		private String[] filePattern;

		public FileFilter(String[] filePattern) {
			this.filePattern = filePattern;
		}

		public boolean accept(URL path) {
			for (int i = 0; i < filePattern.length; i++) {
				String[] regs = filePattern[i].split(";"); //$NON-NLS-1$
				for (int j = 0; j < regs.length; j++) {
					// need use decoded String ???
					if (URLDecoder.decode(path.toString()).toLowerCase().endsWith(regs[j].toLowerCase().substring(1)))
						return true;
				}
			}
			return false;
		}

	};
}
