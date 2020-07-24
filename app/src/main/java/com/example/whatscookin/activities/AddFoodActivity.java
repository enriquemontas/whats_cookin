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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.whatscookin.models.Food;
import com.example.whatscookin.databinding.ActivityAddFoodBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

/**
 * This activity is used to add items to the virtual fridge
 */
public class AddFoodActivity extends AppCompatActivity {

    public static final String TAG = "AddFoodActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 11;
    private ActivityAddFoodBinding binding;
    private Button btnCaptureImage;
    private Button btnAdd;
    private EditText etName;
    private EditText etQuantity;
    private EditText etQuantityUnit;
    private ImageView ivFoodImage;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddFoodBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // TODO add barcode scanner
        btnCaptureImage = binding.btnCaptureImage;
        btnAdd = binding.btnAdd;
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
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
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