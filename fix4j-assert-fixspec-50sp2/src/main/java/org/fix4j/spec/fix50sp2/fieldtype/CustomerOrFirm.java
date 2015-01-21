package org.fix4j.spec.fix50sp2.fieldtype;


import org.fix4j.test.fixspec.BaseFieldType;
import org.fix4j.test.fixspec.FieldClass;
import org.fix4j.test.fixspec.FieldClassLookup;
import org.fix4j.test.fixspec.FieldTypeValueEnum;
import org.fix4j.test.fixmodel.Field;

public class CustomerOrFirm extends BaseFieldType {
    public static final CustomerOrFirm INSTANCE = new CustomerOrFirm();

    private CustomerOrFirm() {
        super(
            "CustomerOrFirm",
            204,
            FieldClassLookup.lookup("INT"),
            Values.class
        );
    }

    public static Field withValue(final String value){ return new Field(INSTANCE, value); }
    public static Field withValue(final long value){ return new Field(INSTANCE, ""+value); }

    public static FieldFactory withValue(){ return new FieldFactory(); }

    public static class FieldFactory{
        public final Field FIRM = new Field(CustomerOrFirm.INSTANCE, Values.FIRM.getOrdinal());
        public final Field CUSTOMER = new Field(CustomerOrFirm.INSTANCE, Values.CUSTOMER.getOrdinal());
    }

    public enum Values implements FieldTypeValueEnum {
        FIRM("1"),
        CUSTOMER("0");

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