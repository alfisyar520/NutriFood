package com.example.nutrifoods.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nutrifoods.Model.MakananModel;
import com.example.nutrifoods.Model.Nutrisi;
import com.example.nutrifoods.Model.User;
import com.example.nutrifoods.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class PostActivity extends AppCompatActivity {

    private Button btn_post;
    private ImageView iv_makanan;
    private EditText et_nama_makanan;
    private Uri mImageUri;
    private String mUser;
    private String currentDate;
    private String currentTime;
    private TextView ganti;


    ProgressDialog pd;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;


    //model
    private MakananModel postMakanan;
    private User user;

    //tflite
    // presets for rgb conversion
    private static final int RESULTS_TO_SHOW = 3;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    // options for model interpreter
    private final Interpreter.Options tfliteOptions = new Interpreter.Options();
    // tflite graph
    private Interpreter tflite;
    // holds all the possible labels for model
    private List<String> labelList;
    // holds the selected image data as bytes
    private ByteBuffer imgData = null;
    // holds the probabilities of each label for non-quantized graphs
    private float[][] labelProbArray = null;
    // holds the probabilities of each label for quantized graphs
    private byte[][] labelProbArrayB = null;
    // array that holds the labels with the highest probabilities
    private String[] topLables = null;
    // array that holds the highest probabilities
    private String[] topConfidence = null;

    // input image dimensions for the Inception Model
    private int DIM_IMG_SIZE_X = 200;
    private int DIM_IMG_SIZE_Y = 200;
    private int DIM_PIXEL_SIZE = 3;

    // int array to hold image data
    private int[] intValues;

    // priority queue that will hold the top results from the CNN
    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postMakanan = new MakananModel();
        user = new User();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        btn_post = findViewById(R.id.btn_post_analisis);
        iv_makanan = findViewById(R.id.post_gambar);
        et_nama_makanan = findViewById(R.id.et_post_namaMakanan);
        ganti = findViewById(R.id.coba_ganti);

        getDataUserFirebase();

        //date
        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //time
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        currentTime = format.format(calendar.getTime());


        // initialize array that holds image data
        intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];

        //initilize graph and labels

        try {
            tflite = new Interpreter(loadModelFile(), tfliteOptions);
            labelList = loadLabelList();
            Log.d("label list", String.valueOf(labelList.size()));
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("eror ki", "masuk error");
        }

        // initialize byte array. The size depends if the input data needs to be quantized or not
        imgData = ByteBuffer.allocateDirect(4 * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());

        // initialize probabilities array. The datatypes that array holds depends if the input data needs to be quantized or not
        labelProbArray = new float[1][labelList.size()];


        // initialize array to hold top labels
        topLables = new String[RESULTS_TO_SHOW];
        // initialize array to hold top probabilities
        topConfidence = new String[RESULTS_TO_SHOW];

        iv_makanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().setAspectRatio(1, 1).start(PostActivity.this);
            }
        });

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (iv_makanan.getDrawable() == null){
                    Toast.makeText(PostActivity.this, "Silahkan pilih gambar terlebih dulu", Toast.LENGTH_SHORT).show();
                }else{
                    pd = new ProgressDialog(PostActivity.this);
                    pd.setMessage("Please wait.. ");
                    pd.show();

                    // get current bitmap from imageView
                    Bitmap bitmap_orig = ((BitmapDrawable) iv_makanan.getDrawable()).getBitmap();
                    // resize the bitmap to the required input size to the CNN
                    Bitmap bitmap = getResizedBitmap(bitmap_orig, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y);
                    // convert bitmap to byte array
                    convertBitmapToByteBuffer(bitmap);
                    // pass byte data to the graph

                    tflite.run(imgData, labelProbArray);

                    // display the results
                    //ganti.setText(printTopKLabels());

                    Intent view_start = new Intent(PostActivity.this, DashboardActivity.class);
                    tambahMakanan(printTopKLabels());
                    //startActivity(view_start);
                    //PostActivity.super.onBackPressed();
                }
            }
        });



    }

    private void getDataUserFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void tambahMakanan(final String top_makanan) {
        final String nama_makanan = et_nama_makanan.getText().toString();

        if (mImageUri != null) {
            final StorageReference storageReference = storage.getReference()
                    .child(System.currentTimeMillis()
                            + ".jpg");
            UploadTask uploadTask = storageReference.putFile(mImageUri);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        postMakanan.setImage(downloadUri.toString());
                        MakananModel postMakanan = new MakananModel(user.getId(), nama_makanan, downloadUri.toString(), user.getUsername(), currentDate, currentTime, top_makanan);

                        db.collection("Data Postingan").document().set(postMakanan);

                        Toast.makeText(PostActivity.this, "Makanan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(PostActivity.this, "Silahkan SWIPE kebawah", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                        PostActivity.super.onBackPressed();
                    } else {
                        Toast.makeText(PostActivity.this, "Gagal menambahkan makanan", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(this, "No Image Seleted", Toast.LENGTH_SHORT).show();
        }

    }

    // loads tflite graph from file
    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("model_train_30_epoch.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    // loads the labels from the label txt file in assets into a string array
    private List<String> loadLabelList() throws IOException {
        List<String> labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(this.getAssets().open("labels.txt")));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    // resizes bitmap to given dimensions
    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    // converts bitmap to byte array which is passed in the tflite graph
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // loop through all pixels
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                // get rgb values from intValues where each int holds the rgb values for a pixel.

                imgData.putFloat((((val >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                imgData.putFloat((((val >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                imgData.putFloat((((val) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);

            }
        }
    }

    // print the top labels and respective confidences
    private String printTopKLabels() {
        // add all results to priority queue
        for (int i = 0; i < labelList.size(); ++i) {
            sortedLabels.add(
                    new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }

        // get top results from priority queue
        final int size = sortedLabels.size();
        for (int i = 0; i < size; ++i) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            topLables[i] = label.getKey();
            topConfidence[i] = String.format("%.0f%%", label.getValue() * 100);
        }

        // set the corresponding textviews with the results
        //ganti.setText("1. "+topLables[2]);
        return (topLables[2]);
        /*
        label2.setText("2. "+topLables[1]);
        label3.setText("3. "+topLables[0]);
        Confidence1.setText(topConfidence[2]);
        Confidence2.setText(topConfidence[1]);
        Confidence3.setText(topConfidence[0]);

         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            iv_makanan.setImageURI(mImageUri);
        }
    }



}
