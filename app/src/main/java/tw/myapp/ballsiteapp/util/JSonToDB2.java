package tw.myapp.ballsiteapp.util;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSonToDB2 {
    private String jsonString;
    private SQLiteDatabase db;

    public JSonToDB2(SQLiteDatabase db) {
        this.db = db;
    }

    public void writeToDatabase(String jsonString) {
        this.jsonString = jsonString;
        try {
            JSONArray rawData = new JSONArray(jsonString);
            for (int i = 0; i < rawData.length(); i++) {
                JSONObject jsonObject = rawData.getJSONObject(i);
                db.execSQL("insert into product values(?,?,?);",
                        new Object[]{
                                jsonObject.getString("site_id"),
                                jsonObject.getString("no_id"),
                                jsonObject.getString("category_id")
                        });
                //測試有沒有成功
                Log.d("JSON", jsonObject.getString("site_id") + ":" + jsonObject.getString("category_id"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
