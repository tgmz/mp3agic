package com.mpatric.mp3agic;

import org.apache.commons.lang3.StringUtils;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;

	public BaseException() {
		super();
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public String getDetailedMessage() {
		Throwable t = this;
		StringBuilder s = new StringBuilder();
		while (true) {
			s.append('[');
			s.append(t.getClass().getName());
			if (StringUtils.isNotEmpty(t.getMessage())) {
				s.append(": ");
				s.append(t.getMessage());
			}
			s.append(']');
			t = t.getCause();
			if (t != null) {
				s.append(" caused by ");
			} else {
				break;
			}
		}
		return s.toString();
	}
}
