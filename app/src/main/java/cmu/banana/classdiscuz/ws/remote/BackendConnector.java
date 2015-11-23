package cmu.banana.classdiscuz.ws.remote;

import android.provider.MediaStore;
import android.util.Log;

import com.google.gson.Gson;

import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmu.banana.classdiscuz.entities.User;
import cmu.banana.classdiscuz.entities.Course;
import cmu.banana.classdiscuz.exception.SignUpException;

public class BackendConnector {

    private static final String BACKEND = "http://10.0.0.5:8080/ClassDiscuzBackend";

    public static ArrayList<User> getMembersByCourse(int courseId){
        User[] response = null;
        try {
            URL url = new URL(BACKEND+"/usersincourse");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String params = "courseId="+courseId;

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            String result = sb.toString();
            if (result.equals("{\"result\":\"1\"}") || result.equals("{\"result\":\"2\"}")) {
                return null;
            }
            response = new Gson().fromJson(result, User[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<User> result = new ArrayList<User>();
        result.addAll(Arrays.asList(response));

        for (User user : result){
            /* transform image, decode Base64 */
            if (user.getAvatar() != null){
                String temp = new String(user.getAvatar()).replace(' ', '+');
                user.setAvatar(Base64.decode(temp.getBytes(), Base64.DEFAULT));
            }
        }
        return result;
    }

    public static ArrayList<Course> getCourses(int userID){
        Course[] response = null;
        try {

            URL url = new URL(BACKEND+"/registered");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String params = "studentId="+userID;

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            String result = sb.toString();
            if (result.equals("{\"result\":\"1\"}") || result.equals("{\"result\":\"2\"}")) {
                return null;
            }

            response = new Gson().fromJson(result, Course[].class);


        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Course> result = new ArrayList<Course>();
        result.addAll(Arrays.asList(response));
        return result;
    }

    public static User getMemberByID(int userID){
        User response = null;
        try {
            URL url = new URL(BACKEND+"/id");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String params = "studentId="+userID;

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();

            String result = sb.toString();
            if (result.equals("{\"result\":\"1\"}") || result.equals("{\"result\":\"2\"}")) {
                return null;
            }

            response = new Gson().fromJson(result, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* transform image, decode Base64 */
        if (response.getAvatar() != null){
            String temp = new String(response.getAvatar()).replace(' ', '+');
            response.setAvatar(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        }

        return response;
    }

    public static ArrayList<Course> getAllCourse() {
        Course[] response = null;
        try {
            URL url = new URL(BACKEND+"/allcourses");
            URLConnection connection = url.openConnection();
//            connection.setRequestProperty("Accept-Charset", charset);
            InputStream in = connection.getInputStream();

            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();

            String result = sb.toString();
            if (result.equals("{\"result\":\"1\"}") || result.equals("{\"result\":\"2\"}")) {
                return null;
            }

            response = new Gson().fromJson(result, Course[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Course> result = new ArrayList<Course>();
        result.addAll(Arrays.asList(response));
        return result;
    }

    public static int regOrDropCourse(int userId, int courseId) {
        try {

            URL url = new URL(BACKEND+"/regordrop?studentId="+userId +"&courseId="+courseId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            String params = "studentId="+userId +"&courseId="+courseId;

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            //out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();

            String result = sb.toString();
            if (result.equals("{\"result\":\"1\"}") || result.equals("{\"result\":\"2\"}")) {
                return -1;
            }
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static User signUp(String email,String password, String name) throws SignUpException{
        User response = null;
        try {
            URL url = new URL(BACKEND+"/signup");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String params = "email="+email+"&password="+password+"&name="+name;

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            String result = sb.toString();

            //exception
            if (result.equals("{\"result\":\"1\"}")) {
                throw new SignUpException(1, "Database Exception.");
            }
            if (result.equals("{\"result\":\"2\"}")) {
                throw new SignUpException(2, "Database Exception.");
            }
            if (result.equals("{\"result\":\"3\"}")) {
                throw new SignUpException(3, "The email already exits.");
            }

            response = new Gson().fromJson(result, User.class);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static User logIn(String email, String password) {
        User response = null;
        try {
            URL url = new URL(BACKEND+"/login");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String params = "email="+email+"&password="+password;

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            String result = sb.toString();
            if (result.equals("{\"result\":\"1\"}") || result.equals("{\"result\":\"2\"}")
                    || result.equals("{\"result\":\"3\"}")) {
                return null;
            }

            response = new Gson().fromJson(result, User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static int updateProfile(int id, String name, String college, String major, byte[] image) {

        try {
            URL url = new URL(BACKEND+"/editprofile");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String params = "studentId="+id+"&name="+name+"&college="+
                    college+"&major="+major+"&avatar="+new String(Base64.encode(image, Base64.DEFAULT));

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            out.write(params.getBytes());
            out.flush();
            out.close();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            String str = null;
            StringBuilder sb = new StringBuilder();
            while ((str = r.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            String result = sb.toString();

            if (result.equals("{\"result\":\"1\"}") || result.equals("{\"result\":\"2\"}")) {
                return -1;
            }
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
