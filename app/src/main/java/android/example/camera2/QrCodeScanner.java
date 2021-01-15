package android.example.camera2;

import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.nio.ByteBuffer;

public class QrCodeScanner {

    QRCodeReader qrCodeReader;
    HandleDecodeTask handleDecodeTask;

    public QrCodeScanner(){
        qrCodeReader = new QRCodeReader();
        handleDecodeTask = new HandleDecodeTask();
    }

    public void scan(Image img) {
        handleDecodeTask.doInBackground(img);
    }

    private void zxingScanner(Image img){
        Result rawResult = null;
        try {
            if (img == null) throw new NullPointerException("cannot be null");
            ByteBuffer buffer = img.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            int width = img.getWidth();
            int height = img.getHeight();
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, width, height);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            rawResult = qrCodeReader.decode(bitmap);
            if (rawResult != null) {
                Log.d("zxingScanner: " , rawResult.toString());;
            }
        } catch (ReaderException ignored) {
            /* Ignored */
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void visionScanner(Image image) {
        //            ByteBuffer buffer = cameraImage.getPlanes()[0].getBuffer();
//            byte[] bytes = new byte[buffer.capacity()];
//            buffer.get(bytes);
//
//            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            Frame frameToProcess = new Frame.Builder().setBitmap(bitmap).build();
//            SparseArray<Barcode> detect = new BarcodeDetector.Builder(getApplicationContext()).build().detect(frameToProcess);
//            if(detect.size() > 0){
//                Toast.makeText(getApplicationContext(), "Barcode detected!", Toast.LENGTH_SHORT).show();
//            }
    }

    public class HandleDecodeTask extends AsyncTask<Image, Void, Void> {


        @Override
        protected Void doInBackground(Image... images) {
            zxingScanner(images[0]);
            return null;
        }
    }
}
