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

package org.eclipse.birt.report.designer.ui.ide.wizards;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.DrillDownComposite;

/**
 * WizardSaveAsPage
 */
public class WizardSaveAsPage extends WizardPage {

	private static String EXTENSIONS = ".rptdesign"; //$NON-NLS-1$
	static {
		List extensionList = ReportPlugin.getDefault().getReportExtensionNameList();
		if (!extensionList.isEmpty()) {
			EXTENSIONS = "." + extensionList.get(0); //$NON-NLS-1$
		}

		for (int i = 1; i < extensionList.size(); i++) {
			EXTENSIONS += ", ." + extensionList.get(i); //$NON-NLS-1$
		}
	}
	private static final String WRONG_DESIGN_EXTENSION = MessageFormat.format(
			Messages.getString("WizardReportSettingPage.Error.ReportorTemplate"), //$NON-NLS-1$
			new String[] { ".rptdesign" });
	private static final String WRONG_EXTENSION = MessageFormat.format(
			Messages.getString("WizardReportSettingPage.Error.ReportorTemplate"), //$NON-NLS-1$
			new String[] { EXTENSIONS });
	private ResourceAndContainerGroup resourceGroup;
	private IResource originalFile;
	private String originalName;
	private ModuleHandle model;

	public WizardSaveAsPage(String pageName) {
		super(pageName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage() {
		if (validatePage() == false) {
			return false;
		}

		if (resourceGroup.getResource().endsWith(".rpttemplate")) //$NON-NLS-1$
		{
			return true;
		}

		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {

		// create a composite with standard margins and spacing
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setFont(parent.getFont());

		Listener listener = new Listener() {

			public void handleEvent(Event event) {
				validatePage();
				try {
					getContainer().updateButtons();
				} catch (Throwable e) {
					// ignore
				}
			}
		};

		resourceGroup = new ResourceAndContainerGroup(composite, listener,
				Messages.getString("WizardSaveAsPage.FileLabel"), //$NON-NLS-1$
				"file", //$NON-NLS-1$
				false, 200);
		resourceGroup.setAllowExistingResources(true);

		setControl(composite);

		initializeControls();

		UIUtil.bindHelp(getControl(), IHelpContextIds.SAVE_AS_WIZARD_ID);
	}

	/**
	 * Initializes the controls of this dialog.
	 */
	private void initializeControls() {
		if (originalFile != null) {
			resourceGroup.setContainerFullPath(originalFile.getParent().getFullPath());
			resourceGroup.setResource(originalFile.getName());
		} else if (originalName != null)
			resourceGroup.setResource(originalName);
	}

	/**
	 * Sets the original file to use.
	 * 
	 * @param originalFile the original file
	 */
	public void setOriginalFile(IFile originalFile) {
		this.originalFile = originalFile;
	}

	/**
	 * Set the original file name to use. Used instead of
	 * <code>setOriginalFile</code> when the original resource is not an IFile. Must
	 * be called before <code>create</code>.
	 * 
	 * @param originalName default file name
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	/**
	 * Sets the model to use.
	 * 
	 * @param ModuleHandle the original file
	 */
	public void setModel(ModuleHandle model) {
		this.model = model;
	}

	/**
	 * Returns whether this page's visual components all contain valid values.
	 * 
	 * @return <code>true</code> if valid, and <code>false</code> otherwise
	 */
	public boolean validatePage() {
		setErrorMessage(null);
		if (!resourceGroup.areAllValuesValid()) {
			if (!resourceGroup.getResource().equals("")) //$NON-NLS-1$
				// if blank name
				// then fail
				// silently//$NON-NLS-1$
				setErrorMessage(resourceGroup.getProblemMessage());
			return false;
		}
		if (resourceGroup.getResource() != null && model instanceof LibraryHandle) {
			if (!resourceGroup.getResource().endsWith(".rptlibrary")) //$NON-NLS-1$
			{
				setErrorMessage(Messages.getString("WizardReportSettingPage.Error.Library")); //$NON-NLS-1$
				return false;
			}
		}

		if (resourceGroup.getResource() != null && model instanceof ReportDesignHandle) {
			// rptdesign can only save as .rptdesign
			if (model.getFileName().endsWith(".rptdesign") && !(resourceGroup.getResource().endsWith(".rptdesign")
					|| resourceGroup.getResource().endsWith(".rpttemplate"))) {
				setErrorMessage(WRONG_DESIGN_EXTENSION);
			} else if (!(ReportPlugin.getDefault().isReportDesignFile(resourceGroup.getResource())
					|| resourceGroup.getResource().endsWith(".rpttemplate"))) //$NON-NLS-1$
			{
				setErrorMessage(WRONG_EXTENSION);
				return false;
			}
		}

		return true;
	}

	/**
	 * Get the saving path
	 * 
	 * @return the saving path
	 */
	public IPath getResult() {

		IPath path = resourceGroup.getContainerFullPath().append(resourceGroup.getResource());

		// If the user does not supply a file extension and if the save
		// as dialog was provided a default file name append the extension
		// of the default filename to the new name
		if (path.getFileExtension() == null) {
			if (originalFile != null && originalFile.getFileExtension() != null)
				path = path.addFileExtension(originalFile.getFileExtension());
			else if (originalName != null) {
				int pos = originalName.lastIndexOf('.');
				if (++pos > 0 && pos < originalName.length())
					path = path.addFileExtension(originalName.substring(pos));
			}
		}

		// If the path already exists then confirm overwrite.
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);

		if (file.exists()) {
			String[] buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL,
					IDialogConstants.CANCEL_LABEL };
			String question = Messages.getFormattedString("WizardSaveAsPage.OverwriteQuestion", //$NON-NLS-1$
					new Object[] { path.toOSString() });
			MessageDialog d = new MessageDialog(getShell(), Messages.getString("WizardSaveAsPage.Question"), //$NON-NLS-1$
					null, question, MessageDialog.QUESTION, buttons, 0);
			int overwrite = d.open();
			switch (overwrite) {
			case 0: // Yes
				break;
			case 1: // No
				return null;
			case 2: // Cancel
			default:
				return Path.EMPTY;
			}
		}

		return path;
	}
}

// the follow classes are copy from package org.eclipse.ui.internal.ide.misc

class ResourceAndContainerGroup implements Listener {

	// problem identifiers
	public static final int PROBLEM_NONE = 0;

	public static final int PROBLEM_RESOURCE_EMPTY = 1;

	public static final int PROBLEM_RESOURCE_EXIST = 2;

	public static final int PROBLEM_RESOURCE_CONTAINS_SEPARATOR = 3;

	public static final int PROBLEM_PATH_INVALID = 4;

	public static final int PROBLEM_CONTAINER_EMPTY = 5;

	public static final int PROBLEM_PROJECT_DOES_NOT_EXIST = 6;

	public static final int PROBLEM_NAME_INVALID = 7;

	public static final int PROBLEM_PATH_OCCUPIED = 8;

	// the client to notify of changes
	private Listener client;

	// whether to allow existing resources
	private boolean allowExistingResources = false;

	// resource type (file, folder, project)
	private String resourceType = "file"; //$NON-NLS-1$

	// show closed projects in the tree, by default
	private boolean showClosedProjects = true;

	// problem indicator
	private String problemMessage = "";//$NON-NLS-1$

	private int problemType = PROBLEM_NONE;

	// widgets
	private ContainerSelectionGroup containerGroup;

	private Text resourceNameField;

	// constants
	private static final int SIZING_TEXT_FIELD_WIDTH = 250;

	/**
	 * Create an instance of the group to allow the user to enter/select a container
	 * and specify a resource name.
	 * 
	 * @param parent             composite widget to parent the group
	 * @param client             object interested in changes to the group's fields
	 *                           value
	 * @param resourceFieldLabel label to use in front of the resource name field
	 * @param resourceType       one word, in lowercase, to describe the resource to
	 *                           the user (file, folder, project)
	 */
	public ResourceAndContainerGroup(Composite parent, Listener client, String resourceFieldLabel,
			String resourceType) {
		this(parent, client, resourceFieldLabel, resourceType, true);
	}

	/**
	 * Create an instance of the group to allow the user to enter/select a container
	 * and specify a resource name.
	 * 
	 * @param parent             composite widget to parent the group
	 * @param client             object interested in changes to the group's fields
	 *                           value
	 * @param resourceFieldLabel label to use in front of the resource name field
	 * @param resourceType       one word, in lowercase, to describe the resource to
	 *                           the user (file, folder, project)
	 * @param showClosedProjects whether or not to show closed projects
	 */
	public ResourceAndContainerGroup(Composite parent, Listener client, String resourceFieldLabel, String resourceType,
			boolean showClosedProjects) {
		this(parent, client, resourceFieldLabel, resourceType, showClosedProjects, SWT.DEFAULT);
	}

	/**
	 * Create an instance of the group to allow the user to enter/select a container
	 * and specify a resource name.
	 * 
	 * @param parent             composite widget to parent the group
	 * @param client             object interested in changes to the group's fields
	 *                           value
	 * @param resourceFieldLabel label to use in front of the resource name field
	 * @param resourceType       one word, in lowercase, to describe the resource to
	 *                           the user (file, folder, project)
	 * @param showClosedProjects whether or not to show closed projects
	 * @param heightHint         height hint for the container selection widget
	 *                           group
	 */
	public ResourceAndContainerGroup(Composite parent, Listener client, String resourceFieldLabel, String resourceType,
			boolean showClosedProjects, int heightHint) {
		super();
		this.resourceType = resourceType;
		this.showClosedProjects = showClosedProjects;
		createContents(parent, resourceFieldLabel, heightHint);
		this.client = client;
	}

	/**
	 * Returns a boolean indicating whether all controls in this group contain valid
	 * values.
	 * 
	 * @return boolean
	 */
	public boolean areAllValuesValid() {
		return problemType == PROBLEM_NONE;
	}

	/**
	 * Creates this object's visual components.
	 * 
	 * @param parent     org.eclipse.swt.widgets.Composite
	 * @param heightHint height hint for the container selection widget group
	 */
	protected void createContents(Composite parent, String resourceLabelString, int heightHint) {

		Font font = parent.getFont();
		// server name group
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		composite.setFont(font);

		// container group
		if (heightHint == SWT.DEFAULT)
			containerGroup = new ContainerSelectionGroup(composite, this, true, null, showClosedProjects);
		else
			containerGroup = new ContainerSelectionGroup(composite, this, true, null, showClosedProjects, heightHint);

		// resource name group
		Composite nameGroup = new Composite(composite, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		nameGroup.setLayout(layout);
		nameGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL));
		nameGroup.setFont(font);

		Label label = new Label(nameGroup, SWT.NONE);
		label.setText(resourceLabelString);
		label.setFont(font);

		// resource name entry field
		resourceNameField = new Text(nameGroup, SWT.BORDER);
		resourceNameField.addListener(SWT.Modify, this);
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		resourceNameField.setLayoutData(data);
		resourceNameField.setFont(font);

		validateControls();
	}

	/**
	 * Returns the path of the currently selected container or null if no container
	 * has been selected. Note that the container may not exist yet if the user
	 * entered a new container name in the field.
	 */
	public IPath getContainerFullPath() {
		return containerGroup.getContainerFullPath();
	}

	/**
	 * Returns an error message indicating the current problem with the value of a
	 * control in the group, or an empty message if all controls in the group
	 * contain valid values.
	 * 
	 * @return java.lang.String
	 */
	public String getProblemMessage() {
		return problemMessage;
	}

	/**
	 * Returns the type of problem with the value of a control in the group.
	 * 
	 * @return one of the PROBLEM_* constants
	 */
	public int getProblemType() {
		return problemType;
	}

	/**
	 * Returns a string that is the path of the currently selected container.
	 * Returns an empty string if no container has been selected.
	 */
	public String getResource() {
		return resourceNameField.getText();
	}

	/**
	 * Handles events for all controls in the group.
	 * 
	 * @param e org.eclipse.swt.widgets.Event
	 */
	public void handleEvent(Event e) {
		validateControls();
		if (client != null) {
			client.handleEvent(e);
		}
	}

	/**
	 * Sets the flag indicating whether existing resources are permitted.
	 */
	public void setAllowExistingResources(boolean value) {
		allowExistingResources = value;
	}

	/**
	 * Sets the value of this page's container.
	 * 
	 * @param path Full path to the container.
	 */
	public void setContainerFullPath(IPath path) {
		IResource initial = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		if (initial != null) {
			if (!(initial instanceof IContainer)) {
				initial = initial.getParent();
			}
			containerGroup.setSelectedContainer((IContainer) initial);
		}
		validateControls();
	}

	/**
	 * Gives focus to the resource name field and selects its contents
	 */
	public void setFocus() {
		// select the whole resource name.
		resourceNameField.setSelection(0, resourceNameField.getText().length());
		resourceNameField.setFocus();
	}

	/**
	 * Sets the value of this page's resource name.
	 * 
	 * @param value new value
	 */
	public void setResource(String value) {
		resourceNameField.setText(value);
		validateControls();
	}

	/**
	 * Returns a <code>boolean</code> indicating whether a container name represents
	 * a valid container resource in the workbench. An error message is stored for
	 * future reference if the name does not represent a valid container.
	 * 
	 * @return <code>boolean</code> indicating validity of the container name
	 */
	protected boolean validateContainer() {
		IPath path = containerGroup.getContainerFullPath();
		if (path == null) {
			problemType = PROBLEM_CONTAINER_EMPTY;
			problemMessage = Messages.getString("WizardSaveAsPage.FolderEmpty"); //$NON-NLS-1$
			return false;
		}
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String projectName = path.segment(0);
		if (projectName == null || !workspace.getRoot().getProject(projectName).exists()) {
			problemType = PROBLEM_PROJECT_DOES_NOT_EXIST;
			problemMessage = Messages.getString("WizardSaveAsPage.NoProject"); //$NON-NLS-1$
			return false;
		}
		// path is invalid if any prefix is occupied by a file
		IWorkspaceRoot root = workspace.getRoot();
		while (path.segmentCount() > 1) {
			if (root.getFile(path).exists()) {
				problemType = PROBLEM_PATH_OCCUPIED;
				problemMessage = Messages.getFormattedString("WizardSaveAsPage.PathOccupied", //$NON-NLS-1$
						new Object[] { path.makeRelative() });
				return false;
			}
			path = path.removeLastSegments(1);
		}
		return true;
	}

	/**
	 * Validates the values for each of the group's controls. If an invalid value is
	 * found then a descriptive error message is stored for later reference. Returns
	 * a boolean indicating the validity of all of the controls in the group.
	 */
	protected boolean validateControls() {
		// don't attempt to validate controls until they have been created
		if (containerGroup == null) {
			return false;
		}
		problemType = PROBLEM_NONE;
		problemMessage = "";//$NON-NLS-1$

		if (!validateContainer() || !validateResourceName())
			return false;

		IPath path = containerGroup.getContainerFullPath().append(resourceNameField.getText());
		return validateFullResourcePath(path);
	}

	/**
	 * Returns a <code>boolean</code> indicating whether the specified resource path
	 * represents a valid new resource in the workbench. An error message is stored
	 * for future reference if the path does not represent a valid new resource
	 * path.
	 * 
	 * @param resourcePath the path to validate
	 * @return <code>boolean</code> indicating validity of the resource path
	 */
	protected boolean validateFullResourcePath(IPath resourcePath) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		IStatus result = workspace.validatePath(resourcePath.toString(), IResource.FOLDER);
		if (!result.isOK()) {
			problemType = PROBLEM_PATH_INVALID;
			problemMessage = result.getMessage();
			return false;
		}

		if (!allowExistingResources && (workspace.getRoot().getFolder(resourcePath).exists()
				|| workspace.getRoot().getFile(resourcePath).exists())) {
			problemType = PROBLEM_RESOURCE_EXIST;
			problemMessage = Messages.getString("WizardSaveAsPage.NameExists"); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	/**
	 * Returns a <code>boolean</code> indicating whether the resource name rep-
	 * resents a valid resource name in the workbench. An error message is stored
	 * for future reference if the name does not represent a valid resource name.
	 * 
	 * @return <code>boolean</code> indicating validity of the resource name
	 */
	protected boolean validateResourceName() {
		String resourceName = resourceNameField.getText();

		if (resourceName.equals("")) {//$NON-NLS-1$
			problemType = PROBLEM_RESOURCE_EMPTY;
			problemMessage = Messages.getFormattedString("WizardSaveAsPage.EmptyName", new Object[] { resourceType }); //$NON-NLS-1$
			return false;
		}

		if (!(new Path("")).isValidPath(resourceName)) { //$NON-NLS-1$
			problemType = PROBLEM_NAME_INVALID;
			problemMessage = Messages.getFormattedString("WizardSaveAsPage.InvalidFileName", //$NON-NLS-1$
					new Object[] { resourceName });
			return false;
		}
		return true;
	}

}

class ContainerSelectionGroup extends Composite {

	// The listener to notify of events
	private Listener listener;

	// Enable user to type in new container name
	private boolean allowNewContainerName = true;

	// show all projects by default
	private boolean showClosedProjects = true;

	// Last selection made by user
	private IContainer selectedContainer;

	// handle on parts
	private Text containerNameField;

	TreeViewer treeViewer;

	// the message to display at the top of this dialog
	private static final String DEFAULT_MSG_NEW_ALLOWED = Messages.getString("WizardSaveAsPage.ContainerGroup"); //$NON-NLS-1$

	private static final String DEFAULT_MSG_SELECT_ONLY = Messages.getString("WizardSaveAsPage.SelectFolder"); //$NON-NLS-1$

	// sizing constants
	private static final int SIZING_SELECTION_PANE_WIDTH = 320;

	private static final int SIZING_SELECTION_PANE_HEIGHT = 300;

	/**
	 * Creates a new instance of the widget.
	 * 
	 * @param parent                The parent widget of the group.
	 * @param listener              A listener to forward events to. Can be null if
	 *                              no listener is required.
	 * @param allowNewContainerName Enable the user to type in a new container name
	 *                              instead of just selecting from the existing
	 *                              ones.
	 */
	public ContainerSelectionGroup(Composite parent, Listener listener, boolean allowNewContainerName) {
		this(parent, listener, allowNewContainerName, null);
	}

	/**
	 * Creates a new instance of the widget.
	 * 
	 * @param parent                The parent widget of the group.
	 * @param listener              A listener to forward events to. Can be null if
	 *                              no listener is required.
	 * @param allowNewContainerName Enable the user to type in a new container name
	 *                              instead of just selecting from the existing
	 *                              ones.
	 * @param message               The text to present to the user.
	 */
	public ContainerSelectionGroup(Composite parent, Listener listener, boolean allowNewContainerName, String message) {
		this(parent, listener, allowNewContainerName, message, true);
	}

	/**
	 * Creates a new instance of the widget.
	 * 
	 * @param parent                The parent widget of the group.
	 * @param listener              A listener to forward events to. Can be null if
	 *                              no listener is required.
	 * @param allowNewContainerName Enable the user to type in a new container name
	 *                              instead of just selecting from the existing
	 *                              ones.
	 * @param message               The text to present to the user.
	 * @param showClosedProjects    Whether or not to show closed projects.
	 */
	public ContainerSelectionGroup(Composite parent, Listener listener, boolean allowNewContainerName, String message,
			boolean showClosedProjects) {
		this(parent, listener, allowNewContainerName, message, showClosedProjects, SIZING_SELECTION_PANE_HEIGHT);
	}

	/**
	 * Creates a new instance of the widget.
	 * 
	 * @param parent                The parent widget of the group.
	 * @param listener              A listener to forward events to. Can be null if
	 *                              no listener is required.
	 * @param allowNewContainerName Enable the user to type in a new container name
	 *                              instead of just selecting from the existing
	 *                              ones.
	 * @param message               The text to present to the user.
	 * @param showClosedProjects    Whether or not to show closed projects.
	 * @param heightHint            height hint for the drill down composite
	 */
	public ContainerSelectionGroup(Composite parent, Listener listener, boolean allowNewContainerName, String message,
			boolean showClosedProjects, int heightHint) {
		super(parent, SWT.NONE);
		this.listener = listener;
		this.allowNewContainerName = allowNewContainerName;
		this.showClosedProjects = showClosedProjects;
		if (message != null)
			createContents(message, heightHint);
		else if (allowNewContainerName)
			createContents(DEFAULT_MSG_NEW_ALLOWED, heightHint);
		else
			createContents(DEFAULT_MSG_SELECT_ONLY, heightHint);
	}

	/**
	 * The container selection has changed in the tree view. Update the container
	 * name field value and notify all listeners.
	 */
	public void containerSelectionChanged(IContainer container) {
		selectedContainer = container;

		if (allowNewContainerName) {
			if (container == null)
				containerNameField.setText("");//$NON-NLS-1$
			else
				containerNameField.setText(container.getFullPath().makeRelative().toString());
		}

		// fire an event so the parent can update its controls
		if (listener != null) {
			Event changeEvent = new Event();
			changeEvent.type = SWT.Selection;
			changeEvent.widget = this;
			listener.handleEvent(changeEvent);
		}
	}

	/**
	 * Creates the contents of the composite.
	 */
	public void createContents(String message) {
		createContents(message, SIZING_SELECTION_PANE_HEIGHT);
	}

	/**
	 * Creates the contents of the composite.
	 * 
	 * @param heightHint height hint for the drill down composite
	 */
	public void createContents(String message, int heightHint) {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		setLayout(layout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(this, SWT.WRAP);
		label.setText(message);
		label.setFont(this.getFont());

		if (allowNewContainerName) {
			containerNameField = new Text(this, SWT.SINGLE | SWT.BORDER);
			containerNameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			containerNameField.addListener(SWT.Modify, listener);
			containerNameField.setFont(this.getFont());
		} else {
			// filler...
			new Label(this, SWT.NONE);
		}

		createTreeViewer(heightHint);
		Dialog.applyDialogFont(this);
	}

	/**
	 * Returns a new drill down viewer for this dialog.
	 * 
	 * @param heightHint height hint for the drill down composite
	 * @return a new drill down viewer
	 */
	protected void createTreeViewer(int heightHint) {
		// Create drill down.
		DrillDownComposite drillDown = new DrillDownComposite(this, SWT.BORDER);
		GridData spec = new GridData(SWT.FILL, SWT.FILL, true, true);
		spec.widthHint = SIZING_SELECTION_PANE_WIDTH;
		spec.heightHint = heightHint;
		drillDown.setLayoutData(spec);

		// Create tree viewer inside drill down.
		treeViewer = new TreeViewer(drillDown, SWT.NONE);
		drillDown.setChildTree(treeViewer);
		ContainerContentProvider cp = new ContainerContentProvider();
		cp.showClosedProjects(showClosedProjects);
		treeViewer.setContentProvider(cp);
		treeViewer.setLabelProvider(WorkbenchLabelProvider.getDecoratingWorkbenchLabelProvider());
		treeViewer.setSorter(new ViewerSorter());
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				containerSelectionChanged((IContainer) selection.getFirstElement()); // allow
				// null
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object item = ((IStructuredSelection) selection).getFirstElement();
					if (treeViewer.getExpandedState(item))
						treeViewer.collapseToLevel(item, 1);
					else
						treeViewer.expandToLevel(item, 1);
				}
			}
		});

		// This has to be done after the viewer has been laid out
		treeViewer.setInput(ResourcesPlugin.getWorkspace());
	}

	/**
	 * Returns the currently entered container name. Null if the field is empty.
	 * Note that the container may not exist yet if the user entered a new container
	 * name in the field.
	 */
	public IPath getContainerFullPath() {
		if (allowNewContainerName) {
			String pathName = containerNameField.getText();
			if (pathName == null || pathName.length() < 1)
				return null;
			else
				// The user may not have made this absolute so do it for them
				return (new Path(pathName)).makeAbsolute();
		} else {
			if (selectedContainer == null)
				return null;
			else
				return selectedContainer.getFullPath();
		}
	}

	/**
	 * Gives focus to one of the widgets in the group, as determined by the group.
	 */
	public void setInitialFocus() {
		if (allowNewContainerName)
			containerNameField.setFocus();
		else
			treeViewer.getTree().setFocus();
	}

	/**
	 * Sets the selected existing container.
	 */
	public void setSelectedContainer(IContainer container) {
		selectedContainer = container;

		// expand to and select the specified container
		List itemsToExpand = new ArrayList();
		IContainer parent = container.getParent();
		while (parent != null) {
			itemsToExpand.add(0, parent);
			parent = parent.getParent();
		}
		treeViewer.setExpandedElements(itemsToExpand.toArray());
		treeViewer.setSelection(new StructuredSelection(container), true);
	}
}

class ContainerContentProvider implements ITreeContentProvider {

	private boolean showClosedProjects = true;

	/**
	 * Creates a new ContainerContentProvider.
	 */
	public ContainerContentProvider() {
	}

	/**
	 * The visual part that is using this content provider is about to be disposed.
	 * Deallocate all allocated SWT resources.
	 */
	public void dispose() {
	}

	/*
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.
	 * Object)
	 */
	public Object[] getChildren(Object element) {
		if (element instanceof IWorkspace) {
			// check if closed projects should be shown
			IProject[] allProjects = ((IWorkspace) element).getRoot().getProjects();
			if (showClosedProjects)
				return allProjects;

			ArrayList accessibleProjects = new ArrayList();
			for (int i = 0; i < allProjects.length; i++) {
				if (allProjects[i].isOpen()) {
					accessibleProjects.add(allProjects[i]);
				}
			}
			return accessibleProjects.toArray();
		} else if (element instanceof IContainer) {
			IContainer container = (IContainer) element;
			if (container.isAccessible()) {
				try {
					List children = new ArrayList();
					IResource[] members = container.members();
					for (int i = 0; i < members.length; i++) {
						if (members[i].getType() != IResource.FILE) {
							children.add(members[i]);
						}
					}
					return children.toArray();
				} catch (CoreException e) {
					// this should never happen because we call #isAccessible
					// before invoking #members
				}
			}
		}
		return new Object[0];
	}

	/*
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	public Object[] getElements(Object element) {
		return getChildren(element);
	}

	/*
	 * @see
	 * org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object )
	 */
	public Object getParent(Object element) {
		if (element instanceof IResource)
			return ((IResource) element).getParent();
		return null;
	}

	/*
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.
	 * Object)
	 */
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	/*
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/**
	 * Specify whether or not to show closed projects in the tree viewer. Default is
	 * to show closed projects.
	 * 
	 * @param show boolean if false, do not show closed projects in the tree
	 */
	public void showClosedProjects(boolean show) {
		showClosedProjects = show;
	}

}
