package ee.jiss.commons.reflect;

import static ee.jiss.commons.lang.CheckUtils.isEmptyArray;
import static ee.jiss.commons.reflect.MetaUtils.readField;
import static ee.jiss.commons.reflect.MetaUtils.writeField;
import static java.util.Arrays.copyOfRange;
import static java.util.Arrays.stream;

public class FlatCopyUtils {
    public static final String TARGET_DELIMITER = "->";

    public static <T> T copy(Object donor, T target, String... fields) {
        if (donor == null || target == null || fields == null || fields.length == 0) return target;

        stream(fields).forEach(property -> writeField(target, property, readField(donor, property)));

        return target;
    }

    public static <T> T copyAsymmetric(Object donor, T target, String... fields) {
        try {
            if (donor == null || target == null || fields == null || fields.length == 0) return target;

            stream(fields).forEach(expression -> {
                String[] properties = expression.split(TARGET_DELIMITER);

                if (properties.length <= 1) {
                    copyField(donor, target, properties[0]);
                } else {
                    copyField(donor, target, properties[0], copyOfRange(properties, 1, properties.length));
                }
            });

            return target;
        } catch (final Exception e) {
            throw new IllegalStateException("Packages scan fail!", e);
        }
    }

    private static <T> void copyField(Object donor, T target, String donorField, String... targetFields) {
        Object donorValue = readField(donor, donorField);

        if (isEmptyArray(targetFields)) writeField(target, donorField, donorValue);

        stream(targetFields).forEach(it -> writeField(target, it, donorValue));
    }
}
