package org.fix4j.spec.fix50sp2.fieldtype;


import org.fix4j.test.fixspec.BaseFieldType;
import org.fix4j.test.fixspec.FieldClass;
import org.fix4j.test.fixspec.FieldClassLookup;
import org.fix4j.test.fixspec.FieldTypeValueEnum;
import org.fix4j.test.fixmodel.Field;

public class TradSesMode extends BaseFieldType {
    public static final TradSesMode INSTANCE = new TradSesMode();

    private TradSesMode() {
        super(
            "TradSesMode",
            339,
            FieldClassLookup.lookup("INT"),
            Values.class
        );
    }

    public static Field withValue(final String value){ return new Field(INSTANCE, value); }
    public static Field withValue(final long value){ return new Field(INSTANCE, ""+value); }

    public static FieldFactory withValue(){ return new FieldFactory(); }

    public static class FieldFactory{
        public final Field PRODUCTION = new Field(TradSesMode.INSTANCE, Values.PRODUCTION.getOrdinal());
        public final Field SIMULATED = new Field(TradSesMode.INSTANCE, Values.SIMULATED.getOrdinal());
        public final Field TESTING = new Field(TradSesMode.INSTANCE, Values.TESTING.getOrdinal());
    }

    public enum Values implements FieldTypeValueEnum {
        PRODUCTION("3"),
        SIMULATED("2"),
        TESTING("1");

        private final String ordinal;

        private Values(final String ordinal) {
            this.ordinal = ordinal;
        }

        @Override
        public String getOrdinal() {
            return ordinal;
        }
    }
}