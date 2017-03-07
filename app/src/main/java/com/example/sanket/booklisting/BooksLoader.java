package com.example.sanket.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.author;
import static android.R.attr.installLocation;
import static android.R.attr.subtypeLocale;
import static com.example.sanket.booklisting.R.id.authors;

/**
 * Created by sanket on 05/03/17.
 */

public class BooksLoader extends AsyncTaskLoader<List<Books>> {

    private String link="";
    public BooksLoader(Context context,String url)
    {
        super(context);
        link = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {

        URL url = createUrl(link);
        String json = "";

        try
        {
            json = makeHttpRequest(url);
        }
        catch(IOException e)
        {
            Log.e("BooksLoader","IOException" ,e);
        }

        if(TextUtils.isEmpty(json))
        {
            return null;
        }

        List<Books> books = new ArrayList<Books>();
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray items = jsonObject.getJSONArray("items");

            for(int i=0; i<items.length(); i++)
            {
                String title = "";
                String publisher = "";
                String rating = "";
                String author = "";
                Bitmap bmp = null;
                String saleability = "";
                String price = "";
                String buttonText = "";
                String infoLink = "";
                String currencyCode = "";

                JSONObject obj = items.getJSONObject(i);
                JSONObject volumeInfo = obj.getJSONObject("volumeInfo");
                JSONObject saleInfo = obj.getJSONObject("saleInfo");

                if(volumeInfo.has("title")) {
                    title = volumeInfo.getString("title");
                }
                else
                {
                    title = getContext().getString(R.string.no_title);
                }
                if(volumeInfo.has("publisher")) {
                    publisher = volumeInfo.getString("publisher");
                }
                else
                {
                    publisher = getContext().getString(R.string.no_publisher);
                }

                if(volumeInfo.has("averageRating")) {
                    rating = String.valueOf(volumeInfo.getDouble("averageRating"));
                }
                else
                {
                    rating = getContext().getString(R.string.no_rating);
                }

                if(volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authors.length(); j++) {
                        author = author + authors.getString(j) + " ";
                    }
                }
                else
                {
                    author = Resources.getSystem().getString(R.string.no_author);
                }

                if(volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    String thumbnail = imageLinks.getString("thumbnail");

                    try {
                        URL url1 = new URL(thumbnail);
                        bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());

                    } catch (MalformedURLException e) {
                        Log.i("BooksLoader", "URL empty or bitmap not formed");
                    } catch (IOException e1) {
                        Log.i("BooksLoader", "connection failed");
                    }
                }

                if(saleInfo.has("saleability")) {
                    saleability = saleInfo.getString("saleability");

                    if (saleability.equals("NOT_FOR_SALE")) {
                        price = getContext().getString(R.string.not_for_sale);
                        currencyCode = "";
                        buttonText = getContext().getString(R.string.info_link);
                        infoLink = volumeInfo.getString("infoLink");
                    } else if (saleability.equals("FOR_SALE")) {
                        JSONObject listPrice = saleInfo.getJSONObject("listPrice");
                        Double temp = (listPrice.getDouble("amount"));
                        price = java.text.NumberFormat.getNumberInstance().format(temp);
                        currencyCode = listPrice.getString("currencyCode");
                        buttonText = getContext().getString(R.string.buy);
                        infoLink = saleInfo.getString("buyLink");
                    } else if (saleability.equals("FREE")) {
                        price = getContext().getString(R.string.free);
                        currencyCode = "";
                        buttonText = getContext().getString(R.string.free_link);
                        infoLink = saleInfo.getString("buyLink");
                    }
                }

                books.add(new Books(bmp,title,author,publisher,price,rating,buttonText,infoLink,currencyCode));
            }

        }
        catch (JSONException e)
        {
            Log.e("BooksLoader", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }

    public static URL createUrl(String link)
    {
        URL url = null;
        try
        {
            url = new URL(link);
        }catch (MalformedURLException e) {
            Log.e("BooksLoader", "Error with creating URL ", e);
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException
    {
        String jsonResponse = "";

        if(url == null)
        {
            return jsonResponse;
        }

        HttpURLConnection connection = null;
        InputStream input = null;

        try
        {
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() == 200)
            {
                input = connection.getInputStream();
                jsonResponse = readFromStream(input);
            }
            else
            {
                Log.e("BooksLoader","Error response code :" + connection.getResponseCode()+connection.getResponseMessage());
            }
        }catch (IOException e)
        {
            Log.e("BooksLoader","Problem retrieving string" ,e);
        }

        return jsonResponse;
    }

    public static String readFromStream(InputStream is) throws IOException
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(is != null) {
            InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();

            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }
        return stringBuilder.toString();
    }

}
