/*************************************************************************************
 * Copyright (c) 2006 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.static_html;

import java.io.File;
import java.net.URL;

import org.eclipse.birt.report.designer.ui.preview.static_html.preferences.PreferenceConstants;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class StaticHTMLPrviewPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.birt.report.designer.ui.preview.static_html"; //$NON-NLS-1$

	public static final String IMG_NAV_FIRST = "FirstPage.gif"; //$NON-NLS-1$

	public static final String IMG_NAV_PRE = "PreviousPage.gif"; //$NON-NLS-1$

	public static final String IMG_NAV_NEXT = "NextPage.gif"; //$NON-NLS-1$

	public static final String IMG_NAV_LAST = "LastPage.gif"; //$NON-NLS-1$

	public static final String IMG_NAV_GO = "Go.gif"; //$NON-NLS-1$

	public static final String IMG_PARAMS = "parameter.gif"; //$NON-NLS-1$

	public static final String IMG_TOC = "Toc.gif"; //$NON-NLS-1$

	public static final String IMG_FORM_TITLE = "form_title.gif"; //$NON-NLS-1$

	public static final String IMG_TOC_LEAF = "Leaf.gif"; //$NON-NLS-1$

	public static final String IMG_RE_RUN = "preview.gif"; //$NON-NLS-1$

	// The shared instance
	private static StaticHTMLPrviewPlugin plugin;

	/**
	 * The constructor
	 */
	public StaticHTMLPrviewPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		if (isCleanTempfolder())
			deleteTempFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		if (isCleanTempfolder())
			deleteFile(new File(getTempFolder()));
	}

	private void deleteFile(final File dir) {
		File[] children = dir.listFiles();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				deleteFolder(children[i]);
			}
		}
	}

	private void deleteFolder(final File dir) {
		File[] children = dir.listFiles();
		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				if (children[i].isDirectory())
					deleteFile(children[i]);
				else {
					children[i].delete();
				}
			}
		}
		dir.delete();
	}

	private void deleteTempFile() {
		Job deleteJob = new Job("Delete temporary files") { //$NON-NLS-1$

			protected IStatus run(IProgressMonitor monitor) {
				deleteFile(new File(getTempFolder()));
				return Status.OK_STATUS;
			}
		};
		deleteJob.setSystem(true);
		deleteJob.schedule();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static StaticHTMLPrviewPlugin getDefault() {
		return plugin;
	}

	protected void initializeImageRegistry(ImageRegistry registry) {
		registerImage(registry, IMG_NAV_FIRST, IMG_NAV_FIRST);
		registerImage(registry, IMG_NAV_PRE, IMG_NAV_PRE);
		registerImage(registry, IMG_NAV_NEXT, IMG_NAV_NEXT);
		registerImage(registry, IMG_NAV_LAST, IMG_NAV_LAST);
		registerImage(registry, IMG_NAV_GO, IMG_NAV_GO);
		registerImage(registry, IMG_PARAMS, IMG_PARAMS);
		registerImage(registry, IMG_TOC, IMG_TOC);
		registerImage(registry, IMG_FORM_TITLE, IMG_FORM_TITLE);
		registerImage(registry, IMG_TOC_LEAF, IMG_TOC_LEAF);
		registerImage(registry, IMG_RE_RUN, IMG_RE_RUN);
	}

	private void registerImage(ImageRegistry registry, String key, String fileName) {
		try {
			IPath path = new Path("icons/" + fileName); //$NON-NLS-1$
			URL url = find(path);
			if (url != null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
		}
	}

	public String getTempFolder() {
		return getPreferenceStore().getString(PreferenceConstants.TEMP_PATH);
	}

	public boolean isCleanTempfolder() {
		return getPreferenceStore().getBoolean(PreferenceConstants.CLEAM_TEMP);
	}
}
