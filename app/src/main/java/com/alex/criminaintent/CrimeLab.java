package com.alex.criminaintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private Map<UUID,Crime> mCrimes;


    public static CrimeLab get(Context context){
        if(sCrimeLab==null){
            sCrimeLab = new CrimeLab(context);
        }return sCrimeLab;
    }

    private CrimeLab (Context context){
        mCrimes = new LinkedHashMap<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            if (i%3==0) crime.setRequiresPolice(1);// Для каждого второго объекта
            mCrimes.put(crime.getId(), crime);
        }
    }

    public List<Crime> getCrimes(){
        return new ArrayList<>(mCrimes.values());
    }

    public Crime getCrime (UUID id){
        return mCrimes.get(id);
    }
}
