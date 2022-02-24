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

package org.eclipse.birt.build.ant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Manifest;
import org.eclipse.birt.build.framework.Bundle;
import org.eclipse.birt.build.framework.Framework;
import org.eclipse.birt.build.framework.FrameworkException;
import org.eclipse.birt.build.pack.Filter;
import org.eclipse.birt.build.pack.FrameworkPacker;

/**
 * The sample build script is:
 * 
 * <pre>
 * &lt;RuntimePack input="platform" output="dir.jar">
 *   &lt;jar name="">
 *     &lt;bundle name="">
 *       &lt;include pattern=""/>
 *       &lt;exclude pattern=""/>
 *     &lt;/bundle>
 *     &lt;bundle name=""/>
 *   &lt;/jar>
 *   &lt;jar name="">
 *     &lt;bundle name=""/>
 *     &lt;bundle name=""/>
 *  &lt;/jar>
 * &lt;/RuntimePack>
 * </pre>
 */
public class PackTask extends Task {

	private String baseDir;
	private String output;
	private Manifest manifest;
	private ArrayList<BundleItem> bundleItems = new ArrayList<BundleItem>();

	public PackTask() {
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public Object createBundle() {
		BundleItem bundle = new BundleItem();
		bundleItems.add(bundle);
		return bundle;
	}

	public void addManifest(Manifest manifest) {
		this.manifest = manifest;
	}

	public void execute() throws BuildException {
		log("create " + output + " from " + baseDir, Project.MSG_INFO);
		Framework framework = new Framework();
		FrameworkPacker packer = new FrameworkPacker(framework);
		try {
			if (bundleItems.isEmpty()) {
				// try to load all data as bundle
				bundleItems.addAll(loadBundleItems(baseDir));
			}
			if (bundleItems.isEmpty()) {
				throw new BuildException("failed to load bundle from directory:" + baseDir);
			}
			for (BundleItem bundleItem : bundleItems) {
				String bundleName = bundleItem.getName();
				File bundleFile = getFile(baseDir, bundleName);
				if (bundleFile != null) {
					try {
						log("load bundle " + bundleName + " from " + bundleFile.getName(), Project.MSG_INFO);
						Bundle bundle = framework.addBundle(bundleFile);
						// create the
						Filter filter = createFilter(bundleItem);
						packer.setFilter(bundle.getBundleID(), filter);
					} catch (FrameworkException ex) {
						log("failed to load the bundle" + bundleName, ex, Project.MSG_WARN);
					}
				} else {
					log("can't find bundle " + bundleName, Project.MSG_WARN);
				}
			}

			ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(output));
			try {
				packer.pack(zipOutput);
				// write the manifest

				if (manifest != null) {
					ZipEntry entry = new ZipEntry("META-INF/MANIFEST.MF");
					zipOutput.putNextEntry(entry);
					try {
						PrintWriter writer = new PrintWriter(zipOutput);
						manifest.write(writer);
						writer.flush();
					} finally {
						zipOutput.close();
					}
				}
			} finally {
				zipOutput.close();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new BuildException(ex);
		} finally {
			framework.close();
		}
	}

	protected List<BundleItem> loadBundleItems(final String baseDir) {
		ArrayList<BundleItem> bundles = new ArrayList<BundleItem>();
		System.out.println("load bundles from folder" + baseDir);
		File parent = new File(baseDir);
		File[] files = parent.listFiles();
		if (files != null) {
			for (File file : files) {
				String bundleName = file.getName();
				if (file.isFile()) {
					if (!bundleName.endsWith(".jar")) {
						continue;
					}
					bundleName = bundleName.substring(0, bundleName.length() - 4);
				}
				int pos = bundleName.indexOf('_');
				if (pos != -1) {
					bundleName = bundleName.substring(0, pos);
				}
				BundleItem bundle = new BundleItem();
				bundle.setName(bundleName);
				bundles.add(bundle);
			}
		}
		return bundles;
	}

	protected File getFile(final String baseDir, final String name) {
		File parent = new File(baseDir);
		File[] files = parent.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String fileName) {
				if (fileName.startsWith(name + "_")) {
					return true;
				}
				return false;
			}
		});
		if (files != null && files.length > 0) {
			return files[0];
		}
		return null;
	}

	private static Filter DEFAULT_FILTER = new Filter(new String[] {},
			new String[] { "plugin\\..*", "fragment\\..*", "(.*\\.jar/)?META-INF/MANIFEST\\.MF",
					"(.*\\.jar/)?META-INF/ECLIPSEF\\..*", "(.*\\.jar/)?META-INF/eclipse\\.inf", "about\\..*",
					"about_files/.*" });

	public Filter createFilter(BundleItem bundleItem) {
		String[] includes = getPatterns(bundleItem.getIncludeFilters());
		String[] excludes = getPatterns(bundleItem.getExcludeFilters());
		return new Filter(DEFAULT_FILTER, includes, excludes);
	}

	protected String[] getPatterns(List<FilterItem> filters) {
		if (filters == null) {
			return new String[] {};
		}
		String[] patterns = new String[filters.size()];
		for (int i = 0; i < filters.size(); i++) {
			patterns[i] = filters.get(i).getPattern();
		}

		return patterns;
	}

}
