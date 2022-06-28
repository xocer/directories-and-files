package dev.vbgrishin.directoriesandfilesapi.model;

import dev.vbgrishin.directoriesandfilesapi.repository.model.SubdirectoryEntity;

import java.util.Comparator;
import java.util.Locale;

public class DirectoryComparator implements Comparator<SubdirectoryEntity> {

    @Override
    public int compare(SubdirectoryEntity o1, SubdirectoryEntity o2) {
        if (o1.getSize().equals("DIR")) {
            return -1;
        } else if (o2.getSize().equals("DIR")) {
            return 1;
        }

        String firstStr = o1.getPath().toLowerCase(Locale.ROOT);
        String secondStr = o2.getPath().toLowerCase(Locale.ROOT);

        int firstCursor = 0;
        int secondCursor = 0;

        while (firstCursor < firstStr.length() && secondCursor < secondStr.length()) {
            // получаем кусочки строки, либо с числами, либо буквы, либо крест на крест
            String pathOfFirstStr = getSequence(firstStr, firstCursor);
            String pathOfSecondStr = getSequence(secondStr, secondCursor);
            firstCursor += pathOfFirstStr.length();
            secondCursor += pathOfSecondStr.length();

            int result;
            // если и там и там числа, то сравниваем их именно как числа
            if (isDigit(pathOfFirstStr.charAt(0)) && isDigit(pathOfSecondStr.charAt(0))) {
                result = pathOfFirstStr.length() - pathOfSecondStr.length();
                if (result == 0) {
                    for (int i = 0; i < pathOfFirstStr.length(); i++) {
                        result = pathOfFirstStr.charAt(i) - pathOfSecondStr.charAt(i);
                        if (result != 0) {
                            return result;
                        }
                    }
                }
            } else {
                // если мы тут, значит у нас либо буквы-буквы, либо буква-точка/число
                // так может получиться например когда у нас "f1.txt" и "f.txt"
                result = pathOfFirstStr.compareTo(pathOfSecondStr);
            }
            if (result != 0) {
                return result;
            }
        }

        return firstStr.length() - secondStr.length();
    }

    private boolean isDigit(char ch) {
        return ((ch >= 48) && (ch <= 57));
    }

    // метод идет по строке и записывает либо буквы, пока не дойдет до цифры,
    // либо цифры, пока не дойдет до буквы
    private String getSequence(String str, int cursor) {
        StringBuilder sb = new StringBuilder();
        char ch = str.charAt(cursor++);
        if (isDigit(ch)) {
            if (ch != 0) {
                sb.append(ch);
            }
            while (cursor < str.length()) {

                ch = str.charAt(cursor++);
                if (!isDigit(ch)) {
                    return sb.toString();
                } else if (ch != 0) {
                    sb.append(ch);
                }
            }
        } else if (ch == 46) { // 46 == "."
            sb.append(ch);
        } else {
            sb.append(ch);
            while (cursor < str.length()) {
                ch = str.charAt(cursor++);
                if (isDigit(ch) || ch == 46)
                    break;
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}
