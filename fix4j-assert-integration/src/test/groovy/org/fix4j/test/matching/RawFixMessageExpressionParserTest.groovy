package org.fix4j.test.matching

import org.fix4j.spec.fix50sp2.FieldTypes
import org.fix4j.spec.fix50sp2.FixSpec
import org.fix4j.test.expression.FlexibleMessageExpressionParser
import org.fix4j.test.expression.MessageExpression
import org.fix4j.test.expression.RawFixMessageExpressionParser
import org.fix4j.test.fixmodel.Field
import org.fix4j.test.fixspec.FixSpecification
import org.fix4j.test.util.ExceptionUtils
import spock.lang.Specification

/**
 * User: ben
 * Date: 7/10/2014
 * Time: 5:51 AM
 */
class RawFixMessageExpressionParserTest extends Specification {
    private FixSpecification spec = FixSpec.INSTANCE;
    private RawFixMessageExpressionParser parser = new RawFixMessageExpressionParser(spec);

    private final String MARKET_DATA_REQUEST = "35=V|262=AASDJKG790|263=0|264=20|267=2|269=0|269=1|146=3|55=GBP/USD|55=AUD/USD|55=USD/JPY|";

    //Parse field type
    def "test parse expression"() {
        when:
        final MessageExpression expression = parser.parse(MARKET_DATA_REQUEST);

        then:
        assert expression.getFieldExpressions().size() == 11;
        assert expression.getFieldExpressions().get(0).equals(new Field(FieldTypes.MsgType, "V"))
        assert expression.getFieldExpressions().get(1).equals(new Field(FieldTypes.MDReqID, "AASDJKG790"))
        assert expression.getFieldExpressions().get(2).equals(new Field(FieldTypes.SubscriptionRequestType, "0"))
        assert expression.getFieldExpressions().get(3).equals(new Field(FieldTypes.MarketDepth, "20"))
        assert expression.getFieldExpressions().get(4).equals(new Field(FieldTypes.NoMDEntryTypes, "2"))
        assert expression.getFieldExpressions().get(5).equals(new Field(FieldTypes.MDEntryType, "0"))
        assert expression.getFieldExpressions().get(6).equals(new Field(FieldTypes.MDEntryType, "1"))
        assert expression.getFieldExpressions().get(7).equals(new Field(FieldTypes.NoRelatedSym, "3"))
        assert expression.getFieldExpressions().get(8).equals(new Field(FieldTypes.Symbol, "GBP/USD"))
        assert expression.getFieldExpressions().get(9).equals(new Field(FieldTypes.Symbol, "AUD/USD"))
        assert expression.getFieldExpressions().get(10).equals(new Field(FieldTypes.Symbol, "USD/JPY"))
    }

    def "test toString"() {
        when:
        final MessageExpression expression = parser.parse(MARKET_DATA_REQUEST);

        then:
        assert expression.toString() == "[MsgType]35=V[MARKETDATAREQUEST]|[MDReqID]262=AASDJKG790|[SubscriptionRequestType]263=0[SNAPSHOT]|[MarketDepth]264=20|[NoMDEntryTypes]267=2|[MDEntryType]269=0[BID]|[MDEntryType]269=1[OFFER]|[NoRelatedSym]146=3|[Symbol]55=GBP/USD|[Symbol]55=AUD/USD|[Symbol]55=USD/JPY|";
    }

    def "test ParseFieldType, tag type is not a number"() {
        when:
        parser.parseFieldType("blah");

        then:
        thrown IllegalArgumentException.class;
    }

    def "test ParseFieldType, known tag"() {
        expect: assert parser.parseFieldType("35") == FieldTypes.MsgType;
    }

    def "test ParseFieldType, custom tag"() {
        expect: assert parser.parseFieldType("12345678") == FieldTypes.forCustomTag(12345678);
    }

    //ParseField
    def "test ParseField, 35=D"() {
        expect: assert parser.parseField("35=D") == new Field(FieldTypes.MsgType, "D");
    }

    def "test ParseField, 35=blah[asdf]"() {
        expect: assert parser.parseField("35=blah[asdf]") == new Field(FieldTypes.MsgType, "blah[asdf]");
    }

    def "test ParseField, 12345678=D"() {
        expect: assert parser.parseField("12345678=D") == new Field(FieldTypes.forCustomTag(12345678), "D");
    }

    def "test ParseField, 12345678=BLAH"() {
        expect: assert parser.parseField("12345678=D") != new Field(FieldTypes.forCustomTag(12345678), "BLAH");
    }

    def "test ParseExpression, no equals in field"(){
        when:
        parser.parse("35=D|foo|bar")

        then:
        final IllegalArgumentException exception = thrown()
        ExceptionUtils.assertExceptionMessagesContain(exception, "Fix field expression does not match '<tag>=<value>' format. Field: 'foo'");
    }

    def "test ParseExpression, too many equals in field"(){
        when:
        parser.parse("35=D|foo=bar=blah")

        then:
        final IllegalArgumentException exception = thrown()
        ExceptionUtils.assertExceptionMessagesContain(exception, "Badly formatted field 'foo=bar=blah'.  More than one equals sign '=' detected.  This could mean that there was more than one equals sign specified in the field, or, it could mean that two or more fields were not separated by a valid field delimiter.  Please ensure that fields are separated by text which matches regex:");
    }
}
