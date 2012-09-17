package test.db.tx;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Propagation;

/**
 * 
 * @author yz69579
 *
 */
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.METHOD)
@Inherited
public @interface Tx {
	
	/**
	 * Spring Transaction Attribute 
	 */
	int timeout() default -1;
	
	int isolationLevel() default -1;
	
	boolean readonly() default false;
	
	Propagation propagation() default Propagation.REQUIRED;
	
	/**
	 * Just for Sybase ASE server
	 */
	boolean unchained() default false;
}
