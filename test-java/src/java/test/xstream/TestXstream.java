package test.xstream;

import test.common.bean.FullName;
import test.common.bean.Name;
import test.common.bean.People;
import test.common.bean.Person;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.mapper.MapperWrapper;

public class TestXstream {
	static void test1(Object o, Class<?> dest) {
		XStream x = new XStream();
		
		x.alias("bean", o.getClass());
		String s = x.toXML(o);
		System.out.println(s);
		
		x.alias("bean", dest);
		Object r = x.fromXML(s);
		System.out.println(r.toString());
	}
	
	static void test2(Object o, Class<?> dest) {
		XStream x = new XStream();
		
		x.alias("bean", o.getClass());
		x.omitField(People.class, "lastName");
		x.omitField(People.class, "address");
		String s = x.toXML(o);
		System.out.println(s);
		
		x.alias("bean", dest);
		Object r = x.fromXML(s);
		System.out.println(r.toString());
	}
	
	static void test3(Object o, Class<?> dest) {
		XStream x = new XStream();
		
		x.alias("bean", o.getClass());
		String s = x.toXML(o);
		System.out.println(s);
		
		x.alias("bean", dest);
		x.omitField(Person.class, "lastName");
		x.omitField(Person.class, "address");
		Object r = x.fromXML(s);
		System.out.println(r.toString());
	}
	
	static void test4(Object o, Class<?> dest) {
		XStream x = new XStream() {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {
					@SuppressWarnings("rawtypes")
					@Override
					public boolean shouldSerializeMember(Class definedIn, String fieldName) {
						if (definedIn == Object.class) {
							return false;
						}
						return super.shouldSerializeMember(definedIn, fieldName);
					}
				};
			}
		};
		
		x.alias("bean", o.getClass());
		String s = x.toXML(o);
		System.out.println(s);
		
		x.alias("bean", dest);
		Object r = x.fromXML(s);
		System.out.println(r.toString());
	}
	
	static void testGroup1() {
		Person o1 = new Person();
		o1.setName(new Name("Yifei", "Zhang"));
		o1.setAge(32);
		o1.setHeight(1.78);
		o1.setMarried(true);
		
		People o2 = new People();
		o2.setName(new FullName("Yifei", null, "Zhang"));
		o2.setAge(32);
		o2.setHeight(1.78);
		o2.setMarried(true);
		o2.setAddress("Dalian");
		
		test1(o1, People.class);
		test2(o2, Person.class);
		test3(o2, Person.class);
		test4(o2, Person.class);
	}
	
	static void testGroup2() {
		test.common.bean.serialize.Person o1 = new test.common.bean.serialize.Person();
		o1.setName(new test.common.bean.serialize.Name("Yifei", "Zhang"));
		o1.setAge(32);
		o1.setHeight(1.78);
		o1.setMarried(true);
		
		test.common.bean.serialize.People o2 = new test.common.bean.serialize.People();
		o2.setName(new test.common.bean.serialize.FullName("Yifei", null, "Zhang"));
		o2.setAge(32);
		o2.setHeight(1.78);
		o2.setMarried(true);
		o2.setAddress("Dalian");
		
		test1(o1, test.common.bean.serialize.People.class);
		test2(o2, test.common.bean.serialize.Person.class);
		test3(o2, test.common.bean.serialize.Person.class);
		test4(o2, test.common.bean.serialize.Person.class);
	}
	
	static void testGroup3() {
		test.common.bean.serialize.override.Person o1 = new test.common.bean.serialize.override.Person();
		o1.setName(new test.common.bean.serialize.override.Name("Yifei", "Zhang"));
		o1.setAge(32);
		o1.setHeight(1.78);
		o1.setMarried(true);
		
		test.common.bean.serialize.override.People o2 = new test.common.bean.serialize.override.People();
		o2.setName(new test.common.bean.serialize.override.FullName("Yifei", null, "Zhang"));
		o2.setAge(32);
		o2.setHeight(1.78);
		o2.setMarried(true);
		o2.setAddress("Dalian");
		
		test1(o1, test.common.bean.serialize.override.People.class);
		test2(o2, test.common.bean.serialize.override.Person.class);
		test3(o2, test.common.bean.serialize.override.Person.class);
		test4(o2, test.common.bean.serialize.override.Person.class);
	}
	
	static void testGroup4() {
		test.common.bean.externalize.Person o1 = new test.common.bean.externalize.Person();
		o1.setName(new test.common.bean.externalize.Name("Yifei", "Zhang"));
		o1.setAge(32);
		o1.setHeight(1.78);
		o1.setMarried(true);
		
		test.common.bean.externalize.People o2 = new test.common.bean.externalize.People();
		o2.setName(new test.common.bean.externalize.FullName("Yifei", null, "Zhang"));
		o2.setAge(32);
		o2.setHeight(1.78);
		o2.setMarried(true);
		o2.setAddress("Dalian");
		
		test1(o1, test.common.bean.externalize.People.class);
		test2(o2, test.common.bean.externalize.Person.class);
		test3(o2, test.common.bean.externalize.Person.class);
		test4(o2, test.common.bean.externalize.Person.class);
	}
	
	public static void main(String[] args) {
		testGroup1();
		testGroup2();
		testGroup3();
		testGroup4();
	}
}
