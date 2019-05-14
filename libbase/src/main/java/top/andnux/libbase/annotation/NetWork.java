package top.andnux.libbase.annotation;

import top.andnux.libbase.network.netstate.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetWork {
    NetType value() default NetType.AUTO;
}
