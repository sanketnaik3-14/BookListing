package com.example.sanket.booklisting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.start;

/**
 * Created by sanket on 05/03/17.
 */

public class BooksAdapter extends ArrayAdapter<Books> {

    public BooksAdapter(Context context, List<Books>books)
    {
        super(context,0,books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        final Books current_book = getItem(position);

        TextView titleView = (TextView)listItemView.findViewById(R.id.title);
        titleView.setText(current_book.getmTitle());

        TextView authorView = (TextView)listItemView.findViewById(R.id.authors);
        authorView.setText(current_book.getmAuthor());

        TextView publisherView = (TextView)listItemView.findViewById(R.id.publishers);
        publisherView.setText(current_book.getmPublisher());

        TextView ratingView = (TextView)listItemView.findViewById(R.id.rating);
        ImageView starView = (ImageView)listItemView.findViewById(R.id.star);

        if(current_book.getmRating().equals("No Rating")) {
            ratingView.setText(current_book.getmRating());
            starView.setVisibility(View.GONE);
        }
        else
        {
            ratingView.setText(current_book.getmRating());
            starView.setVisibility(View.VISIBLE);
        }

        TextView priceView = (TextView)listItemView.findViewById(R.id.price);
        priceView.setText(current_book.getmPrice());

        TextView currencyView = (TextView)listItemView.findViewById(R.id.currency);
        currencyView.setText(current_book.getmCurrencyCode());

        ImageView bookView = (ImageView)listItemView.findViewById(R.id.book_imageview);
        bookView.setImageBitmap(current_book.getmBookImage());

        Button button = (Button)listItemView.findViewById(R.id.button);
        button.setText(current_book.getmButtonText());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = current_book.getmInfoLink();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                Intent chooser = Intent.createChooser(browserIntent, "choose");
                v.getContext().startActivity(chooser);

            }
        });

        return listItemView;
    }
}
