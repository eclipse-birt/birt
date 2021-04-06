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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.dialogs.InputParameterHtmlDialog;
import org.eclipse.birt.report.designer.internal.ui.editors.IAdvanceReportEditorPage;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.viewer.browsers.BrowserManager;
import org.eclipse.birt.report.viewer.utilities.IWebAppInfo;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * Preview Form Page.
 */
public class ReportPreviewFormPage extends ReportPreviewEditor implements IAdvanceReportEditorPage {

	protected static final Logger logger = Logger.getLogger(ReportPreviewFormPage.class.getName());

	public static final String ID = "org.eclipse.birt.report.designer.ui.editors.preview.web"; //$NON-NLS-1$

	private Control control;

	private int staleType;

	private FormEditor editor;

	private boolean isPreviewing;

	// suffix of template file
	public static final String SUFFIX_TEMPLATE_FILE = "rpttemplate"; //$NON-NLS-1$

	// suffix of design config file
	public static final String SUFFIX_DESIGN_CONFIG = "rptconfig"; //$NON-NLS-1$

	// property type
	public static final String PROP_TYPE = "type"; //$NON-NLS-1$

	// Property -- value expression
	public static final String PROP_EXPR = "expr"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#onBroughtToTop
	 * (org.eclipse.birt.report.designer.ui.editors.IReportEditorPage)
	 */
	public boolean onBroughtToTop(IReportEditorPage prePage) {
		if (getEditorInput() != prePage.getEditorInput()) {
			setInput(prePage.getEditorInput());
		}

		boolean bool = true;
		// if the model is dirty, save it at first.
		if (isDirtyModel()) {
			doSave(null);
			bool = false;
		}

		// save the last changes.
		if (prePage.isDirty() && bool) {
			prePage.doSave(null);
		}

		boolean ret = refresh();
		if (ret == false && isMissingParameter()) {
			// if miss parameter yet, can't preview report and scroll to
			// the previous page.
			editor.setActivePage(prePage.getId());
		}

		return ret;
	}

	protected boolean refresh() {
		if (isPreviewing)
			return false;
		isPreviewing = true;

		ModuleHandle model = getModel();

		if (!UIUtil.canPreviewWithErrors(model)) {
			isPreviewing = false;
			return false;
		}

		boolean isDisplay = false;

		if (BrowserManager.getInstance().isEmbeddedBrowserPresent()) {
			showProgress();
		}

		if (hasParameters()) {
			if (parameterDialog != null) {
				parameterDialog.open();

				// if parameter dialog closed successfully, then preview the
				// current report
				if (parameterDialog.getReturnCode() == InputParameterHtmlDialog.RETURN_CODE_BROWSER_CLOSED) {
					isDisplay = true;

					if (isMissingParameter()) {
						isPreviewing = false;
						return false;
					}
				} else {
					hideProgress();
				}
			} else {
				isDisplay = true;
			}
		} else {
			isDisplay = true;
		}

		if (isDisplay) {
			display();
		}

		ReportRequest request = new ReportRequest(ReportPreviewFormPage.this);
		List list = new ArrayList();
		// Fix bug 223758, let attribute view page show a empty page.
		list.add(new Object());

		request.setSelectionObject(list);
		request.setType(ReportRequest.SELECTION);

		// SessionHandleAdapter.getInstance().getMediator().pushState();
		SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);
		isPreviewing = false;
		return true;
	}

	private boolean isDirtyModel() {
		if (getModel() != null) {
			return getModel().needsSave();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	public Control getPartControl() {
		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getId()
	 */
	public String getId() {
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		Control[] children = parent.getChildren();
		control = children[children.length - 1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#markPageStale
	 * (int)
	 */
	public void markPageStale(int type) {
		staleType = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#getStaleType ()
	 */
	public int getStaleType() {
		return staleType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty() {
		return false;
	}

	protected IReportProvider getProvider() {
		return (IReportProvider) editor.getAdapter(IReportProvider.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter.equals(ActionRegistry.class)) {
			return new ActionRegistry();
		}
		return super.getAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#initialize(org.eclipse.ui.forms
	 * .editor.FormEditor)
	 */
	public void initialize(FormEditor editor) {
		this.editor = editor;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getEditor()
	 */
	public FormEditor getEditor() {
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getManagedForm()
	 */
	public IManagedForm getManagedForm() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#setActive(boolean)
	 */
	public void setActive(boolean active) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#isActive()
	 */
	public boolean isActive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#canLeaveThePage()
	 */
	public boolean canLeaveThePage() {
		handleLeaveThePage();
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getIndex()
	 */
	public int getIndex() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#setIndex(int)
	 */
	public void setIndex(int index) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#isEditor()
	 */
	public boolean isEditor() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#selectReveal(java.lang.Object)
	 */
	public boolean selectReveal(Object object) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	public void setInput(IEditorInput input) {
		super.setInput(input);
		if (parameterDialog != null)
			parameterDialog.setUri(getFileUri());
	}

	/**
	 * Get parameter values from config file.
	 * 
	 * @return Map
	 */
	private Map<String, ?> getConfigVars() {
		Map<String, Object> configVars = new HashMap<String, Object>();

		// get design config file name
		String configFileName = getConfigFileName(this.getFileUri());
		if (configFileName == null)
			return configVars;

		ReportDesignHandle handle = null;

		try {
			// Generate the session handle
			SessionHandle sessionHandle = SessionHandleAdapter.getInstance().getSessionHandle();

			File configFile = new File(configFileName);

			// if config file existed, then delete it
			if (configFile != null && configFile.exists() && configFile.isFile()) {
				handle = sessionHandle.openDesign(configFileName);

				if (handle != null) {
					// get parameter values from config file
					Iterator it = handle.configVariablesIterator();
					while (it != null && it.hasNext()) {
						ConfigVariableHandle configVar = (ConfigVariableHandle) it.next();
						if (configVar != null && configVar.getName() != null) {
							String varName = prepareConfigVarName(configVar.getName());

							// check the parameter whether exist or not
							String paramName = getParameterName(varName);

							// get parameter handle
							ScalarParameterHandle parameter = findParameter(paramName);

							if (parameter != null) {
								// get cached parameter type
								String typeVarName = varName + "_" //$NON-NLS-1$
										+ PROP_TYPE + "_"; //$NON-NLS-1$
								ConfigVariable typeVar = handle.findConfigVariable(typeVarName);
								String dataType = null;
								if (typeVar != null)
									dataType = typeVar.getValue();

								// if null or data type changed, skip it
								if (dataType == null || !dataType.equalsIgnoreCase(parameter.getDataType()))
									continue;

								// find cached parameter value expression
								String exprVarName = varName + "_" //$NON-NLS-1$
										+ PROP_EXPR + "_"; //$NON-NLS-1$
								ConfigVariable exprVar = handle.findConfigVariable(exprVarName);
								String expr = parameter.getValueExpr();
								String cachedExpr = null;
								if (exprVar != null)
									cachedExpr = exprVar.getValue();

								if (cachedExpr == null)
									cachedExpr = ""; //$NON-NLS-1$
								if (expr == null)
									expr = ""; //$NON-NLS-1$

								// if value expression changed,skip it
								if (!cachedExpr.equals(expr))
									continue;
							}

							if (paramName != null && paramName.length() > 0) {
								if (DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE
										.equalsIgnoreCase(parameter.getParamType())) {
									// handle multi-value parameter
									List values = (List) configVars.get(paramName);
									if (values == null) {
										values = new ArrayList();
										configVars.put(paramName, values);
									}
									values.add(configVar.getValue());
								} else {
									configVars.put(paramName, configVar.getValue());
								}
							}
						}
					}
					handle.close();
				}
			}
		} catch (DesignFileException e) {
			// close handle
//			try
//			{
//				if ( handle != null )
//				{
//					handle.close( );
//				}
//			}
//			catch ( Exception e1 )
//			{
//				logger.log( Level.SEVERE, e.getMessage( ), e );
//			}
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		return configVars;
	}

	/**
	 * Delete the last "_" part
	 * 
	 * @param name
	 * @return String
	 */
	private String prepareConfigVarName(String name) {
		int index = name.lastIndexOf("_"); //$NON-NLS-1$
		return name.substring(0, index);
	}

	/**
	 * if parameter existed in config file, return the correct parameter name
	 * 
	 * @param configVarName
	 * @return String
	 */
	private String getParameterName(String configVarName) {
		String paramName = null;
		List parameters = null;

		// get parameter list from design handle
		ModuleHandle model = getModel();
		if (model != null) {
			parameters = model.getFlattenParameters();
		}

		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				ScalarParameterHandle parameter = null;

				if (parameters.get(i) instanceof ScalarParameterHandle) {
					parameter = ((ScalarParameterHandle) parameters.get(i));
				}

				// get current name
				String curName = null;
				if (parameter != null && parameter.getName() != null) {
					curName = parameter.getName() + "_" + parameter.getID(); //$NON-NLS-1$
				}

				// if find the parameter exist, return true
				if (curName != null && curName.equalsIgnoreCase(configVarName)) {
					paramName = parameter.getName();
					break;
				}
			}
		}

		return paramName;
	}

	/**
	 * Find parameter by name
	 * 
	 * @param paramName
	 * @return ScalarParameterHandle
	 */
	private ScalarParameterHandle findParameter(String paramName) {
		if (paramName == null)
			return null;

		ScalarParameterHandle parameter = null;
		List parameters = null;

		// get parameter list from design handle
		ModuleHandle model = getModel();
		if (model != null) {
			parameters = model.getFlattenParameters();
		}

		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				if (parameters.get(i) instanceof ScalarParameterHandle) {
					parameter = ((ScalarParameterHandle) parameters.get(i));
				}

				if (parameter != null && paramName.equalsIgnoreCase(parameter.getName())) {
					break;
				}
			}
		}

		return parameter;
	}

	private boolean hasParameters() {
		IWebAppInfo webapp = WebViewer.getCurrentWebApp();

		if (webapp != null && webapp.useCustomParamHandling()) {
			return false;
		}

		ModuleHandle model = getModel();

		List parameters = model.getFlattenParameters();

		if (parameters != null) {
			for (Object p : parameters) {
				if (p instanceof ParameterHandle && !((ParameterHandle) p).isHidden()) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * If miss parameter.
	 * 
	 * @return boolean
	 */
	protected boolean isMissingParameter() {
		IWebAppInfo webapp = WebViewer.getCurrentWebApp();

		if (webapp != null && webapp.useCustomParamHandling()) {
			return false;
		}

		boolean missingParameter = false;

		ModuleHandle model = getModel();

		Map<String, ?> params = this.getConfigVars();
		List parameters = model.getFlattenParameters();
		if (parameters != null) {
			for (int i = 0; i < parameters.size(); i++) {
				if (parameters.get(i) instanceof ScalarParameterHandle) {
					ScalarParameterHandle parameter = ((ScalarParameterHandle) parameters.get(i));

					if (parameter.isHidden() || !parameter.isRequired()) {
						continue;
					}

					if (params == null) {
						missingParameter = true;
						break;
					}

					Object valueObj = params.get(parameter.getName());
					if (valueObj == null) {
						missingParameter = true;
						break;
					}

					if (valueObj instanceof List) {
						// multi-value parameter
						// handle multi-value parameter
						List values = (List) valueObj;
						for (int j = 0; j < values.size(); j++) {
							Object value = values.get(j);
							if (value == null) {
								missingParameter = true;
								break;
							}

							if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(parameter.getDataType())
									&& ((String) value).length() <= 0) {
								missingParameter = true;
								break;
							}
						}

						if (missingParameter)
							break;
					} else {
						if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(parameter.getDataType())
								&& ((String) valueObj).length() <= 0) {
							missingParameter = true;
							break;
						}
					}
				}
			}
		}

		return missingParameter;
	}

	/**
	 * Parse config file name from report design filename.
	 * 
	 * @param reportDesignName String
	 * @return String
	 */

	private String getConfigFileName(String reportDesignName) {
		if (reportDesignName == null)
			return null;

		String[] result = reportDesignName.split("\\."); //$NON-NLS-1$
		String extensionName = result[result.length - 1];
		String configFileName = reportDesignName.substring(0, reportDesignName.length() - extensionName.length())
				+ SUFFIX_DESIGN_CONFIG;

		return configFileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#firePropertyChange(int)
	 */
	protected void firePropertyChange(int type) {
		if (type == PROP_DIRTY) {
			editor.editorDirtyStateChanged();
		} else {
			super.firePropertyChange(type);
		}
	}

	protected void finalize() throws Throwable {
		if (Policy.TRACING_PAGE_CLOSE) {
			System.out.println("Report preview page finalized"); //$NON-NLS-1$
		}
		super.finalize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.IAdvanceReportEditorPage
	 * #isSensitivePartChange()
	 */
	public boolean isSensitivePartChange() {
		return false;
	}
}