/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.editors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.logging.Level;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 *
 */

public class ReportDocumentEditor extends EditorPart {

	private IReportEngine engine;
	private EngineConfig engineConfig;
	private Color fBackgroundColor;
	private Color fForegroundColor;
	private Color fSeparatorColor;
	private String fileName = ""; //$NON-NLS-1$
	private Composite fComposite;

	private Button detailsButton;
	private Composite detailsArea;
	private Control details = null;
	boolean showingDetails = false;
	private Exception e;

	@Override
	public void doSave(IProgressMonitor monitor) {
		// do nothing

	}

	@Override
	public void doSaveAs() {
		// do nothing

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		if (engine == null) {
			init();
		}
		Display display = parent.getDisplay();
		fBackgroundColor = display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		fForegroundColor = display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
		fSeparatorColor = ColorManager.getColor(152, 170, 203);

		fComposite = createComposite(parent);
		fComposite.setLayout(new GridLayout());

		createTitleLabel(fComposite, Messages.getString("ReportDocumentEditor.1")); //$NON-NLS-1$
		createLabel(fComposite, null);
		createLabel(fComposite, null);

		createHeadingLabel(fComposite, Messages.getString("ReportDocumentEditor.2")); //$NON-NLS-1$

		Composite separator = createCompositeSeparator(fComposite);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 2;
		separator.setLayoutData(data);

		createInfomation(fComposite);
	}

	private void createInfomation(Composite parent) {

		Font font = parent.getFont();
		Composite continer = createComposite(parent, font, 2, 2, GridData.FILL_BOTH, 0, 0);
		continer.setBackground(fBackgroundColor);
		IReportDocument document = null;
		try {
			document = engine.openReportDocument(getFileName());
			createScriptgLabel(continer, Messages.getString("ReportDocumentEditor.3")); //$NON-NLS-1$
			createScriptgLabel(continer, document.getName());

			createScriptgLabel(continer, Messages.getString("ReportDocumentEditor.4")); //$NON-NLS-1$
			createScriptgLabel(continer, document.getVersion());

			createScriptgLabel(continer, Messages.getString("ReportDocumentEditor.5")); //$NON-NLS-1$
			createScriptgLabel(continer, "" + document.getPageCount()); //$NON-NLS-1$

		} catch (EngineException e) {
			this.e = e;
			createErrorControl(continer);
		} finally {
			if (document != null) {
				document.close();
			}
		}

	}

	private void createErrorControl(Composite parent) {
		Color bgColor = parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		Color fgColor = parent.getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);

		parent.setBackground(bgColor);
		parent.setForeground(fgColor);

		GridLayout layout = new GridLayout();

		layout.numColumns = 3;

		int spacing = 8;
		int margins = 8;
		layout.marginBottom = margins;
		layout.marginTop = margins;
		layout.marginLeft = margins;
		layout.marginRight = margins;
		layout.horizontalSpacing = spacing;
		layout.verticalSpacing = spacing;
		parent.setLayout(layout);

		Label imageLabel = new Label(parent, SWT.NONE);
		imageLabel.setBackground(bgColor);
		Image image = getImage();
		if (image != null) {
			image.setBackground(bgColor);
			imageLabel.setImage(image);
			imageLabel
					.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_BEGINNING));
		}

		Text text = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.WRAP);
		text.setBackground(bgColor);
		text.setForeground(fgColor);

		// text.setForeground(JFaceColors.getErrorText(text.getDisplay()));
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.setText(Messages.getString("ReportDocumentEditor.errorMessage")); //$NON-NLS-1$

		detailsButton = new Button(parent, SWT.PUSH);
		detailsButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				showDetails(!showingDetails);
			}
		});

		detailsButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		detailsButton.setVisible(e != null);

		updateDetailsText();

		detailsArea = new Composite(parent, SWT.NONE);
		detailsArea.setBackground(bgColor);
		detailsArea.setForeground(fgColor);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		data.verticalSpan = 1;
		detailsArea.setLayoutData(data);
		detailsArea.setLayout(new FillLayout());
		parent.layout(true);
	}

	private Composite createCompositeSeparator(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(fSeparatorColor);
		return composite;
	}

	private Label createTitleLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		if (text != null) {
			label.setText(text);
		}
		label.setBackground(fBackgroundColor);
		label.setForeground(fForegroundColor);
		label.setFont(JFaceResources.getHeaderFont());

		return label;
	}

	private Label createLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.WRAP);
		if (text != null) {
			label.setText(text);
		}
		label.setBackground(fBackgroundColor);
		label.setForeground(fForegroundColor);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		label.setLayoutData(gd);
		return label;
	}

	private Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(fBackgroundColor);

		return composite;
	}

	private Label createHeadingLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		if (text != null) {
			label.setText(text);
		}
		label.setBackground(fBackgroundColor);
		label.setForeground(fForegroundColor);
		label.setFont(JFaceResources.getBannerFont());

		return label;
	}

	private Label createScriptgLabel(Composite parent, String text) {
		Label label = new Label(parent, SWT.NONE);
		if (text != null) {
			label.setText(text);
		}
		label.setBackground(fBackgroundColor);
		label.setForeground(fForegroundColor);
		// label.setFont(JFaceResources.getBannerFont());

		return label;
	}

	protected void display() {

	}

	@Override
	public void setFocus() {
		if (fComposite != null && !fComposite.isDisposed()) {
			fComposite.setFocus();
		}
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private Composite createComposite(Composite parent, Font font, int columns, int hspan, int fill, int marginwidth,
			int marginheight) {
		Composite g = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(columns, false);
		layout.marginWidth = marginwidth;
		layout.marginHeight = marginheight;
		g.setLayout(layout);
		g.setFont(font);
		GridData gd = new GridData(fill);
		gd.horizontalSpan = hspan;
		g.setLayoutData(gd);
		return g;
	}

	private void init() {
		engineConfig = new LauncherEngineConfig();

		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);

		configEngine();
		this.engine = factory.createReportEngine(engineConfig);
		engine.changeLogLevel(Level.WARNING);
	}

	private void configEngine() {
		HTMLRenderOption emitterConfig = new HTMLRenderOption();

		emitterConfig.setActionHandler(new HTMLActionHandler() {

			@Override
			public String getURL(IAction actionDefn, Object context) {
				if (actionDefn.getType() == IAction.ACTION_DRILLTHROUGH) {
					return "birt://" //$NON-NLS-1$
							+ URLEncoder.encode(super.getURL(actionDefn, context));
				}
				return super.getURL(actionDefn, context);
			}

		});

		engineConfig.getEmitterConfigs().put(RenderOption.OUTPUT_FORMAT_HTML, emitterConfig);
	}

	static class LauncherEngineConfig extends EngineConfig {

		/**
		 * constructor
		 */
		public LauncherEngineConfig() {
			super();

			HTMLRenderOption emitterConfig = (HTMLRenderOption) getEmitterConfigs()
					.get(RenderOption.OUTPUT_FORMAT_HTML);

			emitterConfig.setImageHandler(new HTMLCompleteImageHandler());
		}

	}

	private Image getImage() {
		Display d = Display.getCurrent();

		return d.getSystemImage(SWT.ICON_ERROR);

	}

	private void showDetails(boolean shouldShow) {
		if (shouldShow == showingDetails) {
			return;
		}
		this.showingDetails = shouldShow;
		updateDetailsText();
	}

	private void updateDetailsText() {
		if (details != null) {
			details.dispose();
			details = null;
		}

		if (showingDetails) {
			detailsButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
			Text detailsText = new Text(detailsArea,
					SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.READ_ONLY | SWT.LEFT_TO_RIGHT);
			detailsText.setText(getStackTrace(e));
			detailsText.setBackground(detailsText.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			details = detailsText;
			detailsArea.layout(true);
		} else {
			detailsButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
		}
	}

	private String getStackTrace(Throwable throwable) {
		StringWriter swriter = new StringWriter();
		PrintWriter pwriter = new PrintWriter(swriter);
		throwable.printStackTrace(pwriter);
		pwriter.flush();
		pwriter.close();
		return swriter.toString();
	}
}
