package test.faces.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import test.faces.bean.model.Name;

public class NameConverter implements Converter, StateHolder {
	public final static String CONVERTER_ID = "NameConverter";
	private boolean transientFlag;
	private StyleType style;

	public NameConverter() {
		this(StyleType.F_L);
	}

	public NameConverter(StyleType style) {
		this.style = style;
	}

	// Converter methods
	public Object getAsObject(FacesContext context, UIComponent component, String displayString) throws ConverterException {
		String[] strs = displayString.split("\\s");
		
		if (strs.length > 2) {
			throw new ConverterException(new FacesMessage("Conversion error", "Illegal name string format. length=" + strs.length));
		}

		Name name = new Name();
		
		if (strs.length == 0) {
			return name;
		}
		
		if (strs.length == 1) {
			name.setFirstName(strs[0]);
			return name;
		}
		
		if (style == StyleType.F_L) {
			name.setFirstName(strs[0]);
			name.setLastName(strs[1]);
		} else if (style == StyleType.L_F) {
			name.setLastName(strs[0]);
			name.setFirstName(strs[1]);
		} else {
			throw new ConverterException(new FacesMessage("Conversion error", "Illegal style value. style=" + style));
		}
		
		return name;
	}

	public String getAsString(FacesContext context, UIComponent component, Object object) throws ConverterException {
		Name name = (Name) object;
		
		if (name.getFirstName() == null && name.getLastName() == null) {
			return "";
		} else if (name.getFirstName() == null) {
			return name.getLastName();
		} else if (name.getLastName() == null) {
			return name.getFirstName();
		}
		
		if (style == StyleType.F_L) {
			return name.getFirstName() + " " + name.getLastName();
		} else if (style == StyleType.L_F) {
			return name.getLastName() + " " + name.getFirstName();
		} else {
			throw new ConverterException(new FacesMessage("Conversion error", "Illegal style value. style=" + style));
		}
	}

	// Properties
	public StyleType getStyle() {
		return style;
	}

	public void setStyle(StyleType style) {
		this.style = style;
	}

	// StateHolder
	public boolean isTransient() {
		return transientFlag;
	}

	public void setTransient(boolean transientFlag) {
		this.transientFlag = transientFlag;
	}

	public Object saveState(FacesContext context) {
		return style;
	}

	public void restoreState(FacesContext context, Object state) {
		style = (StyleType) state;
	}

	public enum StyleType {
		F_L, L_F
	}
}
