package at.technikum.wien.mse.swe.connector;

import at.technikum.wien.mse.swe.exception.ConnectorReflectionException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

class ObjectCreator {

    private Object createNewEnumObject(Class enumClass, String param) {
        Object object;
        try {
            object = Enum.valueOf(enumClass, param);
        } catch (IllegalArgumentException e) {
            List<Method> methods = Arrays.stream(enumClass.getMethods())
                    .filter(method -> !method.getName().equals("valueOf"))
                    .filter(method -> method.getParameterCount() == 1)
                    .filter(method -> method.getParameterTypes()[0] == String.class)
                    .collect(Collectors.toList());
            if (methods.size() == 0) {
                throw new IllegalArgumentException("Unable to find an initiating method for ENUM " + enumClass);
            }
            try {
                object = methods.get(0).invoke(null, param);
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new ConnectorReflectionException(e);
            }
        }
        return object.getClass() == Optional.class ? ((Optional)object).get() : object;
    }

    private Object createNewEnumObject(Class objectClass, Map<String,Object> params) {
        String key = null;
        for (String paramKey: params.keySet()) {
            key = paramKey;
        }
        return createNewEnumObject(objectClass, (String)params.get(key));
    }

    private Object createNewObjectThroughSetter(Class objectClass, Map<String,Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IntrospectionException {
        Object object = objectClass.getConstructor().newInstance();
        for (Map.Entry<String,Object> entry : params.entrySet()){
            new PropertyDescriptor(entry.getKey(), objectClass)
                    .getWriteMethod()
                    .invoke(object, entry.getValue());
        }
        return object;
    }

    private Object createNewObjectThroughConstructor(Class objectClass, Map<String,Object> params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Object> values = new ArrayList<>(params.values());

        return objectClass.getConstructor(values.stream().map(Object::getClass).toArray(Class[]::new))
                .newInstance(values.toArray());
    }

    Object createNewObject(Class objectClass, Map<String,Object> params) {
        Object object;
        if (objectClass.isEnum() && params.size() == 1) {
            object = createNewEnumObject(objectClass, params);
        } else {
            try {
                object = createNewObjectThroughSetter(objectClass, params);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | IntrospectionException e) {
                try {
                    object = createNewObjectThroughConstructor(objectClass, params);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                    throw new ConnectorReflectionException(ex);
                }
            }
        }
        return object;
    }

    Object createNewObjectFromStringMap(Class objectClass, Map<String,Object> params) {
        Map<String,Object> mappedParam = new HashMap<>();
        params.forEach((name,value) -> {
            try {
                mappedParam.put(name,createNewObject(objectClass.getDeclaredField(name).getType(),value));
            } catch (NoSuchFieldException e) {
                throw new ConnectorReflectionException(e);
            }
        });
        return createNewObject(objectClass, mappedParam);
    }

    Object createNewObject(Class objectClass, Object param) {
        Object object;
        if (objectClass.isEnum()) {
            object = createNewEnumObject(objectClass, (String) param);
        } else {
            try {
                object = objectClass.getConstructor(param.getClass()).newInstance(param);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new ConnectorReflectionException(e);
            }
        }
        return object;
    }
}
