package com.tomecode.oracle.osb12c.xpath;

import java.util.UUID;

public class UUIDUserFunction {

	public static String uuid() {
		return UUID.randomUUID().toString();
	}

}
