package encryption.com.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import encryption.com.AES.Encrypt;
import encryption.com.convert.Convert;
import encryption.com.cybersafeencryption.MainActivity;
import encryption.com.cybersafeencryption.R;

public class EncryptService extends Service {
    NotificationManager nm;

    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        String typeSaving = intent.getStringExtra("type_saving");
        String fileName = intent.getStringExtra("fileName");
        String pathDirectory = intent.getStringExtra("pathDirectory");
        byte[] byteArray = intent.getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        Log.d("typeSaving", typeSaving);
        switch (typeSaving) {
            case "save_without_encryption":
               try {
                   OutputStream stream = new FileOutputStream(pathDirectory + "/" + fileName);
                   bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                   stream.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
                sendNotification("Image has been saved", bmp, pathDirectory, fileName);
                break;
            case "save_with_encryption":
                String key = intent.getStringExtra("key");
                encryptImage(key, bmp, pathDirectory, fileName);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void encryptImage(final String key, final Bitmap bitmap, final String pathDirectory, final String fileName) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Encrypt encrypt = new Encrypt(getApplicationContext(), key);
                    encrypt.EncryptBitmap(Convert.convertBitmapToByteArray(bitmap), pathDirectory + "/" + fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendNotification("Image has been encrypted", bitmap, pathDirectory, fileName);
                stopSelf();
            }
        }).start();
    }

    public float getImageFactor(Resources r) {
        DisplayMetrics metrics = r.getDisplayMetrics();
        return metrics.density / 7f;
    }

    private void sendNotification(String title, Bitmap bitmap, String pathDirectory, String fileName) {
        float multiplier = getImageFactor(getResources());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.icon_save_image)
                        .setContentTitle(title)
                        .setContentText(fileName)
                        .setLargeIcon(Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * multiplier), (int) (bitmap.getHeight() * multiplier), true));

        Intent resultIntent = new Intent();
        resultIntent.setAction(android.content.Intent.ACTION_VIEW);
        File file = new File(pathDirectory + "/" + fileName);
        resultIntent.setDataAndType(Uri.fromFile(file), "image/*");
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }


}