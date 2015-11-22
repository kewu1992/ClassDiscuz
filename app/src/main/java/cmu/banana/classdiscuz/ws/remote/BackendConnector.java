package cmu.banana.classdiscuz.ws.remote;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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

    private static final String BACKEND = "http://128.237.131.190:8080/ClassDiscuzBackend";

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
            response = new Gson().fromJson(sb.toString(), User[].class);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<User> result = new ArrayList<User>();
        result.addAll(Arrays.asList(response));
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
            response = new Gson().fromJson(sb.toString(), Course[].class);
            

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
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
            response = new Gson().fromJson(sb.toString(), User.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            Log.d("log",sb.toString());
            response = new Gson().fromJson(sb.toString(), Course[].class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Course> result = new ArrayList<Course>();
        result.addAll(Arrays.asList(response));
        return result;
    }

    public static int regOrDropCourse(int userId, int courseId) {
        try {

            URL url = new URL(BACKEND+"/regordrop");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String params = "studentId="+userId +"&courseId="+courseId;

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setChunkedStreamingMode(0);

            OutputStream out = new BufferedOutputStream(con.getOutputStream());

            out.write(params.getBytes());
            out.flush();
            out.close();

            return 0;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
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
            //exception
            if (sb.toString().equals("{\"result\":\"1\"}")) {
                throw new SignUpException(1, "Database Exception.");
            }
            if (sb.toString().equals("{\"result\":\"2\"}")) {
                throw new SignUpException(2, "Database Exception.");
            }
            if (sb.toString().equals("{\"result\":\"3\"}")) {
                throw new SignUpException(3, "The email already exits.");
            }

            response = new Gson().fromJson(sb.toString(), User.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            response = new Gson().fromJson(sb.toString(), User.class);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
