package br.com.cponto.Model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public final class Employee {
    private String name,lastName,company_code;
    private int cpf,register;

    private static Employee INSTANCE = new Employee();

    public static final String SHARED_NAME = "sharedUser";


    public static Employee getInstance(Context context) {

        SharedPreferences sharedPrefferenceEdit = context.getSharedPreferences(SHARED_NAME,MODE_PRIVATE);
        String jsonUser = sharedPrefferenceEdit.getString("data",null);

        INSTANCE = new Gson().fromJson( jsonUser,Employee.class );

        return INSTANCE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany_code() {
        return company_code;
    }

    public void setCompany_code(String company_code) {
        this.company_code = company_code;
    }

    public int getCpf() {
        return cpf;
    }

    public void setCpf(int cpf) {
        this.cpf = cpf;
    }

    public int getRegister() {
        return register;
    }

    public void setRegister(int register) {
        this.register = register;
    }
}
