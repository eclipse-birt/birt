/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.samplesview.sampleslocator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * Represents the resource item in samples fragment, which is either folder or
 * report design file in file system
 */
public class SampleReportsEntry implements ResourceEntry {

	private static final String SAMPLE_REPORTS_HOST = "org.eclipse.birt.report.designer.samplereports"; //$NON-NLS-1$

	public static final Bundle samplesBundle = Platform.getBundle(SAMPLE_REPORTS_HOST);

	private String name;

	private String displayName;

	private SampleReportsEntry parent;

	private String path;

	private List children = new ArrayList();

	private ReportDesignHandle sampleReport;

	private boolean isRoot;

	private boolean isFile;

	public SampleReportsEntry(String[] filePattern, String entryname, String fragmentPath,
			SampleReportsEntry parentEntry, boolean isFile) {
		// this( "BIRT Examples", "/samplereports", null, false );
		// //$NON-NLS-1$//$NON-NLS-2$
		this(entryname, fragmentPath, parentEntry, false);
		this.isRoot = true;
		this.displayName = "BIRT Examples"; //$NON-NLS-1$
		// samplesBundle = Platform.getBundle( SAMPLE_REPORTS_HOST );
		if (samplesBundle != null) {
			if (filePattern != null && filePattern.length > 0) {
				for (int i = 0; i < filePattern.length; i++) {
					String[] patterns = filePattern[i].split(";"); //$NON-NLS-1$
					for (int j = 0; j < patterns.length; j++) {
						Enumeration enumeration = samplesBundle.findEntries(fragmentPath, patterns[j], true);
						while (enumeration != null && enumeration.hasMoreElements()) {
							URL element = (URL) enumeration.nextElement();
							String path = element.getPath() + (element.getRef() != null ? "#" //$NON-NLS-1$
									+ element.getRef() : ""); //$NON-NLS-1$
							String[] pathtoken = path.split("/"); //$NON-NLS-1$
							SampleReportsEntry parent = this;
							for (int m = 0; m < pathtoken.length; m++) {
								if (pathtoken[m].equals("") //$NON-NLS-1$
										|| pathtoken[m].equals(fragmentPath.substring(fragmentPath.indexOf('/') + 1)))
									continue;
								SampleReportsEntry child = parent.getChild(pathtoken[m]);
								if (child == null) {
									child = new SampleReportsEntry(pathtoken[m], (parent.path.equals("/") ? "" //$NON-NLS-1$//$NON-NLS-2$
											: parent.path) + "/" //$NON-NLS-1$
											+ pathtoken[m], parent, m == pathtoken.length - 1);
								}
								parent = child;
							}
						}
					}
				}
			}
		}
	}

	private SampleReportsEntry(String entryname, String fragmentPath, SampleReportsEntry parentEntry, boolean isFile) {
		this.name = entryname;
		this.path = fragmentPath;
		this.parent = parentEntry;
		if (parent != null)
			parent.addChild(this);
		this.isFile = isFile;
	}

	private void addChild(SampleReportsEntry entry) {
		this.children.add(entry);
	}

	private SampleReportsEntry getChild(String name) {
		for (Iterator iter = this.children.iterator(); iter.hasNext();) {
			SampleReportsEntry entry = (SampleReportsEntry) iter.next();
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

	public ResourceEntry[] getChildren(Filter filter) {
		ResourceEntry[] children = getChildren();
		List childrenFiltered = new ArrayList();
		for (int i = 0; i < children.length; i++) {
			if (filter.accept(children[i]))
				childrenFiltered.add(children[i]);
		}
		return (ResourceEntry[]) childrenFiltered.toArray(new ResourceEntry[childrenFiltered.size()]);
	}

	public String getName() {
		return this.name;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public Image getImage() {
		if (this.isRoot || hasChildren()) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		}
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
	}

	public ResourceEntry getParent() {
		return this.parent;
	}

	public URL getURL() {
		if (samplesBundle != null)
			return samplesBundle.getResource(this.path);
		return null;
	}

	public boolean isFile() {
		return this.isFile;
	}

	public void dispose() {
		if (this.sampleReport != null) {
			this.sampleReport.close();
			this.sampleReport = null;
		}
		ResourceEntry[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}
	}

	public Object getAdapter(Class adapter) {
		if (adapter == ReportDesignHandle.class) {
			if (!hasChildren() && this.sampleReport == null) {
				try {
					this.sampleReport = SessionHandleAdapter.getInstance().getSessionHandle()
							.openDesign(getURL().toString());
				} catch (DesignFileException e) {
				}
			}
			return sampleReport;
		}
		if (adapter == ResourceEntry.class)
			return this;
		return null;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public boolean hasChildren(Filter filter) {
		return getChildren(filter).length > 0;
	}
}
