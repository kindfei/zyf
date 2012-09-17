package test.copy;

import org.apache.commons.beanutils.BeanUtils;

import test.common.bean.FullName;
import test.common.bean.Name;
import test.common.bean.People;
import test.common.bean.Person;

public class TestCopy {
	static void commonsCopy(Object dest, Object orig) throws Exception {
		BeanUtils.copyProperties(dest, orig);
		System.out.println(dest.toString());
	}
	
	static void springCopy(Object source, Object target) throws Exception {
		org.springframework.beans.BeanUtils.copyProperties(source, target);
		System.out.println(target.toString());
	}
	
	static void copy(Object obj) throws Exception {
		System.out.println(CopyUtil.copy(obj).toString());
	}
	
	public static void main(String[] args) throws Exception {
		Person person = new Person();
		person.setName(new Name("Yifei", "Zhang"));
		person.setAge(32);
		person.setHeight(1.78);
		person.setMarried(true);
		
		People people = new People();
		people.setName(new FullName("Yifei", null, "Zhang"));
		people.setAge(32);
		people.setHeight(1.78);
		people.setMarried(true);
		people.setAddress("Dalian");
		
		commonsCopy(new Person(), people);
		commonsCopy(new People(), person);
		
		springCopy(people, new Person());
		springCopy(person, new People());

		copy(people);
		copy(person);
	}
}
