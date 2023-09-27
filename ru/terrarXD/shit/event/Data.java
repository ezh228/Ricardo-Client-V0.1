package ru.terrarXD.shit.event;

import java.lang.reflect.Method;
/**
 * @author zTerrarxd_
 * @since 16:00 of 7.05.2022
 */
public class Data {

	public final Object source;
	public final Method target;
	public final byte priority;

	public Data(Object source, Method target, byte priority) {
		this.source = source;
		this.target = target;
		this.priority = priority;
	}

}
