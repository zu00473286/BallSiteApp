package tw.myapp.ballsiteapp.util;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSonToDB {
    private String jsonString;
    private SQLiteDatabase db;

    public JSonToDB(SQLiteDatabase db) {
        this.db = db;
    }

    public void writeToDatabase(String jsonString) {
        this.jsonString = jsonString;
        try {
            JSONArray rawData = new JSONArray(jsonString);
            for (int i = 0; i < rawData.length(); i++) {
                JSONObject jsonObject = rawData.getJSONObject(i);
                db.execSQL("insert into product values(?,?,?,?,?,?,?);",
                        new Object[]{
                                jsonObject.getInt("member_id"),
                                jsonObject.getInt("name"),
                                jsonObject.getString("mobile"),
                                jsonObject.getInt("email"),
                                jsonObject.getInt("passwd"),
                                jsonObject.getInt("money"),
                        });
                //測試有沒有成功
                Log.d("JSON", jsonObject.getString("name") + ":" + jsonObject.getInt("calorie"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}