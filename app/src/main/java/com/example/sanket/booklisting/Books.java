package com.example.sanket.booklisting;

import android.graphics.Bitmap;

/**
 * Created by sanket on 05/03/17.
 */

public class Books {

    private Bitmap mBookImage;
    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private String mPrice;
    private String mRating;
    private String mButtonText;
    private String mInfoLink;
    private String mCurrencyCode;


    public Books(Bitmap bookImage, String title, String author, String publisher, String price, String rating, String buttonText, String infoLink,String currencyCode) {
        mBookImage = bookImage;
        mTitle = title;
        mAuthor = author;
        mPublisher = publisher;
        mPrice = price;
        mRating = rating;
        mButtonText = buttonText;
        mInfoLink = infoLink;
        mCurrencyCode = currencyCode;
    }

    public Bitmap getmBookImage() {
        return mBookImage;
    }

    public void setmBookImage(Bitmap mBookImage) {
        this.mBookImage = mBookImage;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmPublisher() {
        return mPublisher;
    }

    public void setmPublisher(String mPublisher) {
        this.mPublisher = mPublisher;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmRating() {
        return mRating;
    }

    public void setmRating(String mRating) {
        this.mRating = mRating;
    }

    public String getmButtonText() { return mButtonText; }

    public void setmButtonText(String mButtonText) { this.mButtonText = mButtonText; }

    public String getmInfoLink() { return mInfoLink; }

    public void setmInfoLink(String mInfoLink) { this.mInfoLink = mInfoLink; }

    public String getmCurrencyCode() {
        return mCurrencyCode;
    }

    public void setmCurrencyCode(String mCurrencyCode) {
        this.mCurrencyCode = mCurrencyCode;
    }
}
