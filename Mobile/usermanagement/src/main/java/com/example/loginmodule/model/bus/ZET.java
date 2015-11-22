package com.example.loginmodule.model.bus;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.TinyBus;

/**
 * Created by david on 15.11.2015..
 */
public class ZET {

    private static final ZET instance = new ZET();
    private TinyBus bus;
    private List<Object> objectList;

    private ZET() {
        objectList = new ArrayList<>();
        bus = new TinyBus();
    }

    public static void register(Object object) {
        if (!instance.objectList.contains(object)) {
            instance.bus.register(object);
            instance.objectList.add(object);
        }
    }

    public static void unregister(Object object) {
        if (instance.objectList.contains(object)) {
            instance.bus.unregister(object);
            instance.objectList.remove(object);
        }
    }

    public static void post(Object object) {
        instance.bus.post(object);
    }

}