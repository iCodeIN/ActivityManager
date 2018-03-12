package com.sdex.activityrunner.util;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.pm.ShortcutInfoCompat;
import android.support.v4.content.pm.ShortcutManagerCompat;
import android.support.v4.graphics.drawable.IconCompat;
import android.widget.Toast;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sdex.activityrunner.R;
import com.sdex.activityrunner.db.ActivityModel;

public class IntentUtils {

  private static Intent getActivityIntent(ComponentName activity) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setComponent(activity);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    return intent;
  }

  private static void createLauncherIcon(Context context, ActivityModel activityModel,
    Bitmap bitmap) {
    final IconCompat iconCompat = IconCompat.createWithBitmap(bitmap);
    ShortcutInfoCompat pinShortcutInfo =
      new ShortcutInfoCompat.Builder(context, activityModel.getName())
        .setIcon(iconCompat)
        .setShortLabel(activityModel.getName())
        .setIntent(getActivityIntent(activityModel.getComponentName()))
        .build();
    ShortcutManagerCompat.requestPinShortcut(context, pinShortcutInfo, null);
  }

  public static void createLauncherIcon(final Context context, final ActivityModel activityModel) {
    GlideApp.with(context)
      .asBitmap()
      .error(R.mipmap.ic_launcher)
      .load(activityModel.getIconPath())
      .into(new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(@NonNull Bitmap resource,
          @Nullable Transition<? super Bitmap> transition) {
          createLauncherIcon(context, activityModel, resource);
        }
      });
  }

  public static void launchActivity(Context context, ComponentName activity, String name) {
    try {
      Intent intent = getActivityIntent(activity);
      context.startActivity(intent);
      Toast.makeText(context, context.getString(R.string.starting_activity, name),
        Toast.LENGTH_SHORT).show();
    } catch (ActivityNotFoundException e) {
      Toast.makeText(context, context.getString(R.string.starting_activity_failed, name),
        Toast.LENGTH_SHORT).show();
    } catch (SecurityException e) {
      Toast.makeText(context, context.getString(R.string.starting_activity_failed, name),
        Toast.LENGTH_SHORT).show();
    }
  }
}