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

package org.eclipse.birt.report.debug.internal.ui.script.editor;

import org.eclipse.birt.report.debug.internal.ui.script.launcher.ScriptSWTFactory;
import org.eclipse.birt.report.debug.internal.ui.script.outline.ScriptOutlinePage;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.ui.editor.script.DecoratedScriptEditor;
import org.eclipse.birt.report.designer.ui.editor.script.ScriptDocumentProvider;
import org.eclipse.birt.report.designer.ui.editors.IReportScriptLocation;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/**
 * DebugJsEditor
 */
public class DebugJsEditor extends DecoratedScriptEditor {

	private static final String SCRIPT_LABEL = "Script:"; //$NON-NLS-1$
	private Label messageLabel;

	private ScriptOutlinePage outlinePage;

	/**
	 * Constructor
	 */
	public DebugJsEditor() {
		super(null);

		setRulerContextMenuId("#ScriptRulerContext"); //$NON-NLS-1$
		setEditorContextMenuId("#ScriptEditorContext"); //$NON-NLS-1$
		setSourceViewerConfiguration(new DebugScriptSourceViewerConfiguration(getContext()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editor.script.DecoratedScriptEditor#
	 * createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		Font font = parent.getFont();
		Composite composite = ScriptSWTFactory.createComposite(parent, font, 1, 1, GridData.FILL_HORIZONTAL, 0, 0);

		Composite header = ScriptSWTFactory.createComposite(composite, font, 2, 1, GridData.FILL_HORIZONTAL, 5, 5);

		Label lblScript = new Label(header, SWT.NONE);
		lblScript.setText(SCRIPT_LABEL);
		final FontData fd = lblScript.getFont().getFontData()[0];
		Font labelFont = FontManager.getFont(fd.getName(), fd.getHeight(), SWT.BOLD);
		lblScript.setFont(labelFont);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalIndent = 5;
		gd.verticalAlignment = GridData.CENTER;
		lblScript.setLayoutData(gd);

		messageLabel = new Label(header, SWT.NONE);
		GridData labelLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		labelLayoutData.verticalAlignment = GridData.END;
		messageLabel.setLayoutData(labelLayoutData);
		messageLabel.setText(getLabelMessage());

		messageLabel.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				if (outlinePage != null) {
					outlinePage.selectionItem(getDebugJSInput().getId());
				}
			}

		});

		final Composite sep = new Composite(composite, SWT.NONE);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.heightHint = 1;
		sep.setLayoutData(layoutData);
		sep.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				Rectangle rect = sep.getBounds();
				gc.setForeground(ReportColorConstants.DarkGrayForground);
				gc.drawLine(0, 0, rect.width, 0);
			}
		});

		Composite mainComsite = new Composite(composite, SWT.NONE);
		mainComsite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		mainComsite.setLayout(new FillLayout());

		super.createPartControl(mainComsite);

		if (getEditorInput() != null && getViewer() != null) {
			getViewer().getTextWidget().setEnabled(((DebugJsInput) getEditorInput()).sourceAvailable());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#doSetInput(org.eclipse.
	 * ui.IEditorInput)
	 */
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		String name = getLabelMessage();

		if (messageLabel != null && !messageLabel.isDisposed()) {
			messageLabel.setText(name);
			messageLabel.redraw();
		}

		ScriptDocumentProvider provider = (ScriptDocumentProvider) getDocumentProvider();
		provider.setId(((DebugJsInput) input).getId());
		provider.setFileName(((DebugJsInput) input).getFile().getAbsolutePath());
		provider.update(provider.getAnnotationModel(input));

		if (getViewer() != null) {
			getViewer().getTextWidget().setEnabled(((DebugJsInput) input).sourceAvailable());
		}
	}

	private String getLabelMessage() {
		DebugJsInput jsInput = getDebugJSInput();
		Object obj = ModuleUtil.getScriptObject(jsInput.getModuleHandle(), jsInput.getId());
		String name = ""; //$NON-NLS-1$
		if (obj instanceof PropertyHandle) {
			name = DEUtil.getFlatHirarchyPathName(((PropertyHandle) obj).getElementHandle()) + "." //$NON-NLS-1$
					+ ((PropertyHandle) obj).getDefn().getName();
		} else {
			name = jsInput.getId();
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editor.script.DecoratedScriptEditor#
	 * createScriptInput(java.lang.String)
	 */
	protected IEditorInput createScriptInput(String script) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	private DebugJsInput getDebugJSInput() {
		return (DebugJsInput) getEditorInput();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#getAdapter(java.lang.
	 * Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == IReportScriptLocation.class) {
			return new IReportScriptLocation() {

				public String getID() {
					return getDebugJSInput().getId();
				}

				public int getLineNumber() {
					return -1;
				}

				public String getReportFileName() {
					return getDebugJSInput().getFile().getAbsolutePath();
				}

				public String getDisplayName() {
					return getLabelMessage();
				}

			};
		}
		if (adapter == IContentOutlinePage.class) {
			outlinePage = new ScriptOutlinePage(getDebugJSInput().getModuleHandle());
			return outlinePage;
		}
		return super.getAdapter(adapter);
	}

}
