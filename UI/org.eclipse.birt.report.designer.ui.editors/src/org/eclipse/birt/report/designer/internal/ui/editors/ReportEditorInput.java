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

package org.eclipse.birt.report.designer.internal.ui.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * Editor input wrapper for report input is not in workspace.
 */

public class ReportEditorInput implements IStorageEditorInput, IPathEditorInput, IPersistableElement {

	private File file = null;

	/**
	 * Constructor
	 * 
	 * @param input
	 */
	public ReportEditorInput(IPathEditorInput input) {
		this(input.getPath().toFile());
	}

	/**
	 * Constructor
	 * 
	 * @param file
	 */
	public ReportEditorInput(File file) {
		this.file = file;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.IStorageEditorInput#
	 * getStorage()
	 */
	public IStorage getStorage() throws CoreException {
		return new ReportStorage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return file.exists();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return file.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return file.getAbsolutePath();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPathEditorInput#getPath()
	 */
	public IPath getPath() {
		return new Path(file.getAbsolutePath());
	}

	/*
	 * (non-Javadoc) Method declared on Object.
	 * 
	 * The <code>ReportEditorInput</code> implementation of this <code>Object</code>
	 * method bases the equality of two <code>ReportEditorInput</code> objects on
	 * the equality of their underlying file.
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof IPathEditorInput) {
			obj = new ReportEditorInput((IPathEditorInput) obj);
		}
		if (!(obj instanceof ReportEditorInput))
			return false;
		return file.equals(((ReportEditorInput) obj).file);
	}

	@Override
	public int hashCode() {
		return file.hashCode();
	}

	public File getFile() {
		return this.file;
	}

	private class ReportStorage implements IStorage {

		public InputStream getContents() throws CoreException {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				ExceptionUtil.handle(e);
				return null;
			}
		}

		public IPath getFullPath() {
			return ReportEditorInput.this.getPath();
		}

		public String getName() {
			return ReportEditorInput.this.getName();
		}

		public boolean isReadOnly() {
			return !file.canWrite();
		}

		public Object getAdapter(Class adapter) {
			return ReportEditorInput.this.getAdapter(adapter);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPersistableElement#getFactoryId()
	 */
	public String getFactoryId() {
		return ReportEditorInputFactory.ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPersistable#saveState(org.eclipse.ui.IMemento)
	 */
	public void saveState(IMemento memento) {
		ReportEditorInputFactory.saveState(memento, this);
	}
}
