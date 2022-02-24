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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractDocumentProvider;
import org.osgi.framework.Bundle;

/**
 * General document provider specialized for IStorage.
 */
public abstract class DocumentProvider extends AbstractDocumentProvider {

	/**
	 * Default file size.
	 *
	 */
	protected static final int DEFAULT_FILE_SIZE = 15 * 1024;

	/** The saveable part. */
	private final ISaveablePart part;

	/**
	 * Creates a new document provider.
	 *
	 */
	public DocumentProvider() {
		this(null);
	}

	/**
	 * Creates a new document provider with the specified saveable part.
	 *
	 * @param part the saveable part.
	 */
	public DocumentProvider(ISaveablePart part) {
		super();
		this.part = part;
	}

	/**
	 * Initializes the given document with the given stream.
	 *
	 * @param document      the document to be initialized
	 * @param contentStream the stream which delivers the document content
	 * @throws CoreException if the given stream can not be read
	 *
	 */
	protected void setDocumentContent(IDocument document, InputStream contentStream) throws CoreException {
		setDocumentContent(document, contentStream, null);
	}

	/**
	 * Initializes the given document with the given stream using the given
	 * encoding.
	 *
	 * @param document      the document to be initialized
	 * @param contentStream the stream which delivers the document content
	 * @param encoding      the character encoding for reading the given stream
	 * @throws CoreException if the given stream can not be read
	 */
	protected void setDocumentContent(IDocument document, InputStream contentStream, String encoding)
			throws CoreException {

		Reader in = null;

		try {

			if (encoding == null) {
				encoding = getDefaultEncoding();
			}

			in = new BufferedReader(new InputStreamReader(contentStream, encoding), DEFAULT_FILE_SIZE);
			StringBuilder buffer = new StringBuilder(DEFAULT_FILE_SIZE);
			char[] readBuffer = new char[2048];
			int n = in.read(readBuffer);
			while (n > 0) {
				buffer.append(readBuffer, 0, n);
				n = in.read(readBuffer);
			}

			document.set(buffer.toString());

		} catch (IOException x) {
			String message = (x.getMessage() != null ? x.getMessage() : ""); //$NON-NLS-1$
			IStatus s = new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK, message, x);
			throw new CoreException(s);
		} finally {
			try {
				if (in != null) {
					in.close();
				} else {
					contentStream.close();
				}
			} catch (IOException x) {
			}
		}
	}

	/**
	 * Initializes the given document from the given editor input using the default
	 * character encoding.
	 *
	 * @param document    the document to be initialized
	 * @param editorInput the input from which to derive the content of the document
	 * @return <code>true</code> if the document content could be set,
	 *         <code>false</code> otherwise
	 * @throws CoreException if the given editor input cannot be accessed
	 */
	protected boolean setDocumentContent(IDocument document, IEditorInput editorInput) throws CoreException {
		return setDocumentContent(document, editorInput, null);
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
	abstract protected boolean setDocumentContent(IDocument document, IEditorInput editorInput, String encoding)
			throws CoreException;

	/*
	 * @see AbstractDocumentProvider#createAnnotationModel(Object)
	 */
	@Override
	protected IAnnotationModel createAnnotationModel(Object element) throws CoreException {
		return null;
	}

	/**
	 * Factory method for creating empty documents.
	 *
	 * @return the newly created document
	 */
	protected IDocument createEmptyDocument() {
		return new Document();
	}

	/*
	 * @see AbstractDocumentProvider#createDocument(Object)
	 */
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		String encoding = null;
		// FIXME
		ModuleHandle module = SessionHandleAdapter.getInstance().getReportDesignHandle();

		if (module != null) {
			encoding = module.getFileEncoding();
		} else {
			encoding = UnicodeUtil.SIGNATURE_UTF_8;
		}

		if (element instanceof IEditorInput) {
			IDocument document = createEmptyDocument();
			if (setDocumentContent(document, (IEditorInput) element, encoding)) {
				setupDocument(element, document);
				return document;
			}
		}

		return null;
	}

	/**
	 * Sets up the given document as it would be provided for the given element. The
	 * content of the document is not changed. This default implementation is empty.
	 * Subclasses may reimplement.
	 *
	 * @param element  the blue-print element
	 * @param document the document to set up
	 */
	protected void setupDocument(Object element, IDocument document) {
	}

	/*
	 * @see AbstractDocumentProvider#doSaveDocument(IProgressMonitor, Object,
	 * IDocument, boolean)
	 */
	@Override
	protected void doSaveDocument(IProgressMonitor monitor, Object element, IDocument document, boolean overwrite)
			throws CoreException {
		if (part != null) {
			part.doSave(monitor);
		}
	}

	/**
	 * Defines the standard procedure to handle <code>CoreExceptions</code>.
	 * Exceptions are written to the plug-in log.
	 *
	 * @param exception the exception to be logged
	 * @param message   the message to be logged
	 */
	protected void handleCoreException(CoreException exception, String message) {

		Bundle bundle = Platform.getBundle(PlatformUI.PLUGIN_ID);
		ILog log = Platform.getLog(bundle);

		if (message != null) {
			log.log(new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, 0, message, exception));
		} else {
			log.log(exception.getStatus());
		}
	}

	/*
	 * @see IStorageDocumentProvider#getDefaultEncoding()
	 */
	public String getDefaultEncoding() {
		return ResourcesPlugin.getEncoding();
	}

	/**
	 * Returns the persisted encoding for the given element.
	 *
	 * @param element the element for which to get the persisted encoding
	 * @return the persisted encoding
	 */
	abstract protected String getPersistedEncoding(Object element);

	/*
	 * @see
	 * org.eclipse.ui.texteditor.AbstractDocumentProvider#getOperationRunner(org.
	 * eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected IRunnableContext getOperationRunner(IProgressMonitor monitor) {
		return null;
	}
}
