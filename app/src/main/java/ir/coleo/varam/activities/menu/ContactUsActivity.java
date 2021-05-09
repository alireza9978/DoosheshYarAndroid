package ir.coleo.varam.activities.menu;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ImageView back = findViewById(R.id.back_image);
        Constants.setImageBack(this, back);
        back.setOnClickListener(view -> finish());

        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString textPart = new SpannableString(getString(R.string.contact_bottom_one));
        builder.append(textPart);
        builder.append(" ");
        textPart = new SpannableString(getString(R.string.contact_bottom_two));
        textPart.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String mailto = "mailto:herdhealther@gmail.com";
                intent.setData(Uri.parse(mailto));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 0, textPart.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(textPart);
        builder.append(" ");
        textPart = new SpannableString(getString(R.string.contact_bottom_three));
        builder.append(textPart);
        ((TextView) findViewById(R.id.bottom_text)).setText(builder);
        ((TextView) findViewById(R.id.bottom_text)).setMovementMethod(LinkMovementMethod.getInstance());


        textPart = new SpannableString("@hhre_damasa");
        textPart.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/hhre_damasa");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/hhre_damasa")));
                }
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 0, textPart.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) findViewById(R.id.instagram_text)).setText(textPart);
        ((TextView) findViewById(R.id.instagram_text)).setMovementMethod(LinkMovementMethod.getInstance());
        findViewById(R.id.instagram_icon).setOnClickListener(v -> {
            Uri uri = Uri.parse("http://instagram.com/_u/hhre_damasa");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/hhre_damasa")));
            }
        });

        textPart = new SpannableString("@hhre_damasa");
        textPart.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/hhre_damasa"));
                startActivity(telegram);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 0, textPart.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) findViewById(R.id.telegram_text)).setText(textPart);
        ((TextView) findViewById(R.id.telegram_text)).setMovementMethod(LinkMovementMethod.getInstance());

        findViewById(R.id.telegram_icon).setOnClickListener(v -> {
            Intent telegram = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/hhre_damasa"));
            startActivity(telegram);
        });

        textPart = new SpannableString("+989918760698");
        textPart.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+989918760698"));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        }, 0, textPart.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) findViewById(R.id.call_text)).setText(textPart);
        ((TextView) findViewById(R.id.call_text)).setMovementMethod(LinkMovementMethod.getInstance());

        findViewById(R.id.call_icon).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "+989918760698"));
            startActivity(intent);
        });


        textPart = new SpannableString("damasahhre.com");
        textPart.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                String url = "http://www.damasahhre.com";
                Intent inetent = new Intent(Intent.ACTION_VIEW);
                inetent.setData(Uri.parse(url));
                startActivity(inetent);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }

        }, 0, textPart.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((TextView) findViewById(R.id.email_text)).setText(textPart);
        ((TextView) findViewById(R.id.email_text)).setMovementMethod(LinkMovementMethod.getInstance());

        findViewById(R.id.email_icon).setOnClickListener(v -> {
            String url = "http://www.damasahhre.com";
            Intent inetent = new Intent(Intent.ACTION_VIEW);
            inetent.setData(Uri.parse(url));
            startActivity(inetent);
        });

    }
}