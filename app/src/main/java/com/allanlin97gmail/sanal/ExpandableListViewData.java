package com.allanlin97gmail.sanal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListViewData {

    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListViewData = new HashMap<String, List<String>>();

        List<String> building1 = new ArrayList<String>();
        building1.add("extinguisher1");
        building1.add("extinguisher2");

        List<String> building2 = new ArrayList<String>();
        building2.add("extinguisher1");
        building2.add("extinguisher2");

        expandableListViewData.put("Building 1", building1);
        expandableListViewData.put("Building 2", building2);

        return expandableListViewData;
    }
}
