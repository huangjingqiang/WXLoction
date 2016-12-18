package hjq.com.wxloction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;

import java.io.File;
import java.io.IOException;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv;
    private TextView tv;
    private final int REQUEST_IMAGE = 0x111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) this.findViewById(R.id.main_iv);
        tv = (TextView) this.findViewById(R.id.main_tv);

        tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MultiImageSelector.create()
                .showCamera(false)
                .single()
                .start(MainActivity.this,REQUEST_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_IMAGE){
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if (path != null && path.size() > 0) {
                    String tmpAvatarLocalPath = path.get(0);
                    Glide.with(this)
                            .load(tmpAvatarLocalPath)
                            .into(iv);

                    File file = new File(tmpAvatarLocalPath);
                    try {
                        Metadata metadata = ImageMetadataReader.readMetadata(file);

                    } catch (ImageProcessingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }
}
