package org.techtown.schedulerproject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class Card extends LinearLayout {
    ImageView markericon;
    TextView textView1;
    TextView textView2;
    Button button;

    public Card(Context context){
        super(context);
        init(context);
    }
    public Card(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_card, this,true);

        markericon = findViewById(R.id.iconImage);
        textView1 = findViewById(R.id.textContent);
        textView2 = findViewById(R.id.dateTextView);
        button = findViewById(R.id.cardIdButton);
    }
    public void setImageView(int icon){
        markericon.setImageResource(icon);
    }
    public void setContent(String content){
        textView1.setText(content);
    }
    public void setDate(String date){
        textView2.setText(date);
    }
    public void setDataid(int dataid){ button.setText(dataid); }
}
