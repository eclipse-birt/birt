/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.editors;

import java.util.HashMap;

import org.eclipse.birt.report.designer.internal.ui.dialogs.InputParameterHtmlDialog;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.preview.PreviewUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.browsers.BrowserManager;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.CloseWindowListener;
import org.eclipse.swt.browser.OpenWindowListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.browser.WindowEvent;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.part.EditorPart;

/**
 * ReportPreviewEditor
 */
public abstract class ReportPreviewEditor extends EditorPart {

	private Button bParameter;

	private Browser browser;

	private Composite mainPane;

	private ProgressBar progressBar;

	protected InputParameterHtmlDialog parameterDialog;

	private HashMap<String, String> options;

	private boolean suggestShowParameterPage;

	/**
	 * Implement this method to save the contents of Report Designer.
	 * <p>
	 * If the save is successful, the part should fire a property changed event
	 * reflecting the new dirty state (<code>PROP_DIRTY</code> property).
	 * </p>
	 * <p>
	 * If the save is canceled through user action, or for any other reason, the
	 * part should invoke <code>setCancelled</code> on the
	 * <code>IProgressMonitor</code> to inform the caller.
	 * </p>
	 * <p>
	 * This method is long-running; progress and cancellation are provided by the
	 * given progress monitor.
	 * </p>
	 * 
	 * @param monitor the progress monitor
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor monitor) {
		IReportProvider provider = getProvider();
		if (provider != null) {
			provider.saveReport(getModel(), getEditorInput(), monitor);
			firePropertyChange(PROP_DIRTY);
		}
		return;
	}

	protected abstract IReportProvider getProvider();

	/**
	 * Is editor in dirty mode.
	 * 
	 * @return edit in dirty mode or not
	 */
	public boolean isDirty() {
		return false;
	}

	/**
	 * Create controls in the preview editor.
	 * 
	 * @param parent parent composite
	 */
	public void createPartControl(Composite parent) {
		final ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		// Create the editor parent composite.
		mainPane = new Composite(sc, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.verticalSpacing = 0;
		mainPane.setLayout(layout);
		mainPane.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Composite buttonTray = new Composite(mainPane, SWT.NONE);
		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.grabExcessHorizontalSpace = true;
		gData.grabExcessVerticalSpace = false;
		buttonTray.setLayoutData(gData);

		layout = new GridLayout(2, false);
		layout.marginWidth = 5;
		layout.horizontalSpacing = 0;
		buttonTray.setLayout(layout);

		bParameter = new Button(buttonTray, SWT.PUSH);
		bParameter.setToolTipText(Messages.getString("PreviewEditor.parameter.hint")); //$NON-NLS-1$
		bParameter.setText(Messages.getString("PreviewEditor.parameter.hint")); //$NON-NLS-1$
		GridData gd = new GridData();
		bParameter.setLayoutData(gd);

		final FormText note = new FormText(buttonTray, SWT.NONE);
		note.setText(
				getDisplayInfoText(
						ViewerPlugin.getDefault().getPluginPreferences().getString(WebViewer.PREVIEW_MAXROW)),
				true, true);
		note.setSize(SWT.DEFAULT - 10, SWT.DEFAULT);
		gd = new GridData();
		gd.horizontalIndent = 20;
		note.setLayoutData(gd);

		note.addHyperlinkListener(new HyperlinkAdapter() {

			public void linkActivated(HyperlinkEvent e) {
				if (PreferencesUtil.createPreferenceDialogOn(UIUtil.getDefaultShell(),
						"org.eclipse.birt.report.designer.ui.preferences.PreviewDataPreferencePage", //$NON-NLS-1$
						new String[] { "org.eclipse.birt.report.designer.ui.preferences.PreviewDataPreferencePage" //$NON-NLS-1$
				}, null).open() == Window.OK) {
					boolean ret = MessageDialog.openQuestion(UIUtil.getDefaultShell(),
							Messages.getString("PreviewEditor.ConfirmRefresh.Title"), //$NON-NLS-1$
							Messages.getString("PreviewEditor.ConfirmRefresh.Message")); //$NON-NLS-1$
					if (ret == true) {
						refresh();
					}
				}
			}

		});

		final IPropertyChangeListener prefListener = new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (note != null && !note.isDisposed() && WebViewer.PREVIEW_MAXROW.equals(event.getProperty())) {
					note.setText(
							getDisplayInfoText(ViewerPlugin.getDefault().getPluginPreferences()
									.getString(WebViewer.PREVIEW_MAXROW)), // $NON-NLS-1$
							true, true);
					buttonTray.layout();
				}
			}
		};

		note.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				ViewerPlugin.getDefault().getPluginPreferences().removePropertyChangeListener(prefListener);
			}
		});

		ViewerPlugin.getDefault().getPluginPreferences().addPropertyChangeListener(prefListener);

		progressBar = new ProgressBar(mainPane, SWT.INDETERMINATE);
		gd = new GridData(GridData.END, GridData.CENTER, false, false);
		gd.heightHint = 10;
		gd.widthHint = 100;
		progressBar.setLayoutData(gd);
		// When initialize preview, show the progress bar
		progressBar.setVisible(false);

		createMainBrowser();

		if (BrowserManager.getInstance().isEmbeddedBrowserPresent()) {
			parameterDialog = new InputParameterHtmlDialog(Display.getCurrent().getActiveShell(),
					InputParameterHtmlDialog.TITLE, getFileUri(), browser);
		}

		if (bParameter != null) {
			bParameter.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					refresh();
				}

			});
		}
		sc.addControlListener(new ControlAdapter() {

			public void controlResized(ControlEvent e) {
				sc.setMinSize(buttonTray.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				mainPane.layout();
			}
		});
		sc.setContent(mainPane);
	}

	private String getDisplayInfoText(String maxRow) {
		return "<form><p><b>"//$NON-NLS-1$
				+ Messages.getString("PreviewEditor.parameter.note")//$NON-NLS-1$
				+ "</b> " //$NON-NLS-1$
				+ Messages.getFormattedString("PreviewEditor.parameter.info", new String[] { maxRow })//$NON-NLS-1$
				+ " (<a>" //$NON-NLS-1$
				+ Messages.getString("PreviewEditor.parameter.change")//$NON-NLS-1$
				+ "</a>)</p></form>";//$NON-NLS-1$
	}

	private void createMainBrowser() {
		if (BrowserManager.getInstance().isEmbeddedBrowserPresent()) {
			if (browser != null && !browser.isDisposed()) {
				browser.dispose();
			}

			// browser = new Browser( mainPane, SWT.NONE );
			if (Constants.OS_LINUX.equalsIgnoreCase(Platform.getOS())) {
				browser = new Browser(mainPane, SWT.MOZILLA);
			} else {
				browser = new Browser(mainPane, SWT.NONE);
			}
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 1;
			browser.setLayoutData(gd);

			// When change the browser location, show the progress bar
			/*
			 * browser.addLocationListener( new LocationAdapter( ) {
			 * 
			 * public void changing( final LocationEvent e ) { progressBar.setVisible( true
			 * ); } } );
			 */

			// When browser loaded completely, the hide the progress bar
			browser.addProgressListener(new ProgressListener() {

				public void changed(ProgressEvent event) {
				}

				public void completed(ProgressEvent event) {
					progressBar.setVisible(false);
				}
			});

			browser.addOpenWindowListener(new OpenWindowListener() {

				public void open(final WindowEvent event) {
					final Shell shell = UIUtil.createDefaultShell();
					shell.setLayout(new FillLayout());
					Browser browser;// = new Browser( shell, SWT.NONE );
					if (Constants.OS_LINUX.equalsIgnoreCase(Platform.getOS())) {
						browser = new Browser(shell, SWT.MOZILLA);
					} else {
						browser = new Browser(shell, SWT.NONE);
					}
					initialize(Display.getCurrent(), browser);
					event.browser = browser;
					shell.open();
				}
			});

			browser.addCloseWindowListener(new CloseWindowListener() {

				public void close(WindowEvent event) {
					// prevent main broswer been accidentally closed by
					// javascript:
					// window.close()
					Display.getCurrent().asyncExec(new Runnable() {

						public void run() {
							createMainBrowser();

							mainPane.layout(true);
						}

					});
				}
			});
		} else {
			Composite control = new Composite(mainPane, SWT.NONE);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 1;
			control.setLayoutData(gd);
			bParameter.setEnabled(false);
			progressBar.setVisible(false);
			control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
			control.setLayout(new GridLayout());
			Label message = new Label(control, SWT.NONE);
			message.setText(Messages.getString("PreviewEditor.browser.notfound")); //$NON-NLS-1$
			message.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		}
	}

	protected boolean refresh() {
		return true;
	}

	protected void enableParameterControl(boolean value) {
		if (bParameter != null && !bParameter.isDisposed()) {
			bParameter.setEnabled(value);
		}
	}

	protected void showProgress() {
		if (progressBar != null && !progressBar.isDisposed()) {
			progressBar.setVisible(true);
		}
	}

	protected void hideProgress() {
		if (progressBar != null && !progressBar.isDisposed()) {
			progressBar.setVisible(false);
		}
	}

	/**
	 * initialize browser.
	 * 
	 * @param display Display
	 * @param browser Browser
	 * 
	 * @return
	 */

	private static void initialize(final Display display, Browser browser) {
		browser.addOpenWindowListener(new OpenWindowListener() {

			public void open(final WindowEvent event) {
				final Shell shell = UIUtil.createDefaultShell();
				shell.setLayout(new FillLayout());
				Browser browser;// = new Browser( shell, SWT.NONE );
				if (Constants.OS_LINUX.equalsIgnoreCase(Platform.getOS())) {
					browser = new Browser(shell, SWT.MOZILLA);
				} else {
					browser = new Browser(shell, SWT.NONE);
				}
				initialize(display, browser);
				event.browser = browser;
				shell.open();
			}
		});

		browser.addTitleListener(new TitleListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.browser.TitleListener#changed(org.eclipse.swt
			 * .browser.TitleEvent)
			 */
			public void changed(TitleEvent event) {
				if (event.title != null && event.title.length() > 0) {
					Browser browser = (Browser) event.widget;

					Shell shell = browser.getShell();

					shell.setText(event.title);
				}
			}
		});
	}

	/**
	 * Get model instance.
	 * 
	 * @return model instance
	 */
	public ModuleHandle getModel() {
		IReportProvider provider = getProvider();

		if (provider != null) {
			return provider.queryReportModuleHandle();
		}

		return null;
	}

	protected void createActions() {
		// // Add page actions
		// Action action = LayoutPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
		//
		// action = NormalPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
		//
		// action = MasterPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
		//
		// action = PreviewPageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
		//
		// action = CodePageAction.getInstance( );
		// getActionRegistry( ).registerAction( action );
		// getSelectionActions( ).add( action.getId( ) );
	}

	/**
	 * Get report design file uri.
	 * 
	 */
	public String getFileUri() {
		IEditorInput input = getEditorInput();

		if (input != null) {
			IReportProvider provider = getProvider();
			if (provider != null) {
				return provider.getInputPath(input).toOSString();
			}
		}

		return null;
	}

	/**
	 * Refresh swt browser
	 */
	public void display() {
		String uri = getFileUri();

		if (uri != null && uri.length() > 0) {
			if (this.options == null) {
				this.options = new HashMap<String, String>();
				this.options.put(WebViewer.SERVLET_NAME_KEY, InputParameterHtmlDialog.VIEWER_RUN);
				this.options.put(WebViewer.FORMAT_KEY, WebViewer.HTML);
			}
			this.options.put(WebViewer.RESOURCE_FOLDER_KEY, ReportPlugin.getDefault().getResourceFolder());
			this.options.put(WebViewer.MAX_ROWS_KEY,
					ViewerPlugin.getDefault().getPluginPreferences().getString(WebViewer.PREVIEW_MAXROW));
//			this.options.put( WebViewer.MAX_CUBE_ROW_LEVELS_KEY,
//					ViewerPlugin.getDefault( )
//							.getPluginPreferences( )
//							.getString( WebViewer.PREVIEW_MAXCUBEROWLEVEL ) );
//			this.options.put( WebViewer.MAX_CUBE_COLUMN_LEVELS_KEY,
//					ViewerPlugin.getDefault( )
//							.getPluginPreferences( )
//							.getString( WebViewer.PREVIEW_MAXCUBECOLUMNLEVEL ) );
			String extKey = ViewerPlugin.getDefault().getPluginPreferences()
					.getString(WebViewer.APPCONTEXT_EXTENSION_KEY);
			if (extKey != null && extKey.length() > 0) {
				this.options.put(WebViewer.APPCONTEXT_EXTENSION_KEY, extKey);
			} else {
				this.options.remove(WebViewer.APPCONTEXT_EXTENSION_KEY);
			}

			if (suggestShowParameterPage) {
				this.options.put(WebViewer.SHOW_PARAMETER_PAGE_KEY, "true"); //$NON-NLS-1$
			} else {
				this.options.remove(WebViewer.SHOW_PARAMETER_PAGE_KEY);
			}

			PreviewUtil.clearSystemProperties();
			PreviewUtil.setSystemProperties();
			// System.setProperty( IPreviewConstants.MAX_CUBE_ROW_LEVELS,
//					ViewerPlugin.getDefault( )
//							.getPluginPreferences( )
//							.getString( WebViewer.PREVIEW_MAXCUBEROWLEVEL ) );
//			System.setProperty( IPreviewConstants.MAX_CUBE_COLUMN_LEVELS,
//					ViewerPlugin.getDefault( )
//							.getPluginPreferences( )
//							.getString( WebViewer.PREVIEW_MAXCUBECOLUMNLEVEL ) );

			if (browser != null && !browser.isDisposed()) {
				WebViewer.display(uri, browser, this.options);
			} else {
				WebViewer.display(uri, this.options);
			}
		}
	}

	/**
	 * handle something when try to leave the page
	 * 
	 */
	public void handleLeaveThePage() {
		if (browser != null && !browser.isDisposed()) {
			WebViewer.cancel(browser);
			browser.stop();
			browser.setUrl("about:blank"); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		super.dispose();

		if (browser != null && !browser.isDisposed()) {
			WebViewer.cancel(browser);
			browser.stop();
		}

		bParameter = null;
		browser = null;
	}

	public void doSaveAs() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.setSite(site);

		setInput(input);
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void setFocus() {
	}

	public Browser getBrowser() {
		return this.browser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(ActionRegistry.class)) {
			new ActionRegistry();
		}
		return super.getAdapter(adapter);
	}
}
