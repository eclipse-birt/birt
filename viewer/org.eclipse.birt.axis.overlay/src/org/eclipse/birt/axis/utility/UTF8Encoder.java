/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.eclipse.birt.axis.utility;

import java.io.IOException;
import java.io.Writer;

import org.apache.axis.components.encoding.AbstractXMLEncoder;
import org.apache.axis.components.encoding.XMLEncoderFactory;
import org.apache.axis.i18n.Messages;

/**
 * UTF-8 Encoder. <br>
 * <br>
 * <b>This is a mod version over original axis implementation to handle the
 * Unicode Extension-B cases. May need update when Axis changes.</b>
 * 
 * @author <a href="mailto:jens@void.fm">Jens Schumann</a>
 * @see <a href="http://encoding.org">encoding.org</a>
 * @see <a href="http://czyborra.com/utf/#UTF-8">UTF 8 explained</a>
 */
public class UTF8Encoder extends AbstractXMLEncoder {

	/**
	 * gets the encoding supported by this encoder
	 * 
	 * @return string
	 */
	public String getEncoding() {
		return XMLEncoderFactory.ENCODING_UTF_8;
	}

	/**
	 * write the encoded version of a given string
	 * 
	 * @param writer    writer to write this string to
	 * @param xmlString string to be encoded
	 */
	public void writeEncoded(Writer writer, String xmlString) throws IOException {
		if (xmlString == null) {
			return;
		}
		int length = xmlString.length();
		char c;
		for (int i = 0; i < length; i++) {
			c = xmlString.charAt(i);
			switch (c) {
			// we don't care about single quotes since axis will
			// use double quotes anyway
			case '&':
				writer.write(AMP);
				break;
			case '"':
				writer.write(QUOTE);
				break;
			case '<':
				writer.write(LESS);
				break;
			case '>':
				writer.write(GREATER);
				break;
			case '\n':
				writer.write(LF);
				break;
			case '\r':
				writer.write(CR);
				break;
			case '\t':
				writer.write(TAB);
				break;
			default:
				if (c < 0x20) {
					throw new IllegalArgumentException(Messages.getMessage("invalidXmlCharacter00", //$NON-NLS-1$
							Integer.toHexString(c), xmlString.substring(0, i)));
				} else if ((c > 0xd7ff && c < 0xdc00) && (i + 1) < length) {
					// handle Unicode Extension-B cases

					i++;

					char nc = xmlString.charAt(i);

					if (nc > 0xdbff && nc < 0xe000) {
						// surrogates matched, must be character >= 0x10000

						int rc = ((c - 0xd7c0) << 10) | (nc & 0x3ff);

						writer.write("&#x"); //$NON-NLS-1$
						writer.write(Integer.toHexString(rc).toUpperCase());
						writer.write(";"); //$NON-NLS-1$
					} else {
						writeChar(writer, c);
						writeChar(writer, nc);
					}
				} else {
					writeChar(writer, c);
				}
				break;
			}
		}
	}

	private void writeChar(Writer writer, char c) throws IOException {
		if (c > 0x7F) {
			writer.write("&#x"); //$NON-NLS-1$
			writer.write(Integer.toHexString(c).toUpperCase());
			writer.write(";"); //$NON-NLS-1$
		} else {
			writer.write(c);
		}
	}
}
