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

package org.eclipse.birt.report.viewer.utilities;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

/**
 * Utility class to append error/fatal/warning/info message into Problems View.
 */
public class MarkerUtil {

	/**
	 * Viewer Problem marker id
	 */
	public static final String PROBLEMS_MARKER_ID = "org.eclipse.birt.report.viewer.ViewerProblemMarker"; //$NON-NLS-1$

	/**
	 * Attribute defined for designer
	 */
	public static final String ELEMENT_ID = "ElementId"; //$NON-NLS-1$

	/**
	 * Current workspace root
	 */
	private static IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

	/**
	 * Add marker
	 * 
	 * @param systemId
	 * @param message
	 * @param elementId
	 * @param lineNumber
	 * @param severity
	 * @param priority
	 * @throws CoreException
	 */
	public static void addMarker(String systemId, String message, long elementId, int lineNumber, int severity,
			int priority) throws CoreException {
		IResource resource = createResourceFromSystemID(systemId);
		addMarker(resource, message, elementId, lineNumber, severity, priority);
	}

	/**
	 * Add marker
	 * 
	 * @param resource
	 * @param message
	 * @param elementId
	 * @param lineNumber
	 * @param severity
	 * @param priority
	 * @throws CoreException
	 */
	public static void addMarker(IResource resource, String message, long elementId, int lineNumber, int severity,
			int priority) throws CoreException {
		if (resource != null) {
			IMarker marker = resource.createMarker(PROBLEMS_MARKER_ID);
			if (message != null)
				marker.setAttribute(IMarker.MESSAGE, message);
			if (lineNumber >= 0)
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			if (elementId > 0)
				marker.setAttribute(ELEMENT_ID, Integer.valueOf((int) elementId));

			marker.setAttribute(IMarker.SEVERITY, severity);
			marker.setAttribute(IMarker.PRIORITY, priority);
		}
	}

	/**
	 * Delete all the problem markers related with current resource
	 * 
	 * @param systemId
	 * @throws CoreException
	 */
	public static void clear(String systemId) throws CoreException {
		IResource resource = createResourceFromSystemID(systemId);
		clear(resource);
	}

	/**
	 * Delete all the problem markers related with current resource
	 * 
	 * @param resource
	 * @throws CoreException
	 */
	public static void clear(IResource resource) throws CoreException {
		if (resource != null) {
			resource.deleteMarkers(PROBLEMS_MARKER_ID, true, IResource.DEPTH_INFINITE);
		}
	}

	/**
	 * Add Error Marker
	 * 
	 * @param systemId
	 * @param message
	 * @param elementId
	 * @param lineNumber
	 * @throws CoreException
	 */
	public static void error(String systemId, String message, long elementId, int lineNumber) throws CoreException {
		addMarker(systemId, message, elementId, lineNumber, IMarker.SEVERITY_ERROR, IMarker.PRIORITY_NORMAL);
	}

	/**
	 * Add Fatal Marker
	 * 
	 * @param systemId
	 * @param message
	 * @param elementId
	 * @param lineNumber
	 * @throws CoreException
	 */
	public static void fatal(String systemId, String message, long elementId, int lineNumber) throws CoreException {
		addMarker(systemId, message, elementId, lineNumber, IMarker.SEVERITY_ERROR, IMarker.PRIORITY_HIGH);
	}

	/**
	 * Add Warning Marker
	 * 
	 * @param systemId
	 * @param message
	 * @param elementId
	 * @param lineNumber
	 * @throws CoreException
	 */
	public static void warning(String systemId, String message, long elementId, int lineNumber) throws CoreException {
		addMarker(systemId, message, elementId, lineNumber, IMarker.SEVERITY_WARNING, IMarker.PRIORITY_LOW);
	}

	/**
	 * Add Info Marker
	 * 
	 * @param systemId
	 * @param message
	 * @param elementId
	 * @param lineNumber
	 * @throws CoreException
	 */
	public static void info(String systemId, String message, long elementId, int lineNumber) throws CoreException {
		addMarker(systemId, message, elementId, lineNumber, IMarker.SEVERITY_INFO, IMarker.PRIORITY_NORMAL);
	}

	/**
	 * create a resource instance from a system identifier
	 * 
	 * @param systemID system identifier
	 */
	public static IResource createResourceFromSystemID(String systemID) {
		IPath path = new Path(systemID);
		IResource resource = workspaceRoot.getFileForLocation(path);
		if (resource == null)
			resource = workspaceRoot.getContainerForLocation(path);

		return resource;
	}
}