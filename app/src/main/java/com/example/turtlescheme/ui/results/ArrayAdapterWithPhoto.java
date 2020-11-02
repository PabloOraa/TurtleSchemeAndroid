package com.example.turtlescheme.ui.results;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turtlescheme.Multimedia.Multimedia;
import com.example.turtlescheme.R;

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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.list_results_layout, null);//set layout for displaying items
        TextView title = (TextView) convertView.findViewById(R.id.tv_result_title);
        TextView author = (TextView) convertView.findViewById(R.id.tv_result_author);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.im_result_cover);
        title.setText(media.get(position).getTitle());
        author.setText(media.get(position).getActors_authors().toString().split("\\[")[1].split("]")[0]);
        imageView.setImageBitmap(getBitmapFromURL(media.get(position).getCover()));
        return convertView;
    }

    public void setMedia(ArrayList<Multimedia> media)
    {
        this.media = media;
    }

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
