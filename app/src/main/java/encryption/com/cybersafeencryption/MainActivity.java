package encryption.com.cybersafeencryption;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import encryption.com.Database.DatabaseHelper;
import encryption.com.adapters.PagerAdapter;
import encryption.com.fragments.PageMainMenuFragment;
import encryption.com.fragments.PageViewDrawingsFragment;
import encryption.com.service.EncryptService;

public class MainActivity extends AppCompatActivity  {
    private Bitmap mBitmap;
    private Boolean mSaveWithEncryption;
    private String mFileName;
    private String mKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewPager();
    }
    private void initViewPager()
    {
        PageMainMenuFragment tabFragmentMainMenu = new PageMainMenuFragment();
        PageViewDrawingsFragment tabFragmentViewDrawings = new PageViewDrawingsFragment();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(tabFragmentMainMenu);
        fragments.add(tabFragmentViewDrawings);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        if (viewPager != null) {
            viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),
                    fragments));
        }
       /* TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        if (tabLayout != null)
            tabLayout.setupWithViewPager(viewPager);

        if (tabLayout != null && viewPager != null) {
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }*/

    }



    protected void onStart() {
        super.onStart();
    }

    public void saveImage(Bitmap bitmap, Boolean savingType, String fileName, String key) {
        mBitmap = bitmap;
        mSaveWithEncryption = savingType;
        mFileName = fileName;
        mKey = key;
        Intent intent = new Intent(this, DirectoryPicker.class);
        startActivityForResult(intent, DirectoryPicker.PICK_DIRECTORY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DirectoryPicker.PICK_DIRECTORY) {
            Bundle extras = data.getExtras();
            String pathDirectory = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);

            Intent intent = new Intent(this, EncryptService.class);

            byte[] byteArray = null;
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                stream.close();
            } catch (IOException e) {
            }
            intent.putExtra("fileName", mFileName);
            intent.putExtra("image", byteArray);
            intent.putExtra("pathDirectory", pathDirectory);
            if (mSaveWithEncryption) {
                Toast.makeText(MainActivity.this, "Encryption", Toast.LENGTH_LONG).show();
                intent.putExtra("type_saving", "save_with_encryption");
                intent.putExtra("key", mKey);
            } else {
                Toast.makeText(MainActivity.this, "Saving", Toast.LENGTH_LONG).show();
                intent.putExtra("type_saving", "save_without_encryption");
            }
            startService(intent);
           /* if (!mSaveWithEncryption) {
                try {
                    OutputStream stream = new FileOutputStream(pathDirectory + "/" + mFileName);
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    stream.close();
                } catch (IOException e) {
                    Log.d("SAVING Image", "Not correct path");
                }
            } else {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    Toast.makeText(MainActivity.this, "TTT", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(this, EncryptService.class);
                    intent.putExtra("image", byteArray);
                    intent.putExtra("key", mKey);
                    intent.putExtra("pathDirectory", pathDirectory);
                    intent.putExtra("fileName", mFileName);

                    startService(intent);

                    stream.close();
                } catch (IOException e) {
                    Log.d("SAVING Image", "Not correct path");
                }
            }*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
