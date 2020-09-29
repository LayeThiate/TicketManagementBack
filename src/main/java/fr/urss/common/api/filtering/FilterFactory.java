package fr.urss.common.api.filtering;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class FilterFactory {

    private Filter filter = new Filter();

    private FilterFactory() {}

    public FilterFactory parse(String filterString) {
        String[] terms = filterString.split("\\|");
        for (String term: terms) {
            for (Filter.Operator operator: Filter.Operator.values()) {
                int index = term.indexOf(operator.toString());
                if (index > -1)
                    filter.addFilter(term.substring(0, index), operator, term.substring(index + operator.toString().length()));
            }
        }
        return this;
    }

    public Filter build() {
        return filter;
    }

}
