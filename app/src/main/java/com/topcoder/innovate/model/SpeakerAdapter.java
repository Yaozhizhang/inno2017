package com.topcoder.innovate.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.topcoder.innovate.innovate2017.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by Jie Yao on 2017/9/2.
 */

public class SpeakerAdapter extends ArrayAdapter<Speaker> {
    private int resourceId;
    private Context activity;
    public SpeakerAdapter(Context context, int resource,List<Speaker> objects) {
        super(context, resource, objects);
        resourceId = resource;
        activity = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Speaker speaker = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.photo = (ImageView)view.findViewById(R.id.photo);
            viewHolder.name = (TextView)view.findViewById(R.id.name);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        int photosrc = activity.getResources().getIdentifier(getpicturename(speaker.getPicture()),"drawable",activity.getPackageName());
        if(photosrc>0)
        {
            viewHolder.photo.setImageResource(photosrc);
        }
        viewHolder.name.setText(speaker.getName());
        viewHolder.title.setText(speaker.getTitle());

        return view;
    }
    class ViewHolder{
        ImageView photo ;
        TextView name ;
        TextView title ;
    }
    private String getpicturename(String str){
        str.trim();
        String [] temp = str.split("/");
        String name = temp[temp.length-1];
        name = name.split("[.]")[0];
        name = name.toLowerCase(Locale.getDefault());
        if(name.charAt(0)>='0'&&name.charAt(0)<='9')
        {
            name = "x"+name;
        }
        return name;
    }
}
