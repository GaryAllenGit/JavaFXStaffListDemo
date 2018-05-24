package staffListDemo;

import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.FXCollections;

public class StaffList extends ObservableListWrapper<Employee> {

	public StaffList() {
		super(FXCollections.observableArrayList());
	}

	public void addEmployee(String id, String  firstname, String surname, String dept){
		super.add(new Employee(id, firstname, surname, dept));
	}

	public Employee findEmployeeByName(String name){
		Employee temp;
		int indexLocation = -1;
		for (int i = 0; i < super.size(); i++) {
			temp = super.get(i);
			if (temp.getSurname().equals(name)){
				indexLocation = i;
				break;
			}
		}

		if (indexLocation == -1) {
			return null;
		} else {
			return super.get(indexLocation);
		}
	}

	public Employee findEmployeeByID(String id){
		Employee temp;
		int indexLocation = -1;
		for (int i = 0; i < super.size(); i++) {
			temp = super.get(i);
			if (temp.getID().equals(id)){
				indexLocation = i;
				break;
			}
		}

		if (indexLocation == -1) {
			return null;
		} else {
			return super.get(indexLocation);
		}        
	}

	public void removeEmployee(String id){
		Employee empToGo = this.findEmployeeByID(id);
		super.remove(empToGo);
	}
}

