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

        若包含中文建議使用 UTF-16 避免亂碼，若無中文哲建議使用 UTF-8，其他編碼類型建議參考官方文件
        https://developer.android.com/reference/java/nio/charset/Charset.html
      */
    private static final String CSV_CHARSET = "UTF_16";

    /*
        單元格分隔符號

        符合 RFC 4180 規範
      */
    private static final String CSV_SEPARATOR = ",";


    public static void toCsvFile(List<String> header, List<List<String>> contents) {
        try
        {
            File file = createFile();
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, CSV_CHARSET);
            BufferedWriter bw = new BufferedWriter(osw);

            writeOneLine(bw, header);
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
                // 雙引號包覆的內容表示一個完整的單元格
                oneLine.append("\"");
                // 若單元格內包含雙引號符號需要轉換為兩個雙引號
                oneLine.append(column.contains("\"") ? column.replaceAll("\"", "\"\"") : column);
                oneLine.append("\"");
                oneLine.append(CSV_SEPARATOR);
            }

            writer.write(oneLine.toString());
            writer.newLine();
        }
    }

}
