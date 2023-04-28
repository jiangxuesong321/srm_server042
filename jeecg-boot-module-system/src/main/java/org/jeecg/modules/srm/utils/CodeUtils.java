package org.jeecg.modules.srm.utils;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;
import java.util.UUID;

public class CodeUtils {
    public static String[] chars = new String[] {"0","1","2","3","4","5","6","7","8","9" };


    public static String generateShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();

    }

    public static void main(String[] args) {
        String a= RandomStringUtils.random(8, "ABCDEFGHIJKLNMOPQRSTUVWXYZ1234567890");
        System.out.println(a);
    }
}
