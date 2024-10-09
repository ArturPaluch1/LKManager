package LKManager.services.Comparators;

import java.util.Comparator;

public class AlphanumericComparator implements Comparator<String> {

    @Override
    public int compare(String s1, String s2) {

        int i = 0, j = 0;
        while (i < s1.length() && j < s2.length()) {
            char c1 = s1.charAt(i);
            char c2 = s2.charAt(j);

            if (Character.isDigit(c1) && Character.isDigit(c2)) {

                String num1 = getFullNumber(s1, i);
                String num2 = getFullNumber(s2, j);


                int compareResult = Integer.compare(Integer.parseInt(num1), Integer.parseInt(num2));
                if (compareResult != 0) {
                    return compareResult;
                }

                i += num1.length();
                j += num2.length();
            } else {
                if (c1 != c2) {
                    return c1 - c2;
                }
                i++;
                j++;
            }
        }

        return s1.length() - s2.length();
    }


    private String getFullNumber(String s, int start) {
        StringBuilder number = new StringBuilder();
        while (start < s.length() && Character.isDigit(s.charAt(start))) {
            number.append(s.charAt(start));
            start++;
        }
        return number.toString();
    }
}