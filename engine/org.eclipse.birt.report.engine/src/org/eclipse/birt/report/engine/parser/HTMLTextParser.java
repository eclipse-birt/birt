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

package org.eclipse.birt.report.engine.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

/**
 * Parse the content of text according to the HTML specification to get the DOM
 * tree.
 * <p>
 * After parsing, the DOM tree has a <code>Document</code> node that has a
 * <code>Element</code> child node whose tag name is body. All other nodes that
 * need to be processed to output are the descendant nodes of "body" node.
 * <p>
 *
 */
public class HTMLTextParser {

	/**
	 * logger used to log syntax errors.
	 */
	protected static Logger logger = Logger.getLogger(HTMLTextParser.class.getName());

	/** Supported tags in HTML */
	protected static HashSet<String> supportedTags = new HashSet<String>();
	/**
	 * Tidy instance
	 */
	protected Tidy tidy = new Tidy();
	/**
	 * Initializes and sets configuration
	 */
	protected static Properties props;

	static {
		supportedTags.add("a"); //$NON-NLS-1$
		supportedTags.add("b"); //$NON-NLS-1$
		// supportedTags.add("BODY");
		supportedTags.add("br"); //$NON-NLS-1$
		supportedTags.add("center"); //$NON-NLS-1$
		supportedTags.add("code"); //$NON-NLS-1$
		supportedTags.add("dd"); //$NON-NLS-1$
		supportedTags.add("del"); //$NON-NLS-1$
		supportedTags.add("div"); //$NON-NLS-1$
		supportedTags.add("dl"); //$NON-NLS-1$
		supportedTags.add("dt"); //$NON-NLS-1$
		supportedTags.add("font"); //$NON-NLS-1$
		supportedTags.add("em"); //$NON-NLS-1$
		// supportedTags.add("HEAD");
		// supportedTags.add("HN");
		// supportedTags.add( "html" );
		supportedTags.add("hr"); //$NON-NLS-1$
		supportedTags.add("i"); //$NON-NLS-1$
		supportedTags.add("img"); //$NON-NLS-1$
		supportedTags.add("ins"); //$NON-NLS-1$
		supportedTags.add("li"); //$NON-NLS-1$
		supportedTags.add("ol"); //$NON-NLS-1$
		supportedTags.add("pre"); //$NON-NLS-1$
		supportedTags.add("p"); //$NON-NLS-1$
		supportedTags.add("span"); //$NON-NLS-1$
		supportedTags.add("strong"); //$NON-NLS-1$
		supportedTags.add("sub"); //$NON-NLS-1$
		supportedTags.add("sup"); //$NON-NLS-1$
		// supportedTags.add("TITLE");
		supportedTags.add("ul"); //$NON-NLS-1$
		supportedTags.add("tt"); //$NON-NLS-1$
		supportedTags.add("u"); //$NON-NLS-1$

		props = new Properties();
		try {
			props.load(HTMLTextParser.class.getResourceAsStream("htmlparser.properties"));//$NON-NLS-1$
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	/** For heading level */
	private static Pattern hn = Pattern.compile("h[\\d]"); //$NON-NLS-1$

	/** whether use the supportedTags map */
	private boolean supportAllTags = true;

	/**
	 * Constructor
	 *
	 */
	public HTMLTextParser() {
		tidy.setConfigurationFromProps(props);
		supportAllTags = true;
	}

	/**
	 * Parse the HTML input stream.
	 *
	 * @param in the HTML input stream
	 * @return created DOM tree, null if any error exists.
	 */
	public Document parseHTML(InputStream in) {
		assert in != null;

		Document doc = tidy.parseDOM(in, null);

		DocumentBuilder docBuilder = DocumentBuilderPool.getDocumentBuilder();
		if (docBuilder == null) {
			return null;
		}
		Document desDoc = docBuilder.newDocument();
		DocumentBuilderPool.releaseDocumentBuilder(docBuilder);

		Node desBody = desDoc.createElement("body"); //$NON-NLS-1$
		desDoc.appendChild(desBody);
		// After parsing with JTidy,normally the children nodes of the root
		// are
		// HTML entity, HTML element and comments node. And The children
		// nodes of the
		// element HTML are Head element and Body element. Only Body element
		// and its descendant nodes are preserved.
		// Entities in raw html are converted to text.
		Node html = null;
		for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.COMMENT_NODE) {
				Comment commentNode = desBody.getOwnerDocument().createComment(child.getNodeValue());
				desBody.appendChild(commentNode);
			} else if (child.getNodeType() == Node.ELEMENT_NODE && "html".equals(child.getNodeName())) {
				html = child;
				break;
			}
		}

		if (html != null) {
			Node head = getNodeByName(html, "head"); //$NON-NLS-1$
			if (head != null) {
				for (Node child = head.getFirstChild(); child != null; child = child.getNextSibling()) {
					short nodeType = child.getNodeType();
					if (nodeType == Node.ELEMENT_NODE) {
						if ("script".equalsIgnoreCase(child.getNodeName())) {
							// copy the element node
							Element ele;
							ele = desBody.getOwnerDocument().createElement(child.getNodeName());

							// copy the attributes
							for (int i = 0; i < child.getAttributes().getLength(); i++) {
								Node attr = child.getAttributes().item(i);
								ele.setAttribute(attr.getNodeName(), attr.getNodeValue());
							}

							desBody.appendChild(ele);
							copyNode(child, ele);
						}
					} else if (nodeType == Node.COMMENT_NODE) {
						Comment commentNode = desBody.getOwnerDocument().createComment(child.getNodeValue());
						desBody.appendChild(commentNode);
					}
				}
			}
			Node body = getNodeByName(html, "body"); //$NON-NLS-1$
			if (body != null) {
				copyNode(body, desBody);
			}
		}
		return desDoc;

	}

	/**
	 * Retrieves the child node by name
	 *
	 * @param parent    the parent node
	 * @param childName the name of the child node to retrieve
	 * @return null if such node does not exist, otherwise return the specified
	 *         node.
	 */
	private Node getNodeByName(Node parent, String childName) {
		for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
			if (child.getNodeType() == Node.ELEMENT_NODE && childName.equals(child.getNodeName())) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Remove the unsupported tags and convert the JTidy DOM tree to W3C DOM tree
	 * recursively.
	 *
	 * @param srcNode the Node in the JTidy DOM tree
	 * @param desNode the Node in the W3c DOM tree
	 * @see org.w3c.dom.Node
	 */
	private void copyNode(Node srcNode, Node desNode) {
		assert srcNode != null && desNode != null;

		// In the definition of <code>org.w3c.dom.Node<code>, there are 12 kinds
		// of nodes. Here only process the text, attribute and element types.
		for (Node child = srcNode.getFirstChild(); child != null; child = child.getNextSibling()) {
			// The child node is a text node or cdata section, and create it and
			// return
			short nodeType = child.getNodeType();
			if (nodeType == Node.TEXT_NODE || nodeType == Node.CDATA_SECTION_NODE) {
				Text txtNode = desNode.getOwnerDocument().createTextNode(child.getNodeValue());
				desNode.appendChild(txtNode);
			} else if (nodeType == Node.COMMENT_NODE) {
				Comment commentNode = desNode.getOwnerDocument().createComment(child.getNodeValue());
				desNode.appendChild(commentNode);
			}
			// The child node is an element node. If it is supported, then
			// create it and call this method on the child node recursively. If
			// it is unsupported, then skip it and call this method recursively.
			else if (nodeType == Node.ELEMENT_NODE) {
				boolean bSupported = true;

				if (!supportAllTags) {
					if (!supportedTags.contains(child.getNodeName())) {
						// Check if it is not a heading level
						if (!hn.matcher(child.getNodeName()).matches()) {
							bSupported = false;
						}
					}
				}
				if (bSupported) {
					// copy the element node
					Element ele;
					ele = desNode.getOwnerDocument().createElement(child.getNodeName());

					// copy the attributes
					for (int i = 0; i < child.getAttributes().getLength(); i++) {
						Node attr = child.getAttributes().item(i);
						ele.setAttribute(attr.getNodeName(), attr.getNodeValue());
					}

					desNode.appendChild(ele);

					copyNode(child, ele);
				} else {
					copyNode(child, desNode);
				}
			}
		}
	}

	static class DocumentBuilderPool {
		private static final int MAX_POOL_SIZE = 16;
		private static BlockingQueue<DocumentBuilder> builders = new LinkedBlockingQueue<>(
				MAX_POOL_SIZE);

		public static DocumentBuilder getDocumentBuilder() {
			DocumentBuilder docBuilder = builders.poll();
			if (docBuilder == null) {
				docBuilder = newDocumentBuilder();
			}
			return docBuilder;
		}

		public static void releaseDocumentBuilder(DocumentBuilder docBuilder) {
			docBuilder.reset();
			builders.offer(docBuilder);
		}

		private static DocumentBuilder newDocumentBuilder() {
			try {
				DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				return docBuilder;
			} catch (ParserConfigurationException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				return null;
			}
		}
	}

}
