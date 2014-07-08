package adullact.publicrowdfunding.model.server;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author Ferrand
 * @brief Storage class for Server information
 */
public class ServerInfo {
	public final static String SERVER_URL = "http://10.0.2.2/PublicrowFunding/PublicrowFunding/rest";
	//public final static String SERVER_URL = "http://192.168.1.21/PublicrowFunding/PublicrowFunding/rest";

	
	/*
	Il faudrait vérifier la connexion 
	private boolean isNetworkAvailable() {
ConnectivityManager connectivityManager 
      = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
return activeNetworkInfo != null && activeNetworkInfo.isConnected();
}
	
	*/
	
	
	private String streamToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

    /* Type of server response */
    static public class SimpleServerResponse {
        public int code;
    }
    static public class ServerFunding {
        public int id;
        public String value;
        public String username;
        public String projectID;
        public Date creationDate;
    }
    static public class ServerBookmark {
        public int id;
        public String username;
        public String projectID;
        public Date creationDate;
    }
    public class ServerCommentary {
        public int id;
        public String username;
        public String projectID;
        public String title;
        public String description;
        public int mark;
        public Date creationDate;
    }
    static public class ServerUser {
        public String username;
        public String password;
        public String mail;
        public String name;
        public String firstName;
        public String administrator;
    }
    static public class ServerProject {
        public String id;
        public String proposedBy;
        public String name;
        public String description;
        public String currentFunding;
        public String requestedFunding;
        public Date creationDate;
        public Date beginDate;
        public Date endDate;
        public LatLng position;
    }
    static public class DetailedServerUser extends ServerUser {
        public ArrayList<ServerProject> projects; // All project
        public ArrayList<ServerFunding> fundedProjects;
        public ArrayList<ServerBookmark> bookmarkedProjects;
        public ArrayList<ServerCommentary> commentaries;
    }
    static public class DetailedServerProject extends ServerProject {
        public ArrayList<ServerFunding> fundedBy;
        public ArrayList<ServerBookmark> bookmarkedBy;
        public ArrayList<ServerCommentary> commentedBy;
    }
    /* ----------------------- */
    static public interface Service {
        // Check
        @GET("/check/authentication")
        Observable<DetailedServerUser> authenticate();
        @GET("/check/ready") // Indicate if server is ready
        Observable<SimpleServerResponse> ready();

        // Users
        @GET("/user/{username}")
        Observable<DetailedServerUser> detailUser(@Path("username") String username);
        @GET("/user/")
        Observable<ArrayList<ServerUser>> listUsers();
        @GET("/user/")
        Observable<ArrayList<ServerUser>> listUsers(@Query("projectID") String projectID);
        @POST("/user/")
        Observable<SimpleServerResponse> createUser(@Body ServerUser serverUser);
        @PUT("/user/{username}")
        Observable<SimpleServerResponse> modifyUser(@Body ServerUser serverUser, @Path("username") String username);
        @DELETE("/user/{username}")
        Observable<SimpleServerResponse> deleteUser(@Path("username") String username);

        // Projects
        @GET("/project/{projectID}")
        Observable<DetailedServerProject> detailProject();
        @GET("/project/")
        Observable<ArrayList<ServerProject>> listProjects();
        @GET("/project/")
        Observable<ArrayList<ServerProject>> listProjects(@Query("userID") String userID);
        @POST("/project/")
        Observable<SimpleServerResponse> createProject(@Body ServerProject serverProject);
        @PUT("/project/{projectID}")
        Observable<SimpleServerResponse> modifyProject(@Path("projectID") String projectID);
        @DELETE("/project/{projectID}")
        Observable<SimpleServerResponse> deleteProject(@Path("projectID") String projectID);

        // Funding
        @GET("/funding/")
        Observable<ArrayList<ServerFunding>> listFunding();
        @GET("/funding/")
        Observable<ArrayList<ServerFunding>> listFundingByUser(@Query("username") String username);
        @GET("/funding/")
        Observable<ArrayList<ServerFunding>> listFundingByProject(@Query("username") String projectID);
        @POST("/funding/")
        Observable<SimpleServerResponse> createFunding(@Body ServerFunding serverFunding);

        // Commentaries
        @GET("/commentary/")
        Observable<ArrayList<ServerFunding>> listCommentaries();
        @GET("/commentary/")
        Observable<ArrayList<ServerFunding>> listCommentariesByUser(@Query("username") String username);
        @GET("/commentary/")
        Observable<ArrayList<ServerFunding>> listCommentariesByProject(@Query("username") String projectID);
        @POST("/commentary/")
        Observable<SimpleServerResponse> createCommentary(@Body ServerCommentary serverCommentary);
        @PUT("/commentary/{commentaryID}")
        Observable<SimpleServerResponse> modifyCommentary(@Path("commentaryID") String commentaryID);
        @DELETE("/commentary/{commentaryID}")
        Observable<SimpleServerResponse> deleteCommentary(@Path("commentaryID") String commentaryID);
    }
}