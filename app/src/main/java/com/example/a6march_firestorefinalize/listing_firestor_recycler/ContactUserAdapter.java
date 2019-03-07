package com.example.a6march_firestorefinalize.listing_firestor_recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a6march_firestorefinalize.ContactUser;
import com.example.a6march_firestorefinalize.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

class ContactUserAdapter extends FirestoreRecyclerAdapter<ContactUser,ContactUserAdapter.InsideHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ContactUserAdapter(@NonNull FirestoreRecyclerOptions<ContactUser> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull InsideHolder holder, int position, @NonNull ContactUser model) {

        holder.textViewNameRef.setText(model.getName());
        holder.textViewPhoneRef.setText(model.getPhone());
        holder.textViewUidRef.setText(model.getUid());

    }

    @NonNull
    @Override
    public InsideHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //return null;

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_layout_cardview,viewGroup,false);


        return new InsideHolder(v);

    }

    public class InsideHolder extends RecyclerView.ViewHolder {

        private TextView textViewNameRef, textViewPhoneRef, textViewUidRef;

        public InsideHolder(@NonNull View itemView) {
            super(itemView);

            textViewNameRef = itemView.findViewById(R.id.textViewName);
            textViewPhoneRef = itemView.findViewById(R.id.textViewPhone);
            textViewUidRef = itemView.findViewById(R.id.textViewUid);

        }
    }
}
