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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Utility class to find a unused port on local machine.
 */
public class SocketUtil {

	private static final Random RANDOM = new Random();

	/**
	 * Returns a free port number, or -1 if none found.
	 */
	public static int findUnusedLocalPort() {
		InetAddress address;
		try {
			address = InetAddress.getByAddress(new byte[] { 127, 0, 0, 1 });
		} catch (UnknownHostException uhe) {
			return -1;
		}

		int port = findUnusedPort(address, 49152, 65535);
		if (port == -1) {
			port = findFreePort();
		}
		return port;
	}

	private static int findUnusedPort(InetAddress address, int from, int to) {
		for (int i = 0; i < 12; i++) {
			ServerSocket ss = null;
			int port = getRandomPort(from, to);
			try {
				ss = new ServerSocket();
				SocketAddress sa = new InetSocketAddress(address, port);
				ss.bind(sa);
				return ss.getLocalPort();
			} catch (IOException e) {
			} finally {
				if (ss != null) {
					try {
						ss.close();
					} catch (IOException ioe) {
					}
				}
			}
		}
		return -1;
	}

	private static int getRandomPort(int low, int high) {
		return (int) (RANDOM.nextFloat() * (high - low)) + low;
	}

	private static int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			return socket.getLocalPort();
		} catch (IOException e) {
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		return -1;
	}
}
