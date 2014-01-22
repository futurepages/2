package org.futurepages.util.template.simpletemplate.template;

import java.io.Serializable;

/**
 *
 * @author thiago
 */
public class TemplateWritter implements Serializable {

	private StringBuilder sb;
	private boolean printNull = false;;

	public TemplateWritter() {
		sb = new StringBuilder();
	}

	public TemplateWritter(String str) {
		sb = new StringBuilder(str);
	}

	public TemplateWritter(CharSequence seq) {
		sb = new StringBuilder(seq);
	}

	public TemplateWritter(boolean printNull) {
		sb = new StringBuilder();
		this.printNull = printNull;
	}

	public TemplateWritter(String str, boolean printNull) {
		sb = new StringBuilder(str);
		this.printNull = printNull;
	}

	public TemplateWritter(CharSequence seq, boolean printNull) {
		sb = new StringBuilder(seq);
		this.printNull = printNull;
	}

	protected boolean isNull(Object obj) {
		return obj == null;
	}

	public TemplateWritter append(Object obj) {
		if (!isNull(obj)) {
			sb.append(obj);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(String str) {
		if (!isNull(str)) {
			sb.append(str);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	private TemplateWritter append(StringBuilder sb) {
		if (!isNull(sb)) {
			this.sb.append(sb);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(StringBuffer sb) {
		if (!isNull(sb)) {
			this.sb.append(sb);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(CharSequence s) {
		if (!isNull(s)) {
			sb.append(s);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(CharSequence s, int start, int end) {
		if (!isNull(s)) {
			sb.append(s, start, end);
		} else if (printNull) {
			sb.append("null");
		}

		return this;
	}

	public TemplateWritter append(char str[]) {
		if (!isNull(str)) {
			sb.append(str);
		} else if (printNull) {
			sb.append("null");
		}

		return this;
	}

	public TemplateWritter append(char str[], int offset, int len) {
		if (!isNull(str)) {
			sb.append(str, offset, len);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(boolean b) {
		if (!isNull(b)) {
			sb.append(b);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(char c) {
		if (!isNull(c)) {
			sb.append(c);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(int i) {
		if (!isNull(i)) {
			sb.append(i);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(long lng) {
		if (!isNull(lng)) {
			sb.append(lng);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(float f) {
		if (!isNull(f)) {
			sb.append(f);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	public TemplateWritter append(double d) {
		if (!isNull(d)) {
			sb.append(d);
		} else if (printNull) {
			sb.append("null");
		}
		return this;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
