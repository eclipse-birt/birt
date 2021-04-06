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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.LibrarySaveChangeEvent;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * ExportElementDialog
 */
public class ExportElementDialog extends ResourceFileFolderSelectionDialog {

	private Text text;
	private String ext = ".rptlibrary"; //$NON-NLS-1$
	private Object designElement;

	protected String newFileName = ""; //$NON-NLS-1$

	private Status OKStatus = new Status(IStatus.OK, ReportPlugin.REPORT_UI, IStatus.OK, "", null); //$NON-NLS-1$
	private Status ErrorStatus = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("ExportElementDialog.ErrorMessage"), //$NON-NLS-1$
			null);
	private Status ErrorStatusNoSelection = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString(""), //$NON-NLS-1$
			null);
	private Status ErrorStatusInvalid = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("ExportElementDialog.ErrorMessageInvalid"), //$NON-NLS-1$
			null);

//	private Status ErrorStatusDuplicate = new Status( IStatus.ERROR,
//			ReportPlugin.REPORT_UI,
//			IStatus.ERROR,
//			Messages.getString( "ExportElementDialog.ErrorMessageDuplicate" ), //$NON-NLS-1$
//			null );

	private Status ErrorStatusCanNotExport = new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, IStatus.ERROR,
			Messages.getString("ExportElementDialog.ErrorStatusCanNotExport"), //$NON-NLS-1$
			null);

	private class Validator implements ISelectionStatusValidator {

		public IStatus validate(Object[] selection) {
			Status status = OKStatus;
			int nSelected = selection.length;
			if (nSelected == 0) {
				return ErrorStatusNoSelection;
			} else if (nSelected > 1) {
				return ErrorStatus;
			} else if (selection[0] instanceof ResourceEntry && ((ResourceEntry) selection[0]).isFile()) {
				status = OKStatus;
			} else if (newFileName == null || newFileName.length() == 0
					|| newFileName.toLowerCase().equals(ext.toLowerCase()) || newFileName.startsWith(".") //$NON-NLS-1$
					|| containInvalidChar(newFileName)) {
				return ErrorStatusInvalid;
			} else if (!newFileName.toLowerCase().endsWith(ext.toLowerCase())) {
				return ErrorStatus;
			}

			if (status == OKStatus)
			// check if the element can be exported
			{
				String path = getPath();
				try {
					ModuleHandle handle = SessionHandleAdapter.getInstance().getSessionHandle().openLibrary(path);
					LibraryHandle libraryHandle = (LibraryHandle) handle;
					boolean can = false;
					// boolean override = false;
					if (designElement instanceof DesignElementHandle) {
						can = ElementExportUtil.canExport((DesignElementHandle) designElement, libraryHandle, true);
						// override = ElementExportUtil.canExport( (
						// DesignElementHandle) designElement,
						// libraryHandle,
						// true );
					} else if (designElement instanceof StructureHandle) {
						can = ElementExportUtil.canExport((StructureHandle) designElement, libraryHandle, true);
						// override = ElementExportUtil.canExport( (
						// StructureHandle) designElement,
						// libraryHandle,
						// true );
					}
					libraryHandle.close();
					if (can == false) {
						// if(override)
						// {
						// return ErrorStatusDuplicate;
						// }else
						// {
						return ErrorStatusCanNotExport;
						// }

					}

				} catch (DesignFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					// return ErrorStatusDuplicate;
				}
			}
			return OKStatus;
		}
	}

	public ExportElementDialog(Object selection) {
		super(false, new String[] { "*.rptlibrary" //$NON-NLS-1$
		});
		setDoubleClickSelects(true);
		setValidator(new Validator());
		setAllowMultiple(false);
		// setInput( getResourceRootFile( ) );
		setTitle(Messages.getString("ExportElementDialog.Dialog.Title")); //$NON-NLS-1$
		setMessage(Messages.getString("ExportElementDialog.Dialog.Message")); //$NON-NLS-1$
		designElement = selection;
		if (designElement instanceof StructuredSelection) {
			designElement = ((StructuredSelection) designElement).getFirstElement();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createDialogArea(org
	 * .eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite rt = (Composite) super.createDialogArea(parent);
		rt.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite pane = new Composite(rt, 0);
		pane.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pane.setLayout(new GridLayout(2, false));
		pane.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label lb = new Label(pane, 0);
		lb.setText(Messages.getString("ExportElementDialog.label.NewFile"));//$NON-NLS-1$

		text = new Text(pane, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String tempText = text.getText().trim();
				if (tempText.length() > 0 && (!tempText.toLowerCase().endsWith(ext))) {
					newFileName = tempText + ext;
				} else {
					newFileName = tempText;
				}
				updateOKStatus();
			}
		});

		configViewer();
		// UIUtil.bindHelp( parent,
		// IHelpContextIds.NEW_ADD_RESOURCE_FILES_DIALOG_ID );
		return rt;
	}

	private void configViewer() {

		getTreeViewer().addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				Object object = ((StructuredSelection) event.getSelection()).getFirstElement();
				if (object instanceof ResourceEntry) {
					ResourceEntry entry = (ResourceEntry) object;
					if (entry.getURL() != null && entry.getURL().getProtocol().equals("file")) //$NON-NLS-1$
					{
						File file = new File(entry.getURL().getPath());
						text.setEnabled(file.isDirectory());
					} else {
						text.setText(""); //$NON-NLS-1$
						text.setEnabled(false);
					}
				} else {
					text.setText(""); //$NON-NLS-1$
					text.setEnabled(false);
				}
				if (!text.isEnabled()) {
					newFileName = ""; //$NON-NLS-1$
				} else {
					String tempText = text.getText().trim();
					if (tempText.length() > 0 && tempText.toLowerCase().endsWith(ext)) {
						newFileName = tempText + ext;
					} else {
						newFileName = tempText;
					}

				}
			}
		});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#okPressed()
	 */
	protected void okPressed() {
		Object firstElement = designElement;
		if (firstElement instanceof StructuredSelection) {
			firstElement = ((StructuredSelection) firstElement).getFirstElement();
		}

		String path = getPath();

		LibraryHandle libraryHandle = null;
		boolean isDone = true;
		try {
			ModuleHandle handle = openOrCreateLibrary(SessionHandleAdapter.getInstance().getSessionHandle(), path);
			libraryHandle = (LibraryHandle) handle;

			if (firstElement instanceof StyleHandle
					&& libraryHandle.getVisibleThemes(IAccessControl.DIRECTLY_INCLUDED_LEVEL).size() > 0) {
				Object[] result = getExportTheme((StyleHandle) firstElement, libraryHandle);
				if (result == null)
					return;
				else if (result[0] == null) {
					cancelPressed();
					return;
				} else {
					boolean notExist = ((Boolean) result[1]).booleanValue();
					if (!notExist) {
						int confirm = confirmOverride();
						switch (confirm) {
						case 0: // Yes
							break;
						case 1: // No
							return;
						case 2: // Cancel
						default:
							cancelPressed();
							return;
						}
					}
					ElementExportUtil.exportStyle((StyleHandle) firstElement, (ThemeHandle) result[0], true);
				}
			} else if (firstElement instanceof DesignElementHandle) {

				boolean notExist = ElementExportUtil.canExport((DesignElementHandle) firstElement, libraryHandle,
						false);
				if (!notExist) {
					int confirm = confirmOverride();
					switch (confirm) {
					case 0: // Yes
						break;
					case 1: // No
						return;
					case 2: // Cancel
					default:
						cancelPressed();
						return;
					}
				}

				if (firstElement instanceof ImageHandle && DesignChoiceConstants.IMAGE_REF_TYPE_EMBED
						.equals(((ImageHandle) firstElement).getSource())) {
					isDone = exportEmbeddedImage((ImageHandle) firstElement, libraryHandle);
				} else {
					ElementExportUtil.exportElement((DesignElementHandle) firstElement, libraryHandle, true);
				}

			} else if (firstElement instanceof StructureHandle) {
				boolean notExist = ElementExportUtil.canExport((StructureHandle) firstElement, libraryHandle, false);
				if (!notExist) {
					int confirm = confirmOverride();
					switch (confirm) {
					case 0: // Yes
						break;
					case 1: // No
						return;
					case 2: // Cancel
					default:
						cancelPressed();
						return;
					}
				}
				ElementExportUtil.exportStructure((StructureHandle) firstElement, libraryHandle, true);
			}

		} catch (DesignFileException e) {
			ExceptionHandler.handle(e);
			e.printStackTrace();
			isDone = false;
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			e.printStackTrace();
			isDone = false;
		} finally {
			if (libraryHandle != null) {
				try {
					libraryHandle.save();
				} catch (IOException e) {
					ExceptionHandler.handle(e);
					e.printStackTrace();
				}
				libraryHandle.close();
			}
		}

		IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

		if (synchronizer != null && isDone) {
			synchronizer.notifyResourceChanged(new LibrarySaveChangeEvent(this, Path.fromOSString(path),
					IReportResourceChangeEvent.LibraySaveChange, Path.fromOSString(path).toOSString()));
		}

		super.okPressed();
	}

	private Object[] getExportTheme(StyleHandle firstElement, LibraryHandle libraryHandle) {
		ExportStyleDialog dialog = new ExportStyleDialog(firstElement, libraryHandle);
		if (dialog.open() == Window.OK) {
			return (Object[]) dialog.getResult();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.dialogs.resource.
	 * FileFolderSelectionDialog#getPath()
	 */
	public String getPath() {
		Object[] selected = getResult();
		String fullPath = ""; //$NON-NLS-1$
		if (selected.length > 0) {
			ResourceEntry entry = (ResourceEntry) selected[0];
			fullPath = entry.getURL().getPath();
			File file = new File(fullPath);
			if (file.isDirectory()) {
				fullPath = fullPath + ((fullPath.equals("") || fullPath.endsWith("/")) ? "" : "/") //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
						+ newFileName;
			}
		} else {
			// rcp should be different
			String resouceFolder = ReportPlugin.getDefault().getResourceFolder();
			fullPath = resouceFolder + ((resouceFolder.equals("") || resouceFolder.endsWith("/")) ? "" : "/") //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
					+ newFileName;
		}
		return fullPath;
	}

	private int confirmOverride(String confirmTitle, String confirmMsg) {
		String[] buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
				IDialogConstants.CANCEL_LABEL };

		if (confirmTitle == null || confirmTitle.trim().length() == 0) {
			confirmTitle = Messages.getString("ExportElementDialog.WarningMessageDuplicate.Title"); //$NON-NLS-1$
		}
		if (confirmMsg == null || confirmMsg.trim().length() == 0) {
			confirmMsg = Messages.getFormattedString("ExportElementDialog.WarningMessageDuplicate.Message", //$NON-NLS-1$
					buttons);
		}

		MessageDialog dialog = new MessageDialog(UIUtil.getDefaultShell(), confirmTitle, null, confirmMsg,
				MessageDialog.QUESTION, buttons, 0);

		return dialog.open();

	}

	private int confirmOverride() {
		return confirmOverride(null, null);
	}

	private boolean exportEmbeddedImage(ImageHandle image, LibraryHandle libraryHandle) throws SemanticException {
		if (!(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED.equals(image.getSource()))) {
			return false;
		}
		EmbeddedImageHandle embeded = image.getEmbeddedImage();
		if (embeded == null) {
			return false;
		}

		boolean notExist = ElementExportUtil.canExport(embeded, libraryHandle, false);
		if (!notExist) {
			int confirm = confirmOverride(null,
					Messages.getString("ExportElementDialog.WarningMessageDuplicate.OverrideImage")); //$NON-NLS-1$
			switch (confirm) {
			case 0: // Yes
				break;
			case 1: // No
			{
				ElementExportUtil.exportElement((DesignElementHandle) image, libraryHandle, true);
				return true;
			}
			case 2: // Cancel
			default:
				cancelPressed();
				return false;
			}
		}
		ElementExportUtil.exportElement((DesignElementHandle) image, libraryHandle, true);
		ElementExportUtil.exportStructure(embeded, libraryHandle, true);
		return true;
	}

	/**
	 * Opens the library with given library file name. If the library is not found,
	 * create it.
	 * 
	 * @param session         the session
	 * @param libraryFileName file name of the library to open
	 * @return the opened library handle
	 * @throws DesignFileException if error encountered when open library file with
	 *                             the given file name.
	 */

	private LibraryHandle openOrCreateLibrary(SessionHandle session, String libraryFileName)
			throws DesignFileException {

		try {
			LibraryHandle library = session.openLibrary(libraryFileName);
			return library;
		} catch (DesignFileException e) {
			if (DesignFileException.DESIGN_EXCEPTION_SYNTAX_ERROR == e.getErrorCode()) {
				List errorList = e.getErrorList();

				// FILE_NOT_FOUND error is always the first one.

				ErrorDetail error = ((ErrorDetail) errorList.get(0));
				if (DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND == error.getErrorCode()) {
					String templateName = UIUtil.getDefaultLibraryTemplate();
					LibraryHandle libraryHandle;
					if (templateName == null) {
						libraryHandle = session.createLibrary();
					} else {
						libraryHandle = session.createLibraryFromTemplate(templateName);
					}

					libraryHandle.setFileName(libraryFileName);
					return libraryHandle;
				}
			}

			throw e;
		}
	}

	private boolean containInvalidChar(String fileName) {
		boolean ret = false;
		char[] invalidChars = new char[] { '\\', '/', ':', '*', '?', '"', '<', '>', '|' };
		for (int i = 0; i < invalidChars.length; i++) {
			if (fileName.indexOf(invalidChars[i]) >= 0) {
				return true;
			}
		}

		return ret;
	}
}
