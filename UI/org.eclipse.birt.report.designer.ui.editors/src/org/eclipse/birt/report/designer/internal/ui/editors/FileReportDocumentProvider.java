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

package org.eclipse.birt.report.designer.internal.ui.editors;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.resources.IEncodedStorage;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;

/**
 * Document provider for file system input.
 */
public class FileReportDocumentProvider extends DocumentProvider {

	/**
	 * Creates a new document provider.
	 * 
	 */
	public FileReportDocumentProvider() {
		super();
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
		InputStream stream = null;

		if (editorInput instanceof IStorageEditorInput) {
			IStorage storage = ((IStorageEditorInput) editorInput).getStorage();
			if (storage != null) {
				stream = storage.getContents();
			}
		}

		if (stream == null) {
			ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();

			if (moduleHandle != null) {
				File file = new File(moduleHandle.getFileName());
				;

				if (file.exists()) {
					try {
						stream = new FileInputStream(file);
					} catch (FileNotFoundException e) {
					}
				}
			}
		}

		if (stream != null) {
			try {
				setDocumentContent(document, stream, encoding);
				stream.close();
				return true;
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			}
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
		if (element instanceof IEncodedStorage) {
			try {
				return ((IEncodedStorage) element).getCharset();
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
		if (element instanceof IPathEditorInput) {
			File file = ((IPathEditorInput) element).getPath().toFile();
			return !file.canWrite();
		}
		return super.isReadOnly(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.DocumentProvider
	 * #doSaveDocument(org.eclipse.core.runtime.IProgressMonitor, java.lang.Object,
	 * org.eclipse.jface.text.IDocument, boolean)
	 */
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite)
			throws CoreException {
		if (element instanceof IPathEditorInput) {
			byte[] bytes = document.get().getBytes();
			IPathEditorInput input = (IPathEditorInput) element;
			File file = input.getPath().toFile();
			OutputStream out = null;
			InputStream in = null;

			try {
				out = new FileOutputStream(file);
				in = new ByteArrayInputStream(bytes);
				int length;
				byte[] read = new byte[64];
				while ((length = in.read(read)) != -1) {
					out.write(read, 0, length);
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

}
