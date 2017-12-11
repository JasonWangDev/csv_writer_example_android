package idv.dev.jason.csv;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by jason on 2017/12/8.
 */

public class CsvWriter {

    /*
        檔案儲存路徑
     */
    private static final String CSV_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator + "download" + File.separator;

    /*
        檔案副檔名
     */
    private static final String CSV_FILE__NAME_EXTENSION = ".csv";

    /*
        檔案編碼格式

        若包含中文內容建議使用 UTF-8，其他編碼類型建議參考官方文件

        https://developer.android.com/reference/java/nio/charset/Charset.html
      */
    private static final String CSV_FILE_CHARSET = "UTF-8";

    /*
        檔案編碼標頭

        採用 UFT-8 編碼文件，使用 Excel 軟體開啟時，內容會顯示異常出現亂碼。為解決此問題，需要在檔案
        開頭以位元流寫入 BOM(Byte Order Mark) 標籤

        http://jeiworld.blogspot.tw/2009/09/phpexcelutf-8csv.html

        https://en.wikipedia.org/wiki/Byte_order_mark
     */
    private static final byte[] CSV_FILE_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    /*
        單元格分隔符號

        符合 RFC 4180 規範
      */
    private static final String CSV_SEPARATOR = ",";


    public static void toCsvFile(List<String> title, List<List<String>> contents) {
        try
        {
            File file = createFile();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(CSV_FILE_BOM);

            OutputStreamWriter osw = new OutputStreamWriter(fos, CSV_FILE_CHARSET);
            BufferedWriter bw = new BufferedWriter(osw);

            writeOneLine(bw, title);
            for(List<String> oneLine : contents)
                writeOneLine(bw, oneLine);

            bw.flush();
            bw.close();
        }
        catch (UnsupportedEncodingException e) { }
        catch (FileNotFoundException e) { }
        catch (IOException e) { }
    }


    private static File createFile() throws IOException {
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        File file = new File(CSV_FILE_PATH + fileName + CSV_FILE__NAME_EXTENSION);
        if (!file.exists())
            file.createNewFile();
        return file;
    }


    private static void writeOneLine(BufferedWriter writer, List<String> oneLineColumns) throws IOException {
        if (null != writer && null != oneLineColumns)
        {
            StringBuffer oneLine = new StringBuffer();
            for (String column : oneLineColumns)
            {
                // 為程式撰寫方便需求，所以將原本應該放在單元格後面的分隔符號改為放在最前面
                oneLine.append(CSV_SEPARATOR);
                // 雙引號包覆的內容表示一個完整的單元格
                oneLine.append("\"");
                // 若單元格內包含雙引號符號需要轉換為兩個雙引號
                oneLine.append(null != column ? column.replaceAll("\"", "\"\"") : "");
                oneLine.append("\"");
            }

            // 移除第一個分隔符號(多餘)
            writer.write(oneLine.toString().replaceFirst(",", ""));
            writer.newLine();
        }
    }

}
