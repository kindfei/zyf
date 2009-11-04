package incubation.jmx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ATTRIBUTE {
	public String name() default "";
	public String type() default "";
	public String description() default "No description";
	public boolean isReadable() default true;
	public boolean isWritable() default false;
	public boolean isToString() default false;
}
