package com.kevinAri.example.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import oracle.sql.TIMESTAMP;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CommonUtil {
    // generate
	public static String generateUuid() {
		StringBuilder uuid = new StringBuilder();
		uuid.append(UUID.randomUUID());
		uuid.replace(23, 24, "");
		uuid.replace(18, 19, "");
		uuid.replace(13, 14, "");
		uuid.replace(8, 9, "");
		uuid.insert(0, System.currentTimeMillis());
		return uuid.toString();
	}
    public static String generateRandomTraceNumber() {
        Random random = new Random();
        // 000000 sampai 999999
        return String.format("%06d", random.nextInt(1000000));
    }
    public static Date currentTime() {
        return new Date(System.currentTimeMillis());
    }
    public static String generateRandomAlphanumeric() {
        StringBuilder randomAlphanumeric = new StringBuilder(UUID.randomUUID().toString());
        randomAlphanumeric.replace(8, 9, "");
        return randomAlphanumeric.substring(0, 9);
    }


    // check
    public static Boolean notBlank (Float input) {
	    return input!=null;
//	    return (input!=null && !input.equals(""));
    }
    public static Boolean notBlank (String input) {
	    try {
            return input!=null && !input.isEmpty();
        } catch (Exception e) {
	        return false;
        }
    }
    public static Boolean notBlank (Object input) {
	    try {
            return input!=null && !input.toString().isEmpty();
        } catch (Exception e) {
	        return false;
        }
    }


    // convert
    public static String convert (Object input, Boolean nullable, Boolean positive, List<String> validValues) {
        if (input==null) {
            if (nullable) {
                if (positive) return "Y";
                else return "N";
            }
            else {
                if (positive) return "N";
                else return "Y";
            }
        }
        else { // input!=null
            if (validValues.contains(input.toString())) {
                if (positive) return "Y";
                else return "N";
            }
            else {
                if (positive) return "N";
                else return "Y";
            }
        }
    }
    public static String convert (Object input) {
	    try {
            if (input==null) return "";
            else return input.toString();
        } catch (Exception e) {
	        return "";
        }
    }
    public static String convertPreserveNull (Object input) {
        try {
            if (input==null || input.equals("")) return null;
            else return input.toString();
        } catch (Exception e) {
            return null;
        }
    }


    // number
    // casting
    public static Long bigDecimalObjectToLong(Object input) {
	    try {
	        return ((BigDecimal) input).longValue();
        } catch (Exception e) {
	        return null;
        }
    }
    // parsing
    public static Long stringToLong(Object input) {
        try {
            if (input==null) return null;
            else return new Long(input.toString());
        } catch (Exception e) {
            return null;
        }
    }
    public static BigDecimal stringToBigDecimal (String input) {
	    try {
            if (input==null || "".equals(input)) return null;
            else return new BigDecimal(input);
        } catch (Exception e) {
	        return null;
        }
    }
    public static BigDecimal stringToBigDecimal (String input, BigDecimal defaultValue) {
        try {
            if (input==null || "".equals(input)) return defaultValue;
            else return new BigDecimal(input);
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static BigDecimal objectToBigDecimal (Object input) {
        try {
            if (input==null) return null;
            else return new BigDecimal(input.toString());
        } catch (Exception e) {
            return null;
        }
    }
    public static BigDecimal objectToBigDecimal (Object input, BigDecimal defaultValue) {
        try {
            if (input==null) return defaultValue;
            else return new BigDecimal(input.toString());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static BigDecimal currencyStringToBigDecimal(String input, BigDecimal defaultValue) {
        try {
            input = input.replaceAll("\\.", "");
            input = input.replaceAll(",", ".");
            return new BigDecimal(input);
        } catch (Exception e) {
            return defaultValue;
        }
    }


    // date
    // casting
    public static Date oracleTimestampObjectToDate (Object oracleTimestamp) {
        try {
	        return ((TIMESTAMP) oracleTimestamp).dateValue();
        } catch (Exception e) {
            return null;
        }
    }
    // parsing
    public static Timestamp stringToTimestamp(String stringTimestamp) {
        try	{
            return Timestamp.valueOf(stringTimestamp);
        } catch (Exception e) {
            return null;
        }
    }
    public static Date parseStringDateToDate(String dateStr, String parseFormat) {
        try {
            if (dateStr==null || "".equals(dateStr.trim())) return null;
            return new SimpleDateFormat(parseFormat).parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
    public static Date parseAmlStringDateToDate (String dateStr) {
        try {
            if (dateStr==null || "".equals(dateStr.trim())) return null;
            else if (dateStr.contains("/")) return new SimpleDateFormat("yyyy/MM/dd").parse(dateStr.trim());
            else return new SimpleDateFormat("ddMMyyyy").parse(dateStr.trim());
        } catch (Exception e) {
            return null;
        }
    }
    public static YearMonth parseStringToYearMonth(String yearMonthStr, String parseFormat) {
        try {
            if (yearMonthStr==null || "".equals(yearMonthStr.trim())) return null;
            return YearMonth.parse(yearMonthStr, DateTimeFormatter.ofPattern(parseFormat));
        } catch (Exception e) {
            return null;
        }
    }
    // format
    public static String oracleTimestampToStringPreserveNull(TIMESTAMP timestampInput, String dateFormat) throws Exception {
        try {
            if (timestampInput==null) return null;
            return new SimpleDateFormat(dateFormat).format(timestampInput.dateValue());
        } catch (Exception e) {
            return null;
        }
    }
    public static String formatDateToStringPreserveNull(Date dateInput, String dateFormat) {
        try {
            if (dateInput==null) return null;
            return new SimpleDateFormat(dateFormat).format(dateInput);
        } catch (Exception e) {
            return null;
        }
    }
    // other date function
    public static Map<String, String> getStartEnd6Month() {
        LocalDate currentDate = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth());
        LocalDate endDate = yearMonth.minusMonths(1).atEndOfMonth();
        LocalDate startDate = yearMonth.minusMonths(6).atDay(1);
        Map<String, String> response = new HashMap<>();
        response.put("startDate", startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
//        response.put("startDate", "20120901");
        response.put("endDate", endDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return response;
    }
    public static List<String> getPast6Month() {
        List<String> response = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonth()).minusMonths(1);
        response.add(yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM")));
        for (int i=0; i<5; i++) {
            yearMonth = yearMonth.minusMonths(1);
            response.add(yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM")));
        }
        return response;
    }



    // jsonNode
    public static String jsonNodeToStringAndTrim(JsonNode input) {
        try {
            if (input==null || input instanceof NullNode) return "";
            else return input.asText().trim();
        } catch (Exception e) {
            return "";
        }
    }
    public static String jsonNodeToStringPreserveNullAndTrim(JsonNode input) {
        try {
            if (input==null || input instanceof NullNode || "".equals(input.asText().trim())) return null;
            else return input.asText().trim();
        } catch (Exception e) {
            return null;
        }
    }
    public static Date parseJsonNodeStringToDate(JsonNode jsonNode, String parseFormat) {
        try {
            if (jsonNode==null || "".equals(jsonNode.asText().trim())) return null;
            return new SimpleDateFormat(parseFormat).parse(jsonNode.asText());
        } catch (Exception e) {
            return null;
        }
    }
    public static Date jsonNodeLongToDate(JsonNode jsonNode) {
        try {
            if (jsonNode==null) return null;
            return new Date(jsonNode.asLong());
        } catch (Exception e) {
            return null;
        }
    }
    public static BigDecimal jsonNodeStringToBigDecimal(JsonNode jsonNode) {
        try {
            if (jsonNode==null || jsonNode instanceof NullNode || "".equals(jsonNode.asText().trim())) return null;
            return new BigDecimal(jsonNode.asText());
        } catch (Exception e) {
            return null;
        }
    }
    public static BigDecimal jsonNodeStringToBigDecimal(JsonNode jsonNode, BigDecimal defaultValue) {
        try {
            if (jsonNode==null || "".equals(jsonNode.asText().trim())) return defaultValue;
            return new BigDecimal(jsonNode.asText());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static Long jsonNodeStringToLong(JsonNode jsonNode) {
        try {
            if (jsonNode==null || "".equals(jsonNode.asText().trim())) return null;
            return Double.valueOf(jsonNode.asText()).longValue();
        } catch (Exception e) {
            return null;
        }
    }
    public static Long jsonNodeStringToLong(JsonNode jsonNode, Long defaultValue) {
        try {
            if (jsonNode==null || "".equals(jsonNode.asText().trim())) return defaultValue;
            return new Long(jsonNode.asText());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    public static Long jsonNodeDoubleToLong(JsonNode jsonNode) {
        try {
            if (jsonNode==null) return null;
            return Math.round(jsonNode.asDouble());
        } catch (Exception e) {
            return null;
        }
    }
    public static Boolean isJsonNodeList(JsonNode jsonNode) {
        if (jsonNode!=null) {
            // jika list
            if (jsonNode.get(0) != null) return true;
            // jika bukan list
            else return false;
        }
        else return null;
    }
    public static boolean isJsonNodeNull(JsonNode jsonNode) {
	    try {
            if (jsonNode == null || jsonNode instanceof NullNode) return true;
            else return false;
        } catch (Exception e) {
	        return true;
        }
    }



    // other function
    public static String formatCurrency (String currencyAmountStr) {
        try {
            Locale ind = new Locale("id", "ID");
            NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(ind);
            String output;
            if (currencyAmountStr==null || currencyAmountStr.equals("")) {
                output = dollarFormat.format(0);
            } else {
                output = dollarFormat.format(new Long(currencyAmountStr));
            }
            return output.substring(2, output.length()-3);
        } catch (Exception e) {
            return "";
        }
    }

    public static Long getBracketContent(String input) {
        try {
            return new Long(input.substring(input.indexOf("(")+1, input.indexOf(")")));
        } catch (Exception e) {
            return null;
        }
    }

    public static String appendQueryToUrl(String url, Map<String, String> params) {
        try {
            int count = 0;
            StringBuilder urlBuilder = new StringBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (count != 0) {
                    urlBuilder.append("&");
                } else {
                    count ++;
                    urlBuilder.append("?");
                }
                urlBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return urlBuilder.toString();
        } catch (Exception e) {
            return url;
        }
    }

    public static String decimalFormatter(Object numberToFormat) {
	    try {
	        DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(numberToFormat);
        } catch (Exception e) {
	        return "";
        }
    }





    public static java.sql.Date convertToDateSQL (Date dateUtil){
        java.sql.Date sqlDate = new java.sql.Date(dateUtil.getTime());
        return  sqlDate;
    }
    
    public static String setLeadingZero(String str, String format){
        NumberFormat formatter = new DecimalFormat(format);
        String res = formatter.format(Long.valueOf(str).longValue());
        return res;
    }
}
