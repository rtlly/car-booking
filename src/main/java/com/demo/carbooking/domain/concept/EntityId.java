package com.demo.carbooking.domain.concept;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Getter
@EqualsAndHashCode
public abstract class EntityId implements ValueObject {
    private final String id;

    public EntityId() {
        this.id = UUID.randomUUID().toString();
    }

    public EntityId(String id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    public static <T extends EntityId> T parse(String value, Class<T> clazz) {
        if (Objects.isNull(value)) {
            return null;
        }
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class);
            return constructor.newInstance(value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Parse id error", e);
        }
    }

    public static <T extends EntityId> List<T> parse(List<String> valueList, Class<T> clazz) {
        if (Objects.isNull(valueList) || valueList.isEmpty()) {
            return Collections.emptyList();
        }
        return valueList.stream()
                .map(value -> EntityId.parse(value, clazz))
                .collect(toList());
    }

    @Override
    public String toString() {
        return id;
    }

}
