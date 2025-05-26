<%-----------------------------------------------------------------------------
	Copyright (c) 2025 Thomas Gutmann.

	This program and the accompanying materials are made available under the
	terms of the Eclipse Public License 2.0 which is available at
	https://www.eclipse.org/legal/epl-2.0/.

	SPDX-License-Identifier: EPL-2.0

	Contributors:
		Thomas Gutmann  - initial API and implementation
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.presentation.aggregation.IFragment,
				 org.eclipse.birt.report.resource.ResourceConstants,
				 org.eclipse.birt.report.resource.BirtResources"  %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />

<%-----------------------------------------------------------------------------
	Information dialog fragment
-----------------------------------------------------------------------------%>
<div class="message_content_slot">
	<div id="message-content">Message Content</div>	
</div>