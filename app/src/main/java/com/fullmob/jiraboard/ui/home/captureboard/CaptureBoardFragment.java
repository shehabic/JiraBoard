package com.fullmob.jiraboard.ui.home.captureboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fullmob.jiraboard.BuildConfig;
import com.fullmob.jiraboard.R;
import com.fullmob.jiraboard.data.Board;
import com.fullmob.jiraboard.data.Column;
import com.fullmob.jiraboard.ui.BaseActivity;
import com.fullmob.jiraboard.ui.BaseFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptureBoardFragment extends BaseFragment implements CaptureBoardView {

    public static final String TAG = CaptureBoardFragment.class.getSimpleName();
    private static final int STORAGE_PERMISSIONS_REQUESTED_FOR_PICKER = 999;
    private String fileName;
    public static final int CAPTURE_IMAGE_REQUEST = 1002;
    public static final int PICK_IMAGE_REQUEST = 1001;
    private static final int STORAGE_PERMISSIONS_REQUESTED = 998;
    @Inject
    CaptureBoardPresenter presenter;

    private OnFragmentInteractionListener mListener;

    @BindView(R.id.board_preview)
    ImageView boardPreview;
    @BindView(R.id.message)
    TextView message;

    @OnClick(R.id.capture)
    void onCaptureClicked() {
        boardPreview.setImageDrawable(null);
        startCameraCapture();
    }

    @OnClick(R.id.pick)
    void onPickImageClicked() {
        boardPreview.setImageDrawable(null);
        startPickingFile();
    }

    public CaptureBoardFragment() {
    }

    public static CaptureBoardFragment newInstance() {
        CaptureBoardFragment fragment = new CaptureBoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBaseActivity().getApp().createCaptureBoardFragmentComponent(this).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_capture_board, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void onCameraImageReceived(int requestCode) {
        onCameraImageReceived(
            requestCode,
            fileName,
            FileProvider.getUriForFile(getBaseActivity(), BuildConfig.APPLICATION_ID, new File(fileName))
        );
    }

    public void onCameraImageReceived(int requestCode, String fileName, Uri photoUri) {
        boardPreview.setImageBitmap(null);
        int rotation = calculateCurrentRotation(photoUri, requestCode, fileName);
        presenter.onImageReceived(photoUri, rotation);
    }

    public int calculateCurrentRotation(Uri imageUri, int requestCode, String filePath) {
        int rotate = 0;
        if (requestCode == PICK_IMAGE_REQUEST) {
            rotate = getCameraPhotoOrientation(getActivity(), imageUri, filePath);
        } else if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            rotate = 90;
        }
        return rotate;
    }

    @Override
    public void showLoading() {
        getBaseActivity().showLoading();
    }

    @Override
    public void hideLoading() {
        getBaseActivity().hideLoading();
    }

    private void startPickingFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (!getBaseActivity().permissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUESTED);

                return;
            }
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void dispatchTakePictureIntent() {

        if (
            !getBaseActivity().permissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                !getBaseActivity().permissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                !getBaseActivity().permissionGranted(Manifest.permission.CAMERA)
            ) {
            requestPermissions(
                new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                },
                STORAGE_PERMISSIONS_REQUESTED
            );

            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getBaseActivity().getPackageManager()) != null) {

            File photo = null;
            try {
                photo = createImageFile();
                fileName = photo.toString();
            } catch (IOException ex) {
            }
            if (photo != null) {
                Uri photoURI = FileProvider.getUriForFile(getBaseActivity(), BuildConfig.APPLICATION_ID, photo);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
            }
        }


    }

    private void startCameraCapture() {
        dispatchTakePictureIntent();
    }

    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath) {
        int rotate = 0;
        try {
            context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    @Override
    public void showOutput(Board project) {
        String headerOutput = "Columns: " + project.getColumns().size();
        String output = "";
        int ticketCount = 0;
        for (int i = 0; i < project.getColumns().size(); i++) {
            Column col = project.getColumns().get(i);
            ticketCount += col.tickets.size();
            output += "in [" + col.text.toUpperCase() + "]: ";
            for (int j = 0; j < col.tickets.size(); j++) {
                output += col.tickets.get(j).text + " , ";
            }
            output += " | ";
        }
        output = headerOutput + ", Tickets: " + ticketCount + "\n" + output;
        message.setText(output);
        boardPreview.setImageBitmap(project.getBitmap());
    }

    @Override
    public void showErrorOccurred() {
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JiraBoard_" + timeStamp + "_";
        File storageDir = getBaseActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == FragmentActivity.RESULT_OK) {
            if (requestCode == CaptureBoardFragment.CAPTURE_IMAGE_REQUEST) {
                onCameraImageReceived(requestCode);
            } else if (requestCode == CaptureBoardFragment.PICK_IMAGE_REQUEST) {
                if (data != null && data.getData() != null) {

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    onCameraImageReceived(requestCode, filePath, data.getData());
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSIONS_REQUESTED) {
            dispatchTakePictureIntent();
        } else if (requestCode == STORAGE_PERMISSIONS_REQUESTED_FOR_PICKER) {

        }
    }

    public interface OnFragmentInteractionListener {
        void onCaptureBoardFragmentInteraction();
    }
}
