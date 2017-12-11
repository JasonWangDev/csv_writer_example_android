package idv.dev.jason.csv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        List<String> header = new ArrayList<>();
        for (int i = 0 ; i < 10 ; i++)
            header.add("欄位 測試換行符號\r\n測試特殊符號,\" End Test " + (i + 1));

        List<List<String>> contents = new ArrayList<>();
        for(int i = 0 ; i < 20 ; i++)
        {
            List<String> oneLineColumns = new ArrayList<>();
            for (int j = 0 ; j < 10 ; j++)
                oneLineColumns.add("單元格 測試特殊符號,\" End Test " + (j + 1));
            contents.add(oneLineColumns);
        }

        CsvWriter.toCsvFile(header, contents);
    }

}
