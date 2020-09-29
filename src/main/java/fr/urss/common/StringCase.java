package fr.urss.common;

import fr.urss.security.domain.Authority;

public class StringCase {

    public static void main(String[] args) {
        for (var a : Authority.values())
            System.out.println(toSnakeCase(a.toString()));
    }

    public static String toSnakeCase(String string) {
        var builder = new StringBuilder();
        var sequence = true;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isUpperCase(string.charAt(i))) {
                if (!sequence) builder.append('_');
                builder.append(Character.toLowerCase(string.charAt(i)));
                sequence = true;
            } else {
                builder.append(string.charAt(i));
                sequence = false;
            }
        }
        return builder.toString();
    }

}
