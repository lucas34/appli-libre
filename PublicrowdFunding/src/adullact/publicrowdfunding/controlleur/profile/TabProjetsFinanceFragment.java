package adullact.publicrowdfunding.controlleur.profile;

import adullact.publicrowdfunding.R;
import adullact.publicrowdfunding.custom.CustomAdapter;
import adullact.publicrowdfunding.model.local.callback.HoldAllToDo;
import adullact.publicrowdfunding.model.local.callback.WhatToDo;
import adullact.publicrowdfunding.model.local.ressource.Account;
import adullact.publicrowdfunding.model.local.ressource.Project;
import adullact.publicrowdfunding.model.local.ressource.User;
import adullact.publicrowdfunding.model.local.utilities.SyncServerToLocal;
import adullact.publicrowdfunding.controlleur.detailProjet.MainActivity;
import adullact.publicrowdfunding.exception.NoAccountExistsInLocal;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

public class TabProjetsFinanceFragment extends Fragment {

	private ListView listeProjets;
	private Vector<Project> funded;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.tab, container, false);

		listeProjets = (ListView) view.findViewById(R.id.liste);
		funded = new Vector<Project>();

		Account compte = null;
		try {
			compte = Account.getOwn();
		} catch (NoAccountExistsInLocal e) {
			Toast.makeText(getActivity().getApplicationContext(), "Erreur",
					Toast.LENGTH_SHORT).show();
		}

		compte.getUser(new WhatToDo<User>() {

			@Override
			public void hold(User resource) {
				resource.getFinancedProjects(new HoldAllToDo<Project>() {

					@Override
					public void holdAll(ArrayList<Project> resources) {
						funded = new Vector<Project>(resources);

					}
				});

			}

			@Override
			public void eventually() {
				// TODO Auto-generated method stub

			}
		});

		ArrayAdapter<Project> adapter = new CustomAdapter(this.getActivity()
				.getBaseContext(), R.layout.projet_list, funded);

		listeProjets.setAdapter(adapter);
		listeProjets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Project projet = (Project) listeProjets
						.getItemAtPosition(position);
				Intent in = new Intent(parent.getContext()
						.getApplicationContext(), MainActivity.class);
				in.putExtra("key", projet.getResourceId());
				startActivity(in);
			}
		});

		return view;
	}
}