package test.faces.validators.taglib;

import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

import test.faces.validators.RegularExpressionValidator;

public class RegExpValidatorTag extends ValidatorTag {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String expression;
	private String errorMessage;

	public RegExpValidatorTag() {
		super();
		setValidatorId(RegularExpressionValidator.VALIDATOR_ID);
	}

	// ValidatorTag methods

	protected Validator createValidator() throws JspException {
		RegularExpressionValidator validator = (RegularExpressionValidator) super.createValidator();

		if (expression != null) {
			validator.setExpression(expression);
		}
		if (errorMessage != null) {
			validator.setErrorMessage(errorMessage);
		}

		return validator;
	}

	public void release() {
		super.release();
		expression = null;
		errorMessage = null;
	}

	// Properties

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
