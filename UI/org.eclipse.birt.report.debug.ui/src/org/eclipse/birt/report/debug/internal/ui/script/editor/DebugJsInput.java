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

package org.eclipse.birt.report.debug.internal.ui.script.editor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.birt.report.designer.internal.ui.editors.IStorageEditorInput;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.ibm.icu.util.ULocale;

/**
 * Input for the DebugJSEditor
 */

public class DebugJsInput implements IStorageEditorInput, IPathEditorInput, IPersistableElement {

	private static final String ERRORMSG = "There is no script available."; //$NON-NLS-1$
	private ModuleHandle handle;
	private File storage;
	private String id;

	/**
	 * Contructor
	 *
	 * @param storage
	 * @param id
	 */
	public DebugJsInput(File storage, String id) {
		this.storage = storage;
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.IStorageEditorInput#
	 * getStorage()
	 */
	@Override
	public IStorage getStorage() throws CoreException {
		return new ScriptStorage();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	@Override
	public boolean exists() {
		return storage.exists();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	@Override
	public String getName() {
		return storage.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	@Override
	public IPersistableElement getPersistable() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return storage.getAbsolutePath();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IPathEditorInput#getPath()
	 */
	@Override
	public IPath getPath() {
		return new Path(storage.getAbsolutePath());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IPersistableElement#getFactoryId()
	 */
	@Override
	public String getFactoryId() {
		return ScriptEditorInputFactory.ID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IPersistable#saveState(org.eclipse.ui.IMemento)
	 */
	@Override
	public void saveState(IMemento memento) {
		ScriptEditorInputFactory.saveState(memento, this);
	}

	/**
	 * Gets the id.
	 *
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the file.
	 *
	 * @return
	 */
	public File getFile() {
		return storage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof DebugJsInput) {
			return ((DebugJsInput) obj).id.equals(id)
					&& ((DebugJsInput) obj).storage.getAbsolutePath().equals(storage.getAbsolutePath());
		}
		return super.equals(obj);
	}

	/**
	 * Gets the module handle
	 *
	 * @return
	 */
	public ModuleHandle getModuleHandle() {
		if (handle == null) {
			try {
				handle = getSessionHandle().openModule(getFile().getAbsolutePath(),
						// No need to close the stream here, the report
						// design parser will automaically close it.
						new FileInputStream(getFile()));
			} catch (DesignFileException | FileNotFoundException e) {
			}
		}
		return handle;
	}

	private SessionHandle getSessionHandle() {
		return new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.getDefault());
	}

	private String getDisplayText() {
		String str = ModuleUtil.getScript(getModuleHandle(), getId());
		if (str == null) {
			str = ERRORMSG;
		}
		return str;
	}

	public boolean sourceAvailable() {
		return !ERRORMSG.equals(getDisplayText());
	}

	/**
	 * ScriptStorage
	 */
	private class ScriptStorage implements IStorage {

		@Override
		public InputStream getContents() throws CoreException {
			String str = getDisplayText();
			InputStream input = new ByteArrayInputStream(str.getBytes());
			return input;
		}

		@Override
		public IPath getFullPath() {
			return DebugJsInput.this.getPath();
		}

		@Override
		public String getName() {
			return DebugJsInput.this.getName();
		}

		@Override
		public boolean isReadOnly() {
			return true;
		}

		@Override
		public Object getAdapter(Class adapter) {
			return DebugJsInput.this.getAdapter(adapter);
		}

	}

}
