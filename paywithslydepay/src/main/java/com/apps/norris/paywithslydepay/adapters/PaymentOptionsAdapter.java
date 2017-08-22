package com.apps.norris.paywithslydepay.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.apps.norris.paywithslydepay.R;
import com.apps.norris.paywithslydepay.core.PayWithSlydepay;
import com.apps.norris.paywithslydepay.models.PaymentOption;
import com.apps.norris.paywithslydepay.views.CustomTextView;
import com.apps.norris.paywithslydepay.views.MaterialProgressBar;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by norrisboateng on 6/7/17.
 */

public class PaymentOptionsAdapter extends RecyclerView.Adapter<PaymentOptionsAdapter.ViewHolder> {

    private ArrayList<PaymentOption> paymentOptions;
    private PayWithSlydepay context;

    public PaymentOptionsAdapter(PayWithSlydepay context, ArrayList<PaymentOption> paymentOptions) {
        this.paymentOptions = paymentOptions;
        this.context = context;
    }

    @Override
    public PaymentOptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_options_layout,parent,false);
        return new PaymentOptionsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PaymentOptionsAdapter.ViewHolder holder, int position) {
        final PaymentOption getPaymentOption = paymentOptions.get(position);
        holder.name.setText(getPaymentOption.getName());
        new DownloadImageTask(holder.logo,holder.progressBar).execute(getPaymentOption.getLogoURL());

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.paymentOption = getPaymentOption.getShortName();
                context.startTransaction();
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentOptions.size();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        CustomTextView name;
        ImageView logo;
        RelativeLayout container;
        MaterialProgressBar progressBar;

        ViewHolder(View convertView) {
            super(convertView);
            name = (CustomTextView) convertView.findViewById(R.id.name);
            logo = (ImageView) convertView.findViewById(R.id.logo);
            container = (RelativeLayout) convertView.findViewById(R.id.container);
            progressBar = (MaterialProgressBar) convertView.findViewById(R.id.progress_bar);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        MaterialProgressBar progressBar;

        DownloadImageTask(ImageView bmImage,MaterialProgressBar progressBar) {
            this.bmImage = bmImage;
            this.progressBar = progressBar;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap icon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                icon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return icon;
        }

        protected void onPostExecute(Bitmap result) {
            progressBar.setVisibility(View.GONE);
            bmImage.setImageBitmap(result);
        }
    }
}
