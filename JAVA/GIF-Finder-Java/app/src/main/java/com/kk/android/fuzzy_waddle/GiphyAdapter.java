package com.kk.android.fuzzy_waddle;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.ByteBufferUtil;
import com.kk.android.fuzzy_waddle.model.GiphyImage;
import com.kk.android.fuzzy_waddle.ui.AspectRatioImageView;
import com.kk.android.fuzzy_waddle.util.Util;
import com.kk.android.fuzzy_waddle.util.ViewUtil;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

class GiphyAdapter extends RecyclerView.Adapter<GiphyAdapter.GiphyViewHolder> {

  private static final String TAG = GiphyAdapter.class.getSimpleName();

  private final Context context;

  private List<GiphyImage> images;
  private OnGiphyImageClickListener listener;

  class GiphyViewHolder extends RecyclerView.ViewHolder implements RequestListener<Drawable> {

    private AspectRatioImageView thumbnail;
    private GiphyImage image;
    private ProgressBar gifProgress;
    private volatile boolean modelReady;

    GiphyViewHolder(View view) {
      super(view);
      thumbnail = ViewUtil.findById(view, R.id.thumbnail);
      gifProgress = ViewUtil.findById(view, R.id.gif_progress);
      thumbnail.setOnClickListener(thumbnailView -> {
        if (listener != null) {
          listener.onGiphyImageClicked(images.get(getAdapterPosition()));
        }
      });
      gifProgress.setVisibility(View.GONE);
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
      synchronized (this) {
        this.modelReady = true;
        notifyAll();
      }

      return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
      synchronized (this) {
        this.modelReady = true;
        notifyAll();
      }

      return false;
    }

    public byte[] getData(boolean forMms) throws ExecutionException, InterruptedException {
      synchronized (this) {
        while (!modelReady) {
          Util.wait(this, 0);
        }
      }

      GifDrawable drawable = Glide.with(context).asGif()
              .load(forMms ? image.getGifMmsUrl() :
                      image.getGifUrl())
              .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
              .get();

      return ByteBufferUtil.toBytes(drawable.getBuffer());
    }

    public synchronized void setModelReady() {
      this.modelReady = true;
      notifyAll();
    }
  }

  GiphyAdapter(@NonNull Context context, @NonNull List<GiphyImage> images) {
    this.context = context.getApplicationContext();
    this.images = images;
  }

  public void setImages(@NonNull List<GiphyImage> images) {
    this.images = images;
    notifyDataSetChanged();
  }

  public void addImages(List<GiphyImage> images) {
    int start = this.images.size();
    this.images.addAll(images);
    notifyItemRangeChanged(start, this.images.size()); //eliminate flicker in new loads
  }

  @Override
  public @NonNull
  GiphyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.giphy_thumbnail, parent, false);

    return new GiphyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull GiphyViewHolder holder, int position) {
    GiphyImage image = images.get(position);

    holder.modelReady = false;
    holder.image = image;
    holder.thumbnail.setAspectRatio(image.getGifAspectRatio());
    holder.gifProgress.setVisibility(View.GONE);

    RequestBuilder<Drawable> thumbnailRequest = Glide.with(context).load(image.getStillUrl()).diskCacheStrategy(DiskCacheStrategy.ALL);

    if (Util.isLowMemory(context)) {

      Glide.with(context).load(image.getStillUrl())
              .placeholder(new ColorDrawable(Color.BLUE))
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .transition(DrawableTransitionOptions.withCrossFade())
              .listener(holder)
              .into(holder.thumbnail);

      holder.setModelReady();
    } else {

      Glide.with(context).load(image.getGifUrl())
              .thumbnail(thumbnailRequest)
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .transition(DrawableTransitionOptions.withCrossFade())
              .listener(holder)
              .into(holder.thumbnail);
    }
  }

  @Override
  public void onViewRecycled(@NonNull GiphyViewHolder holder) {
    super.onViewRecycled(holder);
    Glide.with(context).clear(holder.thumbnail);
  }

  @Override
  public int getItemCount() {
    return images.size();
  }

  public void setListener(OnGiphyImageClickListener listener) {
    this.listener = listener;
  }

  public interface OnGiphyImageClickListener {
    void onGiphyImageClicked(GiphyImage giphyImage);
  }
}