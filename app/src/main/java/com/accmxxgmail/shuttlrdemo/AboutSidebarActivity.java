package com.accmxxgmail.shuttlrdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutSidebarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false) //align left
                .setDescription("Shuttlr Description\nSome other text can go right here")
                .setImage(R.drawable.icon_logo)
                .addItem(new Element().setTitle("BETA Version 1.0"))
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("")
                .addWebsite("")
                .addFacebook("")
                .addTwitter("")
                .addYoutube("")
                .addPlayStore("")
                .addInstagram("")
                .addGitHub("")
                .addItem(getCopyRightsElement())
                .create();

        setContentView(aboutPage);
    }


    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = "Shuttlr Canada";
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.about_icon_copyright);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutSidebarActivity.this, "Made with <3 by Michael Xu", Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

}