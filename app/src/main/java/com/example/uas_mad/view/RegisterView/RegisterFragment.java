package com.example.uas_mad.view.RegisterView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uas_mad.R;
import com.example.uas_mad.helper.SharedPreferenceHelper;
import com.example.uas_mad.model.GameData;
import com.example.uas_mad.model.Profile;
import com.example.uas_mad.view.GameView.GameViewModel;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private TextView btn_login_register;
    private TextInputLayout input_confirm_password, input_password_register, input_name_register, input_email_register, input_city_register, input_school_register, input_birthyear_register, input_username_register;
    private Button btn_register;

    private RegisterViewModel registerViewModel;
    private SharedPreferenceHelper helper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment register.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Deklarasi
        initview();

        //Pencet textview pindah ke halaman login
        btn_login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });

        registerViewModel = new ViewModelProvider(getActivity()).get(RegisterViewModel.class);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!input_name_register.getEditText().getText().toString().isEmpty()
                        && !input_email_register.getEditText().getText().toString().isEmpty()
                        && !input_password_register.getEditText().getText().toString().isEmpty()
                        && !input_confirm_password.getEditText().getText().toString().isEmpty()
                        && !input_city_register.getEditText().getText().toString().isEmpty()
                        && !input_birthyear_register.getEditText().getText().toString().isEmpty()
                        && !input_school_register.getEditText().getText().toString().isEmpty()
                        && !input_username_register.getEditText().getText().toString().isEmpty()){

                    String name = input_name_register.getEditText().getText().toString().trim();
                    String email = input_email_register.getEditText().getText().toString().trim();
                    String pass = input_password_register.getEditText().getText().toString().trim();
                    String cpass = input_confirm_password.getEditText().getText().toString().trim();
                    String username = input_username_register.getEditText().getText().toString().trim();
                    String city = input_city_register.getEditText().getText().toString().trim();
                    String school = input_school_register.getEditText().getText().toString().trim();
                    int birthyear = Integer.parseInt(input_birthyear_register.getEditText().getText().toString().trim());

                    registerViewModel.register(name, email, pass, cpass, school, city, birthyear, username).observe(requireActivity(), registerResponse -> {
                        if (registerResponse != null){

                            //Login
                            helper = SharedPreferenceHelper.getInstance(requireActivity());
                            registerViewModel.login(email, pass).observe(requireActivity(), tokenResponse -> {
                                if (tokenResponse != null){

                                    //Get Student ID
                                    helper.saveAccessToken(tokenResponse.getAuthorization());
                                    helper = SharedPreferenceHelper.getInstance(requireActivity());
                                    registerViewModel = new ViewModelProvider(getActivity()).get(RegisterViewModel.class);
                                    registerViewModel.init(helper.getAccessToken());
                                    registerViewModel.getStudentData();
                                    registerViewModel.getResultStudentData().observe(getActivity(), showprofile);
                                }
                            });

                            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
                            Toast.makeText(requireActivity(), "Register Success", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(requireActivity(), "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(requireActivity(), "All field must not empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Fungsi Deklarasi
    private void initview() {
        input_confirm_password = getActivity().findViewById(R.id.input_confirm_password);
        input_password_register = getActivity().findViewById(R.id.input_password_register);
        input_name_register = getActivity().findViewById(R.id.input_name_register);
        input_email_register = getActivity().findViewById(R.id.input_email_register);
        input_birthyear_register = getActivity().findViewById(R.id.input_birthyear_register);
        input_city_register = getActivity().findViewById(R.id.input_city_register);
        input_school_register = getActivity().findViewById(R.id.input_school_register);
        input_username_register = getActivity().findViewById(R.id.input_username_register);

        btn_register = getActivity().findViewById(R.id.btn_register);
        btn_login_register = getActivity().findViewById(R.id.btn_login_register);
    }

    private Observer<Profile> showprofile = new Observer<Profile>() {
        @Override
        public void onChanged(Profile profile) {
                //Add New Game data
                GameData.Gamedata gamedata = addGamedata(profile.getId(), profile.getId(), 0 , 3, 0, 600000, 20);
                registerViewModel.createGamedata(gamedata).observe(requireActivity(), gamedata1 -> {

                //Logout
                if (gamedata1 != null){
                    registerViewModel.logout().observe(requireActivity(), s ->{
                        if (!s.isEmpty()){
                            helper.clearPref();
                        }
                    });
                }else{
                }
            });
        }
    };

    private GameData.Gamedata addGamedata(int student_gamedata_id, int student_id_gamedata, int total_damage, int health_left, int money, int time_left, int current_damage) {
        GameData.Gamedata gamedata = new GameData.Gamedata(student_gamedata_id, student_id_gamedata, total_damage, health_left, money, time_left, current_damage);
        return gamedata;
    }
}