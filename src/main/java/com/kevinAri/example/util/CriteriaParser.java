package com.kevinAri.example.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter
public class CriteriaParser<T> {
    @Setter
    @Getter
    @AllArgsConstructor
    @ToString
    public static class SearchCriteria {
        private String operator;
        private String key;
        private String value;
        private Boolean dateTime;
    }

    // dateTimeArr adalah kolom-kolom yang bertipe data datetime
    private List<String> dateTimeArr;



    /* contoh query
        normal parse
        nomor>10 AND id<=20 OR nama=contoh nama
        AND OR harus capslock

        parse recursively
        (nomor>10 AND id<=20) OR nama=contoh nama
    */
    // fungsi utama
    public Specification<T> parse (String query) {
        // parsing operator dan operand
        String regex1 = " AND | OR ";
        Pattern pattern = Pattern.compile(regex1);
        Matcher matcher = pattern.matcher(query);
        List<String> operator = new ArrayList<>();
        List<String> operand = new ArrayList<>();
        while (matcher.find()) {
            operator.add (matcher.group().trim());
        }
        for(String splitRes: query.split(regex1, 0)){
            operand.add (splitRes.trim());
        }

        // convert operand menjadi SearchCriteria
        List<SearchCriteria> critArr = new ArrayList<>();
        for (String ele: operand) {
            critArr.add (stringToSearchCriteria(ele));
        }

        // convert SearchCriteria dan operator menjadi Specification
        Specification<T> specs = Specification.where(specBuilder(critArr.get(0)));
        for (int i=1; i<critArr.size(); i++) {
            if (operator.get(i-1).equals("AND")) {
                specs = specs.and(specBuilder(critArr.get(i)));
            } else if (operator.get(i-1).equals("OR")) {
                specs = specs.or(specBuilder(critArr.get(i)));
            }
        }

        return specs;
    }
    public Specification<T> parseRecursively (String query) {
        query = query.trim();
        // cari brackets
        List<List<Integer>> brackets = getBrackets(query);
        // hapus kurung terluar dari query dan array brackets
        if (brackets.get(0).size()!=0) {
            if (brackets.get(0).get(0)==1 & brackets.get(1).get(0)==query.length()-1) {
                query = query.substring(1, query.length()-1);
                brackets.get(0).remove(0);
                brackets.get(1).remove(0);
                // bracket sisanya dikurangi 1 semua
                for (int j=0; j<brackets.get(0).size(); j++) {
                    brackets.get(0).set(j, brackets.get(0).get(j)-1);
                    brackets.get(1).set(j, brackets.get(1).get(j)-1);
                }
            }
        }
        // System.out.println(query);
        // System.out.println(brackets);


        Pattern pattern = Pattern.compile(" AND | OR ");
        Matcher matcher = pattern.matcher(query);
        // base dari rekursif
        if (!matcher.find()) {
//            System.out.printf("execute: %s\n", query);
            return specBuilder(stringToSearchCriteria(query));
        }
        // rekursif
        else {
            // pisahkan operator dan operand pada level tertinggi
            List<List<String>> operatorOperand = parseOperand(query, brackets); // 0:operator, 1:operand
//            System.out.println(operatorOperand + "\n");

            // rekursif
            Specification<T> specs = Specification.where(parseRecursively(operatorOperand.get(1).get(0)));
            for (int i=1; i<operatorOperand.get(1).size(); i++) {
                if (operatorOperand.get(0).get(i-1).equals("AND")) {
                    specs = specs.and(parseRecursively(operatorOperand.get(1).get(i)));
                } else if (operatorOperand.get(0).get(i-1).equals("OR")) {
                    specs = specs.or(parseRecursively(operatorOperand.get(1).get(i)));
                }
            }
//            System.out.printf("execute: %s\n", query);
            return specs;

        }
    }




    // fungsi-fungsi pembantu
    private Specification<T> specBuilder (SearchCriteria crit) {
        return new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> r, CriteriaQuery<?> q, CriteriaBuilder b) {
                // convert value ke datetime
                Timestamp dateTime = null;
                if (crit.getDateTime()) {
                    dateTime = Timestamp.valueOf(crit.getValue());
                }

                // build spec sesuai operator
                if (">=".equals(crit.getOperator())) {
                    if (crit.getDateTime()) {
                        return b.greaterThanOrEqualTo(r.get(crit.getKey()), dateTime);
                    }
                    return b.greaterThanOrEqualTo(r.get(crit.getKey()), crit.getValue());
                }
                else if (">".equals(crit.getOperator())) {
                    if (crit.getDateTime()) {
                        return b.greaterThan(r.get(crit.getKey()), dateTime);
                    }
                    return b.greaterThan(r.get(crit.getKey()), crit.getValue());
                }
                else if ("<=".equals(crit.getOperator())) {
                    if (crit.getDateTime()) {
                        return b.lessThanOrEqualTo(r.get(crit.getKey()), dateTime);
                    }
                    return b.lessThanOrEqualTo(r.get(crit.getKey()), crit.getValue());
                }
                else if ("<".equals(crit.getOperator())) {
                    if (crit.getDateTime()) {
                        return b.lessThan(r.get(crit.getKey()), dateTime);
                    }
                    return b.lessThan(r.get(crit.getKey()), crit.getValue());
                }
                else if ("!=".equals(crit.getOperator())) {
                    if (crit.getDateTime()) {
                        return b.notEqual(r.get(crit.getKey()), dateTime);
                    }
                    return b.notEqual(r.get(crit.getKey()), crit.getValue());
                }
                else if ("=".equals(crit.getOperator())) {
                    if (crit.getDateTime()) {
                        return b.equal(r.get(crit.getKey()), dateTime);
                    }
                    return b.equal(r.get(crit.getKey()), crit.getValue());
                }
                else if (":".equals(crit.getOperator())) {
                    return b.like(r.get(crit.getKey()).as(String.class), crit.getValue());
                }

                return null;
            }
        };
    }
    private Specification<T> specBuilderOld (SearchCriteria crit) {
        if (">=".equals(crit.getOperator())) {
            if (crit.getDateTime()) {
                return (r, q, b) -> b.greaterThanOrEqualTo(r.get(crit.getKey()), Timestamp.valueOf(crit.getValue()));
            }
            return (r, q, b) -> b.greaterThanOrEqualTo(r.get(crit.getKey()), crit.getValue());
        }
        else if (">".equals(crit.getOperator())) {
            if (crit.getDateTime()) {
                return (r, q, b) -> b.greaterThan(r.get(crit.getKey()), Timestamp.valueOf(crit.getValue()));
            }
            return (r, q, b) -> b.greaterThan(r.get(crit.getKey()), crit.getValue());
        }
        else if ("<=".equals(crit.getOperator())) {
            if (crit.getDateTime()) {
                return (r, q, b) -> b.lessThanOrEqualTo(r.get(crit.getKey()), Timestamp.valueOf(crit.getValue()));
            }
            return (r, q, b) -> b.lessThanOrEqualTo(r.get(crit.getKey()), crit.getValue());
        }
        else if ("<".equals(crit.getOperator())) {
            if (crit.getDateTime()) {
                return (r, q, b) -> b.lessThan(r.get(crit.getKey()), Timestamp.valueOf(crit.getValue()));
            }
            return (r, q, b) -> b.lessThan(r.get(crit.getKey()), crit.getValue());
        }
        else if ("!=".equals(crit.getOperator())) {
            if (crit.getDateTime()) {
                return (r, q, b) -> b.notEqual(r.get(crit.getKey()), Timestamp.valueOf(crit.getValue()));
            }
            return (r, q, b) -> b.notEqual(r.get(crit.getKey()), crit.getValue());
        }
        else if ("=".equals(crit.getOperator())) {
            if (crit.getDateTime()) {
                return (r, q, b) -> b.equal(r.get(crit.getKey()), Timestamp.valueOf(crit.getValue()));
            }
            return (r, q, b) -> b.equal(r.get(crit.getKey()), crit.getValue());
        }
        else if (":".equals(crit.getOperator())) {
            return (r, q, b) -> b.like(r.get(crit.getKey()), crit.getValue());
        }

        return null;
    }
    private SearchCriteria stringToSearchCriteria (String input) {
        // parsing string menjadi search criteria
        String regex2 = ">=|>|<=|<|!=|=|:";
        List<SearchCriteria> critArr = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex2);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String[] keyValue = input.split(regex2, 2);
            return new SearchCriteria(matcher.group(), keyValue[0], keyValue[1], dateTimeArr.contains(keyValue[0]));
        }
        return null;
    }



    // fungsi-funsgi untuk support parseRecursively
    private Boolean isBetween (Integer location, List<List<Integer>> brackets) {
        boolean between = false;
        for (int i=0; i<brackets.get(0).size(); i++) {
            if (location>brackets.get(0).get(i) & location<brackets.get(1).get(i)) {
                between = true;
                break;
            }
        }
        return between;
    }
    private List<List<Integer>> getBrackets (String query) {
        Pattern pattern = Pattern.compile("\\(|\\)");
        Matcher matcher = pattern.matcher(query);
        List<List<Integer>> brackets = new ArrayList<>(Arrays.asList(new ArrayList<>(), new ArrayList<>()));

        // cari index lokasi semua tanda kurung
        while (matcher.find()) {
            if (matcher.group().equals("(")) {
                brackets.get(0).add(matcher.end());
                brackets.get(1).add(-1);
            } else { // tutup kurung )
                int i = brackets.get(0).size() - 1;
                while (brackets.get(1).get(i) != -1) i--;
                brackets.get(1).set(i, matcher.start());
            }
            // System.out.printf ("%s %d %d\n", matcher.group(), matcher.start(), matcher.end());
        }
        return brackets;
    }
    private List<List<String>> parseOperand (String query, List<List<Integer>> brackets) {
        Pattern pattern = Pattern.compile(" AND | OR ");
        Matcher matcher = pattern.matcher(query);
        List<String> operand = new ArrayList<>();
        List<String> operator = new ArrayList<>();
        List<List<Integer>> operatorSE = new ArrayList<>(); // index start end dari operator

        // cari index start end dari operator
        while (matcher.find()) {
            if (!isBetween(matcher.start(), brackets)) { // jika operator tidak di dalam kurung
                operator.add(matcher.group().trim());
                operatorSE.add(Arrays.asList(matcher.start(), matcher.end()));
            }
        }

        // pisahkan operand (parsing operand)
        if (operatorSE.size()==0) { // jika tidak ada operator, langsung return query
            operand.add(query);
        } else {
            for (int i = 0; i < operatorSE.size() + 1; i++) {
                if (i == 0) { // operand pertama
                    operand.add(query.substring(0, operatorSE.get(0).get(0)).trim());
                } else if (i == operatorSE.size()) { // operand terakhir
                    operand.add(query.substring(operatorSE.get(i - 1).get(1)).trim());
                } else { // operand di tengah
                    operand.add(query.substring(operatorSE.get(i - 1).get(1), operatorSE.get(i).get(0)).trim());
                }
            }
        }

        return Arrays.asList(operator, operand);
    }


}
