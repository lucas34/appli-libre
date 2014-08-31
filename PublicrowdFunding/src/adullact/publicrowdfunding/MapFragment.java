package adullact.publicrowdfunding;

import java.util.ArrayList;
import java.util.HashMap;

import adullact.publicrowdfunding.model.local.callback.HoldAllToDo;
import adullact.publicrowdfunding.model.local.ressource.Account;
import adullact.publicrowdfunding.model.local.ressource.Project;
import adullact.publicrowdfunding.model.local.utilities.SyncServerToLocal;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements
		OnInfoWindowClickListener {

	private SupportMapFragment fragment;
	private FragmentManager fm;
	private ProgressDialog mprogressDialog;
	private View rootView;
	private GoogleMap googleMap;
	private final HashMap<Marker, Project> markers = new HashMap<Marker, Project>();

	private View infoWindow;
	
	private ArrayList<Project> projets;

	private MapFragment _this;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.activity_maps, container, false);

		
		infoWindow = getLayoutInflater(null).inflate(
				R.layout.fiche_project, null);
		projets = new ArrayList<Project>(); 

		_this = this;

		mprogressDialog = new ProgressDialog(getActivity());
		mprogressDialog.setMessage("Chargement en cours...");
		mprogressDialog.setTitle("Google Map");
		mprogressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mprogressDialog.show();

		fragment = new SupportMapFragment();
		fm = getFragmentManager();

		SyncServerToLocal sync = SyncServerToLocal.getInstance();
		 Account.getOwnOrAnonymous().setLastSync(null);
		sync.sync(new HoldAllToDo<Project>() {

			@Override
			public void holdAll(ArrayList<Project> projects) {
				projets = projects;
				initMaps();
			}
		});

		return rootView;
	}

	@Override
	public void onInfoWindowClick(Marker marker) {

		
		String id = markers.get(marker).getResourceId();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		// ft.setCustomAnimations(R.anim.enter_2, R.anim.exit); Fragment
		Fragment fragment = new adullact.publicrowdfunding.fragment.v4.detailProject.ProjectPagerFragment();
		Bundle bundle = new Bundle();
		bundle.putString("idProject", id);
		fragment.setArguments(bundle);
		fragment.setHasOptionsMenu(true);
		ft.replace(R.id.content_frame, fragment);
		ft.commit();

	}
	
	public void initMaps(){

		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.mapView, fragment, "mapid").commit();

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {

				try{
				googleMap = ((SupportMapFragment) fm.findFragmentByTag("mapid"))
						.getMap();

				if (googleMap != null) {
					for (Project proj : projets) {
						MarkerOptions marker = new MarkerOptions();
						marker.position(proj.getPosition());
						marker.title(proj.getName());
						Marker m = googleMap.addMarker(marker);
						
						markers.put(m, proj);
					}

					googleMap.setOnInfoWindowClickListener(_this);
					googleMap
							.setInfoWindowAdapter(new adullact.publicrowdfunding.custom.MarkerWindowAdaptor(
									 getLayoutInflater(null), markers));

					handler.removeCallbacksAndMessages(null);

					// googleMap.setOnMarkerClickListener(TabMapFragment.this);
					googleMap.setMyLocationEnabled(true);
					LatLng Montpellier = new LatLng(43.652400, 3.761380);
					CameraUpdate center = CameraUpdateFactory
							.newLatLng(Montpellier);
					CameraUpdate zoom = CameraUpdateFactory.zoomTo(9);
					googleMap.moveCamera(center);
					googleMap.animateCamera(zoom);
					mprogressDialog.dismiss();
				}

				else {
					handler.postDelayed(this, 2000);
				}
				}catch(Exception e){
					System.out.println("Impossible de lancer Google Map");
				}
			}
		}, 2000);
	}
	

}