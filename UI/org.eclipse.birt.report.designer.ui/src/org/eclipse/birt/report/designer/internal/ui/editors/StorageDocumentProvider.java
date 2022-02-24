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

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.core.resources.IEncodedStorage;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISaveablePart;

/**
 * Document provider specialized for
 * {@link org.eclipse.birt.report.designer.internal.ui.editors.IStorageEditorInput}.
 */
public class StorageDocumentProvider extends DocumentProvider {

	/**
	 * Creates a new document provider.
	 * 
	 */
	public StorageDocumentProvider() {
		super();
	}

	/**
	 * Creates a new document provider with the specified saveable part.
	 * 
	 * @param part the saveable part.
	 */
	public StorageDocumentProvider(ISaveablePart part) {
		super(part);
	}

	/**
	 * Initializes the given document from the given editor input using the given
	 * character encoding.
	 * 
	 * @param document    the document to be initialized
	 * @param editorInput the input from which to derive the content of the document
	 * @param encoding    the character encoding used to read the editor input
	 * @return <code>true</code> if the document content could be set,
	 *         <code>false</code> otherwise
	 * @throws CoreException if the given editor input cannot be accessed
	 */
	protected boolean setDocumentContent(IDocument document, IEditorInput editorInput, String encoding)
			throws CoreException {
		IStorage storage = null;
		if (editorInput instanceof IStorageEditorInput) {
			storage = ((IStorageEditorInput) editorInput).getStorage();
		}

		if (storage != null) {
			InputStream stream = storage.getContents();
			try {
				setDocumentContent(document, stream, encoding);
			} finally {
				try {
					stream.close();
				} catch (IOException x) {
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns the persisted encoding for the given element.
	 * 
	 * @param element the element for which to get the persisted encoding
	 * @return the persisted encoding
	 */
	protected String getPersistedEncoding(Object element) {
		if (element instanceof IStorageEditorInput) {
			IStorage storage;
			try {
				storage = ((IStorageEditorInput) element).getStorage();
				if (storage instanceof IEncodedStorage)
					return ((IEncodedStorage) storage).getCharset();
			} catch (CoreException e) {
				return null;
			}
		}
		return null;
	}

	public boolean isModifiable(Object element) {
		return !isReadOnly(element);
	}

	public boolean isReadOnly(Object element) {
		IStorage storage = null;
		try {
			if (element instanceof IStorageEditorInput) {
				storage = ((IStorageEditorInput) element).getStorage();
			}
		} catch (CoreException x) {
			ExceptionHandler.handle(x);
		}

		if (storage != null) {
			return storage.isReadOnly();
		}
		return super.isReadOnly(element);
	}
}
