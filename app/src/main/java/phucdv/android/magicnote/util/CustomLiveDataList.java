package phucdv.android.magicnote.util;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class CustomLiveDataList extends MutableLiveData<List<Object>> {
    public CustomLiveDataList(){
        super();
    }

    public CustomLiveDataList(List<Object> lo){
        super(lo);
    }

    public void add(Object o){
        List<Object> lo = getValue();
        lo.add(o);
        setValue(lo);
    }
}
