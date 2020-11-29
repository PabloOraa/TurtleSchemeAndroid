package com.turtlesketch.turtlesketch.ui.results;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.turtlesketch.turtlesketch.Multimedia.Multimedia;
import com.turtlesketch.turtlesketch.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArrayAdapterWithPhoto extends ArrayAdapter<Multimedia>
{
    ArrayList<Multimedia> media = new ArrayList<>();
    public ArrayAdapterWithPhoto(@NonNull Context context, int resource, @NonNull List<Multimedia> objects)
    {
        super(context, resource, objects);
    }

    /**
     * {@inheritDoc}
     * @param position
     * @param convertView
     * @param parent
     * @return View created for the list view.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater;
        if(convertView == null)
        {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_results_layout, new LinearLayout(getContext()), false);//set layout for displaying items
        }
        TextView title = convertView.findViewById(R.id.tv_result_title);
        TextView author = convertView.findViewById(R.id.tv_result_author);
        ImageView imageView = convertView.findViewById(R.id.im_result_cover);
        title.setText(media.get(position).getTitle());
        if(media.get(position).getActors_authors() != null)
            if(!media.get(position).getActors_authors().get(0).equalsIgnoreCase(""))
                if(media.get(position).getActors_authors().toString().contains("["))
                    if(media.get(position).getActors_authors().toString().contains("]"))
                        author.setText(media.get(position).getActors_authors().toString().split("\\[")[1].split("]")[0]);
                    else
                        author.setText(media.get(position).getActors_authors().toString().split("\\[")[1]);
                else
                    author.setText(media.get(position).getActors_authors().toString());
        if(media.get(position).getCover() != null)
            imageView.setImageBitmap(getBitmapFromURL(media.get(position).getCover()));
        return convertView;
    }

    public void setMedia(ArrayList<Multimedia> media)
    {
        this.media = media;
    }

    /**
     * Recovers the image that can be found on the web and creates the bitmap asociated to show it on the activity.
     * @param src URL of the web that contains the image.
     * @return Bitmap image of the content.
     */
    private Bitmap getBitmapFromURL(String src)
    {
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            if(src.contains("http") && !src.contains("https"))
                src = src.replace("http", "https");
            URL url = new URL(src);
            return  BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
