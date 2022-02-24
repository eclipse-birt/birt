/*************************************************************************************
 * Copyright (c) 2007, 2014 Actuate Corporation and others.
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

package org.eclipse.birt.report.designer.ui.editor.script;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor;
import org.eclipse.birt.report.designer.internal.ui.script.JSEditorInput;
import org.eclipse.birt.report.designer.internal.ui.script.JSSyntaxContext;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.schematic.action.TextSaveAction;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceWrapper;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IUndoManager;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.IFoldingCommandIds;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.texteditor.ResourceAction;
import org.eclipse.ui.texteditor.TextOperationAction;

/**
 * A script editor comprising functionality not present in the leaner
 * <code>ScriptEditor</code>, but used in many heavy weight (and especially
 * source editing) editors, such as line numbers, change ruler, overview ruler,
 * print margins, current line highlighting, etc.
 */
public class DecoratedScriptEditor extends AbstractDecoratedTextEditor implements IScriptEditor {

	/**
	 * The Javascript syntax context, provides methods to access available Type
	 * meta-data.
	 */
	private final JSSyntaxContext context = new JSSyntaxContext();

	/** The editor input for javascript. */
	private IEditorInput input = createScriptInput(null);

	/** The action registry */
	private ActionRegistry actionRegistry = null;

	/** The parent editor. */
	private final IEditorPart parent;

	private ScriptSourceViewerConfiguration sourceViewerConfiguration;

	/**
	 * Constructs a decorated script editor with the specified parent.
	 * 
	 * @param parent the parent editor.
	 */
	public DecoratedScriptEditor(IEditorPart parent) {
		this(parent, null);
	}

	/**
	 * Constructs a decorated script editor with the specified parent and the
	 * specified script.
	 * 
	 * @param parent the parent editor.
	 * @param script the script to edit
	 */
	public DecoratedScriptEditor(IEditorPart parent, String script) {
		super();
		this.parent = parent;
		this.sourceViewerConfiguration = new ScriptSourceViewerConfiguration(context);
		setSourceViewerConfiguration(this.sourceViewerConfiguration);
		setDocumentProvider(new ScriptDocumentProvider(parent));
		setScript(script);

		IPreferences preferences = PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault());
		if (preferences instanceof PreferenceWrapper) {
			IPreferenceStore store = ((PreferenceWrapper) preferences).getPrefsStore();
			if (store != null) {
				IPreferenceStore baseEditorPrefs = EditorsUI.getPreferenceStore();
				setPreferenceStore(new ChainedPreferenceStore(new IPreferenceStore[] { store, baseEditorPrefs }));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.StatusTextEditor#createPartControl(org.eclipse
	 * .swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) {
		if (input != null) {
			setInput(input);
		}
		super.createPartControl(parent);

		ISourceViewer viewer = getViewer();

		if (viewer instanceof ProjectionViewer) {
			// Turn projection mode on.
			((ProjectionViewer) viewer).doOperation(ProjectionViewer.TOGGLE);
		}
		// bidi_hcg: Force LTR orientation of the StyledText widget
		getSourceViewer().getTextWidget().setOrientation(SWT.LEFT_TO_RIGHT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.texteditor.AbstractTextEditor#editorContextMenuAboutToShow
	 * (org.eclipse.jface.action.IMenuManager)
	 */
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		menu.add(new Separator(ITextEditorActionConstants.GROUP_UNDO));
		menu.add(new Separator(ITextEditorActionConstants.GROUP_COPY));
		menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));

		if (isEditable()) {
			addAction(menu, ITextEditorActionConstants.GROUP_UNDO, ITextEditorActionConstants.UNDO);

			addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.CUT);

			addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.COPY);

			addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.PASTE);
		} else {
			addAction(menu, ITextEditorActionConstants.GROUP_COPY, ITextEditorActionConstants.COPY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.texteditor.AbstractTextEditor#
	 * isEditorInputIncludedInContextMenu()
	 */
	protected boolean isEditorInputIncludedInContextMenu() {
		return false;
	}

	/**
	 * Creates an editor input with the specified script.
	 * 
	 * @param script the script to edit.
	 * @return an editor input with the specified script.
	 */
	protected IEditorInput createScriptInput(String script) {
		return new JSEditorInput(script);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#createActions()
	 */
	protected void createActions() {
		super.createActions();

		IAction contentAssistAction = new TextOperationAction(Messages.getReportResourceBundle(),
				"ContentAssistProposal_", this, ISourceViewer.CONTENTASSIST_PROPOSALS, true);//$NON-NLS-1$

		IAction expandAll = new TextOperationAction(Messages.getReportResourceBundle(), "JSEditor.Folding.ExpandAll.", //$NON-NLS-1$
				this, ProjectionViewer.EXPAND_ALL, true);

		IAction collapseAll = new TextOperationAction(Messages.getReportResourceBundle(),
				"JSEditor.Folding.CollapseAll.", this, ProjectionViewer.COLLAPSE_ALL, true); //$NON-NLS-1$

		IAction collapseComments = new ResourceAction(Messages.getReportResourceBundle(),
				"JSEditor.Folding.CollapseComments.") { //$NON-NLS-1$

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.Action#run()
			 */
			public void run() {
				collapseStyle(ScriptProjectionAnnotation.SCRIPT_COMMENT);
			}
		};

		IAction collapseMethods = new ResourceAction(Messages.getReportResourceBundle(),
				"JSEditor.Folding.CollapseMethods.") { //$NON-NLS-1$

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.action.Action#run()
			 */
			public void run() {
				collapseStyle(ScriptProjectionAnnotation.SCRIPT_METHOD);
			}
		};

		contentAssistAction.setActionDefinitionId(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS);
		expandAll.setActionDefinitionId(IFoldingCommandIds.FOLDING_EXPAND_ALL);
		collapseAll.setActionDefinitionId(IFoldingCommandIds.FOLDING_COLLAPSE_ALL);

		setAction("ContentAssistProposal", contentAssistAction);//$NON-NLS-1$
		setAction("FoldingExpandAll", expandAll); //$NON-NLS-1$
		setAction("FoldingCollapseAll", collapseAll); //$NON-NLS-1$
		setAction("FoldingCollapseComments", collapseComments); //$NON-NLS-1$
		setAction("FoldingCollapseMethods", collapseMethods); //$NON-NLS-1$
		setAction(ITextEditorActionConstants.SAVE, new TextSaveAction(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#setAction(java.lang.String,
	 * org.eclipse.jface.action.IAction)
	 */
	public void setAction(String actionID, IAction action) {
		super.setAction(actionID, action);
		if (action != null && action.getId() == null) {
			action.setId(actionID);
		}
		if (action != null)
			getActionRegistry().registerAction(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor
	 * #getActionRegistry()
	 */
	public ActionRegistry getActionRegistry() {
		if (actionRegistry == null) {
			actionRegistry = new ActionRegistry();
		}
		return actionRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor
	 * #getViewer()
	 */
	public ISourceViewer getViewer() {
		return getSourceViewer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor
	 * #getScript()
	 */
	public String getScript() {
		IDocumentProvider provider = getDocumentProvider();
		String script = ""; //$NON-NLS-1$

		if (provider != null) {
			IDocument document = provider.getDocument(getEditorInput());

			if (document != null) {
				script = document.get();
			}
		}
		return script;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor
	 * #setScript(java.lang.String)
	 */
	public void setScript(String script) {
		try {
			IDocumentProvider provider = getDocumentProvider();

			if (provider != null) {
				IDocument document = provider.getDocument(getEditorInput());

				if (document != null) {
					document.set(script == null ? "" : script); //$NON-NLS-1$
					return;
				}
			}
			input = createScriptInput(script);
		} finally {
			ISourceViewer viewer = getSourceViewer();

			if (viewer instanceof SourceViewer) {
				IUndoManager undoManager = ((SourceViewer) viewer).getUndoManager();

				if (undoManager != null) {
					undoManager.reset();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptEditor
	 * #getContext()
	 */
	public JSSyntaxContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editor.script.IDebugScriptEditor#
	 * saveDocument()
	 */
	public void saveDocument() {
		ScriptDocumentProvider provider = (ScriptDocumentProvider) getDocumentProvider();
		try {
			((AbstractMarkerAnnotationModel) provider.getAnnotationModel(getEditorInput()))
					.commit(provider.getDocument(getEditorInput()));
		} catch (CoreException e) {
			// do nothing
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractDecoratedTextEditor#createSourceViewer
	 * (org.eclipse.swt.widgets.Composite,
	 * org.eclipse.jface.text.source.IVerticalRuler, int)
	 */
	protected ISourceViewer createSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		fAnnotationAccess = getAnnotationAccess();
		fOverviewRuler = createOverviewRuler(getSharedColors());

		ProjectionViewer viewer = new ProjectionViewer(parent, ruler, getOverviewRuler(), isOverviewRulerVisible(),
				styles);

		ProjectionSupport fProjectionSupport = new ProjectionSupport(viewer, getAnnotationAccess(), getSharedColors());
		fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.error"); //$NON-NLS-1$
		fProjectionSupport.addSummarizableAnnotationType("org.eclipse.ui.workbench.texteditor.warning"); //$NON-NLS-1$
		fProjectionSupport.install();

		// Ensures source viewer decoration support has been created and
		// configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.texteditor.AbstractDecoratedTextEditor#
	 * rulerContextMenuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	protected void rulerContextMenuAboutToShow(IMenuManager menu) {
		super.rulerContextMenuAboutToShow(menu);

		IMenuManager foldingMenu = new MenuManager(Messages.getString("JSEditor.Folding.Group")); //$NON-NLS-1$

		menu.appendToGroup(ITextEditorActionConstants.GROUP_RULERS, foldingMenu);

		IAction expandAll = getAction("FoldingExpandAll"); //$NON-NLS-1$
		IAction collapseAll = getAction("FoldingCollapseAll"); //$NON-NLS-1$
		IAction collapseComments = getAction("FoldingCollapseComments"); //$NON-NLS-1$
		IAction collapseMethods = getAction("FoldingCollapseMethods"); //$NON-NLS-1$

		// Enables all actions.
		enableAction(expandAll);
		enableAction(collapseAll);
		enableAction(collapseComments);
		enableAction(collapseMethods);

		// Adds all actions into folding group.
		foldingMenu.add(expandAll);
		foldingMenu.add(collapseAll);
		foldingMenu.add(collapseComments);
		foldingMenu.add(collapseMethods);
	}

	/**
	 * Set the specified action enabled.
	 * 
	 * @param action the specified action to set enable.
	 */
	private void enableAction(IAction action) {
		if (action instanceof IUpdate) {
			((IUpdate) action).update();
		} else {
			ISourceViewer viewer = getViewer();

			action.setEnabled(
					viewer instanceof ProjectionViewer ? ((ProjectionViewer) viewer).isProjectionMode() : true);
		}
	}

	/**
	 * Collapses all item with the specified style.
	 * 
	 * @param style the style to collapse
	 */
	private void collapseStyle(int style) {
		ISourceViewer viewer = getViewer();

		if (!(viewer instanceof ProjectionViewer)) {
			return;
		}

		ProjectionAnnotationModel model = ((ProjectionViewer) viewer).getProjectionAnnotationModel();

		if (model == null) {
			return;
		}

		List modified = new ArrayList();
		Iterator iter = model.getAnnotationIterator();

		while (iter.hasNext()) {
			Object annotation = iter.next();

			if (annotation instanceof ScriptProjectionAnnotation) {
				ScriptProjectionAnnotation scriptAnnotation = (ScriptProjectionAnnotation) annotation;

				if (!scriptAnnotation.isCollapsed() && scriptAnnotation.isStyle(style)) {
					scriptAnnotation.markCollapsed();
					modified.add(scriptAnnotation);
				}
			}
		}
		model.modifyAnnotations(null, null, (Annotation[]) modified.toArray(new Annotation[modified.size()]));
	}

	/**
	 * Returns the parent editor.
	 * 
	 * @return the parent editor.
	 */
	protected IEditorPart getParent() {
		return parent;
	}

	@Override
	protected void handlePreferenceStoreChanged(PropertyChangeEvent event) {
		if (getSourceViewer() != null) {
			sourceViewerConfiguration.resetScannerColor();
			// reset text widget text
			getSourceViewer().getTextWidget().setText(getSourceViewer().getTextWidget().getText());
			sourceViewerConfiguration.getPresentationReconciler(getSourceViewer()).install(getSourceViewer());
		}
		super.handlePreferenceStoreChanged(event);
	}

}
