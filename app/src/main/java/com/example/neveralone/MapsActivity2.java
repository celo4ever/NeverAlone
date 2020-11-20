package com.example.neveralone;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.example.neveralone.Activity.Home;
import com.example.neveralone.Activity.LoginActivity;
import com.example.neveralone.Activity.RegisterVolunteerActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class MapsActivity2 extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener
{

    private GoogleMap mMap;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private String codigoPostal;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabase2;
    private GoogleMap googleM;
    private Geocoder x;


    DatabaseReference reference;
    DatabaseReference reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    public void obtenerAdresaUsuario() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("myInfoTag2", "entra");
        Log.i("myInfoTag2", userID);

        reference = FirebaseDatabase.getInstance().getReference("Usuarios/" + userID + "/codigopostal");
        Log.i("myInfoTag2", String.valueOf(reference));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("myInfoTag2", String.valueOf(codigoPostal));

                String value = dataSnapshot.getValue(String.class);
                codigoPostal = value;
                Log.i("myInfoTag2.2", String.valueOf(codigoPostal));
                mMap = googleM;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
/*
                List<Address> foundGeocode = null;
                try {
                    foundGeocode = x.getFromLocationName(codigoPostal+" España", 1);
                    Log.i("myInfoTag10eeeee", String.valueOf(foundGeocode));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                LatLng sydney = new LatLng( foundGeocode.get(0).getLatitude(), foundGeocode.get(0).getLongitude());
*/
                LatLng sydney = new LatLng( 2, 40);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));

            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Log.i("myInfoTag1", String.valueOf(codigoPostal));
        googleM = googleMap;
        x= new Geocoder(this);
        obtenerAdresaUsuario();

         Log.i("myInfoTag5", String.valueOf(codigoPostal));
        //FALTA COMPROBARLO Y COMENTAR ALGUNA COSA
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Geocoder geocoder = new Geocoder(this);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Iterable<DataSnapshot> Usuarios_con_peticiones = snapshot.child("User-Peticiones").getChildren();
                    for (DataSnapshot Usuario_con_peticiones : Usuarios_con_peticiones) {
                        Log.i("myInfoTag10 marcador1", "entra");
                        //AÑADIDO DE AQUI...
                        Vector<String> tipos_de_peticiones_del_usr = new Vector<String>();
                        Iterable<DataSnapshot> peticiones = Usuario_con_peticiones.getChildren();
                        boolean Asesoriamiento = false, Compra = false, Acompañamiento = false, Otro = false;
                        for(DataSnapshot peticion : peticiones){
                            //CUIDADO CON COMO SE GUARDAN EN FIREBASE, A VECES CON "" A VECES NO...
                            String tipo_de_peticion = peticion.child("categoria").getValue().toString();

                            if(tipo_de_peticion.equals("Compras")  && !Compra){
                                Compra = true;
                                tipos_de_peticiones_del_usr.add("Compras");
                            }
                            else if(tipo_de_peticion.equals("Asesoramiento") && !Asesoriamiento){
                                Asesoriamiento = true;
                                tipos_de_peticiones_del_usr.add("Asesoramiento");

                            }
                            else if(tipo_de_peticion.equals("Acompañamiento") && !Acompañamiento) {
                                Acompañamiento = true;
                                tipos_de_peticiones_del_usr.add("Acompañamiento");

                            }
                            else if(tipo_de_peticion.equals("Otro") && !Otro) {
                                Otro = true;
                                tipos_de_peticiones_del_usr.add("Otro");

                            }
                        }
                        //...A AQUI
                        Log.i("myInfoTag10 vector", tipos_de_peticiones_del_usr.toString());

                        String codigo_usuario_con_pet = Usuario_con_peticiones.getKey();
                        String CP = null;
                        Iterable<DataSnapshot> Usuarios = snapshot.child("Usuarios").getChildren();
                        for (DataSnapshot User : Usuarios) {
                            Log.i("myInfoTag10 marcador2", "entra");

                            String código_usuario = User.getKey();
                            Log.i("myInfoTag10 usuario:", código_usuario);
                            Log.i("myInfoTag10pet", codigo_usuario_con_pet);
                            if (código_usuario.equals(codigo_usuario_con_pet)) {
                                Log.i("myInfoTag10 marcador2", "entra if");

                                CP = User.child("codigopostal").getValue().toString();
                                Log.i("myInfoTag10", CP);/*
                                List<Address> foundGeocode = null;
                                try {
                                    foundGeocode = geocoder.getFromLocationName(CP + " España", 1);

                                    Log.i("myInfoTag10este", String.valueOf(foundGeocode));

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                LatLng ubi_peticion = new LatLng(foundGeocode.get(0).getLatitude(), foundGeocode.get(0).getLongitude());
*/
                                LatLng ubi_peticion = new LatLng(2,40);

                                Marker MarkerEjemplo1 = mMap.addMarker(new MarkerOptions()
                                        .position(ubi_peticion)
                                        .title("hola")
                                        .title(User.child("nombre").getValue().toString() + " " + User.child("apellidos").getValue().toString())
                                        .snippet(String.valueOf(tipos_de_peticiones_del_usr))); //de momento, habria que mirar como calcular la distancia
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled( DatabaseError error) {

            }
        });
        googleM.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        startActivity(new Intent(MapsActivity2.this, Home.class)); //sustituir home por el perfil del Usuario. Estará guardado en el this.title, seguramente(marcador)
        finish();
    }
}