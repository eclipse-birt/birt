/*************************************************************************************
 * Copyright (c) 2004-2009 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.script;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.ui.swt.custom.CustomChooserComposite;
import org.eclipse.birt.core.ui.swt.custom.TextCombo;
import org.eclipse.birt.core.ui.swt.custom.TextComboViewer;
import org.eclipse.birt.report.designer.core.mediator.IMediatorColleague;
import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptElementNode;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptObjectNode;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.script.JSSyntaxContext;
import org.eclipse.birt.report.designer.internal.ui.script.ScriptValidator;
import org.eclipse.birt.report.designer.internal.ui.views.data.DataViewTreeViewerPage;
import org.eclipse.birt.report.designer.internal.ui.views.outline.DesignerOutlinePage;
import org.eclipse.birt.report.designer.internal.ui.views.property.ReportPropertySheetPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.scripts.IScriptContextInfo;
import org.eclipse.birt.report.designer.ui.scripts.IScriptContextProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage;
import org.eclipse.birt.report.designer.ui.views.attributes.IAttributeViewPage;
import org.eclipse.birt.report.designer.ui.views.data.IDataViewPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.ITemplateMethodInfo;
import org.eclipse.core.commands.operations.IUndoableOperation;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.text.undo.DocumentUndoEvent;
import org.eclipse.text.undo.DocumentUndoManagerRegistry;
import org.eclipse.text.undo.IDocumentUndoListener;
import org.eclipse.text.undo.IDocumentUndoManager;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;

/**
 * Main class of javaScript editor
 */
public class JSEditor extends EditorPart implements IMediatorColleague {

	protected static Logger logger = Logger.getLogger(JSEditor.class.getName());

	private static final String NO_EXPRESSION = Messages.getString("JSEditor.Display.NoExpression"); //$NON-NLS-1$

	static final String CLIENT_CONTEXT = "client"; //$NON-NLS-1$

	static final String CLIENT_SCRIPTS = "clientScripts";//$NON-NLS-1$ ;

	static final String METHOD_DISPLAY_INDENT = "  "; //$NON-NLS-1$

	static final String VIEWER_CATEGORY_KEY = "Category"; //$NON-NLS-1$

	static final String VIEWER_CATEGORY_CONTEXT = "context"; //$NON-NLS-1$

	private IEditorPart editingDomainEditor;

	Combo cmbExpList = null;

	TextCombo cmbSubFunctions = null;

	ComboViewer cmbExprListViewer;

	TextComboViewer cmbSubFunctionsViewer;

	private IPropertyDefn cmbItemLastSelected = null;

	private boolean editorUIEnabled = true;

	/** the tool bar for validation */
	private ToolBar validateTool = null;

	/** the tool button for Reset */
	private ToolItem butReset = null;

	/** the tool button for Validate */
	private ToolItem butValidate = null;

	/** the tool button for enable description */
	private ToolItem butHelp = null;

	/** the icon for validator, default hide */
	private Label validateIcon = null;

	/** the main pane */
	private Composite mainPane = null;

	/** the description pane */
	private Composite descriptionPane = null;

	/** the script's description about current method. */
	private Text descriptionText = null;

	private Label ano;

	private final HashMap<Object, Object> selectionMap = new HashMap<>();

	private boolean isModified;

	private Object editObject;

	private boolean isSaveScript = false;
	/**
	 * Palette page
	 */
	public TreeViewPalettePage palettePage = new TreeViewPalettePage();

	/** the script editor, dosen't include controller. */
	private IScriptEditor scriptEditor;

	/** the script validator */
	private ScriptValidator scriptValidator = null;

	/** the flag if the text listener is enabled. */
	private boolean isTextListenerEnable = true;

	/** the location which is not dirty. */
	private int cleanPoint = -1;

	/** Indicates that the described document event is about to be undone. */
	private boolean undoing = false;

	/** The listener for document changed. */
	private final IDocumentListener documentListener = new IDocumentListener() {

		@Override
		public void documentAboutToBeChanged(DocumentEvent event) {
		}

		@Override
		public void documentChanged(DocumentEvent event) {
			if (isTextListenerEnable) {
				markDirty();
			}

			// Disables buttons if text is empty.
			String text = getEditorText();
			boolean butEnabled = editorUIEnabled && text != null && text.length() > 0;

			butReset.setEnabled(butEnabled);
			butValidate.setEnabled(butEnabled);
		}
	};

	/** the listener for undo operation. */
	private final IDocumentUndoListener undoListener = new IDocumentUndoListener() {

		/** The latest clear point for redoing. */
		private int lastClearPoint = -1;

		@Override
		public void documentUndoNotification(DocumentUndoEvent event) {
			if (event == null) {
				return;
			}

			int type = event.getEventType();
			boolean undone = (type & DocumentUndoEvent.UNDONE) != 0;
			boolean redone = (type & DocumentUndoEvent.REDONE) != 0;

			undoing = (type & (DocumentUndoEvent.ABOUT_TO_REDO | DocumentUndoEvent.ABOUT_TO_UNDO)) != 0;

			if (undoing || !(undone || redone)) {
				return;
			}

			if (undone) {
				lastClearPoint = cleanPoint;
				if (cleanPoint != getUndoLevel() - 1) {
					// Does nothing if not clean point.
					return;
				}
			} else if (redone) {
				if (cleanPoint < 0) {
					cleanPoint = lastClearPoint;
				}
				if (cleanPoint != getUndoLevel() + 1) {
					// Does nothing if not clean point.
					return;
				}
			}

			// Removes dirty flag when undo/redo to the clean point.
			setIsModified(false);
			((IFormPage) getParentEditor()).getEditor().editorDirtyStateChanged();

			firePropertyChange(PROP_DIRTY);
		}
	};

	private ISelectionChangedListener propertyDefnChangeListener = new ISelectionChangedListener() {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (selection != null) {
				Object[] sel = ((IStructuredSelection) selection).toArray();
				if (sel.length == 1) {
					if (sel[0] instanceof IPropertyDefn) {

						// Save the current expression into the DE using DE API
						DesignElementHandle desHandle = (DesignElementHandle) cmbExprListViewer.getInput();
						saveModel();

						// Update the editor to display the expression
						// corresponding to the selected combo item ( method
						// name/ expression name )
						IPropertyDefn elePropDefn = (IPropertyDefn) sel[0];
						cmbItemLastSelected = elePropDefn;

						setEditorText(desHandle.getStringProperty(elePropDefn.getName()));
						if (event.getSource() == cmbExprListViewer) {
							// Store the main selection state only
							selectionMap.put(getModel(), selection);
						} else if (event.getSource() == cmbSubFunctionsViewer) {
							// Store both the main and sub selection state here.
							List<Object> selectionList = new ArrayList<>();
							selectionList
									.add(((StructuredSelection) cmbExprListViewer.getSelection()).getFirstElement());
							selectionList.add(((StructuredSelection) selection).getFirstElement());
							selectionMap.put(getModel(), new StructuredSelection(selectionList));
						}

						String method = cmbItemLastSelected.getName();

						updateScriptContext(desHandle, method);
						updateMethodDescription(method);
						refreshAll();
					}
				}
			}
		}

	};

	/**
	 * JSEditor - constructor
	 */
	public JSEditor(IEditorPart parent) {
		super();
		this.editingDomainEditor = parent;
		setSite(parent.getEditorSite());
		scriptEditor = createScriptEditor();
	}

	/**
	 * Creates script editor, dosen't include controller
	 *
	 * @return a script editor
	 */
	protected IScriptEditor createScriptEditor() {
		return new ScriptEditor(getParentEditor());
	}

	/**
	 * @see AbstractTextEditor#doSave(IProgressMonitor )
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		saveModel();
	}

	public void doSave(IProgressMonitor monitor, boolean chnageText) {
		isSaveScript = !chnageText;
		saveModel();
		isSaveScript = false;
	}

	@Override
	public boolean isDirty() {
		return isCodeModified();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.editors.text.TextEditor#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * disposes all color objects
	 */
	@Override
	public void dispose() {
		// colorManager.dispose( );

		// remove the mediator listener
		// SessionHandleAdapter.getInstance( )
		// .getMediator( root )
		// .removeColleague( this );
		selectionMap.clear();
		editingDomainEditor = null;

		if (scriptEditor != null) {
			scriptEditor.dispose();
			scriptEditor = null;
		}

		super.dispose();
		// ( (ReportMultiPageEditorSite) getSite( ) ).dispose( );
		((MultiPageEditorSite) getSite()).dispose();
	}

	// Parameter names are constructed by taking the java class name
	// and make the first letter lowercase.
	// If there are more than 2 uppercase letters, it's shortened as the list of
	// those. For instance IChartScriptContext becomes icsc

	protected static String convertToParameterName(String fullName) {
		// strip the full qualified name
		fullName = fullName.substring(fullName.lastIndexOf('.') + 1);
		int upCase = 0;
		SortedMap<Object, Object> caps = new TreeMap<>();
		for (int i = 0; i < fullName.length(); i++) {
			char character = fullName.charAt(i);
			if (Character.isUpperCase(character)) {
				upCase++;
				caps.put(Integer.valueOf(i), Integer.valueOf(character));

			}
		}
		if (upCase > 2) {
			StringBuilder result = new StringBuilder();
			for (Iterator<Object> iter = caps.values().iterator(); iter.hasNext();) {
				result.append((char) ((Integer) iter.next()).intValue());
			}
			return result.toString().toLowerCase();
		} else {
			return fullName.substring(0, 1).toLowerCase() + fullName.substring(1);
		}
	}

	private void updateExtensionScriptContext(Object[] adapters, JSSyntaxContext context, String contextName,
			String methodName) {
		if (adapters == null) {
			return;
		}

		for (Object adapt : adapters) {
			IScriptContextProvider contextProvider = (IScriptContextProvider) adapt;

			if (contextProvider != null) {
				IScriptContextInfo[] infos;

				if (methodName == null) {
					infos = contextProvider.getScriptContext(contextName);
				} else {
					infos = contextProvider.getScriptContext(contextName, methodName);
				}

				if (infos != null) {
					for (IScriptContextInfo info : infos) {
						if (info != null) {
							String name = info.getName();
							IClassInfo type = info.getType();

							if (name != null && type != null) {
								context.setVariable(name, type);
							}
						}
					}
				}
			}
		}
	}

	private void updateScriptContext(DesignElementHandle handle, String method) {
		List args = DEUtil.getDesignElementMethodArgumentsInfo(handle, method);
		JSSyntaxContext context = scriptEditor.getContext();

		context.clear();

		for (Iterator iter = args.iterator(); iter.hasNext();) {
			IArgumentInfo element = (IArgumentInfo) iter.next();
			String name = element.getName();
			String type = element.getType();

			// try load system class info first, if failed, then try extension
			// class info
			if (!context.setVariable(name, type)) {
				context.setVariable(name, element.getClassType());
			}
		}

		Object[] adapters = ElementAdapterManager.getAdapters(handle, IScriptContextProvider.class);

		// update script context from adapter
		updateExtensionScriptContext(adapters, context, method, null);

		if (handle instanceof ExtendedItemHandle) {
			ExtendedItemHandle exHandle = (ExtendedItemHandle) handle;

			List mtds = exHandle.getMethods(method);

			// TODO implement better function-wise code assistant.

			if (mtds != null && mtds.size() > 0) {
				for (int i = 0; i < mtds.size(); i++) {
					IMethodInfo mi = (IMethodInfo) mtds.get(i);

					for (Iterator<IArgumentInfoList> itr = mi.argumentListIterator(); itr.hasNext();) {
						IArgumentInfoList ailist = itr.next();

						for (Iterator<IArgumentInfo> argItr = ailist.argumentsIterator(); argItr.hasNext();) {
							IArgumentInfo aiinfo = argItr.next();

							String argName = aiinfo.getName();
							IClassInfo ci = aiinfo.getClassType();

							if (argName == null || argName.length() == 0) {
								argName = convertToParameterName(ci.getName());
							}

							context.setVariable(argName, ci);
						}
					}

					// update script context from adapter
					if (mi.getName() != null) {
						updateExtensionScriptContext(adapters, context, method, mi.getName());
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite child = this.initEditorLayout(parent);

		// Script combo
		cmbExprListViewer = new ComboViewer(cmbExpList);
		JSExpListProvider provider = new JSExpListProvider();
		cmbExprListViewer.setContentProvider(provider);
		cmbExprListViewer.setLabelProvider(provider);
		cmbExprListViewer.setData(VIEWER_CATEGORY_KEY, VIEWER_CATEGORY_CONTEXT);

		// SubFunctions combo
		JSSubFunctionListProvider subProvider = new JSSubFunctionListProvider(this);

		// also add subProvider as listener of expr viewer.
		cmbExprListViewer.addSelectionChangedListener(subProvider);

		cmbSubFunctions.addListener(CustomChooserComposite.DROPDOWN_EVENT, new Listener() {

			@Override
			public void handleEvent(Event event) {
				cmbSubFunctions.deselectAll();

				ScriptParser parser = new ScriptParser(getEditorText());

				Collection<IScriptMethodInfo> coll = parser.getAllMethodInfo();

				for (Iterator<IScriptMethodInfo> itr = coll.iterator(); itr.hasNext();) {
					IScriptMethodInfo mtd = itr.next();

					cmbSubFunctions.markSelection(METHOD_DISPLAY_INDENT + mtd.getName());
				}
			}

		});

		cmbSubFunctionsViewer = new TextComboViewer(cmbSubFunctions);
		cmbSubFunctionsViewer.setContentProvider(subProvider);
		cmbSubFunctionsViewer.setLabelProvider(subProvider);
		cmbSubFunctionsViewer.addSelectionChangedListener(subProvider);
		cmbSubFunctionsViewer.addSelectionChangedListener(propertyDefnChangeListener);

		// Initialize the model for the document.
		Object model = getModel();
		if (model != null) {
			cmbExpList.setVisible(true);
			cmbSubFunctions.setVisible(true);
			setComboViewerInput(model);
		} else {
			setComboViewerInput(Messages.getString("JSEditor.Input.trial")); //$NON-NLS-1$
		}
		cmbExprListViewer.addSelectionChangedListener(palettePage.getSupport());
		cmbExprListViewer.addSelectionChangedListener(propertyDefnChangeListener);

		scriptEditor.createPartControl(child);
		scriptValidator = new ScriptValidator(getViewer());

		disableEditor();

		SourceViewer viewer = getViewer();
		IDocument document = viewer == null ? null : viewer.getDocument();

		if (document != null) {
			IDocumentUndoManager undoManager = DocumentUndoManagerRegistry.getDocumentUndoManager(document);

			if (undoManager != null) {
				undoManager.addDocumentUndoListener(undoListener);
			}
			document.addDocumentListener(documentListener);
		}
	}

	/**
	 * Connect the root to add the listener
	 *
	 * @param root
	 */
	public void connectRoot(ModuleHandle root) {
		if (root == null) {
			root = SessionHandleAdapter.getInstance().getReportDesignHandle();
		}

		SessionHandleAdapter.getInstance().getMediator(root).addColleague(this);
	}

	/**
	 * DisConnect the root to add the listener
	 *
	 * @param root
	 */
	public void disConnectRoot(ModuleHandle root) {
		if (root == null) {
			root = SessionHandleAdapter.getInstance().getReportDesignHandle();
		}

		SessionHandleAdapter.getInstance().getMediator(root).removeColleague(this);
	}

	/**
	 * Sets the status of the text listener.
	 *
	 * @param enabled <code>true</code> if enable, <code>false</code> otherwise.
	 */
	private void setTextListenerEnable(boolean enabled) {
		isTextListenerEnable = enabled;
	}

	/**
	 * Get current edit element, not report design model.
	 *
	 * @return
	 */
	public Object getModel() {
		// return cmbExprListViewer.getInput( );
		return editObject;
	}

	/**
	 * Returns parent editor.
	 *
	 * @return parent editor.
	 */
	public IEditorPart getParentEditor() {
		return editingDomainEditor;
	}

	private void updateAnnotationLabel(Object handle) {
		String name = ProviderFactory.createProvider(handle).getNodeDisplayName(handle);

		if (name == null) {
			ano.setText(""); //$NON-NLS-1$
		} else {
			ano.setText(name);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter.equals(ITextEditor.class)) {
			if (scriptEditor instanceof ITextEditor) {
				return scriptEditor;
			}
			return null;
		} else if (adapter == ActionRegistry.class) {
			return scriptEditor.getActionRegistry();
		} else if (adapter == PalettePage.class) {
			if (cmbExprListViewer != null) {
				cmbExprListViewer.addSelectionChangedListener(palettePage.getSupport());
			}

			palettePage.setViewer(getViewer());
			return palettePage;
		} else if (adapter == IContentOutlinePage.class) {
			// Add JS Editor as a selection listener to Outline view selections.
			// outlinePage.addSelectionChangedListener( jsEditor );
			DesignerOutlinePage outlinePage = new DesignerOutlinePage(
					SessionHandleAdapter.getInstance().getReportDesignHandle());

			return outlinePage;
		} else if (adapter == IPropertySheetPage.class) {
			ReportPropertySheetPage sheetPage = new ReportPropertySheetPage(
					SessionHandleAdapter.getInstance().getReportDesignHandle());
			return sheetPage;
		} else if (adapter == IDataViewPage.class) {
			DataViewTreeViewerPage page = new DataViewTreeViewerPage(
					SessionHandleAdapter.getInstance().getReportDesignHandle());
			return page;
		} else if (adapter == IAttributeViewPage.class) {
			AttributeViewPage page = new AttributeViewPage(SessionHandleAdapter.getInstance().getReportDesignHandle());
			return page;
		} else if (adapter == ITextEditor.class) {
			return scriptEditor;
		}

		return super.getAdapter(adapter);
	}

	protected PropertyHandle getPropertyHandle() {
		if (editObject instanceof DesignElementHandle) {
			DesignElementHandle desHdl = (DesignElementHandle) editObject;
			if (cmbItemLastSelected != null) {
				return desHdl.getPropertyHandle(cmbItemLastSelected.getName());
			}
		}
		return null;
	}

	/**
	 *
	 * initEditorLayout - initialize the UI components of the editor
	 *
	 */
	private Composite initEditorLayout(Composite parent) {
		// Create the editor parent composite.
		mainPane = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();

		mainPane.setLayout(layout);

		createController(mainPane);
		createDescriptionPane(mainPane);

		Composite editorPane = new Composite(mainPane, SWT.NONE);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);

		layout = new GridLayout();
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		editorPane.setLayout(layout);
		editorPane.setLayoutData(layoutData);

		final Composite sep = new Composite(editorPane, SWT.NONE);

		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.heightHint = 1;
		sep.setLayoutData(layoutData);
		sep.addPaintListener(new PaintListener() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse
			 * .swt.events.PaintEvent)
			 */
			@Override
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				Rectangle rect = sep.getBounds();
				gc.setForeground(ReportColorConstants.DarkGrayForground);
				gc.drawLine(0, 0, rect.width, 0);
			}
		});

		// Create the code editor pane.
		Composite jsEditorContainer = new Composite(editorPane, SWT.NONE);

		layoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);

		jsEditorContainer.setLayoutData(layoutData);
		jsEditorContainer.setLayout(new FillLayout());

		return jsEditorContainer;
	}

	/**
	 * Creates tool bar pane.
	 *
	 * @param parent the parent of controller
	 */
	private void createController(Composite parent) {
		Composite barPane = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(8, false);
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);

		barPane.setLayout(layout);
		barPane.setLayoutData(gdata);

		initScriptLabel(barPane);
		initComboBoxes(barPane);

		Composite toolPane = new Composite(barPane, SWT.NONE);
		layout = new GridLayout(3, false);
		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		toolPane.setLayout(layout);

		GridData layoutData = new GridData();
		layoutData.horizontalIndent = 6;
		toolPane.setLayoutData(layoutData);

		ToolBar toolBar = new ToolBar(toolPane, SWT.FLAT);

		// Creates Reset button
		butReset = new ToolItem(toolBar, SWT.NONE);
		butReset.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_SCRIPT_RESET));
		butReset.setToolTipText(Messages.getString("JSEditor.Button.Reset")); //$NON-NLS-1$
		butReset.addSelectionListener(new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				SourceViewer viewer = getViewer();

				if (viewer != null) {
					viewer.getTextWidget().setText(""); //$NON-NLS-1$
					refreshAll();
					setFocus();
				}
			}
		});

		// Creates Help button
		butHelp = new ToolItem(toolBar, SWT.CHECK);
		butHelp.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_SCRIPT_HELP));
		butHelp.setToolTipText(Messages.getString("JSEditor.Button.Help")); //$NON-NLS-1$
		butHelp.addSelectionListener(new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshAll();
			}
		});

		validateTool = new ToolBar(toolPane, SWT.FLAT);

		layoutData = new GridData();
		validateTool.setLayoutData(layoutData);
		new ToolItem(validateTool, SWT.SEPARATOR);

		// Creates Validate button
		butValidate = new ToolItem(validateTool, SWT.NONE);
		butValidate.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_EXPRESSION_VALIDATE));
		butValidate.setToolTipText(Messages.getString("JSEditor.Button.Validate")); //$NON-NLS-1$
		butValidate.addSelectionListener(new SelectionAdapter() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				doValidate();
				refreshAll();
			}
		});

		// Creates Validate icon, default empty.
		validateIcon = new Label(toolPane, SWT.NULL);

		Label column = new Label(barPane, SWT.SEPARATOR | SWT.VERTICAL);
		layoutData = new GridData();
		layoutData.heightHint = 20;
		column.setLayoutData(layoutData);

		ano = new Label(barPane, 0);
		layoutData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER);
		ano.setLayoutData(layoutData);
	}

	/**
	 * Creates description pane.
	 *
	 * @param parent the parent of controller
	 */
	private void createDescriptionPane(Composite parent) {
		descriptionPane = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);

		layout.verticalSpacing = 0;
		layout.marginHeight = 0;
		descriptionPane.setLayout(layout);
		descriptionPane.setLayoutData(layoutData);

		final Composite headerLine = new Composite(descriptionPane, SWT.NONE);

		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.heightHint = 3;
		headerLine.setLayoutData(layoutData);
		headerLine.addPaintListener(new PaintListener() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse
			 * .swt.events.PaintEvent)
			 */
			@Override
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				Rectangle rect = headerLine.getBounds();
				gc.setForeground(ReportColorConstants.DarkShadowLineColor);
				gc.drawLine(0, 0, rect.width, 0);
			}
		});

		// Creates the label of script description
		layoutData = new GridData(GridData.FILL_HORIZONTAL);
		descriptionText = new Text(descriptionPane, SWT.WRAP | SWT.READ_ONLY);
		descriptionText.setLayoutData(layoutData);
	}

	/**
	 * Hides validate button & icon.
	 */
	protected void hideValidateButtonIcon() {
		hideControl(validateTool);
		refreshAll();
	}

	/**
	 * Hides a control from its parent composite.
	 *
	 * @param control the control to hide
	 */
	private void hideControl(Control control) {
		if (control == null) {
			return;
		}

		Object layoutData = control.getLayoutData();

		if (layoutData == null) {
			layoutData = new GridData();
			control.setLayoutData(layoutData);
		}

		if (layoutData instanceof GridData) {
			GridData gridData = (GridData) layoutData;

			gridData.exclude = true;
			control.setLayoutData(gridData);
			control.setVisible(false);
		}
	}

	/**
	 * Shows a control from its parent composite.
	 *
	 * @param control the control to show
	 */
	private void showControl(Control control) {
		if (control == null) {
			return;
		}

		Object layoutData = control.getLayoutData();

		if (layoutData == null) {
			layoutData = new GridData();
			control.setLayoutData(layoutData);
		}

		if (layoutData instanceof GridData) {
			GridData gridData = (GridData) layoutData;

			gridData.exclude = false;
			control.setLayoutData(gridData);
			control.setVisible(true);
		}
	}

	/**
	 * Refreshes all components in main pane.
	 */
	private void refreshAll() {
		if (butHelp != null && !butHelp.getSelection()) {
			hideControl(descriptionPane);
		} else {
			showControl(descriptionPane);
		}

		if (validateTool == null || butValidate == null || !validateTool.isVisible() || !butValidate.getEnabled()) {
			hideControl(validateIcon);
		} else {
			showControl(validateIcon);
		}

		if (mainPane != null) {
			mainPane.layout(true, true);
		}
	}

	private void initScriptLabel(Composite parent) {
		Label lblScript = new Label(parent, SWT.NONE);
		lblScript.setText(Messages.getString("JSEditor.Label.Script")); //$NON-NLS-1$
		final FontData fd = lblScript.getFont().getFontData()[0];
		Font labelFont = FontManager.getFont(fd.getName(), fd.getHeight(), SWT.BOLD);
		lblScript.setFont(labelFont);
		GridData layoutData = new GridData(SWT.BEGINNING);
		lblScript.setLayoutData(layoutData);

	}

	private void initComboBoxes(Composite parent) {

		// Create the script combo box
		cmbExpList = new Combo(parent, SWT.READ_ONLY);
		GridData layoutData = new GridData(GridData.BEGINNING);
		layoutData.widthHint = 140;
		cmbExpList.setLayoutData(layoutData);
		cmbExpList.setVisibleItemCount(30);
		// Create the subfunction combo box
		cmbSubFunctions = new TextCombo(parent, SWT.NONE);// SWT.DROP_DOWN |
		// SWT.READ_ONLY );
		layoutData = new GridData(GridData.BEGINNING);
		layoutData.widthHint = 180;
		cmbSubFunctions.setLayoutData(layoutData);
	}

	/*
	 * SelectionChanged. - Selection listener implementation for changes in other
	 * views Selection of elements in other views, triggers this event. - The code
	 * editor view is updated to show the methods corresponding to the selected
	 * element.
	 */
	public void handleSelectionChanged(SelectionChangedEvent event) {
		ISelection selection = event.getSelection();
		handleSelectionChanged(selection);
	}

	public void handleSelectionChanged(ISelection selection) {

		if (editorUIEnabled) {
			saveModel();
		}

		if (selection != null) {
			Object[] sel = ((IStructuredSelection) selection).toArray();

			IElementPropertyDefn targetMethod = null;

			if (sel.length == 1) {
				editObject = sel[0];
				if (sel[0] instanceof ScriptElementNode) {
					editObject = ((ScriptElementNode) editObject).getParent();
				} else if (sel[0] instanceof ScriptObjectNode) {
					editObject = ((ScriptObjectNode) editObject).getParent();
				}

				if (editObject instanceof PropertyHandle) {
					targetMethod = ((PropertyHandle) editObject).getPropertyDefn();

					// check if this is a method property
					if (targetMethod.getMethodInfo() != null) {
						editObject = ((PropertyHandle) editObject).getElementHandle();
					}
				}
			}

			if (editObject instanceof DesignElementHandle) {
				// set the combo viewer input to the the selected element.
				palettePage.getSupport().setCurrentEditObject(editObject);

				setComboViewerInput(editObject);

				// clear the latest selected item.
				cmbItemLastSelected = null;

				setEditorText(""); //$NON-NLS-1$

				// enable/disable editor based on the items in the
				// expression list.
				if (cmbExpList.getItemCount() > 0) {
					enableEditor();

					if (targetMethod != null) {
						selectItemInComboExpList(new StructuredSelection(targetMethod));
					} else {
						// Selects the last saveed or first item in the
						// expression list.
						ISelection oldSelection = (ISelection) selectionMap.get(getModel());

						if (oldSelection instanceof StructuredSelection
								&& ((StructuredSelection) oldSelection).size() > 1) {
							Object[] sels = ((StructuredSelection) oldSelection).toArray();
							selectItemInComboExpList(new StructuredSelection(sels[0]));
							cmbSubFunctionsViewer.setSelection(new StructuredSelection(sels[1]));
						} else {
							selectItemInComboExpList(oldSelection);
						}
					}
				} else {
					disableEditor();
				}

				/*
				 * if ( editObject instanceof ExtendedItemHandle ) { setEditorText( (
				 * (ExtendedItemHandle) editObject ).getExternalScript( ) );
				 * context.setVariable( "this",
				 * "org.eclipse.birt.report.model.api.ExtendedItemHandle" ); //$NON-NLS-1$
				 * //$NON-NLS-2$ }
				 */
				checkDirty();
				palettePage.getSupport().updateParametersTree();
			} else {
				disableEditor();
				cmbExpList.removeAll();
				cmbSubFunctions.setItems(null);
				cmbItemLastSelected = null;
				palettePage.getSupport().setCurrentEditObject(null);
			}
			if (sel.length > 0) {
				updateAnnotationLabel(sel[0]);
			}
		}
	}

	private void checkDirty() {
		// ( (AbstractMultiPageLayoutEditor) editingDomainEditor ).checkDirty(
		// );
	}

	private void selectItemInComboExpList(ISelection selection) {
		ISelection sel = selection;
		if (sel.isEmpty() && cmbExpList.getItemCount() > 0) {
			IPropertyDefn propDefn = (IPropertyDefn) cmbExprListViewer.getElementAt(0);
			if (propDefn != null) {
				sel = new StructuredSelection(propDefn);
			}
		}

		cmbExprListViewer.setSelection(getNewSelection(sel));
	}

	private ISelection getNewSelection(ISelection selection) {

		// DesignElementHandle model = (DesignElementHandle) getModel( );
		if (!(getModel() instanceof DesignElementHandle) || !(selection instanceof IStructuredSelection)) {
			return selection;
		}
		List<Object> temp = new ArrayList<>();
		List list = ((IStructuredSelection) selection).toList();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof IElementPropertyDefn) {
				String name = ((IElementPropertyDefn) list.get(i)).getName();
				Object obj = findData(name);
				if (obj != null) {
					temp.add(obj);
				} else {
					temp.add(list.get(i));
				}
			} else {
				temp.add(list.get(i));
			}
		}

		return new StructuredSelection(temp);
	}

	private Object findData(String name) {
		if (cmbExprListViewer.getCombo().getItemCount() <= 0) {
			return null;
		}
		// cmbExprListViewer.get
		int count = cmbExprListViewer.getCombo().getItemCount();
		for (int i = 0; i < count; i++) {
			Object obj = cmbExprListViewer.getElementAt(i);
			if (obj instanceof IElementPropertyDefn && ((IElementPropertyDefn) obj).getName().equals(name)) {
				return obj;
			}
		}
		return null;
	}

	/**
	 * setEditorText - sets the editor content.
	 *
	 * @param text
	 */
	protected void setEditorText(String text) {
		if (scriptEditor == null) {
			return;
		}

		try {
			// Disable text listener during setting script, so that the dirty
			// flag isn't changed by program.
			setTextListenerEnable(false);
			scriptEditor.setScript(text);
			if (scriptValidator != null) {
				scriptValidator.init();
				setValidateIcon(null, null);
			}
		} finally {
			setTextListenerEnable(true);
		}
	}

	/**
	 * getEditorText() - gets the editor content.
	 *
	 */
	String getEditorText() {
		return scriptEditor.getScript();
	}

	/**
	 * saveEditorContentsDE - saves the current editor contents to ROM using DE API
	 *
	 * @param desHdl
	 * @return true if updated else false.
	 */
	private boolean saveEditorContentsDE(DesignElementHandle desHdl, boolean isSaveScript) {
		if (desHdl != null && getEditorText() != null) {
			try {
				if (cmbItemLastSelected != null) {
					String name = cmbItemLastSelected.getName();

					desHdl.setStringProperty(name, getEditorText());
					if (!isSaveScript) {
						setEditorText(desHdl.getStringProperty(name));
					}
				}
				selectionMap.put(getModel(), cmbExprListViewer.getSelection());
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
				return false;
			}
		}
		return true;
	}

	/**
	 * Saves input code to model
	 */
	private void saveModel() {
		if (isCodeModified() && editObject instanceof DesignElementHandle) {
			saveEditorContentsDE((DesignElementHandle) editObject, isSaveScript);
		}

		setIsModified(false);

		((IFormPage) getParentEditor()).getEditor().editorDirtyStateChanged();

		firePropertyChange(PROP_DIRTY);

		SourceViewer viewer = getViewer();
		IUndoManager undoManager = viewer == null ? null : viewer.getUndoManager();

		if (undoManager != null) {
			undoManager.endCompoundChange();
		}
		cleanPoint = getUndoLevel();
	}

	/**
	 * Returns current undo level.
	 *
	 * @return current undo level.
	 */
	private int getUndoLevel() {
		SourceViewer viewer = getViewer();
		IUndoableOperation[] history = viewer == null ? null
				: OperationHistoryFactory.getOperationHistory()
						.getUndoHistory(new ObjectUndoContext(viewer.getDocument()));

		return history == null ? -1 : history.length;
	}

	/**
	 * @param b
	 */
	public void setIsModified(boolean b) {
		isModified = b;
	}

	private boolean isCodeModified() {
		return isModified;
	}

	protected void markDirty() {
		if (!isModified) {
			setIsModified(true);
			((IFormPage) getParentEditor()).getEditor().editorDirtyStateChanged();

			firePropertyChange(PROP_DIRTY);
		}

		if (cleanPoint > getUndoLevel() && !undoing) {
			cleanPoint = -1;
		}
	}

	/**
	 * Enables the editor UI components
	 */
	private void enableEditor() {
		if (!editorUIEnabled) {
			getViewer().getTextWidget().setEnabled(true);
			cmbExpList.setEnabled(true);
			butReset.setEnabled(true);
			butValidate.setEnabled(true);
			editorUIEnabled = true;
		}
		setEditorText(""); //$NON-NLS-1$
	}

	/**
	 * Disables the editor UI components
	 */
	private void disableEditor() {
		if (editorUIEnabled) {
			getViewer().getTextWidget().setEnabled(false);
			cmbExpList.setEnabled(false);
			cmbSubFunctions.setEnabled(false);
			butReset.setEnabled(false);
			butValidate.setEnabled(false);
			editorUIEnabled = false;
		}
		setEditorText(NO_EXPRESSION);
	}

	/**
	 * Gets source viewer in the editor
	 *
	 * @return source viewer
	 */
	public SourceViewer getViewer() {
		return (SourceViewer) scriptEditor.getViewer();
	}

	@Override
	public boolean isInterested(IMediatorRequest request) {
		return request instanceof ReportRequest;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.core.util.mediator.IColleague#performRequest
	 * ( org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest )
	 */
	@Override
	public void performRequest(IMediatorRequest request) {
		ReportRequest rqt = (ReportRequest) request;

		if (ReportRequest.SELECTION.equals(request.getType())) {
			handleSelectionChange(rqt.getSelectionModelList());
		} else if (ReportRequest.CREATE_ELEMENT.equals(rqt.getType())
				&& rqt.getSelectionModelList().get(0) instanceof ScriptDataSourceHandle) {
			handleSelectionChange(rqt.getSelectionModelList());
		}
		refreshAll();
	}

	private void setComboViewerInput(Object model) {
		cmbExprListViewer.setInput(model);

		Object oldSelection = selectionMap.get(model);

		if (oldSelection == null) {
			selectItemInComboExpList(new StructuredSelection());
		} else {
			StructuredSelection selection = (StructuredSelection) oldSelection;
			selectItemInComboExpList(new StructuredSelection(selection.getFirstElement()));
		}

		cmbSubFunctionsViewer.setInput(model);
		int itemCount = cmbSubFunctions.getItemCount();
		if (itemCount > 0) {
			if (oldSelection instanceof StructuredSelection && ((StructuredSelection) oldSelection).size() > 1) {
				StructuredSelection selection = (StructuredSelection) oldSelection;
				cmbSubFunctionsViewer.setSelection(new StructuredSelection(selection.toArray()[1]));
			} else {
				cmbSubFunctionsViewer.setSelection(new StructuredSelection(cmbSubFunctionsViewer.getElementAt(0)));
			}
		}
		cmbSubFunctions.setEnabled(itemCount > 0);
	}

	private void setComboViewerInput(String message) {
		cmbExprListViewer.setInput(message);
	}

	/**
	 * Reset the selection forcely.
	 *
	 * @param list
	 */
	public void handleSelectionChange(List list) {
		if (scriptEditor instanceof AbstractTextEditor) {
			SelectionChangedEvent event = new SelectionChangedEvent(
					((AbstractTextEditor) scriptEditor).getSelectionProvider(), new StructuredSelection(list));

			handleSelectionChanged(event);
		}
	}

	/**
	 * Returns the current script editor.
	 *
	 * @return the current script editor.
	 */
	protected IScriptEditor getScriptEditor() {
		return scriptEditor;
	}

	/**
	 * Validates the contents of this editor.
	 */
	public void doValidate() {
		Image image = null;
		String message = null;

		if (scriptValidator == null) {
			return;
		}

		try {
			scriptValidator.validate(true, true);
			image = ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_SCRIPT_NOERROR);
			message = Messages.getString("JSEditor.Validate.NoError"); //$NON-NLS-1$
		} catch (ParseException e) {
			image = ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_SCRIPT_ERROR);
			message = e.getLocalizedMessage();
		} finally {
			setValidateIcon(image, message);
			setFocus();
		}
	}

	/**
	 * Sets the validate icon with the specified image and tool tip text.
	 *
	 *
	 * @param image the icon image
	 * @param tip   the tool tip text
	 */
	private void setValidateIcon(Image image, String tip) {
		if (validateIcon != null) {
			validateIcon.setImage(image);
			validateIcon.setToolTipText(tip);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		scriptEditor.doSaveAs();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 * org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		scriptEditor.init(site, input);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		scriptEditor.setFocus();
	}

	/**
	 *
	 */
	public void resetText() {
		if (editObject instanceof DesignElementHandle && cmbItemLastSelected != null) {
			DesignElementHandle desHdl = (DesignElementHandle) editObject;
			String name = cmbItemLastSelected.getName();

			setEditorText(desHdl.getStringProperty(name));
		}
	}

	/**
	 * Updates the description label with the specified method name.
	 *
	 * @param methodName the method to update.
	 */
	private void updateMethodDescription(String methodName) {
		Object obj = findData(methodName);
		String description = null;

		if (obj instanceof IElementPropertyDefn) {
			IMethodInfo methodInfo = ((IElementPropertyDefn) obj).getMethodInfo();

			if (methodInfo != null) {
				description = methodInfo.getToolTip();
			}
		}
		setDescriptionText(description);
	}

	/**
	 * Sets the description with the specified text.
	 *
	 * @param text the text to set.
	 */
	private void setDescriptionText(String text) {
		Font fontToUse;
		String description;

		if (text != null && text.length() > 0) {
			fontToUse = JFaceResources.getFontRegistry().get(JFaceResources.DEFAULT_FONT);
			description = text;
		} else {
			fontToUse = JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT);
			description = Messages.getString("JSEditor.Text.NoDescription"); //$NON-NLS-1$ ;
		}
		descriptionText.setFont(fontToUse);
		descriptionText.setText(description);
	}
}

/**
 * class JSExpListProvider - Is the content and label provider for the
 * expression list
 *
 */

class JSExpListProvider implements IStructuredContentProvider, ILabelProvider {

	private static final String NO_TEXT = Messages.getString("JSEditor.Text.NoText"); //$NON-NLS-1$ ;

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof DesignElementHandle) {
			DesignElementHandle eleHandle = (DesignElementHandle) inputElement;
			List methods = eleHandle.getMethods();
			List<Object> clientScripts = new ArrayList<>();
			List<Object> elements = new ArrayList<>();
			for (int i = 0; i < methods.size(); i++) {
				IPropertyDefn mtdDef = (IPropertyDefn) methods.get(i);

				// XXX for report design, we search for the "client" methods and
				// group as a list. Then we use the List object to represent this
				// virtual clientScripts context to simulate the sub function UI.
				if (eleHandle instanceof ReportDesignHandle && JSEditor.CLIENT_CONTEXT.equals(mtdDef.getContext())) {
					clientScripts.add(mtdDef);
				} else {
					elements.add(mtdDef);
				}
			}

			if (!clientScripts.isEmpty()) {
				elements.add(clientScripts);
			}

			return elements.toArray(new Object[elements.size()]);
		}
		return new Object[] {};
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		viewer.refresh();

	}

	@Override
	public String getText(Object element) {
		if (element instanceof IPropertyDefn) {
			IPropertyDefn eleDef = (IPropertyDefn) element;

			return eleDef.getName();
		} else if (element instanceof List) {
			// If the selection is the List object, it should be the virtual
			// clientScripts context.
			return JSEditor.CLIENT_SCRIPTS;
		}
		return NO_TEXT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.
	 * jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang
	 * .Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {

	}
}

class JSSubFunctionListProvider implements IStructuredContentProvider, ILabelProvider, ISelectionChangedListener {

	protected static Logger logger = Logger.getLogger(JSSubFunctionListProvider.class.getName());

	// private static final String NO_TEXT = Messages.getString(
	// "JSEditor.Text.NoText" ); //$NON-NLS-1$;
	private JSEditor editor;

	public JSSubFunctionListProvider(JSEditor editor) {
		this.editor = editor;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<Object> elements = new ArrayList<>();

		if (inputElement instanceof ExtendedItemHandle) {
			int selectedIndex = editor.cmbExpList.getSelectionIndex();
			if (selectedIndex >= 0) {
				String scriptName = editor.cmbExpList.getItem(editor.cmbExpList.getSelectionIndex());

				ExtendedItemHandle extHandle = (ExtendedItemHandle) inputElement;
				List methods = extHandle.getMethods(scriptName);

				if (methods != null && methods.size() > 0) {
					elements.add(0, Messages.getString("JSEditor.cmb.NewEventFunction")); //$NON-NLS-1$
					elements.addAll(methods);
				}
			}
		}
		// XXX for report design, we check whether the selection represents the
		// virutal clicentScripts context, which is a List object, then refresh
		// the sub function UI.
		else if (inputElement instanceof ReportDesignHandle) {
			int selectedIndex = editor.cmbExpList.getSelectionIndex();
			if (selectedIndex >= 0) {
				Object mainSelection = ((StructuredSelection) editor.cmbExprListViewer.getSelection())
						.getFirstElement();

				if (mainSelection instanceof List) {
					elements.addAll((List) mainSelection);
				}
			}
		}

		return elements.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			viewer.refresh();
		}

	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof IMethodInfo) {
			IMethodInfo eleDef = (IMethodInfo) element;
			return JSEditor.METHOD_DISPLAY_INDENT + eleDef.getName();
		} else if (element instanceof String) {
			return (String) element;
		} else if (element instanceof IPropertyDefn) {
			return ((IPropertyDefn) element).getName();
		}
		return ""; //$NON-NLS-1$
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		boolean isContextChange = false;

		if (event.getSource() instanceof ComboViewer) {
			isContextChange = JSEditor.VIEWER_CATEGORY_CONTEXT
					.equals(((ComboViewer) event.getSource()).getData(JSEditor.VIEWER_CATEGORY_KEY));
		}

		ISelection selection = event.getSelection();
		if (selection != null) {
			Object[] sel = ((IStructuredSelection) selection).toArray();
			if (sel.length == 1) {
				if (isContextChange) {
					editor.cmbSubFunctionsViewer.refresh();
					int itemCount = editor.cmbSubFunctions.getItemCount();
					if (itemCount > 0) {
						// select first element always
						editor.cmbSubFunctionsViewer
								.setSelection(new StructuredSelection(editor.cmbSubFunctionsViewer.getElementAt(0)));
					}
					editor.cmbSubFunctions.setEnabled(itemCount > 0);
				} else if (sel[0] instanceof IMethodInfo) {
					IMethodInfo methodInfo = (IMethodInfo) sel[0];

					Position pos = findMethod(methodInfo);

					if (pos != null) {
						// locate to existing method
						IScriptEditor viewer = editor.getScriptEditor();

						if (viewer instanceof AbstractTextEditor) {
							AbstractTextEditor editor = (AbstractTextEditor) viewer;
							editor.selectAndReveal(pos.getOffset(), pos.length);
						}
					} else {
						// create new method
						String signature = createSignature(methodInfo);

						try {
							IScriptEditor viewer = editor.getScriptEditor();

							if (viewer instanceof AbstractTextEditor) {
								AbstractTextEditor editor = (AbstractTextEditor) viewer;

								IDocument doc = (editor.getDocumentProvider()).getDocument(viewer.getEditorInput());
								int length = doc.getLength();

								doc.replace(length, 0, signature);
								editor.selectAndReveal(length + 1, signature.length());
							}
						} catch (BadLocationException e) {
							logger.log(Level.SEVERE, e.getMessage(), e);
						}
					}

					editor.cmbSubFunctionsViewer
							.setSelection(new StructuredSelection(editor.cmbSubFunctionsViewer.getElementAt(0)));
				}
			}
		}
	}

	private Position findMethod(IMethodInfo methodInfo) {
		ScriptParser parser = new ScriptParser(editor.getEditorText());

		Collection<IScriptMethodInfo> coll = parser.getAllMethodInfo();

		for (Iterator<IScriptMethodInfo> itr = coll.iterator(); itr.hasNext();) {
			IScriptMethodInfo mtd = itr.next();

			if (methodInfo.getName().equals(mtd.getName())) {
				return mtd.getPosition();
			}
		}

		return null;
	}

	// create the signature to insert in the document:
	// function functionName(param1, param2){}
	private String createSignature(IMethodInfo info) {
		StringBuilder signature = new StringBuilder();
		String javaDoc = info.getJavaDoc();
		if (javaDoc != null && javaDoc.length() > 0) {
			signature.append("\n"); //$NON-NLS-1$
			signature.append(info.getJavaDoc());
		}

		if (info instanceof ITemplateMethodInfo) {
			String code = ((ITemplateMethodInfo) info).getCodeTemplate();

			if (code != null) {
				signature.append("\n").append(code).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$

				return signature.toString();
			}
		}

		signature.append("\nfunction "); //$NON-NLS-1$
		signature.append(info.getName());
		signature.append("( "); //$NON-NLS-1$
		Iterator<IArgumentInfoList> iter = info.argumentListIterator();
		if (iter.hasNext()) {
			// only one iteraration, we ignore overload cases for now
			// need to do multiple iterations if overloaded methods should be
			// supported

			IArgumentInfoList argumentList = iter.next();
			for (Iterator<IArgumentInfo> argumentIter = argumentList.argumentsIterator(); argumentIter.hasNext();) {
				IArgumentInfo argument = argumentIter.next();

				String argName = argument.getName();

				if (argName == null || argName.length() == 0) {
					String type = argument.getType();
					// convert type to parameter name
					argName = JSEditor.convertToParameterName(type);
				}

				signature.append(argName);

				if (argumentIter.hasNext()) {
					signature.append(", ");//$NON-NLS-1$
				}
			}
		}
		signature.append(" )\n{\n}\n"); //$NON-NLS-1$
		return signature.toString();
	}

}
