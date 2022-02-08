/*************************************************************************************
 * Copyright (c) 2006 Actuate Corporation and others.
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

package org.eclipse.birt.report.designer.ui.viewer;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.ui.controller.InputParameterDialog;
import org.eclipse.birt.report.designer.ui.preview.editors.SWTAbstractViewer;
import org.eclipse.birt.report.designer.ui.preview.parameter.AbstractParamGroup;
import org.eclipse.birt.report.designer.ui.preview.parameter.ScalarParam;
import org.eclipse.birt.report.designer.ui.preview.static_html.StaticHTMLPrviewPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.viewer.job.AbstractJob;
import org.eclipse.birt.report.designer.ui.viewer.job.AbstractUIJob;
import org.eclipse.birt.report.designer.ui.viewer.job.RenderJobRule;
import org.eclipse.birt.report.designer.ui.viewer.job.RenderJobRunner;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.ibm.icu.util.ULocale;

public class StaticHTMLViewer extends SWTAbstractViewer {

	private static final String TITLE_MESSAGE = "Showing page {0} of {1}"; //$NON-NLS-1$

	private final HTMLRenderOption renderOption = new HTMLRenderOption();
	private final EngineConfig engineConfig = new HyperlinkEngineConfig();
	private final IRenderOption drillRenderOption = new RenderOption();

	/**
	 * Embedded web browser.
	 */
	private Browser browser = null;

	private long currentPageNum = 1;

	private long totalPageNum = 0;

	// private String preReportDesignFile;

	private boolean isTocUpdate;

	private boolean isDrillThrough = false;

	/**
	 * The report design file to render.
	 */
	private String reportDesignFile;

	private String reportDocumentFile;

	/**
	 * The parameter values for current report design.
	 */
	private Map paramValues = new HashMap();

	/**
	 * The render output file path.
	 */
	private String outputLocation;

	private boolean hasParas;
	/**
	 * Indicate whether itialize the run and render task
	 */
	private boolean isInitialize = false;

	/**
	 * Indicate whether asking for assign parameters values
	 */
	private boolean assignParamValues = false;

	// UI controls
	private FormToolkit toolkit;

	private Form form;

	private SashForm sashForm;

	private Composite browserContainer;

	private List inputParameters;

	private Action paramAction, tocAction, navFirstAction, navPreAction, navNextAction, navLastAction, navGoAction,
			reRunReportAction;

	private Text goPageInput;

	private TreeViewer tocViewer;

	// end UI controls

	public void init() {
		super.init();
		configEngine();
		// configRender( );
	}

	protected void configEngine() {
		HTMLRenderOption emitterConfig = new HTMLRenderOption();

		emitterConfig.setActionHandler(new HTMLActionHandler() {

			public String getURL(IAction actionDefn, Object context) {
				if (actionDefn.getType() == IAction.ACTION_DRILLTHROUGH)
					return "birt://" //$NON-NLS-1$
							+ URLEncoder.encode(super.getURL(actionDefn, context));
				return super.getURL(actionDefn, context);
			}

		});
		// emitterConfig.setImageHandler( new HTMLCompleteImageHandler( ) );
		// emitterConfig.setImageHandler( new HTMLImageHandler( ) );
		engineConfig.getEmitterConfigs().put(RenderOption.OUTPUT_FORMAT_HTML, emitterConfig);
	}

	protected void configRender() {
		renderOption.setOutputFormat(RenderOption.OUTPUT_FORMAT_HTML);
		renderOption.setEmitterID("org.eclipse.birt.report.engine.emitter.html"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.preview.extension.IViewer#createUI(org.
	 * eclipse.swt.widgets.Composite)
	 */
	public Control createUI(Composite parent) {
		if (parent == null) {
			return null;
		}
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);

		form.setFont(JFaceResources.getFontRegistry().get(JFaceResources.BANNER_FONT));
		form.setImage(
				StaticHTMLPrviewPlugin.getDefault().getImageRegistry().get(StaticHTMLPrviewPlugin.IMG_FORM_TITLE));

		toolkit.decorateFormHeading(form);
		form.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		form.getBody().setLayout(layout);

		// Re-run the report action
		reRunReportAction = new Action("Re-run the report", //$NON-NLS-1$
				Action.AS_PUSH_BUTTON) {

			public void run() {
				render();
			}
		};
		reRunReportAction.setToolTipText("Re-run the report"); //$NON-NLS-1$
		reRunReportAction.setImageDescriptor(StaticHTMLPrviewPlugin.getDefault().getImageRegistry()
				.getDescriptor(StaticHTMLPrviewPlugin.IMG_RE_RUN));
		form.getToolBarManager().add(reRunReportAction);

		// paramAction
		paramAction = new Action("Enter parameter", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$

			public void run() {
				render();
			}
		};
		paramAction.setToolTipText("Open Parameters Dialog"); //$NON-NLS-1$
		paramAction.setImageDescriptor(StaticHTMLPrviewPlugin.getDefault().getImageRegistry()
				.getDescriptor(StaticHTMLPrviewPlugin.IMG_PARAMS));
		form.getToolBarManager().add(paramAction);

		// tocAction
		tocAction = new Action("TOC", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$

			public void run() {
				if (sashForm.getMaximizedControl() != null) {
					sashForm.setMaximizedControl(null);
					setChecked(true);
				} else {
					sashForm.setMaximizedControl(browserContainer);
					setChecked(false);
				}
			}
		};
		tocAction.setToolTipText("Show TOC"); //$NON-NLS-1$
		tocAction.setImageDescriptor(
				StaticHTMLPrviewPlugin.getDefault().getImageRegistry().getDescriptor(StaticHTMLPrviewPlugin.IMG_TOC));
		tocAction.setChecked(false);
		form.getToolBarManager().add(tocAction);

		form.getToolBarManager().add(new Separator());

		// navFirstAction
		navFirstAction = new Action("First", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$

			public void run() {
				currentPageNum = 1;
				renderWithoutAskingForParams();
			}
		};
		navFirstAction.setToolTipText("First"); //$NON-NLS-1$
		navFirstAction.setImageDescriptor(StaticHTMLPrviewPlugin.getDefault().getImageRegistry()
				.getDescriptor(StaticHTMLPrviewPlugin.IMG_NAV_FIRST));
		form.getToolBarManager().add(navFirstAction);

		// navPreAction
		navPreAction = new Action("Previous", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$

			public void run() {
				if (currentPageNum > 1) {
					currentPageNum--;
					renderWithoutAskingForParams();
				}
			}
		};

		navPreAction.setToolTipText("Previous"); //$NON-NLS-1$
		navPreAction.setImageDescriptor(StaticHTMLPrviewPlugin.getDefault().getImageRegistry()
				.getDescriptor(StaticHTMLPrviewPlugin.IMG_NAV_PRE));
		form.getToolBarManager().add(navPreAction);

		// navNextAction
		navNextAction = new Action("Next", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$

			public void run() {
				if (currentPageNum < totalPageNum) {
					currentPageNum++;
					renderWithoutAskingForParams();
				}
			}
		};

		navNextAction.setToolTipText("Next"); //$NON-NLS-1$
		navNextAction.setImageDescriptor(StaticHTMLPrviewPlugin.getDefault().getImageRegistry()
				.getDescriptor(StaticHTMLPrviewPlugin.IMG_NAV_NEXT));
		form.getToolBarManager().add(navNextAction);

		// navLastAction
		navLastAction = new Action("Last", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$

			public void run() {
				currentPageNum = totalPageNum;
				renderWithoutAskingForParams();
			}
		};

		navLastAction.setToolTipText("Last"); //$NON-NLS-1$
		navLastAction.setImageDescriptor(StaticHTMLPrviewPlugin.getDefault().getImageRegistry()
				.getDescriptor(StaticHTMLPrviewPlugin.IMG_NAV_LAST));
		form.getToolBarManager().add(navLastAction);

		form.getToolBarManager().add(new Separator());

		ContributionItem inputText = new ContributionItem() {

			public void fill(ToolBar parent, int index) {
				ToolItem toolitem = new ToolItem(parent, SWT.SEPARATOR, index);
				Composite container = new Composite(parent, SWT.NULL);
				// container.setFont( JFaceResources.getFontRegistry( )
				// .get( JFaceResources.TEXT_FONT ) );
				GridLayout layout = new GridLayout();
				layout.numColumns = 2;
				layout.marginWidth = layout.marginHeight = 1;
				container.setLayout(layout);
				Label label = new Label(container, SWT.NULL);
				label.setFont(container.getFont());
				label.setText("Go to page:"); //$NON-NLS-1$

				goPageInput = toolkit.createText(container, "", SWT.BORDER); //$NON-NLS-1$
				goPageInput.setFont(container.getFont());

				goPageInput.setLayoutData(new GridData(GridData.FILL_BOTH));
				goPageInput.addKeyListener(new KeyAdapter() {

					public void keyPressed(KeyEvent e) {
						if (e.character == SWT.LF || e.character == SWT.CR) {
							if (navGoAction.isEnabled()) {
								currentPageNum = Long.parseLong(goPageInput.getText());
								renderWithoutAskingForParams();
							}
						}
					}

				});

				goPageInput.addModifyListener(new ModifyListener() {

					/**
					 * last valid status
					 */
					private boolean isValid = true;

					public void modifyText(ModifyEvent e) {
						if (!"".equals(goPageInput.getText())) //$NON-NLS-1$
						{
							try {
								long page = Long.parseLong(goPageInput.getText());
								if (page > 0 && page <= totalPageNum) {
									if (!isValid) {
										form.setMessage(null);
										isValid = true;
									}
									navGoAction.setEnabled(true);
								} else {
									form.setMessage("Page Number '" //$NON-NLS-1$
											+ page + "' is invalid!", //$NON-NLS-1$
											IMessageProvider.ERROR);
									isValid = false;
									navGoAction.setEnabled(false);
								}
							} catch (NumberFormatException e1) {
								form.setMessage("Page Number '" //$NON-NLS-1$
										+ goPageInput.getText() + "' is invalid!", //$NON-NLS-1$
										IMessageProvider.ERROR);
								isValid = false;
								navGoAction.setEnabled(false);
							}
						} else {
							form.setMessage(null);
							isValid = true;
						}
					}
				});

				toolitem.setWidth(label.computeSize(SWT.DEFAULT, SWT.DEFAULT).x + 40);
				toolitem.setControl(container);
			}

		};
		inputText.setVisible(true);
		form.getToolBarManager().add(inputText);

		// navSelectAction
		navGoAction = new Action("Go to page", Action.AS_PUSH_BUTTON) { //$NON-NLS-1$

			public void run() {
				if (goPageInput != null && !goPageInput.isDisposed()) {
					currentPageNum = Long.parseLong(goPageInput.getText());
					// render( );
					renderWithoutAskingForParams();
				}
			}
		};

		navGoAction.setToolTipText("Go to page"); //$NON-NLS-1$
		navGoAction.setImageDescriptor(StaticHTMLPrviewPlugin.getDefault().getImageRegistry()
				.getDescriptor(StaticHTMLPrviewPlugin.IMG_NAV_GO));
		form.getToolBarManager().add(navGoAction);

		form.updateToolBar();

		sashForm = new SashForm(form.getBody(), SWT.NULL);
		sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));
		sashForm.setLayout(layout);
		toolkit.adapt(sashForm, false, false);

		createTOCSection(sashForm);
		createBrowserSection(sashForm);

		sashForm.setWeights(new int[] { 2, 8 });

		toolkit.paintBordersFor(form.getBody());

		return this.form;
	}

	private void createBrowserSection(Composite parent) {
		browserContainer = toolkit.createComposite(parent);
		browserContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = 1;
		browserContainer.setLayout(layout);

		browser = new Browser(browserContainer, SWT.NONE);
		browser.setLayoutData(new GridData(GridData.FILL_BOTH));

		browser.addLocationListener(new ReportLocationListener(this));
		sashForm.setMaximizedControl(browserContainer);
	}

	private void createTOCSection(Composite parent) {
		Composite toc = toolkit.createComposite(parent);
		toc.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = 1;
		toc.setLayout(new GridLayout());

		toolkit.createLabel(toc, "Table of Contents:"); //$NON-NLS-1$
		Tree t = toolkit.createTree(toc, SWT.NULL);
		t.setLayoutData(new GridData(GridData.FILL_BOTH));
		tocViewer = new TreeViewer(t);
		// TODO config viewer

		tocViewer.setLabelProvider(new TOCLableProvider() {
		});
		tocViewer.setContentProvider(new TOCContentProvider() {
		});

		tocViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (reportDocumentFile != null) {
					StructuredSelection selection = (StructuredSelection) event.getSelection();
					TOCNode node = (TOCNode) selection.getFirstElement();
					try {
						IReportDocument document = openReportDocument(reportDocumentFile);
						setCurrentPage(document.getPageNumber(node.getBookmark()));
						document.close();
						renderWithoutAskingForParams();
					} catch (EngineException e) {
						ExceptionUtil.handle(e);
					}
				}
			}
		});

		toolkit.paintBordersFor(toc);
	}

	protected void refreshTOC() {
		if (!this.isTocUpdate) {
			if (reportDocumentFile != null) {
				try {
					IReportDocument document = openReportDocument(reportDocumentFile);
					tocViewer.setInput(document
							.getTOCTree(DesignChoiceConstants.FORMAT_TYPE_VIEWER, ULocale.getDefault()).getRoot());
					document.close();
				} catch (EngineException e) {
					ExceptionUtil.handle(e);
				}
			}
			this.isTocUpdate = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.preview.extension.IViewer#getUI()
	 */
	public Control getUI() {
		return this.form;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.preview.extension.IViewer#getEngingConfig
	 * ()
	 */
	public EngineConfig getEngineConfig() {
		return engineConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.preview.extension.IViewer#getRenderOption
	 * ()
	 */
	public IRenderOption getRenderOption() {
		if (isDrillThrough) {
			isDrillThrough = false;
			return drillRenderOption;
		}
		return renderOption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.preview.extension.IViewer#setInput(java.
	 * lang.Object)
	 */
	public void setInput(final Object input) {
		if (input instanceof String)
			setReportDesignFile((String) input);
	}

	public void setParamValues(Map paramValues) {
		this.paramValues = paramValues;
		this.assignParamValues = true;
		isDrillThrough = true;
		drillRenderOption.setOption(RenderOption.OUTPUT_FORMAT, paramValues.get("__format")); //$NON-NLS-1$
	}

	public void setReportDesignFile(String reportDesignFile) {
		this.reportDesignFile = reportDesignFile;
		this.isTocUpdate = false;
	}

	/**
	 * Get the path of the report design file to be rendered.
	 * 
	 * @return
	 */
	public String getReportDesignFile() {
		return reportDesignFile;
	}

	/**
	 * Get the parameters pair values for current report design. This method will
	 * raise a dialog if the report design has some parameters. TODO change to check
	 * if there are any required parameters not set then open dialog.
	 * 
	 * @return
	 */
	public void getParameterValues(List params) {
		if (params != null && params.size() > 0) {
			this.hasParas = true;
			// Parameters have been set values, this is case for hyperlink
			// drill-through
			if (assignParamValues && checkParamsValues(params))
				return;
			// assignParamValues = false;
			InputParameterDialog dialog = new InputParameterDialog(Display.getCurrent().getActiveShell(), params,
					paramValues);
			if (dialog.open() == Window.OK) {
				paramValues = dialog.getParameters();
				assignParamValues = true;
			}
		} else {
			this.hasParas = false;
			paramAction.setToolTipText("No Parameters"); //$NON-NLS-1$
		}
	}

	private boolean checkParamsValues(List params) {
		for (Iterator ite = params.iterator(); ite.hasNext();) {
			Object obj = ite.next();
			if (obj instanceof ScalarParam && !((ScalarParam) obj).getHandle().isHidden()) {
				if (!paramValues.containsKey((((ScalarParam) obj).getHandle().getName())))
					return false;
			} else if (obj instanceof AbstractParamGroup) {
				AbstractParamGroup group = (AbstractParamGroup) obj;
				checkParamsValues(group.getChildren());
			}
		}
		return true;
	}

	public void renderReport(IProgressMonitor monitor) {
		if (hasParas && !assignParamValues)
			return;
		assignParamValues = false;

		monitor.subTask("Collecting parameters"); //$NON-NLS-1$
		// getParameterValues( );

		if (monitor.isCanceled()) {
			return;
		}
		monitor.worked(1);

		monitor.subTask("Rendering report"); //$NON-NLS-1$
		if (monitor.isCanceled()) {
			return;
		}

		File reportFile = new File(reportDesignFile);
		String outputFolder = getOutputFolder(reportFile);

		String outputFormat = "html"; //$NON-NLS-1$
		if (isDrillThrough) {
			outputFormat = drillRenderOption.getOutputFormat();
		}
		this.outputLocation = outputFolder + File.separator + reportFile.getName() + "." //$NON-NLS-1$
				+ outputFormat;
		try {
			if (currentPageNum > 0) {
				try {
					this.reportDocumentFile = createReportDocument(reportDesignFile, outputFolder, this.paramValues);
					this.totalPageNum = createReportOutput(this.reportDocumentFile, this.outputLocation,
							currentPageNum);
				} catch (EngineException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (monitor.isCanceled()) {
			return;
		}
		monitor.worked(3);
	}

	private String getOutputFolder(File file) {
		return StaticHTMLPrviewPlugin.getDefault().getTempFolder() + File.separator + file.getName();
	}

	private void setControlStatus() {
		form.setText("Running report..."); //$NON-NLS-1$
		form.setBusy(true);

		paramAction.setEnabled(false);
		reRunReportAction.setEnabled(false);
		tocAction.setEnabled(false);
		navFirstAction.setEnabled(false);
		navPreAction.setEnabled(false);
		navNextAction.setEnabled(false);
		navLastAction.setEnabled(false);
		navGoAction.setEnabled(false);
	}

	public void render() {
		setControlStatus();

		RenderJobRule jobRule = new RenderJobRule(this.reportDesignFile);
		initJob(jobRule);
		getParamValuesJob(jobRule);
		renderJob(jobRule);
		showReportOutputJob(jobRule);
		updateFormJob(jobRule);
	}

	public void renderWithoutAskingForParams() {
		setControlStatus();

		RenderJobRule jobRule = new RenderJobRule(this.reportDesignFile);
		renderJob(jobRule);
		showReportOutputJob(jobRule);
		updateFormJob(jobRule);
	}

	private void initJob(RenderJobRule jobRule) {
		Job initJob = new AbstractJob("Initialize engine", //$NON-NLS-1$
				this.reportDesignFile) {

			public void work(IProgressMonitor monitor) {
				if (!isInitialize) {
					monitor.subTask("Initialize engine"); //$NON-NLS-1$
					init();
					isInitialize = true;
				}
				monitor.subTask("Prepair collect parameters"); //$NON-NLS-1$
				setParameters(getInputParameters(reportDesignFile));
			}
		};
		RenderJobRunner.runRenderJob(initJob, jobRule);
	}

	private void getParamValuesJob(RenderJobRule jobRule) {
		Job getParameterJob = new AbstractUIJob("Collecting parameters", //$NON-NLS-1$
				this.reportDesignFile) {

			public void work(IProgressMonitor monitor) {
				monitor.subTask("Collecting parameters"); //$NON-NLS-1$
				getParameterValues(inputParameters);
			}
		};
		getParameterJob.setSystem(true);
		RenderJobRunner.runRenderJob(getParameterJob, jobRule);
	}

	private void renderJob(RenderJobRule jobRule) {
		Job renderJob = new AbstractJob("Rendering report", //$NON-NLS-1$
				this.reportDesignFile) {

			public void work(IProgressMonitor monitor) {
				monitor.subTask(reportDesignFile);
				renderReport(monitor);
			}
		};
		RenderJobRunner.runRenderJob(renderJob, jobRule);
	}

	private void showReportOutputJob(RenderJobRule jobRule) {
		Job showJob = new AbstractUIJob("Showing report", //$NON-NLS-1$
				this.reportDesignFile) {

			public void work(IProgressMonitor monitor) {
				monitor.subTask("Show report in Browser"); //$NON-NLS-1$
				if (!form.isDisposed()) {
					// browser.setUrl( outputLocation
					// + ( currentBookmark == null ? ""
					// : ( "#" + currentBookmark ) ) );
					browser.setUrl(outputLocation);
					// if special the anchor, SWT browser will not refresh
					// browser.refresh( );
					if (currentPageNum < totalPageNum) {
						navNextAction.setEnabled(true);
						navLastAction.setEnabled(true);
					} else {
						navNextAction.setEnabled(false);
						navLastAction.setEnabled(false);
					}
					if (currentPageNum > 1) {
						navPreAction.setEnabled(true);
						navFirstAction.setEnabled(true);
					} else {
						navPreAction.setEnabled(false);
						navFirstAction.setEnabled(false);
					}
					goPageInput.setText(currentPageNum + ""); //$NON-NLS-1$
					refreshTOC();
				}
			}
		};
		showJob.setSystem(true);
		RenderJobRunner.runRenderJob(showJob, jobRule);
	}

	private void updateFormJob(RenderJobRule jobRule) {
		Job updateFormJob = new AbstractUIJob("Update", "") { //$NON-NLS-1$ //$NON-NLS-2$

			public void work(IProgressMonitor monitor) {
				if (!form.isDisposed()) {
					form.setBusy(false);
					form.setText(MessageFormat.format(TITLE_MESSAGE,
							new Object[] { Long.valueOf(currentPageNum), Long.valueOf(totalPageNum) }));
					reRunReportAction.setEnabled(true);
					paramAction.setEnabled(hasParas);
					navGoAction.setEnabled(true);
					tocAction.setEnabled(true);
					if (hasParas && !assignParamValues && paramValues.isEmpty()) {
						navGoAction.setEnabled(false);
						tocAction.setEnabled(false);
					}
				}
			}
		};
		updateFormJob.setSystem(true);
		RenderJobRunner.runRenderJob(updateFormJob, jobRule);
	}

	protected void setParameters(List inputParameters) {
		this.inputParameters = inputParameters;
	}

	public void setCurrentPage(long page) {
		this.currentPageNum = page;
	}

	public long getCurrentPage() {
		return this.currentPageNum;
	}

}

/**
 * A engine config class, is used to create a hyperlink preview.
 */
class HyperlinkEngineConfig extends EngineConfig {

	/** Path of image files. */
	public static final String IMAGE_PATH = "image"; //$NON-NLS-1$

	/**
	 * constructor
	 */
	public HyperlinkEngineConfig() {
		super();

		HTMLRenderOption emitterConfig = (HTMLRenderOption) getEmitterConfigs().get(RenderOption.OUTPUT_FORMAT_HTML);

		emitterConfig.setImageHandler(new HTMLCompleteImageHandler());
	}
}
