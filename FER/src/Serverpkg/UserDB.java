package Serverpkg;

import sample.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB {
    private static List<String> Name = new ArrayList<>();
    private static List<String> Sur = new ArrayList<>();
    private static List<String> Email = new ArrayList<>();
    private static List<String> Password = new ArrayList<>();
    private static List<Integer> Perm = new ArrayList<>();

    public static User checkUser(String mail, String pass){
        User user = new User();

        for(int i=0;i<Name.size();i++){
            if(Email.get(i).equals(mail) && Password.get(i).equals(pass)){
                user = new User(Name.get(i),Sur.get(i),Email.get(i),Password.get(i),Perm.get(i));
                return user;
            }
        }
        return user;
    }

    public static void addUser(final String name,final String sur,final String mail,final String pass,
                                  final int perm){
        Name.add(name);
        Sur.add(sur);
        Email.add(mail);
        Password.add(pass);
        Perm.add(perm);
    }

    public static ArrayList<User> getUsers(int permission){
        ArrayList<User> userList = new ArrayList<>();
        for(int i=0;i<Name.size();i++){
            if(Perm.get(i)==permission){
                userList.add(new User(Name.get(i),Sur.get(i),Email.get(i),"",Perm.get(i)));
            }
        }
        return userList;
    }

    private static int initialization;
    static{
        initialization=100;
    }
    public static void init(){
        if(initialization==100){
            Name.add("Admin");
            Sur.add("Admin");
            Email.add("admin@a.c");
            Password.add("a");
            Perm.add(3);
            UserDB.addUser("Rahul","Pawar","rahul@gmail.com","12345",2);
            UserDB.addUser("Cierra","Vega","ciera@gmail.com","12345",2);
            UserDB.addUser("Brock","Lesnar","brock@gmail.com","12345",2);
            UserDB.addUser("Samoan","joe","samoan@gmail.com","12345",1);
            UserDB.addUser("Ramesh","Thakur","ramesh@gmail.com","12345",1);
            UserDB.addUser("Vinay","Kumar","vinay@gmail.com","12345",1);
        }
        initialization++;
    }
}
