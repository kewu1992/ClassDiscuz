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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmu.banana.classdiscuz.entities.User;
import cmu.banana.classdiscuz.entities.Course;

public class BackendConnector {

    private static final String BACKEND = "http://localhost:8080/ClassDiscuzBackend";

    public static ArrayList<User> getMembersByCourse(int courseId){
        User[] response = null;
        try {
            HttpClient client = HttpClients.createDefault();
            String postURL = BACKEND+"/usersincourse";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("courseId", String.valueOf(courseId)));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
            post.setEntity(ent);

            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                String json = EntityUtils.toString(resEntity);
                Gson gson = new Gson();
                response = gson.fromJson(json,User[].class);
                Log.d("Info","*******"+json);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<User> result = (ArrayList)Arrays.asList(response);
        return result;
    }

    public static ArrayList<Course> getCourses(int userID){
        Course[] response = null;
        try {
            HttpClient client = HttpClients.createDefault();
            String postURL = BACKEND+"/registered";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("studentId", String.valueOf(userID)));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
            post.setEntity(ent);

            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                String json = EntityUtils.toString(resEntity);
                Gson gson = new Gson();
                response = gson.fromJson(json,Course[].class);
                Log.d("Info","*******"+json);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Course> result = (ArrayList)Arrays.asList(response);
        return result;
    }

    public static User getMemberByID(int userID){
        User response = null;
        try {
            HttpClient client = HttpClients.createDefault();
            String postURL = BACKEND+"/id";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("studentId", String.valueOf(userID)));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
            post.setEntity(ent);

            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                String json = EntityUtils.toString(resEntity);
                Gson gson = new Gson();
                response = gson.fromJson(json,User.class);
                Log.d("Info","*******"+json);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static List<Course> getAllCourse() {
        Course[] response = null;
        try {
            HttpClient client = HttpClients.createDefault();
            String postURL = BACKEND+"/allcourses";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
            post.setEntity(ent);

            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                String json = EntityUtils.toString(resEntity);
                Gson gson = new Gson();
                response = gson.fromJson(json,Course[].class);
                Log.d("Info","*******"+json);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Course> result = (ArrayList)Arrays.asList(response);
        return result;
    }

    public static void regOrDropCourse(int userId, int courseId) {
        try {
            HttpClient client = HttpClients.createDefault();
            String postURL = BACKEND+"/regordrop";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("studentId", String.valueOf(userId)));
            params.add(new BasicNameValuePair("courseId", String.valueOf(courseId)));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
            post.setEntity(ent);

            HttpResponse responsePOST = client.execute(post);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User signUp(String email,String password, String name) {
        User response = null;
        try {
            HttpClient client = HttpClients.createDefault();
            String postURL = BACKEND+"/signup";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("name", name));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
            post.setEntity(ent);

            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                String json = EntityUtils.toString(resEntity);
                Gson gson = new Gson();
                response = gson.fromJson(json,User.class);
                Log.d("Info","*******"+json);
            }
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
            HttpClient client = HttpClients.createDefault();
            String postURL = BACKEND+"/login";
            HttpPost post = new HttpPost(postURL);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params);
            post.setEntity(ent);

            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();
            if (resEntity != null) {
                String json = EntityUtils.toString(resEntity);
                Gson gson = new Gson();
                response = gson.fromJson(json,User.class);
                Log.d("Info","*******"+json);
            }
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
