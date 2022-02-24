/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * This class is used to parse the configuration file that the user specifies.
 * Note that it is an internal tool that can only parse the firmly-formatted
 * file rather than a general parser tool.
 * 
 */
public class ConfigFileParser {

	private static final String PARAMETER = "parameter";
	private static final String TIMESTAMP_FORMAT = "timestamp-format";
	private static final String TIMESTAMP_COLUMN = "timestamp-column";
	private static final String MODE = "mode";
	private static final String QUERY_TEXT = "query-text";
	private static final String ID = "id";
	private Node node;

	private static Logger logger = Logger.getLogger(ConfigFileParser.class.getName());

	// private InputStream stream;

	public ConfigFileParser(InputStream stream) {
		node = this.parseXML2DOM(stream);
	}

	/**
	 * This method is to check whether the current tree contains the specified data
	 * set node.
	 * 
	 * @param String id the data set name
	 * @return true if the current tree contains the specified data set
	 */
	public boolean containDataSet(String id) {
		ArrayList children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node temp = (Node) children.get(i);
			HashMap attributes = temp.getAttributes();
			if (attributes.containsKey(ID) && attributes.get(ID).equals(id)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is to check whether the current tree contains the specified data
	 * set node.
	 * 
	 * @param String id the data set name
	 * @return the parsed query text string
	 */
	public String getQueryTextByID(String id) throws DataException {
		ArrayList children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node temp = (Node) children.get(i);
			HashMap attributes = temp.getAttributes();
			if (attributes.containsKey(ID) && attributes.get(ID).equals(id)) {
				children = temp.getChildren();
				for (int j = 0; j < children.size(); j++) {
					temp = (Node) children.get(j);
					if (temp.getName().equalsIgnoreCase(QUERY_TEXT)) {
						return temp.getValue();
					}
				}
			}
		}
		throw new DataException(ResourceConstants.CONFIG_FILE_PARSER_QUERYTEXT_FAIL);
	}

	public String getModeByID(String id) throws DataException {
		ArrayList children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node temp = (Node) children.get(i);
			HashMap attributes = temp.getAttributes();
			if (attributes.containsKey(ID) && attributes.get(ID).equals(id)) {
				children = temp.getChildren();
				for (int j = 0; j < children.size(); j++) {
					temp = (Node) children.get(j);
					if (temp.getName().equalsIgnoreCase(MODE)) {
						return temp.getValue();
					}
				}
			}
		}
		throw new DataException(ResourceConstants.CONFIG_FILE_PARSER_MODE_FAIL);
	}

	/**
	 * This method is to get the timestamp column name by the specified data set
	 * name.
	 * 
	 * @param String id the data set name
	 * @return the parsed timestamp column name
	 */
	public String getTimeStampColumnByID(String id) throws DataException {
		ArrayList children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node temp = (Node) children.get(i);
			HashMap attributes = temp.getAttributes();
			if (attributes.containsKey(ID) && attributes.get(ID).equals(id)) {
				children = temp.getChildren();
				for (int j = 0; j < children.size(); j++) {
					temp = (Node) children.get(j);
					if (temp.getName().equals(TIMESTAMP_COLUMN)) {
						return temp.getValue();
					}
				}
			}
		}
		throw new DataException(ResourceConstants.CONFIG_FILE_PARSER_TIMESTAMP_COLUMN_FAIL);
	}

	/**
	 * This method is to get the timestamp format string by the specified data set
	 * name.
	 * 
	 * @param String id the data set name
	 * @return the parsed timestamp format string
	 */
	public String getTSFormatByID(String id) throws DataException {
		ArrayList children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node temp = (Node) children.get(i);
			HashMap attributes = temp.getAttributes();
			if (attributes.containsKey(ID) && attributes.get(ID).equals(id)) {
				children = temp.getChildren();
				for (int j = 0; j < children.size(); j++) {
					temp = (Node) children.get(j);
					if (temp.getName().equals(TIMESTAMP_FORMAT)) {
						return temp.getValue();
					}
				}
			}
		}
		throw new DataException(ResourceConstants.CONFIG_FILE_PARSER_TIMESTAMP_FORMAT_FAIL);
	}

	/**
	 * This method is to get the parameters by the specified data set name.
	 * 
	 * @param String id the data set name
	 * @return a HashMap that contains the parsed parameter pairs
	 */
	public HashMap getParametersByID(String id) {
		HashMap params = new HashMap();
		ArrayList children = node.getChildren();
		for (int i = 0; i < children.size(); i++) {
			Node temp = (Node) children.get(i);
			HashMap attributes = temp.getAttributes();
			if (attributes.containsKey(ID) && attributes.get(ID).equals(id)) {
				children = temp.getChildren();
				for (int j = 0; j < children.size(); j++) {
					temp = (Node) children.get(j);
					if (temp.getName().equals(PARAMETER)) {
						children = temp.getChildren();
						break;
					}
				}
			}
		}
		for (int i = 0; i < children.size(); i++) {
			Node temp = (Node) children.get(i);
			params.put(temp.getName(), temp.getValue());
		}
		return params;
	}

	/**
	 * This method is to parse the InputStream's information to a Node.
	 * 
	 * @param String xml string parsed from the configuration file
	 * @return Node that stores all the useful information in the configuration file
	 */
	private Node parseXML2DOM(InputStream stream) {
		StringBuffer buffer = new StringBuffer();

		int c;
		try {
			while ((c = stream.read()) != -1) {
				buffer.append((char) c);
			}
			return parseXML2DOM(buffer.toString());
		} catch (IOException e) {
			logger.log(Level.FINE, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Get the current Node instance.
	 * 
	 * @return Node instance
	 */
	public Node getNode() {
		return this.node;
	}

	/**
	 * This method is to parse the xml-formatted string to a Node.
	 * 
	 * @param String xml-formatted string parsed from the configuration file
	 * @return Node that stores all the useful information in the configuration file
	 */
	private Node parseXML2DOM(String xml) {
		Node node = new Node();
		StringBuffer buffer = new StringBuffer();
		Stack stack = new Stack();
		ArrayList children = new ArrayList();
		Object obj;
		boolean flag = false;
		boolean reserved = false;
		char ch;
		for (int i = 0; i < xml.length(); i++) {
			ch = xml.charAt(i);
			if (reserved) {
				if (ch == ']') {
					String temp = xml.substring(i, i + 3);
					if (temp.equals("]]>")) {
						reserved = false;
						stack.push(buffer.toString().trim());
						buffer = new StringBuffer();
						flag = false;
						i = i + 2;
					}
				} else {
					buffer.append(ch);
				}
				continue;
			} else if (ch == '\n' || ch == '\t') {
				continue;
			} else if (ch == '<') {
				if (flag == true) {
					return null;
				}
				String temp = xml.substring(i, i + 9);
				if (temp.equals("<![CDATA[")) {
					reserved = true;
					i = i + 8;
					continue;
				}
				if (buffer.toString().trim().length() > 0) {
					stack.push(buffer.toString().trim());
					buffer = new StringBuffer();
				}
				buffer.append(ch);
				flag = true;
			} else if (ch == '>') {
				if (flag == false) {
					return null;
				}
				flag = false;
				String temp = buffer.toString().trim();
				buffer = new StringBuffer();
				if (temp.startsWith("</")) {
					temp = temp.substring(2).trim();
					if (stack.isEmpty()) {
						return null;
					}
					obj = stack.pop();
					if (obj instanceof Node) {
						children.add((Node) obj);
						while (!stack.isEmpty()) {
							if ((obj = stack.pop()) instanceof Node) {
								children.add((Node) obj);
							} else {
								break;
							}
						}
					}
					if (obj instanceof String) {
						int time = 0;
						String next = (String) obj;
						// String nodeValue = "";
						// continue to check the next element
						if (next.startsWith("</")) {
							return null;
						}
						if (!next.startsWith("<") && !next.endsWith(">")) {
							if (time == 1) {
								return null;
							}
							// nodeValue = next;
							node.setValue(next);
							time = 1;
							if (stack.isEmpty()) {
								return null;
							}
							obj = stack.pop();
							if (obj instanceof String) {
								next = (String) obj;
							} else {
								return null;
							}
						}
						if (next.startsWith("<") && next.endsWith(">")) {
							if (next.startsWith("<" + temp)) {
								node.setName(temp);
								next = next.substring(temp.length() + 1, next.length() - 1).trim();
								if (!next.equals("")) {
									String[] arrays = next.split("=");
									HashMap attributes = new HashMap();
									String name = arrays[0].trim();
									String value;
									for (int k = 1; k < arrays.length; k++) {
										String[] pair = arrays[k].split("\"");
										if (pair.length < 2) {
											return null;
										}
										value = pair[1];
										attributes.put(name, value);
										if (pair.length == 3) {
											name = pair[2];
										}
									}
									node.setAttributes(attributes);
									attributes = new HashMap();
								}
								if (children.size() > 0) {
									ArrayList nodeArray = new ArrayList();
									for (int j = children.size() - 1; j >= 0; j--) {
										nodeArray.add(children.get(j));
									}
									node.setChildren(nodeArray);
									children = new ArrayList();
								}
								stack.push(node);
								node = new Node();
								flag = false;
							} else {
								// invalid xml format
								return null;
							}
						}
					}
				} else if (temp.startsWith("<")) {
					stack.push(temp + ch);
				} else {
					return null;
				}
			} else {
				buffer.append(ch);
			}
		} // end for
		if (stack.size() != 1) {
			return null;
		}
		obj = stack.pop();
		if (obj instanceof Node) {
			return (Node) obj;
		}
		return null;
	}// end parseXML2DOM()

	/**
	 * An inner class serve as a data structure used to store the information parsed
	 * from a configuration file.
	 * 
	 */
	public static class Node {

		private String name;
		private String value;
		private ArrayList children;
		private HashMap attributes;

		public Node() {
			this.name = "";
			this.value = "";
			children = new ArrayList();
			attributes = new HashMap();
		}

		public Node(String name, String value) {
			this.name = name;
			this.value = value;
			children = new ArrayList();
			attributes = new HashMap();
		}

		public void addChild(Node child) {
			this.children.add(child);
		}

		/**
		 * @param children the children to set
		 */
		public void setChildren(ArrayList children) {
			this.children = children;
		}

		/**
		 * @return the children
		 */
		public ArrayList getChildren() {
			return this.children;
		}

		/**
		 * @param attributes the attributes to set
		 */
		public void setAttributes(HashMap attributes) {
			this.attributes = attributes;
		}

		/**
		 * @return the attributes
		 */
		public HashMap getAttributes() {
			return this.attributes;
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return this.value;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Find the attribute value according to the key value
		 * 
		 * @return the attribute value
		 */
		public String getAttrValue(String key) {
			if (attributes.containsKey(key))
				return (String) attributes.get(key);

			else
				return null;
		}

		/**
		 * Find the child node according to the specified node name
		 * 
		 * @return the child node
		 */
		public Node getChildByName(String name) {
			for (int i = 0; i < children.size(); i++) {
				Node temp = (Node) children.get(i);
				if (temp.getName().equals(name))
					return temp;
			}
			return null;
		}
	}

}
