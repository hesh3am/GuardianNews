package com.example.hesham.guardiannews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Hesham on 20-Aug-18.
 */

public class GuardianAdapter extends ArrayAdapter<Guardian> {

    public GuardianAdapter(Context context, ArrayList<Guardian> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list, parent, false);
        }
        /////////////////////////////////////////////////Get the position
        Guardian currentNews = getItem(position);

        ////////////////////////////////////////////////Author VIEW
        TextView AtextView = listItem.findViewById(R.id.author);
        String authorString = currentNews.getAuthor();
        if(authorString.equals("")){
            authorString = getContext().getString(R.string.noauthorfound);
        }
        AtextView.setText(authorString);

        ////////////////////////////////////////////////Title VIEW
        TextView TTextView = listItem.findViewById(R.id.title);
        String titleString = currentNews.getTitle();
        TTextView.setText(titleString);

        ////////////////////////////////////////////////Data VIEW
        TextView DtextView = listItem.findViewById(R.id.date);
        String dateString = currentNews.getDate();
        DtextView.setText(dateString);

        ////////////////////////////////////////////////Category VIEW
        TextView CtextView = listItem.findViewById(R.id.catagory);
        String categoryString = currentNews.getCategory();
        CtextView.setText(categoryString);

        return listItem;
    }
}
