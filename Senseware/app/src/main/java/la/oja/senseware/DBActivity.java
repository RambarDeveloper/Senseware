package la.oja.senseware;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import la.oja.senseware.data.sensewareDataSource;
import la.oja.senseware.data.sensewareDbHelper;

public class DBActivity extends AppCompatActivity {

    public ArrayList<String> listItems=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db);
        sensewareDbHelper sDbHelper = new sensewareDbHelper(getApplicationContext());
        SQLiteDatabase db = sDbHelper.getReadableDatabase();

        Cursor c = null;
        try
        {
            String[] projection = {
                    sensewareDataSource.Lesson._ID,
                    sensewareDataSource.Lesson.COLUMN_NAME_TITLE,
                    sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY
            };

            String whereCol = ""; //sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY + " = "+ String.valueOf(id_day);

            String sortOrder = sensewareDataSource.Lesson.COLUMN_NAME_ID_DAY + " ASC";

            c = db.query(
                    sensewareDataSource.Lesson.TABLE_NAME,      // The table to query
                    projection,                                 // The columns to return
                    whereCol,                                   // The columns for the WHERE clause
                    null,                                // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    sortOrder                                        // The sort order
            );

            if (c.moveToFirst())
            {
                int i = 0;
                do {
                    listItems.add(c.getString(1) + " > " + c.getString(2));
                    i++;
                } while (c.moveToNext());

                c.close();
                db.close();
            }

            ListView lv  =(ListView)findViewById(R.id.listView);

            ArrayAdapter<String> adapter;
            adapter=new ArrayAdapter<String>(DBActivity.this,
                    android.R.layout.simple_list_item_1,
                    listItems);
            lv.setAdapter(adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(c != null)
                c.close();
            if(!c.isClosed())
                db.close();
        }
    }
}
