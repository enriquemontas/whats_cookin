package com.example.whatscookin.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatscookin.Keys;
import com.example.whatscookin.R;
import com.example.whatscookin.models.Food;
import com.example.whatscookin.databinding.ActivityAddFoodBinding;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This activity is used to add items to the virtual fridge
 */
public class AddFoodActivity extends AppCompatActivity {

    public static final String TAG = "AddFoodActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 11;
    private EditText etName;
    private EditText etQuantity;
    private EditText etQuantityUnit;
    private ImageView ivFoodImage;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    private Barcode barcode;
    private Bitmap takenImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActivityAddFoodBinding binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // TODO add barcode scanner
        final Button btnCaptureImage = binding.btnCaptureImage;
        final Button btnScan = binding.btnScan;
        final Button btnAdd = binding.btnAdd;
        etName = binding.etName;
        etQuantity = binding.etQuantity;
        etQuantityUnit = binding.etQuantityUnit;
        ivFoodImage = binding.ivFoodImage;

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // tries to scan an empty image
                if (takenImage == null) {
                    Toast.makeText(AddFoodActivity.this, "take an image", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BarcodeDetector detector =
                                    new BarcodeDetector.Builder(getApplicationContext())
                                            .setBarcodeFormats(Barcode.UPC_A | Barcode.UPC_E)
                                            .build();

                            if (!detector.isOperational()) {
                                Log.i(TAG, "problem with detector");
                            }

                            Frame frame = new Frame.Builder().setBitmap(takenImage).build();
                            SparseArray<Barcode> barcodes = detector.detect(frame);

                            barcode = barcodes.valueAt(0);
                            queryBarcode();
                        } catch (IOException | JSONException e) {
                            Log.e(TAG, "query barcode: ", e);
                        }
                    }
                }).start();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                int quantity = Integer.parseInt(etQuantity.getText().toString());
                String quantityUnit = etQuantityUnit.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(AddFoodActivity.this, "Add a name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (photoFile == null || ivFoodImage.getDrawable() == null){
                    Toast.makeText(AddFoodActivity.this, "No photo!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(name, currentUser, photoFile, quantity, quantityUnit);
            }
        });

    }

    // query the api given a scanned barcode to get product info
    private void queryBarcode() throws IOException, JSONException {
        final String API_URL = "https://api.barcodelookup.com/v2/products?barcode=" + barcode.rawValue + "&formatted=y&key=" + Keys.getBarcodeApiKey();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(API_URL).build();

        Response response = client.newCall(request).execute();

        final String responseData = response.body().string();
        JSONObject json = new JSONObject(responseData);
        final JSONObject product = json.getJSONArray("products").getJSONObject(0);
        populate(product);

    }

    // extract information from response and populate fields
    private void populate(JSONObject product) throws JSONException {
        Looper.prepare();
        etName.setText(product.getString("product_name"));
        Glide.with(getApplicationContext()).load(product.getJSONArray("images").get(0));

    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(AddFoodActivity.this, "com.example.whatscookin", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(AddFoodActivity.this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivFoodImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(AddFoodActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(AddFoodActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    /**
     * Given field values this method creates a new food object and saves it to parse
     * @param name name of the object
     * @param currentUser owner of the object
     * @param photoFile photo of object
     * @param quantity how many units the user is inserting
     * @param quantityUnit the unit the user will measure the quantity with
     */
    private void savePost(String name, ParseUser currentUser, File photoFile, int quantity, String quantityUnit) {
        Food food = new Food();
        food.setName(name);
        food.setOwner(currentUser);
        food.setImage(new ParseFile(photoFile));
        food.setCurrentQuantity(quantity);
        food.setOriginalQuantity(quantity);
        food.setQuantityUnit(quantityUnit);
        food.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null ){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(AddFoodActivity.this, "problem in saving that", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "saving was successful");
                etName.setText("");
                etQuantity.setText("");
                etQuantityUnit.setText("");
                ivFoodImage.setImageResource(0);
            }
        });
    }
}