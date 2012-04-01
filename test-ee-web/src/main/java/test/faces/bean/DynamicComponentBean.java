package test.faces.bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import test.faces.bean.model.User;
import test.faces.converters.NameConverter;
import test.faces.converters.NameConverter.StyleType;

public class DynamicComponentBean {
	private final static Log log = LogFactory.getLog(DynamicComponentBean.class);
	
	private HtmlPanelGrid controlPanel;
	private List<User> users = new LinkedList<User>();
	
	private DataModel dataModel = new ListDataModel(new ArrayList<User>());
	
	public DynamicComponentBean() {
		System.out.println("Constructor: " + this.getClass().getName());
	}
	
	public HtmlPanelGrid getControlPanel() {
		return controlPanel;
	}

	public void setControlPanel(HtmlPanelGrid controlPanel) {
		this.controlPanel = controlPanel;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public DataModel getDataModel() {
		return dataModel;
	}

	public void setDataModel(DataModel dataModel) {
		this.dataModel = dataModel;
	}

	@SuppressWarnings("unchecked")
	public void addProgrammatically(ActionEvent actionEvent) {
		log.info("children.size=" + controlPanel.getChildren().size() + ", names.size=" + users.size());
		
		Application application = FacesContext.getCurrentInstance().getApplication();
		
		//
		controlPanel.setColumns(1);

		//
		int index = users.size();
		users.add(new User());

		//
		HtmlPanelGrid rowGrid = (HtmlPanelGrid) application.createComponent(HtmlPanelGrid.COMPONENT_TYPE);
		rowGrid.setColumns(4);
		
		if (index == 0) {
			HtmlOutputText nameOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
			HtmlOutputText genderOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
			HtmlOutputText ageOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
			HtmlOutputText blankOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
			
			nameOutput.setValue("Name");
			genderOutput.setValue("Gender");
			ageOutput.setValue("Age");
			
			rowGrid.getChildren().add(nameOutput);
			rowGrid.getChildren().add(genderOutput);
			rowGrid.getChildren().add(ageOutput);
			rowGrid.getChildren().add(blankOutput);
		}
		
		HtmlInputText nameInput = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		HtmlSelectOneMenu genderInput = (HtmlSelectOneMenu) application.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
		HtmlInputText ageInput = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		HtmlCommandButton removeButton = (HtmlCommandButton) application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		
		rowGrid.getChildren().add(nameInput);
		rowGrid.getChildren().add(genderInput);
		rowGrid.getChildren().add(ageInput);
		rowGrid.getChildren().add(removeButton);
		
		controlPanel.getChildren().add(rowGrid);
		
		//
		genderInput.getChildren().add(createItem("male", "male"));
		genderInput.getChildren().add(createItem("female", "female"));
		
		removeButton.setValue("Remove");
		removeButton.setActionListener(application.createMethodBinding("#{dynaCompBean.removeProgrammatically}", new Class[] {ActionEvent.class}));
		removeButton.setImmediate(true);
		removeButton.getAttributes().put("index", index);
		
		String format = "#{dynaCompBean.users[" + index + "].%s}";
		
		nameInput.setValueBinding("value", application.createValueBinding(String.format(format, "name")));
		nameInput.setConverter(new NameConverter(StyleType.L_F));
		
		genderInput.setValueBinding("value", application.createValueBinding(String.format(format, "gender")));
		
		ageInput.setValueBinding("value", application.createValueBinding(String.format(format, "age")));
	}

	private UISelectItem createItem(String value, String label) {
		Application application = FacesContext.getCurrentInstance().getApplication();
		UISelectItem item = (UISelectItem) application.createComponent(UISelectItem.COMPONENT_TYPE);
		item.setItemValue(value);
		item.setItemLabel(label);
		return item;
	}

	@SuppressWarnings("unchecked")
	public void removeProgrammatically(ActionEvent actionEvent) {
		int index = (Integer) actionEvent.getComponent().getAttributes().get("index");
		
		List<UIComponent> children = controlPanel.getChildren();
		log.info("children.size=" + children.size() + ", users.size=" + users.size() + ", index=" + index);
		
		controlPanel.getChildren().remove(index);
		users.remove(index);
		
		Application application = FacesContext.getCurrentInstance().getApplication();
		
		for (int i = index; i < children.size(); i++) {
			HtmlPanelGrid p = (HtmlPanelGrid) children.get(i);
			
			List<UIComponent> list = p.getChildren();
			
			HtmlInputText nameInput = ((HtmlInputText) list.get(0));
			HtmlSelectOneMenu genderInput = ((HtmlSelectOneMenu) list.get(1));
			HtmlInputText ageInput = ((HtmlInputText) list.get(2));
			HtmlCommandButton removeButton = ((HtmlCommandButton) list.get(3));
			
			if (i == 0) {
				HtmlOutputText nameOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
				HtmlOutputText genderOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
				HtmlOutputText ageOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
				HtmlOutputText blankOutput = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
				
				nameOutput.setValue("Name");
				genderOutput.setValue("Gender");
				ageOutput.setValue("Age");
				
				p.getChildren().add(0, nameOutput);
				p.getChildren().add(1, genderOutput);
				p.getChildren().add(2, ageOutput);
				p.getChildren().add(3, blankOutput);
				
				nameInput = ((HtmlInputText) list.get(4));
				genderInput = ((HtmlSelectOneMenu) list.get(5));
				ageInput = ((HtmlInputText) list.get(6));
				removeButton = ((HtmlCommandButton) list.get(7));
			}
			
			String format = "#{dynaCompBean.users[" + i + "].%s}";
			
			nameInput.setValueBinding("value", application.createValueBinding(String.format(format, "name")));
			genderInput.setValueBinding("value", application.createValueBinding(String.format(format, "gender")));
			ageInput.setValueBinding("value", application.createValueBinding(String.format(format, "age")));
			removeButton.getAttributes().put("index", i);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addDataTableRow(ActionEvent actionEvent) {
		List<User> users = (List<User>) dataModel.getWrappedData();
		users.add(new User());
	}
	
	@SuppressWarnings("unchecked")
	public void removeDataTableRow(ActionEvent actionEvent) {
		List<User> users = (List<User>) dataModel.getWrappedData();
		users.remove(dataModel.getRowIndex());
	}

	public String result() {
		controlPanel.getChildren().clear();
		return "dcr";
	}

	public String back() {
		return "dc";
	}
}
