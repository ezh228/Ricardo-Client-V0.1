package ru.terrarXD.shit.event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/**
 * @author zTerrarxd_
 * @since 16:00 of 7.05.2022
 */
public class EventManager {

    private static final Map<Class<? extends Event>, ArrayHelper<Data>> REGISTRY_MAP;

    public  void register(final Object o) {

        for (final Method method : o.getClass().getDeclaredMethods()) {
            if (!isMethodBad(method)) {
                register(method, o);
            }
        }
    }

    public  void register(final Object o, final Class<? extends Event> clazz) {

        for (final Method method : o.getClass().getDeclaredMethods()) {
            if (!isMethodBad(method, clazz)) {
                register(method, o);
            }
        }
    }

    private  void register(final Method method, final Object o) {

        final Class<?> clazz = method.getParameterTypes()[0];
        final Data methodData = new Data(o, method, method.getAnnotation(EventTarget.class).value());

        if (!methodData.target.isAccessible()) {
            methodData.target.setAccessible(true);
        }

        if (EventManager.REGISTRY_MAP.containsKey(clazz)) {
            if (!EventManager.REGISTRY_MAP.get(clazz).contains(methodData)) {
                EventManager.REGISTRY_MAP.get(clazz).add(methodData);
                sortListValue((Class<? extends Event>) clazz);
            }
        } else {
            EventManager.REGISTRY_MAP.put((Class<? extends Event>) clazz, new ArrayHelper<Data>() {

                {
                    this.add(methodData);
                }
            });
        }
    }

    public  void unregister(final Object o) {

        for (final ArrayHelper<Data> flexibalArray : EventManager.REGISTRY_MAP.values()) {
            for (final Data methodData : flexibalArray) {
                if (methodData.source.equals(o)) {
                    flexibalArray.remove(methodData);
                }
            }
        }

        cleanMap(true);
    }

    public  void unregister(final Object o, final Class<? extends Event> clazz) {

        if (EventManager.REGISTRY_MAP.containsKey(clazz)) {
            for (final Data methodData : EventManager.REGISTRY_MAP.get(clazz)) {
                if (methodData.source.equals(o)) {
                    EventManager.REGISTRY_MAP.get(clazz).remove(methodData);
                }
            }

            cleanMap(true);
        }
    }


    public  void cleanMap(final boolean b) {

        final Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = EventManager.REGISTRY_MAP.entrySet().iterator();

        while (iterator.hasNext()) {
            if (!b || iterator.next().getValue().isEmpty()) {
                iterator.remove();
            }
        }
    }

    public  void removeEnty(final Class<? extends Event> clazz) {

        final Iterator<Map.Entry<Class<? extends Event>, ArrayHelper<Data>>> iterator = EventManager.REGISTRY_MAP.entrySet().iterator();

        while (iterator.hasNext()) {
            if (iterator.next().getKey().equals(clazz)) {
                iterator.remove();
                break;
            }
        }
    }

    private  void sortListValue(final Class<? extends Event> clazz) {

        final ArrayHelper<Data> flexibleArray = new ArrayHelper<Data>();

        for (final byte b : Priority.VALUE_ARRAY) {
            for (final Data methodData : EventManager.REGISTRY_MAP.get(clazz)) {
                if (methodData.priority == b) {
                    flexibleArray.add(methodData);
                }
            }
        }

        EventManager.REGISTRY_MAP.put(clazz, flexibleArray);
    }

    private static boolean isMethodBad(final Method method) {

        return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
    }

    private static boolean isMethodBad(final Method method, final Class<? extends Event> clazz) {

        return isMethodBad(method) || method.getParameterTypes()[0].equals(clazz);
    }

    public static ArrayHelper<Data> get(final Class<? extends Event> clazz) {

        return EventManager.REGISTRY_MAP.get(clazz);
    }

    
    public static void shutdown() {

        EventManager.REGISTRY_MAP.clear();
    }

    static {
        REGISTRY_MAP = new HashMap<Class<? extends Event>, ArrayHelper<Data>>();
    }

}
