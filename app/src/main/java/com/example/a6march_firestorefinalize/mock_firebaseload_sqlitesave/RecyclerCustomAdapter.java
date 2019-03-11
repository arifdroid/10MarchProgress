package com.example.a6march_firestorefinalize.mock_firebaseload_sqlitesave;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a6march_firestorefinalize.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class RecyclerCustomAdapter extends RecyclerView.Adapter<RecyclerCustomAdapter.InsideHolder>{

    private List<FireStoreSQLiteClass> liss;

    private static ArrayList<FireStoreSQLiteClass> returnLiss;

    private Context mContext;

    ///////////////

    private PassingResultInterface passingResultInterface;

    public void setPassingResultInterface(PassingResultInterface passingResultInterface){
        this.passingResultInterface=passingResultInterface;
    }


//    private MyCallback myCallback;
//
//    public RecyclerCustomAdapter(MyCallback myCallback){
//
//        this.myCallback=myCallback;
//    }
//
//
//    public interface MyCallback{
//
//         ArrayList<FireStoreSQLiteClass> returnResult(ArrayList<FireStoreSQLiteClass> returning);
//    }




    public RecyclerCustomAdapter(Write_ScoreCard_Activity write_scoreCard_activity, List<FireStoreSQLiteClass> liss) {
            this.liss=liss;
            this.mContext=write_scoreCard_activity;
    }

    @NonNull
    @Override
    public InsideHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.write_score_card_custom_layout,viewGroup,false);

        return new InsideHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull InsideHolder insideHolder, int i) {

        //k=i; //i always same to k
        final int j =i;

        insideHolder.textViewName.setText(liss.get(i).getName());
        insideHolder.circleImageView.setImageURI(Uri.parse(liss.get(i).getImage_url()));

        //need some logic here, since, this will get called each time we draw, so same pointer
        //might occur twice

        insideHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (fromUser == true) {

                    liss.get(j).setRating(ratingBar.getRating());

                    //we can just keep adding this list, then trim?
                       //this means user is changing rating after first rate

                    //this is bad design, since we could be adding more to after rating list than we need
                    Toast.makeText(mContext,"rating is: " + Float.toString(liss.get(j).getRating()),Toast.LENGTH_SHORT).show();

                    returnLiss=(ArrayList<FireStoreSQLiteClass>) (liss);

                    if(passingResultInterface!=null){

                        passingResultInterface.passingArray(returnLiss);

                    }

                }
            }
        });





                //ratingList.add(new GettingRatingClass(liss.get(i).getName(),liss.get(i).getMy_score_ref(),liss.get(i).getName(),))
    }

    @Override
    public int getItemCount() {
        return liss.size();
    }

    public class InsideHolder extends RecyclerView.ViewHolder{

        public TextView textViewName;
        public RatingBar ratingBar;
        public CircleImageView circleImageView;


        public InsideHolder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.writeScoreCardActivity_textViewID);
            ratingBar = itemView.findViewById(R.id.writeScoreCardActivity_ratingBar3ID);
            circleImageView = itemView.findViewById(R.id.writeScoreCardActivity_circleImageViewID);
        }


    }



}
