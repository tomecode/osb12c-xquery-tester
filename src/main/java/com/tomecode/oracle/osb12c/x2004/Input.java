package com.tomecode.oracle.osb12c.x2004;

import java.io.File;

import com.tomecode.oracle.osb12c.Utils;

/**
 * 
 * @author Tome
 *
 */
public final class Input {

	private final String xquery;
	private File file;

	public Input(File file) {
		this.file = file;
		this.xquery = Utils.getContent(file);
	}

	public final String getXquery() {
		return xquery;
	}

	public final File getFile() {
		return file;
	}

}
