package ru.ifmo.baev.inverted.index;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 03.03.14
 */
public class TermStorageImpl implements TermStorage {

    private Map<String, Term> storage;

    private long summaryTermLength;

    public TermStorageImpl() {
        storage = new ConcurrentHashMap<>();
        summaryTermLength = 0;
    }

    @Override
    public void update(Document document) {
        for (String term : document.getTerms()) {
            if (storage.containsKey(term)) {
                Term t = storage.get(term);
                t.matchFound(document.getId());
            } else {
                Term t = new Term(term);
                t.matchFound(document.getId());
                storage.put(term, t);
                summaryTermLength += term.length();
            }
        }
    }

    @Override
    public Term getTerm(String term) {
        if (!storage.containsKey(term)) {
            return new Term(term);
        }
        return storage.get(term);
    }

    @Override
    public double getAverageTermLength() {
        return ((double) summaryTermLength) / size();
    }

    @Override
    public int size() {
        return storage.size();
    }

}
