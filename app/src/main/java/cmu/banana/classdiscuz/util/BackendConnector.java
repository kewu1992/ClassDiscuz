package cmu.banana.classdiscuz.util;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmu.banana.classdiscuz.model.ChatMember;
import cmu.banana.classdiscuz.model.Course;

public class BackendConnector {

    private static final String BACKEND = "http://localhost:8080/ClassDiscuzBackend";

    public static ArrayList<ChatMember> getMembersByCourse(int courseId){
        ChatMember[] response = null;
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
                response = gson.fromJson(json,ChatMember[].class);
                Log.d("Info","*******"+json);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<ChatMember> result = (ArrayList)Arrays.asList(response);
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

    public static ChatMember getMemberByID(int userID){
        ChatMember response = null;
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
                response = gson.fromJson(json,ChatMember.class);
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

    public static ChatMember signUp(String email,String password, String name) {
        ChatMember response = null;
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
                response = gson.fromJson(json,ChatMember.class);
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

    public static ChatMember logIn(String email, String password) {
        ChatMember response = null;
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
                response = gson.fromJson(json,ChatMember.class);
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
