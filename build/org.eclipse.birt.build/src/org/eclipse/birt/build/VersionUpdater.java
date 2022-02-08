
package org.eclipse.birt.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.Manifest;
import java.lang.Integer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Replace;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;
import org.dom4j.DocumentFactory;
import org.dom4j.io.XMLWriter;
import org.dom4j.io.OutputFormat;

/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

/**
 * Update Birt exported plugin version.
 * 
 * @author Rock Yu
 * 
 */
public class VersionUpdater extends Task {

	/**
	 * Project(plugin) folder, "plugin.xml" or "MANIFEST.MF" are under it.
	 */

	private File projectPath = null;

	/**
	 * checkFlag=N, update with suffix directly. If checkFlag=Y, check the cvs diff
	 * log.
	 */
	private String checkFlag = null;

	/**
	 * CVS diff logs are under it.
	 */
	private File cvsLogPath = null;
	/**
	 * CVS control logs are under it.
	 */
	private File cvsControlPath = null;

	/**
	 * New CVS diff property logs need to be kept under it.
	 */
	private File logPath = null;

	/**
	 * plug-in id which need to be updated.
	 */
	private String plugId = null;

	/**
	 * DaysInPast indicates how long the code hasn't been modified.
	 */
	private int daysInPast = 1;

	/**
	 * Suffix.
	 */
	private String suffix = null;

	/**
	 * oldVersion. The version of last update
	 */
	private String oldVersion = null;

	boolean pluginTagStart = false;

	String pluginId = null;
	String pluginVersion = null;

	/**
	 * Set of dtp plugins, each item is a DTP plugin id.
	 */

	private static Set dtpPlugins = null;

	private static String ENTRYNAME = "entry";

	/**
	 * Path of the project.
	 * 
	 * @param path
	 */
	public void setProjectPath(File path) {
		this.projectPath = path;
	}

	public void setCvsLogPath(File cvsLogPath) {
		this.cvsLogPath = cvsLogPath;
	}

	public void setCvsControlPath(File cvsControlPath) {
		this.cvsControlPath = cvsControlPath;
	}

	/**
	 * For OSGI plugins,
	 */

	public void execute() {
		if (this.projectPath == null) {
			throw new BuildException("Please specify the correct projectPath."); //$NON-NLS-1$
		}

		// read in plugin version and id.

		File manifestFile = new File(new File(projectPath, "META-INF"), "MANIFEST.MF"); //$NON-NLS-1$ //$NON-NLS-2$

		if (manifestFile.exists())
			this.updateManifest(manifestFile);
	}

	/**
	 * Update OSGI plugins which define some of the project attributes in the
	 * manifest file.
	 * 
	 * @param manifest
	 * @throws IOException
	 * @throws FileNotFoundException
	 */

	private void updateManifest(File manifest) {
		// add by guxy

		Replace replace = new Replace();
		replace.setProject(getProject());
		replace.setFile(manifest);

		replace.setToken(".qualifier"); //$NON-NLS-1$

		if (checkFlag.equals("N")) {
			replace.setValue(suffix);
		} else {
			getLastVersion(this.cvsControlPath);
			if (ifUpdate(cvsLogPath)) {
				// create DaysInPast entry with new value and new daysinpast
				replace.setValue(suffix);
				this.handleErrorOutput("Update plug-in [" + this.plugId + "] to new version :" + this.suffix); //$NON-NLS-1$
				genVersionLog(logPath, plugId, suffix, 1);

			} else {
				// create DaysInPast entry with old value and new daysinpast
				replace.setValue(oldVersion);
				this.handleErrorOutput("Update plug-in [" + this.plugId + "] to old version :" + this.oldVersion); //$NON-NLS-1$
				genVersionLog(logPath, plugId, oldVersion, daysInPast + 1);
			}
		}
		replace.execute();

		String pluginId = null;
		String pluginVersion = null;
		Manifest mani = new Manifest();
		if (manifest.exists()) {
			InputStream is = null;
			try {
				is = new FileInputStream(manifest);
				mani.read(is);
			} catch (Exception e) {
				e.printStackTrace();
				throw new BuildException(e);
			} finally {
				try {
					is.close();
				} catch (Exception e) {
				}
			}

			pluginId = StringUtil.trimString(mani.getMainAttributes().getValue("Bundle-SymbolicName")); //$NON-NLS-1$
			if (pluginId != null && pluginId.indexOf(';') != -1) {
				int index = pluginId.indexOf(';');
				pluginId = StringUtil.trimString(pluginId.substring(0, index));
			}

			pluginVersion = StringUtil.trimString(mani.getMainAttributes().getValue("Bundle-Version")); //$NON-NLS-1$
		}

		if (pluginId == null)
			this.handleErrorOutput("Can not find Bundle-SymbolicName for manifest under " + this.projectPath); //$NON-NLS-1$
		else if (pluginVersion == null)
			this.handleErrorOutput("Can not identify Bundle-Version for manifest under " + this.projectPath); //$NON-NLS-1$

	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	/**
	 * Set the suffix that will be appended to the version of the plugin. For
	 * example, if current plugin.version="2.0.0", suffix is ".v20060228-1725", the
	 * out of the version will be "2.0.0.v20060228-1725"
	 * 
	 * @param suffix
	 */

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Set the daysInPast, which will be write to CVS information file. If the code
	 * hasn't been changed, (daysInPast+1) will be written to the file, or "1" will
	 * be written to the log file.
	 * 
	 * @param daysInpast
	 */

	public void setDaysInPast(int daysInPast) {
		this.daysInPast = daysInPast;
	}

	/**
	 * Set the oldVersion, which indicates the timestamp used last time. If the code
	 * hasn't been changed, it will be written to the cvs information file again, or
	 * the new timestamp(suffix) will be written to the cvs information file.
	 * 
	 * @param oldVersion
	 */

	public void setOldVersion(String oldVersion) {
		this.oldVersion = oldVersion;
	}

	/**
	 * Set the logpath, the CVS infomation logs will be generated under that folder.
	 * the name of these logs will be ${projectName}_DaysInPast.xml. They will be
	 * checked into CVS in top build script.
	 * 
	 * @param logPath
	 */

	public void setLogPath(File logPath) {
		this.logPath = logPath;
	}

	/**
	 * set the plugId , the id of the plug-in which need to be updated.
	 * 
	 * @param plugId
	 */
	public void setPlugId(String plugId) {
		this.plugId = plugId;
	}

	/**
	 * check the cvslog file to decide if to update
	 * 
	 * @author
	 * @param xmlf
	 * @return
	 */
	private boolean ifUpdate(File xmlf) {
		String name = "";
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(this.cvsLogPath);
		} catch (org.dom4j.DocumentException dex) {
			dex.printStackTrace();
		}
		// check if "entry" exist in CvsDiffLog file
		Element rootElement = document.getRootElement();
		Iterator it = rootElement.elementIterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			name = element.getName();
			if (name.equalsIgnoreCase(ENTRYNAME)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @author
	 * @param folderPath
	 * @param plug_id
	 * @param lastDate
	 * @param dayInpast
	 */
	private void genVersionLog(File folderPath, String plug_id, String lastDate, int dayInpast) {
		// gen the dest file path
		String parentPath = folderPath.getAbsolutePath();
		String fileName = plug_id + "_DayInPast" + ".xml";
		String fullName = parentPath + "/" + fileName;
		File dest = new File(fullName);
		System.out.println("dest file full path:\t" + fullName);
		try {
			// genarate document factory
			DocumentFactory factory = new DocumentFactory();
			// create root element
			DOMElement rootElement = new DOMElement("plugin");
			rootElement.setAttribute("id", plug_id);
			// add child:lastdate
			DOMElement dateElement = new DOMElement("LastDate");
			dateElement.setText(lastDate);
			rootElement.add(dateElement);
			// add child:dayinpast
			DOMElement dayElement = new DOMElement("DayInPast");
			dayElement.setText(Integer.toString(dayInpast));
			rootElement.add(dayElement);
			// gen the doc
			Document doc = factory.createDocument(rootElement);

			// PrettyFormat
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter(new FileWriter(dest), format);
			writer.write(doc);
			writer.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void getLastVersion(File controlpath) {
		System.out.println("old version file path: " + controlpath.getAbsolutePath());
		String dayTag = "DayInPast";
		String versionTag = "LastDate";
		String name;

		/*
		 * this.setCvsControlPath(controlpath);
		 */
		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(this.cvsControlPath);
		} catch (org.dom4j.DocumentException dex) {
			dex.printStackTrace();
		}
		Element rootElement = document.getRootElement();
		Iterator it = rootElement.elementIterator();
		while (it.hasNext()) {
			Element element = (Element) it.next();
			name = element.getName();
			if (name.equalsIgnoreCase(versionTag)) {
				this.oldVersion = element.getText();
			} else if (name.equalsIgnoreCase(dayTag)) {
				this.daysInPast = Integer.valueOf(element.getText().trim()).intValue();

			}
		}

	}

}
