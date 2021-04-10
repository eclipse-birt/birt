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

package org.eclipse.birt.report.debug.internal.ui.script.launcher;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.birt.report.debug.internal.core.launcher.ReportLauncher;
import org.eclipse.birt.report.debug.internal.ui.script.util.ScriptDebugUtil;
import org.eclipse.birt.report.debug.ui.DebugUI;
import org.eclipse.birt.report.debug.ui.i18n.Messages;
import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * ScriptMainTab
 */
public class ScriptMainTab extends AbstractLaunchConfigurationTab implements IReportLaunchConstants {

	private static final String DOCUMENT_FILE_EXT = "rptdocument"; //$NON-NLS-1$
	private static final String LIB_FOLDER = "lib"; //$NON-NLS-1$

	private static final String[] FILETYPE = new String[] { "jar", "zip" //$NON-NLS-1$ //$NON-NLS-2$
	};

	private static final String DEV_PROJECTNAME = "org.eclipse.birt.report.debug.core"; //$NON-NLS-1$

	private Text fMainText;
	private Text fTempFolder;
	private Text fEngineHome;

	private Button bOpenTarget;
	private Button bRun;
	private Button bRender;
	private Button bRunAndRender;
	private Button bRunPlusRender;
	private Button bDebugJava;
	private Button bDebugScript;
	private Button bDebugAll;

	private Button engineHomeSearchButton;
	private Button engineHomeVariableButton;

	private Combo cmbOutputFormat;
	private Button bUseDefaultEngineHome;

	private String lastUsedPath;

	private ModifyListener modifyListener = new ModifyListener() {

		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}
	};

	private SelectionListener taskTypeListener = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent e) {
			if (!((Button) e.widget).getSelection()) {
				return;
			}

			if (e.widget == bRun) {
				bRender.setSelection(false);
				bRunAndRender.setSelection(false);
				bRunPlusRender.setSelection(false);
			} else if (e.widget == bRender) {
				bRun.setSelection(false);
				bRunAndRender.setSelection(false);
				bRunPlusRender.setSelection(false);
			} else if (e.widget == bRunAndRender) {
				bRun.setSelection(false);
				bRender.setSelection(false);
				bRunPlusRender.setSelection(false);
			} else if (e.widget == bRunPlusRender) {
				bRun.setSelection(false);
				bRender.setSelection(false);
				bRunAndRender.setSelection(false);
			}

			bOpenTarget.setEnabled(e.widget != bRun);

			updateLaunchConfigurationDialog();
		}

	};

	private SelectionListener debugTypeListener = new SelectionAdapter() {

		public void widgetSelected(SelectionEvent e) {
			if (!((Button) e.widget).getSelection()) {
				return;
			}

			if (e.widget == bDebugJava) {
				bDebugScript.setSelection(false);
				bDebugAll.setSelection(false);
			} else if (e.widget == bDebugScript) {
				bDebugJava.setSelection(false);
				bDebugAll.setSelection(false);
			} else if (e.widget == bDebugAll) {
				bDebugJava.setSelection(false);
				bDebugScript.setSelection(false);
			}

			updateLaunchConfigurationDialog();
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.
	 * widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite parentG = ScriptSWTFactory.createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_BOTH);

		buildFileName(parentG);

		buildEngineHome(parentG);

		buildTempFolder(parentG);

		buildTypeArea(parentG);

		setControl(parentG);
	}

	private void buildTypeArea(Composite parentG) {
		Font font = parentG.getFont();
		Composite pane = ScriptSWTFactory.createComposite(parentG, font, 3, 1, GridData.FILL_HORIZONTAL);

		buildTaskType(pane);

		buildDebugType(pane);

		buildOutputArea(pane);
	}

	private void buildOutputArea(Composite parentG) {
		Font font = parentG.getFont();
		Group outputGroup = ScriptSWTFactory.createGroup(parentG, Messages.getString("ScriptMainTab.msg.output"), //$NON-NLS-1$
				2, 1, GridData.FILL_BOTH);

		ScriptSWTFactory.createLabel(outputGroup, Messages.getString("ScriptMainTab.msg.format"), font, 1); //$NON-NLS-1$

		cmbOutputFormat = ScriptSWTFactory.createCombo(outputGroup, font, 1);
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		gd.widthHint = 100;
		cmbOutputFormat.setLayoutData(gd);
		cmbOutputFormat.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateLaunchConfigurationDialog();
			}

		});

		bOpenTarget = ScriptSWTFactory.createCheckBox(outputGroup,
				Messages.getString("ScriptMainTab.msg.open.target.file")); //$NON-NLS-1$

		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.verticalIndent = 15;
		bOpenTarget.setLayoutData(gd);

		bOpenTarget.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateLaunchConfigurationDialog();
			}
		});

	}

	private void buildDebugType(Composite parentG) {
		Group debugTypeGroup = ScriptSWTFactory.createGroup(parentG, Messages.getString("ScriptMainTab.msg.debug.type"), //$NON-NLS-1$
				1, 1, GridData.FILL_BOTH);

		bDebugAll = ScriptSWTFactory.createRadioButton(debugTypeGroup,
				Messages.getString("ScriptMainTab.msg.debug.type.all")); //$NON-NLS-1$
		bDebugJava = ScriptSWTFactory.createRadioButton(debugTypeGroup,
				Messages.getString("ScriptMainTab.msg.debug.type.java")); //$NON-NLS-1$
		bDebugScript = ScriptSWTFactory.createRadioButton(debugTypeGroup,
				Messages.getString("ScriptMainTab.msg.debug.type.script")); //$NON-NLS-1$

		bDebugJava.addSelectionListener(debugTypeListener);
		bDebugScript.addSelectionListener(debugTypeListener);
		bDebugAll.addSelectionListener(debugTypeListener);
	}

	private void buildTaskType(Composite parentG) {
		Group taskTypeGroup = ScriptSWTFactory.createGroup(parentG, Messages.getString("ScriptMainTab.msg.task.type"), //$NON-NLS-1$
				1, 1, GridData.FILL_BOTH);

		bRunAndRender = ScriptSWTFactory.createRadioButton(taskTypeGroup,
				Messages.getString("ScriptMainTab.msg.task.type.run.and.render")); //$NON-NLS-1$
		bRun = ScriptSWTFactory.createRadioButton(taskTypeGroup, Messages.getString("ScriptMainTab.msg.task.type.run")); //$NON-NLS-1$
		bRender = ScriptSWTFactory.createRadioButton(taskTypeGroup,
				Messages.getString("ScriptMainTab.msg.task.type.render")); //$NON-NLS-1$
		bRunPlusRender = ScriptSWTFactory.createRadioButton(taskTypeGroup,
				Messages.getString("ScriptMainTab.msg.task.type.run.plus.render")); //$NON-NLS-1$

		bRunAndRender.addSelectionListener(taskTypeListener);
		bRun.addSelectionListener(taskTypeListener);
		bRender.addSelectionListener(taskTypeListener);
		bRunPlusRender.addSelectionListener(taskTypeListener);
	}

	private void buildTempFolder(Composite parentG) {
		Font font = parentG.getFont();
		Group tempFolderGroup = ScriptSWTFactory.createGroup(parentG,
				Messages.getString("ScriptMainTab.msg.temp.folder"), //$NON-NLS-1$
				2, 1, GridData.FILL_HORIZONTAL);
		Composite folder = ScriptSWTFactory.createComposite(tempFolderGroup, font, 3, 2, GridData.FILL_BOTH, 0, 0);
		fTempFolder = ScriptSWTFactory.createSingleText(folder, 1);
		fTempFolder.addModifyListener(modifyListener);

		Button forderSearchButton = createPushButton(folder, Messages.getString("ScriptMainTab.button.text.search"), //$NON-NLS-1$
				null);

		forderSearchButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				IPath chosen = chooseDropLocation(Messages.getString("ScriptMainTab.title.temp.folder"), //$NON-NLS-1$
						Messages.getString("ScriptMainTab.msg.select.temp.folder"), //$NON-NLS-1$
						fTempFolder.getText());
				if (chosen != null)
					fTempFolder.setText(chosen.toOSString());
			}
		});

		createVariableButton(folder, Messages.getString("ScriptMainTab.button.text.variables"), //$NON-NLS-1$
				fTempFolder);
	}

	private void buildEngineHome(Composite parentG) {
		Font font = parentG.getFont();
		Group engineHomeGroup = ScriptSWTFactory.createGroup(parentG,
				Messages.getString("ScriptMainTab.msg.engine.home"), //$NON-NLS-1$
				2, 1, GridData.FILL_HORIZONTAL);

		bUseDefaultEngineHome = ScriptSWTFactory.createCheckBox(engineHomeGroup,
				Messages.getString("ScriptMainTab.msg.use.default.engine.home")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 2;
		bUseDefaultEngineHome.setLayoutData(gd);
		bUseDefaultEngineHome.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateLaunchConfigurationDialog();

				fEngineHome.setEnabled(!bUseDefaultEngineHome.getSelection());
				engineHomeSearchButton.setEnabled(!bUseDefaultEngineHome.getSelection());
				engineHomeVariableButton.setEnabled(!bUseDefaultEngineHome.getSelection());
			}

		});

		Composite enginrHomeFolder = ScriptSWTFactory.createComposite(engineHomeGroup, font, 3, 2, GridData.FILL_BOTH,
				0, 0);
		fEngineHome = ScriptSWTFactory.createSingleText(enginrHomeFolder, 1);
		fEngineHome.addModifyListener(modifyListener);

		engineHomeSearchButton = createPushButton(enginrHomeFolder,
				Messages.getString("ScriptMainTab.button.text.search"), //$NON-NLS-1$
				null);

		engineHomeSearchButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				IPath chosen = chooseDropLocation(Messages.getString("ScriptMainTab.title.engine.home"), //$NON-NLS-1$
						Messages.getString("ScriptMainTab.msg.select.engine.home"), //$NON-NLS-1$
						fEngineHome.getText());
				if (chosen != null)
					fEngineHome.setText(chosen.toOSString());
			}
		});

		engineHomeVariableButton = createVariableButton(enginrHomeFolder,
				Messages.getString("ScriptMainTab.button.text.variables"), //$NON-NLS-1$
				fEngineHome);
	}

	private void buildFileName(Composite parentG) {
		Font font = parentG.getFont();
		Group mainGroup = ScriptSWTFactory.createGroup(parentG,
				Messages.getString("ScriptMainTab.msg.report.file.name"), //$NON-NLS-1$
				2, 1, GridData.FILL_HORIZONTAL);
		Composite comp = ScriptSWTFactory.createComposite(mainGroup, font, 3, 2, GridData.FILL_BOTH, 0, 0);
		fMainText = ScriptSWTFactory.createSingleText(comp, 1);
		fMainText.addModifyListener(modifyListener);

		Button fSearchButton = createPushButton(comp, Messages.getString("ScriptMainTab.button.text.search"), //$NON-NLS-1$
				null);
		fSearchButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent evt) {
				File chosen = chooseReportDesign();
				if (chosen != null)
					fMainText.setText(chosen.getAbsolutePath());
			}

		});
		createVariableButton(comp, Messages.getString("ScriptMainTab.button.text.variables"), //$NON-NLS-1$
				fMainText);
	}

	private IPath chooseDropLocation(String title, String message, String filterPath) {
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		dialog.setFilterPath(filterPath);
		dialog.setText(title);
		dialog.setMessage(message);
		String res = dialog.open();
		if (res != null) {
			return new Path(res);
		}
		return null;
	}

	private File chooseReportDesign() {
		if (lastUsedPath == null) {
			lastUsedPath = ""; //$NON-NLS-1$
		}
		FileDialog dialog = new FileDialog(getShell(), SWT.SINGLE);
		dialog.setText(Messages.getString("ScriptMainTab.msg.select.report.file")); //$NON-NLS-1$

		if (bRender.getSelection()) {
			dialog.setFilterExtensions(new String[] { "*." + DOCUMENT_FILE_EXT }); //$NON-NLS-1$
		} else {
			dialog.setFilterExtensions(new String[] { "*." + IReportElementConstants.DESIGN_FILE_EXTENSION }); //$NON-NLS-1$
		}

		dialog.setFilterPath(lastUsedPath);
		String res = dialog.open();
		if (res == null) {
			return null;
		}
		String[] fileNames = dialog.getFileNames();

		IPath filterPath = new Path(dialog.getFilterPath());
		lastUsedPath = dialog.getFilterPath();

		IPath path = null;
		for (int i = 0; i < 1; i++) {
			path = filterPath.append(fileNames[i]).makeAbsolute();
			return path.toFile();
		}
		return null;
	}

	public String getName() {
		return Messages.getString("ScriptMainTab.title.report"); //$NON-NLS-1$
	}

	public Image getImage() {
		return DebugUI.getDefault().getImageRegistry().get(DebugUI.IMAGE_DEBUGGER_ICON_NAME);
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		try {

			fMainText.setText(configuration.getAttribute(ATTR_REPORT_FILE_NAME, getDefaultFileName()));
			fEngineHome.setText(configuration.getAttribute(ATTR_ENGINE_HOME, "")); //$NON-NLS-1$
			fTempFolder.setText(configuration.getAttribute(ATTR_TEMP_FOLDER, getDefaultTempFolder()));

			bUseDefaultEngineHome.setSelection(configuration.getAttribute(ATTR_USE_DEFULT_ENGINE_HOME, true));
			fEngineHome.setEnabled(!bUseDefaultEngineHome.getSelection());
			engineHomeSearchButton.setEnabled(!bUseDefaultEngineHome.getSelection());
			engineHomeVariableButton.setEnabled(!bUseDefaultEngineHome.getSelection());

			ReportEngine engine = new ReportEngine(new EngineConfig());
			String[] supportedFormats;// = engine.getSupportedFormats( );
			EmitterInfo[] emitters = engine.getEmitterInfo();
			if (emitters == null || emitters.length == 0) {
				supportedFormats = new String[] {};
			} else {
				List<String> temp = new ArrayList<String>();
				for (int i = 0; i < emitters.length; i++) {
					EmitterInfo info = emitters[i];
					if (!info.isHidden()) {
						temp.add(info.getFormat());
					}
				}
				Collections.sort(temp, new AlphabeticallyComparator());
				supportedFormats = temp.toArray(new String[temp.size()]);
			}
			cmbOutputFormat.setItems(supportedFormats);

			String targetFormat = configuration.getAttribute(ATTR_TARGET_FORMAT, DEFAULT_TARGET_FORMAT);

			int idx = Arrays.asList(supportedFormats).indexOf(targetFormat);
			if (idx != -1) {
				cmbOutputFormat.select(idx);
			} else if (supportedFormats.length > 0) {
				cmbOutputFormat.select(0);
			}

			bOpenTarget.setSelection(configuration.getAttribute(ATTR_OPEN_TARGET, false));

			int debugType = configuration.getAttribute(ATTR_DEBUG_TYPE, DEFAULT_DEBUG_TYPE);
			int taskType = configuration.getAttribute(ATTR_TASK_TYPE, DEFAULT_TASK_TYPE);

			bDebugAll.setSelection(false);
			bDebugJava.setSelection(false);
			bDebugScript.setSelection(false);

			bRunAndRender.setSelection(false);
			bRun.setSelection(false);
			bRender.setSelection(false);
			bRunPlusRender.setSelection(false);

			if ((debugType & DEBUG_TYPE_ALL) == DEBUG_TYPE_ALL) {
				bDebugAll.setSelection(true);
			} else if ((debugType & DEBUG_TYPE_JAVA_CLASS) == DEBUG_TYPE_JAVA_CLASS) {
				bDebugJava.setSelection(true);
			} else {
				bDebugScript.setSelection(true);
			}

			if ((taskType & TASK_TYPE_RUN_PLUS_RENDER) == TASK_TYPE_RUN_PLUS_RENDER) {
				bRunPlusRender.setSelection(true);
			} else if ((taskType & TASK_TYPE_RUN) == TASK_TYPE_RUN) {
				bRun.setSelection(true);
			} else if ((taskType & TASK_TYPE_RENDER) == TASK_TYPE_RENDER) {
				bRender.setSelection(true);
			} else {
				bRunAndRender.setSelection(true);
			}

			bOpenTarget.setEnabled(!bRun.getSelection());

		} catch (CoreException e) {
		}
	}

	private String getDefaultFileName() {
		ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		if (handle != null) {
			return handle.getFileName();
		}
		return ""; //$NON-NLS-1$
	}

	private String getDefaultTempFolder() {
		return System.getProperty("java.io.tmpdir", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(ATTR_REPORT_FILE_NAME, fMainText.getText());
		configuration.setAttribute(ATTR_ENGINE_HOME, fEngineHome.getText());
		configuration.setAttribute(ATTR_TEMP_FOLDER, fTempFolder.getText());

		configuration.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
				ReportLauncher.class.getName());

		if (cmbOutputFormat.getItemCount() > 0 && cmbOutputFormat.getSelectionIndex() != -1) {
			configuration.setAttribute(ATTR_TARGET_FORMAT,
					cmbOutputFormat.getItem(cmbOutputFormat.getSelectionIndex()));
		}

		configuration.setAttribute(ATTR_OPEN_TARGET, bOpenTarget.getSelection());
		configuration.setAttribute(ATTR_USE_DEFULT_ENGINE_HOME, bUseDefaultEngineHome.getSelection());

		int debugType = DEFAULT_DEBUG_TYPE;
		if (bDebugJava.getSelection()) {
			debugType = DEBUG_TYPE_JAVA_CLASS;
		} else if (bDebugScript.getSelection()) {
			debugType = DEBUG_TYPE_JAVA_SCRIPT;
		} else if (bDebugAll.getSelection()) {
			debugType = DEBUG_TYPE_ALL;
		}

		int taskType = DEFAULT_TASK_TYPE;
		if (bRunAndRender.getSelection()) {
			taskType = TASK_TYPE_RUN_AND_RENDER;
		} else if (bRunPlusRender.getSelection()) {
			taskType = TASK_TYPE_RUN_PLUS_RENDER;
		} else if (bRun.getSelection()) {
			taskType = TASK_TYPE_RUN;
		} else if (bRender.getSelection()) {
			taskType = TASK_TYPE_RENDER;
		}

		configuration.setAttribute(ATTR_DEBUG_TYPE, debugType);
		configuration.setAttribute(ATTR_TASK_TYPE, taskType);

		if (bUseDefaultEngineHome.getSelection()) {
			configuration.setAttribute(IReportLaunchConstants.ATTR_CLASSPATH, (List) null);
		} else {
			List list = new ArrayList();

			String libDirectory = getJarFolder();
			try {
				libDirectory = ScriptDebugUtil.getSubstitutedString(libDirectory);
			} catch (CoreException e) {
				libDirectory = getJarFolder();
			}

			addEngineLibHomeClassPath(libDirectory, list);

			addProjectClassPath(list);

			configuration.setAttribute(IReportLaunchConstants.ATTR_CLASSPATH, list);
		}
	}

	private void addEngineLibHomeClassPath(String fileDirectory, List list) {
		File file = new File(fileDirectory);
		if (!file.exists() || !file.isDirectory()) {
			return;
		}

		IPath path = new Path(fileDirectory);

		String[] files = file.list();
		if (files == null) {
			return;
		}
		int len = files.length;
		for (int i = 0; i < len; i++) {
			IPath temp = path.append(files[i]).makeAbsolute();
			if (temp.toFile().exists() && isJarFile(temp)) {
				try {
					IRuntimeClasspathEntry entry = JavaRuntime.newArchiveRuntimeClasspathEntry(temp);
					String mometo = entry.getMemento();
					if (!list.contains(mometo)) {
						list.add(mometo);
					}
				} catch (CoreException e) {

				}
			}
		}
	}

	private void addProjectClassPath(List list) {
		try {
			String path = ScriptDebugUtil.getPlugInFile(DEV_PROJECTNAME);
			if (path != null) {
				IRuntimeClasspathEntry entry = JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(path));
				String mometo = entry.getMemento();
				if (!list.contains(mometo)) {
					list.add(mometo);
				}
			}
		} catch (CoreException e) {
		}

		String coreBin = System.getProperty(ReportStandardAppLaunchDelegate.CORE_BIN);
		if (coreBin != null) {
			IRuntimeClasspathEntry entry;
			try {
				entry = JavaRuntime.newArchiveRuntimeClasspathEntry(new Path(coreBin));
				String mometo = entry.getMemento();
				if (!list.contains(mometo)) {
					list.add(mometo);
				}
			} catch (CoreException e) {
			}
		}

	}

	private boolean isJarFile(IPath path) {
		String str = path.getFileExtension();
		for (int i = 0; i < FILETYPE.length; i++) {
			if (FILETYPE[i].equals(str)) {
				return true;
			}
		}
		return false;
	}

	private String getJarFolder() {
		String str = fEngineHome.getText();
		File file = new File(str);
		return file.getAbsolutePath() + File.separator + LIB_FOLDER;
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(ATTR_REPORT_FILE_NAME, getDefaultFileName());
		configuration.setAttribute(ATTR_ENGINE_HOME, ""); //$NON-NLS-1$
		configuration.setAttribute(ATTR_TEMP_FOLDER, getDefaultTempFolder());
		configuration.setAttribute(ATTR_OPEN_TARGET, false);
		configuration.setAttribute(ATTR_TARGET_FORMAT, DEFAULT_TARGET_FORMAT);
		configuration.setAttribute(ATTR_USE_DEFULT_ENGINE_HOME, true);
		configuration.setAttribute(ATTR_DEBUG_TYPE, DEFAULT_DEBUG_TYPE);
		configuration.setAttribute(ATTR_TASK_TYPE, DEFAULT_TASK_TYPE);
	}

	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);

		if (!validateReportDesignFile(launchConfig)) {
			return false;
		}

		if (!validateEngineHome(launchConfig)) {
			return false;
		}

		if (!validateTempFloder(launchConfig)) {
			return false;
		}

		return true;
	}

	private boolean validateTempFloder(ILaunchConfiguration launchConfig) {
		String str = fTempFolder.getText();
		try {
			str = ScriptDebugUtil.getSubstitutedString(str);
		} catch (CoreException e) {
			str = fEngineHome.getText();
		}
		if (str == null || str.length() == 0) {
			setErrorMessage(Messages.getString("ScriptMainTab.error.msg.no.temp.folder")); //$NON-NLS-1$
			return false;
		}
		File file = new File(str);
		if (!file.exists()) {
			setErrorMessage(Messages.getString("ScriptMainTab.error.msg.nonexist.temp.folder")); //$NON-NLS-1$
			return false;
		}
		if (!file.isDirectory()) {
			setErrorMessage(Messages.getString("ScriptMainTab.error.msg.invalid.temp.folder")); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	private boolean validateEngineHome(ILaunchConfiguration launchConfig) {
		if (bUseDefaultEngineHome.getSelection()) {
			return true;
		}

		String str = fEngineHome.getText();
		try {
			str = ScriptDebugUtil.getSubstitutedString(str);
		} catch (CoreException e) {
			str = fEngineHome.getText();
		}
		if (str == null || str.length() == 0) {
			setErrorMessage(Messages.getString("ScriptMainTab.error.msg.no.engine.home")); //$NON-NLS-1$
			return false;
		}
		File file = new File(str);
		if (!file.exists()) {
			setErrorMessage(Messages.getString("ScriptMainTab.error.msg.nonexist.engine.home")); //$NON-NLS-1$
			return false;
		}
		if (!file.isDirectory()) {
			setErrorMessage(Messages.getString("ScriptMainTab.error.msg.invalid.engine.home")); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	private boolean validateReportDesignFile(ILaunchConfiguration launchConfig) {
		String str = fMainText.getText();
		try {
			str = ScriptDebugUtil.getSubstitutedString(str);
		} catch (CoreException e) {
		}
		if (str == null || str.length() == 0) {
			setErrorMessage(Messages.getString("ScriptMainTab.error.msg.no.report.file")); //$NON-NLS-1$
			return false;
		}
		File file = new File(str);
		if (!file.exists() || !file.isFile()) {
			setErrorMessage(Messages.getString("ScriptMainTab.error.msg.nonexist.report.file")); //$NON-NLS-1$
			return false;
		}

		if (bRender.getSelection()) {
			if (!DOCUMENT_FILE_EXT.equalsIgnoreCase(new Path(str).getFileExtension())) {
				setErrorMessage(Messages.getString("ScriptMainTab.error.msg.invalid.document.file")); //$NON-NLS-1$
				return false;
			}
		} else if (!IReportElementConstants.DESIGN_FILE_EXTENSION.equalsIgnoreCase(new Path(str).getFileExtension())) {
			setErrorMessage(Messages.getString("ScriptMainTab.error.msg.invalid.report.file")); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	private Button createVariableButton(Composite parent, String text, final Text source) {
		Button button = createPushButton(parent, text, null);
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent evt) {
				StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(source.getShell());
				if (dialog.open() == Window.OK)
					source.insert(dialog.getVariableExpression());
			}

		});

		return button;
	}
}
