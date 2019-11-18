package com.alex.criminaintent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;


public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;
    private int position;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView =  view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Button callPolice;
        private ImageView mSolvedImageView;
        private Crime mCrime;
        public CrimeHolder(View view) {
            super(view);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            callPolice = itemView.findViewById(R.id.call_police);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
            itemView.setOnClickListener(this);
        }
        public void bind(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");
            String date = dateFormat.format(mCrime.getDate());
            mDateTextView.setText(date);
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE :
                    View.GONE);
        }

        @Override
        public void onClick(View v) {
            position = getAdapterPosition();
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId(), position);
            startActivityForResult(intent, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        position = data.getIntExtra("123", 0);
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List <Crime> mCrimes;
        private final int TYPE_ITEM1 = 0;
        private final int TYPE_ITEM2 = 1;

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position);
            return  crime.getRequiresPolice();
        }

        public CrimeAdapter (List <Crime> crimes){
            mCrimes = crimes;
        }
        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = null;

            switch (viewType) {
                case TYPE_ITEM1:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_crime, parent, false);
                    break;
                case TYPE_ITEM2:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dangerous_crime, parent, false);
                    break;
            }

            return new CrimeHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    public void updateUI(){

        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List <Crime> crimes = crimeLab.getCrimes();
        if (mCrimeAdapter == null) {
        mCrimeAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.notifyItemChanged(position);
        }
    }
}
