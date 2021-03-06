/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse Public License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.amanzi.awe.properties.ui.views.internal;


/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public enum ArraysConverter {

    INTEGER_ARRAY(int[].class) {
        private int[] array;

        @Override
        protected void addValue(final int i, final String value) {
            array[i] = Integer.parseInt(value);

        }

        @Override
        protected Object initArray(final int length) {
            array = new int[length];
            return array;
        }
    },
    LONG_ARRAY(long[].class) {
        private long[] array;

        @Override
        protected void addValue(final int i, final String value) {
            array[i] = Long.parseLong(value);

        }

        @Override
        protected Object initArray(final int length) {
            array = new long[length];
            return array;
        }
    },
    FLOAT_ARRAY(float[].class) {
        private float[] array;

        @Override
        protected void addValue(final int i, final String value) {
            array[i] = Float.parseFloat(value);

        }

        @Override
        protected Object initArray(final int length) {
            array = new float[length];
            return array;
        }
    },
    DOUBLE_ARRAY(Double[].class) {
        private double[] array;

        @Override
        protected void addValue(final int i, final String value) {
            array[i] = Double.parseDouble(value);
        }

        @Override
        protected Object initArray(final int length) {
            array = new double[length];
            return array;
        }
    },
    STRING_ARRAY(String[].class) {
        private String[] array;

        @Override
        protected void addValue(final int i, final String value) {
            array[i] = value;

        }

        @Override
        protected Object initArray(final int length) {
            array = new String[length];
            return array;
        }
    },
    CHAR_ARRAY(char[].class) {
        private char[] array;

        @Override
        protected void addValue(final int i, final String value) throws IllegalArgumentException {
            array[i] = getValue(value);

        }

        protected Character getValue(final String newStringValue) throws IllegalArgumentException {
            if (newStringValue.length() > 1) {
                throw new IllegalArgumentException("can't cast value " + newStringValue + " to Charracter");
            } else {
                return new Character(newStringValue.charAt(0));
            }
        }

        @Override
        protected Object initArray(final int length) {
            array = new char[length];
            return array;
        }
    };

    public static ArraysConverter findByType(final Class< ? > type) {
        for (ArraysConverter converter : values()) {
            if (converter.getType().isAssignableFrom(type)) {
                return converter;
            }
        }
        return null;
    }

    private Class< ? > type;

    private ArraysConverter(final Class< ? > type) {
        this.type = type;
    }

    protected abstract void addValue(int i, String value) throws IllegalArgumentException;

    public Object convertToArray(final String[] stringArray) throws IllegalArgumentException {
        Object array = initArray(stringArray.length);
        for (int i = 0; i < stringArray.length; i++) {
            String newStringValue = stringArray[i];
            addValue(i, newStringValue);
        }
        return array;
    }

    /**
     * @return Returns the type.
     */
    protected Class< ? > getType() {
        return type;
    }

    /**
     * @return
     */

    protected abstract Object initArray(int length);

}
