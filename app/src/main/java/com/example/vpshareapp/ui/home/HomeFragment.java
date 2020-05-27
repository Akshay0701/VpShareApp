package com.example.vpshareapp.ui.home;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.example.vpshareapp.Adapter.AdapterAllCommander;
import com.example.vpshareapp.Adapter.AdapterImage;
import com.example.vpshareapp.MainScreen;
import com.example.vpshareapp.Model.ModelCommander;
import com.example.vpshareapp.R;
import com.example.vpshareapp.introPage;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment  {


    //extra
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 2000;


    Toolbar toolbar;
    ViewPager viewPager;
    SliderView adpater;

    public int[]Activty={
                R.drawable.vpactivity1,
                R.drawable.vpactivity2,
                R.drawable.vpactivity3,
                R.drawable.vpactivity4,
                R.drawable.vpactivity5
    };
    public int[]SchoolNgo={
            R.drawable.vpachivments1,
            R.drawable.vpachivments2,
            R.drawable.vpachivments3,
            R.drawable.vpachivments4,
            R.drawable.vpachivments5
    };


    AdapterImage adapterImage;

    RecyclerView recyclerView,recyclerView1,recyclerView2;
    ObservableScrollView scrollView_Ober;

    //header
    ImageView homeBtn,Call,Website,Location,FaceBook,Instagram;
    //facebook attributes
    public static String FACEBOOK_URL = "https://www.facebook.com/Vidyalankar.VP/";
    public static String FACEBOOK_PAGE_ID = "Vidyalankar.VP";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_home, container, false);


        final int images[]={R.drawable.colleage,R.drawable.college1,R.drawable.college2,R.drawable.colleage3,R.drawable.colleage4,R.drawable.collleage5,R.drawable.colleage6};


        recyclerView=root.findViewById(R.id.recyclerView);
        recyclerView1=root.findViewById(R.id.recyclerView1);
        recyclerView2=root.findViewById(R.id.recyclerView2);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        GridLayoutManager manager1 = new GridLayoutManager(getContext(), 5, GridLayoutManager.VERTICAL, false);
        recyclerView1.setLayoutManager(manager1);

        GridLayoutManager manager2 = new GridLayoutManager(getContext(), 5, GridLayoutManager.VERTICAL, false);
        recyclerView2.setLayoutManager(manager2);
       AdapterImage  adapterImage1=new AdapterImage(getContext(),Activty);
        recyclerView.setAdapter(adapterImage1);

        AdapterImage  adapterImage2=new AdapterImage(getContext(),SchoolNgo);
        recyclerView1.setAdapter(adapterImage2);

        AdapterImage  adapterImage3=new AdapterImage(getContext(),SchoolNgo);
        recyclerView2.setAdapter(adapterImage3);
        //sliding view
        viewPager=(ViewPager)root.findViewById(R.id.viewpager);
        adpater= new SliderView(getContext());
        viewPager.setAdapter(adpater);

        //extra
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == images.length-1) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        //init header
        homeBtn=root.findViewById(R.id.homeBtn);
        Call=root.findViewById(R.id.Call);
        Website=root.findViewById(R.id.Website);
        Location=root.findViewById(R.id.Location);
        FaceBook=root.findViewById(R.id.FaceBook);
        Instagram=root.findViewById(R.id.Instagram);



        //for changing image from flipper
        for (int image:images) {
          //  flipperimg(image);
        }


        //header function
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open home page
                startActivity(new Intent(getActivity(), MainScreen.class));
            }
        });
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open call
                CallToVp("02224161126");
            }
        });

        Website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open website page
                WebsiteToVp("https://vidyalankar.com/vidyalankar-polytechnic/");
            }
        });

        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open home page
                startActivity(new Intent(getActivity(), VpLocation.class));
            }
        });

        Instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open instagram page
                InstagramToVp("https://www.instagram.com/vp_vidyalankar/");
            }
        });
        FaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open home page
                FacebookToVp("facebooklink");
            }
        });


        //permission
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //permission is not granted
            new AlertDialog.Builder(getContext(),4)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to acess this feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    1);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
            // return;

        }
        //request permission
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {


                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(getContext(), "acces deined", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            new AlertDialog.Builder(getContext(),4)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to acess this feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    1);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }

        //phone permission
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            new AlertDialog.Builder(getContext(),4)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to acess this feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }





        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String isdashboradtutorial=prefs.getString("HomeNote","");
        if(isdashboradtutorial.equals("")) {
            tutorialstart();
        }
    }

    private void tutorialstart() {
        TapTargetView.showFor(getActivity(),              // `this` is an Activity
                TapTarget.forView(getActivity().findViewById(R.id.sample), "Note", "if your willing to donate stuff!, Then you can register from user section for further process")
                        // All options below are optional
                        .dimColor(R.color.colorgray)
                        .titleTextDimen(R.dimen.circledimen)
                        .descriptionTextColor(R.color.colorgray)
                        .outerCircleColor(R.color.colorPrimary)
                        .targetCircleColor(android.R.color.black)
                        .transparentTarget(true)
                        .cancelable(false)
                        .textColor(R.color.colorAccent)
                        .id(2),              // Specify the target radius (in dp)
                new TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);      // This call is optional
                        // doSomething();
                        SharedPreferences.Editor editor;
                        editor= PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                        editor.putString("HomeNote", "1");//1 value will note as user as seen intro
                        editor.apply();
                        //  Toast.makeText(
                    }


                });

    }

    //all method for header
    private void FacebookToVp(String facebooklink) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(getContext());
        facebookIntent.setData(Uri.parse(facebookUrl));
        startActivity(facebookIntent);

    }
    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    private void InstagramToVp(String website) {
        Uri uri = Uri.parse(website);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(website)));
        }

    }

    private void WebsiteToVp(String s) {
        String url = s;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void CallToVp(String s) {
        String posted_by = s;

        String uri = "tel:" + posted_by.trim() ;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
                ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            new AlertDialog.Builder(getContext(),4)
                    .setTitle("Required Location Permission")
                    .setMessage("You have to give this permission to acess this feature")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    private void flipperimg(int image) {
        ImageView imageView=new ImageView(getContext());
        imageView.setBackgroundResource(image);
        //setting view


        //animae


    }


}