package at.technikum.wien.mse.swe.connector;

import at.technikum.wien.mse.swe.exception.ConnectorReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class ConnectorImpl<T> implements Connector<T> {

    private final Class<T> typeParameterClass;

    public ConnectorImpl(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    private class Tuple {
        private String key;
        private Object value;

        Tuple(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        String getKey() {
            return key;
        }

        Object getValue() {
            return value;
        }
    }

    private String readFileContent(Path file) {
        String content;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            content = reader.readLine();
        } catch (IOException e) {
            throw new ConnectorReadException(e);
        }
        return content;
    }

    private String removeLeftPadding(String field, String padding) {
        String changedField = field;
        while (!changedField.isEmpty() && changedField.startsWith(padding)) {
            changedField = changedField.substring(padding.length());
        }
        return changedField;
    }

    private String removeRightPadding(String field, String padding) {
        String changedField = field;
        while (!changedField.isEmpty() && changedField.endsWith(padding)){
            changedField = changedField.substring(0, changedField.length()-1 - padding.length());
        }
        return changedField;
    }

    private String extract(String content, int startIndex, int length, String padding, FieldAlignment alignment) {
            String field = content.substring(startIndex, startIndex + length);
        return alignment == FieldAlignment.LEFT ? removeLeftPadding(field,padding) : removeRightPadding(field,padding);
    }

    private Map<String, Object> extractSimpleFields(Class<T> connectionModel, String content){
        return Arrays.stream(connectionModel.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(SimpleField.class))
                .map(field -> new Tuple(field.getName(),
                        new ObjectCreator().createNewObject(field.getType(),
                                extract(content, field.getAnnotation(SimpleField.class).begin(),
                                        field.getAnnotation(SimpleField.class).length(),
                                        field.getAnnotation(SimpleField.class).padding(),
                                        field.getAnnotation(SimpleField.class).alignment()))))
                .collect(Collectors.toMap(Tuple::getKey,Tuple::getValue));
    }

    private Tuple createCompoundField(Field field, String content) {
        Map<String, Object> params = Arrays.stream(field.getAnnotationsByType(CompoundField.class))
                .map(annotation -> new Tuple(annotation.name(),
                        extract(content, annotation.begin(),annotation.length(),annotation.padding(),annotation.alignment())))
                .collect(Collectors.toMap(Tuple::getKey,Tuple::getValue));
        return new Tuple(field.getName(),
                new ObjectCreator().createNewObjectFromStringMap(field.getType(), params));
    }

    private Map<String, Object> extractCompoundFields(Class<T> connectionModel, String content){
        return Arrays.stream(connectionModel.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(CompoundField.class) || field.isAnnotationPresent(CompoundFields.class))
                .map(field -> this.createCompoundField(field, content))
                .collect(Collectors.toMap(Tuple::getKey,Tuple::getValue));
    }

    @Override
    public T read(Path path) {
        String content = readFileContent(path);
        Map<String,Object> fields = this.extractSimpleFields(typeParameterClass, content);
        fields.putAll(Optional.ofNullable(this.extractCompoundFields(typeParameterClass, content)).orElse(new HashMap<>()));
        return typeParameterClass.cast(new ObjectCreator().createNewObject(typeParameterClass, fields));
    }
}
