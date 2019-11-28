package com.alex.criminaintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.Date;

public class DateDialogActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_DATE =
            "com.bignerdranch.android.criminalintent.crime_date";

    public static Intent newIntent(Context packageContext, Date date) {
        Intent intent = new Intent(packageContext, DateDialogActivity.class);
        intent.putExtra(EXTRA_CRIME_DATE, date);
        return intent;
    }
    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent().getSerializableExtra(EXTRA_CRIME_DATE);
        return DatePickerFragment.newInstance(date);
    }
}
