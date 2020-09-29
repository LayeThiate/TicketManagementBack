package fr.urss.common.api.filtering;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    List<Predicate> predicates = new ArrayList<>();

    Filter() {}

    void addFilter(String name, Operator operator, String value) {
        predicates.add(new Predicate(name, operator, value));
    }

    public List<Predicate> getPredicates() {
        return predicates;
    }

    public enum Operator {
        Less("<", "%3C"), LessEqual("<=", "%3C%3D"),
        Equal("=", "%3D"), GreaterEqual(">=", "%3E%3D"),
        Greater(">", "%3E"), NotEqual("<>", "%3C%3E");

        private final String literal;
        private final String URIEncodedLiteral;

        Operator(String literal, String URIEncodedLiteral) {
            this.literal = literal;
            this.URIEncodedLiteral = URIEncodedLiteral;
        }

        public String literal() {
            return literal;
        }

        public String getURIEncodedLiteral() {
            return URIEncodedLiteral;
        }

        @Override
        public String toString() {
            return literal;
        }
    }

    public static class Predicate {
        String name;
        Operator operator;
        String value;

        public Predicate(String name, Operator operator, String value) {
            this.name = name;
            this.operator = operator;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Operator getOperator() {
            return operator;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return name + operator + value;
        }
    }
}
