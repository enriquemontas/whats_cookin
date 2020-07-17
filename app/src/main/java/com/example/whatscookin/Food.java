package com.example.whatscookin;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze={Food.class})
@ParseClassName("Food")
public class Food extends ParseObject {

    public static final String KEY_OWNER = "owner";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_NAME = "name";
    public static final String KEY_BARCODE = "barcode";
    public static final String KEY_CALORIES = "calories";
    public static final String KEY_SERVING_SIZE = "servingSize";

    // empty constructor for parse
    public Food() { }

    public ParseUser getOwner() { return getParseUser(KEY_OWNER); }

    public ParseFile getImage() { return getParseFile(KEY_IMAGE); }

    public String getName() { return getString(KEY_NAME); }

    public int getBarcode() { return getInt(KEY_BARCODE); }

    public int getCalories() { return getInt(KEY_CALORIES); }

    public String getServingSize() { return getString(KEY_SERVING_SIZE); }

    public void setOwner(ParseUser user) { put(KEY_OWNER, user); }

    public void setImage(ParseFile image) { put(KEY_IMAGE, image);}

    public void setName(String name) { put(KEY_NAME, name); }

    public void setBarcode(int image) { put(KEY_BARCODE, image);}

    public void setCalories(int calories) {put(KEY_CALORIES, calories);}

    public void setServingSize(String servingSize) { put(KEY_SERVING_SIZE, servingSize);}
}
