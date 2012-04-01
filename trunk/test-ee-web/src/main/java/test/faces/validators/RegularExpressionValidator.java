package test.faces.validators;

import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class RegularExpressionValidator implements Validator, StateHolder {
	public static final String VALIDATOR_ID = "RegularExpressionValidator";
	private String expression;
	private String errorMessage;

	private boolean transientFlag;
	protected Pattern pattern;

	public RegularExpressionValidator() {
		this("\\w+", "It's not a word character");
	}

	public RegularExpressionValidator(String expression, String errorMessage) {
		super();
		transientFlag = false;
		setExpression(expression);
		setErrorMessage(errorMessage);
	}

	// Validator methods

	public void validate(FacesContext context, UIComponent component, Object inputValue) {
		String value = null;
		try {
			value = inputValue.toString();
		} catch (ClassCastException e) {
			throw new ValidatorException(new FacesMessage("Validation Error: Value can not be converted to String.", null));
		}
		if (!isValid(value)) {
			String messageText = errorMessage;
			if (messageText == null) {
				messageText = "This field is not formatted properly.";
			}
			throw new ValidatorException(new FacesMessage("Validation Error", messageText));
		}
	}

	// Protected methods

	protected boolean isValid(String value) {
		if (pattern == null) {
			throw new NullPointerException("The expression property hasn't been set.");
		}

		return pattern.matcher(value).matches();
	}

	// Properties

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
		if (expression != null) {
			pattern = Pattern.compile(expression);
		}
	}

	// StateHolder methods

	public void setTransient(boolean transientValue) {
		this.transientFlag = transientValue;
	}

	public boolean isTransient() {
		return transientFlag;
	}

	public Object saveState(FacesContext context) {
		Object[] values = new Object[3];
		values[0] = pattern;
		values[1] = expression;
		values[2] = errorMessage;

		return values;
	}

	public void restoreState(FacesContext context, Object state) {
		Object[] values = (Object[]) state;
		pattern = (Pattern) values[0];
		expression = (String) values[1];
		errorMessage = (String) values[2];
	}
}
