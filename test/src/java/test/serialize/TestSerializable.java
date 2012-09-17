package test.serialize;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.CodeAdapter;
import org.objectweb.asm.CodeVisitor;

import test.common.bean.serialize.FullName;
import test.common.bean.serialize.Name;
import test.common.bean.serialize.People;
import test.common.bean.serialize.Person;

public class TestSerializable {
	
	public static void main(String[] args) throws Exception {
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ServerSocket ss = new ServerSocket(2828);
					System.out.println("ServerSocket Started.");
					
					Socket s = ss.accept();
					
					ObjectInput in = new ObjectInputStream(new BufferedInputStream(s.getInputStream())) {
						@Override
						protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
							return Thread.currentThread().getContextClassLoader().loadClass(desc.getName());
						}
					};
					
					Object obj = in.readObject();
					System.out.println(obj);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		t.setContextClassLoader(new ClassLoader(ClassLoader.getSystemClassLoader()) {
			private Map<String, String> maps = new HashMap<String, String>();
			private Set<String> loaded = new HashSet<String>();
			
			{
				maps.put(People.class.getName(), Person.class.getName());
				maps.put(FullName.class.getName(), Name.class.getName());
			}
			
			@Override
			public Class<?> loadClass(final String origName) throws ClassNotFoundException {
				final String destName = maps.get(origName);
				if (destName != null && !loaded.contains(origName)) {
					System.out.println(origName + " --> " + destName);
					final String origPath = origName.replace('.', '/');
					final String destPath = destName.replace('.', '/');
					try {
						// ASM version 4.0
//						ClassReader cr = new ClassReader(getResourceAsStream(destPath + ".class"));
//						ClassWriter cw = new ClassWriter(cr, 0);
//						cr.accept(new RemappingClassAdapter(cw, new Remapper() {
//							public String map(String typeName) {
//								return typeName.replaceAll(destPath, origPath);
//							}
//						}), 0);
						
						// ASM version 1.5.3
						ClassReader cr = new ClassReader(getResourceAsStream(destPath + ".class"));
						ClassWriter cw = new ClassWriter(false);
						cr.accept(new ClassAdapter(cw) {
							public void visit(int version, int access, String name, String superName, String[] interfaces, String sourceFile) {
								name = name.replaceAll(destPath, origPath);
								super.visit(version, access, name, superName, interfaces, sourceFile);
							}
							
							public void visitField(int access, String name, String desc, Object value, Attribute attrs) {
								desc = handleDesc(desc);
								super.visitField(access, name, desc, value, attrs);
							};

							public CodeVisitor visitMethod(int access, String name, String desc, String[] exceptions, Attribute attrs) {
								CodeVisitor mv = cv.visitMethod(access, name, desc, exceptions, attrs);
								return mv == null ? null : new CodeAdapter(mv) {
									public void visitFieldInsn(int opcode, String owner, String name, String desc) {
										owner = owner.replaceAll(destPath, origPath);
										super.visitFieldInsn(opcode, owner, name, desc);
									}
							    };
							}
							
							private String handleDesc(String desc) {
								for (Entry<String, String> e : maps.entrySet()) {
									String o = e.getKey().replace('.', '/');
									String d = e.getValue().replace('.', '/');
									
									if (desc.contains(d)) {
										desc = desc.replaceAll(d, o);
									}
								}
								return desc;
							}
						}, false);
						
						//
						byte[] b = cw.toByteArray();
						
						Class<?> r = defineClass(origName, b, 0, b.length);

						loaded.add(origName);
						
						return r;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				return super.loadClass(origName);
			}
		});
		
		t.start();
		
		Thread.sleep(1000);
		
		//
		People b = new People();
		b.setName(new FullName("Yifei", null, "Zhang"));
		b.setAge(32);
		b.setHeight(1.78);
		b.setMarried(true);
		b.setAddress("Dalian");
		
		Socket s = new Socket("localhost", 2828);
		
		ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));
		out.writeObject(b);
		out.flush();
		
		System.out.println("The bean has been sent. " + b);
	}
	
}
