package com.fibelatti.raffler.views.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.fibelatti.raffler.models.Group;
import com.fibelatti.raffler.models.GroupItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * Created by fibelatti on 03/08/16.
 */
public class FileHelper {
    private Context context;

    private final String jsonPropertyGroupName = "name";
    private final String jsonPropertyGroupItems = "items";

    public FileHelper(Context context) {
        this.context = context;
    }

    public String getGroupFilePath() {
        return context.getFilesDir() + File.separator + Constants.FILE_PATH_EXPORTED_GROUP;
    }

    public boolean createFileFromGroup(Group group) {

        JSONObject groupJson = new JSONObject();
        try {
            groupJson.put(jsonPropertyGroupName, group.getName());
            groupJson.put(jsonPropertyGroupItems, new JSONArray(group.getItemNames()));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        writeToFile(groupJson.toString());

        return true;
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(Constants.FILE_PATH_EXPORTED_GROUP, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(context.getClass().getSimpleName(), "File write failed: " + e.toString());
        }
    }

    public Group readFromFile(Uri uri) {
        String json = readJsonStringFromUri(uri);

        return convertJsonToGroup(json);
    }

    private String readJsonStringFromUri(Uri data) {
        final String scheme = data.getScheme();

        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            try {
                ContentResolver contentResolver = context.getContentResolver();
                InputStream inputStream = contentResolver.openInputStream(data);
                if (inputStream != null) {
                    int size = inputStream.available();
                    byte[] buffer = new byte[size];

                    if (inputStream.read(buffer) > 0) {
                        return new String(buffer, "UTF-8");
                    }
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    private Group convertJsonToGroup(String json) {
        Group group = new Group();

        try {
            JSONObject groupJson = new JSONObject(json);
            JSONArray groupItemsJson = groupJson.getJSONArray(jsonPropertyGroupItems);

            group.setName(groupJson.getString(jsonPropertyGroupName));

            for (int i = 0; i < groupItemsJson.length(); i++) {
                group.getItems().add(new GroupItem(groupItemsJson.get(i).toString()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return group;
    }
}