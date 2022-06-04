package com.example.taserfan;

import com.example.taserfan.Clases.Empleado;

public class LoggedInUserRepository {

    private static LoggedInUserRepository loggedUserRepository;
    private static Empleado empleado;

    private LoggedInUserRepository(){
    }

    public static LoggedInUserRepository getInstance(){
        if(loggedUserRepository==null)
            loggedUserRepository= new LoggedInUserRepository();
        return loggedUserRepository;
    }

    public static void login(Empleado empleado){
        LoggedInUserRepository.empleado=empleado;
    }

    public static Empleado getLogerInUser(){
        return empleado;
    }

    public void logout(){
        empleado=null;
    }
}
