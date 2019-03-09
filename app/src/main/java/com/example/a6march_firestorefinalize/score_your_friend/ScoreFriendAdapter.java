package com.example.a6march_firestorefinalize.score_your_friend;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a6march_firestorefinalize.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class ScoreFriendAdapter extends FirestoreRecyclerAdapter<ScoreFriend,ScoreFriendAdapter.InsideHolder> {
    public static ArrayList<ScoreFriend> test = new ArrayList<>();
    public ScoreFriendAdapter(FirestoreRecyclerOptions<ScoreFriend> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull InsideHolder holder, int position, @NonNull ScoreFriend model) {

        holder.name_here.setText(model.getName());
        holder.circleImageView.setImageURI(Uri.parse(model.getImage_url()));

        test.add(new ScoreFriend(model.getName(),model.getImage_url(),model.getMy_score_ref()));

        Log.i("checkk data","name "+model.getName()+ " score ref "+ model.getMy_score_ref());
    }

    @NonNull
    @Override
    public InsideHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_score_friend,viewGroup,false);

        return new InsideHolder(view);

     //   return null;
    }

    public class InsideHolder extends RecyclerView.ViewHolder {

        public TextView name_here;
        public CircleImageView circleImageView;

        public InsideHolder(@NonNull View itemView) {
            super(itemView);

            name_here= itemView.findViewById(R.id.textView_name_score_friendID);
            circleImageView = itemView.findViewById(R.id.circle_image_view_score_friendID);
        }
    }
}
