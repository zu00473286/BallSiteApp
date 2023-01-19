package tw.myapp.ballsiteapp.util;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSonToDB {
    private  String jsonString;
    private SQLiteDatabase db;

    // 接收 JSON 後寫入 DB
    // 接收需求: JSON &　SQLite DB


    public JSonToDB( SQLiteDatabase db) {
        this.db = db;
    }

    public void writeToDatabase(String jsonString) {
        this.jsonString = jsonString;

        try {
            JSONObject rawData = new JSONObject(jsonString);
            JSONArray records = rawData.getJSONObject("XML_Head").getJSONObject("Infos").getJSONArray("Info");
            for(int i=0; i< records.length() ; i++) {
                JSONObject obj = records.getJSONObject(i);
                db.execSQL("insert into restaurant values(?,?,?,?);",
                        new Object[] {
                                obj.getString("Id"),
                                obj.getString("Name"),
                                obj.getString("email"),
                                obj.getString("Tel"),
                        });
                //Log.d("JSON" , obj.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
