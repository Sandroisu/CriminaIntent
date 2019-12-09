package com.alex.criminaintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alex.criminaintent.database.CrimeBaseHelper;
import com.alex.criminaintent.database.CrimeCursorWrapper;
import com.alex.criminaintent.database.CrimeDbSchema;
import com.alex.criminaintent.database.CrimeDbSchema.CrimeTable.Cols;

import java.io.File;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;



    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME, null, values);

    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<Crime>();
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID crimeId) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID + " = ?",
                new String[]{crimeId.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Crime crime){

        File filesDir = mContext.getFilesDir();
        return new File (filesDir,crime.getPhotoFileName());
        if (externalFilesDir == null) {

        }
    }



    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, crime.getId().toString());
        values.put(Cols.TITLE, crime.getTitle());
        values.put(Cols.DATE, crime.getDate().getTime());
        values.put(Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(Cols.SUSPECT, crime.getSuspect());
        return values;
    }
    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        mDatabase.update(CrimeDbSchema.CrimeTable.NAME, values,
                Cols.UUID + " = ?", new String[]{uuidString});
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String [] whereArgs){
        Cursor cursor = mDatabase.query(CrimeDbSchema.CrimeTable.NAME,
                null, whereClause, whereArgs, null,null,null);
    return new CrimeCursorWrapper(cursor);
    }
}
