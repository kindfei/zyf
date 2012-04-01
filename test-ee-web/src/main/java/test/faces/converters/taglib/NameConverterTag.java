package test.faces.converters.taglib;

import javax.faces.convert.Converter;
import javax.faces.webapp.ConverterTag;

import test.faces.converters.NameConverter;
import test.faces.converters.NameConverter.StyleType;

public class NameConverterTag extends ConverterTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String style;

	public NameConverterTag() {
		super();
		setConverterId(NameConverter.CONVERTER_ID);
	}

	protected Converter createConverter() throws javax.servlet.jsp.JspException {
		NameConverter converter = (NameConverter) super.createConverter();

		if (style != null) {
			converter.setStyle(StyleType.valueOf(style));
		}

		return converter;
	}

	public void release() {
		super.release();
		style = null;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
