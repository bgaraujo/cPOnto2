package br.com.cponto.Model;

import java.util.ArrayList;

public class ModelToAdapterPoints {

    public int date;
    public ArrayList<Register> registerArrayList;

    public ArrayList addRegister( int date, Register register, ArrayList<ModelToAdapterPoints> arrayList){
        boolean create = true;


        if( arrayList.size() == 0 ){
            create = true;
        }else {

            for (int i = 0; arrayList.size() > i; i++) {
                if (arrayList.get(i).date == date) {
                    arrayList.get(i).registerArrayList.add(register);
                    create = false;
                }
            }
        }

        if(create){
            ModelToAdapterPoints modelToAdapterPoints = new ModelToAdapterPoints();
            modelToAdapterPoints.date = date;
            modelToAdapterPoints.registerArrayList = new ArrayList<Register>();
            modelToAdapterPoints.registerArrayList.add(register);

            arrayList.add(modelToAdapterPoints);
        }

        return arrayList;
    }

}
