/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.script;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.birt.report.designer.internal.ui.editors.IStorageEditorInput;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;

/**
 * This is the editor container. All the working editors such as report
 * designer, master page editor, are pages of this editor.
 *
 */

public class JSEditorInput implements IStorageEditorInput {

	private class JSStorage implements IStorage {

		/**
		 *
		 */
		public JSStorage() {
			super();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.core.resources.IStorage#getContents()
		 */
		@Override
		public InputStream getContents() throws CoreException {
			// String encoding = SessionHandleAdapter.getInstance( )
			// .getReportDesignHandle( )
			// .getFileEncoding( );
			if (name == null) {
				name = ""; //$NON-NLS-1$
			}
			try {
				return new ByteArrayInputStream(name.getBytes(encoding));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e.getMessage());
			}

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.core.resources.IStorage#getFullPath()
		 */
		@Override
		public IPath getFullPath() {
			return null;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.core.resources.IStorage#getName()
		 */
		@Override
		public String getName() {
			return name;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.core.resources.IStorage#isReadOnly()
		 */
		@Override
		public boolean isReadOnly() {
			return false;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
		 */
		@Override
		public Object getAdapter(Class adapter) {
			return null;
		}
	}

	private String name = null;
	private String encoding = "";
	private final static String DEFAULT_ENCODING = UnicodeUtil.SIGNATURE_UTF_8;

	/**
	 *
	 */
	public JSEditorInput(String _name, String encoding) {
		super();
		this.name = _name;
		this.encoding = encoding;
	}

	/**
	 *
	 */
	public JSEditorInput(String _name) {
		this(_name, DEFAULT_ENCODING);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	@Override
	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	@Override
	public IPersistableElement getPersistable() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.IStorageEditorInput#getStorage()
	 */
	@Override
	public IStorage getStorage() throws CoreException {
		return new JSStorage();
	}

}
