package com.juniper;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.juniper.Model.Engineer;

public class SearchEngineer extends AppCompatActivity {

    private EditText mSearchField;
    private Button mSearchBtn;
    private RecyclerView mResultList;

    private DatabaseReference mEngineerDatabase;

    private FirebaseRecyclerAdapter mEngineerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_engineer);

        mEngineerDatabase = FirebaseDatabase.getInstance().getReference("Engineers");
        mSearchField = (EditText) findViewById(R.id.search_field);
        mSearchBtn = (Button) findViewById(R.id.search_btn);
        mResultList = (RecyclerView) findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchText = mSearchField.getText().toString();
                firebaseEngineerSearch(searchText);
            }
        });
    }

    private void firebaseEngineerSearch(String searchText) {

        Toast.makeText(SearchEngineer.this, "Started Search", Toast.LENGTH_LONG).show();

        Query firebaseSearchQuery = mEngineerDatabase.orderByChild("engname").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Engineer>().setQuery(firebaseSearchQuery, Engineer.class).build();

        mEngineerAdapter = new FirebaseRecyclerAdapter<Engineer, EngineerViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull EngineerViewHolder viewholder, final int position, @NonNull Engineer model) {

                viewholder.setDetails(getApplicationContext(), model.getEngname(), model.getProfession(), model.getEducation(), model.getImage());
            }

            @NonNull
            @Override
            public EngineerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_layout, viewGroup, false);

                return new EngineerViewHolder(view);
            }
        };

        mResultList.setAdapter(mEngineerAdapter);
    }

    //view holder class
    public static class EngineerViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public EngineerViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDetails(Context ctx, String userName, String userProfession, String userEducation, String userImage) {
            TextView user_name = (TextView) mView.findViewById(R.id.nameText);
            TextView user_profession = (TextView) mView.findViewById(R.id.professionText);
            TextView user_education = (TextView) mView.findViewById(R.id.educationText);
            ImageView user_image = (ImageView) mView.findViewById(R.id.profile_image);

            user_name.setText(userName);
            user_education.setText(userEducation);
            user_profession.setText(userProfession);

            Glide.with(ctx).load(user_image).into(user_image);

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        mEngineerAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mEngineerAdapter.stopListening();
    }
}
