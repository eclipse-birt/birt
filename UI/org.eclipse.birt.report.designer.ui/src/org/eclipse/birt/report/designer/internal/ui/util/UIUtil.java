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

package org.eclipse.birt.report.designer.internal.ui.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Bidi;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil;
import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.IGroupStructureProvider;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.runtime.ErrorStatus;
import org.eclipse.birt.report.designer.core.runtime.GUIException;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DeleteWarningDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ImportLibraryDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButtonProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.IExpressionButtonProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.IRelatedFileChangeResolve;
import org.eclipse.birt.report.designer.internal.ui.editors.IReportEditor;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventManager;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MultipleEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.PaletteEntryExtension;
import org.eclipse.birt.report.designer.internal.ui.util.bidi.BidiUIUtils;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.designer.ui.editors.AbstractMultiPageEditor;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementDetailHandle;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.service.environment.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ILayoutExtension;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.osgi.framework.Bundle;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Utility class for UI related routines.
 */

public class UIUtil {

	protected static final Logger logger = Logger.getLogger(UIUtil.class.getName());

	private static final String MSG_DIALOG_TITLE = Messages.getString("ImportLibraryAction.Title.ImportSuccessfully"); //$NON-NLS-1$
	private static final String MSG_DIALOG_MSG = Messages.getString("ImportLibraryAction.Message.ImportSuccessfully"); //$NON-NLS-1$

	private static final String AC_GROUP_COLLAPSE_LEVEL_PROPERTY = "__ac_group_collapse_level"; //$NON-NLS-1$

	private static String[] EDITOR_IDS = { "org.eclipse.birt.report.designer.ui.editors.ReportEditor", //$NON-NLS-1$
			"org.eclipse.birt.report.designer.ui.editors.LibraryEditor", //$NON-NLS-1$
			"org.eclipse.birt.report.designer.ui.editors.TemplateEditor" //$NON-NLS-1$
	};

	/**
	 * Regex pattern for neutral chars in Bidi Algorithm.
	 */
	static Pattern punctuation = Pattern.compile("\\p{Punct}||\\p{Blank}||\\p{Space}"); //$NON-NLS-1$

	private static boolean embeddedBrowserTested = false;
	private static boolean embeddedBrowserAvailable = false;

	/**
	 * Returns the length in pixels of given string in a control.
	 *
	 * @return the length in pixels
	 */
	public static int getStringWidth(String string, Control control) {
		int width;
		GC gc = new GC(control);
		width = gc.textExtent(string).x;
		gc.dispose();
		return width;
	}

	/**
	 * Returns the maximum length in pixels of given strings in a control. The
	 * string value in the array should not be null.
	 *
	 * @return the length in pixels
	 */
	public static int getMaxStringWidth(String[] strArray, Control control) {
		int maxWidth = 0;
		GC gc = new GC(control);

		for (int i = 0; i < strArray.length; i++) {
			int width = gc.textExtent(strArray[i]).x;
			maxWidth = maxWidth >= width ? maxWidth : width;
		}

		gc.dispose();
		return maxWidth;
	}

	/**
	 * Returns if current active editor is reportEditor.
	 *
	 * @return true if current active editor is reportEditor, or false else.
	 */
	public static boolean isReportEditorActivated() {
		return getActiveReportEditor() != null;
	}

	/**
	 * Returns the current active report editor. The same as getActiveEditor( true
	 * ).
	 *
	 * @return the current active report editor, or null if no report editor is
	 *         active.
	 */
	public static FormEditor getActiveReportEditor() {
		return getActiveReportEditor(true);
	}

	/**
	 * Returns the current active report editor in current active page or current
	 * active workbench.
	 *
	 * @param activePageOnly If this is true, only search the current active page,
	 *                       or will search all pages in current workbench, returns
	 *                       the first active report or null if not found.
	 * @return the current active report editor, or null if no report editor is
	 *         active.
	 */
	public static FormEditor getActiveReportEditor(boolean activePageOnly) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (window != null) {
			if (activePageOnly) {
				IWorkbenchPage pg = window.getActivePage();

				if (pg != null) {
					IEditorPart editor = pg.getActiveEditor();

					if (editor != null) {
						if (editor instanceof IReportEditor) {
							IEditorPart part = ((IReportEditor) editor).getEditorPart();
							if (part instanceof FormEditor) {
								return (FormEditor) part;
							}
						} else if (editor instanceof FormEditor) {
							return (FormEditor) editor;
						}
					}
				}
			} else {
				IWorkbenchPage[] pgs = window.getPages();

				for (int i = 0; i < pgs.length; i++) {
					IWorkbenchPage pg = pgs[i];

					if (pg != null) {
						IEditorPart editor = pg.getActiveEditor();

						if (editor instanceof IReportEditor) {
							IEditorPart part = ((IReportEditor) editor).getEditorPart();
							if (part instanceof FormEditor) {
								return (FormEditor) part;
							}
						} else if (editor instanceof FormEditor) {
							return (FormEditor) editor;
						}
					}
				}
			}
		}

		return null;

	}

	/**
	 * Returns the current active editor part in current active page or current
	 * active workbench.
	 *
	 * @param activePageOnly If this is true, only search the current active page,
	 *                       or will search all pages in current workbench, returns
	 *                       the first active editor part or null if not found.
	 * @return the current active editor part, or null if no editor part is active.
	 */
	public static IEditorPart getActiveEditor(boolean activePageOnly) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		if (window != null) {
			if (activePageOnly) {
				IWorkbenchPage pg = window.getActivePage();

				if (pg != null) {
					return pg.getActiveEditor();
				}
			} else {
				IWorkbenchPage[] pgs = window.getPages();

				for (int i = 0; i < pgs.length; i++) {
					IWorkbenchPage pg = pgs[i];

					if (pg != null) {
						IEditorPart editor = pg.getActiveEditor();

						if (editor != null) {
							return editor;
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * Returns current project according to current selection. 1. If current
	 * selection is editPart, get editor input and return associated project. 2. If
	 * current selection is not ediPart, use first selected element, query from its
	 * IAdaptable interface to get associated project. 3. If the above is not
	 * working, get the first accessible project in the current workspace and return
	 * it. 4. If none is accessible, returns null.
	 *
	 * @return the default project according to current selection.
	 */
	public static IProject getDefaultProject() {
		IWorkbenchWindow benchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPart part = benchWindow.getPartService().getActivePart();

		Object selection = null;
		if (part instanceof IEditorPart) {
			selection = ((IEditorPart) part).getEditorInput();
		} else {
			ISelection sel = benchWindow.getSelectionService().getSelection();
			if ((sel != null) && (sel instanceof IStructuredSelection)) {
				selection = ((IStructuredSelection) sel).getFirstElement();
			}
		}

		if (selection instanceof IAdaptable) {
			IResource resource = (IResource) ((IAdaptable) selection).getAdapter(IResource.class);

			if (resource != null && resource.getProject() != null && resource.getProject().isAccessible()) {
				return resource.getProject();
			}
		}

		IProject[] pjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		for (int i = 0; i < pjs.length; i++) {
			if (pjs[i].isAccessible()) {
				return pjs[i];
			}
		}

		return null;
	}

	public static IProject getCurrentProject() {
		IWorkbench iworkbench = PlatformUI.getWorkbench();
		if (iworkbench == null) {
			return null;
		}

		IWorkbenchWindow iworkbenchwindow = iworkbench.getActiveWorkbenchWindow();
		if (iworkbenchwindow == null) {
			return null;
		}

		IWorkbenchPage iworkbenchpage = iworkbenchwindow.getActivePage();
		if (iworkbenchpage != null) {
			IEditorPart ieditorpart = iworkbenchpage.getActiveEditor();
			if (ieditorpart != null) {
				IEditorInput input = ieditorpart.getEditorInput();
				if (input != null) {
					IProject project = (IProject) ElementAdapterManager.getAdapter(input, IProject.class);
					if (project != null) {
						return project;
					}
				}
			}
		}

		ISelection selection = iworkbenchwindow.getSelectionService().getSelection();
		if (selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection) selection).getFirstElement();
			if (element instanceof IResource) {
				return ((IResource) element).getProject();
			}
		}
		return null;
	}

	/**
	 * Returns the default shell used by dialogs
	 *
	 * @return the active shell of the current display
	 */
	public static Shell getDefaultShell() {
		Shell shell = null;
		try {
			shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			if (shell == null) {
				shell = Display.getCurrent().getActiveShell();
			}
			if (shell == null) {
				shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			}
		} catch (Exception e) {
			// do nothing
		}
		if (shell == null) {
			return new Shell();
		}
		return shell;
	}

	/**
	 * Creates a new group under the given parent
	 *
	 * @param parent The parent of the new group, it should be a table or a list and
	 *               should not be null.
	 * @return true if the group created successfully, false if the creation is
	 *         cancelled or some error occurred.
	 */
	public static boolean createGroup(DesignElementHandle parent) {
		assert parent != null;

		try {
			return addGroup(parent, -1);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			return false;
		}
	}

	/**
	 * Creates a new group in the position under the given parent
	 *
	 * @param parent   The parent of the new group, it should be a table or a list
	 *                 and should not be null.
	 * @param position insert position
	 * @return true if the group created successfully, false if the creation is
	 *         cancelled or some error occurred.
	 */
	public static boolean createGroup(DesignElementHandle parent, int position) {
		assert parent != null;

		try {
			return addGroup(parent, position);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			return false;
		}
	}

	private static boolean addGroup(DesignElementHandle parent, int position) throws SemanticException {
		GroupHandle groupHandle = null;
		SlotHandle slotHandle = null;
		// ElementFactory factory = parent.getElementFactory( );
		DesignElementFactory factory = DesignElementFactory.getInstance(parent.getModuleHandle());
		if (parent instanceof TableHandle) {
			groupHandle = factory.newTableGroup();
			slotHandle = ((TableHandle) parent).getGroups();
			int columnCount = ((TableHandle) parent).getColumnCount();
			groupHandle.getHeader().add(factory.newTableRow(columnCount));
			groupHandle.getFooter().add(factory.newTableRow(columnCount));
		} else if (parent instanceof ListHandle) {
			groupHandle = factory.newListGroup();
			slotHandle = ((ListHandle) parent).getGroups();
		}

		if (groupHandle != null && slotHandle != null) {
			String collapseLevel = parent.getStringProperty(AC_GROUP_COLLAPSE_LEVEL_PROPERTY);
			if (collapseLevel != null && collapseLevel.trim().length() > 0 && position >= 0) {
				String[] levels = collapseLevel.split(","); //$NON-NLS-1$
				List<Integer> levelList = new ArrayList<>();
				for (int i = 0; i < levels.length; i++) {
					try {
						int level = Integer.parseInt(levels[i]);
						if (level >= position) {
							level++;
						}
						levelList.add(level);
					} catch (NumberFormatException e) {
					}
				}

				StringBuilder buffer = new StringBuilder();
				for (int i = 0; i < levelList.size(); i++) {
					buffer.append(levelList.get(i));
					if (i < levelList.size() - 1) {
						buffer.append(","); //$NON-NLS-1$
					}
				}

				String value = buffer.toString().trim().length() > 0 ? buffer.toString().trim() : null;
				parent.setStringProperty(AC_GROUP_COLLAPSE_LEVEL_PROPERTY, value);
			}

			slotHandle.add(groupHandle, position);
			// if ( !DEUtil.getDataSetList( parent ).isEmpty( ) )
			{// If data set can be found or a blank group will be inserted.
				GroupDialog dialog = new GroupDialog(getDefaultShell(), GroupDialog.GROUP_DLG_TITLE_NEW);
				// dialog.setDataSetList( DEUtil.getDataSetList( parent ) );
				dialog.setInput(groupHandle);
				if (dialog.open() == Window.CANCEL) {// Cancel the action
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Gets the first selected edit part in layout editor. Whenever the user has
	 * deselected all edit parts, the contents edit part should be returned.
	 *
	 * @return the first selected EditPart or root edit part
	 */
	public static EditPart getCurrentEditPart() {
		EditPartViewer viewer = getLayoutEditPartViewer();
		if (viewer == null) {
			return null;
		}
		IStructuredSelection targets = (IStructuredSelection) viewer.getSelection();
		if (targets.isEmpty()) {
			return null;
		}
		return (EditPart) targets.getFirstElement();
	}

	/**
	 * Gets EditPartViewer in layout editor.
	 *
	 * @return the EditPartViewer in layout editor, or null if not found.
	 */
	public static EditPartViewer getLayoutEditPartViewer() {
		IEditorPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();

		AbstractMultiPageEditor reportEditor = null;
		if (part instanceof AbstractMultiPageEditor) {
			reportEditor = (AbstractMultiPageEditor) part;
		} else if (part instanceof IReportEditor) {
			IEditorPart activeEditor = ((IReportEditor) part).getEditorPart();
			if (activeEditor instanceof AbstractMultiPageEditor) {
				reportEditor = (AbstractMultiPageEditor) activeEditor;
			}
		}

		if (reportEditor == null
				|| !(reportEditor.getActivePageInstance() instanceof GraphicalEditorWithFlyoutPalette)) {
			return null;
		}
		return ((GraphicalEditorWithFlyoutPalette) reportEditor.getActivePageInstance()).getGraphicalViewer();
	}

	/**
	 * Creates a new grid layout without margins by default
	 *
	 * @return the layout created
	 */
	public static GridLayout createGridLayoutWithoutMargin() {
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = 0;
		return layout;
	}

	public static GridLayout createGridLayoutWithMargin(int margin) {
		GridLayout layout = new GridLayout();
		layout.marginHeight = layout.marginWidth = margin;
		return layout;
	}

	/**
	 * Creates a new grid layout without margins with given the number of columns,
	 * and whether or not the columns should be forced to have the same width
	 *
	 * @param numsColumn            the number of columns in the grid
	 * @param makeColumnsEqualWidth whether or not the columns will have equal width
	 *
	 * @return the layout created
	 */
	public static GridLayout createGridLayoutWithoutMargin(int numsColumn, boolean makeColumnsEqualWidth) {
		GridLayout layout = new GridLayout(numsColumn, makeColumnsEqualWidth);
		layout.marginHeight = layout.marginWidth = 0;
		return layout;
	}

	/**
	 * Convert the give string to GUI style, which cannot be null
	 *
	 * @param string the string to convert
	 * @return the string, or an empty string for null
	 */
	public static String convertToGUIString(String string) {
		if (string == null) {
			string = ""; //$NON-NLS-1$
		}
		return string;
	}

	/**
	 * Convert the give string to Model style
	 *
	 * @param string the string to convert
	 * @param trim   specify if the string needs to be trimmed
	 * @return the string, or null for an empty string
	 */
	public static String convertToModelString(String string, boolean trim) {
		if (string == null) {
			return null;
		}
		if (trim) {
			string = string.trim();
		}
		if (string.length() == 0) {
			string = null;
		}
		return string;
	}

	/**
	 * Returns the width hint for the given control.
	 *
	 * @param wHint the width hint
	 * @param c     the control
	 *
	 * @return the width hint
	 */
	public static int getWidthHint(int wHint, Control c) {
		boolean wrap = isWrapControl(c);
		return wrap ? wHint : SWT.DEFAULT;
	}

	/**
	 * Returns the height hint for the given control.
	 *
	 * @param hHint the width hint
	 * @param c     the control
	 *
	 * @return the height hint
	 */
	public static int getHeightHint(int hHint, Control c) {
		if (c instanceof Composite) {
			Layout layout = ((Composite) c).getLayout();
			if (layout instanceof ColumnLayout) {
				return hHint;
			}
		}
		return SWT.DEFAULT;
	}

	/**
	 * Updates the page scroll increment for given composite.
	 *
	 * @param scomp
	 */
	public static void updatePageIncrement(ScrolledComposite scomp) {
		ScrollBar vbar = scomp.getVerticalBar();
		if (vbar != null) {
			Rectangle clientArea = scomp.getClientArea();
			int increment = clientArea.height - 5;
			vbar.setPageIncrement(increment);
		}
	}

	private static boolean isWrapControl(Control c) {
		if (c instanceof Composite) {
			return ((Composite) c).getLayout() instanceof ILayoutExtension;
		}
		return (c.getStyle() & SWT.WRAP) != 0;
	}

	/**
	 * Returns table editpart.
	 *
	 * @param editParts a list of editpart
	 * @return the current selected table editpart, null if no table editpart, more
	 *         than one table, or other non-table editpart. Cell editpart is also a
	 *         type of table editpart.
	 */
	public static TableEditPart getTableEditPart(List<Object> editParts) {
		if (editParts == null || editParts.isEmpty()) {
			return null;
		}
		int size = editParts.size();
		TableEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = editParts.get(i);

			TableEditPart currentEditPart = null;
			if (obj instanceof TableEditPart) {
				currentEditPart = (TableEditPart) obj;
			} else if (obj instanceof TableCellEditPart) {
				currentEditPart = (TableEditPart) ((TableCellEditPart) obj).getParent();
			} else if (obj instanceof DummyEditpart) {
				continue;
			}
			if (part == null) {
				part = currentEditPart;
			}
			// Check if select only one table
			if (currentEditPart == null || currentEditPart != null && part != currentEditPart) {
				return null;
			}
		}
		// Only table permitted
		if (part instanceof GridEditPart) {
			return null;
		}
		return part;
	}

	/**
	 * @param editParts
	 * @return
	 */
	public static ReportElementEditPart getTableMultipleEditPart(List<Object> editParts) {
		if (editParts == null || editParts.isEmpty()) {
			return null;
		}
		int size = editParts.size();
		ReportElementEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = editParts.get(i);

			ReportElementEditPart currentEditPart = null;
			if (obj instanceof MultipleEditPart && ((MultipleEditPart) obj).getModel() instanceof TableHandle) {
				currentEditPart = (ReportElementEditPart) obj;
			} else if (obj instanceof DummyEditpart) {
				continue;
			}
			if (part == null) {
				part = currentEditPart;
			}
			// Check if select only one table
			if (currentEditPart == null || currentEditPart != null && part != currentEditPart) {
				return null;
			}
		}
		// Only table permitted
		if (part instanceof GridEditPart) {
			return null;
		}
		return part;
	}

	/**
	 * Returns list editpart.
	 *
	 * @param editParts a list of editpart
	 * @return the current selected list editpart, null if no list editpart, more
	 *         than one list, or other list editpart. List band editpart is also a
	 *         type of list editpart.
	 */
	public static ListEditPart getListEditPart(List<Object> editParts) {
		if (editParts == null || editParts.isEmpty()) {
			return null;
		}
		int size = editParts.size();
		ListEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = editParts.get(i);

			ListEditPart currentEditPart = null;
			if (obj instanceof ListEditPart) {
				currentEditPart = (ListEditPart) obj;
			} else if (obj instanceof ListBandEditPart) {
				currentEditPart = (ListEditPart) ((ListBandEditPart) obj).getParent();
			}
			if (part == null) {
				part = currentEditPart;
			}
			// Check if select only one list
			if (currentEditPart == null || currentEditPart != null && part != currentEditPart) {
				return null;
			}
		}
		return part;
	}

	/**
	 * Tests if the specified element is on the given tree viewer
	 *
	 * @param treeViewer the tree viewer
	 * @param element    the element
	 *
	 * @return true if the element is on the tree, or false else.
	 */
	public static boolean containElement(AbstractTreeViewer treeViewer, Object element) {
		ITreeContentProvider provider = (ITreeContentProvider) treeViewer.getContentProvider();
		Object input = treeViewer.getInput();
		if (input instanceof Object[]) {
			Object[] inputs = (Object[]) input;
			for (int i = 0; i < inputs.length; i++) {
				if (containElement(inputs[i], provider, element)) {
					return true;
				}
			}
			return false;
		}
		return containElement(input, provider, element);
	}

	private static boolean containElement(Object parent, ITreeContentProvider provider, Object element) {
		if (parent == null) {
			return false;
		}
		if (parent == element || parent.equals(element)) {
			return true;
		}

		if (provider == null) {
			return false;
		}
		Object[] children = provider.getChildren(parent);
		for (int i = 0; i < children.length; i++) {
			if (containElement(children[i], provider, element)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the plug-in provider
	 *
	 * @param pluginId the identify of the plugin
	 *
	 * @return the plug-in provider, or null if the plug-in is not found
	 */
	public static String getPluginProvider(String pluginId) {
		return getBundleValue(pluginId, org.osgi.framework.Constants.BUNDLE_VENDOR);
	}

	/**
	 * Returns the plug-in name
	 *
	 * @param pluginId the identify of the plugin
	 *
	 * @return the plug-in name, or null if the plug-in is not found
	 */
	public static String getPluginName(String pluginId) {
		return getBundleValue(pluginId, org.osgi.framework.Constants.BUNDLE_NAME);
	}

	/**
	 * Returns the plug-in version
	 *
	 * @param pluginId the identify of the plugin
	 *
	 * @return the plug-in version, or null if the plug-in is not found
	 */
	public static String getPluginVersion(String pluginId) {
		return getBundleValue(pluginId, org.osgi.framework.Constants.BUNDLE_VERSION);
	}

	private static String getBundleValue(String pluginId, String key) {
		assert pluginId != null;

		Bundle bundle = Platform.getBundle(pluginId);
		if (bundle != null) {
			return bundle.getHeaders().get(key);
		}
		return null;
	}

	public static void resetViewSelection(final EditPartViewer viewer, final boolean notifyToMedia) {
		final List<Object> list = new ArrayList<>(((StructuredSelection) viewer.getSelection()).toList());

		boolean hasColumnOrRow = false;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) instanceof TableEditPart.DummyRowEditPart
					|| list.get(i) instanceof TableEditPart.DummyColumnEditPart) {
				hasColumnOrRow = true;
				break;
			}
		}

		if (hasColumnOrRow) {
			int selectionType = 0;// 0 select row 1select colum
			TableEditPart part = null;
			int[] selectContents = {};
			for (int i = 0; i < list.size(); i++) {
				Object obj = list.get(i);
				int number = -1;
				if (obj instanceof TableEditPart.DummyRowEditPart) {
					selectionType = 0;// select row
					number = ((TableEditPart.DummyRowEditPart) obj).getRowNumber();
				} else if (obj instanceof TableEditPart.DummyColumnEditPart) {
					selectionType = 1;// select column
					number = ((TableEditPart.DummyColumnEditPart) obj).getColumnNumber();
				} else if (obj instanceof TableCellEditPart) {
					part = (TableEditPart) ((TableCellEditPart) obj).getParent();
				}
				if (number != -1) {
					int lenegth = selectContents.length;
					int[] temp = new int[lenegth + 1];

					System.arraycopy(selectContents, 0, temp, 0, lenegth);
					temp[lenegth] = number;
					selectContents = temp;
				}
			}
			if (part == null || selectContents.length == 0 || !viewer.getControl().isVisible()) {
				return;
			}

			if (selectionType == 0) {
				part.selectRow(selectContents, notifyToMedia);
			} else if (selectionType == 1) {
				part.selectColumn(selectContents, notifyToMedia);
			}

		} else if (!viewer.getControl().isDisposed()) {
			if (viewer instanceof DeferredGraphicalViewer) {
				((DeferredGraphicalViewer) viewer).setSelection(new StructuredSelection(list), notifyToMedia);
			}
		}
	}

	/**
	 * Creates a folder resource given the folder handle.
	 *
	 * @param folderHandle the folder handle to create a folder resource for
	 * @param monitor      the progress monitor to show visual progress with
	 * @exception CoreException              if the operation fails
	 * @exception OperationCanceledException if the operation is canceled
	 */
	public static void createFolder(IFolder folderHandle, IProgressMonitor monitor) throws CoreException {
		try {
			// Create the folder resource in the workspace
			// Update: Recursive to create any folders which do not exist
			// already
			if (!folderHandle.exists()) {
				IPath path = folderHandle.getFullPath();
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				int numSegments = path.segmentCount();
				if (numSegments > 2 && !root.getFolder(path.removeLastSegments(1)).exists()) {
					// If the direct parent of the path doesn't exist, try
					// to create the
					// necessary directories.
					for (int i = numSegments - 2; i > 0; i--) {
						IFolder folder = root.getFolder(path.removeLastSegments(i));
						if (!folder.exists()) {
							folder.create(false, true, monitor);
						}
					}
				}
				folderHandle.create(false, true, monitor);
			}
		} catch (CoreException e) {
			// If the folder already existed locally, just refresh to get
			// contents
			if (e.getStatus().getCode() == IResourceStatus.PATH_OCCUPIED) {
				folderHandle.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 500));
			} else {
				throw e;
			}
		}

		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
	}

	/**
	 * @return Report Designer UI plugin installation directory as OS string.
	 */
	public static String getHomeDirectory() {
		URL url = ReportPlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
		String home = null;
		try {
			// Fixed Bugzilla 263905 - Default path of Template Folder in
			// Preference setting is incorrect
			home = new File(FileLocator.resolve(url).getPath()).getAbsolutePath();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return home;
	}

	/**
	 * @return Report Designer UI plugin installation directory as OS string.
	 */
	public static String getFragmentDirectory() {
		Bundle bundle = Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST);
		if (bundle == null) {
			return null;
		}
		URL url = bundle.getEntry("/"); //$NON-NLS-1$
		if (url == null) {
			return null;
		}
		String directory = null;
		try {
			directory = FileLocator.resolve(url).getPath();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return directory;
	}

	/**
	 * Creates a blank label under the given parent.
	 *
	 * @return the label created
	 */
	public static Label createBlankLabel(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setVisible(false);
		return label;
	}

	public static boolean includeLibrary(ModuleHandle moduleHandle, String libraryPath)
			throws DesignFileException, SemanticException {
		return includeLibrary(moduleHandle, libraryPath, false);
	}

	/**
	 * Includes the library into within the given module.
	 *
	 * @param moduleHandle the handle module
	 * @param libraryPath  the full path of the library
	 * @return true if it included successfully, or false if the operation failed.
	 */
	public static boolean includeLibrary(ModuleHandle moduleHandle, String libraryPath, boolean isDefault)
			throws DesignFileException, SemanticException {
		String namespace = getLibraryNamespace(moduleHandle, libraryPath, isDefault);
		if (namespace != null) {
			// is a filesystem file.
			if (libraryPath.startsWith("file") || new File(libraryPath).exists()) //$NON-NLS-1$
			{
				moduleHandle.includeLibrary(
						DEUtil.getRelativedPath(ReportPlugin.getDefault().getResourceFolder(), libraryPath), namespace);
			}
			// is a bundle resource
			else if (libraryPath.startsWith("bundleresource")) //$NON-NLS-1$
			{
				try {
					moduleHandle.includeLibrary(new URL(libraryPath).getPath(), namespace);
				} catch (MalformedURLException e) {
					ExceptionHandler.openMessageBox(MSG_DIALOG_TITLE,
							MessageFormat.format(MSG_DIALOG_MSG, new String[] { libraryPath }), SWT.ICON_INFORMATION);
				}
			} else {
				moduleHandle.includeLibrary(libraryPath, namespace);
			}
			// ExceptionHandler.openMessageBox( MSG_DIALOG_TITLE,
			// MessageFormat.format( MSG_DIALOG_MSG, new String[]{
			// libraryPath
			// } ),
			// SWT.ICON_INFORMATION );
			return true;
		}
		return false;
	}

	public static boolean includeLibrary(ModuleHandle moduleHandle, LibraryHandle libraryHandle)
			throws DesignFileException, SemanticException {
		return includeLibrary(moduleHandle, libraryHandle, false);
	}

	/**
	 * Includes the library into within the given module.
	 *
	 * @param moduleHandle  the handle module
	 * @param libraryHandle the library to include.
	 * @return true if it included successfully, or false if the operation failed.
	 */
	public static boolean includeLibrary(ModuleHandle moduleHandle, LibraryHandle libraryHandle, boolean isDefault)
			throws DesignFileException, SemanticException {
		if (moduleHandle != libraryHandle && !moduleHandle.isInclude(libraryHandle)) {
			return includeLibrary(moduleHandle, libraryHandle.getFileName(), isDefault);
		}
		return true;
	}

	/**
	 * Includes the library into within the current module.
	 *
	 * @param libraryHandle the library to include.
	 * @return true if it included successfully, or false if the operation failed.
	 */
	public static boolean includeLibrary(LibraryHandle libraryHandle) throws DesignFileException, SemanticException {
		return includeLibrary(SessionHandleAdapter.getInstance().getReportDesignHandle(), libraryHandle);
	}

	/**
	 * Returns the name for the file
	 *
	 * @param filePath the full path of the file
	 * @return Returns the name of the file
	 */
	public static String getSimpleFileName(String filePath) {
		return new File(filePath).getName();
	}

	/**
	 * Returns the namespace of the library for inculde
	 *
	 * @param handle      the module handle to include the library
	 * @param libraryPath the full path of the library file to include
	 * @return the namespace used to include, or null if the user cancels this
	 *         operator
	 */
	private static String getLibraryNamespace(ModuleHandle handle, String libraryPath, boolean isDefault) {
		String namespace = getSimpleFileName(libraryPath).split("\\.")[0]; //$NON-NLS-1$
		if (isDefault && handle.getLibrary(namespace) != null) {
			return null;
		}
		if (handle.getLibrary(namespace) != null) {
			ImportLibraryDialog dialog = new ImportLibraryDialog(namespace);
			if (dialog.open() == Dialog.OK) {
				namespace = (String) dialog.getResult();
			} else {
				namespace = null;
			}
		}
		return namespace;
	}

	public static ThemeHandle themeInModuleHandle(ThemeHandle handle, ModuleHandle moduleHandle) {

		String themeName = handle.getName().trim();
		String themeFileName = handle.getModuleHandle().getFileName();

		LibraryHandle libHandle = moduleHandle.findLibrary(themeFileName);
		if (libHandle == null) {
			return null;
		}
		Iterator<?> iterator = moduleHandle.getVisibleThemes(IAccessControl.DIRECTLY_INCLUDED_LEVEL).iterator();

		if (iterator != null) {
			while (iterator.hasNext()) {
				ReportElementHandle elementHandle = (ReportElementHandle) iterator.next();

				if (elementHandle.getName().trim().equals(themeName) && elementHandle.getRoot() == libHandle) {
					return (ThemeHandle) elementHandle;
				}

			}
		}

		return null;
	}

	public static ThemeHandle applyTheme(ThemeHandle handle, ModuleHandle moduleHandle, LibraryHandle library) {

		if (handle.getRoot() == moduleHandle) {
			try {
				moduleHandle.setTheme(handle);
			} catch (SemanticException e) {
				GUIException exception = GUIException.createGUIException(ReportPlugin.REPORT_UI, e,
						"Library.DND.messages.cannotApplyTheme");//$NON-NLS-1$
				ExceptionHandler.handle(exception);
			}
			return handle;
		}

		ThemeHandle applyThemeHandle = themeInModuleHandle(handle, moduleHandle);
		if (applyThemeHandle != null) {
			try {
				moduleHandle.setTheme(applyThemeHandle);
			} catch (SemanticException e) {
				GUIException exception = GUIException.createGUIException(ReportPlugin.REPORT_UI, e,
						"Library.DND.messages.cannotApplyTheme");//$NON-NLS-1$
				ExceptionHandler.handle(exception);

			}
		}
		return applyThemeHandle;

	}

	/**
	 * Get
	 *
	 * @param lineText
	 * @return
	 */
	public static int[] getExpressionBidiSegments(String lineText) {
		if (lineText == null || "".equals(lineText)) { //$NON-NLS-1$
			return null;
		}
		int[] level = getExpressionBidiLevel(lineText);
		int[] segments = new int[level.length];
		int j = 0;
		segments[j++] = 0;
		for (int i = 1; i < level.length; i++) {
			if (level[i] != level[i - 1]) {
				segments[j++] = i;
			}
		}
		if (j < segments.length) {
			int[] result = new int[j];
			System.arraycopy(segments, 0, result, 0, j);
			segments = result;
		}
		return segments;
	}

	/**
	 * Get Bidi level of Expression String.
	 *
	 * @param message
	 * @return
	 */
	public static int[] getExpressionBidiLevel(String message) {
		java.text.Bidi bidi = new Bidi(message,
				// Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT );
				Bidi.DIRECTION_LEFT_TO_RIGHT); // bidi_hcg
		int[] level = new int[message.length()];
		boolean bidiStart = false;
		Stack<Character> bracket = new Stack<>();
		for (int i = 0; i < message.length(); i++) {
			char c = message.charAt(i);
			if (isNeutral(c)) {
				// Neutral char enclosed with ' or " should bidi with surround
				// bidichar.
				// otherwise should not bidi.
				if (c == '\'' || c == '\"') {
					if (bracket.empty()) {
						bracket.add(Character.valueOf(c));
					} else if (bracket.peek().charValue() == c) {
						bracket.pop();
						bidiStart = false;
					} else {
						bracket.add(Character.valueOf(c));
					}
				}
				level[i] = bidiStart && !bracket.empty() ? 1 : 0;
			} else {
				level[i] = bidi.getLevelAt(i);
				if (level[i] % 2 != 0) {
					bidiStart = true;
				}
			}
		}
		return level;
	}

	private static boolean isNeutral(char c) {
		boolean match = punctuation.matcher(new StringBuffer(0).append(c)).matches();
		return match;
	}

	/**
	 * Sets the given help context id on the given control's shell.
	 *
	 * @param control   the control on which to register the context id
	 * @param contextId the context id to use when F1 help is invoked
	 */
	public static void bindHelp(Control control, String contextId) {
		// disable the help
		// try
		// {
		// IWorkbench workbench = PlatformUI.getWorkbench( );
		// workbench.getHelpSystem( ).setHelp( control, contextId );
		// }
		// catch ( RuntimeException e )
		// {
		// // Do nothing since there's no workbench
		// }
	}

	/**
	 * Gets the ViewPart with the specified id
	 *
	 * @param id the id of view part
	 *
	 * @return Returns the view part, or null if not found
	 */

	public static IViewPart getView(String id) {
		IWorkbenchPage tPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewReference[] v = tPage.getViewReferences();
		int i;
		for (i = 0; i < v.length; i++) {
			if (v[i].getId().equals(id)) {
				return (IViewPart) v[i].getPart(true);
			}
		}
		return null;
	}

	public static IEditorPart getEditor(String id) {
		IWorkbenchPage tPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (tPage == null) {
			return null;
		}
		IEditorReference[] v = tPage.getEditorReferences();
		int i;
		for (i = 0; i < v.length; i++) {
			if (v[i].getId().equals(id)) {
				return (IEditorPart) v[i].getPart(true);
			}
		}
		return null;
	}

	public static IEditorPart getActiveEditor(String id) {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}

		IWorkbenchPage tPage = window.getActivePage();
		if (tPage == null) {
			return null;
		}

		IEditorPart activeEditPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		IEditorReference[] v = tPage.getEditorReferences();
		int i;
		for (i = 0; i < v.length; i++) {
			if (v[i].getId().equals(id)) {
				IEditorPart temp = (IEditorPart) v[i].getPart(false);
				if (temp == activeEditPart) {
					return activeEditPart;
				}
			}
		}
		return null;
	}

	/**
	 * Check if the property should add quote. Currently use in set fontfamily
	 * property.
	 *
	 * @param elementName
	 * @param property
	 * @param value
	 * @return
	 */
	public static boolean needAddQuote(String elementName, String property, String value) {
		IChoice[] choices = ChoiceSetFactory.getElementChoiceSet(elementName, property).getChoices();
		for (int i = 0; i < choices.length; i++) {
			if (choices[i].getValue().equals(value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Notice: Please dispose the image after done.
	 *
	 * @param composite
	 * @return
	 */
	public static Image newImageFromComposite(Composite composite) {
		Point compositeSize = composite.getSize();
		GC gc = new GC(composite);
		Image image = new Image(Display.getCurrent(), compositeSize.x, compositeSize.y);
		gc.copyArea(image, 0, 0);
		gc.dispose();
		return image;
	}

	public static IModelEventManager getModelEventManager() {
		IEditorPart input = null;

		for (int i = 0; i < EDITOR_IDS.length; i++) {
			input = getActiveEditor(EDITOR_IDS[i]);
			if (input != null) {
				break;
			}
		}

		if (input == null) {
			IEditorPart part = getActiveEditor(true);
			if (part instanceof IReportEditor) {
				input = part;
			}
		}

		if (input == null) {
			return null;
		}

		Object adapter = input.getAdapter(IModelEventManager.class);
		if (adapter instanceof IModelEventManager) {
			return (IModelEventManager) adapter;
		}
		return null;
	}

	/**
	 * Return the project folder if current edited report file is in eclipse
	 * project, else return the report file's folder.
	 *
	 * This method is used for set IModuleOption.RESOURCE_FOLDER_KEY property when
	 * open report.
	 *
	 * @return
	 */
	public static String getProjectFolder() {
		FormEditor editor = getActiveReportEditor();
		if (editor != null) {
			IEditorInput input = editor.getEditorInput();
			if (input != null) {
				return getProjectFolder(input);
			}
		}
		return null;
	}

	public static String getHeadColumnDisplayName(List<ColumnHintHandle> list, ResultSetColumnHandle column) {
		for (ColumnHintHandle element : list) {
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				if (element.getHeading() != null) {
					return element.getHeading();
				}
				if (element.getDisplayNameKey() != null) {
					String displayName = element.getExternalizedValue(ColumnHint.DISPLAY_NAME_ID_MEMBER,
							ColumnHint.DISPLAY_NAME_MEMBER);
					if (displayName != null) {
						return displayName;
					}
				}
				return element.getDisplayName() == null ? column.getColumnName() : element.getDisplayName();
			}
		}
		return column.getColumnName();
	}

	public static String getHeadColumnDisplayName(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);
		for (Iterator<?> iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter
				.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				if (element.getHeading() != null) {
					return element.getHeading();
				}
				if (element.getDisplayNameKey() != null) {
					String displayName = element.getExternalizedValue(ColumnHint.DISPLAY_NAME_ID_MEMBER,
							ColumnHint.DISPLAY_NAME_MEMBER);
					if (displayName != null) {
						return displayName;
					}
				}
				return element.getDisplayName() == null ? column.getColumnName() : element.getDisplayName();
			}
		}
		return column.getColumnName();
	}

	public static String getColumnDisplayName(List<ColumnHintHandle> list, ResultSetColumnHandle column) {
		for (ColumnHintHandle element : list) {
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				if (element.getDisplayNameKey() != null) {
					String displayName = element.getExternalizedValue(ColumnHint.DISPLAY_NAME_ID_MEMBER,
							ColumnHint.DISPLAY_NAME_MEMBER);
					if (displayName != null) {
						return displayName;
					}
				}
				return element.getDisplayName() == null ? column.getColumnName() : element.getDisplayName();
			}
		}
		return column.getColumnName();
	}

	/**
	 * Return the display name of dataset column
	 *
	 * @param column
	 * @return
	 */
	public static String getColumnDisplayName(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);
		for (Iterator<?> iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter
				.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				if (element.getDisplayNameKey() != null) {
					String displayName = element.getExternalizedValue(ColumnHint.DISPLAY_NAME_ID_MEMBER,
							ColumnHint.DISPLAY_NAME_MEMBER);
					if (displayName != null) {
						return displayName;
					}
				}
				return element.getDisplayName() == null ? column.getColumnName() : element.getDisplayName();
			}
		}
		return column.getColumnName();
	}

	public static String getColumnDisplayNameKey(List<ColumnHintHandle> list, ResultSetColumnHandle column) {
		for (ColumnHintHandle element : list) {
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element.getDisplayNameKey();
			}
		}
		return null;
	}

	/**
	 * Return the display name of dataset column
	 *
	 * @param column
	 * @return
	 */
	public static String getColumnDisplayNameKey(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);
		for (Iterator<?> iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter
				.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element.getDisplayNameKey();
			}
		}
		return null;
	}

	public static String getColumnHeaderDisplayNameKey(List<ColumnHintHandle> list, ResultSetColumnHandle column) {
		for (ColumnHintHandle element : list) {
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element.getHeadingKey();
			}
		}
		return null;
	}

	public static String getColumnHeaderDisplayNameKey(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);
		for (Iterator<?> iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter
				.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element.getHeadingKey();
			}
		}
		return null;
	}

	public static boolean isWordWrap(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);
		for (Iterator<?> iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter
				.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element.wordWrap();
			}
		}
		return false;
	}

	public static String getClolumnHandleAlignment(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);
		for (Iterator<?> iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter
				.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element.getHorizontalAlign();
			}
		}
		return null;
	}

	public static String getClolumnHandleHelpText(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);
		for (Iterator<?> iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter
				.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element.getHelpText();
			}
		}
		return null;
	}

	/**
	 * Return the analysis of dataset column
	 *
	 * @param column
	 * @return
	 */
	public static String getColumnAnalysis(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);

		List<ColumnHintHandle> columnHints = DataUtil.getColumnHints(dataset);

		for (ColumnHintHandle columnHint : columnHints) {
			if (column.getColumnName().equals(columnHint.getColumnName())
					|| column.getColumnName().equals(columnHint.getAlias())) {
				return columnHint.getAnalysis();
			}
		}
		return null;
	}

	/**
	 * Return the AnalysisColumn
	 *
	 * @param column
	 * @return
	 */
	public static String getAnalysisColumn(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);
		for (Iterator<?> iter = dataset.getPropertyHandle(DataSetHandle.COLUMN_HINTS_PROP).iterator(); iter
				.hasNext();) {
			ColumnHintHandle element = (ColumnHintHandle) iter.next();
			if (element.getColumnName().equals(column.getColumnName())
					|| column.getColumnName().equals(element.getAlias())) {
				return element.getAnalysisColumn();
			}
		}
		return null;
	}

	public static ActionHandle getColumnAction(List<ColumnHintHandle> list, ResultSetColumnHandle column) {

		for (ColumnHintHandle columnHint : list) {
			if (column.getColumnName().equals(columnHint.getColumnName())
					|| column.getColumnName().equals(columnHint.getAlias())) {
				return columnHint.getActionHandle();
			}
		}
		return null;
	}

	/**
	 * Return the action property of dataset column from column hint
	 *
	 * @param column
	 * @return
	 */
	public static ActionHandle getColumnAction(ResultSetColumnHandle column) {
		DataSetHandle dataset = getDataSet(column);

		List<ColumnHintHandle> columnHints = DataUtil.getColumnHints(dataset);

		for (ColumnHintHandle columnHint : columnHints) {
			if (column.getColumnName().equals(columnHint.getColumnName())
					|| column.getColumnName().equals(columnHint.getAlias())) {
				return columnHint.getActionHandle();
			}
		}
		return null;
	}

	/**
	 * Convenient method to setup button to invoke expression builder
	 */
	public static void setExpressionButtonImage(Button button) {
		String imageName;
		if (button.isEnabled()) {
			imageName = IReportGraphicConstants.ICON_ENABLE_EXPRESSION_BUILDERS;
		} else {
			imageName = IReportGraphicConstants.ICON_DISABLE_EXPRESSION_BUILDERS;
		}
		Image image = ReportPlatformUIImages.getImage(imageName);

		GridData gd = new GridData();
		if (Platform.getOS().equals(Platform.OS_WIN32)) {
			gd.widthHint = 20;
			gd.heightHint = 20;
		} else {
			gd.widthHint = button.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		}

		button.setLayoutData(gd);

		button.setImage(image);
		if (button.getImage() != null) {
			button.getImage().setBackground(button.getBackground());
		}
		button.setToolTipText(Messages.getString("ExpressionBuilder.ToolTip")); //$NON-NLS-1$

	}

	public static ExpressionButton createExpressionButton(Composite parent, int style) {
		return createExpressionButton(parent, style, true);
	}

	public static ExpressionButton createExpressionButton(Composite parent, int style, boolean allowConstant) {
		ExpressionButton button = new ExpressionButton(parent, style, allowConstant);
		IExpressionButtonProvider provider = (IExpressionButtonProvider) ElementAdapterManager.getAdapter(button,
				IExpressionButtonProvider.class);
		if (provider != null) {
			button.setExpressionButtonProvider(provider);
		}

		GridData gd = new GridData();
		if (Platform.getOS().equals(Platform.OS_WIN32)) {
			gd.heightHint = 20;
		}

		button.getControl().setLayoutData(gd);
		return button;
	}

	public static ExpressionButton createExpressionButton(Composite parent, int style, boolean allowConstant,
			boolean showLeafOnlyInThirdColumn) {
		ExpressionButton button = new ExpressionButton(parent, style, allowConstant);
		IExpressionButtonProvider provider = (IExpressionButtonProvider) ElementAdapterManager.getAdapter(button,
				IExpressionButtonProvider.class);
		if (provider != null) {
			button.setExpressionButtonProvider(provider);
		}
		if (button.getExpressionButtonProvider() instanceof ExpressionButtonProvider) {
			((ExpressionButtonProvider) button.getExpressionButtonProvider())
					.setShowLeafOnlyInThirdColumn(showLeafOnlyInThirdColumn);
		}

		GridData gd = new GridData();
		if (Platform.getOS().equals(Platform.OS_WIN32)) {
			gd.heightHint = 20;
		}

		button.getControl().setLayoutData(gd);
		return button;
	}

	/**
	 * Get the default script type set in preference.
	 *
	 * @return
	 */
	public static String getDefaultScriptType() {
		return PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault(), UIUtil.getCurrentProject())
				.getString(ReportPlugin.DEFAULT_SCRIPT_TYPE);
	}

	/**
	 * Get the default fiscal year start date set in preference.
	 *
	 * @return
	 */
	public static String getFiscalYearStart() {
		return PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault(), UIUtil.getCurrentProject())
				.getString(ReportPlugin.FISCAL_YEAR_START);
	}

	/**
	 * @return Returns all extended items that doesn't register any UI extensions,
	 *         which implies they are invisible to UI.
	 */
	public static List<IElementDefn> getInvisibleExtensionElements() {
		List<IElementDefn> list = new ArrayList<>(DEUtil.getMetaDataDictionary().getExtensions());

		List<ExtendedElementUIPoint> points = ExtensionPointManager.getInstance().getExtendedElementPoints();
		for (ExtendedElementUIPoint point : points) {
			if (isVisibleExtensionElement(point)) {
				list.remove(DEUtil.getElementDefn(point.getExtensionName()));
			}
		}

		PaletteEntryExtension[] entries = EditpartExtensionManager.getPaletteEntries();
		for (PaletteEntryExtension entry : entries) {
			list.remove(DEUtil.getElementDefn(entry.getItemName()));
		}

		return list;
	}

	public static boolean isVisibleExtensionElement(ExtendedElementUIPoint point) {
		String preference = (String) point
				.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER_BY_PREFERENCE);
		if (preference != null) {
			String[] splits = preference.split("/"); //$NON-NLS-1$
			if (splits.length == 2) {
				IPreferences wrapper = PreferenceFactory.getInstance().getPluginPreferences(splits[0], null);
				if (wrapper != null) {
					Boolean bool = wrapper.getBoolean(splits[1]);
					return bool;
				}
			}
		} else {
			return ((Boolean) point.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER)).booleanValue();
		}
		return true;
	}

	/**
	 * Returns all supported elements from UI for given SlotHandle
	 *
	 * @param slotHandle
	 * @return
	 */
	public static List<IElementDefn> getUIElementSupportList(ElementDetailHandle slotHandle) {
		List<IElementDefn> list = null;
		if (slotHandle instanceof SlotHandle) {
			list = DEUtil.getElementSupportList((SlotHandle) slotHandle);
		} else if (slotHandle instanceof PropertyHandle) {
			list = DEUtil.getElementSupportList((PropertyHandle) slotHandle);
		}

		list.removeAll(getInvisibleExtensionElements());

		return list;
	}

	/**
	 * Returns all supported elements from UI for given PropertyHandle
	 *
	 * @param propertyHandle
	 * @return
	 */
	public static List<IElementDefn> getUIElementSupportList(PropertyHandle propertyHandle) {
		List<IElementDefn> list = DEUtil.getElementSupportList(propertyHandle);

		list.removeAll(getInvisibleExtensionElements());

		return list;
	}

	public static void doFinishSave(ModuleHandle model) {
		Object[] resolves = ElementAdapterManager.getAdapters(model, IRelatedFileChangeResolve.class);
		if (resolves == null) {
			return;
		}

		for (int i = 0; i < resolves.length; i++) {
			IRelatedFileChangeResolve find = (IRelatedFileChangeResolve) resolves[i];
			find.notifySaveFile(model);
		}
	}

	public static boolean reloadModuleHandleLibraries(ModuleHandle moduleHandle) {
		boolean retBoolean = true;
		try {
			moduleHandle.reloadLibraries();
		} catch (SemanticException | DesignFileException e) {
			ExceptionHandler.handle(e);
			retBoolean = false;
		}
		return retBoolean;
	}

	/**
	 * If there are not the default library template return null
	 *
	 * @return
	 */
	public static String getDefaultLibraryTemplate() {
		if (Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST) == null) {
			return null;
		}

		URL url = FileLocator.find(Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST),
				new Path("/templates/blank_library.rpttemplate"), //$NON-NLS-1$
				null);

		if (url == null) {
			return null;
		}

		try {
			return FileLocator.resolve(url).getPath();
		} catch (IOException e1) {
			return null;
		}
	}

	/**
	 * Get the current font family.
	 *
	 * @return The current font family
	 */
	public static Font getFont(ReportItemHandle handle) {
		StyleHandle styleHandle = handle.getPrivateStyle();

		String family = (String) (styleHandle.getFontFamilyHandle().getValue());
		// some font not defined in model is enclosed with quote.
		family = DEUtil.removeQuote(family);
		String FontFamily = (String) DesignerConstants.familyMap.get(family);

		if (FontFamily == null) {
			FontFamily = family;
		}

		// fix bugzilla 210899, set minimum font size as 1.
		int fontSize = Math.max(DEUtil.getFontSizeIntValue(handle), 1);

		int fontStyle = 0;
		String fontWeight = styleHandle.getFontWeight();
		String style = styleHandle.getFontStyle();

		// Eclipse does not distinct ITALIC and OBLIQUE, so we treat OBLIQUE as
		// ITATIC. And if font weight >= 700, deal with BOLD.
		if (fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_BOLD)
				|| fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_BOLDER)
				|| fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_700)
				|| fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_800)
				|| fontWeight.equals(DesignChoiceConstants.FONT_WEIGHT_900)) {
			fontStyle = fontStyle | SWT.BOLD;
		}

		if (style.equals(DesignChoiceConstants.FONT_STYLE_ITALIC)
				|| style.equals(DesignChoiceConstants.FONT_STYLE_OBLIQUE)) {
			fontStyle = fontStyle | SWT.ITALIC;
		}

		Font font = FontManager.getFont(FontFamily, fontSize, fontStyle);

		return font;
	}

	public static boolean canPreviewWithErrors(ModuleHandle model) {
		if (model == null) {
			return false;
		}
		model.checkReport();
		List<?> errorList = model.getErrorList();
		if (errorList.size() > 0) {
			ErrorStatus status = new ErrorStatus(ReportPlugin.REPORT_UI, 1009,
					Messages.getString("UIUtil.previewconfirm.title"), //$NON-NLS-1$
					null);

			for (int i = 0; i < errorList.size(); i++) {
				ErrorDetail ed = (ErrorDetail) errorList.get(i);

				status.addError("Line " //$NON-NLS-1$
						+ ed.getLineNo() + ": " //$NON-NLS-1$
						+ ed.getMessage());
			}

			return new ErrorDialog(Display.getCurrent().getActiveShell(),
					Messages.getString("UIUtil.previewconfirm.title"), //$NON-NLS-1$
					Messages.getString("UIUtil.previewconfirm.message"), //$NON-NLS-1$
					status, IStatus.OK | IStatus.INFO | IStatus.WARNING | IStatus.ERROR) {

				@Override
				protected void createButtonsForButtonBar(Composite parent) {
					createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
					createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
					createDetailsButton(parent);
				}
			}.open() == Window.OK;
		}
		return true;
	}

	/**
	 * @return Creates a shell according to default window orientation
	 */
	public static Shell createDefaultShell() {
		return new Shell(SWT.SHELL_TRIM | Window.getDefaultOrientation());
	}

	public static void refreshCurrentEditorMarkers() {
		FormEditor editor = UIUtil.getActiveReportEditor();
		if (editor instanceof AbstractMultiPageEditor) {
			try {
				((AbstractMultiPageEditor) editor).refreshMarkers(editor.getEditorInput());
			} catch (CoreException e) {
			}
		}
	}

	public static final String CONFIRM_PARAM_DELETE_TITLE = Messages
			.getString("DefaultNodeProvider.ParameterGroup.ConfirmTitle"); //$NON-NLS-1$

	public static final String CONFIRM_PARAM_DELETE_MESSAGE = Messages
			.getString("DefaultNodeProvider.ParameterGroup.ConfirmMessage"); //$NON-NLS-1$

	public static final String DLG_CONFIRM_MSG = Messages.getString("DefaultNodeProvider.Dlg.Confirm"); //$NON-NLS-1$

	public static final String DLG_HAS_FOLLOWING_CLIENTS_MSG = Messages.getString("DefaultNodeProvider.Tree.Clients"); //$NON-NLS-1$

	public static final String DLG_REFERENCE_FOUND_TITLE = Messages.getString("DefaultNodeProvider.Tree.Reference"); //$NON-NLS-1$

	/**
	 * Test if the passed object can be delete. This method will check whether the
	 * deleted elements are referenced by others, if is, a confirm dialog will
	 * popup.
	 *
	 * From DeleteHandler.
	 *
	 * @param object
	 * @return
	 */
	public static boolean canDelete(Object object) {
		if (object instanceof IStructuredSelection) {
			for (Iterator<?> itor = ((IStructuredSelection) object).iterator(); itor.hasNext();) {
				Object obj = itor.next();
				if (!canDelete(obj)) {
					return false;
				}
			}
			return true;
		} else if (object instanceof List) {
			for (Iterator<?> itor = ((List<?>) object).iterator(); itor.hasNext();) {
				Object obj = itor.next();
				if (!canDelete(obj)) {
					return false;
				}
			}
			return true;
		} else if (object instanceof EditPart) {
			return canDelete(((EditPart) object).getModel());
		} else if (object instanceof DesignElementHandle) {
			DesignElementHandle handle = (DesignElementHandle) object;
			if (handle instanceof ParameterGroupHandle) {
				if (((ParameterGroupHandle) handle).getParameters().getCount() > 0) {
					if (!MessageDialog.openQuestion(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
							CONFIRM_PARAM_DELETE_TITLE, CONFIRM_PARAM_DELETE_MESSAGE)) {
						return false;
					}
					for (Iterator<?> iter = ((ParameterGroupHandle) handle).getParameters().iterator(); iter
							.hasNext();) {
						Object obj = iter.next();
						if (obj instanceof ParameterHandle) {
							ParameterHandle parameter = (ParameterHandle) obj;
							ConfigVariable cv = parameter.getModuleHandle().findConfigVariable(parameter.getName());
							try {
								if (cv != null) {
									parameter.getModuleHandle().getPropertyHandle(ReportDesignHandle.CONFIG_VARS_PROP)
											.removeItem(cv);
								}
							} catch (SemanticException e) {
								ExceptionHandler.handle(e);
							}
						}
					}
				}
			}
			ArrayList<Object> referenceList = new ArrayList<>();
			for (Iterator<?> itor = handle.clientsIterator(); itor.hasNext();) {
				referenceList.add(itor.next());
			}
			if (!referenceList.isEmpty()) {
				DeleteWarningDialog dialog = new DeleteWarningDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(), DLG_REFERENCE_FOUND_TITLE,
						referenceList);
				dialog.setPreString(Messages.getFormattedString("DefaultNodeProvider.Tree.Clients", //$NON-NLS-1$
						new Object[] { DEUtil.getDisplayLabel(handle) }));

				dialog.setSufString(Messages.getFormattedString("DefaultNodeProvider.Dlg.Confirm", //$NON-NLS-1$
						new Object[] { DEUtil.getDisplayLabel(handle) }));
				return dialog.open() != Dialog.CANCEL;
			}
			return true;
		}
		return true;
	}

	/**
	 * Check if the name of a group/dimension level is allowed.
	 *
	 * @return allowed
	 */
	public static boolean validateDimensionName(String name) {
		DataRequestSession session = null;
		ICubeQueryUtil cubeQueryUtil = null;
		try {
			session = DataRequestSession
					.newSession(new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION));
			cubeQueryUtil = session.getCubeQueryUtil();
		} catch (Exception e) {
			ExceptionHandler.handle(e);
			return false;
		}
		boolean bool = false;
		if (cubeQueryUtil != null) {
			bool = cubeQueryUtil.isValidDimensionName(name);
		}

		if (session != null) {
			session.shutdown();
		}
		return bool;
	}

	/**
	 * Gets the project folder from the input
	 *
	 * @param input
	 * @return
	 */
	public static String getProjectFolder(IEditorInput input) {
		Object fileAdapter = input.getAdapter(IFile.class);
		IFile file = null;
		if (fileAdapter != null) {
			file = (IFile) fileAdapter;
		}
		if (file != null && file.getProject() != null) {
			return file.getProject().getLocation().toOSString();
		}
		if (input instanceof IPathEditorInput) {
			File fileSystemFile = ((IPathEditorInput) input).getPath().toFile();
			return fileSystemFile.getParent();
		}
		return null;
	}

	/**
	 * Sets the session the resource folder.
	 *
	 * @param input
	 * @param project
	 * @param handle
	 */
	public static void processSessionResourceFolder(IEditorInput input, IProject project, ModuleHandle handle) {
		processSessionResourceFolder(input, project, handle, false);
	}

	public static void processSessionResourceFolder(IEditorInput input, IProject project, ModuleHandle handle,
			boolean useThreadLocal) {
		String resourceFolder = "";//$NON-NLS-1$

		if (input != null) {
			resourceFolder = getProjectFolder(input);
		}

		if (project == null) {
			project = getProjectFromInput(input);
		}

		if (StringUtil.isBlank(resourceFolder)) {
			if (project != null) {
				resourceFolder = project.getLocation().toOSString();
			}
		}

		if (StringUtil.isBlank(resourceFolder)) {
			if (handle != null) {
				resourceFolder = handle.getResourceFolder();
			}
		}

		resourceFolder = ReportPlugin.getDefault().getResourceFolder(project, resourceFolder);

		SessionHandleAdapter.getInstance().getSessionHandle(useThreadLocal).setResourceFolder(resourceFolder);
	}

	/**
	 * Gets the project from the input
	 *
	 * @param input
	 * @return
	 */
	public static IProject getProjectFromInput(IEditorInput input) {
		IProject retValue = null;
		if (input == null) {
			retValue = getCurrentProject();
		} else {
			Object fileAdapter = input.getAdapter(IFile.class);
			IFile file = null;
			if (fileAdapter != null) {
				file = (IFile) fileAdapter;
			}
			if (file != null) {
				retValue = file.getProject();
			}
		}

		if (retValue == null) {
			retValue = getCurrentProject();
		}

		return retValue;
	}

	/**
	 * Process the report design orientation change.
	 *
	 * @param newOrientation
	 * @param viewer
	 */
	public static void processOrientationChange(String newOrientation, EditPartViewer viewer) {
		if (newOrientation == null || viewer == null) {
			return;
		}
		boolean mirrored = DesignChoiceConstants.BIDI_DIRECTION_RTL.equals(newOrientation);

		viewer.flush();

		// Apply new orientation to the view.
		Composite parent = viewer.getControl().getParent();
		BidiUIUtils.INSTANCE.applyOrientation(parent, mirrored);

		parent.layout(true);

		viewer.setProperty(IReportGraphicConstants.REPORT_BIDIORIENTATION_PROPERTY, newOrientation);
	}

	public static void drawArrow(GC gc, Rectangle rect, int style) {
		Point point = new Point(rect.x + (rect.width / 2), rect.y + (rect.height / 2));
		int[] points = null;
		switch (style) {
		case SWT.LEFT:
			points = new int[] { point.x + 2, point.y - 4, point.x + 2, point.y + 4, point.x - 2, point.y };
			gc.fillPolygon(points);
			break;

		/*
		 * Low efficiency because of Win98 bug.
		 */
		case SWT.UP:
			gc.fillRectangle(new Rectangle(point.x, point.y - 1, 1, 1));
			gc.fillRectangle(new Rectangle(point.x - 1, point.y, 3, 1));
			gc.fillRectangle(new Rectangle(point.x - 2, point.y + 1, 5, 1));
			break;

		case SWT.RIGHT:
			points = new int[] { point.x - 2, point.y - 4, point.x - 2, point.y + 4, point.x + 2, point.y };
			gc.fillPolygon(points);
			break;

		/*
		 * Low efficiency because of Win98 bug.
		 */
		default:
			gc.fillRectangle(new Rectangle(point.x - 2, point.y - 1, 5, 1));
			gc.fillRectangle(new Rectangle(point.x - 1, point.y, 3, 1));
			gc.fillRectangle(new Rectangle(point.x, point.y + 1, 1, 1));
			break;
		}

	}

	/**
	 * Blends c1 and c2 based in the provided ratio.
	 *
	 * @param c1    first color
	 * @param c2    second color
	 * @param ratio percentage of the first color in the blend (0-100)
	 * @return the RGB value of the blended color
	 * @since 3.1
	 */
	public static RGB blend(RGB c1, RGB c2, int ratio) {
		int r = blend(c1.red, c2.red, ratio);
		int g = blend(c1.green, c2.green, ratio);
		int b = blend(c1.blue, c2.blue, ratio);
		return new RGB(r, g, b);
	}

	/**
	 * Blends two primary color components based on the provided ratio.
	 *
	 * @param v1    first component
	 * @param v2    second component
	 * @param ratio percentage of the first component in the blend
	 * @return
	 */
	private static int blend(int v1, int v2, int ratio) {
		int b = (ratio * v1 + (100 - ratio) * v2) / 100;
		return Math.min(255, b);
	}

	private static final String[] htmlCode = new String[256];

	static {
		for (int i = 0; i < 10; i++) {
			htmlCode[i] = "&#00" + i + ";"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		for (int i = 10; i < 32; i++) {
			htmlCode[i] = "&#0" + i + ";"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		for (int i = 32; i < 128; i++) {
			htmlCode[i] = String.valueOf((char) i);
		}

		// Special characters
		htmlCode['\t'] = "\t"; //$NON-NLS-1$
		htmlCode['\n'] = "<br/>\n"; //$NON-NLS-1$
		htmlCode['\"'] = "&quot;"; // double quote //$NON-NLS-1$
		htmlCode['&'] = "&amp;"; // ampersand //$NON-NLS-1$
		htmlCode['<'] = "&lt;"; // lower than //$NON-NLS-1$
		htmlCode['>'] = "&gt;"; // greater than //$NON-NLS-1$

		for (int i = 128; i < 256; i++) {
			htmlCode[i] = "&#" + i + ";"; //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	public static String encode(String string) {
		int n = string.length();
		char character;
		StringBuilder buffer = new StringBuilder();
		// loop over all the characters of the String.
		for (int i = 0; i < n; i++) {
			character = string.charAt(i);
			// the Htmlcode of these characters are added to a StringBuffer one
			// by one
			if (character < 256) {
				buffer.append(htmlCode[character]);
			} else {
				// Improvement posted by Joachim Eyrich
				buffer.append("&#").append((int) character).append(";"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return buffer.toString().trim();
	}

	public static Object[] getInsertPamaterElements(Object[] newObjs) {
		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		Object[] insertedObjs = new Object[newObjs.length];
		for (int i = 0; i < newObjs.length; i++) {
			insertedObjs[i] = newObjs[i];
			DesignElementHandle elementHandle = (DesignElementHandle) newObjs[i];
			if (elementHandle.getRoot() instanceof LibraryHandle) {
				LibraryHandle library = (LibraryHandle) elementHandle.getRoot();
				if (moduleHandle != library) {
					try {
						if (UIUtil.includeLibrary(moduleHandle, library)) {
							elementHandle = moduleHandle.getElementFactory().newElementFrom(elementHandle,
									elementHandle.getName());
							moduleHandle.addElement(elementHandle, ModuleHandle.PARAMETER_SLOT);
							insertedObjs[i] = elementHandle;
							continue;
						}
					} catch (Exception e) {
						ExceptionHandler.handle(e);
					}
				}
			}
		}
		return insertedObjs;
	}

	public synchronized static boolean isEmbeddedBrowserAvailable() {
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				test();
			}
		});
		embeddedBrowserTested = true;
		return embeddedBrowserAvailable;
	}

	/**
	 * Must run on UI thread
	 *
	 * @return
	 */
	private static boolean test() {
		if (!Constants.OS_WIN32.equalsIgnoreCase(Platform.getOS())
				&& !Constants.OS_LINUX.equalsIgnoreCase(Platform.getOS())) {
			return false;
		}
		if (!embeddedBrowserTested) {
			embeddedBrowserTested = true;
			Shell sh = new Shell();
			try {
				new Browser(sh, SWT.NONE);
				embeddedBrowserAvailable = true;
			} catch (SWTError se) {
				if (se.code == SWT.ERROR_NO_HANDLES) {
					// Browser not implemented
					embeddedBrowserAvailable = false;
				}
			} catch (Exception e) {
				// Browser not implemented
			}
			if (sh != null && !sh.isDisposed()) {
				sh.dispose();
			}
		}
		return embeddedBrowserAvailable;
	}

	/**
	 * Add the createby property to the mudule handle
	 *
	 * @param handles
	 */
	public static void addCreateBy(ModuleHandle handle) {
		String VERSION_MESSAGE = Messages.getString("TextPropertyDescriptor.Message.Version"); //$NON-NLS-1$
		String designerVersion = MessageFormat.format(VERSION_MESSAGE, ReportPlugin.getVersion());
		handle.setCreatedBy(designerVersion);
	}

	public static void setDPI(ReportDesignHandle handle) {
		int[] DPI = getScreenResolution();
		try {
			handle.setImageDPI(DPI[0]);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * Returns the DPI info of current display environment.
	 *
	 * @return the DPI values in format of {hdpi, vdpi}.
	 */
	public static int[] getScreenResolution() {
		int[] dpi = { 0, 0 };

		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		if (display.getThread().equals(Thread.currentThread())) {
			Point p = display.getDPI();
			dpi[0] = p.x;
			dpi[1] = p.y;

			return dpi;
		}
		final Point[] points = { new Point(0, 0) };
		final Display tempDisplay = display;
		display.syncExec(new Runnable() {

			@Override
			public void run() {
				points[0] = tempDisplay.getDPI();
			}
		});
		dpi[0] = points[0].x;
		dpi[1] = points[0].y;
		return dpi;
	}

	/**
	 * Returns the DPI info of given image if applicable.
	 *
	 * @param imageStream
	 * @return the DPI values in format of {hdpi, vdpi}.
	 */
	public static int[] getImageResolution(InputStream imageStream) {
		int[] dpi = { 0, 0 };

		if (imageStream != null) {
			try {
				ImageInputStream iis = ImageIO.createImageInputStream(imageStream);
				Iterator<ImageReader> i = ImageIO.getImageReaders(iis);
				ImageReader r = i.next();
				r.setInput(iis);
				r.read(0);

				IIOMetadata meta = r.getImageMetadata(0);

				if (meta != null) {
					double mm2inch = 25.4;

					NodeList lst;
					Element node = (Element) meta.getAsTree("javax_imageio_1.0"); //$NON-NLS-1$
					lst = node.getElementsByTagName("HorizontalPixelSize"); //$NON-NLS-1$
					if (lst != null && lst.getLength() == 1) {
						dpi[0] = (int) (mm2inch / Float.parseFloat(((Element) lst.item(0)).getAttribute("value"))); //$NON-NLS-1$
					}

					lst = node.getElementsByTagName("VerticalPixelSize"); //$NON-NLS-1$
					if (lst != null && lst.getLength() == 1) {
						dpi[1] = (int) (mm2inch / Float.parseFloat(((Element) lst.item(0)).getAttribute("value"))); //$NON-NLS-1$
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return dpi;
	}

	/**
	 * Format the data type parameter display name
	 *
	 * @param str
	 * @param param
	 * @return
	 */
	public static String formatData(Object str, AbstractScalarParameterHandle param) {
		DateFormatter formatter = new DateFormatter();
		String dataType = param.getDataType();

		try {
			if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(dataType)) {
				formatter.applyPattern("yyyy-MM-dd HH:mm:ss.SSS");//$NON-NLS-1$
				return formatter.format((Date) str);
			} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(dataType)) {
				formatter.applyPattern("yyyy-MM-dd");//$NON-NLS-1$
				return formatter.format((Date) str);
			} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(dataType)) {
				formatter.applyPattern("HH:mm:ss.SSS");//$NON-NLS-1$
				return formatter.format((Date) str);
			}
		} catch (Exception ex) {
			return str.toString();
		}
		return str.toString();
	}

	/**
	 *
	 * @param fileName the fileName
	 * @return the editor with the given fileName, or null if not found.
	 */
	public static IEditorPart findOpenedEditor(String fileName) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IEditorReference[] editors = page.getEditorReferences();

		for (int i = 0; i < editors.length; i++) {
			IEditorPart part = editors[i].getEditor(true);
			IPath location = ((IPathEditorInput) part.getEditorInput()).getPath();

			if (fileName.equalsIgnoreCase(location.toOSString())) {
				return part;
			}
		}

		return null;
	}

	/**
	 * Gets the object from the context through the key.If the value is a Object
	 * return null.
	 *
	 * @param context
	 * @param key
	 * @return
	 */
	public static Object getVariableFromContext(IEvaluationContext context, String key) {
		Object retValue = context.getVariable(key);
		if (retValue == null) {
			return null;
		}
		if (retValue.getClass().getName().equals("java.lang.Object"))//$NON-NLS-1$
		{
			retValue = null;
		}
		return retValue;
	}

	public static TextAttribute getAttributeFor(String preferenceName) {
		String prefString = PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault())
				.getString(preferenceName);
		String[] stylePrefs = ColorHelper.unpackStylePreferences(prefString);

		Color fDefaultForeground = getEclipseEditorForeground();
		Color fDefaultBackground = getEclipseEditorBackground();
		TextAttribute ta = new TextAttribute(fDefaultForeground, fDefaultBackground, SWT.NORMAL);

		if (stylePrefs != null) {
			RGB foreground = ColorHelper.toRGB(stylePrefs[0]);
			RGB background = ColorHelper.toRGB(stylePrefs[1]);

			int fontModifier = SWT.NORMAL;

			if (stylePrefs.length > 2) {
				boolean on = Boolean.parseBoolean(stylePrefs[2]);
				if (on) {
					fontModifier = fontModifier | SWT.BOLD;
				}
			}
			if (stylePrefs.length > 3) {
				boolean on = Boolean.parseBoolean(stylePrefs[3]);
				if (on) {
					fontModifier = fontModifier | SWT.ITALIC;
				}
			}
			if (stylePrefs.length > 4) {
				boolean on = Boolean.parseBoolean(stylePrefs[4]);
				if (on) {
					fontModifier = fontModifier | TextAttribute.STRIKETHROUGH;
				}
			}
			if (stylePrefs.length > 5) {
				boolean on = Boolean.parseBoolean(stylePrefs[5]);
				if (on) {
					fontModifier = fontModifier | TextAttribute.UNDERLINE;
				}
			}

			ta = new TextAttribute((foreground != null) ? ColorManager.getColor(foreground) : null,
					(background != null) ? ColorManager.getColor(background) : null, fontModifier);

		}
		return ta;
	}

	public static Color getEclipseEditorForeground() {
		ScopedPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,
				"org.eclipse.ui.editors");//$NON-NLS-1$
		Color color = null;
		if (preferenceStore != null) {
			color = preferenceStore.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT) ? null
					: createColor(preferenceStore, AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND,
							Display.getCurrent());
		}
		if (color == null) {
			color = Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
		}
		return color;
	}

	public static Color getEclipseEditorBackground() {
		ScopedPreferenceStore preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,
				"org.eclipse.ui.editors");//$NON-NLS-1$
		Color color = null;
		if (preferenceStore != null) {
			color = preferenceStore.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT) ? null
					: createColor(preferenceStore, AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND,
							Display.getCurrent());
		}
		if (color == null) {
			color = Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		}
		return color;
	}

	public static Color createColor(IPreferenceStore store, String key, Display display) {
		RGB rgb = null;
		if (store.contains(key)) {
			if (store.isDefault(key)) {
				rgb = PreferenceConverter.getDefaultColor(store, key);
			} else {
				rgb = PreferenceConverter.getColor(store, key);
			}
			if (rgb != null) {
				return new Color(display, rgb);
			}
		}
		return null;
	}

	public static String stripMnemonic(String string) {
		int index = 0;
		int length = string.length();
		do {
			while ((index < length) && (string.charAt(index) != '&')) {
				index++;
			}
			if (++index >= length) {
				return string;
			}
			if (string.charAt(index) != '&') {
				return string.substring(0, index - 1) + string.substring(index, length);
			}
			index++;
		} while (index < length);
		return string;
	}

	private static DataSetHandle getDataSet(ResultSetColumnHandle column) {
		IExtendedDataModelUIAdapter adapter = ExtendedDataModelUIAdapterHelper.getInstance().getAdapter();

		DataSetHandle dataSet;

		if (adapter != null && adapter.getDataSet(column) != null) {
			dataSet = adapter.getDataSet(column);
		} else {
			dataSet = (DataSetHandle) column.getElementHandle();
		}

		return dataSet;
	}

	public static boolean containsFocusControl(Control container) {
		Control control = container.getDisplay().getFocusControl();

		if (control == container) {
			return true;
		}

		while (control != null) {
			control = control.getParent();
			if (control == container) {
				return true;
			}
		}

		return false;
	}

	public static List<DataSetHandle> getVisibleDataSetHandles(ModuleHandle handle) {
		ArrayList<DataSetHandle> list = new ArrayList<>();
		for (Iterator<?> iterator = handle.getVisibleDataSets().iterator(); iterator.hasNext();) {
			DataSetHandle dataSetHandle = (DataSetHandle) iterator.next();
			list.add(dataSetHandle);
		}
		LinkedDataSetAdapter adapter = new LinkedDataSetAdapter();
		list.addAll(adapter.getVisibleLinkedDataSetsDataSetHandles(handle));
		return list;
	}

	public static List<CubeHandle> getVisibleCubeHandles(ModuleHandle handle) {
		ArrayList<CubeHandle> list = new ArrayList<>();
		for (Iterator<?> iterator = handle.getVisibleCubes().iterator(); iterator.hasNext();) {
			CubeHandle cubeHandle = (CubeHandle) iterator.next();
			list.add(cubeHandle);
		}
		LinkedDataSetAdapter adapter = new LinkedDataSetAdapter();
		list.addAll(adapter.getVisibleLinkedDataSetsCubeHandles(handle));
		return list;
	}

	public static Image getElementIcon(String elementName) {
		if (IReportElementConstants.REPORT_ELEMENT_LABEL.equals(elementName)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_LABEL);
		}
		if (IReportElementConstants.REPORT_ELEMENT_TEXT.equals(elementName)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_TEXT);
		}
		if (IReportElementConstants.REPORT_ELEMENT_TEXTDATA.equals(elementName)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_TEXTDATA);
		}
		if (IReportElementConstants.REPORT_ELEMENT_DATA.equals(elementName)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_DATA);
		}
		if (IReportElementConstants.REPORT_ELEMENT_IMAGE.equals(elementName)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_IMAGE);
		}
		if (IReportElementConstants.REPORT_ELEMENT_GRID.equals(elementName)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_GRID);
		}
		if (IReportElementConstants.REPORT_ELEMENT_LIST.equals(elementName)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_LIST);
		}
		if (IReportElementConstants.REPORT_ELEMENT_TABLE.equals(elementName)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_TABLE);
		}

		ExtendedElementUIPoint uipoint = ExtensionPointManager.getInstance().getExtendedElementPoint(elementName);
		if (uipoint != null) {
			ImageDescriptor descriptor = (ImageDescriptor) uipoint
					.getAttribute(IExtensionConstants.ATTRIBUTE_KEY_PALETTE_ICON);
			return UIHelper.getImage("element-icon-" + elementName, descriptor); //$NON-NLS-1$
		}

		PaletteEntryExtension extension = EditpartExtensionManager.getPaletteEntry(elementName);
		if (extension != null) {
			return UIHelper.getImage("element-icon-" + elementName, extension.getIcon()); //$NON-NLS-1$
		}

		return null;
	}

	/**
	 * @return Returns the groups for given element
	 */
	public static List<GroupHandle> getGroups(DesignElementHandle handle) {
		List<GroupHandle> groupList = new ArrayList<>();
		if (handle instanceof ListingHandle) {
			SlotHandle groupSlotHandle = ((ListingHandle) handle).getGroups();
			for (Iterator iter = groupSlotHandle.iterator(); iter.hasNext();) {
				GroupHandle group = (GroupHandle) iter.next();
				groupList.add(group);
			}
			return groupList;
		}

		// if it's not listing element, try using adapter to get the group
		// structure.
		Object adapter = ElementAdapterManager.getAdapter(handle, IGroupStructureProvider.class);
		if (adapter instanceof IGroupStructureProvider) {
			List<GroupHandle> groups = ((IGroupStructureProvider) adapter).getGroups(handle);

			if (groups != null && groups.size() > 0) {
				return groups;
			}
		}

		// otherwise try traversing up the parents in case it's the subelement.
		DesignElementHandle result = handle.getContainer();
		if (result != null) {
			if (result instanceof GroupHandle) {
				groupList.add((GroupHandle) result);
				return groupList;
			}
			return getGroups(result);
		}

		return groupList;
	}
}
