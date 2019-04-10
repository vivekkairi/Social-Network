package sample;

import java.sql.SQLException;
import java.util.ArrayList;

public class User {

    private String name;
    private String username;
    private String password;
    private ArrayList<String> interests;
    public User friends;
    private String img;
    private String dob;
    private String gender;
    private String mobile;

    User() {
        interests = new ArrayList<>();
        friends = null;
        name = "";
        username = "";
        password = "";
        img = "";
        dob = "";
        gender = "";
        mobile = "";
    }

    User(User u) {
        this.name = u.name;
        this.username = u.username;
        this.password = u.password;
        this.interests = u.interests;
        this.friends = null;
        this.img = u.img;
        this.dob = u.dob;
        this.mobile = u.mobile;
    }

    public String getImg() {
        return img;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setFriends(User friends) {
        this.friends = friends;
    }

    public User(String name, String username, String password, String dob, String gender, String mobile) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.interests = new ArrayList<>();
        this.friends = null;
        this.dob = dob;
        this.gender = gender;
        this.mobile = mobile;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getDob() {
        return dob;
    }

    public void setData(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void addFriend(User u) {
        if (friends == null) {
            friends = u;
        } else {
            User temp = friends;
            while (temp.friends != null) {
                temp = temp.friends;
            }
            temp.friends = u;
        }
    }

    public void removeFriend(User u) {
        User temp = friends;
        User prev = friends;
        if(friends.getUsername().equals(u.getUsername())){
            friends = friends.friends;
            return;
        }
        while (temp!=null){
            if(temp.getUsername().equals(u.getUsername())){
                prev.friends = temp.friends;
                break;
            }
            prev = temp;
            temp = temp.friends;
        }
    }

    public void createList(String array) {
        if(array == null || array.equals("") || array.equals("[]") || array.equals("null")) {
            return;
        }
        String []friendList = array.split("\",\"");
        friendList[0] = friendList[0].substring(2);
        friendList[friendList.length-1] = friendList[friendList.length-1].substring(0,friendList[friendList.length-1].length()-3);
        User temp = null;
        for(String s:friendList) {
            System.out.println(s);
            User u = Accounts.searchUsername(s);
            User friend = new User(u);
            if (friends == null) {
                friends = friend;
                temp = friend;
            } else {
               temp.friends = friend;
               temp = temp.friends;
            }
        }

    }

    public void setInterests(String interest) {
        interests.add(interest);
        String query = "UPDATE users SET '" + interest +"' = 1 where username='" + username +"'";
        System.out.println(query);
        try {
            Datasource.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(String s:interests){
            System.out.println("Check: "+ s);
        }
    }
    public void setInterests(boolean tech,boolean politics, boolean sports, boolean entertainment) {
        if(tech)
            interests.add("tech");
        if(politics)
            interests.add("politics");
        if(sports)
            interests.add("sports");
        if(entertainment)
            interests.add("entertainment");
    }

    public String stringOfInterests() {
        String ret = "";
        for(String s:interests) {
            ret = ret + s + ", ";
        }
        return ret;
    }

    public String displayIntersts() {
        String ret="";
        for(String s:interests){
            String s1 = s.substring(0,1).toUpperCase() + s.substring(1);
            ret += s1 + "\n";
        }
       // ret = ret.substring(0,ret.length()-2);
        System.out.println("Interest: " + ret);
        return ret;
    }

}

class Accounts {
    static ArrayList<User> accountList=new ArrayList<>();

    public static void addPerson(User u) {
        accountList.add(u);
    }

    public static User searchUsername(String username) {
        for(User u: accountList) {
            if(u.getUsername().equals(username)){
                return u;
            }
        }
        return null;
    }

    public static User searchName(String name) {
        for(User u: accountList) {
            if(u.getName().equals(name)){
                return u;
            }
        }
        return null;
    }

}