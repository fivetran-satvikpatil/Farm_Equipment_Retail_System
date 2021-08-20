package Serverpkg;

import sample.User;


import java.util.ArrayList;
import java.util.List;

public class Server {
    static int init =0;
    public static ArrayList<User> run(List<String> request){
        if(init==0){
            UserDB.init();
            init++;
        }
        User user = new User();
        ArrayList<User> response = new ArrayList<>();
        try{
            switch(request.get(0)){
                case "login":
                    user = login(request.get(1),request.get(2));
                    break;
                case "guest":
                    user = new User();
                    break;
                case "register_user":
                    user = register(request.get(1),request.get(2),request.get(3),request.get(4),1);
                    break;

                case "register_employee":
                    user = register(request.get(1),request.get(2),request.get(3),request.get(4), 2);
                    break;

                case "get_employees":
                    ArrayList<User> employees = UserDB.getUsers(2);
                    return employees;

                case "get_users":
                    ArrayList<User> users = UserDB.getUsers(1);
                    return users;


                default:
                    break;
            }
            response.add(user);
            return response;
        }catch(Exception e){
            e.printStackTrace();
        }
        response.add(user);
        return response;
    }

    public static User login(String email, String password) {
        User nullUser = new User();
        try {
            nullUser = UserDB.checkUser(email,password);
            return nullUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nullUser;
    }

    public static User register(String name, String surname, String mail, String password, int permission) {
        User nullUser = new User();

        try {
            nullUser = UserDB.checkUser(mail,password);
            if(nullUser.getName().equals(name)){
                //already present
                return new User();
            }
            UserDB.addUser(name,surname,mail,password,permission);

            User newUser = new User(name, surname, mail, password, permission);
            // registration successful, the User object is returned
            return newUser;

        } catch (Exception e) {
            e.printStackTrace();
        }
        // registration not successful, the nullUser object is returned
        return nullUser;
    }
}
