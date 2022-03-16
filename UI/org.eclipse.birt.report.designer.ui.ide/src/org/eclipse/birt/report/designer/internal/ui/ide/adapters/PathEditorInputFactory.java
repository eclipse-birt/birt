/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.ui.editors.IPathEditorInputFactory;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Adapter factory for <code>IEditorInput</code> based on the local file system
 * path.
 */
public class PathEditorInputFactory implements IAdapterFactory {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
	 * java.lang.Class)
	 */
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IPathEditorInputFactory.class.equals(adapterType)) {

			return new IPathEditorInputFactory() {

				/*
				 * (non-Javadoc)
				 *
				 * @seeorg.eclipse.birt.report.designer.ui.editors. IPathEditorInputFactory
				 * #create(org.eclipse.core.runtime.IPath)
				 */
				@Override
				public IEditorInput create(IPath path) {
					final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);

					if (file != null) {
						return new FileEditorInput(file);
					}

					return new PathEditorInput(path);
				}
			};
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	@Override
	public Class[] getAdapterList() {
		return new Class[] { IPathEditorInputFactory.class };
	}

	/**
	 * Implements an IPathEditorInput instance appropriate for
	 * <code>IFileStore</code> elements that represent files that are not part of
	 * the current workspace.
	 */
	private static class PathEditorInput extends FileStoreEditorInput implements IPathEditorInput {

		/** The path to a file store within the scheme of this file system. */
		private final IPath path;

		/**
		 * Creates a new adapter for the given path.
		 *
		 * @param path A path to a file store within the scheme of this file system.
		 */
		public PathEditorInput(IPath path) {
			super(EFS.getLocalFileSystem().getStore(path));
			this.path = path;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.ui.IPathEditorInput#getPath()
		 */
		@Override
		public IPath getPath() {
			return path;
		}
	}
}
