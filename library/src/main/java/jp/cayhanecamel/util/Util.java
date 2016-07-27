package jp.cayhanecamel.util;

import java.io.FileInputStream;
import java.io.IOException;

public class Util {

    /**
     * テキストファイルかどうかを判定する
     *
     * @param filePath テキストファイル
     * @return trueならテキストファイル falseならバイナリファイル
     */
    public static boolean isTextFile(String filePath) {

        FileInputStream in = null;
        try {
            in = new FileInputStream(filePath);

            byte[] b = new byte[1];
            while (in.read(b, 0, 1) > 0) {
                if (b[0] == 0) {
                    return false;
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
        }
    }
}
