package com.emoge.app.emoge.ui.gallery;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jh on 17. 8. 8.
 */

public class ImageFormatChecker {
    public static final List<String> IMAGE_FORMAT = Arrays.asList(".PNG", ".JPEG", "jpg", ".png", ".jpeg", ".JPG");
    public static final List<String> GIF_FORMAT = Arrays.asList(".GIF", ".gif");

    public static boolean inFormat(File file, List<String> formats) {
        if (file == null || !file.isFile()) {
            return false;
        }
        String name = file.getName();
        if (name.startsWith(".") || file.length() == 0) {
            return false;
        }
        for(String format : formats) {
            if (name.endsWith(format)) {
                return true;
            }
        }
        return false;
    }
}
