package phucdv.android.magicnote.util;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class LiveDataListHelper {
    public static void add(MutableLiveData<List<Object>> listLiveData, Object o){
        List<Object> lo = listLiveData.getValue();
        lo.add(o);
        listLiveData.setValue(lo);
    }

    public static void addAll(MutableLiveData<List<Object>> listLiveData, List<Object> lo){
        List<Object> lo2 = listLiveData.getValue();
        lo2.addAll(lo);
        listLiveData.setValue(lo);
    }
}
