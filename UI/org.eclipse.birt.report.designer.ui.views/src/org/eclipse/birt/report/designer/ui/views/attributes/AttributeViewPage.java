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

package org.eclipse.birt.report.designer.ui.views.attributes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.mediator.IMediator;
import org.eclipse.birt.report.designer.core.mediator.IMediatorColleague;
import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.actions.GlobalActionFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.Page;

/**
 * Attribute view shows the attributes of the selected control. If no control is
 * selected, it will show no attributes and a sentence describing there is
 * nothing to show for the selected object.
 * </p>
 * Multi-selection of control of the same type will normally show the same UI as
 * if only one control was selected. Some of the values may be gray or blank if
 * the selected controls have different attributes. If the controls have
 * different type, nothing will be shown in the attributes view.
 * </P>
 */
public class AttributeViewPage extends Page
		implements IAttributeViewPage, INullSelectionListener, IMediatorColleague, IModelEventProcessor {

	/**
	 * WorkbenchPart ID list that attribute view interests in selection changing
	 * occupied in these WorkbenchParts.
	 */
	protected List<String> PART_IDS = Arrays
			.asList(new String[] { "org.eclipse.birt.report.designer.ui.editors.ReportEditor", //$NON-NLS-1$
					"org.eclipse.birt.report.designer.ui.editors.LibraryReportEditor", //$NON-NLS-1$
					"org.eclipse.birt.report.designer.ui.editors.TemplateEditor", //$NON-NLS-1$
					IPageLayout.ID_OUTLINE, });

	/**
	 * Keeps current selection When AttributeView is not on the top of window.
	 */
	private ISelection selection;

	/**
	 * Attribute view UI builder
	 */
	protected AttributesBuilder builder;

	// add restore library properties action
	private RestoreLibraryPropertiesAction restoreLibraryPropertiesAction;

	private ModuleHandle model;

	public class RestoreLibraryPropertiesAction extends Action {

		AttributeViewPage view;

		RestoreLibraryPropertiesAction(AttributeViewPage view) {
			this.view = view;
			setImageDescriptor(
					ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_ENABLE_RESTORE_PROPERTIES));
			setEnabled(false);
			setToolTipText(
					Messages.getString("AttributeView.toolbar.tooltip.RestoreLibraryPropertiesAction.RestoreMsg")); //$NON-NLS-1$
		}

		@Override
		public void run() {
			if (view != null) {

				MessageDialog prefDialog = new MessageDialog(UIUtil.getDefaultShell(),
						Messages.getString("AttributeView.dialg.Message.Warning"), //$NON-NLS-1$
						null, Messages.getString("AttributeView.dialg.Message.PromptMsg"), //$NON-NLS-1$
						MessageDialog.INFORMATION, new String[] { Messages.getString("AttributeView.dialg.Message.Yes"), //$NON-NLS-1$
								Messages.getString("AttributeView.dialg.Message.No")//$NON-NLS-1$
						}, 0);
				int ret = prefDialog.open();

				if (!(ret == 2)) {

					if (ret == Window.OK) {
						resetLocalProperties(ret);
						pageGenerator = builder.getPageGenerator(getModelList(selection));
						pageGenerator.createControl(container, getModelList(selection));
						setEnabled(false);
					}
				}

			}
		}
	}

	public AttributeViewPage(ModuleHandle model) {
		this.model = model;
	}

	/**
	 * Clear all the local properties
	 *
	 * @param ret
	 */
	public void resetLocalProperties(int ret) {
		try {
			Object data = SessionHandleAdapter.getInstance().getMediator(model).getState().getData();

			StructuredSelection selection;

			if (data instanceof List) {
				selection = new StructuredSelection((List) data);
			} else if (data != null) {
				selection = new StructuredSelection(data);
			} else {
				selection = new StructuredSelection();
			}

			DEUtil.getGroupElementHandle(getModelList(selection)).clearLocalProperties();
			DEUtil.getGroupElementHandle(getModelList(selection)).clearLocalPropertiesIncludeSubElement();
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 * Creates the SWT controls for this workbench part.
	 * <p>
	 * Clients should not call this method (the workbench calls this method when it
	 * needs to, which may be never).
	 * </p>
	 * <p>
	 * For implementors this is a multi-step process:
	 * <ol>
	 * <li>Create one or more controls within the parent.</li>
	 * <li>Set the parent layout as needed.</li>
	 * <li>Register any global actions with the <code>IActionService</code>.</li>
	 * <li>Register any popup menus with the <code>IActionService</code>.</li>
	 * <li>Register a selection provider with the <code>ISelectionService</code>
	 * (optional).</li>
	 * </ol>
	 * </p>
	 *
	 * @param parent the parent control
	 */
	private Composite container;

	@Override
	public void createControl(Composite parent) {
		addActions();
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);
		builder = new AttributesBuilder();

		IWorkbenchPage page = getSite().getPage();
		selection = page.getSelection();
		page.addSelectionListener(this);

		if (selection == null && UIUtil.getActiveReportEditor() != null) {
			selection = (ISelection) UIUtil.getActiveReportEditor().getAdapter(ISelection.class);
		}

		SessionHandleAdapter.getInstance().getMediator(model).addColleague(this);

		handleSelectionChanged(selection);
	}

	private void addActions() {
		if (restoreLibraryPropertiesAction == null) {
			restoreLibraryPropertiesAction = new RestoreLibraryPropertiesAction(this);
		}
		clearOldRestoreLibraryPropertiesAction();

		getSite().getActionBars().getToolBarManager().add(restoreLibraryPropertiesAction);

	}

	private void clearOldRestoreLibraryPropertiesAction() {
		IContributionItem[] items = getSite().getActionBars().getToolBarManager().getItems();
		for (IContributionItem item : items) {

			if (item instanceof ActionContributionItem) {
				ActionContributionItem aItem = (ActionContributionItem) item;
				if (aItem.getAction() instanceof RestoreLibraryPropertiesAction) {
					getSite().getActionBars().getToolBarManager().remove(item);
				}
			}
		}
	}

	/**
	 * Asks this part to take focus within the workbench.
	 * <p>
	 * Clients should not call this method (the workbench calls this method at
	 * appropriate times). To have the workbench activate a part, use
	 * <code>IWorkbenchPage.activate(IWorkbenchPart) instead</code>.
	 * </p>
	 */
	@Override
	public void setFocus() {
		Display.getCurrent().asyncExec(new Runnable() {

			@Override
			public void run() {
				handleSelectionChanged(selection);
			}
		});
	}

	private void setPartName() {
		String typeInfo = builder.getTypeInfo();
		IViewPart view = UIUtil.getView(AttributeView.ID);
		if (view != null && typeInfo != null) {
			((AttributeView) view).setPartName(typeInfo);
		}
	}

	private boolean hasLocalProperties(ISelection selection) {
		return hasLocalProperties(getModelList(selection));
	}

	private boolean hasLocalProperties(List modelList) {
		GroupElementHandle groupHandle = DEUtil.getGroupElementHandle(modelList);

		return groupHandle.hasLocalPropertiesForExtendedElements() || groupHandle.hasLocalPropertiesIncludeSubElement();
	}

	public void resetRestorePropertiesAction(List modelList) {
		restoreLibraryPropertiesAction.setEnabled(hasLocalProperties(modelList));
	}

	/**
	 * Parse out the DE models for all kinds of input source.
	 *
	 * @param selection the current selection.
	 * @return
	 */
	protected List getModelList(ISelection selection) {
		List list = new ArrayList();
		if ((selection == null) || !(selection instanceof StructuredSelection)) {
			return list;
		}

		StructuredSelection structured = (StructuredSelection) selection;
		if (structured.getFirstElement() instanceof ReportElementEditPart) {
			boolean bool = false;
			for (Iterator it = structured.iterator(); it.hasNext();) {
				ReportElementEditPart object = (ReportElementEditPart) it.next();
				if (object instanceof DummyEditpart) {
					list.add(object.getModel());
					bool = true;
				}
				if (!bool) {
					list.add(object.getModel());
				}
			}
		} else {
			list = structured.toList();
		}
		return list;
	}

	/**
	 * Notifies this listener that the selection has changed.
	 * <p>
	 * This method is called when the selection changes from one to a
	 * <code>non-null</code> value, but not when the selection changes to
	 * <code>null</code>. If there is a requirement to be notified in the latter
	 * scenario, implement <code>INullSelectionListener</code>. The event will be
	 * posted through this method.
	 * </p>
	 *
	 * @param part      the workbench part containing the selection
	 * @param selection the current selection. This may be <code>null</code> if
	 *                  <code>INullSelectionListener</code> is implemented.
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// if ( part != null && !PART_IDS.contains( part.getSite( ).getId( ) )
		// && !ID.equals( part.getSite( ).getId( ) )
		// && !PaletteView.ID.equals( part.getSite( ).getId( ) ) )
		if (SessionHandleAdapter.getInstance().getModule() == model) {
			if (part != null && !ReportPlugin.getDefault().containIgnoreViewID(part.getSite().getId())) {
				if (selection == null && requesList.isEmpty()) {
					IMediator mediator = SessionHandleAdapter.getInstance().getMediator(model, false);
					if (mediator != null && mediator.getState() != null
							&& mediator.getState().getData() instanceof List) {
						// When close and reopen the attribute view, display the
						// old selection.
						ReportRequest request = new ReportRequest(this);
						request.setSelectionObject((List) mediator.getState().getData());
						request.setType(ReportRequest.SELECTION);
						SessionHandleAdapter.getInstance().getMediator(model).notifyRequest(request);
						return;
					}

				}
				handleSelectionChanged(new StructuredSelection());
			}
		}
	}

	/**
	 * Disposes of this workbench part.
	 * <p>
	 * This is the last method called on the <code>IWorkbenchPart</code>. At this
	 * point the part controls (if they were ever created) have been disposed as
	 * part of an SWT composite. There is no guarantee that createPartControl() has
	 * been called, so the part controls may never have been created.
	 * </p>
	 * <p>
	 * Within this method a part may release any resources, fonts, images,
	 * etc.&nbsp; held by this part. It is also very important to deregister all
	 * listeners from the workbench.
	 * </p>
	 * <p>
	 * Clients should not call this method (the workbench calls this method at
	 * appropriate times).
	 * </p>
	 */
	@Override
	public void dispose() {
		if (pageGenerator instanceof AbstractPageGenerator) {
			((AbstractPageGenerator) pageGenerator).dispose();
		}
		deRegisterEventManager();
		IWorkbenchPage page = getSite().getPage();
		page.removeSelectionListener(this);
		// page.removePartListener( partListener );

		// remove the mediator listener
		IMediator mediator = SessionHandleAdapter.getInstance().getMediator(model, false);
		if (mediator != null) {
			mediator.removeColleague(this);
		}

		super.dispose();
	}

	/**
	 * Handles all global actions
	 */
	private void handleGlobalAction() {
		for (int i = 0; i < GlobalActionFactory.GLOBAL_STACK_ACTIONS.length; i++) {
			String id = GlobalActionFactory.GLOBAL_STACK_ACTIONS[i];
			getSite().getActionBars().setGlobalActionHandler(id,
					GlobalActionFactory.createStackAction(id, SessionHandleAdapter.getInstance().getCommandStack()));
		}

		getSite().getActionBars().setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), new SelectAllAction());

		getSite().getActionBars().updateActionBars();
	}

	private static class SelectAllAction extends Action {

		private SelectAllAction() {
			setId(ActionFactory.SELECT_ALL.getId());
		}

		@Override
		public void runWithEvent(Event event) {
			try {
				execute();
			} catch (ExecutionException e) {
				ExceptionUtil.handle(e);
			}
		}

		/**
		 * The parameters to pass to the method this handler invokes. This handler
		 * always passes no parameters.
		 */
		protected static final Class[] NO_PARAMETERS = {};

		private static final String methodName = "selectAll";//$NON-NLS-1$

		public final Object execute() throws ExecutionException {
			final Method methodToExecute = getMethodToExecute();
			if (methodToExecute != null) {
				try {
					final Control focusControl = Display.getCurrent().getFocusControl();

					// if it's an embedded swing component, fail gracefully for
					// now.
					if ((focusControl instanceof Composite)
							&& ((((Composite) focusControl).getStyle() & SWT.EMBEDDED) != 0)) {
						return null;
					}

					final int numParams = methodToExecute.getParameterTypes().length;

					if (numParams == 0) {
						// This is a no-argument selectAll method.
						methodToExecute.invoke(focusControl, (Object[]) null);
						focusControl.notifyListeners(SWT.Selection, null);

					} else if (numParams == 1) {
						// This is a single-point selection method.
						final Method textLimitAccessor = focusControl.getClass().getMethod("getTextLimit", //$NON-NLS-1$
								NO_PARAMETERS);
						final Integer textLimit = (Integer) textLimitAccessor.invoke(focusControl, (Object[]) null);
						final Object[] parameters = { new Point(0, textLimit.intValue()) };
						methodToExecute.invoke(focusControl, parameters);
						focusControl.notifyListeners(SWT.Selection, null);

					} else {
						/*
						 * This means that getMethodToExecute() has been changed, while this method
						 * hasn't.
						 */
						throw new ExecutionException("Too many parameters on select all", new Exception()); //$NON-NLS-1$

					}

				} catch (InvocationTargetException e) {
					throw new ExecutionException("An exception occurred while executing " //$NON-NLS-1$
							+ getMethodToExecute(), e.getTargetException());

				} catch (IllegalAccessException | NoSuchMethodException e) {
					// I can't get the text limit. Do nothing.

				}
			}

			return null;
		}

		/**
		 * Looks up the select all method on the given focus control.
		 *
		 * @return The method on the focus control; <code>null</code> if none.
		 */
		protected Method getMethodToExecute() {
			final Control focusControl = Display.getCurrent().getFocusControl();
			Method method = null;

			if (focusControl != null) {
				final Class clazz = focusControl.getClass();
				try {
					method = clazz.getMethod(methodName, NO_PARAMETERS);
				} catch (NoSuchMethodException e) {
					// Fall through...
				}
			}

			if ((method == null) && (focusControl instanceof Composite)
					&& ((((Composite) focusControl).getStyle() & SWT.EMBEDDED) != 0)) {
				/*
				 * We couldn't find the appropriate method on the current focus control. It is
				 * possible that the current focus control is an embedded SWT composite, which
				 * could be containing some Swing components. If this is the case, then we
				 * should try to pass through to the underlying Swing component hierarchy.
				 * Insha'allah, this will work.
				 */
				try {
					final Class focusManagerClass = Class.forName("javax.swing.FocusManager"); //$NON-NLS-1$
					final Method focusManagerGetCurrentManagerMethod = focusManagerClass.getMethod("getCurrentManager", //$NON-NLS-1$
							(Class[]) null);
					final Object focusManager = focusManagerGetCurrentManagerMethod.invoke(focusManagerClass,
							(Object[]) null);
					final Method focusManagerGetFocusOwner = focusManagerClass.getMethod("getFocusOwner", //$NON-NLS-1$
							(Class[]) null);
					final Object focusComponent = focusManagerGetFocusOwner.invoke(focusManager, (Object[]) null);
					final Class clazz = focusComponent.getClass();

					try {
						method = clazz.getMethod(methodName, NO_PARAMETERS);
					} catch (NoSuchMethodException e) {
						// Do nothing.
					}
				} catch (final ClassNotFoundException e) {
					// There is no Swing support, so do nothing.

				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
					// The API has changed, which seems amazingly unlikely.
					throw new Error("Something is seriously wrong here"); //$NON-NLS-1$
				}
			}

			return method;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.core.util.mediator.IColleague#performRequest
	 * ( org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest )
	 */
	List requesList = Collections.EMPTY_LIST;

	private IPageGenerator pageGenerator;

	@Override
	public boolean isInterested(IMediatorRequest request) {
		return request instanceof ReportRequest;
	}

	@Override
	public void performRequest(IMediatorRequest request) {
		if (ReportRequest.SELECTION.equals(request.getType())) {
			if (!requesList.equals(request.getData())) {
				deRegisterEventManager();
				requesList = (List) request.getData();
				handleSelectionChanged(new StructuredSelection(requesList));
				registerEventManager();
			}
			if (request.getExtras() != null && request.getExtras().containsKey(AbstractPageGenerator.ACTIVE_PAGE)) {
				if (pageGenerator instanceof TabPageGenerator
						&& request.getExtras().get(TabPageGenerator.ACTIVE_PAGE) instanceof String) {
					((TabPageGenerator) pageGenerator)
							.selectTabItem((String) request.getExtras().get(TabPageGenerator.ACTIVE_PAGE));
				}
			}
			setPartName();
		}
	}

	/**
	 * Removes model change listener.
	 */
	protected void deRegisterEventManager() {
		if (UIUtil.getModelEventManager() != null) {
			UIUtil.getModelEventManager().removeModelEventProcessor(this);
		}
	}

	/**
	 * Registers model change listener to DE elements.
	 */
	protected void registerEventManager() {
		if (UIUtil.getModelEventManager() != null) {
			UIUtil.getModelEventManager().addModelEventProcessor(this);
		}
	}

	/**
	 * Notifies this listener that the selection has changed.
	 * <p>
	 * This method is called when the selection changes from one to a
	 * <code>non-null</code> value, but not when the selection changes to
	 * <code>null</code>. If there is a requirement to be notified in the latter
	 * scenario, implement <code>INullSelectionListener</code>. The event will be
	 * posted through this method.
	 * </p>
	 *
	 * @param part      the workbench part containing the selection
	 * @param selection the current selection. This may be <code>null</code> if
	 *                  <code>INullSelectionListener</code> is implemented.
	 */

	public void handleSelectionChanged(ISelection selection) {
		List modelList = getModelList(selection);
		if (modelList == null || modelList.size() == 0) {
			return;
		}
		pageGenerator = builder.getPageGenerator(modelList);
		if (container != null && !container.isDisposed()) {
			pageGenerator.createControl(container, modelList);
			pageGenerator.refresh();
		}
		if (SessionHandleAdapter.getInstance().getReportDesignHandle() != null) {
			restoreLibraryPropertiesAction.setEnabled(hasLocalProperties(selection));
			handleGlobalAction();
			setPartName();
		}
		this.selection = selection;
	}

	static class MessagePageGenerator extends TabPageGenerator {

		@Override
		public void createTabItems(List input) {
			super.createTabItems(input);
			Composite pane = new Composite(tabFolder, SWT.NONE);
			pane.setLayout(new FillLayout());

			new MessageAttributePage(pane, SWT.NONE, input);

			CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);

			if (input.get(0) instanceof ModelClassWrapper) {
				tabItem.setText(((ModelClassWrapper) input.get(0)).getTypeMessage());
			}
			tabItem.setControl(pane);
		}
	}

	static class MessageAttributePage extends Composite {

		private List model;

		public MessageAttributePage(Composite parent, int style, List input) {
			super(parent, style);
			model = input;
			buildUI();

		}

		private void buildUI() {

			FillLayout layout = new FillLayout();
			setLayout(layout);

			Label label = new Label(this, SWT.LEFT | SWT.TOP | SWT.WRAP);
			String extendsString = "";//$NON-NLS-1$
			if (model.get(0) instanceof ModelClassWrapper) {

				extendsString = ((ModelClassWrapper) model.get(0)).getExtendsString();
			}

			label.setText(extendsString);
		}
	}

	static class ModelClassWrapper {

		private GroupElementHandle groupElementHandle;

		public ModelClassWrapper(List modelList) {
			groupElementHandle = DEUtil.getMultiSelectionHandle(modelList);
		}

		public String getTypeMessage() {
			if (groupElementHandle.getElements().get(0) instanceof DesignElementHandle) {
				return DEUtil.getDisplayLabel(groupElementHandle.getElements().get(0));
			}
			return "";//$NON-NLS-1$
		}

		public String getExtendsString() {
			DesignElementHandle handle = (DesignElementHandle) groupElementHandle.getElements().get(0);
			String name = DEUtil.getDisplayLabel(handle);
			String extendsFrom = handle.getExtends().getDisplayLabel();
			String libName = handle.getExtends().getRoot().getDisplayLabel();
			return Messages.getFormattedString("AttributeView.view.message.Emptypage", new Object[] { name, //$NON-NLS-1$
					extendsFrom, libName, name });
		}

		public Object getElement() {
			if (groupElementHandle != null) {
				return groupElementHandle.getElements().get(0);
			}
			return new Object();
		}
	}

	@Override
	public Control getControl() {
		return container;
	}

	@Override
	public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {

	}

	@Override
	public void clear() {

	}

	@Override
	public void postElementEvent() {
		restoreLibraryPropertiesAction.setEnabled(hasLocalProperties(selection));
		if (pageGenerator != null && pageGenerator.getControl() != null && !pageGenerator.getControl().isDisposed()) {
			pageGenerator.refresh();
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}
}
