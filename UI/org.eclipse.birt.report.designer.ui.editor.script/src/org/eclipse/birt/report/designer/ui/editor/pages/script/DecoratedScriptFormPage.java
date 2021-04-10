/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editor.pages.script;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor;
import org.eclipse.birt.report.designer.internal.ui.editors.script.JSEditor;
import org.eclipse.birt.report.designer.ui.editor.script.DecoratedScriptEditor;
import org.eclipse.birt.report.designer.ui.editor.script.IDebugScriptEditor;
import org.eclipse.birt.report.designer.ui.editor.script.ScriptDocumentProvider;
import org.eclipse.birt.report.designer.ui.editor.script.ScriptDocumentProvider.DebugResourceMarkerAnnotationModel;
import org.eclipse.birt.report.designer.ui.editors.IReportScriptLocation;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportScriptFormPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CommandNotMappedException;
import org.eclipse.ui.actions.ContributedAction;
import org.eclipse.ui.texteditor.ConfigurationElementSorter;

/**
 * A script editor page comprising functionality not present in the leaner
 * <code>ReportScriptFormPage</code>, but used in many heavy weight (and
 * especially source editing) editors, such as line numbers, change ruler,
 * overview ruler, print margins, current line highlighting, etc.
 */
public class DecoratedScriptFormPage extends ReportScriptFormPage {

	private static final String TAG_CONTRIBUTION_TYPE = "editorContribution"; //$NON-NLS-1$
	private static final String ID = "org.eclipse.birt.report.designer.ui.editor.script.DecoratedScriptEditor"; //$NON-NLS-1$
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.pages.ReportScriptFormPage#
	 * createEditor()
	 */

	protected IEditorPart createJSEditor() {
		return new DebugJSEditor(this);
	}

	/**
	 * ReportDecoratedScriptEditor
	 */
	private class ReportDecoratedScriptEditor extends DecoratedScriptEditor implements IDebugScriptEditor {
		private String fileName = "";//$NON-NLS-1$

		/**
		 * Constructs the editor with a specified parent.
		 * 
		 * @param parent the parent editor.
		 */
		public ReportDecoratedScriptEditor(IEditorPart parent) {
			super(parent);
			setRulerContextMenuId("#ReportScriptRulerContext"); //$NON-NLS-1$
		}

		public IAction getAction(String actionID) {
			IAction action = super.getAction(actionID);

			if (action == null) {
				action = findContributedAction(actionID);
				if (action != null)
					setAction(actionID, action);
			}
			return action;
		}

		private IAction findContributedAction(String actionID) {
			List actions = new ArrayList();
			IConfigurationElement[] elements = Platform.getExtensionRegistry()
					.getConfigurationElementsFor(PlatformUI.PLUGIN_ID, "editorActions"); //$NON-NLS-1$
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement element = elements[i];
				if (TAG_CONTRIBUTION_TYPE.equals(element.getName())) {
					if (!ID.equals(element.getAttribute("targetID"))) //$NON-NLS-1$
						continue;

					IConfigurationElement[] children = element.getChildren("action"); //$NON-NLS-1$
					for (int j = 0; j < children.length; j++) {
						IConfigurationElement child = children[j];
						if (actionID.equals(child.getAttribute("actionID"))) //$NON-NLS-1$
							actions.add(child);
					}
				}
			}
			int actionSize = actions.size();
			if (actionSize > 0) {
				IConfigurationElement element;
				if (actionSize > 1) {
					IConfigurationElement[] actionArray = (IConfigurationElement[]) actions
							.toArray(new IConfigurationElement[actionSize]);
					ConfigurationElementSorter sorter = new ConfigurationElementSorter() {
						/*
						 * @see
						 * org.eclipse.ui.texteditor.ConfigurationElementSorter#getConfigurationElement(
						 * java.lang.Object)
						 */
						public IConfigurationElement getConfigurationElement(Object object) {
							return (IConfigurationElement) object;
						}
					};
					sorter.sort(actionArray);
					element = actionArray[0];
				} else
					element = (IConfigurationElement) actions.get(0);

				try {
					return new ContributedAction(getSite(), element);
				} catch (CommandNotMappedException e) {
					// out of luck, no command action mapping
				}
			}

			return null;
		}

		public Object getAdapter(Class adapter) {
			if (adapter == IReportScriptLocation.class) {
				IEditorPart parent = getParent();

				return parent == null ? null : parent.getAdapter(adapter);
			}
			return super.getAdapter(adapter);
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor#
		 * updateScipt(java.lang.String)
		 */
		public void updateScipt(PropertyHandle handle) {
			ScriptDocumentProvider provider = (ScriptDocumentProvider) getDocumentProvider();
			provider.update(provider.getAnnotationModel(getEditorInput()));
			((DebugResourceMarkerAnnotationModel) provider.getAnnotationModel(getEditorInput())).resetReportMarkers();

		}

		public void beforeChangeContents(PropertyHandle handle) {
			ScriptDocumentProvider provider = (ScriptDocumentProvider) getDocumentProvider();
			String id = ModuleUtil.getScriptUID(handle);
			boolean isSame = provider.getId().equals(id);
			if (!isSame) {
				jsEditor.doSave(null);
			}
			if (id == null) {
				provider.setId("");
			} else {
				provider.setId(id);
			}
			provider.setSameElement(isSame);
			if (handle != null) {
				provider.setFileName(handle.getElementHandle().getModuleHandle().getFileName());
			}

			((DebugResourceMarkerAnnotationModel) provider.getAnnotationModel(getEditorInput())).beforeChangeText();
		}
	}

	private class DebugJSEditor extends JSEditor {
		public DebugJSEditor(IEditorPart parent) {
			super(parent);
		}

		@Override
		public void createPartControl(Composite parent) {
			super.createPartControl(parent);
			hideValidateButtonIcon();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.script.JSEditor#
		 * createScriptEditor()
		 */
		protected IScriptEditor createScriptEditor() {
			return new ReportDecoratedScriptEditor(getParentEditor());
		}

		public Object getAdapter(Class adapter) {
			if (adapter == IReportScriptLocation.class) {
				final PropertyHandle handle = getPropertyHandle();
				if (handle == null) {
					return null;
				}

				return new IReportScriptLocation() {

					public String getID() {
						return ModuleUtil.getScriptUID(handle);
					}

					public int getLineNumber() {
						return -1;
					}

					public String getReportFileName() {
						return handle.getElementHandle().getModuleHandle().getFileName();
					}

					public String getDisplayName() {
						return DEUtil.getFlatHirarchyPathName(handle.getElementHandle()) + "." //$NON-NLS-1$
								+ handle.getDefn().getName();
					}

				};
			}
			return super.getAdapter(adapter);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.script.JSEditor#
		 * setEditorText(java.lang.String)
		 */
		protected void setEditorText(String text) {
			final PropertyHandle handle = getPropertyHandle();
			if (getScriptEditor() instanceof IDebugScriptEditor) {
				((IDebugScriptEditor) getScriptEditor()).beforeChangeContents(handle);
			}
			super.setEditorText(text);

			if (getScriptEditor() instanceof IDebugScriptEditor) {
				((IDebugScriptEditor) getScriptEditor()).updateScipt(handle);
			}
		}

		public void doSave(IProgressMonitor monitor, boolean chnageText) {
			super.doSave(monitor, chnageText);
			if (getScriptEditor() instanceof IDebugScriptEditor) {
				((IDebugScriptEditor) getScriptEditor()).saveDocument();
			}
		}

		@Override
		public void doSave(IProgressMonitor monitor) {
			super.doSave(monitor);
			if (getScriptEditor() instanceof IDebugScriptEditor) {
				((IDebugScriptEditor) getScriptEditor()).saveDocument();
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.script.JSEditor#
		 * getScriptEditor()
		 */
		protected IScriptEditor getScriptEditor() {
			return super.getScriptEditor();
		}
	}
}
