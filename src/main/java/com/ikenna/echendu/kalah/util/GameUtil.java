package com.ikenna.echendu.kalah.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameUtil {

    public static String getNumberSequence() {
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(999999999) + 1;
        String paddedNumStr = StringUtils.leftPad(Integer.toString(number), 9, '0');
        StringBuilder sb = new StringBuilder(paddedNumStr);
        sb.insert(3, '-');
        sb.insert(7, '-');
        return sb.toString();
    }
}
