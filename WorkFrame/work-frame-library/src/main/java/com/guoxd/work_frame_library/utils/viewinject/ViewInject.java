package com.guoxd.work_frame_library.utils.viewinject;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
	public int id();
	public String click() default "";
	public String longClick() default "";
	public String itemClick() default "";
	public String itemLongClick() default "";
	public Select select() default @Select(selected="") ;
}
/**
 * 使用案例： 在声明全局变量时
 * @ViewInject(id = R.id.tv_logout, click = "toAccountLogoutClick")
 * private TextView tv_logout;
 **/