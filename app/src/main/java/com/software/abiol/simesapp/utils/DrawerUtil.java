package com.software.abiol.simesapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.activities.AddCourse;
import com.software.abiol.simesapp.activities.EditTimetable;
import com.software.abiol.simesapp.activities.JoinExecutives;
import com.software.abiol.simesapp.activities.MainActivity;
import com.software.abiol.simesapp.activities.MultipleUploads;
import com.software.abiol.simesapp.activities.MyExecutives;
import com.software.abiol.simesapp.activities.MySimesBlog;
import com.software.abiol.simesapp.activities.SettingsActivity;
import com.software.abiol.simesapp.activities.StartActivity;
import com.software.abiol.simesapp.activities.TimeTableWeek;
import com.software.abiol.simesapp.activities.UploadToLibraryActivity;
import com.software.abiol.simesapp.activities.Elibrary;
import com.software.abiol.simesapp.application.SimesApp;


public class DrawerUtil extends SimesApp {







    public static void getMyDrawer(final Activity activity, Bundle savedInstanceState, final Toolbar mToolbar) {

        //initialize and create the image loader logic
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.profile_placeholder)
                        .error(R.drawable.profile_placeholder)
                        .priority(Priority.HIGH);
                Glide.with(activity).load(uri).apply(options).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                super.cancel(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name().equals(tag)) {
                    return DrawerUIUtils.getPlaceHolder(ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name().equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(com.mikepenz.materialdrawer.R.color.primary).sizeDp(56);
                } else if ("customUrlItem".equals(tag)) {
                    return new IconicsDrawable(ctx).iconText(" ").backgroundColorRes(R.color.md_red_500).sizeDp(56);
                }

                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()

                return super.placeholder(ctx, tag);
            }
        });



        if (thumb_image != null) {

            /*Intent i = activity.getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( activity.getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivity(i);*/
        }

        // Create the AccountHeader
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
        new ProfileDrawerItem().withName(name).withEmail(email).withIcon(thumb_image)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {

                        Intent accountIntent  = new Intent(activity, SettingsActivity.class);
                        activity.startActivity(accountIntent);
                        return true;
                    }
                })//.withSavedInstance(savedInstanceState)
                .build();




        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("MySimesMessenger").withIdentifier(1);

        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("MySimesForum").withIdentifier(2);


        ExpandableDrawerItem item3 = new ExpandableDrawerItem().withName("Timetable").withSubItems(
                new SecondaryDrawerItem().withName("My Timetable").withIdentifier(31),
                new SecondaryDrawerItem().withName("Edit time table").withIdentifier(32),
                new SecondaryDrawerItem().withName("Add course").withIdentifier(33)
                );

        ExpandableDrawerItem item4 = new ExpandableDrawerItem().withName("MySimes E-Library").withSubItems(
                new SecondaryDrawerItem().withName("E-Library").withIdentifier(41),
                new SecondaryDrawerItem().withName("Upload a File").withIdentifier(42),
                new SecondaryDrawerItem().withName("Upload Multiple Files").withIdentifier(43)

                );

        ExpandableDrawerItem item5 = new ExpandableDrawerItem().withName("Executives").withSubItems(
                new SecondaryDrawerItem().withName("My Executives").withIdentifier(51),
                new SecondaryDrawerItem().withName("Join Executives").withIdentifier(52)
        );


        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withName("Logout").withIdentifier(6);




        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withToolbar(mToolbar)
                .withActivity(activity)
                .withCloseOnClick(true)
                .withAccountHeader(headerResult)

                .addDrawerItems(
                        item1, item2, item3, item4, item5, item6

                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(activity, MainActivity.class);
                            } else if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(activity, MySimesBlog.class);
                            } else if (drawerItem.getIdentifier() == 31) {
                                intent = new Intent(activity, TimeTableWeek.class);
                            } else if (drawerItem.getIdentifier() == 32) {
                                intent = new Intent(activity, EditTimetable.class);
                            } else if (drawerItem.getIdentifier() == 33) {
                                intent = new Intent(activity, AddCourse.class);
                            }else if (drawerItem.getIdentifier() == 41) {
                                intent = new Intent(activity, Elibrary.class);
                            } else if (drawerItem.getIdentifier() == 42) {
                                intent = new Intent(activity, UploadToLibraryActivity.class);
                            } else if(drawerItem.getIdentifier() == 43){
                                intent = new Intent(activity, MultipleUploads.class);
                            } else if (drawerItem.getIdentifier() == 51) {
                                intent = new Intent(activity, MyExecutives.class);
                            } else if (drawerItem.getIdentifier() == 52) {
                                intent = new Intent(activity, JoinExecutives.class);
                            } else if (drawerItem.getIdentifier() == 6) {
                                FirebaseAuth.getInstance().signOut();
                                intent = activity.getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage( activity.getBaseContext().getPackageName() );
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                /*intent = new Intent(activity, StartActivity.class);
                                activity.finish();*/
                            } /* else if (drawerItem.getIdentifier() == 15) {
                                intent = new Intent(activity, CrossfadeDrawerLayoutActvitiy.class);
                            }*/ else if (drawerItem.getIdentifier() == 20) {
                                /*intent = new LibsBuilder()
                                        .withFields(R.string.class.getFields())
                                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                        .intent(activity);*/
                            }
                            if (intent != null) {
                                activity.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .build();


        headerResult.updateProfile(new ProfileDrawerItem().withIcon(R.drawable.profile).withName(name).withEmail(email));


    }
}
